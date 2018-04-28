package com.ecochain.wallet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ecochain.wallet.entity.SystemSendVcode;
import com.ecochain.wallet.mapper.SystemSendVcodeMapper;

@Service
public class SendVodeService  {

    
    @Autowired
    private SystemSendVcodeMapper systemSendVcodeMapper;
    
    public String findVcodeByPhone(String phone) {
        boolean isSend = isSendBy30Minute(phone);
        if(!isSend){
            return null;
        }
        return systemSendVcodeMapper.findVcodeByPhone(phone);
    }
    
    public boolean isSendBy30Minute(String phone){
        return systemSendVcodeMapper.isSendBy30Minute(phone)>0;
    }
    
    public int findCountBy30Minute(String phone){
        return systemSendVcodeMapper.findCountBy30Minute(phone);
    }
    
    public int findCountByDay(String phone){
        return systemSendVcodeMapper.findCountByDay(phone);
    }
    
    public boolean addVcode(SystemSendVcode systemSendVcode){
        return systemSendVcodeMapper.addVcode(systemSendVcode)>0;
    }

}
