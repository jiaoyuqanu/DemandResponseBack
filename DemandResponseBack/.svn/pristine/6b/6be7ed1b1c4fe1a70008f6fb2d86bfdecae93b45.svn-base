package com.xqxy.dr.modular.newloadmanagement.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DemandEvaluationVo {

    /**
     * 需求响应执行时段（单日，若是累计时可不传）
     */
    @ApiModelProperty("需求响应执行时段")
    private String demandTime;
    /**
     * 需求响应执行天数（累计，若是单日则不传）
     */
    @ApiModelProperty("需求响应执行天数")
    private Long demandDay;
    /**
     * 需求响应缺口负荷（单日，若是累计时可不传）
     */
    @ApiModelProperty("需求响应缺口负荷")
    private BigDecimal demandGapLoad;
    /**
     * 需求响应累计执行时长（累计，若是单日则不传）
     */
    @ApiModelProperty("需求响应累计执行时长")
    private String demandCumTime;
    /**
     * 需求响应累计影响户次（多时段则累计，发生在单日）
     */
    @ApiModelProperty("需求响应累计影响户次")
    private Integer demandCumHouse;
    /**
     * 需求响应最大错避峰（多时段则累计）
     */
    @ApiModelProperty("需求响应最大错避峰")
    private BigDecimal demandCumRegulation;
    /**
     * 需求响应累计影响电量（多时段则累计）
     */
    @ApiModelProperty("需求响应累计影响电量")
    private String demandCumInfluenceElc;
    /**
     * 最大错避峰日期（YYYY-MM-DD）（累计，若是单日则不传）
     */
    @ApiModelProperty("最大错避峰日期")
    private String demandCumDate;

}
