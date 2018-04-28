package com.ecochain.wallet.mapper;


import java.util.List;

import com.ecochain.wallet.entity.IcoGatherRecord;

/**
 * 
* @ClassName: IcoGatherRecordMapper 
* @Description: TODO(归集记录Mapper) 
* @author dxm 
* @date 2017年8月21日 下午11:25:53 
*
 */
public interface IcoGatherRecordMapper {

    int insert(IcoGatherRecord record);
    
    List<IcoGatherRecord> listGatherRecord();
    
    int updateGatherStatusRecordByID(IcoGatherRecord record);
    
    IcoGatherRecord getGetherRecordByOrderNO(String orderNO);
    
    int delGatherRecordByID(IcoGatherRecord record);
    
}