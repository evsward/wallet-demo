package com.ecochain.wallet.mapper;

import java.util.List;

import com.ecochain.wallet.entity.DigitalCoin;


public interface DigitalCoinMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DigitalCoin record);

    int insertSelective(DigitalCoin record);

    DigitalCoin selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DigitalCoin record);

    int updateByPrimaryKey(DigitalCoin record);
    
    DigitalCoin getCoinPrice(String coinName);
    
    List<DigitalCoin> getAllCoinPrice();
}