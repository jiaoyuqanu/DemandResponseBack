package com.xqxy.dr.modular.subsidy.result;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class MonthlySubsidyInfo implements Serializable {

    private String subsidyId;
    private String custId;
    private String consId;
    private String consName;
    private String tel;
    private String subsidyMonth;
    private BigDecimal settledAmount;
    private String status;
}
