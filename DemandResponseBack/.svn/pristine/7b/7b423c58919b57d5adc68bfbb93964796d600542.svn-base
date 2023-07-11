package com.xqxy.dr.modular.subsidy.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.xqxy.core.pojo.base.entity.BaseEntity;
import com.xqxy.dr.modular.evaluation.entity.CustEvaluation;
import com.xqxy.dr.modular.event.entity.Event;
import com.xqxy.sys.modular.cust.entity.Cust;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 客户事件激励费用
 * </p>
 *
 * @author hu xingxing
 * @since 2021-10-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dr_cust_subsidy")
@ApiModel(value="CustSubsidy对象", description="客户事件激励费用")
public class CustSubsidy extends BaseEntity implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "补贴标识")
    @TableId(type = IdType.ASSIGN_ID)
    private Long subsidyId;

    @ApiModelProperty(value = "事件标识")
    @TableField("EVENT_ID")
    private Long eventId;

    @ApiModelProperty(value = "本实体记录的唯一标识，产生规则为流水号")
    @TableField("CUST_ID")
    private Long custId;

    @ApiModelProperty(value = "是否集成商")
    @TableField("INTEGRATOR")
    private String integrator;

    @ApiModelProperty(value = "异常描述")
    @TableField("REMARK")
    private String remark;

    @ApiModelProperty(value = "实际响应负荷")
    private BigDecimal actualCap;

    @ApiModelProperty(value = "实际响应电量")
    private BigDecimal actualEnergy;

    @ApiModelProperty(value = "签约价格")
    @TableField("CONTRACT_PRICE")
    private BigDecimal contractPrice;

    @ApiModelProperty(value = "激励金额")
    private BigDecimal subsidyAmount;

    @ApiModelProperty(value = "计算规则，1，2，3")
    private String calRule;

    @TableField(exist = false)
    private Event event;

    @TableField(exist = false)
    private CustEvaluation custEvaluation;

    @TableField(exist = false)
    private Cust cust;

    @ApiModelProperty(value = "调控时间系数")
    @TableField("time_coefficient")
    private BigDecimal timeCoefficient;

    @ApiModelProperty(value = "负荷响应率系数")
    @TableField("rate_coefficient")
    private BigDecimal rateCoefficient;

    @TableField(exist = false)
    private BigDecimal total;

    @ApiModelProperty(value = "是否申诉")
    @TableField("is_appeal")
    private String isAppeal;

    @ApiModelProperty(value = "申诉前调控时间系数")
    @TableField("time_coefficient_old")
    private BigDecimal timeCoefficientOld;

    @ApiModelProperty(value = "申诉前负荷响应率系数")
    @TableField("rate_coefficient_old")
    private BigDecimal rateCoefficientOld;

    @ApiModelProperty(value = "申诉前激励金额")
    private BigDecimal subsidyAmountOld;

}
