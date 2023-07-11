package com.xqxy.dr.modular.powerplant.entity;

import java.math.BigDecimal;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 *
 * </p>
 *
 * @author shi
 * @since 2021-12-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dr_da_cons_execute")
public class DaConsExecute implements Serializable {

    private static final long serialVersionUID=1L;

      private Long id;

    /**
     * 用户编号
     */
    @Excel(name = "用户编号",width = 10)
    private String consNo;

    /**
     * 用户名称
     */
    @Excel(name = "用户名称",width = 10)
    private String consName;

    /**
     * 供电单位编码
     */
    @Excel(name = "供电单位编码",width = 10)
    private String orgNo;

    /**
     * 供电单位名称
     */
    @Excel(name = "供电单位名称",width = 10)
    private String orgName;

    /**
     * 行业分类编码
     */
    @Excel(name = "行业分类编码",width = 10)
    @TableField("TRADE_CODE")
    private String tradeCode;

    /**
     * 行业分类编码
     */
    @Excel(name = "行业分类名称",width = 10)
    @TableField("TRADE_Name")
    private String tradeName;

    /**
     * 基准负荷
     */
    @Excel(name = "基准负荷",width = 10)
    private BigDecimal baselineLoad;

    /**
     * 计划负荷
     */
    @Excel(name = "计划负荷",width = 10)
    private BigDecimal planLoad;

    /**
     * 响应负荷
     */
    @Excel(name = "响应负荷",width = 10)
    private BigDecimal replyLoad;

    /**
     * 响应电量
     */
    @Excel(name = "响应电量",width = 10)
    private BigDecimal replyElectric;

    /**
     * 是否有效
     */
    @Excel(name = "是否有效",width = 10)
    private String isEffect;

    /**
     * 执行户数
     */
    @TableField(exist=false)
    private Integer executeCount;

    /**
     * 有效户数
     */
    @TableField(exist=false)
    private Integer effectiveCount;



}
