package com.xqxy.dr.modular.workbench.VO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ContractDetailVO {
    /**
     * 用户编号
     */
    private String consId;
    /**
     * 用户名称
     */
    private String consName;
    /**
     * 日前约时削峰响应负荷
     */
    private BigDecimal dayInvtionPeakContractCap;
    /**
     * 日前约时削峰响应最小时长
     */
    private Integer dayInvtionPeakMinTimes;
    /**
     * 小时级约时削峰响应负荷
     */
    private BigDecimal hourInvtionPeakContractCap;
    /**
     * 小时级约时削峰响应最小时长
     */
    private Integer hourInvtionPeakMinTimes;
    /**
     * 分钟级实时削峰响应负荷
     */
    private BigDecimal minuteInvtionPeakContractCap;
    /**
     * 分钟级实时削峰响应最小时长
     */
    private Integer minuteInvtionPeakMinTimes;
    /**
     * 秒级实时削峰响应负荷
     */
    private BigDecimal secondInvtionPeakContractCap;
    /**
     * 秒级实时削峰响应最小时长
     */
    private Integer secondInvtionPeakMinTimes;
    /**
     * 日前约时填谷响应负荷
     */
    private BigDecimal dayInvtionValleyContractCap;
    /**
     * 日前约时填谷响应最小时长
     */
    private Integer dayInvtionValleyMinTimes;
    /**
     * 小时级约时填谷响应响应负荷
     *//*
    private BigDecimal hourInvtionValleyContractCap;
    *//**
     * 小时级约时填谷响应最小时长
     *//*
    private Integer hourInvtionValleyMinTimes;
    *//**
     * 分钟级约时填谷响应负荷
     *//*
    private BigDecimal minuteInvtionValleyContractCap;
    *//**
     * 分钟级约时填谷响应最小时长
     *//*
    private Integer minuteInvtionValleyMinTimes;
    *//**
     * 秒级约时填谷响应负荷
     *//*
    private BigDecimal secondInvtionValleyContractCap;
    *//**
     * 秒级约时填谷响应最小时长
     *//*
    private Integer secondInvtionValleyMinTimes;*/
}
