package com.xqxy.dr.modular.event.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class DrEventInvitationEntity implements Serializable {
    private static final long serialVersionUID = -97254174736916723L;

    /**
     * 市码
     */
    private String cityCode;

    /**
     * 区县码
     */
    private String countyCode;

    /**
     * 地市
     */
    private String cityName;

    private String countyName;//区县

    private String consType;//参与方式

    private String consNo;//用户编号

    private String consName; //用户名称

    private BigDecimal replyCap;//响应负荷确认值

    private BigDecimal avgLoadBaseline;//平均基线负荷
    private BigDecimal maxLoadBaseline;//最大基线负荷

    private BigDecimal avgLoadActual;//实际平均值

    private BigDecimal maxLoadActual;//实际最大负荷

    private BigDecimal min; //最大负荷与最大基线负荷差值

    private BigDecimal actualCap;//实际响应负荷

    private String isEffective;//是否有效


}
