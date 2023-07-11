package com.xqxy.dr.modular.evaluation.service;

import com.xqxy.dr.modular.evaluation.entity.EvaluTask;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.dr.modular.evaluation.param.EvaluTaskParam;
import com.xqxy.dr.modular.event.entity.Event;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 效果评估计算任务表 服务类
 * </p>
 *
 * @author hu xingxing
 * @since 2021-10-20
 */
public interface EvaluTaskService extends IService<EvaluTask> {

    /**
     * @description: 根据调控日期查询评估任务表
     * @param: evaluTaskParam 查询参数
     * @return:
     * @author: hu xingxing
     * @date: 2021-10-19 15:25
     */
    List<EvaluTask> list(EvaluTaskParam evaluTaskParam);

    /**
     * @description: 根据事件标识和用户标识查询用户任务标识表
     * @param: eventId,consId 查询参数
     * @return:
     * @author: hu xingxing
     * @date: 2021-10-22 14:10
     */
    EvaluTask getEvaluTaskByEventIdAndConsId(long eventId, String consId);

    List<EvaluTask> getEvaluTaskByEvents(List<Event> eventList);

    void batchUpdateImplement(List<EvaluTask> evaluTaskList);
}
