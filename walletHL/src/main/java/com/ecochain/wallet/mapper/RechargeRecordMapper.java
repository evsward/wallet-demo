package com.ecochain.wallet.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ecochain.wallet.entity.RechargeRecord;
import com.ecochain.wallet.entity.RechargeRecordExample;

public interface RechargeRecordMapper {
    long countByExample(RechargeRecordExample example);

    int deleteByExample(RechargeRecordExample example);

    int deleteByPrimaryKey(String id);

    int insert(RechargeRecord record);

    int insertSelective(RechargeRecord record);

    List<RechargeRecord> selectByExample(RechargeRecordExample example);

    RechargeRecord selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") RechargeRecord record, @Param("example") RechargeRecordExample example);

    int updateByExample(@Param("record") RechargeRecord record, @Param("example") RechargeRecordExample example);

    int updateByPrimaryKeySelective(RechargeRecord record);

    int updateByPrimaryKey(RechargeRecord record);
    
    RechargeRecord findRechargeRecordByTxHash(String txHash);
    
    int updateRechargeRecordByTxHash(RechargeRecord rechargeRecord);
}