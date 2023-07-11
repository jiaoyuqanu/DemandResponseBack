package com.xqxy.dr.modular.device.param;

import com.baomidou.mybatisplus.annotation.TableField;
import com.xqxy.core.pojo.base.param.BaseParam;
import com.xqxy.sys.modular.cust.entity.CustCertifyFile;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;

@ApiModel(description = "设备 参数")
@EqualsAndHashCode(callSuper = true)
@Data
public class DeviceAdjustableBaseParam extends BaseParam {

    //主键标识
    private Long id;

    //用户标识
    private String consId;

    //设备类型名称
    private String deviceTypeName;

    //设备类型编码
    private String deviceTypeCode;

    //设备数量
    private Integer deviceNum;

    //额定电压
    private Integer ratedVoltage;

    /**
     * 额定电流
     */
    private Integer ratedCurrent;

    //额定功率
    private Integer ratedPower;

    //削峰响应可持续时间
    private String desResponseTime;

    //削峰响应可持续时间编码
    private String desResponseTimeCode;

    //填谷响应可持续时间
    private String risResponseTime;

    //填谷响应可持续时间编码
    private String risResponseTimeCode;

    //设备爬坡时间
    private String deviceRiseTime;

    //设备爬坡时间编码
    private String deviceRiseTimeCode;

    //负荷等级
    private String powerLevel;

    //负荷等级编码
    private String powerLevelCode;

    //设备名称
    private String deviceName;

    //设备标识
    private String deviceId;

    //设备型号
    private String deviceModel;

    @ApiModelProperty(value = "安装位置")
    private String address;

    @ApiModelProperty(value = "是否监测，字典 是 否")
    private String montored;

    @ApiModelProperty(value = "数据接入日期")
    private String accessDate;

    @ApiModelProperty(value = "关联文件ID")
    @TableField("file_id")
    private Long fileId;

    @ApiModelProperty(value = "文件类型")
    @TableField("file_type")
    private String fileType;

    // 供电单位编号
    private String orgNo;

    // 供电单位名称
    private String orgName;

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

    //用户标识 用于支路信息接口调用
    private String consNo;
}
