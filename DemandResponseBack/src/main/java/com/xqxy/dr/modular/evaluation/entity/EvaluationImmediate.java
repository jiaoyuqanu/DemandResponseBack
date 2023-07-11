package com.xqxy.dr.modular.evaluation.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xqxy.core.pojo.base.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 今日响应效果评估
 * </p>
 *
 * @author chen zhi jun
 * @since 2021-10-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dr_cons_evaluation_immediate")
public class EvaluationImmediate extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 本实体记录的唯一标识，产生规则为流水号
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long evaluationId;

    /**
     * 本实体记录的唯一标识，产生规则为流水号
     */
    private String consId;

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
    private Integer effectiveTime;

    /**
     * 异常描述
     */
    private String exceptionRemark;

    @TableField(exist = false)
    private String cityName;

    @TableField(exist = false)
    private String countyName;

    /**
     * 项目有效性判断
     */
    @TableField(exist = false)
    private List<String> validityJudgment;

    private String joinUserType;

    private String remark;

    @ApiModelProperty(value = "负荷响应率")
    private BigDecimal executeRate;


}
