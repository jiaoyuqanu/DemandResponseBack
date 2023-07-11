package com.xqxy.dr.modular.upload.entity;


import java.math.BigDecimal;

/**
 * <p>
 * 
 * </p>
 *
 * @author Caoj
 * @since 2021-10-18
 */

public class CustContractDetail{

    private Long detailId;//明细标识

    private Long contractId;

    private String responseType;//响应类型1削峰，2填谷

    private String timeType;//时间类型 1邀约，2实时

    private String startTime;//响应开始时间

    private String endTime;//响应结束时间

    private BigDecimal responseCap;//可响应负荷为该代理子用户设备可调节负荷的累加

    private BigDecimal contractCap;//合同容量

    private BigDecimal contractPrice;//签约价格

    private BigDecimal extractRatio;//分成比例

    private Long projectDetailId;//项目明细标识

    public CustContractDetail() {
    }

    public CustContractDetail(Long detailId, Long contractId, String responseType, String timeType, String startTime, String endTime, BigDecimal responseCap, BigDecimal contractCap, BigDecimal contractPrice, BigDecimal extractRatio, Long projectDetailId) {
        this.detailId = detailId;
        this.contractId = contractId;
        this.responseType = responseType;
        this.timeType = timeType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.responseCap = responseCap;
        this.contractCap = contractCap;
        this.contractPrice = contractPrice;
        this.extractRatio = extractRatio;
        this.projectDetailId = projectDetailId;
    }

    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public String getResponseType() {
        return responseType;
    }

    public void setResponseType(String responseType) {
        this.responseType = responseType;
    }

    public String getTimeType() {
        return timeType;
    }

    public void setTimeType(String timeType) {
        this.timeType = timeType;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public BigDecimal getResponseCap() {
        return responseCap;
    }

    public void setResponseCap(BigDecimal responseCap) {
        this.responseCap = responseCap;
    }

    public BigDecimal getContractCap() {
        return contractCap;
    }

    public void setContractCap(BigDecimal contractCap) {
        this.contractCap = contractCap;
    }

    public BigDecimal getContractPrice() {
        return contractPrice;
    }

    public void setContractPrice(BigDecimal contractPrice) {
        this.contractPrice = contractPrice;
    }

    public BigDecimal getExtractRatio() {
        return extractRatio;
    }

    public void setExtractRatio(BigDecimal extractRatio) {
        this.extractRatio = extractRatio;
    }

    public Long getProjectDetailId() {
        return projectDetailId;
    }

    public void setProjectDetailId(Long projectDetailId) {
        this.projectDetailId = projectDetailId;
    }
}
