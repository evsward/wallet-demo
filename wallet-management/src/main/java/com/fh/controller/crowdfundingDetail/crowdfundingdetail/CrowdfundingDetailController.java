package com.fh.controller.crowdfundingDetail.crowdfundingdetail;

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
import com.fh.service.crowdfundingDetail.crowdfundingdetail.CrowdfundingDetailManager;

/** 
 * 说明：ICO众筹项目详情表
 * 创建人：FH Q313596790
 * 创建时间：2017-08-22
 */
@Controller
@RequestMapping(value="/crowdfundingdetail")
public class CrowdfundingDetailController extends BaseController {
	
	String menuUrl = "crowdfundingdetail/list.do"; //菜单地址(权限用)
	@Resource(name="crowdfundingdetailService")
	private CrowdfundingDetailManager crowdfundingdetailService;
	@Resource(name="crowdfundingService")
	private CrowdFundingManager crowdfundingService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/save")
	public ModelAndView save() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增CrowdfundingDetail");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		crowdfundingdetailService.save(pd);
		mv.addObject("msg","success");
		mv.setViewName("save_result");
		return mv;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	public void delete(PrintWriter out) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"删除CrowdfundingDetail");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return;} //校验权限
		PageData pd = new PageData();
		pd = this.getPageData();
		crowdfundingdetailService.delete(pd);
		out.write("success");
		out.close();
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改CrowdfundingDetail");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		crowdfundingdetailService.edit(pd);
		mv.addObject("msg","success");
		mv.setViewName("save_result");
		return mv;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	public ModelAndView list(Page page) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"列表CrowdfundingDetail");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String keywords = pd.getString("keywords");				//关键词检索条件
		if(null != keywords && !"".equals(keywords)){
			pd.put("keywords", keywords.trim());
		}
		page.setPd(pd);
		List<PageData>	varList = crowdfundingdetailService.list(page);	//列出CrowdfundingDetail列表
		mv.setViewName("crowdfundingDetail/crowdfundingdetail/crowdfundingdetail_list");
		mv.addObject("varList", varList);
		mv.addObject("pd", pd);
		mv.addObject("QX",Jurisdiction.getHC());	//按钮权限
		return mv;
	}
	
	/**去新增页面
	 * @param
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value="/goAddEdit")
	public ModelAndView goAddEdit()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		PageData crowdfundPd = new PageData();
		PageData crowdfundDetailPd = new PageData();
		pd = this.getPageData();
		String projectId = pd.getString("PROJECT_ID");
		crowdfundDetailPd = crowdfundingdetailService.findById(pd);//  查询crowdfundingdetail
		crowdfundPd = crowdfundingService.findById(pd);//查询  crowdfunding
		if(crowdfundDetailPd!=null){
			mv.addObject("msg", "edit");
			mv.addObject("pd", crowdfundDetailPd);
		}else{
		    pd = new PageData();
			pd.put("PROJECT_ID", projectId);
			pd.put("LOCK_MAX_AMOUNT", crowdfundPd.get("INVEST_AMOUNT")==null?"":crowdfundPd.get("INVEST_AMOUNT"));// 
			mv.addObject("msg", "save");
			mv.addObject("pd", pd);
		}
		mv.setViewName("crowdfundingDetail/crowdfundingdetail/crowdfundingdetail_edit");
		mv.addObject("STARTDATE",crowdfundPd.get("START_TIME")==null?"":crowdfundPd.get("START_TIME"));
		return mv;
	}	
	
	 /**去修改页面
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	public ModelAndView goEdit()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = crowdfundingdetailService.findById(pd);	//根据ID读取
		
		return mv;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@ResponseBody
	public Object deleteAll() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"批量删除CrowdfundingDetail");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(null != DATA_IDS && !"".equals(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			crowdfundingdetailService.deleteAll(ArrayDATA_IDS);
			pd.put("msg", "ok");
		}else{
			pd.put("msg", "no");
		}
		pdList.add(pd);
		map.put("list", pdList);
		return AppUtil.returnObject(pd, map);
	}
	
	 /**导出到excel
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/excel")
	public ModelAndView exportExcel() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"导出CrowdfundingDetail到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("参与锁定众筹项目ID");	//1
		titles.add("锁定众筹项目开始时间");	//2
		titles.add("锁定众筹项目结束时间");	//3
		titles.add("目标锁定金额");	//4
		titles.add("锁定最大可投金额，默认是1");	//5
		titles.add("锁定最小可投金额，默认是0.01");	//6
		titles.add("实际锁定金额");	//7
		titles.add("实际锁定金额所占比例");	//8
		titles.add("创建时间");	//9
		titles.add("更新时间");	//10
		titles.add("备用字段");	//11
		titles.add("项目投资回报率");	//12
		dataMap.put("titles", titles);
		List<PageData> varOList = crowdfundingdetailService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("PROJECT_ID"));	    //1
			vpd.put("var2", varOList.get(i).getString("LOCK_PROJECT_START_TIME"));	    //2
			vpd.put("var3", varOList.get(i).getString("LOCK_PROJECT_END_TIME"));	    //3
			vpd.put("var4", varOList.get(i).getString("TARGET_LOCK_AMOUNT"));	    //4
			vpd.put("var5", varOList.get(i).getString("LOCK_MAX_AMOUNT"));	    //5
			vpd.put("var6", varOList.get(i).getString("LOCK_MIN_AMOUNT"));	    //6
			vpd.put("var7", varOList.get(i).getString("ACTUAL_LOCK_AMOUNT"));	    //7
			vpd.put("var8", varOList.get(i).getString("ACTUAL_LOCK_RATE"));	    //8
			vpd.put("var9", varOList.get(i).getString("CREATE_TIME"));	    //9
			vpd.put("var10", varOList.get(i).getString("UPDATE_TIME"));	    //10
			vpd.put("var11", varOList.get(i).getString("EXT1"));	    //11
			vpd.put("var12", varOList.get(i).getString("LOCK_BACK_RATE"));	    //12
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder){
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format,true));
	}
}
