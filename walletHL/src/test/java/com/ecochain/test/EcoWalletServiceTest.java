package com.ecochain.test;

import java.math.BigDecimal;
import java.util.UUID;

import org.bitcoinj.core.Coin;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.wallet.Wallet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import com.ecochain.EcoWalletApplication;
import com.ecochain.wallet.service.EcoWalletService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EcoWalletApplication.class)
public class EcoWalletServiceTest {
    
    Logger log = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private EcoWalletService ecoWalletService;
    
    NetworkParameters params = TestNet3Params.get();
    
    static String receiverAddress = "n48XR2byazUqxYw6opLpjKVuSRYp7dSuHf";
    
    @Test
    public void testCreateWallet(){
        
        //创建并同步数据
        String address = ecoWalletService.createWallet(params, "test1", "123456");
        log.info("address1 = " + address);
        
        
        
        address = ecoWalletService.createWallet(params, "test2", "123456");
        log.info("address2 = " + address);
        
    }
    
//    @Test
//    public void testFetchAndDumpAndSendMoney(){
//        String userName = "test1";
//        
//        String address = ecoWalletService.fetchBlock(params, userName);
//        Wallet wallet = ecoWalletService.dumpWallet(userName, "mpZWm3rTmkwy3wCCu621MMdqLMDRSnqZxK");
//        log.info("address= " + address + "\n  " + wallet);
//        ecoWalletService.sendMoney(params, userName,"123456", Coin.parseCoin("0.0009"), receiverAddress);
//        address = ecoWalletService.fetchBlock(params, userName);
//        wallet = ecoWalletService.dumpWallet(userName, "mpZWm3rTmkwy3wCCu621MMdqLMDRSnqZxK");
//        log.info("address= " + address + "\n sendMoney after :  \n" + wallet);
//    }
//    
//    @Test
//    public void testSendMoney(){
//        String userName = "test1";
//        ecoWalletService.sendMoney(params, userName,"123456", Coin.parseCoin("0.0009"), receiverAddress);
//    }
    
//    @Test
//    public void testBuyBTC(){
//        ecoWalletService.buyBTC("test2", "123456", new BigDecimal(0.0001), 1);
//    }
    
//    @Test
//    public void testFetchBlock(){
//        String userName = UUID.randomUUID().toString();
////        userName = "27fff621-24d9-49a6-9dbf-ffc6b4ff0eb3";//uuid
//        userName = "testxiong";
////        String address = ecoWalletService.fetchBlock(params, userName);
//        String address = ecoWalletService.fetchBlock(params, userName);
//        log.info("address1 = " + address);
//        userName = UUID.randomUUID().toString();
////        address = ecoWalletService.fetchBlock(params, userName);
////        userName = "392e50be-a3b2-4133-9aeb-45c9d9f1bf0c";
//        userName = "test2";
////        address = ecoWalletService.fetchBlock(params, userName);
//        log.info("address2 = " + address);
//    }
    
    @Test
    public void dumpWallet(){
        String userName = "test2";
        log.info("\n"+ecoWalletService.dumpWallet(userName, "mpZWm3rTmkwy3wCCu621MMdqLMDRSnqZxK").toString());
//        ecoWalletService.dumpWallet("392e50be-a3b2-4133-9aeb-45c9d9f1bf0c", "mkEkUfJFfNT2e23tDC8ZJceFaKAmGB7ZGw");
    }
    
    private void add(String address){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        
        
        HttpEntity<String> formEntity = new HttpEntity<String>(address, headers);

        for (int i = 0; i < 10; i++) {
            String result = restTemplate.postForObject("http://faucet.xeno-genesis.com/", formEntity, String.class);
            
        }
        
    }
    
}
