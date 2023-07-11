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
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 响应补贴结算记录表
 * </p>
 *
 * @author hu xingxing
 * @since 2022-01-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dr_settlement_rec")
public class SettlementRec extends BaseEntity implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 记录标识
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 结算批号,不能重复，自动生成格式：‘yyyyMMddhhmmssSSS’
     */
    private String settlementId;

    /**
     * 开始日期
     */
    private LocalDate beginDate;

    /**
     * 截至日期
     */
    private LocalDate endDate;

    /**
     * 总金额
     */
    private BigDecimal subsidyAmount;

    /**
     * 结算周期,依据开始结束日志自动生成
     */
    private String settlementDesc;

    /**
     * 结算状态: 1 待发放 2已发放
     */
    private String state;

    /**
     * 项目标识
     */
    private Long projectId;

    /**
     * 客户个人金额
     */
    private BigDecimal settledAmount;
}
