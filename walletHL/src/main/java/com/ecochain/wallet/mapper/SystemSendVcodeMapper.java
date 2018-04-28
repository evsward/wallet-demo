package com.ecochain.wallet.mapper;

import com.ecochain.wallet.entity.SystemSendVcode;


public interface SystemSendVcodeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SystemSendVcode record);

    int insertSelective(SystemSendVcode record);

    SystemSendVcode selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SystemSendVcode record);

    int updateByPrimaryKey(SystemSendVcode record);
    
    String findVcodeByPhone(String phone);
    
    int isSendBy30Minute(String phone);
    
    int findCountBy30Minute(String phone);
    
    int findCountByDay(String phone);
    
    int addVcode(SystemSendVcode systemSendVcode);
}