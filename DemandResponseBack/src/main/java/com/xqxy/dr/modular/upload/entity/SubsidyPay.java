package com.xqxy.dr.modular.upload.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户激励费用发放
 * </p>
 *
 * @author hu xingxing
 * @since 2021-10-20
 */
public class SubsidyPay {


    private Long payId;//发放标识

    private String consId;//用户标识

    private String payNo;//激励费用批号

    private LocalDate beginDate;//开始日期

    private LocalDate endDate;//截至日期

    private BigDecimal subsidyAmount;//激励金额

    private Integer particNum;//参与次数

    private String payStatus;//发放状态: 0 待发放 1 发放中 2 已发放

    private LocalDateTime createTime;//创建时间

    private Long createUser;//创建人

    private LocalDateTime updateTime;//更新时间

    private Long updateUser;//更新人

    public SubsidyPay() {
    }

    public SubsidyPay(Long payId, String consId, String payNo, LocalDate beginDate, LocalDate endDate, BigDecimal subsidyAmount, Integer particNum, String payStatus, LocalDateTime createTime, Long createUser, LocalDateTime updateTime, Long updateUser) {
        this.payId = payId;
        this.consId = consId;
        this.payNo = payNo;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.subsidyAmount = subsidyAmount;
        this.particNum = particNum;
        this.payStatus = payStatus;
        this.createTime = createTime;
        this.createUser = createUser;
        this.updateTime = updateTime;
        this.updateUser = updateUser;
    }

    public Long getPayId() {
        return payId;
    }

    public void setPayId(Long payId) {
        this.payId = payId;
    }

    public String getConsId() {
        return consId;
    }

    public void setConsId(String consId) {
        this.consId = consId;
    }

    public String getPayNo() {
        return payNo;
    }

    public void setPayNo(String payNo) {
        this.payNo = payNo;
    }

    public LocalDate getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(LocalDate beginDate) {
        this.beginDate = beginDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getSubsidyAmount() {
        return subsidyAmount;
    }

    public void setSubsidyAmount(BigDecimal subsidyAmount) {
        this.subsidyAmount = subsidyAmount;
    }

    public Integer getParticNum() {
        return particNum;
    }

    public void setParticNum(Integer particNum) {
        this.particNum = particNum;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public Long getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public Long getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(Long updateUser) {
        this.updateUser = updateUser;
    }
}
