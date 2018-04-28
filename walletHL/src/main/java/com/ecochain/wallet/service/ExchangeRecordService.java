package com.ecochain.wallet.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecochain.core.config.CoreMessageSource;
import com.ecochain.wallet.entity.ExchangeRecord;
import com.ecochain.wallet.mapper.ExchangeRecordMapper;
import com.ecochain.wallet.mapper.UserWalletMapper;

@Service
public class ExchangeRecordService {

    Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private CoreMessageSource messageSource;
    
    @Autowired
    private UserWalletMapper userWalletMapper;
    
    @Autowired
    private UserWalletService userWalletService;
    
    @Autowired
    private ExchangeRecordMapper mapper;
    
    
    public boolean currencyExchange(ExchangeRecord exchangeRecord){
        
        logger.info("----------------币种兑换------------start---------");
        boolean exchangeResult = false;
        boolean companyResult = false;
        if("1".equals(exchangeRecord.getTradeInOut())){//买进，人民币减少，虚拟币增加
            exchangeResult = userWalletService.exchangeRMB2Coin(exchangeRecord.getAmnt(),exchangeRecord.getMoney(),exchangeRecord.getUserId(),exchangeRecord.getCurrency());
            logger.info("用户买进虚拟币，exchangeResult is "+exchangeResult);
            if(exchangeResult){
                companyResult = userWalletService.companySubCoin(exchangeRecord.getAmnt(), exchangeRecord.getMoney(),exchangeRecord.getCurrency());
                logger.info("用户买进虚拟币，companySubCoin is "+companyResult);
            }
        }else if("2".equals(exchangeRecord.getTradeInOut())){//卖出，人民币增加，虚拟币减少
            exchangeResult = userWalletService.exchangeCoin2RMB(exchangeRecord.getAmnt(), exchangeRecord.getMoney(), exchangeRecord.getUserId(), exchangeRecord.getCurrency());
            logger.info("用户卖出虚拟币，exchangeResult is "+exchangeResult);
            if(exchangeResult){
                companyResult = userWalletService.companyAddCoin(exchangeRecord.getAmnt(), exchangeRecord.getMoney(),exchangeRecord.getCurrency());
                logger.info("用户卖出虚拟币，companySubCoin is "+companyResult);
            }
        }
        //插账户流水
        mapper.insertSelective(exchangeRecord);
        logger.info("--------------币种兑换------------end------------结果(exchangeResult&&companyResult)："+(exchangeResult&&companyResult));
        return (exchangeResult&&companyResult);
    }
    
}
