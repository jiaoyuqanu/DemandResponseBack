package com.xqxy.dr.modular.upload.entity;


import java.math.BigDecimal;

/**
 * <p>
 * 用户项目申报基本信息
 * </p>
 *
 * @author PengChuqing
 * @since 2021-10-13
 */

public class ContractInfo {
    private Long contractId;//申报标识

    private String consId;//用户标识

    private Long projectId;//项目标识

    private String particType;//1直接参与，2代理参与

    private BigDecimal extractRatio;//分成比例

    private String firstContactName;//第一联系人

    private String firstContactInfo;//第一联系人联系方式

    private String secondContactName;//第二联系人姓名

    private String secondContactInifo;//第二联系人联系方式

    private String createTime;//创建时间

    private Long createUser;//创建人

    private String updateTime;//更新时间

    private Long updateUser;//更新人

    private String checkStatus;//1:未提交，2：审核中，3：审核通过，4：审核不通过

    private String status;//1 保存  2 已签约 3撤销

    private String fileName;//签约文件名称

    private Long fileId;//签约关联文件ID

    private String fileType;//签约文件类型

    private ContractDetail contractDetail;

    private String startTime;//响应开始时间

    private BigDecimal contractCap;//最大容量

    private String endTime;//响应结束时间

    private Long projectDetailId;//项目明细标识

    private String responseType;//响应类型

    private String timeType;//时间类型

    private Drcons drcons;

    private String CITY_CODE;//市码

    private String COUNTY_CODE;//区县码
    public ContractInfo() {
    }

    public ContractInfo(Long contractId, String consId, Long projectId, String particType, BigDecimal extractRatio, String firstContactName, String firstContactInfo, String secondContactName, String secondContactInifo, String createTime, Long createUser, String updateTime, Long updateUser, String checkStatus, String status, String fileName, Long fileId, String fileType, ContractDetail contractDetail, String startTime, BigDecimal contractCap, String endTime, Long projectDetailId, String responseType, String timeType, Drcons drcons, String CITY_CODE, String COUNTY_CODE) {
        this.contractId = contractId;
        this.consId = consId;
        this.projectId = projectId;
        this.particType = particType;
        this.extractRatio = extractRatio;
        this.firstContactName = firstContactName;
        this.firstContactInfo = firstContactInfo;
        this.secondContactName = secondContactName;
        this.secondContactInifo = secondContactInifo;
        this.createTime = createTime;
        this.createUser = createUser;
        this.updateTime = updateTime;
        this.updateUser = updateUser;
        this.checkStatus = checkStatus;
        this.status = status;
        this.fileName = fileName;
        this.fileId = fileId;
        this.fileType = fileType;
        this.contractDetail = contractDetail;
        this.startTime = startTime;
        this.contractCap = contractCap;
        this.endTime = endTime;
        this.projectDetailId = projectDetailId;
        this.responseType = responseType;
        this.timeType = timeType;
        this.drcons = drcons;
        this.CITY_CODE = CITY_CODE;
        this.COUNTY_CODE = COUNTY_CODE;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public String getConsId() {
        return consId;
    }

    public void setConsId(String consId) {
        this.consId = consId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getParticType() {
        return particType;
    }

    public void setParticType(String particType) {
        this.particType = particType;
    }

    public BigDecimal getExtractRatio() {
        return extractRatio;
    }

    public void setExtractRatio(BigDecimal extractRatio) {
        this.extractRatio = extractRatio;
    }

    public String getFirstContactName() {
        return firstContactName;
    }

    public void setFirstContactName(String firstContactName) {
        this.firstContactName = firstContactName;
    }

    public String getFirstContactInfo() {
        return firstContactInfo;
    }

    public void setFirstContactInfo(String firstContactInfo) {
        this.firstContactInfo = firstContactInfo;
    }

    public String getSecondContactName() {
        return secondContactName;
    }

    public void setSecondContactName(String secondContactName) {
        this.secondContactName = secondContactName;
    }

    public String getSecondContactInifo() {
        return secondContactInifo;
    }

    public void setSecondContactInifo(String secondContactInifo) {
        this.secondContactInifo = secondContactInifo;
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

    public String getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(String checkStatus) {
        this.checkStatus = checkStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public ContractDetail getContractDetail() {
        return contractDetail;
    }

    public void setContractDetail(ContractDetail contractDetail) {
        this.contractDetail = contractDetail;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public BigDecimal getContractCap() {
        return contractCap;
    }

    public void setContractCap(BigDecimal contractCap) {
        this.contractCap = contractCap;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Long getProjectDetailId() {
        return projectDetailId;
    }

    public void setProjectDetailId(Long projectDetailId) {
        this.projectDetailId = projectDetailId;
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

    public Drcons getDrcons() {
        return drcons;
    }

    public void setDrcons(Drcons drcons) {
        this.drcons = drcons;
    }

    public String getCITY_CODE() {
        return CITY_CODE;
    }

    public void setCITY_CODE(String CITY_CODE) {
        this.CITY_CODE = CITY_CODE;
    }

    public String getCOUNTY_CODE() {
        return COUNTY_CODE;
    }

    public void setCOUNTY_CODE(String COUNTY_CODE) {
        this.COUNTY_CODE = COUNTY_CODE;
    }
}
