package com.xqxy.dr.modular.gwapp.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class GwappCustAuditParam {

    @ApiModelProperty("0：审核通过，1：审核不通过")
    @NotNull(message = "审核状态不能为空")
    private Integer checkStatus;
    @ApiModelProperty("审批意见")
    private String approvalComments;

}
