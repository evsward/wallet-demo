package com.ecochain.wallet.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.ecochain.user.entity.BaseEntity;

public class EcoWalletRecord extends BaseEntity {
    private Integer id;

    private String userName;

    private String parities;

    private BigDecimal rmb;

    private BigDecimal btc;

    private Date createTime;

    private String type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getParities() {
        return parities;
    }

    public void setParities(String parities) {
        this.parities = parities == null ? null : parities.trim();
    }

    public BigDecimal getRmb() {
        return rmb;
    }

    public void setRmb(BigDecimal rmb) {
        this.rmb = rmb;
    }

    public BigDecimal getBtc() {
        return btc;
    }

    public void setBtc(BigDecimal btc) {
        this.btc = btc;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }
}