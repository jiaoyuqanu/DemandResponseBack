package com.xqxy.dr.modular.dispatch.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xqxy.core.annotion.NeedSetValue;
import com.xqxy.core.pojo.base.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 调度需求响应指令
 * </p>
 *
 * @author chen zhi jun
 * @since 2021-10-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dr_demand")
public class Dispatch extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 本实体记录的唯一标识，产生规则为流水号
     */
    @ApiModelProperty(value = "需求标识id")
    @TableId(type = IdType.ASSIGN_ID)
    private Long regulateId;

    /**
     * 指令编号:yyyy-mm-dd-00
     */
    @ApiModelProperty(value = "负荷调度编号")
    private String regulateNo;

    /**
     * 负荷调度日期
     */
    @ApiModelProperty(value = "负荷调度日期")
    @TableField("REGULATE_DATE")
    private LocalDate regulateDate;

    /**
     * 区域类型（调控范围类别）
     */
    @ApiModelProperty(value = "区域类型")
    @TableField("RANGE_TYPE")
    private String rangeType;

    /**
     * 目标区域（调控范围）
     */
    @ApiModelProperty(value = "目标区域")
    @TableField("REGULATE_RANGE")
    private String regulateRange;

    /**
     * 所属项目
     */
    @ApiModelProperty(value = "所属项目")
    @TableField("project_id")
    private Long projectId;

    /**
     * 时段描述
     */
    @ApiModelProperty(value = "时间类型")
    @TableField("TIME_TYPE")
    private String periodDetail;

    /**
     * 响应开始时段:
     */
    @ApiModelProperty(value = "开始时间")
    @TableField("START_TIME")
    private String startTime;

    /**
     * 响应结束时段:
     */
    @ApiModelProperty(value = "结束时间")
    @TableField("END_TIME")
    private String endTime;

    /**
     * 响应类型
     */
    @ApiModelProperty(value = "调节类型")
    @TableField("RESPONSE_TYPE")
    private String responseType;

    /**
     * 调节目标值
     */
    @ApiModelProperty(value = "预测供电缺口")
    @TableField("REGULATE_CAP")
    private BigDecimal regulateCap;

    /**
     * 状态(需求状态01保存02事件已生成03指令已下发)
     */
    @ApiModelProperty(value = "需求状态01保存02事件已生成03指令已下发04超时")
    @TableField("status")
    private String status;

   /* *//**
     * 发起人姓名
     *//*
    @TableField(exist = false)
    @NeedSetValue(beanClass = com.xqxy.sys.modular.user.service.SysUserService.class, method = "getCreateNameByID", params = {"createUser"}, targetField = "createUserName")
    private String createUserName;*/

    /**
     * 提前通知时间(分钟)
     */
    @ApiModelProperty(value = "提前通知时间(分钟)")
    @TableField("ADVANCE_NOTICE_TIME")
    private BigDecimal advanceNoticeTime;


    /**
     * 发起人姓名
     */
    @ApiModelProperty(value = "发起人姓名")
    @TableField("CREATE_USER_NAME")
    private String createUserName;

    /**
     * 项目名称
     */
    @ApiModelProperty(value = "项目名称")
    @TableField(exist = false)
    private String projectName;

    /**
     * 年份搜索
     */
    @ApiModelProperty(value = "搜索条件年度")
    @TableField(exist = false)
    private String year;

    /**
     * 负荷调度日期
     */
    @ApiModelProperty(value = "负荷调度日期")
    @TableField(exist = false)
    private String regulateDateStr;

    /**
     * 下发时间
     */
    @ApiModelProperty(value = "下发时间")
    @TableField(exist = false)
    private String createTimeStr;

    /**
     * 状态中文
     */
    @ApiModelProperty(value = "状态中文值")
    @TableField(exist = false)
    private String statusStr;

    /**
     * 响应类型中文
     */
    @ApiModelProperty(value = "响应类型中文值")
    @TableField(exist = false)
    private String responseTypeStr;

    /**
     * 目标区域（调控范围）
     */
    @ApiModelProperty(value = "目标区域中文")
    @TableField(exist = false)
    private String regulateRangeStr;

    /**
     * 目标区域（调控范围）
     */
    @ApiModelProperty(value = "目标区域数组英文")
    @TableField(exist = false)
    private List<List<String>> regulateRangeList;

    /**
     * 事件标识
     */
    @ApiModelProperty(value = "事件标识")
    @TableField(exist = false)
    private String eventId;

    private String stregyId;

}
