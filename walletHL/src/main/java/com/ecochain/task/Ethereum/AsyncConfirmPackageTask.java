package com.ecochain.task.Ethereum;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import com.ecochain.client.Web3JClient;
import com.ecochain.constant.Constant;
import com.ecochain.wallet.entity.IcoGatherRecord;
import com.ecochain.wallet.entity.IcoRechargeRecord;
import com.ecochain.wallet.service.EcoWalletService;
import com.ecochain.wallet.service.EthWalletService;
import com.ecochain.wallet.service.IcoGatherRecordService;
import com.ecochain.wallet.service.IcoRechargeRecordService;
import com.ecochain.wallet.util.WalletConstant;

/**
 * 
 * 
* @ClassName: AsyncTaskConsumer 
* @Description: TODO(归集交易的确认打包) 
* @author dxm 
* @date 2017年8月22日 下午5:33:35 
*
 */
@Component
public class AsyncConfirmPackageTask{
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private static Web3j web3j = Web3JClient.getClient();
	
	@Autowired
	private IcoRechargeRecordService rechargeRecordService;
	
	@Autowired
	private EthWalletService ethWalletService;
	
	@Autowired
    private EcoWalletService ecoWalletService;
	
	@Autowired
	private IcoGatherRecordService gatherRecordService;

	long maxWaitTime = 1 * 60 * 60 * 1000;
	
	/**
	 * 
	* @Title: replayGatherTxHash 
	* @Description: TODO(重新归集)
	* @return void    返回类型 
	* @throws 
	* @author Anne
	 */
	@Scheduled(cron = "0 1/5 * * * ?")
	public void replayGatherTxHash(){
		logger.info("begin scan last time gather txhash");
		/**
		 * 如果归集成功，更新归集记录表数据归集状态为成功；
		 * 如果归集失败，更新归集记录表和充值记录表同时为失败；
		 */
		List<IcoGatherRecord> gatherRecordList = gatherRecordService.listGatherRecordByGather();
		for(IcoGatherRecord gatherRecord : gatherRecordList){
			String orderId = gatherRecord.getOrderNo();
			try{
				if(StringUtils.isEmpty(orderId)){
					gatherRecord.setGatherStatus(Constant.GATHER_STATUS_FAIL);
					gatherRecordService.updateGetherRecord(gatherRecord);
					continue;
				}
				String txHash = gatherRecord.getHash();
				IcoRechargeRecord record = rechargeRecordService.getIcoRechargeRecordByOrderId(orderId);
				long createTime = gatherRecord.getCreateTime().getTime();
				long deltaTime = System.currentTimeMillis() - createTime;
				if (deltaTime >= maxWaitTime) {
					if(record!=null){
						//充值记录表里面是否归集改为未归集，以便再次进行归集处理。
						record.setIsConcentrate(Constant.IS_UNCONCENTRATE);
						record.setStatus(WalletConstant.FINISH);
						rechargeRecordService.updateRechargeRecord(record);
					}
					gatherRecord.setGatherStatus(Constant.GATHER_STATUS_FAIL);
					gatherRecordService.updateGetherRecord(gatherRecord);
					logger.warn("orderId:{} txHash:{} createTime:{}, it is timeout, retry later", 
							record.getOrderNo(), txHash, createTime);
					continue;
				}
				logger.info("check gatherTask orderId:{} txhash:{}",orderId,txHash);
				if(txHash!=null){
					EthGetTransactionReceipt ethGetTransactionReceipt = web3j
							.ethGetTransactionReceipt(txHash).sendAsync().get();
					if(ethGetTransactionReceipt == null || ethGetTransactionReceipt.hasError()){
						logger.error("ethGetTransactionReceipt is null, orderId:{} txHash:{}", 
								record.getOrderNo(), txHash);
						if(record!=null){
							record.setIsConcentrate(Constant.IS_UNCONCENTRATE);
							record.setStatus(WalletConstant.FINISH);
							rechargeRecordService.updateRechargeRecord(record);
						}
						gatherRecord.setGatherStatus(Constant.GATHER_STATUS_FAIL);
						gatherRecordService.updateGetherRecord(gatherRecord);
						continue;
					}
					TransactionReceipt tx = null;
					try {
						tx = web3j.ethGetTransactionReceipt(txHash).send().getResult();
					} catch (IOException e) {
						e.printStackTrace();
						logger.error(e.getMessage());
					}
					if(tx==null){
						if(record!=null){
							record.setIsConcentrate(Constant.IS_UNCONCENTRATE);
							record.setStatus(WalletConstant.FINISH);
							rechargeRecordService.updateRechargeRecord(record);
						}
						gatherRecord.setGatherStatus(Constant.GATHER_STATUS_FAIL);
						gatherRecordService.updateGetherRecord(gatherRecord);
						continue;
					}
					if(tx.getTransactionHash().equalsIgnoreCase(txHash) && tx.getBlockNumber()!=null && tx.getBlockNumber().compareTo(BigInteger.ZERO)==1){
						BigInteger blockNum = tx.getBlockNumber();
						gatherRecord.setGatherStatus(Constant.GATHER_STATUS_SUCCESS);
						record.setStatus(WalletConstant.CONCENTRATE);
						gatherRecordService.updateGetherRecord(gatherRecord);
						logger.info("orderId:{} txHash:{} address:{} tblockNum:{} gather success!",
								gatherRecord.getOrderNo(),txHash, gatherRecord.getFromAddress(),
								blockNum);
					}else{
						logger.info("orderId:{} txHash:{} ignore it",gatherRecord,txHash);
					}
				}
			}catch(Exception e){
				e.printStackTrace();
				logger.error(e.getMessage());
			}
			
		}
		logger.info("end scan last time gather txhash");
	}
}