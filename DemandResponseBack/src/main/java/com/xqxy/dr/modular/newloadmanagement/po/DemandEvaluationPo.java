package com.xqxy.dr.modular.newloadmanagement.po;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DemandEvaluationPo {

    private String eventId;
    private String regulateDate;
    private String startTime;
    private String endTime;
    private BigDecimal regulateCap;
    private BigDecimal cbfCap;
    private Integer consSize;

}
