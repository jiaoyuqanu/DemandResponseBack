package com.xqxy.dr.modular.newloadmanagement.vo;

import lombok.Data;


import java.time.LocalDateTime;

@Data
public class ExchDemandResponsePlanInfoVo {

    private String planNo;

    private String orgNo;

    private String pOrgNo;

    private LocalDateTime execStaTime;

    private LocalDateTime execEndTime;


}
