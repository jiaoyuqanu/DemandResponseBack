package com.xqxy.dr.modular.event.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xqxy.core.pojo.base.entity.BaseEntity;
import com.xqxy.sys.modular.cust.entity.Cons;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户邀约
 * </p>
 *
 * @author PengChuqing
 * @since 2021-10-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dr_cons_invitation")
public class ConsInvitation  extends BaseEntity implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 邀约标识
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long invitationId;

    /**
     * 用户标识
     */
    @TableField("CONS_ID")
    private String consId;

    /**
     * 事件标识
     */
    @TableField("EVENT_ID")
    private Long eventId;

    /**
     * 基线负荷标识
     */
    private Long baselineCapId;

    /**
     * 邀约时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime invitationTime;

    /**
     * 邀约响应量
     */
    private BigDecimal invitationCap;

    /**
     * 邀约轮次
     */
    private Integer invitationRound;

    /**
     * 邀约序位
     */
    private Integer invitationNo;

    /**
     * 反馈截止时间
     */
    private LocalDateTime deadlineTime;

    /**
     * 是否响应邀约:0 不参与 1 参与
     */
    private String isParticipate;

    /**
     * 是否响应邀约:0 不参与 1 参与
     */
    private String isReply;
    /**
     * 未反馈原因
     */
    private String noReplyReason;

    /**
     * 反馈响应量
     */
    private BigDecimal replyCap;

    /**
     * 反馈价格
     */
    private BigDecimal replyPrice;

    /**
     * 反馈时间
     */
    private LocalDateTime replyTime;

    /**
     * 反馈途径:1 需求响应web 2 需求响应app
     */
    private String replySource;

    /**
     * 用户基线信息
     */
    @TableField(exist = false)
    private Cons consInfo;

    /**
     * 集成商名称
     */
    @TableField(exist = false)
    private String aggreName;

    @TableField(exist = false)
    private String consName;

    @TableField(exist = false)
    private String elecAddr;

    @TableField(exist = false)
    private String bigTradeName;

    @TableField(exist = false)
    private String bigTradeCode;

    @TableField(exist = false)
    private String dayMaxPower;

    @TableField(exist = false)
    private String contractCap;

    @TableField(exist = false)
    private BigDecimal contractCapData;

    @TableField(exist = false)
    private String runCap;

    @TableField(exist = false)
    private String orgName;

    @TableField(exist = false)
    private String orgNo;

    @TableField(exist = false)
    private String provinceCode;

    @TableField(exist = false)
    private String cityCode;

    @TableField(exist = false)
    private String countyCode;

    @TableField(exist = false)
    private String firstContactName;

    @TableField(exist = false)
    private String firstContactInfo;

    @TableField(exist = false)
    private Event event;

    @TableField(exist = false)
    private Cons cons;

    /**
     * 同步方案标识
     */
    @TableField("SYNCH_TO_PLAN")
    private String synchToPlan;

    @ApiModelProperty(value = "用户参与方式")
    @TableField("join_user_type")
    private String joinUserType;


}
