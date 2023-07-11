package com.xqxy.dr.modular.subsidy.result;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class EventCustSubsidyPayInfo {
    @ApiModelProperty("事件id")
    private String eventId;
    @ApiModelProperty("事件编号")
    private String eventNo;
    @ApiModelProperty("事件名称")
    private String eventName;
    @ApiModelProperty("参与户数")
    private String totalCons;
    @ApiModelProperty("补偿户数")
    private String isEffectiveCons;
    @ApiModelProperty("补偿金额（元）")
    private String totalMoney;
}
