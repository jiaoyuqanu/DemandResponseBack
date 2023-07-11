package com.xqxy.dr.modular.device.model;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceBaseModel {

    @Excel(name = "电力户号", width = 30, height = 15)
    private String consId;
    
//    @Excel(name = "设备类型名称", width = 30, height = 15)
//    private String deviceTypeName;

    @Excel(name = "设备类型编码", width = 20, height = 15)
    private String deviceTypeCode;

    @Excel(name = "额定电压(单位: V)", width = 20, height = 15)
    private Integer ratedVoltage;

    @Excel(name = "额定功率(单位: kW)", width = 20, height = 15)
    private Integer ratedPower;

    @Excel(name = "额定电流(单位: A)", width = 20, height = 15)
    private Integer ratedCurrent;

    @Excel(name = "设备名称", width = 30, height = 15)
    private String deviceName;
    
    @Excel(name = "设备编号", width = 30, height = 15)
    private String deviceId;
    
    @Excel(name = "安装位置", width = 30, height = 15)
    private String address;

    @Excel(name = "设备运行最大负荷(kW)", width = 30, height = 15)
    private String runMaxLoad;

    @Excel(name = "设备运行最小负荷(kW)", width = 30, height = 15)
    private String runMinLoad;
    
    @Excel(name = "是否监测，是Y否N", width = 30, height = 15)
    private String montored;

//    @Excel(name = "设备型号", width = 20, height = 15)
//    private String deviceModel;

    @Excel(name = "数据接入日期 （格式：2022-01-01）", width = 35, height = 15,exportFormat = "yyyy-MM-dd", importFormat =  "yyyy-MM-dd" ,databaseFormat = "yyyy-MM-dd")
    private String accessDate;

//    @Excel(name = "设备注册日期 （格式：2022-01-01）", width = 35, height = 15,exportFormat = "yyyy-MM-dd", importFormat =  "yyyy-MM-dd" ,databaseFormat = "yyyy-MM-dd")
//    private String createTime;

    @Excel(name = "设备爬坡时间编码", width = 30, height = 15)
    private String deviceRiseTimeCode;

    @Excel(name = "削峰响应可持续时间编码", width = 30, height = 15)
    private String desResponseTimeCode;

    @Excel(name = "填谷响应可持续时间编码", width = 30, height = 15)
    private String risResponseTimeCode;

    @Excel(name = "负荷等级编码", width = 30, height = 15)
    private String powerLevelCode;
}
