package com.yhyy.service;

import com.yhyy.client.Web3JClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthSendTransaction;

import java.io.IOException;
import java.math.BigInteger;

public class TransactionService {
    @Autowired
    private AccountService accountService;

    // 转账交易
    public EthSendTransaction sendTransaction(String from, String fromPassword, String to, long amount) {
        Admin web3j = Web3JClient.getAdminClient();
        EthSendTransaction ethSendTransaction = null;
        if (accountService.unlockAccount(from, fromPassword)) {
            BigInteger amountWei = BigInteger.valueOf((long) (amount * Math.pow(10, 18)));
            Transaction transaction = new Transaction(from, null, null, null, to, amountWei, null);
            try {
                ethSendTransaction = web3j.personalSendTransaction(transaction, fromPassword).send();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ethSendTransaction;
    }
}
