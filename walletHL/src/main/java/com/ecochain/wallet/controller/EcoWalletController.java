package com.ecochain.wallet.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.wallet.Wallet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecochain.EcoWalletApplication;
import com.ecochain.constant.CodeConstant;
import com.ecochain.core.config.CoreMessageSource;
import com.ecochain.util.Validator;
import com.ecochain.wallet.entity.UserLogin;
import com.ecochain.wallet.entity.WithdrawRecord;
import com.ecochain.wallet.mapper.UserLoginMapper;
import com.ecochain.wallet.mapper.WithdrawRecordMapper;
import com.ecochain.wallet.service.EcoWalletService;
import com.ecochain.wallet.service.SysGencodeService;
import com.ecochain.wallet.service.VerificationAddress;
import com.ecochain.wallet.util.WalletConstant;

@RestController
@RequestMapping("/api/btc")
@Api(value = "比特币相关接口")
public class EcoWalletController {

	Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private EcoWalletService ecoWalletService;

    @Autowired
    private CoreMessageSource messageSource;
    
    @Autowired
    private SysGencodeService sysGencodeService;
    
    @Autowired
    private UserLoginMapper userLoginMapper;
    
    @Autowired
    private WithdrawRecordMapper withdrawRecordMapper;
    
    @Autowired
    private VerificationAddress btcValidAddressImpl;

    @Autowired
    private VerificationAddress sanValidAddressImpl;

    @GetMapping("validAddress/{address}")
    @ApiImplicitParam()
    public AjaxResponse validAddress(@PathVariable("address") String address){
        try {
            btcValidAddressImpl.isValidAddress(address);
            return AjaxResponse.success(messageSource.getMessage("btcAddressSuccess"), address);
        } catch (Exception e) {
            return AjaxResponse.falied(messageSource.getMessage("btcAddressError"), address);
        }
    }

    @GetMapping("validSanAddress/{address}")
    @ApiImplicitParam()
    public AjaxResponse validSanAddress(@PathVariable("address") String address){
        try {
            sanValidAddressImpl.isValidAddress(address);
            return AjaxResponse.success(messageSource.getMessage("sanAddressSuccess"), address);
        } catch (Exception e) {
            return AjaxResponse.falied(messageSource.getMessage("sanAddressError"), address);
        }
    }

    @ApiOperation(value = "创建比特币钱包", notes = "创建比特币钱包例如：http://127.0.0.1:8001/create/user1/123456")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "userName", value = "用户名或者手机号", dataType = "String", required = true),
            @ApiImplicitParam(paramType = "path", name = "passWord", value = "密码", dataType = "String", required = true) })
    @PostMapping("/create/{userName}/{passWord}")
    public AjaxResponse createWallet(@PathVariable("userName") String userName,
            @PathVariable("passWord") String passWord) {

        NetworkParameters params = EcoWalletApplication.params;

        String address = ecoWalletService.createWallet(params, userName,
                passWord);

        return AjaxResponse.success("比特币钱包创建成功", address);
    }
//
//    @ApiOperation(value = "查询主账户比特币钱包余额", notes = "查询比特币钱包例如：http://127.0.0.1:8001/balance/user1/123456")
//    @ApiImplicitParams({
//            @ApiImplicitParam(paramType = "path", name = "userName", value = "用户名或者手机号", dataType = "String", required = true),
//            @ApiImplicitParam(paramType = "path", name = "passWord", value = "密码", dataType = "String", required = true) })
//    @GetMapping("/balance/{userName}/{passWord}")
//    public AjaxResponse bananceWallet(
//            @PathVariable("userName") String userName,
//            @PathVariable("passWord") String passWord) {
//
//        try {
//            Wallet wallet = ecoWalletService.dumpWallet(userName, passWord);
//            System.out.println(wallet);
//
//            return AjaxResponse.success("查询成功", wallet.getBalance()
//                    .toFriendlyString());
//        } catch (Exception e) {
//            e.printStackTrace();
//            return AjaxResponse.falied(e.getMessage());
//        }
//    }
//    
//    
//    @ApiOperation(value = "提现", notes = "提现比特币，参数：userName 用户名，password 密码，money 金额，address 地址")
//    @ApiImplicitParams({
//        @ApiImplicitParam(name = "withdrawRecord", value = "提现参数", required = true, dataType = "WithdrawRecord")
//    })
//    @GetMapping("/sendMoney")
//	public AjaxResponse sendMoney(HttpServletRequest request,@RequestBody WithdrawRecord withdrawRecord){
//        
//    	if(withdrawRecord == null || StringUtils.isEmpty(withdrawRecord.getUserName())||
//         	   StringUtils.isEmpty(withdrawRecord.getPassword())||withdrawRecord.getMoney()==null||
//         	   StringUtils.isEmpty(withdrawRecord.getAddress())||Validator.isMoney4(withdrawRecord.getMoney().toString())){
//    			AjaxResponse.falied("BTC提现参数有误", CodeConstant.PARAM_ERROR);
//         	}
//         	String uplimit = "";
//         	String lowlimit = "";
//             List<Map<String, String>> codeList = sysGencodeService.findByGroupCode("WITHDRAW_LIMIT");
//             for(Map<String, String> mapObj:codeList){
//                 if("BTC_LIMIT".equals(String.valueOf(mapObj.get("code_name")))){
//                 	uplimit = mapObj.get("uplimit").toString();
//                 	lowlimit = mapObj.get("lowlimit").toString();
//                 }
//             }
//             if(withdrawRecord.getMoney().compareTo(new BigDecimal(lowlimit))<0){
//             	AjaxResponse.falied("BTC提现金额最低"+lowlimit, CodeConstant.PARAM_ERROR);
//             }
//             
//             if(!StringUtils.isEmpty(uplimit)&&withdrawRecord.getMoney().compareTo(new BigDecimal(uplimit))>0){
//             	AjaxResponse.falied("BTC每次提现金额不得超出"+uplimit, CodeConstant.PARAM_ERROR);
//             }
//             
//             String btc_fee = "";//手续费（包含矿工费）
//             List<Map<String, String>> codeList1 = sysGencodeService.findByGroupCode("WITHDRAW_SERVICE_CHARGE");
//             for(Map<String, String> mapObj:codeList1){
//                 if("BTC_FEE".equals(String.valueOf(mapObj.get("code_name")))){
//                	 btc_fee = mapObj.get("code_value").toString();
//                 }
//             }
//             
//         	UserLogin userLogin = userLoginMapper.getUserByAccAndPass(withdrawRecord.getUserName(), withdrawRecord.getPassword());
//         	if(userLogin == null){
//         		return AjaxResponse.falied("用户名或密码错误", CodeConstant.PARAM_ERROR);
//         	}
//         	WithdrawRecord tWithdrawRecord = withdrawRecordMapper.getWithDrawRecord(String.valueOf(userLogin.getUserId()));
//             if (!WalletConstant.APPROVE.equals(tWithdrawRecord.getStatus())){
//                 return AjaxResponse.falied("订单状态有误", CodeConstant.PARAM_ERROR);
//             }
//    	
//        Coin value = null;
//        try {
//            value = Coin.parseCoin(withdrawRecord.getMoney().toString());
//        } catch (Exception e) {
//            e.printStackTrace();
//            return AjaxResponse.falied(e.getMessage());
//        }
//        
//        try {
//        	value = value.subtract(Coin.parseCoin(btc_fee));
//            NetworkParameters params = EcoWalletApplication.params;
//            Address to = Address.fromBase58(params, withdrawRecord.getAddress());
//            EcoWalletApplication.kit.wallet().sendCoins( EcoWalletApplication.kit.peerGroup(), to, value);
//            
//            return AjaxResponse.success("查询成功",EcoWalletApplication.kit.wallet().getBalance().toFriendlyString());
//        } catch (Exception e) {
//            e.printStackTrace();
//            return AjaxResponse.falied(e.getMessage());
//        }
//    }

}
