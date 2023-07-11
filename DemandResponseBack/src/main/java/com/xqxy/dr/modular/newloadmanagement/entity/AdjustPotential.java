package com.xqxy.dr.modular.newloadmanagement.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AdjustPotential {
    public BigDecimal contractCap;//签约容量
    public BigDecimal drRtLoad;
}
