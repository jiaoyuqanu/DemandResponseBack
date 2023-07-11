package com.xqxy.dr.modular.subsidy.result;

import lombok.Data;
import org.apache.poi.hpsf.Decimal;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ConsNoSubsidy implements Serializable {

    private String consId;

    private String consName;

    private Long eventId;

    private String eventName;

    private String eventNo;

    private BigDecimal subsidyAmount;

    private String isEffective;

    private BigDecimal actualCap;

    private BigDecimal contractPrice;

    private String regulateDate;

    private String startTime;

    private String endTime;

    private String deadlineTime;

    private String createTime;
}
