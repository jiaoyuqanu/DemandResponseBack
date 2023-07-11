package com.xqxy.dr.modular.event.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.xqxy.core.pojo.base.entity.BaseEntity;
import com.xqxy.sys.modular.cust.entity.Cust;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 客户邀约
 * </p>
 *
 * @author PengChuqing
 * @since 2021-10-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dr_cust_invitation")
public class CustInvitation  extends BaseEntity implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 客户邀约标识
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long invitationId;

    /**
     * 事件标识
     */
    @TableField("EVENT_ID")
    private Long eventId;

    /**
     * 本实体记录的唯一标识，产生规则为流水号
     */
    @TableField("CUST_ID")
    private Long custId;

    /**
     * 是否集成商
     */
    private String integrator;

    /**
     * 邀约时间
     */
    private LocalDateTime invitationTime;

    /**
     * 是否响应邀约:0 不参与 1 参与
     */
    private String isReply;
    /**
     * 未反馈原因
     */
    private String noReplyReason;
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

    private BigDecimal replyPrice;

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
     * 注册手机号
     */
    @TableField(exist = false)
    private String tel;

    /**
     * 统一社会信用代码证就是三证合一。 三证合一,就是把营业执照、税务登记证和组织机构代码证这三个证件合三为一
     */
    @TableField(exist = false)
    private String creditCode;

    /**
     * 法定代表人姓名
     */
    @TableField(exist = false)
    private String legalName;

    /**
     * 法定代表人号码
     */
    @TableField(exist = false)
    private String legalNo;

    /**
     * 1身份证，2护照
     */
    @TableField(exist = false)
    private String legalCardType;

    /**
     * 经办人证件号码
     */
    @TableField(exist = false)
    private String applyNo;

    /**
     * 1身份证，2护照
     */
    @TableField(exist = false)
    private String applyCardType;

    /**
     * 经办人
     */
    @TableField(exist = false)
    private String applyName;

    /**
     * 客户的编号（保留）
     */
    @TableField(exist = false)
    private String custNo;

    /**
     * 客户的名称（保留）
     */
    @TableField(exist = false)
    private String custName;

    /**
     * 省码（保留）
     */
    @TableField(exist = false)
    private String provinceCode;

    /**
     * 市码（保留）
     */
    @TableField(exist = false)
    private String cityCode;

    /**
     * 区县码（保留）
     */
    @TableField(exist = false)
    private String countyCode;

    /**
     * 街道码（乡镇）（保留）
     */
    @TableField(exist = false)
    private String streetCode;

    /**
     * 客户地址（保留）
     */
    @TableField(exist = false)
    private String custAddr;

    /**
     * 1:未提交，2：审核中，3：审核通过，4：审核不通过
     */
    @TableField(exist = false)
    private String checkStatus;

    /**
     * 1:未认证，2:已认证，3：撤销，4：认证失败
     */
    @TableField(exist = false)
    private String state;

    /**
     * 同步方案标识
     */
    @TableField("SYNCH_TO_PLAN")
    private String synchToPlan;


}
