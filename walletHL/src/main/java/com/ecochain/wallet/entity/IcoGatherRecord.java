package com.ecochain.wallet.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_EMPTY)
public class IcoGatherRecord implements Serializable {
	
    private String id;

    private String userId;

    private BigDecimal amount;

    private BigDecimal cost;

    private String gatherStatus;

    private String currency;
    
    private String hash;
    
    private String nonce;
    
    private String blockHash;
    
    private String blockNumber;
    
    private String transactionIndex;
    
    private String fromAddress;
    
    private String toAddress;
    
    private String value;
    
    private String gasPrice;
    
    private String gas;
    
    private String input;
    
    private String creates;
    
    private String publicKey;

    private String orderNo;

    private Date createTime;

    private Date updateTime;
    
    private String remark;

    private static final long serialVersionUID = 2L;
}