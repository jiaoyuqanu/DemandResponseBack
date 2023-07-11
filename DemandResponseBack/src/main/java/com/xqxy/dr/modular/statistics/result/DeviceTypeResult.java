package com.xqxy.dr.modular.statistics.result;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DeviceTypeResult {
    /**
     * 设备类型名称
     * */
   @ApiModelProperty(value = "设备类型名称")
   private String name;

    /**
     * 设备类型数量
     */
    @ApiModelProperty(value = "设备类型数量")
    private Integer total;

}
