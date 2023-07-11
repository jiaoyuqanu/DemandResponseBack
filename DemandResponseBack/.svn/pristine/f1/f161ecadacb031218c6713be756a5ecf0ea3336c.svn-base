package com.xqxy.dr.modular.statistics.entity;

import java.math.BigDecimal;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 *
 * </p>
 *
 * @author shi
 * @since 2022-02-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dr_load_statistic")
public class LoadStatistic implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
      private Long id;

    /**
     * 项目标识
     */
    private Long projectId;

    /**
     * 组织机构
     */
    @Excel(name = "供电单位",width = 20)
    private String orgNo;

    /**
     * 签约户数(户)
     */
    @Excel(name = "签约户数(户)",width = 10)
    private Integer contractHouseholds;

    /**
     * 签约负荷(万千瓦)
     */
    @Excel(name = "签约负荷(万千瓦)",width = 10)
    private BigDecimal contractCap;

    /**
     * 备用容量(万千瓦)
     */
    @Excel(name = "备用容量(万千瓦)",width = 10)
    private BigDecimal spareCapacity;

    /**
     * 96点采集成功户数(户)
     */
    @Excel(name = "96点采集成功户数(户)",width = 15)
    private Integer collectionSuccessful;

    /**
     * 采集成功率
     */
    @Excel(name = "采集成功率",width = 10)
    private String successRate;

    /**
     * 当日最大负荷
     */
    @Excel(name = "当日最大负荷(万千瓦)",width = 10)
    private BigDecimal dayMaxLoad;

    /**
     * 最大负荷发生时间
     */
    @Excel(name = "最大负荷发生时间",width = 10)
    private String maxLoadTime;

    /**
     * 当日最小负荷
     */
    @Excel(name = "当日最小负荷(万千瓦)",width = 10)
    private BigDecimal dayMinLoad;

    /**
     * 最小负荷发生时间
     */
    @Excel(name = "最小负荷发生时间",width = 10)
    private String minLoadTime;

    /**
     * 保安负荷
     */
    @Excel(name = "保安负荷(万千瓦)",width = 10)
    private BigDecimal safetyLoad;

    /**
     * 午高峰平均负荷
     */
    @Excel(name = "午高峰平均负荷(万千瓦)",width = 10)
    private BigDecimal noonPeakAverageLoad;

    /**
     * 晚高峰平均负荷
     */
    @Excel(name = "晚高峰平均负荷(万千瓦)",width = 15)
    private BigDecimal eveningPeakAverageLoad;

    /**
     * 夜间平均负荷
     */
    @Excel(name = "夜间平均负荷(万千瓦)",width = 15)
    private BigDecimal nightAverageLoad;

    /**
     * 午高峰理论最大填谷能力
     */
    @Excel(name = "午高峰理论最大填谷能力(万千瓦)",width = 15)
    private BigDecimal noonRisAbility;

    /**
     * 晚高峰理论最大填谷能力
     */
    @Excel(name = "晚高峰理论最大填谷能力(万千瓦)",width = 15)
    private BigDecimal eveningDesAbility;

    /**
     * 夜间理论最大填谷能力
     */
    @Excel(name = "夜间理论最大填谷能力(万千瓦)",width = 15)
    private BigDecimal nightRisAbility;

    /**
     * 最大备用能力
     */
    @Excel(name = "最大备用能力(万千瓦)",width = 15)
    private BigDecimal maxReserveCapacity;

    /**
     * 统计日期
     */
    private LocalDate statistilDate;


}
