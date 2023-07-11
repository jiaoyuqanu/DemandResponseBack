package com.xqxy.dr.modular.newloadmanagement.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("dr_cons_evaluation") //表名 用户执行效果评估
public class Drconsevaluation {
    private Long evaluationId;//评估标识

    private Long eventId;//事件标识

    private String consId;//用户标识

    private BigDecimal invitationCap;//邀约响应量

    private BigDecimal replyCap;//反馈响应量

    private BigDecimal maxLoadBaseline;//基线最大负荷

    private BigDecimal minLoadBaseline;//基线最小负荷

    private BigDecimal avgLoadBaseline;//基线平均负荷

    private BigDecimal maxLoadActual;//实际最大负荷

    private BigDecimal minLoadActual;//实际最小负荷

    private BigDecimal avgLoadActual;//实际平均负荷

    private BigDecimal actualCap;//实际响应负荷

    private BigDecimal actualEnergy;//实际响应电量

    private BigDecimal confirmCap;//核定响应负荷

    private String isEffective;//是否有效:N 无效；Y 有效

    private Integer effectiveTime;//有效响应时长(**分钟)

    private String isQualified;//是否合格(平均负荷)

    private String isOut;//是否越界(最大负荷)

    private String createTime;//创建时间

    private Long createUser;//创建人

    private String updateTime;//更新时间

    private Long updateUser;//更新人


}
