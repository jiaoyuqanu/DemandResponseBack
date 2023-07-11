package com.xqxy.dr.modular.event.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.dr.modular.event.entity.Plan;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xqxy.dr.modular.event.entity.PlanCons;
import com.xqxy.dr.modular.event.param.DeleteRuleParam;
import com.xqxy.dr.modular.event.param.PlanParam;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 需求响应方案 Mapper 接口
 * </p>
 *
 * @author PengChuqing
 * @since 2021-10-08
 */
public interface PlanMapper extends BaseMapper<Plan> {

    List<PlanCons> getConsIdByCustAndEvent(PlanParam planParam);
    List<PlanCons> getConsIdByCustAndEvent2(PlanParam planParam);
    void saveOutBoxMessage(Map<String,Object> map);
    Integer getOutBoxMessageCount(Map<String,Object> map);

}
