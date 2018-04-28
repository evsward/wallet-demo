package com.ecochain.wallet.service.impl;

import org.bitcoinj.core.Address;
import org.springframework.stereotype.Service;

import com.ecochain.EcoWalletApplication;
import com.ecochain.wallet.service.VerificationAddress;

@Service
public class BtcValidAddressImpl implements VerificationAddress{

    @Override
    public void isValidAddress(String address) {
        
        Address.fromBase58(EcoWalletApplication.params, address);
        
    }

}
