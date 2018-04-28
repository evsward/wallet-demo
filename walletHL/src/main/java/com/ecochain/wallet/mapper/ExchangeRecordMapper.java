package com.ecochain.wallet.mapper;

import com.ecochain.wallet.entity.ExchangeRecord;



public interface ExchangeRecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(ExchangeRecord record);

    int insertSelective(ExchangeRecord record);

    ExchangeRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ExchangeRecord record);

    int updateByPrimaryKey(ExchangeRecord record);
    
    
}