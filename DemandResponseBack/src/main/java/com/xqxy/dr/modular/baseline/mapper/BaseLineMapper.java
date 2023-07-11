package com.xqxy.dr.modular.baseline.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xqxy.dr.modular.baseline.entity.BaseLine;
import com.xqxy.dr.modular.baseline.entity.CustBaseLineDetail;
import com.xqxy.dr.modular.baseline.entity.PlanBaseLine;
import com.xqxy.dr.modular.event.entity.ConsBaseline;

import java.util.List;

/**
 * <p>
 * 基线管理 Mapper 接口
 * </p>
 *
 * @author chen zhi jun
 * @since 2021-10-18
 */
public interface BaseLineMapper extends BaseMapper<BaseLine> {
    List<BaseLine> getBaseLineInfo();

    List<ConsBaseline> getConsBaseLineInfo();

    List<ConsBaseline> getConsBaseLineByBaseLineId(Long baselineId);

    List<ConsBaseline> getConsBaseLineByBaseLineIdAll(Long baselineId);

    List<PlanBaseLine> getPlanBaseLineList();

    List<PlanBaseLine> getPlanBaseLineListAll();

}
