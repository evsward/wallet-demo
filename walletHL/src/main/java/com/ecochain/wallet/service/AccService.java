package com.ecochain.wallet.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.ecochain.wallet.entity.DigitalCoin;
import com.ecochain.wallet.entity.UserCoin;
import com.ecochain.wallet.entity.UserWallet;
import com.ecochain.wallet.mapper.UserCoinMapper;

@Service
public class AccService {
    
    @Autowired
    private UserWalletService userWalletService;
    
    @Autowired
    private DigitalCoinService digitalCoinService;
    
    @Autowired
    private UserCoinMapper userCoinMapper;
    
    public Map<String,Object> getBalance(Map<String,Object> data, JSONObject user){
        UserWallet userWallet = userWalletService.getWalletByUserId(String.valueOf(user.get("user_id")));
//        BigDecimal totalMoney = userWallet.getMoney();
        BigDecimal totalMoney = BigDecimal.ZERO;
        List<DigitalCoin> listCoin = digitalCoinService.getAllCoinPrice();
//        BigDecimal hlc_money = new BigDecimal("0");
        BigDecimal btc_money = new BigDecimal("0");
        BigDecimal ltc_money = new BigDecimal("0");
        BigDecimal eth_money = new BigDecimal("0");
//        BigDecimal etc_money = new BigDecimal("0");
        BigDecimal san_money = new BigDecimal("0");
        for(DigitalCoin coin:listCoin){
            if("BTC".equals(coin.getCoinName())){
                String coinPrice  = coin.getCoinRate().split(":")[0];
                btc_money = userWallet.getBtcAmnt().multiply(new BigDecimal(coinPrice));
                totalMoney = btc_money.add(totalMoney);
            }else if("ETH".equals(coin.getCoinName())){
                String coinPrice  = coin.getCoinRate().split(":")[0];
                eth_money = userWallet.getEtcAmnt().multiply(new BigDecimal(coinPrice));
                totalMoney = eth_money.add(totalMoney);
            }else if("LTC".equals(coin.getCoinName())){
                String coinPrice  = coin.getCoinRate().split(":")[0];
                ltc_money = userWallet.getLtcAmnt().multiply(new BigDecimal(coinPrice));
                totalMoney = ltc_money.add(totalMoney);
            }/*else if("HLC".equals(coin.getCoinName())){//合链币
                String coinPrice  = coin.getCoinRate().split(":")[0];
                hlc_money = userWallet.getHlcAmnt().multiply(new BigDecimal(coinPrice));
                totalMoney = hlc_money.add(totalMoney);
            }else if("ETC".equals(coin.getCoinName())){
                String coinPrice  = coin.getCoinRate().split(":")[0];
                etc_money = userWallet.getEtcAmnt().multiply(new BigDecimal(coinPrice));
                totalMoney = etc_money.add(totalMoney);
            }*/else if("SAN".equals(coin.getCoinName())){
                String coinPrice  = coin.getCoinRate().split(":")[0];
                san_money = userWallet.getSanAmnt().multiply(new BigDecimal(coinPrice));
                totalMoney = san_money.add(totalMoney);
            }
        }
        List<Map<String,Object>> coinList = new ArrayList<Map<String,Object>>();
        //人民币
        /*Map<String,Object> coinMap1 = new HashMap<String,Object>();
        coinMap1.put("address", "");
        coinMap1.put("coin_name", "RMB");
        coinMap1.put("coin_name_brief", "人民币");
        coinMap1.put("coin_amnt", userWallet.getMoney());
        coinMap1.put("froze_amnt", userWallet.getFrozeRmbAmnt());
        coinMap1.put("coin_price", "");
        coinMap1.put("percent", userWallet.getMoney().multiply(new BigDecimal("100")).divide(totalMoney, 2, RoundingMode.HALF_UP).toString()+"%");
        coinList.add(coinMap1);*/
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
            Map<String,Object> coinMap2 = new HashMap<String,Object>();
            if("BTC".equals(coin.getCoinName())){
                String coinPrice  = coin.getCoinRate().split(":")[0];
                btc_money = userWallet.getBtcAmnt().multiply(new BigDecimal(coinPrice));
                coinMap2.put("coin_name", "BTC");
                coinMap2.put("address", btcAddress);
                coinMap2.put("coin_amnt", userWallet.getBtcAmnt());
                coinMap2.put("froze_amnt", userWallet.getFrozeBtcAmnt());
                coinMap2.put("coin_price", coinPrice);
                if(totalMoney.compareTo(BigDecimal.ZERO)==0){
                	coinMap2.put("percent","0.00%");
                }else{
                	coinMap2.put("percent", btc_money.multiply(new BigDecimal("100")).divide(totalMoney, 2, RoundingMode.HALF_UP).toString()+"%");
                }
                
            }else if("ETH".equals(coin.getCoinName())){
                String coinPrice  = coin.getCoinRate().split(":")[0];
                eth_money = userWallet.getEthAmnt().multiply(new BigDecimal(coinPrice));
                coinMap2.put("coin_name", "ETH");
                coinMap2.put("address", ethAddress);
                coinMap2.put("coin_amnt", userWallet.getEthAmnt());
                coinMap2.put("froze_amnt", userWallet.getFrozeEthAmnt());
                coinMap2.put("coin_price", coinPrice);
                if(totalMoney.compareTo(BigDecimal.ZERO)==0){
                	coinMap2.put("percent","0.00%");
                }else{
                	coinMap2.put("percent", eth_money.multiply(new BigDecimal("100")).divide(totalMoney, 2, RoundingMode.HALF_UP).toString()+"%");
                }
                
            }else if("LTC".equals(coin.getCoinName())){
                String coinPrice  = coin.getCoinRate().split(":")[0];
                ltc_money = userWallet.getLtcAmnt().multiply(new BigDecimal(coinPrice));
                coinMap2.put("coin_name", "LTC");
                coinMap2.put("address", ltcAddress);
                coinMap2.put("coin_amnt", userWallet.getLtcAmnt());
                coinMap2.put("froze_amnt", userWallet.getFrozeLtcAmnt());
                coinMap2.put("coin_price", coinPrice);
                if(totalMoney.compareTo(BigDecimal.ZERO)==0){
                	coinMap2.put("percent","0.00%");
                }else{
                	coinMap2.put("percent", ltc_money.multiply(new BigDecimal("100")).divide(totalMoney, 2, RoundingMode.HALF_UP).toString()+"%");
                }
                
            }/*else if("HLC".equals(coin.getCoinName())){//合链币
                String coinPrice  = coin.getCoinRate().split(":")[0];
                hlc_money = userWallet.getHlcAmnt().multiply(new BigDecimal(coinPrice));
                coinMap2.put("coin_name", "HLC");
                coinMap2.put("address", "");
                coinMap2.put("coin_amnt", userWallet.getHlcAmnt());
                coinMap2.put("froze_amnt", userWallet.getFrozeHlcAmnt());
                coinMap2.put("coin_price", coinPrice);
                coinMap2.put("percent", hlc_money.multiply(new BigDecimal("100")).divide(totalMoney, 2, RoundingMode.HALF_UP).toString()+"%");
            }else if("ETC".equals(coin.getCoinName())){
                String coinPrice  = coin.getCoinRate().split(":")[0];
                String etc_amnt =String.valueOf(userWallet.getEtcAmnt());
                etc_money = new BigDecimal(etc_amnt).multiply(new BigDecimal(coinPrice));
                coinMap2.put("coin_name", "ETC");
                coinMap2.put("address", "");
                coinMap2.put("coin_amnt", userWallet.getEtcAmnt());
                coinMap2.put("froze_amnt", userWallet.getFrozeEtcAmnt());
                coinMap2.put("coin_price", coinPrice);
                coinMap2.put("percent", etc_money.multiply(new BigDecimal("100")).divide(totalMoney, 2, RoundingMode.HALF_UP).toString()+"%");
            }*/else if("SAN".equals(coin.getCoinName())){
                String coinPrice  = coin.getCoinRate().split(":")[0];
                String san_amnt =String.valueOf(userWallet.getSanAmnt());
                san_money = new BigDecimal(san_amnt).multiply(new BigDecimal(coinPrice));
                coinMap2.put("coin_name", "SAN");
                coinMap2.put("address", sanAddress);
                coinMap2.put("coin_amnt", userWallet.getSanAmnt());
                coinMap2.put("froze_amnt", 0);
                coinMap2.put("coin_price", coinPrice);
                if(totalMoney.compareTo(BigDecimal.ZERO)==0){
                	coinMap2.put("percent","0.00%");
                }else{
                	coinMap2.put("percent", san_money.multiply(new BigDecimal("100")).divide(totalMoney, 2, RoundingMode.HALF_UP).toString()+"%");
                }
                
            }
            
            coinList.add(coinMap2);
        }
        
        data.put("coinList", coinList);
        data.put("totalMoney", totalMoney);
        
        return data;
       
    }
    
}
