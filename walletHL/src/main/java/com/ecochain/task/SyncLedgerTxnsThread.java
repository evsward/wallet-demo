package com.ecochain.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.ecochain.wallet.entity.EcoWallet;
import com.ecochain.wallet.service.EcoWalletService;
import com.tfcc.client.api.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

@Service
public class SyncLedgerTxnsThread {

    private static final Logger log = LoggerFactory.getLogger(SyncLedgerTxnsThread.class);

    private static final String LISTEN_REDIS_KEY = "REDIS_IMFORM_TFCC_INFO_IDS";

    @Autowired
    TxnExecutor txnExecutor;

    @Autowired
    EcoWalletService ecoWalletService;

    @Autowired
    RedisTemplate<String,Long> redisTemplate;


    //@Scheduled(cron = "0 0/1 * * * ? ")
    public void syncTask(){
        try {
            log.info("start sync task.");
            try {
                log.info("SyncLedgerTxnsThread2");
                String ledgerStartKey = "tfcc.ledger.start.current";
//                String value = "1446390";//jedis.get(ledgerStartKey);
                Long value = redisTemplate.opsForValue().get(ledgerStartKey);//"1446390";//jedis.get(ledgerStartKey);
                if(value == null){
                    value = 143000L;
                }

                Integer ledgerIndex = value.intValue();

                int ledgerCurrentIndex = Utils.ledgerClosed();
                String completeLedgers = Utils.completeLedgers();
                log.info("complete ledgers:" + completeLedgers);
                if( ledgerCurrentIndex > ledgerIndex ) {
                    int startIndex = ledgerIndex;
                    startIndex = Utils.nextLedgerIndex(completeLedgers, startIndex);
                    while (startIndex <= ledgerCurrentIndex) {
                        if(startIndex<0){
                            log.error("wrong start index number.");
                            return;
                        }
                        log.info("get ledger: " + startIndex);
                        String ledger = Utils.ledger(startIndex);
                        if(ledger != null){
                            JSONObject ledgerJson = JSONObject.parseObject(ledger);
                            if(ledgerJson.containsKey("status") && ledgerJson.getString("status").equals("success")) {
                                JSONObject ledgerResultData = ledgerJson.getJSONObject("result").getJSONObject("ledger");
                                JSONArray txArray = ledgerResultData.getJSONArray("transactions");
                                if (txArray != null && txArray.size() > 0) {
                                    Long closeTime = ledgerResultData.getLong("close_time");
                                    int txLength = txArray.size();
                                    for (int i = 0; i < txLength; i++) {
                                        JSONObject tx = txArray.getJSONObject(i);
                                        log.info("origin txn data="+tx.toString());
                                        JSONObject txData = parseTxData(tx);
                                        if (txData != null) {
                                            log.info("scan txn:"+txData.toString());

                                            String account = txData.getString("Account");
                                            String destination = txData.getString("Destination");

                                            EcoWallet accountWallet = ecoWalletService.findWalletByAddress(account);
                                            EcoWallet destinationWallet = ecoWalletService.findWalletByAddress(destination);
                                            if(accountWallet==null && destinationWallet==null){
                                                continue;
                                            }
//                                            if(!jedis.sismember(LISTEN_REDIS_KEY,account) && !jedis.sismember(LISTEN_REDIS_KEY,destination)){
//                                                log.info("not in listen account list, txn:"+txData.toString());
//                                                continue;
//                                            }

                                            txData.put("closeTime", closeTime);
                                            String txType = tx.getString("TransactionType");
                                            if (!txType.equalsIgnoreCase("Payment")) {
                                                continue;
                                            }
                                            txData.put("TransactionType", txType);
                                            log.info("scan process txn:"+txData.toString());
                                            txnExecutor.runTask(txData);
                                        }
                                    }
                                } else {
                                    log.info("ledger: " + startIndex + " is done." + " no tx found.");
                                }
                                redisTemplate.opsForValue().set(ledgerStartKey,(long)startIndex);
                                startIndex = Utils.nextLedgerIndex(completeLedgers, startIndex);
                            }else{
                                log.info("ledger:" + startIndex + " not done." + " no ledger found. Retry...");
                            }
                        }else{
                            log.info("ledger: " + startIndex + " not done." + " timeout to get data. Retry...");
                        }
                        Thread.sleep(100);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error(e.getMessage(), e);
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
    }

    private static JSONObject parseTxData(JSONObject resultData) throws URISyntaxException, InterruptedException {
        try {
            String txType = resultData.getString("TransactionType");
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("Account", resultData.get("Account"));
            dataMap.put("Fee", resultData.get("Fee"));
            dataMap.put("hash", resultData.get("hash"));
            String txnResult = resultData.getJSONObject("metaData").getString("TransactionResult");
            dataMap.put("TransactionResult", txnResult);
            log.info("get tx hash:" + dataMap.get("hash") + ", TransactionResult=" + dataMap.get("TransactionResult"));

            if(txnResult == null || !txnResult.equalsIgnoreCase("tesSUCCESS")){   //todo validated
                return null;
            }

            if (txType.equalsIgnoreCase("Payment")) {
                dataMap.put("Amount", resultData.get("Amount"));
                dataMap.put("Destination", resultData.get("Destination"));
                return new JSONObject(dataMap);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
