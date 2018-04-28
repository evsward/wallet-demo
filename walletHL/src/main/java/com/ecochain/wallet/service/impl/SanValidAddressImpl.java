package com.ecochain.wallet.service.impl;

import com.ecochain.wallet.service.VerificationAddress;
import org.springframework.stereotype.Service;

@Service
public class SanValidAddressImpl implements VerificationAddress{

    @Override
    public void isValidAddress(String address) {
        com.tfcc.core.AccountID.fromAddress(address);
    }

}
