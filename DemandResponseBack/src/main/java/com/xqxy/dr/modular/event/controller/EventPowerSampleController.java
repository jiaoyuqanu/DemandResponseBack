package com.xqxy.dr.modular.event.controller;


import com.xqxy.core.annotion.BusinessLog;
import com.xqxy.core.enums.LogAnnotionOpTypeEnum;
import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.event.param.EventParam;
import com.xqxy.dr.modular.event.service.EventPowerSampleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 样本负荷曲线 前端控制器
 * </p>
 *
 * @author xiao jun
 * @since 2021-05-13
 */
@Api(tags = "样本负荷曲线API接口")
@RestController
@RequestMapping("/event/event-power-sample")
public class EventPowerSampleController {

    @Resource
    EventPowerSampleService eventPowerSampleService;

    /**
     * 样本负荷曲线列表
     *
     * @author xiao jun
     * @date 2021-03-11 15:49
     */
//    @BusinessLog(title = "获取所有的事件执行曲线", opType = LogAnnotionOpTypeEnum.QUERY)
    @ApiOperation(value = "样本负荷曲线列表", notes = "样本负荷曲线列表", produces = "application/json")
    @PostMapping("/list")
    public ResponseData list(@RequestBody EventParam eventParam) {
        return ResponseData.success(eventPowerSampleService.list(eventParam.getEventId(), eventParam.getConsId()));
    }

    /**
     * 样本负荷数据导出
     *
     * @param eventParam
     * @param
     * @author xiao jun
     * @date 2021-05-24 09:26
     */
//    @BusinessLog(title = "样本负荷数据导出", opType = LogAnnotionOpTypeEnum.EXPORT)
    @ApiOperation(value = "样本负荷数据导出", notes = "样本负荷数据导出", produces = "application/json")
    @PostMapping("/export")
    public void export(@RequestBody EventParam eventParam) {
        eventPowerSampleService.export(eventParam.getEventId(), eventParam.getConsId());
    }

}

