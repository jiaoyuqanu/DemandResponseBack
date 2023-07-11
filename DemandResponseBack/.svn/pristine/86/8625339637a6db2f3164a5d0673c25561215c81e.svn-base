package com.xqxy.dr.modular.newloadmanagement.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("dr_cons_invitation") //表名 用户邀约
public class Drconsinvitation {

    private Long invitationId;//邀约标识
    private String consId;//用户标识
    private Long eventId;//事件标识
    private String baselineCapId;//基线负荷标识
    private LocalDateTime invitationTime;//邀约时间
    private BigDecimal invitationCap;//邀约响应量
    private Integer invitationRound;//邀约轮次
    private Integer invitationNo;//邀约序位
    private LocalDateTime deadlineTime;//反馈截止时间
    private String isParticipate;//是否响应邀约:0 不参与 1 参与
    private BigDecimal replyCap;//反馈响应量
    private BigDecimal replyPrice;//反馈价格
    private LocalDateTime replyTime;//反馈时间
    private String replySource;//反馈途径:1 需求响应web 2 需求响应app
    private String createTime;//创建时间
    private Long createUser;//创建人
    private String updateTime;//更新时间
    private Long updateUser;//更新人
    private String synchToPlan;//是否已经同步到序位表

}
