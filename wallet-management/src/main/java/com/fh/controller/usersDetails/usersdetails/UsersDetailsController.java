package com.fh.controller.usersDetails.usersdetails;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fh.controller.base.BaseController;
import com.fh.entity.Page;
import com.fh.service.userLogin.userlogin.UserLoginManager;
import com.fh.service.usersDetails.usersdetails.UsersDetailsManager;
import com.fh.util.AppUtil;
import com.fh.util.DateUtil;
import com.fh.util.Jurisdiction;
import com.fh.util.MD5Util;
import com.fh.util.ObjectExcelView;
import com.fh.util.PageData;

/** 
 * 说明：用户详情
 * 创建人：FH Q313596790
 * 创建时间：2017-08-01
 */
@Controller
@RequestMapping(value="/usersdetails")
public class UsersDetailsController extends BaseController {
	
	String menuUrl = "usersdetails/list.do"; //菜单地址(权限用)
	@Resource(name="usersdetailsService")
	private UsersDetailsManager usersdetailsService;
	@Resource(name="userloginService")
	private UserLoginManager userloginService;
	
	
	/**判断编号是否存在
	 * @return
	 */
	@RequestMapping(value="/hasA")
	@ResponseBody
	public Object hasN(){
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		try{
			pd = this.getPageData();
			if(userloginService.findByAccount(pd).size()>0 ){
				errInfo = "error";
			}
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		map.put("result", errInfo);				//返回结果
		return AppUtil.returnObject(new PageData(), map);
	}
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/save")
	public ModelAndView save() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增UsersDetails");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("REG_TIME", DateUtil.getTime());
		usersdetailsService.save(pd);
		pd.put("USER_ID", pd.get("id"));
		//pd.put("PASSWORD", MD5Util.getMd5Code((String)pd.get("PASSWORD")));
		userloginService.save(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"删除UsersDetails");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return;} //校验权限
		PageData pd = new PageData();
		pd = this.getPageData();
		usersdetailsService.delete(pd);
		userloginService.delete(pd);
		out.write("success");
		out.close();
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改UsersDetails");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("REG_TIME", DateUtil.getTime());
		usersdetailsService.edit(pd);
		pd.put("PASSWORD", MD5Util.getMd5Code((String)pd.get("PASSWORD")));
		userloginService.edit(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"列表UsersDetails");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String keywords = pd.getString("keywords");				//关键词检索条件
		if(null != keywords && !"".equals(keywords)){
			pd.put("keywords", keywords.trim());
		}
		page.setPd(pd);
		List<PageData>	varList = usersdetailsService.list(page);	//列出UsersDetails列表
		for(PageData pdObj:varList){
			String MOBILE_PHONE = (String)pdObj.get("MOBILE_PHONE");
			pdObj.put("MOBILE_PHONE", StringUtils.isEmpty(MOBILE_PHONE)?"":MOBILE_PHONE.substring(0, 3)+"XXXXXXXX");
		}
		
		mv.setViewName("usersDetails/usersdetails/usersdetails_list");
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
		mv.setViewName("usersDetails/usersdetails/usersdetails_edit");
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
		pd = usersdetailsService.findById(pd);	//根据ID读取
		mv.setViewName("usersDetails/usersdetails/usersdetails_edit");
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
		logBefore(logger, Jurisdiction.getUsername()+"批量删除UsersDetails");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(null != DATA_IDS && !"".equals(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			usersdetailsService.deleteAll(ArrayDATA_IDS);
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
		logBefore(logger, Jurisdiction.getUsername()+"导出UsersDetails到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("用户头像");	//1
		titles.add("身份证");	//2
		titles.add("会员Email ");	//3
		titles.add("公钥");	//4
		titles.add("省信息");	//5
		titles.add("地址");	//6
		titles.add("用户地址信息");	//7
		titles.add("1 男 2女");	//8
		titles.add("种子");	//9
		titles.add("用户名 ");	//10
		titles.add("用户类型:1-普通会员 2-钻石会员创业会员 3-店铺 4-供应商 5-代理商 6-国内物流 7-国外物流");	//11
		titles.add("注册时间");	//12
		titles.add("推荐人的usercode");	//13
		titles.add("会员推荐码");	//14
		titles.add("昵称");	//15
		titles.add("移动电话");	//16
		titles.add("二维码");	//17
		titles.add("身份证号");	//18
		titles.add("交易密码");	//19
		titles.add("收款二维码");	//20
		titles.add("");	//21
		titles.add("");	//22
		titles.add("");	//23
		titles.add("");	//24
		titles.add("");	//25
		dataMap.put("titles", titles);
		List<PageData> varOList = usersdetailsService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("USER_IMG"));	    //1
			vpd.put("var2", varOList.get(i).getString("ID_CARD"));	    //2
			vpd.put("var3", varOList.get(i).getString("EMAIL"));	    //3
			vpd.put("var4", varOList.get(i).getString("PUBLIC_KEY"));	    //4
			vpd.put("var5", varOList.get(i).getString("PROVINCE"));	    //5
			vpd.put("var6", varOList.get(i).getString("ADDRESS"));	    //6
			vpd.put("var7", varOList.get(i).getString("ADDRESS_MSG"));	    //7
			vpd.put("var8", varOList.get(i).get("SEX").toString());	//8
			vpd.put("var9", varOList.get(i).getString("SEEDS"));	    //9
			vpd.put("var10", varOList.get(i).getString("USER_NAME"));	    //10
			vpd.put("var11", varOList.get(i).getString("USER_TYPE"));	    //11
			vpd.put("var12", varOList.get(i).getString("REG_TIME"));	    //12
			vpd.put("var13", varOList.get(i).getString("PARENT_USERCODE"));	    //13
			vpd.put("var14", varOList.get(i).getString("USERCODE"));	    //14
			vpd.put("var15", varOList.get(i).getString("ALIAS"));	    //15
			vpd.put("var16", varOList.get(i).getString("MOBILE_PHONE"));	    //16
			vpd.put("var17", varOList.get(i).getString("RCODE"));	    //17
			vpd.put("var18", varOList.get(i).getString("IDNO"));	    //18
			vpd.put("var19", varOList.get(i).getString("TRANS_PASSWORD"));	    //19
			vpd.put("var20", varOList.get(i).getString("PAY_RCODE"));	    //20
			vpd.put("var21", varOList.get(i).getString("REMARK1"));	    //21
			vpd.put("var22", varOList.get(i).getString("REMARK2"));	    //22
			vpd.put("var23", varOList.get(i).getString("REMARK3"));	    //23
			vpd.put("var24", varOList.get(i).getString("REMARK4"));	    //24
			vpd.put("var25", varOList.get(i).getString("REMARK5"));	    //25
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
