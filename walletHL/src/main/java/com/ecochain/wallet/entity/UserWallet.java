package com.ecochain.wallet.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

@ToString // 所有属性toString
@Data //设置所有属性的 get set 方法
//@NoArgsConstructor //无参构造方法
//@AllArgsConstructor // 全部参数构造方法
@JsonInclude(Include.NON_EMPTY)
public class UserWallet {
    private Integer id;

    private Integer userId;

    private BigDecimal btcAmnt;

    private BigDecimal ltcAmnt;

    private BigDecimal ethAmnt;

    private BigDecimal etcAmnt;

    private BigDecimal hlcAmnt;

    private BigDecimal sanAmnt;

    private BigDecimal money;

    private Date lastCaldate;

    private String syscode;

    private BigDecimal frozeRmbAmnt;

    private BigDecimal frozeHlcAmnt;

    private BigDecimal frozeBtcAmnt;

    private BigDecimal frozeLtcAmnt;

    private BigDecimal frozeEthAmnt;

    private BigDecimal frozeEtcAmnt;
    
    private BigDecimal frozeSanAmnt;

    private Date createTime;

    private Date modifyTime;

    private String operator;

    private String versionId;

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

    public BigDecimal getBtcAmnt() {
        return btcAmnt;
    }

    public void setBtcAmnt(BigDecimal btcAmnt) {
        this.btcAmnt = btcAmnt;
    }

    public BigDecimal getLtcAmnt() {
        return ltcAmnt;
    }

    public void setLtcAmnt(BigDecimal ltcAmnt) {
        this.ltcAmnt = ltcAmnt;
    }

    public BigDecimal getEthAmnt() {
        return ethAmnt;
    }

    public void setEthAmnt(BigDecimal ethAmnt) {
        this.ethAmnt = ethAmnt;
    }

    public BigDecimal getEtcAmnt() {
        return etcAmnt;
    }

    public void setEtcAmnt(BigDecimal etcAmnt) {
        this.etcAmnt = etcAmnt;
    }

    public BigDecimal getHlcAmnt() {
        return hlcAmnt;
    }

    public void setHlcAmnt(BigDecimal hlcAmnt) {
        this.hlcAmnt = hlcAmnt;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public Date getLastCaldate() {
        return lastCaldate;
    }

    public void setLastCaldate(Date lastCaldate) {
        this.lastCaldate = lastCaldate;
    }

    public String getSyscode() {
        return syscode;
    }

    public void setSyscode(String syscode) {
        this.syscode = syscode == null ? null : syscode.trim();
    }

    public BigDecimal getFrozeRmbAmnt() {
        return frozeRmbAmnt;
    }

    public void setFrozeRmbAmnt(BigDecimal frozeRmbAmnt) {
        this.frozeRmbAmnt = frozeRmbAmnt;
    }

    public BigDecimal getFrozeHlcAmnt() {
        return frozeHlcAmnt;
    }

    public void setFrozeHlcAmnt(BigDecimal frozeHlcAmnt) {
        this.frozeHlcAmnt = frozeHlcAmnt;
    }

    public BigDecimal getFrozeBtcAmnt() {
        return frozeBtcAmnt;
    }

    public void setFrozeBtcAmnt(BigDecimal frozeBtcAmnt) {
        this.frozeBtcAmnt = frozeBtcAmnt;
    }

    public BigDecimal getFrozeLtcAmnt() {
        return frozeLtcAmnt;
    }

    public void setFrozeLtcAmnt(BigDecimal frozeLtcAmnt) {
        this.frozeLtcAmnt = frozeLtcAmnt;
    }

    public BigDecimal getFrozeEthAmnt() {
        return frozeEthAmnt;
    }

    public void setFrozeEthAmnt(BigDecimal frozeEthAmnt) {
        this.frozeEthAmnt = frozeEthAmnt;
    }

    public BigDecimal getFrozeEtcAmnt() {
        return frozeEtcAmnt;
    }

    public void setFrozeEtcAmnt(BigDecimal frozeEtcAmnt) {
        this.frozeEtcAmnt = frozeEtcAmnt;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="Asia/Shanghai")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="Asia/Shanghai")
    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
    }

    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId == null ? null : versionId.trim();
    }
}