package com.xqxy.dr.modular.event.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 事件效果评估表实体类
 *
 */
@Data
public class DrEventInvitationEffectEval implements Serializable {

    private static final long serialVersionUID = 8229750479092407047L;
    /**
     * 事件ID
     */
    private Long eventId;
    /**
     * 响应区域
     */
    private String cityName;
    /**
     * 响应日期
     */
    private String responseDate;
    /**
     * 开始时间
     */
    private String startTime;
    /**
     * 结束时间
     */
    private String endTime;
    /**
     * 目标容量
     */
    private BigDecimal regulateCap;
    /**
     * 实际工业用户数量
     */
    private int actualIndustrialUsers;
    /**
     * 实际商业用户数量
     */
    private int actualBusinessUsers;
    /**
     * 实际农业用户数量
     */
    private int actualAgriculturalUsers;
    /**
     * 实际居民用户数量
     */
    private int actualResidentUser;
    /**
     * 实际公交场站
     */
    private int actualBusStation;
    /**
     * 实际居民侧
     */
    private int actualResidentialside;
    /**
     * 实际储能
     */
    private int actualEnergyStorage;
    /**
     * 实际响应负荷
     */
    private BigDecimal actualResponseLoad;
    /**
     * 实际响应电量
     */
    private BigDecimal actualResponsePower;
    /**
     * 目标完成率
     */
    private double targetCompletionRate;
    /**
     * 有效工业用户数量
     */
    private int effectiveIndustrialUsers;
    /**
     * 有效商业用户数量
     */
    private int effectBusinessUsers;
    /**
     * 有效农业用户数量
     */
    private int effectAgriculturalUsers;
    /**
     * 有效居民用户数量
     */
    private int effectResidentUser;
    /**
     * 有效公交场站
     */
    private int effectBusStation;
    /**
     * 有效居民侧
     */
    private int effectResidentialside;
    /**
     * 有效储能
     */
    private int effectEnergyStorage;

    /**
     * 调控范围类别：地区/分区/变电站/线路/台区
     */
    @TableField(exist = false)
    private String rangeType;
}
