package com.xqxy.dr.modular.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xqxy.dr.modular.project.entity.DrOrgGoal ;
import com.xqxy.dr.modular.workbench.param.WorkbenchParam;

import java.util.List;

/**
 * <p>
 * 各单位任务指标 Mapper 接口
 * </p>
 *
 * @author liqirui
 * @since 2022-01-11
 */
public interface DrOrgGoalMapper extends BaseMapper<DrOrgGoal> {

    /**
     * 查询对应项目的 各单位指标
     * @param workbenchParam
     * @return
     */
    List<DrOrgGoal> groupProjectIdAndOrgNo(WorkbenchParam workbenchParam);
}
