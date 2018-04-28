package com.ecochain.base;


import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.ecochain.wallet.controller.AjaxResponse;
import com.ecochain.wallet.entity.Page;
import com.ecochain.wallet.entity.PageData;


public class BaseWebService {
	
    private Logger logger = LoggerFactory.getLogger(getClass());

	private static final long serialVersionUID = 6357869213649815390L;
	protected ModelAndView mv = this.getModelAndView();
	protected AjaxResponse ar = new AjaxResponse();
	protected PageData pd = new PageData();
	protected HttpServletResponse response;
	/**
	 * 得到PageData
	 */
	public PageData getPageData(){
		return new PageData(this.getRequest());
	}
	
	/**
	 * 得到ModelAndView
	 */
	public ModelAndView getModelAndView(){
		return new ModelAndView();
	}
	
	/**
	 * 得到request对象
	 */
	public HttpServletRequest getRequest() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		
		return request;
	}
	/**
	 * 设置response对象
	 */
	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}
	/**
	 * 得到32位的uuid
	 * @return
	 */
	public String get32UUID(){
		
		return UUID.randomUUID().toString().replace("-", "");
	}
	
	/**
	 * 得到分页列表的信息 
	 */
	public Page getPage(){
		
		return new Page();
	}
	
	public static void logBefore(Logger logger, String interfaceName){
		logger.info("");
		logger.info("start");
		logger.info(interfaceName);
	}
	
	public static void logAfter(Logger logger){
		logger.info("end");
		logger.info("");
	}

	public  AjaxResponse fastReturn(Object obj,boolean result,String msg,Short errorCode){
		ar.setData(obj);
		ar.setSuccess(result);
		ar.setMessage(msg);
		ar.setErrorCode(errorCode);
		return ar;
	}
	
}
