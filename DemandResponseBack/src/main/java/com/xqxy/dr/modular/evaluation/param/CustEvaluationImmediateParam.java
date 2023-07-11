package com.xqxy.dr.modular.evaluation.param;

import com.baomidou.mybatisplus.annotation.TableField;
import com.xqxy.core.pojo.base.param.BaseParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@ApiModel(description = "客户当日效果评估")
@EqualsAndHashCode(callSuper = true)
@Data
public class CustEvaluationImmediateParam  extends BaseParam {

    @ApiModelProperty(value = "评估标识")
    private Long evaluationId;

    @ApiModelProperty(value = "事件标识")
    @TableField("EVENT_ID")
    private Long eventId;

    @ApiModelProperty(value = "本实体记录的唯一标识，产生规则为流水号")
    @TableField("CUST_ID")
    private Long custId;

    private String legalName;

    private String creditCode;

    @ApiModelProperty(value = "邀约响应量")
    private BigDecimal invitationCap;

    @ApiModelProperty(value = "反馈响应量")
    private BigDecimal replyCap;

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

    @ApiModelProperty(value = "实际响应负荷")
    private BigDecimal actualCap;

    @ApiModelProperty(value = "实际响应电量")
    private BigDecimal actualEnergy;

    @ApiModelProperty(value = "核定响应负荷")
    private BigDecimal confirmCap;

    @ApiModelProperty(value = "是否有效:N 无效；Y 有效")
    private String isEffective;

    @ApiModelProperty(value = "有效响应时长(**分钟)")
    private Integer effectiveTime;

    @ApiModelProperty(value = "是否合格(平均负荷)")
    private String isQualified;

    @ApiModelProperty(value = "是否越界(最大负荷)")
    private String isOut;

    @ApiModelProperty(value = "机构")
    private String orgNo;

    @ApiModelProperty(value = "是否集成商")
    private String integrator;

}
