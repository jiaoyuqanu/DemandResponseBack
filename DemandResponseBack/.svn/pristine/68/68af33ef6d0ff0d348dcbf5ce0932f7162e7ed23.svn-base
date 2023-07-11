package com.xqxy.dr.modular.workbench.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.io.Serializable;

/**
 * 容量补偿（工作台）
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReserveSubsidy implements Serializable {

    /**
     * 项目id
     */
    private Long projectId;
    /**
     * 参与主体类型
     */
    private String participantType;
    /**
     * 是否集成商 1是 0否
     */
    private String integrator;
    /**
     * 金额
     */
    private Double subsidyAmount;

    /**
     * 总金额
     */
    private Double totalAmount;
    /**
     * 负荷聚合商补偿金额
     */
    private Double aggregator;
    /**
     * 独立参与用户补偿金额
     */
    private Double independent;
    /**
     * 代理参与用户金额
     */
    private Double proxy;
}
