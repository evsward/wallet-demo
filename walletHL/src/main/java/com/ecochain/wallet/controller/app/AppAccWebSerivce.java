package com.ecochain.wallet.controller.app;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ecochain.annotation.AppLoginVerify;
import com.ecochain.base.BaseWebService;
import com.ecochain.constant.CodeConstant;
import com.ecochain.constant.Constant;
import com.ecochain.core.config.CoreMessageSource;
import com.ecochain.util.DateUtil;
import com.ecochain.util.MD5Util;
import com.ecochain.util.OrderGenerater;
import com.ecochain.util.Validator;
import com.ecochain.wallet.controller.AjaxResponse;
import com.ecochain.wallet.entity.AccRecordDTO;
import com.ecochain.wallet.entity.DigitalCoin;
import com.ecochain.wallet.entity.ExchangeRecord;
import com.ecochain.wallet.entity.PageData;
import com.ecochain.wallet.entity.TransferRecord;
import com.ecochain.wallet.entity.UserBank;
import com.ecochain.wallet.entity.UserCoin;
import com.ecochain.wallet.entity.UserLogin;
import com.ecochain.wallet.entity.UserWallet;
import com.ecochain.wallet.entity.WithdrawRecord;
import com.ecochain.wallet.mapper.UserCoinMapper;
import com.ecochain.wallet.mapper.WithdrawRecordMapper;
import com.ecochain.wallet.service.AccRecordService;
import com.ecochain.wallet.service.AccService;
import com.ecochain.wallet.service.DigitalCoinService;
import com.ecochain.wallet.service.ExchangeRecordService;
import com.ecochain.wallet.service.SendVodeService;
import com.ecochain.wallet.service.SysGencodeService;
import com.ecochain.wallet.service.UserBankService;
import com.ecochain.wallet.service.UserLoginService;
import com.ecochain.wallet.service.UserWalletService;
import com.ecochain.wallet.service.UsersDetailsService;
import com.ecochain.wallet.service.WithdrawRecordService;
import com.github.pagehelper.PageInfo;

/**
 * 账户控制类
 * @author zhangchunming
 */
@RestController
@RequestMapping("/api/rest/acc")
@Api(value = "APP账户管理")
public class AppAccWebSerivce extends BaseWebService{
    
    private Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private SysGencodeService sysGencodeService;
    /*@Autowired
    private ISessionUtil SessionUtil;*/
    @Autowired
    private UserWalletService userWalletService;
    @Autowired
    private UsersDetailsService usersDetailsService;
    
    @Autowired
    private AccService accService;
    
    @Autowired
    private UserLoginService userLoginService;
    @Autowired
    private DigitalCoinService digitalCoinService;
    @Autowired
    private SendVodeService sendVodeService;
    @Autowired
    private UserBankService userBankService;
    @Autowired
    private AccRecordService accRecordService;
    @Autowired
    private WithdrawRecordService withdrawRecordService;
    @Autowired
    private ExchangeRecordService exchangeRecordService;
    @Autowired
    private UserCoinMapper userCoinMapper;
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
    @AppLoginVerify
    @PostMapping("/listPageAcc")
    @ApiOperation(nickname = "账户流水", value = "账户流水", notes = "账户流水")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", value = "当前页", required = false, paramType = "query", dataType = "String")
    })
    public AjaxResponse listPageAcc(HttpServletRequest request){
        AjaxResponse ar = new AjaxResponse();
        Map<String,Object> data = new HashMap<String, Object>();
        PageData pd = new PageData();
        pd = this.getPageData();
        try {
            JSONObject user = JSONObject.parseObject(JSON.toJSONString(request.getSession().getAttribute("User")));
            pd.put("user_id", user.get("user_id"));
            List<AccRecordDTO> listPageAcc = accRecordService.listPageAccRecord(pd.getPage(),null,String.valueOf(user.get("user_id")),"");
            List<PageData> accList = new ArrayList<PageData>();
            for(AccRecordDTO accRecordDTO:listPageAcc){
                PageData tpd = new PageData();
                tpd.put("id", accRecordDTO.getId());
                tpd.put("rmb_amnt", accRecordDTO.getMoney());
                tpd.put("status", accRecordDTO.getStatus());
                tpd.put("coin_name", accRecordDTO.getCurrency());
                tpd.put("otherno", accRecordDTO.getOrderNo());
                tpd.put("create_time", accRecordDTO.getCreateTime());
                tpd.put("caldate", accRecordDTO.getCreateTime());
                tpd.put("rela_account", accRecordDTO.getRelaAccount());
                tpd.put("buy_in_out", accRecordDTO.getTradeInOut());
                tpd.put("coin_rate", accRecordDTO.getPrice());
                tpd.put("coin_amnt", accRecordDTO.getAmnt());
                tpd.put("type", accRecordDTO.getType());
                tpd.put("hash", accRecordDTO.getHash());
                tpd.put("other_account", accRecordDTO.getRelaAccount());
                tpd.put("address", accRecordDTO.getAddress());
                tpd.put("remark1", accRecordDTO.getRemark1());
                accList.add(tpd);
//                otherno,caldate,acc_type,acc_name,rmb_amnt,coin_name,coin_rate,coin_amnt,buy_in_out,remark1,create_time,id,hash,other_account 
            }
            data.put("pageInfo", new PageInfo<PageData>(accList));
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
    
    
    @AppLoginVerify
    @PostMapping("/withdrawal")
    @ApiOperation(nickname = "提币", value = "提币", notes = "提币")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "coin_name", value = "币种名称", required = true, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "address", value = "提币地址", required = false, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "revbankaccno", value = "银行账号", required = false, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "revbankdepname", value = "开户行", required = false, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "amount", value = "提币数量", required = true, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "network_fee", value = "网络手续费", required = false, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "trans_password", value = "资金密码", required = true, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "vcode", value = "验证码", required = true, paramType = "query", dataType = "String")
    })
    public AjaxResponse withdrawal(HttpServletRequest request){
        AjaxResponse ar = new AjaxResponse();
        try {
            PageData pd = new PageData();
            pd = this.getPageData();
            logger.info("**************提币*******pd value is "+pd.toString());
            JSONObject user = JSONObject.parseObject(JSON.toJSONString(request.getSession().getAttribute("User")));
            pd.put("account", user.getString("account"));
            pd.put("user_name", user.getString("user_name"));
            pd.put("user_id", String.valueOf(user.get("user_id")));
//            String userType = user.getString("user_type");
            if(StringUtils.isEmpty(pd.getString("coin_name"))){
                return AjaxResponse.falied(messageSource.getMessage("coinNameNull"), CodeConstant.PARAM_ERROR);
            }
            if(!"BTC".equals(pd.getString("coin_name"))&&!"LTC".equals(pd.getString("coin_name"))
            		&&!"ETH".equals(pd.getString("coin_name"))&&!"SAN".equals(pd.getString("coin_name"))){
            	return AjaxResponse.falied(messageSource.getMessage("coinNameError"), CodeConstant.PARAM_ERROR);
            }
            /*if("RMB".equals(pd.getString("coin_name"))){
                if(StringUtils.isEmpty(pd.getString("revbankaccno"))||StringUtils.isEmpty(pd.getString("revbankdepname"))){
                    ar.setSuccess(false);
                    ar.setMessage("请选择银行！");
                    ar.setErrorCode(CodeConstant.PARAM_ERROR);
                    return ar;
                }
                if(StringUtils.isEmpty(pd.getString("amount"))){
                    ar.setSuccess(false);
                    ar.setMessage("请输入提币金额");
                    ar.setErrorCode(CodeConstant.PARAM_ERROR);
                    return ar;
                }
                if(!Validator.isMoney2(pd.getString("amount"))){
                    ar.setSuccess(false);
                    ar.setMessage("提币金额格式有误，请按提示输入");
                    ar.setErrorCode(CodeConstant.PARAM_ERROR);
                    return ar;
                }
            }else{*/
                if(StringUtils.isEmpty(pd.getString("address"))){
                	return AjaxResponse.falied(messageSource.getMessage("addressNull"), CodeConstant.PARAM_ERROR);
                }
                
                if(user.getString("address").equals(pd.getString("address"))){
                	return AjaxResponse.falied(messageSource.getMessage("ownAddressError"), CodeConstant.PARAM_ERROR);
                }
                if(StringUtils.isEmpty(pd.getString("amount"))){
                	return AjaxResponse.falied(messageSource.getMessage("coinNumNull"), CodeConstant.PARAM_ERROR);
                }
                if(!Validator.isMoney4(pd.getString("amount"))){
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
                if(Constant.CURRENCY_LTC.equals(pd.getString("coin_name"))){
                	if(new BigDecimal(pd.getString("amount")).compareTo(new BigDecimal(ltc_lowlimit))<0){
//                    	return AjaxResponse.falied("LTC提币数量最低"+ltc_lowlimit, CodeConstant.PARAM_ERROR);
                    	return AjaxResponse.falied(messageSource.getMessage("coinLowLimit", "LTC",ltc_lowlimit), CodeConstant.PARAM_ERROR);
                	}
                    
                    if(!StringUtils.isEmpty(ltc_uplimit)&&new BigDecimal(pd.getString("amount")).compareTo(new BigDecimal(ltc_uplimit))>0){
//                    	return AjaxResponse.falied("LTC每次提币不得超出"+ltc_uplimit, CodeConstant.PARAM_ERROR);
                    	return AjaxResponse.falied(messageSource.getMessage("coinUpLimit", "LTC",ltc_uplimit), CodeConstant.PARAM_ERROR);
                    }
                    pd.put("cost", new BigDecimal(ltc_fee));
                }else if(Constant.CURRENCY_BTC.equals(pd.getString("coin_name"))){
                	if(new BigDecimal(pd.getString("amount")).compareTo(new BigDecimal(btc_lowlimit))<0){
//                		return AjaxResponse.falied("BTC提币数量最低"+btc_lowlimit, CodeConstant.PARAM_ERROR);
                		return AjaxResponse.falied(messageSource.getMessage("coinLowLimit", "BTC",btc_lowlimit), CodeConstant.PARAM_ERROR);
                	}
                    
                    if(!StringUtils.isEmpty(btc_uplimit)&&new BigDecimal(pd.getString("amount")).compareTo(new BigDecimal(btc_uplimit))>0){
//                    	return AjaxResponse.falied("BTC每次提币不得超出"+btc_uplimit, CodeConstant.PARAM_ERROR);
                    	return AjaxResponse.falied(messageSource.getMessage("coinUpLimit", "BTC",btc_uplimit), CodeConstant.PARAM_ERROR);
                    }
                    pd.put("cost", new BigDecimal(btc_fee));
                }else if(Constant.CURRENCY_ETH.equals(pd.getString("coin_name"))){
                	if(new BigDecimal(pd.getString("amount")).compareTo(new BigDecimal(eth_lowlimit))<0){
//                		return AjaxResponse.falied("ETH提币数量最低"+eth_lowlimit, CodeConstant.PARAM_ERROR);
                		return AjaxResponse.falied(messageSource.getMessage("coinLowLimit", "ETH",eth_lowlimit), CodeConstant.PARAM_ERROR);
                    }
                    
                    if(!StringUtils.isEmpty(eth_uplimit)&&new BigDecimal(pd.getString("amount")).compareTo(new BigDecimal(eth_uplimit))>0){
//                    	return AjaxResponse.falied("ETH每次提币不得超出"+eth_uplimit, CodeConstant.PARAM_ERROR);
                    	return AjaxResponse.falied(messageSource.getMessage("coinUpLimit", "ETH",eth_uplimit), CodeConstant.PARAM_ERROR);
                    }
                    pd.put("cost", new BigDecimal(eth_fee));
                }else if(Constant.CURRENCY_SAN.equals(pd.getString("coin_name"))){
                	if(new BigDecimal(pd.getString("amount")).compareTo(new BigDecimal(san_lowlimit))<0){
//                		return AjaxResponse.falied("SAN提币数量最低"+san_lowlimit, CodeConstant.PARAM_ERROR);
                		return AjaxResponse.falied(messageSource.getMessage("coinLowLimit", "SAN",san_lowlimit), CodeConstant.PARAM_ERROR);
                    }
                    
                    if(!StringUtils.isEmpty(san_uplimit)&&new BigDecimal(pd.getString("amount")).compareTo(new BigDecimal(san_uplimit))>0){
//                    	return AjaxResponse.falied("SAN每次提币不得超出"+san_uplimit, CodeConstant.PARAM_ERROR);
                    	return AjaxResponse.falied(messageSource.getMessage("coinUpLimit", "SAN",san_uplimit), CodeConstant.PARAM_ERROR);
                    }
                    //三界宝提币手续费0.1%
                    pd.put("cost", new BigDecimal(pd.getString("amount")).multiply(new BigDecimal(san_fee)).divide(new BigDecimal(100), 6, BigDecimal.ROUND_HALF_UP));
                }
//            }
            
            if(StringUtils.isEmpty(pd.getString("trans_password"))){
            	return AjaxResponse.falied(messageSource.getMessage("transPasswordNull"), CodeConstant.PARAM_ERROR);
            }
            
            if(StringUtils.isEmpty(pd.getString("vcode"))){
            	return AjaxResponse.falied(messageSource.getMessage("smsVcodeNull"), CodeConstant.PARAM_ERROR);
            }
            
            
            UserWallet userWallet = userWalletService.getWalletByUserId(String.valueOf(user.get("user_id")));
            
            if("RMB".equals(pd.getString("coin_name"))){
                if((new BigDecimal(pd.getString("amount"))).compareTo(userWallet.getMoney())>0){
                	return AjaxResponse.falied(messageSource.getMessage("balanceError","RMB"), CodeConstant.BALANCE_NOT_ENOUGH);
                }
            }else if("BTC".equals(pd.getString("coin_name"))){
                if((new BigDecimal(pd.getString("amount"))).compareTo(userWallet.getBtcAmnt())>0){
                	return AjaxResponse.falied(messageSource.getMessage("balanceError","BTC"), CodeConstant.BALANCE_NOT_ENOUGH);
                }
            }else if("LTC".equals(pd.getString("coin_name"))){
                if((new BigDecimal(pd.getString("amount"))).compareTo(userWallet.getLtcAmnt())>0){
                	return AjaxResponse.falied(messageSource.getMessage("balanceError","LTB"), CodeConstant.BALANCE_NOT_ENOUGH);
                }
            }else if("ETH".equals(pd.getString("coin_name"))){
                if((new BigDecimal(pd.getString("amount"))).compareTo(userWallet.getEthAmnt())>0){
                	return AjaxResponse.falied(messageSource.getMessage("balanceError","ETH"), CodeConstant.BALANCE_NOT_ENOUGH);
                }
            }else if("ETC".equals(pd.getString("coin_name"))){
                if((new BigDecimal(pd.getString("amount"))).compareTo(userWallet.getEtcAmnt())>0){
                	return AjaxResponse.falied(messageSource.getMessage("balanceError","ETC"), CodeConstant.BALANCE_NOT_ENOUGH);
                }
            }else if("HLC".equals(pd.getString("coin_name"))){
                if((new BigDecimal(pd.getString("amount"))).compareTo(userWallet.getHlcAmnt())>0){
                	return AjaxResponse.falied(messageSource.getMessage("balanceError","HLC"), CodeConstant.BALANCE_NOT_ENOUGH);
                }
            }else if("SAN".equals(pd.getString("coin_name"))){
                if((new BigDecimal(pd.getString("amount"))).compareTo(userWallet.getSanAmnt())>0){
                	return AjaxResponse.falied(messageSource.getMessage("balanceError","SAN"), CodeConstant.BALANCE_NOT_ENOUGH);
                }
            }
            String transPassword = MD5Util.getMd5Code(pd.getString("trans_password"));
            Boolean existTransPassword = usersDetailsService.isExistTransPassword(String.valueOf(user.get("user_id")),transPassword);
            if(!existTransPassword){
            	return AjaxResponse.falied(messageSource.getMessage("transPasswordError"), CodeConstant.BALANCE_NOT_ENOUGH);
            }
            
            //半小时之内的短信验证码有效
            String tVcode =sendVodeService.findVcodeByPhone(user.getString("account")); 
            if(tVcode==null||!pd.getString("vcode").equals(tVcode)){
            	return AjaxResponse.falied(messageSource.getMessage("smsVcodeError"), CodeConstant.ERROR_VCODE);
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
            
            //查询数据库提币次数配置
            int withDrawCount = withdrawRecordMapper.getWithDrawCountByDay(String.valueOf(user.get("user_id")),pd.getString("coin_name"));
            if(limit_count_day == 0){
//            	return AjaxResponse.falied("提币功能暂未开通，敬请期待！", CodeConstant.PARAM_ERROR);
            	return AjaxResponse.falied(messageSource.getMessage("coinDisable"), CodeConstant.PARAM_ERROR);
            }
            if(withDrawCount >= limit_count_day){
//            	return AjaxResponse.falied("每日提币次数最多"+limit_count_day, CodeConstant.PARAM_ERROR);
            	return AjaxResponse.falied(messageSource.getMessage("coinMaxDay",limit_count_day), CodeConstant.PARAM_ERROR);
            }
            
            String orderNo =OrderGenerater.generateOrderNo();
            WithdrawRecord withdrawRecord = new WithdrawRecord();
            withdrawRecord.setUserId(String.valueOf(user.get("user_id")));
            withdrawRecord.setOrderNo(orderNo);
            withdrawRecord.setCurrency(pd.getString("coin_name"));
            withdrawRecord.setAddress(pd.getString("address"));
            withdrawRecord.setBankCardName(pd.getString("revbankdepname"));
            withdrawRecord.setBankCardNo(pd.getString("revbankaccno"));
            withdrawRecord.setMoney(new BigDecimal(pd.getString("amount")));
//            withdrawRecord.setCost(new BigDecimal(pd.getString("network_fee")==null?"0":pd.getString("network_fee")));
            withdrawRecord.setCost((BigDecimal)pd.get("cost"));
            withdrawRecord.setByUserId(String.valueOf(user.get("user_id")));
            withdrawRecord.setStatus("0");
            /*if("SAN".equals(pd.getString("coin_name"))){
             withdrawRecord.setStatus("2"); //SAN单独跑现成处理调用三界链
            }else{
                withdrawRecord.setStatus("0");
            }*/
            
            if(withdrawRecordService.applyWithDrawal(withdrawRecord)){
                return AjaxResponse.success(messageSource.getMessage("coinSuccess"), null);
            }else{
            	return AjaxResponse.falied(messageSource.getMessage("coinFailure"), CodeConstant.UPDATE_FAIL);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResponse.falied(messageSource.getMessage("sysException"), CodeConstant.SYS_ERROR);
        }finally{
            logger.info("-----------提币申请------------end-----------");
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
    @AppLoginVerify
    @PostMapping("/getWallet")
    @ApiOperation(nickname = "查询账户余额", value = "查询账户余额", notes = "查询账户余额")
    @ApiImplicitParams({
    })
    public AjaxResponse getWallet(HttpServletRequest request,HttpServletResponse response){
        AjaxResponse ar = new AjaxResponse();
        Map<String,Object> data = new HashMap<String,Object>();
        try {
            JSONObject user = JSONObject.parseObject(JSON.toJSONString(request.getSession().getAttribute("User")));
            UserWallet tuserWallet = userWalletService.getWalletByUserId(String.valueOf(user.get("user_id")));
//            BigDecimal totalMoney = new BigDecimal(String.valueOf(tuserWallet.getMoney()));
            BigDecimal totalMoney = BigDecimal.ZERO;
            List<DigitalCoin> listCoin = digitalCoinService.getAllCoinPrice();
//            BigDecimal hlc_money = new BigDecimal("0");
            BigDecimal btc_money = new BigDecimal("0");
            BigDecimal ltc_money = new BigDecimal("0");
            BigDecimal eth_money = new BigDecimal("0");
            BigDecimal san_money = new BigDecimal("0");
//            BigDecimal etc_money = new BigDecimal("0");
            
            PageData tpd = new PageData();//返给前端
            tpd.put("btc_amnt", tuserWallet.getBtcAmnt());
            tpd.put("ltc_amnt", tuserWallet.getLtcAmnt());
            tpd.put("eth_amnt", tuserWallet.getEthAmnt());
            tpd.put("san_amnt", tuserWallet.getSanAmnt());
//            tpd.put("etc_amnt", tuserWallet.getEtcAmnt());
//            tpd.put("hlc_amnt", tuserWallet.getHlcAmnt());
//            tpd.put("money", tuserWallet.getMoney());
//            tpd.put("froze_rmb_amnt", tuserWallet.getFrozeRmbAmnt());
//            tpd.put("froze_hlc_amnt", tuserWallet.getHlcAmnt());
            tpd.put("froze_btc_amnt", tuserWallet.getBtcAmnt());
            tpd.put("froze_ltc_amnt", tuserWallet.getLtcAmnt());
            tpd.put("froze_eth_amnt", tuserWallet.getEthAmnt());
            tpd.put("froze_san_amnt", tuserWallet.getFrozeSanAmnt());
//            tpd.put("froze_etc_amnt", tuserWallet.getEtcAmnt());
            
            String btcAddress = "",ltcAddress = "",ethAddress = "",sanAddress = "";
            List<UserCoin> userCoinList = userCoinMapper.getCoinListByUserId(String.valueOf(user.get("user_id")));
            for(UserCoin userCoin:userCoinList){
            	if("BTC".equals(userCoin.getCurrency())){
            		btcAddress = userCoin.getAddress();
            	}else if("LTC".equals(userCoin.getCurrency())){
            		ltcAddress = userCoin.getAddress();
            	}else if("ETH".equals(userCoin.getCurrency())){
            		ethAddress = userCoin.getAddress();
            	}else if("SAN".equals(userCoin.getCurrency())){
            		sanAddress = userCoin.getAddress();
            	}
            }
            
            for(DigitalCoin coin:listCoin){
                /*if("HLC".equals(coin.getCoinName())){//合链币
                    String coinPrice  = coin.getCoinRate().split(":")[0];
                    tpd.put("hlc_rate", coinPrice);
                    hlc_money = tuserWallet.getHlcAmnt().multiply(new BigDecimal(coinPrice));
                    totalMoney = hlc_money.add(totalMoney);
                }else */if("BTC".equals(coin.getCoinName())){
                    String coinPrice  = coin.getCoinRate().split(":")[0];
                    tpd.put("btc_rate", coinPrice);
                    btc_money = tuserWallet.getBtcAmnt().multiply(new BigDecimal(coinPrice));
                    totalMoney = btc_money.add(totalMoney);
                    tpd.put("btc_address", btcAddress);
                }else if("LTC".equals(coin.getCoinName())){
                    String coinPrice  = coin.getCoinRate().split(":")[0];
                    tpd.put("ltc_rate", coinPrice);
                    ltc_money = tuserWallet.getLtcAmnt().multiply(new BigDecimal(coinPrice));
                    totalMoney = ltc_money.add(totalMoney);
                    tpd.put("ltc_address", ltcAddress);
                }else if("ETH".equals(coin.getCoinName())){
                    String coinPrice  = coin.getCoinRate().split(":")[0];
                    tpd.put("eth_rate", coinPrice);
                    eth_money = tuserWallet.getEthAmnt().multiply(new BigDecimal(coinPrice));
                    totalMoney = eth_money.add(totalMoney);
                    tpd.put("eth_address", ethAddress);
                }else if("SAN".equals(coin.getCoinName())){
                    String coinPrice  = coin.getCoinRate().split(":")[0];
                    tpd.put("san_rate", coinPrice);
                    san_money = tuserWallet.getSanAmnt().multiply(new BigDecimal(coinPrice));
                    totalMoney = san_money.add(totalMoney);
                    tpd.put("san_address", sanAddress);
                }/*else if("ETC".equals(coin.getCoinName())){
                    String coinPrice  = coin.getCoinRate().split(":")[0];
                    tpd.put("etc_rate", coinPrice);
                    etc_money = tuserWallet.getEtcAmnt().multiply(new BigDecimal(coinPrice));
                    totalMoney = etc_money.add(totalMoney);
                }*/
            }
            totalMoney = totalMoney.setScale(2,BigDecimal.ROUND_HALF_UP);
            String btc_percent = "";
            String ltc_percent = "";
            String eth_percent = "";
            String san_percent = "";
            if(totalMoney.compareTo(BigDecimal.ZERO)==0){
            	 btc_percent = "0.00%";
                 ltc_percent = "0.00%";
                 eth_percent = "0.00%";
                 san_percent = "0.00%";
            }else{
//            	hlc_percent = hlc_money.multiply(new BigDecimal("100")).divide(totalMoney, 2, RoundingMode.HALF_UP).toString()+"%";
                btc_percent = btc_money.multiply(new BigDecimal("100")).divide(totalMoney, 2, RoundingMode.HALF_UP).toString()+"%";
                ltc_percent = ltc_money.multiply(new BigDecimal("100")).divide(totalMoney, 2, RoundingMode.HALF_UP).toString()+"%";
                eth_percent = eth_money.multiply(new BigDecimal("100")).divide(totalMoney, 2, RoundingMode.HALF_UP).toString()+"%";
                san_percent = san_money.multiply(new BigDecimal("100")).divide(totalMoney, 2, RoundingMode.HALF_UP).toString()+"%";
//              etc_percent = etc_money.multiply(new BigDecimal("100")).divide(totalMoney, 2, RoundingMode.HALF_UP).toString()+"%";
//              money_percent = tuserWallet.getMoney().multiply(new BigDecimal("100")).divide(totalMoney, 2, RoundingMode.HALF_UP).toString()+"%";
            }
//            
//            tpd.put("hlc_percent", hlc_percent);
            tpd.put("btc_percent", btc_percent);
            tpd.put("ltc_percent", ltc_percent);
            tpd.put("eth_percent", eth_percent);
            tpd.put("san_percent", san_percent);
//            tpd.put("etc_percent", etc_percent);
//            tpd.put("money_percent", money_percent);
            tpd.put("totalMoney", totalMoney);
            
            data.put("userWallet", tpd);
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
     * @describe:查询账户余额
     * @author: zhangchunming
     * @date: 2016年11月1日下午7:20:54
     * @param request
     * @param response
     * @return: AjaxResponse
     */
    @AppLoginVerify
    @PostMapping("/getWalletByArray")
    @ApiOperation(nickname = "查询账户余额", value = "查询账户余额", notes = "查询账户余额")
    @ApiImplicitParams({
    })
    public AjaxResponse getWalletByArray(HttpServletRequest request){
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
            ar.setData(null);
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
     * @param response
     * @return: AjaxResponse
     */
    @AppLoginVerify
    @PostMapping("/transferAccount")
    @ApiOperation(nickname = "转币", value = "转币", notes = "转币！")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "revbankaccno", value = "对方账号", required = true, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "money", value = "转账金额", required = true, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "coin_name", value = "币种名称（BTC）", required = true, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "remark4", value = "备注", required = false, paramType = "query", dataType = "String")
    })
    public AjaxResponse transferAccount(HttpServletRequest request){
        logBefore(logger, "---------转币----transferAccount-----------");
        AjaxResponse ar = new AjaxResponse();
        Map<String,Object> data =  new HashMap<String,Object>();
        try {
            JSONObject user = JSONObject.parseObject(JSON.toJSONString(request.getSession().getAttribute("User")));
            PageData pd = new PageData();
            pd = this.getPageData();
            pd.remove("CSESSIONID");
            pd.put("bussType", "transferAccount");
            pd.put("user_name", user.getString("user_name"));
            pd.put("create_time", DateUtil.getCurrDateTime());
            pd.put("account", user.getString("account"));
            pd.put("revbankaccno", pd.getString("revbankaccno")==null?"":pd.getString("revbankaccno").trim());
            pd.put("money", pd.getString("money")==null?"":pd.getString("money").trim());
            if(StringUtils.isEmpty(pd.getString("revbankaccno"))){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("otherAccountNull"));
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            if(StringUtils.isEmpty(pd.getString("money"))){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("moneyNull"));
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            if(!Validator.isMoney4(pd.getString("money"))){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("moneyError"));
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            if(new BigDecimal(pd.getString("money")).compareTo(new BigDecimal("0"))==0){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("moneyNotZero"));
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            
            if(StringUtils.isEmpty(pd.getString("coin_name"))){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("coinNameNull"));
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            
            if(!"BTC".equals(pd.getString("coin_name"))&&!"LTC".equals(pd.getString("coin_name"))
            		&&!"ETH".equals(pd.getString("coin_name"))&&!"SAN".equals(pd.getString("coin_name"))){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("coinNameError"));
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            
            if(!userLoginService.findIsExist(pd.getString("revbankaccno"))){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("otherAccountNotExist"));
                ar.setErrorCode(CodeConstant.ERROR_NO_ACCOUNT);
                return ar;
            }
            UserLogin userLogin = userLoginService.getUserLoginByUserId(String.valueOf(user.get("user_id")));
            if(pd.getString("revbankaccno").equals(userLogin.getAccount())){
                ar.setSuccess(false);
                ar.setMessage(messageSource.getMessage("NotRransferYourselfAccount"));
                ar.setErrorCode(CodeConstant.ERROR_DISABLE);
                return ar;
            }
            UserWallet userWallet = userWalletService.getWalletByUserId(String.valueOf(user.get("user_id")));
            if("BTC".equals(pd.getString("coin_name"))){//比特币转账
                String btc_amnt = String.valueOf(userWallet.getBtcAmnt());
                if(new BigDecimal(pd.getString("money")).compareTo(new BigDecimal(btc_amnt))>0){
                    ar.setSuccess(false);
                    ar.setMessage(messageSource.getMessage("underbalance","BTC"));
                    ar.setErrorCode(CodeConstant.BALANCE_NOT_ENOUGH);
                    return ar;
                }
            }else if("LTC".equals(pd.getString("coin_name"))){//莱特币转账
                String ltc_amnt = String.valueOf(userWallet.getLtcAmnt());
                if(new BigDecimal(pd.getString("money")).compareTo(new BigDecimal(ltc_amnt))>0){
                    ar.setSuccess(false);
                    ar.setMessage(messageSource.getMessage("underbalance","LTC"));
                    ar.setErrorCode(CodeConstant.BALANCE_NOT_ENOUGH);
                    return ar;
                }
            }else if("ETH".equals(pd.getString("coin_name"))){//以太坊转账
                String eth_amnt = String.valueOf(userWallet.getEthAmnt());
                if(new BigDecimal(pd.getString("money")).compareTo(new BigDecimal(eth_amnt))>0){
                    ar.setSuccess(false);
                    ar.setMessage(messageSource.getMessage("underbalance","ETH"));
                    ar.setErrorCode(CodeConstant.BALANCE_NOT_ENOUGH);
                    return ar;
                }
            }else if("SAN".equals(pd.getString("coin_name"))){//小三币转账
                String san_amnt = String.valueOf(userWallet.getSanAmnt());
                if(new BigDecimal(pd.getString("money")).compareTo(new BigDecimal(san_amnt))>0){
                    ar.setSuccess(false);
                    ar.setMessage(messageSource.getMessage("underbalance","SAN"));
                    ar.setErrorCode(CodeConstant.BALANCE_NOT_ENOUGH);
                    return ar;
                }
            }
            
            String orderNo =OrderGenerater.generateOrderNo();
            
            TransferRecord transferRecord = new TransferRecord();
            transferRecord.setOrderNo(orderNo);
            transferRecord.setUserId(String.valueOf(user.get("user_id")));
            transferRecord.setRelaAccount(pd.getString("revbankaccno"));
            transferRecord.setAccount(user.getString("account"));
            transferRecord.setMoney(new BigDecimal(pd.getString("money")));
            transferRecord.setByUserId(String.valueOf(user.get("user_id")));
            transferRecord.setCurrency(pd.getString("coin_name"));
            Map<String, Object> realUserInfo = userLoginService.getUserInfoByAccount(pd.getString("revbankaccno"));
            transferRecord.setRelaUserId(String.valueOf(realUserInfo.get("user_id")));
            transferRecord.setRemark(pd.getString("remark4"));
            transferRecord.setStatus("1");
            boolean transferResult = userWalletService.transferAccount(transferRecord);
            if(transferResult){
                data.put("flowno", orderNo);
                data.put("create_time", DateUtil.dateToStamp(DateUtil.getCurrDateTime()));
                data.put("revbankaccno", pd.getString("revbankaccno"));
                data.put("money", pd.getString("money"));
                data.put("coin_name", transferRecord.getCurrency());
                data.put("remark1","转账-"+pd.getString("coin_name"));//说明
                data.put("remark4",pd.getString("remark4"));
                return AjaxResponse.success(messageSource.getMessage("transferSuccess"), data);
            }else{
                return AjaxResponse.falied(messageSource.getMessage("transferFailure"), CodeConstant.UPDATE_FAIL);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResponse.falied(messageSource.getMessage("sysException"), CodeConstant.SYS_ERROR);
        }finally{
            logAfter(logger);
        }   
    }
    
    /**
     * @describe:获取银行卡列表
     * @author: zhangchunming
     * @date: 2016年11月25日下午2:07:32
     * @param request
     * @param response
     * @return: AjaxResponse
     */
    @AppLoginVerify
    @PostMapping("/getBankList")
    @ApiOperation(nickname = "查询银行卡列表", value = "查询银行卡列表", notes = "查询银行卡列表")
    @ApiImplicitParams({
    })
    public AjaxResponse getBankList(HttpServletRequest request){
        AjaxResponse ar = new AjaxResponse();
        Map<String,Object> data = new HashMap<String, Object>();
        try {
            JSONObject user = JSONObject.parseObject(JSON.toJSONString(request.getSession().getAttribute("User")));
            List<UserBank> bankList = userBankService.getBankList(String.valueOf(user.get("user_id")));
            List<Map<String,Object>> bankList1 = new ArrayList<Map<String,Object>>();
            for(UserBank userBank :bankList){
                Map<String,Object> map = new HashMap<String,Object>();
                map.put("id", userBank.getId());
                map.put("bank_no", userBank.getBankNo());
                map.put("bank_name", userBank.getBankName());
                map.put("icon", userBank.getIcon());
                map.put("is_default", userBank.getIsDefault());
                bankList1.add(map);
            }
            data.put("bankList", bankList1);
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
