package com.xqxy.dr.modular.newloadmanagement.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xqxy.dr.modular.upload.entity.ConsInvitation;
import com.xqxy.dr.modular.upload.entity.ConsSubsidy;
import com.xqxy.dr.modular.upload.entity.Consevaluation;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;


@Data
@TableName("dr_event") //表名 需求响应事件
public class Drevent {

    private Long eventId;//事件标识

    private Long regulateId;//方案标识

    private Long projectId;//项目标识

    private Long eventNo;//事件编号

    private String eventName;//事件名称

    private String responseType;//响应类型

    private String timeType;//时间类型

    private String rangeType;//调控范围类别：地区/分区/变电站/线路/台区

    private String regulateRange;//调控范围

    private BigDecimal regulateCap;//调控目标

    private LocalDate regulateDate;//调控日期

    private String startTime;//响应开始时间

    private String endTime;//响应结束时间

    private String advanceNoticeTime;//提前通知时间

    private String eventStatus;//事件状态 1:保存，2待执行，3执行中，4结束，5废弃

    private String checkStatus;//审核状态 1:未提交，2：审核中，3：审核通过，4：审核不通过

    private String particiCondition;//参与条件  [{type:1,value:1,2,3,4},....]

    private String incentiveStandard;//激励标准 格式：1，2，3

    private String validityJudgment;//有效性判定,格式：1，2，3

    private String baseLineCal;//基线计算准则 格式：1，2，3

    private Long baselinId;//基线库标识

    private Drdemand drdemand;

    private  Drconsevaluation drconsevaluation;
    private int planQuantity;//计划执行户数
    private int executeQuantity;//执行到位户数
    private int noExecuteQuantity;//未执行到位户数
    private BigDecimal maxLoadBaseline;//基线最大负荷
    private BigDecimal avgLoadBaseline;//基线平均负荷
    private BigDecimal actualCap;//实际响应负荷
    private BigDecimal confirmCap;//核定响应负荷
    private BigDecimal replyCap;//反馈响应量
    private String isEffective;//是否有效:N 无效；Y 有效
    private String createTime;//创建时间
    private Long createUser;//创建人
    private String updateTime;//更新时间
    private Long updateUser;//更新人

    private Drconsinvitation drconsinvitation;
    private String consId;//用户标识
    private BigDecimal replyPrice;//反馈价格
    private BigDecimal invitationCap;//邀约响应量

}
