package com.ecochain.wallet.util;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.bitcoinj.core.AddressFormatException;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.DumpedPrivateKey;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.TransactionInput;
import org.bitcoinj.core.TransactionOutput;
import org.bitcoinj.core.listeners.TransactionConfidenceEventListener;
import org.bitcoinj.script.Script;
import org.bitcoinj.wallet.Wallet;
import org.bitcoinj.wallet.listeners.KeyChainEventListener;
import org.bitcoinj.wallet.listeners.ScriptsChangeEventListener;
import org.bitcoinj.wallet.listeners.WalletCoinsReceivedEventListener;
import org.bitcoinj.wallet.listeners.WalletCoinsSentEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ecochain.EcoWalletApplication;
import com.ecochain.core.exception.RuntimeServiceException;
import com.ecochain.util.AES;
import com.ecochain.util.OrderGenerater;
import com.ecochain.wallet.entity.EcoWallet;
import com.ecochain.wallet.entity.RechargeRecord;
import com.ecochain.wallet.entity.UserWallet;
import com.ecochain.wallet.entity.WithdrawRecord;
import com.ecochain.wallet.mapper.UserCoinMapper;
import com.ecochain.wallet.service.EcoWalletService;
import com.ecochain.wallet.service.RechargeRecordService;
import com.ecochain.wallet.service.UserWalletService;
import com.ecochain.wallet.service.UsersDetailsService;
import com.ecochain.wallet.service.WithdrawRecordService;

@Component
public class WalletAsync implements Runnable{
    
    static Logger log = LoggerFactory.getLogger(WalletAsync.class);
    
    @Value("${wallet.privateKey}")
    String privateKey;
    
    @Autowired
    private EcoWalletService ecoWalletService;
    
    @Autowired
    private WithdrawRecordService withdrawRecordService;
    
    @Autowired
    private RechargeRecordService rechargeRecordService;
    
    @Autowired
    private UserWalletService userWalletService;
    
    @Resource
    private UserCoinMapper userCoinMapper;
    
    public WalletAsync() {
    }
    
    @Override
    public void run() {
        log.info("---------------------------------" + userWalletService);
        log.info("---------------------------------" + rechargeRecordService);
        
        // Now we initialize a new WalletAppKit. The kit handles all the boilerplate for us and is the easiest way to get everything up and running.
        // Have a look at the WalletAppKit documentation and its source to understand what's happening behind the scenes: https://github.com/bitcoinj/bitcoinj/blob/master/core/src/main/java/org/bitcoinj/kits/WalletAppKit.java
        

        // In case you want to connect with your local bitcoind tell the kit to connect to localhost.
        // You must do that in reg test mode.
        //kit.connectToLocalHost();

        // Now we start the kit and sync the blockchain.
        // bitcoinj is working a lot with the Google Guava libraries. The WalletAppKit extends the AbstractIdleService. Have a look at the introduction to Guava services: https://github.com/google/guava/wiki/ServiceExplained
        
        List<ECKey> ecKeys = EcoWalletApplication.kit.wallet().getImportedKeys();
        
        if(ecKeys == null || ecKeys.size() == 0){
            log.info("--------------init key---------");
            
            ECKey key;
            DumpedPrivateKey dumpedPrivateKey = DumpedPrivateKey.fromBase58(EcoWalletApplication.params, 
                    AES.decrypt(privateKey) );
            key = dumpedPrivateKey.getKey();
            EcoWalletApplication.kit.wallet().importKey(key);
        }
        log.info(" keys == " + EcoWalletApplication.kit.wallet().getImportedKeys().size());
        
        EcoWalletApplication.kit.wallet().addCoinsReceivedEventListener(new WalletCoinsReceivedEventListener() {
            @Override
            public void onCoinsReceived(Wallet wallet, Transaction tx, Coin prevBalance, Coin newBalance) {
                log.info("addCoinsReceivedEventListener getValueSentToMe=="+  tx.getValueSentToMe(wallet) +",free="+ tx.getFee());
                log.info("addCoinsReceivedEventListener getValueSentFromMe=="+ tx.getValueSentFromMe(wallet));
                log.info("--------> coins resceived: " + tx.getHashAsString() + ", value = " + tx.getValue(wallet));
                    
                if (tx.getValue(wallet).compareTo(Coin.valueOf(10000)) == -1) {
                    log.info("receive coin balance is less than 10000sato, do not accept.");
                    log.info("tx:" + tx.getHashAsString());
                    return;
                }
                
                for (TransactionOutput txop : tx.getOutputs()){
                    Coin value = txop.getValue();
                    if (value.compareTo(Coin.valueOf(10000)) == -1) {
                        log.info("receive txo is less than 10000sato, do not accept.");
                        log.info("tx:" + tx.getHashAsString() + ", tx_output:" + txop.getIndex());
                        return;
                    }
                    
                    String address = txop.getScriptPubKey().getToAddress(EcoWalletApplication.params).toString();
                    log.info("Received address= " + address);
                    if (txop.isMine(wallet)){
                        String userId = userCoinMapper.getUserIdByAddress(address);
                        
                        if (StringUtils.isEmpty(userId)){
                            throw new RuntimeServiceException("用户信息不存在");
                        }
                        
                        RechargeRecord record = new RechargeRecord();
                        String orderNo =OrderGenerater.generateOrderNo();
                        record.setByUserId(userId);
                        record.setOrderNo(orderNo);
                        Coin sendToMe = tx.getValueSentToMe(wallet);
                        Coin fee = tx.getFee();
                        if (fee == null){
                            fee = Coin.valueOf(0);
                        }
                        record.setUserId(userId);
                        record.setMoney(new BigDecimal(sendToMe.toPlainString()));
                        record.setFree(new BigDecimal (fee.toPlainString()));
                        record.setStatus(WalletConstant.SEND);//1-待确认 2-已确认
                        record.setCurrency("BTC");
                        record.setCreateTime(new Date());
                        record.setTxHash(tx.getHashAsString());
                        record.setAddress(address);
                        //增加充值记录
                        rechargeRecordService.insertRechargeRecord(record);
                        
                        
                        log.info(address + ",addCoinsReceive money===" + txop.getValue().toFriendlyString());
                    }else{
                        log.info("=========not me======");
                    }
                }
            }
                
        });

        EcoWalletApplication.kit.wallet().addCoinsSentEventListener(new WalletCoinsSentEventListener() {
            @Override
            public void onCoinsSent(Wallet wallet, Transaction tx, Coin prevBalance, Coin newBalance) {
                log.info("addCoinsSentEventListener getValueSentToMe=="+  tx.getValueSentToMe(wallet));
                log.info("addCoinsSentEventListener getValueSentFromMe=="+ tx.getValueSentFromMe(wallet) + ",free= " + tx.getFee());
                log.info("--------> coins sent: " + tx.getHashAsString());
                log.info("coins sent");
                for (TransactionInput txip : tx.getInputs()){
                    log.info(" TransactionInput：{} " , txip);
                }
                
            }
        });

        EcoWalletApplication.kit.wallet().addKeyChainEventListener(new KeyChainEventListener() {
            @Override
            public void onKeysAdded(List<ECKey> keys) {
                log.info("new key added");
            }
        });

        EcoWalletApplication.kit.wallet().addScriptsChangeEventListener(new ScriptsChangeEventListener() {
            @Override
            public void onScriptsChanged(Wallet wallet, List<Script> scripts, boolean isAddingScripts) {
                log.info("new script added");
            }
        });

        EcoWalletApplication.kit.wallet().addTransactionConfidenceEventListener(new TransactionConfidenceEventListener() {
            @Override
            public void onTransactionConfidenceChanged(Wallet wallet, Transaction tx) {
                log.info("Depth =: " + tx.getConfidence().getDepthInBlocks());
                log.info("Peers =: " + tx.getConfidence().numBroadcastPeers());
                
                int depth = tx.getConfidence().getDepthInBlocks();
                
                
                //在真实BTC网络里,1600个区块
                log.info("--------depth--{}",depth);
                if (depth == 0 || depth > 1600){
                    //do nothing if confirmed time is zero
                    //100次确认之内的交易都有重新处理的机会
                    log.info("==========depth return");
                    return;
                }
                
                log.info("confidence changed: " + tx.getHashAsString() + ", new block depth: " + depth);
                
//                //交易确认   金额<1是3个，<10是4个，<100是5个。剩余6个
//                int peers = tx.getConfidence().numBroadcastPeers();
//                if (peers < 3){
//                    log.info("==========peers return");
//                    return;
//                }
                
                //TODO 通过hash 取充值提现记录，更新状态
                for (TransactionOutput txop : tx.getOutputs()){
                    
                    if (txop.isMine(wallet)){
                        String address = txop.getScriptPubKey().getToAddress(EcoWalletApplication.params).toString();
                        log.info("record ----address:{} ,tx :{},value:{} " , address, tx.getHashAsString(), tx.getValue(wallet));
                        log.info("record ----fee:{} " , tx.getFee());
                        
//                        Coin receiveCoin = tx.getValue(wallet);
                        
                        //交易确认   金额<1是3个，<10是4个，<100是5个。剩余6个
//                        boolean updateFlag = checkValue(receiveCoin, peers);
//                        log.info("updateFlag: {}",updateFlag);
//                        
//                        if (updateFlag){
                            
                            //hash需要判断是充值记录还是提现记录都要判断，
                            WithdrawRecord record = withdrawRecordService.findWithdrawRecordByTxHash(tx.getHashAsString());
                            //是我们发起的交易
                            if (record != null && WalletConstant.SEND.equals(record.getStatus())){
                                log.info("提现记录  WithdrawRecord " );
                                record.setStatus(WalletConstant.FINISH);
                                record.setUpdateTime(new Date());
                                record.setFree(new BigDecimal (tx.getFee().toPlainString()));
                                withdrawRecordService.updateWithdrawRecordByTxHash(record);
                                
                            } 
                            RechargeRecord rechargeRecord = rechargeRecordService.findRechargeRecordByTxHash(tx.getHashAsString());
                            
                            if (rechargeRecord != null  && WalletConstant.SEND.equals(rechargeRecord.getStatus())) {
                                log.info("TransactionConfidence:"+address + " , " + tx.getHashAsString() + ", "+tx.getFee());
                                log.info("TransactionConfidence===" + txop.getValue().toFriendlyString());
                                log.info("充值记录  WithdrawRecord " );
                                rechargeRecord.setStatus(WalletConstant.FINISH);
                                rechargeRecord.setUpdateTime(new Date());
                                
                                rechargeRecordService.updateRechargeRecordByTxHash(rechargeRecord);
                                
                                String userId = userCoinMapper.getUserIdByAddress(address);
                                
                                userWalletService.addBTCWallet(new BigDecimal(tx.getValueSentToMe(wallet).toPlainString()), userId);
                                
                            }
//                        }
                        
                    }
                }
            }
        });
        
        // Ready to run. The kit syncs the blockchain and our wallet event listener gets notified when something happens.
        // To test everything we create and print a fresh receiving address. Send some coins to that address and see if everything works.
        log.info("send money to: " + EcoWalletApplication.kit.wallet().freshReceiveAddress().toString());
        
        //导入数据库里所有key
        List<EcoWallet> ecoWallets = ecoWalletService.listAllEcoWallets();
        if (ecoWallets != null ){
            
            for (EcoWallet ecoWallet : ecoWallets) {
                
                try {
                    ECKey key;
                    DumpedPrivateKey dumpedPrivateKey = DumpedPrivateKey.fromBase58(EcoWalletApplication.params, 
                            AES.decrypt(ecoWallet.getPrivateKey()) );
                    key = dumpedPrivateKey.getKey();
                    boolean containsFlag = EcoWalletApplication.kit.wallet().getImportedKeys().contains(key);
                    boolean addressFlag = key.toAddress(EcoWalletApplication.params).toBase58().equals(ecoWallet.getWalletAddress());
                    
                    log.info("contains key :{}, address : {}, data key {}", key , 
                            key.toAddress(EcoWalletApplication.params).toBase58() , ecoWallet.getWalletAddress() );
                    log.info("addressFlag , {},containsFlag :{}", addressFlag, containsFlag);
                    if (!containsFlag && addressFlag){
                        EcoWalletApplication.kit.wallet().importKey(key);
                    }
                } catch (AddressFormatException e) {
                    log.info(" 数据格式异常 ",e);
                }
                
            }
        }
        
        for (ECKey ecKey : ecKeys) {
            log.info("address= " + ecKey.toAddress(EcoWalletApplication.params).toBase58());
        }
    
        log.info(EcoWalletApplication.kit.wallet().getBalance().toFriendlyString());
    }
    
    /**
     * 交易确认   金额<1是3个，<10是4个，<100是5个。剩余6个
     * @param receiveCoin
     * @param peers
     * @return
     */
    public boolean checkValue(Coin receiveCoin, int peers){
        
        log.debug("receiveCoin : {}, peers : {}", receiveCoin.longValue() , peers);
        
        if (peers < 3){
            return false;
        }
        //100000000L 为1个BTC
        if (100000000L >= receiveCoin.longValue() && peers >= 3){
            return true;
        }
        //10个
        if (1000000000L >= receiveCoin.longValue() && peers >= 4){
            return true;
        }
        //100个
        if (10000000000L >= receiveCoin.longValue() && peers >= 5){
            return true;
        }
        //大于100个
        if (10000000000L < receiveCoin.longValue() && peers >= 6){
            return true;
        }
        
        return false;
    }
}
