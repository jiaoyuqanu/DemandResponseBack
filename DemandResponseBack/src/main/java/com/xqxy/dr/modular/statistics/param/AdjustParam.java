package com.xqxy.dr.modular.statistics.param;

import com.xqxy.core.pojo.base.param.BaseParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@ApiModel(description = "用户可调资源参数")
@EqualsAndHashCode(callSuper = true)
@Data
public class AdjustParam extends BaseParam {

    /**
     * 开始时间，查询条件
     */
    @ApiModelProperty(value = "开始时间，查询条件")
    private String startTime;

    /**
     * 结束时间，查询条件
     */
    @ApiModelProperty(value = "结束时间，查询条件")
    private String endTime;




}
