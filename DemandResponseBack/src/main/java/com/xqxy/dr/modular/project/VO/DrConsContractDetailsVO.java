package com.xqxy.dr.modular.project.VO;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DrConsContractDetailsVO {
    /**
     * 市码
     */
    // @Excel(name = "市码", width = 10)
    private String cityCode;

    /**
     * 市码
     */
    // @Excel(name = "市名词 ", width = 10)
    private String cityCodeDesc;

    /**
     * 区县码
     */
    // @Excel(name = "区县码", width = 10)
    private String countyCode;

    /**
     * 市码
     */
    // @Excel(name = "区县名称", width = 10)
    private String countyCodeDesc;

    /**
     * 客户名称
     */
    // @Excel(name = "客户名称", width = 10)
    private String name;
    /**
     * 联系方式
     */
    // @Excel(name = "联系方式", width = 10)
    private String firstContactInfo;
    /**
     * 行业类型
     */
    // @Excel(name = "行业类型", width = 10)
    private String tradeCode;
    /**
     * 电力营销户号
     */
    private String consId;

    /**
     * 审核状态
     */
    // @Excel(name = "审核状态", width = 10)
    private String checkStatus;

    /**
     * 审核状态
     */
    // @Excel(name = "审核状态", width = 10)
    private String checkStatusDesc;

    /**
     * 时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    // @Excel(name = "时间", width = 10)
    private String createTime;

    /**
     * 签约负荷(容量)
     */
    private BigDecimal contractCap;

    @ApiModelProperty(value = "最小响应时长")
    private Integer minTimes;


    /**
     * 日前约时削峰响应负荷
     */
    // @Excel(name = "响应负荷(kW)", width = 10)
    private BigDecimal dayInvtionPeakContractCap;
    /**
     * 日前约时削峰响应最小时长
     */
    // @Excel(name = "响应负荷最小时长(分)", width = 10)
    private Integer dayInvtionPeakMinTimes;


    /**
     * 日前约时填谷响应负荷
     */
    // @Excel(name = "响应负荷(kW)", width = 10)
    private BigDecimal dayInvtionValleyContractCap;
    /**
     * 日前约时填谷响应最小时长
     */
    // @Excel(name = "响应负荷最小时长(分)", width = 10)
    private Integer dayInvtionValleyMinTimes;


    /**
     * 小时级约时削峰响应负荷
     */
    // @Excel(name = "响应负荷(kW)", width = 10)
    private BigDecimal hourInvtionPeakContractCap;
    /**
     * 小时级约时削峰响应最小时长
     */
    // @Excel(name = "响应负荷最小时长(分)", width = 10)
    private Integer hourInvtionPeakMinTimes;


    /**
     * 分钟级实时削峰响应负荷
     */
    // @Excel(name = "响应负荷(kW)", width = 10)
    private BigDecimal minuteInvtionPeakContractCap;
    /**
     * 分钟级实时削峰响应最小时长
     */
    // @Excel(name = "响应负荷最小时长(分)", width = 10)
    private Integer minuteInvtionPeakMinTimes;


    /**
     * 秒级实时削峰响应负荷
     */
    // @Excel(name = "响应负荷(kW)", width = 10)
    private BigDecimal secondInvtionPeakContractCap;
    /**
     * 秒级实时削峰响应最小时长
     */
    // @Excel(name = "响应负荷最小时长(分)", width = 10)
    private Integer secondInvtionPeakMinTimes;


    @ApiModelProperty(value = "备用容量最小响应时长 单位分钟")
    private Integer spareMinTimes;

    /**
     * 申报标识
     */
    private Long contractId;

    /**
     * 客户Id
     */
    private Long custId;

    /**
     * 统一信用代码
     */
    private String creditCode;

    private String orgId;

    private String orgName;


    @ApiModelProperty(value = "明细标识")
    @TableId("DETAIL_ID")
    private Long detailId;

    @ApiModelProperty(value = "响应类型")
    @TableField("RESPONSE_TYPE")
    private String responseType;

    @ApiModelProperty(value = "时间类型")
    @TableField("TIME_TYPE")
    private String timeType;

   /* @ApiModelProperty(value = "响应时段")
    @TableField("RESPONSE_PERIOD")
    private String responsePeriod;*/

    @ApiModelProperty(value = "响应开始时间")
    @TableField("START_TIME")
    private String startTime;

    @ApiModelProperty(value = "响应结束时间")
    @TableField("END_TIME")
    private String endTime;

    @ApiModelProperty(value = "可响应负荷为该代理子用户设备可调 节负荷的累加")
    @TableField("RESPONSE_CAP")
    private BigDecimal responseCap;
    @ApiModelProperty(value = "签约价格")
    @TableField("CONTRACT_PRICE")
    private BigDecimal contractPrice;

    @ApiModelProperty(value = "分成比例")
    @TableField("EXTRACT_RATIO")
    private BigDecimal extractRatio;

    @ApiModelProperty(value = "项目明细标识")
    @TableField("PROJECT_DETAIL_ID")
    private Long projectDetailId;

    @ApiModelProperty(value = "提前通知时间")
    private Integer advanceNoticeTime;

    @ApiModelProperty(value = "备用容量")
    private BigDecimal spareCap;

    private String orgNo;

    /**
     * 省 供电公司
     */
    @ApiModelProperty(value = "省 供电公司")
    private String provinceOrgNo;


    /**
     * 市 供电公司
     */
    @ApiModelProperty(value = "市 供电公司")
    private String cityOrgNo;


    /**
     * 区/县 供电公司
     */
    @ApiModelProperty(value = "区/县 供电公司")
    private String areaOrgNo;


    /**
     * 乡/镇/街道 供电公司
     */
    @ApiModelProperty(value = "乡/镇/街道 供电公司")
    private String streetOrgNo;
}
