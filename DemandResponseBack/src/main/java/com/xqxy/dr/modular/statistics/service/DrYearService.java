package com.xqxy.dr.modular.statistics.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.dr.modular.statistics.param.StatisticalParam;
import com.xqxy.dr.modular.statistics.result.DrYearResult;

public interface DrYearService {

    /**
     * 用户需求响应年度统计
     * @return
     */
    Page<DrYearResult> drYearStatistics(StatisticalParam statisticalParam);

    /**
     * 用户需求响应年度统计导出
     * @return
     */
    void exportYear(StatisticalParam statisticalParam);
}
