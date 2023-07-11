package com.xqxy.dr.modular.upload.entity;


import java.math.BigDecimal;

/**
 * <p>
 * 客户项目申报基本信息
 * </p>
 *
 * @author PengChuqing
 * @since 2021-10-13
 */
public class CustContractInfo{

    private Long contractId;//申报标识

    private Long projectId;//项目标识

    private Long custId;//本实体记录的唯一标识，产生规则为流水号

    private String integrator;//是否集成商 1是 2否

    private String firstContactName;//第一联系人

    private String firstContactInfo;//第一联系人联系方式

    private String secondContactName;//第二联系人姓名

    private String secondContactInifo;//第二联系人联系方式

    private BigDecimal contractCap;//签约容量

    private String status;//1 保存  2 已签约 3撤销

    private String checkStatus;//1 保存  2 已签约 3撤销

    private String fileName;//签约文件名称

    private Long fileId;//签约关联文件ID

    private String fileType;//签约文件类型

    public CustContractInfo() {
    }

    public CustContractInfo(Long contractId, Long projectId, Long custId, String integrator, String firstContactName, String firstContactInfo, String secondContactName, String secondContactInifo, BigDecimal contractCap, String status, String checkStatus, String fileName, Long fileId, String fileType) {
        this.contractId = contractId;
        this.projectId = projectId;
        this.custId = custId;
        this.integrator = integrator;
        this.firstContactName = firstContactName;
        this.firstContactInfo = firstContactInfo;
        this.secondContactName = secondContactName;
        this.secondContactInifo = secondContactInifo;
        this.contractCap = contractCap;
        this.status = status;
        this.checkStatus = checkStatus;
        this.fileName = fileName;
        this.fileId = fileId;
        this.fileType = fileType;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getCustId() {
        return custId;
    }

    public void setCustId(Long custId) {
        this.custId = custId;
    }

    public String getIntegrator() {
        return integrator;
    }

    public void setIntegrator(String integrator) {
        this.integrator = integrator;
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

    public BigDecimal getContractCap() {
        return contractCap;
    }

    public void setContractCap(BigDecimal contractCap) {
        this.contractCap = contractCap;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(String checkStatus) {
        this.checkStatus = checkStatus;
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
}
