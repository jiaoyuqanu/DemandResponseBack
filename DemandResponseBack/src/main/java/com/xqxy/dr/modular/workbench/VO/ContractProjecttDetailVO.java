package com.xqxy.dr.modular.workbench.VO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ContractProjecttDetailVO {

    /**
     * 供电单位编码
     */
    private String orgNo;

    /**
     * 项目详情id
     */
    private String projectDetailId;

    /**
     * 签约容量
     */
    private BigDecimal contractCap;

    /**
     * 签约容量
     */
    private BigDecimal goal;

    /**
     * 审核状态  1:未提交，2：审核中，3：审核通过，4：审核不通过
     */
    private String checkStatus;
}
