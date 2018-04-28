package com.ecochain.wallet.mapper;

import com.ecochain.wallet.entity.TransferRecord;



public interface TransferRecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(TransferRecord record);

    int insertSelective(TransferRecord record);

    TransferRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(TransferRecord record);

    int updateByPrimaryKey(TransferRecord record);
}