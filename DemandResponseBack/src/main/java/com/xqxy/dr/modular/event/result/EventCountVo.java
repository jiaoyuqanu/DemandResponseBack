package com.xqxy.dr.modular.event.result;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class EventCountVo {
    private Integer consCount;
    private Integer demandConsCount;
    private BigDecimal demandCap;
}
