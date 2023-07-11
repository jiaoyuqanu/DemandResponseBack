package com.xqxy.dr.modular.statistics.result;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class TotalStatisticsResult implements Serializable {

    private static final long serialVersionUID=1L;

    //累计压降负荷
    private BigDecimal actualCapSum;
    //最大压降负荷
    private BigDecimal actualCapMax;
    //累计影响电量
    private BigDecimal actualEnergySum;
    //最大影响电量
    private BigDecimal actualEnergyMax;
    //累计执行户次
    private Integer consCount;
    //累计执行天数
    private Integer eventDays;


}
