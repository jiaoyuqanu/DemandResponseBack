package com.xqxy.dr.modular.event.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.xqxy.core.pojo.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 事件监测任务表
 * </p>
 *
 * @author Shen
 * @since 2021-10-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dr_event_monitor_task")
@ApiModel(value="EventMonitorTask对象", description="事件监测任务表")
public class EventMonitorTask extends BaseEntity implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "本实体记录的唯一标识，产生规则为流水号")
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty(value = "关联事件ID")
    private Long eventId;

    @ApiModelProperty(value = "响应开始日期")
    private LocalDate startDate;

    @ApiModelProperty(value = "响应结束日期")
    private LocalDate endDate;

    @ApiModelProperty(value = "响应开始时段:hh:mm:ss")
    private String startPeriod;

    @ApiModelProperty(value = "响应结束时段:hh:mm:ss")
    private String endPeriod;

    @ApiModelProperty(value = "参与用户ID集：用户1ID,用户2ID,用户3ID")
    private String consIds;



}
