package com.xqxy.dr.modular.evaluation.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 客户效果评估计算任务表
 * </p>
 *
 * @author hu xingxing
 * @since 2021-10-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dr_evalu_cust_task")
@ApiModel(value="EvaluCustTask对象", description="客户效果评估计算任务表")
public class EvaluCustTask implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "记录标识")
    @TableField("ID")
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty(value = "事件标识")
    @TableField("EVENT_ID")
    private Long eventId;

    @ApiModelProperty(value = "调控日期")
    @TableField("REGULATE_DATE")
    private LocalDate regulateDate;

    @ApiModelProperty(value = "响应开始时间")
    @TableField("START_TIME")
    private String startTime;

    @ApiModelProperty(value = "响应结束时间")
    @TableField("END_TIME")
    private String endTime;

    @ApiModelProperty(value = "客户标识")
    @TableField("CUST_ID")
    private Long custId;

    @ApiModelProperty(value = "是否集成商")
    @TableField("INTEGRATOR")
    private String integrator;

    @ApiModelProperty(value = "当日效果评估状态1：未计算，2：计算完成，3：异常")
    @TableField("EVALU_TODAY_STATUS")
    private String evaluTodayStatus;

    @ApiModelProperty(value = "当日效果评估标识")
    @TableField("EVALU_TODAY_ID")
    private Long evaluTodayId;

    @ApiModelProperty(value = "当日效果评估描述")
    @TableField("EVALU_TODAY_DESC")
    private String evaluTodayDesc;

    @ApiModelProperty(value = "次日效果评估状态1：未计算，2：计算完成，3：异常")
    @TableField("EVALU_NEXTDAY_STATUS")
    private String evaluNextdayStatus;

    @ApiModelProperty(value = "次日效果评估标识")
    @TableField("EVALU_NEXTDAY_ID")
    private Long evaluNextdayId;

    @ApiModelProperty(value = "次日效果评估描述")
    @TableField("EVALU_NEXTDAY_DESC")
    private String evaluNextdayDesc;

    @ApiModelProperty(value = "补贴计算状态1：未计算，2：计算完成，3：异常")
    @TableField("SUBSIDY_STATUS")
    private String subsidyStatus;

    @ApiModelProperty(value = "补贴计算标识")
    @TableField("SUBSIDY_ID")
    private Long subsidyId;

    @ApiModelProperty(value = "补贴计算描述")
    @TableField("SUBSIDY_DESC")
    private String subsidyDesc;

    @ApiModelProperty(value = "创建时间")
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建人")
    @TableField("CREATE_USER")
    private Long createUser;

    @ApiModelProperty(value = "更新时间")
    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "更新人")
    @TableField("UPDATE_USER")
    private Long updateUser;

    @ApiModelProperty(value = "激励标准")
    @TableField("INCENTIVE_STANDARD")
    private String incentiveStandard;

    @ApiModelProperty(value = "有效性判定")
    @TableField("VALIDITY_JUDGMENT")
    private String validityJudgment;

    @ApiModelProperty(value = "是否执行")
    @TableField("IMPLEMENT")
    private String implement;
}
