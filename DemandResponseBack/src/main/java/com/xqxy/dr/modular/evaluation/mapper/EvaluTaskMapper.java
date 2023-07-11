package com.xqxy.dr.modular.evaluation.mapper;

import com.xqxy.dr.modular.evaluation.entity.EvaluCustTask;
import com.xqxy.dr.modular.evaluation.entity.EvaluTask;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xqxy.dr.modular.evaluation.param.EvaluCustTaskParam;
import com.xqxy.dr.modular.evaluation.param.EvaluTaskParam;
import com.xqxy.dr.modular.event.entity.Event;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 效果评估计算任务表 Mapper 接口
 * </p>
 *
 * @author hu xingxing
 * @since 2021-10-20
 */
public interface EvaluTaskMapper extends BaseMapper<EvaluTask> {
    List<EvaluTask> getEvaluTaskByEvents(@Param("eventList") List<Event> eventList);
    List<EvaluTask> getEvaluTaskByCon(EvaluTaskParam evaluCustTaskParam);
    void batchUpdateImplement(@Param("evaluTaskList")  List<EvaluTask> evaluTaskList);
}
