package com.xqxy.dr.modular.event.param;

import com.baomidou.mybatisplus.annotation.TableField;
import com.xqxy.core.pojo.base.param.BaseParam;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@ApiModel(description = "客户邀约信息 参数")
@EqualsAndHashCode(callSuper = true)
@Data
public class CustInvitationParam  extends BaseParam {

    /**
     * 客户邀约标识
     */
    private Long invitationId;

    /**
     * 事件标识
     */
    private Long eventId;

    /**
     * 本实体记录的唯一标识，产生规则为流水号
     */
    private Long custId;

    /**
     * 是否集成商
     */
    private String integrator;

    private String  creditCode;

    /**
     * 邀约时间
     */
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
     * 反馈响应量
     */
    private BigDecimal replyCap;

    /**
     * 反馈时间
     */
    private LocalDateTime replyTime;

    /**
     * 反馈途径:1 需求响应web 2 需求响应app
     */
    private String replySource;

    /**
     * 客户名称
     */
    private String custName;

    private String isReply;

}
