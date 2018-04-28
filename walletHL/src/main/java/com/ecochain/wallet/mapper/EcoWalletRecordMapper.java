package com.ecochain.wallet.mapper;

import com.ecochain.wallet.entity.EcoWalletRecord;
import com.ecochain.wallet.entity.EcoWalletRecordExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface EcoWalletRecordMapper {
    long countByExample(EcoWalletRecordExample example);

    int deleteByExample(EcoWalletRecordExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(EcoWalletRecord record);

    int insertSelective(EcoWalletRecord record);

    List<EcoWalletRecord> selectByExample(EcoWalletRecordExample example);

    EcoWalletRecord selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") EcoWalletRecord record, @Param("example") EcoWalletRecordExample example);

    int updateByExample(@Param("record") EcoWalletRecord record, @Param("example") EcoWalletRecordExample example);

    int updateByPrimaryKeySelective(EcoWalletRecord record);

    int updateByPrimaryKey(EcoWalletRecord record);
}