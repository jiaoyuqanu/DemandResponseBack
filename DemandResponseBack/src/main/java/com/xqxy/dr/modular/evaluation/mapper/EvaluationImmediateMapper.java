package com.xqxy.dr.modular.evaluation.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.dr.modular.evaluation.entity.EvaluationImmediate;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 响应效果评估 Mapper 接口
 * </p>
 *
 * @author xiao jun
 * @since 2021-06-03
 */
public interface EvaluationImmediateMapper extends BaseMapper<EvaluationImmediate> {
    List<Long> getCustIdsByEventId(Long eventId);
    List<Long> getNextDayCustIdsByEventId(Long eventId);
    List<Long> getNextDayCustIdsByEventIdAndConsId(@Param("eventId") Long eventId,@Param("custId") Long custId);
    Integer getCountByEventId(Long eventId);
    Integer getNextDayCountByEventId(Long eventId);

}
