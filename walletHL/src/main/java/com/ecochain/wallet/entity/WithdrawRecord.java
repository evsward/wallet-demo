package com.ecochain.wallet.entity;


import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

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
public class WithdrawRecord extends BaseEntity{
    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "用户ID")
    private String userId;

    @ApiModelProperty(value = "编码")
    private String code;

    @NotNull(message="请输入提现金额或者提现数量")
    @ApiModelProperty(value = "提现金额或提现数量")
    private BigDecimal money;
    
    @ApiModelProperty(value = "余额")
    private BigDecimal remainderMoney;
    
    @ApiModelProperty(value = "手续费")
    private BigDecimal cost;

    @ApiModelProperty(value = "状态：0-待审核 1-成功 2-失败")
    private String status;

    @NotBlank(message="请选择币种")
    @ApiModelProperty(value = "币种类型：如BTC,LTC,ETH,ETC,HLC")
    private String currency;

    @ApiModelProperty(value = "提现地址")
    private String address;
    
    @ApiModelProperty(value = "订单ID")
    private String orderId;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更细时间")
    private Date updateTime;

    @ApiModelProperty(value = "操作人ID")
    private String byUserId;
    
    @ApiModelProperty(value = "交易密码")
    private String transPassword;
    
    @ApiModelProperty(value = "验证码")
    private String vcode;
    
    @ApiModelProperty(value = "网络手续费")
    private BigDecimal free;

    @ApiModelProperty(value = "交易hash")
    private String txHash;
    
    @ApiModelProperty(value = "银行卡号")
    private String bankCardNo;

    @ApiModelProperty(value = "银行卡名称")
    private String bankCardName;

    @ApiModelProperty(value = "订单号")
    private String orderNo;
    
    @ApiModelProperty(value = "用户名")
    private String userName;
    
    @ApiModelProperty(value = "密码")
    private String password;
    
    public String getTransPassword() {
        return transPassword;
    }

    public void setTransPassword(String transPassword) {
        this.transPassword = transPassword;
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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
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