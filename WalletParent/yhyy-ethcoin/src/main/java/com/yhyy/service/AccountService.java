package com.yhyy.service;

import com.yhyy.client.Web3JClient;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.NewAccountIdentifier;
import org.web3j.protocol.admin.methods.response.PersonalListAccounts;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;
import org.web3j.protocol.core.Response;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class AccountService {
    private Admin web3j = Web3JClient.getAdminClient();

    // 创建账户
    public NewAccountIdentifier createAccount(String password) {
        NewAccountIdentifier newAccount = new NewAccountIdentifier();
        if ("".equals(password)) {
            newAccount.setResult("fail");
            newAccount.setError(new Response.Error(1, "password can't be null"));
            return newAccount;
        }
        try {
            // geth要支持rpcapi包括 personal web3
            newAccount = web3j.personalNewAccount(password).send();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newAccount;
    }

    // 解锁账户
    public boolean unlockAccount(String address, String pwd) {
        boolean result = false;
        PersonalUnlockAccount personalUnlockAccount = null;
        try {
            personalUnlockAccount = web3j.personalUnlockAccount(address, pwd).sendAsync().get();
            result = personalUnlockAccount.accountUnlocked();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return result;
    }

    // 获取本地节点的账户列表
    public List<String> getCurrentAccountsList() {
        List<String> accountsAddressList = null;
        PersonalListAccounts personalListAccounts = null;
        try {
            personalListAccounts = web3j.personalListAccounts().send();
            accountsAddressList = personalListAccounts.getAccountIds();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return accountsAddressList;
    }
}
