package com.xqxy.dr.modular.device.entity;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class DeviceMonitor implements Serializable {

    /**
     * 设备id
     */
    private String deviceId;
    /**
     * 用户名称
     */
    private String consName;

    /**
     * 设备名称
     */
    private String DeviceName;

    /**
     * 设备类型名称
     */
    private String deviceTypeName;

    /**
     * 设备类型code
     */
    private String deviceTypeCode;

    /**
     * 额定功率
     */
    private Integer ratedPower;

    /**
     * 调节速度
     */
    private String deviceRiseTime;

    /**
     * 用户编号
     */
    private String consId;
}
