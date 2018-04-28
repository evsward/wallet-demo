package com.ecochain.wallet.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class IcoWithdrawRecord implements Serializable {
    private String id;

    private String userId;

    private String code;

    private BigDecimal money;

    private BigDecimal remainderMoney;

    private BigDecimal cost;

    private String status;

    private String currency;

    private String bankCardNo;

    private String bankCardName;

    private String address;

    private String outerAddress;

    private String orderNo;

    private String orderId;

    private Date createTime;

    private Date updateTime;

    private String byUserId;

    private BigDecimal free;

    private String txHash;

    private String txHashBak;

    private String withdrawMsg;

    private static final long serialVersionUID = 1L;

    public IcoWithdrawRecord(String id, String userId, String code, BigDecimal money, BigDecimal remainderMoney, BigDecimal cost, String status, String currency, String bankCardNo, String bankCardName, String address, String orderNo, String orderId, Date createTime, Date updateTime, String byUserId, BigDecimal free, String txHash) {
        this.id = id;
        this.userId = userId;
        this.code = code;
        this.money = money;
        this.remainderMoney = remainderMoney;
        this.cost = cost;
        this.status = status;
        this.currency = currency;
        this.bankCardNo = bankCardNo;
        this.bankCardName = bankCardName;
        this.address = address;
        this.orderNo = orderNo;
        this.orderId = orderId;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.byUserId = byUserId;
        this.free = free;
        this.txHash = txHash;
    }

    public IcoWithdrawRecord(String id, String userId, String code, BigDecimal money, BigDecimal remainderMoney, BigDecimal cost, String status, String currency, String bankCardNo, String bankCardName, String address, String outerAddress, String orderNo, String orderId, Date createTime, Date updateTime, String byUserId, BigDecimal free, String txHash, String txHashBak, String withdrawMsg) {
        this.id = id;
        this.userId = userId;
        this.code = code;
        this.money = money;
        this.remainderMoney = remainderMoney;
        this.cost = cost;
        this.status = status;
        this.currency = currency;
        this.bankCardNo = bankCardNo;
        this.bankCardName = bankCardName;
        this.address = address;
        this.outerAddress = outerAddress;
        this.orderNo = orderNo;
        this.orderId = orderId;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.byUserId = byUserId;
        this.free = free;
        this.txHash = txHash;
        this.txHashBak = txHashBak;
        this.withdrawMsg = withdrawMsg;
    }

    public String getTxHashBak() {
        return txHashBak;
    }

    public void setTxHashBak(String txHashBak) {
        this.txHashBak = txHashBak;
    }

    public String getWithdrawMsg() {
        return withdrawMsg;
    }

    public void setWithdrawMsg(String withdrawMsg) {
        this.withdrawMsg = withdrawMsg;
    }

    public String getOuterAddress() {
        return outerAddress;
    }

    public void setOuterAddress(String outerAddress) {
        this.outerAddress = outerAddress;
    }

    public IcoWithdrawRecord() {
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
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

    public String getBankCardNo() {
        return bankCardNo;
    }

    public void setBankCardNo(String bankCardNo) {
        this.bankCardNo = bankCardNo == null ? null : bankCardNo.trim();
    }

    public String getBankCardName() {
        return bankCardName;
    }

    public void setBankCardName(String bankCardName) {
        this.bankCardName = bankCardName == null ? null : bankCardName.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo == null ? null : orderNo.trim();
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
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

    public BigDecimal getFree() {
        return free;
    }

    public void setFree(BigDecimal free) {
        this.free = free;
    }

    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash == null ? null : txHash.trim();
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
        IcoWithdrawRecord other = (IcoWithdrawRecord) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getCode() == null ? other.getCode() == null : this.getCode().equals(other.getCode()))
            && (this.getMoney() == null ? other.getMoney() == null : this.getMoney().equals(other.getMoney()))
            && (this.getRemainderMoney() == null ? other.getRemainderMoney() == null : this.getRemainderMoney().equals(other.getRemainderMoney()))
            && (this.getCost() == null ? other.getCost() == null : this.getCost().equals(other.getCost()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getCurrency() == null ? other.getCurrency() == null : this.getCurrency().equals(other.getCurrency()))
            && (this.getBankCardNo() == null ? other.getBankCardNo() == null : this.getBankCardNo().equals(other.getBankCardNo()))
            && (this.getBankCardName() == null ? other.getBankCardName() == null : this.getBankCardName().equals(other.getBankCardName()))
            && (this.getAddress() == null ? other.getAddress() == null : this.getAddress().equals(other.getAddress()))
            && (this.getOrderNo() == null ? other.getOrderNo() == null : this.getOrderNo().equals(other.getOrderNo()))
            && (this.getOrderId() == null ? other.getOrderId() == null : this.getOrderId().equals(other.getOrderId()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getByUserId() == null ? other.getByUserId() == null : this.getByUserId().equals(other.getByUserId()))
            && (this.getFree() == null ? other.getFree() == null : this.getFree().equals(other.getFree()))
            && (this.getTxHash() == null ? other.getTxHash() == null : this.getTxHash().equals(other.getTxHash()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getCode() == null) ? 0 : getCode().hashCode());
        result = prime * result + ((getMoney() == null) ? 0 : getMoney().hashCode());
        result = prime * result + ((getRemainderMoney() == null) ? 0 : getRemainderMoney().hashCode());
        result = prime * result + ((getCost() == null) ? 0 : getCost().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getCurrency() == null) ? 0 : getCurrency().hashCode());
        result = prime * result + ((getBankCardNo() == null) ? 0 : getBankCardNo().hashCode());
        result = prime * result + ((getBankCardName() == null) ? 0 : getBankCardName().hashCode());
        result = prime * result + ((getAddress() == null) ? 0 : getAddress().hashCode());
        result = prime * result + ((getOrderNo() == null) ? 0 : getOrderNo().hashCode());
        result = prime * result + ((getOrderId() == null) ? 0 : getOrderId().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getByUserId() == null) ? 0 : getByUserId().hashCode());
        result = prime * result + ((getFree() == null) ? 0 : getFree().hashCode());
        result = prime * result + ((getTxHash() == null) ? 0 : getTxHash().hashCode());
        return result;
    }
}