package com.xqxy.dr.modular.project.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 备用容量签约设备表
 * </p>
 *
 * @author Caoj
 * @since 2022-01-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dr_spare_contract_device")
public class SpareContractDevice implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 标识
     */
    private Long id;

    /**
     * 用户标识
     */
    private String consId;

    /**
     * 项目标识
     */
    private Long projectId;

    /**
     * 1直接参与，2代理参与
     */
    private String particType;

    /**
     * 申报标识
     */
    private Long contractId;

    /**
     * 响应类型 1削峰 2填谷
     */
    private String responseType;

    /**
     * 时间类型 1邀约 2实时
     */
    private String timeType;

    /**
     * 可相应负荷为该代理子用户设备可调
     */
    private BigDecimal responseCap;

    /**
     * 签约容量
     */
    private BigDecimal contractCap;

    /**
     * 响应开始时间，格式"12:30"
     */
    private String startTime;

    /**
     * 响应结束时间，格式"12:30"
     */
    private String endTime;

    /**
     * 签约价格
     */
    private BigDecimal contractPrice;

    /**
     * 项目明细标识
     */
    private Long projectDetailId;

    /**
     * 设备标识
     */
    private String deviceId;

    /**
     * 设备名称
     */
    @TableField(exist = false)
    private String deviceName;

    /**
     * 设备类型名称
     */
    @TableField(exist = false)
    private String deviceTypeName;

    /**
     * 签约明细标识
     */
    @TableField(exist = false)
    private Long contractDetailId;

    /**
     * 额定功率
     */
    @TableField(exist = false)
    private Long ratedPower;
}
