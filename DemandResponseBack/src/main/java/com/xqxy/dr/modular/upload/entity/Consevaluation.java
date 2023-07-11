package com.xqxy.dr.modular.upload.entity;


import java.math.BigDecimal;

public class Consevaluation {
    private Long evaluationId;//评估标识

    private Long eventId;//事件标识

    private String consId;//用户标识

    private BigDecimal invitationCap;//邀约响应量

    private BigDecimal replyCap;//反馈响应量

    private BigDecimal maxLoadBaseline;//基线最大负荷

    private BigDecimal minLoadBaseline;//基线最小负荷

    private BigDecimal avgLoadBaseline;//基线平均负荷

    private BigDecimal maxLoadActual;//实际最大负荷

    private BigDecimal minLoadActual;//实际最小负荷

    private BigDecimal avgLoadActual;//实际平均负荷

    private BigDecimal actualCap;//实际响应负荷

    private BigDecimal actualEnergy;//实际响应电量

    private BigDecimal confirmCap;//核定响应负荷

    private String isEffective;//是否有效:N 无效；Y 有效

    private Integer effectiveTime;//有效响应时长(**分钟)

    private String isQualified;//是否合格(平均负荷)

    private String isOut;//是否越界(最大负荷)

    private String createTime;//创建时间

    private Long createUser;//创建人

    private String updateTime;//更新时间

    private Long updateUser;//更新人

    public Consevaluation() {
    }

    public Consevaluation(Long evaluationId, Long eventId, String consId, BigDecimal invitationCap, BigDecimal replyCap, BigDecimal maxLoadBaseline, BigDecimal minLoadBaseline, BigDecimal avgLoadBaseline, BigDecimal maxLoadActual, BigDecimal minLoadActual, BigDecimal avgLoadActual, BigDecimal actualCap, BigDecimal actualEnergy, BigDecimal confirmCap, String isEffective, Integer effectiveTime, String isQualified, String isOut, String createTime, Long createUser, String updateTime, Long updateUser) {
        this.evaluationId = evaluationId;
        this.eventId = eventId;
        this.consId = consId;
        this.invitationCap = invitationCap;
        this.replyCap = replyCap;
        this.maxLoadBaseline = maxLoadBaseline;
        this.minLoadBaseline = minLoadBaseline;
        this.avgLoadBaseline = avgLoadBaseline;
        this.maxLoadActual = maxLoadActual;
        this.minLoadActual = minLoadActual;
        this.avgLoadActual = avgLoadActual;
        this.actualCap = actualCap;
        this.actualEnergy = actualEnergy;
        this.confirmCap = confirmCap;
        this.isEffective = isEffective;
        this.effectiveTime = effectiveTime;
        this.isQualified = isQualified;
        this.isOut = isOut;
        this.createTime = createTime;
        this.createUser = createUser;
        this.updateTime = updateTime;
        this.updateUser = updateUser;
    }

    public Long getEvaluationId() {
        return evaluationId;
    }

    public void setEvaluationId(Long evaluationId) {
        this.evaluationId = evaluationId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getConsId() {
        return consId;
    }

    public void setConsId(String consId) {
        this.consId = consId;
    }

    public BigDecimal getInvitationCap() {
        return invitationCap;
    }

    public void setInvitationCap(BigDecimal invitationCap) {
        this.invitationCap = invitationCap;
    }

    public BigDecimal getReplyCap() {
        return replyCap;
    }

    public void setReplyCap(BigDecimal replyCap) {
        this.replyCap = replyCap;
    }

    public BigDecimal getMaxLoadBaseline() {
        return maxLoadBaseline;
    }

    public void setMaxLoadBaseline(BigDecimal maxLoadBaseline) {
        this.maxLoadBaseline = maxLoadBaseline;
    }

    public BigDecimal getMinLoadBaseline() {
        return minLoadBaseline;
    }

    public void setMinLoadBaseline(BigDecimal minLoadBaseline) {
        this.minLoadBaseline = minLoadBaseline;
    }

    public BigDecimal getAvgLoadBaseline() {
        return avgLoadBaseline;
    }

    public void setAvgLoadBaseline(BigDecimal avgLoadBaseline) {
        this.avgLoadBaseline = avgLoadBaseline;
    }

    public BigDecimal getMaxLoadActual() {
        return maxLoadActual;
    }

    public void setMaxLoadActual(BigDecimal maxLoadActual) {
        this.maxLoadActual = maxLoadActual;
    }

    public BigDecimal getMinLoadActual() {
        return minLoadActual;
    }

    public void setMinLoadActual(BigDecimal minLoadActual) {
        this.minLoadActual = minLoadActual;
    }

    public BigDecimal getAvgLoadActual() {
        return avgLoadActual;
    }

    public void setAvgLoadActual(BigDecimal avgLoadActual) {
        this.avgLoadActual = avgLoadActual;
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

    public BigDecimal getConfirmCap() {
        return confirmCap;
    }

    public void setConfirmCap(BigDecimal confirmCap) {
        this.confirmCap = confirmCap;
    }

    public String getIsEffective() {
        return isEffective;
    }

    public void setIsEffective(String isEffective) {
        this.isEffective = isEffective;
    }

    public Integer getEffectiveTime() {
        return effectiveTime;
    }

    public void setEffectiveTime(Integer effectiveTime) {
        this.effectiveTime = effectiveTime;
    }

    public String getIsQualified() {
        return isQualified;
    }

    public void setIsQualified(String isQualified) {
        this.isQualified = isQualified;
    }

    public String getIsOut() {
        return isOut;
    }

    public void setIsOut(String isOut) {
        this.isOut = isOut;
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
}
