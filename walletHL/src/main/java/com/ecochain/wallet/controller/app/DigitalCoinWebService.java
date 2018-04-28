package com.ecochain.wallet.controller.app;


import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecochain.annotation.AppLoginVerify;
import com.ecochain.base.BaseWebService;
import com.ecochain.constant.CodeConstant;
import com.ecochain.core.config.CoreMessageSource;
import com.ecochain.wallet.controller.AjaxResponse;
import com.ecochain.wallet.entity.DigitalCoin;
import com.ecochain.wallet.entity.PageData;
import com.ecochain.wallet.service.DigitalCoinService;
import com.ecochain.wallet.service.UserWalletService;

/**
 * Created by LiShuo on 2017/05/23.
 */
@RestController
@RequestMapping(value = "/api/rest/digitalCoin")
public class DigitalCoinWebService extends BaseWebService {

    private Logger logger = Logger.getLogger(DigitalCoinWebService.class);

    @Autowired
    private DigitalCoinService digitalCoinService;

    @Autowired
    private UserWalletService userWalletService;
    
    @Autowired
    private CoreMessageSource messageSource;

    /**
     * 获取当前币种价格
     */
    @AppLoginVerify
    @GetMapping("getCoinPrice")
    @ApiOperation(nickname = "getCoinPrice", value = "获取当前币种价格", notes = "获取当前币种价格！！")
    @ApiImplicitParams({
    })
    public AjaxResponse getCoinPrice(HttpServletRequest request){
        AjaxResponse ar= new AjaxResponse();
        Map<String, Object> map  = new HashMap<String, Object>();
        try{
            List<DigitalCoin> listCoin = digitalCoinService.getAllCoinPrice();
            for(DigitalCoin coin:listCoin){
                if("HLC".equals(coin.getCoinName())){//合链币
                    String coinPrice  = coin.getCoinRate().split(":")[0];
                    map.put("HLC", coinPrice);
                }else if("BTC".equals(coin.getCoinName())){
                    String coinPrice  = coin.getCoinRate().split(":")[0];
                    map.put("BTC", coinPrice);
                }else if("LTC".equals(coin.getCoinName())){
                    String coinPrice  = coin.getCoinRate().split(":")[0];
                    map.put("LTC", coinPrice);
                }else if("ETH".equals(coin.getCoinName())){
                    String coinPrice  = coin.getCoinRate().split(":")[0];
                    map.put("ETH", coinPrice);
                }else if("ETC".equals(coin.getCoinName())){
                    String coinPrice  = coin.getCoinRate().split(":")[0];
                    map.put("ETC", coinPrice);
                }
            }
            ar=fastReturn(map,true,messageSource.getMessage("getSuccess"),CodeConstant.SC_OK);
        }catch(Exception e){
            logger.debug(e.toString(), e);
            ar=fastReturn(null,false,messageSource.getMessage("sysException"),CodeConstant.SYS_ERROR);
        }
        return ar;
    }

    /**
     * 获取买家账户信息
     */
   /* @LoginVerify
    @GetMapping("getBuyerInfo")
    @ApiOperation(nickname = "getBuyerInfo", value = "获取买家账户信息", notes = "获取买家账户信息！！")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "CSESSIONID", value = "CSESSIONID", required = true, paramType = "query", dataType = "String"),
    })
    public AjaxResponse getBuyerInfo(HttpServletRequest request){
        AjaxResponse ar= new AjaxResponse();
        try{
            String userstr = SessionUtil.getAttibuteForUser(RequestUtils.getRequestValue(CookieConstant.CSESSIONID, request));
            JSONObject user = JSONObject.parseObject(userstr);
            ar=fastReturn(this.userWalletService.getWalletByUserId(String.valueOf(user.get("id")), Constant.VERSION_NO),true,"获取买家账户信息！",CodeConstant.SC_OK);
        }catch(Exception e){
            logger.debug(e.toString(), e);
            ar=fastReturn(null,false,"系统异常，获取买家账户信息失败！",CodeConstant.SYS_ERROR);
        }
        return ar;
    }*/
}
