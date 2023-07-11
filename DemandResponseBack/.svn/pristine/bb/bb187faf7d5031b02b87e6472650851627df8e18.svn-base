package com.xqxy.dr.modular.subsidy.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.xqxy.core.pojo.base.entity.BaseEntity;
import com.xqxy.sys.modular.cust.entity.Cons;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 月补贴
 * </p>
 *
 * @author hu xingxing
 * @since 2021-10-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dr_subsidy_monthly")
@ApiModel(value="SubsidyMonthly对象", description="月补贴")
public class SubsidyMonthly extends BaseEntity implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty(value = "关联用户ID")
    private String consId;

    @ApiModelProperty(value = "补贴月份")
    private String subsidyMonth;

    @ApiModelProperty(value = "总补贴金额")
    private BigDecimal totalAmount;

    @ApiModelProperty(value = "电力用户补偿金额")
    private BigDecimal consAmount;

    @ApiModelProperty(value = "负荷集成商补偿金额")
    private BigDecimal aggregateAmount;

    @ApiModelProperty(value = "有效响应户次")
    private Integer effectiveNum;

    @ApiModelProperty(value = "电力用户有效响应户次")
    private Integer consEffectiveNum;

    @ApiModelProperty(value = "负荷集成商有效响应户次")
    private Integer aggregateEffectiveNum;

    @ApiModelProperty(value = "状态(字典 0 未公示 1 已公示 2 已冻结)")
    private String status;

    @TableField(exist = false)
    private Cons cons;
}
