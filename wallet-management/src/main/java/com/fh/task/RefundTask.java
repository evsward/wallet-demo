package com.fh.task;

import com.fh.service.crowdfunding.crowdfunding.CrowdFundingManager;
import com.fh.util.PageData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class RefundTask {

    Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private CrowdFundingManager crowdfundingService;

    @Scheduled(cron = "0 0/30 * * * ? ")// 每半小时执行
    public void InvestRefund() {
        // 事务处理没有
        log.info("众筹结束失败投资退回处理定时器启动-------------------" + new Date());
        List<PageData> records = null;
        try {
            records = crowdfundingService.findInvestRefund();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("InvestRefund findInvestRefund failed:{}", e);
        }

        for (PageData record : records) {
            log.info(" InvestRefundRecord: {} ", record);
            PageData pd = new PageData();
            pd.put("amnt", record.get("invest_amount"));
            pd.put("supportAmountId", record.get("support_amount_id"));
            pd.put("userId", record.get("user_id"));
            pd.put("payType", record.get("pay_type"));
            pd.put("modifyTime", new Date());
            try {
                crowdfundingService.updateInvestRefund(pd);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("InvestRefund updateInvestRefund failed:{}", record);
            }
        }

        log.info("众筹结束失败投资退回处理定时器结束----------------end---");
    }

}
