package com.xqxy.dr.modular.dispatch.mapper;

import com.xqxy.dr.modular.dispatch.entity.OrgDemand;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xqxy.dr.modular.statistics.param.StatisticalParam;

import java.util.List;

/**
* @author Yechs
* @description 针对表【dr_org_demand(调度指令分解)】的数据库操作Mapper
* @createDate 2022-06-28 16:42:19
* @Entity com.xqxy.dr.modular.dispatch.entity.OrgDemand
*/
public interface OrgDemandMapper extends BaseMapper<OrgDemand> {

    /**
     * 查询 电力缺口 查询条件
     * @param
     * @return
     */
    List<OrgDemand> queryOrgDemandByEventAndProject();
}




