package com.xqxy.dr.modular.subsidy.result;

import lombok.Data;
import org.apache.poi.hpsf.Decimal;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class MySubsidyInfo implements Serializable {

    //事件编号，事件名称，补贴日期，客户名称，统一社会信用代码，结算金额（元）
    private Long eventId;

    private String eventNo;

    private String eventName;

    private LocalDate subsidyDate;

    private String legalName;

    private String creditCode;

    private String subsidyAmount;

    private String subsidyPub;
}
