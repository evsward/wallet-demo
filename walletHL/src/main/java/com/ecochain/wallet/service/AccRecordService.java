package com.ecochain.wallet.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecochain.wallet.entity.AccRecordDTO;
import com.ecochain.wallet.mapper.AccRecordCommonMapper;
import com.github.pagehelper.PageHelper;


@Service
public class AccRecordService {

    Logger log = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private AccRecordCommonMapper accRecordmapper;
    
    public List<AccRecordDTO> listPageAccRecord(int page,String type,String userId,String currency){
        PageHelper.startPage(page, 10);
        List<AccRecordDTO> list  = null;
        if("1".equals(type)){//充值
            list = (List<AccRecordDTO>)accRecordmapper.listPageRechargeRecord(userId,currency);
        }else if("2".equals(type)){//提现
            list = (List<AccRecordDTO>)accRecordmapper.listPageWithdrawRecord(userId,currency);
        }else if("3".equals(type)){//转账
            list = (List<AccRecordDTO>)accRecordmapper.listPageTransferRecord(userId,currency);
        }else if("4".equals(type)){//兑换
            list = (List<AccRecordDTO>)accRecordmapper.listPageExchangeRecord(userId);
        }else{//充值、提现、转账
            list = (List<AccRecordDTO>)accRecordmapper.listPageAccRecord(userId);
        }
        return list;
    }
}
