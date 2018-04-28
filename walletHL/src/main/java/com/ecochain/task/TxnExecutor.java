
package com.ecochain.task;


import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.ecochain.wallet.entity.RechargeRecord;
import com.ecochain.wallet.entity.UserCoin;
import com.ecochain.wallet.entity.UserWallet;
import com.ecochain.wallet.entity.WithdrawRecord;
import com.ecochain.wallet.service.EcoWalletService;
import com.ecochain.wallet.service.RechargeRecordService;
import com.ecochain.wallet.service.UserLoginService;
import com.ecochain.wallet.service.UserWalletService;
import com.ecochain.wallet.service.UsersDetailsService;
import com.ecochain.wallet.service.WithdrawRecordService;

@Service
public class TxnExecutor {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(TxnExecutor.class);
    private static final String PREFIX_OF_TXNLOG_LOCK = "mutex_txn_log_";

    private static final String LISTEN_REDIS_KEY = "REDIS_IMFORM_TFCC_INFO_IDS";
//    private static String HOTWALLET = Config.getInstance().getProperty("SAN.deposit.hotwallet");


    @Autowired
    WithdrawRecordService withdrawRecordService;

    @Autowired
    RechargeRecordService rechargeRecordService;
    @Autowired
    UserWalletService userWalletService;

    @Autowired
    EcoWalletService ecoWalletService;

    @Autowired
    UserLoginService userLoginService;

    @Autowired
    UsersDetailsService usersDetailsService;

//
//    @Autowired
//    RemoteElecoinService remoteElecoinService;

//    @Autowired
//    JedisClient jedisClient;

    private Executor taskExecutor =Executors.newFixedThreadPool(10);;

    public void runTask(JSONObject task) {
        this.taskExecutor.execute(new PushTask(task));
    }

    private class PushTask implements Runnable {
        private JSONObject task;

        private  final int startIndex = 10;

        PushTask(JSONObject task) {
            logger.info(Thread.currentThread().getName() + ":runtask:"+task.toString());
            this.task = task;
        }


/**
         * 1、获取源地址F以及目标地址D、交易Hash: H
         * 2、根据F、 D查询交易账号是否在监控账号内，若是：
         * 3、根据F查询热钱包地址，判断是充值还是提现，若提现，转4，否则转5
         * 4、提现：根据交易hash查询订单，修改订单状态
         * 5、充值：首先根据D查询elecoin_info，则存储elecoin_order、生成txnlog等操作
         */


        @Transactional(rollbackFor=Exception.class,propagation= Propagation.REQUIRED)
        public void run() {
            String mutexKey;
            Long lock;

            String mutexKeyTxnHash = "";
            Long lockTxnHash = 0L;

//                mutexKeyTxnHash = PREFIX_OF_TXNLOG_LOCK + txnHash;
//                lockTxnHash = jedis.setnx(mutexKeyTxnHash, "");
//                if(lockTxnHash <= 0){
//                    if (jedis.ttl(mutexKeyTxnHash) == -1) {
//                        jedis.expire(mutexKeyTxnHash, 300);
//                    }
//                    logger.info("the txn is processing, just return:"+txnHash );
//                    return;
//                }

                try {
                    String destination = task.getString("Destination");
                    String txnHash = task.getString("hash");
                    BigDecimal amount = new BigDecimal(task.getString("Amount"))
                            .divide(new BigDecimal(1000000),6,BigDecimal.ROUND_HALF_UP);
                    BigDecimal fee = new BigDecimal(task.getString("Fee"))
                            .divide(new BigDecimal(1000000),6,BigDecimal.ROUND_HALF_UP);
                    String account = task.getString("Account");
                    System.out.println("当前的hash"+txnHash);

                    WithdrawRecord withdrawRecord = getByTxnHash(txnHash);

                    if(withdrawRecord==null){ //充值
                        //根据钱包地址找到用户
                        UserCoin userCoin = usersDetailsService.getUserCoinByAddress(destination);

                        //1. 插入充值记录   status = 3   currency=SAN
                        RechargeRecord rechargeRecord = new RechargeRecord();
                        rechargeRecord.setStatus("3");
                        rechargeRecord.setCurrency("SAN");
                        rechargeRecord.setAddress(destination);
                        rechargeRecord.setByUserId("system");
                        rechargeRecord.setCreateTime(new Date());
                        rechargeRecord.setTxHash(txnHash);
                        rechargeRecord.setFree(fee);
                        rechargeRecord.setMoney(amount);
                        rechargeRecord.setUserId(userCoin.getUserId().toString());


                        //2. 更新钱包表 user_wallet 余额
//                    UserWallet wallet = userWalletService.getWalletByUserId(userid);
                        UserWallet wallet = new UserWallet();
                        wallet.setSanAmnt(amount);
                        wallet.setUserId(userCoin.getUserId());
                        wallet.setOperator("SanTask");
                        wallet.setModifyTime(new Date());

                        //事物  否则不生效
                        rechargeRecordService.RechargeAndAddBalance(rechargeRecord,wallet);
//
//
                    }else {
                        String currency = withdrawRecord.getCurrency();
                        String status = withdrawRecord.getStatus();
                        if("SAN".equals(currency) && "2".equals(status)){
                            withdrawRecord.setStatus("3");
                            withdrawRecordService.updateTxHashById(withdrawRecord);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }


        private WithdrawRecord getByTxnHash(String txnHash){
            try {
                WithdrawRecord withdrawRecordByTxHash = withdrawRecordService.findWithdrawRecordByTxHash(txnHash);
                return withdrawRecordByTxHash;
            }catch (Exception e){
                logger.error(e.getMessage(),e);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                logger.info("getbytxnhash retry:"+txnHash);
                return withdrawRecordService.findWithdrawRecordByTxHash(txnHash);
            }
        }

//        private int processOrder(ElecoinOrder order, String txnHash){
//            try {
//                if(order.getType() == ElecoinOrder.Type.WITHDRAW.value()){
//                    //已经更新，跳过
//                    if (order.getStatus() == ElecoinOrder.Status.DONE.value()) {
//                        return 1;
//                    }
//                    order.setStatus(ElecoinOrder.Status.DONE.value());
//                    order = remoteElecoinService.updateOrder(order);
//                }else if(order.getType() == ElecoinOrder.Type.DEPOSIT.value()){
//                    //充值逻辑入口
//                    //已更新过状态，跳过
//                    if (order.getStatus() != ElecoinOrder.Status.INIT.value()) {
//                        return 1;
//                    }
//                    order.setStatus(ElecoinOrder.Status.CONFIRM.value());
//                    User user = remoteUserService.getById(order.getUserId());
//                    TxnLog txnLog = TxnLog.generateTxnLog(
//                            String.valueOf(order.getId()),
//                            HOTWALLET,
//                            user.getAddress(),
//                            CurrencyCodeUtils.getCurCode(ElecoinInfo.CoinType.SAN.name()),
//                            order.getIssuer(),
//                            order.getAmount(),
//                            InitConst.ASYNC_TASKTYPE_ELECOIN,
//                            InitConst.TXN_LOG_TXNTYPE_PAYMENT);
//                    remoteElecoinService.updateOrderAndPay(order, txnLog);
//                }else {
//                    logger.info("task ignored:" + txnHash + ",orderId="+order.getId() + ",orderType="+order.getType());
//                }
//            }catch (Exception e){
//                logger.error("processOrder"+e.getMessage(),e);
//                logger.error("processOrder,txnHash="+txnHash);
//                return 0; //need retry
//            }
//            return 1;
//        }

//        private List<ElecoinOrder> geneterateDepositOrder(String address, String amountStr, String txnHash, int retryTimes) {
//            try {
//                List<ElecoinOrder> orderList = new ArrayList<>();
//                BigDecimal amount = new BigDecimal(amountStr);
//                if (amount.compareTo(new BigDecimal(100)) == -1) {
//                    logger.info("receive coin balance is less than 100u, do not accept.txnHash:"+txnHash);
//                    return orderList;
//                }
//
//                amount = amount.divide(new BigDecimal(1000000)).setScale(6);
//
//                //获取存款地址，状态不限
//                ElecoinInfo info = remoteElecoinService.getElecoinByAddress(address,ElecoinInfo.CoinType.SAN.value(),ElecoinInfo.Type.DEPOSIT.value(), null,null);
//                if (info == null) {
//                    logger.error("transactionOutput address: " + address + " has no associated info record.");
//                    return orderList;
//                }
//                ElecoinOrder order = remoteElecoinService.getOrder(info.getUserId(), txnHash, info.getId());
//                if (order == null) {
//                    order = new ElecoinOrder();
//                    order.setCurrency(ElecoinInfo.CoinType.SAN.name());
//                    order.setAmount(amount);
//                    order.setStatus(ElecoinOrder.Status.INIT.value());
//                    order.setOuterTxHash(txnHash);
//                    order.setIssuer(CurrencyCodeUtils.getIssuer(ElecoinInfo.CoinType.SAN.name()));
//                    order.setType(ElecoinOrder.Type.DEPOSIT.value());
//                    order.setCreatedAt(System.currentTimeMillis());
//                    order.setUpdatedAt(System.currentTimeMillis());
//                    order.setInfoId(info.getId());
//                    order.setOuterAddress(info.getAddress());
//                    order.setUserId(info.getUserId());
//                    order.setFee(new BigDecimal(0));
//                    orderList.add(order);
//                } else {
//                    logger.info("Order has been saved for tx:" + txnHash + ", infoId:" + info.getId());
//                }
//                if (CollectionUtils.isNotEmpty(orderList)) {
//                    orderList = remoteElecoinService.saveOrderList(orderList);
//                }
//                return orderList;
//            }catch (Exception e){
//                logger.error("geneterateDepositOrder"+e.getMessage(),e);
//                logger.info("geneterateDepositOrder retry:"+txnHash);
//                retryTimes ++;
//                if(retryTimes < 5){
//                    try {
//                        Thread.sleep(100);
//                    } catch (InterruptedException e1) {
//                        e1.printStackTrace();
//                    }
//                    return geneterateDepositOrder(address,amountStr,txnHash,retryTimes);
//                }else {
//                    logger.info("geneterateDepositOrder retry upto maxtimes:"+txnHash);
//                    return new ArrayList<>();
//                }
//            }
//
//        }

    }
}

