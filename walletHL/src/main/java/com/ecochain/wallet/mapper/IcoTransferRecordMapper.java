package com.ecochain.wallet.mapper;


import com.ecochain.wallet.entity.IcoTransferRecord;

import java.util.List;

public interface IcoTransferRecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(IcoTransferRecord record);

    IcoTransferRecord selectByPrimaryKey(String id);

    List<IcoTransferRecord> selectAll();

    int updateByPrimaryKey(IcoTransferRecord record);
}