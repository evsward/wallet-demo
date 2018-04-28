package com.ecochain.wallet.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ecochain.core.exception.RuntimeServiceException;
import com.ecochain.wallet.entity.TransferRecord;
import com.ecochain.wallet.entity.UserWallet;
import com.ecochain.wallet.entity.UserWalletExample;
import com.ecochain.wallet.mapper.TransferRecordMapper;
import com.ecochain.wallet.mapper.UserWalletMapper;

@Service
public class UserWalletService {

    private Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private UserWalletMapper mapper;
    
    @Autowired
    private UserLoginService userLoginService;
    
    @Autowired
    private TransferRecordMapper transferRecordMapper;
    
    public boolean updateUserWallet(String userName){
        
        //TODO 通过userName获取一下用户信息
        Integer userId = null;
        
        UserWalletExample example = new UserWalletExample();
        example.createCriteria().andUserIdEqualTo(userId);
        
        UserWallet record = new UserWallet();
        //TODO 更新钱包里的金额，判断金额是否够用，等等一系列逻辑
//        record.setBtcAmnt(0);
        //设置各种金额的加减
        
        mapper.updateByExampleSelective(record, example);
        
        return true;
    }
    
    /**
     * 比特币充值
     * @param value
     * @param userId
     */
    @Transactional
    public void addBTCWallet(BigDecimal value, String userId){

        UserWallet userWallet = getWalletByUserId(userId);
        
        //userWallet.getBtcAmnt() 默认为0.0 所以不做非空判断
        BigDecimal newBtcAmnt = userWallet.getBtcAmnt().add(value);
        
        //只更新余额
        UserWallet userWalletUpdate = new UserWallet();
        userWalletUpdate.setUserId(Integer.parseInt(userId));
        userWalletUpdate.setBtcAmnt(newBtcAmnt);
        updateUserWallet(userWalletUpdate);
    }
    
    public boolean updateSub(UserWallet userWallet){
        return mapper.updateSub(userWallet)>0;
    }
    
    public UserWallet getWalletByUserId(String userId) {
        return mapper.getWalletByUserId(userId);
    }
    
    public UserWallet getWalletByUserName(String userName) {
        return mapper.getWalletByUserName(userName);
    }

    @Transactional(propagation=Propagation.REQUIRED)
    public boolean transferAccount(TransferRecord transferRecord){
        logger.info("------------------内部转账start---------------");
        UserWallet userWallet = new UserWallet();
        userWallet.setUserId(Integer.valueOf(transferRecord.getUserId()));
        if("BTC".equals(transferRecord.getCurrency())){//比特币转账
            userWallet.setBtcAmnt(transferRecord.getMoney());
        }else if("LTC".equals(transferRecord.getCurrency())){
        	userWallet.setLtcAmnt(transferRecord.getMoney());
        }else if("ETH".equals(transferRecord.getCurrency())){
        	userWallet.setEthAmnt(transferRecord.getMoney());
        }else if("ETC".equals(transferRecord.getCurrency())){
        	userWallet.setEtcAmnt(transferRecord.getMoney());
        }else if("HLC".equals(transferRecord.getCurrency())){
        	userWallet.setHlcAmnt(transferRecord.getMoney());
        }else if("SAN".equals(transferRecord.getCurrency())){
        	userWallet.setSanAmnt(transferRecord.getMoney());
        }
        userWallet.setOperator(transferRecord.getUserId());
        boolean updateSubResult = updateSub(userWallet);
        logger.info("------------------内部转账----从钱包扣钱updateSubResult："+updateSubResult);
        
        //获取对方信息
        Map<String, Object> userInfo = userLoginService.getUserInfoByAccount(transferRecord.getRelaAccount());
        boolean updateAddResult = false;
        if(updateSubResult){
            UserWallet userWallet1 = new UserWallet();
            if("BTC".equals(transferRecord.getCurrency())){//比特币转账
                userWallet1.setBtcAmnt(transferRecord.getMoney());
            }else if("LTC".equals(transferRecord.getCurrency())){
            	userWallet1.setLtcAmnt(transferRecord.getMoney());
            }else if("ETH".equals(transferRecord.getCurrency())){
            	userWallet1.setEthAmnt(transferRecord.getMoney());
            }else if("ETC".equals(transferRecord.getCurrency())){
            	userWallet1.setEtcAmnt(transferRecord.getMoney());
            }else if("HLC".equals(transferRecord.getCurrency())){
            	userWallet1.setHlcAmnt(transferRecord.getMoney());
            }else if("SAN".equals(transferRecord.getCurrency())){
            	userWallet1.setSanAmnt(transferRecord.getMoney());
            }
            userWallet1.setUserId((Integer)userInfo.get("user_id"));//对方用户id
            userWallet1.setOperator(transferRecord.getUserId());
            updateAddResult = updateAdd(userWallet1);
            logger.info("------------------内部转账----向钱包加钱updateAddResult：---------------"+updateAddResult);
            logger.info("------------------内部转账end---------转账结果："+(updateAddResult&&updateSubResult));
        }
        if(updateAddResult&&updateSubResult){
            //生成账户流水
            transferRecord.setRelaUserId(String.valueOf(userInfo.get("userId")));//对方账户
            transferRecord.setStatus("1");
            transferRecord.setByUserId(transferRecord.getUserId());
            transferRecordMapper.insertSelective(transferRecord);
        }
        return(updateAddResult&&updateSubResult);
    }
    /**
     * 通过userid更新UserWallet
     * @param userWallet
     */
    public UserWallet updateUserWallet(UserWallet userWallet) {
        
        if(null != userWallet.getUserId()){
            UserWalletExample example = new UserWalletExample();
            example.createCriteria().andUserIdEqualTo(userWallet.getUserId());
            
            mapper.updateByExampleSelective(userWallet, example);
            
            return userWallet;
        }else{
            throw new RuntimeServiceException("用户id不能为空");
        }
    }
    
    public boolean updateAdd(UserWallet userWallet){
        return mapper.updateAdd(userWallet)>0;
    }
    
    public boolean exchangeRMB2Coin(BigDecimal amnt,BigDecimal rmbAmnt,String userId,String currency){
        return mapper.exchangeRMB2Coin(amnt, rmbAmnt, userId,currency)>0;
    }
    
    public boolean companySubCoin(BigDecimal amnt,BigDecimal rmbAmnt,String currency){
        return mapper.companySubCoin(amnt, rmbAmnt,currency)>0;
    }
    
    public boolean exchangeCoin2RMB(BigDecimal amnt,BigDecimal rmbAmnt,String userId,String currency){
        return mapper.exchangeCoin2RMB(amnt, rmbAmnt,userId,currency)>0;
    }
    
    public boolean companyAddCoin(BigDecimal amnt,BigDecimal rmbAmnt,String currency){
        return mapper.companyAddCoin(amnt, rmbAmnt,currency)>0;
    }
    
    public List<Map<String,Object>> getAddressAndBalance(String userId){
        return mapper.getAddressAndBalance(userId);
    }
}
