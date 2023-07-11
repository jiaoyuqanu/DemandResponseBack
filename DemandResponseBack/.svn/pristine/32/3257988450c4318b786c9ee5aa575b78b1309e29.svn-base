package com.xqxy.dr.modular.event.mapper;

import com.xqxy.dr.modular.event.entity.PlanBaselineTask;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 事件基线计算任务表，事件创建后插入数据，计算后更新 Mapper 接口
 * </p>
 *
 * @author PengChuqing
 * @since 2021-10-19
 */
public interface PlanBaselineTaskMapper extends BaseMapper<PlanBaselineTask> {
    Integer getCount(PlanBaselineTask planBaselineTask);
    Integer getConsBaseCount(PlanBaselineTask planBaselineTask);
    Integer getCustBaseCount(PlanBaselineTask planBaselineTask);
    void batchBaseLineTask(List<PlanBaselineTask> planBaseLines);

}
