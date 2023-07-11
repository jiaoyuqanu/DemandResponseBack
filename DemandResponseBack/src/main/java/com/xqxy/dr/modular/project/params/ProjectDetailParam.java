package com.xqxy.dr.modular.project.params;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.xqxy.core.pojo.base.param.BaseParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@ApiModel(description = "需求响应项目明细 参数")
@EqualsAndHashCode(callSuper = true)
@Data
public class ProjectDetailParam extends BaseParam {

    @ApiModelProperty(value = "明细标识")
    private Long detailId;

    @ApiModelProperty(value = "项目标识")
    private Long projectId;

    @ApiModelProperty(value = "1削峰，2填谷")
    private String responseType;

    @ApiModelProperty(value = "1邀约，2实时")
    private String timeType;

    @ApiModelProperty(value = "开始时间 格式 18：00")
    private String startTime;

    @ApiModelProperty(value = "结束时间 格式 20:00")
    private String endTime;

    @ApiModelProperty(value = "提前通知时间 单位分钟")
    private Integer advanceNoticeTime;

    @ApiModelProperty(value = "补偿价格，只有激励性项目才有")
    private BigDecimal price;

    @ApiModelProperty(value = "目标负荷")
    private Long up;
}
