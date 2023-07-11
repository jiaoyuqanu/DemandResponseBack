package com.xqxy.dr.modular.event.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class DrConsCountEntity implements Serializable {
    private static final long serialVersionUID = -97254174736916732L;
    private Long eventId;//事件id

    private BigDecimal inv;//应邀户数

    private BigDecimal eva;//成功响应户数

    private BigDecimal per1;//用户成功率

    private BigDecimal replyCap;//应邀响应负荷

    private BigDecimal actualCap;//实际响应负荷

    private BigDecimal per2;//响应成功率

    private Long cityCode;//城市code

    private String cityName;//城市名称


}
