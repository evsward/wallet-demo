package com.ecochain.wallet.service.impl;

import org.springframework.stereotype.Service;

import com.ecochain.util.EtherWalletUtil;

@Service
public class EthValidAddressImpl {

    /**
     * 
    * @Title: isValidAddress 
    * @Description: TODO(验证以太坊地址) 
    * @param @param address
    * @param @return    设定文件 
    * @return boolean    返回类型 
    * @throws 
    * @author Anne
     */
    public boolean isValidAddress(String address) {
        return EtherWalletUtil.isValidAddress(address);
    }

}
