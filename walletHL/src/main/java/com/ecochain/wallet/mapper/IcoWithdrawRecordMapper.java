package com.ecochain.wallet.mapper;


import com.ecochain.wallet.entity.IcoWithdrawRecord;

import java.util.List;

public interface IcoWithdrawRecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(IcoWithdrawRecord record);

    IcoWithdrawRecord selectByPrimaryKey(String id);

    List<IcoWithdrawRecord> selectAll();

    int updateByPrimaryKey(IcoWithdrawRecord record);

    List<IcoWithdrawRecord> getWithDrawOrder();

    int updateWithdrawJson(String withdrawMsg);
}