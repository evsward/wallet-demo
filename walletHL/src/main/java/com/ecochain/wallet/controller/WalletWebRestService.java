package com.ecochain.wallet.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ecochain.client.Web3JClient;
import com.ecochain.constant.CodeConstant;
import com.ecochain.util.AES;
import com.ecochain.util.EtherWalletUtil;
import com.ecochain.wallet.controller.app.NewMoonToken;
import com.ecochain.wallet.entity.EcoWallet;
import com.ecochain.wallet.service.WalletWebService;
import com.google.gson.Gson;
import com.tfcc.client.ws.CoralWebSocketClient;
import com.tfcc.core.Wallet;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.web3j.abi.datatypes.Address;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lisandro on 2017年8月17日21:32:08
 */
@RestController
@RequestMapping(value = "/api/wallet")
@Api(value = "SAN")
public class WalletWebRestService {

    private final Logger logger = LoggerFactory.getLogger(WalletWebRestService.class);

    @Resource
    private WalletWebService walletWebService;

    @PostMapping("/getSanAddress/{userName}")
    @ApiOperation(nickname = "生成SAN地址", value = "生成SAN地址", notes = "生成SAN地址")
    public AjaxResponse getSanAddress(@PathVariable("userName") String userName) {
        try {
            if (walletWebService.findSanAddress(userName) < 1) {
                EcoWallet ecoWallet = new EcoWallet();
                Wallet wallet = new Wallet();
                ecoWallet.setUserName(userName);
                ecoWallet.setCreateTime(new Date());
                logger.info("userName的SAN地址 --------------->"+wallet.seed());
                ecoWallet.setPrivateKey(AES.encrypt(wallet.seed().getBytes("UTF-8")));
                ecoWallet.setKeyType("5"); //SAN
                ecoWallet.setWalletAddress(wallet.account().address);
                if (walletWebService.saveSanInfo(ecoWallet) > 0) {
                    return AjaxResponse.success("创建SAN地址成功!", ecoWallet.getWalletAddress());
                }else{
                    return AjaxResponse.falied("创建SAN地址失败!", CodeConstant.SYS_ERROR);
                }
            }else{
                return AjaxResponse.falied("创建SAN地址失败,该用户存在!", CodeConstant.ACCOUNT_EXISTS);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return AjaxResponse.falied("创建SAN地址失败!", CodeConstant.SYS_ERROR);
    }

    @PostMapping("/getSanAddressTranscationInfo/{address}")
    @ApiOperation(nickname = "获取SAN地址交易信息", value = "获取SAN地址交易信息", notes = "获取SAN地址交易信息")
    public AjaxResponse getSanAddressTranscationInfo(@PathVariable("address") String address) {
        try {
            Map<String, Object> requestData = new HashMap<>();
            requestData.put("id", 0);
            requestData.put("command", "account_tx");
            requestData.put("account", address);
            requestData.put("offset", 0);
            requestData.put("limit", "100");
            requestData.put("ledger_index_max", -1);
            requestData.put("ledger_index_min",-1);
            String data = new Gson().toJson(requestData);
            String accountInfo = CoralWebSocketClient.request(data);
            if(StringUtils.isNotEmpty(accountInfo)){
                return AjaxResponse.success(JSONObject.parse(accountInfo).toString(), CodeConstant.SC_OK);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return AjaxResponse.falied("获取SAN地址交易信息失败!", CodeConstant.SYS_ERROR);
        }
        return AjaxResponse.falied("获取SAN地址交易信息失败!", CodeConstant.SYS_ERROR);
    }

    public static void main(String[] args) throws  Exception{

        /*int seq = com.tfcc.client.api.util.Utils.getSequence("sEmB6iUA6ZAwLDs7BFEVTyMFTGfJoFZGta");
        System.out.println(seq);
        System.out.println("------------------------->转账之前余额");
        System.out.println(Utils.getAccountInfo("sEmB6iUA6ZAwLDs7BFEVTyMFTGfJoFZGta"));
        com.tfcc.core.Amount amount = com.tfcc.core.Amount.fromDropString(new BigDecimal("0.5").multiply(new BigDecimal(1000000)).longValue()+"");
        int lastLedgerSequence = com.tfcc.client.api.util.Utils.ledgerClosed() + 20;
        com.tfcc.core.types.known.tx.signed.SignedTransaction sign =
                com.tfcc.client.api.util.Utils.payment("rhy1Fpdz4seCAFBCNCQDMZztFVExb", "sM8oaF4S9iBoo35vsWmZzn1duqr75q8Qyf", amount, 2, true, null, null, "123456788", lastLedgerSequence);
        String res = Utils.sendTx(sign.tx_blob);

        System.out.println("------------------------->转账之后余额");
        System.out.println(Utils.getAccountInfo("sEmB6iUA6ZAwLDs7BFEVTyMFTGfJoFZGta"));*/


        /*//调用提币逻辑
        String userInfo = "[{\"coinName\":\"BBT\",\"address\":\"ggggggggggggg\",\"coinAmount\":\"500\"},{\"coinName\":\"CCT\",\"address\":\"cccccccccccccccc\",\"coinAmount\":\"888\"}]";
        // 如果要获取 JSONArray 的值，需要先把获取到的 list 数组转换为字符串，然后转换为 Object 再强转为 JSONArray
        JSONArray noArr = (JSONArray)JSON.parse(userInfo);
        for(Object no : noArr) {
            JSONObject userJB2 = JSON.parseObject(no.toString());
            System.out.println(userJB2.getString("coinName"));
            System.out.println(no.toString());
        }*/

        //web3j
        //1.调用生成地址后自己拼接json
        //2.调用发币接口获取币，若都ok，update  json

        //发币逻辑
        String userInfo = "[{\"coinName\":\"BBT\",\"address\":\"ggggggggggggg\",\"coinAmount\":\"500\"},{\"coinName\":\"CCT\",\"address\":\"cccccccccccccccc\",\"coinAmount\":\"888\"}]";
        // 如果要获取 JSONArray 的值，需要先把获取到的 list 数组转换为字符串，然后转换为 Object 再强转为 JSONArray
        JSONArray noArr = (JSONArray) JSON.parse(userInfo);
        for(Object no : noArr) {
            JSONObject userJB2 = JSON.parseObject(no.toString());
            System.out.println(userJB2.getString("coinName"));
            System.out.println(no.toString());
        }

        EtherWalletUtil.EtherWallet etherWallet = EtherWalletUtil.createWallet();
        StringBuffer ethAddress =new StringBuffer("0x");
        if(etherWallet != null) {
            if (WalletUtils.isValidAddress(etherWallet.getAddress())) {
                ethAddress.append(etherWallet.getAddress());
            }
        }
        System.out.println("instaed coin address --->"+ethAddress.toString());
        Credentials credentials = WalletUtils.loadCredentials("123", "F:/devbin/UTC--2017-01-03T07-22-03.101520928Z--d7cb1bc65cc2ad64a996ed5ce886eb982789b68b");

        BigInteger GAS_PRICE = BigInteger.valueOf(20_000_000_000L);
        BigInteger GAS_LIMIT = BigInteger.valueOf(4_300_000);
        NewMoonToken contract = NewMoonToken.load(
                "0x7f99a6bbf7c5f46bf62a49129d2ec1ecb9a2937f",
                 Web3JClient.getClient(), credentials, GAS_PRICE, GAS_LIMIT);
        System.out.println(contract.name().get());
        //contract.transfer(new Address("0xb88559612d632445f9bebd0bb543f8fb0791c15c"), new Uint256(10)).get();
        System.out.println(contract.balanceOf(new Address("0xb88559612d632445f9bebd0bb543f8fb0791c15c")).get().getValue());
        System.out.println(contract.balanceOf(new Address("0xf51b596e68c15b23a1d5fc97e7ed21781bb015ee")).get().getValue());
        //contract.transferFrom(new Address("0xb88559612d632445f9bebd0bb543f8fb0791c15c"),new Address("0xf51b596e68c15b23a1d5fc97e7ed21781bb015ee"), new Uint256(1)).get();

       /* TransactionReceipt transactionReceipt = contract.someMethod(
                new Type(...),...).get();


        Type result = contract.someMethod(new Type(...), ...).get();*/
    }


}