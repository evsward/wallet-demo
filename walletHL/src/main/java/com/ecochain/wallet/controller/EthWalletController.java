package com.ecochain.wallet.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.math.BigDecimal;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ecochain.core.config.CoreMessageSource;
import com.ecochain.wallet.service.EthWalletService;
import com.ecochain.wallet.service.impl.EthValidAddressImpl;
import com.github.pagehelper.StringUtil;

/**
 * 
* @ClassName: EthWalletController 
* @Description: TODO(以太坊钱包controller) 
* @author dxm 
* @date 2017年8月18日 上午10:55:34 
*
 */
@RestController
@RequestMapping("/api/ethWallet")
@Api("以太坊钱包")
public class EthWalletController {
	
	@Autowired
	private EthWalletService ethWalletService;

	
	@Autowired
    private CoreMessageSource messageSource;
	
	@Autowired
	private EthValidAddressImpl ethValidAddressImpl;
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Value("${ETH.wallet.hot.address}")
	private String ethWalletHotAddress;
	
	@ApiOperation(value = "创建以太坊钱包", notes = "创建以太坊钱包例如：http://192.168.10.123:6767/api/ethWallet/createEthWallet/leee/111111")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "userName", value = "用户名或者手机号", dataType = "String", required = true),
            @ApiImplicitParam(paramType = "path", name = "passWord", value = "密码", dataType = "String", required = true) })
    @PostMapping("/createEthWallet/{userName}/{passWord}")
	public AjaxResponse createEthWallet(@PathVariable("userName") String userName,
            @PathVariable("passWord") String passWord) {
		
		if(StringUtil.isEmpty(userName)){
			logger.error("userName is null!");
			return AjaxResponse.falied(messageSource.getMessage("UserNameNull"));
		}
		
		if(StringUtil.isEmpty(passWord)){
			logger.error("passWord is null!");
			return AjaxResponse.falied(messageSource.getMessage("passwordNull"));
		}
		
		String address = ethWalletService.createEthWallet(userName, passWord);
		
		return AjaxResponse.success(messageSource.getMessage("addSuccess"), address);
	}
	
	@ApiOperation(value = "查询以太坊主钱包余额", notes = "查询以太坊主钱包余额")
	@PostMapping("/getEthPrimaryAccountBalance")
	public AjaxResponse getEthPrimaryAccountBalance(HttpServletRequest request){
		String data = null;
		try {
			String address = ethWalletHotAddress;
			logger.info("primaryAccountAddress: "+address);
			BigDecimal primaryBalance = ethWalletService.getBalanceEther(address);
			data = primaryBalance.toString();
			return AjaxResponse.success(messageSource.getMessage("getSuccess"), data);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getEthPrimaryAccountBalance error: "+e.getMessage());
			data = BigDecimal.ZERO.toString();
			return AjaxResponse.success(messageSource.getMessage("getSuccess"), data);
		}
	}
	
	
	@ApiOperation(value = "查询以太坊钱包余额", notes = "查询以太坊钱包余额")
	@PostMapping("/getEthWalletBalance")
	public AjaxResponse getEthWalletBalance(HttpServletRequest request){
		String data = null;
		JSONObject user = JSONObject.parseObject(JSON.toJSONString(
				request.getSession().getAttribute("User")));
		if(user == null){
			return AjaxResponse.falied(messageSource.getMessage("notLogin"));
		}
		String userid = String.valueOf(user.get("user_id"));
		
		logger.info("userid = "+userid);
		HashMap<String, Object> result = ethWalletService.getEthWalletBalance(userid);
		if(result.get("balance") == null){
			data = (String) result.get("msg");
		}else{
			BigDecimal balance =  (BigDecimal) result.get("balance");
			data = balance.toString();
		}
		return AjaxResponse.success(messageSource.getMessage("getSuccess"), data);
	}
	
	
	@GetMapping("validAddress/{address}")
    @ApiImplicitParam()
    public AjaxResponse validAddress(@PathVariable("address") String address){
        try {
            if(!ethValidAddressImpl.isValidAddress(address))
            	return AjaxResponse.falied(messageSource.getMessage("ethAddressSuccess"), address);
        } catch (Exception e) {
        	e.printStackTrace();
            return AjaxResponse.falied(messageSource.getMessage("ethAddressError"));
        }
        return AjaxResponse.success(messageSource.getMessage("ethAddressError"), address);
    }
//	
//	@ApiOperation(value = "以太坊钱包转账交易", notes = "以太坊钱包转账交易")
//	@ApiImplicitParams({
//		@ApiImplicitParam(paramType = "path", name = "fromAddress", value = "转出地址", dataType = "String", required = true),
//		@ApiImplicitParam(paramType = "path", name = "toAddress", value = "转入地址", dataType = "String", required = true),
//		@ApiImplicitParam(paramType = "path", name = "password", value = "交易密码", dataType = "String", required = true),
//		@ApiImplicitParam(paramType = "path", name = "amount", value = "交易金额", dataType = "String", required = true)
//	})
//	@GetMapping("/ethSendTransaction/{fromAddress}/{toAddress}/{password}/{amount}")
//	public AjaxResponse ethSendTransaction(
//			@PathVariable("fromAddress") String fromAddress, 
//			@PathVariable("toAddress") String toAddress, 
//			@PathVariable("password") String password,
//			@PathVariable("amount") String amount,
//			HttpServletRequest request){
//		JSONObject user = JSONObject.parseObject(JSON.toJSONString(
//				request.getSession().getAttribute("User")));
//		if(user == null){
//			return AjaxResponse.falied(messageSource.getMessage("notLogin"));
//		}
//		if(StringUtil.isEmpty(fromAddress)){
//			logger.error("fromAddress is null!");
//			return AjaxResponse.falied("转账地址不能为空！");
//		}
//		
//		if(StringUtil.isEmpty(toAddress)){
//			logger.error("toAddress is null!");
//			return AjaxResponse.falied("转账地址不能为空！");
//		}
//		
//		if(StringUtil.isEmpty(amount)){
//			logger.error("amount is null!");
//			return AjaxResponse.falied("交易金额不能为空！");
//		}
//		
//		if(StringUtil.isEmpty(password)){
//			logger.error("passWord is null!");
//			return AjaxResponse.falied("密码不能为空！");
//		}
//		
//		String result = ethWalletService.sendTransactionFromUser(fromAddress, toAddress, amount);
//		if(StringUtils.isEmpty(result)){
//			return AjaxResponse.falied("交易失败");
//		}
//		return AjaxResponse.success("交易成功", result);
//	}
}
