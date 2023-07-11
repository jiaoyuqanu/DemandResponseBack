package com.xqxy.dr.modular.event.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.core.client.SystemClient;
import com.xqxy.core.enums.ApprovalCodeEnum;
import com.xqxy.core.enums.BusTypeEnum;
import com.xqxy.core.enums.CurrenUserInfoExceptionEnum;
import com.xqxy.core.enums.YesOrNotEnum;
import com.xqxy.core.exception.ServiceException;
import com.xqxy.core.pojo.login.CurrenUserInfo;
import com.xqxy.core.util.HttpServletUtil;
import com.xqxy.core.util.SecurityUtils;
import com.xqxy.dr.modular.event.entity.Event;
import com.xqxy.dr.modular.event.entity.Plan;
import com.xqxy.dr.modular.event.entity.PlanCons;
import com.xqxy.dr.modular.event.entity.PlanCust;
import com.xqxy.dr.modular.event.enums.CheckStatusEnum;
import com.xqxy.dr.modular.event.enums.EliminateRulesEnum;
import com.xqxy.dr.modular.event.enums.EventExceptionEnum;
import com.xqxy.dr.modular.event.enums.EventStatusEnum;
import com.xqxy.dr.modular.event.mapper.PlanConsMapper;
import com.xqxy.dr.modular.event.mapper.PlanCustMapper;
import com.xqxy.dr.modular.event.mapper.PlanMapper;
import com.xqxy.dr.modular.event.param.DeleteConsParam;
import com.xqxy.dr.modular.event.param.DeleteRuleParam;
import com.xqxy.dr.modular.event.param.PlanParam;
import com.xqxy.dr.modular.event.result.EliminatedStatistics;
import com.xqxy.dr.modular.event.service.EventService;
import com.xqxy.dr.modular.event.service.PlanConsService;
import com.xqxy.dr.modular.event.service.PlanCustService;
import com.xqxy.dr.modular.event.service.PlanService;
import com.xqxy.dr.modular.project.enums.ExamineExceptionEnum;
import com.xqxy.dr.modular.project.enums.ProjectCheckStatusEnums;
import com.xqxy.dr.modular.project.params.ExamineParam;
import com.xqxy.dr.modular.strategy.CalculateStrategy;
import com.xqxy.dr.modular.strategy.CalculateStrategyContext;
import com.xqxy.sys.modular.cust.entity.Cust;
import com.xqxy.sys.modular.cust.param.BusConfigParam;
import com.xqxy.sys.modular.cust.result.Result;
import com.xqxy.sys.modular.cust.service.CustService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 需求响应方案 服务实现类
 * </p>
 *
 * @author PengChuqing
 * @since 2021-10-08
 */
@Service
public class PlanServiceImpl extends ServiceImpl<PlanMapper, Plan> implements PlanService {

//    @Value("${calculateStrategy}")
    @Value("hunanFinalCalculateStrategy")
    private String calculateStrategyValue;

    @Resource
    private PlanService planService;

    @Resource
    private PlanConsService planConsService;

    @Resource
    private PlanConsMapper planConsMapper;

    @Resource
    private PlanCustService planCustService;

    @Resource
    private SystemClient systemClient;

    @Resource
    private EventService eventService;

    @Resource
    private CalculateStrategyContext calculateStrategyContext;

    @Resource
    private PlanCustMapper planCustMapper;

    @Autowired
    private CustService custService;

    @Override
    public void generatePlan(PlanParam planParam) {
        this.createPlan(planParam);
    }

    /**
     * 生成事件执行方案
     *
     * @param planParam
     */
    private void createPlan(PlanParam planParam) {
        CalculateStrategy calculateStrategy = calculateStrategyContext.strategySelect(calculateStrategyValue);
        calculateStrategy.createPlan(planParam);
    }

    /*@Override
    public Page<PlanCons> eliminate(PlanParam planParam) {
        // String deleteRule = planParam.getDeleteRule();
        List<String> deleteRuleList = planParam.getDeleteRuleList();
        EliminatedStatistics eliminatedStatistics = new EliminatedStatistics();
        // 查询事件对应的方案
        DeleteRuleParam deleteRuleParam = new DeleteRuleParam();
        deleteRuleParam.setDeleted(YesOrNotEnum.N.getCode());
        Plan plan = planService.getByEventId(planParam.getEventId());
        deleteRuleParam.setPlanId(plan.getPlanId());
        for (String deleteRule : deleteRuleList) {
            if (deleteRule.equals(EliminateRulesEnum.BASELINE_EMPTY.getCode())) {
                // 基线为空
                deleteRuleParam.setBaselineEmpty(EliminateRulesEnum.BASELINE_EMPTY.getCode());
            }
            if (deleteRule.equals(EliminateRulesEnum.LESS_THAN_TEN.getCode())) {
                // 平均基线负荷小于10kW
                deleteRuleParam.setLessThanTen(EliminateRulesEnum.LESS_THAN_TEN.getCode());
            }
            if (deleteRule.equals(EliminateRulesEnum.LESS_THAN_REPLYCAP.getCode())) {
                // 平均基线负荷小于可响应容量
                deleteRuleParam.setLessThanReplycap(EliminateRulesEnum.LESS_THAN_REPLYCAP.getCode());
            }
            if (deleteRule.equals(EliminateRulesEnum.REGION_RULE.getCode())) {
                // 分局范围剔除
                if (planParam.getRangeType().equals(RangeTypeEnum.ADMINISTRATIVE_REGION)) {
                    // 行政区域
                    DeleteRuleParam deleteRuleParamGov = this.listPlanConsByRegionCode(plan.getPlanId(), planParam.getRegulateRange());
                    BeanUtil.copyProperties(deleteRuleParamGov,deleteRuleParam);
                }
                if (planParam.getRangeType().equals(RangeTypeEnum.ELECTRICIC_REGION)) {
                    // 供电分区
                    DeleteRuleParam deleteRuleParamOrg = this.listPlanConsByOrgNo(plan.getPlanId(), planParam.getRegulateRange());
                    BeanUtil.copyProperties(deleteRuleParamOrg,deleteRuleParam);
                }
            }
        }
        Page<PlanCons> planConsPage = planConsService.pageByDeleteRule(deleteRuleParam);
        return planConsPage;
    }*/


    @Transactional
    @Override
    public Page<PlanCons> eliminate(PlanParam planParam) {
        // String deleteRule = planParam.getDeleteRule();
        List<String> deleteRuleList = planParam.getDeleteRuleList();
        //EliminatedStatistics eliminatedStatistics = new EliminatedStatistics();
        // 没有选择剔除规则，直接返回空数据
        if(CollectionUtil.isEmpty(planParam.getDeleteRuleList())) {
            int pageSize = 20;
            int pageNo = 1;
            HttpServletRequest request = HttpServletUtil.getRequest();
            //每页条数
            String pageSizeString = request.getParameter("pageSize");
            if (ObjectUtil.isNotEmpty(pageSizeString)) {
                pageSize = Integer.parseInt(pageSizeString);
            }
            //第几页
            String pageNoString = request.getParameter("pageNo");
            if (ObjectUtil.isNotEmpty(pageNoString)) {
                pageNo = Integer.parseInt(pageNoString);
            }
            return new Page<>(pageNo,pageSize);
        }
        // 查询事件对应的方案
        Plan plan = planService.getByEventId(planParam.getEventId());
        if(null==plan) {
            return new Page<>();
        }
        if(null!=deleteRuleList && deleteRuleList.size()>0) {
            for (String deleteRule : deleteRuleList) {
                if (deleteRule.equals(EliminateRulesEnum.BASELINE_EMPTY.getCode())) {
                    // 基线为空
                    List<PlanCons> planCons = planConsService.listWithEmptyBaseline(plan.getPlanId());
//            BigDecimal demandCapSum = planCons.stream().map(PlanCons::getDemandCap).reduce(BigDecimal.ZERO, BigDecimal::add);
                    //            eliminatedStatistics.setSumDeletedCap(demandCapSum);
                    List<String> consIdList = planCons.stream().map(PlanCons::getConsId).collect(Collectors.toList());
                    if (CollectionUtil.isNotEmpty(consIdList)) {
                        planConsService.eliminateManual(consIdList, planParam.getEventId(), EliminateRulesEnum.BASELINE_EMPTY.getCode());
                    }
               /* Long count = planCons.stream().count();
                if (ObjectUtil.isNull(eliminatedStatistics.getConsNum())) {
                    eliminatedStatistics.setConsNum(count);
                } else {
                    eliminatedStatistics.setConsNum(eliminatedStatistics.getConsNum() + count);
                }
                if (CollectionUtil.isEmpty(eliminatedStatistics.getPlanConsList())) {
                    eliminatedStatistics.setPlanConsList(planCons);
                } else {
                    eliminatedStatistics.getPlanConsList().addAll(planCons);
                    eliminatedStatistics.setPlanConsList(eliminatedStatistics.getPlanConsList());
                }*/

                }
                if (EliminateRulesEnum.LESS_THAN_TEN.getCode().equals(deleteRule)) {
                    // 平均基线负荷小于10kW
                    BigDecimal ten = new BigDecimal("10");
                    List<PlanCons> planCons = planConsService.listWithCapLessThan(plan.getPlanId(), ten);
                    List<String> consIdList = planCons.stream().map(PlanCons::getConsId).collect(Collectors.toList());
                    if (CollectionUtil.isNotEmpty(consIdList)) {
                        planConsService.eliminateManual(consIdList, planParam.getEventId(), EliminateRulesEnum.LESS_THAN_TEN.getCode());
                    }
                }
                if (EliminateRulesEnum.LESS_THAN_REPLYCAP.getCode().equals(deleteRule)) {
                    // 平均基线负荷小于可响应容量
                    List<PlanCons> planCons = planConsService.listLessThanDemandCap(plan.getPlanId());
                    List<String> consIdList = planCons.stream().map(PlanCons::getConsId).collect(Collectors.toList());
                    if (CollectionUtil.isNotEmpty(consIdList)) {
                        planConsService.eliminateManual(consIdList, planParam.getEventId(), EliminateRulesEnum.LESS_THAN_REPLYCAP.getCode());
                    }
                }
           /* if (deleteRule.equals(EliminateRulesEnum.REGION_RULE.getCode())) {
                // 分局范围剔除
                EliminatedStatistics tempStatistics = new EliminatedStatistics();

                if (planParam.getRangeType().equals(RangeTypeEnum.ADMINISTRATIVE_REGION)) {
                    // 行政区域
                    tempStatistics = this.listPlanConsByRegionCode(plan.getPlanId(), planParam.getRegulateRange());
                }
                if (planParam.getRangeType().equals(RangeTypeEnum.ELECTRICIC_REGION)) {
                    // 供电分区
                    tempStatistics = this.listPlanConsByOrgNo(plan.getPlanId(), planParam.getRegulateRange());
                }
                if (ObjectUtil.isNull(eliminatedStatistics.getConsNum())) {
                    eliminatedStatistics.setConsNum(tempStatistics.getConsNum());
                } else {
                    eliminatedStatistics.setConsNum(eliminatedStatistics.getConsNum() + tempStatistics.getConsNum());
                }
                if (CollectionUtil.isEmpty(eliminatedStatistics.getPlanConsList())) {
                    eliminatedStatistics.setPlanConsList(tempStatistics.getPlanConsList());
                } else {
                    eliminatedStatistics.getPlanConsList().addAll(tempStatistics.getPlanConsList());
                    eliminatedStatistics.setPlanConsList(eliminatedStatistics.getPlanConsList());
                }
            }*/
            }
        }
        DeleteRuleParam deleteRuleParam = new DeleteRuleParam();
        deleteRuleParam.setDeleted(YesOrNotEnum.Y.getCode());
        deleteRuleParam.setPlanId(plan.getPlanId());
        deleteRuleParam.setDelRuleList(deleteRuleList);
        Page<PlanCons> planConsPage = planConsService.pageConfirmed(deleteRuleParam);
        return planConsPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchInvation(PlanParam planParam) {
        //校验是否可执行
        checkStatus(planParam.getEventId());
        List<PlanCons> planConsList = new ArrayList<>();
        Plan plan = null;
        List<String> consIdList = new ArrayList<>();
        List<PlanCust> planCustList = new ArrayList<>();
        if(null!=planParam) {
            plan = planService.getByEventId(planParam.getEventId());
            if (null == plan) {
                throw new ServiceException(EventExceptionEnum.EVENT_PLAN_NOT_EXIST);
            }
            if (null != planParam.getIds() && planParam.getIds().size() > 0) {
                LambdaQueryWrapper<PlanCons> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.eq(PlanCons::getPlanId,plan.getPlanId());
                //根据方案查询所有方案用户
                List<PlanCons> planConss = planConsService.list(lambdaQueryWrapper);
                for (Long data : planParam.getIds()) {
                    if (null != data) {
                        PlanCons planCons = new PlanCons();
                        planCons.setDeleted("N");
                        planCons.setDelRule("");
                        planCons.setParticId(data);
                        planConsList.add(planCons);
                        //设置用户效果评估任务状态
                        List<PlanCons>  planConsList1 = planConss.stream().filter(plan1 -> plan1.getParticId().equals(data))
                                .collect(Collectors.toList());
                        if(null!=planConsList1 && planConsList1.size()>0) {
                            consIdList.add(planConsList1.get(0).getConsId());
                        }
                    }
                }
            }
        }
        if(planConsList.size()>0) {
            LambdaQueryWrapper<PlanCust> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(PlanCust::getPlanId,plan.getPlanId());
            List<PlanCust> planCustList1 = planCustService.list(lambdaQueryWrapper);
            planConsService.updateBatchById(planConsList);
            //用户状态变为邀约，其客户状态也修改为邀约
            Map<String,Object> map = new HashMap<>();
            map.put("consIdList",consIdList);
            map.put("eventId",planParam.getEventId());
            map.put("deleted","N");
            List<PlanCust> planCusts = planConsMapper.getDeletedCount(map);
            if(null!=planCusts && planCusts.size()>0) {
                for(PlanCust planCust : planCusts) {
                    if(planCust.getCount()>0) {
                        planCust.setPlanId(plan.getPlanId());
                        planCust.setDeleted("N");
                        if(null==planParam.getDeleteRule()) {
                            planParam.setDeleteRule("");
                        }
                        List<PlanCust> planCustList2 = planCustList1.stream().filter(con ->
                                planCust.getCustId().equals(con.getCustId())).collect(Collectors.toList());
                        planCust.setDelRule(planParam.getDeleteRule());
                        if(null!=planCustList2 && planCustList2.size()>0) {
                            planCust.setParticId(planCustList2.get(0).getParticId());
                            planCustList.add(planCust);
                        }
                    }
                }
            }
        }
        //更新客户剔除状态
        if(null!=planCustList && planCustList.size()>0) {
            planCustMapper.batchUpdateDelete(planCustList);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchCustInvation(PlanParam planParam) {
        //校验是否可执行
        checkStatus(planParam.getEventId());
        List<PlanCons> planConsList = new ArrayList<>();
        List<PlanCust> planCusts = new ArrayList<>();
        if(null!=planParam) {
            Plan plan = planService.getByEventId(planParam.getEventId());
            if (null == plan) {
                throw new ServiceException(EventExceptionEnum.EVENT_PLAN_NOT_EXIST);
            }
            if (null != planParam.getIds() && planParam.getIds().size() > 0) {
                for (Long data : planParam.getIds()) {
                    if (null != data) {
                        PlanCust planCust = new PlanCust();
                        planCust.setDeleted("N");
                        if(null==planParam.getDeleteRule()) {
                            planParam.setDeleteRule("");
                        }
                        planCust.setDelRule(planParam.getDeleteRule());
                        planCust.setParticId(data);
                        planCusts.add(planCust);
                    }
                }
            }
            //查询集成商下代理用户
            planConsList = baseMapper.getConsIdByCustAndEvent2(planParam);
            if(null==planConsList) {
                planConsList=new ArrayList<>();
            } else if(planConsList.size()>0) {
                for(PlanCons planCon : planConsList) {
                    planCon.setDeleted("N");
                    planCon.setDelRule("");
                }
            }
        }

        if(planCusts.size()>0) {
            planCustService.updateBatchById(planCusts);
        }

        if(planConsList.size()>0) {
            planConsService.updateBatchById(planConsList);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteInvation(PlanParam planParam) {
        //校验是否可执行
        //checkStatus(planParam.getEventId());
        List<PlanCons> planConsList = new ArrayList<>();
        Plan plan = null;
        List<String> consIdList = new ArrayList<>();
        List<PlanCust> planCustList = new ArrayList<>();
        if(null!=planParam) {
            plan = planService.getByEventId(planParam.getEventId());
            if (null == plan) {
                throw new ServiceException(EventExceptionEnum.EVENT_PLAN_NOT_EXIST);
            }
            DeleteConsParam deleteConsParam = new DeleteConsParam();
            deleteConsParam.setDelRule(planParam.getDeleteRule());
            deleteConsParam.setEventId(planParam.getEventId().toString());
            //判断是否选中用户，没选删除所有，选择删除选中的
            if(null==planParam.getIds() || planParam.getIds().size()==0) {
                //删除所有的
                List<PlanCons> ids = planConsMapper.consDeleteIdList(deleteConsParam);
                if (null != ids && ids.size() > 0) {
                    for (PlanCons data : ids) {
                        if (null != data) {
                            PlanCons planCons = new PlanCons();
                            planCons.setDeleted("Y");
                            if(null==planParam.getDeleteRule()) {
                                planParam.setDeleteRule("");
                            }
                            planCons.setDelRule(planParam.getDeleteRule());
                            planCons.setParticId(data.getParticId());
                            planConsList.add(planCons);
                            //设置用户id
                            consIdList.add(data.getConsId());
                        }
                    }
                }
            } else {
                //删除选中的
                LambdaQueryWrapper<PlanCons> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.eq(PlanCons::getPlanId,plan.getPlanId());
                //根据方案查询所有方案用户
                List<PlanCons> planConss = planConsService.list(lambdaQueryWrapper);
                for (Long data : planParam.getIds()) {
                    if (null != data) {
                        PlanCons planCons = new PlanCons();
                        planCons.setDeleted("Y");
                        if(null==planParam.getDeleteRule()) {
                            planParam.setDeleteRule("");
                        }
                        planCons.setDelRule(planParam.getDeleteRule());
                        planCons.setParticId(data);
                        planConsList.add(planCons);
                        //设置用户效果评估任务状态
                        List<PlanCons>  planConsList1 = planConss.stream().filter(plan1 -> plan1.getParticId().equals(data))
                                .collect(Collectors.toList());
                        if(null!=planConsList1 && planConsList1.size()>0) {
                            consIdList.add(planConsList1.get(0).getConsId());
                        }
                    }
                }
            }
        }
        if(planConsList.size()>0) {
            LambdaQueryWrapper<PlanCust> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(PlanCust::getPlanId,plan.getPlanId());
            List<PlanCust> planCustList1 = planCustService.list(lambdaQueryWrapper);
            planCustMapper.batchUpdateDeleteCons(planConsList);
            //用户状态变为删除，其客户所有用户如果都被删除，客户状态也修改为删除
            Map<String,Object> map = new HashMap<>();
            map.put("consIdList",consIdList);
            map.put("eventId",planParam.getEventId());
            map.put("deleted","N");
            List<PlanCust> planCusts = planConsMapper.getDeletedCount(map);
            if(null!=planCusts && planCusts.size()>0) {
                for(PlanCust planCust : planCusts) {
                    if(planCust.getCount()==0) {
                        planCust.setPlanId(plan.getPlanId());
                        planCust.setDeleted("Y");
                        if(null==planParam.getDeleteRule()) {
                            planParam.setDeleteRule("");
                        }
                        List<PlanCust> planCustList2 = planCustList1.stream().filter(con ->
                                planCust.getCustId().equals(con.getCustId())).collect(Collectors.toList());
                        planCust.setDelRule(planParam.getDeleteRule());
                        if(null!=planCustList2 && planCustList2.size()>0) {
                            planCust.setParticId(planCustList2.get(0).getParticId());
                            planCustList.add(planCust);
                        }
                    }
                }
            }
        }
        //更新客户剔除状态
        if(null!=planCustList && planCustList.size()>0) {
            planCustMapper.batchUpdateDelete(planCustList);
        }
    }
    public Boolean checkStatus(Long eventId) {
        Event event = eventService.getById(eventId);
        if(null!=event) {
            //仅待执行状态可剔除规则,且事件未开始
            if(!"02".equals(event.getEventStatus())) {
                throw new ServiceException(EventExceptionEnum.EVENT_EXECUTE_STATE);
            }
            Integer endHour = 0;
            Integer endMinute = 0;
            if (null != event.getStartTime()) {
                endHour = Integer.parseInt(event.getStartTime().substring(0, 2));
                endMinute = Integer.parseInt(event.getStartTime().substring(3));
            }
            LocalDate localDate = LocalDate.now();
            LocalTime startTime = LocalTime.of(endHour, endMinute);
            if (event.getRegulateDate().compareTo(localDate) == 0) {
                if (LocalTime.now().compareTo(startTime) > 0) {
                    throw new ServiceException(EventExceptionEnum.EVENT_BEGIN);
                }
            } else if (event.getRegulateDate().compareTo(localDate) < 0) {
                throw new ServiceException(EventExceptionEnum.EVENT_BEGIN);
            }
        } else {
            throw new ServiceException(EventExceptionEnum.EVENT_NOT_EXIST);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCustInvation(PlanParam planParam) {
        //校验是否可执行
        checkStatus(planParam.getEventId());
        List<PlanCons> planConsList = new ArrayList<>();
        List<PlanCust> planCusts = new ArrayList<>();
        Plan plan = null;
        if(null!=planParam) {
            plan = planService.getByEventId(planParam.getEventId());
            if (null == plan) {
                throw new ServiceException(EventExceptionEnum.EVENT_PLAN_NOT_EXIST);
            }
            if (null != planParam.getIds() && planParam.getIds().size() > 0) {
                for (Long data : planParam.getIds()) {
                    if (null != data) {
                        PlanCust planCust = new PlanCust();
                        planCust.setDeleted("Y");
                        planCust.setDelRule(planParam.getDeleteRule());
                        planCust.setParticId(data);
                        planCusts.add(planCust);
                    }
                }
            }
            if(null!=planParam.getCustIds()) {
                planParam.setCustIdList(planParam.getCustIds());
            }
            //查询集成商下代理用户
            planConsList = baseMapper.getConsIdByCustAndEvent2(planParam);
            if(null==planConsList) {
                planConsList=new ArrayList<>();
            } else if(planConsList.size()>0) {
                for(PlanCons planCon : planConsList) {
                    planCon.setDeleted("Y");
                    planCon.setDelRule(planParam.getDeleteRule());
                }
            }
        }

        if(planCusts.size()>0) {
            planCustService.updateBatchById(planCusts);
        }

        if(planConsList.size()>0) {
            planConsService.updateBatchById(planConsList);
        }
    }

    private EliminatedStatistics listPlanConsByOrgNo(Long planId, String regulateRange) {
        JSONArray jsonArray = new JSONArray();
        List<String> list = new ArrayList<>();
        jsonArray = JSONArray.parseArray(regulateRange);
        if (null != jsonArray && jsonArray.size() > 0) {
            for (int j = 0; j < jsonArray.size(); j++) {
                JSONArray value = jsonArray.getJSONArray(j);
                if (null != value && value.size() > 0) {
                    for (int i = 0; i < value.size(); i++) {
                        list.add(value.get(i).toString());
                    }
                }
            }
        }
        /*DeleteRuleParam deleteRuleParam = new DeleteRuleParam();
        deleteRuleParam.setRegulateRangeList(list);*/
        EliminatedStatistics eliminatedStatistics = new EliminatedStatistics();
        List<PlanCons> planCons = planConsService.listByOrgNo(planId, list);
//        BigDecimal demandCapSum = planCons.stream().map(PlanCons::getDemandCap).reduce(BigDecimal.ZERO, BigDecimal::add);
        Long count = planCons.stream().count();
        eliminatedStatistics.setConsNum(count);
//        eliminatedStatistics.setSumDeletedCap(demandCapSum);
        eliminatedStatistics.setPlanConsList(planCons);
        return eliminatedStatistics;
    }

    private EliminatedStatistics listPlanConsByRegionCode(Long planId, String regulateRange) {
        JSONArray jsonArray = new JSONArray();
        List<String> provinceList = new ArrayList<>();
        List<String> cityList = new ArrayList<>();
        List<String> countyList = new ArrayList<>();
        jsonArray = JSONArray.parseArray(regulateRange);
        if (null != jsonArray && jsonArray.size() > 0) {
            for (int j = 0; j < jsonArray.size(); j++) {
                JSONArray value = jsonArray.getJSONArray(j);
                if (null != value && value.size() > 0) {
                    for (int i = 0; i < value.size(); i++) {
                        if (i == 0) {
                            provinceList.add(value.get(i).toString());
                        } else if (i == 1) {
                            cityList.add(value.get(i).toString());
                        } else {
                            countyList.add(value.get(i).toString());
                        }
                    }
                }
            }
        }
        /*DeleteRuleParam deleteRuleParam = new DeleteRuleParam();
        deleteRuleParam.setProvinceList(provinceList);
        deleteRuleParam.setCityList(cityList);
        deleteRuleParam.setCountyList(countyList);*/
        planConsService.listByGovCode(planId, provinceList, cityList, countyList);
        EliminatedStatistics eliminatedStatistics = new EliminatedStatistics();
       /* if(regionLevel.equals(RegionLevelEnum.LEVEL_ONE.getCode())) {
            // 省
            List<PlanCons> planCons = planConsService.listByProvinceCode(planId,regulateRange);
//            BigDecimal demandCapSum = planCons.stream().map(PlanCons::getDemandCap).reduce(BigDecimal.ZERO, BigDecimal::add);
            Long count = planCons.stream().count();
            eliminatedStatistics.setConsNum(count);
//            eliminatedStatistics.setSumDeletedCap(demandCapSum);
            eliminatedStatistics.setPlanConsList(planCons);
        }
        if(regionLevel.equals(RegionLevelEnum.LEVEL_TWO.getCode())) {
            // 市
            List<PlanCons> planCons = planConsService.listByCityCode(planId,regulateRange);
//            BigDecimal demandCapSum = planCons.stream().map(PlanCons::getDemandCap).reduce(BigDecimal.ZERO, BigDecimal::add);
            Long count = planCons.stream().count();
            eliminatedStatistics.setConsNum(count);
//            eliminatedStatistics.setSumDeletedCap(demandCapSum);
            eliminatedStatistics.setPlanConsList(planCons);
        }
        if(regionLevel.equals(RegionLevelEnum.LEVEL_THREE.getCode())) {
            // 区县
            List<PlanCons> planCons = planConsService.listByCountyCode(planId,regulateRange);
//            BigDecimal demandCapSum = planCons.stream().map(PlanCons::getDemandCap).reduce(BigDecimal.ZERO, BigDecimal::add);
            Long count = planCons.stream().count();
            eliminatedStatistics.setConsNum(count);
//            eliminatedStatistics.setSumDeletedCap(demandCapSum);
            eliminatedStatistics.setPlanConsList(planCons);
        }*/
        return eliminatedStatistics;
    }

    @Override
    public void eliminateManual(PlanParam planParam) {
        if (CollectionUtil.isEmpty(planParam.getConsIdList())) return;
        planConsService.eliminateManual(planParam.getConsIdList(), planParam.getEventId(),planParam.getDeleteRule());
    }

    @Override
    public Page<PlanCons> confirmManual(PlanParam planParam) {
        //校验是否可执行
        checkStatus(planParam.getEventId());
        if (CollectionUtil.isEmpty(planParam.getConsIdList())) return null;
        List<PlanCons> planCons = planConsService.listByConsIds(planParam.getConsIdList(),planParam.getEventId());
        List<String> consIdList = planCons.stream().map(PlanCons::getConsId).collect(Collectors.toList());
        if(CollectionUtil.isNotEmpty(consIdList)) {
            planConsService.eliminateManual(consIdList,planParam.getEventId(),EliminateRulesEnum.BASELINE_EMPTY.getCode()); }
       /*  BigDecimal demandCapSum = planCons.stream().map(PlanCons::getDemandCap).reduce(BigDecimal.ZERO, BigDecimal::add);
        Long count = planCons.stream().count();
        eliminatedStatistics.setConsNum(count);
         eliminatedStatistics.setSumDeletedCap(demandCapSum);
        eliminatedStatistics.setPlanConsList(planCons);
        return eliminatedStatistics;*/
        Plan plan = this.getByEventId(planParam.getEventId());
        DeleteRuleParam deleteRuleParam = new DeleteRuleParam();
        deleteRuleParam.setDeleted(YesOrNotEnum.N.getCode());
        deleteRuleParam.setPlanId(plan.getPlanId());
        List<String> ruleList = new ArrayList<>();
        ruleList.add(EliminateRulesEnum.MANUAL_RULE.getCode());
        deleteRuleParam.setDelRuleList(ruleList);
        Page<PlanCons> planConsPage = planConsService.pageConfirmed(deleteRuleParam);
        return planConsPage;
    }

    @Override
    public Plan getByEventId(Long eventId) {
        LambdaQueryWrapper<Plan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Plan::getRegulateId, eventId);
        List<Plan> list = this.list(queryWrapper);
        if (CollectionUtil.isNotEmpty(list)) return list.get(0);
        return null;
    }

    @Override
    public void removePlanByEventId(Long eventId) {
        LambdaQueryWrapper<Plan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Plan::getRegulateId, eventId);
        List<Plan> list = this.list(queryWrapper);
        if (CollectionUtil.isNotEmpty(list)) {
            // 删除方案参与用户信息
            //List<Long> collect = list.stream().map(Plan::getPlanId).collect(Collectors.toList());
            //planConsService.removeByIds(collect);
            LambdaQueryWrapper<PlanCons> planConsLambdaQueryWrapper = new LambdaQueryWrapper<>();
            planConsLambdaQueryWrapper.eq(PlanCons::getPlanId, list.get(0).getPlanId());
            planConsService.remove(planConsLambdaQueryWrapper);

            LambdaQueryWrapper<PlanCust> planCustLambdaQueryWrapper = new LambdaQueryWrapper<>();
            planCustLambdaQueryWrapper.eq(PlanCust::getPlanId, list.get(0).getPlanId());
            planCustService.remove(planCustLambdaQueryWrapper);
        }
        this.remove(queryWrapper);
    }

    @Transactional
    @Override
    public void submit(PlanParam planParam) {
        BusConfigParam busConfigParam = new BusConfigParam();
        // 暂时不行
        // CurrenUserInfo sysLoginUser = LoginContextHolder.me().getSysLoginUser();
        CurrenUserInfo currentUserInfo = SecurityUtils.getCurrentUserInfoUTF8();
        if (ObjectUtil.isNull(currentUserInfo)) {
            throw new ServiceException(CurrenUserInfoExceptionEnum.CURRENT_USER_INFO_NOT_EXIST);
        }
        String infoId = currentUserInfo.getId();
        if(infoId != null){
            Long custId = Long.valueOf(infoId);
            if(custId != null){
                Cust cust = custService.getById(custId);
                if(cust != null){
                    busConfigParam.setApplyManName(cust.getApplyName());
                }
            }
        }

        busConfigParam.setBusId(String.valueOf(planParam.getPlanId()));
        busConfigParam.setApplyOrgId(currentUserInfo.getOrgId()); // 申请人组织机构id
        busConfigParam.setBusType(BusTypeEnum.PLAN_PROCESS.getCode()); // 业务类型
        busConfigParam.setApplyManId(Long.parseLong(currentUserInfo.getId())); // 申请人id
        busConfigParam.setLevel(1);
        busConfigParam.setOperaManId(currentUserInfo.getId());

        Result result = systemClient.selectInfo(busConfigParam);
        if (ObjectUtil.isNotNull(result) && result.getCode().equals("000000") && result.getData().getString("handleCode").equals("000")) {
            // 创建待办任务成功，否则为失败
            // 修改项目的状态为提交审核
            Plan plan = new Plan();
            plan.setPlanId(planParam.getPlanId());
            plan.setCheckStatus(ProjectCheckStatusEnums.UNDER_REVIEW.getCode());
            this.updateById(plan);
            // 同步修改事件的状态
            Plan sourcePlan = this.getById(planParam.getPlanId());
            Event event = new Event();
            event.setEventId(sourcePlan.getRegulateId());
            event.setCheckStatus(CheckStatusEnum.UNDER_REVIEW.getCode());
            eventService.updateById(event);
            return;
        }
        // 创建失败，抛出异常
        throw new ServiceException(ExamineExceptionEnum.CREATION_FAILED);
    }

    @Override
    public void examine(ExamineParam examineParam) {
        BusConfigParam busConfigParam = new BusConfigParam();
        BeanUtil.copyProperties(examineParam, busConfigParam);
        // 暂时不行
        // CurrenUserInfo sysLoginUser = LoginContextHolder.me().getSysLoginUser();
        CurrenUserInfo currentUserInfo = SecurityUtils.getCurrentUserInfoUTF8();
        if (ObjectUtil.isNull(currentUserInfo)) {
            throw new ServiceException(CurrenUserInfoExceptionEnum.CURRENT_USER_INFO_NOT_EXIST);
        }

        busConfigParam.setOperaManId(currentUserInfo.getId()); // 操作人id

        Result result = systemClient.selectInfo(busConfigParam);
        if (ObjectUtil.isNotNull(result) && result.getCode().equals("000000")) {
            if (result.getData().getString("handleCode").equals(ApprovalCodeEnum.PROCESS_FAIL.getCode())) {
                // 审核流程失败
                throw new ServiceException(ExamineExceptionEnum.APPROVAL_FAIL);
            }
            // 修改项目的状态
            Plan plan = new Plan();
            plan.setPlanId(Long.parseLong(examineParam.getBusId()));

            // 审核结束
            if (result.getData().getString("handleCode").equals(ApprovalCodeEnum.PROCESS_SUCCESS.getCode())) {
                plan.setCheckStatus(ProjectCheckStatusEnums.PASS_THE_AUDIT.getCode());
                // 同步修改事件的状态
                Plan sourcePlan = this.getById(examineParam.getBusId());
                Event event = new Event();
                event.setEventId(sourcePlan.getRegulateId());
                event.setEventStatus(EventStatusEnum.STATUS_REVIEW.getCode());
                event.setCheckStatus(CheckStatusEnum.PASS_THE_AUDIT.getCode());
                eventService.updateById(event);
            }
            // 申请被驳回
            if (result.getData().getString("handleCode").equals(ApprovalCodeEnum.APPROVAL_REJECT.getCode())) {
                plan.setCheckStatus(ProjectCheckStatusEnums.AUDIT_FAILED.getCode());
                // 同步修改事件的状态
                Plan sourcePlan = this.getById(examineParam.getBusId());
                Event event = new Event();
                event.setEventId(sourcePlan.getRegulateId());
                event.setCheckStatus(CheckStatusEnum.AUDIT_FAILED.getCode());
                eventService.updateById(event);
            }
            this.updateById(plan);
            return;
        }
        // 接口状态码不为000000，抛出异常
        throw new ServiceException(ExamineExceptionEnum.CREATION_FAILED);
    }


}
