package com.xqxy.dr.modular.evaluation.service;

import com.xqxy.dr.modular.evaluation.entity.EvaluCustTask;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.dr.modular.evaluation.entity.EvaluTask;
import com.xqxy.dr.modular.evaluation.param.EvaluCustTaskParam;
import com.xqxy.dr.modular.event.entity.Event;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 客户效果评估计算任务表 服务类
 * </p>
 *
 * @author hu xingxing
 * @since 2021-10-21
 */
public interface EvaluCustTaskService extends IService<EvaluCustTask> {

    /**
     * @description: 根据调控日期查询客户评估任务表
     * @param: evaluCustTaskParam 查询参数
     * @return:
     * @author: hu xingxing
     * @date: 2021-10-20 14:00
     */
    List<EvaluCustTask> list(EvaluCustTaskParam evaluCustTaskParam);

    List<EvaluCustTask> getEvaluTaskByEvents(List<Event> eventList);

    void batchUpdateImplement(List<EvaluCustTask> evaluCustTaskList);

    List<String> getConsIdByPlanId(Long planId);
}
