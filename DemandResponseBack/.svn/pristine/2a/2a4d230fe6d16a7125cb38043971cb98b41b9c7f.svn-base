package com.xqxy.dr.modular.statistics.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.dr.modular.statistics.param.StatisticalParam;
import com.xqxy.dr.modular.statistics.result.ProjectEffectResult;

import java.util.List;

public interface ProjectEffectMapper {

    /**
     *  用户需求响应项目效果统计
     * @return
     */
    Page<ProjectEffectResult> projectEffectStatistics(Page<Object> defaultPage, StatisticalParam statisticalParam);

    /**
     *  用户需求响应项目效果统计导出
     * @return
     */
    List<ProjectEffectResult> exportProject(StatisticalParam statisticalParam);
}
