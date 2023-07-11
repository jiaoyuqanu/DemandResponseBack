package com.xqxy.dr.modular.statistics.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.dr.modular.statistics.VO.ConsAreaStatisticsVO;
import com.xqxy.dr.modular.statistics.entity.EventStatistics;
import com.xqxy.dr.modular.statistics.param.StatisticalParam;
import com.xqxy.dr.modular.statistics.result.EventStatisticsResult;
import com.xqxy.sys.modular.cust.result.ConsStatisticsResult;
import com.xqxy.sys.modular.cust.result.StatisticsByTypeResult;

import java.util.List;
import java.util.Map;

public interface ConsAreaStatisticsService {

    /**
     *  签约用户的地区统计
     * @return
     */
    Map<String,Integer> consAreaStatistics(Long projectId);


    /**
     *  各地市用户类型统计 --- 签约数据
     * @return
     */
    List<ConsStatisticsResult> consCityStatistics(Long projectId);


    /**
     *  各地市用户类型统计 --- 所有 审核 数据
     * @return
     */
    List<ConsStatisticsResult> consCityStatisticsAll(Long projectId);

    /**
     * 统计 每个月用户签约详情 数量
     * @param statisticalParam
     * @return
     */
    Map<String, List> consStatisticsMonthCount(StatisticalParam statisticalParam);

    /**
     * 统计 每个月用户签约详情 签约负荷
     * @param statisticalParam
     * @return
     */
    Map<String, List> consStatisticsCapCount(StatisticalParam statisticalParam);

    /**
     * 统计 执行效果统计 事件执行统计
     */
    Page<EventStatistics> pageEventStatistics(StatisticalParam statisticalParam);
}
