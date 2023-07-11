package com.xqxy.dr.modular.evaluation.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xqxy.dr.modular.evaluation.entity.EvaluCustTask;
import com.xqxy.dr.modular.evaluation.entity.EvaluTask;
import com.xqxy.dr.modular.evaluation.mapper.EvaluCustTaskMapper;
import com.xqxy.dr.modular.evaluation.param.EvaluCustTaskParam;
import com.xqxy.dr.modular.evaluation.service.EvaluCustTaskService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.dr.modular.event.entity.Event;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 客户效果评估计算任务表 服务实现类
 * </p>
 *
 * @author hu xingxing
 * @since 2021-10-21
 */
@Service
public class EvaluCustTaskServiceImpl extends ServiceImpl<EvaluCustTaskMapper, EvaluCustTask> implements EvaluCustTaskService {

    @Override
    public List<EvaluCustTask> list(EvaluCustTaskParam evaluCustTaskParam) {
        return baseMapper.getEvaluTaskByCon(evaluCustTaskParam);
    }

    @Override
    public List<EvaluCustTask> getEvaluTaskByEvents(List<Event> eventList) {
        return baseMapper.getEvaluTaskByEvents(eventList);
    }

    @Override
    public void batchUpdateImplement(List<EvaluCustTask> evaluCustTaskList) {
        baseMapper.batchUpdateImplement(evaluCustTaskList);
    }

    @Override
    public List<String> getConsIdByPlanId(Long planId) {
        return baseMapper.getConsIdByPlanId(planId);
    }
}
