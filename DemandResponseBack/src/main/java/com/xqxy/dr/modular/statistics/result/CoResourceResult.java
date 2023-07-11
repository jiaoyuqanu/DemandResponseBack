package com.xqxy.dr.modular.statistics.result;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CoResourceResult {
    /**
     * 单位名称
     * */
   @ApiModelProperty(value = "单位名称")
   private String NAME;

    /**
     * 目标容量
     */
    @ApiModelProperty(value = "目标容量")
    private Integer goal;

    /**
     * 削峰容量
     */
    @ApiModelProperty(value = "削峰容量")
    private BigDecimal des;

    /**
     * 填谷容量
     */
    @ApiModelProperty(value = "填谷容量")
    private BigDecimal ris;

}
