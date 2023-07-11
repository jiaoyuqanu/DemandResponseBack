package com.xqxy.dr.modular.event.VO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.xqxy.core.pojo.base.entity.BaseEntity;
import com.xqxy.dr.modular.event.entity.ConsBaseline;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
public class EventVO extends BaseEntity implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 事件标识
     */
    private Long eventId;

    /**
     * 调度指令标识
     */
    private Long regulateId;

    /**
     * 项目标识
     */
    private Long projectId;

    /**
     * 事件编号
     */
    private Long eventNo;

    /**
     * 事件名称
     */
    private String eventName;

    /**
     * 响应类型
     */
    private String responseType;

    /**
     * 时间类型
     */
    private String timeType;

    /**
     * 调控范围类别：地区/分区/变电站/线路/台区
     */
    private String rangeType;

    /**
     * 调控范围
     */
    private String regulateRange;

    /**
     * 调控目标
     */
    private BigDecimal regulateCap;

    /**
     * 调控目标的百分比， 不能超过1.5倍
     */
    private BigDecimal regulateMultiple;

    /**
     * 调控日期
     */
    private Date regulateDate;

    /**
     * 响应开始时间
     */
    private String startTime;

    /**
     * 响应结束时间
     */
    private String endTime;

    /**
     * 即不符合要求截止条件 1.截止日期 2. 反馈目标*反馈倍数 3.或者满足任意条件，即不符合要求
     */
    private Integer endCondition;

    /**
     * 提前通知时间
     */
    private String advanceNoticeTime;

    /**
     * 事件状态 1:保存，2待执行，3执行中，4结束，5废弃, 6事件已发布,7撤销
     */
    private String eventStatus;

    /**
     * 审核状态 1:未提交，2：审核中，3：审核通过，4：审核不通过
     */
    private String checkStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 更新人
     */
    private Long updateUser;

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
     * 是否已经同步到序位表
     */
    private String synchToPlan;

    /**
     * 基线库标识
     */
    private Long baselinId;

    /**
     *  从邀约表 sum 应邀反馈容量
     */
    private BigDecimal replyCap;

    /**
     *  从邀约表 count 应邀反馈人数
     */
    private Integer invitationCount;

    /**
     *  从效果评估表 sum 实际响应负荷
     */
    private BigDecimal actualCap;

    /**
     *  从效果评估表 count 应邀反馈人数
     */
    private Integer evaluationCount;

    /**
     *  统计各个事件的数量
     */
    private Integer count;
    /**
     * 目标区域（调控范围）
     */
    @ApiModelProperty(value = "目标区域数组中文")
    private String regulateRangeStr;

    @ApiModelProperty(value = "目标区域中文")
    private String regulateRangeStrText;

    /**
     * 目标区域（调控范围）
     */
    @ApiModelProperty(value = "目标区域数组英文")
    private List<List<String>> regulateRangeList;
}
