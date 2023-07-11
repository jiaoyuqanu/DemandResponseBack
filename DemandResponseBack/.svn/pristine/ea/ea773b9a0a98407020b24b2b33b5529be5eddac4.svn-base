package com.xqxy.dr.modular.data.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author xiao jun
 * @since 2021-05-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dr_grid_power")
public class GridPower implements Serializable {

    private static final long serialVersionUID=1L;

      private String id;

    /**
     * 最大值
     */
    private BigDecimal maxP;

    /**
     * 最大值发生时间
     */
    private LocalDateTime maxPTime;

    /**
     * 最小值
     */
    private BigDecimal minP;

    /**
     * 最小值发生时间
     */
    @TableField("min_P_time")
    private LocalDateTime minPTime;

    /**
     * 有序用电限电最大值
     */
    private BigDecimal maxYxyd;

    /**
     * 有序用电限电最大日期
     */
    private LocalDate maxYxydDate;

    /**
     * 年度
     */
    private String year;


}
