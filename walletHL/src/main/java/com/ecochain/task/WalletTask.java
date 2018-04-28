package com.ecochain.task;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.AddressFormatException;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.InsufficientMoneyException;
import org.bitcoinj.wallet.Wallet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.ecochain.EcoWalletApplication;
import com.ecochain.constant.Constant;
import com.ecochain.util.RestUtil;
import com.ecochain.wallet.entity.UserLogin;
import com.ecochain.wallet.entity.WithdrawRecord;
import com.ecochain.wallet.service.EcoWalletService;
import com.ecochain.wallet.service.SysGencodeService;
import com.ecochain.wallet.service.UserLoginService;
import com.ecochain.wallet.service.WithdrawRecordService;

@Service
public class WalletTask {

    Logger log = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private EcoWalletService ecoWalletService;
    
    @Autowired
    private WithdrawRecordService withdrawRecordService;
    @Autowired
    private SysGencodeService sysGencodeService;
    @Autowired
    private UserLoginService userLoginService;
    
    @Scheduled(cron = "0 0/1 * * * ? ")
    public void payResultQuery() {
        log.info("审核结果查询定时器启动-------------------" + new Date());
        List<WithdrawRecord> records = withdrawRecordService.listAllApprove();
        
        for (WithdrawRecord withdrawRecord : records) {
            log.info(" WithdrawRecord: {} ", withdrawRecord);
            if(withdrawRecord.getCurrency().equals(Constant.CURRENCY_BTC)){
            	ecoWalletService.sendMoney(EcoWalletApplication.params, withdrawRecord);
            }
            
        }
        
        log.info("支付结果查询定时器启动----------------end---");
    }
    
    //TODO 启动归集
//    @Scheduled(cron = "0 0/5 * * * ?")
    public void sendBtcToColdWallet(){
        
        try {
            Wallet wallet = ecoWalletService.dumpWallet("eco", "");
            
            // 热钱包钱超过500个BTC,将钱转到冷钱包
            //TODO 提供冷钱包地址
            String address = "";
            if ( wallet.getBalance().compareTo(Coin.parseCoin("500")) == 1 ){
                Address to = Address.fromBase58(EcoWalletApplication.params, address);

                Coin coin = wallet.getBalance().subtract(Coin.parseCoin("500"));
                Wallet.SendResult result = wallet.sendCoins(EcoWalletApplication.kit.peerGroup(), to, coin);
                log.info("coins sent. transaction hash: " + result.tx.getHashAsString());
            }else{
                log.info("不足500个BTC");
            }
        } catch (AddressFormatException e) {
            e.printStackTrace();
        } catch (InsufficientMoneyException e) {
            e.printStackTrace();
        }
        
    }
    
}
