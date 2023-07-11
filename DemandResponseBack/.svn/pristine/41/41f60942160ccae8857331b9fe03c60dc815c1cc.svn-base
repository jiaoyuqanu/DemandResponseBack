package com.xqxy.dr.modular.evaluation.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xqxy.dr.modular.baseline.entity.PlanBaseLine;
import com.xqxy.dr.modular.evaluation.entity.EvaluTask;
import com.xqxy.dr.modular.evaluation.mapper.EvaluTaskMapper;
import com.xqxy.dr.modular.evaluation.param.EvaluTaskParam;
import com.xqxy.dr.modular.evaluation.service.EvaluTaskService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.dr.modular.event.entity.Event;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 效果评估计算任务表 服务实现类
 * </p>
 *
 * @author hu xingxing
 * @since 2021-10-20
 */
@Service
public class EvaluTaskServiceImpl extends ServiceImpl<EvaluTaskMapper, EvaluTask> implements EvaluTaskService {

    @Override
    public List<EvaluTask> list(EvaluTaskParam evaluTaskParam) {
        return baseMapper.getEvaluTaskByCon(evaluTaskParam);
    }

    @Override
    public EvaluTask getEvaluTaskByEventIdAndConsId(long eventId, String consId) {

        LambdaQueryWrapper<EvaluTask> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(eventId)) {
            queryWrapper.eq(EvaluTask::getEventId, eventId);
        }

        if (ObjectUtil.isNotNull(consId)) {
            queryWrapper.eq(EvaluTask::getConsId, consId);
        }

        return this.getOne(queryWrapper);
    }

    @Override
    public List<EvaluTask> getEvaluTaskByEvents(List<Event> eventList) {
        return baseMapper.getEvaluTaskByEvents(eventList);
    }

    @Override
    public void batchUpdateImplement(List<EvaluTask> evaluTaskList) {
        baseMapper.batchUpdateImplement(evaluTaskList);
    }
}
