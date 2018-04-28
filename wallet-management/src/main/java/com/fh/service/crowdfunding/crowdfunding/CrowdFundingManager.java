package com.fh.service.crowdfunding.crowdfunding;

import java.util.List;
import com.fh.entity.Page;
import com.fh.util.PageData;

/** 
 * 说明： 众筹管理接口
 * 创建人：FH Q313596790
 * 创建时间：2017-07-26
 * @version
 */
public interface CrowdFundingManager{

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
	
	/**列表详情
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> detailList(Page page)throws Exception;
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception;
	
	/**列表金额(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAmount(PageData pd)throws Exception;
	
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

	/**查找投资未成功需要退款的列表
	 * @throws Exception
	 */
	List<PageData> findInvestRefund()throws Exception;

	/**更新投资退回状态 并且 增加钱包金额
	 * @throws Exception
	 */
	void updateInvestRefund(PageData pd)throws Exception;

	/**全站货币总量
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listCurrency(PageData pd)throws Exception;
	
	/**查询项目附件
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAttachment(PageData pd)throws Exception;
	
}

