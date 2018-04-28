package com.yhyy.wallet.mapper;

import com.yhyy.wallet.entity.DigitalCoin;

public interface DigitalCoinMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DigitalCoin record);

    int insertSelective(DigitalCoin record);

    DigitalCoin selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DigitalCoin record);

    int updateByPrimaryKey(DigitalCoin record);
    
    DigitalCoin selectDigitalCoinByCoinName(String coinName);
}