package com.fh.controller.news.news;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.fh.controller.base.BaseController;
import com.fh.entity.Page;
import com.fh.service.news.news.NewsManager;
import com.fh.service.system.dictionaries.DictionariesManager;
import com.fh.util.AppUtil;
import com.fh.util.Const;
import com.fh.util.DateUtil;
import com.fh.util.DbFH;
import com.fh.util.DelAllFile;
import com.fh.util.FileUpload;
import com.fh.util.Jurisdiction;
import com.fh.util.ObjectExcelView;
import com.fh.util.PageData;
import com.fh.util.PathUtil;
import com.fh.util.Tools;

/** 
 * 说明：新闻管理
 * 创建人：FH Q313596790
 * 创建时间：2017-08-09
 */
@Controller
@RequestMapping(value="/news")
public class NewsController extends BaseController {
	
	String menuUrl = "news/list.do"; //菜单地址(权限用)
	@Resource(name="newsService")
	private NewsManager newsService;
	@Resource(name="dictionariesService")
	private DictionariesManager dictionariesService;
	
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/save")
	public ModelAndView save(HttpServletRequest request,
			@RequestParam(value="PICTURE_URL",required=false) MultipartFile file,
			String TITLE,
			String NEWS_TYPE,
			String LABEL,
			String CONTENT,
			String SUBTITLE
			) throws Exception{
		System.out.println("CONTENT:"+CONTENT);
		logBefore(logger, Jurisdiction.getUsername()+"新增News");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("TITLE", TITLE);
		pd.put("NEWS_TYPE", NEWS_TYPE);
		pd.put("LABEL", LABEL);
		pd.put("SUBTITLE", SUBTITLE);
		String  ffile = DateUtil.getDays(), fileName = "";
		if (null != file && !file.isEmpty()) {
			fileName = FileUpload.fileUp(file,  (String)DbFH.getPprVue().get("pload.filePath")+ File.separatorChar + ffile, this.get32UUID());				
			pd.put("PICTURE_NAME", fileName);
			pd.put("PICTURE_URL", (String)DbFH.getPprVue().get("pload.filePath.Url") + ffile +  File.separatorChar + fileName);
		}else{
			System.out.println("上传失败");
		}
		String fileUrl = (String)DbFH.getPprVue().get("pload.filePath.richText");
		//String dbpath = pros.getProperty("dbpath");		
		pd.put("id", this.get32UUID());	//主键
		File fileContentUrl = new File(fileUrl + File.separatorChar + ffile
	                + File.separatorChar
	                + pd.getString("id"));
		pd.put("RICH_TEXT", fileContentUrl.getPath());
		System.out.println("CONTENT:"+CONTENT);
		try {  // 写入文件
            FileUtils.write(fileContentUrl, CONTENT);
        } catch (IOException e) {
            e.printStackTrace();
           
        }
		
		newsService.save(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"删除News");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return;} //校验权限
		PageData pd = new PageData();
		pd = this.getPageData();
		newsService.delete(pd);
		out.write("success");
		out.close();
	}
	
	/**删除图片
	 * @param out
	 * @throws Exception 
	 */
	@RequestMapping(value="/editPictureUrl")
	public void editPictureUrl(PrintWriter out) throws Exception {
		PageData pd = new PageData();
		pd = this.getPageData();
		String PATH = pd.getString("PICTURE_URL");	
		if(Tools.notEmpty(PATH.trim())){//图片路径
			DelAllFile.delFolder((String)DbFH.getPprVue().get("pload.filePath")+ File.separatorChar + PATH.substring(PATH.lastIndexOf("/")-8, PATH.length())); 	//删除图片
		}
		if(PATH != null){
			pd.put("PICTURE_URL", "");
			newsService.edit(pd);															//删除数据库中图片数据
		}	
		out.write("success");
		out.close();
	}
	
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit(HttpServletRequest request,
			@RequestParam(value="PICTURE_URL",required=false) MultipartFile file,
			String TITLE,
			String NEWS_TYPE,
			String LABEL,
			String CONTENT,
			String id,
			String  SUBTITLE
			) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改News");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("TITLE", TITLE);
		pd.put("NEWS_TYPE", NEWS_TYPE);
		pd.put("LABEL", LABEL);
		pd.put("id", id);
		pd.put("SUBTITLE", SUBTITLE);
		String  ffile = DateUtil.getDays(), fileName = "";
		if (null != file && !file.isEmpty()) {
			fileName = FileUpload.fileUp(file,  (String)DbFH.getPprVue().get("pload.filePath")+ File.separatorChar + ffile, this.get32UUID());				
			pd.put("PICTURE_NAME", fileName);
			pd.put("PICTURE_URL", (String)DbFH.getPprVue().get("pload.filePath.Url") + ffile +  File.separatorChar + fileName);
		}else{
			System.out.println("上传失败");
		}
		
		String fileUrl = (String)DbFH.getPprVue().get("pload.filePath.richText");
		//String dbpath = pros.getProperty("dbpath");		
		File fileContentUrl = new File(fileUrl + File.separatorChar + ffile
	                + File.separatorChar
	                + pd.getString("id"));
		pd.put("RICH_TEXT", fileContentUrl.getPath());
		System.out.println("CONTENT:"+CONTENT);
		try {  // 写入文件
            FileUtils.write(fileContentUrl, CONTENT);
        } catch (IOException e) {
            e.printStackTrace();
        }
		
		newsService.edit(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"列表News");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String keywords = pd.getString("keywords");				//关键词检索条件
		if(null != keywords && !"".equals(keywords)){
			pd.put("keywords", keywords.trim());
		}
		page.setPd(pd);
		List<PageData>	varList = newsService.list(page);	//列出News列表
		mv.setViewName("news/news/news_list");
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
		pd.put("group_id", "0d3d9c9ead8e4dbdbfcdfc56cba10024");//新闻类型
		List<PageData>	newsTypes = dictionariesService.listAllFromGeneralCode(pd); //查询字典表 system_general_code
			
		mv.addObject("newsTypes", newsTypes);
		mv.setViewName("news/news/news_edit");
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
		pd = newsService.findById(pd);	//根据ID读取
		
		//读取富文本
        File file = new File(pd.getString("RICH_TEXT"));
        try {
            String richText = FileUtils.readFileToString(file);
            System.out.println("richText:"+richText);
            pd.put("CONTENT", richText);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        
        pd.put("group_id", "0d3d9c9ead8e4dbdbfcdfc56cba10024");//新闻类型
		List<PageData>	newsTypes = dictionariesService.listAllFromGeneralCode(pd); //查询字典表 system_general_code
		
		mv.addObject("newsTypes", newsTypes);
		mv.setViewName("news/news/news_edit");
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
		logBefore(logger, Jurisdiction.getUsername()+"批量删除News");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(null != DATA_IDS && !"".equals(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			newsService.deleteAll(ArrayDATA_IDS);
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
		logBefore(logger, Jurisdiction.getUsername()+"导出News到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("标题");	//1
		titles.add("图片路径");	//2
		titles.add("图片名称");	//3
		titles.add("富文本路径");	//4
		titles.add("浏览数");	//5
		titles.add("新闻类型");	//6
		titles.add("标签");	//7
		titles.add("开始时间");	//8
		titles.add("修改时间");	//9
		dataMap.put("titles", titles);
		List<PageData> varOList = newsService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("TITLE"));	    //1
			vpd.put("var2", varOList.get(i).getString("PICTURE_URL"));	    //2
			vpd.put("var3", varOList.get(i).getString("PICTURE_NAME"));	    //3
			vpd.put("var4", varOList.get(i).getString("RICH_TEXT"));	    //4
			vpd.put("var5", varOList.get(i).get("BROWSE_COUNT").toString());	//5
			vpd.put("var6", varOList.get(i).getString("NEWS_TYPE"));	    //6
			vpd.put("var7", varOList.get(i).getString("LABEL"));	    //7
			vpd.put("var8", varOList.get(i).getString("CREATE_TIME"));	    //8
			vpd.put("var9", varOList.get(i).getString("UPDATE_TIME"));	    //9
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
