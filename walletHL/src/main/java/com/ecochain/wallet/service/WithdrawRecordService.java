package com.ecochain.wallet.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ecochain.core.config.CoreMessageSource;
import com.ecochain.wallet.entity.UserWallet;
import com.ecochain.wallet.entity.WithdrawRecord;
import com.ecochain.wallet.entity.WithdrawRecordExample;
import com.ecochain.wallet.mapper.UserCoinMapper;
import com.ecochain.wallet.mapper.UserWalletMapper;
import com.ecochain.wallet.mapper.WithdrawRecordMapper;
import com.ecochain.wallet.util.WalletConstant;

@Service
public class WithdrawRecordService {

    Logger log = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private CoreMessageSource messageSource;
    
    @Autowired
    private WithdrawRecordMapper mapper;
    @Autowired
    private UserWalletMapper userWalletMapper;
    @Autowired
    private SysGencodeService sysGencodeService;
    @Autowired
    private UserCoinMapper userCoinMapper;
    
    public WithdrawRecord insertWithdrawRecord(WithdrawRecord record){
        //TODO 设置订单编码
        mapper.insert(record);
        return record;
    }
    
    public boolean updateTxHashById(WithdrawRecord record){
        if(StringUtils.isBlank(record.getTxHash())){
            throw new RuntimeException("交易hash 不能为空");
        }
        
        WithdrawRecordExample example = new WithdrawRecordExample();
        example.createCriteria().andIdEqualTo(record.getId());
        
        return mapper.updateByExample(record, example) > 0;
    }
    
    public boolean updateWithdrawRecordByTxHash(WithdrawRecord record){
        return mapper.updateWithdrawRecordByTxHash(record);
    }
    
    public WithdrawRecord findWithdrawRecordByTxHash(String txHash){
        WithdrawRecordExample example = new WithdrawRecordExample();
        example.createCriteria().andTxHashEqualTo(txHash);
        
        List<WithdrawRecord> list = mapper.selectByExample(example);
        
        if (list == null || list.size() == 0){
            return null;
        }
        
        return list.get(0);
    }
    
    @Transactional(propagation=Propagation.REQUIRED)
    public boolean applyWithDrawal(WithdrawRecord record){
    	/*//判断提币地址是否平台内部地址，如果是走1、转账流程，手续费照扣，否则走 2、提币流程，需区块链确认
    	String userId = userCoinMapper.getUserIdByAddress(record.getAddress());
    	//1、平台内部转账。
        if (StringUtils.isNotBlank(userId)){
        	//扣钱
            UserWallet userWallet1 = new UserWallet();
            if("BTC".equals(record.getCurrency())){
            	userWallet1.setBtcAmnt(record.getMoney());
            }else if("ETH".equals(record.getCurrency())){
            	userWallet1.setEthAmnt(record.getMoney());
            }else if("LTC".equals(record.getCurrency())){
            	userWallet1.setLtcAmnt(record.getMoney());
            }else if("SAN".equals(record.getCurrency())){
            	userWallet1.setSanAmnt(record.getMoney());
            }
            userWallet1.setUserId(Integer.valueOf(record.getUserId()));
            userWallet1.setModifyTime(new Date());
            boolean updateSub = userWalletMapper.updateSub(userWallet1)>0;
            if(updateSub){
            	//实际提币金额=申请金额-手续费
            	record.setMoney(record.getMoney().subtract(record.getCost()));
            	//给另一个用户加钱
            	UserWallet userWallet2 = new UserWallet();
            	userWallet2.setUserId(Integer.valueOf(userId));
            	if("BTC".equals(record.getCurrency())){
            		userWallet2.setBtcAmnt(record.getMoney());
                }else if("ETH".equals(record.getCurrency())){
                	userWallet2.setEthAmnt(record.getMoney());
                }else if("LTC".equals(record.getCurrency())){
                	userWallet2.setLtcAmnt(record.getMoney());
                }else if("SAN".equals(record.getCurrency())){
                	userWallet2.setSanAmnt(record.getMoney());
                }
            	userWallet2.setModifyTime(new Date());
                boolean updateAdd = userWalletMapper.updateAdd(userWallet2)>0;
                if(updateAdd){
                	record.setTxHash("");
                	record.setStatus(WalletConstant.FINISH);
                	record.setFree(BigDecimal.ZERO);
                	
                	return mapper.insertSelective(record)>0;
                }
            }
            return false;
        }*/
        
        //2、提币流程
        //扣钱
        UserWallet userWallet = new UserWallet();
        if("BTC".equals(record.getCurrency())){
            userWallet.setBtcAmnt(record.getMoney());
        }else if("ETH".equals(record.getCurrency())){
            userWallet.setEthAmnt(record.getMoney());
        }else if("LTC".equals(record.getCurrency())){
            userWallet.setLtcAmnt(record.getMoney());
        }else if("SAN".equals(record.getCurrency())){
            userWallet.setSanAmnt(record.getMoney());
        }
        userWallet.setUserId(Integer.valueOf(record.getUserId()));
        userWallet.setModifyTime(new Date());
        boolean updateSub = userWalletMapper.updateSub(userWallet)>0;
        if(updateSub){
        	//实际提币金额=申请金额-手续费
        	record.setMoney(record.getMoney().subtract(record.getCost()));
            return mapper.insertSelective(record)>0;
        }
        return false;
    }

    /**
     * 
     */
    public List<WithdrawRecord> listAllApprove() {
        WithdrawRecordExample example = new WithdrawRecordExample();
        example.createCriteria().andStatusEqualTo(WalletConstant.APPROVE);
        
        List<WithdrawRecord> records = mapper.selectByExample(example);
        
        return records;
    }

    public List<WithdrawRecord> listETHApprove() {
        WithdrawRecordExample example = new WithdrawRecordExample();
        example.createCriteria().andStatusEqualTo(WalletConstant.APPROVE).andCurrencyEqualTo("ETH");

        List<WithdrawRecord> records = mapper.selectByExample(example);

        return records;
    }

    public List<WithdrawRecord> listETHSend() {
        WithdrawRecordExample example = new WithdrawRecordExample();
        example.createCriteria().andStatusEqualTo(WalletConstant.SEND).andCurrencyEqualTo("ETH")
        .andTxHashNotEqualTo("***********************************");
        List<WithdrawRecord> records = mapper.selectByExample(example);
        return records;
    }
    
    
    
}
