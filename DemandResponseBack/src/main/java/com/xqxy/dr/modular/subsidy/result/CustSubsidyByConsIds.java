package com.xqxy.dr.modular.subsidy.result;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class CustSubsidyByConsIds implements Serializable {

    @ApiModelProperty("事件id")
    private String eventId;
    @ApiModelProperty("用户id")
    private String consId;
    @ApiModelProperty("用户名")
    private String consName;
    @ApiModelProperty("事件名")
    private String eventName;
    @ApiModelProperty("实际相应负荷")
    private BigDecimal actualCap;
    @ApiModelProperty("是否有效")
    private String isEffective;
    @ApiModelProperty("相应时长")
    private BigDecimal effectiveTime;
    @ApiModelProperty("负荷响应率")
    private BigDecimal loadResponseRate;
    @ApiModelProperty("分成比率")
    private BigDecimal extractRadio;
    @ApiModelProperty("响应类型1削峰，2填谷")
    private Integer responceType;
    @ApiModelProperty("时间类型 1邀约，2实时 ")
    private Integer timeType;
    @ApiModelProperty("签约容量")
    private BigDecimal contractCap;
    @ApiModelProperty("邀约负荷")
    private BigDecimal replyCap;
    @ApiModelProperty("有效相应负荷")
    private BigDecimal confirmCap;
    @ApiModelProperty("签约价格")
    private BigDecimal contractPrice;
    @ApiModelProperty("补偿金额")
    private BigDecimal subsidyAmount;

    /**
     * 调控时间系数
     */
    @ApiModelProperty("调控时间系数")
    private BigDecimal timeCoefficient;
}
