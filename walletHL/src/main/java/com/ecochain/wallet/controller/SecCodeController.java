package com.ecochain.wallet.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ecochain.util.ValidationCode;
import com.ecochain.util.ValidationCodeUtil;
import com.ecochain.util.ValidationCodeWrap;

/**
 * @author Liuhaihua
 *         前台使用<img  src="http://127.0.0.1:8080/code/vc" />
 */
@RequestMapping("/api/code")
@Api("图片验证码和短信")
@RestController
public class SecCodeController {
    
    /**
     * 获取CsrfToken
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "获取CsrfToken和SessionId", httpMethod = "GET", response = AjaxResponse.class, notes = "获取CsrfToken和SessionId")
    @RequestMapping(value = "getCsrfToken", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResponse getCsrfToken(HttpServletRequest request) {
             request.getSession().getAttributeNames();
//             CsrfToken csrfToken = (CsrfToken) request.getAttribute("_csrf");
             String sessionId=request.getSession().getId();
             Map<String,Object>  map =  new HashMap<String,Object>();
//             map.put("csrfToken", csrfToken);
             map.put("CSESSIONID", sessionId);
             map.put("code", 200);
             AjaxResponse response = AjaxResponse.success(null, map);

        return response;     
             
    }

    /**
     * 图片验证码生成
     *
     * @param request
     * @param response
     */
    @ApiOperation(value = "获取图片验证码", httpMethod = "GET", notes = "获取图片验证码，生成一个图片流")
    @RequestMapping(value = "vc", method = RequestMethod.GET)
    public void generatevc(HttpServletRequest request, HttpServletResponse response) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        //String code = drawImg(output);
        ValidationCodeWrap vcw = ValidationCodeUtil.getCode(10);
        ValidationCode code = vcw.getVc();
        HttpSession session = request.getSession();
        session.setAttribute("_validationCode", code);

        try {
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            response.setContentType("image/jpeg");
            ServletOutputStream out = response.getOutputStream();
            ImageIO.write(vcw.getImage(), "jpg", output);
            output.writeTo(out);
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 短信验证码
     *
     * @param mobile
     * @param request
     * @param response
     *//*
    @ApiOperation(value = "发送短信", httpMethod = "POST", notes = "通过短信网关发送文件")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "mobile", value = "手机号", dataType = "String"),
            @ApiImplicitParam(paramType = "path", name = "vcode", value = "图像验证码", dataType = "String"),
            @ApiImplicitParam(paramType = "path", name = "type", value = "发送验证码类型1-注册 2忘记密码 3修改手机号4重置密码", dataType = "String")
    })
    @RequestMapping(value = "sms/{mobile}/{vcode}/{type}", method = RequestMethod.POST)
    public ResultDto<String> generatesms(@PathVariable String mobile,@PathVariable String vcode, @PathVariable Integer type, HttpServletRequest request, HttpServletResponse response) {
    	 ResultDto<String> resultDto  = null; 
    	 if(checkisopenval(vcode)){//自动化测试需要加一个万能的验证吗
	    	 if (!"true".equalsIgnoreCase(devModel)) {
		    	 ValidationCode sessionVcode = (ValidationCode) request.getSession().getAttribute("_validationCode");
		         if (null == sessionVcode || !sessionVcode.validate(vcode)) {
		        	 resultDto  =  new  ResultDto<String>("500","图像验证码输入有误或者验证码过期");
		        	 return resultDto;
		         }
	    	 }
    	 }
    	 if(!mobile.matches(RegexpConstant.MOBILE_PATTERN)){
    		 resultDto  =  new  ResultDto<String>("500","手机格式不正确");
        	 return resultDto;
    	 }
    	ValidationCodeWrap vcw = ValidationCodeUtil.createCode(mobile, 10);
        ValidationCode code = vcw.getVc();
        HttpSession session = request.getSession();
        *//**
         * 接入短信平台发送短信验证码
         *//*
        String content = "";
        switch (type) {
            case 1:
            	RegDto regDto =userAPIService.checkIsReg(mobile);
            	if(regDto.isFlag()){
	            	 resultDto  = new ResultDto<String>("500","该手机号已注册,请直接登录！");
	            	 return resultDto;
           	    }
                content = message.getMessage("sms_reg", new Object[]{code.getCode()});
                break;
            case 2:
            	RegDto regDto1 =userAPIService.checkIsReg(mobile);
            	if(!regDto1.isFlag()){
	            	 resultDto  = new ResultDto<String>("500","此账户没有权限！");
	            	 return resultDto;
            	}
                content = message.getMessage("sms_forgetpassword", new Object[]{code.getCode()});
                break;
            case 3:
                content = message.getMessage("sms_updateMobile", new Object[]{code.getCode()});
                break;
            case 4:
                content = message.getMessage("sms_resetpassword", new Object[]{code.getCode()});
                break;
            default:
                break;
        }
        ismsapiService.send(mobile, content);
//        LOG.error("短信验证码：" + code.getCode());
        session.setAttribute("_SMSCode", code);
        if (!"true".equalsIgnoreCase(devModel)) {
	        //移除图像验证吗
	        request.getSession().removeAttribute("_validationCode");
        }
        resultDto = ResultDto.addOperationSuccess();
        return resultDto;
    }
    *//**
     * 校验是否开启校验
     * @param vcode
     * @return
     *//*
    public  boolean  checkisopenval(String vcode){
    	boolean  flag = true;
    	if(vcode.equals("8888")){
    		flag = false;
    	}
    	if(login_allow_type.contains("3")){
    		flag = false;
    	}
    	return flag;
    }*/
    
}
