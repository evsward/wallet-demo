package com.ecochain.wallet.controller.app;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ecochain.annotation.AppLoginVerify;
import com.ecochain.base.BaseWebService;
import com.ecochain.constant.CodeConstant;
import com.ecochain.core.config.CoreMessageSource;
import com.ecochain.util.Base64;
import com.ecochain.util.InternetUtil;
import com.ecochain.util.MD5Util;
import com.ecochain.util.Validator;
import com.ecochain.wallet.controller.AjaxResponse;
import com.ecochain.wallet.entity.PageData;
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
@RequestMapping(value = "/api/rest/user")
@Api(value = "app用户管理")
public class AppUsersWebService extends BaseWebService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserLoginService userLoginService;
    @Autowired
    private SendVodeService sendVodeService;
    @Autowired
    private SysGencodeService sysGencodeService;
    @Autowired
    private UsersDetailsService usersDetailsService;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private CoreMessageSource messageSource;
    

    /**
     * 请求登录，验证用户
     */
    @PostMapping("/login")
    @ApiOperation(nickname = "登录接口", value = "用户登录", notes = "用户登录！")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "account", value = "登录账号(邮箱)", required = true, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "password", value = "密码", required = true, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "source", value = "来源：APP或其他，APP的必填", required = false, paramType = "query", dataType = "String")
    })
    public AjaxResponse login(HttpServletRequest request,HttpServletResponse response){
        Map<String,Object> data  = new HashMap<String,Object>();
        PageData pd = new PageData();
        pd = this.getPageData();
        try {
            String account = StringUtils.isEmpty(pd.getString("account"))?null:pd.getString("account").trim();
            String password = StringUtils.isEmpty(pd.getString("password"))?null:pd.getString("password").trim();
            if(StringUtils.isEmpty(account)){
                return AjaxResponse.falied(messageSource.getMessage("InputAccount"), CodeConstant.USER_NO_EXISTS);
            }
            if(StringUtils.isEmpty(password)){
                return AjaxResponse.falied(messageSource.getMessage("InputPassword"), CodeConstant.ERROE_PASSWORD_NULL);
            }
            pd.put("account", account);
            password = MD5Util.getMd5Code(password);
            pd.put("password", password);
            UserLogin tuserLogin = usersDetailsService.getUserByAccAndPass(account, password);
            if(tuserLogin != null){
                if("1".equals(tuserLogin.getStatus())){
                    String lastloginIp = InternetUtil.getRemoteAddr(request);
                    long lastlogin_port = InternetUtil.getRemotePort(request);
                    usersDetailsService.updateLoginTimeById(tuserLogin.getId(), lastloginIp, lastlogin_port);
                    Map userInfo = usersDetailsService.getUserInfoByUserId(tuserLogin.getUserId());
                    String token = request.getSession().getId();
                    request.getSession().setAttribute("User", userInfo);
                    redisTemplate.opsForValue().set("app:usertoken:"+token,userInfo);
                    data.put("CSESSIONID", request.getSession().getId());
                    data.put("user_name", userInfo.get("user_name"));
                    data.put("address", userInfo.get("address"));
                    data.put("vip_flag", tuserLogin.isVipFlag());
                    return AjaxResponse.success(messageSource.getMessage("LoginSuccess"), data);
                }else{
                    return AjaxResponse.falied(messageSource.getMessage("YouInBlackList"), CodeConstant.ERROR_BLACK);
                }
            }else{
                return AjaxResponse.falied(messageSource.getMessage("AccountPasswordError"), CodeConstant.USERNAMEORPWD_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResponse.falied(messageSource.getMessage("sysException"), CodeConstant.SYS_ERROR);
        }
    }

    /**
     * @describe:注册
     * @author: zhangchunming
     * @date: 2017年3月2日下午3:36:32
     * @param request
     * @param response
     * @return: AjaxResponse
     */
    @PostMapping("/register")
    @ApiOperation(nickname = "用户注册", value = "用户注册", notes = "用户注册")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "user_name", value = "会员名称（中英文字符均可、4-8个字符以内）", required = true, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "password", value = "创建密码（字母数字组合，6-16个字符以内）", required = true, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "confirmPassword", value = "确认密码", required = true, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "account", value = "输入手机号（11位数字）", required = true, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "vcode", value = "手机短信验证码", required = true, paramType = "query", dataType = "String")
    })
    public AjaxResponse register(HttpServletRequest request,HttpServletResponse response){
        AjaxResponse ar = new AjaxResponse();
        Map<String,Object> data  = new HashMap<String,Object>();
        try {
            PageData pd = new PageData();
            pd = this.getPageData();
            logger.info("--------------register  pd value is "+pd.toString());
            String account = StringUtils.isEmpty(pd.getString("account"))?null:pd.getString("account").trim();
            String password = StringUtils.isEmpty(pd.getString("password"))?null:pd.getString("password").trim();
            String user_name = StringUtils.isEmpty(pd.getString("user_name"))?null:pd.getString("user_name").trim();
            String vcode = StringUtils.isEmpty(pd.getString("vcode"))?null:pd.getString("vcode").trim();
            String confirmPassword = StringUtils.isEmpty(pd.getString("confirmPassword"))?null:pd.getString("confirmPassword").trim();
            if(StringUtils.isEmpty(user_name)){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("UserNameNull"));
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }

            if(user_name.length()<4||user_name.length()>8){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("userNameError"));
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }

            if(StringUtils.isEmpty(password)){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("InputPassword"));
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }

            if(password.length()<6||password.length()>16){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("PasswordLengthError"));
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }

            if(!Validator.isAccountByLetterAndNum(password)){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("PasswordLengthError"));
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }

            if(StringUtils.isEmpty(confirmPassword)){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("confirmPasswordNull"));
                ar.setErrorCode(CodeConstant.ERROE_CONFIRM_PASSWORD_NULL);
                return ar;
            }
            if(!pd.getString("password").equals(confirmPassword)){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("twoPasswordError"));
                ar.setErrorCode(CodeConstant.ERROE_PASSWORD_DIFFERENT);
                return ar;
            }

            if(StringUtils.isEmpty(account)){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("InputAccount"));
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            /*if(!Validator.isMobile(account)){
                ar.setSuccess(false);
                ar.setMessage("手机号格式不正确！");
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }*/

            if(StringUtils.isEmpty(vcode)){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("smsVcodeNull"));
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }

            //半小时之内的短信验证码有效
            String tVcode =sendVodeService.findVcodeByPhone(pd.getString("account"));
            if(tVcode==null||!pd.getString("vcode").equals(tVcode)){
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
            }else if(!pd.getString("vcode").equalsIgnoreCase(tVcode)){
                ar.setSuccess(false);
                ar.setMessage("验证码错误，请重新输入");
                ar.setErrorCode(CodeConstant.ERROR_VCODE);
                return ar;
            }*/


            password = MD5Util.getMd5Code(password);
            //判断用户名是否已存在
            if(usersDetailsService.isExistByUserName(user_name)){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("UserNameExist"));
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            //判断用户是否已存在
            if(usersDetailsService.findIsExist(account)){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("AccountExist"));
                ar.setErrorCode(CodeConstant.ACCOUNT_EXISTS);
                return ar;
            }
            //用户信息
            UsersDetails usersDetails = new UsersDetails();
            usersDetails.setUserType("1");
            usersDetails.setMobilePhone(account);
            usersDetails.setUserName(user_name);
            //用户登录信息
            UserLogin userLogin = new UserLogin();
            userLogin.setAccount(account);
            userLogin.setPassword(password);
            userLogin.setStatus("1");
            userLogin.setUserName(user_name);
            userLogin.setLastloginIp(InternetUtil.getRemoteAddr(request));
            userLogin.setLastloginPort(InternetUtil.getRemotePort(request));
            //用户钱包
            UserWallet userWallet = new UserWallet();

            if(!usersDetailsService.addUser(usersDetails,userLogin,userWallet)){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("RegisterError"));
                ar.setErrorCode(CodeConstant.REGISTER_FAIL);
                return ar;
            }
            Map<String, Object> userInfo = userLoginService.getUserInfoByAccount(account);
            request.getSession().setAttribute("User", userInfo);
            data.put("CSESSIONID", request.getSession().getId());
            data.put("user_name", userInfo.get("user_name"));
            data.put("address", userInfo.get("address"));
            ar.setData(data);
            ar.setSuccess(true);
            ar.setMessage(messageSource.getMessage("registerSuccess"));
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
     * 访问系统首页
     */
    @AppLoginVerify
    @PostMapping("/index")
    @ApiOperation(nickname = "获取用户信息", value = "获取用户信息", notes = "获取用户信息")
    @ApiImplicitParams({

    })
    public AjaxResponse index(HttpServletRequest request){
        AjaxResponse ar = new AjaxResponse();
        Map<String,Object> data = new HashMap<String,Object>();
        PageData pd = new PageData();
        pd = this.getPageData();
        try {
            JSONObject user = JSONObject.parseObject(JSON.toJSONString(request.getSession().getAttribute("User")));
            Map userInfo = usersDetailsService.getUserInfoByUserId(user.getInteger("user_id"));
            data.put("userInfo", userInfo);
            ar.setData(data);
            ar.setSuccess(true);
            ar.setMessage(messageSource.getMessage("getSuccess"));
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
     * 用户注销
     * @param request
     * @return
     */
    @AppLoginVerify
    @PostMapping(value="/logout")
    @ApiOperation(nickname = "退出登录", value = "退出登录", notes = "退出登录！")
    @ApiImplicitParams({
    })
    public AjaxResponse logout(HttpServletRequest request)throws Exception{
        String token = request.getSession().getId();
        redisTemplate.delete("app:usertoken:"+token);
        request.getSession().removeAttribute("User");
        return AjaxResponse.success(messageSource.getMessage("LogoutSuccess"), null);
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
        @ApiImplicitParam(name = "account", value = "登录账号", required = true, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "password", value = "新密码", required = true, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "cfPassWord", value = "确认密码", required = true, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "vcode", value = "验证码", required = true, paramType = "query", dataType = "String")
    })
    public AjaxResponse forgetpwd(HttpServletRequest request,HttpServletResponse response){
        AjaxResponse ar = new AjaxResponse();
        try {
            String account  =request.getParameter("account");
//            String phone  =request.getParameter("phone");
            String passWord  =request.getParameter("password");
            String cfPassWord  =request.getParameter("cfPassWord");
            String vcode  =request.getParameter("vcode");
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
            if(StringUtils.isEmpty(passWord)||StringUtils.isEmpty(cfPassWord)){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("passwordNull"));
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            if(passWord.length()<6||passWord.length()>16){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("PasswordLengthError"));
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            if(!Validator.isPasswordByLetterAndNum(passWord)){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("PasswordLengthError"));
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }

            if(!passWord.equals(cfPassWord)){
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
            /*Map<String, Object> userInfo = userLoginService.getUserInfoByAccount(account);
            if(StringUtils.isEmpty(String.valueOf(userInfo.get("mobile_phone")))){
                ar.setSuccess(false);
                ar.setMessage("您的账号未绑定手机号，忘记密码功能无法使用，请联系客服！");
                ar.setErrorCode(CodeConstant.USERMOBILE_NOEXISTS);
                return ar;
            }*/
            //半小时之内的短信验证码有效
            String tVcode =sendVodeService.findVcodeByPhone(account);
            if(tVcode ==null||!vcode.trim().equals(tVcode.trim())){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("smsVcodeError"));
                ar.setErrorCode(CodeConstant.ERROR_VCODE);
                return ar;
            }
            if(!userLoginService.modifyPwd(account, MD5Util.getMd5Code(passWord))){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("setPasswordFailure"));
                ar.setErrorCode(CodeConstant.UPDATE_FAIL);
                return ar;
            }else{
            	ar.setSuccess(true);
                ar.setMessage(messageSource.getMessage("setPasswordSuccess"));
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



    @AppLoginVerify
    @PostMapping("/getUserInfo")
    @ApiOperation(nickname = "获取用户基本信息", value = "获取用户基本信息", notes = "获取用户基本信息")
    @ApiImplicitParams({
    })
    public AjaxResponse getUserInfo(HttpServletRequest request){
        logBefore(logger,"获取用户信息");
        AjaxResponse ar = new AjaxResponse();
        try {
            JSONObject user = JSONObject.parseObject(JSON.toJSONString(request.getSession().getAttribute("User")));
            Map userInfo = usersDetailsService.getUserInfoByUserId(user.getInteger("user_id"));
            ar.setSuccess(true);
            ar.setData(userInfo);
            ar.setMessage(messageSource.getMessage("getSuccess"));
        } catch (Exception e) {
            e.printStackTrace();
            ar.setSuccess(false);
            ar.setMessage(messageSource.getMessage("sysException"));
            ar.setErrorCode(CodeConstant.SYS_ERROR);
        }
        logAfter(logger);
        return ar;
    }

    /**
     * @describe:设置交易密码
     * @author: zhangchunming
     * @date: 2017年5月31日下午7:53:07
     * @param request
     * @return: AjaxResponse
     */
    @AppLoginVerify
    @PostMapping("/setTransPassword")
    @ApiOperation(nickname = "设置交易密码", value = "设置交易密码", notes = "设置交易密码")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "trans_password", value = "交易密码", required = true, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "comfirm_trans_password", value = "确认交易密码", required = true, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "vcode", value = "短信验证码", required = true, paramType = "query", dataType = "String")
    })
    public AjaxResponse setTransPassword(HttpServletRequest request){
        logBefore(logger,"----------设置交易密码-------------");
        AjaxResponse ar = new AjaxResponse();
        PageData pd = this.getPageData();
        try {
            JSONObject user = JSONObject.parseObject(JSON.toJSONString(request.getSession().getAttribute("User")));
            String userId = String.valueOf(user.get("user_id"));
            if(StringUtils.isEmpty(pd.getString("trans_password"))){
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                ar.setMessage(messageSource.getMessage("transPasswordNull"));
                ar.setSuccess(false);
                return ar;
            }

            if(StringUtils.isEmpty(pd.getString("comfirm_trans_password"))){
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                ar.setMessage(messageSource.getMessage("confirmPasswordNull"));
                ar.setSuccess(false);
                return ar;
            }

            if(!pd.getString("trans_password").equals(pd.getString("comfirm_trans_password"))){
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                ar.setMessage(messageSource.getMessage("twoPasswordError"));
                ar.setSuccess(false);
                return ar;
            }
            if(StringUtils.isEmpty(pd.getString("vcode"))){//短信验证码
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("smsVcodeNull"));
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }

            //半小时之内的短信验证码有效
            String tVcode = sendVodeService.findVcodeByPhone(user.getString("mobile_phone"));

            if(tVcode==null||!pd.getString("vcode").equals(tVcode)){
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
            }else if(!pd.getString("vcode").equalsIgnoreCase(tVcode)){
                ar.setSuccess(false);
                ar.setMessage("验证码错误，请重新输入");
                ar.setErrorCode(CodeConstant.ERROR_VCODE);
                return ar;
            }*/
            String transPassword = MD5Util.getMd5Code(pd.getString("trans_password"));
            
            UserLogin userLogin = userLoginService.getUserLoginByUserId(String.valueOf(user.get("user_id")));
            if(transPassword.equals(userLogin.getPassword())){
            	return AjaxResponse.falied(messageSource.getMessage("transPassAndPassWordNotSame"), CodeConstant.PARAM_ERROR);
            }
            
            boolean setTransPassword = usersDetailsService.setTransPassword(userId, transPassword);
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
        logAfter(logger);
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
    @PostMapping("/sendVcode")
    @ApiOperation(nickname = "发送短信验证码", value = "发送短信验证码", notes = "发送短信验证码")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "phone", value = "手机号", required = true, paramType = "query", dataType = "String")
    })
    public AjaxResponse sendVcode(HttpServletRequest request,HttpServletResponse response){
        AjaxResponse ar = new AjaxResponse();
        try {
            PageData pd  = new PageData();
            pd = this.getPageData();
            String phone = StringUtils.isEmpty(pd.getString("phone"))?null:pd.getString("phone").trim();
            if(StringUtils.isEmpty(pd.getString("phone"))){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("inputEmailAccount"));
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            /*if(Validator.isMobile(pd.getString("phone"))){
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
//            String content = "";//发送内容
            if("0".equals(smsflag)){
                vCode = "888888";
            }else{
                vCode = (int)((Math.random()*9+1)*100000)+"";
                
                String title = messageSource.getMessage("registeEmailTitle");
                String msg = messageSource.getMessage("registeEmailContent", vCode);
                
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
            systemSendVcode.setSmsType("7");//注册合链钱包
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
    @AppLoginVerify
    @PostMapping("/sendVcode1")
    @ApiOperation(nickname = "发送短信验证码", value = "发送短信验证码", notes = "发送短信验证码")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "sms_type", value = "业务类型：1-更新密码2-更新交易密码3-设置交易密码4-找回交易密码5-修改手机号6-提币7-注册", required = true, paramType = "query", dataType = "String")
    })
    public AjaxResponse sendVcode1(HttpServletRequest request,HttpServletResponse response){
        AjaxResponse ar = new AjaxResponse();
        try {
            PageData pd  = new PageData();
            pd = this.getPageData();

            JSONObject user = JSONObject.parseObject(JSON.toJSONString(request.getSession().getAttribute("User")));
            String phone = user.getString("account");
            if(StringUtils.isEmpty(pd.getString("sms_type"))){
                AjaxResponse.falied(messageSource.getMessage("dataError"), CodeConstant.PARAM_ERROR);
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

            if(!sms_type.contains(pd.getString("sms_type"))||pd.getString("sms_type").length()!=1){
                return AjaxResponse.falied(messageSource.getMessage("dataError"), CodeConstant.PARAM_ERROR);
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
                switch (sms_type) {
                case "1":
                    title = messageSource.getMessage("sendVcodeTitle_1");
                    msg = messageSource.getMessage("sendVcodeContent_1");
                    break;
                case "2":
                    title = messageSource.getMessage("sendVcodeTitle_2");   
                    msg = messageSource.getMessage("sendVcodeContent_2");
                    break;
                case "3":
                    title = messageSource.getMessage("sendVcodeTitle_3");
                    msg = messageSource.getMessage("sendVcodeContent_3");
                    break;
                case "4":
                    title = messageSource.getMessage("sendVcodeTitle_4");
                    msg = messageSource.getMessage("sendVcodeContent_4");
                    break;
                case "5":
                    title = messageSource.getMessage("sendVcodeTitle_5");
                    msg = messageSource.getMessage("sendVcodeContent_5");
                    break;
                default:
                    break;
                }

                vCode = (int)((Math.random()*9+1)*100000)+"";
                
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
            systemSendVcode.setSmsType(pd.getString("sms_type"));
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
    @PostMapping("/sendVcodeByAccount")
    @ApiOperation(nickname = "忘记密码发送短信验证码", value = "忘记密码发送短信验证码", notes = "忘记密码发送短信验证码")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "account", value = "登陆账号", required = true, paramType = "query", dataType = "String")
    })
    public AjaxResponse sendVcodeByAccount(HttpServletRequest request,HttpServletResponse response){
        AjaxResponse ar = new AjaxResponse();
        try {
            PageData pd  = new PageData();
            pd = this.getPageData();
            pd.put("account", pd.getString("account")==null?"":pd.getString("account").trim());
            Map<String, Object> userInfo = userLoginService.getUserInfoByAccountOrUserName(pd.getString("account"));
            String phone = String.valueOf(userInfo.get("account")).trim();
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
            systemSendVcode.setSmsType("8");//忘记密码
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
    @AppLoginVerify
    @PostMapping("/validTranPassword")
    @ApiOperation(nickname = "验证交易密码", value = "验证交易密码", notes = "验证交易密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "trans_password", value = "交易密码", required = true, paramType = "query", dataType = "String"),
    })
    public AjaxResponse validTranPassword(HttpServletRequest request)throws Exception{
        AjaxResponse ar = new AjaxResponse();
        PageData pd = new PageData();
        pd = this.getPageData();
        try {
            JSONObject user = JSONObject.parseObject(JSON.toJSONString(request.getSession().getAttribute("User")));
            String userId = String.valueOf(user.get("user_id"));
            if(StringUtils.isEmpty(pd.getString("trans_password"))){
                ar.setMessage(messageSource.getMessage("transPasswordNull"));
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                ar.setSuccess(false);
                return ar;
            }
            String transPassword = MD5Util.getMd5Code(pd.getString("trans_password"));
            Boolean existTransPassword = usersDetailsService.isExistTransPassword(userId, transPassword);
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
     * @describe:修改密码（app端）
     * @author: zhangchunming
     * @date: 2016年12月19日上午10:22:35
     * @param request
     * @param response
     * @return: AjaxResponse
     */
    @AppLoginVerify
    @PostMapping("/updatePassword")
    @ApiOperation(nickname = "修改密码", value = "修改密码", notes = "修改密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "oldPassword", value = "原密码", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "newPassword", value = "新密码", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "cfPassWord", value = "确认密码", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "vcode", value = "短信验证码", required = true, paramType = "query", dataType = "String")
    })
    public AjaxResponse updatePassword(HttpServletRequest request,HttpServletResponse response){
        AjaxResponse ar = new AjaxResponse();
        PageData pd  = new PageData();
        pd = this.getPageData();
        try {
            JSONObject user = JSONObject.parseObject(JSON.toJSONString(request.getSession().getAttribute("User")));
            if(StringUtils.isEmpty(pd.getString("oldPassword"))){//原密码
                ar.setMessage(messageSource.getMessage("oldPasswordNull"));
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                ar.setSuccess(false);
                return ar;
            }
            if(StringUtils.isEmpty(pd.getString("newPassword"))){//新密码
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("newPasswordNull"));
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }

            if(pd.getString("newPassword").length()<6||pd.getString("newPassword").length()>16){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("PasswordLengthError"));
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }

            if(!Validator.isAccountByLetterAndNum(pd.getString("newPassword"))){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("PasswordLengthError"));
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            if(StringUtils.isEmpty(pd.getString("cfPassWord"))){//确认密码
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("confirmPasswordNull"));
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            if(!pd.getString("newPassword").equals(pd.getString("cfPassWord"))){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("twoPasswordError"));
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }

            if(StringUtils.isEmpty(pd.getString("vcode"))){//短信验证码
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("smsVcodeNull"));
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }

            //半小时之内的短信验证码有效
            String tVcode =sendVodeService.findVcodeByPhone(user.getString("mobile_phone"));

            if(tVcode==null||!pd.getString("vcode").equals(tVcode)){
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
            }else if(!pd.getString("vcode").equalsIgnoreCase(tVcode)){
                ar.setSuccess(false);
                ar.setMessage("验证码错误，请重新输入");
                ar.setErrorCode(CodeConstant.ERROR_VCODE);
                return ar;
            }*/

            UserLogin userLogin = userLoginService.getUserLoginByUserId(String.valueOf(user.get("user_id")));
            if(!MD5Util.getMd5Code(pd.getString("oldPassword")).equals(userLogin.getPassword())){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("oldPasswordError"));
                ar.setErrorCode(CodeConstant.ERROR_PASSWORD);
                return ar;
            }

            if(MD5Util.getMd5Code(pd.getString("newPassword")).equals(userLogin.getPassword())){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("newAndOldPassSameError"));
                ar.setErrorCode(CodeConstant.ERROE_PASSWORD_NEW_OLD);
                return ar;
            }
            String password = MD5Util.getMd5Code(pd.getString("newPassword"));
            if(!userLoginService.modifyPwd(user.getString("account"), password)){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("updatePasswordFailure"));
                ar.setErrorCode(CodeConstant.UPDATE_FAIL);
                return ar;
            }else{
            	ar.setSuccess(true);
                ar.setMessage(messageSource.getMessage("updatePasswordSuccess"));
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
     * @describe:修改交易密码
     * @author: zhangchunming
     * @date: 2016年12月19日上午10:22:35
     * @param request
     * @param response
     * @return: AjaxResponse
     */

    @AppLoginVerify
    @PostMapping("/updateTransPassword")
    @ApiOperation(nickname = "修改交易密码", value = "修改交易密码", notes = "修改交易密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "oldPassword", value = "原交易密码", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "newPassword", value = "新交易密码", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "cfPassWord", value = "确认交易密码", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "vcode", value = "短信验证码", required = true, paramType = "query", dataType = "String")
    })
    public AjaxResponse updateTransPassword(HttpServletRequest request,HttpServletResponse response){
        AjaxResponse ar = new AjaxResponse();
        try {
            PageData pd  = new PageData();
            pd = this.getPageData();
            JSONObject user = JSONObject.parseObject(JSON.toJSONString(request.getSession().getAttribute("User")));
            if(StringUtils.isEmpty(pd.getString("oldPassword"))){//原密码
                ar.setMessage(messageSource.getMessage("oldPasswordNull"));
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                ar.setSuccess(false);
                return ar;
            }
            if(StringUtils.isEmpty(pd.getString("newPassword"))){//新密码
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("newPasswordNull"));
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }

            if(pd.getString("newPassword").length()<6||pd.getString("newPassword").length()>16){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("PasswordLengthError"));
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }

            if(!Validator.isAccountByLetterAndNum(pd.getString("newPassword"))){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("PasswordLengthError"));
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            if(StringUtils.isEmpty(pd.getString("cfPassWord"))){//确认密码
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("confirmPasswordNull"));
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            if(!pd.getString("newPassword").equals(pd.getString("cfPassWord"))){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("twoPasswordError"));
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }

            if(StringUtils.isEmpty(pd.getString("vcode"))){//短信验证码
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("smsVcodeNull"));
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }

            //半小时之内的短信验证码有效
            String tVcode =sendVodeService.findVcodeByPhone(user.getString("mobile_phone"));

            if(tVcode==null||!pd.getString("vcode").equals(tVcode)){
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
            }else if(!pd.getString("vcode").equalsIgnoreCase(tVcode)){
                ar.setSuccess(false);
                ar.setMessage("验证码错误，请重新输入");
                ar.setErrorCode(CodeConstant.ERROR_VCODE);
                return ar;
            }*/

            String ttransPassword = usersDetailsService.getTransPassword(String.valueOf(user.get("user_id")));
            if(!MD5Util.getMd5Code(pd.getString("oldPassword")).equals(ttransPassword)){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("oldPasswordError"));
                ar.setErrorCode(CodeConstant.ERROR_PASSWORD);
                return ar;
            }

            if(MD5Util.getMd5Code(pd.getString("newPassword")).equals(ttransPassword)){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("newAndOldPassSameError"));
                ar.setErrorCode(CodeConstant.ERROE_PASSWORD_NEW_OLD);
                return ar;
            }
            String transPassword = MD5Util.getMd5Code(pd.getString("newPassword"));
            if(!usersDetailsService.setTransPassword(String.valueOf(user.get("user_id")), transPassword)){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("updateTransPasswordFailure"));
                ar.setErrorCode(CodeConstant.UPDATE_FAIL);
                return ar;
            }else{
            	ar.setSuccess(true);
                ar.setMessage(messageSource.getMessage("updateTransPasswordSuccess"));
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

    @AppLoginVerify
    @PostMapping("/forgetTransPassword")
    @ApiOperation(nickname = "忘记交易密码", value = "忘记交易密码，输入新交易密码", notes = "忘记交易密码，输入新交易密码！")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "password", value = "新密码", required = true, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "cfPassWord", value = "确认密码", required = true, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "vcode", value = "短信验证码", required = true, paramType = "query", dataType = "String")
    })
    public AjaxResponse forgetTransPassword(HttpServletRequest request,HttpServletResponse response){
        AjaxResponse ar = new AjaxResponse();
        PageData pd = new PageData();
        pd = this.getPageData();
        try {
            JSONObject user = JSONObject.parseObject(JSON.toJSONString(request.getSession().getAttribute("User")));
            String passWord  =request.getParameter("password");
            String cfPassWord  =request.getParameter("cfPassWord");
            String vcode  =request.getParameter("vcode");

            if(StringUtils.isEmpty(passWord)||StringUtils.isEmpty(cfPassWord)){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("passwordNull"));
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            if(passWord.length()<6||passWord.length()>16){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("PasswordLengthError"));
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }

            if(!Validator.isAccountByLetterAndNum(passWord)){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("PasswordLengthError"));
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            if(StringUtils.isEmpty(pd.getString("cfPassWord"))){//确认密码
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("confirmPasswordNull"));
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }

            if(!passWord.equals(cfPassWord)){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("twoPasswordError"));
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            if(StringUtils.isEmpty(vcode.trim())){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("smsVcodeNull"));
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
          //半小时之内的短信验证码有效
            String tVcode =sendVodeService.findVcodeByPhone(user.getString("mobile_phone"));

            if(tVcode==null||!pd.getString("vcode").equals(tVcode)){
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
            }else if(!vcode.equalsIgnoreCase(tVcode)){
                ar.setSuccess(false);
                ar.setMessage("验证码错误，请重新输入");
                ar.setErrorCode(CodeConstant.ERROR_VCODE);
                return ar;
            }*/
            String transPassword = MD5Util.getMd5Code(passWord);
            if(!usersDetailsService.setTransPassword(String.valueOf(user.get("user_id")), transPassword)){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("setTransPasswordFailure"));
                ar.setErrorCode(CodeConstant.UPDATE_FAIL);
                return ar;
            }else{
            	ar.setSuccess(true);
                ar.setMessage(messageSource.getMessage("setTransPasswordSuccess"));
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


    public static void main(String[] args) throws  Exception{
      //初始
        /*BigInteger num = new BigInteger("0");  
        num = num.setBit(2);  
        System.out.println(num);  
        num = num.setBit(1);  
        System.out.println(num);  
        System.out.println(num.testBit(2));  
        System.out.println(num.testBit(1));  
        System.out.println(num.testBit(3));  
        System.out.println("".length());
        String a = "KzopQgjVwAG2cqEHLWLJ9DKU5LpiUjcbREKoX3LbVgzPVC4S3Hynl".substring(0, 32);
        System.out.println(a.length());
        System.out.println(a.toString());
        
        System.out.println("KzopQgjVwAG2cqEHLWLJ9DKU5LpiUjcbREKoX3LbVgzPVC4S3Hynl");
        System.out.println(Base64.getBase64("KzopQgjVwAG2cqEHLWLJ9DKU5LpiUjcbREKoX3LbVgzPVC4S3Hynl"));
        System.out.println(Base64.getFromBase64("S3pvcFFnalZ3QUcyY3FFSExXTEo5REtVNUxwaVVqY2JSRUtvWDNMYlZnelBWQzRTM0h5bmw="));*/
        String str = "abcdefghijklmnopqrstuvwxyz";
        String strNew = "";
        char[] b = str.toCharArray();
        for(int i=0;i<4;i++){
            int index =(int) (Math.random()*b.length);
            strNew += b[index];
        }
        System.out.println(strNew);
        String base64 = Base64.getBase64("9fd50096398d4b428d57da0f4bffbb67");
        System.out.println("base64="+base64);

        char[] a = {'1','2'};
        System.out.println(a.toString());

    }
}
