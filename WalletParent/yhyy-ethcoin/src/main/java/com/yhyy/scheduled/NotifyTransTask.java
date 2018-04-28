package com.yhyy.scheduled;

import com.yhyy.client.Web3JClient;
import com.yhyy.service.AccountService;
import com.yhyy.util.CheckPointUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthTransaction;
import org.web3j.protocol.core.methods.response.Transaction;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

@Component
public class NotifyTransTask {
    Logger logger = LoggerFactory.getLogger(getClass());

    public final static long ONE_Second = 1000;
    public final static String Filename = "check-point.txt";

    @Autowired
    private AccountService accountService;

    /**
     * 扫描区块交易信息，监听账户到账通知（到账确认）
     */
    @Scheduled(fixedRate = ONE_Second * 10)
    public void notifyTransScheduled() {
        long checkBlockPoint = Long.parseLong(CheckPointUtil.read(Filename));
        Admin web3j = Web3JClient.getAdminClient();
        try {
            long currentAdvancedNum = web3j.ethBlockNumber().send().getBlockNumber().longValue();
            for (long i = checkBlockPoint; i <= currentAdvancedNum; i++) {
                if (i == currentAdvancedNum) {
                    CheckPointUtil.write(Filename, currentAdvancedNum + "");
                    logger.info("区块扫描结束，最后扫描块为：" + currentAdvancedNum);
                }
                EthBlock ethBlock = web3j.ethGetBlockByNumber(DefaultBlockParameter.valueOf(BigInteger.valueOf(i)), false).send();
                EthBlock.Block block = ethBlock.getResult();
                List<EthBlock.TransactionResult> tranList = ethBlock.getBlock().getTransactions();
                if (tranList.size() == 0)
                    continue;
                logger.info("当前块中交易数量：" + tranList.size());
                for (EthBlock.TransactionResult tran : tranList) {
                    String transactionHash = (String) tran.get();
                    EthTransaction ethTransaction = web3j.ethGetTransactionByHash(transactionHash).send();
                    Transaction transaction = ethTransaction.getTransaction().get();
                    String toAddress = transaction.getTo();
                    if (accountService.getCurrentAccountsList().contains(toAddress)) {
                        CheckPointUtil.write(Filename, i + "");
                        // TODO: 回调函数到账通知
                        logger.info("监测到账户：" + toAddress + "发生交易。");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
