package com.xqxy.dr.modular.event.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class EventUserCustLineParam {

    @ApiModelProperty(value = "事件id", required = true)
    private Long eventId;

}
