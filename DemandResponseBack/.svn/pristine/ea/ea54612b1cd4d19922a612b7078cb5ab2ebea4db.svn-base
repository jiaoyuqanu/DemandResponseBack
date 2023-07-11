package com.xqxy.dr.modular.statistics.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.dr.modular.statistics.param.StatisticalParam;
import com.xqxy.dr.modular.statistics.result.DrYearResult;

import java.util.List;

public interface DrYearMapper {

    /**
     * 用户需求响应年度统计
     * @return
     */
    Page<DrYearResult> drYearStatistics(Page<Object> defaultPage, StatisticalParam statisticalParam);

    /**
     * 用户需求响应年度统计导出
     * @return
     */
    List<DrYearResult> exportYear(StatisticalParam statisticalParam);
}
