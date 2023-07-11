package com.xqxy.dr.modular.upload.entity;

import java.math.BigDecimal;

public class ConsSubsidy {
    private Long subsidyId;//补贴标识

    private String consId;//用户标识

    private Long eventId;//事件标识

    private BigDecimal actualCap;//实际响应负荷

    private BigDecimal actualEnergy;//实际响应电量

    private BigDecimal contractPrice;//签约价格

    private BigDecimal subsidyAmount;//激励金额

    private String calRule;//计算规则，1，2，3

    private String createTime;//创建时间

    private Long createUser;//创建人

    private String updateTime;//更新时间

    private Long updateUser;//更新人

    private String remark;//异常描述

    private BigDecimal settledAmount;//结算金额

    public ConsSubsidy() {
    }

    public ConsSubsidy(Long subsidyId, String consId, Long eventId, BigDecimal actualCap, BigDecimal actualEnergy, BigDecimal contractPrice, BigDecimal subsidyAmount, String calRule, String createTime, Long createUser, String updateTime, Long updateUser, String remark, BigDecimal settledAmount) {
        this.subsidyId = subsidyId;
        this.consId = consId;
        this.eventId = eventId;
        this.actualCap = actualCap;
        this.actualEnergy = actualEnergy;
        this.contractPrice = contractPrice;
        this.subsidyAmount = subsidyAmount;
        this.calRule = calRule;
        this.createTime = createTime;
        this.createUser = createUser;
        this.updateTime = updateTime;
        this.updateUser = updateUser;
        this.remark = remark;
        this.settledAmount = settledAmount;
    }

    public Long getSubsidyId() {
        return subsidyId;
    }

    public void setSubsidyId(Long subsidyId) {
        this.subsidyId = subsidyId;
    }

    public String getConsId() {
        return consId;
    }

    public void setConsId(String consId) {
        this.consId = consId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public BigDecimal getActualCap() {
        return actualCap;
    }

    public void setActualCap(BigDecimal actualCap) {
        this.actualCap = actualCap;
    }

    public BigDecimal getActualEnergy() {
        return actualEnergy;
    }

    public void setActualEnergy(BigDecimal actualEnergy) {
        this.actualEnergy = actualEnergy;
    }

    public BigDecimal getContractPrice() {
        return contractPrice;
    }

    public void setContractPrice(BigDecimal contractPrice) {
        this.contractPrice = contractPrice;
    }

    public BigDecimal getSubsidyAmount() {
        return subsidyAmount;
    }

    public void setSubsidyAmount(BigDecimal subsidyAmount) {
        this.subsidyAmount = subsidyAmount;
    }

    public String getCalRule() {
        return calRule;
    }

    public void setCalRule(String calRule) {
        this.calRule = calRule;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Long getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Long getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(Long updateUser) {
        this.updateUser = updateUser;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public BigDecimal getSettledAmount() {
        return settledAmount;
    }

    public void setSettledAmount(BigDecimal settledAmount) {
        this.settledAmount = settledAmount;
    }
}
