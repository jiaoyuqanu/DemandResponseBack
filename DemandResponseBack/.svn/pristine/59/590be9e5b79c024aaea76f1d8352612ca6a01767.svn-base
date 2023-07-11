package com.xqxy.dr.modular.statistics.controller;

import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.statistics.param.StatisticalParam;
import com.xqxy.dr.modular.statistics.service.AreaEventsService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/areaEvents")
public class AreaEventsController {

    @Resource
    private AreaEventsService areaProjectService;

    /**
     *  地区事件执行效果统计
     * @return
     */
    @ApiOperation(value = "地区事件执行效果统计", notes = "地区事件执行效果统计", produces = "application/json")
    @PostMapping("/areaEventsStatistics")
    public ResponseData areaEventsStatistics(@RequestBody StatisticalParam statisticalParam) {

        return ResponseData.success(areaProjectService.areaEventsStatistics(statisticalParam));
    }

    /**
     *  地区事件执行效果统计导出
     * @return
     */
    @ApiOperation(value = "地区事件执行效果统计导出", notes = "地区事件执行效果统计导出", produces = "application/json")
    @PostMapping("/exportRegion")
    public void exportRegion() {

        areaProjectService.exportRegion();
    }
}
