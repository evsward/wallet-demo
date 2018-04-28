package com.ecochain.task;


import com.alibaba.fastjson.JSONObject;
import com.ecochain.util.AES;
import com.ecochain.wallet.entity.IcoWithdrawRecord;
import com.ecochain.wallet.mapper.EcoWalletMapper;
import com.ecochain.wallet.mapper.IcoWithdrawRecordMapper;
import com.ecochain.wallet.mapper.UserCoinMapper;
import com.ecochain.wallet.mapper.UserWalletMapper;
import com.tfcc.client.api.util.Utils;
import com.tfcc.core.types.known.tx.signed.SignedTransaction;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * create by lisandro 2017年8月19日15:01:08
 * describe:SAN 定时提现处理
 */
@Service
public class AutomaticWithDrawThread implements Runnable {

    @Autowired
    private IcoWithdrawRecordMapper icoWithdrawRecordMapper;

    @Autowired
    private UserWalletMapper userWalletMapper;

    @Autowired
    private UserCoinMapper userCoinMapper;

    @Autowired
    private EcoWalletMapper ecoWalleTMapper;

   /* private  final static String ECO_WALLET_ADDRESS="srTX2Vvdb2WmBVhRcLnE8BVcyB7pMd393n";
    private  final static String ECO_WALLET_PASS="rrfDy2eXSvZ5g923aGxNvttQ6Prqf";
*/

    @Value("${SAN.wallet.address}")
    private  String ECO_WALLET_ADDRESS;
    @Value("${SAN.wallet.password}")
    private  String ECO_WALLET_PASS;

    private static final Logger logger = LogManager.getLogger(AutomaticWithDrawThread.class);

    //@Scheduled(cron = "0 0/1 9-22 * *  ? ")
    @Transactional
    @Override
    public void run() {
        try {
            logger.info("<-------------------- Start to SAN deal withdraw orders. -------------------->");
            try {
                //从ico_withdraw_record表 扫描所有订单状态为1的订单，然后发送交易   SAN 手续费0.1%
                List<IcoWithdrawRecord> IcoWithdrawRecordlist = icoWithdrawRecordMapper.getWithDrawOrder();
                Collection<IcoWithdrawRecord> orderList = IcoWithdrawRecordlist;
                if (!CollectionUtils.isEmpty(orderList)) {
                    for (IcoWithdrawRecord IcoWithdrawRecord : orderList) {
                        try {
                            processOrder(IcoWithdrawRecord);
                        } catch (Exception e) {
                            logger.error(e.getMessage(), e);
                        }
                    }
                } else {
                    logger.info("No withdraw orders found, try next time.");
                }
            } catch (Exception ex) {
                logger.error("Deal withdraw orders error,", ex);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void processOrder(IcoWithdrawRecord order) throws Exception, com.tfcc.client.api.exception.APIException {
        logger.info("Deal order:" + order.getId() + ", amount=" + order.getMoney());
        if (StringUtils.isNotEmpty(order.getTxHash())) {
            if (!order.getTxHash().endsWith("-errorhash")) {
                logger.info("withdraw orders OuterTxHas is not null, ignored." + order.getId());
                return;
            }
        }
        /*Double balance = getUserBalance(order.getAddress());
        if (balance <= 0.002 && order.getMoney().doubleValue() <=0) {
            logger.info("Wwithdraw:balance is zero :id=" + order.getId() + ",balance=" + balance);
            return;
        }*/
        /*Double fee = order.getMoney().divide(new BigDecimal(1000), 6, BigDecimal.ROUND_HALF_UP).setScale(6).doubleValue();
        fee = Math.max(0.002, fee);
        BigDecimal realAmount = new BigDecimal(order.getMoney().doubleValue() - fee).setScale(6, BigDecimal.ROUND_HALF_UP);*/
        int seq = com.tfcc.client.api.util.Utils.getSequence(ECO_WALLET_ADDRESS);
        //提币查询是否ICO内部地址，是直接操作DB 扣手续费
        Map parm =new HashMap();
        parm.put("address",order.getAddress());
        parm.put("currency",order.getCurrency());
        BigDecimal orignlMoney =order.getMoney();
        String orginlUserId= order.getUserId();
        String userId =userCoinMapper.findTransferUserIdByConinAddress(parm);
        if(StringUtils.isNotEmpty(userId)){  //系统内部转账不调用三界链
            order.setUserId(userId);         //内部转账的用户ID
            //order.setMoney(realAmount);      //转出扣除手续费的SAN
            if(this.userWalletMapper.innerTransferSan(order) > 0){
                order.setStatus("3");
                order.setMoney(orignlMoney);
                order.setUserId(orginlUserId);
                order.setFree(new BigDecimal(0));
                order.setWithdrawMsg("System Inner Transfer");
                icoWithdrawRecordMapper.updateByPrimaryKey(order);
                logger.info("Deal System order:" + order.getId() + " withdraw Success !!!");
                    return;
            }else {
                logger.info("Deal System order:" + order.getId() + " withdraw Failure !!!");
                return;
            }
        }
        //BigDecimal realAmount = order.getAmount().subtract(order.getFee()).setScale(6,BigDecimal.ROUND_HALF_DOWN);
        //BigDecimal realAmount = order.getMoney().setScale(6, BigDecimal.ROUND_HALF_DOWN);
        BigDecimal amountBD = order.getMoney().multiply(new BigDecimal(1000000));
        com.tfcc.core.Amount amount = com.tfcc.core.Amount.fromDropString(amountBD.longValue() + "");
        String destination = order.getAddress();//提现地址
        int lastLedgerSequence = com.tfcc.client.api.util.Utils.ledgerClosed() + 10;
        if (lastLedgerSequence < 1000000) { //未取到ledger 不做处理
            logger.info("withdraw id " +order.getId() +" withdraw uncompleted,because ledger to small.");
            return;
        }else{
            logger.info("The User Private key before decrypt is -------->"+ECO_WALLET_PASS);
            logger.info("The User Private key after  decrypt is -------->"+AES.decrypt(ECO_WALLET_PASS));
            SignedTransaction sign = Utils.payment(AES.decrypt(ECO_WALLET_PASS), destination, amount, seq, true, null, null, order.getId() + "", lastLedgerSequence);
            order.setStatus("2");
            //order.setTxHash(sign.hash.toHex());
                String withdrawMsg = Utils.sendTx(sign.tx_blob);
            if (StringUtils.isNotEmpty(withdrawMsg)) {
                //TODO 用户SAN提币金额加入冻结金额
                //直接扣除用户SAN余额
               /* Map updateMap =new HashMap();
                updateMap.put("user_id",order.getUserId());
                updateMap.put("realAmount",order.getMoney());*/
                order.setWithdrawMsg(withdrawMsg);
                JSONObject  jsonObject = JSONObject.parseObject(withdrawMsg);
                order.setTxHashBak(sign.hash.toHex());
                order.setTxHash(jsonObject.getJSONObject("result").getJSONObject("tx_json").getString("hash"));
                order.setFree(new BigDecimal(0.001));
                icoWithdrawRecordMapper.updateByPrimaryKey(order);
                //userWalletMapper.updateSanAmount(updateMap);
                logger.info("Deal order:" + order.getId() + " update completed, broadcast tx to network.");
                logger.info("Deal order:" + order.getId() + " update completed, broadcast tx to network.res=" + withdrawMsg);
            }else{
                return;
            }
        }
        /*Map userAddressInfo = ecoWalleTMapper.getUserInfo(order.getUserId());
        if (userAddressInfo.containsKey("walletAddress") && userAddressInfo.containsKey("privateKey")) {

        }else{
            return;
        }*/
    }

    private static Double getUserBalance(String address){
        try {
            String accountInfo = com.tfcc.client.api.util.Utils.getAccountInfo(address);
            JSONObject jsonObject;

            if (StringUtils.isEmpty(accountInfo)) {
                return 0d;
            }

            jsonObject = JSONObject.parseObject(accountInfo);
            if (!jsonObject.containsKey("result") || !jsonObject.getJSONObject("result").containsKey("account_data")) {
                return 0d;
            }
            BigDecimal san = new BigDecimal(jsonObject.getJSONObject("result").getJSONObject("account_data").getString("Balance"));
            return san.divide(new BigDecimal(1000000), 6, BigDecimal.ROUND_DOWN).doubleValue();
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return 0d;
    }


    public static void main(String[] args) throws Exception{
        String json="{\n" +
                "    \"result\":{\n" +
                "        \"tx_json\":{\n" +
                "            \"Account\":\"srTX2Vvdb2WmBVhRcLnE8BVcyB7pMd393n\",\n" +
                "            \"Destination\":\"sM8oaF4S9iBoo35vsWmZzn1duqr75q8Qyf\",\n" +
                "            \"TransactionType\":\"Payment\",\n" +
                "            \"TxnSignature\":\"3044022037A8A3FAFB392D5527B5F61AE81390F4BB7F75957BB466E712160F4D834E4651022033FDBC430890AC8BEE02584EB5C6BA5AA9BF5028A46ABC225B8563D2BF7B9532\",\n" +
                "            \"SigningPubKey\":\"02201536305B7F6ED77312916709138017CFD46C6B32E721088F42EC7AD84AE8A1\",\n" +
                "            \"Amount\":\"499000\",\n" +
                "            \"Fee\":\"1000\",\n" +
                "            \"Flags\":2147483648,\n" +
                "            \"Sequence\":18,\n" +
                "            \"LastLedgerSequence\":1450942,\n" +
                "            \"hash\":\"3FBA27410AEE47690B9CC3DCA454F1DE5CF6CEAC622A91A1631B231E6AC3894C\"\n" +
                "        },\n" +
                "        \"engine_result_code\":0,\n" +
                "        \"tx_blob\":\"12000022800000002400000012201B001623BE614000000000079D386840000000000003E8732102201536305B7F6ED77312916709138017CFD46C6B32E721088F42EC7AD84AE8A174463044022037A8A3FAFB392D5527B5F61AE81390F4BB7F75957BB466E712160F4D834E4651022033FDBC430890AC8BEE02584EB5C6BA5AA9BF5028A46ABC225B8563D2BF7B953281141AF497688A9A17DCA3A62E2B1D6B76E92A9E39708314E40BB76F1489F6E6FD81BB990A8A391CA15ACD6A\",\n" +
                "        \"engine_result\":\"tesSUCCESS\",\n" +
                "        \"engine_result_message\":\"The transaction was applied. Only final in a validated ledger.\"\n" +
                "    },\n" +
                "    \"id\":0,\n" +
                "    \"type\":\"response\",\n" +
                "    \"status\":\"success\"\n" +
                "}";
        //System.out.println(Utils.checkTxnSuccess("DA5C17600250FA63949DCD637BB8AD5B51787D546574259D178535684D6C412D"));
        JSONObject  jsonObject = JSONObject.parseObject(json);
        System.out.println(jsonObject.getJSONObject("result").getJSONObject("tx_json").getString("hash"));
    }
}
