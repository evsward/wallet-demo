package com.ecochain.wallet.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ecochain.wallet.entity.PageData;
import com.ecochain.wallet.entity.UserBank;

public interface UserBankMapper {
    int deleteByPrimaryKey(String id);

    int insert(UserBank record);

    int insertSelective(UserBank record);

    UserBank selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(UserBank record);

    int updateByPrimaryKey(UserBank record);
    
    List<UserBank> getBankList(String userId);
    
    int setDefault(@Param("userId") String userId,@Param("id") String id);
    
    int cancelDefault(String userId);
    
    int isExist(@Param("userId") String userId,@Param("bankNo") String bankNo);
}