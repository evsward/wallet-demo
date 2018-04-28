package com.fh.service.withdrawrecord.withdrawrecord.impl;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.fh.dao.DaoSupport;
import com.fh.entity.Page;
import com.fh.service.withdrawrecord.withdrawrecord.WithdrawRecordManager;
import com.fh.util.OrderGenerater;
import com.fh.util.PageData;
import com.fh.util.UuidUtil;

/** 
 * 说明： 提现记录管理
 * 创建人：FH Q313596790
 * 创建时间：2017-07-28
 * @version
 */
@Service("withdrawrecordService")
public class WithdrawRecordService implements WithdrawRecordManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("WithdrawRecordMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("WithdrawRecordMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		String status = pd.getString("STATUS")==null?"":pd.getString("STATUS");
		String id = pd.getString("id")==null?"":pd.getString("id");
		String order_no = pd.getString("ORDER_NO")==null?"":pd.getString("ORDER_NO");
		String user_id = pd.getString("USER_ID")==null?"":pd.getString("USER_ID");
		BigDecimal money = pd.get("MONEY")==null?new BigDecimal(0):(BigDecimal)pd.get("MONEY");
		BigDecimal cost = pd.get("COST")==null?new BigDecimal(0):(BigDecimal)pd.get("COST");
		String currency = pd.getString("CURRENCY")==null?"":pd.getString("CURRENCY");
		if(StringUtils.isNotEmpty(status)&&status.equals("1")){
			//查找  user_coin  
			PageData pdd= (PageData)dao.findForObject("WithdrawRecordMapper.findCountByCoin", pd);
			if(pdd!=null&&StringUtils.isNotEmpty(pdd.get("USER_ID").toString())){
				//有数据  更改    提现状态为3  增加  ico_recharge_record  一条数据
				pd.put("STATUS", "3");
				pd.put("id", UuidUtil.get32UUID());	//主键
				pd.put("IS_CONCENTRATE", "1");	//1已归集
				pd.put("ORDER_NO", OrderGenerater.generateOrderNo());	
				if(StringUtils.isEmpty(pd.getString("TX_HASH"))){
					pd.put("TX_HASH", UuidUtil.get32UUID());
				}
				pd.put("USER_ID", pdd.get("USER_ID"));
				dao.save("RechargeRecordMapper.save", pd);
				pd.put(currency+"_AMNT", money);
				dao.update("WithdrawRecordMapper.updateWallet", pd);
			}
		}
		if(StringUtils.isNotEmpty(status)&&status.equals("-1")){
			//修改		更改    提现状态为-1
			//增加  user_wallet  一条数据  根据user_id   把提现   money+cost  加到  user_wallet  userID  相应的币种中
			BigDecimal walletMoney = (money).add(cost);
			pd.put(currency+"_AMNT", walletMoney);
			dao.update("WithdrawRecordMapper.updateWallet", pd);
		}
		pd.put("ORDER_NO", order_no);
		pd.put("id", id);
		pd.put("USER_ID",user_id);
		pd.remove("FREE");
		dao.update("WithdrawRecordMapper.edit", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("WithdrawRecordMapper.datalistPage", page);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listRechargeWithdraw(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("WithdrawRecordMapper.listRechargeWithdraw", pd);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("WithdrawRecordMapper.listAll", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("WithdrawRecordMapper.findById", pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("WithdrawRecordMapper.deleteAll", ArrayDATA_IDS);
	}
	
	
	public PageData findCountByCoin(PageData pd)throws Exception{
		return (PageData)dao.findForObject("WithdrawRecordMapper.findCountByCoin", pd);
	}
	/**修改user_Wallet 数据
	 * @param pd
	 * @throws Exception
	 */
	public void updateWallet(PageData pd)throws Exception{
		dao.update("WithdrawRecordMapper.updateWallet", pd);
	}
	
}

