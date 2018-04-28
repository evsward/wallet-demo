package com.ecochain.wallet.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.ecochain.constant.Constant;
import com.ecochain.util.RestUtil;
import com.ecochain.wallet.entity.UserCoin;
import com.ecochain.wallet.entity.UserLogin;
import com.ecochain.wallet.entity.UserWallet;
import com.ecochain.wallet.entity.UsersDetails;
import com.ecochain.wallet.mapper.UserCoinMapper;
import com.ecochain.wallet.mapper.UserLoginMapper;
import com.ecochain.wallet.mapper.UserWalletMapper;
import com.ecochain.wallet.mapper.UsersDetailsMapper;

@Service("userDetailsService")
public class UsersDetailsService {

    private Logger logger = LoggerFactory.getLogger(getClass());
    
    @Resource
    private SysGencodeService sysGencodeService;
    
    @Resource
    private EthWalletService ethWalletService; 
    
    @Resource
    private UsersDetailsMapper usersDetailsMapper;
    
    @Resource
    private UserLoginMapper userLoginMapper;
    
    @Resource
    private UserWalletMapper userWalletMapper;
    
    @Resource
    private UserCoinMapper userCoinMapper;
    
    public boolean isExistTransPassword(String userId, String transPassword) {
        return usersDetailsMapper.isExistTransPassword(userId, transPassword)>0;
    }
    
    public UserLogin getUserByAccAndPass(String account,String password) {
        return userLoginMapper.getUserByAccAndPass(account, password);
    }

    public boolean updateLoginTimeById(Integer id, String lastloginIp,long lastloginPort){
        return userLoginMapper.updateLoginTimeById(id, lastloginIp, lastloginPort)>0;
    }
    
    public Map getUserInfoByUserId(Integer userId){
        return usersDetailsMapper.getUserInfoByUserId(userId);
    }
    
    public String getUserIdByAddress(String address) {
        return usersDetailsMapper.getUserIdByAddress(address);
    }
    
    public boolean setTransPassword(String userId,String transPassword) {
        return usersDetailsMapper.setTransPassword(userId, transPassword)>0;
    }
    
    public UsersDetails selectByPrimaryKey(Integer id){
        return usersDetailsMapper.selectByPrimaryKey(id);
    }
    
    public boolean findIsExist(String mobilePhone){
        return usersDetailsMapper.findIsExist(mobilePhone)>0;
    }
    
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean updatePhone(String userId,String mobilePhone) {
        boolean updatePhoneResult = usersDetailsMapper.updatePhone(userId,mobilePhone)>0;
        logger.info("------修改手机号，结果updatePhoneResult="+updatePhoneResult);
        boolean updateAccountResult = false;
        if(updatePhoneResult){
            updateAccountResult = userLoginMapper.updateAccount(mobilePhone,userId)>0;
            logger.info("------修改账号，结果updateAccountResult="+updateAccountResult);
        }
        logger.info("--------修改手机号总结果(updatePhoneResult&&updateAccountResult)="+(updatePhoneResult&&updateAccountResult));
        return (updatePhoneResult&&updateAccountResult);
    }
    
    public boolean isExistByUserName(String userName){
        return usersDetailsMapper.isExistByUserName(userName)>0;
    }
    
    @Transactional(propagation =Propagation.REQUIRED)
    public boolean addUser(UsersDetails usersDetails,UserLogin userLogin,UserWallet userWallet) throws Exception{
        //添加用户详细表
        usersDetailsMapper.insertSelective(usersDetails);
        //添加用户登陆表
        userLogin.setUserId(usersDetails.getId());
        userLoginMapper.insertSelective(userLogin);
        
        //添加用户钱包账户
        userWallet.setMoney(new BigDecimal(100000));
        userWallet.setBtcAmnt(new BigDecimal(0));
        userWallet.setLtcAmnt(new BigDecimal(4));
        userWallet.setEthAmnt(new BigDecimal(5));
        userWallet.setEtcAmnt(new BigDecimal(3));
        userWallet.setHlcAmnt(new BigDecimal(2));
        userWallet.setUserId(usersDetails.getId());
        userWalletMapper.insertSelective(userWallet);
        
        logger.info("===========注册创建钱包=========区块链钱包接口调用========start================");
        /*String kql_url =null;
        String ltc_url =null;
        List<Map<String, String>> codeList = sysGencodeService.findByGroupCode("QKL_URL");
        for(Map<String, String> mapObj:codeList){
            if("QKL_URL".equals(String.valueOf(mapObj.get("code_name")))){
                kql_url = mapObj.get("code_value").toString();
            }else if("LTC_URL".equals(String.valueOf(mapObj.get("code_name")))){
            	ltc_url = mapObj.get("code_value").toString();
            }
        }*/
        List<UserCoin> userCoins =new ArrayList<UserCoin>();
        String btc_api_url =null ,ltc_api_url=null,eth_api_url=null,san_api_url=null;
        List<Map<String, String>> codeList =sysGencodeService.findByGroupCode("COIN_API");
        for(Map<String, String> mapObj:codeList){
            if("BTC_API_URL".equals(mapObj.get("code_name"))&&"1".equals(mapObj.get("remark1"))){
            	btc_api_url = mapObj.get("code_value").toString();
            }
            if("LTC_API_URL".equals(mapObj.get("code_name"))&&"1".equals(mapObj.get("remark1"))){
            	ltc_api_url = mapObj.get("code_value").toString();
            }
            if("ETH_API_URL".equals(mapObj.get("code_name"))&&"1".equals(mapObj.get("remark1"))){
            	eth_api_url = mapObj.get("code_value").toString();
            }
            if("SAN_API_URL".equals(mapObj.get("code_name"))&&"1".equals(mapObj.get("remark1"))){
            	san_api_url = mapObj.get("code_value").toString();
            }
        }
        logger.info("---注册----btc_api_url="+btc_api_url);
        logger.info("---注册----ltc_api_url="+ltc_api_url);
        logger.info("---注册----eth_api_url="+eth_api_url);
        logger.info("---注册----san_api_url="+san_api_url);
        
        if(StringUtils.isNotBlank(btc_api_url)){
			String jsonStr = RestUtil.restPostPath(btc_api_url+userLogin.getUserName()+"/"+userLogin.getPassword());
			JSONObject jsonObj = JSONObject.parseObject(jsonStr);
			if(jsonObj==null||jsonObj.getBooleanValue("success")==false){
				throw new Exception("创建BTC钱包失败！");
			}
			UserCoin userCoin = new UserCoin();
	        userCoin.setUserId(usersDetails.getId());
	        userCoin.setCurrency(Constant.CURRENCY_BTC);
	        userCoin.setAddress(jsonObj.getString("data"));
			userCoin.setCreateTime(new Date());
			userCoins.add(userCoin);
        }
        if(StringUtils.isNotBlank(san_api_url)){
			String sanStr = RestUtil.restPostPath(san_api_url+userLogin.getUserName());
			JSONObject sanObject= JSONObject.parseObject(sanStr);
			if(sanObject==null||sanObject.getBooleanValue("success")==false){
				throw new Exception("创建SAN钱包失败！");
			}
			logger.info("SAN Address-----------------> "+sanObject.getString("data"));
			UserCoin userCoinTwo = new UserCoin();
			userCoinTwo.setUserId(usersDetails.getId());
			userCoinTwo.setCurrency(Constant.CURRENCY_SAN);
			userCoinTwo.setAddress(sanObject.getString("data"));
			userCoinTwo.setCreateTime(new Date());
			userCoins.add(userCoinTwo);
        }
        if(StringUtils.isNotBlank(eth_api_url)){
            String ethAccountStr = RestUtil.restPostPath(eth_api_url+userLogin.getUserName()+"/"+userLogin.getPassword());
            JSONObject ethJson = JSONObject.parseObject(ethAccountStr);
            if(ethJson==null||ethJson.getBooleanValue("success")==false){
				throw new Exception("创建ETH钱包失败！");
			}
            UserCoin userCoinThree = new UserCoin();
            userCoinThree.setUserId(usersDetails.getId());
            userCoinThree.setCurrency(Constant.CURRENCY_ETH);
            userCoinThree.setAddress(ethJson.getString("data"));
            userCoinThree.setCreateTime(new Date());
            userCoins.add(userCoinThree);
        }
        if(StringUtils.isNotBlank(ltc_api_url)){
            String liteStr = RestUtil.restPostPath(ltc_api_url+userLogin.getUserName()+"/"+userLogin.getPassword());
            JSONObject liteJson = JSONObject.parseObject(liteStr);
            if(liteJson==null||liteJson.getBooleanValue("success")==false){
				throw new Exception("创建LTC钱包失败！");
			}
            UserCoin userCoinFour = new UserCoin();
            userCoinFour.setUserId(usersDetails.getId());
            userCoinFour.setCurrency(Constant.CURRENCY_LTC);
            userCoinFour.setAddress(liteJson.getString("data"));
            userCoinFour.setCreateTime(new Date());
            userCoins.add(userCoinFour);
        }
        
        userCoinMapper.batchInsertSelective(userCoins);
        logger.info("==========注册创建钱包==========区块链钱包接口调用========end================");
        return true;
    }
    
    public String getTransPassword(String userId){
        return usersDetailsMapper.getTransPassword(userId);
    }
    
    public boolean realName(UsersDetails usersDetails){
        return usersDetailsMapper.realName(usersDetails)>0;
    }

    public UserCoin getUserCoinByAddress(String address){
        return userCoinMapper.getUserCoinByAddress(address);
    }
}
