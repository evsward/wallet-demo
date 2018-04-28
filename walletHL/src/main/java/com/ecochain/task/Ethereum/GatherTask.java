package com.ecochain.task.Ethereum;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthTransaction;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.tx.Transfer;

import com.ecochain.client.Web3JClient;
import com.ecochain.constant.Constant;
import com.ecochain.util.AES;
import com.ecochain.wallet.entity.EcoWallet;
import com.ecochain.wallet.entity.EcoWalletExample;
import com.ecochain.wallet.entity.IcoGatherRecord;
import com.ecochain.wallet.entity.IcoRechargeRecord;
import com.ecochain.wallet.mapper.EcoWalletMapper;
import com.ecochain.wallet.service.EcoWalletService;
import com.ecochain.wallet.service.EthWalletService;
import com.ecochain.wallet.service.IcoGatherRecordService;
import com.ecochain.wallet.service.IcoRechargeRecordService;
import com.ecochain.wallet.service.UsersDetailsService;
import com.ecochain.wallet.util.WalletConstant;

/**
 * 
* @ClassName: GatherTask 
* @Description: TODO(归集到热钱包) 
* @author dxm 
* @date 2017年8月21日 上午9:24:46 
*
 */
@Component
public class GatherTask {

	@Autowired
	private IcoRechargeRecordService rechargeRecordService;
	
	@Autowired
	private EthWalletService ethWalletService;
	
	@Autowired
    private EcoWalletService ecoWalletService;
	
	@Autowired
	private IcoGatherRecordService gatherRecordService;
	
	@Autowired
    private UsersDetailsService userDetailsService;
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	BigDecimal ethMinGatherVal = new BigDecimal(0.001);
	
	@Value("${ETH.wallet.hot.address}")
	private String ethWalletHotAddress;
	
	private static Web3j web3j = Web3JClient.getClient();
	
	@Autowired
    private EcoWalletMapper ecoWalletMapper;
	
	@Scheduled(cron = "0 1/1 * * * ?")
	@Transactional
	public void doGether(){
		logger.info("-------------------gatherThread start-------------------------");
		List<IcoRechargeRecord> recordList = rechargeRecordService.listRechargeRecordByGather();
		if(recordList == null || recordList.size()==0){
			logger.warn("rechargeRecordList is null");
			return;
		}
		for(IcoRechargeRecord record : recordList){
			BigDecimal money = record.getMoney();
			if(money == null || money.compareTo(ethMinGatherVal) == -1){
				logger.warn("orderid:{} gather amount:{} less {},we ignore it.",record.getId(),
						record.getMoney(),ethMinGatherVal);
        		continue;
			}
			String address = record.getAddress();
			if(StringUtils.isNotEmpty(address)){
				BigDecimal balance = ethWalletService.getBalanceWei(address);
				if(balance == null || balance.toBigInteger().equals(BigInteger.ZERO)){
					logger.warn("balance is null or balance is zero");
					continue;
				}
			}
			String userID = record.getUserId();
			if(StringUtils.isEmpty(userID)){
				logger.warn("userid is null");
				continue;
			}
			Map userInfo = userDetailsService.getUserInfoByUserId(Integer.valueOf(userID));
			if(userInfo == null){
				logger.error("userId:{} not find usersDetails data!", userID);
				continue;
			}
			EcoWalletExample example = new EcoWalletExample();
			example.createCriteria().andUserNameEqualTo((String)userInfo.get("user_name")).andKeyTypeEqualTo("2");
			List<EcoWallet> wallets = ecoWalletMapper.selectByExample(example);
			if(wallets == null){
				logger.error("fromAddress:{} userId:{} not find ecoWallet data!", address, userID);
				continue;
			}
			EcoWallet ecoWallet = wallets.get(0);
			if(ecoWallet == null){
				logger.error("The ecoWallet of address:"+address+" is null!");
            	continue;
			}
			try {
				String privateKey = AES.decrypt(ecoWallet.getPrivateKey());
				Credentials credentials = Credentials.create(privateKey);
				if(sendEtherTransaction(ecoWallet, record, credentials)) continue;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		logger.info("-------------------gatherThread end-------------------------");
	}
	
	@Transactional
	public boolean sendEtherTransaction(EcoWallet ecoWallet, IcoRechargeRecord record, 
			Credentials credentials){
		String txHashVal = null;
		String address = record.getAddress();
    	try {
			//交易手续费（矿工费）
    		BigInteger gasPrice = ethWalletService.getGasPrice();
    		BigInteger gasCost = gasPrice.multiply(Transfer.GAS_LIMIT);
    		//send transaction start
    		BigDecimal actualBalance = ethWalletService.getBalanceWei(address);
    		if(actualBalance == null || actualBalance.toBigInteger().equals(BigInteger.ZERO)){
    			logger.warn("userID:{},address:{},balance is null or balance is zero", 
    					record.getUserId(), address);
    			return false;
    		}
    		BigInteger actualBalanceWei = actualBalance.toBigInteger();
//    		BigDecimal amount = record.getMoney();
//    		BigInteger sendAmountWei = Convert.toWei(amount.toString(), Unit.ETHER).toBigInteger();
//			logger.info("orderId:"+record.getOrdid()+"\toAddress:"+address
//					+"\tactualBalance:"+actualBalanceWei+"\treceivedAmountWei:"+ sendAmountWei);
//    		if(actualBalanceWei.compareTo(sendAmountWei) == -1){
//				logger.warn("we change sendAmountWei from "+sendAmountWei+" to "+actualBalanceWei);
//    			sendAmountWei = actualBalanceWei;
//    		}
    		BigDecimal actualWei = new BigDecimal(actualBalanceWei.subtract(gasCost));
//    		BigDecimal actualSendEther = Convert.toWei(actualWei, Unit.ETHER);
//			logger.info("orderId:"+record.getOrdid()+"\t originalSendEther:"+ 
//			        "\t actualSendEther:"+actualSendEther);
			txHashVal = ethWalletService.sendTransactionFromUser(address,
					ethWalletHotAddress, actualWei.toString());
			if(StringUtils.isNotEmpty(txHashVal)){
				logger.info("Transaction ether originalAmount "+",actualEther:"
				+actualWei
						+" from sourceAddress:"+address+" to HOTWALLET;The hash is "
				+txHashVal);
				EthTransaction ethTransaction = web3j.ethGetTransactionByHash(txHashVal)
						.sendAsync().get();
				if(ethTransaction == null || ethTransaction.hasError()){
					logger.error("ethGetTransactionByHash error, txHash:{}, error:{}", 
							txHashVal, ethTransaction.getError().getMessage());
					return false;
				}
				Transaction transaction = ethTransaction.getTransaction().get();
				record.setStatus(WalletConstant.CONCENTRATE);
				record.setIsConcentrate(Constant.IS_CONCENTRATE);
				record.setConcentrateType(Constant.CONCENTRATE_TYPE_ETH);
				record.setConcentrateMsg(transaction.getHash());
				rechargeRecordService.updateRechargeRecord(record);
				IcoGatherRecord gatherRecord = new IcoGatherRecord();
				gatherRecord.setUserId(record.getUserId());
				gatherRecord.setAmount(record.getMoney());
				gatherRecord.setCost(new BigDecimal(gasCost));
				gatherRecord.setGatherStatus(Constant.GATHER_STATUS_ING);
				gatherRecord.setCurrency(Constant.CURRENCY_ETH);
				gatherRecord.setHash(transaction.getHash());
				gatherRecord.setNonce(transaction.getNonce().toString());
				gatherRecord.setBlockHash(transaction.getBlockHash());
				gatherRecord.setBlockNumber(transaction.getBlockNumber().toString());
				gatherRecord.setTransactionIndex(transaction.getTransactionIndex().toString());
				gatherRecord.setFromAddress(address);
				gatherRecord.setToAddress(ethWalletHotAddress);
				gatherRecord.setValue(transaction.getValue().toString());
				gatherRecord.setGasPrice(transaction.getGasPrice().toString());
				gatherRecord.setGas(transaction.getGas().toString());
				gatherRecord.setInput(transaction.getInput());
				gatherRecord.setCreates(transaction.getCreates());
				gatherRecord.setPublicKey(transaction.getPublicKey());
				gatherRecord.setOrderNo(record.getOrderNo());
				gatherRecord.setCreateTime(new Date());
				gatherRecord.setUpdateTime(new Date());
				gatherRecordService.insert(gatherRecord);
				logger.info("The order:"+record.getOrdid()
						+" sendtx success,wait for receipt block ! the txHashVal:"+txHashVal);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("orderId:{} Error processing transaction request, error:{}",
					record.getOrdid(),e.getMessage());
		}
		return false;
	}
}
