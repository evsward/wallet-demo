package com.ecochain.wallet.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecochain.wallet.entity.IcoRechargeRecord;
import com.ecochain.wallet.mapper.IcoRechargeRecordMapper;

/**
 * 
* @ClassName: IcoRechargeRecordService 
* @Description: TODO(ico充值记录service) 
* @author dxm
* @date 2017年8月21日 上午10:04:22 
*
 */
@Service
public class IcoRechargeRecordService {

	@Autowired
	private IcoRechargeRecordMapper  rechargeRecordMapper;
	
	/**
	 * 
	* @Title: listRechargeRecordByGather 
	* @Description: TODO(查询所有的充值记录，用于归集)
	* @return List<IcoRechargeRecord>    返回充值记录列表 
	* @throws 
	* @author Anne
	 */
	public List<IcoRechargeRecord> listRechargeRecordByGather(){
		return rechargeRecordMapper.listRechargeRecordByGather();
	}
	
	/**
	 * 
	* @Title: updateRechargeRecord 
	* @Description: TODO(归集完毕后，修改给已归集) 
	* @param @param record 参数
	* @return int    返回修改条数 
	* @throws 
	* @author Anne
	 */
	public int updateRechargeRecord(IcoRechargeRecord record){
		return rechargeRecordMapper.updateRechargeRecordByID(record);
	}
	
	/**
	 * 
	* @Title: getIcoRechargeRecordByOrderId 
	* @Description: TODO(根据orderID获取IcoRechargeRecord对象) 
	* @param ordid 订单号
	* @return IcoRechargeRecord    返回充值记录对象 
	* @throws 
	* @author Anne
	 */
	public IcoRechargeRecord getIcoRechargeRecordByOrderId(String ordid){
		return rechargeRecordMapper.getIcoRechargeRecordByOrderId(ordid);
	}
}
