package com.xqxy.dr.modular.project.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * 项目明细
 * </p>
 *
 * @author PengChuqing
 * @since 2021-10-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dr_project_detail")
@ApiModel(value = "ProjectDetail对象", description = "项目明细")
public class ProjectDetail extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "明细标识")
    @TableId(type = IdType.ASSIGN_ID)
    private Long detailId;

    @ApiModelProperty(value = "项目标识")
    private Long projectId;

    @ApiModelProperty(value = "1削峰，2填谷")
    @TableField("RESPONSE_TYPE")
    private String responseType;

    @ApiModelProperty(value = "1邀约，2实时")
    @TableField("TIME_TYPE")
    private String timeType;

    /*@ApiModelProperty(value = "响应时段 格式：18:00-20:00")
    @TableField("RESPONSE_PERIOD")
    private String responsePeriod;*/

    @ApiModelProperty(value = "开始时间 格式 18：00")
    private String startTime;

    @ApiModelProperty(value = "结束时间 格式 20:00")
    private String endTime;

    @ApiModelProperty(value = "提前通知时间 单位分钟")
    private Integer advanceNoticeTime;

    @ApiModelProperty(value = "补偿价格，只有激励性项目才有")
    private BigDecimal price;


    @ApiModelProperty(value = "签约文件名称")
    @TableField(exist = false)
    private String fileName;

    @ApiModelProperty(value = "签约关联文件ID")
    @TableField(exist = false)
    private Long fileId;

    @ApiModelProperty(value = "签约文件类型")
    @TableField(exist = false)
    private String fileType;

    @ApiModelProperty(value = "签约容量")
    @TableField(exist = false)
    private BigDecimal contractCap;

    @ApiModelProperty(value = "分成比例")
    @TableField(exist = false)
    private Long extractRatio;

    @ApiModelProperty(value = "相应容量")
    @TableField(exist = false)
    private Long responseCap;

    @ApiModelProperty(value = "签约价格")
    @TableField(exist = false)
    private Long contractPrice;

    @ApiModelProperty(value = "用户签约状态")
    @TableField(exist = false)
    private Long status;

    @ApiModelProperty(value = "用户审核状态")
    @TableField(exist = false)
    private Long checkStatus;

    @ApiModelProperty(value = "签约明细标识")
    @TableField(exist = false)
    private Long contractDetailId;

    @ApiModelProperty(value = "目标负荷")
    private BigDecimal targetCup;

    @ApiModelProperty(value = "备用容量")
    @TableField(exist = false)
    private Long spareCap;

    @ApiModelProperty(value = "签约标识")
    @TableField(exist = false)
    private Long contractId;

    @ApiModelProperty(value = "完成率")
    @TableField(exist = false)
    private String completionRate;

    @ApiModelProperty(value = "最小响应时长")
    @TableField(exist = false)
    private Integer minTimes;

    @ApiModelProperty(value = "备用容量最小响应时长 单位分钟")
    @TableField(exist = false)
    private Integer spareMinTimes;

    @ApiModelProperty(value = "空调容量")
    @TableField(exist = false)
    private BigDecimal AirconditionCap;

    /**
     *  用户签约明细 的 已签约设备 的 签约容量总和
     */
    @TableField(exist = false)
    private BigDecimal sumCap;

    @ApiModelProperty(value = "新型负荷管理可控负荷值")
    @TableField(exist = false)
    private BigDecimal controlCap;
}
