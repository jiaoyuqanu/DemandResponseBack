package com.xqxy.dr.modular.workbench.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 签约用户资源
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Contracts implements Serializable {

    /**
     * 供电单位
     */
    private String orgNo;
    /**
     * 申报标识
     */
    private String contractId;
    /**
     * 响应类型 1削峰，2填谷
     */
    private String responseType;
    /**
     * 时间类型 1邀约，2实时
     */
    private String timeType;
    /**
     * 提前通知时间 单位分钟
     */
    private String advanceNoticeTime;
    /**
     * 签约容量（kW）
     */
    private BigDecimal contractCap;

    /**
     * 供电单位名称
     */
    private String orgName;
    /**
     * 签约容量总数（万千瓦）
     */
    private BigDecimal contractNum;

    /**
     * 单位对应的区域编码
     */
    private String regionId;

}
