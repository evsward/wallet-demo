package com.ecochain.wallet.service;

import com.ecochain.wallet.entity.UserWallet;
import com.ecochain.wallet.mapper.UserWalletMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecochain.wallet.entity.RechargeRecord;
import com.ecochain.wallet.mapper.RechargeRecordMapper;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RechargeRecordService {

    @Autowired
    RechargeRecordMapper mapper;

    @Autowired
    private UserWalletMapper userWalletMapper;
    
    public RechargeRecord insertRechargeRecord(RechargeRecord record){
        mapper.insertSelective(record);
        return record;
    }
    public RechargeRecord findRechargeRecordByTxHash(String txHash){
        return mapper.findRechargeRecordByTxHash(txHash);
    }
    public boolean updateRechargeRecordByTxHash(RechargeRecord rechargeRecord){
        return mapper.updateRechargeRecordByTxHash(rechargeRecord)>0;
    }

    @Transactional(rollbackFor = Exception.class)
    public void RechargeAndAddBalance(RechargeRecord record, UserWallet userWallet){
        mapper.insertSelective(record);
        userWalletMapper.updateAdd(userWallet);
    }
    
}
