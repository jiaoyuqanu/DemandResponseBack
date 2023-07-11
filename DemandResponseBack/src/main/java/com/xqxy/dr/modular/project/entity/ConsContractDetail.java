package com.xqxy.dr.modular.project.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xqxy.core.pojo.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 申报明细
 * </p>
 *
 * @author PengChuqing
 * @since 2021-10-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dr_cons_contract_detail")
@ApiModel(value="ConsContractDetail对象", description="申报明细")
public class ConsContractDetail extends BaseEntity implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "明细标识")
    @TableId("DETAIL_ID")
    private Long detailId;

    @TableField("contract_id")
    private Long contractId;

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

    @ApiModelProperty(value = "最大容量")
    @TableField("CONTRACT_CAP")
    private BigDecimal contractCap;

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

    @ApiModelProperty(value = "最小响应时长")
    private Integer minTimes;

    @ApiModelProperty(value = "备用容量最小响应时长 单位分钟")
    @TableField("spare_min_times")
    private Integer spareMinTimes;

    @ApiModelProperty(value = "空调容量")
    @TableField("aircondition_cap")
    private BigDecimal AirconditionCap;

    @ApiModelProperty(value = "新型负荷管理可控负荷值 ")
    @TableField(exist = false)
    private BigDecimal controlCap;

    @ApiModelProperty(value = "用户签约 合同容量 ")
    @TableField(exist = false)
    private BigDecimal contractInfnCap;
}
