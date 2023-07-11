package com.xqxy.dr.modular.subsidy.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.xqxy.core.pojo.base.entity.BaseEntity;
import com.xqxy.sys.modular.cust.entity.Cust;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 客户激励费用发放
 * </p>
 *
 * @author hu xingxing
 * @since 2021-10-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dr_cust_subsidy_pay")
@ApiModel(value="CustSubsidyPay对象", description="客户激励费用发放")
public class CustSubsidyPay extends BaseEntity implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "发放标识")
    @TableId(type = IdType.ASSIGN_ID)
    private Long payId;

    @ApiModelProperty(value = "激励费用批号")
    private String payNo;

    @ApiModelProperty(value = "是否集成商")
    private String integrator;

    @ApiModelProperty(value = "开始日期")
    private LocalDate beginDate;

    @ApiModelProperty(value = "截至日期")
    private LocalDate endDate;

    @ApiModelProperty(value = "激励金额")
    private BigDecimal subsidyAmount;

    @ApiModelProperty(value = "参与次数")
    private Integer particNum;

    @ApiModelProperty(value = "发放状态: 0 待发放 1 发放中 2 已发放")
    private String payStatus;

    @ApiModelProperty(value = "本实体记录的唯一标识，产生规则为流水号")
    @TableField("CUST_ID")
    private Long custId;

    @TableField(exist = false)
    private Cust cust;
}
