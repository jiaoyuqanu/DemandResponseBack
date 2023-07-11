package com.xqxy.dr.modular.newloadmanagement.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class OrgDemand64PointParam {

    @ApiModelProperty("组织机构id")
    private String orgId;

    @ApiModelProperty("日期")
    private String queryDate;
}
