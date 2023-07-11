package com.xqxy.dr.modular.event.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.core.client.SmsSendCilent;
import com.xqxy.core.client.SystemClient;
import com.xqxy.core.client.SystemClientService;
import com.xqxy.core.enums.YesOrNotEnum;
import com.xqxy.core.exception.ServiceException;
import com.xqxy.core.pojo.login.CurrenUserInfo;
import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.core.util.OrganizationUtil;
import com.xqxy.core.util.SecurityUtils;
import com.xqxy.dr.modular.baseline.entity.CustBaseLineDetail;
import com.xqxy.dr.modular.baseline.service.CustBaseLineDetailService;
import com.xqxy.dr.modular.baseline.util.CurveUtil;
import com.xqxy.dr.modular.data.entity.ConsCurve;
import com.xqxy.dr.modular.data.entity.ConsCurveToday;
import com.xqxy.dr.modular.data.param.ConsCurveParam;
import com.xqxy.dr.modular.data.param.CustCurveParam;
import com.xqxy.dr.modular.data.service.ConsCurveService;
import com.xqxy.dr.modular.data.service.ConsCurveTodayService;
import com.xqxy.dr.modular.device.entity.SysOrgs;
import com.xqxy.dr.modular.dispatch.entity.OrgDemand;
import com.xqxy.dr.modular.dispatch.service.OrgDemandService;
import com.xqxy.dr.modular.evaluation.entity.CustEvaluation;
import com.xqxy.dr.modular.evaluation.entity.EvaluCustTask;
import com.xqxy.dr.modular.evaluation.entity.EvaluTask;
import com.xqxy.dr.modular.evaluation.service.CustEvaluationService;
import com.xqxy.dr.modular.evaluation.service.EvaluCustTaskService;
import com.xqxy.dr.modular.evaluation.service.EvaluTaskService;
import com.xqxy.dr.modular.event.VO.PlanConsMonitorExportVO;
import com.xqxy.dr.modular.event.entity.*;
import com.xqxy.dr.modular.event.enums.EventExceptionEnum;
import com.xqxy.dr.modular.event.enums.EventResponseTypeEnum;
import com.xqxy.dr.modular.event.enums.PlanExecuteEnum;
import com.xqxy.dr.modular.event.mapper.PlanConsMapper;
import com.xqxy.dr.modular.event.mapper.PlanCustMapper;
import com.xqxy.dr.modular.event.mapper.PlanMapper;
import com.xqxy.dr.modular.event.param.*;
import com.xqxy.dr.modular.event.result.*;
import com.xqxy.dr.modular.event.service.*;
import com.xqxy.dr.modular.event.utils.OrgUtils;
import com.xqxy.dr.modular.project.entity.ConsContractDetail;
import com.xqxy.dr.modular.project.entity.ConsContractInfo;
import com.xqxy.dr.modular.project.entity.DrOrgGoal;
import com.xqxy.dr.modular.project.service.ConsContractDetailService;
import com.xqxy.dr.modular.project.service.ConsContractInfoService;
import com.xqxy.dr.modular.strategy.Utils.StrategyUtils;
import com.xqxy.sys.modular.cust.entity.Cons;
import com.xqxy.sys.modular.cust.entity.Cust;
import com.xqxy.sys.modular.cust.enums.OrgTitleEnum;
import com.xqxy.sys.modular.cust.result.Result;
import com.xqxy.sys.modular.cust.service.ConsService;
import com.xqxy.sys.modular.cust.service.CustService;
import com.xqxy.sys.modular.sms.entity.SysSmsSend;
import com.xqxy.sys.modular.sms.param.SmsSendParam;
import com.xqxy.sys.modular.sms.service.impl.SysSmsSendServiceImpl;
import com.xqxy.sys.modular.utils.CityAndCountyUtils;
import com.xqxy.sys.modular.utils.JSONObjectToEntityUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * <p>
 * 方案参与用户 服务实现类
 * </p>
 *
 * @author PengChuqing
 * @since 2021-10-09
 */
@Service
public class PlanConsServiceImpl extends ServiceImpl<PlanConsMapper, PlanCons> implements PlanConsService {

    @Resource
    private PlanService planService;

    @Resource
    private ConsBaselineService consBaselineService;

    @Resource
    private ConsBaselineAllService consBaselineAllService;

    @Resource
    private ConsCurveTodayService consCurveTodayService;

    @Resource
    private ConsCurveService consCurveService;

    @Resource
    private EventService eventService;

    @Resource
    private PlanConsService planConsService;

    @Resource
    private EventExecuteService eventExecuteService;

    @Resource
    private PlanCustService planCustService;

    @Resource
    private CustService custService;

    @Resource
    private DrPlanImplemRecService drPlanImplemRecService;

    @Resource
    private DrPlanCustRecService drPlanCustRecService;

    @Resource
    private DrPlanConsRecService drPlanConsRecService;

    @Resource
    private PlanMapper planMapper;

    @Resource
    private ConsService consService;
    @Resource
    private EvaluTaskService evaluTaskService;
    @Resource
    private CustBaseLineDetailService custBaseLineDetailService;
    @Resource
    private CustEvaluationService custEvaluationService;
    @Resource
    private ConsContractInfoService consContractInfoService;
    @Resource
    private ConsContractDetailService consContractDetailService;
    @Resource
    private PlanCustMapper planCustMapper;
    @Resource
    private EvaluCustTaskService evaluCustTaskService;
    @Resource
    private EventMonitorTaskService eventMonitorTaskService;

    @Resource
    private SmsSendCilent smsSendCilent;

    @Resource
    private SysSmsSendServiceImpl sysSmsSendServiceimpl;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private CustBaselineAllService custBaselineAllService;

    @Resource
    private SystemClientService systemClientService;

    @Resource
    private ConsInvitationService consInvitationService;

    @Resource
    private SystemClient systemClient;
    @Resource
    private OrgDemandService orgDemandService;

    @Override
    public Page<PlanCons> pageNotDeleted(PlanConsParam planConsParam) {
        //数据权限
        CurrenUserInfo currenUserInfo = SecurityUtils.getCurrentUserInfoUTF8();
        if (null == currenUserInfo) {
            return new Page<>();
        }
        String orgNo = currenUserInfo.getOrgId();
        String orgTitle = currenUserInfo.getOrgTitle();
        //机构子集
        List<String> list = new ArrayList<>();
        List<String> list1 = null;
        List<String> list2 = null;
        if (null != orgNo && !"".equals(orgNo)) {
            if (!"1".equals(orgTitle)) {
                //获取用户权限内所有组织机构集合
                list1 = OrganizationUtil.getAllOrgByOrgNo();
                if (null == list1 || list1.size() == 0) {
                    return new Page<>();
                }
                //根据参数查询其所有子集
                if (null != planConsParam.getOrgId()) {
                    list2 = OrganizationUtil.getAllOrgByOrgNoPamarm(planConsParam.getOrgId());
                    //筛选用户机构集合中包含参数的机构
                    if (null != list1 && null != list2) {
                        for (String s : list2) {
                            if (list1.contains(s)) {
                                list.add(s);
                            }
                        }
                    }
                    if (CollectionUtil.isEmpty(list)) {
                        return new Page<>();
                    }
                } else {
                    list = list1;
                }
            } else {
                if (null != planConsParam.getOrgId()) {
                    list = OrganizationUtil.getAllOrgByOrgNoPamarm(planConsParam.getOrgId());
                }
            }
        } else {
            return new Page<>();
        }
        Plan plan = planService.getByEventId(planConsParam.getEventId());
        QueryWrapper<Object> queryWrapper = new QueryWrapper<>();
        if (ObjectUtil.isNull(plan) && ObjectUtil.isEmpty(planConsParam.getPlanId())) {
            long pageNo = planConsParam.getCurrent();
            long pageSize = planConsParam.getSize();
            return new Page<>(pageNo, pageSize);
        }
        if (ObjectUtil.isNotNull(plan)) {
            queryWrapper.eq("pla.plan_id", plan.getPlanId());
        }

        if (ObjectUtil.isNotNull(planConsParam)) {
            if (ObjectUtil.isNotEmpty(planConsParam.getPlanId())) {
                queryWrapper.eq("pla.plan_id", planConsParam.getPlanId());
            }
            // 剔除规则
            if (ObjectUtil.isNotEmpty(planConsParam.getDelRule())) {
                queryWrapper.eq("pla.del_rule", planConsParam.getDelRule());
            }
            if (ObjectUtil.isNotEmpty(planConsParam.getDeleted())) {
                queryWrapper.eq("pla.deleted", planConsParam.getDeleted());
            }
            if (CollectionUtil.isNotEmpty(planConsParam.getConsIdList())) {
                queryWrapper.in("pla.cons_id", planConsParam.getConsIdList());
            }
            if (ObjectUtil.isNotEmpty(planConsParam.getConsId())) {
                queryWrapper.like("pla.cons_id", planConsParam.getConsId());
            }
            if (ObjectUtil.isNotEmpty(planConsParam.getConsName())) {
                queryWrapper.like("c.cons_name", planConsParam.getConsName());
            }
            // 机构等级
            if (ObjectUtil.isNotEmpty(list)) {
                queryWrapper.in("c.ORG_NO", list);
            }
            //市级查询所有用户，省级查询直接用户
            if (ObjectUtil.isNotNull(orgTitle) && "1".equals(orgTitle)) {
                queryWrapper.eq("pla.join_user_type", "1");
            }

            if (ObjectUtil.isNotNull(planConsParam.getSortColumn())) {
                if (planConsParam.getOrder().equals("asc")) {
                    queryWrapper.orderByAsc("pla.contract_cap");
                } else {
                    queryWrapper.orderByDesc("pla.contract_cap");
                }
            }
        }

        //根据排序升序排列，序号越小越在前
//        queryWrapper.orderByDesc("pla.partic_id");
        return getBaseMapper().pageConsMonitor(planConsParam.getPage(), queryWrapper);
    }

    @Override
    public Page<PlanCust> pageCustNotDeleted(PlanCustParam planCustParam) {
        //数据权限
        CurrenUserInfo currenUserInfo = SecurityUtils.getCurrentUserInfoUTF8();
        if (null == currenUserInfo) {
            return new Page<>();
        }
        String orgNo = currenUserInfo.getOrgId();
        String orgTitle = currenUserInfo.getOrgTitle();

        if (null != orgNo && !"".equals(orgNo)) {
            if (!"1".equals(orgTitle)) {
                //市级看不到聚合商数据
                return new Page<>();
            }
        } else {
            return new Page<>();
        }
        Plan plan = planService.getByEventId(planCustParam.getEventId());
        QueryWrapper<Object> queryWrapper = new QueryWrapper<>();
        if (ObjectUtil.isNull(plan) && ObjectUtil.isEmpty(planCustParam.getPlanId())) {
            long pageNo = planCustParam.getCurrent();
            long pageSize = planCustParam.getSize();
            return new Page<>(pageNo, pageSize);
        }
        if (ObjectUtil.isNotNull(plan)) {
            queryWrapper.eq("pla.plan_id", plan.getPlanId());
        }

        if (ObjectUtil.isNotNull(planCustParam)) {
            if (ObjectUtil.isNotEmpty(planCustParam.getPlanId())) {
                queryWrapper.eq("pla.plan_id", planCustParam.getPlanId());
            }
            // 剔除规则
            if (ObjectUtil.isNotEmpty(planCustParam.getDelRule())) {
                queryWrapper.eq("pla.del_rule", planCustParam.getDelRule());
            }
            if (ObjectUtil.isNotEmpty(planCustParam.getDeleted())) {
                queryWrapper.eq("pla.deleted", planCustParam.getDeleted());
            }
            if (ObjectUtil.isNotEmpty(planCustParam.getIntegrator())) {
                queryWrapper.eq("pla.integrator", planCustParam.getIntegrator());
            }
            if (ObjectUtil.isNotEmpty(planCustParam.getCustId())) {
                queryWrapper.like("pla.cust_id", planCustParam.getCustId());
            }
            if (ObjectUtil.isNotEmpty(planCustParam.getCustName())) {
                queryWrapper.like("c.legal_name", planCustParam.getCustName());
            }
            if (ObjectUtil.isNotEmpty(planCustParam.getLegalName())) {
                queryWrapper.like("c.legal_name", planCustParam.getLegalName());
            }
            if (ObjectUtil.isNotEmpty(planCustParam.getCreditCode())) {
                queryWrapper.like("c.CREDIT_CODE", planCustParam.getCreditCode());
            }

            if (ObjectUtil.isNotNull(planCustParam.getSortColumn())) {
                if (planCustParam.getOrder().equals("asc")) {
                    queryWrapper.orderByAsc("pla.contract_cap");
                } else {
                    queryWrapper.orderByDesc("pla.contract_cap");
                }
            }


        }
        //根据排序升序排列，序号越小越在前
//        queryWrapper.orderByDesc("pla.partic_id");
        return getBaseMapper().pageCustMonitor(planCustParam.getPage(), queryWrapper);
    }


    @Transactional
    @Override
    public void eliminateManual(List<String> consIdList, Long eventId, String deleteRule) {
        Plan plan = planService.getByEventId(eventId);
        LambdaUpdateWrapper<PlanCons> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(PlanCons::getDeleted, YesOrNotEnum.Y.getCode());
        updateWrapper.set(PlanCons::getDelRule, null);
        updateWrapper.eq(PlanCons::getPlanId, plan.getPlanId());
        updateWrapper.in(PlanCons::getConsId, consIdList);
        this.update(updateWrapper);
        // 如果是代理用户，要把关联的集成商也剔除了
        List<Long> aggreListByConsId = custService.getAggreListByConsId(consIdList);
        if (CollectionUtil.isNotEmpty(aggreListByConsId)) {
            planCustService.eliminateByCustIds(aggreListByConsId, plan.getPlanId());

        }
    }

    @Override
    public List<PlanCons> listByConsIds(List<String> consIdList, Long eventId) {
        Plan plan = planService.getByEventId(eventId);
        QueryWrapper<Object> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("pla.plan_id", plan.getPlanId());
        queryWrapper.in("pla.cons_id", consIdList);
        return getBaseMapper().listByConsIds(queryWrapper);
    }

    @Override
    public List<PlanCons> list(PlanConsParam planConsParam) {
        LambdaQueryWrapper<PlanCons> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(planConsParam)) {
            if (ObjectUtil.isNotEmpty(planConsParam.getPlanId())) {
                queryWrapper.eq(PlanCons::getPlanId, planConsParam.getPlanId());
            }
            // 是否剔除
            if (ObjectUtil.isNotEmpty(planConsParam.getDeleted())) {
                queryWrapper.eq(PlanCons::getDeleted, planConsParam.getDeleted());
            }
            // 剔除规则
            if (ObjectUtil.isNotEmpty(planConsParam.getDelRule())) {
                queryWrapper.eq(PlanCons::getDelRule, planConsParam.getDelRule());
            }
        }
        return this.list(queryWrapper);
    }

    @Override
    public List<PlanCons> listWithEmptyBaseline(Long planId) {
        return getBaseMapper().listWithEmptyBaseline(planId);
    }

    @Override
    public List<PlanCons> listWithCapLessThan(Long planId, BigDecimal ten) {
        return getBaseMapper().listWithCapLessThan(planId, ten);
    }

    @Override
    public List<PlanCons> listLessThanDemandCap(Long planId) {
        return getBaseMapper().listLessThanDemandCap(planId);
    }

    @Override
    public List<PlanCons> listByProvinceCode(Long planId, String regulateRange) {
        return getBaseMapper().listByProvinceCode(planId, regulateRange);
    }

    @Override
    public List<PlanCons> listByCityCode(Long planId, String regulateRange) {
        return getBaseMapper().listByCityCode(planId, regulateRange);
    }

    @Override
    public List<PlanCons> listByCountyCode(Long planId, String regulateRange) {
        return getBaseMapper().listByCountyCode(planId, regulateRange);
    }

    @Override
    public List<PlanCons> listByOrgNo(Long planId, String regulateRange) {
        return getBaseMapper().listByOrgNo(planId, regulateRange);
    }

    @Override
    public List<PlanCons> listByOrgNo(Long planId, List<String> regulateRangeList) {
        return getBaseMapper().listByOrgNoList(planId, regulateRangeList);
    }

    @Override
    public List<PlanCons> listNotDeleted(PlanConsParam planConsParam) {
        LambdaQueryWrapper<PlanCons> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PlanCons::getDeleted, YesOrNotEnum.N.getCode());
        if (ObjectUtil.isNotNull(planConsParam)) {
            // 是否剔除
            if (ObjectUtil.isNotEmpty(planConsParam.getPlanId())) {
                queryWrapper.eq(PlanCons::getPlanId, planConsParam.getPlanId());
            }
            // 剔除规则
            if (ObjectUtil.isNotEmpty(planConsParam.getDelRule())) {
                queryWrapper.eq(PlanCons::getDelRule, planConsParam.getDelRule());
            }
        }

        //根据排序升序排列，序号越小越在前
        queryWrapper.orderByAsc(PlanCons::getCreateTime);
        return this.list(queryWrapper);
    }

    @Override
    public BigDecimal getSumCapByEventId(Long eventId) {
        Plan plan = planService.getByEventId(eventId);
        if (ObjectUtil.isNull(plan)) return null;
        BigDecimal sumCapByEventId = getBaseMapper().getSumCapByEventId(plan.getPlanId(), YesOrNotEnum.Y.getCode());
        return Optional.ofNullable(sumCapByEventId).orElse(BigDecimal.ZERO);
    }

    @Override
    public Page<PlanCons> pageConsMonitor(PlanConsParam planConsParam) {
        //数据权限
        CurrenUserInfo currenUserInfo = SecurityUtils.getCurrentUserInfoUTF8();
        if (null == currenUserInfo) {
            return new Page<>();
        }

        // 查询事件对应的方案信息
        Plan plan = planService.getByEventId(planConsParam.getEventId());
        if (null != plan) {
            planConsParam.setPlanId(plan.getPlanId());
        } else {
            return new Page<PlanCons>();
        }

        String orgNo = planConsParam.getOrgId();
        if(StringUtils.isEmpty(orgNo)){
            orgNo = currenUserInfo.getOrgId();
        }
        //机构子集
        List<String> list;
        if (null != orgNo && !"".equals(orgNo)) {
            //获取所有组织机构集合
            ResponseData<List<String>> allNextOrgId = systemClientService.getAllNextOrgId(orgNo);
            if (allNextOrgId != null) {
                if ("000000".equals(allNextOrgId.getCode())) {
                    list = allNextOrgId.getData();
                    planConsParam.setOrgNos(list);
                }
            }
        }

        //调整执行时间
        String executeTime = planConsParam.getExecuteTime();
        if(executeTime == null){
            //默认查询最新的 执行时间
            LambdaQueryWrapper<EventExecute> eventExecuteueryWrapper = new LambdaQueryWrapper<>();
            eventExecuteueryWrapper.eq(EventExecute::getEventId,planConsParam.getEventId());
            List<EventExecute> eventExecutes = eventExecuteService.list(eventExecuteueryWrapper);
            if(CollectionUtils.isNotEmpty(eventExecutes)){
                List<EventExecute> executes = eventExecutes.stream().sorted((n1, n2) -> n2.getExecuteTime().compareTo(n1.getExecuteTime())).collect(Collectors.toList());
                executeTime = executes.get(0).getExecuteTime().toString();
            }
        }else {
            //时间拼接
            executeTime = plan.getRegulateDate()  + " " + executeTime + ":00";
        }
        planConsParam.setExecuteTime(executeTime);
        Page<PlanCons> planConsPage = getBaseMapper().pageConsExecute(planConsParam.getPage(), planConsParam);
        return planConsPage;
    }

    @Override
    public Map<String, Object> exportConsMonitor(PlanConsParam planConsParam) throws ExecutionException, InterruptedException {
        Map<String, Object> dataMap = new HashMap<>();
        //全天基线
        List<PlanConsMonitorExportVO> planConsList = null;
        //执行曲线
        List<PlanConsMonitorExportVO> planConsList2 = new ArrayList<>();
        //压降
        List<PlanConsMonitorExportVO> planConsList3 = new ArrayList<>();
        //执行率
        List<PlanConsMonitorExportVO> planConsList4 = new ArrayList<>();
        Event event = eventService.getById(planConsParam.getEventId());
        if (ObjectUtil.isNull(event)) {
            return dataMap;
        }
        // 事件开始点和事件结束点
        int startP = CurveUtil.covDateTimeToPoint(event.getStartTime());
        int endP = CurveUtil.covDateTimeToPoint(event.getEndTime());
        //数据权限
        CurrenUserInfo currenUserInfo = SecurityUtils.getCurrentUserInfoUTF8();
        if (null == currenUserInfo) {
            return dataMap;
        }
        String orgNo = planConsParam.getOrgId();
        //机构子集
        List<String> list = new ArrayList<>();
        if (null != orgNo && !"".equals(orgNo)) {
            //获取所有组织机构集合
            ResponseData<List<String>> allNextOrgId = systemClientService.getAllNextOrgId(orgNo);
            if (allNextOrgId != null) {
                if ("000000".equals(allNextOrgId.getCode())) {
                    list = allNextOrgId.getData();
                }
            }
        } else {
            return dataMap;
        }

        // 查询事件对应的方案信息
        Plan plan = planService.getByEventId(planConsParam.getEventId());
        if (null != plan) {
            planConsParam.setPlanId(plan.getPlanId());
        } else {
            return dataMap;
        }
        QueryWrapper<Object> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("pla.INVOLVED_IN", YesOrNotEnum.Y.getCode());
        queryWrapper.eq("pla.IMPLEMENT", YesOrNotEnum.Y.getCode());
        if (ObjectUtil.isNotNull(planConsParam)) {
            // 根据事件ID模糊查询
            if (ObjectUtil.isNotEmpty(planConsParam.getPlanId())) {
                queryWrapper.eq("pla.plan_id", planConsParam.getPlanId());
            }
            // 用户id
            if (ObjectUtil.isNotNull(planConsParam.getConsId()) && !"".equals(planConsParam.getConsId())) {
                queryWrapper.like("pla.cons_id", planConsParam.getConsId());
            }
            // 用户名称
            if (ObjectUtil.isNotNull(planConsParam.getConsName()) && !"".equals(planConsParam.getConsName())) {
                queryWrapper.like("c.cons_name", planConsParam.getConsName());
            }
            if (ObjectUtil.isNotNull(planConsParam.getProvinceCode()) && !"".equals(planConsParam.getProvinceCode())) {
                queryWrapper.like("c.PROVINCE_CODE", planConsParam.getProvinceCode());
            }
            if (ObjectUtil.isNotNull(planConsParam.getCityCode()) && !"".equals(planConsParam.getCityCode())) {
                queryWrapper.like("c.CITY_CODE", planConsParam.getCityCode());
            }
            if (ObjectUtil.isNotNull(planConsParam.getCountryCode()) && !"".equals(planConsParam.getCountryCode())) {
                queryWrapper.like("c.COUNTY_CODE", planConsParam.getCountryCode());
            }
            // 供电单位编码 改为 下属机构级联查询
//            if (ObjectUtil.isNotNull(planConsParam.getOrgId()) && !"".equals(planConsParam.getOrgId())) {
//                queryWrapper.eq("c.ORG_NO", planConsParam.getOrgId());
//            }
            // 机构等级
            if (ObjectUtil.isNotEmpty(list)) {
                queryWrapper.in("c.ORG_NO", list);
            }
            if (ObjectUtil.isNotEmpty(planConsParam.getSortColumn()) && "demandCap".equals(planConsParam.getSortColumn())) {
                if (null != planConsParam.getOrder() && "asc".equals(planConsParam.getOrder())) {
                    queryWrapper.orderByAsc("pla.DEMAND_CAP");
                } else if (null != planConsParam.getOrder() && "desc".equals(planConsParam.getOrder())) {
                    queryWrapper.orderByDesc("pla.DEMAND_CAP");
                }
            }
        }
        CityAndCountyUtils cityAndCountyUtils = new CityAndCountyUtils();
        List<String> cons = null;
        planConsList = getBaseMapper().getConsMonitor(queryWrapper);
        if (null == planConsList) {
            planConsList = new ArrayList<>();
        } else {
            cons = planConsList.stream().map(PlanConsMonitorExportVO::getConsId).collect(Collectors.toList());
            if (null == cons || cons.size() == 0) {
                planConsList = new ArrayList<>();
            }
            //组织机构翻译
            com.alibaba.fastjson.JSONObject datas = systemClientService.queryAllOrg();
            com.alibaba.fastjson.JSONArray jsonArray = null;
            if (null != datas && "000000".equals(datas.getString("code"))) {
                jsonArray = datas.getJSONArray("data");
            }
            List<SysOrgs> orgsListDate = new ArrayList<>();
            if (null != datas && datas.size() > 0) {
                for (Object object : jsonArray) {
                    com.alibaba.fastjson.JSONObject jsonObject = (com.alibaba.fastjson.JSONObject) com.alibaba.fastjson.JSONObject.toJSON(object);
                    SysOrgs sysOrgs = JSONObjectToEntityUtils.JSONObjectToSysOrg(jsonObject);
                    orgsListDate.add(sysOrgs);
                }
            }
            //时间段基线
            LambdaQueryWrapper<ConsBaseline> baselineLambdaQueryWrapper = new LambdaQueryWrapper<>();
            baselineLambdaQueryWrapper.eq(ConsBaseline::getBaselineLibId, plan.getBaselinId());
            baselineLambdaQueryWrapper.in(ConsBaseline::getConsId, cons);
            List<ConsBaseline> baselineList = consBaselineService.list(baselineLambdaQueryWrapper);
            //全天基线
            LambdaQueryWrapper<ConsBaselineAll> baselineAllLambdaQueryWrapper = new LambdaQueryWrapper<>();
            baselineAllLambdaQueryWrapper.eq(ConsBaselineAll::getBaselineLibId, plan.getBaselinId());
            baselineAllLambdaQueryWrapper.in(ConsBaselineAll::getConsId, cons);
            List<ConsBaselineAll> consBaselineAllList = consBaselineAllService.list(baselineAllLambdaQueryWrapper);
            //实时负荷
            LambdaQueryWrapper<ConsCurveToday> curveTodayLambdaQueryWrapper = new LambdaQueryWrapper<>();
            curveTodayLambdaQueryWrapper.eq(ConsCurveToday::getDataDate, event.getRegulateDate());
            curveTodayLambdaQueryWrapper.in(ConsCurveToday::getConsId, cons);
            List<ConsCurveToday> consCurveTodayList = consCurveTodayService.list(curveTodayLambdaQueryWrapper);
            //实时负荷
            LambdaQueryWrapper<ConsCurve> consCurveLambdaQueryWrapper = new LambdaQueryWrapper<>();
            consCurveLambdaQueryWrapper.eq(ConsCurve::getDataDate, event.getRegulateDate());
            consCurveLambdaQueryWrapper.in(ConsCurve::getConsId, cons);
            List<ConsCurve> consCurveList = consCurveService.list(consCurveLambdaQueryWrapper);
            OrgUtils orgUtils = new OrgUtils();
            Map<Integer, Method> consMethodMap = new HashMap<>();
            Map<Integer, Method> consMethodMap1 = new HashMap<>();
            Map<Integer, Method> consMethodMap2 = new HashMap<>();
            Map<Integer, Method> consMethodMap3 = new HashMap<>();
            Map<Integer, Method> consMethodMap4 = new HashMap<>();
            try {
                for (int j = 1; j <= 96; j++) {
                    consMethodMap.put(j, ConsBaseline.class.getMethod("getP" + j));
                    consMethodMap1.put(j, ConsBaselineAll.class.getMethod("getP" + j));
                    consMethodMap2.put(j, ConsCurveToday.class.getMethod("getP" + j));
                    consMethodMap3.put(j, ConsCurve.class.getMethod("getP" + j));
                    consMethodMap4.put(j, PlanConsMonitorExportVO.class.getMethod("setP" + j, BigDecimal.class));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (PlanConsMonitorExportVO planCons : planConsList) {
                planCons.setCurveType("全天基线负荷(kw)");
                PlanConsMonitorExportVO planConsMonitorExportVO2 = setParam(planCons);
                planConsMonitorExportVO2.setCurveType("执行负荷(kw)");
                PlanConsMonitorExportVO planConsMonitorExportVO3 = setParam(planCons);
                planConsMonitorExportVO3.setCurveType("压降负荷(kw)");
                PlanConsMonitorExportVO planConsMonitorExportVO4 = setParam(planCons);
                planConsMonitorExportVO4.setCurveType("执行率(100%)");
                //设置平均基线、最小基线、最大基线
                List<ConsBaseline> consBaselines = baselineList.stream()
                        .filter(con -> planCons.getConsId().equals(con.getConsId()))
                        .collect(Collectors.toList());
                if (null != consBaselines && consBaselines.size() > 0) {
                    planCons.setAvgBaseline(consBaselines.get(0).getAvgLoadBaseline());
                    planCons.setMaxBaseline(consBaselines.get(0).getMaxLoadBaseline());
                    planCons.setMinBaseline(consBaselines.get(0).getMinLoadBaseline());
                    planConsMonitorExportVO2.setAvgBaseline(consBaselines.get(0).getAvgLoadBaseline());
                    planConsMonitorExportVO2.setMaxBaseline(consBaselines.get(0).getMaxLoadBaseline());
                    planConsMonitorExportVO2.setMinBaseline(consBaselines.get(0).getMinLoadBaseline());
                    planConsMonitorExportVO3.setAvgBaseline(consBaselines.get(0).getAvgLoadBaseline());
                    planConsMonitorExportVO3.setMaxBaseline(consBaselines.get(0).getMaxLoadBaseline());
                    planConsMonitorExportVO3.setMinBaseline(consBaselines.get(0).getMinLoadBaseline());
                    planConsMonitorExportVO4.setAvgBaseline(consBaselines.get(0).getAvgLoadBaseline());
                    planConsMonitorExportVO4.setMaxBaseline(consBaselines.get(0).getMaxLoadBaseline());
                    planConsMonitorExportVO4.setMinBaseline(consBaselines.get(0).getMinLoadBaseline());
                }
                List<ConsBaselineAll> consBaselineAlls = consBaselineAllList.stream()
                        .filter(con -> planCons.getConsId().equals(con.getConsId()))
                        .collect(Collectors.toList());
                List<ConsCurveToday> consCurveTodays = null;
                List<ConsCurve> consCurves = null;
                if (event.getRegulateDate().compareTo(LocalDate.now()) == 0) {
                    consCurveTodays = consCurveTodayList.stream()
                            .filter(con -> planCons.getConsId().equals(con.getConsId()))
                            .collect(Collectors.toList());
                } else if (event.getRegulateDate().compareTo(LocalDate.now()) < 0) {
                    consCurves = consCurveList.stream()
                            .filter(con -> planCons.getConsId().equals(con.getConsId()))
                            .collect(Collectors.toList());
                }
                //设置事件段基线、全天基线、执行负荷、压降负荷、执行率
                for (int i = startP; i <= endP; i++) {
                    BigDecimal baselinePoint = null;
                    BigDecimal baselineAllPoint = null;
                    BigDecimal realTimePoint = null;
                    BigDecimal responseCapPoint = null;
                    BigDecimal executeRatePoint = null;
                    if (null != consBaselineAlls && consBaselineAlls.size() > 0) {
                        try {
                            baselineAllPoint = (BigDecimal) consMethodMap1.get(i).invoke(consBaselineAlls.get(0));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (null != consCurveTodays && consCurveTodays.size() > 0) {
                        try {
                            realTimePoint = (BigDecimal) consMethodMap2.get(i).invoke(consCurveTodays.get(0));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (null != consCurves && consCurves.size() > 0) {
                        try {
                            realTimePoint = (BigDecimal) consMethodMap3.get(i).invoke(consCurves.get(0));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (null != baselineAllPoint && null != realTimePoint) {
                        responseCapPoint = orgUtils.accuracy(NumberUtil.sub(baselineAllPoint, realTimePoint), 2);
                    }
                    if (null != responseCapPoint) {
                        executeRatePoint = NumberUtil.div(responseCapPoint, planCons.getDemandCap());
                        executeRatePoint = orgUtils.accuracy(NumberUtil.mul(executeRatePoint, 100), 2);
                    }

                    if (null != baselineAllPoint) {
                        try {
                            consMethodMap4.get(i).invoke(planCons, baselineAllPoint);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (null != realTimePoint) {
                        try {
                            consMethodMap4.get(i).invoke(planConsMonitorExportVO2, realTimePoint);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (null != responseCapPoint) {
                        try {
                            consMethodMap4.get(i).invoke(planConsMonitorExportVO3, responseCapPoint);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (null != executeRatePoint) {
                        try {
                            consMethodMap4.get(i).invoke(planConsMonitorExportVO4, executeRatePoint);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                //查询组织机构
                List<SysOrgs> single = orgsListDate.stream().filter(n -> planCons.getOrgNo().equals(n.getId())).collect(Collectors.toList());
                if (null != single && single.size() > 0) {
                    planCons.setOrgName(single.get(0).getName());
                    planConsMonitorExportVO2.setOrgName(single.get(0).getName());
                    planConsMonitorExportVO3.setOrgName(single.get(0).getName());
                    planConsMonitorExportVO4.setOrgName(single.get(0).getName());
                }
                Map<String, Object> map = null;
                map = cityAndCountyUtils.cityAndCounty(planCons.getOrgNo(), datas);
                if (null != map) {
                    String cityOrg = (String) map.get("city");
                    String countyOrg = (String) map.get("county");
                    if (null != cityOrg) {
                        planCons.setCityOrg(cityOrg);
                        planConsMonitorExportVO2.setCityOrg(cityOrg);
                        planConsMonitorExportVO3.setCityOrg(cityOrg);
                        planConsMonitorExportVO4.setCityOrg(cityOrg);
                    }
                    if (null != countyOrg) {
                        planCons.setCountyOrg(countyOrg);
                        planConsMonitorExportVO2.setCountyOrg(countyOrg);
                        planConsMonitorExportVO3.setCountyOrg(countyOrg);
                        planConsMonitorExportVO4.setCountyOrg(countyOrg);
                    }
                }
                planConsList2.add(planConsMonitorExportVO2);
                planConsList3.add(planConsMonitorExportVO3);
                planConsList4.add(planConsMonitorExportVO4);
            }
        }
        dataMap.put("baseLineAll", planConsList);
        dataMap.put("curveAll", planConsList2);
        dataMap.put("responseAll", planConsList3);
        dataMap.put("executeRateAll", planConsList4);
        return dataMap;
    }

    public PlanConsMonitorExportVO setParam(PlanConsMonitorExportVO planConsMonitorExportVO) {
        PlanConsMonitorExportVO monitorExportVO = new PlanConsMonitorExportVO();
        if (null != planConsMonitorExportVO) {
            monitorExportVO.setConsId(planConsMonitorExportVO.getConsId());
            monitorExportVO.setConsName(planConsMonitorExportVO.getConsName());
            monitorExportVO.setFirstContactName(planConsMonitorExportVO.getFirstContactName());
            monitorExportVO.setFirstContactInfo(planConsMonitorExportVO.getFirstContactInfo());
            monitorExportVO.setDemandCap(planConsMonitorExportVO.getDemandCap());
        }
        return monitorExportVO;
    }

    @Override
    public ConsMonitorCurve curveOfBaseAndTarget(PlanConsParam planConsParam) {

        if (ObjectUtil.isNull(planConsParam.getConsId())) {
            String consId = SecurityUtils.getCurrentUserInfo().getId();
            planConsParam.setConsId(consId);
        }
        ConsMonitorCurve consMonitorCurve = new ConsMonitorCurve();
        // 查询负荷基线
        Plan plan = planService.getByEventId(planConsParam.getEventId());
        if (null == plan) {
            plan = new Plan();
        }
        // 查询实际负荷
        ConsCurveParam consCurveParam = new ConsCurveParam();
        consCurveParam.setConsId(planConsParam.getConsId());
        consCurveParam.setDataDate(plan.getRegulateDate());
        ConsCurve curveByConsIdAndDate = consCurveService.getCurveByConsIdAndDate(consCurveParam);

        consMonitorCurve.setConsCurve(curveByConsIdAndDate);
        // 查询目标曲线
        Event event = eventService.getById(planConsParam.getEventId());
        ConsCurve tempCurve = new ConsCurve();
        int startP = CurveUtil.covDateTimeToPoint(event.getStartTime());
        int endP = CurveUtil.covDateTimeToPoint(event.getEndTime());

        //查询用户全量基线
        LambdaQueryWrapper<ConsBaselineAll> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ConsBaselineAll::getConsId, consCurveParam.getConsId());
        queryWrapper.eq(ConsBaselineAll::getBaselineLibId, plan.getBaselinId());
        List<ConsBaselineAll> consBaselineAlls = consBaselineAllService.list(queryWrapper);

        ConsBaselineAll consBaselineAll = null;
        if (null != consBaselineAlls && consBaselineAlls.size() > 0) {
            consBaselineAll = consBaselineAlls.get(0);
            if (consBaselineAll != null) {
                for (int i = 1; i <= 96; i++) {
                    if (i < startP || i > endP) {
                        ReflectUtil.setFieldValue(consBaselineAll, "p" + i, null);
                    }
                }
            }
        } else {
            consBaselineAll = new ConsBaselineAll();
        }
        consMonitorCurve.setConsBaselineAll(consBaselineAll);

        //查询用户区间基线
        LambdaQueryWrapper<ConsBaseline> consBaselineQueryWrapper = new LambdaQueryWrapper<>();
        consBaselineQueryWrapper.eq(ConsBaseline::getConsId, consCurveParam.getConsId());
        consBaselineQueryWrapper.eq(ConsBaseline::getBaselineLibId, plan.getBaselinId());
        List<ConsBaseline> consBaselines = consBaselineService.list(consBaselineQueryWrapper);
        if (!CollectionUtils.isEmpty(consBaselines)) {
            consMonitorCurve.setConsBaseline(consBaselines.get(0));
        }

        ConsBaseline targetCurve = new ConsBaseline();
        PlanCons planCons = planConsService.getByConsIdAndPlanId(plan.getPlanId(), planConsParam.getConsId());
        if (ObjectUtil.isNotNull(consBaselineAll)) {
            if (event.getResponseType().equals(EventResponseTypeEnum.PEAK_OF_INVITATION.getCode()) || event.getResponseType().equals(EventResponseTypeEnum.PEAK_OF_REALTIME.getCode())) {
                //邀约削峰或者实时削峰
                for (int i = startP + 1; i <= endP; i++) {
                    BigDecimal fieldValue = (BigDecimal) ReflectUtil.getFieldValue(consBaselineAll, "p" + i);
                    ReflectUtil.setFieldValue(targetCurve, "p" + i, NumberUtil.add(fieldValue, planCons.getDemandCap()));
                }
            } else {
                for (int i = startP + 1; i <= endP; i++) {
                    BigDecimal fieldValue = (BigDecimal) ReflectUtil.getFieldValue(consBaselineAll, "p" + i);
                    ReflectUtil.setFieldValue(targetCurve, "p" + i, NumberUtil.sub(fieldValue, planCons.getDemandCap()));
                }
            }
            consMonitorCurve.setTargetCurve(targetCurve);
            // 响应负荷下限
            for (int i = startP; i <= endP; i++) {
                // BigDecimal fieldValue = (BigDecimal) ReflectUtil.getFieldValue(baseCurve, "p" + i);
                ReflectUtil.setFieldValue(tempCurve, "p" + i, NumberUtil.sub(Optional.ofNullable(consBaselineAll.getMaxLoadBaseline()).orElse(BigDecimal.ZERO), Optional.ofNullable(planCons.getDemandCap()).orElse(BigDecimal.ZERO)));
            }
        }
        consMonitorCurve.setTempCurve(tempCurve);


        return consMonitorCurve;

    }

    @Override
    public PlanCons getByConsIdAndPlanId(Long planId, String consId) {
        LambdaQueryWrapper<PlanCons> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PlanCons::getPlanId, planId);
        queryWrapper.eq(PlanCons::getConsId, consId);
        return this.getOne(queryWrapper);
    }

    @Override
    public List<PlanCons> listByGovCode(Long planId, List<String> provinceList, List<String> cityList, List<String> countyList) {
        return getBaseMapper().listByGovCode(planId, provinceList, cityList, countyList);
    }

    @Override
    public Page<PlanCons> pageByDeleteRule(DeleteRuleParam deleteRuleParam) {
        return getBaseMapper().pageByDeleteRule(deleteRuleParam.getPage(), deleteRuleParam);
    }

    @Override
    public Page<PlanCons> pageConfirmed(DeleteRuleParam deleteRuleParam) {
        return getBaseMapper().pageConfirmed(deleteRuleParam.getPage(), deleteRuleParam);
    }

    @Override
    public Page<PlanCons> consDeleteList(DeleteConsParam deleteRuleParam) {
        //校验是否可执行
        checkStatus(deleteRuleParam.getEventId());
        return getBaseMapper().consDeleteList(deleteRuleParam.getPage(), deleteRuleParam);
    }

    public Boolean checkStatus(String eventId) {
        Event event = eventService.getById(eventId);
        if (null != event) {
            //仅待执行状态可剔除规则,且事件未开始
            if (!"02".equals(event.getEventStatus())) {
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

    public Boolean checkStatus2(Long eventId) {
        boolean flag = redisTemplate.hasKey("sendCityTarget");
        if (flag) {
            throw new ServiceException(EventExceptionEnum.CANNOT_OPERATOR);
        }
        boolean flag2 = redisTemplate.hasKey("sendBaselineAndCons");
        if (flag2) {
            throw new ServiceException(EventExceptionEnum.CANNOT_OPERATOR);
        }
        Integer count = baseMapper.getBaselineSendCount(eventId);
        if (count > 0) {
            throw new ServiceException(EventExceptionEnum.CANNOT_OPERATOR_COM);
        }
        Event event = eventService.getById(eventId);
        if (null != event) {
            if ("1".equals(event.getSendStatus())) {
                throw new ServiceException(EventExceptionEnum.CANNOT_OPERATOR_COM);
            }
            //仅执行中状态可剔除规则,且事件未开始
            if (!"03".equals(event.getEventStatus())) {
                throw new ServiceException(EventExceptionEnum.EVENT_EXECUTE_COM_STATE);
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
    public Page<PlanCons> custDeleteList(DeleteCustParam deleteRuleParam) {
        //校验是否可执行
        checkStatus(deleteRuleParam.getEventId());
        return getBaseMapper().custDeleteList(deleteRuleParam.getPage(), deleteRuleParam);
    }

    @Override
    public Page<PlanCons> getConsExecuteList(DeleteConsParam deleteRuleParam) {
        //查询该方案是否全部同步完成
       /* int count =  getBaseMapper().getPlanConsCount(deleteRuleParam);
        if(count>0) {
            return new Page<>();
        }*/
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
                //获取所有组织机构集合
                list = OrganizationUtil.getAllOrgByOrgNo();
                if (ObjectUtil.isNotEmpty(deleteRuleParam.getOrgId()) && list.indexOf(deleteRuleParam.getOrgId()) != -1) {
                    list = OrganizationUtil.getAllOrgByOrgNoPamarm(deleteRuleParam.getOrgId());
                }
                if (CollectionUtil.isEmpty(list)) {
                    return new Page<>();
                }
                deleteRuleParam.setOrgs(list);
            } else if ("1".equals(orgTitle)) {
                if (ObjectUtil.isNotEmpty(deleteRuleParam.getOrgId())) {
                    list = OrganizationUtil.getAllOrgByOrgNoPamarm(deleteRuleParam.getOrgId());
                    if (CollectionUtil.isEmpty(list)) {
                        return new Page<>();
                    }
                    deleteRuleParam.setOrgs(list);
                }
                deleteRuleParam.setJoinUserType("1");
            }
        } else {
            return new Page<>();
        }
        Page<PlanCons> planConsPage = getBaseMapper().getConsExecuteList(deleteRuleParam.getPage(), deleteRuleParam);
        if (null != planConsPage) {
            List<PlanCons> result = planConsPage.getRecords();
            if (null != result && result.size() > 0) {
                com.alibaba.fastjson.JSONObject datas = systemClientService.queryAllOrg();
                com.alibaba.fastjson.JSONArray jsonArray = null;
                if (null != datas && "000000".equals(datas.getString("code"))) {
                    jsonArray = datas.getJSONArray("data");
                }
                List<SysOrgs> orgsListDate = new ArrayList<>();
                if (null != datas && datas.size() > 0) {
                    for (Object object : jsonArray) {
                        com.alibaba.fastjson.JSONObject jsonObject = (com.alibaba.fastjson.JSONObject) com.alibaba.fastjson.JSONObject.toJSON(object);
                        SysOrgs sysOrgs = JSONObjectToEntityUtils.JSONObjectToSysOrg(jsonObject);
                        orgsListDate.add(sysOrgs);
                    }
                }
                for (PlanCons planCons : result) {
                    List<SysOrgs> single = orgsListDate.stream().filter(n -> planCons.getOrgNo().equals(n.getId())).collect(Collectors.toList());
                    if (null != single && single.size() > 0) {
                        planCons.setOrgName(single.get(0).getName());
                    }
                }
                planConsPage.setRecords(result);
            }
        }
        return planConsPage;
    }

    @Override
    public List<PlanCons> exportConsExecuteList(DeleteConsParam deleteRuleParam) throws ExecutionException, InterruptedException {
        //查询该方案是否全部同步完成
       /* int count =  getBaseMapper().getPlanConsCount(deleteRuleParam);
        if(count>0) {
            return new Page<>();
        }*/
        List<PlanCons> result = null;
        //数据权限
        CurrenUserInfo currenUserInfo = SecurityUtils.getCurrentUserInfoUTF8();
        if (null == currenUserInfo) {
            result = new ArrayList<>();
            return result;
        }
        String orgNo = currenUserInfo.getOrgId();
        String orgTitle = currenUserInfo.getOrgTitle();
        //机构子集
        List<String> list = new ArrayList<>();
        if (null != orgNo && !"".equals(orgNo)) {
            if (!"1".equals(orgTitle)) {
                //获取所有组织机构集合
                list = OrganizationUtil.getAllOrgByOrgNo();
                if (ObjectUtil.isNotEmpty(deleteRuleParam.getOrgId()) && list.indexOf(deleteRuleParam.getOrgId()) != -1) {
                    list = OrganizationUtil.getAllOrgByOrgNoPamarm(deleteRuleParam.getOrgId());
                }
                if (CollectionUtil.isEmpty(list)) {
                    result = new ArrayList<>();
                    return result;
                }
                deleteRuleParam.setOrgs(list);
            } else if ("1".equals(orgTitle)) {
                if (ObjectUtil.isNotEmpty(deleteRuleParam.getOrgId())) {
                    list = OrganizationUtil.getAllOrgByOrgNoPamarm(deleteRuleParam.getOrgId());
                    if (CollectionUtil.isEmpty(list)) {
                        result = new ArrayList<>();
                        return result;
                    }
                    deleteRuleParam.setOrgs(list);
                }
                deleteRuleParam.setJoinUserType("1");
            }
        } else {
            result = new ArrayList<>();
            return result;
        }
        result = getBaseMapper().exportConsExecuteList(deleteRuleParam);
        CityAndCountyUtils cityAndCountyUtils = new CityAndCountyUtils();
        OrgUtils orgUtils = new OrgUtils();
        if (null != result && result.size() > 0) {
            com.alibaba.fastjson.JSONObject datas = systemClientService.queryAllOrg();
            com.alibaba.fastjson.JSONArray jsonArray = null;
            if (null != datas && "000000".equals(datas.getString("code"))) {
                jsonArray = datas.getJSONArray("data");
            }
            List<SysOrgs> orgsListDate = new ArrayList<>();
            if (null != datas && datas.size() > 0) {
                for (Object object : jsonArray) {
                    com.alibaba.fastjson.JSONObject jsonObject = (com.alibaba.fastjson.JSONObject) com.alibaba.fastjson.JSONObject.toJSON(object);
                    SysOrgs sysOrgs = JSONObjectToEntityUtils.JSONObjectToSysOrg(jsonObject);
                    orgsListDate.add(sysOrgs);
                }
            }
            for (PlanCons planCons : result) {
                List<SysOrgs> single = orgsListDate.stream().filter(n -> planCons.getOrgNo().equals(n.getId())).collect(Collectors.toList());
                if (null != single && single.size() > 0) {
                    planCons.setOrgName(single.get(0).getName());
                }
                Map<String, Object> map = null;
                map = cityAndCountyUtils.cityAndCounty(planCons.getOrgNo(), datas);
                //map = orgUtils.getData4(jsonArray,planCons.getOrgNo());
                if (null != map) {
                    String cityOrg = (String) map.get("city");
                    String countyOrg = (String) map.get("county");
                    if (null != cityOrg) {
                        planCons.setCityCode(cityOrg);
                    }
                    if (null != countyOrg) {
                        planCons.setCountyCode(countyOrg);
                    }
                }
            }
        } else {
            result = new ArrayList<>();
        }
        return result;
    }

    @Override
    public List<PlanCust> exportCustExecuteList(DeleteCustParam deleteRuleParam) {
        //判断该方案是否全部同步完成
        /*int count = planCustMapper.getPlanCustCount(deleteRuleParam);
        if(count>0) {
            return new Page<>();
        }*/
        List<PlanCust> planCusts = new ArrayList<>();
        //数据权限
        CurrenUserInfo currenUserInfo = SecurityUtils.getCurrentUserInfoUTF8();
        if (null == currenUserInfo) {
            return planCusts;
        }
        String orgNo = currenUserInfo.getOrgId();
        String orgTitle = currenUserInfo.getOrgTitle();

        if (null != orgNo && !"".equals(orgNo)) {
            if (!"1".equals(orgTitle)) {
                //市级无法看集成商数据
                return planCusts;
            }
        } else {
            return planCusts;
        }
        return getBaseMapper().exportCustExecuteList(deleteRuleParam);
    }

    @Override
    public void deleteExecuteCons(PlanParam planParam) {
        checkStatus2(planParam.getEventId());
        List<PlanCons> planConsList = new ArrayList<>();
        List<String> consIdList = new ArrayList<>();
        List<EvaluTask> evaluTaskList = new ArrayList<>();
        List<EvaluCustTask> evaluCustTaskList = new ArrayList<>();
        List<PlanCust> planCustList = new ArrayList<>();
        Plan plan = null;
        if (null != planParam) {
            plan = planService.getByEventId(planParam.getEventId());
            if (null == plan) {
                throw new ServiceException(EventExceptionEnum.EVENT_PLAN_NOT_EXIST);
            }
            LambdaQueryWrapper<PlanCons> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(PlanCons::getPlanId, plan.getPlanId());
            lambdaQueryWrapper.eq(PlanCons::getDeleted, "N");
            lambdaQueryWrapper.eq(PlanCons::getInvolvedIn, "Y");
            //根据方案查询所有方案用户
            List<PlanCons> planConss = planConsService.list(lambdaQueryWrapper);
            if (null != planParam.getIds() && planParam.getIds().size() > 0) {
                for (Long data : planParam.getIds()) {
                    if (null != data) {
                        PlanCons planCons = new PlanCons();
                        planCons.setImplement("Y");
                        planCons.setParticId(data);
                        planConsList.add(planCons);
                        if (null != planConss && planConss.size() > 0) {
                            //设置用户效果评估任务状态
                            List<PlanCons> planConsList1 = planConss.stream().filter(plan1 -> plan1.getParticId().equals(data))
                                    .collect(Collectors.toList());
                            if (null != planConsList1 && planConsList1.size() > 0) {
                                EvaluTask evaluTask = new EvaluTask();
                                evaluTask.setImplement("Y");
                                evaluTask.setEventId(planParam.getEventId());
                                evaluTask.setConsId(planConsList1.get(0).getConsId());
                                evaluTaskList.add(evaluTask);
                                consIdList.add(planConsList1.get(0).getConsId());
                            }
                        }
                    }
                }
            }
        }
        //更新用户执行方案状态
        if (planConsList.size() > 0) {
            planConsService.updateBatchById(planConsList);
            //用户状态变为执行，其客户状态也修改为执行
            Map<String, Object> map = new HashMap<>();
            map.put("consIdList", consIdList);
            map.put("eventId", planParam.getEventId());
            List<PlanCust> planCusts = baseMapper.getCustCount(map);
            if (null != planCusts && planCusts.size() > 0) {
                for (PlanCust planCust : planCusts) {
                    planCust.setPlanId(plan.getPlanId());
                    planCust.setImplement("Y");
                    planCustList.add(planCust);
                    //设置修改客户评估任务状态
                    EvaluCustTask evaluCustTask = new EvaluCustTask();
                    evaluCustTask.setEventId(planParam.getEventId());
                    evaluCustTask.setCustId(planCust.getCustId());
                    evaluCustTask.setImplement("Y");
                    evaluCustTaskList.add(evaluCustTask);
                }
            }
            //更新用户效果评估任务
            if (null != evaluTaskList && evaluTaskList.size() > 0) {
                evaluTaskService.batchUpdateImplement(evaluTaskList);
            }

            //更新客户效果评估任务
            if (null != evaluCustTaskList && evaluCustTaskList.size() > 0) {
                evaluCustTaskService.batchUpdateImplement(evaluCustTaskList);
            }

            //更新客户执行状态
            if (null != planCustList && planCustList.size() > 0) {
                planCustMapper.batchUpdateImplement(planCustList);
            }

            //重算事件基线
            Event event = new Event();
            event.setEventId(planParam.getEventId());
            event.setBaselineStatus("N");
            eventService.updateById(event);

            //更新执行监测曲线用户任务表
            LambdaQueryWrapper<EventMonitorTask> monitorTaskLambdaQueryWrapper = new LambdaQueryWrapper<>();
            monitorTaskLambdaQueryWrapper.eq(EventMonitorTask::getEventId, planParam.getEventId());
            EventMonitorTask eventMonitor = eventMonitorTaskService.getOne(monitorTaskLambdaQueryWrapper);
            LambdaQueryWrapper<PlanCons> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(PlanCons::getPlanId, plan.getPlanId());
            lambdaQueryWrapper.eq(PlanCons::getDeleted, "N");
            lambdaQueryWrapper.eq(PlanCons::getInvolvedIn, "Y");
            lambdaQueryWrapper.eq(PlanCons::getImplement, "Y");
            List<PlanCons> planConsLists = planConsService.list(lambdaQueryWrapper);
            if (null != planConsLists && planConsLists.size() > 0) {
                String eventConsIds = "";
                for (PlanCons planCons : planConsLists) {
                    eventConsIds = eventConsIds + planCons.getConsId() + ",";
                }
                //去掉最后逗号
                if (null != eventConsIds && !"".equals(eventConsIds)) {
                    if (",".equals(eventConsIds.substring(eventConsIds.length() - 1))) {
                        eventConsIds = eventConsIds.substring(0, eventConsIds.length() - 1);
                    }
                }
                if (null != eventMonitor) {
                    eventMonitor.setConsIds(eventConsIds);
                    eventMonitorTaskService.updateById(eventMonitor);
                }
            }

        }

    }

    @Override
    public void deleteExecuteCust(PlanParam planParam) {
        checkStatus2(planParam.getEventId());
        List<PlanCust> planCusts = new ArrayList<>();
        List<PlanCons> planConsList = null;
        List<EvaluTask> evaluTaskList = new ArrayList<>();
        List<EvaluCustTask> evaluCustTaskList = new ArrayList<>();
        Plan plan = null;
        if (null != planParam) {
            plan = planService.getByEventId(planParam.getEventId());
            if (null == plan) {
                throw new ServiceException(EventExceptionEnum.EVENT_PLAN_NOT_EXIST);
            }
            LambdaQueryWrapper<PlanCust> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(PlanCust::getPlanId, plan.getPlanId());
            lambdaQueryWrapper.eq(PlanCust::getDeleted, "N");
            lambdaQueryWrapper.eq(PlanCust::getInvolvedIn, "Y");
            List<PlanCust> planCustList = planCustService.list(lambdaQueryWrapper);
            if (null != planParam.getIds() && planParam.getIds().size() > 0) {
                for (Long data : planParam.getIds()) {
                    if (null != data) {
                        PlanCust planCust = new PlanCust();
                        planCust.setImplement("Y");
                        planCust.setParticId(data);
                        planCusts.add(planCust);
                        List<PlanCust> planCusts1 = null;
                        if (null != planCustList && planCustList.size() > 0) {
                            planCusts1 = planCustList.stream().filter(plan1 -> plan1.getParticId().equals(data))
                                    .collect(Collectors.toList());
                        }
                        if (null != planCusts1 && planCusts1.size() > 0) {
                            EvaluCustTask evaluCustTask = new EvaluCustTask();
                            evaluCustTask.setImplement("Y");
                            evaluCustTask.setCustId(planCusts1.get(0).getCustId());
                            evaluCustTask.setEventId(planParam.getEventId());
                            evaluCustTaskList.add(evaluCustTask);
                        }
                    }
                }
            }
            if (null != planParam.getCustIds()) {
                planParam.setCustIdList(planParam.getCustIds());
            }
            planConsList = planMapper.getConsIdByCustAndEvent(planParam);
            if (null == planConsList) {
                planConsList = new ArrayList<>();
            } else if (planConsList.size() > 0) {
                for (PlanCons planCon : planConsList) {
                    planCon.setImplement("Y");
                    EvaluTask evaluTask = new EvaluTask();
                    evaluTask.setImplement("Y");
                    evaluTask.setEventId(planParam.getEventId());
                    evaluTask.setConsId(planCon.getConsId());
                    evaluTaskList.add(evaluTask);
                }
            }
        }

        //执行集成商
        if (planCusts.size() > 0) {
            //重算事件基线
            Event event = new Event();
            event.setEventId(planParam.getEventId());
            event.setBaselineStatus("N");
            eventService.updateById(event);
            planCustService.updateBatchById(planCusts);
        }

        //将集成商下代理用户也执行
        if (planConsList.size() > 0) {
            planConsService.updateBatchById(planConsList);
        }

        //更新用户效果评估任务
        if (null != evaluTaskList && evaluTaskList.size() > 0) {
            evaluTaskService.batchUpdateImplement(evaluTaskList);
        }

        //更新客户效果评估任务
        if (null != evaluCustTaskList && evaluCustTaskList.size() > 0) {
            evaluCustTaskService.batchUpdateImplement(evaluCustTaskList);
        }

        //更新执行监测曲线用户任务表
        LambdaQueryWrapper<EventMonitorTask> monitorTaskLambdaQueryWrapper = new LambdaQueryWrapper<>();
        monitorTaskLambdaQueryWrapper.eq(EventMonitorTask::getEventId, planParam.getEventId());
        EventMonitorTask eventMonitor = eventMonitorTaskService.getOne(monitorTaskLambdaQueryWrapper);
        LambdaQueryWrapper<PlanCons> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(PlanCons::getPlanId, plan.getPlanId());
        lambdaQueryWrapper.eq(PlanCons::getDeleted, "N");
        lambdaQueryWrapper.eq(PlanCons::getInvolvedIn, "Y");
        lambdaQueryWrapper.eq(PlanCons::getImplement, "Y");
        List<PlanCons> planConsLists = planConsService.list(lambdaQueryWrapper);
        if (null != planConsLists && planConsLists.size() > 0) {
            String eventConsIds = "";
            for (PlanCons planCons : planConsLists) {
                eventConsIds = eventConsIds + planCons.getConsId() + ",";
            }
            //去掉最后逗号
            if (null != eventConsIds && !"".equals(eventConsIds)) {
                if (",".equals(eventConsIds.substring(eventConsIds.length() - 1))) {
                    eventConsIds = eventConsIds.substring(0, eventConsIds.length() - 1);
                }
            }
            if (null != eventMonitor) {
                eventMonitor.setConsIds(eventConsIds);
                eventMonitorTaskService.updateById(eventMonitor);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelDeleteExecuteCons(PlanParam planParam) {
        checkStatus2(planParam.getEventId());
        List<PlanCons> planConsList = new ArrayList<>();
        List<String> consIdList = new ArrayList<>();
        List<EvaluTask> evaluTaskList = new ArrayList<>();
        List<EvaluCustTask> evaluCustTaskList = new ArrayList<>();
        List<PlanCust> planCustList = new ArrayList<>();
        Plan plan = null;
        if (null != planParam) {
            plan = planService.getByEventId(planParam.getEventId());
            if (null == plan) {
                throw new ServiceException(EventExceptionEnum.EVENT_PLAN_NOT_EXIST);
            }
            LambdaQueryWrapper<PlanCons> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(PlanCons::getPlanId, plan.getPlanId());
            lambdaQueryWrapper.eq(PlanCons::getDeleted, "N");
            lambdaQueryWrapper.eq(PlanCons::getInvolvedIn, "Y");
            //根据方案查询所有方案用户
            List<PlanCons> planConss = planConsService.list(lambdaQueryWrapper);
            if (null != planParam.getIds() && planParam.getIds().size() > 0) {
                for (Long data : planParam.getIds()) {
                    if (null != data) {
                        PlanCons planCons = new PlanCons();
                        planCons.setImplement("N");
                        planCons.setParticId(data);
                        planConsList.add(planCons);
                        if (null != planConss && planConss.size() > 0) {
                            //设置用户效果评估任务状态
                            List<PlanCons> planConsList1 = planConss.stream().filter(plan1 -> plan1.getParticId().equals(data))
                                    .collect(Collectors.toList());
                            if (null != planConsList1 && planConsList1.size() > 0) {
                                EvaluTask evaluTask = new EvaluTask();
                                evaluTask.setImplement("N");
                                evaluTask.setEventId(planParam.getEventId());
                                evaluTask.setConsId(planConsList1.get(0).getConsId());
                                evaluTaskList.add(evaluTask);
                                consIdList.add(planConsList1.get(0).getConsId());
                            }
                        }
                    }
                }
            }
        }
        //更新用户执行方案状态
        if (planConsList.size() > 0) {
            planConsService.updateBatchById(planConsList);
            //查询用户对应的客户是否有可执行用户，如果没有，将客户状态也修改为取消
            Map<String, Object> map = new HashMap<>();
            map.put("consIdList", consIdList);
            map.put("eventId", planParam.getEventId());
            map.put("implement", "Y");
            List<PlanCust> planCusts = baseMapper.getCustCount(map);
            if (null != planCusts && planCusts.size() > 0) {
                for (PlanCust planCust : planCusts) {
                    if (planCust.getCount() == 0) {
                        planCust.setPlanId(plan.getPlanId());
                        planCust.setImplement("N");
                        planCustList.add(planCust);
                        //设置修改客户评估任务状态
                        EvaluCustTask evaluCustTask = new EvaluCustTask();
                        evaluCustTask.setEventId(planParam.getEventId());
                        evaluCustTask.setCustId(planCust.getCustId());
                        evaluCustTask.setImplement("N");
                        evaluCustTaskList.add(evaluCustTask);
                    }
                }
            }
            //重算事件基线
            Event event = new Event();
            event.setEventId(planParam.getEventId());
            event.setBaselineStatus("N");
            eventService.updateById(event);

            //更新用户效果评估任务
            if (null != evaluTaskList && evaluTaskList.size() > 0) {
                evaluTaskService.batchUpdateImplement(evaluTaskList);
            }

            //更新客户效果评估任务
            if (null != evaluCustTaskList && evaluCustTaskList.size() > 0) {
                evaluCustTaskService.batchUpdateImplement(evaluCustTaskList);
            }

            //更新客户执行状态
            if (null != planCustList && planCustList.size() > 0) {
                planCustMapper.batchUpdateImplement(planCustList);
            }

            //更新执行监测曲线用户任务表
            LambdaQueryWrapper<EventMonitorTask> monitorTaskLambdaQueryWrapper = new LambdaQueryWrapper<>();
            monitorTaskLambdaQueryWrapper.eq(EventMonitorTask::getEventId, planParam.getEventId());
            EventMonitorTask eventMonitor = eventMonitorTaskService.getOne(monitorTaskLambdaQueryWrapper);
            LambdaQueryWrapper<PlanCons> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(PlanCons::getPlanId, plan.getPlanId());
            lambdaQueryWrapper.eq(PlanCons::getDeleted, "N");
            lambdaQueryWrapper.eq(PlanCons::getInvolvedIn, "Y");
            lambdaQueryWrapper.eq(PlanCons::getImplement, "Y");
            List<PlanCons> planConsLists = planConsService.list(lambdaQueryWrapper);
            if (null != planConsLists && planConsLists.size() > 0) {
                String eventConsIds = "";
                for (PlanCons planCons : planConsLists) {
                    eventConsIds = eventConsIds + planCons.getConsId() + ",";
                }
                //去掉最后逗号
                if (null != eventConsIds && !"".equals(eventConsIds)) {
                    if (",".equals(eventConsIds.substring(eventConsIds.length() - 1))) {
                        eventConsIds = eventConsIds.substring(0, eventConsIds.length() - 1);
                    }
                }
                if (null != eventMonitor) {
                    eventMonitor.setConsIds(eventConsIds);
                    eventMonitorTaskService.updateById(eventMonitor);
                }
            }

        }

    }

    @Override
    public void cancelDeleteExecuteCust(PlanParam planParam) {
        checkStatus2(planParam.getEventId());
        List<PlanCust> planCusts = new ArrayList<>();
        List<PlanCons> planConsList = null;
        List<EvaluTask> evaluTaskList = new ArrayList<>();
        List<EvaluCustTask> evaluCustTaskList = new ArrayList<>();
        Plan plan = null;
        if (null != planParam) {
            plan = planService.getByEventId(planParam.getEventId());
            if (null == plan) {
                throw new ServiceException(EventExceptionEnum.EVENT_PLAN_NOT_EXIST);
            }
            LambdaQueryWrapper<PlanCust> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(PlanCust::getPlanId, plan.getPlanId());
            lambdaQueryWrapper.eq(PlanCust::getDeleted, "N");
            lambdaQueryWrapper.eq(PlanCust::getInvolvedIn, "Y");
            List<PlanCust> planCustList = planCustService.list(lambdaQueryWrapper);
            if (null != planParam.getIds() && planParam.getIds().size() > 0) {
                for (Long data : planParam.getIds()) {
                    if (null != data) {
                        PlanCust planCust = new PlanCust();
                        planCust.setImplement("N");
                        planCust.setParticId(data);
                        planCusts.add(planCust);
                        List<PlanCust> planCusts1 = null;
                        if (null != planCustList && planCustList.size() > 0) {
                            planCusts1 = planCustList.stream().filter(plan1 -> plan1.getParticId().equals(data))
                                    .collect(Collectors.toList());
                        }
                        if (null != planCusts1 && planCusts1.size() > 0) {
                            EvaluCustTask evaluCustTask = new EvaluCustTask();
                            evaluCustTask.setImplement("N");
                            evaluCustTask.setCustId(planCusts1.get(0).getCustId());
                            evaluCustTask.setEventId(planParam.getEventId());
                            evaluCustTaskList.add(evaluCustTask);
                        }
                    }
                }
            }
            if (null != planParam.getCustIds()) {
                planParam.setCustIdList(planParam.getCustIds());
            }
            planConsList = planMapper.getConsIdByCustAndEvent(planParam);
            if (null == planConsList) {
                planConsList = new ArrayList<>();
            } else if (planConsList.size() > 0) {
                for (PlanCons planCon : planConsList) {
                    planCon.setImplement("N");
                    EvaluTask evaluTask = new EvaluTask();
                    evaluTask.setImplement("N");
                    evaluTask.setEventId(planParam.getEventId());
                    evaluTask.setConsId(planCon.getConsId());
                    evaluTaskList.add(evaluTask);
                }
            }
        }

        //取消执行集成商
        if (planCusts.size() > 0) {
            //重算事件基线
            Event event = new Event();
            event.setEventId(planParam.getEventId());
            event.setBaselineStatus("N");
            eventService.updateById(event);
            planCustService.updateBatchById(planCusts);
        }

        //将集成商下代理用户也取消
        if (planConsList.size() > 0) {
            planConsService.updateBatchById(planConsList);
        }

        //更新用户效果评估任务
        if (null != evaluTaskList && evaluTaskList.size() > 0) {
            evaluTaskService.batchUpdateImplement(evaluTaskList);
        }

        //更新客户效果评估任务
        if (null != evaluCustTaskList && evaluCustTaskList.size() > 0) {
            evaluCustTaskService.batchUpdateImplement(evaluCustTaskList);
        }

        //更新执行监测曲线用户任务表
        LambdaQueryWrapper<EventMonitorTask> monitorTaskLambdaQueryWrapper = new LambdaQueryWrapper<>();
        monitorTaskLambdaQueryWrapper.eq(EventMonitorTask::getEventId, planParam.getEventId());
        EventMonitorTask eventMonitor = eventMonitorTaskService.getOne(monitorTaskLambdaQueryWrapper);
        //查询可执行用户
        LambdaQueryWrapper<PlanCons> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(PlanCons::getPlanId, plan.getPlanId());
        lambdaQueryWrapper.eq(PlanCons::getDeleted, "N");
        lambdaQueryWrapper.eq(PlanCons::getInvolvedIn, "Y");
        lambdaQueryWrapper.eq(PlanCons::getImplement, "Y");
        List<PlanCons> planConsLists = planConsService.list(lambdaQueryWrapper);
        if (null != planConsLists && planConsLists.size() > 0) {
            String eventConsIds = "";
            for (PlanCons planCons : planConsLists) {
                eventConsIds = eventConsIds + planCons.getConsId() + ",";
            }
            //去掉最后逗号
            if (null != eventConsIds && !"".equals(eventConsIds)) {
                if (",".equals(eventConsIds.substring(eventConsIds.length() - 1))) {
                    eventConsIds = eventConsIds.substring(0, eventConsIds.length() - 1);
                }
            }
            if (null != eventMonitor) {
                eventMonitor.setConsIds(eventConsIds);
                eventMonitorTaskService.updateById(eventMonitor);
            }
        }
    }


    @Override
    public Page<PlanCust> getCustExecuteList(DeleteCustParam deleteRuleParam) {
        //判断该方案是否全部同步完成
        /*int count = planCustMapper.getPlanCustCount(deleteRuleParam);
        if(count>0) {
            return new Page<>();
        }*/
        //数据权限
        CurrenUserInfo currenUserInfo = SecurityUtils.getCurrentUserInfoUTF8();
        if (null == currenUserInfo) {
            return new Page<>();
        }
        String orgNo = currenUserInfo.getOrgId();
        String orgTitle = currenUserInfo.getOrgTitle();

        if (null != orgNo && !"".equals(orgNo)) {
            if (!"1".equals(orgTitle)) {
                //市级无法看集成商数据
                return new Page<>();
            }
        } else {
            return new Page<>();
        }
        return getBaseMapper().getCustExecuteList(deleteRuleParam.getPage(), deleteRuleParam);
    }

    @Override
    public Page<ConsMessage> getConsMessageList(ConsMessageParam deleteRuleParam) {
        //数据权限
        CurrenUserInfo currenUserInfo = SecurityUtils.getCurrentUserInfoUTF8();
        if (null == currenUserInfo) {
            return new Page<>();
        }
        String orgNo = currenUserInfo.getOrgId();
        String orgTitle = currenUserInfo.getOrgTitle();
        //机构子集
        List<String> list = new ArrayList<>();
        List<String> joinUserTypes = new ArrayList<>();
        if (null != orgNo && !"".equals(orgNo)) {
            if (!"1".equals(orgTitle)) {
                //获取所有组织机构集合,市级查看其子集机构数据(包含本身)
                list = OrganizationUtil.getAllOrgByOrgNo();
                if (CollectionUtil.isEmpty(list)) {
                    return new Page<>();
                }
                deleteRuleParam.setOrgs(list);

                //市级查看代理和独立用户
                joinUserTypes.add("2");
                joinUserTypes.add("1");
                deleteRuleParam.setJoinUserTypes(joinUserTypes);

            } else if ("1".equals(orgTitle)) {
                //省级只能查看独立用户
                joinUserTypes.add("1");
                deleteRuleParam.setJoinUserTypes(joinUserTypes);
            } else {
                joinUserTypes.add("2");
                joinUserTypes.add("1");
                deleteRuleParam.setJoinUserTypes(joinUserTypes);
            }
        } else {
            return new Page<>();
        }
        LambdaQueryWrapper<Cons> queryWrapper = new LambdaQueryWrapper<>();
        if (CollectionUtils.isNotEmpty(list)) {
            queryWrapper.eq(Cons::getOrgNo, list);
        }
        List<Cons> consList = consService.list(queryWrapper);
        if (CollectionUtils.isNotEmpty(consList)) {
            List<String> consIds = consList.stream().map(Cons::getId).collect(Collectors.toList());
            deleteRuleParam.setConsIds(consIds);
        }

        SmsSendParam smsSendParam = new SmsSendParam();
        smsSendParam.setUserName(deleteRuleParam.getConsName());
        smsSendParam.setUserNo(deleteRuleParam.getConsId());
        smsSendParam.setPhoneNumbers(deleteRuleParam.getPhone());
        smsSendParam.setConsIds(deleteRuleParam.getConsIds());
        smsSendParam.setBusinessRela(deleteRuleParam.getEventId());
        smsSendParam.setBusinessCode(deleteRuleParam.getBusinessCode());
        if (StringUtils.isNotEmpty(deleteRuleParam.getState())) {
            smsSendParam.setStatus(Integer.valueOf(deleteRuleParam.getState()));
        }
        smsSendParam.setJoinUserTypes(deleteRuleParam.getJoinUserTypes());
        JSONObject jsonObject = smsSendCilent.getSmsByBusiness(smsSendParam);

        Page<ConsMessage> page = new Page<>();

        List<ConsMessage> consMessages = new ArrayList<>();
        if (jsonObject != null) {
            if ("000000".equals(jsonObject.getString("code"))) {
                JSONObject jsonObject1 = jsonObject.getJSONObject("data");

                String total = jsonObject1.getString("total");
                String size = jsonObject1.getString("size");
                String current = jsonObject1.getString("current");
                String pages = jsonObject1.getString("pages");

                page.setTotal(Long.valueOf(total));
                page.setSize(Long.valueOf(size));
                page.setCurrent(Long.valueOf(current));
                page.setPages(Long.valueOf(pages));

                JSONArray jsonArray = jsonObject1.getJSONArray("records");
                for (Object o : jsonArray) {
                    JSONObject jsonObject2 = JSONObject.fromObject(o);
                    SysSmsSend smsSend = sysSmsSendServiceimpl.jsonToSysSmsSend(jsonObject2);

                    //短信对象转换CustMessage
                    ConsMessage consMessage = smsSendToConsMessage(smsSend);
                    consMessages.add(consMessage);
                }
            }
        }
        page.setRecords(consMessages);
//        return getBaseMapper().getConsMessageList(deleteRuleParam.getPage(), deleteRuleParam);
        return page;
    }

    /**
     * sms 对象转换成 ConsMessage
     *
     * @param smsSend
     * @return
     */
    private ConsMessage smsSendToConsMessage(SysSmsSend smsSend) {
        ConsMessage consMessage = new ConsMessage();
        consMessage.setConsId(smsSend.getUserNo());
        consMessage.setConsName(smsSend.getUserName());
        consMessage.setPhone(smsSend.getPhoneNumbers());
        consMessage.setContent(smsSend.getContent());
        if (smsSend.getStatus() != null) {
            consMessage.setState(smsSend.getStatus().toString());
        }
        if (smsSend.getCreateTime() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            consMessage.setUpdateLatsTime(sdf.format(smsSend.getCreateTime()));
        }
        return consMessage;
    }

    @Override
    public Page<CustMessage> getCustMessageList(CustMessageParam deleteRuleParam) {
        //数据权限
        CurrenUserInfo currenUserInfo = SecurityUtils.getCurrentUserInfoUTF8();
        if (null == currenUserInfo) {
            return new Page<>();
        }
        String orgNo = currenUserInfo.getOrgId();
        String orgTitle = currenUserInfo.getOrgTitle();

        //机构子集
        List<String> list = new ArrayList<>();
        List<String> joinUserTypes = new ArrayList<>();
        if (null != orgNo && !"".equals(orgNo)) {
            if (!"1".equals(orgTitle)) {
                //市级无法看集成商数据
                list = OrganizationUtil.getAllOrgByOrgNo();
                if (CollectionUtil.isEmpty(list)) {
                    return new Page<>();
                }
                deleteRuleParam.setOrgs(list);
                //市级查看代理和独立用户，不能看集成商
                joinUserTypes.add("2");
                joinUserTypes.add("1");
                joinUserTypes.add("4");
                deleteRuleParam.setJoinUserTypes(joinUserTypes);
            } else if ("1".equals(orgTitle)) {
                //省级只能查看独立用户集成商，不能看代理用户
                joinUserTypes.add("1");
                joinUserTypes.add("3");
                joinUserTypes.add("4");
                deleteRuleParam.setJoinUserTypes(joinUserTypes);
            } else {
                joinUserTypes.add("2");
                joinUserTypes.add("1");
                joinUserTypes.add("4");
                deleteRuleParam.setJoinUserTypes(joinUserTypes);
            }
        } else {
            return new Page<>();
        }

        /*LambdaQueryWrapper<Cons> queryWrapper = new LambdaQueryWrapper<>();
        if (CollectionUtils.isNotEmpty(list)) {
            queryWrapper.in(Cons::getOrgNo, list);
        }
        List<Cons> consList = consService.list(queryWrapper);
        if (CollectionUtils.isNotEmpty(consList)) {
            List<String> consIds = consList.stream().map(Cons::getId).collect(Collectors.toList());
            deleteRuleParam.setConsIds(consIds);
        } else {
            return new Page<>();
        }*/
        SmsSendParam smsSendParam = new SmsSendParam();
        smsSendParam.setUserName(deleteRuleParam.getCustName());
        smsSendParam.setUserNo(deleteRuleParam.getCreditCode());
        smsSendParam.setPhoneNumbers(deleteRuleParam.getPhone());
        //smsSendParam.setConsIds(deleteRuleParam.getConsIds());
        smsSendParam.setBusinessRela(deleteRuleParam.getEventId());
        smsSendParam.setBusinessCode(deleteRuleParam.getBusinessCode());
        if (StringUtils.isNotEmpty(deleteRuleParam.getState())) {
            smsSendParam.setStatus(Integer.valueOf(deleteRuleParam.getState()));
        }
        smsSendParam.setJoinUserTypes(deleteRuleParam.getJoinUserTypes());
        smsSendParam.setCurrent(deleteRuleParam.getCurrent());
        smsSendParam.setSize(deleteRuleParam.getSize());
        Page<CustMessage> page = new Page<>();
        List<CustMessage> list2 = new ArrayList<>();
        JSONObject jsonObject = smsSendCilent.getSmsByBusiness(smsSendParam);
        if (null != jsonObject) {
            if ("000000".equals(jsonObject.getString("code"))) {
                JSONObject jsonObject1 = jsonObject.getJSONObject("data");

                String total = jsonObject1.getString("total");
                String size = jsonObject1.getString("size");
                String current = jsonObject1.getString("current");
                String pages = jsonObject1.getString("pages");

                page.setTotal(Long.valueOf(total));
                page.setSize(Long.valueOf(size));
                page.setCurrent(Long.valueOf(current));
                page.setPages(Long.valueOf(pages));
                Long currentCount = (page.getCurrent() - 1) * page.getSize();
                Long nextCount = currentCount + page.getSize();
                JSONArray jsonArray = jsonObject1.getJSONArray("records");
                /*for (Object o : jsonArray) {
                    JSONObject jsonObject2 = JSONObject.fromObject(o);
                    SysSmsSend smsSend = sysSmsSendServiceimpl.jsonToSysSmsSend(jsonObject2);

                    //短信对象转换CustMessage
                    CustMessage custMessage = smsSendToCustMessage(smsSend);
                    custMessages.add(custMessage);
                }*/
                int length = jsonArray.size();
                if (length > 0) {
                    for (int i = currentCount.intValue(); i < nextCount; i++) {
                        if (length > i) {
                            Object o = jsonArray.get(i);
                            JSONObject jsonObject2 = JSONObject.fromObject(o);
                            SysSmsSend smsSend = sysSmsSendServiceimpl.jsonToSysSmsSend(jsonObject2);
                            CustMessage custMessage = smsSendToCustMessage(smsSend);
                            list2.add(custMessage);
                        }
                    }
                }
            }
        }

        page.setRecords(list2);
        return page;
//        return getBaseMapper().getCustMessageList(deleteRuleParam.getPage(), deleteRuleParam);
    }

    /**
     * 短信对象转换CustMessage
     *
     * @param smsSend
     * @return
     */
    private CustMessage smsSendToCustMessage(SysSmsSend smsSend) {
        CustMessage custMessage = new CustMessage();
        custMessage.setCustId(smsSend.getUserNo());
        custMessage.setCustName(smsSend.getUserName());
        custMessage.setPhone(smsSend.getPhoneNumbers());
        custMessage.setContent(smsSend.getContent());
        //原先逻辑是这样
        custMessage.setCreditCode(smsSend.getUserNo());
        if (smsSend.getStatus() != null) {
            custMessage.setState(smsSend.getStatus().toString());
        }
        if (smsSend.getCreateTime() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            custMessage.setUpdateLatsTime(sdf.format(smsSend.getCreateTime()));
        }
        return custMessage;
    }

    @Override
    public Page<EventConsOrCustResult> queryExecuteCust(ExecutePlanConsParam planConsParam) {
        return getBaseMapper().queryExecuteCust(planConsParam.getPage(), planConsParam);
    }

    @Override
    public Page<EventConsOrCustResult> queryExecuteCons(ExecutePlanConsParam planConsParam) {
        return getBaseMapper().queryExecuteCons(planConsParam.getPage(), planConsParam);
    }

    @Override
    public ExecutePlanTotal queryExecutePlanTotal(ExecutePlanConsParam planConsParam) {
        return getBaseMapper().queryExecutePlanTotal(planConsParam);
    }

    @Override
    @Transactional
    public String executePlan(ExecutePlanConsParam planConsParam) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        LambdaQueryWrapper<DrPlanImplemRec> eWrapper = new LambdaQueryWrapper<>();
        eWrapper.eq(DrPlanImplemRec::getEventId, planConsParam.getEventId());
        eWrapper.orderByDesc(DrPlanImplemRec::getExecuteTime);
        List<DrPlanImplemRec> drPlanImplemRecs = drPlanImplemRecService.list(eWrapper);
        if (null != drPlanImplemRecs && drPlanImplemRecs.size() > 0) {
            DrPlanImplemRec drPlanImplemRec = drPlanImplemRecs.get(0);
            if (null != drPlanImplemRec) {
                if (null != drPlanImplemRec.getExecuteTime()) {
                    try {
                        if (date.getTime() - dateFormat.parse(drPlanImplemRec.getExecuteTime()).getTime() < 6000) {
                            return PlanExecuteEnum.OVERTIME.getCode();
                        }
                    } catch (Exception e) {
                        e.getMessage();
                    }

                }
            }
        }
        //保存执行方案编制记录
        DrPlanImplemRec drPlanImplemRec = new DrPlanImplemRec();
        drPlanImplemRec.setEventId(planConsParam.getEventId());
        drPlanImplemRec.setExecuteTime(dateFormat.format(date));
        drPlanImplemRecService.save(drPlanImplemRec);
        List<EventConsOrCustResult> list = getBaseMapper().getExecuteAllCons(planConsParam);
        //参与方案执行的用户集合
        List<PlanCons> planConsList = new ArrayList<>();
        //参与方案执行客户集合
        List<PlanCust> planCustList = new ArrayList<>();
        //执行方案参与用户编制记录
        List<DrPlanConsRec> drPlanConsRecs = new ArrayList<>();
        //执行方案参与客户编制记录
        List<DrPlanCustRec> drPlanCustRecs = new ArrayList<>();
        if (null != list && list.size() > 0) {
            //获取调控目标1.2倍值
            BigDecimal regulateCap = list.get(0).getRegulateCap();
            if (regulateCap.equals(0) || regulateCap == null) {
                return PlanExecuteEnum.NOCAP.getCode();
            } else {
                regulateCap = NumberUtil.mul(regulateCap, 1.2);
            }
            BigDecimal total = BigDecimal.ZERO;
            for (EventConsOrCustResult eventConsOrCustResult : list) {
                DrPlanConsRec drPlanConsRec = new DrPlanConsRec();
                drPlanConsRec.setConsId(eventConsOrCustResult.getConsId());
                drPlanConsRec.setEventId(planConsParam.getEventId());
                drPlanConsRec.setImplemId(drPlanImplemRec.getId());
                if (null != eventConsOrCustResult) {
                    total = total.add(eventConsOrCustResult.getDemandCap());
                    //判断用户累加值是否大于1.2倍调控目标,如果不大于，继续累加
                    if (total.compareTo(regulateCap) <= 0) {
                        PlanCons planCons = new PlanCons();
                        planCons.setParticId(eventConsOrCustResult.getId());
                        planCons.setImplement("Y");
                        planConsList.add(planCons);
                        drPlanConsRec.setImplement("Y");
                        drPlanConsRecs.add(drPlanConsRec);
                    } else {
                        PlanCons planCons = new PlanCons();
                        planCons.setParticId(eventConsOrCustResult.getId());
                        planCons.setImplement("N");
                        planConsList.add(planCons);
                    }

                }
            }
        } else {
            return PlanExecuteEnum.NOCONS.getCode();
        }
        //更新执行用户
        this.updateBatchById(planConsList);
        //查询执行用户关联的客户
        List<EventConsOrCustResult> custList = getBaseMapper().getExecuteAllCust(planConsParam);
        if (null != custList && custList.size() > 0) {
            for (EventConsOrCustResult cust : custList) {
                DrPlanCustRec drPlanCustRec = new DrPlanCustRec();
                drPlanCustRec.setCustId(cust.getCustId());
                drPlanCustRec.setEventId(planConsParam.getEventId());
                drPlanCustRec.setImplement("Y");
                drPlanCustRec.setImplemId(drPlanImplemRec.getId());
                drPlanCustRecs.add(drPlanCustRec);
                if (null != cust) {
                    PlanCust planCust = new PlanCust();
                    planCust.setParticId(cust.getId());
                    planCust.setImplement("Y");
                    planCustList.add(planCust);
                }
            }
        }
        //将该计划所有客户执行状态更新为N
        getBaseMapper().updatePlanCustByPlan(planConsParam);
        //将执行客户状态更新为Y
        planCustService.updateBatchById(planCustList);
        //保存执行方案参与用户编制记录
        drPlanConsRecService.saveBatch(drPlanConsRecs);
        //保存执行方案参与客户编制记录
        drPlanCustRecService.saveBatch(drPlanCustRecs);
        return PlanExecuteEnum.SUCCESS.getCode();
    }

    @Override
    public List<ConsExecuteStatistic> getExecuteStatisticCitys(PlanParam planParam) {
        Result allOrgs = systemClientService.getAllOrgs();
        List<SysOrgs> sysOrgsLists = new ArrayList<>();
        List<DrOrgGoal> result = new ArrayList<>();

        com.alibaba.fastjson.JSONObject jsonObject2 = systemClientService.queryAllOrg();
        if ("000000".equals(jsonObject2.getString("code"))) {
            com.alibaba.fastjson.JSONArray data = jsonObject2.getJSONArray("data");
            for (Object ignored : data) {
                com.alibaba.fastjson.JSONObject jsonObject = (com.alibaba.fastjson.JSONObject) com.alibaba.fastjson.JSONObject.toJSON(ignored);
                SysOrgs sysOrgs = JSONObjectToEntityUtils.JSONObjectToSysOrg(jsonObject);
                sysOrgsLists.add(sysOrgs);
            }
        }

        if (null == planParam || null == planParam.getEventId()) {
            return new ArrayList<>();
        }
        Plan plan = planService.getByEventId(planParam.getEventId());
        if (null == plan) {
            return new ArrayList<>();
        }

        List<PlanCons> planConsList = planConsService.lambdaQuery().eq(PlanCons::getPlanId, plan.getPlanId())
                .eq(PlanCons::getDeleted, "N")
                .eq(PlanCons::getInvolvedIn, "Y").list();
        List<PlanCust> planCustList = planCustService.lambdaQuery().eq(PlanCust::getPlanId, plan.getPlanId())
                .eq(PlanCust::getIntegrator, 1)
                .eq(PlanCust::getInvolvedIn, 'Y')
                .eq(PlanCust::getDeleted, 'N').list();

        List<Cons> consList = consService.listByIds(planConsList.stream().map(PlanCons::getConsId).collect(Collectors.toList()));
        List<Cust> custList = planCustList.size() > 0 ?
                custService.listByIds(planCustList.stream().map(PlanCust::getCustId).collect(Collectors.toList())) : new ArrayList<>();

        Predicate<PlanCons> invokePredicateY = planCons -> Objects.equals(planCons.getImplement(), "Y");
        Predicate<PlanCons> invokePredicateN = planCons -> Objects.equals(planCons.getImplement(), "N");
        Predicate<PlanCons> joinPredicate = planCons -> Objects.equals(planCons.getJoinUserType(), "1");


        List<ConsExecuteStatistic> list = new ArrayList<>();
        if (!org.springframework.util.CollectionUtils.isEmpty(sysOrgsLists)) {
            SysOrgs filterOrg = null;
            if (ObjectUtil.isNotEmpty(planParam.getOrgId())) {
                List<SysOrgs> sysOrgs = sysOrgsLists.stream().filter(n -> planParam.getOrgId().equals(n.getId())).collect(Collectors.toList());
                if (ObjectUtil.isNotEmpty(sysOrgs)) {
                    filterOrg = sysOrgs.get(0);
                }
            }
            SysOrgs finalFilterOrg = filterOrg;
            List<SysOrgs> sysOrgsList = new ArrayList<>();
            if (filterOrg != null && OrgTitleEnum.CITY.getCode().equals(filterOrg.getOrgTitle())) {
                sysOrgsList.add(filterOrg);
            } else {
                List<SysOrgs> sysOrgsList2 = sysOrgsLists.stream().filter(n -> {
                            if (finalFilterOrg != null && OrgTitleEnum.PROVINCE.getCode().equals(finalFilterOrg.getOrgTitle())) {
                                return n == finalFilterOrg;
                            }
                            return OrgTitleEnum.PROVINCE.getCode().equals(n.getOrgTitle());
                        })
                        .flatMap(item -> {
                            List<SysOrgs> list2 = new ArrayList<>();
                            list2.add(item);
                            list2.addAll(sysOrgsLists.stream().filter(n -> OrgTitleEnum.CITY.getCode().equals(n.getOrgTitle()))
                                    .filter(n -> Objects.equals(n.getParentId(), item.getId())).collect(Collectors.toList()));
                            return list2.stream();
                        })
                        .collect(Collectors.toList());
                sysOrgsList.addAll(sysOrgsList2);
            }
            for (SysOrgs sysOrgs : sysOrgsList) {
                List<String> allNextOrgIds = getAllNextOrgIds(sysOrgsLists, sysOrgs.getId());
                List<String> consIds = consList.stream().filter(item -> allNextOrgIds.indexOf(item.getOrgNo()) != -1).map(Cons::getId).collect(Collectors.toList());
                List<Long> custIds = custList.stream().filter(item -> allNextOrgIds.indexOf(item.getOrgNo()) != -1).map(Cust::getId).collect(Collectors.toList());

                Predicate<PlanCons> orgConsPredicate = item -> consIds.indexOf(item.getConsId()) != -1;
                Predicate<PlanCust> orgCustPredicate = item -> Boolean.TRUE;
//                Predicate<PlanCust> orgCustPredicate = item -> custIds.indexOf(item.getCustId()) != -1;

                long count = planConsList.stream().filter(invokePredicateY).filter(orgConsPredicate).count();
                long consCount = planConsList.stream().filter(invokePredicateY).filter(joinPredicate)
                        .filter(orgConsPredicate)
                        .count();
                long custCount = planCustList.stream().filter(orgCustPredicate).count();

                BigDecimal consDemandCap = planConsList.stream().filter(invokePredicateY).filter(joinPredicate)
                        .filter(orgConsPredicate).map(PlanCons::getDemandCap)
                        .reduce(BigDecimal.ZERO, BigDecimal::add).movePointLeft(4).setScale(2, RoundingMode.HALF_UP);


                BigDecimal custDemandCap = planCustList.stream().filter(orgCustPredicate).map(PlanCust::getDemandCap).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);

                long consParticipateCount = consInvitationService.lambdaQuery().eq(ConsInvitation::getEventId, planParam.getEventId())
                        .eq(ConsInvitation::getIsParticipate, 'Y').list().stream().filter(item -> consIds.indexOf(item.getConsId()) != -1).count();

                BigDecimal commitConsCap = consInvitationService.lambdaQuery().eq(ConsInvitation::getEventId, planParam.getEventId())
                        .eq(ConsInvitation::getIsParticipate, 'Y').list().stream().filter(item -> consIds.indexOf(item.getConsId()) != -1)
                        .map(ConsInvitation::getReplyCap).reduce(BigDecimal.ZERO, BigDecimal::add)
                        .movePointLeft(4).setScale(2, RoundingMode.HALF_UP);


                long execConsSize = planConsList.stream().filter(invokePredicateY).filter(orgConsPredicate).count();

                long noExecConsSize = planConsList.stream().filter(invokePredicateN).filter(orgConsPredicate).count();

                BigDecimal execConsCap = planConsList.stream().filter(invokePredicateY).filter(orgConsPredicate)
                        .map(PlanCons::getDemandCap)
                        .filter(Objects::nonNull)
                        .reduce(BigDecimal.ZERO, BigDecimal::add).movePointLeft(4).setScale(2, RoundingMode.HALF_UP);


                BigDecimal noExecConsCap = planConsList.stream().filter(invokePredicateN)
                        .filter(orgConsPredicate)
                        .map(PlanCons::getDemandCap)
                        .filter(Objects::nonNull)
                        .reduce(BigDecimal.ZERO, BigDecimal::add).movePointLeft(4).setScale(2, RoundingMode.HALF_UP);

                ConsExecuteStatistic consExecuteStatistic = new ConsExecuteStatistic();
                consExecuteStatistic.setOrgId(sysOrgs.getId());
                consExecuteStatistic.setOrgName(sysOrgs.getName());
                consExecuteStatistic.setRegName(sysOrgs.getSimpleName());
                consExecuteStatistic.setTargetCap(Optional.ofNullable(plan.getRegulateCap()).orElse(BigDecimal.ZERO).movePointLeft(4).setScale(2, RoundingMode.HALF_UP));
                consExecuteStatistic.setCustCap(custDemandCap);
                consExecuteStatistic.setConsCap(consDemandCap);
                consExecuteStatistic.setCount(Math.toIntExact(count));
                consExecuteStatistic.setCustCount(Math.toIntExact(custCount));
                consExecuteStatistic.setConsCount(Math.toIntExact(consCount));
                consExecuteStatistic.setCommitConsSize(Math.toIntExact(consParticipateCount));
                consExecuteStatistic.setCommitConsCap(commitConsCap);
                consExecuteStatistic.setExecConsSize(Math.toIntExact(execConsSize));
                consExecuteStatistic.setNoExecConsSize(Math.toIntExact(noExecConsSize));
                consExecuteStatistic.setExecConsCap(execConsCap);
                consExecuteStatistic.setNoExecConsCap(noExecConsCap);

                list.add(consExecuteStatistic);
            }
        }
        return list;
    }

    public static List<String> getAllNextOrgIds(List<SysOrgs> list, String parentId) {
        List<String> retList = new ArrayList<>();
        retList.add(parentId);
        retList.addAll(list.stream().filter(item -> Objects.equals(item.getParentId(), parentId)).map(SysOrgs::getId)
                .flatMap(item -> getAllNextOrgIds(list, item).stream())
                .collect(Collectors.toList()));
        return retList;
    }



    @Override
    public ConsExecuteStatistic getExecuteStatistic(PlanParam planParam) {
        if (null == planParam || null == planParam.getEventId()) {
            return new ConsExecuteStatistic();
        }
        Plan plan = planService.getByEventId(planParam.getEventId());
        if (null == plan) {
            return new ConsExecuteStatistic();
        }

        Event event = eventService.getById(plan.getRegulateId());

        List<String> orgIds = null;
        CurrenUserInfo currentUserInfoUTF8 = SecurityUtils.getCurrentUserInfoUTF8();
        String orgTitle = currentUserInfoUTF8.getOrgTitle();
        if ("1".equals(orgTitle)) {
            if (StringUtils.isNotEmpty(planParam.getOrgId())) {
                orgIds = systemClientService.getAllNextOrgId(planParam.getOrgId()).getData();
            } else {
                orgIds = null;
            }
        } else {
            orgIds = systemClientService.getAllNextOrgId(currentUserInfoUTF8.getOrgId()).getData();
            if (StringUtils.isNotEmpty(planParam.getOrgId())) {
                if (orgIds.indexOf(planParam.getOrgId()) != -1) {
                    orgIds = systemClientService.getAllNextOrgId(planParam.getOrgId()).getData();
                }
            }
        }


        List<PlanCons> planConsList = planConsService.lambdaQuery().eq(PlanCons::getPlanId, plan.getPlanId())
                .eq(PlanCons::getDeleted, "N")
                .eq(PlanCons::getInvolvedIn, "Y").list();
        List<PlanCust> planCustList = planCustService.lambdaQuery().eq(PlanCust::getPlanId, plan.getPlanId())
                .eq(PlanCust::getIntegrator, 1)
                .eq(PlanCust::getInvolvedIn, 'Y')
                .eq(PlanCust::getDeleted, 'N').list();
        List<ConsInvitation> consInvitations = consInvitationService.lambdaQuery().eq(ConsInvitation::getEventId, planParam.getEventId())
                .eq(ConsInvitation::getIsParticipate, 'Y').list();

        if ((orgIds != null && orgIds.size() == 0) || (planConsList != null && planConsList.size() == 0)) {
            planConsList = new ArrayList<>();
        } else {
            List<String> consList = consService.lambdaQuery().in(ObjectUtil.isNotEmpty(orgIds), Cons::getOrgNo, orgIds)
                    .in(ObjectUtil.isNotEmpty(planConsList), Cons::getId, planConsList.stream().map(PlanCons::getConsId)
                            .collect(Collectors.toList())).list().stream().map(Cons::getId).collect(Collectors.toList());
            planConsList = planConsList.stream().filter(item -> consList.indexOf(item.getConsId()) != -1).collect(Collectors.toList());
        }
        if ((orgIds != null && orgIds.size() == 0) || (consInvitations != null && consInvitations.size() == 0)) {
            consInvitations = new ArrayList<>();
        } else {
            List<String> consList = consService.lambdaQuery().in(ObjectUtil.isNotEmpty(orgIds), Cons::getOrgNo, orgIds)
                    .in(ObjectUtil.isNotEmpty(planConsList), Cons::getId, consInvitations.stream().map(ConsInvitation::getConsId)
                            .collect(Collectors.toList())).list().stream().map(Cons::getId).collect(Collectors.toList());
            consInvitations = consInvitations.stream().filter(item -> consList.indexOf(item.getConsId()) != -1).collect(Collectors.toList());
        }

        Predicate<PlanCons> invokePredicateY = planCons -> Objects.equals(planCons.getImplement(), "Y");
        Predicate<PlanCons> invokePredicateN = planCons -> Objects.equals(planCons.getImplement(), "N");
        Predicate<PlanCons> joinPredicate = planCons -> Objects.equals(planCons.getJoinUserType(), "1");

        long count = planConsList.stream().filter(invokePredicateY).count();
        long consCount = planConsList.stream().filter(invokePredicateY).filter(joinPredicate)
                .count();
        long custCount = planCustList.stream().count();

        BigDecimal consDemandCap = planConsList.stream().filter(invokePredicateY).filter(joinPredicate).map(PlanCons::getDemandCap)
                .reduce(BigDecimal.ZERO, BigDecimal::add).movePointLeft(4).setScale(2, RoundingMode.HALF_UP);


        BigDecimal custDemandCap = planCustList.stream().map(PlanCust::getDemandCap).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);


        long consParticipateCount = consInvitations.size();
        BigDecimal commitConsCap = consInvitations.stream()
                .map(ConsInvitation::getReplyCap).reduce(BigDecimal.ZERO, BigDecimal::add)
                .movePointLeft(4).setScale(2, RoundingMode.HALF_UP);

        long execConsSize = planConsList.stream().filter(invokePredicateY).count();

        long noExecConsSize = planConsList.stream().filter(invokePredicateN).count();

        BigDecimal execConsCap = planConsList.stream().filter(invokePredicateY).map(PlanCons::getDemandCap)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add).movePointLeft(4).setScale(2, RoundingMode.HALF_UP);


        BigDecimal noExecConsCap = planConsList.stream().filter(invokePredicateN)
                .map(PlanCons::getDemandCap)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add).movePointLeft(4).setScale(2, RoundingMode.HALF_UP);

        BigDecimal targetCap = null;
        if (!"1".equals(currentUserInfoUTF8.getOrgTitle())) {
            com.alibaba.fastjson.JSONObject allOrgs = systemClient.queryAllOrg();
            com.alibaba.fastjson.JSONArray allOrgsData = allOrgs.getJSONArray("data");
            Map<String, com.alibaba.fastjson.JSONObject> orgIdMap = new HashMap<>();
            for (int i = 0; i < allOrgsData.size(); i++) {
                com.alibaba.fastjson.JSONObject jsonObject = allOrgsData.getJSONObject(i);
                orgIdMap.put(jsonObject.getString("id"), jsonObject);
            }
            String orgId = currentUserInfoUTF8.getOrgId();
            com.alibaba.fastjson.JSONObject orgInfo = orgIdMap.get(orgId);
            while (orgInfo != null && !"2".equals(orgInfo.getString("orgTitle"))) {
                orgInfo = orgIdMap.get(orgInfo.getString("parentId"));
                orgId = orgInfo.getString("id");
            }

            LambdaQueryWrapper<OrgDemand> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(OrgDemand::getRegulateId, event.getRegulateId());
            lambdaQueryWrapper.eq(OrgDemand::getOrgId, orgId);
            List<OrgDemand> list = orgDemandService.list(lambdaQueryWrapper);
            if (ObjectUtil.isNotEmpty(list)) {
                targetCap = list.get(0).getGoal();
            } else {
                targetCap = null;
            }
        } else {
            targetCap = Optional.ofNullable(plan.getRegulateCap()).orElse(BigDecimal.ZERO).movePointLeft(4).setScale(2, RoundingMode.HALF_UP);
        }

        ConsExecuteStatistic consExecuteStatistic = new ConsExecuteStatistic();
        consExecuteStatistic.setOrgId(planParam.getOrgId());
        consExecuteStatistic.setTargetCap(targetCap);
        consExecuteStatistic.setCustCap(custDemandCap);
        consExecuteStatistic.setConsCap(consDemandCap);
        consExecuteStatistic.setCount(Math.toIntExact(count));
        consExecuteStatistic.setCustCount(Math.toIntExact(custCount));
        consExecuteStatistic.setConsCount(Math.toIntExact(consCount));
        consExecuteStatistic.setCommitConsSize(Math.toIntExact(consParticipateCount));
        consExecuteStatistic.setCommitConsCap(commitConsCap);
        consExecuteStatistic.setExecConsSize(Math.toIntExact(execConsSize));
        consExecuteStatistic.setNoExecConsSize(Math.toIntExact(noExecConsSize));
        consExecuteStatistic.setExecConsCap(execConsCap);
        consExecuteStatistic.setNoExecConsCap(noExecConsCap);

        return consExecuteStatistic;
    }

    /**
     * @description: 电力用户-执行监测-用户监测-分页
     * @param:
     * @return:
     * @author: PengChuqing
     * @date: 2021/10/27 9:46
     */
    @Override
    public Page<EventUserConsPlanListResult> pageUserConsMonitor(EventUserConsPlanListParam eventUserConsPlanListParam) {
        CurrenUserInfo currentUserInfoUTF8 = SecurityUtils.getCurrentUserInfoUTF8();
        List<String> consIdListByCust = consService.getConsIdListByCust(Long.valueOf(currentUserInfoUTF8.getId()));

        LambdaQueryWrapper<Cons> consLambdaQueryWrapper = new LambdaQueryWrapper<>();
        consLambdaQueryWrapper.in(Cons::getId, consIdListByCust);
        consLambdaQueryWrapper.eq(StringUtils.isNotEmpty(eventUserConsPlanListParam.getConsCode()), Cons::getId, eventUserConsPlanListParam.getConsCode());
        consLambdaQueryWrapper.like(StringUtils.isNotEmpty(eventUserConsPlanListParam.getConsName()), Cons::getConsName, eventUserConsPlanListParam.getConsName());
        consLambdaQueryWrapper.like(StringUtils.isNotEmpty(eventUserConsPlanListParam.getOrgName()), Cons::getOrgName, eventUserConsPlanListParam.getOrgName());
        consLambdaQueryWrapper.eq(StringUtils.isNotEmpty(eventUserConsPlanListParam.getOrgId()), Cons::getOrgNo, eventUserConsPlanListParam.getOrgId());
        consLambdaQueryWrapper.select(Cons::getId, Cons::getConsName);
        Map<String, String> consIdName = consService.list(consLambdaQueryWrapper).stream().collect(Collectors.toMap(Cons::getId, Cons::getConsName));

        Event event = eventService.getById(eventUserConsPlanListParam.getEventId());

        Map<Long, String> consContractIds = consContractInfoService.list(new LambdaQueryWrapper<ConsContractInfo>().in(ConsContractInfo::getConsId, consIdListByCust).eq(ConsContractInfo::getProjectId, event.getProjectId()).select(ConsContractInfo::getContractId, ConsContractInfo::getConsId)).stream().collect(Collectors.toMap(ConsContractInfo::getContractId, ConsContractInfo::getConsId));
        Map<String, BigDecimal> consContractMap = consContractDetailService.list(new LambdaQueryWrapper<ConsContractDetail>().in(ConsContractDetail::getContractId, consContractIds.keySet()).eq(ConsContractDetail::getResponseType, event.getResponseType()).eq(ConsContractDetail::getTimeType, event.getTimeType()).eq(ConsContractDetail::getAdvanceNoticeTime, event.getAdvanceNoticeTime())).stream().collect(Collectors.toMap(new Function<ConsContractDetail, String>() {
            @Override
            public String apply(ConsContractDetail consContractDetail) {
                return consContractIds.get(consContractDetail.getContractId());
            }
        }, ConsContractDetail::getContractCap));

        Page<EventUserConsPlanListResult> eventConsList = getBaseMapper().getEventConsList(eventUserConsPlanListParam.getPage(), eventUserConsPlanListParam.getEventId(), consIdName.keySet());
        eventConsList.getRecords().forEach(item -> item.setContractCap(consContractMap.getOrDefault(item.getConsCode(), BigDecimal.ZERO)));
        return eventConsList;
    }


    /**
     * @description: 电力用户-执行监测-用户执行曲线
     * @param:
     * @return:
     * @author: 李奇瑞 修改
     * @date: 2022.06.30
     */
    @Override
    public EventUserConsLineResult getUserConsLine(EventUserConsLineParam eventUserConsLineParam) {
        EventUserConsLineResult eventUserConsLineResult = new EventUserConsLineResult();

        Long eventId = eventUserConsLineParam.getEventId();
        String consId = eventUserConsLineParam.getConsId();

        Plan plan = planService.getByEventId(eventId);
        if (plan != null) {
            Integer pnStart = CurveUtil.covDateTimeToPoint(plan.getStartTime().toString());
            Integer pnEnd = CurveUtil.covDateTimeToPoint(plan.getEndTime().toString());

            // 查询用户区间基线
            List<ConsBaseline> consBaselines = consBaselineService.getConsBaseByEventId(eventId, consId);
            ConsBaseline consBaseline = null;
            if (CollectionUtils.isNotEmpty(consBaselines)) {
                consBaseline = consBaselines.get(0);
                if (consBaseline != null) {
                    for (int i = 1; i <= 96; i++) {
                        if (i < pnStart || i > pnEnd) {
                            ReflectUtil.setFieldValue(consBaseline, "p" + i, null);
                        }
                    }
                }
            }

            List<ConsBaselineAll> consBaselineAllList = consBaselineService.getBaselineAllByEventId(eventId, consId);
            ConsBaselineAll consBaselineAll = null;
            if (!CollectionUtils.isEmpty(consBaselineAllList)) {
                consBaselineAll = consBaselineAllList.get(0);
                if (consBaselineAll != null) {
                    for (int i = 1; i <= 96; i++) {
                        if (i < pnStart || i > pnEnd) {
                            ReflectUtil.setFieldValue(consBaselineAll, "p" + i, null);
                        }
                    }
                }
            }

            ConsCurveParam consCurveParam = new ConsCurveParam();
            consCurveParam.setConsId(consId);
            consCurveParam.setDataDate(plan.getRegulateDate());
            ConsCurve curveByConsIdAndDate = consCurveService.getCurveByConsIdAndDate(consCurveParam);
            if (eventUserConsLineParam.getType() != null) {
                switch (eventUserConsLineParam.getType()) {
                    case 1:
                        eventUserConsLineResult.setConsBaseline(consBaseline);
                        break;
                    case 2:
                        eventUserConsLineResult.setConsCurve(curveByConsIdAndDate);
                        break;
                    case 3:
                        eventUserConsLineResult.setConsBaselineAll(consBaselineAll);
                        break;
                    default:
                        eventUserConsLineResult.setConsCurve(curveByConsIdAndDate);
                        eventUserConsLineResult.setConsBaseline(consBaseline);
                        eventUserConsLineResult.setConsBaselineAll(consBaselineAll);
                        break;
                }
            } else {
                eventUserConsLineResult.setConsCurve(curveByConsIdAndDate);
                eventUserConsLineResult.setConsBaseline(consBaseline);
                eventUserConsLineResult.setConsBaselineAll(consBaselineAll);
            }
        }
        return eventUserConsLineResult;
    }

    /**
     * @description: 负荷聚合商-执行监测-用户执行曲线
     * @param:
     * @return:
     * @author: 李奇瑞 修改
     * @date: 2022.06.30
     */
    @Override
    public EventUserCustLineResult getUserCustLine(EventUserCustLineParam eventUserCustLineParam) {
        EventUserCustLineResult eventUserCustLineResult = new EventUserCustLineResult();

        CurrenUserInfo currentUserInfoUTF8 = SecurityUtils.getCurrentUserInfoUTF8();

        String custId = currentUserInfoUTF8.getId();
        Long eventId = eventUserCustLineParam.getEventId();
        List<String> consIds = consService.getConsIdListByCust(Long.valueOf(custId));

        Event event = eventService.getById(eventId);
        if (event == null) {
            throw new ServiceException(500, "未查找到对应事件");
        }

        LambdaQueryWrapper<ConsContractInfo> custQueryWrapper = new LambdaQueryWrapper<>();
        custQueryWrapper.in(ConsContractInfo::getConsId, consIds);
        custQueryWrapper.eq(ConsContractInfo::getProjectId, event.getProjectId());
        List<ConsContractInfo> consContractInfos = consContractInfoService.list(custQueryWrapper);

        if (!CollectionUtils.isEmpty(consContractInfos)) {
            List<Long> infoIds = consContractInfos.stream().map(ConsContractInfo::getContractId).collect(Collectors.toList());
            LambdaQueryWrapper<ConsContractDetail> custDetailQueryWrapper = new LambdaQueryWrapper<>();
            custDetailQueryWrapper.in(ConsContractDetail::getContractId, infoIds);
            custDetailQueryWrapper.eq(ConsContractDetail::getResponseType, event.getResponseType());
            custDetailQueryWrapper.eq(ConsContractDetail::getTimeType, event.getTimeType());
            List<ConsContractDetail> consContractDetails = consContractDetailService.list(custDetailQueryWrapper);

            if (!CollectionUtils.isEmpty(consContractDetails)) {
                Double contractCap = consContractDetails.stream().map(item -> {
                    return item.getContractCap() == null ? 0 : item.getContractCap().doubleValue();
                }).collect(Collectors.summingDouble(Double::valueOf));
                eventUserCustLineResult.setContractCap(BigDecimal.valueOf(contractCap));

                CustEvaluation custEvaluation = custEvaluationService.getCustEvaluationByEventIdAndCustId(eventId, Long.valueOf(custId));
                if (null != custEvaluation) {
                    eventUserCustLineResult.setReplyCap(custEvaluation.getReplyCap());
                    eventUserCustLineResult.setAvgLoadBaseline(custEvaluation.getAvgLoadBaseline());
                    eventUserCustLineResult.setMaxLoadBaseline(custEvaluation.getMaxLoadBaseline());
                }
            }
        }

        Plan plan = planService.getByEventId(eventId);

        if (plan != null) {
            Integer pnStart = CurveUtil.covDateTimeToPoint(plan.getStartTime().toString());
            Integer pnEnd = CurveUtil.covDateTimeToPoint(plan.getEndTime().toString());

            LambdaQueryWrapper<CustBaseLineDetail> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(CustBaseLineDetail::getCustId, custId);
            lambdaQueryWrapper.eq(CustBaseLineDetail::getBaselineLibId, plan.getBaselinId());
            List<CustBaseLineDetail> custBaselines = custBaseLineDetailService.list(lambdaQueryWrapper);

            if (!CollectionUtils.isEmpty(custBaselines)) {
                CustBaseLineDetail custBaseLineDetail = custBaselines.get(0);
                if (custBaseLineDetail != null) {
                    for (int i = 1; i <= 96; i++) {
                        if (i < pnStart || i > pnEnd) {
                            ReflectUtil.setFieldValue(custBaseLineDetail, "p" + i, null);
                        }
                    }
                    eventUserCustLineResult.setCustBaseLine(custBaseLineDetail);
                }
            }

            CustCurveParam custCurveParam = new CustCurveParam();
            custCurveParam.setCustId(custId);
            custCurveParam.setDataDate(plan.getRegulateDate());
            custCurveParam.setPlanId(plan.getPlanId());
            ConsCurve curveByConsIdAndDate = null;
            if (LocalDate.now().isEqual(custCurveParam.getDataDate())) {
                curveByConsIdAndDate = planCustService.getCustCurveToday(custCurveParam);
            } else {
                curveByConsIdAndDate = planCustService.getCustCurveHistory(custCurveParam);
            }
            eventUserCustLineResult.setCustConsCurve(curveByConsIdAndDate);

            //查询 客户全量基线
            LambdaQueryWrapper<CustBaselineAll> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(CustBaselineAll::getCustId, custId);
            queryWrapper.eq(CustBaselineAll::getBaselineLibId, plan.getBaselinId());
            List<CustBaselineAll> consBaselineAllList = custBaselineAllService.list(queryWrapper);

            if (!CollectionUtils.isEmpty(consBaselineAllList)) {
                CustBaselineAll custBaselineAll = consBaselineAllList.get(0);
                if (custBaselineAll != null) {
                    for (int i = 1; i <= 96; i++) {
                        if (i < pnStart || i > pnEnd) {
                            ReflectUtil.setFieldValue(custBaselineAll, "p" + i, null);
                        }
                    }
                    eventUserCustLineResult.setCustBaselineAll(custBaselineAll);
                }
            }
        }
        return eventUserCustLineResult;
    }

    @Override
    public List<PlanCons> getPlanNoDe(List<Long> eventIds) {
        return baseMapper.getPlanNoDe(eventIds);
    }

    @Override
    public List<PlanCons> getCustPlan(long planId) {
        return baseMapper.getCustPlan(planId);
    }

}
