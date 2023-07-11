package com.xqxy.dr.modular.evaluation.param;

import com.xqxy.core.pojo.base.param.BaseParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@ApiModel(description = "用户当日效果评估")
@EqualsAndHashCode(callSuper = true)
@Data
public class ConsEvaluationImmediateParam extends BaseParam {

    @ApiModelProperty(value = "本实体记录的唯一标识，产生规则为流水号")
    private Long evaluationId;

    @ApiModelProperty(value = "本实体记录的唯一标识，产生规则为流水号")
    private String consId;

    //用户名称
    private String consName;

    @ApiModelProperty(value = "本实体记录的唯一标识，产生规则为流水号")
    private Long eventId;

    @ApiModelProperty(value = "约定响应量")
    private BigDecimal invitationCap;

    @ApiModelProperty(value = "反馈响应量")
    private BigDecimal replyCap;

    @ApiModelProperty(value = "实际响应量")
    private BigDecimal actualCap;

    @ApiModelProperty(value = "核定响应量")
    private BigDecimal confirmCap;

    @ApiModelProperty(value = "基线最大负荷")
    private BigDecimal maxLoadBaseline;

    @ApiModelProperty(value = "基线最小负荷")
    private BigDecimal minLoadBaseline;

    @ApiModelProperty(value = "基线平均负荷")
    private BigDecimal avgLoadBaseline;

    @ApiModelProperty(value = "实际最大负荷")
    private BigDecimal maxLoadActual;

    @ApiModelProperty(value = "实际最小负荷")
    private BigDecimal minLoadActual;

    @ApiModelProperty(value = "实际平均负荷")
    private BigDecimal avgLoadActual;

    @ApiModelProperty(value = "基线用电量")
    private BigDecimal electricityBaseline;

    @ApiModelProperty(value = "实际用电量")
    private BigDecimal electricityActual;

    @ApiModelProperty(value = "是否有效响应:0 无效 1 有效")
    private String isEffective;

    @ApiModelProperty(value = "有效响应时长:**分钟")
    private Long effectiveTime;

    @ApiModelProperty(value = "异常描述")
    private String exceptionRemark;

    @ApiModelProperty(value = "机构")
    private String orgNo;
}
