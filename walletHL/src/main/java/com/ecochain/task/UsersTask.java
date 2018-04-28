package com.ecochain.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ecochain.util.HttpUtil;
import com.ecochain.util.MD5Util;
import com.ecochain.wallet.mapper.UserLoginMapper;
import com.ecochain.wallet.service.SysGencodeService;
import com.github.pagehelper.StringUtil;

@Service
public class UsersTask {

    private Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private UserLoginMapper userLoginMapper;
    
    @Autowired
    private SysGencodeService sysGencodeService;
    
//    @Scheduled(cron = "0 0/1 * * * ? ")
    public void payResultQuery() {
        logger.info("ICO账户同步定时器启动-----------Start--------" + new Date());
        try {
            List<Map> userList100 = userLoginMapper.getUserList100();
            JSONArray array = new JSONArray();
            JSONObject requestObj = new JSONObject();
            requestObj.put("key", "qklbbs6989$!");
            requestObj.put("timestamp", System.currentTimeMillis());
            requestObj.put("token", MD5Util.getMd5Code(requestObj.getString("key")+requestObj.getString("timestamp")));
            for(Map userLogin:userList100){
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("username", userLogin.get("user_name"));
                jsonObj.put("password", userLogin.get("password"));
                if(StringUtil.isEmpty(String.valueOf(userLogin.get("email")))){
                    jsonObj.put("email", "admin@admin.com");
                }else{
                    jsonObj.put("email", userLogin.get("email"));
                }
                array.add(jsonObj);
            }
            requestObj.put("userinfo", array);
            if(array.size()>0){
                List<Map<String, String>> codeList = sysGencodeService.findByGroupCode("OTHER_URL");
                String ico_bbs_url ="";
                for(Map<String, String> mapObj:codeList){
                    if("ICO_BBS".equals(mapObj.get("code_name"))){
                        ico_bbs_url = mapObj.get("code_value").toString();
                        logger.info("---------ico_bbs同步用户地址--------ico_bbs_url："+ico_bbs_url);
                    }
                }
                String postJson = HttpUtil.postJson(ico_bbs_url, requestObj.toJSONString());
                JSONObject responseObj = JSONObject.parseObject(postJson);
                if(!responseObj.getBooleanValue("result")){//可能部分成功部分失败
                    
                }else{//全部同步成功
                  List<String> userNameList = new ArrayList<String>();  
                  for(Map userLogin:userList100){
                      userNameList.add(String.valueOf(userLogin.get("user_name")));
                  }
                  userLoginMapper.updateICOBBSByUserName("1",userNameList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            logger.info("ICO账户同步定时器启动------------end--------" + new Date());  
        }
        
    }
    
}
