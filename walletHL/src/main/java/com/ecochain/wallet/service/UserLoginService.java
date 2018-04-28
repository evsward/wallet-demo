package com.ecochain.wallet.service;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecochain.wallet.entity.PageData;
import com.ecochain.wallet.entity.UserLogin;
import com.ecochain.wallet.mapper.UserLoginMapper;


@Service
public class UserLoginService {
    

    @Autowired
    private UserLoginMapper userLoginMapper;
    
    public boolean findIsExist(String account) {
        return userLoginMapper.findIsExist(account)>0;
    }
    
    public boolean modifyPwd(String account, String password) {
        return userLoginMapper.modifyPwd(account, password)>0;
    }
    
    public UserLogin getUserLoginByUserId(String userId){
        return userLoginMapper.getUserLoginByUserId(userId);
    }

    public Map<String,Object> getUserInfoByAccount(String account){
        return userLoginMapper.getUserInfoByAccount(account);
    }
    
    public UserLogin getLastLoginTime(String account){
        return userLoginMapper.getLastLoginTime(account);
    }
    
    public boolean IsExistByAccountOrUserName(String account) {
        return userLoginMapper.IsExistByAccountOrUserName(account)>0;
    }
    
    public Map<String,Object> getUserInfoByAccountOrUserName(String account){
        return userLoginMapper.getUserInfoByAccountOrUserName(account);
    }
}
