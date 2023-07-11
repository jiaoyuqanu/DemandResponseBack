package com.xqxy.dr.modular.adjustable.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 设备级可调节潜力信息
 * </p>
 *
 * @author liqirui
 * @since 2021-11-06
 */
@Data
@TableName("dr_device_adjustable_potential")
@ApiModel(value="DrDeviceAdjustablePotential对象", description="设备可调节潜力")
public class DrDeviceAdjustablePotential implements Serializable {

private static final long serialVersionUID=1L;

    /**
     * 主键标识
     */
    @Excel(name = "主键标识",width = 10)
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 设备基础信息标识 
     */
    @Excel(name = "设备基础信息标识",width = 10)
    @TableField("DEVICE_ID")
    private String deviceId;

    /**
     * 响应时段
     */
    @Excel(name = "响应时段",width = 10)
    @TableField("RESPONSE_PERIOD")
    private String responsePeriod;

    /**
     * 响应负荷
     */
    @Excel(name = "响应负荷",width = 10)
    @TableField("RESPONSE_POWER")
    private BigDecimal responsePower;

    /**
     * 响应类型1削峰2填谷
     */
    @Excel(name = "响应类型1削峰2填谷",width = 10)
    @TableField("RESPONSE_TYPE")
    private String responseType;

    /**
     * 响应类型1削峰2填谷
     */
    @Excel(name = "响应类型1削峰2填谷",width = 10)
    @TableField("RESPONSE_TYPE")
    private String responseTypeDesc;

    /**
     * 时间类型1邀约2实时
     */
    @Excel(name = "时间类型1邀约2实时",width = 10)
    @TableField("TIME_TYPE")
    private String timeType;

    /**
     * 时间类型1邀约2实时
     */
    @Excel(name = "时间类型1邀约2实时",width = 10)
    @TableField("TIME_TYPE")
    private String timeTypeDesc;

    /**
     * 响应开始时间
     */
    @Excel(name = "响应开始时间",width = 10)
    @TableField("START_TIME")
    private String startTime;

    /**
     * 响应结束时间
     */
    @Excel(name = "响应结束时间",width = 10)
    @TableField("END_TIME")
    private String endTime;

    @TableField("DEVICE_NAME")
    private String deviceName;

    @TableField(exist = false)
    private String deviceTypeCode;


}
