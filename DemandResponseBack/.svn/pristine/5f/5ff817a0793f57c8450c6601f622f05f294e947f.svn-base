package com.xqxy.dr.modular.subsidy.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.core.annotion.NeedSetValueField;
import com.xqxy.core.client.SystemClient;
import com.xqxy.core.enums.ApprovalCodeEnum;
import com.xqxy.core.enums.BusTypeEnum;
import com.xqxy.core.enums.CurrenUserInfoExceptionEnum;
import com.xqxy.core.enums.YesOrNotEnum;
import com.xqxy.core.exception.ServiceException;
import com.xqxy.core.pojo.login.CurrenUserInfo;
import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.core.util.SecurityUtils;
import com.xqxy.dr.modular.baseline.util.CurveUtil;
import com.xqxy.dr.modular.bidding.enums.BiddingCheckStatusEnums;
import com.xqxy.dr.modular.evaluation.entity.ConsEvaluation;
import com.xqxy.dr.modular.evaluation.entity.ConsEvaluationAppeal;
import com.xqxy.dr.modular.evaluation.entity.CustEvaluation;
import com.xqxy.dr.modular.evaluation.mapper.ConsEvaluationMapper;
import com.xqxy.dr.modular.evaluation.service.*;
import com.xqxy.dr.modular.event.entity.*;
import com.xqxy.dr.modular.event.service.*;
import com.xqxy.dr.modular.project.entity.Project;
import com.xqxy.dr.modular.project.entity.ProjectDetail;
import com.xqxy.dr.modular.project.enums.ExamineExceptionEnum;
import com.xqxy.dr.modular.project.enums.ProjectCheckStatusEnums;
import com.xqxy.dr.modular.project.enums.ProjectTypeEnums;
import com.xqxy.dr.modular.project.params.ExamineParam;
import com.xqxy.dr.modular.project.service.ProjectDetailService;
import com.xqxy.dr.modular.project.service.ProjectService;
import com.xqxy.dr.modular.strategy.Utils.StrategyUtils;
import com.xqxy.dr.modular.subsidy.entity.*;
import com.xqxy.dr.modular.subsidy.enums.SubsidyExceptionEnum;
import com.xqxy.dr.modular.subsidy.mapper.ConsSubsidyDailyMapper;
import com.xqxy.dr.modular.subsidy.mapper.ConsSubsidyMapper;
import com.xqxy.dr.modular.subsidy.mapper.CustSubsidyDailyMapper;
import com.xqxy.dr.modular.subsidy.mapper.SubsidyAppealMapper;
import com.xqxy.dr.modular.subsidy.param.*;
import com.xqxy.dr.modular.subsidy.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.dr.modular.subsidy.util.DictUtil;
import com.xqxy.sys.modular.cust.entity.Cust;
import com.xqxy.sys.modular.cust.param.BusConfigParam;
import com.xqxy.sys.modular.cust.result.Result;
import com.xqxy.sys.modular.cust.service.CustService;
import com.xqxy.sys.modular.dict.entity.DictData;
import com.xqxy.sys.modular.dict.service.DictTypeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 补贴申诉 服务实现类
 * </p>
 *
 * @author Shen
 * @since 2021-10-25
 */
@Service
public class SubsidyAppealServiceImpl extends ServiceImpl<SubsidyAppealMapper, SubsidyAppeal> implements SubsidyAppealService {

    @Resource
    SystemClient systemClient;

    @Resource
    private ConsSubsidyService consSubsidyService;

    @Resource
    private CustSubsidyService custSubsidyService;

    @Autowired
    private CustService custService;

    @Autowired
    private ConsEvaluationService consEvaluationService;

    @Autowired
    private ConsInvitationService consInvitationService;

    @Autowired
    private EventService eventService;

    @Resource
    ConsEvaluationMapper consEvaluationMapper;

    @Resource
    private ConsEvaluationAppealService consEvaluationAppealService;
    @Autowired
    private CustInvitationService custInvitationService;

    @Autowired
    private EvaluationImmediateService evaluationImmediateService;

    @Autowired
    private CustEvaluationService custEvaluationService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectDetailService projectDetailService;

    @Autowired
    private DictTypeService dictTypeService;

    @Autowired
    ConsSubsidyAppealService consSubsidyAppealService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ConsSubsidyMapper consSubsidyMapper;

    @Autowired
    private ConsSubsidyDailyMapper consSubsidyDailyMapper;

    @Autowired
    private ConsSubsidyDailyService consSubsidyDailyService;

    @Autowired
    private CustSubsidyDailyService custSubsidyDailyService;

    @Autowired
    private CustSubsidyDailyMapper custSubsidyDailyMapper;

    @Autowired
    private PlanService planService;

    @Autowired
    private PlanConsService planConsService;

    private final DateTimeFormatter simpleDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    @Override
    public void update(SubsidyAppealParam subsidyAppealParam) {
        SubsidyAppeal subsidyAppeal = new SubsidyAppeal();
        BeanUtils.copyProperties(subsidyAppealParam,subsidyAppeal);
        this.updateById(subsidyAppeal);
    }


    @Override
    @NeedSetValueField
    public SubsidyAppeal detail(Long busId) {
        SubsidyAppeal subsidyAppeal = baseMapper.getDetail(busId);
        if(subsidyAppeal != null) {
            return subsidyAppeal;
        }
        throw new ServiceException(SubsidyExceptionEnum.SUBSIDY_EXCEPTION_NUll);
    }

    @Override
    public Page<SubsidyAppeal> page(SubsidyAppealParam subsidyAppealParam) {
        // 异议工单
        LambdaQueryWrapper<SubsidyAppeal> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(subsidyAppealParam)) {
            if (ObjectUtil.isNotEmpty(subsidyAppealParam.getCustId())) {
                // 客户事件补贴
                CustSubsidyDailyParam custSubsidyDailyParam = new CustSubsidyDailyParam();
                custSubsidyDailyParam.setCustId(subsidyAppealParam.getCustId());
                List<CustSubsidy> custSubsidyList = custSubsidyService.list(custSubsidyDailyParam);
                List<Long> custSubsidyIdList = custSubsidyList.stream().map(CustSubsidy::getSubsidyId).collect(Collectors.toList());
                queryWrapper.in(SubsidyAppeal::getEvaluationId,custSubsidyIdList);
            }
            if (ObjectUtil.isNotEmpty(subsidyAppealParam.getConsId()) || ObjectUtil.isNotEmpty(subsidyAppealParam.getConsIdList())) {
                // 用户事件补贴
                ConsSubsidyDailyParam consSubsidyDailyParam = new ConsSubsidyDailyParam();
                consSubsidyDailyParam.setConsId(subsidyAppealParam.getConsId());
                consSubsidyDailyParam.setConsIdList(subsidyAppealParam.getConsIdList());
                List<ConsSubsidy> ConsSubsidyList = consSubsidyService.list(consSubsidyDailyParam);
                List<Long> consSubsidyIdList = ConsSubsidyList.stream().map(ConsSubsidy::getSubsidyId).collect(Collectors.toList());
                queryWrapper.in(SubsidyAppeal::getEvaluationId,consSubsidyIdList);
            }
        }
        queryWrapper.orderByDesc(SubsidyAppeal::getCreateTime);
        return this.page(subsidyAppealParam.getPage(),queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseData amendSubsidy(SubsidyAppealAmendParam subsidyAppealParam) {
        Long eventId = subsidyAppealParam.getEventId();
        String consId = subsidyAppealParam.getConsId();
        String key = "susidyAppeal" + eventId + consId;
        boolean flag = redisTemplate.hasKey(key);
        if(flag) {
            return ResponseData.fail("-1","数据正在计算中!","数据正在计算中!");
        }
        ConsEvaluationAppeal consEvaluationNew = null;
        try {
            //redis发送状态值,上锁
            Map<String, Object> eventInfo = new HashMap<>();
            eventInfo.put(key, "1");
            JSONObject eventInfoJson = new JSONObject(eventInfo);
            redisTemplate.convertAndSend(key, eventInfoJson.toJSONString());
            StrategyUtils strategyUtils = new StrategyUtils();
            List<String> cityStatus = new ArrayList<>();
            cityStatus.add("2");
            cityStatus.add("4");
            cityStatus.add("5");
            cityStatus.add("6");
            LambdaQueryWrapper<SubsidyAppeal> subsidyAppealLambdaQueryWrapper = new LambdaQueryWrapper<>();
            subsidyAppealLambdaQueryWrapper.eq(SubsidyAppeal::getId,subsidyAppealParam.getId());
            subsidyAppealLambdaQueryWrapper.in(SubsidyAppeal::getStatusCity,cityStatus);
            List<SubsidyAppeal> subsidyAppeals = this.list(subsidyAppealLambdaQueryWrapper);
            if(null!=subsidyAppeals && subsidyAppeals.size()>0) {
                ResponseData.fail("-1","已提交审核，无法重算!","已提交审核，无法重算!");
            }
            Event event = eventService.getById(eventId);
            if(null==event) {
                ResponseData.fail("-1","无事件信息!","无事件信息!");
            }
            Project project = projectService.getById(event.getProjectId());
            if(null==project) {
                ResponseData.fail("-1","无项目信息!","无项目信息!");
            }
            ConsInvitation consInvitation = null;
            if(null!=subsidyAppealParam) {
                //用户效果评估
                ConsEvaluation consEvaluation = null;
                //效果评估新表
                consEvaluationNew = new ConsEvaluationAppeal();
                consEvaluationNew.setSubsidyAppealId(subsidyAppealParam.getId());
                LambdaQueryWrapper<ConsEvaluation> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.eq(ConsEvaluation::getConsId,consId);
                lambdaQueryWrapper.eq(ConsEvaluation::getEventId,eventId);
                List<ConsEvaluation> consEvaluations = consEvaluationService.list(lambdaQueryWrapper);
                if(null!=consEvaluations && consEvaluations.size()>0) {
                    consEvaluation = consEvaluations.get(0);
                    BeanUtils.copyProperties(consEvaluation,consEvaluationNew);
                }
                //查询邀约信息
                List<ConsInvitation> consInvitations = consInvitationService.getConsInfoByEvent(eventId,consId);
                if(null!=consInvitations && consInvitations.size()>0) {
                    consInvitation = consInvitations.get(0);
                    consEvaluationNew.setReplyCap(consInvitations.get(0).getReplyCap());
                    consEvaluationNew.setJoinUserType(consInvitations.get(0).getJoinUserType());
                } else {
                    ResponseData.fail("-1","无邀约信息!","无邀约信息!");
                }
                BeanUtils.copyProperties(subsidyAppealParam,consEvaluationNew);
                //是否越界
                BigDecimal maxresult=consEvaluationNew.getMaxLoadBaseline().subtract(consEvaluationNew.getMaxLoadActual());
                //是否合格
                BigDecimal avgresult=consEvaluationNew.getAvgLoadBaseline().subtract(consEvaluationNew.getAvgLoadActual());
                if("2".equals(event.getResponseType())) {
                    avgresult = NumberUtil.mul(avgresult, -1);
                    maxresult = NumberUtil.mul(maxresult, -1);
                    //evaluation.setEffectiveTime(strategyUtils.calEeffectiveTime2(actualList, forecastList, eventInvitation.getReplyCap()) * 15);
                }
                if(maxresult.compareTo(consEvaluationNew.getReplyCap())<1)
                {
                    consEvaluationNew.setIsOut(YesOrNotEnum.Y.getCode());
                }
                else {
                    consEvaluationNew.setIsOut(YesOrNotEnum.N.getCode());
                }
                BigDecimal replyCapparam=consEvaluationNew.getReplyCap().multiply(new BigDecimal("0.8"));
                if(avgresult.compareTo(replyCapparam)>=0)
                {
                    consEvaluationNew.setIsQualified(YesOrNotEnum.Y.getCode());
                } else {
                    consEvaluationNew.setIsQualified(YesOrNotEnum.N.getCode());
                    consEvaluationNew.setRemark("负荷响应率小于百分80");

                }
                //有效时长
                int startP = CurveUtil.covDateTimeToPoint(event.getStartTime());
                int endP = CurveUtil.covDateTimeToPoint(event.getEndTime());
                int hour = (endP-startP)*15;
                if(hour<60) {
                    consEvaluationNew.setRemark("响应时长小于60分钟");
                }
                consEvaluationNew.setEffectiveTime(hour);
                if(consEvaluationNew.getMaxLoadActual().compareTo(consEvaluationNew.getMaxLoadBaseline())>0) {
                    consEvaluationNew.setRemark("实际最大负荷大于基线最大负荷");
                }
                //负荷响应量为实际负荷占反馈响应量百分比
                if(null==consEvaluationNew.getReplyCap() || avgresult.compareTo(BigDecimal.ZERO)==0) {
                    consEvaluationNew.setExecuteRate(BigDecimal.ZERO);
                } else {
                    consEvaluationNew.setExecuteRate(NumberUtil.div(avgresult,consEvaluationNew.getReplyCap()));
                }
                ConsSubsidyAppeal consSubsidyAppeal = new ConsSubsidyAppeal();
                consSubsidyAppeal.setSubsidyAppealId(subsidyAppealParam.getId());
                LambdaQueryWrapper<ConsSubsidy> lambdaSubsidyQueryWrapper = new LambdaQueryWrapper<>();
                lambdaSubsidyQueryWrapper.eq(ConsSubsidy::getEventId,eventId);
                lambdaSubsidyQueryWrapper.eq(ConsSubsidy::getConsId,consId);
                //List<ConsSubsidy> consSubsidyList = consSubsidyService.list(lambdaSubsidyQueryWrapper);
                Map<String, ProjectDetail> projectDetailMap = new HashMap<>();
                List<ProjectDetail> projectDetails = null;
                List<ProjectDetail> projectDetailList = projectDetailService.list();
                projectDetails = projectDetailList.stream().filter(projectDetail -> projectDetail.getProjectId().equals(event.getProjectId())).collect(Collectors.toList());
                if (null != projectDetails && projectDetails.size()>0) {
                    for (ProjectDetail projectDetail : projectDetails) {
                        String timeType = projectDetail.getTimeType();
                        String responseType = projectDetail.getResponseType();
                        String type = timeType + "-" + responseType;
                        projectDetailMap.put(type, projectDetail);
                    }
                }
                String projectType = project.getProjectType();
                BigDecimal price = BigDecimal.ZERO;
                String timeType = event.getTimeType();  //1约时；2实时
                String responseType = event.getResponseType();  //1削峰；2填谷
                if (projectType.equals(ProjectTypeEnums.PRICE_TYPE.getCode())) {
                    price = consInvitation.getReplyPrice();
                } else if (projectType.equals(ProjectTypeEnums.EXCITATION_TYPE.getCode())) {
                    String type = timeType + "-" + responseType;
                    ProjectDetail projectDetail = projectDetailMap.get(type);
                    if (ObjectUtil.isNotNull(projectDetail)) {
                        price = projectDetail.getPrice();
                    }
                }
                List<DictData> regulationTimeDictDataList = dictTypeService.getDictDataByTypeCode("regulation_time");
                List<DictData> loadResponseDictDataList = dictTypeService.getDictDataByTypeCode("load_response");
                BigDecimal time;
                BigDecimal powerRate;
                if (hour>=60) {
                    time = DictUtil.getDictValue(regulationTimeDictDataList, new BigDecimal(hour));
                    consSubsidyAppeal.setTimeCoefficient(time);
                } else {
                    consSubsidyAppeal.setTimeCoefficient(BigDecimal.ZERO);
                    time = BigDecimal.ZERO;
                }
                powerRate = DictUtil.getDictValue(loadResponseDictDataList, consEvaluationNew.getExecuteRate());
                if (powerRate.compareTo(new BigDecimal("0")) == 0) {
                    consSubsidyAppeal.setRemark("响应负荷率不在0.8-1.2之间");
                    consSubsidyAppeal.setRateCoefficient(BigDecimal.ZERO);
                } else {
                    consSubsidyAppeal.setRateCoefficient(BigDecimal.ONE);
                }
                //用户事件补贴
                consSubsidyAppeal.setEventId(eventId);
                consSubsidyAppeal.setConsId(consId);
                consSubsidyAppeal.setActualCap(avgresult);
                consSubsidyAppeal.setContractPrice(price);
                consSubsidyAppeal.setCalRule(project.getIncentiveStandard());
                consSubsidyAppeal.setJoinUserType(consInvitation.getJoinUserType());
                BigDecimal temp = strategyUtils.getConfirmCap(consEvaluationNew.getReplyCap(),avgresult);
                //有效性
                if(hour >= 60 && "Y".equals(consEvaluationNew.getIsQualified()) && consEvaluationNew.getMaxLoadActual().compareTo(consEvaluationNew.getMaxLoadBaseline())<=0) {
                    consEvaluationNew.setRemark("数据申诉成功");
                    consEvaluationNew.setIsEffective("Y");
                    consSubsidyAppeal.setSubsidyAmount(NumberUtil.mul(temp, price, time, powerRate));
                    consSubsidyAppeal.setSettledAmount(NumberUtil.mul(temp, price, time, powerRate));
                    consSubsidyAppeal.setRemark("数据申诉成功");
                } else {
                    consEvaluationNew.setIsEffective("N");
                    consSubsidyAppeal.setSubsidyAmount(BigDecimal.ZERO);
                    consSubsidyAppeal.setSettledAmount(BigDecimal.ZERO);
                }
                LambdaQueryWrapper<ConsSubsidyAppeal> lambdaSubsidyAppQueryWrapper = new LambdaQueryWrapper<>();
                lambdaSubsidyAppQueryWrapper.eq(ConsSubsidyAppeal::getSubsidyAppealId,subsidyAppealParam.getId());
                List<ConsSubsidyAppeal> consSubsidyAppealList = consSubsidyAppealService.list(lambdaSubsidyAppQueryWrapper);
                if(null!=consSubsidyAppealList && consSubsidyAppealList.size()>0) {
                    consSubsidyAppeal.setSubsidyId(consSubsidyAppealList.get(0).getSubsidyId());
                    consSubsidyAppealService.updateById(consSubsidyAppeal);
                } else {
                    long id = IdWorker.getId();
                    consSubsidyAppeal.setSubsidyId(id);
                    consSubsidyAppealService.save(consSubsidyAppeal);
                }
                consEvaluationNew.setActualCap(avgresult);
                if("Y".equals(consEvaluationNew.getIsEffective())) {
                    consEvaluationNew.setConfirmCap(temp);
                } else {
                    consEvaluationNew.setConfirmCap(BigDecimal.ZERO);
                }
                //判断记录是否已经存在
                Long evaId = consEvaluationMapper.getEvaluationAppeal(subsidyAppealParam);
                if(null!=evaId) {
                    consEvaluationNew.setUpdateTime(new Date());
                    consEvaluationNew.setEvaluationId(evaId);
                    consEvaluationAppealService.updateById(consEvaluationNew);
                } else {
                    long eid = IdWorker.getId();
                    consEvaluationNew.setEvaluationId(eid);
                    consEvaluationNew.setCreateTime(new Date());
                    consEvaluationAppealService.save(consEvaluationNew);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            redisTemplate.delete(key);
        }

        return ResponseData.success(consEvaluationNew);
    }

    @Override
    public void sumbitCheck(SubsidyAppealParam subsidyAppealParam) {
        if(null==subsidyAppealParam.getEventId() || null==subsidyAppealParam.getConsId()) {
            throw new ServiceException(SubsidyExceptionEnum.NO_PARAM_REC);
        }
        Event event  = eventService.getById(subsidyAppealParam.getEventId());
        if(null==event) {
            throw new ServiceException(SubsidyExceptionEnum.NO_EVENT_REC);
        } else {
            if(!"04".equals(event.getEventStatus())) {
                throw new ServiceException(SubsidyExceptionEnum.EVENT_NOTEND_REC);
            }
        }
        LocalDate localDate = event.getRegulateDate().plusDays(1);
        //当前时间小于等于调度日期后一天，不允许申诉
        if(LocalDate.now().compareTo(localDate)<=0) {
            throw new ServiceException(SubsidyExceptionEnum.NO_TIME_REC);
        }
        LambdaQueryWrapper<Plan> planLambdaQueryWrapper = new LambdaQueryWrapper<>();
        planLambdaQueryWrapper.eq(Plan::getRegulateId,subsidyAppealParam.getEventId());
        List<Plan> plans = planService.list(planLambdaQueryWrapper);
        if(null==plans || plans.size()==0) {
            throw new ServiceException(SubsidyExceptionEnum.NO_PLAN_REC);
        }
        LambdaQueryWrapper<PlanCons> planConsLambdaQueryWrapper = new LambdaQueryWrapper<>();
        planConsLambdaQueryWrapper.eq(PlanCons::getConsId,subsidyAppealParam.getConsId());
        planConsLambdaQueryWrapper.eq(PlanCons::getPlanId,plans.get(0).getPlanId());
        planConsLambdaQueryWrapper.eq(PlanCons::getImplement,"Y");
        List<PlanCons> planCons = planConsService.list(planConsLambdaQueryWrapper);
        if(null==planCons||planCons.size()==0) {
            throw new ServiceException(SubsidyExceptionEnum.NO_PLAN_EXECUTE_REC);
        }
        //如果该事件用户已有申诉，不允许再提交
        LambdaQueryWrapper<SubsidyAppeal> subsidyAppealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        subsidyAppealLambdaQueryWrapper.eq(SubsidyAppeal::getEventId,subsidyAppealParam.getEventId());
        subsidyAppealLambdaQueryWrapper.eq(SubsidyAppeal::getConsId,subsidyAppealParam.getConsId());
        List<SubsidyAppeal> subsidyAppeals = this.list(subsidyAppealLambdaQueryWrapper);
        if(null!=subsidyAppeals && subsidyAppeals.size()>0) {
            for(SubsidyAppeal subsidyAppeal : subsidyAppeals) {
                if (!"6".equals(subsidyAppeal.getStatus())) {
                    throw new ServiceException(SubsidyExceptionEnum.DUPLICATE_APPEAL);
                }
            }
        }
        BusConfigParam busConfigParam = new BusConfigParam();
        CurrenUserInfo currentUserInfo = SecurityUtils.getCurrentUserInfoUTF8();
        if(ObjectUtil.isNull(currentUserInfo)) {
            throw new ServiceException(CurrenUserInfoExceptionEnum.CURRENT_USER_INFO_NOT_EXIST);
        }
        // 创建待办任务成功，否则为失败
        // 修改竞价的状态为提交审核
        SubsidyAppeal subsidyAppeal = new SubsidyAppeal();
        BeanUtils.copyProperties(subsidyAppealParam,subsidyAppeal);
        subsidyAppeal.setIsAppeal("N");
        subsidyAppeal.setStatus(BiddingCheckStatusEnums.UNSUBMITTED.getCode());
        this.save(subsidyAppeal);

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
        busConfigParam.setBusId(String.valueOf(subsidyAppeal.getId()));
        busConfigParam.setApplyOrgId(currentUserInfo.getOrgId()); // 申请人组织机构id
        busConfigParam.setBusType(BusTypeEnum.SUBSIDY_PROCESS.getCode()); // 业务类型
        busConfigParam.setApplyManId(Long.parseLong(currentUserInfo.getId())); // 申请人id
        busConfigParam.setLevel(1);
        busConfigParam.setOperaManId(currentUserInfo.getId());

        Result result = null;
        try{
            System.out.println(busConfigParam);
            result = systemClient.selectInfo(busConfigParam);
        }catch (Exception e){
            log.error(e.toString());
        }finally{
            if(ObjectUtil.isNotNull(result) && result.getCode().equals("000000") && result.getData().getString("handleCode").equals("000")) {
                //保存
                subsidyAppeal.setStatus("1");
                this.updateById(subsidyAppeal);

            }else{
                // 创建失败，抛出异常
                this.removeById(subsidyAppeal);
                //throw new ServiceException(ExamineExceptionEnum.CREATION_FAILED);
            }
        }
        return;
    }


    @Override
    public void examine(ExamineParam examineParam) {
        BusConfigParam busConfigParam = new BusConfigParam();
        BeanUtil.copyProperties(examineParam,busConfigParam);
        // 暂时不行
        // CurrenUserInfo sysLoginUser = LoginContextHolder.me().getSysLoginUser();
        CurrenUserInfo currentUserInfo = SecurityUtils.getCurrentUserInfoUTF8();
        if(ObjectUtil.isNull(currentUserInfo)) {
            throw new ServiceException(CurrenUserInfoExceptionEnum.CURRENT_USER_INFO_NOT_EXIST);
        }
        busConfigParam.setOperaManId(currentUserInfo.getId()); // 操作人id
        busConfigParam.setBusType(BusTypeEnum.SUBSIDY_PROCESS.getCode()); // 业务类型
        Result result = systemClient.selectInfo(busConfigParam);
        System.out.println(busConfigParam);
        if(ObjectUtil.isNotNull(result) && result.getCode().equals("000000")) {
            if(result.getData().getString("handleCode").equals(ApprovalCodeEnum.PROCESS_FAIL.getCode())) {
                // 审核流程失败
                throw new ServiceException(ExamineExceptionEnum.APPROVAL_FAIL);
            }
            // 修改异议工单的状态
            SubsidyAppeal subsidyAppeal = new SubsidyAppeal();
            SubsidyAppealParam subsidyAppealParam = new SubsidyAppealParam();

            SubsidyAppeal detail = this.detail(Long.parseLong(examineParam.getBusId()));
            BeanUtils.copyProperties(detail,subsidyAppeal);

            // 审核结束
            if(result.getData().getString("handleCode").equals(ApprovalCodeEnum.PROCESS_SUCCESS.getCode())) {
                subsidyAppeal.setStatus(ProjectCheckStatusEnums.PASS_THE_AUDIT.getCode());
            }
            // 申请被驳回
            if(result.getData().getString("handleCode").equals(ApprovalCodeEnum.APPROVAL_REJECT.getCode())) {
                subsidyAppeal.setStatus(ProjectCheckStatusEnums.AUDIT_FAILED.getCode());
            }
            this.updateById(subsidyAppeal);
            return ;
        }
        // 接口状态码不为000000，抛出异常
        throw new ServiceException(ExamineExceptionEnum.CREATION_FAILED);
    }

    @Override
    public String getFileName(String fileId)
    {
        if(ObjectUtil.isEmpty(fileId)) {
            return "";
        }
        String fileName=getBaseMapper().getFileName(fileId);
        return  fileName;
    }


    @Override
    public Page<SubsidyAppeal> cityManagePage(SubsidyAppealParam subsidyAppealParam) {
        //获取当前用户权限下可查看机构集合
        String orgId = SecurityUtils.getCurrentUserInfoUTF8().getOrgId();
        List<String> orgList =  systemClient.getAllNextOrgId(orgId).getData();
        subsidyAppealParam.setOrgNos(orgList);
        //查询市专责管理记录列表
        List<SubsidyAppeal> cityManageList = baseMapper.getCityManageList(subsidyAppealParam);
        Integer cityManageTotals = baseMapper.getCityManageTotals(subsidyAppealParam);
        //记录是否升序排序
        if(!subsidyAppealParam.isDesc()){
            Collections.reverse(cityManageList);
        }
        //记录分页展示
        Page<SubsidyAppeal> page = new Page<>();
        page.setTotal(cityManageTotals);
        page.setRecords(cityManageList);
        return page;
    }

    @Override
    public SubsidyAppeal appealDetail(long id) {
        SubsidyAppeal subsidyAppeal = baseMapper.appealDetail(id);
        if(subsidyAppeal != null) {
            return subsidyAppeal;
        }
        throw new ServiceException(SubsidyExceptionEnum.SUBSIDY_EXCEPTION_NUll);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseData synSubsidy(SubsidyAppealAmendParam subsidyAppealParam) {
        Long eventId = subsidyAppealParam.getEventId();
        String consId = subsidyAppealParam.getConsId();
        String key = "susidyAppealSyn" + eventId + consId;
        boolean flag = redisTemplate.hasKey(key);
        if(flag) {
            return ResponseData.fail("-1","数据正在计算中!","数据正在计算中!");
        }
        ConsEvaluationAppeal consEvaluationNew = null;
        //用户效果评估
        ConsEvaluation consEvaluation = null;
        try {
            //redis发送状态值,上锁
            Map<String, Object> eventInfo = new HashMap<>();
            eventInfo.put(key, "1");
            JSONObject eventInfoJson = new JSONObject(eventInfo);
            redisTemplate.convertAndSend(key, eventInfoJson.toJSONString());
            LambdaQueryWrapper<SubsidyAppeal> subsidyAppealLambdaQueryWrapper = new LambdaQueryWrapper<>();
            subsidyAppealLambdaQueryWrapper.eq(SubsidyAppeal::getId,subsidyAppealParam.getId());
            List<SubsidyAppeal> subsidyAppeals = this.list(subsidyAppealLambdaQueryWrapper);
            if(null!=subsidyAppeals && subsidyAppeals.size()>0) {
                if("4".equals(subsidyAppeals.get(0).getStatusCity())) {
                    return ResponseData.success("数据已同步过!", "数据已同步过!");
                } else if(!"5".equals(subsidyAppeals.get(0).getStatusCity())){
                    return ResponseData.fail("-1", "未审批通过，不可同步!", "未审批通过，不可同步!");
                }
            } else {
                return ResponseData.fail("-1","无申诉记录，无法同步数据!","无申诉记录，无法同步数据!");
            }
            Event event = eventService.getById(eventId);
            if(null==event) {
                return ResponseData.fail("-1","无事件信息!","无事件信息!");
            }
            Project project = projectService.getById(event.getProjectId());
            if(null==project) {
                return ResponseData.fail("-1","无项目信息!","无项目信息!");
            }
            //有效时长
            int startP = CurveUtil.covDateTimeToPoint(event.getStartTime());
            int endP = CurveUtil.covDateTimeToPoint(event.getEndTime());
            int hour = (endP-startP)*15;
            if(null!=subsidyAppealParam) {
                LambdaQueryWrapper<ConsEvaluationAppeal> consEvaluationAppealLambdaQueryWrapper = new LambdaQueryWrapper<>();
                consEvaluationAppealLambdaQueryWrapper.eq(ConsEvaluationAppeal::getSubsidyAppealId,subsidyAppealParam.getId());
                //效果评估新表
                List<ConsEvaluationAppeal > consEvaluationAppealList = consEvaluationAppealService.list(consEvaluationAppealLambdaQueryWrapper);
                if(null==consEvaluationAppealList || consEvaluationAppealList.size()==0) {
                    return ResponseData.fail("-1","无申诉重算数据!","无申诉重算数据!");
                } else {
                    consEvaluationNew = consEvaluationAppealList.get(0);
                }
                LambdaQueryWrapper<ConsEvaluation> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.eq(ConsEvaluation::getConsId,consId);
                lambdaQueryWrapper.eq(ConsEvaluation::getEventId,eventId);
                List<ConsEvaluation> consEvaluations = consEvaluationService.list(lambdaQueryWrapper);
                List<ConsEvaluation> consEvaluations2 = consEvaluationService.list(lambdaQueryWrapper);
                if(null!=consEvaluations && consEvaluations.size()>0) {
                    consEvaluation = consEvaluations.get(0);
                    //已同步过
                    if(null!=consEvaluation.getIsAppeal()) {
                        BeanUtils.copyProperties(consEvaluationNew,consEvaluation);
                        consEvaluation.setEvaluationId(consEvaluations2.get(0).getEvaluationId());
                        consEvaluationService.updateById(consEvaluation);
                    } else {
                        //未同步过
                        consEvaluation.setIsAppeal("1");
                        BeanUtils.copyProperties(consEvaluationNew,consEvaluation);
                        consEvaluation.setEvaluationId(consEvaluations2.get(0).getEvaluationId());
                        consEvaluation.setActualCapOld(consEvaluations2.get(0).getActualCap());
                        consEvaluation.setConfirmCapOld(consEvaluations2.get(0).getConfirmCap());
                        consEvaluation.setEffectiveTimeOld(consEvaluations2.get(0).getEffectiveTime());
                        consEvaluation.setIsEffectiveOld(consEvaluations2.get(0).getIsEffective());
                        consEvaluation.setIsOutOld(consEvaluations2.get(0).getIsOut());
                        consEvaluation.setIsQualifiedOld(consEvaluations2.get(0).getIsQualified());
                        consEvaluation.setAvgLoadActualOld(consEvaluations2.get(0).getAvgLoadActual());
                        consEvaluation.setAvgLoadBaselineOld(consEvaluations2.get(0).getAvgLoadBaseline());
                        consEvaluation.setMaxLoadActualOld(consEvaluations2.get(0).getMaxLoadActual());
                        consEvaluation.setMinLoadActualOld(consEvaluations2.get(0).getMinLoadActual());
                        consEvaluation.setMaxLoadBaselineOld(consEvaluations2.get(0).getMaxLoadBaseline());
                        consEvaluation.setMinLoadBaselineOld(consEvaluations2.get(0).getMinLoadBaseline());
                        consEvaluationService.updateById(consEvaluation);
                    }
                } else {
                    //未同步过
                    consEvaluation.setIsAppeal("1");
                    BeanUtils.copyProperties(consEvaluationNew,consEvaluation);
                    long id = IdWorker.getId();
                    consEvaluation.setEvaluationId(id);
                    consEvaluation.setActualCapOld(consEvaluations2.get(0).getActualCap());
                    consEvaluation.setConfirmCapOld(consEvaluations2.get(0).getConfirmCap());
                    consEvaluation.setEffectiveTimeOld(consEvaluations2.get(0).getEffectiveTime());
                    consEvaluation.setIsEffectiveOld(consEvaluations2.get(0).getIsEffective());
                    consEvaluation.setIsOutOld(consEvaluations2.get(0).getIsOut());
                    consEvaluation.setIsQualifiedOld(consEvaluations2.get(0).getIsQualified());
                    consEvaluation.setAvgLoadActualOld(consEvaluations2.get(0).getAvgLoadActual());
                    consEvaluation.setAvgLoadBaselineOld(consEvaluations2.get(0).getAvgLoadBaseline());
                    consEvaluation.setMaxLoadActualOld(consEvaluations2.get(0).getMaxLoadActual());
                    consEvaluation.setMinLoadActualOld(consEvaluations2.get(0).getMinLoadActual());
                    consEvaluation.setMaxLoadBaselineOld(consEvaluations2.get(0).getMaxLoadBaseline());
                    consEvaluation.setMinLoadBaselineOld(consEvaluations2.get(0).getMinLoadBaseline());
                    consEvaluationService.save(consEvaluation);
                }
                //用户事件补贴
                LambdaQueryWrapper<ConsSubsidy> lambdaSubsidyQueryWrapper = new LambdaQueryWrapper<>();
                lambdaSubsidyQueryWrapper.eq(ConsSubsidy::getEventId,eventId);
                lambdaSubsidyQueryWrapper.eq(ConsSubsidy::getConsId,consId);
                List<ConsSubsidy> consSubsidyList = consSubsidyService.list(lambdaSubsidyQueryWrapper);
                List<ConsSubsidy> consSubsidyList2 = consSubsidyService.list(lambdaSubsidyQueryWrapper);
                //查询申诉补贴
                LambdaQueryWrapper<ConsSubsidyAppeal> consSubsidyAppealLambdaQueryWrapper = new LambdaQueryWrapper<>();
                consSubsidyAppealLambdaQueryWrapper.eq(ConsSubsidyAppeal::getSubsidyAppealId,subsidyAppealParam.getId());
                List<ConsSubsidyAppeal> consSubsidyAppealList = consSubsidyAppealService.list(consSubsidyAppealLambdaQueryWrapper);
                if(null!=consSubsidyList && consSubsidyList.size()>0) {
                    //已同步
                    if(null!=consSubsidyList.get(0).getIsAppeal()) {
                        if(null!=consSubsidyAppealList && consSubsidyAppealList.size()>0) {
                            BeanUtils.copyProperties(consSubsidyAppealList.get(0), consSubsidyList.get(0));
                            consSubsidyList.get(0).setSubsidyId(consSubsidyList2.get(0).getSubsidyId());
                            consSubsidyService.updateById(consSubsidyList.get(0));
                        }
                    } else {
                        //未同步
                        consSubsidyList.get(0).setIsAppeal("1");
                        if(null!=consSubsidyAppealList.get(0)) {
                            BeanUtils.copyProperties(consSubsidyAppealList.get(0), consSubsidyList.get(0));
                        }
                        consSubsidyList.get(0).setSubsidyId(consSubsidyList2.get(0).getSubsidyId());
                        consSubsidyList.get(0).setRateCoefficientOld(consSubsidyList2.get(0).getRateCoefficient());
                        consSubsidyList.get(0).setSettledAmountOld(consSubsidyList2.get(0).getSettledAmount());
                        consSubsidyList.get(0).setSubsidyAmountOld(consSubsidyList2.get(0).getSubsidyAmount());
                        consSubsidyList.get(0).setTimeCoefficientOld(consSubsidyList2.get(0).getTimeCoefficient());
                        consSubsidyService.updateById(consSubsidyList.get(0));
                    }
                } else {
                    ConsSubsidy consSubsidy = new ConsSubsidy();
                    if(null!=consSubsidyAppealList.get(0)) {
                        BeanUtils.copyProperties(consSubsidyAppealList.get(0), consSubsidy);
                    }
                    consSubsidy.setIsAppeal("1");
                    long id = IdWorker.getId();
                    consSubsidy.setSubsidyId(id);
                    consSubsidyService.save(consSubsidy);
                }
                //客户效果评估
                Cust cust = custService.getCustByConsId(consId);
                List<Long> custIds = null;
                //查询存在次日评估有效用户的客户
                if(null!=cust) {
                    custIds = evaluationImmediateService.getNextDayCustIdsByEventId(eventId,cust.getId());
                }
                //查询事件参与客户
                List<CustInvitation> custInvitations = custInvitationService.getConsInfoByEvent(eventId,cust.getId());
                CustInvitation eventInvitation = null;
                if (null != custInvitations && custInvitations.size() > 0) {
                    eventInvitation = custInvitations.get(0);
                }
                LambdaQueryWrapper<CustEvaluation> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(CustEvaluation::getEventId, eventId);
                queryWrapper.eq(CustEvaluation::getCustId,cust.getId());
                List<CustEvaluation> evaluationList = custEvaluationService.list(queryWrapper);
                List<CustEvaluation> evaluationList2 = custEvaluationService.list(queryWrapper);
                LambdaQueryWrapper<CustEvaluation> queryCustWrapper = new LambdaQueryWrapper<>();
                queryCustWrapper.eq(CustEvaluation::getEventId, eventId);
                queryCustWrapper.eq(CustEvaluation::getCustId,cust.getId());
                List<CustEvaluation> evaluations = custEvaluationService.list(queryCustWrapper);
                CustEvaluation custEvaluationAppeal = new CustEvaluation();
                if(null!=evaluations && evaluations.size()>0) {
                    BeanUtils.copyProperties(evaluations.get(0),custEvaluationAppeal);
                } else {
                    custEvaluationAppeal.setCustId(cust.getId());
                    custEvaluationAppeal.setEventId(eventId);
                    custEvaluationAppeal.setActualCap(eventInvitation.getReplyCap());
                    custEvaluationAppeal.setConfirmCap(eventInvitation.getReplyCap());
                }
                //如果原表只有一个有效用户，申诉以后将该用户变为无效，客户状态还是有效
                if("0".equals(eventInvitation.getIntegrator())) {
                    //直接客户如果有效果评估有效用户，直接有效
                    if (null != custIds && custIds.size() > 0) {
                        if (custIds.contains(eventInvitation.getCustId())) {
                            custEvaluationAppeal.setEffectiveTime(hour);
                            custEvaluationAppeal.setIsEffective(YesOrNotEnum.Y.getCode());
                            custEvaluationAppeal.setRemark("数据申诉成功");
                            if (null == evaluationList || evaluationList.size() == 0) {
                                long id = IdWorker.getId();
                                custEvaluationAppeal.setEvaluationId(id);
                                custEvaluationAppeal.setIsAppeal("1");
                                custEvaluationAppeal.setActualCapOld(evaluationList2.get(0).getActualCap());
                                custEvaluationAppeal.setConfirmCapOld(evaluationList2.get(0).getConfirmCap());
                                custEvaluationAppeal.setEffectiveTimeOld(evaluationList2.get(0).getEffectiveTime());
                                custEvaluationAppeal.setIsEffectiveOld(evaluationList2.get(0).getIsEffective());
                                custEvaluationAppeal.setIsOutOld(evaluationList2.get(0).getIsOut());
                                custEvaluationAppeal.setIsQualifiedOld(evaluationList2.get(0).getIsQualified());
                                custEvaluationAppeal.setAvgLoadActualOld(evaluationList2.get(0).getAvgLoadActual());
                                custEvaluationAppeal.setAvgLoadBaselineOld(evaluationList2.get(0).getAvgLoadBaseline());
                                custEvaluationAppeal.setMaxLoadActualOld(evaluationList2.get(0).getMaxLoadActual());
                                custEvaluationAppeal.setMinLoadActualOld(evaluationList2.get(0).getMinLoadActual());
                                custEvaluationAppeal.setMaxLoadBaselineOld(evaluationList2.get(0).getMaxLoadBaseline());
                                custEvaluationAppeal.setMinLoadBaselineOld(evaluationList2.get(0).getMinLoadBaseline());
                                custEvaluationService.save(custEvaluationAppeal);
                            } else {
                                //已同步过
                                if(null!=evaluationList.get(0).getIsAppeal()) {
                                    custEvaluationAppeal.setEvaluationId(evaluationList.get(0).getEvaluationId());
                                    custEvaluationService.updateById(custEvaluationAppeal);
                                } else {
                                    //未同步过
                                    custEvaluationAppeal.setIsAppeal("1");
                                    custEvaluationAppeal.setEvaluationId(evaluationList.get(0).getEvaluationId());
                                    custEvaluationAppeal.setActualCapOld(evaluationList2.get(0).getActualCap());
                                    custEvaluationAppeal.setConfirmCapOld(evaluationList2.get(0).getConfirmCap());
                                    custEvaluationAppeal.setEffectiveTimeOld(evaluationList2.get(0).getEffectiveTime());
                                    custEvaluationAppeal.setIsEffectiveOld(evaluationList2.get(0).getIsEffective());
                                    custEvaluationAppeal.setIsOutOld(evaluationList2.get(0).getIsOut());
                                    custEvaluationAppeal.setIsQualifiedOld(evaluationList2.get(0).getIsQualified());
                                    custEvaluationAppeal.setAvgLoadActualOld(evaluationList2.get(0).getAvgLoadActual());
                                    custEvaluationAppeal.setAvgLoadBaselineOld(evaluationList2.get(0).getAvgLoadBaseline());
                                    custEvaluationAppeal.setMaxLoadActualOld(evaluationList2.get(0).getMaxLoadActual());
                                    custEvaluationAppeal.setMinLoadActualOld(evaluationList2.get(0).getMinLoadActual());
                                    custEvaluationAppeal.setMaxLoadBaselineOld(evaluationList2.get(0).getMaxLoadBaseline());
                                    custEvaluationAppeal.setMinLoadBaselineOld(evaluationList2.get(0).getMinLoadBaseline());
                                    custEvaluationService.updateById(custEvaluationAppeal);
                                }
                            }
                        }
                    } else {
                        custEvaluationAppeal.setEffectiveTime(0);
                        custEvaluationAppeal.setIsEffective(YesOrNotEnum.N.getCode());
                        custEvaluationAppeal.setRemark("客户无有效用户，数据申诉失败");
                        if (null == evaluationList || evaluationList.size() == 0) {
                            long id = IdWorker.getId();
                            custEvaluationAppeal.setEvaluationId(id);
                            custEvaluationAppeal.setIsAppeal("1");
                            custEvaluationAppeal.setActualCapOld(evaluationList2.get(0).getActualCap());
                            custEvaluationAppeal.setConfirmCapOld(evaluationList2.get(0).getConfirmCap());
                            custEvaluationAppeal.setEffectiveTimeOld(evaluationList2.get(0).getEffectiveTime());
                            custEvaluationAppeal.setIsEffectiveOld(evaluationList2.get(0).getIsEffective());
                            custEvaluationAppeal.setIsOutOld(evaluationList2.get(0).getIsOut());
                            custEvaluationAppeal.setIsQualifiedOld(evaluationList2.get(0).getIsQualified());
                            custEvaluationAppeal.setAvgLoadActualOld(evaluationList2.get(0).getAvgLoadActual());
                            custEvaluationAppeal.setAvgLoadBaselineOld(evaluationList2.get(0).getAvgLoadBaseline());
                            custEvaluationAppeal.setMaxLoadActualOld(evaluationList2.get(0).getMaxLoadActual());
                            custEvaluationAppeal.setMinLoadActualOld(evaluationList2.get(0).getMinLoadActual());
                            custEvaluationAppeal.setMaxLoadBaselineOld(evaluationList2.get(0).getMaxLoadBaseline());
                            custEvaluationAppeal.setMinLoadBaselineOld(evaluationList2.get(0).getMinLoadBaseline());
                            custEvaluationService.save(custEvaluationAppeal);
                        } else {
                            evaluationList.get(0).setIsEffective("N");
                            if(null!=evaluationList.get(0).getIsAppeal()) {
                                custEvaluationAppeal.setEvaluationId(evaluationList.get(0).getEvaluationId());
                                custEvaluationService.updateById(custEvaluationAppeal);
                            } else {
                                custEvaluationAppeal.setIsAppeal("1");
                                custEvaluationAppeal.setEvaluationId(evaluationList.get(0).getEvaluationId());
                                custEvaluationAppeal.setActualCapOld(evaluationList2.get(0).getActualCap());
                                custEvaluationAppeal.setConfirmCapOld(evaluationList2.get(0).getConfirmCap());
                                custEvaluationAppeal.setEffectiveTimeOld(evaluationList2.get(0).getEffectiveTime());
                                custEvaluationAppeal.setIsEffectiveOld(evaluationList2.get(0).getIsEffective());
                                custEvaluationAppeal.setIsOutOld(evaluationList2.get(0).getIsOut());
                                custEvaluationAppeal.setIsQualifiedOld(evaluationList2.get(0).getIsQualified());
                                custEvaluationAppeal.setAvgLoadActualOld(evaluationList2.get(0).getAvgLoadActual());
                                custEvaluationAppeal.setAvgLoadBaselineOld(evaluationList2.get(0).getAvgLoadBaseline());
                                custEvaluationAppeal.setMaxLoadActualOld(evaluationList2.get(0).getMaxLoadActual());
                                custEvaluationAppeal.setMinLoadActualOld(evaluationList2.get(0).getMinLoadActual());
                                custEvaluationAppeal.setMaxLoadBaselineOld(evaluationList2.get(0).getMaxLoadBaseline());
                                custEvaluationAppeal.setMinLoadBaselineOld(evaluationList2.get(0).getMinLoadBaseline());
                                custEvaluationService.updateById(custEvaluationAppeal);
                            }
                        }
                    }

                } else {
                    //集成商
                }
                //客户补贴
                if("0".equals(eventInvitation.getIntegrator())) {
                    LambdaQueryWrapper<CustSubsidy> lambdaSubsidyCustQueryWrapper = new LambdaQueryWrapper<>();
                    lambdaSubsidyCustQueryWrapper.eq(CustSubsidy::getEventId, eventId);
                    lambdaSubsidyCustQueryWrapper.eq(CustSubsidy::getCustId, cust.getId());
                    List<CustSubsidy> custSubsidyList = custSubsidyService.list(lambdaSubsidyCustQueryWrapper);
                    List<CustSubsidy> custSubsidyList2 = custSubsidyService.list(lambdaSubsidyCustQueryWrapper);
                    ConsSubsidy consSubsidy = consSubsidyMapper.getCustSubsidy(eventId,cust.getId());
                    if(null!=consSubsidy) {
                        BigDecimal subsidyAmount = consSubsidy.getSubsidyAmount();
                        // BigDecimal settledAmount = consSubsidy.getSettledAmount();
                        if(null!=custSubsidyList && custSubsidyList.size()>0) {
                            //已同步
                            if(null!=custSubsidyList.get(0).getIsAppeal()) {
                                custSubsidyList.get(0).setActualCap(custEvaluationAppeal.getActualCap());
                                custSubsidyList.get(0).setSubsidyAmount(subsidyAmount);
                                custSubsidyService.updateById(custSubsidyList.get(0));
                            } else {
                                //未同步
                                custSubsidyList.get(0).setIsAppeal("1");
                                custSubsidyList.get(0).setActualCap(custEvaluationAppeal.getActualCap());
                                custSubsidyList.get(0).setSubsidyAmount(subsidyAmount);
                                custSubsidyList.get(0).setRateCoefficientOld(custSubsidyList2.get(0).getRateCoefficient());
                                custSubsidyList.get(0).setSubsidyAmountOld(custSubsidyList2.get(0).getSubsidyAmount());
                                custSubsidyList.get(0).setTimeCoefficientOld(custSubsidyList2.get(0).getTimeCoefficient());
                                custSubsidyService.updateById(custSubsidyList.get(0));
                            }
                        } else {
                            CustSubsidy custSubsidy = new CustSubsidy();
                            long id = IdWorker.getId();
                            custSubsidy.setSubsidyId(id);
                            custSubsidy.setIsAppeal("1");
                            custSubsidy.setActualCap(custEvaluationAppeal.getActualCap());
                            custSubsidy.setSubsidyAmount(subsidyAmount);
                            custSubsidy.setEventId(eventId);
                            custSubsidy.setCustId(cust.getId());
                            custSubsidy.setIntegrator("0");
                            custSubsidy.setCalRule("2");
                            custSubsidyService.save(custSubsidy);
                        }
                    }
                } else {
                    //集成商
                }
                //用户日补贴
                //根据日期查询用户补贴汇总
                List<ConsSubsidyDaily> consSubsidyDailyList = consSubsidyDailyMapper.getSubsidyDailyTotalByCondition(simpleDateFormat.format(event.getRegulateDate()),consId);
                //根据日期查询项目和事件关系
                List<ConsSubsidyDaily> consSubsidys = consSubsidyDailyMapper.getAllSubsidyDailyByDate(simpleDateFormat.format(event.getRegulateDate()));
                //根据日期查询日补贴记录
                LambdaQueryWrapper<ConsSubsidyDaily> queryConsDailyWrapper = new LambdaQueryWrapper<>();
                queryConsDailyWrapper.eq(ConsSubsidyDaily::getSubsidyDate,event.getRegulateDate());
                queryConsDailyWrapper.eq(ConsSubsidyDaily::getConsId,consId);
                List<ConsSubsidyDaily> consSubsidyDailies = consSubsidyDailyService.list(queryConsDailyWrapper);
                List<ConsSubsidyDaily> inserList = new ArrayList<>();
                List<ConsSubsidyDaily> updateList = new ArrayList<>();
                if(null!=consSubsidyDailyList && consSubsidyDailyList.size()>0) {
                    for(ConsSubsidyDaily consSubsidyDaily : consSubsidyDailyList) {
                        consSubsidyDaily.setSubTime(LocalDateTime.now());
                        //根据项目事件对应关系，设置用户参与事件
                        List<ConsSubsidyDaily> events =consSubsidys.stream().filter(dayily -> dayily.getConsId().equals(consSubsidyDaily.getConsId())
                                && dayily.getProjectId().equals(consSubsidyDaily.getProjectId())
                        ).collect(Collectors.toList());
                        if(null!=events && events.size()>0) {
                            consSubsidyDaily.setEventNum(events.size());
                            String eventIds = "";
                            for(ConsSubsidyDaily consSubsidyDaily1 : events) {
                                eventIds = eventIds+","+ consSubsidyDaily1.getEventId();
                            }
                            //截取第一个逗号
                            eventIds = eventIds.substring(1);
                            consSubsidyDaily.setEventIds(eventIds);
                        } else {
                            continue;
                        }
                        //查找日补贴是否已经存在
                        List<ConsSubsidyDaily> isExist = consSubsidyDailies.stream().filter(dayily -> dayily.getConsId().equals(consSubsidyDaily.getConsId())
                                && dayily.getProjectId().equals(consSubsidyDaily.getProjectId())
                        ).collect(Collectors.toList());
                        if(null!=isExist && isExist.size()>0) {
                            consSubsidyDaily.setId(isExist.get(0).getId());
                            if(null==isExist.get(0).getIsAppeal()) {
                                consSubsidyDaily.setIsAppeal("1");
                                consSubsidyDaily.setSettledAmountOld(isExist.get(0).getSettledAmount());
                            }
                            updateList.add(consSubsidyDaily);
                        } else {
                            consSubsidyDaily.setIsAppeal("1");
                            inserList.add(consSubsidyDaily);
                        }
                    }
                }
                if(null!=updateList && updateList.size()>0) {
                    consSubsidyDailyService.updateBatchById(updateList);
                }

                if(null!=inserList && inserList.size()>0) {
                    consSubsidyDailyService.saveBatch(inserList);
                }
                //客户日补贴
                //根据日期查询用户补贴汇总
                List<CustSubsidyDaily> custSubsidyDailyList = custSubsidyDailyMapper.getSubsidyDailyTotalByCondition(simpleDateFormat.format(event.getRegulateDate()),cust.getId());
                //根据日期查询项目和事件关系
                List<CustSubsidyDaily> custSubsidys = custSubsidyDailyMapper.getAllSubsidyDailyByDate(simpleDateFormat.format(event.getRegulateDate()));
                //根据日期查询日补贴记录
                LambdaQueryWrapper<CustSubsidyDaily> queryCustSubDailyWrapper = new LambdaQueryWrapper<>();
                queryCustSubDailyWrapper.eq(CustSubsidyDaily::getSubsidyDate,simpleDateFormat.format(event.getRegulateDate()));
                List<CustSubsidyDaily> custSubsidyDailies = custSubsidyDailyService.list(queryCustSubDailyWrapper);
                List<CustSubsidyDaily> inserCustList = new ArrayList<>();
                List<CustSubsidyDaily> updateCustList = new ArrayList<>();
                if(null!=custSubsidyDailyList && custSubsidyDailyList.size()>0) {
                    for(CustSubsidyDaily consSubsidyDaily : custSubsidyDailyList) {
                        consSubsidyDaily.setSubTime(LocalDateTime.now());
                        //根据项目事件对应关系，设置用户参与事件
                        List<CustSubsidyDaily> events =custSubsidys.stream().filter(dayily -> dayily.getCustId().equals(consSubsidyDaily.getCustId())
                                && dayily.getProjectId().equals(consSubsidyDaily.getProjectId())
                        ).collect(Collectors.toList());
                        if(null!=events && events.size()>0) {
                            consSubsidyDaily.setEventNum(events.size());
                            String eventIds = "";
                            for(CustSubsidyDaily custSubsidyDaily : events) {
                                eventIds = eventIds+","+ custSubsidyDaily.getEventId();
                            }
                            //截取第一个逗号
                            eventIds = eventIds.substring(1);
                            consSubsidyDaily.setEventIds(eventIds);
                        } else {
                            continue;
                        }
                        //查找日补贴是否已经存在
                        List<CustSubsidyDaily> isExist = custSubsidyDailies.stream().filter(dayily -> dayily.getCustId().equals(consSubsidyDaily.getCustId())
                                && dayily.getProjectId().equals(consSubsidyDaily.getProjectId())
                        ).collect(Collectors.toList());
                        if(null!=isExist && isExist.size()>0) {
                            consSubsidyDaily.setId(isExist.get(0).getId());
                            if(null==isExist.get(0).getIsAppeal()) {
                                consSubsidyDaily.setIsAppeal("1");
                                consSubsidyDaily.setSettledAmountOld(isExist.get(0).getSettledAmount());
                            }
                            updateCustList.add(consSubsidyDaily);
                        } else {
                            consSubsidyDaily.setIsAppeal("1");
                            inserCustList.add(consSubsidyDaily);
                        }
                    }
                }
                if(null!=updateCustList && updateCustList.size()>0) {
                    custSubsidyDailyService.updateBatchById(updateCustList);
                }

                if(null!=inserCustList && inserCustList.size()>0) {
                    custSubsidyDailyService.saveBatch(inserCustList);
                }
                //更新市专责状态为修正完成
                subsidyAppeals.get(0).setStatusCity("4");
                subsidyAppeals.get(0).setIsAppeal("Y");
                this.updateById(subsidyAppeals.get(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            redisTemplate.delete(key);
        }
        return ResponseData.success("数据正在同步，请等待一分钟","数据正在同步，请等待一分钟");
    }


}
