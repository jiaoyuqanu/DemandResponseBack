package com.xqxy.dr.modular.event.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.core.enums.YesOrNotEnum;
import com.xqxy.core.pojo.login.CurrenUserInfo;
import com.xqxy.core.util.OrganizationUtil;
import com.xqxy.core.util.SecurityUtils;
import com.xqxy.dr.modular.data.entity.ConsCurve;
import com.xqxy.dr.modular.data.param.CustCurveParam;
import com.xqxy.dr.modular.event.entity.Plan;
import com.xqxy.dr.modular.event.entity.PlanCons;
import com.xqxy.dr.modular.event.entity.PlanCust;
import com.xqxy.dr.modular.event.mapper.PlanCustMapper;
import com.xqxy.dr.modular.event.param.PlanCustParam;
import com.xqxy.dr.modular.event.service.PlanCustService;
import com.xqxy.dr.modular.event.service.PlanService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 方案参与客户 服务实现类
 * </p>
 *
 * @author PengChuqing
 * @since 2021-10-09
 */
@Service
public class PlanCustServiceImpl extends ServiceImpl<PlanCustMapper, PlanCust> implements PlanCustService {

    @Resource
    private PlanService planService;

    @Override
    public List<PlanCust> listNotDeleted(PlanCustParam planCustParam) {
        LambdaQueryWrapper<PlanCust> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ne(PlanCust::getDeleted, YesOrNotEnum.Y.getCode());
        if (ObjectUtil.isNotNull(planCustParam)) {
            // 是否剔除
            if (ObjectUtil.isNotEmpty(planCustParam.getPlanId())) {
                queryWrapper.like(PlanCust::getPlanId, planCustParam.getPlanId());
            }
            // 剔除规则
            if (ObjectUtil.isNotEmpty(planCustParam.getDelRule())) {
                queryWrapper.ge(PlanCust::getDelRule, planCustParam.getDelRule());
            }
        }
        //根据排序升序排列，序号越小越在前
        queryWrapper.orderByAsc(PlanCust::getCreateTime);
        return this.list(queryWrapper);
    }

    @Override
    public void eliminateByCustIds(List<Long> aggreListByConsId, Long planId) {
        LambdaUpdateWrapper<PlanCust> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(PlanCust::getDeleted, YesOrNotEnum.Y.getCode());
        updateWrapper.set(PlanCust::getDelRule, null);
        updateWrapper.eq(PlanCust::getPlanId,planId);
        updateWrapper.in(PlanCust::getCustId,aggreListByConsId);
        this.update(updateWrapper);
    }

    @Override
    public List<PlanCust> list(PlanCustParam planCustParam) {
        LambdaQueryWrapper<PlanCust> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(planCustParam)) {
            if (ObjectUtil.isNotEmpty(planCustParam.getPlanId())) {
                queryWrapper.eq(PlanCust::getPlanId, planCustParam.getPlanId());
            }
            // 是否剔除
            if (ObjectUtil.isNotEmpty(planCustParam.getDeleted())) {
                queryWrapper.eq(PlanCust::getDeleted, planCustParam.getDeleted());
            }
            // 剔除规则
            if (ObjectUtil.isNotEmpty(planCustParam.getDelRule())) {
                queryWrapper.eq(PlanCust::getDelRule, planCustParam.getDelRule());
            }
        }
        return this.list(queryWrapper);
    }

    @Override
    public Page<PlanCust> pageCustMonitor(PlanCustParam planCustParam) {
        //数据权限
        CurrenUserInfo currenUserInfo = SecurityUtils.getCurrentUserInfoUTF8();
        if (null == currenUserInfo) {
            return new Page<>();
        }
        String orgNo = currenUserInfo.getOrgId();
        String orgTitle = currenUserInfo.getOrgTitle();
        //机构子集
        List<String> list = new ArrayList<>();
        if (null != orgNo && !"".equals(orgNo)) {
            if (!"1".equals(orgTitle)) {
                //聚合商如果不是省级，直接返回空数据
                return new Page<>();
            }
        } else {
            return new Page<>();
        }
        // 查询事件对应的方案信息
        Plan plan = planService.getByEventId(planCustParam.getEventId());
        if(null!=plan) {
            planCustParam.setPlanId(plan.getPlanId());
        } else {
            return new Page<PlanCust>();
        }
        return getBaseMapper().pageCustMonitor(planCustParam.getPage(),planCustParam);
    }

    @Override
    public ConsCurve getCustCurveToday(CustCurveParam custCurveParam) {
        return getBaseMapper().getCustCurveToday(custCurveParam);
    }

    @Override
    public ConsCurve getCustCurveHistory(CustCurveParam custCurveParam) {
        return getBaseMapper().getCustCurveHistory(custCurveParam);
    }

    @Override
    public List<PlanCust> getPlanNoDe(List<Long> eventIds) {
        return getBaseMapper().getPlanNoDe(eventIds);
    }
}
