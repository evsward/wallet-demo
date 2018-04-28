package com.ecochain.task;


import com.alibaba.fastjson.JSONObject;
import com.ecochain.util.AES;
import com.ecochain.util.OrderGenerater;
import com.ecochain.wallet.entity.IcoRechargeRecord;
import com.ecochain.wallet.mapper.EcoWalletMapper;
import com.ecochain.wallet.mapper.IcoRechargeRecordMapper;
import com.tfcc.client.api.util.Utils;
import com.tfcc.core.types.known.tx.signed.SignedTransaction;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;


/**
 * create by lisandro 2017年8月19日15:04:08
 * describe SAN 定时归集
 */
@Service
public class AutoSanGatherThread implements Runnable{

    @Autowired
    private EcoWalletMapper ecoWalleTMapper;

    @Autowired
    private IcoRechargeRecordMapper icoRechargeRecordMapper;

    private static final Logger logger = LogManager.getLogger(AutoSanGatherThread.class);
    private static String GATHER_HOTWALLET_ADDRESS = "srTX2Vvdb2WmBVhRcLnE8BVcyB7pMd393n";

    private static Set<String> addressList = new HashSet<>();

    //@Scheduled(cron = "0 0/1 * * * ? ")
    @Override
    public void run() {
        try {
            logger.info("<--------------------start SAN gather--------------------->");
            //从ico_recharge_record表 扫描所有订单状态为3、未归集的订单，然后发送交易
            List<IcoRechargeRecord> IcoRechargeRecordlist = icoRechargeRecordMapper.getWithRechargeOrder();
            Collection<IcoRechargeRecord> orderList = IcoRechargeRecordlist;
            if (!CollectionUtils.isEmpty(orderList)) {
                for (IcoRechargeRecord icoRechargeRecord : orderList) {
                    logger.info("gather:process id=" + icoRechargeRecord.getId());
                    Double balance = getUserBalance(icoRechargeRecord.getAddress());
                    if (balance <= 0.002 && icoRechargeRecord.getMoney().doubleValue() <=0) {
                        logger.info("gather:balance is zero :id=" + icoRechargeRecord.getId() + ",balance=" + balance);
                        continue;
                    }
                    Map userAddressInfo = ecoWalleTMapper.getUserInfo(icoRechargeRecord.getUserId());
                    if (userAddressInfo.containsKey("walletAddress") && userAddressInfo.containsKey("privateKey")) {
                        /*Double fee = icoRechargeRecord.getMoney().divide(new BigDecimal(1000), 6, BigDecimal.ROUND_HALF_UP).setScale(6).doubleValue();
                        fee = Math.max(0.001, fee);
                        BigDecimal realAmount = new BigDecimal(icoRechargeRecord.getMoney().doubleValue() - fee).setScale(6, BigDecimal.ROUND_HALF_UP);*/
                        int lastLedgerSequence = com.tfcc.client.api.util.Utils.ledgerClosed() + 10;
                        BigDecimal amountBD = icoRechargeRecord.getMoney().multiply(new BigDecimal(1000000));
                        com.tfcc.core.Amount amount = com.tfcc.core.Amount.fromDropString(amountBD.longValue() + "");
                        int seq = com.tfcc.client.api.util.Utils.getSequence(icoRechargeRecord.getAddress());
                        if (StringUtils.isNotEmpty(AES.decrypt(userAddressInfo.get("privateKey").toString()))) {
                            SignedTransaction sign = Utils.payment(AES.decrypt(userAddressInfo.get("privateKey").toString()), GATHER_HOTWALLET_ADDRESS, amount, seq, true, null, null, icoRechargeRecord.getId() + "", lastLedgerSequence);
                            //com.coral.client.api.util.Log.LogFormat.log(InitConst.LOG_OPMODULE_ADMIN_CONSOLE, "gather_deposit", "gather_t", "", icoRechargeRecord.getId() + "," + realAmount.toPlainString(), sign.hash.toHex());
                            String res = Utils.sendTx(sign.tx_blob);
                            logger.info("gahter order:" + icoRechargeRecord.getId() + " gather completed, broadcast tx to network.res=" + res);
                            icoRechargeRecord.setIsConcentrate("1");
                            icoRechargeRecord.setConcentrateType("5");
                            icoRechargeRecord.setConcentrateMsg(res);
                            icoRechargeRecord.setOrderNo(OrderGenerater.generateOrderNo());
                            this.icoRechargeRecordMapper.updateByPrimaryKey(icoRechargeRecord);
                        } else {
                            logger.info("gather:error:id=" + icoRechargeRecord.getId());
                        }
                    }
                }
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
    }

    private static Double getUserBalance(String address){
        try {
            String accountInfo = com.tfcc.client.api.util.Utils.getAccountInfo(address);
            JSONObject jsonObject;

            if (StringUtils.isEmpty(accountInfo)) {
                return 0d;
            }

            jsonObject = JSONObject.parseObject(accountInfo);
            if (!jsonObject.containsKey("result") || !jsonObject.getJSONObject("result").containsKey("account_data")) {
                return 0d;
            }
            BigDecimal san = new BigDecimal(jsonObject.getJSONObject("result").getJSONObject("account_data").getString("Balance"));
            return san.divide(new BigDecimal(1000000), 6, BigDecimal.ROUND_DOWN).doubleValue();
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return 0d;
    }

    public static void main(String[] args) {
        System.out.println(getUserBalance("sEmB6iUA6ZAwLDs7BFEVTyMFTGfJoFZGta"));
    }
}
