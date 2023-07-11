package com.xqxy.dr.modular.subsidy.param;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.xqxy.core.pojo.base.param.BaseParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class EventCustSubsidyPayParam extends BaseParam {

    @ApiModelProperty("结算批号")
    private String settlementNo;
    @ApiModelProperty("事件编号")
    private String eventCode;
    @ApiModelProperty("事件名称")
    private String eventName;

    @JsonIgnore
    private String custId;
    @JsonIgnore
    private List<String> eventIds;
    @JsonIgnore
    private List<String> consIds;

}
