package com.xqxy.dr.modular.subsidy.result;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class CustSubsidyMonthlyInfo implements Serializable {

    private Long custId;
    private String consId;
    private String integrator;
    private BigDecimal settledAmount;
}
