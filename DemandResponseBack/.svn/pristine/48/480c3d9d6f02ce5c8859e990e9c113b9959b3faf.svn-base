package com.xqxy.dr.modular.event.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.xqxy.core.pojo.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 需求响应方案
 * </p>
 *
 * @author PengChuqing
 * @since 2021-10-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dr_plan")
public class Plan  extends BaseEntity implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 方案标识
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long planId;

    /**
     * 需求标识
     */
    @TableField("REGULATE_ID")
    private Long regulateId;

    /**
     * 方案编号
     */
    @TableField("PLAN_NO")
    private String planNo;

    /**
     * 方案名称
     */
    @TableField("PLAN_NAME")
    private String planName;

    /**
     * 编制单位名称
     */
    @TableField("CREATE_ORG_NAME")
    private String createOrgName;

    /**
     * 编制单位编号
     */
    @TableField("CREATE_ORG")
    private String createOrg;

    /**
     * 响应类型1削峰，2填谷
     */
    @TableField("RESPONSE_TYPE")
    private String responseType;

    /**
     * 时间类型 1邀约，2实时
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
    private LocalTime startTime;

    /**
     * 响应结束时间
     */
    @TableField("END_TIME")
    private LocalTime endTime;

    /**
     * 提前通知时间
     */
    private String advanceNoticeTime;

    /**
     * 剔除规则
     */
    private String delRule;

    /**
     * 1保存，2生效
     */
    @TableField("STATE")
    private String state;

    /**
     * 审核状态
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

    /**
     * 基线库标识
     */
    private Long baselinId;




}
