package com.xqxy.dr.modular.event.entity;

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
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 事件基线计算任务表，事件创建后插入数据，计算后更新
 * </p>
 *
 * @author PengChuqing
 * @since 2021-10-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dr_plan_baseline_task")
@ApiModel(value="PlanBaselineTask对象", description="事件基线计算任务表，事件创建后插入数据，计算后更新")
public class PlanBaselineTask extends BaseEntity implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "记录标识")
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;


    private Long baselinId;


    private LocalDate regulateDate;


    private String startTime;


    private String endTime;


    private String consId;

    private String baselineStatus;


    private String baselineDesc;

    private Long baselineCapId;

    private String baseLineCal;

    private String failTimes;
}
