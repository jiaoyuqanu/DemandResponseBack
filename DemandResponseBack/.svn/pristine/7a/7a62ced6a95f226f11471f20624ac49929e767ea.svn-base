package com.xqxy.dr.modular.subsidy.param;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xqxy.core.pojo.base.param.BaseParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class CustConsSubsidyPayParam extends BaseParam {
    @ApiModelProperty(value = "事件id", required = true)
    private String eventId;
    @ApiModelProperty("用户编号")
    private String consCode;
    @ApiModelProperty("用户名称")
    private String consName;


    @JsonIgnore
    private String custId;
    @JsonIgnore
    private List<String> consIds;
}
