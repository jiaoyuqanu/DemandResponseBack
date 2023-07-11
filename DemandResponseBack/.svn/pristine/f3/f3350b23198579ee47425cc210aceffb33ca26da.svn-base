package com.xqxy.dr.modular.statistics.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.statistics.param.StatisticalParam;
import com.xqxy.dr.modular.statistics.param.TotalStatisticsParam;
import com.xqxy.dr.modular.statistics.result.TotalStatisticsTableResult;
import com.xqxy.dr.modular.statistics.service.AreaEventsService;
import com.xqxy.dr.modular.statistics.service.TotalStatisticsService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/totalStatistics")
public class TotalStatisticsController {

    @Resource
    private TotalStatisticsService totalStatistics;

    /**
     *  数据总览统计
     * @return
     */
    @ApiOperation(value = "数据总览统计", notes = "数据总览统计", produces = "application/json")
    @PostMapping("/getTotalStatistics")
    public ResponseData getTotalStatistics() {
        return ResponseData.success(totalStatistics.getTotalStatistics());
    }

    /**
     *  数据总览统计
     * @return
     */
    @ApiOperation(value = "累计数据列表", notes = "累计数据列表", produces = "application/json")
    @PostMapping("/getTotalStatisticsTable")
    public ResponseData getTotalStatisticsTable(@RequestBody TotalStatisticsParam totalStatisticsParam) {
        Page<TotalStatisticsTableResult> page = new Page<>(totalStatisticsParam.getCurrent(),totalStatisticsParam.getSize());
        return ResponseData.success(totalStatistics.getTotalStatisticsTable(page,totalStatisticsParam));
    }


}
