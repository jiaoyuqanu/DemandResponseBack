package com.xqxy.dr.modular.evaluation.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.dr.modular.evaluation.entity.EvaluationImmediate;
import com.xqxy.dr.modular.evaluation.mapper.EvaluationImmediateMapper;
import com.xqxy.dr.modular.evaluation.service.EvaluationImmediateService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 响应效果评估 服务实现类
 * </p>
 *
 * @author xiao jun
 * @since 2021-06-03
 */
@Service
public class EvaluationImmediateServiceImpl extends ServiceImpl<EvaluationImmediateMapper, EvaluationImmediate> implements EvaluationImmediateService {


    @Override
    public List<Long> getCustIdsByEventId(Long eventId) {
        return baseMapper.getCustIdsByEventId(eventId);
    }

    @Override
    public List<Long> getNextDayCustIdsByEventId(Long eventId) {
        return baseMapper.getNextDayCustIdsByEventId(eventId);
    }

    @Override
    public List<Long> getNextDayCustIdsByEventId(Long eventId, Long custId) {
        return baseMapper.getNextDayCustIdsByEventIdAndConsId(eventId,custId);
    }


    @Override
    public Integer getCountByEventId(Long eventId) {
        return baseMapper.getCountByEventId(eventId);
    }

    @Override
    public Integer getNextDayCountByEventId(Long eventId) {
        return baseMapper.getNextDayCountByEventId(eventId);
    }
}
