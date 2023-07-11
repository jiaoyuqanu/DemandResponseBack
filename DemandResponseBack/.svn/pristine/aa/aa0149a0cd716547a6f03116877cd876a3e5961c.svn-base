package com.xqxy.dr.modular.device.VO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceAdjustableBaseVO {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键标识")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "用户标识")
    @TableField("CONS_ID")
    private String consId;

    @ApiModelProperty(value = "设备类型名称")
    @TableField("DEVICE_TYPE_NAME")
    private String deviceTypeName;

    @ApiModelProperty(value = "设备类型编码")
    @TableField("DEVICE_TYPE_CODE")
    private String deviceTypeCode;

    @ApiModelProperty(value = "设备数量")
    @TableField("DEVICE_NUM")
    private Integer deviceNum;

    /**
     * 设备品牌
     */
    @ApiModelProperty(value = "设备品牌")
    @TableField("DEVICE_BRAND")
    private String deviceBrand;

    /**
     * 额定电流
     */
    @ApiModelProperty(value = "额定电流")
    @TableField("RATED_CURRENT")
    private Integer ratedCurrent;


    @ApiModelProperty(value = "额定电压")
    @TableField("RATED_VOLTAGE")
    private Integer ratedVoltage;

    @ApiModelProperty(value = "额定功率")
    @TableField("RATED_POWER")
    private Integer ratedPower;

    @ApiModelProperty(value = "削峰响应可持续时间")
    @TableField("DES_RESPONSE_TIME")
    private String desResponseTime;

    @ApiModelProperty(value = "削峰响应可持续时间编码")
    @TableField("DES_RESPONSE_TIME_CODE")
    private String desResponseTimeCode;

    @ApiModelProperty(value = "填谷响应可持续时间")
    @TableField("RIS_RESPONSE_TIME")
    private String risResponseTime;

    @ApiModelProperty(value = "填谷响应可持续时间编码")
    @TableField("RIS_RESPONSE_TIME_CODE")
    private String risResponseTimeCode;

    @ApiModelProperty(value = "设备爬坡时间")
    @TableField("DEVICE_RISE_TIME")
    private String deviceRiseTime;

    @ApiModelProperty(value = "设备爬坡时间编码")
    @TableField("DEVICE_RISE_TIME_CODE")
    private String deviceRiseTimeCode;

    @ApiModelProperty(value = "负荷等级")
    @TableField("POWER_LEVEL")
    private String powerLevel;

    @ApiModelProperty(value = "负荷等级编码")
    @TableField("POWER_LEVEL_CODE")
    private String powerLevelCode;

    @ApiModelProperty(value = "设备名称")
    @TableField("DEVICE_NAME")
    private String deviceName;

    @ApiModelProperty(value = "设备标识")
    @TableField("DEVICE_Id")
    private String deviceId;

    @ApiModelProperty(value = "省电力公司码")
    @TableField("PROVINCE_ELE_CODE")
    private String provinceEleCode;

    @ApiModelProperty(value = "省电力公司名称")
    @TableField("PROVINCE_ELE_NAME")
    private String provinceEleName;

    @ApiModelProperty(value = "市电力公司码")
    @TableField("CITY_ELE_CODE")
    private String cityEleCode;

    @ApiModelProperty(value = "市电力公司名称")
    @TableField("CITY_ELE_NAME")
    private String cityEleName;

    @ApiModelProperty(value = "安装位置")
    @TableField("ADDRESS")
    private String address;

    @ApiModelProperty(value = "是否监测，字典 是 否")
    @TableField("MONTORED")
    private String montored;

    @ApiModelProperty(value = "设备型号")
    @TableField("DEVICE_MODEL")
    private String deviceModel;

    @ApiModelProperty(value = "数据接入日期")
    private String accessDate;

    @ApiModelProperty(value = "数据注册日期")
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
//    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date createTime;

    @ApiModelProperty(value = "关联文件ID")
    @TableField("file_id")
    private Long fileId;

    @ApiModelProperty(value = "文件类型")
    @TableField("file_type")
    private String fileType;

    @ApiModelProperty(value = "文件名称（上传时候的文件名）")
    @TableField("file_origin_name")
    private String fileOriginName;

    @ApiModelProperty(value = "存储到bucket的名称（文件唯一标识id）")
    @TableField("file_object_name")
    private String fileObjectName;

    @ApiModelProperty(value = "支路标识")
    @TableField("branch_id")
    private String branchId;

    @ApiModelProperty(value = "支路名称")
    @TableField("branch_name")
    private String branchName;

    @ApiModelProperty(value = "量测单元标识")
    @TableField("gauge_no")
    private String gaugeNo;

    @ApiModelProperty(value = "量测单元名称")
    @TableField("gauge_name")
    private String gaugeName;

    @ApiModelProperty(value = "最小运行负荷")
    private BigDecimal runMinLoad;

    @ApiModelProperty(value = "最大运行负荷")
    private BigDecimal runMaxLoad;


    /**
     * 设备负荷点 集合
     */
    private List<String> PointList;
}
