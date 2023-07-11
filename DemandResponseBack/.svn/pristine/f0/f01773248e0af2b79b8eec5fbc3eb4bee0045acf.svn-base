package com.xqxy.dr.modular.eventEvaluation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xqxy.dr.modular.eventEvaluation.entity.EventEvaluation;
import com.xqxy.executor.entity.ExecuteMaximumLoad;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Rabbit
 * @description 针对表【dr_event_evaluation(事件执行效果评估)】的数据库操作Mapper
 * @createDate 2022-06-28 15:37:38
 * @Entity generator.domain.EventEvaluation
 */
public interface EventEvaluationMapper extends BaseMapper<EventEvaluation> {

    /**
     * 本年度需求响应（削峰）执行最大负荷（万kW）
     *
     * @param orgNoList 供电编码
     * @return ExecuteMaximumLoad
     */
    ExecuteMaximumLoad executeMaximumLoad(@Param("orgNoList") List<String> orgNoList);
}




