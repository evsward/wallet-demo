package com.fh.service.crowdfunding.crowdfunding.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fh.dao.DaoSupport;
import com.fh.entity.Page;
import com.fh.util.PageData;
import com.fh.service.crowdfunding.crowdfunding.CrowdFundingManager;

import org.springframework.transaction.annotation.Transactional;

/** 
 * 说明： 众筹管理
 * 创建人：FH Q313596790
 * 创建时间：2017-07-26
 * @version
 */
@Service("crowdfundingService")
public class CrowdFundingService implements CrowdFundingManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("CrowdFundingMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("CrowdFundingMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("CrowdFundingMapper.edit", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("CrowdFundingMapper.datalistPage", page);
	}
	
	/**列表详情
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> detailList(Page page)throws Exception{
		return (List<PageData>)dao.findForList("CrowdFundingMapper.detaillistPage", page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("CrowdFundingMapper.listAll", pd);
	}
	
	/**列表金额(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAmount(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("CrowdFundingMapper.listAmount", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("CrowdFundingMapper.findById", pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("CrowdFundingMapper.deleteAll", ArrayDATA_IDS);
	}

	@Override
	public List<PageData> findInvestRefund()throws Exception {
		return (List<PageData>) dao.findForList("CrowdFundingMapper.findInvestRefund", null);
	}

	@Override
    @Transactional
	public void updateInvestRefund(PageData pd)throws Exception {
		dao.update("CrowdFundingMapper.updateProSupportAmountRefundState", pd);
		dao.update("CrowdFundingMapper.updateAdd", pd);
	}
	
	@Override
	public List<PageData> listCurrency(PageData pd)throws Exception {
		return (List<PageData>) dao.findForList("CrowdFundingMapper.listCurrency", pd);
	}
	
	@Override
	public List<PageData> listAttachment(PageData pd)throws Exception{
		return (List<PageData>) dao.findForList("CrowdFundingMapper.listAttachment", pd);
	}
}

