package com.xqxy.dr.modular.powerplant.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xqxy.core.pojo.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author Caoj
 * @since 2021-12-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dr_da_cons_income")
public class DaConsIncome extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 收益标识
     */
    private Long id;

    /**
     * 用户标识
     */
    private String consId;

    /**
     * 调控日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date pickLoadDate;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 基线负荷
     */
    private BigDecimal baselineLoad;

    /**
     * 实际响应负荷
     */
    private BigDecimal realResponseLoad;

    /**
     * 实际响应电量
     */
    private BigDecimal realResponseElect;

    /**
     * 激励费用
     */
    private BigDecimal urgeCost;


}
