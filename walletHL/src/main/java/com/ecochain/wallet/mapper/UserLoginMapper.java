package com.ecochain.wallet.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.ecochain.wallet.entity.UserLogin;


public interface UserLoginMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserLogin record);

    int insertSelective(UserLogin record);

    UserLogin selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserLogin record);

    int updateByPrimaryKey(UserLogin record);
    
    UserLogin getUserByAccAndPass(@Param("account")String account,@Param("password")String password);
    
    int updateLoginTimeById(@Param("id") Integer id, @Param("lastloginIp")String lastloginIp,@Param("lastloginPort")long lastloginPort);
    
    int findIsExist(String account);
    
    int modifyPwd(@Param("account")String account, @Param("password") String password);
    
    UserLogin getUserLoginByUserId(String userId);
    
    Map<String,Object> getUserInfoByAccount(String account);
    
    UserLogin getLastLoginTime(String account);
    
    int IsExistByAccountOrUserName(String account);
    
    Map<String,Object> getUserInfoByAccountOrUserName(String account);
    
    int updateAccount(@Param("account") String account,@Param("userId") String userId);
    
    List<Map> getUserList100();
    
    int updateICOBBSByUserName(@Param("isIcoBbs")String isIcoBbs,@Param("userNameList") List<String> userNameList);
}