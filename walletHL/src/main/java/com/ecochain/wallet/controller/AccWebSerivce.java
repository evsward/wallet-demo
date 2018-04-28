package com.ecochain.wallet.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ecochain.annotation.LoginVerify;
import com.ecochain.constant.CodeConstant;
import com.ecochain.constant.Constant;
import com.ecochain.core.config.CoreMessageSource;
import com.ecochain.util.*;
import com.ecochain.wallet.entity.AccRecordDTO;
import com.ecochain.wallet.entity.TransferRecord;
import com.ecochain.wallet.entity.UserWallet;
import com.ecochain.wallet.entity.WithdrawRecord;
import com.ecochain.wallet.mapper.SystemSendVcodeMapper;
import com.ecochain.wallet.mapper.UserCoinMapper;
import com.ecochain.wallet.mapper.WithdrawRecordMapper;
import com.ecochain.wallet.service.*;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 账户控制类
 * @author zhangchunming
 */
@RestController
@RequestMapping("/api/acc")
@Api(value = "账户管理")
public class AccWebSerivce {
    private Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private AccService accService;
    
    @Autowired
    private UserLoginService userLoginService;
    @Autowired
    private DigitalCoinService digitalCoinService;
    
    @Autowired
    private UserWalletService userWalletService;
    
    @Autowired
    private SendVodeService sendVodeService;
    @Autowired
    private SystemSendVcodeMapper SystemSendVcodeMapper;
    @Autowired
    private UsersDetailsService usersDetailsService;
    @Autowired
    private AccRecordService accRecordService;
    @Autowired
    private WithdrawRecordService withdrawRecordService;
    @Autowired
    private ExchangeRecordService exchangeRecordService;
    @Autowired
    private UserBankService userBankService;
    @Autowired
    private UserCoinMapper userCoinMapper;
    @Autowired
    private SysGencodeService sysGencodeService;
    @Autowired
    private WithdrawRecordMapper withdrawRecordMapper;
    @Autowired
    private CoreMessageSource messageSource;
    /**
     * @describe:查询账户列表
     * @author: zhangchunming
     * @date: 2016年11月7日下午8:02:29
     * @param request
     * @param page
     * @return: AjaxResponse
     */
    @LoginVerify
    @GetMapping("/listPageAcc/{type}/{currency}")
    @ApiOperation(nickname = "账户流水", value = "账户流水", notes = "账户流水")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "query", name = "page", value= "第几页", dataType ="int"),
        @ApiImplicitParam(paramType = "path", name = "type", value = "业务类型：1-充值 2-提币 3-转账", dataType = "String"),
        @ApiImplicitParam(paramType = "path", name = "currency", value = "币种类型：BTC ETH LTC SAN", dataType = "String")
    })
    public AjaxResponse listPageAcc(HttpServletRequest request,
            @PathVariable("type") String type,
            @RequestParam("page") int page,
            @PathVariable("currency") String currency){
        try {
            System.out.println("=====" + (Integer)request.getSession().getAttribute("currentUserId"));
            Map user = (Map) request.getSession().getAttribute("User");
            List<AccRecordDTO> listPageAccRecord = accRecordService.listPageAccRecord(page,type,String.valueOf(user.get("user_id")),currency);
            for(AccRecordDTO a:listPageAccRecord){
                System.out.println("a="+a.getCreateTime());
            }
            return AjaxResponse.success(messageSource.getMessage("getSuccess"),new PageInfo<AccRecordDTO>(listPageAccRecord));
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResponse.falied(messageSource.getMessage("sysException"));
        }
    }
    
    @LoginVerify
    @PostMapping("/withdrawal")
    @ApiOperation(nickname = "提币", value = "提币", notes = "参数说明:"+
            "{\n" +
            "  \"currency\": \"货币种类，必填（可选值：BTC,RMB；暂不可选：LTC,ETH,ETC,LTC）\",\n" +
            "  \"address\": \"提币地址，必填\",\n" +
            "  \"bankCardNo\": \"如货币种类选择RMB，此值必填；否则，不填\",\n" +
            "  \"bankCardName\": \"如货币种类选择RMB，此值必填；否则，不填\",\n" +
            "  \"money\": \"提币金额，或提币数量\",\n" +
            "  \"free\": \"网络手续费，选BTC时，填写\",\n" +
            "  \"transPassword\": \"必填，交易密码\",\n" +
            "  \"vcode\": \"必填，验证码\",\n" +
            "}！")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "withdrawRecord", value = "提币参数", required = true, dataType = "WithdrawRecord")
    })
    public AjaxResponse withdrawal(HttpServletRequest request,
            @RequestBody @Valid WithdrawRecord withdrawRecord){
        logger.info("-----------提币申请------------start-----------");
        AjaxResponse ar = new AjaxResponse();
        Map user = (Map) request.getSession().getAttribute("User");
        String userId = String.valueOf(user.get("user_id"));
        withdrawRecord.setUserId(userId);
        logger.info("withdrawRecord.toString():"+withdrawRecord.toString());
        try {
        	
        	if(!"BTC".equals(withdrawRecord.getCurrency())&&!"LTC".equals(withdrawRecord.getCurrency())
            		&&!"ETH".equals(withdrawRecord.getCurrency())&&!"SAN".equals(withdrawRecord.getCurrency())){
                return AjaxResponse.falied(messageSource.getMessage("coinNameError"), CodeConstant.PARAM_ERROR);
            }
//            if("RMB".equals(withdrawRecord.getCurrency())){
//                if(StringUtils.isEmpty(withdrawRecord.getBankCardNo())||StringUtils.isEmpty(withdrawRecord.getBankCardName())){
//                    ar.setSuccess(false);
//                    ar.setMessage("请选择银行！");
//                    ar.setErrorCode(CodeConstant.PARAM_ERROR);
//                    return ar;
//                }
//                if(withdrawRecord.getMoney()==null){
//                    ar.setSuccess(false);
//                    ar.setMessage("请输入提币金额");
//                    ar.setErrorCode(CodeConstant.PARAM_ERROR);
//                    return ar;
//                }
//                if(!Validator.isMoney2(withdrawRecord.getMoney().toString())){
//                    ar.setSuccess(false);
//                    ar.setMessage("提币金额格式有误，请按提示输入");
//                    ar.setErrorCode(CodeConstant.PARAM_ERROR);
//                    return ar;
//                }
//            }else{
                if(StringUtils.isEmpty(withdrawRecord.getAddress())){
                    return AjaxResponse.falied(messageSource.getMessage("addressNull"), CodeConstant.PARAM_ERROR);
                }
                if(String.valueOf(user.get("address")).equals(withdrawRecord.getAddress())){
                    return AjaxResponse.falied(messageSource.getMessage("ownAddressError"), CodeConstant.PARAM_ERROR);
                }
                if(withdrawRecord.getMoney()==null){
                    return AjaxResponse.falied(messageSource.getMessage("coinNumNull"), CodeConstant.PARAM_ERROR);
                }
                if(!Validator.isMoney4(withdrawRecord.getMoney().toString())){
                    return AjaxResponse.falied(messageSource.getMessage("coinNumError"), CodeConstant.PARAM_ERROR);
                }
                /***************************限额及手续费********start**************/
                String ltc_uplimit = "";
            	String ltc_lowlimit = "";
            	String btc_uplimit = "";
            	String btc_lowlimit = "";
            	String eth_uplimit = "";
            	String eth_lowlimit = "";
            	String san_uplimit = "";
            	String san_lowlimit = "";
            	int limit_count_day = 0;
                List<Map<String, String>> codeList = sysGencodeService.findByGroupCode("WITHDRAW_LIMIT");
                for(Map<String, String> mapObj:codeList){
                    if("LTC_LIMIT".equals(String.valueOf(mapObj.get("code_name")))){
                    	ltc_uplimit = mapObj.get("uplimit").toString();
                    	ltc_lowlimit = mapObj.get("lowlimit").toString();
                    }else if("BTC_LIMIT".equals(String.valueOf(mapObj.get("code_name")))){
                    	btc_uplimit = mapObj.get("uplimit").toString();
                    	btc_lowlimit = mapObj.get("lowlimit").toString();
                    }else if("ETH_LIMIT".equals(String.valueOf(mapObj.get("code_name")))){
                    	eth_uplimit = mapObj.get("uplimit").toString();
                    	eth_lowlimit = mapObj.get("lowlimit").toString();
                    }else if("SAN_LIMIT".equals(String.valueOf(mapObj.get("code_name")))){
                    	san_uplimit = mapObj.get("uplimit").toString();
                    	san_lowlimit = mapObj.get("lowlimit").toString();
                    }else if("LIMIT_COUNT_DAY".equals(String.valueOf(mapObj.get("code_name")))){
                    	limit_count_day = Integer.valueOf(String.valueOf(mapObj.get("code_value")));
                    }
                }
                String ltc_fee = "";//手续费（包含矿工费）
                String btc_fee = "";//手续费（包含矿工费）
                String eth_fee = "";//手续费（包含矿工费）
                String san_fee = "";//手续费（包含矿工费）
                List<Map<String, String>> codeList1 = sysGencodeService.findByGroupCode("WITHDRAW_SERVICE_CHARGE");
                for(Map<String, String> mapObj:codeList1){
                    if("LTC_FEE".equals(String.valueOf(mapObj.get("code_name")))){
                    	ltc_fee = mapObj.get("code_value").toString();
                    }else if("BTC_FEE".equals(String.valueOf(mapObj.get("code_name")))){
                    	btc_fee = mapObj.get("code_value").toString();
                    }else if("ETH_FEE".equals(String.valueOf(mapObj.get("code_name")))){
                    	eth_fee = mapObj.get("code_value").toString();
                    }else if("SAN_FEE".equals(String.valueOf(mapObj.get("code_name")))){
                    	san_fee = mapObj.get("code_value").toString();
                    }
                }
                /***************************限额及手续费*********end*************/
                if(Constant.CURRENCY_LTC.equals(withdrawRecord.getCurrency())){
                	if(withdrawRecord.getMoney().compareTo(new BigDecimal(ltc_lowlimit))<0){
//                    	return AjaxResponse.falied("LTC提币数量最低"+ltc_lowlimit, CodeConstant.PARAM_ERROR);
                    	return AjaxResponse.falied(messageSource.getMessage("coinLowLimit", "LTC",ltc_lowlimit), CodeConstant.PARAM_ERROR);
                    }
                    
                    if(!StringUtils.isEmpty(ltc_uplimit)&&withdrawRecord.getMoney().compareTo(new BigDecimal(ltc_uplimit))>0){
//                    	return AjaxResponse.falied("LTC每次提币不得超出"+ltc_uplimit, CodeConstant.PARAM_ERROR);
                    	return AjaxResponse.falied(messageSource.getMessage("coinUpLimit", "LTC",ltc_uplimit), CodeConstant.PARAM_ERROR);
                    }
                    withdrawRecord.setCost(new BigDecimal(ltc_fee));
                }else if(Constant.CURRENCY_BTC.equals(withdrawRecord.getCurrency())){
                	if(withdrawRecord.getMoney().compareTo(new BigDecimal(btc_lowlimit))<0){
//                		return AjaxResponse.falied("BTC提币数量最低"+btc_lowlimit, CodeConstant.PARAM_ERROR);
                		return AjaxResponse.falied(messageSource.getMessage("coinLowLimit", "BTC",btc_lowlimit), CodeConstant.PARAM_ERROR);
                    }
                    
                    if(!StringUtils.isEmpty(btc_uplimit)&&withdrawRecord.getMoney().compareTo(new BigDecimal(btc_uplimit))>0){
//                    	return AjaxResponse.falied("BTC每次提币不得超出"+btc_uplimit, CodeConstant.PARAM_ERROR);
                    	return AjaxResponse.falied(messageSource.getMessage("coinUpLimit", "BTC",btc_uplimit), CodeConstant.PARAM_ERROR);
                    }
                    withdrawRecord.setCost(new BigDecimal(btc_fee));
                }else if(Constant.CURRENCY_ETH.equals(withdrawRecord.getCurrency())){
                	if(withdrawRecord.getMoney().compareTo(new BigDecimal(eth_lowlimit))<0){
//                		return AjaxResponse.falied("ETH提币数量最低"+eth_lowlimit, CodeConstant.PARAM_ERROR);
                		return AjaxResponse.falied(messageSource.getMessage("coinLowLimit", "ETH",eth_lowlimit), CodeConstant.PARAM_ERROR);
                	}
                    
                    if(!StringUtils.isEmpty(eth_uplimit)&&withdrawRecord.getMoney().compareTo(new BigDecimal(eth_uplimit))>0){
//                    	return AjaxResponse.falied("ETH每次提币不得超出"+eth_uplimit, CodeConstant.PARAM_ERROR);
                    	return AjaxResponse.falied(messageSource.getMessage("coinUpLimit", "ETH",eth_uplimit), CodeConstant.PARAM_ERROR);
                    }
                    withdrawRecord.setCost(new BigDecimal(eth_fee));
                }else if(Constant.CURRENCY_SAN.equals(withdrawRecord.getCurrency())){
                	if(withdrawRecord.getMoney().compareTo(new BigDecimal(san_lowlimit))<0){
//                		return AjaxResponse.falied("SAN提币数量最低"+san_lowlimit, CodeConstant.PARAM_ERROR);
                		return AjaxResponse.falied(messageSource.getMessage("coinLowLimit", "SAN",san_lowlimit), CodeConstant.PARAM_ERROR);
                	}
                    
                    if(!StringUtils.isEmpty(san_uplimit)&&withdrawRecord.getMoney().compareTo(new BigDecimal(san_uplimit))>0){
//                    	return AjaxResponse.falied("SAN每次提币不得超出"+san_uplimit, CodeConstant.PARAM_ERROR);
                    	return AjaxResponse.falied(messageSource.getMessage("coinUpLimit", "SAN",san_uplimit), CodeConstant.PARAM_ERROR);
                    }
                    //三界宝提币手续费0.1%
                    withdrawRecord.setCost(withdrawRecord.getMoney().multiply(new BigDecimal(san_fee)).divide(new BigDecimal(100), 6, BigDecimal.ROUND_HALF_UP));
                }
//            }
            
            if(StringUtils.isEmpty(withdrawRecord.getTransPassword())){
                return AjaxResponse.falied(messageSource.getMessage("transPasswordNull"), CodeConstant.PARAM_ERROR);
            }
            
            if(StringUtils.isEmpty(withdrawRecord.getVcode())){
                return AjaxResponse.falied(messageSource.getMessage("smsVcodeNull"), CodeConstant.PARAM_ERROR);
            }
            
            UserWallet userWallet = userWalletService.getWalletByUserId(userId);
            
            if("BTC".equals(withdrawRecord.getCurrency())){
                if(withdrawRecord.getMoney().compareTo(userWallet.getBtcAmnt())>0){
                    return AjaxResponse.falied(messageSource.getMessage("balanceError","BTC"), CodeConstant.BALANCE_NOT_ENOUGH);
                }
            }else if("LTC".equals(withdrawRecord.getCurrency())){//提币手续费每次0.03
                if(withdrawRecord.getMoney().compareTo(userWallet.getLtcAmnt())>0){
                    return AjaxResponse.falied(messageSource.getMessage("balanceError","LTC"), CodeConstant.BALANCE_NOT_ENOUGH);
                }
            }else if("ETH".equals(withdrawRecord.getCurrency())){
                if(withdrawRecord.getMoney().compareTo(userWallet.getEthAmnt())>0){
                    return AjaxResponse.falied(messageSource.getMessage("balanceError","ETH"), CodeConstant.BALANCE_NOT_ENOUGH);
                }
            }else if("SAN".equals(withdrawRecord.getCurrency())){
                if(withdrawRecord.getMoney().compareTo(userWallet.getSanAmnt())>0){
                    return AjaxResponse.falied(messageSource.getMessage("balanceError","SAN"), CodeConstant.BALANCE_NOT_ENOUGH);
                }
            }else if("RMB".equals(withdrawRecord.getCurrency())){
                if(withdrawRecord.getMoney().compareTo(userWallet.getMoney())>0){
                    return AjaxResponse.falied(messageSource.getMessage("balanceError","RMB"), CodeConstant.BALANCE_NOT_ENOUGH);
                }
            }else if("ETC".equals(withdrawRecord.getCurrency())){
                if(withdrawRecord.getMoney().compareTo(userWallet.getEtcAmnt())>0){
                    return AjaxResponse.falied(messageSource.getMessage("balanceError","ETC"), CodeConstant.BALANCE_NOT_ENOUGH);
                }
            }else if("HLC".equals(withdrawRecord.getCurrency())){
                if(withdrawRecord.getMoney().compareTo(userWallet.getHlcAmnt())>0){
                    return AjaxResponse.falied(messageSource.getMessage("balanceError","HLC"), CodeConstant.BALANCE_NOT_ENOUGH);
                }
            }
            String transPassword = MD5Util.getMd5Code(withdrawRecord.getTransPassword());
            Boolean existTransPassword = usersDetailsService.isExistTransPassword(userId,transPassword);
            if(!existTransPassword){
                return AjaxResponse.falied(messageSource.getMessage("transPasswordError"), CodeConstant.PARAM_ERROR);
            }
            
            //半小时之内的短信验证码有效
            String tVcode =sendVodeService.findVcodeByPhone(String.valueOf(user.get("mobile_phone"))); 
            if(tVcode==null){
                return AjaxResponse.falied(messageSource.getMessage("vcodeFailure"), CodeConstant.OVERTIME_VCODE);
            }else if(!withdrawRecord.getVcode().equalsIgnoreCase(tVcode)){
                return AjaxResponse.falied(messageSource.getMessage("smsVcodeError"), CodeConstant.ERROR_VCODE);
            }
            //查询数据库提币次数配置
            int withDrawCount = withdrawRecordMapper.getWithDrawCountByDay(userId,withdrawRecord.getCurrency());
            if(limit_count_day == 0){
//            	return AjaxResponse.falied("提币功能暂未开通，敬请期待！", CodeConstant.PARAM_ERROR);
            	return AjaxResponse.falied(messageSource.getMessage("coinDisable"), CodeConstant.PARAM_ERROR);
            }
            if(withDrawCount >= limit_count_day){
//            	return AjaxResponse.falied("每日提币次数最多"+limit_count_day, CodeConstant.PARAM_ERROR);
            	return AjaxResponse.falied(messageSource.getMessage("coinMaxDay",limit_count_day), CodeConstant.PARAM_ERROR);
            }
            String orderNo =OrderGenerater.generateOrderNo();
            withdrawRecord.setUserId(userId);
            withdrawRecord.setOrderNo(orderNo);
            withdrawRecord.setStatus("0");
            //屏蔽前台传进来的矿工费
            withdrawRecord.setFree(null);
            /*if("SAN".equals(withdrawRecord.getCurrency())){
                withdrawRecord.setStatus("0"); //SAN单独跑现成处理调用三界链
            }else{
                withdrawRecord.setStatus("0");
            }*/
            //实际提币金额=申请金额-手续费
            
            if(withdrawRecordService.applyWithDrawal(withdrawRecord)){
//                return AjaxResponse.success("提币申请提交成功！", null);
                return AjaxResponse.success(messageSource.getMessage("coinSuccess"), null);
            }else{
//                return AjaxResponse.falied("提币申请提交失败！", CodeConstant.UPDATE_FAIL);
                return AjaxResponse.falied(messageSource.getMessage("coinFailure"), CodeConstant.UPDATE_FAIL);
            }
        } catch (Exception e) {
            e.printStackTrace();
//            return AjaxResponse.falied(messageSource.getMessage("sysException"), CodeConstant.SYS_ERROR);
            return AjaxResponse.falied(messageSource.getMessage("sysException"), CodeConstant.SYS_ERROR);
        }finally{
            logger.info("-----------提币申请------------end-----------");
        }   
    }
    /**
     * @describe:获取充值地址
     * @author: zhangchunming
     * @date: 2017年7月29日下午3:17:26
     * @param request
     * @param response
     * @return: AjaxResponse
     */
    @LoginVerify
    @GetMapping("/getRechangeAddress")
    @ApiOperation(nickname = "查询充值地址", value = "查询充值地址", notes = "查询充值地址")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "currency", value = "币种类型 如" +
                    "BTC ETC LTC SAN", required = true, paramType = "query", dataType = "currency")
    })
    public AjaxResponse getRechangeAddress(HttpServletRequest request,HttpServletResponse response){
        Map<String,Object> data = new HashMap<String,Object>();
        try {
            if(StringUtils.isEmpty(request.getParameter("currency"))) {
            	return AjaxResponse.falied(messageSource.getMessage("coinNumNull"), CodeConstant.PARAM_ERROR);
            }
            Map user = (Map) request.getSession().getAttribute("User");
            user.put("currency",request.getParameter("currency"));
            String address =this.userCoinMapper.findCoinAddresByCurrency(user);
            data.put("address", address);
            String src = QRCodes.createQRCode(address+"&"+request.getParameter("currency"), 150, "1");
            data.put("qcimg", src);
            return AjaxResponse.success(messageSource.getMessage("getSuccess"),data);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResponse.falied(messageSource.getMessage("sysException"));
        }
    }
    
    /**
     * @describe:查询账户余额
     * @author: zhangchunming
     * @date: 2016年11月1日下午7:20:54
     * @param request
     * @param response
     * @return: AjaxResponse
     */
    @LoginVerify
    @PostMapping("/getWalletByArray")
    @ApiOperation(nickname = "查询账户余额", value = "查询账户余额", notes = "查询账户余额")
    @ApiImplicitParams({
    })
    public AjaxResponse getWalletByArray(HttpServletRequest request,HttpServletResponse response){
        AjaxResponse ar = new AjaxResponse();
        Map<String,Object> data = new HashMap<String,Object>();
        try {
            JSONObject user = JSONObject.parseObject(JSON.toJSONString(request.getSession().getAttribute("User")));
             
            data = accService.getBalance(data, user) ;
            
            ar.setData(data);
            ar.setSuccess(true);
            ar.setMessage(messageSource.getMessage("getSuccess"));
        } catch (Exception e) {
            e.printStackTrace();
            ar.setSuccess(false);
            ar.setErrorCode(CodeConstant.SYS_ERROR);
            ar.setMessage(messageSource.getMessage("sysException"));
        }   
        return ar;
    }
    
    
    /**
     * @describe:转账
     * @author: zhangchunming
     * @date: 2016年11月1日下午7:25:39
     * @param request
     * @return: AjaxResponse
     */
    @LoginVerify
    @RequestMapping(value="/transferAccount", method=RequestMethod.POST)
    @PostMapping("/login")
    @ApiOperation(nickname = "转币", value = "转币", notes = "参数说明：relaAccount：对方账号；currency：币种名称，如BTC；money:转账金额；")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "transferRecord", value = "转币参数", required = true, paramType = "body", dataType = "TransferRecord")
    })
    public AjaxResponse transferAccount(HttpServletRequest request,@RequestBody @Valid TransferRecord transferRecord){
        logger.info("---------转币transferAccount---------start-----------");
        AjaxResponse ar = new AjaxResponse();
        Map<String,Object> data =  new HashMap<String,Object>();
        try {
            JSONObject user = JSONObject.parseObject(JSON.toJSONString(request.getSession().getAttribute("User")));
            transferRecord.setUserId(String.valueOf(user.get("user_id")));
            transferRecord.setAccount(user.getString("account"));
            String relaAccount = transferRecord.getRelaAccount()==null?"":transferRecord.getRelaAccount().trim();
            String currency = transferRecord.getCurrency()==null?"":transferRecord.getCurrency().trim();
            if(StringUtils.isEmpty(relaAccount)){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("otherAccountNull"));
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            if(transferRecord.getMoney() == null){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("moneyNull"));
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            if(!Validator.isMoney4(transferRecord.getMoney().toString())){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("moneyError"));
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            if(transferRecord.getMoney().compareTo(new BigDecimal("0"))==0){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("moneyNotZero"));
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            
            if(StringUtils.isEmpty(currency)){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("coinNameNull"));
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            
            if(!"BTC".equals(transferRecord.getCurrency())&&!"LTC".equals(transferRecord.getCurrency())
            		&&!"ETH".equals(transferRecord.getCurrency())&&!"SAN".equals(transferRecord.getCurrency())){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("coinNameError"));
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            
            if(!userLoginService.findIsExist(transferRecord.getRelaAccount())){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("otherAccountNotExist"));
                ar.setErrorCode(CodeConstant.ERROR_NO_ACCOUNT);
                return ar;
            }
//            UserLogin userLogin = userLoginService.getUserLoginByUserId(String.valueOf(user.get("user_id")));
            if(transferRecord.getRelaAccount().equals(user.get("account"))){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("NotRransferYourselfAccount"));
                ar.setErrorCode(CodeConstant.ERROR_DISABLE);
                return ar;
            }
            UserWallet userWallet = userWalletService.getWalletByUserId(String.valueOf(user.get("user_id")));
            if("BTC".equals(transferRecord.getCurrency())){//比特币转账
                if(transferRecord.getMoney().compareTo(userWallet.getBtcAmnt())>0){
                    ar.setSuccess(false);
                    ar.setMessage(messageSource.getMessage("underbalance","BTC"));
                    ar.setErrorCode(CodeConstant.BALANCE_NOT_ENOUGH);
                    return ar;
                }
            }else if("LTC".equals(transferRecord.getCurrency())){//提币手续费每次0.03
                if(transferRecord.getMoney().compareTo(userWallet.getLtcAmnt())>0){
                    ar.setSuccess(false);
                    ar.setMessage(messageSource.getMessage("underbalance","LTC"));
                    ar.setErrorCode(CodeConstant.BALANCE_NOT_ENOUGH);
                    return ar;
                }
            }else if("ETH".equals(transferRecord.getCurrency())){
                if(transferRecord.getMoney().compareTo(userWallet.getEtcAmnt())>0){
                    ar.setSuccess(false);
                    ar.setMessage(messageSource.getMessage("underbalance","ETH"));
                    ar.setErrorCode(CodeConstant.BALANCE_NOT_ENOUGH);
                    return ar;
                }
            }else if("ETC".equals(transferRecord.getCurrency())){
                if(transferRecord.getMoney().compareTo(userWallet.getEtcAmnt())>0){
                    ar.setSuccess(false);
                    ar.setMessage(messageSource.getMessage("underbalance","ETC"));
                    ar.setErrorCode(CodeConstant.BALANCE_NOT_ENOUGH);
                    return ar;
                }
            }else if("HLC".equals(transferRecord.getCurrency())){
                if(transferRecord.getMoney().compareTo(userWallet.getHlcAmnt())>0){
                    ar.setSuccess(false);
                    ar.setMessage(messageSource.getMessage("underbalance","HLC"));
                    ar.setErrorCode(CodeConstant.BALANCE_NOT_ENOUGH);
                    return ar;
                }
            }else if("SAN".equals(transferRecord.getCurrency())){
                if(transferRecord.getMoney().compareTo(userWallet.getSanAmnt())>0){
                    ar.setSuccess(false);
                    ar.setMessage(messageSource.getMessage("underbalance","SAN"));
                    ar.setErrorCode(CodeConstant.BALANCE_NOT_ENOUGH);
                    return ar;
                }
            }
            String orderNo =OrderGenerater.generateOrderNo();
            transferRecord.setOrderNo(orderNo);
            boolean transferResult = userWalletService.transferAccount(transferRecord);
            if(transferResult){
                data.put("orderNo", orderNo);
                data.put("createTime", DateUtil.dateToStamp(DateUtil.getCurrDateTime()));
                data.put("relaAccount", transferRecord.getRelaAccount());
                data.put("money", transferRecord.getMoney());
                data.put("currency", transferRecord.getCurrency());
                data.put("remark",transferRecord.getRemark());
                return AjaxResponse.success(messageSource.getMessage("transferSuccess"), data);
            }else{
                return AjaxResponse.falied(messageSource.getMessage("transferFailure"), CodeConstant.UPDATE_FAIL);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResponse.falied(messageSource.getMessage("sysException"), CodeConstant.SYS_ERROR);
        }finally{
            logger.info("-------------转币transferAccount-----------end-----------");
        }   
    }


    /**
     * @describe:减少钱包余额
     * @author: hanpeng
     * @date: 2017-8-7 20:59:13
     * @param request
     * @return: AjaxResponse
     */
    @PostMapping("/updateSub")
    @ApiOperation(nickname = "减少钱包余额", value = "减少钱包余额", notes = "减少钱包余额！")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "transferRecord", value = "参数", required = true, paramType = "body", dataType = "TransferRecord")
    })
    public AjaxResponse updateSub(HttpServletRequest request,@RequestBody @Valid TransferRecord transferRecord){
        logger.info("---------减少钱包余额----updateSub-----------");
        logger.info("x-auth-token :====" + request.getHeader("x-auth-token"));
        logger.info("sessionId == " + request.getSession().getId());
        AjaxResponse ar = new AjaxResponse();
        Map<String,Object> data =  new HashMap<String,Object>();
        try {
            JSONObject user = JSONObject.parseObject(JSON.toJSONString(request.getSession().getAttribute("User")));
            transferRecord.setUserId(String.valueOf(user.get("user_id")));
            transferRecord.setAccount(user.getString("account"));
            String currency = transferRecord.getCurrency()==null?"":transferRecord.getCurrency().trim();



            UserWallet userWallet = userWalletService.getWalletByUserId(String.valueOf(user.get("user_id")));

            // 为了防止更新所有字段
            UserWallet subUserWallet = new UserWallet();
            subUserWallet.setUserId(userWallet.getUserId());


            if("BTC".equals(currency)){//比特币转账
                if(transferRecord.getMoney().compareTo(userWallet.getBtcAmnt())>0){
                    ar.setSuccess(false);
                    ar.setMessage(messageSource.getMessage("underbalance","BTC"));
                    ar.setErrorCode(CodeConstant.BALANCE_NOT_ENOUGH);
                    return ar;
                }else {
                    // 设置要减少的金额
                    subUserWallet.setBtcAmnt(transferRecord.getMoney());
                }
            }

            if("ETH".equals(currency)){//以太坊转账
                if(transferRecord.getMoney().compareTo(userWallet.getEthAmnt())>0){
                    ar.setSuccess(false);
                    ar.setMessage(messageSource.getMessage("underbalance","ETH"));
                    ar.setErrorCode(CodeConstant.BALANCE_NOT_ENOUGH);
                    return ar;
                }else {
                    // 设置要减少的金额
                    subUserWallet.setEthAmnt(transferRecord.getMoney());
                }
            }

            if("LTC".equals(currency)){//莱特币转账
                if(transferRecord.getMoney().compareTo(userWallet.getLtcAmnt())>0){
                    ar.setSuccess(false);
                    ar.setMessage(messageSource.getMessage("underbalance","LTC"));
                    ar.setErrorCode(CodeConstant.BALANCE_NOT_ENOUGH);
                    return ar;
                }else {
                    // 设置要减少的金额
                    subUserWallet.setLtcAmnt(transferRecord.getMoney());
                }
            }

            if("SAN".equals(currency)){//小三币转账
                if(transferRecord.getMoney().compareTo(userWallet.getSanAmnt())>0){
                    ar.setSuccess(false);
                    ar.setMessage(messageSource.getMessage("underbalance","SAN"));
                    ar.setErrorCode(CodeConstant.BALANCE_NOT_ENOUGH);
                    return ar;
                }else {
                    // 设置要减少的金额
                    subUserWallet.setSanAmnt(transferRecord.getMoney());
                }
            }



            boolean updateSubResult = userWalletService.updateSub(subUserWallet);
            if(updateSubResult){
                data.put("money", transferRecord.getMoney());
                data.put("coin_name", currency);
                data.put("remark1",messageSource.getMessage("reduceBalance")+"-"+transferRecord.getMoney());//说明
                return AjaxResponse.success(messageSource.getMessage("reduceSuccess"), data);
            }else{
                return AjaxResponse.falied(messageSource.getMessage("reduceFailure"), CodeConstant.UPDATE_FAIL);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResponse.falied(messageSource.getMessage("sysException"), CodeConstant.SYS_ERROR);
        }finally{
            logger.info("-------------减少钱包余额----updateSub-----------end-----------");
        }
    }
    
    /**
     * @describe:获取钱包地址及金额
     * @author: zhangchunming
     * @date: 2017年7月31日下午8:25:40
     * @param request
     * @return: AjaxResponse
     */
    @LoginVerify
    @PostMapping("/getAddressAndBalance")
    @ApiOperation(nickname = "获取钱包地址及金额", value = "获取钱包地址及金额", notes = "获取钱包地址及金额")
    @ApiImplicitParams({
    })
    public AjaxResponse getAddressAndBalance(HttpServletRequest request){
        Map<String,Object> data = new HashMap<String,Object>();
        try {
            JSONObject user = JSONObject.parseObject(JSON.toJSONString(request.getSession().getAttribute("User")));
            String userId = String.valueOf(user.get("user_id"));
            List<Map<String, Object>> addressAndBalance = userWalletService.getAddressAndBalance(userId);
            for (int i=0;i<addressAndBalance.size();i++){
                if (i>0) {
                    addressAndBalance.get(i).remove("btc_amnt");
                    addressAndBalance.get(i).remove("ltc_amnt");
                    addressAndBalance.get(i).remove("eth_amnt");
                    addressAndBalance.get(i).remove("san_amnt");
                    addressAndBalance.get(i).remove("money");
                    addressAndBalance.get(i).remove("froze_btc_amnt");
                    addressAndBalance.get(i).remove("froze_etc_amnt");
                    addressAndBalance.get(i).remove("froze_ltc_amnt");
                    addressAndBalance.get(i).remove("froze_san_amnt");
                    addressAndBalance.get(i).remove("froze_rmb_amnt");
                }
            }
            data.put("addressAndBalance", addressAndBalance);
            return AjaxResponse.success(messageSource.getMessage("getSuccess"), data);
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResponse.falied(messageSource.getMessage("sysException"), CodeConstant.SYS_ERROR);
        }
    }
    
    
    /**
     * @describe:查询手续费
     * @author: zhangchunming
     * @date: 2017年7月31日下午8:25:40
     * @param request
     * @return: AjaxResponse
     */
    @LoginVerify
    @GetMapping("/getFee")
    @ApiOperation(nickname = "查询手续费", value = "查询手续费", notes = "查询手续费")
    @ApiImplicitParams({
    })
    public AjaxResponse getFee(HttpServletRequest request){
    	Map<String,Object> data = new HashMap<String,Object>();
    	try {
    		String ltc_fee = "";//手续费（包含矿工费）
            String btc_fee = "";//手续费（包含矿工费.）
            String eth_fee = "";//手续费（包含矿工费）
            String san_fee = "";//手续费（包含矿工费）
            List<Map<String, String>> codeList1 = sysGencodeService.findByGroupCode("WITHDRAW_SERVICE_CHARGE");
            for(Map<String, String> mapObj:codeList1){
                if("LTC_FEE".equals(String.valueOf(mapObj.get("code_name")))){
                	ltc_fee = mapObj.get("code_value").toString();
                }else if("BTC_FEE".equals(String.valueOf(mapObj.get("code_name")))){
                	btc_fee = mapObj.get("code_value").toString();
                }else if("ETH_FEE".equals(String.valueOf(mapObj.get("code_name")))){
                	eth_fee = mapObj.get("code_value").toString();
                }else if("SAN_FEE".equals(String.valueOf(mapObj.get("code_name")))){
                	san_fee = mapObj.get("code_value").toString();
                }
            }
    		data.put("ltc_fee", ltc_fee);
    		data.put("btc_fee", btc_fee);
    		data.put("eth_fee", eth_fee);
    		data.put("san_fee", san_fee);
    		return AjaxResponse.success(messageSource.getMessage("getSuccess"), data);
    	}catch (Exception e){
    		e.printStackTrace();
    		return AjaxResponse.falied(messageSource.getMessage("sysException"), CodeConstant.SYS_ERROR);
    	}
    }
    
    /**
     * @describe:判断是否拥有足够的余额
     * @author: zhangchunming
     * @date: 2017年7月31日下午8:25:40
     * @param request
     * @return: AjaxResponse
     */
    @LoginVerify
    @GetMapping("/isHasBalance/{coinName}/{money}")
    @ApiOperation(nickname = "判断是否拥有足够的余额", value = "判断是否拥有足够的余额", notes = "判断是否拥有足够的余额")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "coinName", value = "币种名称", required = true, paramType = "path", dataType = "String"),
    	@ApiImplicitParam(name = "money", value = "金额或者币种数量", required = true, paramType = "path", dataType = "String")
    })
    public AjaxResponse isHasBalance(HttpServletRequest request,@PathVariable("coinName") String coinName,@PathVariable("money") String money){
    	try {
    		JSONObject user = JSONObject.parseObject(JSON.toJSONString(request.getSession().getAttribute("User")));
            String userId = String.valueOf(user.get("user_id"));
            logger.info("----coinName="+coinName);
            UserWallet userWallet = userWalletService.getWalletByUserId(userId);
            if("RMB".equals(coinName)){
                if(new BigDecimal(money).compareTo(userWallet.getMoney())>0){
                    return AjaxResponse.falied(messageSource.getMessage("balanceError","RMB"), CodeConstant.BALANCE_NOT_ENOUGH);
                }
            }else if("BTC".equals(coinName)){
                if(new BigDecimal(money).compareTo(userWallet.getBtcAmnt())>0){
                    return AjaxResponse.falied(messageSource.getMessage("balanceError","BTC"), CodeConstant.BALANCE_NOT_ENOUGH);
                }
            }else if("LTC".equals(coinName)){
                if(new BigDecimal(money).compareTo(userWallet.getLtcAmnt())>0){
                	return AjaxResponse.falied(messageSource.getMessage("balanceError","LTC"), CodeConstant.BALANCE_NOT_ENOUGH);
                }
            }else if("ETH".equals(coinName)){
                if(new BigDecimal(money).compareTo(userWallet.getEthAmnt())>0){
                    return AjaxResponse.falied(messageSource.getMessage("balanceError","ETH"), CodeConstant.BALANCE_NOT_ENOUGH);
                }
            }else if("ETC".equals(coinName)){
            	if(new BigDecimal(money).compareTo(userWallet.getEtcAmnt())>0){
                    return AjaxResponse.falied(messageSource.getMessage("balanceError","ETC"), CodeConstant.BALANCE_NOT_ENOUGH);
                }
            }else if("HLC".equals(coinName)){
            	if(new BigDecimal(money).compareTo(userWallet.getHlcAmnt())>0){
                    return AjaxResponse.falied(messageSource.getMessage("balanceError","HLC"), CodeConstant.BALANCE_NOT_ENOUGH);
                }
            }else if("SAN".equals(coinName)){
            	if(new BigDecimal(money).compareTo(userWallet.getSanAmnt())>0){
                    return AjaxResponse.falied(messageSource.getMessage("balanceError","SAN"), CodeConstant.BALANCE_NOT_ENOUGH);
                }
            }else{
            	return AjaxResponse.falied(messageSource.getMessage("paramError"), CodeConstant.PARAM_ERROR);
            }
            
            return AjaxResponse.success(messageSource.getMessage("balanceEnough",coinName), null);
    	}catch (Exception e){
    		e.printStackTrace();
    		return AjaxResponse.falied(messageSource.getMessage("sysException"), CodeConstant.SYS_ERROR);
    	}
    }
    
    /**
     * 查询提币功能是否开通
     * @param request
     * @throws Exception
     */
    @GetMapping("/isEnableWithDraw")
    @ApiOperation(nickname = "查询提币功能是否开通", value = "提币功能是否开通", notes = "提币功能是否开通")
    /*@ApiImplicitParams({
    	@ApiImplicitParam(name = "id", value = "主键", required = true, paramType = "query", dataType = "String"),
    	@ApiImplicitParam(name = "is_default", value = "1为默认此卡", required = true, paramType = "query", dataType = "String")
    })*/
    public AjaxResponse isEnableWithDraw(HttpServletRequest request)throws Exception{
    	int limit_count_day = 0;
        List<Map<String, String>> codeList = sysGencodeService.findByGroupCode("WITHDRAW_LIMIT");
        for(Map<String, String> mapObj:codeList){
            if("LIMIT_COUNT_DAY".equals(String.valueOf(mapObj.get("code_name")))){
            	limit_count_day = Integer.valueOf(String.valueOf(mapObj.get("code_value")));
            }
        }
        if(limit_count_day == 0){
        	return AjaxResponse.falied(messageSource.getMessage("coinDisable"), CodeConstant.PARAM_ERROR);
        }else{
        	return AjaxResponse.success(messageSource.getMessage("coinEnable"), null);
        }
    }
    
}
