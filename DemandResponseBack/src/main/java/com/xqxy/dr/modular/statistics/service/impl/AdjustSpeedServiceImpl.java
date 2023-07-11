package com.xqxy.dr.modular.statistics.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.dr.modular.statistics.mapper.StatisticsMapper;
import com.xqxy.dr.modular.statistics.param.AdjustParam;
import com.xqxy.dr.modular.statistics.result.*;
import com.xqxy.dr.modular.statistics.service.AdjustSpeedService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AdjustSpeedServiceImpl implements AdjustSpeedService {
    @Resource
    StatisticsMapper statisticsMapper;

    @Override
    public Page<AdjustSpeedResult> adjustSpeed(AdjustParam adjustParam){
        Page<AdjustSpeedResult> page = new Page<>(adjustParam.getCurrent(),adjustParam.getSize());
        List<AdjustSpeedResult> list = statisticsMapper.adjustSpeed(page);
        page.setRecords(list);
        return page;
    }

    @Override
    public Page<AdjustResourceResult> getUserAdjustRes(AdjustParam adjustParam) {
        Page<AdjustResourceResult> page = new Page<>(adjustParam.getCurrent(),adjustParam.getSize());
        List<AdjustResourceResult> list = statisticsMapper.getUserAdjustRes(page,adjustParam.getStartTime(),adjustParam.getEndTime());
        page.setRecords(list);
        return page;
    }

    @Override
    public Page<DeviceResponseTimeResult> getDeviceResponseTime(AdjustParam adjustParam) {
        Page<DeviceResponseTimeResult> page = new Page<>(adjustParam.getCurrent(),adjustParam.getSize());
        List<DeviceResponseTimeResult> list = statisticsMapper.getDeviceResponseTime(page,adjustParam.getStartTime(),adjustParam.getEndTime());
        page.setRecords(list);
        return page;
    }

    @Override
    public List<DeviceTypeResult> getDeviceType() {
        return statisticsMapper.getDeviceType();
    }

    @Override
    public List<DeviceTypeResult> getCoResource() {
        return statisticsMapper.getCoResource();
    }

    @Override
    public ResponseTimeStatisticsResult responseTimeStatistics()
    {
        return statisticsMapper.responseTimeStatistics();
    }
}
