package com.ecochain.wallet.entity;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;
import lombok.ToString;

import com.ecochain.user.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@ToString // 所有属性toString
@Data //设置所有属性的 get set 方法
//@NoArgsConstructor //无参构造方法
//@AllArgsConstructor // 全部参数构造方法
@JsonInclude(Include.NON_EMPTY)
public class RechargeRecord extends BaseEntity{
    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "用户ID")
    private String userId;

    @ApiModelProperty(value = "编码")
    private String code;

    @ApiModelProperty(value = "充值金额")
    private BigDecimal money;

    @ApiModelProperty(value = "余额")
    private BigDecimal remainderMoney;

    @ApiModelProperty(value = "状态：0-待审核 1-成功 2-失败")
    private String status;

    @ApiModelProperty(value = "币种类型：如BTC,LTC,ETH,ETC,HLC")
    private String currency;

    @ApiModelProperty(value = "订单ID")
    private String ordid;

    @ApiModelProperty(value = "订单日期")
    private String orddate;

    @ApiModelProperty(value = "订单号")
    private String orderNo;
    
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "操作人ID")
    private String byUserId;

    private BigDecimal free;

    private String txHash;
    
    private String address;

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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="Asia/Shanghai")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="Asia/Shanghai")
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
}