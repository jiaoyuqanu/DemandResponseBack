package com.xqxy.dr.modular.dispatch.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.core.client.SystemClient;
import com.xqxy.dr.modular.dispatch.entity.Dispatch;
import com.xqxy.dr.modular.dispatch.entity.OrgDemand;
import com.xqxy.dr.modular.dispatch.param.OrgDemandParam;
import com.xqxy.dr.modular.dispatch.service.DispatchService;
import com.xqxy.dr.modular.dispatch.service.OrgDemandService;
import com.xqxy.dr.modular.dispatch.mapper.OrgDemandMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Yechs
 * @description 针对表【dr_org_demand(调度指令分解)】的数据库操作Service实现
 * @createDate 2022-06-28 16:42:19
 */
@Service
public class OrgDemandServiceImpl extends ServiceImpl<OrgDemandMapper, OrgDemand> implements OrgDemandService {

    @Resource
    private DispatchService dispatchService;
    @Resource
    private SystemClient systemClient;

    @Override
    public List<OrgDemand> getByRegulateId(Long regulateId) {
        Assert.notNull(regulateId, "调度指令id不能为空");
        Dispatch dispatch = dispatchService.getById(regulateId);
        Assert.notNull(dispatch, "调度指令不存在");
        return this.lambdaQuery().eq(OrgDemand::getRegulateId, regulateId).orderByAsc(OrgDemand::getOrgId).list();
    }

    @Override
    public void saveOrgDemand(OrgDemandParam orgDemandParam) {
        Assert.notNull(orgDemandParam.getRegulateId(), "调度指令id不能为空");
        Assert.notEmpty(orgDemandParam.getOrgDemandInfos(), "调度指令分解详情为空");
        Dispatch dispatch = dispatchService.getById(orgDemandParam.getRegulateId());
        String year = dispatch.getRegulateDate() != null ? String.valueOf(dispatch.getRegulateDate().getYear()) : null;
        Assert.notNull(dispatch, "调度指令不存在");
        this.remove(new LambdaQueryWrapper<OrgDemand>().eq(OrgDemand::getRegulateId, orgDemandParam.getRegulateId()));

        List<OrgDemand> orgDemands = orgDemandParam.getOrgDemandInfos().stream().map(item -> {
            OrgDemand orgDemand = new OrgDemand();
            orgDemand.setOrgId(item.getOrgId());
            orgDemand.setOrgName(item.getOrgName());
            orgDemand.setRegulateId(orgDemandParam.getRegulateId());
            orgDemand.setGoal(item.getGoal());
            orgDemand.setYear(year);
            return orgDemand;
        }).collect(Collectors.toList());
        this.saveBatch(orgDemands);
    }

}




