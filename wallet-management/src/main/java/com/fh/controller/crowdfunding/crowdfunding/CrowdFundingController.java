package com.fh.controller.crowdfunding.crowdfunding;

import java.io.File;
import java.io.IOException;
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

import org.apache.commons.io.FileUtils;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fh.controller.base.BaseController;
import com.fh.dao.redis.RedisDao;
import com.fh.entity.Page;
import com.fh.service.crowdfunding.crowdfunding.CrowdFundingManager;
import com.fh.service.system.dictionaries.DictionariesManager;
import com.fh.util.AppUtil;
import com.fh.util.Jurisdiction;
import com.fh.util.Logger;
import com.fh.util.ObjectExcelView;
import com.fh.util.PageData;

/** 
 * 说明：众筹管理
 * 创建人：FH Q313596790
 * 创建时间：2017-07-26
 */
@Controller
@RequestMapping(value="/crowdfunding")
public class CrowdFundingController extends BaseController {
	
	protected Logger logger = Logger.getLogger(this.getClass());
	String menuUrl = "crowdfunding/list.do"; //菜单地址(权限用)
	@Resource(name="crowdfundingService")
	private CrowdFundingManager crowdfundingService;
	@Resource(name="dictionariesService")
	private DictionariesManager dictionariesService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/save")
	public ModelAndView save() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增CrowdFunding");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("PROJECT_ID", this.get32UUID());	//主键
		crowdfundingService.save(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"删除CrowdFunding");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return;} //校验权限
		PageData pd = new PageData();
		pd = this.getPageData();
		crowdfundingService.delete(pd);
		out.write("success");
		out.close();
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改CrowdFunding");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("PROJECT_STATE", 3);//设置状态为审核通过
		pd.put("CREATE_TIME", new Date());//设置状态为创建时间
		System.out.println("PROJECT_DESC_DETAIL:"+pd.getString("PROJECT_DESC_DETAIL")==null?"":pd.getString("PROJECT_DESC_DETAIL"));
		System.out.println("RICH_TEXT:"+pd.getString("RICH_TEXT")==null?"":pd.getString("RICH_TEXT"));
		try {  // 写入文件
            FileUtils.write(new File(pd.getString("RICH_TEXT")==null?"":pd.getString("RICH_TEXT")), pd.getString("PROJECT_DESC_DETAIL")==null?"":pd.getString("PROJECT_DESC_DETAIL"));
        } catch (IOException e) {
            e.printStackTrace();
        }
		pd.put("PROJECT_DESC_DETAIL", "");
		crowdfundingService.edit(pd);
		
		mv.addObject("msg","success");
		mv.setViewName("save_result");
		return mv;
	}
	
	/**列表明细
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/detailList")
	public ModelAndView detailList(Page page) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"列表详情CrowdFunding");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String PROJECT_ID = pd.getString("PROJECT_ID");	
		if(null != PROJECT_ID && !"".equals(PROJECT_ID)){
			pd.put("PROJECT_ID", PROJECT_ID.trim());
		}
		page.setPd(pd);
		List<PageData>	varList = crowdfundingService.detailList(page);	//列出CrowdFunding列表
		
		
		mv.setViewName("crowdfunding/crowdfunding/crowdfunding_detailList");
		mv.addObject("varList", varList);
		mv.addObject("pd", pd);
		mv.addObject("QX",Jurisdiction.getHC());	//按钮权限
		return mv;
	}
	
	
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	public ModelAndView list(Page page) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"列表CrowdFunding");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String keywords = pd.getString("keywords");				//关键词检索条件
		String projectState= pd.getString("PROJECT_STATE");				//关键词检索条件
		if(null != keywords && !"".equals(keywords)){
			pd.put("keywords", keywords.trim());
		}
		if(null != projectState && !"".equals(projectState)){
			pd.put("projectState", projectState.trim());
		}
		page.setPd(pd);
		List<PageData>	varList = crowdfundingService.list(page);	//列出CrowdFunding列表
		for(PageData pdobj : varList){//  去掉科学计数法
			pdobj.put("INVEST_AMOUNT",new DecimalFormat("######0.0000000").format(pdobj.get("INVEST_AMOUNT")==null?0:pdobj.get("INVEST_AMOUNT")));
			pdobj.put("INVEST_COUNT",new DecimalFormat("######0.0000000").format(pdobj.get("INVEST_COUNT")==null?0:pdobj.get("INVEST_COUNT")));
		}
		
		pd.put("group_id", "0d3d9c9ead8e4dbdbfcdfc56cba10021");//ico_projectState
		List<PageData>	ico_projectState = dictionariesService.listAllFromGeneralCode(pd); 
		
		pd.put("group_id", "0d3d9c9ead8e4dbdbfcdfc56cba10017");//ico_projectStage
		List<PageData>	ico_projectStage = dictionariesService.listAllFromGeneralCode(pd); //查询字典表 system_general_code
		
		mv.addObject("ico_projectStage", ico_projectStage);
		mv.addObject("ico_projectState", ico_projectState);
		mv.setViewName("crowdfunding/crowdfunding/crowdfunding_list");
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
		mv.setViewName("crowdfunding/crowdfunding/crowdfunding_edit");
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
		pd = crowdfundingService.findById(pd);	//根据ID读取
		
		pd.put("INVEST_AMOUNT",new DecimalFormat("######0.0000000").format(pd.get("INVEST_AMOUNT")==null?0:pd.get("INVEST_AMOUNT")));
		pd.put("INVEST_COUNT",new DecimalFormat("######0.0000000").format(pd.get("INVEST_COUNT")==null?0:pd.get("INVEST_COUNT")));
	
		
		if(null==pd.getObject("TARGET_LIMIT")){
			pd.put("TARGET_LIMIT", "0");
		}
		if(null==pd.getObject("INVEST_AMOUNT")){
			pd.put("INVEST_AMOUNT", "0");
		}
		
		//读取富文本
        File file = new File(pd.getString("RICH_TEXT"));
        try {
            String richText = FileUtils.readFileToString(file);
            System.out.println("richText:"+richText);
            pd.put("PROJECT_DESC_DETAIL", richText);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        pd.put("group_id", "0d3d9c9ead8e4dbdbfcdfc56cba10017");//ico_projectStage
		List<PageData>	ico_projectStage = dictionariesService.listAllFromGeneralCode(pd); //查询字典表 system_general_code
		
		pd.put("group_id", "0d3d9c9ead8e4dbdbfcdfc56cba10018");//ico_officeAddress
		List<PageData>	ico_officeAddress = dictionariesService.listAllFromGeneralCode(pd); 
		
		pd.put("group_id", "0d3d9c9ead8e4dbdbfcdfc56cba10019");//ico_investCoinType
		List<PageData>	ico_investCoinType = dictionariesService.listAllFromGeneralCode(pd); 
		
		pd.put("group_id", "0d3d9c9ead8e4dbdbfcdfc56cba10021");//ico_projectState
		List<PageData>	ico_projectState = dictionariesService.listAllFromGeneralCode(pd); 
		
		pd.put("group_id", "0d3d9c9ead8e4dbdbfcdfc56cba10022");//ico_ipoStage
		List<PageData>	ico_ipoStage = dictionariesService.listAllFromGeneralCode(pd); 
		
		List<PageData>	attachments = crowdfundingService.listAttachment(pd); //查询项目附属材料
		mv.addObject("attachments", attachments);
		mv.addObject("ico_projectStage", ico_projectStage);
		mv.addObject("ico_officeAddress", ico_officeAddress);
		mv.addObject("ico_investCoinType", ico_investCoinType);
		mv.addObject("ico_projectState", ico_projectState);
		mv.addObject("ico_ipoStage", ico_ipoStage);
		
		mv.setViewName("crowdfunding/crowdfunding/crowdfunding_edit");
		mv.addObject("msg", "edit");
		mv.addObject("pd", pd);
		mv.addObject("QX",Jurisdiction.getHC());	//按钮权限
		return mv;
	}	
	
	 /**去修改页面
		 * @param
		 * @throws Exception
		 */
	@RequestMapping(value="/detail")
	public ModelAndView detail()throws Exception{
		ModelAndView mav = goEdit();
		mav.addObject("button", "1");
		return mav;
	}	
		
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@ResponseBody
	public Object deleteAll() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"批量删除CrowdFunding");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(null != DATA_IDS && !"".equals(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			crowdfundingService.deleteAll(ArrayDATA_IDS);
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
		logBefore(logger, Jurisdiction.getUsername()+"导出CrowdFunding到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("用户id");	//1
		titles.add("项目名");	//2
		titles.add("项目所属阶段");	//3
		titles.add("真实姓名");	//4
		titles.add("办公地址编码");	//5
		titles.add("办公地址详情");	//6
		titles.add("团队人数");	//7
		titles.add("融资阶段");	//8
		titles.add("項目介紹");	//9
		titles.add("項目介紹");	//10
		titles.add("视频地址");	//11
		titles.add("封面图片");	//12
		titles.add("富文本路径");	//13
		titles.add("投资币种类型(1.BTC 2.ETH 3.LTC)");	//14
		titles.add("是否有限额 ");	//15
		titles.add("投资金额");	//16
		titles.add("回报说明");	//17
		titles.add("名额限制");	//18
		titles.add("支持限制");	//19
		titles.add("是否包邮");	//20
		titles.add("回报时间");	//21
		titles.add("ico天数");	//22
		titles.add("是否有限额(0:无限额 ,1：有限额)");	//23
		titles.add("目标类型");	//24
		titles.add("投资比率");	//25
		titles.add("当前总投资多少个");	//26
		titles.add("目标金额");	//27
		titles.add("联系人");	//28
		titles.add("负责职位");	//29
		titles.add("联系电话");	//30
		titles.add("开始时间");	//31
		titles.add("结束时间");	//32
		titles.add("创建时间");	//33
		titles.add("0未提交、1待审核 、2审核中、3审核通过、4即将开始、5进行中、6已完成、7审核失败、8已结束--未投满");	//34
		titles.add("最小投资金额");	//35
		titles.add("最大投资金额");	//36
		dataMap.put("titles", titles);
		List<PageData> varOList = crowdfundingService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).get("USERID").toString());	//1
			vpd.put("var2", varOList.get(i).getString("PROJECT_NAME"));	    //2
			vpd.put("var3", varOList.get(i).get("PROJECT_STAGE").toString());	//3
			vpd.put("var4", varOList.get(i).getString("REAL_NAME"));	    //4
			vpd.put("var5", varOList.get(i).getString("OFFICE_ADDRESS"));	    //5
			vpd.put("var6", varOList.get(i).getString("OFFICE_DETAIL"));	    //6
			vpd.put("var7", varOList.get(i).get("TEAM_COUNT").toString());	//7
			vpd.put("var8", varOList.get(i).get("IPO_STAGE").toString());	//8
			vpd.put("var9", varOList.get(i).getString("PROJECT_DESC"));	    //9
			vpd.put("var10", varOList.get(i).getString("PROJECT_DESC_DETAIL"));	    //10
			vpd.put("var11", varOList.get(i).getString("PUBLICITY_VIDEO"));	    //11
			vpd.put("var12", varOList.get(i).getString("COVER_PICTURE"));	    //12
			vpd.put("var13", varOList.get(i).getString("RICH_TEXT"));	    //13
			vpd.put("var14", varOList.get(i).get("INVEST_COIN_TYPE").toString());	//14
			vpd.put("var15", varOList.get(i).get("INVEST_LIMIT").toString());	//15
			vpd.put("var16", varOList.get(i).getString("INVEST_AMOUNT"));	    //16
			vpd.put("var17", varOList.get(i).getString("REPAY_DESC"));	    //17
			vpd.put("var18", varOList.get(i).get("QUOTA_LIMIT").toString());	//18
			vpd.put("var19", varOList.get(i).get("SUPPORT_LIMIT").toString());	//19
			vpd.put("var20", varOList.get(i).get("IS_FREE_DELIVER").toString());	//20
			vpd.put("var21", varOList.get(i).get("REPAY_PERIOD").toString());	//21
			vpd.put("var22", varOList.get(i).get("ICO_PERIOD").toString());	//22
			vpd.put("var23", varOList.get(i).get("TARGET_LIMIT").toString());	//23
			vpd.put("var24", varOList.get(i).get("TARGET_TYPE").toString());	//24
			vpd.put("var25", varOList.get(i).getString("INVEST_RATE"));	    //25
			vpd.put("var26", varOList.get(i).getString("INVEST_COUNT"));	    //26
			vpd.put("var27", varOList.get(i).getString("TARGET_AMOUNT"));	    //27
			vpd.put("var28", varOList.get(i).getString("CONTACT_NAME"));	    //28
			vpd.put("var29", varOList.get(i).getString("JOB_POSITION"));	    //29
			vpd.put("var30", varOList.get(i).getString("CONTACT_TEL"));	    //30
			vpd.put("var31", varOList.get(i).getString("START_TIME"));	    //31
			vpd.put("var32", varOList.get(i).getString("END_TIME"));	    //32
			vpd.put("var33", varOList.get(i).getString("CREATE_TIME"));	    //33
			vpd.put("var34", varOList.get(i).get("PROJECT_STATE").toString());	//34
			vpd.put("var35", varOList.get(i).getString("MIN_INVESTMENT_AMOUNT"));	    //35
			vpd.put("var36", varOList.get(i).getString("MAX_INVESTMENT_AMOUNT"));	    //36
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
