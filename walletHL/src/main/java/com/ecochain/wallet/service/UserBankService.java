package com.ecochain.wallet.service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.ecochain.wallet.entity.UserBank;
import com.ecochain.wallet.mapper.UserBankMapper;

@Service
public class UserBankService {

    
    @Autowired
    private UserBankMapper mapper;
    
    private Logger logger = LoggerFactory.getLogger(getClass());
    
    
    public boolean deleteById(String id) {
        return mapper.deleteByPrimaryKey(id)>0;
    }

    
    public boolean insert(UserBank record){
        return mapper.insert(record)>0;
    }

    
    public boolean insertSelective(UserBank record) {
        return mapper.insertSelective(record)>0;
    }

    
    public boolean updateByIdSelective(UserBank record) {
        return mapper.updateByPrimaryKeySelective(record)>0;
    }

    
    public boolean updateById(UserBank record){
        return mapper.updateByPrimaryKey(record)>0;
    }

    
    public List<UserBank> getBankList(String userId){
        return mapper.getBankList(userId);
    }

    
    @Transactional(propagation =Propagation.REQUIRED)
    public boolean setDefault(String userId,String id){
        boolean cancelDefault = cancelDefault(userId);
        if(cancelDefault){
            return mapper.setDefault(userId, id)>0;
        }
        return false;
    }

    
    public boolean cancelDefault(String userId){
        return mapper.cancelDefault(userId)>0;
    }

    
    public boolean isExist(String userId,String bankNo){
        return mapper.isExist(userId, bankNo)>0;
    }

}
