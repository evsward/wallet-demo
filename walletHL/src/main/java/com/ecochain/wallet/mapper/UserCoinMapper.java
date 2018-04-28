package com.ecochain.wallet.mapper;

import com.ecochain.wallet.entity.UserCoin;

import java.util.List;
import java.util.Map;

public interface UserCoinMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserCoin record);

    int insertSelective(UserCoin record);

    UserCoin selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserCoin record);

    int updateByPrimaryKey(UserCoin record);
    
    String getUserCoinByUserid(String userid);

    UserCoin getUserCoinByAddress(String address);

    String findCoinAddresByCurrency(Map map);

    String findTransferUserIdByConinAddress(Map map);
    
    int batchInsertSelective(List<UserCoin> list);
    
    String getUserIdByAddress(String address);
    
    List<UserCoin> getCoinListByUserId(String userId);
    
}