package com.xqxy.dr.modular.event.result;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class EventUserConsPlanListResult {

    @ApiModelProperty("用户名称")
    private String consName;
    @ApiModelProperty("用户编号")
    private String consCode;
    @ApiModelProperty("签约响应容量")
    private BigDecimal contractCap;
    @ApiModelProperty("应邀负荷")
    private BigDecimal replyCap;
    @ApiModelProperty("基线平均负荷")
    private BigDecimal avgLoadBaseline;
    @ApiModelProperty("基线最大负荷")
    private BigDecimal maxLoadBaseline;
}
