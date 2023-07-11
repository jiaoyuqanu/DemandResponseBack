package com.xqxy.dr.modular.statistics.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.dr.modular.statistics.param.StatisticalParam;
import com.xqxy.dr.modular.statistics.result.AreaProjectResult;

public interface AreaEventsService {

    /**
     *  地区事件执行效果统计
     * @return
     */
    Page<AreaProjectResult> areaEventsStatistics(StatisticalParam statisticalParam);

    void exportRegion();
}
