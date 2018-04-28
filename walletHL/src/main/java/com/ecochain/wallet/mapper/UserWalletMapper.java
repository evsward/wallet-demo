package com.ecochain.wallet.mapper;

import com.ecochain.wallet.entity.IcoWithdrawRecord;
import com.ecochain.wallet.entity.UserWallet;
import com.ecochain.wallet.entity.UserWalletExample;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface UserWalletMapper {
    long countByExample(UserWalletExample example);

    int deleteByExample(UserWalletExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(UserWallet record);

    int insertSelective(UserWallet record);

    List<UserWallet> selectByExample(UserWalletExample example);

    UserWallet selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") UserWallet record, @Param("example") UserWalletExample example);

    int updateByExample(@Param("record") UserWallet record, @Param("example") UserWalletExample example);

    int updateByPrimaryKeySelective(UserWallet record);

    int updateByPrimaryKey(UserWallet record);
    
    UserWallet getWalletByUserId(String userId);
    
    UserWallet getWalletByUserName(String userName);
    
    int updateSub(UserWallet userWallet);
    
    int updateAdd(UserWallet userWallet);
    
    int exchangeRMB2Coin(@Param("amnt") BigDecimal amnt,@Param("rmbAmnt") BigDecimal rmbAmnt,@Param("userId") String userId,@Param("currency") String currency);
    
    int companySubCoin(@Param("amnt") BigDecimal amnt,@Param("rmbAmnt") BigDecimal rmbAmnt,@Param("currency") String currency);
    
    int exchangeCoin2RMB(@Param("amnt") BigDecimal amnt,@Param("rmbAmnt") BigDecimal rmbAmnt,@Param("userId") String userId,@Param("currency") String currency);
    
    int companyAddCoin(@Param("amnt") BigDecimal amnt,@Param("rmbAmnt") BigDecimal rmbAmnt,@Param("currency") String currency);

    List<Map<String,Object>> getAddressAndBalance(String userId);

    int updateSanAmount(Map updateMap);

    int innerTransferSan(IcoWithdrawRecord icoWithdrawRecord);
}