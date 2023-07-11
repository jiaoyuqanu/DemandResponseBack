package com.xqxy.dr.modular.statistics.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.dr.modular.statistics.param.TotalStatisticsParam;
import com.xqxy.dr.modular.statistics.result.TotalStatisticsResult;
import com.xqxy.dr.modular.statistics.result.TotalStatisticsTableResult;

import java.util.List;

public interface TotalStatisticsService extends IService<TotalStatisticsTableResult> {
    TotalStatisticsResult getTotalStatistics();

    List<TotalStatisticsTableResult> getExecuteData(List<String> orgIds);

    List<TotalStatisticsTableResult> getExecuteDataCity(List<String> orgIds);

    Page<TotalStatisticsTableResult> getTotalStatisticsTable(Page<TotalStatisticsTableResult> page,TotalStatisticsParam totalStatisticsParam);
}
