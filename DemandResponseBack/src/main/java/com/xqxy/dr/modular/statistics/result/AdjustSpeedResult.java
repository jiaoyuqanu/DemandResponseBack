package com.xqxy.dr.modular.statistics.result;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AdjustSpeedResult {

 /**
  * id
  * */
  @ApiModelProperty(value = "id")
  private String id;

 /**
  * 用户名称
  * */
  @ApiModelProperty(value = "用户名称")
  private String name;
    /**
     * 削峰一分钟以下
     * */
   @ApiModelProperty(value = "削峰一分钟以下")
   private BigDecimal consDesPower1;

    /**
     * 削峰一分钟至五分钟
     */
    @ApiModelProperty(value = "削峰一分钟至五分钟")
    private BigDecimal consDesPower2;

    /**
     * 削峰五分钟至十分钟
     */
    @ApiModelProperty(value = "削峰五分钟至十分钟")
    private BigDecimal consDesPower3;

    /**
     * 削峰十分钟以上
     */
    @ApiModelProperty(value = "削峰十分钟以上")
    private BigDecimal consDesPower4;

    /**
     * 填谷一分钟以下
     */
    @ApiModelProperty(value = "填谷一分钟以下")
    private BigDecimal consRisPower1;

    /**
     * 填谷一分钟至五分钟
     */
    @ApiModelProperty(value = "填谷一分钟至五分钟")
    private BigDecimal consRisPower2;

    /**
     * 填谷五分钟至十分钟
     */
    @ApiModelProperty(value = "填谷五分钟至十分钟")
    private BigDecimal consRisPower3;

    /**
     * 填谷十分钟以上
     */
    @ApiModelProperty(value = "填谷十分钟以上")
    private BigDecimal consRisPower4;
}
