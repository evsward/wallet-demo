package com.ecochain.task.Ethereum;


import com.ecochain.wallet.entity.WithdrawRecord;
import com.ecochain.wallet.service.EthWalletService;
import com.ecochain.wallet.service.WithdrawRecordService;
import lombok.Data;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 
* @ClassName: MonitorWithDrawThread 
* @Description: TODO(以太坊提币监控线程类) 
* @author 焦博韬  
* @date 2017年10月12日 上午10:47:46 
*
 */
@Data
@Component("monitorWithDrawThread")
public class MonitorWithDrawThread implements Runnable {
    private Logger logger = org.apache.log4j.Logger.getLogger(MonitorWithDrawThread.class);

    @Autowired
    private EthWalletService ethWalletService;

    @Autowired
    private WithdrawRecordService withdrawRecordService;

    @Scheduled(cron = "0 1/3 * * * ? ")
    @Override
    public void run() {

        logger.info("以太坊提币线程-----开始------------------" + new Date());
        List<WithdrawRecord> records = withdrawRecordService.listETHApprove();

        for (WithdrawRecord withdrawRecord : records) {
            try {
                ethWalletService.sendMoney(withdrawRecord);
            } catch (Exception e) {
                e.printStackTrace();
            }
            logger.info(" WithdrawRecord: " + withdrawRecord + " ");
        }
        logger.info("以太坊提币线程-----结束---------------------");

    }
}
