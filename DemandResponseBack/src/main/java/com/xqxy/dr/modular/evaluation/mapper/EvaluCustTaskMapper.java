package com.xqxy.dr.modular.evaluation.mapper;

import com.xqxy.dr.modular.evaluation.entity.EvaluCustTask;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xqxy.dr.modular.evaluation.entity.EvaluTask;
import com.xqxy.dr.modular.evaluation.param.EvaluCustTaskParam;
import com.xqxy.dr.modular.event.entity.Event;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 客户效果评估计算任务表 Mapper 接口
 * </p>
 *
 * @author hu xingxing
 * @since 2021-10-21
 */
public interface EvaluCustTaskMapper extends BaseMapper<EvaluCustTask> {
    List<EvaluCustTask> getEvaluTaskByEvents(@Param("eventList") List<Event> eventList);

    List<EvaluCustTask> getEvaluTaskByCon(EvaluCustTaskParam evaluCustTaskParam);

    void batchUpdateImplement(@Param("evaluTaskList")  List<EvaluCustTask> evaluCustTaskList);

    List<String> getConsIdByPlanId(Long planId);

}
