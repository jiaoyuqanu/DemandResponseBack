package com.xqxy.dr.modular.statistics.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.dr.modular.statistics.param.AdjustParam;
import com.xqxy.dr.modular.statistics.result.*;

import java.util.List;

public interface AdjustSpeedService {

    /**
     * 调节速度统计
     * @return
     */
    Page<AdjustSpeedResult> adjustSpeed(AdjustParam adjustParam);

    /**
     * 用户可调资源统计
     * @return
     */
    Page<AdjustResourceResult> getUserAdjustRes(AdjustParam adjustParam);

    /**
     * 设备可持续响应时间统计
     * @return
     */
    Page<DeviceResponseTimeResult> getDeviceResponseTime(AdjustParam adjustParam);

    /**
     * 设备可持续响应时间统计
     * @return
     */
    List<DeviceTypeResult> getDeviceType();

    /**
     * 各单位资源统计
     * @return
     */
    List<DeviceTypeResult> getCoResource();

    /**
     * 响应准备时间可调节资源统计
     * @return
     */
    ResponseTimeStatisticsResult responseTimeStatistics();
}
