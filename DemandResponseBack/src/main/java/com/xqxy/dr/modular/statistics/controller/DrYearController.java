package com.xqxy.dr.modular.statistics.controller;


import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.statistics.param.StatisticalParam;
import com.xqxy.dr.modular.statistics.service.DrYearService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/drYear")
public class DrYearController {

    @Resource
    private DrYearService drYearService;

    /**
     * 用户需求响应年度统计
     * @return
     */
    @ApiOperation(value = "用户需求响应年度统计", notes = "用户需求响应年度统计", produces = "application/json")
    @PostMapping("/drYearStatistics")
    public ResponseData drYearStatistics(@RequestBody StatisticalParam statisticalParam) {

        return ResponseData.success(drYearService.drYearStatistics(statisticalParam));
    }

    /**
     * 用户需求响应年度统计导出
     * @return
     */
    @ApiOperation(value = "用户需求响应年度统计导出", notes = "用户需求响应年度统计导出", produces = "application/json")
    @PostMapping("/exportYear")
    public void exportYear(@RequestBody StatisticalParam statisticalParam) {

        drYearService.exportYear(statisticalParam);
    }

}
