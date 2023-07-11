package com.xqxy.dr.modular.event.controller;

import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.event.param.EventJobParam;
import com.xqxy.executor.service.jobhandler.ScheduleAndScheduleOfCustomersJob;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/event/job")
@Api(tags = "事件job")
public class EventJobController {

    @Resource
    private ScheduleAndScheduleOfCustomersJob scheduleAndScheduleOfCustomersJob;

    /**
     * 事件手动执行job
     */
    @ApiOperation(value = "执行job")
    @RequestMapping(value = "execute", method = RequestMethod.POST)
    public ResponseData executeJob(@RequestBody EventJobParam eventJobParam) {
        switch (eventJobParam.getJobName()) {
            case "daysAgoScheduleAndScheduleOfCustomers":
                scheduleAndScheduleOfCustomersJob.saveDaysAgoScheduleAndScheduleOfCustomers(null, eventJobParam.getEventId());
                break;
        }
        return ResponseData.success();
    }


}
