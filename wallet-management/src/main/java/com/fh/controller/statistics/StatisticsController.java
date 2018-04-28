package com.fh.controller.statistics;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fh.controller.base.BaseController;
import com.fh.entity.Page;
import com.fh.util.AppUtil;
import com.fh.util.ObjectExcelView;
import com.fh.util.PageData;
import com.fh.util.Jurisdiction;
import com.fh.util.Tools;
import com.fh.service.crowdfunding.crowdfunding.CrowdFundingManager;
import com.fh.service.rechargeRecord.rechargerecord.RechargeRecordManager;
import com.fh.service.system.dictionaries.DictionariesManager;

/** 
 * 说明：统计
 * 创建人：
 * 创建时间：2017-08-02
 */
@Controller
@RequestMapping(value="/statistics")
public class StatisticsController extends BaseController {
	
	@Resource(name="crowdfundingService")
	private CrowdFundingManager crowdfundingService;
	@Resource(name="dictionariesService")
	private DictionariesManager dictionariesService;
	
	/**统计系统内充币总量统计
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/listCurrency")
	public ModelAndView listCurrency(Page page) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"列表WithdrawRecord");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String lastStart = pd.getString("lastStart");				//关键词检索条件
		String lastEnd = pd.getString("lastEnd");
		String invest_coin_type = pd.getString("invest_coin_type");
		if(null != lastStart && !"".equals(lastStart)){
			pd.put("lastStart", lastStart.trim());
		}
		
		if(null != lastEnd && !"".equals(lastEnd)){
			pd.put("lastEnd", lastEnd.trim());
		}
		if(null != invest_coin_type && !"".equals(invest_coin_type)){
			pd.put("invest_coin_type", invest_coin_type.trim());
		}
		//page.setPd(pd);
		List<PageData>	listCurrency = crowdfundingService.listCurrency(pd);	//列出WithdrawRecord列表
		
		
		pd.put("group_id", "0d3d9c9ead8e4dbdbfcdfc56cba10019");//ico_investCoinType
		List<PageData>	ico_investCoinType = dictionariesService.listAllFromGeneralCode(pd); 
		mv.addObject("ico_investCoinType", ico_investCoinType);
		mv.setViewName("statistics/currency_list");
		mv.addObject("varList", listCurrency);
		mv.addObject("pd", pd);
		mv.addObject("QX",Jurisdiction.getHC());	//按钮权限
		return mv;
	}
}
