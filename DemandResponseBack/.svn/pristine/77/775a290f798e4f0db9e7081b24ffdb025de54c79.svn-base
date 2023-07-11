package com.xqxy.dr.modular.statistics.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.dr.modular.dispatch.entity.Dispatch;
import com.xqxy.dr.modular.dispatch.mapper.DispatchMapper;
import com.xqxy.dr.modular.powerplant.entity.DrDaAggregator;
import com.xqxy.dr.modular.statistics.mapper.AreaEventsMapper;
import com.xqxy.dr.modular.statistics.mapper.TotalStatisticsMapper;
import com.xqxy.dr.modular.statistics.param.TotalStatisticsParam;
import com.xqxy.dr.modular.statistics.result.TotalStatisticsResult;
import com.xqxy.dr.modular.statistics.result.TotalStatisticsTableResult;
import com.xqxy.dr.modular.statistics.service.TotalStatisticsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class TotalStatisticsServiceImpl extends ServiceImpl<TotalStatisticsMapper, TotalStatisticsTableResult> implements TotalStatisticsService {

    @Resource
    private TotalStatisticsMapper totalStatisticsMapper;


    @Override
    public TotalStatisticsResult getTotalStatistics() {
        TotalStatisticsResult totalStatisticsResult = totalStatisticsMapper.getCapAndEnergy();
        List<String> eventDays = totalStatisticsMapper.getEventDays();
        TotalStatisticsResult statisticsResultMax = totalStatisticsMapper.getMaxCapAndEnergy();
        if(null==eventDays) {
            eventDays = new ArrayList<>();
        }
        Integer consCount = totalStatisticsMapper.getConsCount();
        if(null==totalStatisticsResult) {
            totalStatisticsResult = new TotalStatisticsResult();
        }
        totalStatisticsResult.setConsCount(consCount);
        totalStatisticsResult.setEventDays(eventDays.size());
        if(null != statisticsResultMax) {
            totalStatisticsResult.setActualCapMax(statisticsResultMax.getActualCapMax());
            totalStatisticsResult.setActualEnergyMax(statisticsResultMax.getActualEnergyMax());
        } else {
            totalStatisticsResult.setActualCapMax(BigDecimal.ZERO);
            totalStatisticsResult.setActualEnergyMax(BigDecimal.ZERO);
        }
        return totalStatisticsResult;
    }

    @Override
    public List<TotalStatisticsTableResult> getExecuteData(List<String> orgIds) {
        return totalStatisticsMapper.getExecuteData(orgIds);
    }

    @Override
    public List<TotalStatisticsTableResult> getExecuteDataCity(List<String> orgIds) {
        return totalStatisticsMapper.getExecuteDataCity(orgIds);
    }

    @Override
    public Page<TotalStatisticsTableResult> getTotalStatisticsTable(Page<TotalStatisticsTableResult> page,TotalStatisticsParam totalStatisticsParam) {
        QueryWrapper<TotalStatisticsTableResult> queryWrapper = new QueryWrapper<>();
        if (ObjectUtil.isNotNull(totalStatisticsParam)) {
            if (ObjectUtil.isNotEmpty(totalStatisticsParam.getOrgId())) {
                queryWrapper.eq("org_id", totalStatisticsParam.getOrgId());
            }
            if (ObjectUtil.isNotEmpty(totalStatisticsParam.getProjectId())) {
                queryWrapper.eq("project_id", totalStatisticsParam.getProjectId());
            }
            if (ObjectUtil.isNotEmpty(totalStatisticsParam.getYear())) {
                queryWrapper.eq("year", totalStatisticsParam.getYear());
            }
        }
        Page<TotalStatisticsTableResult> list = this.page(page,queryWrapper);
        return list;
    }
}
