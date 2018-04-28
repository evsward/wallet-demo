package com.ecochain.task.Ethereum;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import lombok.Data;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.EthTransaction;

import com.ecochain.client.Web3JClient;
import com.ecochain.wallet.entity.WithdrawRecord;
import com.ecochain.wallet.service.EthWalletService;
import com.ecochain.wallet.service.WithdrawRecordService;
import com.ecochain.wallet.util.WalletConstant;
import com.github.pagehelper.StringUtil;

/**
 * 
* @ClassName: ConfirmWithdrawTxThread 
* @Description: TODO(提币确认交易线程) 
* @author 焦博韬  
* @date 2017年10月12日 上午10:51:05 
*
 */
@Data
@Component("confirmWithdrawTxThread")
public class ConfirmWithdrawTxThread implements Runnable {

    private Logger logger = Logger.getLogger(ConfirmWithdrawTxThread.class);

    @Autowired
    private WithdrawRecordService withdrawRecordService;

    @Autowired
    private EthWalletService ethWalletService;

    private Integer confirmNum =12;

    @Scheduled(cron = "0 1/3 * * * ? ")
    @Override
    public void run() {
        logger.info("提币确认工作线程！ 开始！");
        BigInteger maxBlock = this.getMaxBlockNumber();
        List<WithdrawRecord> records = null;
        try{
            records = withdrawRecordService.listETHSend();
        }catch (Exception e ){
            e.printStackTrace();
        }

        logger.info("wait confirm withdrawRecord listSize:"+records.size());
        for (WithdrawRecord withdrawRecord : records) {
            confirmWithdrawTransaction(withdrawRecord, maxBlock);
        }

        logger.info("提币确认工作线程！结束！");
    }

    public synchronized void confirmWithdrawTransaction(WithdrawRecord withdrawRecord,BigInteger maxBlock){
        BigInteger txBlockNum = BigInteger.ZERO;
        String txHash = withdrawRecord.getTxHash();
        if(StringUtil.isEmpty(txHash)) {
            logger.debug("can't find txHash ,return!");
            return;
        }
        Web3j web3j = Web3JClient.getClient();
        try {
            EthTransaction ethTransaction = web3j.ethGetTransactionByHash(txHash).sendAsync().get();
            txBlockNum = ethTransaction.getTransaction().get().getBlockNumber();
            try {
                BigInteger free = ethTransaction.getTransaction().get().getGas().multiply(
                        ethTransaction.getTransaction().get().getGasPrice());
                withdrawRecord.setFree(new BigDecimal(free.longValue()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        int height = maxBlock.subtract(txBlockNum).intValue();
        logger.info("txBlockNum:"+txBlockNum+"\tmaxBlock:"+maxBlock+"\theight:"+height);
        if(height>=confirmNum) {
            try{
                withdrawRecord.setStatus(WalletConstant.FINISH);
                withdrawRecord.setUpdateTime(new Date());
                withdrawRecordService.updateWithdrawRecordByTxHash(withdrawRecord);
                logger.debug("withdrawRecord updated!");
            }catch(Exception e){
                e.printStackTrace();
            }

        }
    }

    public BigInteger getMaxBlockNumber(){
        Web3j web3j = Web3JClient.getClient();
        EthBlockNumber maxblockNumber = null;
        try {
            maxblockNumber = web3j.ethBlockNumber().sendAsync().get();
            logger.debug("The max Block Number is :"+maxblockNumber.getBlockNumber());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if(maxblockNumber==null){
            logger.error("The maxBlockNumber is null, we retry later!");
            return BigInteger.ZERO;
        }

        return  maxblockNumber.getBlockNumber();
    }
}
