package com.xqxy.dr.modular.subsidy.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xqxy.core.pojo.base.entity.BaseEntity;
import com.xqxy.sys.modular.cust.entity.Cons;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * <p>
 * 用户激励费用发放
 * </p>
 *
 * @author hu xingxing
 * @since 2021-10-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dr_cons_subsidy_pay")
@ApiModel(value="ConsSubsidyPay对象", description="用户激励费用发放")
public class ConsSubsidyPay extends BaseEntity implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "发放标识")
    @TableId(type = IdType.ASSIGN_ID)
    private Long payId;

    @ApiModelProperty(value = "用户标识")
    @TableField("CONS_ID")
    private String consId;

    @ApiModelProperty(value = "激励费用批号")
    private String payNo;

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

    @TableField(exist = false)
    private Cons cons;
}
