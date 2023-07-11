package com.xqxy.dr.modular.event.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 事件效果评估表实体类
 *
 */
@Data
public class DrEventInvitationEffectEvalDetail implements Serializable {

    private static final long serialVersionUID = -6903411437657923665L;
    /**
     * 事件ID
     */
    private Long eventId;
    /**
     * 地市
     */
    private String cityName;
    /**
     * 区县
     */
    private String countyName;
    /**
     * 参与方式
     */
    private String consType;
    /**
     * 用户编号
     */
    private String consNo;
    /**
     * 用户名称
     */
    private String consName;
    /**
     * 响应负荷确认值
     */
    private BigDecimal replyCap;
    /**
     * 平均基线负荷
     */
    private BigDecimal avgLoadBaseline;
    /**
     * 最大基线负荷
     */
    private BigDecimal maxLoadBaseline;
    /**
     * 实际平均负荷
     */
    private BigDecimal avgLoadActual;
    /**
     * 实际最大负荷
     */
    private BigDecimal maxLoadActual;
    /**
     * 最大基线负荷与实际最大负荷差值(kW)
     */
    private BigDecimal min;
    /**
     * 实际响应负荷
     */
    private BigDecimal actualCap;
    /**
     * 是否有效
     */
    private String isEffective;
}
