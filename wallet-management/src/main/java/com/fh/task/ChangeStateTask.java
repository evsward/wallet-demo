package com.fh.task;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.fh.service.crowdfunding.crowdfunding.CrowdFundingManager;
import com.fh.util.PageData;
@Component
public class ChangeStateTask  {  
	
	Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private CrowdFundingManager crowdfundingService;
    
    @Scheduled(cron = "0 0/1 * * * ? ")// 每分钟执行
    public void ChangeState(){  
        try{  
        	 log.info("定时任务众筹审核状态启动-------------------" + new Date());
             //查询'0未提交、1待审核 、2审核中、3审核通过、4即将开始、5进行中、6已完成、7审核失败、8已结束--未投满'
             //查询  状态为 3的数据  当前时间-开始时间<=5天  修改 状态为  4  
             PageData pd = new PageData();
     		 pd.put("PROJECT_STATE", "4");
             List<PageData> varOList = crowdfundingService.listAll(pd);
             for(PageData pdojb:varOList){
            	 pdojb.put("PROJECT_STATE", "4");//设置状态为审核通过
         		 crowdfundingService.edit(pdojb);
             }
             //查询 状态为4   开始时间<当前时间<结束时间  修改状态 为 5
             pd.put("PROJECT_STATE", 5);
             varOList = crowdfundingService.listAll(pd);
             for(PageData pdojb:varOList){
            	 List<PageData> listAmounts  = crowdfundingService.listAmount(pdojb); //查询  pro_support_amount 表
            	 if(listAmounts!=null&&listAmounts.size()>0){
            		 for(PageData amountObj:listAmounts){
            			 String code_name = (String) amountObj.get("code_name"); //币种名称
            			 BigDecimal amount = (BigDecimal) amountObj.get("amount");//币种数量
            			 if(!StringUtils.isEmpty(code_name)&&amount!=null && amount.compareTo(new BigDecimal(0))==1){
            				 pdojb.put("INVEST_"+code_name+"_COUNT", amount) ;  //替换金额
            			 }
            		 }
            	 }
            	 pdojb.put("PROJECT_STATE", 5);//设置状态为审核通过
         		 crowdfundingService.edit(pdojb);
             }
             //查询 状态为 5  invest_count<target_amount 当前时间>结束时间      修改 状态为  8
             pd.put("PROJECT_STATE", 8);
             varOList = crowdfundingService.listAll(pd);
             for(PageData pdojb:varOList){
            	 pdojb.put("PROJECT_STATE", 8);//设置状态为审核通过
         		 crowdfundingService.edit(pdojb);
             }
             //查询 状态为 6  target_amount<invest_count 当前时间>结束时间      修改 状态为  6
             pd.put("PROJECT_STATE", 6);
             varOList = crowdfundingService.listAll(pd);
             for(PageData pdojb:varOList){
            	 pdojb.put("PROJECT_STATE", 6);//设置状态为审核通过
         		 crowdfundingService.edit(pdojb);
             }
             log.info("定时任务众筹审核状态 修改结束-------------------" + new Date());
         }catch(Exception ex){  
             ex.printStackTrace(); 
             log.info("定时任务众筹审核状态 修改结束-------------------" + new Date());
         }  
     }  
} 
