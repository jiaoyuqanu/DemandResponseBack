package com.xqxy.dr.modular.subsidy.result;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 省市下的用户补偿统计
 */
@Data
public class RegionConsSubsidyInfo implements Serializable {

    /**
     * 地区名称
     */
    private String regionName;
    /**
     * 市码:用户地址市码，说明用户地址属于哪个市(地区),,引用国家标准GB T 2260-2002
     */
    private String regionCode;
    /**
     * 省市下总的补偿金额
     */
    private String inAllAmount;
    /**
     * 省市下直接参与用户的补偿金额
     */
    private BigDecimal directAmount;
    /**
     * 省市下代理参与用户的补偿金额
     */
    private BigDecimal agencyAmount;

    /**
     * 省市下结算金额
     */
    private BigDecimal settledAmount;
    /**
     * 需求响应用户分类:2为直接参与用户，3为代理参与用户
     */
    private String consType;
    /**
     * 用户id
     */
    private String consId;
    /**
     * 用户名称
     */
    private String consName;

    /**
     * 补贴日期
     */
    private LocalDate subsidyDate;

    /**
     * 时段，（比如：12:30-02:30）格式的数据
     */
    private String timeInterval;
}
