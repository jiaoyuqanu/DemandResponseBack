package com.xqxy.dr.modular.event.result;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExecutePlanTotal {

    private Integer count;//执行方案户数总计

    private BigDecimal dirTotal;//直接参与容量总计

    private BigDecimal intTotal;//聚合参与容量总计

    private BigDecimal total;//执行方案容量总计

    private BigDecimal target;//邀约目标

}
