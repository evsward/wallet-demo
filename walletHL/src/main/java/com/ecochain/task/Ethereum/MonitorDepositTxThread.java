package com.ecochain.task.Ethereum;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import lombok.Data;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import com.ecochain.client.Web3JClient;
import com.ecochain.util.OrderGenerater;
import com.ecochain.wallet.entity.RechargeRecord;
import com.ecochain.wallet.entity.UserCoin;
import com.ecochain.wallet.entity.UserWallet;
import com.ecochain.wallet.service.RechargeRecordService;
import com.ecochain.wallet.service.UserWalletService;
import com.ecochain.wallet.service.UsersDetailsService;
import com.ecochain.wallet.util.WalletConstant;

/**
 * 
* @ClassName: MonitorDepositTxThread 
* @Description: TODO(监控区块信息，将交易信息是我们平台的记录，增加充值记录) 
* @author 焦博韬  
* @date 2017年10月12日 上午10:49:07 
*
 */
@Data
@Component("monitorDepositTxThread")
public class MonitorDepositTxThread implements Runnable {

    private Logger logger = Logger.getLogger(MonitorDepositTxThread.class);

    @Autowired
    private EtherHotWalletTool etherHotWalletTool;

    @Autowired
    private RechargeRecordService rechargeRecordService;

    @Autowired
    private UserWalletService userWalletService;

    @Autowired
    private UsersDetailsService usersDetailsService;

    @Value("wallet.address.hot")
    private String walletAddressHot;

    @Value("wallet.address.cold")
    private String walletAddressCold;

    private static Web3j web3j = Web3JClient.getClient();

    private BigInteger lastBlock ;

//    @Scheduled(cron = "0 1/3 * * * ? ")
    @Override
    public void run() {
        logger.debug("============================充值交易检测线程！开始！================================");
        lastBlock = etherHotWalletTool.getLastBlock();
        if (lastBlock.compareTo(new BigInteger("4229671")) < 0){
            lastBlock = new BigInteger("4229670");
        }//0xeee5221d7bf6ae08e11317162f603eb5342da3255f0a05ad5e0209dfa4a16f20
        logger.info("get last Block:" + lastBlock + " from redis!");
        startEtherScanDeposit();
        logger.debug("============================充值交易检测线程！结束！================================");
    }

    public void startEtherScanDeposit() {
        if (lastBlock.compareTo(BigInteger.ZERO) == 0) {
            logger.info("First run,the lastBlock is 0");
            saveEtherDespoist(DefaultBlockParameterName.LATEST);
            // reset lastBlock
            //lastBlock = this.etherHotWalletTool.getLastBlock();

        } else if (lastBlock.compareTo(BigInteger.ZERO) == 1) {
            EthBlockNumber maxblockNumber = null;
            try {
                maxblockNumber = web3j.ethBlockNumber().sendAsync().get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            if (maxblockNumber == null) {
                logger.error("The maxBlockNumber is null, we retry later!");
                return;
            }

            BigInteger endBlock = maxblockNumber.getBlockNumber();
            logger.info("begin scan block from startBlock:" + lastBlock.longValue() + " to endBlock:"
                    + endBlock.longValue());

            for (; lastBlock.compareTo(endBlock) < 0; lastBlock = lastBlock.add(BigInteger.ONE)) {
                DefaultBlockParameter param = new DefaultBlockParameterNumber(lastBlock);
                saveEtherDespoist(param);
            }
        }
    }

    public void saveEtherDespoist(DefaultBlockParameter param) {
        try {
            EthBlock _block = web3j.ethGetBlockByNumber(param, true).send();
            for (EthBlock.TransactionResult<Transaction> txResult : _block.getResult().getTransactions()) {
                Transaction _tx = txResult.get();
                if (_tx.getTo() == null) {
                    continue;
                }
                String fromAddress = Numeric.cleanHexPrefix(_tx.getFrom()).toLowerCase();
                String toAddress = Numeric.cleanHexPrefix(_tx.getTo()).toLowerCase();
                BigDecimal receivedEther = Convert.fromWei(_tx.getValue().toString(), Convert.Unit.ETHER);
                int txIdx = _tx.getTransactionIndex().intValue();
                String blkNum = _tx.getBlockNumber().toString();
                String txHash = Numeric.cleanHexPrefix(_tx.getHash()).toLowerCase();

                logger.debug("fromAddress="+fromAddress+" toAddress="+toAddress+" receivedEther="+receivedEther+" txIdx="+txIdx
                +" blkNum="+blkNum+" txHash="+txHash );
                logger.debug("gas="+_tx.getGas()+" gasprice="+_tx.getGasPrice()+"Fee: "+_tx.getGasPrice().multiply(_tx.getGas()));
                logger.debug(_tx.getGasPriceRaw()+"============="+_tx.getGasRaw());

                //if (this.etherHotWalletTool.isExistForDepositAddr(toAddress)) {
                    logger.info("scan deposit for addr:" + toAddress);
                    /**
                     * 注：充值额度低于10000000000000000wei(0.01Ether)时，不记录到充值记录表中，以免归集的时候，归集不到。
                     */
                    if(receivedEther.compareTo(new BigDecimal("0.01"))>=0){
                    	saveElecoinOrder(toAddress, txHash, receivedEther, txIdx,
                    	        blkNum,Convert.fromWei(new BigDecimal(_tx.getGasPrice().multiply(_tx.getGas())).toString(), Convert.Unit.ETHER)  );
                    }
                //}
                if(this.etherHotWalletTool.isExistForDepositAddr(fromAddress)) {
                    logger.info("scan withDraw for addr:" + toAddress);

                }
            }
            this.etherHotWalletTool.saveLastBlock(_block.getBlock().getNumber());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public boolean saveElecoinOrder(String toAddress, String txhash, BigDecimal receivedEther, int txIdx,
    String blockNum,BigDecimal fee) {
    	toAddress = "0x"+toAddress;
    	UserCoin userCoin = usersDetailsService.getUserCoinByAddress(toAddress);
        if(userCoin == null || userCoin.getUserId() == null){
        	return false;
        }
        logger.debug("userId========="+userCoin.getUserId());
        String userId = String.valueOf(userCoin.getUserId());
        UserWallet userWallet = userWalletService.getWalletByUserId(userId);
        if(userWallet == null){
        	return false;
        }

        RechargeRecord record = new RechargeRecord();
        String orderNo = OrderGenerater.generateOrderNo();
        record.setByUserId(userId);
        record.setOrderNo(orderNo);

        record.setUserId(userId);
        record.setMoney(receivedEther);
        record.setFree(fee);
        record.setStatus(WalletConstant.FINISH);
        record.setCurrency(EtherHotWalletTool.COIN_TYPE_ETHEREUM);
        record.setCreateTime(new Date());
        record.setTxHash(txhash);
        record.setAddress(toAddress);
        //增加充值记录
        try{
            rechargeRecordService.insertRechargeRecord(record);
        }catch (Exception e){
            e.printStackTrace();
        }


        userWallet.setEthAmnt(userWallet.getEthAmnt().add(receivedEther));
        UserWallet userWallet1 = userWalletService.updateUserWallet(userWallet);
        logger.debug("userWallet.eth amt:"+userWallet1.getEthAmnt());
        return true;
    }



    public static void main(String[] args) {
//        MonitorDepositTxThread  monitorDepositTxThread =new MonitorDepositTxThread();
//        Thread thread = new Thread(monitorDepositTxThread);
//        thread.start();
        String encodeBlkNum = "100";
        try {
            System.out.println(encodeBlkNum.toCharArray());
            System.out.println(Hex.decodeHex(encodeBlkNum.toCharArray()));
        } catch (DecoderException e) {
            e.printStackTrace();
        }
    	
    }

}
