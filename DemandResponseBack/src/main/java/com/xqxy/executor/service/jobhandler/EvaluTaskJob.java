package com.xqxy.executor.service.jobhandler;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.thread.ExecutorBuilder;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.xqxy.core.enums.YesOrNotEnum;
import com.xqxy.dr.modular.baseline.entity.BaseLine;
import com.xqxy.dr.modular.baseline.entity.CustBaseLineDetail;
import com.xqxy.dr.modular.baseline.mapper.BaseLineMapper;
import com.xqxy.dr.modular.baseline.mapper.CustBaseLineMapper;
import com.xqxy.dr.modular.baseline.service.BaseLineService;
import com.xqxy.dr.modular.baseline.service.CustBaseLineDetailService;
import com.xqxy.dr.modular.baseline.util.CurveUtil;
import com.xqxy.dr.modular.data.entity.ConsCurve;
import com.xqxy.dr.modular.data.entity.ConsCurveToday;
import com.xqxy.dr.modular.data.service.ConsCurveService;
import com.xqxy.dr.modular.data.service.ConsCurveTodayService;
import com.xqxy.dr.modular.evaluation.entity.*;
import com.xqxy.dr.modular.evaluation.param.EvaluCustTaskParam;
import com.xqxy.dr.modular.evaluation.param.EvaluTaskParam;
import com.xqxy.dr.modular.evaluation.service.*;
import com.xqxy.dr.modular.event.entity.*;
import com.xqxy.dr.modular.event.service.*;
import com.xqxy.dr.modular.strategy.Utils.StrategyUtils;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.log.XxlJobLogger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

/**
 * @Description 效果评估四个定时任务
 * @ClassName EventJob
 * @Author chen zhi jun
 * @date 2021.05.11 14:35
 */
@Component
public class EvaluTaskJob {
    private static final Log log = Log.get();

    @Resource
    private EventService eventService;

    @Resource
    private PlanService planService;

    @Resource
    private ConsInvitationService consInvitationService;

    @Resource
    private CustInvitationService custInvitationService;

    @Resource
    private EvaluTaskService evaluTaskService;

    @Resource
    private EvaluCustTaskService evaluCustTaskService;

    @Resource
    private ConsEvaluationService consEvaluationService;

    @Resource
    private EvaluationImmediateService evaluationImmediateService;

    @Resource
    private EventPowerExecuteService eventPowerExecuteService;

    @Resource
    private CustEvaluationService custEvaluationService;

    @Resource
    private EventPowerExecuteImmediateService eventPowerExecuteImmediateService;

    @Resource
    private CustEvaluationImmediateService custEvaluationImmediateService;

    @Resource
    private ConsCurveService consCurveService;

    @Resource
    private ConsBaselineService consBaselineService;

    @Resource
    private CustBaseLineDetailService custBaseLineDetailService;

    @Resource
    private ConsCurveTodayService consCurveTodayService;

    @Resource
    private BaseLineMapper baseLineMapper;

    @Resource
    private CustBaseLineMapper custBaseLineMapper;

    @Resource
    private BaseLineService baseLineService;

    @Value("${executor.corePoolSize}")
    private int corePoolSize;

    @Value("${executor.maxPoolSize}")
    private int maxPoolSize;

    @Value("${executor.workQueue}")
    private int workQueue;

    @Value("${spring.datasource.mysql.username}")
    private String userName;

    @Value("${spring.datasource.mysql.jdbc-url}")
    private String dataurl;

    @Value("${spring.datasource.mysql.password}")
    private String datapassword;

    @Value("${spring.datasource.mysql.driver-class-name}")
    private String driver;

    private final DateTimeFormatter simpleDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 当日效果评估
     *
     * @throws Exception
     */
    @XxlJob("executeEvaluationListImmediate")
    public ReturnT<String> executeEvaluationListImmediates(String param) throws Exception {
        XxlJobLogger.log("当日效果评估");
        this.executeEvaluationListImmediate(param);
        // default success
        return ReturnT.SUCCESS;
    }

    /**
     * 客户当日效果评估
     *
     * @throws Exception
     */
    @XxlJob("executeCustEvaluationListImmediate")
    public ReturnT<String> executeCustEvaluationListImmediates(String param) throws Exception {
        XxlJobLogger.log("客户当日效果评估");
        this.executeCustEvaluationListImmediate(param);
        // default success
        return ReturnT.SUCCESS;
    }

    /**
     * 次日效果评估
     *
     * @throws Exception
     */
    @XxlJob("executeEvaluationList")
    public ReturnT<String> executeEvaluationLists(String param) throws Exception {
        XxlJobLogger.log("次日效果评估");
        this.executeEvaluationListNew(param);
        // default success
        return ReturnT.SUCCESS;
    }

    /**
     * 次日客户效果评估
     *
     * @throws Exception
     */
    @XxlJob("executeCustEvaluationList")
    public ReturnT<String> executeCustEvaluationLists(String param) throws Exception {
        XxlJobLogger.log("次日效果评估");
        this.executeCustEvaluationList(param);
        // default success
        return ReturnT.SUCCESS;
    }

    /**
     * 当日事件效果评估
     */
    public void executeEvaluationListImmediate(String param) {
        log.info(">>> 当日效果评估任务开始");
        LocalDate localDate = LocalDate.now();
        // 1.查询当日效果评估任务
        EvaluTaskParam evaluTaskParam = new EvaluTaskParam();
        if(null!=param && !"".equals(param)) {
            if(StrategyUtils.isDate(param)) {
                evaluTaskParam.setRegulateDate(LocalDate.parse(param));
                evaluTaskParam.setRegulateDateStr(param);
            } else {
                evaluTaskParam.setRegulateDate(localDate);
                evaluTaskParam.setRegulateDateStr(simpleDateFormat.format(localDate));
            }
        } else {
            evaluTaskParam.setRegulateDate(localDate);
            evaluTaskParam.setRegulateDateStr(simpleDateFormat.format(localDate));
        }
        evaluTaskParam.setEvaluTodayStatus("1");
        List<EvaluTask> evaluTasks = null;
        evaluTasks = evaluTaskService.list(evaluTaskParam);
        // 2.循环对事件进行评价
        if (CollectionUtil.isEmpty(evaluTasks)) {
            log.info("无效果评估任务!");
            return;
        }
        ExecutorService executor = ExecutorBuilder.create()
                .setCorePoolSize(corePoolSize)
                .setMaxPoolSize(maxPoolSize)
                .setWorkQueue(new LinkedBlockingQueue<>(workQueue))
                .build();
        executor.execute(this.createEvaluationImmediateNew(evaluTasks,evaluTaskParam));
        executor.shutdown();
    }

    //当日
    public Runnable createEvaluationImmediateNew(List<EvaluTask>  evaluTasks,EvaluTaskParam evaluTaskParam) {
        LocalDate localDate = LocalDate.now();
        LocalDateTime localTime =LocalDateTime.now();
        LocalTime localTime2 = LocalTime.now();
        //查询事件信息
        /*LambdaQueryWrapper<Event> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Event::getRegulateDate, evaluTaskParam.getRegulateDate());
        lambdaQueryWrapper.eq(Event::getTimeType, "1");
        List<String> statusList = new ArrayList<>();
        statusList.add("03");
        statusList.add("04");
        lambdaQueryWrapper.in(Event::getEventStatus,statusList);*/
        Map<Integer, Method> consMethodMap = new HashMap<>();
        Map<Integer, Method> consMethodMap2 = new HashMap<>();
        StrategyUtils strategyUtils = new StrategyUtils();
        try{
            for (int j=1; j<=96; j++){
                consMethodMap.put(j, ConsBaseline.class.getMethod("getP"+j));
                consMethodMap2.put(j, ConsCurve.class.getMethod("getP"+j));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                long eventId = evaluTasks.get(0).getEventId();
                Event event = eventService.getById(eventId);
                PreparedStatement preparedStatement = null;
                Connection conn = null;
                PreparedStatement preparedStatement2 = null;
                Connection conn2 = null;
                PreparedStatement preparedStatement3 = null;
                Connection conn3 = null;
                PreparedStatement preparedStatement4 = null;
                Connection conn4 = null;
                PreparedStatement preparedStatement5 = null;
                Connection conn5 = null;
                if(null==event) {
                    log.info("无事件信息");
                    return;
                }
                // 判断事件是否已经截至
                Integer endHour = 0;
                Integer endMinute = 0;
                String status = event.getEventStatus();
                endHour = Integer.parseInt(event.getEndTime().substring(0, 2));
                endMinute = Integer.parseInt(event.getEndTime().substring(3));
                LocalTime endTime = LocalTime.of(endHour, endMinute);
                //String time = evaluTaskParam.getRegulateDate() + " " + event.getEndTime();
                //LocalDateTime compareTime = LocalDateTime.parse(time,DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                if (evaluTaskParam.getRegulateDate().compareTo(localDate) == 0) {
                    if (LocalTime.now().isBefore(endTime)) {
                        // 事件未截止，当前事件不用进行评价了
                        Integer count = event.getNextdayCount();
                        if(null==count) {
                            count = 0;
                        }
                        count = count + 1;
                        event.setNextdayCount(count);
                        eventService.updateById(event);
                        log.info(">>> 事件未截止，事件ID:{}", eventId);
                        return;
                    }
                } else if (evaluTaskParam.getRegulateDate().compareTo(localDate) > 0) {
                    Integer count = event.getNextdayCount();
                    if(null==count) {
                        count = 0;
                    }
                    count = count + 1;
                    event.setNextdayCount(count);
                    eventService.updateById(event);
                    // 事件未截止，当前事件不用进行评价了
                    log.info(">>> 事件未截止，事件ID:{}", eventId);
                    return;
                }
                try {
                    // 事件开始点和事件结束点
                    int startP = CurveUtil.covDateTimeToPoint(event.getStartTime());
                    int endP = CurveUtil.covDateTimeToPoint(event.getEndTime());
                    int hour = (endP-startP)*15;
                    //查询事件参与所有客户
                    List<ConsInvitation> consInvitations = consInvitationService.getConsInfoByEvent(eventId);
                    if (null == consInvitations || consInvitations.size() == 0) {
                        log.info("无用户邀约信息");
                        return;
                    }
                    ConsInvitation eventInvitation = null;
                    LambdaQueryWrapper<Plan> queryWrapperPlan = new LambdaQueryWrapper<>();
                    queryWrapperPlan.eq(Plan::getRegulateId,eventId);
                    List<Plan> planList = planService.list(queryWrapperPlan);
                    if (null == planList || planList.size() == 0) {
                        log.info("无方案信息");
                        return;
                    }
                    Plan plan = null;
                    //获取所有用户基线
                    //List<ConsBaseline> consBaselineList = baseLineMapper.getConsBaseLineInfo();
                    LambdaQueryWrapper<ConsBaseline> queryWrapperBase = new LambdaQueryWrapper<>();
                    queryWrapperBase.eq(ConsBaseline::getBaselineLibId,planList.get(0).getBaselinId());
                    List<ConsBaseline> consBaselineList = consBaselineService.list(queryWrapperBase);
                    List<ConsBaseline> consBaselines = null;
                    //获取效果今日评估数据
                    LambdaQueryWrapper<EvaluationImmediate> queryWrapper = new LambdaQueryWrapper<>();
                    queryWrapper.eq(EvaluationImmediate::getEventId, eventId);
                    List<EvaluationImmediate> evaluationImmediateList = evaluationImmediateService.list(queryWrapper);
                    EvaluationImmediate evaluationImmediate = null;
                    ConsBaseline consBaseline = null;
                    //获取实时曲线
                    LambdaQueryWrapper<ConsCurveToday> curveTodayLambdaQueryWrapper = new LambdaQueryWrapper<>();
                    curveTodayLambdaQueryWrapper.eq(ConsCurveToday::getDataDate, localDate);
                    List<ConsCurveToday> consCurveTodays = consCurveTodayService.list(curveTodayLambdaQueryWrapper);
                    //查询历史曲线
                    LambdaQueryWrapper<ConsCurve> curveLambdaQueryWrapper = new LambdaQueryWrapper<>();
                    curveLambdaQueryWrapper.eq(ConsCurve::getDataDate, evaluTaskParam.getRegulateDate());
                    List<ConsCurve> consCurveHistorys = consCurveService.list(curveLambdaQueryWrapper);
                    //查询执行曲线
                    LambdaQueryWrapper<EventPowerExecuteImmediate> executeImmediateLambdaQueryWrapper = new LambdaQueryWrapper<>();
                    executeImmediateLambdaQueryWrapper.eq(EventPowerExecuteImmediate::getDataDate, evaluTaskParam.getRegulateDate());
                    List<EventPowerExecuteImmediate> executeImmediates = eventPowerExecuteImmediateService.list(executeImmediateLambdaQueryWrapper);
                    List<EvaluTask> evaluTaskUpdateList = new ArrayList<>();
                    //List<Event> eventUpdateList = new ArrayList<>();
                    List<EvaluationImmediate> evaluationImmediateUpdateList = new ArrayList<>();
                    List<EvaluationImmediate> evaluationImmediateInsertList = new ArrayList<>();
                    List<EventPowerExecuteImmediate> executeImmediateUpdateList = new ArrayList<>();
                    List<EventPowerExecuteImmediate> executeImmediateInertList = new ArrayList<>();
                    for (EvaluTask evaluTask : evaluTasks) {
                        List<ConsInvitation> eventInvitations = consInvitations.stream().filter(consInvitation ->
                                consInvitation.getEventId().equals(evaluTask.getEventId()) && consInvitation.getConsId().equals(evaluTask.getConsId())
                        ).collect(Collectors.toList());
                        if (null == eventInvitations || eventInvitations.size() == 0) {
                            evaluTask.setEvaluTodayDesc("无用户邀约信息");
                            evaluTask.setEvaluTodayStatus("3");
                            evaluTaskUpdateList.add(evaluTask);
                            continue;
                        } else {
                            eventInvitation = eventInvitations.get(0);
                            if (null == eventInvitation) {
                                evaluTask.setEvaluTodayDesc("无用户邀约信息");
                                evaluTask.setEvaluTodayStatus("3");
                                evaluTaskUpdateList.add(evaluTask);
                                continue;
                            }
                        }
                        List<EvaluationImmediate> evaluationImmediates = null;
                        if (null != evaluTask.getConsId() && null != evaluationImmediateList && evaluationImmediateList.size() > 0) {
                            evaluationImmediates = evaluationImmediateList.stream().filter(immediate -> immediate.getConsId().equals(evaluTask.getConsId())
                                    && immediate.getEventId().equals(evaluTask.getEventId())
                            ).collect(Collectors.toList());
                        }
                        if (null == evaluationImmediates || evaluationImmediates.size() == 0) {
                            //定义响应效果评估
                            evaluationImmediate = new EvaluationImmediate();
                            evaluationImmediate.setEventId(evaluTask.getEventId());
                            evaluationImmediate.setConsId(evaluTask.getConsId());
                            evaluationImmediate.setJoinUserType(eventInvitation.getJoinUserType());
                        } else {
                            evaluationImmediate = evaluationImmediates.get(0);
                        }
                        evaluationImmediate.setInvitationCap(eventInvitation.getInvitationCap());
                        evaluationImmediate.setReplyCap(eventInvitation.getReplyCap());
                        //获取需求响应方案
                        List<Plan> plans = planList.stream().filter(plan1 -> plan1.getRegulateId().equals(eventId))
                                .collect(Collectors.toList());
                        if (null != plans && plans.size() > 0) {
                            plan = plans.get(0);
                        }
                        if (null != plan && null != plan.getBaselinId()) {
                            //获取事件当日用户基线
                            consBaselines = consBaselineList.stream().filter(consBaseline1 -> consBaseline1.getConsId().equals(evaluTask.getConsId())
                                    && consBaseline1.getBaselineLibId().equals(plans.get(0).getBaselinId())
                            ).collect(Collectors.toList());
                            if (null != consBaselines && consBaselines.size() > 0) {
                                consBaseline = consBaselines.get(0);
                            } else {
                                //基线为空，直接有效
                                evaluationImmediate.setIsEffective(YesOrNotEnum.Y.getCode());
                                evaluationImmediate.setEffectiveTime(hour);
                                evaluationImmediate.setActualCap(eventInvitation.getReplyCap());
                                evaluationImmediate.setConfirmCap(eventInvitation.getReplyCap());
                                evaluationImmediate.setRemark("基线为空,直接设置为有效");
                                if (null == evaluationImmediates || evaluationImmediates.size() == 0) {
                                    //生成id
                                    long id = IdWorker.getId();
                                    evaluationImmediate.setEvaluationId(id);
                                    evaluationImmediateInsertList.add(evaluationImmediate);
                                } else {
                                    evaluationImmediateUpdateList.add(evaluationImmediate);
                                }
                                evaluTask.setEvaluTodayDesc("基线为空,直接设置为有效");
                                evaluTask.setEvaluTodayStatus("2");
                                evaluTask.setEvaluTodayId(evaluationImmediate.getEvaluationId());
                                evaluTaskUpdateList.add(evaluTask);
                                continue;
                            }
                        } else {
                            evaluTask.setEvaluTodayDesc("需求响应方案不存在");
                            evaluTask.setEvaluTodayStatus("3");
                            evaluTaskUpdateList.add(evaluTask);
                            continue;
                        }
                        evaluationImmediate.setAvgLoadBaseline(consBaseline.getAvgLoadBaseline());
                        evaluationImmediate.setMaxLoadBaseline(consBaseline.getMaxLoadBaseline());
                        evaluationImmediate.setMinLoadBaseline(consBaseline.getMinLoadBaseline());
                        ConsCurve consCurve = null;
                        if (localDate.compareTo(evaluTask.getRegulateDate()) == 0) {
                            if (null != consCurveTodays && consCurveTodays.size() > 0) {
                                List<ConsCurveToday> curveTodays = consCurveTodays.stream().filter(consCurveToday -> consCurveToday.getConsId().equals(evaluTask.getConsId()))
                                        .collect(Collectors.toList());
                                if (null != curveTodays && curveTodays.size() > 0) {
                                    consCurve = new ConsCurve();
                                    BeanUtils.copyProperties(curveTodays.get(0), consCurve);
                                }
                            }
                        } else {
                            if (null != consCurveHistorys && consCurveHistorys.size() > 0) {
                                List<ConsCurve> consCurves = consCurveHistorys.stream().filter(conslist -> conslist.getConsId().equals(evaluTask.getConsId())
                                ).collect(Collectors.toList());
                                if (null != consCurves && consCurves.size() > 0) {
                                    consCurve = consCurves.get(0);
                                }
                            }
                        }
                        LocalDateTime futureFif = LocalDateTime.of(event.getRegulateDate(), LocalTime.of(endHour, endMinute)).plusMinutes(45);
                        if (strategyUtils.judgeIsAllNull(consCurve, startP, endP)) {
                            // 判断曲线是否为空
                            if (localTime.isAfter(futureFif)) {
                                // 如果事件已经结束了超过45分钟，直接设置为有效
                                evaluationImmediate.setIsEffective(YesOrNotEnum.Y.getCode());
                                evaluationImmediate.setEffectiveTime(hour);
                                evaluationImmediate.setActualCap(eventInvitation.getReplyCap());
                                evaluationImmediate.setConfirmCap(eventInvitation.getReplyCap());
                                evaluationImmediate.setRemark("事件结束45分钟，仍无今日实时荷,直接设置为有效");
                                if (null == evaluationImmediates || evaluationImmediates.size() == 0) {
                                    long id = IdWorker.getId();
                                    evaluationImmediate.setEvaluationId(id);
                                    evaluationImmediateInsertList.add(evaluationImmediate);
                                } else {
                                    evaluationImmediateUpdateList.add(evaluationImmediate);
                                }
                                evaluTask.setEvaluTodayDesc("事件结束45分钟，仍无今日实时荷,直接设置为有效");
                                evaluTask.setEvaluTodayStatus("2");
                                evaluTask.setEvaluTodayId(evaluationImmediate.getEvaluationId());
                                evaluTaskUpdateList.add(evaluTask);
                                continue;
                            }
                            //判断事件是否到了夜里23:49
                            LocalTime futureFif2 = LocalTime.of(23, 49);
                            // 判断曲线是否为空
                            if (localTime2.isAfter(futureFif2)) {
                                // 如果9点45分无历史负荷，直接设置为有效
                                evaluationImmediate.setIsEffective(YesOrNotEnum.Y.getCode());
                                evaluationImmediate.setEffectiveTime(hour);
                                evaluationImmediate.setActualCap(eventInvitation.getReplyCap());
                                evaluationImmediate.setConfirmCap(eventInvitation.getReplyCap());
                                evaluationImmediate.setRemark("23点45分钟无响应负荷直接设置为有效");
                                if (null == evaluationImmediates || evaluationImmediates.size() == 0) {
                                    long id = IdWorker.getId();
                                    evaluationImmediate.setEvaluationId(id);
                                    evaluationImmediateInsertList.add(evaluationImmediate);
                                } else {
                                    evaluationImmediateUpdateList.add(evaluationImmediate);
                                }
                                evaluTask.setEvaluTodayDesc("23点45分钟无今日实时负荷,直接设置为有效");
                                evaluTask.setEvaluTodayStatus("2");
                                evaluTask.setEvaluTodayId(evaluationImmediate.getEvaluationId());
                                evaluTaskUpdateList.add(evaluTask);
                                continue;
                            }
                            evaluTask.setEvaluTodayDesc("用户负荷曲线不存在");
                            evaluTask.setEvaluTodayStatus("3");
                            evaluTask.setEvaluTodayId(evaluationImmediate.getEvaluationId());
                            evaluTaskUpdateList.add(evaluTask);
                            continue;
                        } else {
                            EventPowerExecuteImmediate eventPowerExecuteImmediate = new EventPowerExecuteImmediate();
                            BeanUtils.copyProperties(consCurve, eventPowerExecuteImmediate);
                            eventPowerExecuteImmediate.setEventId(evaluTask.getEventId());
                            eventPowerExecuteImmediate.setConsId(eventInvitation.getConsId());
                            eventPowerExecuteImmediate.setDataDate(event.getRegulateDate());
                            eventPowerExecuteImmediate.setDataPointFlag("1");
                            if (executeImmediates != null && executeImmediates.size() > 0) {
                                List<EventPowerExecuteImmediate> executeImmediates1 = executeImmediates.stream().filter(immediate -> immediate.getConsId().equals(evaluTask.getConsId())
                                        && immediate.getEventId().equals(evaluTask.getEventId())).collect(Collectors.toList());
                                if (null != executeImmediates1 && executeImmediates1.size() > 0) {
                                    eventPowerExecuteImmediate.setDataId(executeImmediates1.get(0).getDataId());
                                    executeImmediateUpdateList.add(eventPowerExecuteImmediate);
                                } else {
                                    executeImmediateInertList.add(eventPowerExecuteImmediate);
                                }
                            } else {
                                executeImmediateInertList.add(eventPowerExecuteImmediate);
                            }
                        }

                        if (localTime.isBefore(futureFif)) {
                            // 事件结束45分钟以内
                            BigDecimal endValue = null;
                            try {
                                endValue = (BigDecimal) consMethodMap2.get(endP).invoke(consCurve);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (ObjectUtil.isNull(endValue) || BigDecimal.ZERO.compareTo(endValue) == 0) {
                                // 事件最后一个点为0，直接设置为无效
                                if (null == evaluationImmediates || evaluationImmediates.size() == 0) {
                                    long id = IdWorker.getId();
                                    evaluationImmediate.setEvaluationId(id);
                                    evaluationImmediateInsertList.add(evaluationImmediate);
                                } else {
                                    evaluationImmediateUpdateList.add(evaluationImmediate);
                                }
                                evaluTask.setEvaluTodayDesc("实际负荷最后点为0值");
                                evaluTask.setEvaluTodayStatus("3");
                                evaluTask.setEvaluTodayId(evaluationImmediate.getEvaluationId());
                                evaluTaskUpdateList.add(evaluTask);
                                continue;
                            }
                        }
                        List<BigDecimal> actualList = new ArrayList<>();
                        List<BigDecimal> forecastList = new ArrayList<>();
                        for (int i = startP; i <= endP; i++) {
                            BigDecimal power = null;
                            BigDecimal power2 = null;
                            try {
                                power = (BigDecimal) consMethodMap2.get(i).invoke(consCurve);
                                power2 = (BigDecimal) consMethodMap.get(i).invoke(consBaseline);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (ObjectUtil.isNotNull(power)) {

                                actualList.add(power);
                            } else {
                                actualList.add(BigDecimal.ZERO);
                            }
                            if (ObjectUtil.isNotNull(power2)) {
                                forecastList.add(power2);
                            } else {
                                forecastList.add(BigDecimal.ZERO);
                            }
                        }

                        if (CollectionUtil.hasNull(actualList) || CollectionUtil.hasNull(forecastList)) {
                            evaluTask.setEvaluTodayDesc("实际负荷或者基线负荷不完整");
                            evaluTask.setEvaluTodayStatus("3");
                            evaluTask.setEvaluTodayId(evaluationImmediate.getEvaluationId());
                            evaluTaskUpdateList.add(evaluTask);
                            continue;
                        }

                        BigDecimal sumActual = actualList.stream().reduce(BigDecimal.ZERO, (d1, d2) -> {
                            return Optional.ofNullable(d1).orElse(BigDecimal.ZERO).add(Optional.ofNullable(d2).orElse(BigDecimal.ZERO));
                        });
                        BigDecimal minActual = actualList.stream().min(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
                        BigDecimal maxActual = actualList.stream().max(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
                        evaluationImmediate.setAvgLoadActual(NumberUtil.div(sumActual, actualList.size(), 2));
                        evaluationImmediate.setMaxLoadActual(maxActual);
                        evaluationImmediate.setMinLoadActual(minActual);
                        BigDecimal actualCap = NumberUtil.sub(evaluationImmediate.getAvgLoadBaseline(), evaluationImmediate.getAvgLoadActual());
                        /*if (BigDecimal.ZERO.compareTo(actualCap) > 0) {
                            actualCap = NumberUtil.mul(actualCap, -1);
                        }*/
                        if("2".equals(event.getResponseType())) {
                            actualCap = NumberUtil.mul(actualCap, -1);
                            //evaluationImmediate.setEffectiveTime(strategyUtils.calEeffectiveTime2(actualList, forecastList, eventInvitation.getReplyCap()) * 15);
                            //evaluationImmediate.setEffectiveTime(hour);
                        }
                        evaluationImmediate.setActualCap(actualCap);
                        evaluationImmediate.setEffectiveTime(hour);
                        evaluationImmediate = strategyUtils.judgeEffectiveOfRule(evaluationImmediate, evaluTask, event);
                        //evaluationImmediate.setIsEffective(strategyUtils.judgeEffectiveOfRule(evaluationImmediate, evaluTask, event) ? YesOrNotEnum.Y.getCode() : YesOrNotEnum.N.getCode());
                        //负荷响应量为实际负荷占反馈响应量百分比
                        if(null==eventInvitation.getReplyCap() || eventInvitation.getReplyCap().compareTo(BigDecimal.ZERO)==0) {
                            evaluationImmediate.setExecuteRate(BigDecimal.ZERO);
                        } else {
                            evaluationImmediate.setExecuteRate(NumberUtil.div(actualCap,eventInvitation.getReplyCap()));
                        }
                        //核定响应量
                        if (evaluationImmediate.getIsEffective().equals(YesOrNotEnum.Y.getCode())) {
                            //如果实际响应量大于1.2倍反馈响应量，取1.2倍反馈响应量，否则取实际响应量
                            BigDecimal temp = strategyUtils.getConfirmCap(eventInvitation.getReplyCap(),actualCap);
                            evaluationImmediate.setConfirmCap(temp);
                            evaluTask.setEvaluTodayStatus("2");
                            evaluTask.setEvaluTodayDesc("");
                        } else {
                            //无效响应核定响应量为0
                            evaluationImmediate.setConfirmCap(new BigDecimal("0"));
                            //evaluationImmediate.setRemark("无效响应,核定响应量为0");
                            evaluTask.setEvaluTodayStatus("2");
                            evaluationImmediate.setEffectiveTime(0);
                            evaluTask.setEvaluTodayDesc(evaluationImmediate.getRemark());
                        }
                        if (null == evaluationImmediates || evaluationImmediates.size() == 0) {
                            long id = IdWorker.getId();
                            evaluationImmediate.setEvaluationId(id);
                            evaluationImmediateInsertList.add(evaluationImmediate);
                        } else {
                            evaluationImmediateUpdateList.add(evaluationImmediate);
                        }
                        evaluTask.setEvaluTodayId(evaluationImmediate.getEvaluationId());
                        evaluTaskUpdateList.add(evaluTask);
                    }
                    //更新
                        String url = dataurl;
                        String user = userName;
                        String password = datapassword;
                        //2）、加载驱动，不需要显示注册驱动
                        Class.forName(driver);
                        //更新同步邀约信息状态
                        String sql = "UPDATE dr_evalu_task set EVALU_TODAY_STATUS=?,EVALU_TODAY_ID=?,EVALU_TODAY_DESC=?,UPDATE_TIME=? where id=?";
                        if (null != evaluTaskUpdateList && evaluTaskUpdateList.size() > 0) {
                            //获取数据库连接
                            conn = DriverManager.getConnection(url, user, password);
                            conn.setAutoCommit(false);
                            preparedStatement = conn.prepareStatement(sql);
                            int i = 0;
                            for (EvaluTask evaluTask : evaluTaskUpdateList) {
                                if(null!=evaluTask.getEvaluTodayStatus()) {
                                    preparedStatement.setString(1, evaluTask.getEvaluTodayStatus());
                                } else {
                                    preparedStatement.setNull(1,Types.VARCHAR);
                                }
                                if(null!=evaluTask.getEvaluTodayId()) {
                                    preparedStatement.setLong(2, evaluTask.getEvaluTodayId());
                                } else {
                                    preparedStatement.setNull(2,Types.BIGINT);
                                }
                                if(null!=evaluTask.getEvaluTodayDesc()) {
                                    preparedStatement.setString(3, evaluTask.getEvaluTodayDesc());
                                } else {
                                    preparedStatement.setNull(3,Types.VARCHAR);
                                }
                                preparedStatement.setString(4, dateFormat.format(new Date()));
                                preparedStatement.setLong(5,evaluTask.getId());
                                preparedStatement.addBatch();
                                if ((i + 1) % 1000 == 0 || i == evaluTaskUpdateList.size() - 1) {
                                    //每1000条提交一次
                                    preparedStatement.executeBatch();
                                    //清空记录
                                    preparedStatement.clearBatch();
                                }
                                i++;
                            }
                        }
                        log.info("更新今日效果评估任务表完成，共:" + evaluTaskUpdateList.size() + "条");
                        String sql2 = "INSERT INTO dr_cons_evaluation_immediate (evaluation_id,cons_id,event_id,invitation_cap,reply_cap,actual_cap,confirm_cap,max_load_baseline,min_load_baseline,avg_load_baseline,max_load_actual,min_load_actual,avg_load_actual,is_effective,effective_time,exception_remark,create_time,join_user_type,remark,execute_rate)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                        if (null !=evaluationImmediateInsertList  && evaluationImmediateInsertList.size() > 0) {
                            //获取数据库连接
                            conn2 = DriverManager.getConnection(url, user, password);
                            conn2.setAutoCommit(false);
                            preparedStatement2 = conn2.prepareStatement(sql2);
                            int i = 0;
                            for (EvaluationImmediate evaluationImmediate1 : evaluationImmediateInsertList) {
                                preparedStatement2.setLong(1, evaluationImmediate1.getEvaluationId());
                                preparedStatement2.setString(2, evaluationImmediate1.getConsId());
                                preparedStatement2.setLong(3, evaluationImmediate1.getEventId());
                                preparedStatement2.setBigDecimal(4, evaluationImmediate1.getInvitationCap());
                                preparedStatement2.setBigDecimal(5, evaluationImmediate1.getReplyCap());
                                preparedStatement2.setBigDecimal(6, evaluationImmediate1.getActualCap());
                                preparedStatement2.setBigDecimal(7, evaluationImmediate1.getConfirmCap());
                                preparedStatement2.setBigDecimal(8, evaluationImmediate1.getMaxLoadBaseline());
                                preparedStatement2.setBigDecimal(9, evaluationImmediate1.getMinLoadBaseline());
                                preparedStatement2.setBigDecimal(10, evaluationImmediate1.getAvgLoadBaseline());
                                preparedStatement2.setBigDecimal(11, evaluationImmediate1.getMaxLoadActual());
                                preparedStatement2.setBigDecimal(12, evaluationImmediate1.getMinLoadActual());
                                preparedStatement2.setBigDecimal(13, evaluationImmediate1.getAvgLoadActual());
                                preparedStatement2.setString(14, evaluationImmediate1.getIsEffective());
                                if(null!=evaluationImmediate1.getEffectiveTime()) {
                                    preparedStatement2.setInt(15, evaluationImmediate1.getEffectiveTime());
                                } else {
                                    preparedStatement2.setNull(15,Types.INTEGER);
                                }
                                if(null!=evaluationImmediate1.getExceptionRemark()) {
                                    preparedStatement2.setString(16, evaluationImmediate1.getExceptionRemark());
                                } else {
                                    preparedStatement2.setNull(16, Types.VARCHAR);
                                }
                                preparedStatement2.setString(17, dateFormat.format(new Date()));
                                if(null!=evaluationImmediate1.getJoinUserType()) {
                                    preparedStatement2.setString(18, evaluationImmediate1.getJoinUserType());
                                } else {
                                    preparedStatement2.setNull(18,Types.VARCHAR);
                                }
                                preparedStatement2.setString(19,evaluationImmediate1.getRemark());
                                preparedStatement2.setBigDecimal(20,evaluationImmediate1.getExecuteRate());
                                preparedStatement2.addBatch();
                                if ((i + 1) % 1000 == 0 || i == evaluationImmediateInsertList.size() - 1) {
                                    //每1000条提交一次
                                    preparedStatement2.executeBatch();
                                    //清空记录
                                    preparedStatement2.clearBatch();
                                }
                                i++;
                            }

                        }
                        log.info("保存今日效果评估数据完成，共:" + evaluationImmediateInsertList.size() + "条");
                        String sql3 = "update dr_cons_evaluation_immediate set cons_id=?, event_id=?, invitation_cap=?, reply_cap=?, actual_cap=?, confirm_cap=?, max_load_baseline=?, min_load_baseline=?, avg_load_baseline=?, max_load_actual=?, min_load_actual=?, avg_load_actual=?, is_effective=?, effective_time=?, exception_remark=?, update_time=?,join_user_type=?,remark=?,execute_rate=? where evaluation_id=?";
                        if (null !=evaluationImmediateUpdateList  && evaluationImmediateUpdateList.size() > 0) {
                            //获取数据库连接
                            conn3 = DriverManager.getConnection(url, user, password);
                            conn3.setAutoCommit(false);
                            preparedStatement3 = conn3.prepareStatement(sql3);
                            int i = 0;
                            for (EvaluationImmediate evaluationImmediate1 : evaluationImmediateUpdateList) {
                                preparedStatement3.setString(1, evaluationImmediate1.getConsId());
                                preparedStatement3.setLong(2, evaluationImmediate1.getEventId());
                                preparedStatement3.setBigDecimal(3, evaluationImmediate1.getInvitationCap());
                                preparedStatement3.setBigDecimal(4, evaluationImmediate1.getReplyCap());
                                preparedStatement3.setBigDecimal(5, evaluationImmediate1.getActualCap());
                                preparedStatement3.setBigDecimal(6, evaluationImmediate1.getConfirmCap());
                                preparedStatement3.setBigDecimal(7, evaluationImmediate1.getMaxLoadBaseline());
                                preparedStatement3.setBigDecimal(8, evaluationImmediate1.getMinLoadBaseline());
                                preparedStatement3.setBigDecimal(9, evaluationImmediate1.getAvgLoadBaseline());
                                preparedStatement3.setBigDecimal(10, evaluationImmediate1.getMaxLoadActual());
                                preparedStatement3.setBigDecimal(11, evaluationImmediate1.getMinLoadActual());
                                preparedStatement3.setBigDecimal(12, evaluationImmediate1.getAvgLoadActual());
                                if(null!=evaluationImmediate1.getIsEffective()) {
                                    preparedStatement3.setString(13, evaluationImmediate1.getIsEffective());
                                } else {
                                    preparedStatement3.setNull(13,Types.VARCHAR);
                                }
                                if(null!=evaluationImmediate1.getEffectiveTime()) {
                                    preparedStatement3.setInt(14, evaluationImmediate1.getEffectiveTime());
                                } else {
                                    preparedStatement3.setNull(14,Types.INTEGER);
                                }
                                if(null!=evaluationImmediate1.getExceptionRemark()) {
                                    preparedStatement3.setString(15, evaluationImmediate1.getExceptionRemark());
                                } else {
                                    preparedStatement3.setNull(15, Types.VARCHAR);
                                }
                                preparedStatement3.setString(16, dateFormat.format(new Date()));
                                if(null!=evaluationImmediate1.getJoinUserType()) {
                                    preparedStatement3.setString(17, evaluationImmediate1.getJoinUserType());
                                } else {
                                    preparedStatement3.setNull(17, Types.VARCHAR);
                                }
                                preparedStatement3.setString(18,evaluationImmediate1.getRemark());
                                preparedStatement3.setBigDecimal(19,evaluationImmediate1.getExecuteRate());
                                preparedStatement3.setLong(20,evaluationImmediate1.getEvaluationId());
                                preparedStatement3.addBatch();
                                if ((i + 1) % 1000 == 0 || i == evaluationImmediateUpdateList.size() - 1) {
                                    //每1000条提交一次
                                    preparedStatement3.executeBatch();
                                    //清空记录
                                    preparedStatement3.clearBatch();
                                }
                                i++;
                            }

                        }
                        log.info("更新今日效果评估数据完成，共:" + evaluationImmediateUpdateList.size() + "条");
                        String sql4 = "INSERT INTO dr_event_power_execute_immediate (\n" +
                                "cons_id,data_date,data_point_flag,p1,p2,p3, p4,p5,p6,p7,p8,p9,p10,p11,p12,p13,p14,p15,p16,p17,\n" +
                                "p18,p19,p20,p21,p22,p23,p24,p25,p26,p27,p28,p29,p30,p31,p32,p33,p34,p35,p36,p37,p38,p39,p40,p41,p42,p43,\n" +
                                "p44,p45,p46,p47,p48,p49,p50,p51,p52,p53,p54,p55,p56,p57,p58,p59,p60,p61,p62,p63,p64,p65,p66,p67,p68,p69,\n" +
                                "p70, p71, p72,p73,p74,p75,p76,p77,p78,p79,p80,p81,p82,p83,p84,p85,p86,p87,p88,p89,p90, p91,p92,p93,p94,p95,p96,event_id\n" +
                                ")VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                        if (null != executeImmediateInertList && executeImmediateInertList.size() > 0) {
                            int k = 0;
                            //2）、加载驱动，不需要显示注册驱动
                            Class.forName(driver);
                            //获取数据库连接
                            conn4 = DriverManager.getConnection(url, user, password);
                            conn4.setAutoCommit(false);
                            preparedStatement4 = conn4.prepareStatement(sql4);
                            for (EventPowerExecuteImmediate eventPowerExecuteImmediate : executeImmediateInertList) {
                                preparedStatement4.setString(1, eventPowerExecuteImmediate.getConsId());
                                if(null!=eventPowerExecuteImmediate.getDataDate()) {
                                    preparedStatement4.setString(2, simpleDateFormat.format(eventPowerExecuteImmediate.getDataDate()));
                                } else {
                                    preparedStatement4.setNull(2,Types.VARCHAR);
                                }
                                if(null!=eventPowerExecuteImmediate.getDataPointFlag()) {
                                    preparedStatement4.setString(3, eventPowerExecuteImmediate.getDataPointFlag());
                                } else {
                                    preparedStatement4.setNull(3,Types.VARCHAR);
                                }
                                preparedStatement4.setBigDecimal(4, eventPowerExecuteImmediate.getP1());
                                preparedStatement4.setBigDecimal(5, eventPowerExecuteImmediate.getP2());
                                preparedStatement4.setBigDecimal(6, eventPowerExecuteImmediate.getP3());
                                preparedStatement4.setBigDecimal(7, eventPowerExecuteImmediate.getP4());
                                preparedStatement4.setBigDecimal(8, eventPowerExecuteImmediate.getP5());
                                preparedStatement4.setBigDecimal(9, eventPowerExecuteImmediate.getP6());
                                preparedStatement4.setBigDecimal(10, eventPowerExecuteImmediate.getP7());
                                preparedStatement4.setBigDecimal(11, eventPowerExecuteImmediate.getP8());
                                preparedStatement4.setBigDecimal(12, eventPowerExecuteImmediate.getP9());
                                preparedStatement4.setBigDecimal(13, eventPowerExecuteImmediate.getP10());
                                preparedStatement4.setBigDecimal(14, eventPowerExecuteImmediate.getP11());
                                preparedStatement4.setBigDecimal(15, eventPowerExecuteImmediate.getP12());
                                preparedStatement4.setBigDecimal(16, eventPowerExecuteImmediate.getP13());
                                preparedStatement4.setBigDecimal(17, eventPowerExecuteImmediate.getP14());
                                preparedStatement4.setBigDecimal(18, eventPowerExecuteImmediate.getP15());
                                preparedStatement4.setBigDecimal(19, eventPowerExecuteImmediate.getP16());
                                preparedStatement4.setBigDecimal(20, eventPowerExecuteImmediate.getP17());
                                preparedStatement4.setBigDecimal(21, eventPowerExecuteImmediate.getP18());
                                preparedStatement4.setBigDecimal(22, eventPowerExecuteImmediate.getP19());
                                preparedStatement4.setBigDecimal(23, eventPowerExecuteImmediate.getP20());
                                preparedStatement4.setBigDecimal(24, eventPowerExecuteImmediate.getP21());
                                preparedStatement4.setBigDecimal(25, eventPowerExecuteImmediate.getP22());
                                preparedStatement4.setBigDecimal(26, eventPowerExecuteImmediate.getP23());
                                preparedStatement4.setBigDecimal(27, eventPowerExecuteImmediate.getP24());
                                preparedStatement4.setBigDecimal(28, eventPowerExecuteImmediate.getP25());
                                preparedStatement4.setBigDecimal(29, eventPowerExecuteImmediate.getP26());
                                preparedStatement4.setBigDecimal(30, eventPowerExecuteImmediate.getP27());
                                preparedStatement4.setBigDecimal(31, eventPowerExecuteImmediate.getP28());
                                preparedStatement4.setBigDecimal(32, eventPowerExecuteImmediate.getP29());
                                preparedStatement4.setBigDecimal(33, eventPowerExecuteImmediate.getP30());
                                preparedStatement4.setBigDecimal(34, eventPowerExecuteImmediate.getP31());
                                preparedStatement4.setBigDecimal(35, eventPowerExecuteImmediate.getP32());
                                preparedStatement4.setBigDecimal(36, eventPowerExecuteImmediate.getP33());
                                preparedStatement4.setBigDecimal(37, eventPowerExecuteImmediate.getP34());
                                preparedStatement4.setBigDecimal(38, eventPowerExecuteImmediate.getP35());
                                preparedStatement4.setBigDecimal(39, eventPowerExecuteImmediate.getP36());
                                preparedStatement4.setBigDecimal(40, eventPowerExecuteImmediate.getP37());
                                preparedStatement4.setBigDecimal(41, eventPowerExecuteImmediate.getP38());
                                preparedStatement4.setBigDecimal(42, eventPowerExecuteImmediate.getP39());
                                preparedStatement4.setBigDecimal(43, eventPowerExecuteImmediate.getP40());
                                preparedStatement4.setBigDecimal(44, eventPowerExecuteImmediate.getP41());
                                preparedStatement4.setBigDecimal(45, eventPowerExecuteImmediate.getP42());
                                preparedStatement4.setBigDecimal(46, eventPowerExecuteImmediate.getP43());
                                preparedStatement4.setBigDecimal(47, eventPowerExecuteImmediate.getP44());
                                preparedStatement4.setBigDecimal(48, eventPowerExecuteImmediate.getP45());
                                preparedStatement4.setBigDecimal(49, eventPowerExecuteImmediate.getP46());
                                preparedStatement4.setBigDecimal(50, eventPowerExecuteImmediate.getP47());
                                preparedStatement4.setBigDecimal(51, eventPowerExecuteImmediate.getP48());
                                preparedStatement4.setBigDecimal(52, eventPowerExecuteImmediate.getP49());
                                preparedStatement4.setBigDecimal(53, eventPowerExecuteImmediate.getP50());
                                preparedStatement4.setBigDecimal(54, eventPowerExecuteImmediate.getP51());
                                preparedStatement4.setBigDecimal(55, eventPowerExecuteImmediate.getP52());
                                preparedStatement4.setBigDecimal(56, eventPowerExecuteImmediate.getP53());
                                preparedStatement4.setBigDecimal(57, eventPowerExecuteImmediate.getP54());
                                preparedStatement4.setBigDecimal(58, eventPowerExecuteImmediate.getP55());
                                preparedStatement4.setBigDecimal(59, eventPowerExecuteImmediate.getP56());
                                preparedStatement4.setBigDecimal(60, eventPowerExecuteImmediate.getP57());
                                preparedStatement4.setBigDecimal(61, eventPowerExecuteImmediate.getP58());
                                preparedStatement4.setBigDecimal(62, eventPowerExecuteImmediate.getP59());
                                preparedStatement4.setBigDecimal(63, eventPowerExecuteImmediate.getP60());
                                preparedStatement4.setBigDecimal(64, eventPowerExecuteImmediate.getP61());
                                preparedStatement4.setBigDecimal(65, eventPowerExecuteImmediate.getP62());
                                preparedStatement4.setBigDecimal(66, eventPowerExecuteImmediate.getP63());
                                preparedStatement4.setBigDecimal(67, eventPowerExecuteImmediate.getP64());
                                preparedStatement4.setBigDecimal(68, eventPowerExecuteImmediate.getP65());
                                preparedStatement4.setBigDecimal(69, eventPowerExecuteImmediate.getP66());
                                preparedStatement4.setBigDecimal(70, eventPowerExecuteImmediate.getP67());
                                preparedStatement4.setBigDecimal(71, eventPowerExecuteImmediate.getP68());
                                preparedStatement4.setBigDecimal(72, eventPowerExecuteImmediate.getP69());
                                preparedStatement4.setBigDecimal(73, eventPowerExecuteImmediate.getP70());
                                preparedStatement4.setBigDecimal(74, eventPowerExecuteImmediate.getP71());
                                preparedStatement4.setBigDecimal(75, eventPowerExecuteImmediate.getP72());
                                preparedStatement4.setBigDecimal(76, eventPowerExecuteImmediate.getP73());
                                preparedStatement4.setBigDecimal(77, eventPowerExecuteImmediate.getP74());
                                preparedStatement4.setBigDecimal(78, eventPowerExecuteImmediate.getP75());
                                preparedStatement4.setBigDecimal(79, eventPowerExecuteImmediate.getP76());
                                preparedStatement4.setBigDecimal(80, eventPowerExecuteImmediate.getP77());
                                preparedStatement4.setBigDecimal(81, eventPowerExecuteImmediate.getP78());
                                preparedStatement4.setBigDecimal(82, eventPowerExecuteImmediate.getP79());
                                preparedStatement4.setBigDecimal(83, eventPowerExecuteImmediate.getP80());
                                preparedStatement4.setBigDecimal(84, eventPowerExecuteImmediate.getP81());
                                preparedStatement4.setBigDecimal(85, eventPowerExecuteImmediate.getP82());
                                preparedStatement4.setBigDecimal(86, eventPowerExecuteImmediate.getP83());
                                preparedStatement4.setBigDecimal(87, eventPowerExecuteImmediate.getP84());
                                preparedStatement4.setBigDecimal(88, eventPowerExecuteImmediate.getP85());
                                preparedStatement4.setBigDecimal(89, eventPowerExecuteImmediate.getP86());
                                preparedStatement4.setBigDecimal(90, eventPowerExecuteImmediate.getP87());
                                preparedStatement4.setBigDecimal(91, eventPowerExecuteImmediate.getP88());
                                preparedStatement4.setBigDecimal(92, eventPowerExecuteImmediate.getP89());
                                preparedStatement4.setBigDecimal(93, eventPowerExecuteImmediate.getP90());
                                preparedStatement4.setBigDecimal(94, eventPowerExecuteImmediate.getP91());
                                preparedStatement4.setBigDecimal(95, eventPowerExecuteImmediate.getP92());
                                preparedStatement4.setBigDecimal(96, eventPowerExecuteImmediate.getP93());
                                preparedStatement4.setBigDecimal(97, eventPowerExecuteImmediate.getP94());
                                preparedStatement4.setBigDecimal(98, eventPowerExecuteImmediate.getP95());
                                preparedStatement4.setBigDecimal(99, eventPowerExecuteImmediate.getP96());
                                preparedStatement4.setLong(100, eventPowerExecuteImmediate.getEventId());
                                preparedStatement4.addBatch();
                                if ((k + 1) % 500 == 0 || k == executeImmediateInertList.size() - 1) {
                                    //每1000条提交一次
                                    preparedStatement4.executeBatch();
                                    //清空记录
                                    preparedStatement4.clearBatch();
                                }
                                k++;
                            }
                        }
                        log.info("保存用户执行曲线成功,共" + executeImmediateInertList.size() + "条");
                        String sql5 ="UPDATE dr_event_power_execute_immediate \n" +
                                "SET cons_id =?,data_date =?,data_point_flag =?,p1 =?,p2 =?,p3 =?,p4 =?,p5 =?,p6 =?,p7 =?,p8 =?,p9 =?,p10 =?,p11 =?,p12 =?,p13 =?,p14 =?,p15 =?,p16 =?,p17 =?,p18 =?,p19 =?,p20 =?,p21 =?,p22 =?,p23 =?,p24 =?,p25 =?,p26 =?,p27 =?,p28 =?,p29 =?,p30 =?,p31 =?,p32 =?,p33 =?,p34 =?,p35 =?,p36 =?,p37 =?,p38 =?,p39 =?,p40 =?,p41 =?,p42 =?,p43 =?,p44 =?,p45 =?,p46 =?,p47 =?,p48 =?,p49 =?,p50 =?,p51 =?,p52 =?,p53 =?,p54 =?,p55 =?,p56 =?,p57 =?,p58 =?,p59 =?,p60 =?,p61 =?,p62 =?,p63 =?,p64 =?,p65 =?,p66 =?,p67 =?,p68 =?,p69 =?,p70 =?,p71 =?,p72 =?,p73 =?,p74 =?,p75 =?,p76 =?,p77 =?,p78 =?,p79 =?,p80 =?,p81 =?,p82 =?,p83 =?,p84 =?,p85 =?,p86 =?,p87 =?,p88 =?,p89 =?,p90 =?,p91 =?,p92 =?,p93 =?,p94 =?,p95 =?,p96 =? \n" +
                                "WHERE\n" +
                                "\tdata_id =?";
                            //保存样本曲线
                            if(null!=executeImmediateUpdateList && executeImmediateUpdateList.size()>0) {
                                int j=0;
                                //2）、加载驱动，不需要显示注册驱动
                                Class.forName(driver);
                                //获取数据库连接
                                conn5= DriverManager.getConnection(url,user,password);
                                conn5.setAutoCommit(false);
                                preparedStatement5 = conn5.prepareStatement(sql5);
                                for(EventPowerExecuteImmediate eventPowerExecuteImmediate : executeImmediateUpdateList) {
                                    preparedStatement5.setString(1, eventPowerExecuteImmediate.getConsId());
                                    preparedStatement5.setString(2, simpleDateFormat.format(eventPowerExecuteImmediate.getDataDate()));
                                    if(null!=eventPowerExecuteImmediate.getDataPointFlag()) {
                                        preparedStatement5.setString(3, eventPowerExecuteImmediate.getDataPointFlag());
                                    } else {
                                        preparedStatement5.setNull(3,Types.VARCHAR);
                                    }
                                    preparedStatement5.setBigDecimal(4, eventPowerExecuteImmediate.getP1());
                                    preparedStatement5.setBigDecimal(5, eventPowerExecuteImmediate.getP2());
                                    preparedStatement5.setBigDecimal(6, eventPowerExecuteImmediate.getP3());
                                    preparedStatement5.setBigDecimal(7, eventPowerExecuteImmediate.getP4());
                                    preparedStatement5.setBigDecimal(8, eventPowerExecuteImmediate.getP5());
                                    preparedStatement5.setBigDecimal(9, eventPowerExecuteImmediate.getP6());
                                    preparedStatement5.setBigDecimal(10, eventPowerExecuteImmediate.getP7());
                                    preparedStatement5.setBigDecimal(11, eventPowerExecuteImmediate.getP8());
                                    preparedStatement5.setBigDecimal(12, eventPowerExecuteImmediate.getP9());
                                    preparedStatement5.setBigDecimal(13, eventPowerExecuteImmediate.getP10());
                                    preparedStatement5.setBigDecimal(14, eventPowerExecuteImmediate.getP11());
                                    preparedStatement5.setBigDecimal(15, eventPowerExecuteImmediate.getP12());
                                    preparedStatement5.setBigDecimal(16, eventPowerExecuteImmediate.getP13());
                                    preparedStatement5.setBigDecimal(17, eventPowerExecuteImmediate.getP14());
                                    preparedStatement5.setBigDecimal(18, eventPowerExecuteImmediate.getP15());
                                    preparedStatement5.setBigDecimal(19, eventPowerExecuteImmediate.getP16());
                                    preparedStatement5.setBigDecimal(20, eventPowerExecuteImmediate.getP17());
                                    preparedStatement5.setBigDecimal(21, eventPowerExecuteImmediate.getP18());
                                    preparedStatement5.setBigDecimal(22, eventPowerExecuteImmediate.getP19());
                                    preparedStatement5.setBigDecimal(23, eventPowerExecuteImmediate.getP20());
                                    preparedStatement5.setBigDecimal(24, eventPowerExecuteImmediate.getP21());
                                    preparedStatement5.setBigDecimal(25, eventPowerExecuteImmediate.getP22());
                                    preparedStatement5.setBigDecimal(26, eventPowerExecuteImmediate.getP23());
                                    preparedStatement5.setBigDecimal(27, eventPowerExecuteImmediate.getP24());
                                    preparedStatement5.setBigDecimal(28, eventPowerExecuteImmediate.getP25());
                                    preparedStatement5.setBigDecimal(29, eventPowerExecuteImmediate.getP26());
                                    preparedStatement5.setBigDecimal(30, eventPowerExecuteImmediate.getP27());
                                    preparedStatement5.setBigDecimal(31, eventPowerExecuteImmediate.getP28());
                                    preparedStatement5.setBigDecimal(32, eventPowerExecuteImmediate.getP29());
                                    preparedStatement5.setBigDecimal(33, eventPowerExecuteImmediate.getP30());
                                    preparedStatement5.setBigDecimal(34, eventPowerExecuteImmediate.getP31());
                                    preparedStatement5.setBigDecimal(35, eventPowerExecuteImmediate.getP32());
                                    preparedStatement5.setBigDecimal(36, eventPowerExecuteImmediate.getP33());
                                    preparedStatement5.setBigDecimal(37, eventPowerExecuteImmediate.getP34());
                                    preparedStatement5.setBigDecimal(38, eventPowerExecuteImmediate.getP35());
                                    preparedStatement5.setBigDecimal(39, eventPowerExecuteImmediate.getP36());
                                    preparedStatement5.setBigDecimal(40, eventPowerExecuteImmediate.getP37());
                                    preparedStatement5.setBigDecimal(41, eventPowerExecuteImmediate.getP38());
                                    preparedStatement5.setBigDecimal(42, eventPowerExecuteImmediate.getP39());
                                    preparedStatement5.setBigDecimal(43, eventPowerExecuteImmediate.getP40());
                                    preparedStatement5.setBigDecimal(44, eventPowerExecuteImmediate.getP41());
                                    preparedStatement5.setBigDecimal(45, eventPowerExecuteImmediate.getP42());
                                    preparedStatement5.setBigDecimal(46, eventPowerExecuteImmediate.getP43());
                                    preparedStatement5.setBigDecimal(47, eventPowerExecuteImmediate.getP44());
                                    preparedStatement5.setBigDecimal(48, eventPowerExecuteImmediate.getP45());
                                    preparedStatement5.setBigDecimal(49, eventPowerExecuteImmediate.getP46());
                                    preparedStatement5.setBigDecimal(50, eventPowerExecuteImmediate.getP47());
                                    preparedStatement5.setBigDecimal(51, eventPowerExecuteImmediate.getP48());
                                    preparedStatement5.setBigDecimal(52, eventPowerExecuteImmediate.getP49());
                                    preparedStatement5.setBigDecimal(53, eventPowerExecuteImmediate.getP50());
                                    preparedStatement5.setBigDecimal(54, eventPowerExecuteImmediate.getP51());
                                    preparedStatement5.setBigDecimal(55, eventPowerExecuteImmediate.getP52());
                                    preparedStatement5.setBigDecimal(56, eventPowerExecuteImmediate.getP53());
                                    preparedStatement5.setBigDecimal(57, eventPowerExecuteImmediate.getP54());
                                    preparedStatement5.setBigDecimal(58, eventPowerExecuteImmediate.getP55());
                                    preparedStatement5.setBigDecimal(59, eventPowerExecuteImmediate.getP56());
                                    preparedStatement5.setBigDecimal(60, eventPowerExecuteImmediate.getP57());
                                    preparedStatement5.setBigDecimal(61, eventPowerExecuteImmediate.getP58());
                                    preparedStatement5.setBigDecimal(62, eventPowerExecuteImmediate.getP59());
                                    preparedStatement5.setBigDecimal(63, eventPowerExecuteImmediate.getP60());
                                    preparedStatement5.setBigDecimal(64, eventPowerExecuteImmediate.getP61());
                                    preparedStatement5.setBigDecimal(65, eventPowerExecuteImmediate.getP62());
                                    preparedStatement5.setBigDecimal(66, eventPowerExecuteImmediate.getP63());
                                    preparedStatement5.setBigDecimal(67, eventPowerExecuteImmediate.getP64());
                                    preparedStatement5.setBigDecimal(68, eventPowerExecuteImmediate.getP65());
                                    preparedStatement5.setBigDecimal(69, eventPowerExecuteImmediate.getP66());
                                    preparedStatement5.setBigDecimal(70, eventPowerExecuteImmediate.getP67());
                                    preparedStatement5.setBigDecimal(71, eventPowerExecuteImmediate.getP68());
                                    preparedStatement5.setBigDecimal(72, eventPowerExecuteImmediate.getP69());
                                    preparedStatement5.setBigDecimal(73, eventPowerExecuteImmediate.getP70());
                                    preparedStatement5.setBigDecimal(74, eventPowerExecuteImmediate.getP71());
                                    preparedStatement5.setBigDecimal(75, eventPowerExecuteImmediate.getP72());
                                    preparedStatement5.setBigDecimal(76, eventPowerExecuteImmediate.getP73());
                                    preparedStatement5.setBigDecimal(77, eventPowerExecuteImmediate.getP74());
                                    preparedStatement5.setBigDecimal(78, eventPowerExecuteImmediate.getP75());
                                    preparedStatement5.setBigDecimal(79, eventPowerExecuteImmediate.getP76());
                                    preparedStatement5.setBigDecimal(80, eventPowerExecuteImmediate.getP77());
                                    preparedStatement5.setBigDecimal(81, eventPowerExecuteImmediate.getP78());
                                    preparedStatement5.setBigDecimal(82, eventPowerExecuteImmediate.getP79());
                                    preparedStatement5.setBigDecimal(83, eventPowerExecuteImmediate.getP80());
                                    preparedStatement5.setBigDecimal(84, eventPowerExecuteImmediate.getP81());
                                    preparedStatement5.setBigDecimal(85, eventPowerExecuteImmediate.getP82());
                                    preparedStatement5.setBigDecimal(86, eventPowerExecuteImmediate.getP83());
                                    preparedStatement5.setBigDecimal(87, eventPowerExecuteImmediate.getP84());
                                    preparedStatement5.setBigDecimal(88, eventPowerExecuteImmediate.getP85());
                                    preparedStatement5.setBigDecimal(89, eventPowerExecuteImmediate.getP86());
                                    preparedStatement5.setBigDecimal(90, eventPowerExecuteImmediate.getP87());
                                    preparedStatement5.setBigDecimal(91, eventPowerExecuteImmediate.getP88());
                                    preparedStatement5.setBigDecimal(92, eventPowerExecuteImmediate.getP89());
                                    preparedStatement5.setBigDecimal(93, eventPowerExecuteImmediate.getP90());
                                    preparedStatement5.setBigDecimal(94, eventPowerExecuteImmediate.getP91());
                                    preparedStatement5.setBigDecimal(95, eventPowerExecuteImmediate.getP92());
                                    preparedStatement5.setBigDecimal(96, eventPowerExecuteImmediate.getP93());
                                    preparedStatement5.setBigDecimal(97, eventPowerExecuteImmediate.getP94());
                                    preparedStatement5.setBigDecimal(98, eventPowerExecuteImmediate.getP95());
                                    preparedStatement5.setBigDecimal(99, eventPowerExecuteImmediate.getP96());
                                    preparedStatement5.setLong(100, eventPowerExecuteImmediate.getDataId());
                                    preparedStatement5.addBatch();
                                    if((j+1)%500 == 0 || j == executeImmediateUpdateList.size()-1) {
                                        //每1000条提交一次
                                        preparedStatement5.executeBatch();
                                        //清空记录
                                        preparedStatement5.clearBatch();
                                    }
                                    j++;
                                }
                            }
                            log.info("更新执行曲线成功,共"+executeImmediateUpdateList.size()+"条");
                        if (null != conn) {
                            conn.commit();
                        }
                        if (null != conn2) {
                            conn2.commit();
                        }
                        if (null != conn3) {
                            conn3.commit();
                        }
                        if (null != conn4) {
                            conn4.commit();
                        }
                        if (null != conn5) {
                            conn5.commit();
                        }
                        log.info("用户今日效果评估计算完成!");
                    } catch (Exception e) {
                        try {
                            if (null != conn) {
                                conn.rollback();
                            }
                            if (null != conn2) {
                                conn2.rollback();
                            }
                            if (null != conn3) {
                                conn3.rollback();
                            }
                            if (null != conn4) {
                                conn4.rollback();
                            }
                            if (null != conn5) {
                                conn5.rollback();
                            }
                        } catch (SQLException sqlException) {
                            sqlException.printStackTrace();
                        }
                        e.printStackTrace();
                } finally {
                    if (!"04".equals(status)) {
                        event.setEventStatus("04");
                    }
                    Integer count = event.getTodayCount();
                    if(null==count) {
                        count = 0;
                    }
                    count = count + 1;
                    event.setTodayCount(count);
                    eventService.updateById(event);
                    if (null != conn) {
                        try {
                            conn.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    if (null != preparedStatement) {
                        try {
                            preparedStatement.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                    }
                    if (null != conn2) {
                        try {
                            conn2.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    if (null != preparedStatement2) {
                        try {
                            preparedStatement2.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                    }
                    if (null != conn3) {
                        try {
                            conn3.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    if (null != preparedStatement3) {
                        try {
                            preparedStatement3.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                    }
                    if (null != conn4) {
                        try {
                            conn4.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    if (null != preparedStatement4) {
                        try {
                            preparedStatement4.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                    }
                    if (null != conn5) {
                        try {
                            conn5.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    if (null != preparedStatement5) {
                        try {
                            preparedStatement5.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        };
        return runnable;
    }

    /**
     * 客户当日事件效果评估
     */
    public void executeCustEvaluationListImmediate(String param) {
        log.info(">>> 客户当日效果评估任务开始");
        // 1.查询当日效果评估任务
        LocalDate localDate = LocalDate.now();
        EvaluCustTaskParam evaluTaskParam = new EvaluCustTaskParam();
        if(null!=param && !"".equals(param)) {
            if(StrategyUtils.isDate(param)) {
                evaluTaskParam.setRegulateDateStr(param);
                evaluTaskParam.setRegulateDate(LocalDate.parse(param));
            } else {
                evaluTaskParam.setRegulateDateStr(simpleDateFormat.format(localDate));
                evaluTaskParam.setRegulateDate(localDate);
            }
        } else {
            evaluTaskParam.setRegulateDateStr(simpleDateFormat.format(localDate));
            evaluTaskParam.setRegulateDate(localDate);
        }
        evaluTaskParam.setEvaluTodayStatus("1");
        List<EvaluCustTask>  evaluTasks = null;
        evaluTasks = evaluCustTaskService.list(evaluTaskParam);
        // 2.循环对事件进行评价
        if (CollectionUtil.isEmpty(evaluTasks)) {
            log.info("无效果评估任务!");
            return;
        }
        //查询事件信息
        LambdaQueryWrapper<Event> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Event::getRegulateDate, evaluTaskParam.getRegulateDate());
        lambdaQueryWrapper.eq(Event::getTimeType, "1");
        List<String> statusList = new ArrayList<>();
        statusList.add("03");
        statusList.add("04");
        lambdaQueryWrapper.in(Event::getEventStatus,statusList);
        List<Event> eventList = eventService.list(lambdaQueryWrapper);
        List<Long> eventIds = new ArrayList<>();
        if(null!=eventList && eventList.size()>0) {
            for(Event event : eventList) {
                eventIds.add(event.getEventId());
            }
        } else {
            log.info("无事件信息!");
            return;
        }
        ExecutorService executor = ExecutorBuilder.create()
                .setCorePoolSize(corePoolSize)
                .setMaxPoolSize(maxPoolSize)
                .setWorkQueue(new LinkedBlockingQueue<>(workQueue))
                .build();
        executor.execute(this.executeCustEvaluationListImmediateNew(evaluTasks,evaluTaskParam,eventIds));
        executor.shutdown();
    }

    public Runnable executeCustEvaluationListImmediateNew(List<EvaluCustTask>  evaluTasks,EvaluCustTaskParam evaluTaskParam,List<Long> eventIds) {
        LocalDate localDate = LocalDate.now();
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalTime localTime2 = LocalTime.now();
        Map<Integer, Method> consMethodMap = new HashMap<>();
        //Map<Integer, Method> consMethodMap2 = new HashMap<>();
        StrategyUtils strategyUtils = new StrategyUtils();
        try{
            for (int j=1; j<=96; j++){
                consMethodMap.put(j, CustBaseLineDetail.class.getMethod("getP"+j));
                //consMethodMap2.put(j, ConsCurve.class.getMethod("getP"+j));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                long eventId = evaluTasks.get(0).getEventId();
                Event event = eventService.getById(eventId);
                if(null==event) {
                    log.info("无事件信息");
                    return;
                }
                PreparedStatement preparedStatement = null;
                Connection conn = null;
                PreparedStatement preparedStatement2 = null;
                Connection conn2 = null;
                PreparedStatement preparedStatement3 = null;
                Connection conn3 = null;
                try {
                    Long baselinId= event.getBaselinId();
                    BaseLine baseLine = baseLineService.getById(baselinId);
                    String sampleDates = null;
                    if(null!=baseLine) {
                        sampleDates = baseLine.getSimplesDate();
                    } else {
                        log.info("基线库为空");
                        return;
                    }
                    //List<String> sampleDateList = Arrays.asList(sampleDates.split(","));
                    //String status = event.getEventStatus();
                    // 判断事件是否已经截至
                    Integer endHour = 0;
                    Integer endMinute = 0;
                    endHour = Integer.parseInt(event.getEndTime().substring(0, 2));
                    endMinute = Integer.parseInt(event.getEndTime().substring(3));
                    LocalTime endTime = LocalTime.of(endHour, endMinute);
                    if (evaluTaskParam.getRegulateDate().compareTo(localDate) == 0) {
                        if (LocalTime.now().isBefore(endTime)) {
                            // 事件未截止，当前事件不用进行评价了
                            log.info(">>> 事件未截止，事件ID:{}", eventId);
                            return;
                        }
                    } else if (evaluTaskParam.getRegulateDate().compareTo(localDate) > 0) {
                        // 事件未截止，当前事件不用进行评价了
                        log.info(">>> 事件未截止，事件ID:{}", eventId);
                        return;
                    }
                    // 事件开始点和事件结束点
                    int startP = CurveUtil.covDateTimeToPoint(event.getStartTime());
                    int endP = CurveUtil.covDateTimeToPoint(event.getEndTime());
                    int hour = (endP-startP)*15;
                    //查询事件参与所有客户
                    List<CustInvitation> custInvitations = custInvitationService.getConsInfoByEvent(eventId);
                    if (null == custInvitations || custInvitations.size() == 0) {
                        log.info("无客户邀约信息");
                        return;
                    }
                    CustInvitation eventInvitation = null;
                    LambdaQueryWrapper<Plan> queryWrapperPlan = new LambdaQueryWrapper<>();
                    queryWrapperPlan.eq(Plan::getRegulateId,eventId);
                    List<Plan> planList = planService.list(queryWrapperPlan);
                    if (null == planList || planList.size() == 0) {
                        log.info("无方案信息");
                        return;
                    }
                    Plan plan = null;
                    //查询存在用户效果评估有效的客户，不包含集成商
                    List<Long> custIds = evaluationImmediateService.getCustIdsByEventId(eventId);
                    Integer counts = evaluationImmediateService.getCountByEventId(eventId);
                    //获取所有用户基线
                    //List<CustBaseLineDetail> custBaselineList = custBaseLineMapper.getCustBaseLineInfo();
                    LambdaQueryWrapper<CustBaseLineDetail> queryWrapperBase = new LambdaQueryWrapper<>();
                    queryWrapperBase.eq(CustBaseLineDetail::getBaselineLibId,planList.get(0).getBaselinId());
                    List<CustBaseLineDetail> custBaselineList = custBaseLineDetailService.list(queryWrapperBase);
                    List<CustBaseLineDetail> custBaselines = null;
                    //获取今日效果评估数据
                    LambdaQueryWrapper<CustEvaluationImmediate> queryWrapper = new LambdaQueryWrapper<>();
                    queryWrapper.in(CustEvaluationImmediate::getEventId, eventIds);
                    List<CustEvaluationImmediate> evaluationImmediateList = custEvaluationImmediateService.list(queryWrapper);
                    CustEvaluationImmediate evaluationImmediate = null;
                    List<String> regulateList = new ArrayList<>();
                    String regulateDate = simpleDateFormat.format(event.getRegulateDate());
                    regulateList.add(regulateDate);
                    List<String> conIds =evaluCustTaskService.getConsIdByPlanId(planList.get(0).getPlanId());
                    //查询客户历史曲线
                    List<CustBaseLineDetail> consCurveHistorys =  custBaseLineMapper.getCustCurve(regulateList,conIds);
                    //查询客户实时曲线
                    List<CustBaseLineDetail> consCurveTodays = custBaseLineMapper.getCustCurveToday(regulateList,conIds);
                    List<EvaluCustTask> evaluTaskUpdateList = new ArrayList<>();
                    List<CustEvaluationImmediate> evaluationImmediateUpdateList = new ArrayList<>();
                    List<CustEvaluationImmediate> evaluationImmediateInsertList = new ArrayList<>();
                    for (EvaluCustTask evaluTask : evaluTasks) {
                        List<CustInvitation> eventInvitations = custInvitations.stream().filter(custInvitation ->
                                custInvitation.getEventId().equals(evaluTask.getEventId()) && custInvitation.getCustId().equals(evaluTask.getCustId())
                        ).collect(Collectors.toList());
                        if (null == eventInvitations || eventInvitations.size() == 0) {
                            evaluTask.setEvaluTodayDesc("无客户邀约信息");
                            evaluTask.setEvaluTodayStatus("3");
                            evaluTaskUpdateList.add(evaluTask);
                            continue;
                        } else {
                            eventInvitation = eventInvitations.get(0);
                            if (null == eventInvitation || null == eventInvitation.getCustId()) {
                                evaluTask.setEvaluTodayDesc("无客户邀约信息");
                                evaluTask.setEvaluTodayStatus("3");
                                evaluTaskUpdateList.add(evaluTask);
                                continue;
                            }
                        }
                        List<CustEvaluationImmediate> evaluationImmediates = null;
                        if (null != evaluTask.getCustId() && null != evaluationImmediateList && evaluationImmediateList.size() > 0) {
                            evaluationImmediates = evaluationImmediateList.stream().filter(immediate -> immediate.getCustId().equals(evaluTask.getCustId())
                                    && immediate.getEventId().equals(evaluTask.getEventId())
                            ).collect(Collectors.toList());
                        }
                        if (null == evaluationImmediates || evaluationImmediates.size() == 0) {
                            //定义响应效果评估
                            evaluationImmediate = new CustEvaluationImmediate();
                            evaluationImmediate.setEventId(evaluTask.getEventId());
                            evaluationImmediate.setCustId(evaluTask.getCustId());
                        } else {
                            evaluationImmediate = evaluationImmediates.get(0);
                        }
                        evaluationImmediate.setInvitationCap(eventInvitation.getInvitationCap());
                        evaluationImmediate.setReplyCap(eventInvitation.getReplyCap());
                        if("0".equals(eventInvitation.getIntegrator())) {
                            //直接客户如果有效果评估有效用户，直接有效
                            if(null!=custIds && custIds.size()>0) {
                                if(custIds.contains(eventInvitation.getCustId())) {
                                    evaluTask.setEvaluTodayDesc("直接客户存在有效用户，直接设置为有效");
                                    evaluTask.setEvaluTodayStatus("2");
                                    evaluationImmediate.setRemark("直接客户存在有效用户，直接设置为有效");
                                    evaluationImmediate.setEffectiveTime(hour);
                                    evaluationImmediate.setIsEffective(YesOrNotEnum.Y.getCode());
                                    evaluationImmediate.setActualCap(eventInvitation.getReplyCap());
                                    evaluationImmediate.setConfirmCap(eventInvitation.getReplyCap());
                                    if (null == evaluationImmediates || evaluationImmediates.size() == 0) {
                                        long id = IdWorker.getId();
                                        evaluationImmediate.setEvaluationId(id);
                                        evaluationImmediateInsertList.add(evaluationImmediate);
                                    } else {
                                        evaluationImmediateUpdateList.add(evaluationImmediate);
                                    }
                                    evaluTask.setEvaluTodayId(evaluationImmediate.getEvaluationId());
                                    evaluTaskUpdateList.add(evaluTask);
                                    evaluationImmediate.setIsEffective("Y");
                                } else {
                                    evaluTask.setEvaluTodayDesc("直接客户无有效用户，直接设置为无效");
                                    evaluTask.setEvaluTodayStatus("2");
                                    evaluationImmediate.setRemark("直接客户无有效用户，直接设置为无效");
                                    evaluationImmediate.setEffectiveTime(0);
                                    evaluationImmediate.setIsEffective(YesOrNotEnum.N.getCode());
                                    evaluationImmediate.setActualCap(eventInvitation.getReplyCap());
                                    evaluationImmediate.setConfirmCap(eventInvitation.getReplyCap());
                                    if (null == evaluationImmediates || evaluationImmediates.size() == 0) {
                                        long id = IdWorker.getId();
                                        evaluationImmediate.setEvaluationId(id);
                                        evaluationImmediateInsertList.add(evaluationImmediate);
                                    } else {
                                        evaluationImmediateUpdateList.add(evaluationImmediate);
                                    }
                                    evaluTask.setEvaluTodayId(evaluationImmediate.getEvaluationId());
                                    evaluTaskUpdateList.add(evaluTask);
                                    evaluationImmediate.setIsEffective("N");
                                }
                            } else {
                                //判断是否所有评估过的用户
                                if(counts>0) {
                                    evaluTask.setEvaluTodayDesc("直接客户无有效用户，直接设置为无效");
                                    evaluTask.setEvaluTodayStatus("2");
                                    evaluationImmediate.setRemark("直接客户无有效用户，直接设置为无效");
                                    evaluationImmediate.setEffectiveTime(0);
                                    evaluationImmediate.setIsEffective(YesOrNotEnum.N.getCode());
                                    evaluationImmediate.setActualCap(eventInvitation.getReplyCap());
                                    evaluationImmediate.setConfirmCap(eventInvitation.getReplyCap());
                                    if (null == evaluationImmediates || evaluationImmediates.size() == 0) {
                                        long id = IdWorker.getId();
                                        evaluationImmediate.setEvaluationId(id);
                                        evaluationImmediateInsertList.add(evaluationImmediate);
                                    } else {
                                        evaluationImmediateUpdateList.add(evaluationImmediate);
                                    }
                                    evaluTask.setEvaluTodayId(evaluationImmediate.getEvaluationId());
                                    evaluTaskUpdateList.add(evaluTask);
                                    evaluationImmediate.setIsEffective("N");
                                }
                            }
                        } else {
                            //获取需求响应方案
                            List<Plan> plans = planList.stream().filter(plan1 -> plan1.getRegulateId().equals(eventId))
                                    .collect(Collectors.toList());
                            if (null != plans && plans.size() > 0) {
                                plan = plans.get(0);
                            }
                            //查询客户基线
                            CustBaseLineDetail custCurveBase = null;
                            if (null != plan && null != plan.getBaselinId()) {
                                custBaselines = custBaselineList.stream().filter(consBaseline1 -> consBaseline1.getCustId().equals(evaluTask.getCustId())
                                        && consBaseline1.getBaselineLibId().equals(plans.get(0).getBaselinId())
                                ).collect(Collectors.toList());
                                if (null == custBaselines || custBaselines.size() == 0) {
                                    evaluTask.setEvaluTodayDesc("基线为空，直接设置为有效");
                                    evaluTask.setEvaluTodayStatus("2");
                                    evaluationImmediate.setRemark("基线为空，直接设置为有效");
                                    evaluationImmediate.setEffectiveTime(hour);
                                    evaluationImmediate.setIsEffective(YesOrNotEnum.Y.getCode());
                                    evaluationImmediate.setActualCap(eventInvitation.getReplyCap());
                                    evaluationImmediate.setConfirmCap(eventInvitation.getReplyCap());
                                    if (null == evaluationImmediates || evaluationImmediates.size() == 0) {
                                        long id = IdWorker.getId();
                                        evaluationImmediate.setEvaluationId(id);
                                        evaluationImmediateInsertList.add(evaluationImmediate);
                                    } else {
                                        evaluationImmediateUpdateList.add(evaluationImmediate);
                                    }
                                    evaluTask.setEvaluTodayId(evaluationImmediate.getEvaluationId());
                                    evaluTaskUpdateList.add(evaluTask);
                                    continue;
                                } else {
                                    custCurveBase = custBaselines.get(0);
                                }
                            } else {
                                evaluTask.setEvaluTodayDesc("需求响应方案不存在");
                                evaluTask.setEvaluTodayStatus("3");
                                evaluTaskUpdateList.add(evaluTask);
                                continue;
                            }
                            evaluationImmediate.setAvgLoadBaseline(custCurveBase.getAvgLoadBaseline());
                            evaluationImmediate.setMaxLoadBaseline(custCurveBase.getMaxLoadBaseline());
                            evaluationImmediate.setMinLoadBaseline(custCurveBase.getMinLoadBaseline());
                            CustBaseLineDetail consCurve = null;
                            if(localDate.compareTo(evaluTask.getRegulateDate())==0) {
                                if (null != consCurveTodays && consCurveTodays.size() > 0) {
                                    List<CustBaseLineDetail> curveTodays = consCurveTodays.stream().filter(consCurveToday -> consCurveToday.getCustId().equals(evaluTask.getCustId())
                                            && consCurveToday.getSimplesDate().equals(regulateDate))
                                            .collect(Collectors.toList());
                                    if (null != curveTodays && curveTodays.size() > 0) {
                                        consCurve = new CustBaseLineDetail();
                                        BeanUtils.copyProperties(curveTodays.get(0), consCurve);
                                    }
                                }
                            } else {
                                if (null != consCurveHistorys && consCurveHistorys.size() > 0) {
                                    List<CustBaseLineDetail> consCurves = consCurveHistorys.stream().filter(consCurveHis -> consCurveHis.getCustId().equals(evaluTask.getCustId())
                                            && consCurveHis.getSimplesDate().equals(regulateDate)).collect(Collectors.toList());
                                    if (null != consCurves && consCurves.size() > 0) {
                                        consCurve = consCurves.get(0);
                                    }
                                }
                            }
                            if (strategyUtils.judgeIsAllNull2(consCurve, startP, endP)) {
                                // 判断曲线是否为空
                                LocalDateTime futureFif = LocalDateTime.of(event.getRegulateDate(), LocalTime.of(endHour, endMinute)).plusMinutes(45);
                                if (localDateTime.isAfter(futureFif)) {
                                    // 如果事件已经结束了超过45分钟，直接设置为有效
                                    evaluationImmediate.setIsEffective(YesOrNotEnum.Y.getCode());
                                    evaluationImmediate.setEffectiveTime(hour);
                                    evaluationImmediate.setActualCap(eventInvitation.getReplyCap());
                                    evaluationImmediate.setConfirmCap(eventInvitation.getReplyCap());
                                    evaluationImmediate.setAvgLoadActual(null);
                                    evaluationImmediate.setMaxLoadActual(null);
                                    evaluationImmediate.setMinLoadActual(null);
                                    evaluationImmediate.setRemark("事件结束45分钟，仍无今日实时荷,直接设置为有效");
                                    if (null == evaluationImmediates || evaluationImmediates.size() == 0) {
                                        long id = IdWorker.getId();
                                        evaluationImmediate.setEvaluationId(id);
                                        evaluationImmediateInsertList.add(evaluationImmediate);
                                    } else {
                                        evaluationImmediateUpdateList.add(evaluationImmediate);
                                    }
                                    evaluTask.setEvaluTodayDesc("事件结束45分钟，仍无今日实时荷,直接设置为有效");
                                    evaluTask.setEvaluTodayStatus("2");
                                    evaluTask.setEvaluTodayId(evaluationImmediate.getEvaluationId());
                                    evaluTaskUpdateList.add(evaluTask);
                                    continue;
                                }
                                //判断事件是否到了夜里23:49
                                LocalTime futureFif2 = LocalTime.of(23, 49);
                                // 判断曲线是否为空
                                if (localTime2.isAfter(futureFif2)) {
                                    // 如果9点45分无历史负荷，直接设置为有效
                                    evaluationImmediate.setIsEffective(YesOrNotEnum.Y.getCode());
                                    evaluationImmediate.setEffectiveTime(hour);
                                    evaluationImmediate.setActualCap(eventInvitation.getReplyCap());
                                    evaluationImmediate.setConfirmCap(eventInvitation.getReplyCap());
                                    evaluationImmediate.setRemark("23点45分无响应负荷直接设置为有效");
                                    if (null == evaluationImmediates || evaluationImmediates.size() == 0) {
                                        long id = IdWorker.getId();
                                        evaluationImmediate.setEvaluationId(id);
                                        evaluationImmediateInsertList.add(evaluationImmediate);
                                    } else {
                                        evaluationImmediateUpdateList.add(evaluationImmediate);
                                    }
                                    evaluTask.setEvaluTodayDesc("23点45分无响应负荷,直接设置为有效");
                                    evaluTask.setEvaluTodayStatus("2");
                                    evaluTask.setEvaluTodayId(evaluationImmediate.getEvaluationId());
                                    evaluTaskUpdateList.add(evaluTask);
                                    continue;
                                }
                                evaluTask.setEvaluTodayDesc("客户负荷曲线不存在");
                                evaluTask.setEvaluTodayStatus("3");
                                evaluTask.setEvaluTodayId(evaluationImmediate.getEvaluationId());
                                evaluTaskUpdateList.add(evaluTask);
                                continue;
                            }
                            List<BigDecimal> actualList = new ArrayList<>();
                            List<BigDecimal> forecastList = new ArrayList<>();
                            LocalDateTime futureFif = LocalDateTime.of(event.getRegulateDate(), LocalTime.of(endHour, endMinute)).plusMinutes(45);
                            if (LocalDateTime.now().isBefore(futureFif)) {
                                // 事件结束45分钟以内
                                BigDecimal endValue =null;
                                try {
                                    endValue = (BigDecimal) consMethodMap.get(endP).invoke(consCurve);
                                } catch (Exception e) {
                                    e.getMessage();
                                }
                                if (ObjectUtil.isNull(endValue) || BigDecimal.ZERO.compareTo(endValue) == 0) {
                                    // 事件最后一个点为0，直接设置为无效
                                    if (null == evaluationImmediates || evaluationImmediates.size() == 0) {
                                        long id = IdWorker.getId();
                                        evaluationImmediate.setEvaluationId(id);
                                        evaluationImmediateInsertList.add(evaluationImmediate);
                                    } else {
                                        evaluationImmediateUpdateList.add(evaluationImmediate);
                                    }
                                    evaluTask.setEvaluTodayDesc("实际负荷最后点为0值");
                                    evaluTask.setEvaluTodayStatus("3");
                                    evaluTask.setEvaluTodayId(evaluationImmediate.getEvaluationId());
                                    evaluTaskUpdateList.add(evaluTask);
                                    continue;
                                }
                            }

                            for (int i = startP; i <= endP; i++) {
                                BigDecimal power = null;
                                BigDecimal power2 = null;
                                try {
                                    power = (BigDecimal) consMethodMap.get(i).invoke(consCurve);
                                    power2 = (BigDecimal) consMethodMap.get(i).invoke(custCurveBase);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                if (ObjectUtil.isNotNull(power)) {

                                    actualList.add(power);
                                } else {
                                    actualList.add(BigDecimal.ZERO);
                                }
                                if (ObjectUtil.isNotNull(power2)) {
                                    forecastList.add(power2);
                                } else {
                                    forecastList.add(BigDecimal.ZERO);
                                }
                            }

                            if (CollectionUtil.hasNull(actualList) || CollectionUtil.hasNull(forecastList)) {
                                if (null == evaluationImmediates || evaluationImmediates.size() == 0) {
                                    long id = IdWorker.getId();
                                    evaluationImmediate.setEvaluationId(id);
                                    evaluationImmediateInsertList.add(evaluationImmediate);
                                } else {
                                    evaluationImmediateUpdateList.add(evaluationImmediate);
                                }
                                evaluTask.setEvaluTodayDesc("实际负荷或者基线负荷不完整");
                                evaluTask.setEvaluTodayStatus("3");
                                evaluTask.setEvaluTodayId(evaluationImmediate.getEvaluationId());
                                evaluTaskUpdateList.add(evaluTask);
                                continue;
                            }

                            BigDecimal sumActual = actualList.stream().reduce(BigDecimal.ZERO, (d1, d2) -> {
                                return Optional.ofNullable(d1).orElse(BigDecimal.ZERO).add(Optional.ofNullable(d2).orElse(BigDecimal.ZERO));
                            });
                            BigDecimal minActual = actualList.stream().min(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
                            BigDecimal maxActual = actualList.stream().max(BigDecimal::compareTo).orElse(BigDecimal.ZERO);

                            evaluationImmediate.setAvgLoadActual(NumberUtil.div(sumActual, actualList.size(), 2));
                            evaluationImmediate.setMaxLoadActual(maxActual);
                            evaluationImmediate.setMinLoadActual(minActual);

                            BigDecimal actualCap = NumberUtil.sub(evaluationImmediate.getAvgLoadBaseline(), evaluationImmediate.getAvgLoadActual());
                      /*  if(BigDecimal.ZERO.compareTo(actualCap)>0) {
                            actualCap = NumberUtil.mul(actualCap,-1);
                        }*/
                            if("2".equals(event.getResponseType())) {
                                actualCap = NumberUtil.mul(actualCap, -1);
                                //evaluationImmediate.setEffectiveTime(strategyUtils.calEeffectiveTime2(actualList, forecastList, eventInvitation.getReplyCap()) * 15);
                            }
                            evaluationImmediate.setActualCap(actualCap);
                            evaluationImmediate.setEffectiveTime(hour);
                            evaluationImmediate = strategyUtils.judgeCustEffectiveOfRule(evaluationImmediate, evaluTask,event);
                            //evaluationImmediate.setIsEffective(strategyUtils.judgeCustEffectiveOfRule(evaluationImmediate, evaluTask,event) ? YesOrNotEnum.Y.getCode() : YesOrNotEnum.N.getCode());
                            //负荷响应量为实际负荷占反馈响应量百分比
                            if(null==eventInvitation.getReplyCap() || eventInvitation.getReplyCap().compareTo(BigDecimal.ZERO)==0) {
                                evaluationImmediate.setExecuteRate(BigDecimal.ZERO);
                            } else {
                                evaluationImmediate.setExecuteRate(NumberUtil.div(actualCap,eventInvitation.getReplyCap()));
                            }
                            //核定响应量
                            if (evaluationImmediate.getIsEffective().equals(YesOrNotEnum.Y.getCode())) {
                                //如果实际响应量大于1.2倍反馈响应量，取1.2倍反馈响应量，否则取实际响应量
                                BigDecimal temp = strategyUtils.getConfirmCap(eventInvitation.getReplyCap(),actualCap);
                                evaluationImmediate.setConfirmCap(temp);
                                evaluTask.setEvaluTodayStatus("2");
                                evaluTask.setEvaluTodayDesc("");
                            } else {
                                //无效响应核定响应量为0
                                evaluationImmediate.setConfirmCap(new BigDecimal("0"));
                                //evaluationImmediate.setRemark("无效响应,核定响应量为0");
                                evaluationImmediate.setEffectiveTime(0);
                                evaluTask.setEvaluTodayStatus("2");
                                evaluTask.setEvaluTodayDesc(evaluationImmediate.getRemark());
                            }
                            if (null == evaluationImmediates || evaluationImmediates.size() == 0) {
                                long id = IdWorker.getId();
                                evaluationImmediate.setEvaluationId(id);
                                evaluationImmediateInsertList.add(evaluationImmediate);
                            } else {
                                evaluationImmediateUpdateList.add(evaluationImmediate);
                            }
                            evaluTask.setEvaluTodayId(evaluationImmediate.getEvaluationId());
                            evaluTaskUpdateList.add(evaluTask);
                        }

                    }
                    //更新
                    String url = dataurl;
                    String user = userName;
                    String password = datapassword;
                    //2）、加载驱动，不需要显示注册驱动
                    Class.forName(driver);
                    //更新同步邀约信息状态
                    String sql = "UPDATE dr_evalu_cust_task set EVALU_TODAY_STATUS=?,EVALU_TODAY_ID=?,EVALU_TODAY_DESC=?,UPDATE_TIME=? where id=?";
                    if (null != evaluTaskUpdateList && evaluTaskUpdateList.size() > 0) {
                        //获取数据库连接
                        conn = DriverManager.getConnection(url, user, password);
                        conn.setAutoCommit(false);
                        preparedStatement = conn.prepareStatement(sql);
                        int i = 0;
                        for (EvaluCustTask evaluTask : evaluTaskUpdateList) {
                            preparedStatement.setString(1, evaluTask.getEvaluTodayStatus());
                            if(null!=evaluTask.getEvaluTodayId()) {
                                preparedStatement.setLong(2, evaluTask.getEvaluTodayId());
                            } else {
                                preparedStatement.setNull(2,Types.BIGINT);
                            }
                            if(null!=evaluTask.getEvaluTodayDesc()) {
                                preparedStatement.setString(3, evaluTask.getEvaluTodayDesc());
                            } else {
                                preparedStatement.setNull(3,Types.VARCHAR);
                            }
                            preparedStatement.setString(4, dateFormat.format(new Date()));
                            preparedStatement.setLong(5,evaluTask.getId());
                            preparedStatement.addBatch();
                            if ((i + 1) % 1000 == 0 || i == evaluTaskUpdateList.size() - 1) {
                                //每1000条提交一次
                                preparedStatement.executeBatch();
                                //清空记录
                                preparedStatement.clearBatch();
                            }
                            i++;
                        }
                    }
                    log.info("更新客户今日效果评估任务表完成，共:" + evaluTaskUpdateList.size() + "条");
                    String sql2 = "INSERT INTO dr_cust_evaluation_immediate (evaluation_id,cust_id,event_id,invitation_cap,reply_cap,actual_cap,confirm_cap,max_load_baseline,min_load_baseline,avg_load_baseline,max_load_actual,min_load_actual,avg_load_actual,is_effective,effective_time,create_time,remark,execute_rate)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    if (null !=evaluationImmediateInsertList  && evaluationImmediateInsertList.size() > 0) {
                        //获取数据库连接
                        conn2 = DriverManager.getConnection(url, user, password);
                        conn2.setAutoCommit(false);
                        preparedStatement2 = conn2.prepareStatement(sql2);
                        int i = 0;
                        for (CustEvaluationImmediate evaluationImmediate1 : evaluationImmediateInsertList) {
                            preparedStatement2.setLong(1, evaluationImmediate1.getEvaluationId());
                            preparedStatement2.setLong(2, evaluationImmediate1.getCustId());
                            preparedStatement2.setLong(3, evaluationImmediate1.getEventId());
                            preparedStatement2.setBigDecimal(4, evaluationImmediate1.getInvitationCap());
                            preparedStatement2.setBigDecimal(5, evaluationImmediate1.getReplyCap());
                            preparedStatement2.setBigDecimal(6, evaluationImmediate1.getActualCap());
                            preparedStatement2.setBigDecimal(7, evaluationImmediate1.getConfirmCap());
                            preparedStatement2.setBigDecimal(8, evaluationImmediate1.getMaxLoadBaseline());
                            preparedStatement2.setBigDecimal(9, evaluationImmediate1.getMinLoadBaseline());
                            preparedStatement2.setBigDecimal(10, evaluationImmediate1.getAvgLoadBaseline());
                            preparedStatement2.setBigDecimal(11, evaluationImmediate1.getMaxLoadActual());
                            preparedStatement2.setBigDecimal(12, evaluationImmediate1.getMinLoadActual());
                            preparedStatement2.setBigDecimal(13, evaluationImmediate1.getAvgLoadActual());
                            preparedStatement2.setString(14, evaluationImmediate1.getIsEffective());
                            if(null!=evaluationImmediate1.getEffectiveTime()) {
                                preparedStatement2.setInt(15, evaluationImmediate1.getEffectiveTime());
                            } else {
                                preparedStatement2.setInt(15, 0);
                            }
                            preparedStatement2.setString(16, dateFormat.format(new Date()));
                            preparedStatement2.setString(17,evaluationImmediate1.getRemark());
                            preparedStatement2.setBigDecimal(18,evaluationImmediate1.getExecuteRate());
                            preparedStatement2.addBatch();
                            if ((i + 1) % 1000 == 0 || i == evaluationImmediateInsertList.size() - 1) {
                                //每1000条提交一次
                                preparedStatement2.executeBatch();
                                //清空记录
                                preparedStatement2.clearBatch();
                            }
                            i++;
                        }

                    }
                    log.info("保存客户今日效果评估数据完成，共:" + evaluationImmediateInsertList.size() + "条");
                    String sql3 = "update dr_cust_evaluation_immediate set cust_id=?, event_id=?, invitation_cap=?, reply_cap=?, actual_cap=?, confirm_cap=?, max_load_baseline=?, min_load_baseline=?, avg_load_baseline=?, max_load_actual=?, min_load_actual=?, avg_load_actual=?, is_effective=?, effective_time=?, update_time=?,remark=?,execute_rate=? where evaluation_id=?";
                    if (null !=evaluationImmediateUpdateList  && evaluationImmediateUpdateList.size() > 0) {
                        //获取数据库连接
                        conn3 = DriverManager.getConnection(url, user, password);
                        conn3.setAutoCommit(false);
                        preparedStatement3 = conn3.prepareStatement(sql3);
                        int i = 0;
                        for (CustEvaluationImmediate evaluationImmediate1 : evaluationImmediateUpdateList) {
                            preparedStatement3.setLong(1, evaluationImmediate1.getCustId());
                            preparedStatement3.setLong(2, evaluationImmediate1.getEventId());
                            preparedStatement3.setBigDecimal(3, evaluationImmediate1.getInvitationCap());
                            preparedStatement3.setBigDecimal(4, evaluationImmediate1.getReplyCap());
                            preparedStatement3.setBigDecimal(5, evaluationImmediate1.getActualCap());
                            preparedStatement3.setBigDecimal(6, evaluationImmediate1.getConfirmCap());
                            preparedStatement3.setBigDecimal(7, evaluationImmediate1.getMaxLoadBaseline());
                            preparedStatement3.setBigDecimal(8, evaluationImmediate1.getMinLoadBaseline());
                            preparedStatement3.setBigDecimal(9, evaluationImmediate1.getAvgLoadBaseline());
                            preparedStatement3.setBigDecimal(10, evaluationImmediate1.getMaxLoadActual());
                            preparedStatement3.setBigDecimal(11, evaluationImmediate1.getMinLoadActual());
                            preparedStatement3.setBigDecimal(12, evaluationImmediate1.getAvgLoadActual());
                            if(null!=evaluationImmediate1.getIsEffective()) {
                                preparedStatement3.setString(13, evaluationImmediate1.getIsEffective());
                            } else {
                                preparedStatement3.setNull(13,Types.VARCHAR);
                            }
                            if(null!=evaluationImmediate1.getEffectiveTime()) {
                                preparedStatement3.setInt(14, evaluationImmediate1.getEffectiveTime());
                            } else {
                                preparedStatement3.setNull(14,Types.INTEGER);
                            }
                            preparedStatement3.setString(15, dateFormat.format(new Date()));
                            preparedStatement3.setString(16,evaluationImmediate1.getRemark());
                            preparedStatement3.setBigDecimal(17,evaluationImmediate1.getExecuteRate());
                            preparedStatement3.setLong(18,evaluationImmediate1.getEvaluationId());
                            preparedStatement3.addBatch();
                            if ((i + 1) % 1000 == 0 || i == evaluationImmediateUpdateList.size() - 1) {
                                //每1000条提交一次
                                preparedStatement3.executeBatch();
                                //清空记录
                                preparedStatement3.clearBatch();
                            }
                            i++;
                        }

                    }
                    log.info("更新客户今日效果评估数据完成，共:" + evaluationImmediateUpdateList.size() + "条");
                    if (null != conn) {
                        conn.commit();
                    }
                    if (null != conn2) {
                        conn2.commit();
                    }
                    if (null != conn3) {
                        conn3.commit();
                    }
                    log.info("今日客户效果评估数据计算完成!");
                } catch (Exception e) {
                    try {
                        if (null != conn) {
                            conn.rollback();
                        }
                        if (null != conn2) {
                            conn2.rollback();
                        }
                        if (null != conn3) {
                            conn3.rollback();
                        }
                    } catch (SQLException sqlException) {
                        sqlException.printStackTrace();
                    }
                    e.printStackTrace();
                } finally {
                    Integer count = event.getCustTodayCount();
                    if(null==count) {
                        count = 0;
                    }
                    count = count + 1;
                    event.setCustTodayCount(count);
                    eventService.updateById(event);
                    if (null != conn) {
                        try {
                            conn.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    if (null != preparedStatement) {
                        try {
                            preparedStatement.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                    }
                    if (null != conn2) {
                        try {
                            conn2.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    if (null != preparedStatement2) {
                        try {
                            preparedStatement2.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                    }
                    if (null != conn3) {
                        try {
                            conn3.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    if (null != preparedStatement3) {
                        try {
                            preparedStatement3.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        };
        return runnable;
    }

    /**
     * 用户次日事件效果评估
     */
    public void executeEvaluationListNew(String param) {
        log.info(">>> 用户次日效果评估任务开始");
        LocalDate lastDay = LocalDate.now().minusDays(1);
        // 1.查询当日效果评估任务
        EvaluTaskParam evaluTaskParam = new EvaluTaskParam();
        if(null!=param && !"".equals(param)) {
            if(StrategyUtils.isDate(param)) {
                evaluTaskParam.setRegulateDate(LocalDate.parse(param));
                evaluTaskParam.setRegulateDateStr(param);
            } else {
                evaluTaskParam.setRegulateDate(lastDay);
                evaluTaskParam.setRegulateDateStr(simpleDateFormat.format(lastDay));
            }
        } else {
            evaluTaskParam.setRegulateDate(lastDay);
            evaluTaskParam.setRegulateDateStr(simpleDateFormat.format(lastDay));
        }
        evaluTaskParam.setEvaluNextDayStatus("1");
        List<EvaluTask> evaluTasks = null;
        evaluTasks = evaluTaskService.list(evaluTaskParam);
        // 2.循环对事件进行评价
        if (CollectionUtil.isEmpty(evaluTasks)) {
            log.info("无效果评估任务!");
            return;
        }
        //查询事件信息
        LambdaQueryWrapper<Event> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Event::getRegulateDate, evaluTaskParam.getRegulateDate());
        lambdaQueryWrapper.eq(Event::getTimeType, "1");
        List<String> statusList = new ArrayList<>();
        statusList.add("03");
        statusList.add("04");
        lambdaQueryWrapper.in(Event::getEventStatus,statusList);
        List<Event> eventList = eventService.list(lambdaQueryWrapper);
        List<Long> eventIds = new ArrayList<>();
        if(null!=eventList && eventList.size()>0) {
            for(Event event : eventList) {
                eventIds.add(event.getEventId());
            }
        } else {
            log.info("无事件信息!");
            return;
        }
        ExecutorService executor = ExecutorBuilder.create()
                .setCorePoolSize(corePoolSize)
                .setMaxPoolSize(maxPoolSize)
                .setWorkQueue(new LinkedBlockingQueue<>(workQueue))
                .build();
        executor.execute(this.createEvaluationNew(evaluTasks,evaluTaskParam,eventIds));
        executor.shutdown();
    }

    //用户次日
    public Runnable createEvaluationNew(List<EvaluTask>  evaluTasks,EvaluTaskParam evaluTaskParam,List<Long> eventIds) {
        LocalTime localTime = LocalTime.now();
        Map<Integer, Method> consMethodMap = new HashMap<>();
        Map<Integer, Method> consMethodMap2 = new HashMap<>();
        StrategyUtils strategyUtils = new StrategyUtils();
        try{
            for (int j=1; j<=96; j++){
                consMethodMap.put(j, ConsBaseline.class.getMethod("getP"+j));
                consMethodMap2.put(j, ConsCurve.class.getMethod("getP"+j));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                long eventId = evaluTasks.get(0).getEventId();
                Event event = eventService.getById(eventId);
                if(null==event) {
                    log.info("无事件信息");
                    return;
                }
                String status = event.getEventStatus();
                PreparedStatement preparedStatement = null;
                Connection conn = null;
                PreparedStatement preparedStatement2 = null;
                Connection conn2 = null;
                PreparedStatement preparedStatement3 = null;
                Connection conn3 = null;
                PreparedStatement preparedStatement4 = null;
                Connection conn4 = null;
                PreparedStatement preparedStatement5 = null;
                Connection conn5 = null;
                try {
                    // 判断事件是否已经截至
                    Integer endHour = 0;
                    Integer endMinute = 0;
                    endHour = Integer.parseInt(event.getEndTime().substring(0, 2));
                    endMinute = Integer.parseInt(event.getEndTime().substring(3));
                    LocalTime endTime = LocalTime.of(endHour, endMinute);
                    // 事件开始点和事件结束点
                    int startP = CurveUtil.covDateTimeToPoint(event.getStartTime());
                    int endP = CurveUtil.covDateTimeToPoint(event.getEndTime());
                    int hour = (endP-startP)*15;
                    //查询事件参与所有用户
                    List<ConsInvitation> consInvitations = consInvitationService.getConsInfoByEvent(eventId);
                    if (null == consInvitations || consInvitations.size() == 0) {
                        log.info("无用户邀约信息");
                        return;
                    }
                    ConsInvitation eventInvitation = null;
                    LambdaQueryWrapper<Plan> queryWrapperPlan = new LambdaQueryWrapper<>();
                    queryWrapperPlan.eq(Plan::getRegulateId,eventId);
                    List<Plan> planList = planService.list(queryWrapperPlan);
                    if (null == planList || planList.size() == 0) {
                        log.info("无方案信息");
                        return;
                    }
                    Plan plan = null;
                    //获取所有用户基线
                    //List<ConsBaseline> consBaselineList = baseLineMapper.getConsBaseLineInfo();
                    LambdaQueryWrapper<ConsBaseline> queryWrapperBase = new LambdaQueryWrapper<>();
                    queryWrapperBase.eq(ConsBaseline::getBaselineLibId,planList.get(0).getBaselinId());
                    List<ConsBaseline> consBaselineList = consBaselineService.list(queryWrapperBase);
                    List<ConsBaseline> consBaselines = null;
                    //获取效果今日评估数据
                    LambdaQueryWrapper<ConsEvaluation> queryWrapper = new LambdaQueryWrapper<>();
                    queryWrapper.in(ConsEvaluation::getEventId, eventIds);
                    List<ConsEvaluation> evaluationList = consEvaluationService.list(queryWrapper);
                    ConsEvaluation evaluation = null;
                    ConsBaseline consBaseline = null;
                    //查询历史曲线
                    LambdaQueryWrapper<ConsCurve> curveLambdaQueryWrapper = new LambdaQueryWrapper<>();
                    curveLambdaQueryWrapper.eq(ConsCurve::getDataDate, evaluTaskParam.getRegulateDate());
                    List<ConsCurve> consCurveHistorys = consCurveService.list(curveLambdaQueryWrapper);
                    //查询执行曲线
                    LambdaQueryWrapper<EventPowerExecute> executeLambdaQueryWrapper = new LambdaQueryWrapper<>();
                    executeLambdaQueryWrapper.eq(EventPowerExecute::getDataDate, evaluTaskParam.getRegulateDate());
                    List<EventPowerExecute> executeImmediates = eventPowerExecuteService.list(executeLambdaQueryWrapper);
                    List<EvaluTask> evaluTaskUpdateList = new ArrayList<>();
                    List<ConsEvaluation> evaluationUpdateList = new ArrayList<>();
                    List<ConsEvaluation> evaluationInsertList = new ArrayList<>();
                    List<EventPowerExecute> executeUpdateList = new ArrayList<>();
                    List<EventPowerExecute> executeInertList = new ArrayList<>();
                    for (EvaluTask evaluTask : evaluTasks) {
                        List<ConsInvitation> eventInvitations = consInvitations.stream().filter(consInvitation ->
                                consInvitation.getEventId().equals(evaluTask.getEventId()) && consInvitation.getConsId().equals(evaluTask.getConsId())
                        ).collect(Collectors.toList());
                        if (null == eventInvitations || eventInvitations.size() == 0) {
                            evaluTask.setEvaluNextdayDesc("无用户邀约信息");
                            evaluTask.setEvaluNextdayStatus("3");
                            evaluTaskUpdateList.add(evaluTask);
                            continue;
                        } else {
                            eventInvitation = eventInvitations.get(0);
                            if (null == eventInvitation) {
                                evaluTask.setEvaluNextdayDesc("无用户邀约信息");
                                evaluTask.setEvaluNextdayStatus("3");
                                evaluTaskUpdateList.add(evaluTask);
                                continue;
                            }
                        }
                        List<ConsEvaluation> evaluations = null;
                        if (null != evaluTask.getConsId() && null != evaluationList && evaluationList.size() > 0) {
                            evaluations = evaluationList.stream().filter(immediate -> immediate.getConsId().equals(evaluTask.getConsId())
                                    && immediate.getEventId().equals(evaluTask.getEventId())
                            ).collect(Collectors.toList());
                        }
                        if (null == evaluations || evaluations.size() == 0) {
                            //定义响应效果评估
                            evaluation = new ConsEvaluation();
                            evaluation.setEventId(evaluTask.getEventId());
                            evaluation.setConsId(evaluTask.getConsId());
                            evaluation.setJoinUserType(eventInvitation.getJoinUserType());
                        } else {
                            evaluation = evaluations.get(0);
                        }
                        evaluation.setInvitationCap(eventInvitation.getInvitationCap());
                        evaluation.setReplyCap(eventInvitation.getReplyCap());
                        //获取需求响应方案
                        List<Plan> plans = planList.stream().filter(plan1 -> plan1.getRegulateId().equals(eventId))
                                .collect(Collectors.toList());
                        if (null != plans && plans.size() > 0) {
                            plan = plans.get(0);
                        }
                        if (null != plan && null != plan.getBaselinId()) {
                            //获取事件当日用户基线
                            consBaselines = consBaselineList.stream().filter(consBaseline1 -> consBaseline1.getConsId().equals(evaluTask.getConsId())
                                    && consBaseline1.getBaselineLibId().equals(plans.get(0).getBaselinId())
                                    && consBaseline1.getNormal().equals("Y")
                            ).collect(Collectors.toList());
                            if (null != consBaselines && consBaselines.size() > 0) {
                                consBaseline = consBaselines.get(0);
                            } else {
                                //基线为空，直接有效
                                evaluation.setIsEffective(YesOrNotEnum.Y.getCode());
                                evaluation.setEffectiveTime(hour);
                                evaluation.setActualCap(eventInvitation.getReplyCap());
                                evaluation.setConfirmCap(eventInvitation.getReplyCap());
                                evaluation.setRemark("基线为空,直接设置为有效");
                                if (null == evaluations || evaluations.size() == 0) {
                                    //生成id
                                    long id = IdWorker.getId();
                                    evaluation.setEvaluationId(id);
                                    evaluationInsertList.add(evaluation);
                                } else {
                                    evaluationUpdateList.add(evaluation);
                                }
                                evaluTask.setEvaluNextdayDesc("基线为空,直接设置为有效");
                                evaluTask.setEvaluNextdayStatus("2");
                                evaluTask.setEvaluNextdayId(evaluation.getEvaluationId());
                                evaluTaskUpdateList.add(evaluTask);
                                continue;
                            }
                        } else {
                            evaluTask.setEvaluNextdayDesc("需求响应方案不存在");
                            evaluTask.setEvaluNextdayStatus("3");
                            evaluTaskUpdateList.add(evaluTask);
                            continue;
                        }
                        evaluation.setAvgLoadBaseline(consBaseline.getAvgLoadBaseline());
                        evaluation.setMaxLoadBaseline(consBaseline.getMaxLoadBaseline());
                        evaluation.setMinLoadBaseline(consBaseline.getMinLoadBaseline());
                        ConsCurve consCurve = null;
                        if (null != consCurveHistorys && consCurveHistorys.size() > 0) {
                            List<ConsCurve> curveHistorys = consCurveHistorys.stream().filter(consCurve1 -> consCurve1.getConsId().equals(evaluTask.getConsId()))
                                    .collect(Collectors.toList());
                            if (null != curveHistorys && curveHistorys.size() > 0) {
                                consCurve = new ConsCurve();
                                consCurve = curveHistorys.get(0);
                            }
                        }
                        LocalTime futureFif = LocalTime.of(9, 45);
                        if (strategyUtils.judgeIsAllNull(consCurve, startP, endP)) {
                            // 判断曲线是否为空
                            if (localTime.isAfter(futureFif)) {
                                // 如果9点45分无历史负荷，直接设置为有效
                                evaluation.setIsEffective(YesOrNotEnum.Y.getCode());
                                evaluation.setEffectiveTime(hour);
                                evaluation.setActualCap(eventInvitation.getReplyCap());
                                evaluation.setConfirmCap(eventInvitation.getReplyCap());
                                evaluation.setRemark("早上09:45 依然无昨日历史负荷，直接有效");
                                if (null == evaluations || evaluations.size() == 0) {
                                    long id = IdWorker.getId();
                                    evaluation.setEvaluationId(id);
                                    evaluationInsertList.add(evaluation);
                                } else {
                                    evaluationUpdateList.add(evaluation);
                                }
                                evaluTask.setEvaluNextdayDesc("早上09:45 依然无昨日历史负荷，直接有效");
                                evaluTask.setEvaluNextdayStatus("2");
                                evaluTask.setEvaluNextdayId(evaluation.getEvaluationId());
                                evaluTaskUpdateList.add(evaluTask);
                                continue;
                            }
                            evaluTask.setEvaluNextdayDesc("用户负荷曲线不存在");
                            evaluTask.setEvaluNextdayStatus("3");
                            evaluTask.setEvaluNextdayId(evaluation.getEvaluationId());
                            evaluTaskUpdateList.add(evaluTask);
                            continue;
                        } else {
                            EventPowerExecute eventPowerExecute = new EventPowerExecute();
                            BeanUtils.copyProperties(consCurve, eventPowerExecute);
                            eventPowerExecute.setEventId(evaluTask.getEventId());
                            eventPowerExecute.setConsId(eventInvitation.getConsId());
                            eventPowerExecute.setDataDate(event.getRegulateDate());
                            eventPowerExecute.setDataPointFlag("1");
                            if (executeImmediates != null && executeImmediates.size() > 0) {
                                List<EventPowerExecute> executeImmediates1 = executeImmediates.stream().filter(immediate -> immediate.getConsId().equals(evaluTask.getConsId())
                                        && immediate.getEventId().equals(evaluTask.getEventId())).collect(Collectors.toList());
                                if (null != executeImmediates1 && executeImmediates1.size() > 0) {
                                    eventPowerExecute.setDataId(executeImmediates1.get(0).getDataId());
                                    executeUpdateList.add(eventPowerExecute);
                                } else {
                                    executeInertList.add(eventPowerExecute);
                                }
                            } else {
                                executeInertList.add(eventPowerExecute);
                            }
                        }

                        if (localTime.isBefore(futureFif)) {
                            // 事件结束45分钟以内
                            BigDecimal endValue = null;
                            try {
                                endValue = (BigDecimal) consMethodMap2.get(endP).invoke(consCurve);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (ObjectUtil.isNull(endValue) || BigDecimal.ZERO.compareTo(endValue) == 0) {
                                // 事件最后一个点为0，直接设置为无效
                                if (null == evaluations || evaluations.size() == 0) {
                                    long id = IdWorker.getId();
                                    evaluation.setEvaluationId(id);
                                    evaluationInsertList.add(evaluation);
                                } else {
                                    evaluationUpdateList.add(evaluation);
                                }
                                evaluTask.setEvaluNextdayDesc("实际负荷最后点为0值");
                                evaluTask.setEvaluNextdayStatus("3");
                                evaluTask.setEvaluNextdayId(evaluation.getEvaluationId());
                                evaluTaskUpdateList.add(evaluTask);
                                continue;
                            }
                        }
                        List<BigDecimal> actualList = new ArrayList<>();
                        List<BigDecimal> forecastList = new ArrayList<>();
                        for (int i = startP; i <= endP; i++) {
                            BigDecimal power = null;
                            BigDecimal power2 = null;
                            try {
                                power = (BigDecimal) consMethodMap2.get(i).invoke(consCurve);
                                power2 = (BigDecimal) consMethodMap.get(i).invoke(consBaseline);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (ObjectUtil.isNotNull(power)) {

                                actualList.add(power);
                            } else {
                                actualList.add(BigDecimal.ZERO);
                            }
                            if (ObjectUtil.isNotNull(power2)) {
                                forecastList.add(power2);
                            } else {
                                forecastList.add(BigDecimal.ZERO);
                            }
                        }

                        if (CollectionUtil.hasNull(actualList) || CollectionUtil.hasNull(forecastList)) {
                            evaluTask.setEvaluNextdayDesc("实际负荷或者基线负荷不完整");
                            evaluTask.setEvaluNextdayStatus("3");
                            evaluTask.setEvaluNextdayId(evaluation.getEvaluationId());
                            evaluTaskUpdateList.add(evaluTask);
                            continue;
                        }

                        BigDecimal sumActual = actualList.stream().reduce(BigDecimal.ZERO, (d1, d2) -> {
                            return Optional.ofNullable(d1).orElse(BigDecimal.ZERO).add(Optional.ofNullable(d2).orElse(BigDecimal.ZERO));
                        });
                        BigDecimal minActual = actualList.stream().min(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
                        BigDecimal maxActual = actualList.stream().max(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
                        evaluation.setAvgLoadActual(NumberUtil.div(sumActual, actualList.size(), 2));
                        evaluation.setMaxLoadActual(maxActual);
                        evaluation.setMinLoadActual(minActual);

                        //是否越界
                        BigDecimal maxresult=evaluation.getMaxLoadBaseline().subtract(evaluation.getMaxLoadActual());
                        //是否合格
                        /*BigDecimal avgresult=evaluation.getAvgLoadBaseline().subtract(evaluation.getAvgLoadActual());
                        BigDecimal replyCapparam=evaluation.getReplyCap().multiply(new BigDecimal(0.8));
                        if(avgresult.compareTo(replyCapparam)<1)
                        {
                            evaluation.setIsQualified(YesOrNotEnum.N.getCode());
                        }
                        else {
                            evaluation.setIsQualified(YesOrNotEnum.Y.getCode());
                        }
    */
                        BigDecimal actualCap = NumberUtil.sub(evaluation.getAvgLoadBaseline(), evaluation.getAvgLoadActual());
                        /*if (BigDecimal.ZERO.compareTo(actualCap) > 0) {
                            actualCap = NumberUtil.mul(actualCap, -1);
                        }*/
                        if("2".equals(event.getResponseType())) {
                            actualCap = NumberUtil.mul(actualCap, -1);
                            maxresult = NumberUtil.mul(maxresult, -1);
                            //evaluation.setEffectiveTime(strategyUtils.calEeffectiveTime2(actualList, forecastList, eventInvitation.getReplyCap()) * 15);
                        }
                        if(maxresult.compareTo(evaluation.getReplyCap())<1)
                        {
                            evaluation.setIsOut(YesOrNotEnum.Y.getCode());
                        }
                        else {
                            evaluation.setIsOut(YesOrNotEnum.N.getCode());
                        }
                        evaluation.setActualCap(actualCap);
                        evaluation.setEffectiveTime(hour);
                        //evaluation.setIsEffective(strategyUtils.judgeEeffectiveNextOfRule(evaluation, evaluTask, event) ? YesOrNotEnum.Y.getCode() : YesOrNotEnum.N.getCode());
                        evaluation = strategyUtils.judgeEeffectiveNextOfRule(evaluation, evaluTask, event);
                        //负荷响应量为实际负荷占反馈响应量百分比
                        if(null==eventInvitation.getReplyCap() || eventInvitation.getReplyCap().compareTo(BigDecimal.ZERO)==0) {
                            evaluation.setExecuteRate(BigDecimal.ZERO);
                        } else {
                            evaluation.setExecuteRate(NumberUtil.div(actualCap,eventInvitation.getReplyCap()));
                        }
                        //核定响应量
                        if (evaluation.getIsEffective().equals(YesOrNotEnum.Y.getCode())) {
                            //如果实际响应量大于1.2倍反馈响应量，取1.2倍反馈响应量，否则取实际响应量
                            BigDecimal temp = strategyUtils.getConfirmCap(eventInvitation.getReplyCap(),actualCap);
                            evaluation.setConfirmCap(temp);
                            evaluation.setIsQualified(YesOrNotEnum.Y.getCode());
                            evaluation.setRemark("");
                            evaluTask.setEvaluNextdayStatus("2");
                            evaluTask.setEvaluNextdayDesc("");
                        } else {
                            //无效响应核定响应量为0
                            evaluation.setConfirmCap(new BigDecimal("0"));
                            evaluation.setIsQualified(YesOrNotEnum.N.getCode());
                            evaluation.setEffectiveTime(0);
                            //evaluation.setRemark("无效响应,核定响应量为0");
                            evaluTask.setEvaluNextdayStatus("2");
                            evaluTask.setEvaluNextdayDesc(evaluation.getRemark());
                        }
                        if (null == evaluations || evaluations.size() == 0) {
                            long id = IdWorker.getId();
                            evaluation.setEvaluationId(id);
                            evaluationInsertList.add(evaluation);
                        } else {
                            evaluationUpdateList.add(evaluation);
                        }
                        evaluTask.setEvaluNextdayId(evaluation.getEvaluationId());
                        evaluTaskUpdateList.add(evaluTask);
                    }
                    //更新
                    String url = dataurl;
                    String user = userName;
                    String password = datapassword;
                    //2）、加载驱动，不需要显示注册驱动
                    Class.forName(driver);
                    //更新同步邀约信息状态
                    String sql = "UPDATE dr_evalu_task set EVALU_NEXTDAY_STATUS=?,EVALU_NEXTDAY_ID=?,EVALU_NEXTDAY_DESC=?,UPDATE_TIME=? where id=?";
                    if (null != evaluTaskUpdateList && evaluTaskUpdateList.size() > 0) {
                        //获取数据库连接
                        conn = DriverManager.getConnection(url, user, password);
                        conn.setAutoCommit(false);
                        preparedStatement = conn.prepareStatement(sql);
                        int i = 0;
                        for (EvaluTask evaluTask : evaluTaskUpdateList) {
                            preparedStatement.setString(1, evaluTask.getEvaluNextdayStatus());
                            if(null!=evaluTask.getEvaluNextdayId()) {
                                preparedStatement.setLong(2, evaluTask.getEvaluNextdayId());
                            } else {
                                preparedStatement.setNull(2,Types.BIGINT);
                            }
                            if(null!=evaluTask.getEvaluNextdayDesc()) {
                                preparedStatement.setString(3, evaluTask.getEvaluNextdayDesc());
                            } else {
                                preparedStatement.setNull(3,Types.VARCHAR);
                            }
                            preparedStatement.setString(4, dateFormat.format(new Date()));
                            preparedStatement.setLong(5,evaluTask.getId());
                            preparedStatement.addBatch();
                            if ((i + 1) % 1000 == 0 || i == evaluTaskUpdateList.size() - 1) {
                                //每1000条提交一次
                                preparedStatement.executeBatch();
                                //清空记录
                                preparedStatement.clearBatch();
                            }
                            i++;
                        }
                    }
                    log.info("更新次日效果评估任务表完成，共:" + evaluTaskUpdateList.size() + "条");
                    String sql2 = "INSERT INTO dr_cons_evaluation (evaluation_id,cons_id,event_id,invitation_cap,reply_cap,actual_cap,confirm_cap,max_load_baseline,min_load_baseline,avg_load_baseline,max_load_actual,min_load_actual,avg_load_actual,is_effective,effective_time,is_qualified,is_out,create_time,join_user_type,remark,execute_rate)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    if (null !=evaluationInsertList  && evaluationInsertList.size() > 0) {
                        //获取数据库连接
                        conn2 = DriverManager.getConnection(url, user, password);
                        conn2.setAutoCommit(false);
                        preparedStatement2 = conn2.prepareStatement(sql2);
                        int i = 0;
                        for (ConsEvaluation evaluationImmediate1 : evaluationInsertList) {
                            preparedStatement2.setLong(1, evaluationImmediate1.getEvaluationId());
                            preparedStatement2.setString(2, evaluationImmediate1.getConsId());
                            preparedStatement2.setLong(3, evaluationImmediate1.getEventId());
                            preparedStatement2.setBigDecimal(4, evaluationImmediate1.getInvitationCap());
                            preparedStatement2.setBigDecimal(5, evaluationImmediate1.getReplyCap());
                            preparedStatement2.setBigDecimal(6, evaluationImmediate1.getActualCap());
                            preparedStatement2.setBigDecimal(7, evaluationImmediate1.getConfirmCap());
                            preparedStatement2.setBigDecimal(8, evaluationImmediate1.getMaxLoadBaseline());
                            preparedStatement2.setBigDecimal(9, evaluationImmediate1.getMinLoadBaseline());
                            preparedStatement2.setBigDecimal(10, evaluationImmediate1.getAvgLoadBaseline());
                            preparedStatement2.setBigDecimal(11, evaluationImmediate1.getMaxLoadActual());
                            preparedStatement2.setBigDecimal(12, evaluationImmediate1.getMinLoadActual());
                            preparedStatement2.setBigDecimal(13, evaluationImmediate1.getAvgLoadActual());
                            if(null!=evaluationImmediate1.getIsEffective()) {
                                preparedStatement2.setString(14, evaluationImmediate1.getIsEffective());
                            } else {
                                preparedStatement2.setNull(14,Types.VARCHAR);
                            }
                            if(null!=evaluationImmediate1.getEffectiveTime()) {
                                preparedStatement2.setInt(15, evaluationImmediate1.getEffectiveTime());
                            } else {
                                preparedStatement2.setNull(15,Types.INTEGER);
                            }
                            if(null!=evaluationImmediate1.getIsQualified()) {
                                preparedStatement2.setString(16, evaluationImmediate1.getIsQualified());
                            } else {
                                preparedStatement2.setNull(16, Types.VARCHAR);
                            }
                            if(null!=evaluationImmediate1.getIsOut()) {
                                preparedStatement2.setString(17, evaluationImmediate1.getIsOut());
                            } else {
                                preparedStatement2.setNull(17, Types.VARCHAR);
                            }
                            preparedStatement2.setString(18, dateFormat.format(new Date()));
                            if(null!=evaluationImmediate1.getJoinUserType()) {
                                preparedStatement2.setString(19, evaluationImmediate1.getJoinUserType());
                            } else {
                                preparedStatement2.setNull(19,Types.VARCHAR);
                            }
                            preparedStatement2.setString(20,evaluationImmediate1.getRemark());
                            preparedStatement2.setBigDecimal(21,evaluationImmediate1.getExecuteRate());
                            preparedStatement2.addBatch();
                            if ((i + 1) % 1000 == 0 || i == evaluationInsertList.size() - 1) {
                                //每1000条提交一次
                                preparedStatement2.executeBatch();
                                //清空记录
                                preparedStatement2.clearBatch();
                            }
                            i++;
                        }

                    }
                    log.info("保存次日效果评估数据完成，共:" + evaluationInsertList.size() + "条");
                    String sql3 = "update dr_cons_evaluation set cons_id=?, event_id=?, invitation_cap=?, reply_cap=?, actual_cap=?, confirm_cap=?, max_load_baseline=?, min_load_baseline=?, avg_load_baseline=?, max_load_actual=?, min_load_actual=?, avg_load_actual=?, is_effective=?, effective_time=?, is_qualified=?, is_out=?, update_time=?,join_user_type=?,remark=?,execute_rate=? where evaluation_id=?";
                    if (null !=evaluationUpdateList  && evaluationUpdateList.size() > 0) {
                        //获取数据库连接
                        conn3 = DriverManager.getConnection(url, user, password);
                        conn3.setAutoCommit(false);
                        preparedStatement3 = conn3.prepareStatement(sql3);
                        int i = 0;
                        for (ConsEvaluation evaluationImmediate1 : evaluationUpdateList) {
                            preparedStatement3.setString(1, evaluationImmediate1.getConsId());
                            preparedStatement3.setLong(2, evaluationImmediate1.getEventId());
                            preparedStatement3.setBigDecimal(3, evaluationImmediate1.getInvitationCap());
                            preparedStatement3.setBigDecimal(4, evaluationImmediate1.getReplyCap());
                            preparedStatement3.setBigDecimal(5, evaluationImmediate1.getActualCap());
                            preparedStatement3.setBigDecimal(6, evaluationImmediate1.getConfirmCap());
                            preparedStatement3.setBigDecimal(7, evaluationImmediate1.getMaxLoadBaseline());
                            preparedStatement3.setBigDecimal(8, evaluationImmediate1.getMinLoadBaseline());
                            preparedStatement3.setBigDecimal(9, evaluationImmediate1.getAvgLoadBaseline());
                            preparedStatement3.setBigDecimal(10, evaluationImmediate1.getMaxLoadActual());
                            preparedStatement3.setBigDecimal(11, evaluationImmediate1.getMinLoadActual());
                            preparedStatement3.setBigDecimal(12, evaluationImmediate1.getAvgLoadActual());
                            if(null!=evaluationImmediate1.getIsEffective()) {
                                preparedStatement3.setString(13, evaluationImmediate1.getIsEffective());
                            } else {
                                preparedStatement3.setNull(13,Types.VARCHAR);
                            }
                            if(null!=evaluationImmediate1.getEffectiveTime()) {
                                preparedStatement3.setInt(14, evaluationImmediate1.getEffectiveTime());
                            } else {
                                preparedStatement3.setNull(14,Types.INTEGER);
                            }
                            if(null!=evaluationImmediate1.getIsQualified()) {
                                preparedStatement3.setString(15, evaluationImmediate1.getIsQualified());
                            } else {
                                preparedStatement3.setNull(15, Types.VARCHAR);
                            }
                            if(null!=evaluationImmediate1.getIsOut()) {
                                preparedStatement3.setString(16, evaluationImmediate1.getIsOut());
                            } else {
                                preparedStatement3.setNull(16, Types.VARCHAR);
                            }
                            preparedStatement3.setString(17, dateFormat.format(new Date()));
                            if(null!=evaluationImmediate1.getJoinUserType()) {
                                preparedStatement3.setString(18, evaluationImmediate1.getJoinUserType());
                            } else {
                                preparedStatement3.setNull(18, Types.VARCHAR);
                            }
                            preparedStatement3.setString(19,evaluationImmediate1.getRemark());
                            preparedStatement3.setBigDecimal(20,evaluationImmediate1.getExecuteRate());
                            preparedStatement3.setLong(21,evaluationImmediate1.getEvaluationId());
                            preparedStatement3.addBatch();
                            if ((i + 1) % 1000 == 0 || i == evaluationUpdateList.size() - 1) {
                                //每1000条提交一次
                                preparedStatement3.executeBatch();
                                //清空记录
                                preparedStatement3.clearBatch();
                            }
                            i++;
                        }

                    }
                    log.info("更新次日效果评估数据完成，共:" + evaluationUpdateList.size() + "条");
                    String sql4 = "INSERT INTO dr_event_power_execute (\n" +
                            "cons_id,data_date,data_point_flag,p1,p2,p3, p4,p5,p6,p7,p8,p9,p10,p11,p12,p13,p14,p15,p16,p17,\n" +
                            "p18,p19,p20,p21,p22,p23,p24,p25,p26,p27,p28,p29,p30,p31,p32,p33,p34,p35,p36,p37,p38,p39,p40,p41,p42,p43,\n" +
                            "p44,p45,p46,p47,p48,p49,p50,p51,p52,p53,p54,p55,p56,p57,p58,p59,p60,p61,p62,p63,p64,p65,p66,p67,p68,p69,\n" +
                            "p70, p71, p72,p73,p74,p75,p76,p77,p78,p79,p80,p81,p82,p83,p84,p85,p86,p87,p88,p89,p90, p91,p92,p93,p94,p95,p96,event_id\n" +
                            ")VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    if (null != executeInertList && executeInertList.size() > 0) {
                        int k = 0;
                        //2）、加载驱动，不需要显示注册驱动
                        Class.forName(driver);
                        //获取数据库连接
                        conn4 = DriverManager.getConnection(url, user, password);
                        conn4.setAutoCommit(false);
                        preparedStatement4 = conn4.prepareStatement(sql4);
                        for (EventPowerExecute eventPowerExecuteImmediate : executeInertList) {
                            preparedStatement4.setString(1, eventPowerExecuteImmediate.getConsId());
                            preparedStatement4.setString(2, simpleDateFormat.format(eventPowerExecuteImmediate.getDataDate()));
                            preparedStatement4.setString(3, eventPowerExecuteImmediate.getDataPointFlag());
                            preparedStatement4.setBigDecimal(4, eventPowerExecuteImmediate.getP1());
                            preparedStatement4.setBigDecimal(5, eventPowerExecuteImmediate.getP2());
                            preparedStatement4.setBigDecimal(6, eventPowerExecuteImmediate.getP3());
                            preparedStatement4.setBigDecimal(7, eventPowerExecuteImmediate.getP4());
                            preparedStatement4.setBigDecimal(8, eventPowerExecuteImmediate.getP5());
                            preparedStatement4.setBigDecimal(9, eventPowerExecuteImmediate.getP6());
                            preparedStatement4.setBigDecimal(10, eventPowerExecuteImmediate.getP7());
                            preparedStatement4.setBigDecimal(11, eventPowerExecuteImmediate.getP8());
                            preparedStatement4.setBigDecimal(12, eventPowerExecuteImmediate.getP9());
                            preparedStatement4.setBigDecimal(13, eventPowerExecuteImmediate.getP10());
                            preparedStatement4.setBigDecimal(14, eventPowerExecuteImmediate.getP11());
                            preparedStatement4.setBigDecimal(15, eventPowerExecuteImmediate.getP12());
                            preparedStatement4.setBigDecimal(16, eventPowerExecuteImmediate.getP13());
                            preparedStatement4.setBigDecimal(17, eventPowerExecuteImmediate.getP14());
                            preparedStatement4.setBigDecimal(18, eventPowerExecuteImmediate.getP15());
                            preparedStatement4.setBigDecimal(19, eventPowerExecuteImmediate.getP16());
                            preparedStatement4.setBigDecimal(20, eventPowerExecuteImmediate.getP17());
                            preparedStatement4.setBigDecimal(21, eventPowerExecuteImmediate.getP18());
                            preparedStatement4.setBigDecimal(22, eventPowerExecuteImmediate.getP19());
                            preparedStatement4.setBigDecimal(23, eventPowerExecuteImmediate.getP20());
                            preparedStatement4.setBigDecimal(24, eventPowerExecuteImmediate.getP21());
                            preparedStatement4.setBigDecimal(25, eventPowerExecuteImmediate.getP22());
                            preparedStatement4.setBigDecimal(26, eventPowerExecuteImmediate.getP23());
                            preparedStatement4.setBigDecimal(27, eventPowerExecuteImmediate.getP24());
                            preparedStatement4.setBigDecimal(28, eventPowerExecuteImmediate.getP25());
                            preparedStatement4.setBigDecimal(29, eventPowerExecuteImmediate.getP26());
                            preparedStatement4.setBigDecimal(30, eventPowerExecuteImmediate.getP27());
                            preparedStatement4.setBigDecimal(31, eventPowerExecuteImmediate.getP28());
                            preparedStatement4.setBigDecimal(32, eventPowerExecuteImmediate.getP29());
                            preparedStatement4.setBigDecimal(33, eventPowerExecuteImmediate.getP30());
                            preparedStatement4.setBigDecimal(34, eventPowerExecuteImmediate.getP31());
                            preparedStatement4.setBigDecimal(35, eventPowerExecuteImmediate.getP32());
                            preparedStatement4.setBigDecimal(36, eventPowerExecuteImmediate.getP33());
                            preparedStatement4.setBigDecimal(37, eventPowerExecuteImmediate.getP34());
                            preparedStatement4.setBigDecimal(38, eventPowerExecuteImmediate.getP35());
                            preparedStatement4.setBigDecimal(39, eventPowerExecuteImmediate.getP36());
                            preparedStatement4.setBigDecimal(40, eventPowerExecuteImmediate.getP37());
                            preparedStatement4.setBigDecimal(41, eventPowerExecuteImmediate.getP38());
                            preparedStatement4.setBigDecimal(42, eventPowerExecuteImmediate.getP39());
                            preparedStatement4.setBigDecimal(43, eventPowerExecuteImmediate.getP40());
                            preparedStatement4.setBigDecimal(44, eventPowerExecuteImmediate.getP41());
                            preparedStatement4.setBigDecimal(45, eventPowerExecuteImmediate.getP42());
                            preparedStatement4.setBigDecimal(46, eventPowerExecuteImmediate.getP43());
                            preparedStatement4.setBigDecimal(47, eventPowerExecuteImmediate.getP44());
                            preparedStatement4.setBigDecimal(48, eventPowerExecuteImmediate.getP45());
                            preparedStatement4.setBigDecimal(49, eventPowerExecuteImmediate.getP46());
                            preparedStatement4.setBigDecimal(50, eventPowerExecuteImmediate.getP47());
                            preparedStatement4.setBigDecimal(51, eventPowerExecuteImmediate.getP48());
                            preparedStatement4.setBigDecimal(52, eventPowerExecuteImmediate.getP49());
                            preparedStatement4.setBigDecimal(53, eventPowerExecuteImmediate.getP50());
                            preparedStatement4.setBigDecimal(54, eventPowerExecuteImmediate.getP51());
                            preparedStatement4.setBigDecimal(55, eventPowerExecuteImmediate.getP52());
                            preparedStatement4.setBigDecimal(56, eventPowerExecuteImmediate.getP53());
                            preparedStatement4.setBigDecimal(57, eventPowerExecuteImmediate.getP54());
                            preparedStatement4.setBigDecimal(58, eventPowerExecuteImmediate.getP55());
                            preparedStatement4.setBigDecimal(59, eventPowerExecuteImmediate.getP56());
                            preparedStatement4.setBigDecimal(60, eventPowerExecuteImmediate.getP57());
                            preparedStatement4.setBigDecimal(61, eventPowerExecuteImmediate.getP58());
                            preparedStatement4.setBigDecimal(62, eventPowerExecuteImmediate.getP59());
                            preparedStatement4.setBigDecimal(63, eventPowerExecuteImmediate.getP60());
                            preparedStatement4.setBigDecimal(64, eventPowerExecuteImmediate.getP61());
                            preparedStatement4.setBigDecimal(65, eventPowerExecuteImmediate.getP62());
                            preparedStatement4.setBigDecimal(66, eventPowerExecuteImmediate.getP63());
                            preparedStatement4.setBigDecimal(67, eventPowerExecuteImmediate.getP64());
                            preparedStatement4.setBigDecimal(68, eventPowerExecuteImmediate.getP65());
                            preparedStatement4.setBigDecimal(69, eventPowerExecuteImmediate.getP66());
                            preparedStatement4.setBigDecimal(70, eventPowerExecuteImmediate.getP67());
                            preparedStatement4.setBigDecimal(71, eventPowerExecuteImmediate.getP68());
                            preparedStatement4.setBigDecimal(72, eventPowerExecuteImmediate.getP69());
                            preparedStatement4.setBigDecimal(73, eventPowerExecuteImmediate.getP70());
                            preparedStatement4.setBigDecimal(74, eventPowerExecuteImmediate.getP71());
                            preparedStatement4.setBigDecimal(75, eventPowerExecuteImmediate.getP72());
                            preparedStatement4.setBigDecimal(76, eventPowerExecuteImmediate.getP73());
                            preparedStatement4.setBigDecimal(77, eventPowerExecuteImmediate.getP74());
                            preparedStatement4.setBigDecimal(78, eventPowerExecuteImmediate.getP75());
                            preparedStatement4.setBigDecimal(79, eventPowerExecuteImmediate.getP76());
                            preparedStatement4.setBigDecimal(80, eventPowerExecuteImmediate.getP77());
                            preparedStatement4.setBigDecimal(81, eventPowerExecuteImmediate.getP78());
                            preparedStatement4.setBigDecimal(82, eventPowerExecuteImmediate.getP79());
                            preparedStatement4.setBigDecimal(83, eventPowerExecuteImmediate.getP80());
                            preparedStatement4.setBigDecimal(84, eventPowerExecuteImmediate.getP81());
                            preparedStatement4.setBigDecimal(85, eventPowerExecuteImmediate.getP82());
                            preparedStatement4.setBigDecimal(86, eventPowerExecuteImmediate.getP83());
                            preparedStatement4.setBigDecimal(87, eventPowerExecuteImmediate.getP84());
                            preparedStatement4.setBigDecimal(88, eventPowerExecuteImmediate.getP85());
                            preparedStatement4.setBigDecimal(89, eventPowerExecuteImmediate.getP86());
                            preparedStatement4.setBigDecimal(90, eventPowerExecuteImmediate.getP87());
                            preparedStatement4.setBigDecimal(91, eventPowerExecuteImmediate.getP88());
                            preparedStatement4.setBigDecimal(92, eventPowerExecuteImmediate.getP89());
                            preparedStatement4.setBigDecimal(93, eventPowerExecuteImmediate.getP90());
                            preparedStatement4.setBigDecimal(94, eventPowerExecuteImmediate.getP91());
                            preparedStatement4.setBigDecimal(95, eventPowerExecuteImmediate.getP92());
                            preparedStatement4.setBigDecimal(96, eventPowerExecuteImmediate.getP93());
                            preparedStatement4.setBigDecimal(97, eventPowerExecuteImmediate.getP94());
                            preparedStatement4.setBigDecimal(98, eventPowerExecuteImmediate.getP95());
                            preparedStatement4.setBigDecimal(99, eventPowerExecuteImmediate.getP96());
                            preparedStatement4.setLong(100, eventPowerExecuteImmediate.getEventId());
                            preparedStatement4.addBatch();
                            if ((k + 1) % 500 == 0 || k == executeInertList.size() - 1) {
                                //每1000条提交一次
                                preparedStatement4.executeBatch();
                                //清空记录
                                preparedStatement4.clearBatch();
                            }
                            k++;
                        }
                    }
                    log.info("保存用户执行曲线成功,共" + executeInertList.size() + "条");
                    String sql5 ="UPDATE dr_event_power_execute \n" +
                            "SET cons_id =?,data_date =?,data_point_flag =?,p1 =?,p2 =?,p3 =?,p4 =?,p5 =?,p6 =?,p7 =?,p8 =?,p9 =?,p10 =?,p11 =?,p12 =?,p13 =?,p14 =?,p15 =?,p16 =?,p17 =?,p18 =?,p19 =?,p20 =?,p21 =?,p22 =?,p23 =?,p24 =?,p25 =?,p26 =?,p27 =?,p28 =?,p29 =?,p30 =?,p31 =?,p32 =?,p33 =?,p34 =?,p35 =?,p36 =?,p37 =?,p38 =?,p39 =?,p40 =?,p41 =?,p42 =?,p43 =?,p44 =?,p45 =?,p46 =?,p47 =?,p48 =?,p49 =?,p50 =?,p51 =?,p52 =?,p53 =?,p54 =?,p55 =?,p56 =?,p57 =?,p58 =?,p59 =?,p60 =?,p61 =?,p62 =?,p63 =?,p64 =?,p65 =?,p66 =?,p67 =?,p68 =?,p69 =?,p70 =?,p71 =?,p72 =?,p73 =?,p74 =?,p75 =?,p76 =?,p77 =?,p78 =?,p79 =?,p80 =?,p81 =?,p82 =?,p83 =?,p84 =?,p85 =?,p86 =?,p87 =?,p88 =?,p89 =?,p90 =?,p91 =?,p92 =?,p93 =?,p94 =?,p95 =?,p96 =? \n" +
                            "WHERE\n" +
                            "\tdata_id =?";
                    //保存样本曲线
                    if(null!=executeUpdateList && executeUpdateList.size()>0) {
                        int j=0;
                        //2）、加载驱动，不需要显示注册驱动
                        Class.forName(driver);
                        //获取数据库连接
                        conn5= DriverManager.getConnection(url,user,password);
                        conn5.setAutoCommit(false);
                        preparedStatement5 = conn5.prepareStatement(sql5);
                        for(EventPowerExecute eventPowerExecuteImmediate : executeUpdateList) {
                            preparedStatement5.setString(1, eventPowerExecuteImmediate.getConsId());
                            preparedStatement5.setString(2, simpleDateFormat.format(eventPowerExecuteImmediate.getDataDate()));
                            preparedStatement5.setString(3, eventPowerExecuteImmediate.getDataPointFlag());
                            preparedStatement5.setBigDecimal(4, eventPowerExecuteImmediate.getP1());
                            preparedStatement5.setBigDecimal(5, eventPowerExecuteImmediate.getP2());
                            preparedStatement5.setBigDecimal(6, eventPowerExecuteImmediate.getP3());
                            preparedStatement5.setBigDecimal(7, eventPowerExecuteImmediate.getP4());
                            preparedStatement5.setBigDecimal(8, eventPowerExecuteImmediate.getP5());
                            preparedStatement5.setBigDecimal(9, eventPowerExecuteImmediate.getP6());
                            preparedStatement5.setBigDecimal(10, eventPowerExecuteImmediate.getP7());
                            preparedStatement5.setBigDecimal(11, eventPowerExecuteImmediate.getP8());
                            preparedStatement5.setBigDecimal(12, eventPowerExecuteImmediate.getP9());
                            preparedStatement5.setBigDecimal(13, eventPowerExecuteImmediate.getP10());
                            preparedStatement5.setBigDecimal(14, eventPowerExecuteImmediate.getP11());
                            preparedStatement5.setBigDecimal(15, eventPowerExecuteImmediate.getP12());
                            preparedStatement5.setBigDecimal(16, eventPowerExecuteImmediate.getP13());
                            preparedStatement5.setBigDecimal(17, eventPowerExecuteImmediate.getP14());
                            preparedStatement5.setBigDecimal(18, eventPowerExecuteImmediate.getP15());
                            preparedStatement5.setBigDecimal(19, eventPowerExecuteImmediate.getP16());
                            preparedStatement5.setBigDecimal(20, eventPowerExecuteImmediate.getP17());
                            preparedStatement5.setBigDecimal(21, eventPowerExecuteImmediate.getP18());
                            preparedStatement5.setBigDecimal(22, eventPowerExecuteImmediate.getP19());
                            preparedStatement5.setBigDecimal(23, eventPowerExecuteImmediate.getP20());
                            preparedStatement5.setBigDecimal(24, eventPowerExecuteImmediate.getP21());
                            preparedStatement5.setBigDecimal(25, eventPowerExecuteImmediate.getP22());
                            preparedStatement5.setBigDecimal(26, eventPowerExecuteImmediate.getP23());
                            preparedStatement5.setBigDecimal(27, eventPowerExecuteImmediate.getP24());
                            preparedStatement5.setBigDecimal(28, eventPowerExecuteImmediate.getP25());
                            preparedStatement5.setBigDecimal(29, eventPowerExecuteImmediate.getP26());
                            preparedStatement5.setBigDecimal(30, eventPowerExecuteImmediate.getP27());
                            preparedStatement5.setBigDecimal(31, eventPowerExecuteImmediate.getP28());
                            preparedStatement5.setBigDecimal(32, eventPowerExecuteImmediate.getP29());
                            preparedStatement5.setBigDecimal(33, eventPowerExecuteImmediate.getP30());
                            preparedStatement5.setBigDecimal(34, eventPowerExecuteImmediate.getP31());
                            preparedStatement5.setBigDecimal(35, eventPowerExecuteImmediate.getP32());
                            preparedStatement5.setBigDecimal(36, eventPowerExecuteImmediate.getP33());
                            preparedStatement5.setBigDecimal(37, eventPowerExecuteImmediate.getP34());
                            preparedStatement5.setBigDecimal(38, eventPowerExecuteImmediate.getP35());
                            preparedStatement5.setBigDecimal(39, eventPowerExecuteImmediate.getP36());
                            preparedStatement5.setBigDecimal(40, eventPowerExecuteImmediate.getP37());
                            preparedStatement5.setBigDecimal(41, eventPowerExecuteImmediate.getP38());
                            preparedStatement5.setBigDecimal(42, eventPowerExecuteImmediate.getP39());
                            preparedStatement5.setBigDecimal(43, eventPowerExecuteImmediate.getP40());
                            preparedStatement5.setBigDecimal(44, eventPowerExecuteImmediate.getP41());
                            preparedStatement5.setBigDecimal(45, eventPowerExecuteImmediate.getP42());
                            preparedStatement5.setBigDecimal(46, eventPowerExecuteImmediate.getP43());
                            preparedStatement5.setBigDecimal(47, eventPowerExecuteImmediate.getP44());
                            preparedStatement5.setBigDecimal(48, eventPowerExecuteImmediate.getP45());
                            preparedStatement5.setBigDecimal(49, eventPowerExecuteImmediate.getP46());
                            preparedStatement5.setBigDecimal(50, eventPowerExecuteImmediate.getP47());
                            preparedStatement5.setBigDecimal(51, eventPowerExecuteImmediate.getP48());
                            preparedStatement5.setBigDecimal(52, eventPowerExecuteImmediate.getP49());
                            preparedStatement5.setBigDecimal(53, eventPowerExecuteImmediate.getP50());
                            preparedStatement5.setBigDecimal(54, eventPowerExecuteImmediate.getP51());
                            preparedStatement5.setBigDecimal(55, eventPowerExecuteImmediate.getP52());
                            preparedStatement5.setBigDecimal(56, eventPowerExecuteImmediate.getP53());
                            preparedStatement5.setBigDecimal(57, eventPowerExecuteImmediate.getP54());
                            preparedStatement5.setBigDecimal(58, eventPowerExecuteImmediate.getP55());
                            preparedStatement5.setBigDecimal(59, eventPowerExecuteImmediate.getP56());
                            preparedStatement5.setBigDecimal(60, eventPowerExecuteImmediate.getP57());
                            preparedStatement5.setBigDecimal(61, eventPowerExecuteImmediate.getP58());
                            preparedStatement5.setBigDecimal(62, eventPowerExecuteImmediate.getP59());
                            preparedStatement5.setBigDecimal(63, eventPowerExecuteImmediate.getP60());
                            preparedStatement5.setBigDecimal(64, eventPowerExecuteImmediate.getP61());
                            preparedStatement5.setBigDecimal(65, eventPowerExecuteImmediate.getP62());
                            preparedStatement5.setBigDecimal(66, eventPowerExecuteImmediate.getP63());
                            preparedStatement5.setBigDecimal(67, eventPowerExecuteImmediate.getP64());
                            preparedStatement5.setBigDecimal(68, eventPowerExecuteImmediate.getP65());
                            preparedStatement5.setBigDecimal(69, eventPowerExecuteImmediate.getP66());
                            preparedStatement5.setBigDecimal(70, eventPowerExecuteImmediate.getP67());
                            preparedStatement5.setBigDecimal(71, eventPowerExecuteImmediate.getP68());
                            preparedStatement5.setBigDecimal(72, eventPowerExecuteImmediate.getP69());
                            preparedStatement5.setBigDecimal(73, eventPowerExecuteImmediate.getP70());
                            preparedStatement5.setBigDecimal(74, eventPowerExecuteImmediate.getP71());
                            preparedStatement5.setBigDecimal(75, eventPowerExecuteImmediate.getP72());
                            preparedStatement5.setBigDecimal(76, eventPowerExecuteImmediate.getP73());
                            preparedStatement5.setBigDecimal(77, eventPowerExecuteImmediate.getP74());
                            preparedStatement5.setBigDecimal(78, eventPowerExecuteImmediate.getP75());
                            preparedStatement5.setBigDecimal(79, eventPowerExecuteImmediate.getP76());
                            preparedStatement5.setBigDecimal(80, eventPowerExecuteImmediate.getP77());
                            preparedStatement5.setBigDecimal(81, eventPowerExecuteImmediate.getP78());
                            preparedStatement5.setBigDecimal(82, eventPowerExecuteImmediate.getP79());
                            preparedStatement5.setBigDecimal(83, eventPowerExecuteImmediate.getP80());
                            preparedStatement5.setBigDecimal(84, eventPowerExecuteImmediate.getP81());
                            preparedStatement5.setBigDecimal(85, eventPowerExecuteImmediate.getP82());
                            preparedStatement5.setBigDecimal(86, eventPowerExecuteImmediate.getP83());
                            preparedStatement5.setBigDecimal(87, eventPowerExecuteImmediate.getP84());
                            preparedStatement5.setBigDecimal(88, eventPowerExecuteImmediate.getP85());
                            preparedStatement5.setBigDecimal(89, eventPowerExecuteImmediate.getP86());
                            preparedStatement5.setBigDecimal(90, eventPowerExecuteImmediate.getP87());
                            preparedStatement5.setBigDecimal(91, eventPowerExecuteImmediate.getP88());
                            preparedStatement5.setBigDecimal(92, eventPowerExecuteImmediate.getP89());
                            preparedStatement5.setBigDecimal(93, eventPowerExecuteImmediate.getP90());
                            preparedStatement5.setBigDecimal(94, eventPowerExecuteImmediate.getP91());
                            preparedStatement5.setBigDecimal(95, eventPowerExecuteImmediate.getP92());
                            preparedStatement5.setBigDecimal(96, eventPowerExecuteImmediate.getP93());
                            preparedStatement5.setBigDecimal(97, eventPowerExecuteImmediate.getP94());
                            preparedStatement5.setBigDecimal(98, eventPowerExecuteImmediate.getP95());
                            preparedStatement5.setBigDecimal(99, eventPowerExecuteImmediate.getP96());
                            preparedStatement5.setLong(100, eventPowerExecuteImmediate.getDataId());
                            preparedStatement5.addBatch();
                            if((j+1)%500 == 0 || j == executeUpdateList.size()-1) {
                                //每1000条提交一次
                                preparedStatement5.executeBatch();
                                //清空记录
                                preparedStatement5.clearBatch();
                            }
                            j++;
                        }
                    }
                    log.info("更新执行曲线成功,共"+executeUpdateList.size()+"条");
                    if (null != conn) {
                        conn.commit();
                    }
                    if (null != conn2) {
                        conn2.commit();
                    }
                    if (null != conn3) {
                        conn3.commit();
                    }
                    if (null != conn4) {
                        conn4.commit();
                    }
                    if (null != conn5) {
                        conn5.commit();
                    }
                    log.info("用户次日效果评估完成!");
                } catch (Exception e) {
                    try {
                        if (null != conn) {
                            conn.rollback();
                        }
                        if (null != conn2) {
                            conn2.rollback();
                        }
                        if (null != conn3) {
                            conn3.rollback();
                        }
                        if (null != conn4) {
                            conn4.rollback();
                        }
                        if (null != conn5) {
                            conn5.rollback();
                        }
                    } catch (SQLException sqlException) {
                        sqlException.printStackTrace();
                    }
                    e.printStackTrace();
                } finally {
                    if (!"04".equals(status)) {
                        event.setEventStatus("04");
                    }
                    Integer count = event.getNextdayCount();
                    if(null==count) {
                        count = 0;
                    }
                    count = count + 1;
                    event.setNextdayCount(count);
                    eventService.updateById(event);
                    if (null != conn) {
                        try {
                            conn.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    if (null != preparedStatement) {
                        try {
                            preparedStatement.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                    }
                    if (null != conn2) {
                        try {
                            conn2.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    if (null != preparedStatement2) {
                        try {
                            preparedStatement2.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                    }
                    if (null != conn3) {
                        try {
                            conn3.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    if (null != preparedStatement3) {
                        try {
                            preparedStatement3.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                    }
                    if (null != conn4) {
                        try {
                            conn4.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    if (null != preparedStatement4) {
                        try {
                            preparedStatement4.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                    }
                    if (null != conn5) {
                        try {
                            conn5.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    if (null != preparedStatement5) {
                        try {
                            preparedStatement5.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        };
        return runnable;
    }

    /**
     * 客户次日事件效果评估
     */
    public void executeCustEvaluationList(String param) {
        log.info(">>> 客户次日事件效果评估");
        // 1.查询当日效果评估任务
        LocalDate lastDay = LocalDate.now().minusDays(1);
        EvaluCustTaskParam evaluTaskParam = new EvaluCustTaskParam();
        if(null!=param && !"".equals(param)) {
            if(StrategyUtils.isDate(param)) {
                evaluTaskParam.setRegulateDateStr(param);
                evaluTaskParam.setRegulateDate(LocalDate.parse(param));
            } else {
                evaluTaskParam.setRegulateDateStr(simpleDateFormat.format(lastDay));
                evaluTaskParam.setRegulateDate(lastDay);
            }
        } else {
            evaluTaskParam.setRegulateDateStr(simpleDateFormat.format(lastDay));
            evaluTaskParam.setRegulateDate(lastDay);
        }
        evaluTaskParam.setEvaluNextdayStatus("1");
        List<EvaluCustTask>  evaluTasks = null;
        evaluTasks = evaluCustTaskService.list(evaluTaskParam);
        // 2.循环对事件进行评价
        if (CollectionUtil.isEmpty(evaluTasks)) {
            log.info("无效果评估任务!");
            return;
        }
        //查询事件信息
        LambdaQueryWrapper<Event> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Event::getRegulateDate, evaluTaskParam.getRegulateDate());
        lambdaQueryWrapper.eq(Event::getTimeType, "1");
        List<String> statusList = new ArrayList<>();
        statusList.add("03");
        statusList.add("04");
        lambdaQueryWrapper.in(Event::getEventStatus,statusList);
        List<Event> eventList = eventService.list(lambdaQueryWrapper);
        List<Long> eventIds = new ArrayList<>();
        if(null!=eventList && eventList.size()>0) {
            for(Event event : eventList) {
                eventIds.add(event.getEventId());
            }
        } else {
            log.info("无事件信息!");
            return;
        }
        ExecutorService executor = ExecutorBuilder.create()
                .setCorePoolSize(corePoolSize)
                .setMaxPoolSize(maxPoolSize)
                .setWorkQueue(new LinkedBlockingQueue<>(workQueue))
                .build();
        executor.execute(this.executeCustEvaluationList(evaluTasks,evaluTaskParam,eventIds));
        executor.shutdown();
    }

    public Runnable executeCustEvaluationList(List<EvaluCustTask>  evaluTasks,EvaluCustTaskParam evaluTaskParam,List<Long> eventIds) {
        LocalDate localDate = LocalDate.now();
        LocalTime localTime = LocalTime.now();
        Map<Integer, Method> consMethodMap = new HashMap<>();
        StrategyUtils strategyUtils = new StrategyUtils();
        try{
            for (int j=1; j<=96; j++){
                consMethodMap.put(j, CustBaseLineDetail.class.getMethod("getP"+j));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                long eventId = evaluTasks.get(0).getEventId();
                Event event = eventService.getById(eventId);
                if(null==event) {
                    log.info("无事件信息");
                    return;
                }
                PreparedStatement preparedStatement = null;
                Connection conn = null;
                PreparedStatement preparedStatement2 = null;
                Connection conn2 = null;
                PreparedStatement preparedStatement3 = null;
                Connection conn3 = null;
                try {
                    Long baselinId= event.getBaselinId();
                    BaseLine baseLine = baseLineService.getById(baselinId);
                    String sampleDates = null;
                    if(null!=baseLine) {
                        sampleDates = baseLine.getSimplesDate();
                    } else {
                        log.info("基线库为空");
                        return;
                    }
                    //List<String> sampleDateList = Arrays.asList(sampleDates.split(","));
                    //String status = event.getEventStatus();
                    // 判断事件是否已经截至
                    Integer endHour = 0;
                    Integer endMinute = 0;
                    endHour = Integer.parseInt(event.getEndTime().substring(0, 2));
                    endMinute = Integer.parseInt(event.getEndTime().substring(3));
                    LocalTime endTime = LocalTime.of(endHour, endMinute);
                    // 事件开始点和事件结束点
                    int startP = CurveUtil.covDateTimeToPoint(event.getStartTime());
                    int endP = CurveUtil.covDateTimeToPoint(event.getEndTime());
                    int hour = (endP-startP)*15;
                    if(evaluTaskParam.getRegulateDate().compareTo(localDate)==0) {
                        if (LocalTime.now().isBefore(endTime)) {
                            // 事件未截止，当前事件不用进行评价了
                            log.info(">>> 事件未截止，事件ID:{}", eventId);
                            return;
                        }
                    } else if(evaluTaskParam.getRegulateDate().compareTo(localDate)>0) {
                        // 事件未截止，当前事件不用进行评价了
                        log.info(">>> 事件未截止，事件ID:{}", eventId);
                        return;
                    }
                    //查询事件参与所有客户
                    List<CustInvitation> custInvitations = custInvitationService.getConsInfoByEvent(eventId);
                    if (null == custInvitations || custInvitations.size() == 0) {
                        log.info("无客户邀约信息");
                        return;
                    }
                    CustInvitation eventInvitation = null;
                    LambdaQueryWrapper<Plan> queryWrapperPlan = new LambdaQueryWrapper<>();
                    queryWrapperPlan.eq(Plan::getRegulateId,eventId);
                    List<Plan> planList = planService.list(queryWrapperPlan);
                    if (null == planList || planList.size() == 0) {
                        log.info("无方案信息");
                        return;
                    }
                    Plan plan = null;
                    //查询存在次日评估有效用户的客户
                    List<Long> custIds = evaluationImmediateService.getNextDayCustIdsByEventId(eventId);
                    Integer counts = evaluationImmediateService.getNextDayCountByEventId(eventId);
                    //获取所有用户基线
                    //List<CustBaseLineDetail> custBaselineList = custBaseLineMapper.getCustBaseLineInfo();
                    LambdaQueryWrapper<CustBaseLineDetail> queryWrapperBase = new LambdaQueryWrapper<>();
                    queryWrapperBase.eq(CustBaseLineDetail::getBaselineLibId,planList.get(0).getBaselinId());
                    List<CustBaseLineDetail> custBaselineList = custBaseLineDetailService.list(queryWrapperBase);
                    List<CustBaseLineDetail> custBaselines = null;
                    //获取今日效果评估数据
                    LambdaQueryWrapper<CustEvaluation> queryWrapper = new LambdaQueryWrapper<>();
                    queryWrapper.in(CustEvaluation::getEventId, eventIds);
                    List<CustEvaluation> evaluationList = custEvaluationService.list(queryWrapper);
                    CustEvaluation evaluation = null;
                    List<String> regulateList = new ArrayList<>();
                    String regulateDate = simpleDateFormat.format(event.getRegulateDate());
                    regulateList.add(regulateDate);
                    List<String> conIds =evaluCustTaskService.getConsIdByPlanId(planList.get(0).getPlanId());
                    //查询客户历史曲线
                    List<CustBaseLineDetail> consCurveHistorys =  custBaseLineMapper.getCustCurve(regulateList,conIds);
                    //查询客户实时曲线
                    List<CustBaseLineDetail> consCurveTodays = custBaseLineMapper.getCustCurveToday(regulateList,conIds);
                    List<EvaluCustTask> evaluTaskUpdateList = new ArrayList<>();
                    List<CustEvaluation> evaluationUpdateList = new ArrayList<>();
                    List<CustEvaluation> evaluationInsertList = new ArrayList<>();
                    for (EvaluCustTask evaluTask : evaluTasks) {
                        List<CustInvitation> eventInvitations = custInvitations.stream().filter(custInvitation ->
                                custInvitation.getEventId().equals(evaluTask.getEventId()) && custInvitation.getCustId().equals(evaluTask.getCustId())
                        ).collect(Collectors.toList());
                        if (null == eventInvitations || eventInvitations.size() == 0) {
                            evaluTask.setEvaluNextdayDesc("无客户邀约信息");
                            evaluTask.setEvaluNextdayStatus("3");
                            evaluTaskUpdateList.add(evaluTask);
                            continue;
                        } else {
                            eventInvitation = eventInvitations.get(0);
                            if (null == eventInvitation || null == eventInvitation.getCustId()) {
                                evaluTask.setEvaluNextdayDesc("无客户邀约信息");
                                evaluTask.setEvaluNextdayStatus("3");
                                evaluTaskUpdateList.add(evaluTask);
                                continue;
                            }
                        }
                        List<CustEvaluation> evaluations = null;
                        if (null != evaluTask.getCustId() && null != evaluationList && evaluationList.size() > 0) {
                            evaluations = evaluationList.stream().filter(eva -> eva.getCustId().equals(evaluTask.getCustId())
                                    && eva.getEventId().equals(evaluTask.getEventId())
                            ).collect(Collectors.toList());
                        }
                        if (null == evaluations || evaluations.size() == 0) {
                            //定义响应效果评估
                            evaluation = new CustEvaluation();
                            evaluation.setEventId(evaluTask.getEventId());
                            evaluation.setCustId(evaluTask.getCustId());
                        } else {
                            evaluation = evaluations.get(0);
                        }
                        //获取需求响应方案
                        List<Plan> plans = planList.stream().filter(plan1 -> plan1.getRegulateId().equals(eventId))
                                .collect(Collectors.toList());
                        if (null != plans && plans.size() > 0) {
                            plan = plans.get(0);
                        }
                        evaluation.setInvitationCap(eventInvitation.getInvitationCap());
                        evaluation.setReplyCap(eventInvitation.getReplyCap());
                        if("0".equals(eventInvitation.getIntegrator())) {
                            //直接客户如果有效果评估有效用户，直接有效
                            if(null!=custIds && custIds.size()>0) {
                                if(custIds.contains(eventInvitation.getCustId())) {
                                    evaluTask.setEvaluNextdayDesc("直接客户存在有效用户，直接设置为有效");
                                    evaluTask.setEvaluNextdayStatus("2");
                                    evaluation.setRemark("直接客户存在有效用户，直接设置为有效");
                                    evaluation.setEffectiveTime(hour);
                                    evaluation.setIsEffective(YesOrNotEnum.Y.getCode());
                                    evaluation.setActualCap(eventInvitation.getReplyCap());
                                    evaluation.setConfirmCap(eventInvitation.getReplyCap());
                                    if (null == evaluations || evaluations.size() == 0) {
                                        long id = IdWorker.getId();
                                        evaluation.setEvaluationId(id);
                                        evaluationInsertList.add(evaluation);
                                    } else {
                                        evaluationUpdateList.add(evaluation);
                                    }
                                    evaluTask.setEvaluNextdayId(evaluation.getEvaluationId());
                                    evaluTaskUpdateList.add(evaluTask);
                                    evaluation.setIsEffective("Y");
                                } else {
                                    evaluTask.setEvaluNextdayDesc("直接客户无有效用户，直接设置为无效");
                                    evaluTask.setEvaluNextdayStatus("2");
                                    evaluation.setRemark("直接客户无有效用户，直接设置为无效");
                                    evaluation.setEffectiveTime(0);
                                    evaluation.setIsEffective(YesOrNotEnum.N.getCode());
                                    evaluation.setActualCap(eventInvitation.getReplyCap());
                                    evaluation.setConfirmCap(eventInvitation.getReplyCap());
                                    if (null == evaluations || evaluations.size() == 0) {
                                        long id = IdWorker.getId();
                                        evaluation.setEvaluationId(id);
                                        evaluationInsertList.add(evaluation);
                                    } else {
                                        evaluationUpdateList.add(evaluation);
                                    }
                                    evaluTask.setEvaluNextdayId(evaluation.getEvaluationId());
                                    evaluTaskUpdateList.add(evaluTask);
                                    evaluation.setIsEffective("N");
                                }
                            } else {
                                if(counts>0) {
                                    evaluTask.setEvaluNextdayDesc("直接客户无有效用户，直接设置为无效");
                                    evaluTask.setEvaluNextdayStatus("2");
                                    evaluation.setRemark("直接客户无有效用户，直接设置为无效");
                                    evaluation.setEffectiveTime(0);
                                    evaluation.setIsEffective(YesOrNotEnum.N.getCode());
                                    evaluation.setActualCap(eventInvitation.getReplyCap());
                                    evaluation.setConfirmCap(eventInvitation.getReplyCap());
                                    if (null == evaluations || evaluations.size() == 0) {
                                        long id = IdWorker.getId();
                                        evaluation.setEvaluationId(id);
                                        evaluationInsertList.add(evaluation);
                                    } else {
                                        evaluationUpdateList.add(evaluation);
                                    }
                                    evaluTask.setEvaluNextdayId(evaluation.getEvaluationId());
                                    evaluTaskUpdateList.add(evaluTask);
                                    evaluation.setIsEffective("N");
                                }
                            }
                        } else {
                            //查询客户基线
                            CustBaseLineDetail custCurveBase = null;
                            if (null != plan && null != plan.getBaselinId()) {
                                custBaselines = custBaselineList.stream().filter(consBaseline1 -> consBaseline1.getCustId().equals(evaluTask.getCustId())
                                        && consBaseline1.getBaselineLibId().equals(plans.get(0).getBaselinId())
                                ).collect(Collectors.toList());
                                if (null == custBaselines || custBaselines.size() == 0) {
                                    evaluTask.setEvaluNextdayDesc("基线为空，直接设置为有效");
                                    evaluTask.setEvaluNextdayStatus("2");
                                    evaluation.setIsEffective(YesOrNotEnum.Y.getCode());
                                    evaluation.setEffectiveTime(hour);
                                    evaluation.setActualCap(eventInvitation.getReplyCap());
                                    evaluation.setConfirmCap(eventInvitation.getReplyCap());
                                    evaluation.setRemark("基线为空，直接设置为有效");
                                    if (null == evaluations || evaluations.size() == 0) {
                                        long id = IdWorker.getId();
                                        evaluation.setEvaluationId(id);
                                        evaluationInsertList.add(evaluation);
                                    } else {
                                        evaluationUpdateList.add(evaluation);
                                    }
                                    evaluTask.setEvaluNextdayId(evaluation.getEvaluationId());
                                    evaluTaskUpdateList.add(evaluTask);
                                    continue;
                                } else {
                                    custCurveBase = custBaselines.get(0);
                                }
                            } else {
                                evaluTask.setEvaluNextdayDesc("需求响应方案不存在");
                                evaluTask.setEvaluNextdayStatus("3");
                                evaluTaskUpdateList.add(evaluTask);
                                continue;
                            }
                            evaluation.setAvgLoadBaseline(custCurveBase.getAvgLoadBaseline());
                            evaluation.setMaxLoadBaseline(custCurveBase.getMaxLoadBaseline());
                            evaluation.setMinLoadBaseline(custCurveBase.getMinLoadBaseline());
                            CustBaseLineDetail consCurve = null;
                            if(localDate.compareTo(evaluTask.getRegulateDate())==0) {
                                if (null != consCurveTodays && consCurveTodays.size() > 0) {
                                    List<CustBaseLineDetail> curveTodays = consCurveTodays.stream().filter(consCurveToday -> consCurveToday.getCustId().equals(evaluTask.getCustId())
                                            && consCurveToday.getSimplesDate().equals(regulateDate))
                                            .collect(Collectors.toList());
                                    if (null != curveTodays && curveTodays.size() > 0) {
                                        consCurve = new CustBaseLineDetail();
                                        BeanUtils.copyProperties(curveTodays.get(0), consCurve);
                                    }
                                }
                            } else {
                                if (null != consCurveHistorys && consCurveHistorys.size() > 0) {
                                    List<CustBaseLineDetail> consCurves = consCurveHistorys.stream().filter(consCurveHis -> consCurveHis.getCustId().equals(evaluTask.getCustId())
                                            && consCurveHis.getSimplesDate().equals(regulateDate)).collect(Collectors.toList());
                                    if (null != consCurves && consCurves.size() > 0) {
                                        consCurve = consCurves.get(0);
                                    }
                                }
                            }
                            LocalTime futureFif = LocalTime.of(9, 45);
                            if (strategyUtils.judgeIsAllNull2(consCurve, startP, endP)) {
                                // 判断曲线是否为空
                                if (localTime.isAfter(futureFif)) {
                                    // 如果到了9点45还没有历史负荷，直接设置为有效
                                    evaluation.setIsEffective(YesOrNotEnum.Y.getCode());
                                    evaluation.setEffectiveTime(hour);
                                    evaluation.setActualCap(eventInvitation.getReplyCap());
                                    evaluation.setConfirmCap(eventInvitation.getReplyCap());
                                    evaluation.setRemark("9点45还没有历史负荷，直接设置为有效");
                                    if (null == evaluations || evaluations.size() == 0) {
                                        long id = IdWorker.getId();
                                        evaluation.setEvaluationId(id);
                                        evaluationInsertList.add(evaluation);
                                    } else {
                                        evaluationUpdateList.add(evaluation);
                                    }
                                    evaluTask.setEvaluNextdayDesc("9点45还没有历史负荷，直接设置为有效");
                                    evaluTask.setEvaluNextdayStatus("2");
                                    evaluTask.setEvaluNextdayId(evaluation.getEvaluationId());
                                    evaluTaskUpdateList.add(evaluTask);
                                    continue;
                                }
                                evaluTask.setEvaluNextdayDesc("客户负荷曲线不存在");
                                evaluTask.setEvaluNextdayStatus("3");
                                evaluTask.setEvaluNextdayId(evaluation.getEvaluationId());
                                evaluTaskUpdateList.add(evaluTask);
                                continue;
                            }
                            List<BigDecimal> actualList = new ArrayList<>();
                            List<BigDecimal> forecastList = new ArrayList<>();
                            if (localTime.isBefore(futureFif)) {
                                // 事件结束45分钟以内
                                BigDecimal endValue =null;
                                try {
                                    endValue = (BigDecimal) consMethodMap.get(endP).invoke(consCurve);
                                } catch (Exception e) {
                                    e.getMessage();
                                }
                                if (ObjectUtil.isNull(endValue) || BigDecimal.ZERO.compareTo(endValue) == 0) {
                                    // 事件最后一个点为0，直接设置为无效
                                    if (null == evaluations || evaluations.size() == 0) {
                                        long id = IdWorker.getId();
                                        evaluation.setEvaluationId(id);
                                        evaluationInsertList.add(evaluation);
                                    } else {
                                        evaluationUpdateList.add(evaluation);
                                    }
                                    evaluTask.setEvaluNextdayDesc("实际负荷最后点为0值");
                                    evaluTask.setEvaluNextdayStatus("3");
                                    evaluTask.setEvaluNextdayId(evaluation.getEvaluationId());
                                    evaluTaskUpdateList.add(evaluTask);
                                    continue;
                                }
                            }

                            for (int i = startP; i <= endP; i++) {
                                BigDecimal power = null;
                                BigDecimal power2 = null;
                                try {
                                    power = (BigDecimal) consMethodMap.get(i).invoke(consCurve);
                                    power2 = (BigDecimal) consMethodMap.get(i).invoke(custCurveBase);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                if (ObjectUtil.isNotNull(power)) {

                                    actualList.add(power);
                                } else {
                                    actualList.add(BigDecimal.ZERO);
                                }
                                if (ObjectUtil.isNotNull(power2)) {
                                    forecastList.add(power2);
                                } else {
                                    forecastList.add(BigDecimal.ZERO);
                                }
                            }

                            if (CollectionUtil.hasNull(actualList) || CollectionUtil.hasNull(forecastList)) {
                                if (null == evaluations || evaluations.size() == 0) {
                                    long id = IdWorker.getId();
                                    evaluation.setEvaluationId(id);
                                    evaluationInsertList.add(evaluation);
                                } else {
                                    evaluationUpdateList.add(evaluation);
                                }
                                evaluTask.setEvaluNextdayDesc("实际负荷或者基线负荷不完整");
                                evaluTask.setEvaluNextdayStatus("3");
                                evaluTask.setEvaluNextdayId(evaluation.getEvaluationId());
                                evaluTaskUpdateList.add(evaluTask);
                                continue;
                            }

                            BigDecimal sumActual = actualList.stream().reduce(BigDecimal.ZERO, (d1, d2) -> {
                                return Optional.ofNullable(d1).orElse(BigDecimal.ZERO).add(Optional.ofNullable(d2).orElse(BigDecimal.ZERO));
                            });
                            BigDecimal minActual = actualList.stream().min(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
                            BigDecimal maxActual = actualList.stream().max(BigDecimal::compareTo).orElse(BigDecimal.ZERO);

                            evaluation.setAvgLoadActual(NumberUtil.div(sumActual, actualList.size(), 2));
                            evaluation.setMaxLoadActual(maxActual);
                            evaluation.setMinLoadActual(minActual);

                            BigDecimal actualCap = NumberUtil.sub(evaluation.getAvgLoadBaseline(), evaluation.getAvgLoadActual());
                        /*if(BigDecimal.ZERO.compareTo(actualCap)>0) {
                            actualCap = NumberUtil.mul(actualCap,-1);
                        }*/
                            if("2".equals(event.getResponseType())) {
                                actualCap = NumberUtil.mul(actualCap, -1);
                                //evaluation.setEffectiveTime(strategyUtils.calEeffectiveTime2(actualList, forecastList, eventInvitation.getReplyCap()) * 15);
                            }
                            evaluation.setActualCap(actualCap);
                            evaluation.setEffectiveTime(hour);
                            evaluation = strategyUtils.judgeCustEffectiveNextOfRule(evaluation, evaluTask,event);
                            //evaluation.setIsEffective(strategyUtils.judgeCustEffectiveNextOfRule(evaluation, evaluTask,event) ? YesOrNotEnum.Y.getCode() : YesOrNotEnum.N.getCode());
                            //负荷响应量为实际负荷占反馈响应量百分比
                            if(null==eventInvitation.getReplyCap() || eventInvitation.getReplyCap().compareTo(BigDecimal.ZERO)==0) {
                                evaluation.setExecuteRate(BigDecimal.ZERO);
                            } else {
                                evaluation.setExecuteRate(NumberUtil.div(actualCap,eventInvitation.getReplyCap()));
                            }
                            //核定响应量
                            if (evaluation.getIsEffective().equals(YesOrNotEnum.Y.getCode())) {
                                //如果实际响应量大于1.2倍反馈响应量，取1.2倍反馈响应量，否则取实际响应量
                                BigDecimal temp = strategyUtils.getConfirmCap(eventInvitation.getReplyCap(),actualCap);
                                evaluation.setConfirmCap(temp);
                                evaluTask.setEvaluNextdayStatus("2");
                                evaluTask.setEvaluNextdayDesc("");
                            } else {
                                //无效响应核定响应量为0
                                evaluation.setConfirmCap(new BigDecimal("0"));
                                evaluation.setEffectiveTime(0);
                                evaluTask.setEvaluNextdayStatus("2");
                                evaluTask.setEvaluNextdayDesc(evaluation.getRemark());
                            }
                            if (null == evaluations || evaluations.size() == 0) {
                                long id = IdWorker.getId();
                                evaluation.setEvaluationId(id);
                                evaluationInsertList.add(evaluation);
                            } else {
                                evaluationUpdateList.add(evaluation);
                            }
                            evaluTask.setEvaluNextdayId(evaluation.getEvaluationId());
                            evaluTaskUpdateList.add(evaluTask);
                        }
                    }
                    //更新
                    String url = dataurl;
                    String user = userName;
                    String password = datapassword;
                    //2）、加载驱动，不需要显示注册驱动
                    Class.forName(driver);
                    //更新同步邀约信息状态
                    String sql = "UPDATE dr_evalu_cust_task set EVALU_NEXTDAY_STATUS=?,EVALU_NEXTDAY_ID=?,EVALU_NEXTDAY_DESC=?,UPDATE_TIME=? where id=?";
                    if (null != evaluTaskUpdateList && evaluTaskUpdateList.size() > 0) {
                        //获取数据库连接
                        conn = DriverManager.getConnection(url, user, password);
                        conn.setAutoCommit(false);
                        preparedStatement = conn.prepareStatement(sql);
                        int i = 0;
                        for (EvaluCustTask evaluTask : evaluTaskUpdateList) {
                            preparedStatement.setString(1, evaluTask.getEvaluNextdayStatus());
                            if(null!=evaluTask.getEvaluNextdayId()) {
                                preparedStatement.setLong(2, evaluTask.getEvaluNextdayId());
                            } else {
                                preparedStatement.setNull(2,Types.BIGINT);
                            }
                            if(null!=evaluTask.getEvaluNextdayDesc()) {
                                preparedStatement.setString(3, evaluTask.getEvaluNextdayDesc());
                            } else {
                                preparedStatement.setNull(3,Types.VARCHAR);
                            }
                            preparedStatement.setString(4, dateFormat.format(new Date()));
                            preparedStatement.setLong(5,evaluTask.getId());
                            preparedStatement.addBatch();
                            if ((i + 1) % 1000 == 0 || i == evaluTaskUpdateList.size() - 1) {
                                //每1000条提交一次
                                preparedStatement.executeBatch();
                                //清空记录
                                preparedStatement.clearBatch();
                            }
                            i++;
                        }
                    }
                    log.info("更新客户次日效果评估任务表完成，共:" + evaluTaskUpdateList.size() + "条");
                    String sql2 = "INSERT INTO dr_cust_evaluation (evaluation_id,cust_id,event_id,invitation_cap,reply_cap,actual_cap,confirm_cap,max_load_baseline,min_load_baseline,avg_load_baseline,max_load_actual,min_load_actual,avg_load_actual,is_effective,effective_time,create_time,remark,execute_rate)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    if (null !=evaluationInsertList  && evaluationInsertList.size() > 0) {
                        //获取数据库连接
                        conn2 = DriverManager.getConnection(url, user, password);
                        conn2.setAutoCommit(false);
                        preparedStatement2 = conn2.prepareStatement(sql2);
                        int i = 0;
                        for (CustEvaluation evaluation1 : evaluationInsertList) {
                            preparedStatement2.setLong(1, evaluation1.getEvaluationId());
                            preparedStatement2.setLong(2, evaluation1.getCustId());
                            preparedStatement2.setLong(3, evaluation1.getEventId());
                            preparedStatement2.setBigDecimal(4, evaluation1.getInvitationCap());
                            preparedStatement2.setBigDecimal(5, evaluation1.getReplyCap());
                            preparedStatement2.setBigDecimal(6, evaluation1.getActualCap());
                            preparedStatement2.setBigDecimal(7, evaluation1.getConfirmCap());
                            preparedStatement2.setBigDecimal(8, evaluation1.getMaxLoadBaseline());
                            preparedStatement2.setBigDecimal(9, evaluation1.getMinLoadBaseline());
                            preparedStatement2.setBigDecimal(10, evaluation1.getAvgLoadBaseline());
                            preparedStatement2.setBigDecimal(11, evaluation1.getMaxLoadActual());
                            preparedStatement2.setBigDecimal(12, evaluation1.getMinLoadActual());
                            preparedStatement2.setBigDecimal(13, evaluation1.getAvgLoadActual());
                            if(null!=evaluation1.getIsEffective()) {
                                preparedStatement2.setString(14, evaluation1.getIsEffective());
                            } else {
                                preparedStatement2.setNull(14,Types.VARCHAR);
                            }
                            if(null!=evaluation1.getEffectiveTime()) {
                                preparedStatement2.setInt(15, evaluation1.getEffectiveTime());
                            } else {
                                preparedStatement2.setInt(15, 0);
                            }
                            preparedStatement2.setString(16, dateFormat.format(new Date()));
                            preparedStatement2.setString(17,evaluation1.getRemark());
                            preparedStatement2.setBigDecimal(18,evaluation1.getExecuteRate());
                            preparedStatement2.addBatch();
                            if ((i + 1) % 1000 == 0 || i == evaluationInsertList.size() - 1) {
                                //每1000条提交一次
                                preparedStatement2.executeBatch();
                                //清空记录
                                preparedStatement2.clearBatch();
                            }
                            i++;
                        }

                    }
                    log.info("保存次日客户效果评估数据完成，共:" + evaluationInsertList.size() + "条");
                    String sql3 = "update dr_cust_evaluation set cust_id=?, event_id=?, invitation_cap=?, reply_cap=?, actual_cap=?, confirm_cap=?, max_load_baseline=?, min_load_baseline=?, avg_load_baseline=?, max_load_actual=?, min_load_actual=?, avg_load_actual=?, is_effective=?, effective_time=?, update_time=?,remark=?,execute_rate=? where evaluation_id=?";
                    if (null !=evaluationUpdateList  && evaluationUpdateList.size() > 0) {
                        //获取数据库连接
                        conn3 = DriverManager.getConnection(url, user, password);
                        conn3.setAutoCommit(false);
                        preparedStatement3 = conn3.prepareStatement(sql3);
                        int i = 0;
                        for (CustEvaluation evaluation1 : evaluationUpdateList) {
                            preparedStatement3.setLong(1, evaluation1.getCustId());
                            preparedStatement3.setLong(2, evaluation1.getEventId());
                            preparedStatement3.setBigDecimal(3, evaluation1.getInvitationCap());
                            preparedStatement3.setBigDecimal(4, evaluation1.getReplyCap());
                            preparedStatement3.setBigDecimal(5, evaluation1.getActualCap());
                            preparedStatement3.setBigDecimal(6, evaluation1.getConfirmCap());
                            preparedStatement3.setBigDecimal(7, evaluation1.getMaxLoadBaseline());
                            preparedStatement3.setBigDecimal(8, evaluation1.getMinLoadBaseline());
                            preparedStatement3.setBigDecimal(9, evaluation1.getAvgLoadBaseline());
                            preparedStatement3.setBigDecimal(10, evaluation1.getMaxLoadActual());
                            preparedStatement3.setBigDecimal(11, evaluation1.getMinLoadActual());
                            preparedStatement3.setBigDecimal(12, evaluation1.getAvgLoadActual());
                            if(null!=evaluation1.getIsEffective()) {
                                preparedStatement3.setString(13, evaluation1.getIsEffective());
                            } else {
                                preparedStatement3.setNull(13,Types.VARCHAR);
                            }
                            if(null!=evaluation1.getEffectiveTime()) {
                                preparedStatement3.setInt(14, evaluation1.getEffectiveTime());
                            } else {
                                preparedStatement3.setNull(14,Types.INTEGER);
                            }
                            preparedStatement3.setString(15, dateFormat.format(new Date()));
                            preparedStatement3.setString(16,evaluation1.getRemark());
                            preparedStatement3.setBigDecimal(17,evaluation1.getExecuteRate());
                            preparedStatement3.setLong(18,evaluation1.getEvaluationId());
                            preparedStatement3.addBatch();
                            if ((i + 1) % 1000 == 0 || i == evaluationUpdateList.size() - 1) {
                                //每1000条提交一次
                                preparedStatement3.executeBatch();
                                //清空记录
                                preparedStatement3.clearBatch();
                            }
                            i++;
                        }

                    }
                    log.info("更新客户次日效果评估数据完成，共:" + evaluationUpdateList.size() + "条");
                    if (null != conn) {
                        conn.commit();
                    }
                    if (null != conn2) {
                        conn2.commit();
                    }
                    if (null != conn3) {
                        conn3.commit();
                    }
                    log.info("次日客户效果评估数据计算完成!");
                } catch (Exception e) {
                    try {
                        if (null != conn) {
                            conn.rollback();
                        }
                        if (null != conn2) {
                            conn2.rollback();
                        }
                        if (null != conn3) {
                            conn3.rollback();
                        }
                    } catch (SQLException sqlException) {
                        sqlException.printStackTrace();
                    }
                    e.printStackTrace();
                } finally {
                    Integer count = event.getCustNextdayCount();
                    if(null==count) {
                        count = 0;
                    }
                    count = count + 1;
                    event.setCustNextdayCount(count);
                    eventService.updateById(event);
                    if (null != conn) {
                        try {
                            conn.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    if (null != preparedStatement) {
                        try {
                            preparedStatement.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                    }
                    if (null != conn2) {
                        try {
                            conn2.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    if (null != preparedStatement2) {
                        try {
                            preparedStatement2.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                    }
                    if (null != conn3) {
                        try {
                            conn3.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    if (null != preparedStatement3) {
                        try {
                            preparedStatement3.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        };
        return runnable;
    }
}
