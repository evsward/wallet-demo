package com.ecochain.test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import org.bitcoinj.core.Coin;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Sign;
import org.web3j.utils.Convert;

import com.ecochain.util.AES;
import com.ecochain.util.EtherWalletUtil;
import com.ecochain.util.EtherWalletUtil.EtherWallet;

public class SomeTest {
    public static void main(String[] args) {
        
        BigDecimal pare = new BigDecimal("1.1");
        
        BigDecimal rmb = new BigDecimal("1");

        
        System.out.println(pare.compareTo(rmb));
        test();
    }
    
    @Test
    public void testETH(){
        EtherWalletUtil util = new EtherWalletUtil();
        EtherWallet wallet = util.createWallet();
        System.out.println( wallet.getAddress());
        System.out.println(wallet.getPrivateKeys());
        
        String privateKey = "108650470365878924559519356195079918833907710797744364808042769542823359301559";
        String publicKey  = wallet.getPublicKey();
//      credentials = Credentials.create(privateKey);
        ECKeyPair ecKeyPair = new ECKeyPair(new BigInteger(privateKey), new BigInteger(publicKey));
        Credentials credentials = Credentials.create(ecKeyPair);
        System.out.println(credentials.getAddress());
        
        BigInteger pub = Sign.publicKeyFromPrivate(new BigInteger(privateKey));
        System.out.println(pub + "," + publicKey);
        
        ecKeyPair = new ECKeyPair(new BigInteger(privateKey), pub);
        credentials = Credentials.create(ecKeyPair);
        
        System.out.println(credentials.getAddress());
        BigInteger value = Convert.toWei("0.9", Convert.Unit.ETHER).toBigInteger();
        System.out.println(value.toString());
        
        System.out.println(Coin.valueOf(10000).toPlainString());
    }
    
    public static void test(){
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://192.168.200.86:8001/{net}/create/{userName}/{passWord}";
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        Map<String, Object> urlVariables = new HashMap<String, Object>();
        urlVariables.put("net", "test");
        urlVariables.put("userName", "test1231asd");
        urlVariables.put("passWord", "123456");
        String resultDataDto = restTemplate.postForObject(url, null, String.class, urlVariables);
        System.out.println(resultDataDto);
    }
}
