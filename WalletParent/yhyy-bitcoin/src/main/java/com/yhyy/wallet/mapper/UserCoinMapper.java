package com.yhyy.wallet.mapper;

import com.yhyy.wallet.entity.UserCoin;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface UserCoinMapper {
    int deleteByPrimaryKey(String id);

    int insert(UserCoin userCoin);

    int insertSelective(UserCoin userCoin);

    UserCoin selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(UserCoin userCoin);

    int updateByPrimaryKey(UserCoin userCoin);
    
    UserCoin getUserCoinByAddress(@Param("address") String address);
    
    UserCoin getUserCoinByAccountAndCurrency(@Param("account") String account,@Param("currency") String currency);
    
    List<UserCoin> getAllUserCoin(@Param("currency") String currency);
    
    
}