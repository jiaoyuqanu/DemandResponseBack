package com.xqxy.dr.modular.event.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 用户邀约信息(DrEventInvitation)实体类
 *
 * @author makejava
 * @since 2021-07-14 21:37:47
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dr_event_invitation")
public class DrEventInvitation implements Serializable {
    private static final long serialVersionUID = -97254174736916727L;
    /**
     * 本实体记录的唯一标识，产生规则为流水号
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long invitationId;
    /**
     * 关联事件ID
     */
    private Long eventId;
    /**
     * 关联用户ID
     */
    private String consId;
    /**
     * 邀约时间
     */
    private Date invitationTime;
    /**
     * 基线最大负荷
     */
    private Double maxLoadBaseline;
    /**
     * 基线最小负荷
     */
    private Double minLoadBaseline;
    /**
     * 基线平均负荷
     */
    private Double avgLoadBaseline;
    /**
     * 受邀响应量
     */
    private Double invitationCap;
    /**
     * 补偿价格
     */
    private Double subsidyPrice;
    /**
     * 反馈截止时间
     */
    private Date deadlineTime;
    /**
     * 是否响应邀约:0 不参与 1 参与
     */
    private String isParticipate;
    /**
     * 反馈响应量
     */
    private Double replyCap;
    /**
     * 反馈时间
     */
    private Date replyTime;
    /**
     * 反馈途径:1 需求响应web 2 需求响应app
     */
    private String replySource;
    /**
     * 邀约轮次
     */
    private Integer invitationRound;
    /**
     * 邀约序位
     */
    private Long invitationSequence;
    /**
     * 异常描述
     */
    private String exceptionRemark;
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

    private Date calTime;

    private String calStatus;

    private String calException;
    /**
     * 评价计算时间
     */
    private Date evalTime;
    /**
     * 评价计算状态：0 失败 1 成功
     */
    private String evalStatus;
    /**
     * 评价计算异常信息
     */
    private String evalException;
    /**
     * 实时评价计算时间
     */
    private Date immediateEvalTime;
    /**
     * 实时评价计算状态：0 失败 1 成功
     */
    private String immediateEvalStatus;
    /**
     * 实时评价计算异常信息
     */
    private String immediateEvalException;
    /**
     * 补贴计算时间
     */
    private Date subTime;
    /**
     * 补贴计算状态：0 失败 1 成功
     */
    private String subStatus;
    /**
     * 补贴计算异常信息
     */
    private String subException;

    /**
     * 地区
     */
    private String regionName;
    /**
     * 应邀总户数
     */
    private BigDecimal yyzhs;
    /**
     * 成功响应总户数
     */
    private BigDecimal cgxyzhs;
    /**
     * 用户成功率
     */
    private BigDecimal yhcgl;
    /**
     * 应邀响应负荷
     */
    private BigDecimal yyxyfh;
    /**
     * 响应负荷
     */
    private BigDecimal xyfh;
    /**
     * 响应成功率
     */
    private BigDecimal xycgl;

    private String startDate;
    private String startTime;
    private String endtTime;
    private String endtDate;

    private String periodTimeId;
    private List<DrEventInvitationMap> list;
}
