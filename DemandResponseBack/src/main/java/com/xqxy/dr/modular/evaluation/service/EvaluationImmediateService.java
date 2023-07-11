package com.xqxy.dr.modular.evaluation.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.core.pojo.page.PageResult;
import com.xqxy.dr.modular.evaluation.entity.EvaluationImmediate;
import com.xqxy.dr.modular.evaluation.param.EvaluationImmediateParam;

import java.util.List;

/**
 * <p>
 * 响应效果评估 服务类
 * </p>
 *
 * @author xiao jun
 * @since 2021-06-03
 */
public interface EvaluationImmediateService extends IService<EvaluationImmediate> {
    List<Long> getCustIdsByEventId(Long eventId);
    List<Long> getNextDayCustIdsByEventId(Long eventId);
    List<Long> getNextDayCustIdsByEventId(Long eventId,Long custId);
    Integer getCountByEventId(Long eventId);
    Integer getNextDayCountByEventId(Long eventId);

}
