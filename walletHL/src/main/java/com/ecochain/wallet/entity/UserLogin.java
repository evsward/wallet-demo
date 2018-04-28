package com.ecochain.wallet.entity;

import java.util.Date;

import lombok.Data;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@ToString // 所有属性toString
@Data //设置所有属性的 get set 方法
//@NoArgsConstructor //无参构造方法
//@AllArgsConstructor // 全部参数构造方法
@JsonInclude(Include.NON_EMPTY)
public class UserLogin {
    private Integer id;

    private Integer userId;

    private String account;

    private String userName;

    private String password;

    private Date lastloginTime;

    private String lastloginIp;

    private long lastloginPort;

    private String status;

    private Integer loginErrorTimes;

    private Date lastloginErrorTime;
    
    private String  cfPassWord;
    
    private String  vcode;

    private String phone;
    
    private String oldPassword;
    
    private String newPassword;
    
    private String source;

    private boolean vipFlag;

    public boolean isVipFlag() {
        return vipFlag;
    }

    public void setVipFlag(boolean vipFlag) {
        this.vipFlag = vipFlag;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account == null ? null : account.trim();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="Asia/Shanghai")
    public Date getLastloginTime() {
        return lastloginTime;
    }

    public void setLastloginTime(Date lastloginTime) {
        this.lastloginTime = lastloginTime;
    }

    public String getLastloginIp() {
        return lastloginIp;
    }

    public void setLastloginIp(String lastloginIp) {
        this.lastloginIp = lastloginIp == null ? null : lastloginIp.trim();
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Integer getLoginErrorTimes() {
        return loginErrorTimes;
    }

    public void setLoginErrorTimes(Integer loginErrorTimes) {
        this.loginErrorTimes = loginErrorTimes;
    }

    public Date getLastloginErrorTime() {
        return lastloginErrorTime;
    }

    public void setLastloginErrorTime(Date lastloginErrorTime) {
        this.lastloginErrorTime = lastloginErrorTime;
    }
}