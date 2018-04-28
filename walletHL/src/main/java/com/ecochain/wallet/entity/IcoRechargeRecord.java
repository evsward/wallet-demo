package com.ecochain.wallet.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class IcoRechargeRecord implements Serializable {
    private String id;

    private String userId;

    private String code;

    private BigDecimal money;

    private BigDecimal remainderMoney;

    private String status;

    private String currency;

    private String ordid;

    private String orddate;

    private String orderNo;

    private Date createTime;

    private Date updateTime;

    private String byUserId;

    private BigDecimal free;

    private String txHash;

    private String address;

    private String isConcentrate;

    private String concentrateType;

    private String concentrateMsg;

    private static final long serialVersionUID = 1L;

    public IcoRechargeRecord(String id, String userId, String code, BigDecimal money, BigDecimal remainderMoney, String status, String currency, String ordid, String orddate, String orderNo, Date createTime, Date updateTime, String byUserId, BigDecimal free, String txHash, String address, String isConcentrate, String concentrateType, String concentrateMsg) {
        this.id = id;
        this.userId = userId;
        this.code = code;
        this.money = money;
        this.remainderMoney = remainderMoney;
        this.status = status;
        this.currency = currency;
        this.ordid = ordid;
        this.orddate = orddate;
        this.orderNo = orderNo;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.byUserId = byUserId;
        this.free = free;
        this.txHash = txHash;
        this.address = address;
        this.isConcentrate = isConcentrate;
        this.concentrateType = concentrateType;
        this.concentrateMsg = concentrateMsg;
    }

    public IcoRechargeRecord() {
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

    public String getOrdid() {
        return ordid;
    }

    public void setOrdid(String ordid) {
        this.ordid = ordid == null ? null : ordid.trim();
    }

    public String getOrddate() {
        return orddate;
    }

    public void setOrddate(String orddate) {
        this.orddate = orddate == null ? null : orddate.trim();
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getIsConcentrate() {
        return isConcentrate;
    }

    public void setIsConcentrate(String isConcentrate) {
        this.isConcentrate = isConcentrate == null ? null : isConcentrate.trim();
    }

    public String getConcentrateType() {
        return concentrateType;
    }

    public void setConcentrateType(String concentrateType) {
        this.concentrateType = concentrateType == null ? null : concentrateType.trim();
    }

    public String getConcentrateMsg() {
        return concentrateMsg;
    }

    public void setConcentrateMsg(String concentrateMsg) {
        this.concentrateMsg = concentrateMsg == null ? null : concentrateMsg.trim();
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
        IcoRechargeRecord other = (IcoRechargeRecord) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
                && (this.getCode() == null ? other.getCode() == null : this.getCode().equals(other.getCode()))
                && (this.getMoney() == null ? other.getMoney() == null : this.getMoney().equals(other.getMoney()))
                && (this.getRemainderMoney() == null ? other.getRemainderMoney() == null : this.getRemainderMoney().equals(other.getRemainderMoney()))
                && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
                && (this.getCurrency() == null ? other.getCurrency() == null : this.getCurrency().equals(other.getCurrency()))
                && (this.getOrdid() == null ? other.getOrdid() == null : this.getOrdid().equals(other.getOrdid()))
                && (this.getOrddate() == null ? other.getOrddate() == null : this.getOrddate().equals(other.getOrddate()))
                && (this.getOrderNo() == null ? other.getOrderNo() == null : this.getOrderNo().equals(other.getOrderNo()))
                && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
                && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
                && (this.getByUserId() == null ? other.getByUserId() == null : this.getByUserId().equals(other.getByUserId()))
                && (this.getFree() == null ? other.getFree() == null : this.getFree().equals(other.getFree()))
                && (this.getTxHash() == null ? other.getTxHash() == null : this.getTxHash().equals(other.getTxHash()))
                && (this.getAddress() == null ? other.getAddress() == null : this.getAddress().equals(other.getAddress()))
                && (this.getIsConcentrate() == null ? other.getIsConcentrate() == null : this.getIsConcentrate().equals(other.getIsConcentrate()))
                && (this.getConcentrateType() == null ? other.getConcentrateType() == null : this.getConcentrateType().equals(other.getConcentrateType()))
                && (this.getConcentrateMsg() == null ? other.getConcentrateMsg() == null : this.getConcentrateMsg().equals(other.getConcentrateMsg()));
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
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getCurrency() == null) ? 0 : getCurrency().hashCode());
        result = prime * result + ((getOrdid() == null) ? 0 : getOrdid().hashCode());
        result = prime * result + ((getOrddate() == null) ? 0 : getOrddate().hashCode());
        result = prime * result + ((getOrderNo() == null) ? 0 : getOrderNo().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getByUserId() == null) ? 0 : getByUserId().hashCode());
        result = prime * result + ((getFree() == null) ? 0 : getFree().hashCode());
        result = prime * result + ((getTxHash() == null) ? 0 : getTxHash().hashCode());
        result = prime * result + ((getAddress() == null) ? 0 : getAddress().hashCode());
        result = prime * result + ((getIsConcentrate() == null) ? 0 : getIsConcentrate().hashCode());
        result = prime * result + ((getConcentrateType() == null) ? 0 : getConcentrateType().hashCode());
        result = prime * result + ((getConcentrateMsg() == null) ? 0 : getConcentrateMsg().hashCode());
        return result;
    }
}