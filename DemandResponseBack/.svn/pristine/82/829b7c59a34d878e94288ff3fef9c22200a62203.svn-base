package com.xqxy.dr.modular.statistics.controller;

import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.statistics.param.AdjustParam;
import com.xqxy.dr.modular.statistics.service.AdjustSpeedService;
import com.xqxy.sys.modular.cust.entity.Cons;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 系统用户控制器
 *
 * @author chenzhijun
 * @date 2021/11/29 15:14
 */
@Api(tags = "统计接口")
@RestController
@RequestMapping("/statistics/statistics")
public class StatisticsController {

    @Resource
    AdjustSpeedService adjustSpeedService;

    /**
     * 调节速度统计
     * @return
     */
    @ApiOperation(value = "调节速度统计", notes = "调节速度统计", produces = "application/json")
    @PostMapping("/adjustSpeed")
    public ResponseData adjustSpeed(@RequestBody AdjustParam adjustParam) {
        return ResponseData.success(adjustSpeedService.adjustSpeed(adjustParam));
    }

    /**
     * 用户可调资源统计
     * @return
     */
    @ApiOperation(value = "用户可调资源统计", notes = "用户可调资源统计", produces = "application/json")
    @PostMapping("/getUserAdjustRes")
    public ResponseData getUserAdjustRes(@RequestBody AdjustParam adjustParam) {
        return ResponseData.success(adjustSpeedService.getUserAdjustRes(adjustParam));
    }

    /**
     * 设备可持续响应时间统计
     * @return
     */
    @ApiOperation(value = "设备可持续响应时间统计", notes = "设备可持续响应时间统计", produces = "application/json")
    @PostMapping("/getDeviceResponseTime")
    public ResponseData getDeviceResponseTime(@RequestBody AdjustParam adjustParam) {
        return ResponseData.success(adjustSpeedService.getDeviceResponseTime(adjustParam));
    }

    /**
     * 设备类型统计
     * @return
     */
    @ApiOperation(value = "设备类型统计", notes = "设备类型统计", produces = "application/json")
    @PostMapping("/getDeviceType")
    public ResponseData getDeviceType() {
        return ResponseData.success(adjustSpeedService.getDeviceType());
    }

    /**
     * 各单位资源统计
     * @return
     */
    @ApiOperation(value = "各单位资源统计", notes = "各单位资源统计", produces = "application/json")
    @PostMapping("/getCoResource")
    public ResponseData getCoResource() {
        return ResponseData.success(adjustSpeedService.getCoResource());
    }


    /**
     * 响应准备时间可调节资源统计
     * @return
     */
    @ApiOperation(value = "响应准备时间可调节资源统计", notes = "响应准备时间可调节资源统计", produces = "application/json")
    @PostMapping("/responseTimeStatistics")
    public ResponseData responseTimeStatistics() {
        return ResponseData.success(adjustSpeedService.responseTimeStatistics());
    }

}
