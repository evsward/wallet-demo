package com.fh.controller.withdrawrecord.withdrawrecord;

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
import com.fh.service.withdrawrecord.withdrawrecord.WithdrawRecordManager;

/** 
 * 说明：提现记录管理
 * 创建人：FH Q313596790
 * 创建时间：2017-07-28
 */
@Controller
@RequestMapping(value="/withdrawrecord")
public class WithdrawRecordController extends BaseController {
	
	String menuUrl = "withdrawrecord/list.do"; //菜单地址(权限用)
	@Resource(name="withdrawrecordService")
	private WithdrawRecordManager withdrawrecordService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/save")
	public ModelAndView save() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增WithdrawRecord");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("id", this.get32UUID());	//主键
		withdrawrecordService.save(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"删除WithdrawRecord");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return;} //校验权限
		PageData pd = new PageData();
		pd = this.getPageData();
		withdrawrecordService.delete(pd);
		out.write("success");
		out.close();
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改WithdrawRecord");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String STATUS = pd.getString("STATUS");
		pd = withdrawrecordService.findById(pd);
		pd.put("STATUS", STATUS);//设置状态为审核通过
		withdrawrecordService.edit(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"列表WithdrawRecord");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String keywords = pd.getString("keywords");				//关键词检索条件
		String STATUS = pd.getString("STATUS");	
		if(null != keywords && !"".equals(keywords)){
			pd.put("keywords", keywords.trim());
		}
		if(null != STATUS && !"".equals(STATUS)){
			pd.put("STATUS", STATUS.trim());
		}
		page.setPd(pd);
		List<PageData>	varList = withdrawrecordService.list(page);	//列出WithdrawRecord列表
		for(PageData pdobj : varList){//  去掉科学计数法
			pdobj.put("FREE",new DecimalFormat("######0.0000000").format(pdobj.get("FREE")==null?0:pdobj.get("FREE")));
			pdobj.put("MONEY",new DecimalFormat("######0.0000000").format(pdobj.get("MONEY")==null?0:pdobj.get("MONEY")));
		}
		mv.setViewName("withdrawrecord/withdrawrecord/withdrawrecord_list");
		mv.addObject("varList", varList);
		mv.addObject("pd", pd);
		mv.addObject("QX",Jurisdiction.getHC());	//按钮权限
		return mv;
	}
	
	/**统计系统内充币总量提币总量统计
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/listRechargeWithdraw")
	public ModelAndView listRechargeWithdraw(Page page) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"列表WithdrawRecord");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String lastStart = pd.getString("lastStart");				//关键词检索条件
		String lastEnd = pd.getString("lastEnd");
		if(null != lastStart && !"".equals(lastStart)){
			pd.put("lastStart", lastStart.trim());
		}
		
		if(null != lastEnd && !"".equals(lastEnd)){
			pd.put("lastEnd", lastEnd.trim());
		}
		//page.setPd(pd);
		List<PageData>	varList = withdrawrecordService.listRechargeWithdraw(pd);	//列出WithdrawRecord列表
		/*for(PageData pdobj : varList){//  去掉科学计数法
			pdobj.put("FREE",new DecimalFormat("######0.00000000").format(pdobj.get("FREE")==null?0:pdobj.get("FREE")));
		}*/
		mv.setViewName("withdrawrecord/withdrawrecord/withdrawRecharge_list");
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
		mv.setViewName("withdrawrecord/withdrawrecord/withdrawrecord_edit");
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
		pd = withdrawrecordService.findById(pd);	//根据ID读取
		mv.setViewName("withdrawrecord/withdrawrecord/withdrawrecord_edit");
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
		logBefore(logger, Jurisdiction.getUsername()+"批量删除WithdrawRecord");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(null != DATA_IDS && !"".equals(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			withdrawrecordService.deleteAll(ArrayDATA_IDS);
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
		logBefore(logger, Jurisdiction.getUsername()+"导出WithdrawRecord到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("用户id");	//1
		titles.add("编号");	//2
		titles.add("提现金额");	//3
		titles.add("账户余额");	//4
		titles.add("费用");	//5
		titles.add("状态");	//6
		titles.add("货币种类");	//7
		titles.add("银行卡号");	//8
		titles.add("银行卡名称");	//9
		titles.add("提现地址");	//10
		titles.add("订单号");	//11
		titles.add("订单号");	//12
		titles.add("创建时间");	//13
		titles.add("修改时间");	//14
		titles.add("操作用户id");	//15
		titles.add("矿工费用");	//16
		titles.add("交易hash");	//17
		dataMap.put("titles", titles);
		List<PageData> varOList = withdrawrecordService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("USER_ID"));	    //1
			vpd.put("var2", varOList.get(i).getString("CODE"));	    //2
			vpd.put("var3", varOList.get(i).getString("MONEY"));	    //3
			vpd.put("var4", varOList.get(i).getString("REMAINDER_MONEY"));	    //4
			vpd.put("var5", varOList.get(i).getString("COST"));	    //5
			vpd.put("var6", varOList.get(i).getString("STATUS"));	    //6
			vpd.put("var7", varOList.get(i).getString("CURRENCY"));	    //7
			vpd.put("var8", varOList.get(i).getString("BANK_CARD_NO"));	    //8
			vpd.put("var9", varOList.get(i).getString("BANK_CARD_NAME"));	    //9
			vpd.put("var10", varOList.get(i).getString("ADDRESS"));	    //10
			vpd.put("var11", varOList.get(i).getString("ORDER_NO"));	    //11
			vpd.put("var12", varOList.get(i).getString("ORDER_ID"));	    //12
			vpd.put("var13", varOList.get(i).getString("CREATE_TIME"));	    //13
			vpd.put("var14", varOList.get(i).getString("UPDATE_TIME"));	    //14
			vpd.put("var15", varOList.get(i).getString("BY_USER_ID"));	    //15
			vpd.put("var16", varOList.get(i).getString("FREE"));	    //16
			vpd.put("var17", varOList.get(i).getString("TX_HASH"));	    //17
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
