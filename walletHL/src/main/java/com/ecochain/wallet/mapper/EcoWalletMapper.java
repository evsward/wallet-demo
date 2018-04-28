package com.ecochain.wallet.mapper;

import com.ecochain.wallet.entity.EcoWallet;
import com.ecochain.wallet.entity.EcoWalletExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface EcoWalletMapper {
    long countByExample(EcoWalletExample example);

    int deleteByExample(EcoWalletExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(EcoWallet record);

    int insertSelective(EcoWallet record);

    List<EcoWallet> selectByExample(EcoWalletExample example);

    EcoWallet selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") EcoWallet record, @Param("example") EcoWalletExample example);

    int updateByExample(@Param("record") EcoWallet record, @Param("example") EcoWalletExample example);

    int updateByPrimaryKeySelective(EcoWallet record);

    int updateByPrimaryKey(EcoWallet record);

    Map getUserInfo(String userId);

    int findSanAddress(String userName);
    
}