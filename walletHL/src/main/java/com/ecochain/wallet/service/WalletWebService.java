package com.ecochain.wallet.service;

import com.ecochain.wallet.entity.EcoWallet;
import com.ecochain.wallet.mapper.EcoWalletMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Lisandro on 2017/8/17.
 */
@Service("walletWebService")
public class  WalletWebService{

    @Resource
    private EcoWalletMapper ecoWalletMapper;

    public int saveSanInfo(EcoWallet ecoWallet) {
        return this.ecoWalletMapper.insert(ecoWallet);
    }

    public int findSanAddress(String userName) {
        return this.ecoWalletMapper.findSanAddress(userName);
    }
}
