package com.ecochain.wallet.mapper;


import com.ecochain.wallet.entity.IcoRechargeRecord;

import java.util.List;

public interface IcoRechargeRecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(IcoRechargeRecord record);

    IcoRechargeRecord selectByPrimaryKey(String id);

    List<IcoRechargeRecord> selectAll();

    int updateByPrimaryKey(IcoRechargeRecord record);

    List<IcoRechargeRecord> getWithRechargeOrder();
    
    List<IcoRechargeRecord> listRechargeRecordByGather();
    
    int updateRechargeRecordByID(IcoRechargeRecord record);
    
    IcoRechargeRecord getIcoRechargeRecordByOrderId(String ordid);
}