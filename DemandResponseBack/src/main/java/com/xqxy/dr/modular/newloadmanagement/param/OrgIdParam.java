package com.xqxy.dr.modular.newloadmanagement.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description
 * @Author Rabbit
 * @Date 2022/6/22 15:06
 */
@Data
@ApiModel(description = "需求响当前时段执行事件及实时执行情况参数")
public class OrgIdParam {
    @ApiModelProperty(value = "区域编号 不传则返回省级加16个地市数据")
    private String orgId;
}
