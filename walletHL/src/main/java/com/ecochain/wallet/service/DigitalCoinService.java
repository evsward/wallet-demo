package com.ecochain.wallet.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecochain.wallet.entity.DigitalCoin;
import com.ecochain.wallet.mapper.DigitalCoinMapper;

@Service
public class DigitalCoinService {

    @Autowired
    private DigitalCoinMapper digitalCoinMapper;
    
    public DigitalCoin getCoinPrice(String coinName){
        return digitalCoinMapper.getCoinPrice(coinName);
    }
    /**
     * @describe:查询所有币种
     * @author: zhangchunming
     * @date: 2017年7月19日下午2:33:14
     * @return: PageData
     */
    public List<DigitalCoin> getAllCoinPrice(){
        return digitalCoinMapper.getAllCoinPrice();
    }
}
