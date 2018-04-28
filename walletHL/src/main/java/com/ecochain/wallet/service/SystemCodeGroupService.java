package com.ecochain.wallet.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecochain.wallet.entity.SystemCodeGroup;
import com.ecochain.wallet.mapper.SystemCodeGroupMapper;

@Service
public class SystemCodeGroupService {

    Logger log = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private SystemCodeGroupMapper systemCodeGroupMapper;
    
    public String insert(SystemCodeGroup systemCodeGroup){
    	return systemCodeGroupMapper.insertSystemCodeGroup(systemCodeGroup);
    }
	
    
    
}
