package com.ecochain.wallet.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.ecochain.wallet.entity.UserLogin;
import com.ecochain.wallet.entity.UsersDetails;


public interface UsersDetailsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UsersDetails record);

    int insertSelective(UsersDetails record);

    UsersDetails selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UsersDetails record);

    int updateByPrimaryKey(UsersDetails record);
    
    int isExistTransPassword(@Param("userId") String userId,@Param("transPassword") String transPassword);
    
    Map getUserInfoByUserId(Integer userId);
    
    String getUserIdByAddress(String address);
    
    int setTransPassword(@Param("userId") String userId,@Param("transPassword") String transPassword);
    
    int findIsExist(String mobilePhone);
    
    int updatePhone(@Param("userId") String userId,@Param("mobilePhone") String mobilePhone);
    
    int isExistByUserName(String userName);
    
    String getTransPassword(String userId);
    
    int realName(UsersDetails usersDetails);
}