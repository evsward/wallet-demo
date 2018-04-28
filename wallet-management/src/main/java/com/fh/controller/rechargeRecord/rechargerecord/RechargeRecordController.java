package com.fh.controller.rechargeRecord.rechargerecord;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.DecimalFormat;
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
import com.fh.service.rechargeRecord.rechargerecord.RechargeRecordManager;
import com.fh.service.system.dictionaries.DictionariesManager;

/** 
 * 说明：充值记录管理
 * 创建人：FH Q313596790
 * 创建时间：2017-08-02
 */
@Controller
@RequestMapping(value="/rechargerecord")
public class RechargeRecordController extends BaseController {
	
	String menuUrl = "rechargerecord/list.do"; //菜单地址(权限用)
	@Resource(name="rechargerecordService")
	private RechargeRecordManager rechargerecordService;
	@Resource(name="dictionariesService")
	private DictionariesManager dictionariesService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/save")
	public ModelAndView save() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增RechargeRecord");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("id", this.get32UUID());	//主键
		rechargerecordService.save(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"删除RechargeRecord");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return;} //校验权限
		PageData pd = new PageData();
		pd = this.getPageData();
		rechargerecordService.delete(pd);
		out.write("success");
		out.close();
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改RechargeRecord");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		rechargerecordService.edit(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"列表RechargeRecord");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String keywords = pd.getString("keywords");				//关键词检索条件
		if(null != keywords && !"".equals(keywords)){
			pd.put("keywords", keywords.trim());
		}
		page.setPd(pd);
		List<PageData>	varList = rechargerecordService.list(page);	//列出RechargeRecord列表
		for(PageData pdobj : varList){//  去掉科学计数法
			pdobj.put("MONEY",new DecimalFormat("######0.0000000").format(pdobj.get("MONEY")==null?0:pdobj.get("MONEY")));
			pdobj.put("REMAINDER_MONEY",new DecimalFormat("######0.0000000").format(pdobj.get("REMAINDER_MONEY")==null?0:pdobj.get("REMAINDER_MONEY")));
			
		}
		
		mv.setViewName("rechargeRecord/rechargerecord/rechargerecord_list");
		mv.addObject("varList", varList);
		mv.addObject("pd", pd);
		mv.addObject("QX",Jurisdiction.getHC());	//按钮权限
		return mv;
	}
	
	/**去新增页面
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goAdd")
	public ModelAndView goAdd()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("group_id", "0d3d9c9ead8e4dbdbfcdfc56cba10019");//ico_investCoinType
		List<PageData>	ico_investCoinType = dictionariesService.listAllFromGeneralCode(pd); 
		
		mv.addObject("ico_investCoinType", ico_investCoinType);
		mv.setViewName("rechargeRecord/rechargerecord/rechargerecord_edit");
		mv.addObject("msg", "save");
		mv.addObject("pd", pd);
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
		pd = rechargerecordService.findById(pd);	//根据ID读取
		
		pd.put("group_id", "0d3d9c9ead8e4dbdbfcdfc56cba10019");//ico_investCoinType
		List<PageData>	ico_investCoinType = dictionariesService.listAllFromGeneralCode(pd); 
		
		mv.addObject("ico_investCoinType", ico_investCoinType);
		mv.setViewName("rechargeRecord/rechargerecord/rechargerecord_edit");
		mv.addObject("msg", "edit");
		mv.addObject("pd", pd);
		return mv;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@ResponseBody
	public Object deleteAll() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"批量删除RechargeRecord");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(null != DATA_IDS && !"".equals(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			rechargerecordService.deleteAll(ArrayDATA_IDS);
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
		logBefore(logger, Jurisdiction.getUsername()+"导出RechargeRecord到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("用户id");	//1
		titles.add("编号");	//2
		titles.add("充值金额");	//3
		titles.add("账户余额");	//4
		titles.add("状态");	//5
		titles.add("货币种类");	//6
		titles.add("订单号");	//7
		titles.add("订单时间");	//8
		titles.add("订单号");	//9
		titles.add("创建时间");	//10
		titles.add("修改时间");	//11
		titles.add("操作用户id");	//12
		titles.add("矿工费");	//13
		titles.add("交易hash");	//14
		titles.add("");	//15
		dataMap.put("titles", titles);
		List<PageData> varOList = rechargerecordService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("USER_ID"));	    //1
			vpd.put("var2", varOList.get(i).getString("CODE"));	    //2
			vpd.put("var3", varOList.get(i).getString("MONEY"));	    //3
			vpd.put("var4", varOList.get(i).getString("REMAINDER_MONEY"));	    //4
			vpd.put("var5", varOList.get(i).getString("STATUS"));	    //5
			vpd.put("var6", varOList.get(i).getString("CURRENCY"));	    //6
			vpd.put("var7", varOList.get(i).getString("ORDID"));	    //7
			vpd.put("var8", varOList.get(i).getString("ORDDATE"));	    //8
			vpd.put("var9", varOList.get(i).getString("ORDER_NO"));	    //9
			vpd.put("var10", varOList.get(i).getString("CREATE_TIME"));	    //10
			vpd.put("var11", varOList.get(i).getString("UPDATE_TIME"));	    //11
			vpd.put("var12", varOList.get(i).getString("BY_USER_ID"));	    //12
			vpd.put("var13", varOList.get(i).getString("FREE"));	    //13
			vpd.put("var14", varOList.get(i).getString("TX_HASH"));	    //14
			vpd.put("var15", varOList.get(i).getString("ADDRESS"));	    //15
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
