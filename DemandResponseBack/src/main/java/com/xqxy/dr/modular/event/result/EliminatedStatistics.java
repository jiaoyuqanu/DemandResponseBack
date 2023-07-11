package com.xqxy.dr.modular.event.result;

import com.xqxy.dr.modular.event.entity.PlanCons;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Data
public class EliminatedStatistics {

    /**
     * 剔除用户总数
     */
    private Long consNum;

    /**
     * 剔除用户总容量
     */
    private BigDecimal sumDeletedCap;

    /**
     * 剔除用户列表
     */
    private List<PlanCons> planConsList;

}
