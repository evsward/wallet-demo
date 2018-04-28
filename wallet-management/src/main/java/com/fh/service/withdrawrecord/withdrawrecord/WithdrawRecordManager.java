package com.fh.service.withdrawrecord.withdrawrecord;

import java.util.List;

import com.fh.entity.Page;
import com.fh.util.PageData;

/** 
 * 说明： 提现记录管理接口
 * 创建人：FH Q313596790
 * 创建时间：2017-07-28
 * @version
 */
public interface WithdrawRecordManager{

	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception;
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception;
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception;
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception;
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception;
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception;
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception;
	
	public List<PageData> listRechargeWithdraw(PageData pd)throws Exception;
	
	/**查询user_coin 数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findCountByCoin(PageData pd)throws Exception;
	/**修改user_Wallet 数据
	 * @param pd
	 * @throws Exception
	 */
	public void updateWallet(PageData pd)throws Exception;
	
}

