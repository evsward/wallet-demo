package com.ecochain.wallet.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import com.ecochain.user.entity.BaseEntity;
@Data //设置所有属性的 get set 方法
public class Page extends BaseEntity{

    @ApiModelProperty(value = "当前页")
    private Integer page;
    
    @ApiModelProperty(value = "每页多少行")
    private Integer rows;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }
}
