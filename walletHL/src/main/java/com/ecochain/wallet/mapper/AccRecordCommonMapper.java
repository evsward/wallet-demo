package com.ecochain.wallet.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ecochain.wallet.entity.AccRecordDTO;
import com.ecochain.wallet.entity.EcoWallet;


public interface AccRecordCommonMapper {
    /**
     * @describe:查询账户流水记录（充值，提现，转账）
     * @author: zhangchunming
     * @date: 2017年7月26日下午2:24:07
     * @param accRecordDTO
     * @return: List<AccRecordDTO>
     */
    List<AccRecordDTO> listPageAccRecord(@Param("userId") String userId);
    /**
     * @describe:查询充值记录
     * @author: zhangchunming
     * @date: 2017年7月26日下午5:34:01
     * @param userId
     * @return: List<AccRecordDTO>
     */
    List<AccRecordDTO> listPageRechargeRecord(@Param("userId") String userId,@Param("currency") String currency);
    /**
     * @describe:查询提现记录
     * @author: zhangchunming
     * @date: 2017年7月26日下午5:34:16
     * @param userId
     * @return: List<AccRecordDTO>
     */
    List<AccRecordDTO> listPageWithdrawRecord(@Param("userId") String userId,@Param("currency") String currency);
    
    /**
     * @describe:转币记录
     * @author: zhangchunming
     * @date: 2017年7月31日下午1:51:13
     * @param userId
     * @return: List<AccRecordDTO>
     */
    List<AccRecordDTO> listPageTransferRecord(@Param("userId") String userId,@Param("currency") String currency);
    /**
     * @describe:
     * @author: zhangchunming
     * @date: 2017年7月31日下午3:51:55
     * @param userId
     * @return: List<AccRecordDTO>
     */
    List<AccRecordDTO> listPageExchangeRecord(@Param("userId") String userId);
}