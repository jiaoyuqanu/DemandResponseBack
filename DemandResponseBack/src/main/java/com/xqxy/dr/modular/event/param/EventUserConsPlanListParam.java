package com.xqxy.dr.modular.event.param;

import com.xqxy.core.pojo.base.param.BaseParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class EventUserConsPlanListParam extends BaseParam {

    @ApiModelProperty(value = "事件id", required = true)
    private String eventId;
    @ApiModelProperty("用户名称")
    private String consName;
    @ApiModelProperty("用户编号")
    private String consCode;
    @ApiModelProperty("供电单位")
    private String orgId;
    @ApiModelProperty("供电单位")
    private String orgName;


}
