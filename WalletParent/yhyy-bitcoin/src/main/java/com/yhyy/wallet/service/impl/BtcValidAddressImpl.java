package com.yhyy.wallet.service.impl;

import com.yhyy.EcoWalletApplication;
import com.yhyy.wallet.service.VerificationAddress;

import org.bitcoinj.core.Address;
import org.springframework.stereotype.Service;

@Service
public class BtcValidAddressImpl implements VerificationAddress{

    @Override
    public void isValidAddress(String address) {
        
        Address.fromBase58(EcoWalletApplication.params, address);
        
    }

}
