package com.ecochain.wallet.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ecochain.wallet.entity.WithdrawRecord;
import com.ecochain.wallet.entity.WithdrawRecordExample;

public interface WithdrawRecordMapper {
    long countByExample(WithdrawRecordExample example);

    int deleteByExample(WithdrawRecordExample example);

    int deleteByPrimaryKey(String id);

    int insert(WithdrawRecord record);

    int insertSelective(WithdrawRecord record);

    List<WithdrawRecord> selectByExample(WithdrawRecordExample example);

    WithdrawRecord selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") WithdrawRecord record, @Param("example") WithdrawRecordExample example);

    int updateByExample(@Param("record") WithdrawRecord record, @Param("example") WithdrawRecordExample example);

    int updateByPrimaryKeySelective(WithdrawRecord record);

    int updateByPrimaryKey(WithdrawRecord record);
    
    boolean updateWithdrawRecordByTxHash(WithdrawRecord record);
    
    boolean updateTxHashById(WithdrawRecord record);
    
    WithdrawRecord getWithDrawRecord(String userId);
    
    int getWithDrawCountByDay(@Param("userId") String userId,@Param("currency") String currency);
    
}