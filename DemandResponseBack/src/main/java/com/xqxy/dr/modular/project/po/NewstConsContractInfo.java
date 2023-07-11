package com.xqxy.dr.modular.project.po;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class NewstConsContractInfo {

    private String consId;
    private String consName;
    private String orgNo;
    private String orgName;
    private String signTime;
    private String contractId;
    private String fileId;
    private String fileName;
    private String fileType;
    private byte[] fileBytes;
    private BigDecimal invitationDrAbiltity;
}
