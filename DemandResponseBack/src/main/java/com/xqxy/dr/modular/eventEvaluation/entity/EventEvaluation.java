package com.xqxy.dr.modular.eventEvaluation.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xqxy.core.pojo.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 事件执行效果评估
 *
 * @author Rabbit
 * @TableName dr_event_evaluation
 */
@TableName(value = "dr_event_evaluation")
@Data
@ApiModel(description = "事件执行效果评估")
public class EventEvaluation implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 评估标识
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "评估标识")
    private Long id;
    /**
     * 事件标识
     */
    @ApiModelProperty(value = "事件标识")
    private Long eventId;
    /**
     * 邀约响应量
     */
    @ApiModelProperty(value = "邀约响应量")
    private BigDecimal invitationCap;
    /**
     * 反馈响应量
     */
    @ApiModelProperty(value = "反馈响应量")
    private BigDecimal replyCap;
    /**
     * 基线最大负荷
     */
    @ApiModelProperty(value = "基线最大负荷")
    private BigDecimal maxLoadBaseline;
    /**
     * 基线最小负荷
     */
    @ApiModelProperty(value = "基线最小负荷")
    private BigDecimal minLoadBaseline;
    /**
     * 基线平均负荷
     */
    @ApiModelProperty(value = "基线平均负荷")
    private BigDecimal avgLoadBaseline;
    /**
     * 实际最大负荷
     */
    @ApiModelProperty(value = "实际最大负荷")
    private BigDecimal maxLoadActual;
    /**
     * 实际最小负荷
     */
    @ApiModelProperty(value = "实际最小负荷")
    private BigDecimal minLoadActual;
    /**
     * 实际平均负荷
     */
    @ApiModelProperty(value = "实际平均负荷")
    private BigDecimal avgLoadActual;
    /**
     * 实际响应负荷
     */
    @ApiModelProperty(value = "实际响应负荷")
    private BigDecimal actualCap;
    /**
     * 实际响应电量
     */
    @ApiModelProperty(value = "实际响应电量")
    private BigDecimal actualEnergy;
    /**
     * 核定响应负荷
     */
    @ApiModelProperty(value = "核定响应负荷")
    private BigDecimal confirmCap;
    /**
     * 负荷响应率
     */
    @ApiModelProperty(value = "负荷响应率")
    private BigDecimal executeRate;
    /**
     * 是否有效:N 无效；Y 有效
     */
    @ApiModelProperty(value = "是否有效:N 无效；Y 有效")
    private String isEffective;
    /**
     * 有效响应时长(**分钟)
     */
    @ApiModelProperty(value = "有效响应时长(**分钟)")
    private Integer effectiveTime;
    /**
     * 是否合格(平均负荷)
     */
    @ApiModelProperty(value = "是否合格(平均负荷)")
    private String isQualified;
    /**
     * 是否越界(最大负荷)
     */
    @ApiModelProperty(value = "是否越界(最大负荷)")
    private String isOut;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private Long createUser;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人")
    private Long updateUser;
    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private String remark;
    /**
     * 组织机构
     */
    @ApiModelProperty(value = "组织机构")
    private String orgNo;

    /**
     * 最大响应负荷
     */
    @ApiModelProperty(value = "最大响应负荷")
    private BigDecimal actualMaxCap;

    /**
     * 最小响应负荷
     */
    @ApiModelProperty(value = "最小响应负荷")
    private BigDecimal actualMinCap;

    /**
     * 最大响应负荷时间点
     */
    @ApiModelProperty(value = "最大响应负荷时间点")
    private String maxTime;

    /**
     * 最小响应负荷时间点
     */
    @ApiModelProperty(value = "最小响应负荷时间点")
    private String minTime;
}