package com.xqxy.dr.modular.upload.entity;


import java.math.BigDecimal;

/**
 * <p>
 * 方案参与用户
 * </p>
 *
 * @author PengChuqing
 * @since 2021-10-09
 */

public class PlanCons {

    private Long particId;//参与标识

    private String consId;//用户标识

    private Long planId;//方案标识

    private Long baselineCapId;//基线负荷标识

    private BigDecimal demandCap;//可响应负荷

    private BigDecimal replyPrice;//响应价格

    private Integer sequenceNo;//方案序位

    private String deleted;//是否剔除

    private String delRule;//被剔除使用的规则

    private String createTime;//创建时间

    private Long createUser;//创建人

    private String updateTime;//更新时间

    private Long updateUser;//更新人

    private String involvedIn;//是否参与事件

    public PlanCons() {
    }

    public PlanCons(Long particId, String consId, Long planId, Long baselineCapId, BigDecimal demandCap, BigDecimal replyPrice, Integer sequenceNo, String deleted, String delRule, String createTime, Long createUser, String updateTime, Long updateUser, String involvedIn) {
        this.particId = particId;
        this.consId = consId;
        this.planId = planId;
        this.baselineCapId = baselineCapId;
        this.demandCap = demandCap;
        this.replyPrice = replyPrice;
        this.sequenceNo = sequenceNo;
        this.deleted = deleted;
        this.delRule = delRule;
        this.createTime = createTime;
        this.createUser = createUser;
        this.updateTime = updateTime;
        this.updateUser = updateUser;
        this.involvedIn = involvedIn;
    }

    public Long getParticId() {
        return particId;
    }

    public void setParticId(Long particId) {
        this.particId = particId;
    }

    public String getConsId() {
        return consId;
    }

    public void setConsId(String consId) {
        this.consId = consId;
    }

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public Long getBaselineCapId() {
        return baselineCapId;
    }

    public void setBaselineCapId(Long baselineCapId) {
        this.baselineCapId = baselineCapId;
    }

    public BigDecimal getDemandCap() {
        return demandCap;
    }

    public void setDemandCap(BigDecimal demandCap) {
        this.demandCap = demandCap;
    }

    public BigDecimal getReplyPrice() {
        return replyPrice;
    }

    public void setReplyPrice(BigDecimal replyPrice) {
        this.replyPrice = replyPrice;
    }

    public Integer getSequenceNo() {
        return sequenceNo;
    }

    public void setSequenceNo(Integer sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public String getDelRule() {
        return delRule;
    }

    public void setDelRule(String delRule) {
        this.delRule = delRule;
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

    public String getInvolvedIn() {
        return involvedIn;
    }

    public void setInvolvedIn(String involvedIn) {
        this.involvedIn = involvedIn;
    }
}
