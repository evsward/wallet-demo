package com.ecochain.wallet.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class IcoTransferRecord implements Serializable {
    private String id;

    private String userId;

    private String account;

    private BigDecimal money;

    private BigDecimal remainderMoney;

    private BigDecimal cost;

    private String status;

    private String currency;

    private String relaUserId;

    private String relaAccount;

    private String relaAddress;

    private String orderNo;

    private Date createTime;

    private Date updateTime;

    private String byUserId;

    private String code;

    private String remark;

    private static final long serialVersionUID = 1L;

    public IcoTransferRecord(String id, String userId, String account, BigDecimal money, BigDecimal remainderMoney, BigDecimal cost, String status, String currency, String relaUserId, String relaAccount, String relaAddress, String orderNo, Date createTime, Date updateTime, String byUserId, String code, String remark) {
        this.id = id;
        this.userId = userId;
        this.account = account;
        this.money = money;
        this.remainderMoney = remainderMoney;
        this.cost = cost;
        this.status = status;
        this.currency = currency;
        this.relaUserId = relaUserId;
        this.relaAccount = relaAccount;
        this.relaAddress = relaAddress;
        this.orderNo = orderNo;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.byUserId = byUserId;
        this.code = code;
        this.remark = remark;
    }

    public IcoTransferRecord() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account == null ? null : account.trim();
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public BigDecimal getRemainderMoney() {
        return remainderMoney;
    }

    public void setRemainderMoney(BigDecimal remainderMoney) {
        this.remainderMoney = remainderMoney;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency == null ? null : currency.trim();
    }

    public String getRelaUserId() {
        return relaUserId;
    }

    public void setRelaUserId(String relaUserId) {
        this.relaUserId = relaUserId == null ? null : relaUserId.trim();
    }

    public String getRelaAccount() {
        return relaAccount;
    }

    public void setRelaAccount(String relaAccount) {
        this.relaAccount = relaAccount == null ? null : relaAccount.trim();
    }

    public String getRelaAddress() {
        return relaAddress;
    }

    public void setRelaAddress(String relaAddress) {
        this.relaAddress = relaAddress == null ? null : relaAddress.trim();
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo == null ? null : orderNo.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getByUserId() {
        return byUserId;
    }

    public void setByUserId(String byUserId) {
        this.byUserId = byUserId == null ? null : byUserId.trim();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        IcoTransferRecord other = (IcoTransferRecord) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getAccount() == null ? other.getAccount() == null : this.getAccount().equals(other.getAccount()))
            && (this.getMoney() == null ? other.getMoney() == null : this.getMoney().equals(other.getMoney()))
            && (this.getRemainderMoney() == null ? other.getRemainderMoney() == null : this.getRemainderMoney().equals(other.getRemainderMoney()))
            && (this.getCost() == null ? other.getCost() == null : this.getCost().equals(other.getCost()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getCurrency() == null ? other.getCurrency() == null : this.getCurrency().equals(other.getCurrency()))
            && (this.getRelaUserId() == null ? other.getRelaUserId() == null : this.getRelaUserId().equals(other.getRelaUserId()))
            && (this.getRelaAccount() == null ? other.getRelaAccount() == null : this.getRelaAccount().equals(other.getRelaAccount()))
            && (this.getRelaAddress() == null ? other.getRelaAddress() == null : this.getRelaAddress().equals(other.getRelaAddress()))
            && (this.getOrderNo() == null ? other.getOrderNo() == null : this.getOrderNo().equals(other.getOrderNo()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getByUserId() == null ? other.getByUserId() == null : this.getByUserId().equals(other.getByUserId()))
            && (this.getCode() == null ? other.getCode() == null : this.getCode().equals(other.getCode()))
            && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getAccount() == null) ? 0 : getAccount().hashCode());
        result = prime * result + ((getMoney() == null) ? 0 : getMoney().hashCode());
        result = prime * result + ((getRemainderMoney() == null) ? 0 : getRemainderMoney().hashCode());
        result = prime * result + ((getCost() == null) ? 0 : getCost().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getCurrency() == null) ? 0 : getCurrency().hashCode());
        result = prime * result + ((getRelaUserId() == null) ? 0 : getRelaUserId().hashCode());
        result = prime * result + ((getRelaAccount() == null) ? 0 : getRelaAccount().hashCode());
        result = prime * result + ((getRelaAddress() == null) ? 0 : getRelaAddress().hashCode());
        result = prime * result + ((getOrderNo() == null) ? 0 : getOrderNo().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getByUserId() == null) ? 0 : getByUserId().hashCode());
        result = prime * result + ((getCode() == null) ? 0 : getCode().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        return result;
    }
}