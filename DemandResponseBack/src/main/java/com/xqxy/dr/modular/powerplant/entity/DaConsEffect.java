package com.xqxy.dr.modular.powerplant.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xqxy.core.annotion.NeedSetValue;
import com.xqxy.core.pojo.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

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
@TableName("dr_da_cons_effect")
public class DaConsEffect extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户编号
     */
    @Excel(name = "用户编号", width = 25)
    private Long id;

    /**
     * 用户名称
     */
    @Excel(name = "用户名称", width = 20)
    private String consName;

    /**
     * 所属集成商标识
     */
    private String custId;

    /**
     * 供电单位编号
     */
    private String orgNo;

    /**
     * 供电线路电压
     */
    private BigDecimal lineVoltage;

    /**
     * 供电线路名称
     */
    private BigDecimal lineName;

    /**
     * 供电单位
     */
    @Excel(name = "供电单位", width = 25)
    private String orgName;

    /**
     * 基线负荷
     */
    @Excel(name = "基线负荷", width = 25)
    private BigDecimal baselineLoad;

    /**
     * 响应电量
     */
    @Excel(name = "响应电量", width = 15)
    private BigDecimal responseElect;

    /**
     * 激励费用
     */
    @Excel(name = "激励费用", width = 15)
    private BigDecimal urgeExpense;

    /**
     * 实际负荷曲线
     */
    @TableField(exist = false)
    private DaUserCurve daUserCurve;

    /**
     * 所属集成商名称
     */
    @TableField(exist = false)
    @NeedSetValue(beanClass = com.xqxy.dr.modular.powerplant.service.DrDaAggregatorService.class, method = "getNameById", params = {"custId"}, targetField = "drDaAggregator")
    private String daAggregatorName;


}
