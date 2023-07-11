package com.xqxy.dr.modular.baseline.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xqxy.core.pojo.base.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * <p>
 * 事件基线计算任务表
 * </p>
 *
 * @author chen zhi jun
 * @since 2021-10-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dr_plan_baseline_task")
public class PlanBaseLine extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 本实体记录的唯一标识，基线主键id
     */
    @ApiModelProperty(value = "基线主键id")
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 基线库标识
     */
    @ApiModelProperty(value = "基线库标识")
    private Long baselinId;

    /**
     * 调控日期
     */
    @ApiModelProperty(value = "调控日期")
    private LocalDate regulateDate;

    /**
     * 响应开始时段:hh:mm
     */
    @ApiModelProperty(value = "响应开始时段:hh:mm")
    private String startTime;

    /**
     * 响应结束时段:hh:mm
     */
    @ApiModelProperty(value = "响应结束时段:hh:mm")
    private String endTime;

    /**
     * 用户标识
     */
    @ApiModelProperty(value = "用户标识")
    private String consId;

    /**
     * 基线计算状态 1：未计算，2：计算完成，3：异常
     */
    @ApiModelProperty(value = "基线计算状态 1：未计算，2：计算完成，3：异常")
    private String baselineStatus;

    /**
     * 基线计算描述
     */
    @ApiModelProperty(value = "基线计算描述")
    private String baselineDesc;

    /**
     * 基线负荷标识
     */
    @ApiModelProperty(value = "基线负荷标识")
    private Long baselineCapId;

    /**
     * 基线计算准则 格式：1，2，3
     */
    @ApiModelProperty(value = "基线计算准则 格式：1，2，3")
    private String baseLineCal;

    /**
     * 计算次数不能超过三次
     */
    @ApiModelProperty(value = "计算次数不能超过三次")
    private Integer failTimes;


}
