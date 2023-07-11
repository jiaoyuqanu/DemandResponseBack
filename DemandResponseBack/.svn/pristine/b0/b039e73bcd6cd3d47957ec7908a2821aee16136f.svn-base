package com.xqxy.dr.modular.evaluation.param;

import com.xqxy.core.pojo.base.param.BaseParam;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * <p>
 * 实时响应效果评估 参数
 * </p>
 *
 * @author xiao jun
 * @since 2021-03-11
 */
@ApiModel(description = "实时响应效果评估 参数")
@EqualsAndHashCode(callSuper = true)
@Data
public class EvaluationImmediateParam extends BaseParam {

    /**
     * 本实体记录的唯一标识，产生规则为流水号
     */
    private Long evaluationId;

    /**
     * 本实体记录的唯一标识，产生规则为流水号
     */
    private String consId;

    private String consName;

    private String creditCode;

    private String legalName;

    /**
     * 本实体记录的唯一标识，产生规则为流水号
     */
    private Long eventId;

    /**
     * 约定响应量
     */
    private BigDecimal invitationCap;

    /**
     * 反馈响应量
     */
    private BigDecimal replyCap;

    /**
     * 实际响应量
     */
    private BigDecimal actualCap;

    /**
     * 核定响应量
     */
    private BigDecimal confirmCap;

    /**
     * 基线最大负荷
     */
    private BigDecimal maxLoadBaseline;

    /**
     * 基线最小负荷
     */
    private BigDecimal minLoadBaseline;

    /**
     * 基线平均负荷
     */
    private BigDecimal avgLoadBaseline;

    /**
     * 实际最大负荷
     */
    private BigDecimal maxLoadActual;

    /**
     * 实际最小负荷
     */
    private BigDecimal minLoadActual;

    /**
     * 实际平均负荷
     */
    private BigDecimal avgLoadActual;

    /**
     * 基线用电量
     */
    private BigDecimal electricityBaseline;

    /**
     * 实际用电量
     */
    private BigDecimal electricityActual;

    /**
     * 是否有效响应:0 无效 1 有效
     */
    private String isEffective;

    /**
     * 有效响应时长:**分钟
     */
    private Long effectiveTime;

    /**
     * 异常描述
     */
    private String exceptionRemark;

    /**
     * 是否聚合商用户，Y-是，N-否
     */
    private String isAggreCons;

    /**
     * 负荷集成商id
     *
     */
    private String aggregateConsId;



}
