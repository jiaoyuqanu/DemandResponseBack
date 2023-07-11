package com.xqxy.dr.modular.event.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xqxy.core.pojo.base.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 需求响应事件
 * </p>
 *
 * @author PengChuqing
 * @since 2021-10-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dr_event")
public class Event extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 事件标识
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long eventId;

    /**
     * 方案标识
     */
    @TableField("REGULATE_ID")
    private Long regulateId;

    /**
     * 项目标识
     */
    @TableField("PROJECT_ID")
    private Long projectId;

    /**
     * 事件编号
     */
    @TableField("EVENT_NO")
    private Long eventNo;

    /**
     * 事件名称
     */
    @TableField("EVENT_NAME")
    private String eventName;

    /**
     * 响应类型
     */
    @TableField("RESPONSE_TYPE")
    private String responseType;

    /**
     * 时间类型
     */
    @TableField("TIME_TYPE")
    private String timeType;

    /**
     * 调控范围类别：地区/分区/变电站/线路/台区
     */
    @TableField("RANGE_TYPE")
    private String rangeType;

    /**
     * 调控范围
     */
    @TableField("REGULATE_RANGE")
    private String regulateRange;

    /**
     * 调控目标
     */
    @TableField("REGULATE_CAP")
    private BigDecimal regulateCap;

    /**
     * 调控日期
     */
    @TableField("REGULATE_DATE")
    private LocalDate regulateDate;

    /**
     * 响应开始时间
     */
    @TableField("START_TIME")
    private String startTime;

    /**
     * 响应结束时间
     */
    @TableField("END_TIME")
    private String endTime;

    /**
     * 提前通知时间
     */
    private String advanceNoticeTime;

    /**
     * 事件状态 1:保存，2待执行，3执行中，4结束，5废弃
     */
    private String eventStatus;

    /**
     * 审核状态 1:未提交，2：审核中，3：审核通过，4：审核不通过
     */
    private String checkStatus;

    /**
     * 参与条件  [{type:1,value:1,2,3,4},....]
     */
    private String particiCondition;

    /**
     * 激励标准 格式：1，2，3
     */
    private String incentiveStandard;

    /**
     * 有效性判定,格式：1，2，3
     */
    private String validityJudgment;

    /**
     * 基线计算准则 格式：1，2，3
     */
    private String baseLineCal;


    private Long baselinId;

    /**
     * 邀约信息是否同步到方案
     */
    private String synchToPlan;


    @TableField(exist = false)
    private ConsBaseline consBaseline;

    @TableField(exist = false)
    private Long planId;


    @TableField(exist = false)
    private LocalDateTime deadlineTime;


    @TableField(exist = false)
    private String createOrg;


    /**
     * 目标区域（调控范围）
     */
    @ApiModelProperty(value = "目标区域数组中文")
    @TableField(exist = false)
    private String regulateRangeStr;

    @ApiModelProperty(value = "目标区域中文")
    @TableField(exist = false)
    private String regulateRangeStrText;

    /**
     * 目标区域（调控范围）
     */
    @ApiModelProperty(value = "目标区域数组英文")
    @TableField(exist = false)
    private List<List<String>> regulateRangeList;

    /**
     * 目标区域（调控范围）
     */
    @TableField(exist = false)
    private List<String> list;

    /**
     * 邀约状态
     */
    @TableField(exist = false)
    private String invitationStatus;

    /**
     * 用户邀约标识
     */
    @TableField(exist = false)
    private Long invitationId;

    /**
     * 调度缺口
     */
    @TableField(exist = false)
    private BigDecimal regulateGap;

    /**
     * 用户户号
     */
    @TableField(exist = false)
    private String consId;

    /**
     * 用户名称
     */
    @TableField(exist = false)
    private String consName;

    /**
     * 调控目标的百分比， 不能超过1.5倍
     */
    @ApiModelProperty(value = "调控目标的百分比， 不能超过1.5倍")
    @TableField("REGULATE_MULTIPLE")
    private BigDecimal regulateMultiple;

    /**
     * 截止条件 1.截止日期 2. 反馈目标*反馈倍数 3.或者满足任意条件，即全选
     */
    @ApiModelProperty(value = " 截止条件 1.截止日期 2. 反馈目标*反馈倍数 3.或者满足任意条件，即不符合要求")
    @TableField("end_condition")
    private Integer endCondition;

    @ApiModelProperty(value = "事件基线是否计算Y是N否")
    @TableField("baseline_status")
    private String baselineStatus;

    @ApiModelProperty(value = "事件基线计算失败次数")
    @TableField("baseline_num")
    private Integer baselineNum;

    @ApiModelProperty(value = "推送状态1表示推送")
    @TableField("send_status")
    private String sendStatus;

    @ApiModelProperty(value = "今日效果评估计算次数")
    @TableField("today_count")
    private Integer todayCount;

    @ApiModelProperty(value = "次日效果评估计算次数")
    @TableField("nextday_count")
    private Integer nextdayCount;

    @ApiModelProperty(value = "客户今日效果评估计算次数")
    @TableField("cust_today_count")
    private Integer custTodayCount;

    @ApiModelProperty(value = "客户次日效果评估计算次数")
    @TableField("cust_nextday_count")
    private Integer custNextdayCount;

    @ApiModelProperty(value = "是否公布补贴0表示不公布1表示公布")
    @TableField("subsidy_pub")
    private String subsidyPub;


    private String orgId;

    @TableField(exist = false)
    @ApiModelProperty(value = "dr_subsidy_appeal主键")
    private String drSubsidyAppealId;

    @TableField(exist = false)
    @ApiModelProperty(value = "用户侧状态(1 保存  2受理中 3撤回 4驳回(市级) 5申诉成功(能源局) 6申诉失败(省级或能源局))")
    private String status;

    @TableField(exist = false)
    @ApiModelProperty(value = "市区状态(1 未审批  2审批中 3撤回 4修正完成  5审批通过(能源局) 6审批失败(省级或能源局))")
    private String statusCity;

    @TableField(exist = false)
    @ApiModelProperty(value = "省层次状态(1 未审批  2审批中 3审批失不通过省级或能源局) 4审批通过(能源局))")
    private String statusProvince;

    @TableField(exist = false)
    @ApiModelProperty(value = "能源局状态(1 未审批  2审批不通过(能源局) 3审批通过(能源局))")
    private String statusEnergy;

    @TableField(exist = false)
    @ApiModelProperty(value = "受理公司")
    private String acceptingCompany;


    @TableField(exist = false)
    @ApiModelProperty(value = "用户侧-是否撤回")
    private String whetherToWithdraw;

    @TableField(exist = false)
    @ApiModelProperty(value = "市专责提交时间")
    private String submitCityTime;

    @TableField(exist = false)
    @ApiModelProperty(value = "省专机构提交时间")
    private String submitProvinceTime;

    @TableField(exist = false)
    @ApiModelProperty(value = "能源局提交时间")
    private String submitEnergyTime;

    @TableField(exist = false)
    @ApiModelProperty(value = "用户侧提交时间")
    private String submitUserTime;

    @TableField(exist = false)
    @ApiModelProperty(value = "省侧、能源局-是否可处理")
    private String ifDeaths;

    @TableField(exist = false)
    @ApiModelProperty(value = "用户侧-是否可驳回")
    private String ifTurnDown;


    @TableField(exist = false)
    @ApiModelProperty(value = "用户侧-是否可驳回")
    private String evaluationId;


    @TableField(exist = false)
    private String ed;

    @TableField(exist = false)
    private String deadLineTime2;

    @TableField(exist = false)
    private String allDayTime;
}
