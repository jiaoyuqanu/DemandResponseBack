package com.xqxy.dr.modular.event.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class EventJobParam {

    @ApiModelProperty(value = "事件id")
    private String eventId;

    @ApiModelProperty("job名称: daysAgoScheduleAndScheduleOfCustomers")
    private String jobName;

}
