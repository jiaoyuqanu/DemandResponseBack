package com.xqxy.dr.modular.dispatch.service;

import com.xqxy.dr.modular.dispatch.entity.OrgDemand;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.dr.modular.dispatch.param.OrgDemandParam;

import java.util.List;

/**
* @author Yechs
* @description 针对表【dr_org_demand(调度指令分解)】的数据库操作Service
* @createDate 2022-06-28 16:42:19
*/
public interface OrgDemandService extends IService<OrgDemand> {

    List<OrgDemand> getByRegulateId(Long regulateId);

    void saveOrgDemand(OrgDemandParam orgDemandParam);
}
