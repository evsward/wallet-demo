package com.ecochain.wallet.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ecochain.annotation.LoginVerify;
import com.ecochain.base.BaseWebService;
import com.ecochain.constant.CodeConstant;
import com.ecochain.core.config.CoreMessageSource;
import com.ecochain.util.HttpUtil;
import com.ecochain.util.InternetUtil;
import com.ecochain.util.MD5Util;
import com.ecochain.util.Validator;
import com.ecochain.wallet.entity.SystemSendVcode;
import com.ecochain.wallet.entity.UserLogin;
import com.ecochain.wallet.entity.UserWallet;
import com.ecochain.wallet.entity.UsersDetails;
import com.ecochain.wallet.service.EmailService;
import com.ecochain.wallet.service.SendVodeService;
import com.ecochain.wallet.service.SysGencodeService;
import com.ecochain.wallet.service.UserLoginService;
import com.ecochain.wallet.service.UsersDetailsService;

/*
 * 总入口
 */
@RestController
@RequestMapping(value = "/api/user")
@Api(value = "用户Service")
public class UsersWebService extends BaseWebService {
    
    private Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private UserLoginService userLoginService;
    
    @Autowired
    private UsersDetailsService usersDetailsService;
    
    @Autowired
    private SendVodeService sendVodeService;
    
    @Autowired
    private SysGencodeService sysGencodeService;
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private CoreMessageSource messageSource;
    
    /**
     * 前端请求登录，验证用户
     */
    @PostMapping("/login")
    @ApiOperation(nickname = "ICO登录接口", value = "ICO用户登录", notes = "参数说明：account：用户名或手机号登录，password：密码，source：来源，app登录时填APP，否则不填")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "userLogin", value = "登录参数", required = true, paramType = "body", dataType = "UserLogin")
    })
    public AjaxResponse htmlLogin(HttpServletRequest request,@RequestBody UserLogin userLogin){
        Map<String,Object> data  = new HashMap<String,Object>();
        try {
            String account = StringUtils.isEmpty(userLogin.getAccount())?null:userLogin.getAccount().trim();
            String password = StringUtils.isEmpty(userLogin.getPassword())?null:userLogin.getPassword().trim();
            if(StringUtils.isEmpty(account)){
                return AjaxResponse.falied(messageSource.getMessage("InputAccount"), CodeConstant.USER_NO_EXISTS);
            }
            if(StringUtils.isEmpty(password)){
                return AjaxResponse.falied(messageSource.getMessage("InputPassword"), CodeConstant.ERROE_PASSWORD_NULL);
            }
            password = MD5Util.getMd5Code(password);
            UserLogin tuserLogin = usersDetailsService.getUserByAccAndPass(account, password);
            if(tuserLogin != null){
                if("1".equals(tuserLogin.getStatus())){
                    String lastloginIp = InternetUtil.getRemoteAddr(request);
                    long lastlogin_port = InternetUtil.getRemotePort(request);
                    usersDetailsService.updateLoginTimeById(tuserLogin.getId(), lastloginIp, lastlogin_port);
                    Map userInfo = usersDetailsService.getUserInfoByUserId(tuserLogin.getUserId());
                    request.getSession().setAttribute("User", userInfo);
                    request.getSession().setAttribute("currentUserId", tuserLogin.getUserId());
                    String sessionId = request.getSession().getId();
                    data.put("sessionId", sessionId);
                    data.put("user_name", userInfo.get("user_name"));
                    data.put("address", userInfo.get("address"));
                    
                    return AjaxResponse.success(messageSource.getMessage("LoginSuccess"), data);
                }else{
                    return AjaxResponse.falied(messageSource.getMessage("YouInBlackList"), CodeConstant.ERROR_BLACK);
                }
            }else{
                return AjaxResponse.falied("账号或密码有误！", CodeConstant.USERNAMEORPWD_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResponse.falied(messageSource.getMessage("sysException"), CodeConstant.SYS_ERROR);
        }
    }
    
	/**
     * @describe:忘记密码
     * @author: zhangchunming
     * @date: 2016年10月26日下午1:40:57
     * @param request
     * @param response
     * @return: AjaxResponse
     */
    @PostMapping("/forgetpwd")
    @ApiOperation(nickname = "忘记密码", value = "忘记密码，输入新密码", notes = "忘记密码，输入新密码！")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "userLogin", value = "忘记密码参数", required = true, paramType = "body", dataType = "UserLogin")
    })
    public AjaxResponse forgetpwd(HttpServletRequest request,@RequestBody UserLogin userLogin){
        AjaxResponse ar = new AjaxResponse();
        try {
            String account  = userLogin.getAccount();
            String password  = userLogin.getPassword();
            String cfPassWord  = userLogin.getCfPassWord();
            String vcode  = userLogin.getVcode();
            if(StringUtils.isEmpty(account)){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("InputAccount"));
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            /*if(!Validator.isMobile(phone)){
                ar.setSuccess(false);
                ar.setMessage("手机号为11位数字，请重新输入");
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }*/
            if(StringUtils.isEmpty(vcode.trim())){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("smsVcodeNull"));
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            if(StringUtils.isEmpty(password)||StringUtils.isEmpty(cfPassWord)){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("passwordNull"));
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            } 
            if(password.length()<6||password.length()>16){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("PasswordLengthError"));
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            if(!Validator.isPasswordByLetterAndNum(password)){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("PasswordLengthError"));
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }

            if(!password.equals(cfPassWord)){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("twoPasswordError"));
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            if(!userLoginService.findIsExist(account)){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("accountNotExist"));
                ar.setErrorCode(CodeConstant.USER_NO_EXISTS);
                return ar;
            }
            
            
            //半小时之内的短信验证码有效
            String tVcode = sendVodeService.findVcodeByPhone(userLogin.getAccount()); 
            if(tVcode ==null||!vcode.trim().equals(tVcode.trim())){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("smsVcodeError"));
                ar.setErrorCode(CodeConstant.ERROR_VCODE);
                return ar;
            }
            if(!userLoginService.modifyPwd(account, MD5Util.getMd5Code(password))){         
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("setPasswordFailure"));
                ar.setErrorCode(CodeConstant.UPDATE_FAIL);
                return ar;
            }
                ar.setSuccess(true);
                ar.setMessage(messageSource.getMessage("setPasswordSuccess"));
                return ar;
            
        } catch (Exception e) {
            e.printStackTrace();
            ar.setSuccess(false);
            ar.setMessage(messageSource.getMessage("sysException"));
            ar.setErrorCode(CodeConstant.SYS_ERROR);
        }
        return ar;
    }
    /**
     * @describe:获取用户信息
     * @author: zhangchunming
     * @date: 2017年7月28日下午9:03:19
     * @param request
     * @return: AjaxResponse
     */
    @LoginVerify
    @PostMapping("/getUserInfo")
    @ApiOperation(nickname = "获取用户基本信息", value = "获取用户基本信息", notes = "获取用户基本信息")
    @ApiImplicitParams({
    })
    public AjaxResponse getUserInfo(HttpServletRequest request){
        try {
            Map user = (Map) request.getSession().getAttribute("User");
            Map userInfo = usersDetailsService.getUserInfoByUserId((Integer)(user.get("user_id")));
            return AjaxResponse.success(messageSource.getMessage("getSuccess"), userInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResponse.falied(messageSource.getMessage("infoError"));
        }
    }
    
    /**
     * @describe:设置交易密码
     * @author: zhangchunming
     * @date: 2017年5月31日下午7:53:07
     * @param request
     * @return: AjaxResponse
     */
    @LoginVerify
    @PostMapping("/setTransPassword")
    @ApiOperation(nickname = "设置交易密码", value = "设置交易密码", notes = "参数说明：  transPassword：交易密码  cfPassword：确认密码  vcode：验证码")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "usersDetails", value = "交易密码", required = true, paramType = "body", dataType = "UsersDetails")
    })
    public AjaxResponse setTransPassword(HttpServletRequest request,@RequestBody @Valid UsersDetails usersDetails){
        AjaxResponse ar = new AjaxResponse();
        try {
            Map user = (Map) request.getSession().getAttribute("User");
            
            if(StringUtils.isEmpty(usersDetails.getTransPassword())){
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                ar.setMessage(messageSource.getMessage("transPasswordNull"));
                ar.setSuccess(false);
                return ar;
            }
            
            if(StringUtils.isEmpty(usersDetails.getCfPassword())){
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                ar.setMessage(messageSource.getMessage("confirmPasswordNull"));
                ar.setSuccess(false);
                return ar;
            }
            
            if(!usersDetails.getTransPassword().equals(usersDetails.getCfPassword())){
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                ar.setMessage(messageSource.getMessage("twoPasswordError"));
                ar.setSuccess(false);
                return ar;
            }
            if(StringUtils.isEmpty(usersDetails.getVcode())){//短信验证码
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("smsVcodeNull"));
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            
            //半小时之内的短信验证码有效
            String tVcode =sendVodeService.findVcodeByPhone(String.valueOf(user.get("mobile_phone"))); 
            if(tVcode==null||!usersDetails.getVcode().equals(tVcode)){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("smsVcodeError"));
                ar.setErrorCode(CodeConstant.ERROR_VCODE);
                return ar;
            }
            /*if(tVcode==null){
                ar.setSuccess(false);
                ar.setMessage("验证码已失效，请重新获取");
                ar.setErrorCode(CodeConstant.OVERTIME_VCODE);
                return ar;
            }else if(!usersDetails.getVcode().equalsIgnoreCase(tVcode)){
                ar.setSuccess(false);
                ar.setMessage("验证码错误，请重新输入");
                ar.setErrorCode(CodeConstant.ERROR_VCODE);
                return ar;
            }*/
            String transPassword = MD5Util.getMd5Code(usersDetails.getTransPassword());
            UserLogin userLogin = userLoginService.getUserLoginByUserId(String.valueOf(user.get("user_id")));
            if(transPassword.equals(userLogin.getPassword())){
            	return AjaxResponse.falied(messageSource.getMessage("transPassAndPassWordNotSame"), CodeConstant.PARAM_ERROR);
            }
            
            boolean setTransPassword = usersDetailsService.setTransPassword(String.valueOf(user.get("user_id")),transPassword);
            if(setTransPassword){
                ar.setSuccess(true);
                ar.setMessage(messageSource.getMessage("setTransPasswordSuccess"));
                return ar;
            }else{
                ar.setSuccess(false);
                ar.setErrorCode(CodeConstant.UPDATE_FAIL);
                ar.setMessage(messageSource.getMessage("setTransPasswordFailure"));
                return ar;
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            ar.setSuccess(false);
            ar.setMessage(messageSource.getMessage("sysException"));
            ar.setErrorCode(CodeConstant.SYS_ERROR);
        }
        return ar;
    }
    
    
    /**
     * @describe:发送验证码
     * @author: zhangchunming
     * @date: 2017年7月18日下午8:37:44
     * @param request
     * @param response
     * @return: AjaxResponse
     */
    @PostMapping("/sendVcode/{phone:.+}")//:.+ @PathVariable出现点号&quot;.&quot;时导致路径参数截断获取不全的解决办法
    @ApiOperation(nickname = "发送短信验证码", value = "发送短信验证码", notes = "发送短信验证码")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "phone", value = "手机号", required = true, paramType = "path", dataType = "String")
    })
    public AjaxResponse sendVcode(HttpServletRequest request,@PathVariable("phone") String phone){
        AjaxResponse ar = new AjaxResponse();
        try {
            
            phone = StringUtils.isEmpty(phone)?null:phone.trim();
            if(StringUtils.isEmpty(phone)){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("inputEmailAccount"));
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            /*if(Validator.isMobile(phone)){
                ar.setSuccess(false);
                ar.setMessage("手机号有误！");
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }*/
            List<Map<String, String>> codeList = sysGencodeService.findByGroupCode("SENDSMS_FLAG");
            String smsflag ="";
            String sms_day_num = "";
            String sms_half_hour_num = "";
            for(Map<String, String> mapObj:codeList){
                if("SENDSMS_FLAG".equals(mapObj.get("code_name"))){
                    smsflag = mapObj.get("code_value").toString();
                    logger.info("---------发送验证码--------短信发送标识smsflag："+smsflag);
                }else if("SMS_DAY_NUM".equals(mapObj.get("code_name"))){
                    sms_day_num = mapObj.get("code_value").toString();
                    logger.info("---------发送验证码--------短信每天限量sms_day_num："+sms_day_num);
                }else if("SMS_HALF_HOUR_NUM".equals(mapObj.get("code_name"))){
                    sms_half_hour_num = mapObj.get("code_value").toString();
                    logger.info("---------发送验证码--------短信半小时限量sms_half_hour_num："+sms_half_hour_num);
                }
            }
            if(StringUtils.isEmpty(smsflag)||StringUtils.isEmpty(sms_day_num)||StringUtils.isEmpty(sms_half_hour_num)){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("emailSystemError"));
                ar.setErrorCode(CodeConstant.SYS_ERROR);
                return ar;
            }
//            pd.put("sms_day_num", sms_day_num);
            //判断短信发送量是否超出限制
            Integer findCountBy30Minute = sendVodeService.findCountBy30Minute(phone);
            if(findCountBy30Minute>=Integer.valueOf(sms_half_hour_num)){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("sendingFrequent"));
                ar.setErrorCode(CodeConstant.ERROE_SMS_FREQUENTLY);
                return ar;
            }
            Integer findCountByDay = sendVodeService.findCountByDay(phone);
            if(findCountByDay>=Integer.valueOf(sms_day_num)){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("overrunTheUpperLimit"));
                ar.setErrorCode(CodeConstant.ERROE_SMS_OVER);
                return ar;
            }
            String vCode = "";//验证码
            if("0".equals(smsflag)){
                vCode = "888888";
            }else{
                vCode = (int)((Math.random()*9+1)*100000)+"";
                
                String title = messageSource.getMessage("registeEmailTitle");
                
                String msg = messageSource.getMessage("registeEmailContne",vCode);
                
                Map<String, Object> content = new HashMap<String, Object>();
                
                content.put("content", msg);
                
                boolean sendSms = emailService.sendTemplateMail(phone, title, 
                        content, "commomTemplate");//发送成功，验证码插入数据库
                if(!sendSms){
                    ar.setSuccess(false);
                    ar.setMessage(messageSource.getMessage("sendCodeError"));
                    ar.setErrorCode(CodeConstant.SMS_GET_ERROR);
                    return ar;
                }
                
            }
            SystemSendVcode systemSendVcode = new SystemSendVcode();
            systemSendVcode.setVcode(vCode);
            systemSendVcode.setPhone(phone);
            systemSendVcode.setSmsType("6");//注册ICO
            if(!sendVodeService.addVcode(systemSendVcode)){//验证码进库
                logger.info("addVcode fail ,phone is "+phone +"!");             
            }
            ar.setSuccess(true);
            ar.setMessage(messageSource.getMessage("sendCodeSuccess"));
            return ar;
            
        } catch (Exception e) {
            e.printStackTrace();
            ar.setSuccess(false);
            ar.setMessage(messageSource.getMessage("sysException"));
            ar.setErrorCode(CodeConstant.SYS_ERROR);
        }
        return ar;
    }
    
    
    /**
     * @describe:发送验证码
     * @author: zhangchunming
     * @date: 2017年7月18日下午8:37:44
     * @param request
     * @param response
     * @return: AjaxResponse
     */
    @LoginVerify
    @PostMapping("/sendVcode1/{smsType:.+}")//:.+ @PathVariable出现点号&quot;.&quot;时导致路径参数截断获取不全的解决办法
    @ApiOperation(nickname = "发送短信验证码", value = "发送短信验证码", notes = "发送短信验证码")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "smsType", value = "业务类型：1-更新密码2-更新交易密码3-设置交易密码4-找回交易密码5-修改手机号", required = true, paramType = "path", dataType = "String")
    })
    public AjaxResponse sendVcode1(HttpServletRequest request,@PathVariable("smsType")String smsType){
        AjaxResponse ar = new AjaxResponse();
        try {
            System.out.print(request.getHeader("x-auth-token"));
            Map user = (Map) request.getSession().getAttribute("User");
            String phone = String.valueOf(user.get("mobile_phone"));
            if(StringUtils.isEmpty(smsType)){
                AjaxResponse.falied("请输入短信业务类型", CodeConstant.PARAM_ERROR);
            }
            
            List<Map<String, String>> codeList = sysGencodeService.findByGroupCode("SENDSMS_FLAG");
            String smsflag ="";
            String sms_day_num = "";
            String sms_half_hour_num = "";
            String sms_type = "";
            for(Map<String, String> mapObj:codeList){
                if("SENDSMS_FLAG".equals(mapObj.get("code_name"))){
                    smsflag = mapObj.get("code_value").toString();
                    logger.info("---------发送验证码--------短信发送标识smsflag："+smsflag);
                }else if("SMS_DAY_NUM".equals(mapObj.get("code_name"))){
                    sms_day_num = mapObj.get("code_value").toString();
                    logger.info("---------发送验证码--------短信每天限量sms_day_num："+sms_day_num);
                }else if("SMS_HALF_HOUR_NUM".equals(mapObj.get("code_name"))){
                    sms_half_hour_num = mapObj.get("code_value").toString();
                    logger.info("---------发送验证码--------短信半小时限量sms_half_hour_num："+sms_half_hour_num);
                }else if("SMS_TYPE".equals(mapObj.get("code_name"))){
                    sms_type = mapObj.get("code_value").toString();
                    logger.info("---------发送验证码--------短信业务类型sms_type："+sms_type);
                }
            }
            
            if(!sms_type.contains(smsType)||smsType.length()!=1){
                return AjaxResponse.falied("短信业务类型有误！", CodeConstant.PARAM_ERROR);
            }
            if(StringUtils.isEmpty(smsflag)||StringUtils.isEmpty(sms_day_num)||StringUtils.isEmpty(sms_half_hour_num)){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("emailSystemError"));
                ar.setErrorCode(CodeConstant.SYS_ERROR);
                return ar;
            }
            //判断短信发送量是否超出限制
            Integer findCountBy30Minute = sendVodeService.findCountBy30Minute(phone);
            if(findCountBy30Minute>=Integer.valueOf(sms_half_hour_num)){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("sendingFrequent"));
                ar.setErrorCode(CodeConstant.ERROE_SMS_FREQUENTLY);
                return ar;
            }
            Integer findCountByDay = sendVodeService.findCountByDay(phone);
            if(findCountByDay>=Integer.valueOf(sms_day_num)){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("overrunTheUpperLimit"));
                ar.setErrorCode(CodeConstant.ERROE_SMS_OVER);
                return ar;
            }
            String vCode = "";//验证码
            String title = "";//发送内容
            if("0".equals(smsflag)){
                vCode = "888888";
            }else{
                String msg = "";
                vCode = (int)((Math.random()*9+1)*100000)+"";
                switch (smsType) {
                case "1":
                    title = messageSource.getMessage("sendVcodeTitle_1");
                    msg = messageSource.getMessage("sendVcodeContent_1",vCode);
                    break;
                case "2":
                    title = messageSource.getMessage("sendVcodeTitle_2");   
                    msg = messageSource.getMessage("sendVcodeContent_2",vCode);
                    break;
                case "3":
                    title = messageSource.getMessage("sendVcodeTitle_3");
                    msg = messageSource.getMessage("sendVcodeContent_3",vCode);
                    break;
                case "4":
                    title = messageSource.getMessage("sendVcodeTitle_4");
                    msg = messageSource.getMessage("sendVcodeContent_4",vCode);
                    break;
                case "5":
                    title = messageSource.getMessage("sendVcodeTitle_5");
                    msg = messageSource.getMessage("sendVcodeContent_5",vCode);
                    break;
                case "6":
                    title = messageSource.getMessage("sendVcodeTitle_6");
                    msg = messageSource.getMessage("sendVcodeContent_6",vCode);
                    break;
                case "7":
                    title = messageSource.getMessage("sendVcodeTitle_7");
                    msg = messageSource.getMessage("sendVcodeContent_7",vCode);
                    break;
                case "8":
                    title = messageSource.getMessage("sendVcodeTitle_8");
                    msg = messageSource.getMessage("sendVcodeContent_8",vCode);
                    break;
                default:
                    break;
                }

                Map<String, Object> content = new HashMap<String, Object>();
                
                content.put("content", msg);
                content.put("vCode", vCode);
                logger.info("Email content is ->"+content);

                boolean sendSms = emailService.sendTemplateMail(phone, title, 
                        content, "commomTemplate");//发送成功，验证码插入数据库
                if(!sendSms){
                    ar.setSuccess(false);
                    ar.setMessage(messageSource.getMessage("sendCodeError"));
                    ar.setErrorCode(CodeConstant.SMS_GET_ERROR);
                    return ar;
                }
                
            }
            SystemSendVcode systemSendVcode = new SystemSendVcode();
            systemSendVcode.setVcode(vCode);
            systemSendVcode.setPhone(phone);
            systemSendVcode.setSmsType(smsType);
            if(!sendVodeService.addVcode(systemSendVcode)){//验证码进库
                logger.info("addVcode fail ,phone is "+phone +"!");             
            }
            ar.setSuccess(true);
            ar.setMessage(messageSource.getMessage("sendCodeSuccess"));
            return ar;
            
        } catch (Exception e) {
            e.printStackTrace();
            ar.setSuccess(false);
            ar.setMessage(messageSource.getMessage("sysException"));
            ar.setErrorCode(CodeConstant.SYS_ERROR);
        }
        return ar;
    }
    
    /**
     * @describe:根据账号查询
     * @author: zhangchunming
     * @date: 2016年12月6日下午3:14:51
     * @param request
     * @param response
     * @return: AjaxResponse
     */
    
    @PostMapping("/sendVcodeByAccount/{account:.+}")//:.+ @PathVariable出现点号&quot;.&quot;时导致路径参数截断获取不全的解决办法
    @ApiOperation(nickname = "忘记密码发送短信验证码", value = "忘记密码发送短信验证码", notes = "忘记密码发送短信验证码")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "account", value = "登录账号", required = true, paramType = "path", dataType = "String")
    })
    public AjaxResponse sendVcodeByAccount(HttpServletRequest request,@PathVariable("account") String account){
        AjaxResponse ar = new AjaxResponse();
        try {
            account = account ==null?"":account.trim();
            if(StringUtils.isEmpty(account)){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("inputEmailAccount"));
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            boolean findIsExist = userLoginService.IsExistByAccountOrUserName(account);
            if(!findIsExist){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("accountNotExist"));
                ar.setErrorCode(CodeConstant.USER_NO_EXISTS);
                return ar;
            }
            Map<String, Object> userInfo = userLoginService.getUserInfoByAccountOrUserName(account);
            String phone = String.valueOf(userInfo.get("mobile_phone")).trim();
            /*if(smsBlackService.isBlackPhone(phone, Constant.VERSION_NO)){
                ar.setSuccess(false);
                ar.setMessage("您已被加入短信黑名单！");
                ar.setErrorCode(CodeConstant.ERROR_SMS_BLACK);
                return ar;
            }*/
            
            List<Map<String, String>> codeList = sysGencodeService.findByGroupCode("SENDSMS_FLAG");
            String smsflag ="";
            String sms_day_num = "";
            String sms_half_hour_num = "";
            for(Map<String, String> mapObj:codeList){
                if("SENDSMS_FLAG".equals(mapObj.get("code_name"))){
                    smsflag = mapObj.get("code_value").toString();
                    logger.info("---------发送验证码--------短信发送标识smsflag："+smsflag);
                }else if("SMS_DAY_NUM".equals(mapObj.get("code_name"))){
                    sms_day_num = mapObj.get("code_value").toString();
                    logger.info("---------发送验证码--------短信每天限量sms_day_num："+sms_day_num);
                }else if("SMS_HALF_HOUR_NUM".equals(mapObj.get("code_name"))){
                    sms_half_hour_num = mapObj.get("code_value").toString();
                    logger.info("---------发送验证码--------短信半小时限量sms_half_hour_num："+sms_half_hour_num);
                }
            }
            if(StringUtils.isEmpty(smsflag)||StringUtils.isEmpty(sms_day_num)||StringUtils.isEmpty(sms_half_hour_num)){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("emailSystemError"));
                ar.setErrorCode(CodeConstant.SYS_ERROR);
                return ar;
            }
            //判断短信发送量是否超出限制
            Integer findCountBy30Minute = sendVodeService.findCountBy30Minute(phone);
            if(findCountBy30Minute>=Integer.valueOf(sms_half_hour_num)){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("sendingFrequent"));
                ar.setErrorCode(CodeConstant.ERROE_SMS_FREQUENTLY);
                return ar;
            }
            Integer findCountByDay = sendVodeService.findCountByDay(phone);
            if(findCountByDay>=Integer.valueOf(sms_day_num)){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("overrunTheUpperLimit"));
                ar.setErrorCode(CodeConstant.ERROE_SMS_OVER);
                return ar;
            }
            String vCode = "";//验证码
            if("0".equals(smsflag)){
                vCode = "888888";
            }else{
                vCode = (int)((Math.random()*9+1)*100000)+"";
                String title = messageSource.getMessage("forgetPasswordTitle");
                String msg = messageSource.getMessage("forgetPasswordContent",vCode);
                
                Map<String, Object> content = new HashMap<String, Object>();
                
                content.put("content", msg);
                
                boolean sendSms = emailService.sendTemplateMail(phone, title, 
                        content, "commomTemplate");//发送成功，验证码插入数据库
                if(!sendSms){
                    ar.setSuccess(false);
                    ar.setMessage(messageSource.getMessage("sendCodeError"));
                    ar.setErrorCode(CodeConstant.SMS_GET_ERROR);
                    return ar;
                }
                
            }
            SystemSendVcode systemSendVcode = new SystemSendVcode();
            systemSendVcode.setVcode(vCode);
            systemSendVcode.setPhone(phone);
            systemSendVcode.setSmsType("8");//注册合链钱包
            if(!sendVodeService.addVcode(systemSendVcode)){//验证码进库
                logger.info("addVcode fail ,phone is "+phone +"!");             
            }
            ar.setSuccess(true);
            ar.setMessage(messageSource.getMessage("sendCodeSuccess"));
            return ar;
            
        } catch (Exception e) {
            e.printStackTrace();
            ar.setSuccess(false);
            ar.setMessage(messageSource.getMessage("sysException"));
            ar.setErrorCode(CodeConstant.SYS_ERROR);
        }
        return ar;
    }
    
    /**
     * @describe:验证交易密码是否正确
     * @author: lisandro
     * @date: 2017年7月18日22:34:01
     * @param request
     * @return: AjaxResponse
     */
    @LoginVerify
    @PostMapping("/validTranPassword")
    @ApiOperation(nickname = "验证交易密码", value = "验证交易密码", notes = "验证交易密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "transPassword", value = "交易密码", required = true, paramType = "form", dataType = "String"),
    })
    public AjaxResponse validTranPassword(HttpServletRequest request)throws Exception{
        AjaxResponse ar = new AjaxResponse();
        try {
            Map user = (Map) request.getSession().getAttribute("User");
            if(StringUtils.isEmpty(request.getParameter("transPassword"))){
                ar.setMessage(messageSource.getMessage("transPasswordNull"));
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                ar.setSuccess(false);
                return ar;
            }
            String transPassword = MD5Util.getMd5Code(request.getParameter("transPassword"));
            System.out.println("transPassword="+transPassword);
            Boolean existTransPassword = usersDetailsService.isExistTransPassword(String.valueOf(user.get("user_id")),transPassword);
            if(!existTransPassword){
                ar.setMessage(messageSource.getMessage("transPassworError"));
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                ar.setSuccess(false);
                return ar;
            }else{
                ar.setMessage(messageSource.getMessage("transPassworCorrect"));
                ar.setSuccess(true);
                return ar;
            }
            
        }catch (Exception e){
            e.printStackTrace();
            ar.setSuccess(false);
            ar.setMessage(messageSource.getMessage("sysException"));
            ar.setErrorCode(CodeConstant.SYS_ERROR);
        }
        return ar;
    }

    /**
     * @describe:修改交易密码
     * @author: zhangchunming
     * @date: 2016年12月19日上午10:22:35
     * @param request
     * @param response
     * @return: AjaxResponse
     */
    
    @LoginVerify
    @PostMapping("/updateTransPassword")
    @ApiOperation(nickname = "修改交易密码", value = "修改交易密码", notes = "参数说明：oldPassword：旧密码  newPassword：新密码  cfPassword：确认密码  vcode：验证码")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "usersDetails", value = "交易密码参数", required = true, paramType = "body", dataType = "UsersDetails")
    })
    public AjaxResponse updateTransPassword(HttpServletRequest request,@RequestBody @Valid UsersDetails usersDetails){
        logger.info("---------------修改交易密码-------start-------------");
        AjaxResponse ar = new AjaxResponse();
        try {
            JSONObject user = JSONObject.parseObject(JSON.toJSONString(request.getSession().getAttribute("User")));
            if(StringUtils.isEmpty(usersDetails.getOldPassword())){//原密码
                ar.setMessage(messageSource.getMessage("oldPasswordNull"));
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                ar.setSuccess(false);
                return ar;
            }
            if(StringUtils.isEmpty(usersDetails.getNewPassword())){//新密码
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("newPasswordNull"));
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            } 
            
            if(usersDetails.getOldPassword().length()<6||usersDetails.getNewPassword().length()>16){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("PasswordLengthError"));
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            
            if(!Validator.isAccountByLetterAndNum(usersDetails.getNewPassword())){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("PasswordLengthError"));
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            
            if(StringUtils.isEmpty(usersDetails.getCfPassword())){//确认密码
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("confirmPasswordNull"));
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            if(!usersDetails.getNewPassword().equals(usersDetails.getCfPassword())){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("twoPasswordError"));
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            
            if(StringUtils.isEmpty(usersDetails.getVcode())){//短信验证码
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("smsVcodeNull"));
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            
            //半小时之内的短信验证码有效
            String tVcode =sendVodeService.findVcodeByPhone(user.getString("mobile_phone")); 
            if(tVcode==null||!usersDetails.getVcode().equals(tVcode)){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("smsVcodeError"));
                ar.setErrorCode(CodeConstant.ERROR_VCODE);
                return ar;
            }
            /*if(tVcode==null){
                ar.setSuccess(false);
                ar.setMessage("验证码已失效，请重新获取");
                ar.setErrorCode(CodeConstant.OVERTIME_VCODE);
                return ar;
            }else if(!usersDetails.getVcode().equalsIgnoreCase(tVcode)){
                ar.setSuccess(false);
                ar.setMessage("验证码错误，请重新输入");
                ar.setErrorCode(CodeConstant.ERROR_VCODE);
                return ar;
            }*/
            UsersDetails tusersDetails = usersDetailsService.selectByPrimaryKey(user.getInteger("user_id"));
            
            if(!MD5Util.getMd5Code(usersDetails.getOldPassword()).equals(tusersDetails.getTransPassword())){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("oldPasswordError"));
                ar.setErrorCode(CodeConstant.ERROR_PASSWORD);
                return ar;
            }
            
            if(MD5Util.getMd5Code(usersDetails.getNewPassword()).equals(tusersDetails.getTransPassword())){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("newAndOldPassSameError"));
                ar.setErrorCode(CodeConstant.ERROE_PASSWORD_NEW_OLD);
                return ar;
            }
            String transPassword = MD5Util.getMd5Code(usersDetails.getTransPassword());
            boolean setTransPassword = usersDetailsService.setTransPassword(String.valueOf(user.get("user_id")),transPassword);
            if(!setTransPassword){          
                return AjaxResponse.falied(messageSource.getMessage("updatePasswordFailure"), CodeConstant.UPDATE_FAIL);
            }else{
                return AjaxResponse.success(messageSource.getMessage("updatePasswordSuccess"), null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResponse.falied(messageSource.getMessage("sysException"), CodeConstant.SYS_ERROR);
        }finally{
            logger.info("---------------修改交易密码-------end-------------");
        }
    }
    
    /**
     * @describe:忘记交易密码
     * @author: zhangchunming
     * @date: 2017年7月29日下午12:20:55
     * @param request
     * @param response
     * @return: AjaxResponse
     */
    
    @LoginVerify
    @PostMapping("/forgetTransPassword")
    @ApiOperation(nickname = "忘记交易密码", value = "忘记交易密码，输入新交易密码", notes = "忘记交易密码，输入新交易密码！")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "usersDetails", value = "忘记密码参数", required = true, paramType = "body", dataType = "UsersDetails")
    })
    public AjaxResponse forgetTransPassword(HttpServletRequest request,@RequestBody @Valid UsersDetails usersDetails){
        logger.info("-----------忘记交易密码-------------start------------");
        try {
            JSONObject user = JSONObject.parseObject(JSON.toJSONString(request.getSession().getAttribute("User")));
            
            String passWord  = usersDetails.getTransPassword();
            String cfPassWord  = usersDetails.getCfPassword();
            String vcode  = usersDetails.getVcode();
            
            if(StringUtils.isEmpty(passWord)||StringUtils.isEmpty(cfPassWord)){
                return AjaxResponse.falied(messageSource.getMessage("passwordNull"), CodeConstant.PARAM_ERROR);
            } 
            if(passWord.length()<6||passWord.length()>16){
                return AjaxResponse.falied(messageSource.getMessage("PasswordLengthError"), CodeConstant.PARAM_ERROR);
            }
            
            if(!Validator.isAccountByLetterAndNum(passWord)){
                return AjaxResponse.falied(messageSource.getMessage("PasswordLengthError"), CodeConstant.PARAM_ERROR);
            }
            if(StringUtils.isEmpty(cfPassWord)){//确认密码
                return AjaxResponse.falied(messageSource.getMessage("confirmPasswordNull"), CodeConstant.PARAM_ERROR);
            }

            if(!passWord.equals(cfPassWord)){
                return AjaxResponse.falied(messageSource.getMessage("twoPasswordError"), CodeConstant.PARAM_ERROR);
            }
            if(StringUtils.isEmpty(vcode.trim())){
                return AjaxResponse.falied(messageSource.getMessage("smsVcodeNull"), CodeConstant.PARAM_ERROR);
            }
            //半小时之内的短信验证码有效
            String tVcode =sendVodeService.findVcodeByPhone(user.getString("mobile_phone")); 
            if(tVcode==null||!vcode.equalsIgnoreCase(tVcode)){
                return AjaxResponse.falied(messageSource.getMessage("smsVcodeError"), CodeConstant.ERROR_VCODE);
            }
            /*if(tVcode==null){
                return AjaxResponse.falied("验证码已失效，请重新获取！", CodeConstant.OVERTIME_VCODE);
            }else if(!vcode.equalsIgnoreCase(tVcode)){
                return AjaxResponse.falied("验证码错误，请重新输入！", CodeConstant.ERROR_VCODE);
            }*/
            String transPassword = MD5Util.getMd5Code(usersDetails.getTransPassword());
            boolean setTransPassword = usersDetailsService.setTransPassword(String.valueOf(user.get("user_id")),transPassword);
            if(!setTransPassword){         
                return AjaxResponse.falied(messageSource.getMessage("setTransPasswordFailure"), CodeConstant.UPDATE_FAIL);
            }else{
                return AjaxResponse.success(messageSource.getMessage("setTransPasswordSuccess"), null);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResponse.falied(messageSource.getMessage("sysException"), CodeConstant.SYS_ERROR);
        }finally{
            logger.info("-----------忘记交易密码-------------end------------");
        }
    }
    
    /**
     * @describe:修改密码（app端）
     * @author: zhangchunming
     * @date: 2016年12月19日上午10:22:35
     * @param request
     * @param response
     * @return: AjaxResponse
     */
    @LoginVerify
    @PostMapping("/updatePassword")
    @ApiOperation(nickname = "修改密码", value = "修改密码", notes = "修改密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userLogin", value = "忘记密码参数", required = true, paramType = "body", dataType = "UserLogin")
    })
    public AjaxResponse updatePassword(HttpServletRequest request,@RequestBody @Valid UserLogin userLogin){
        AjaxResponse ar = new AjaxResponse();
        try {
            JSONObject user = JSONObject.parseObject(JSON.toJSONString(request.getSession().getAttribute("User")));
            if(StringUtils.isEmpty(userLogin.getOldPassword())){//原密码
                ar.setMessage(messageSource.getMessage("oldPasswordNull"));
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                ar.setSuccess(false);
                return ar;
            }
            if(StringUtils.isEmpty(userLogin.getNewPassword())){//新密码
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("newPasswordNull"));
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            } 
            
            if(userLogin.getNewPassword().length()<6||userLogin.getNewPassword().length()>16){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("PasswordLengthError"));
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            
            if(!Validator.isAccountByLetterAndNum(userLogin.getNewPassword())){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("PasswordLengthError"));
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            if(StringUtils.isEmpty(userLogin.getCfPassWord())){//确认密码
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("confirmPasswordNull"));
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            if(!userLogin.getNewPassword().equals(userLogin.getCfPassWord())){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("twoPasswordError"));
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            
            if(StringUtils.isEmpty(userLogin.getVcode())){//短信验证码
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("smsVcodeNull"));
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            
            //半小时之内的短信验证码有效
            String tVcode =sendVodeService.findVcodeByPhone(user.getString("mobile_phone")); 
            if(tVcode==null||!userLogin.getVcode().equals(tVcode)){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("smsVcodeError"));
                ar.setErrorCode(CodeConstant.ERROR_VCODE);
                return ar;
            }
            /*if(tVcode==null){
                ar.setSuccess(false);
                ar.setMessage("验证码已失效，请重新获取");
                ar.setErrorCode(CodeConstant.OVERTIME_VCODE);
                return ar;
            }else if(!userLogin.getVcode().equalsIgnoreCase(tVcode)){
                ar.setSuccess(false);
                ar.setMessage("验证码错误，请重新输入");
                ar.setErrorCode(CodeConstant.ERROR_VCODE);
                return ar;
            }*/
            
            UserLogin tuserLogin = userLoginService.getUserLoginByUserId(String.valueOf(user.get("user_id")));
            if(!MD5Util.getMd5Code(userLogin.getOldPassword()).equals(tuserLogin.getPassword())){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("oldPasswordError"));
                ar.setErrorCode(CodeConstant.ERROR_PASSWORD);
                return ar;
            }
            
            if(MD5Util.getMd5Code(userLogin.getNewPassword()).equals(tuserLogin.getPassword())){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("newAndOldPassSameError"));
                ar.setErrorCode(CodeConstant.ERROE_PASSWORD_NEW_OLD);
                return ar;
            }
            String password = MD5Util.getMd5Code(userLogin.getNewPassword());
            boolean modifyPwd = userLoginService.modifyPwd(user.getString("account"), password);
            if(!modifyPwd){          
                return AjaxResponse.falied(messageSource.getMessage("updatePasswordFailure"), CodeConstant.UPDATE_FAIL);
            }else{
                return AjaxResponse.success(messageSource.getMessage("updatePasswordSuccess"), null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResponse.falied(messageSource.getMessage("sysException"), CodeConstant.SYS_ERROR);
        }
    }
    
    /**
     * 修改手机号
     * <p>用户实名  </p>
     * @Title: realname 
     * @return  json格式的
     * @create author kezhiyi
     * @create date 2016年8月23日
     */
    @LoginVerify
    @PostMapping("/updatePhone")
    @ApiOperation(nickname = "修改手机号", value = "修改手机号", notes = "参数说明：oldVcode-旧手机验证码，mobilePhone-新手机号，vcode-新手机验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "usersDetails", value = "忘记密码参数", required = true, paramType = "body", dataType = "UsersDetails")
    })
    public AjaxResponse updatePhone(HttpServletRequest request,@RequestBody @Valid UsersDetails usersDetails){
        try {
            JSONObject user = JSONObject.parseObject(JSON.toJSONString(request.getSession().getAttribute("User")));
            String userId = String.valueOf(user.get("user_id"));
            String oldVcode  = usersDetails.getOldVcode()==null?"":usersDetails.getOldVcode().trim();
            String mobilePhone  = usersDetails.getMobilePhone()==null?"":usersDetails.getMobilePhone().trim();
            String vcode  = usersDetails.getVcode()==null?"":usersDetails.getVcode().trim();
            if(StringUtils.isEmpty(oldVcode)){
                AjaxResponse.falied(messageSource.getMessage("oldAccountCodeIsNull"), CodeConstant.PARAM_ERROR);
            }
            
            /*if(!Validator.isMobile(mobilePhone)){
                return AjaxResponse.falied("新手机号格式不正确！", CodeConstant.PARAM_ERROR);
            }*/
            
            if(StringUtils.isEmpty(mobilePhone)){
                AjaxResponse.falied(messageSource.getMessage("newAccountIsNull"), CodeConstant.PARAM_ERROR);
            }
            
            if(StringUtils.isEmpty(vcode)){
                AjaxResponse.falied(messageSource.getMessage("newAccountCodeIsNull"), CodeConstant.PARAM_ERROR);
            }
            
            //半小时之内的短信验证码有效
            String tVcode = sendVodeService.findVcodeByPhone(user.getString("mobile_phone")); 
            if(tVcode==null||!oldVcode.equals(tVcode.trim())){
                return AjaxResponse.falied(messageSource.getMessage("oldAccountCodeError"), CodeConstant.PARAM_ERROR);
            }
            
            //判断用户是否已存在
            if(usersDetailsService.findIsExist(mobilePhone)){
                return AjaxResponse.falied(messageSource.getMessage("newAccountExist"), CodeConstant.PARAM_ERROR);
            }
            
            //半小时之内的短信验证码有效
            String tVcode1 =sendVodeService.findVcodeByPhone(mobilePhone); 
            if(tVcode1==null||!vcode.equals(tVcode1.trim())){
                return AjaxResponse.falied(messageSource.getMessage("newAccountCodeError"), CodeConstant.PARAM_ERROR);
            }
            
            if(!usersDetailsService.updatePhone(userId,mobilePhone)){  
                return AjaxResponse.falied(messageSource.getMessage("saveFailed"), CodeConstant.UPDATE_FAIL);
            }else{
                Map userInfo = usersDetailsService.getUserInfoByUserId((Integer)user.get("user_id"));
                request.getSession().setAttribute("User", userInfo);
                return AjaxResponse.success(messageSource.getMessage("saveSuccess"), null);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResponse.falied(messageSource.getMessage("sysException"), CodeConstant.SYS_ERROR);
        }
    }
    
    /**
     * @describe:获取最后一次登录时间
     * @author: zhangchunming
     * @date: 2017年8月3日下午5:23:24
     * @param request
     * @return: AjaxResponse
     */
    @LoginVerify
    @GetMapping("/getLastLoginTime")
    @ApiOperation(nickname = "获取最后一次登录时间", value = "获取最后一次登录时间", notes = "获取最后一次登录时间")
    @ApiImplicitParams({
    })
    public AjaxResponse getLastLoginTime(HttpServletRequest request){
        Map<String,Object> data = new HashMap<String,Object>();
        try {
            JSONObject user = JSONObject.parseObject(JSON.toJSONString(request.getSession().getAttribute("User")));
            UserLogin lastLoginTime = userLoginService.getLastLoginTime(user.getString("account"));
            data.put("lastLoginTime", lastLoginTime);
            return AjaxResponse.success(messageSource.getMessage("getSuccess"), lastLoginTime);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResponse.falied(messageSource.getMessage("sysException"), CodeConstant.SYS_ERROR);
        }
    }
    
//    /**
//     * @describe:实名认证
//     * @author: zhangchunming
//     * @date: 2017年8月15日下午7:49:25
//     * @param request
//     * @return: AjaxResponse
//     */
//    @LoginVerify
//    @PostMapping("/realName")
//    @ApiOperation(nickname = "实名认证", value = "实名认证", notes = "实名认证")
//    @ApiImplicitParams({
//        @ApiImplicitParam(name = "usersDetails", value = "name：姓名，必填；id_card：身份证号，必填", required = true, paramType = "body", dataType = "UsersDetails")
//    })
//    public AjaxResponse realName(HttpServletRequest request,@RequestBody UsersDetails usersDetails ){
//        Map<String,Object> data = new HashMap<String,Object>();
//        try {
//            JSONObject user = JSONObject.parseObject(JSON.toJSONString(request.getSession().getAttribute("User")));
//            if(StringUtils.isEmpty(usersDetails.getName())){
//                return AjaxResponse.falied("请输入姓名！", CodeConstant.PARAM_ERROR);
//            }
//            if(StringUtils.isEmpty(usersDetails.getNationality())){
//                return AjaxResponse.falied("请选择国籍！", CodeConstant.PARAM_ERROR);
//            }
//            if(StringUtils.isEmpty(usersDetails.getIdCard())){
//                return AjaxResponse.falied("请输入身份证号！", CodeConstant.PARAM_ERROR);
//            }
//            usersDetails.setId(user.getInteger("user_id"));
//            boolean realName = usersDetailsService.realName(usersDetails);
//            if(realName){
//                return AjaxResponse.success("实名成功！", null);
//            }else{
//                return AjaxResponse.falied("实名认证失败！",CodeConstant.UPDATE_FAIL);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return AjaxResponse.falied(messageSource.getMessage("sysException"), CodeConstant.SYS_ERROR);
//        }
//    }
//    
//    public static void main(String[] args) {
//        try {
//            String result = HttpUtil.sendPostData("http://192.168.100.17:8001/api/user/validTranPassword", "transPassword=zxc123");
//            System.out.println("result="+result);
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }
}
