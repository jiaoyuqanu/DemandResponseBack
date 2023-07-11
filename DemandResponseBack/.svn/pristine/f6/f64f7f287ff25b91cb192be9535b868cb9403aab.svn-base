package com.xqxy.executor.service.jobhandler;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.thread.ExecutorBuilder;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.xqxy.core.enums.YesOrNotEnum;
import com.xqxy.dr.modular.evaluation.entity.*;
import com.xqxy.dr.modular.evaluation.enums.EvaluTaskEnum;
import com.xqxy.dr.modular.evaluation.param.ConsEvaluationParam;
import com.xqxy.dr.modular.evaluation.param.EvaluCustTaskParam;
import com.xqxy.dr.modular.evaluation.param.EvaluTaskParam;
import com.xqxy.dr.modular.evaluation.service.ConsEvaluationService;
import com.xqxy.dr.modular.evaluation.service.CustEvaluationService;
import com.xqxy.dr.modular.evaluation.service.EvaluCustTaskService;
import com.xqxy.dr.modular.evaluation.service.EvaluTaskService;
import com.xqxy.dr.modular.event.entity.ConsInvitation;
import com.xqxy.dr.modular.event.entity.Event;
import com.xqxy.dr.modular.event.service.ConsInvitationService;
import com.xqxy.dr.modular.event.service.EventService;
import com.xqxy.dr.modular.project.entity.Project;
import com.xqxy.dr.modular.project.entity.ProjectDetail;
import com.xqxy.dr.modular.project.enums.ProjectTypeEnums;
import com.xqxy.dr.modular.project.params.ProjectParam;
import com.xqxy.dr.modular.project.result.ProjectInfo;
import com.xqxy.dr.modular.project.service.ProjectDetailService;
import com.xqxy.dr.modular.project.service.ProjectService;
import com.xqxy.dr.modular.strategy.Utils.StrategyUtils;
import com.xqxy.dr.modular.subsidy.entity.*;
import com.xqxy.dr.modular.subsidy.enums.CalRuleEnum;
import com.xqxy.dr.modular.subsidy.mapper.*;
import com.xqxy.dr.modular.subsidy.result.ConsSubsidyInfo;
import com.xqxy.dr.modular.subsidy.result.CustSubsidyMonthlyInfo;
import com.xqxy.dr.modular.subsidy.service.*;
import com.xqxy.dr.modular.subsidy.util.DictUtil;
import com.xqxy.sys.modular.cust.entity.Cust;
import com.xqxy.sys.modular.cust.entity.UserConsRela;
import com.xqxy.sys.modular.cust.enums.IsAggregatorEnum;
import com.xqxy.sys.modular.cust.service.CustService;
import com.xqxy.sys.modular.dict.entity.DictData;
import com.xqxy.sys.modular.dict.service.DictTypeService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.log.XxlJobLogger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

/**
 * @Description 补贴6个定时任务
 * @ClassName EventJob
 * @Author chen zhi jun
 * @date 2021.05.11 14:35
 */
@Component
public class EventJob {
    private static final Log log = Log.get();

    @Resource
    private EventService eventService;

    @Resource
    private ConsInvitationService consInvitationService;

    @Resource
    private EvaluTaskService evaluTaskService;

    @Resource
    private EvaluCustTaskService evaluCustTaskService;

    @Resource
    private ConsEvaluationService consEvaluationService;

    @Resource
    private CustEvaluationService custEvaluationService;

    @Resource
    private ConsSubsidyService consSubsidyService;

    @Resource
    private CustSubsidyService custSubsidyService;

    @Resource
    private ConsSubsidyMapper consSubsidyMapper;

    @Resource
    private DictTypeService dictTypeService;

    @Resource
    private CustSubsidyMapper custSubsidyMapper;

    @Resource
    private ConsSubsidyDailyMapper consSubsidyDailyMapper;

    @Resource
    private CustSubsidyDailyMapper custSubsidyDailyMapper;

    @Resource
    private ConsSubsidyDailyService consSubsidyDailyService;

    @Resource
    private CustSubsidyDailyService custSubsidyDailyService;

    @Resource
    private SubsidyMonthlyService subsidyMonthlyService;

    @Resource
    private SubsidyMonthlyCustService subsidyMonthlyCustService;

    @Resource
    private SubsidyMonthlyMapper subsidyMonthlyMapper;

    @Resource
    private SubsidyMonthlyCustMapper subsidyMonthlyCustMapper;

    @Resource
    private ProjectService projectService;

    @Resource
    private ProjectDetailService projectDetailService;

    @Resource
    private CustService custService;

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
     * 电力用户事件补贴计算
     *
     * @param
     * @return
     * @throws Exception
     */
    @XxlJob("consSubsidyCal")
    public ReturnT<String> consSubsidyCal(String param) throws Exception {
        XxlJobLogger.log("电力用户事件补贴计算");
        this.generateConsEventSubsidy(param);
        return ReturnT.SUCCESS;
    }

    /**
     * 负荷集成商事件补贴计算
     *
     * @param
     * @return
     * @throws Exception
     */
    @XxlJob("custSubsidyCal")
    public ReturnT<String> custSubsidyCal(String param) throws Exception {
        XxlJobLogger.log("负荷集成商事件补贴计算");
        this.generateCustEventSubsidyNew(param);
        return ReturnT.SUCCESS;
    }

    /**
     * 电力用户日补贴计算
     *
     * @param
     * @return
     * @throws Exception
     */
    @XxlJob("consDailySubsidyCal")
    public ReturnT<String> consDailySubsidyCal(String param) throws Exception {
        XxlJobLogger.log("电力用户日补贴计算");
        this.generateConsDailySubsidy(param);
        return ReturnT.SUCCESS;
    }

    /**
     * 负荷集成商日补贴计算
     *
     * @param
     * @return
     * @throws Exception
     */
    @XxlJob("custDailySubsidyCal")
    public ReturnT<String> custDailySubsidyCal(String param) throws Exception {
        XxlJobLogger.log("负荷集成商日补贴计算");
        this.generateCustDailySubsidy(param);
        return ReturnT.SUCCESS;
    }

    /**
     * 电力用户月补贴计算
     *
     * @param
     * @return
     * @throws Exception
     */
    @XxlJob("consMonthSubsidyCal")
    public ReturnT<String> consMonthSubsidyCals(String param) throws Exception {
        XxlJobLogger.log("电力用户月补贴计算");
        this.consMonthSubsidyCal(param);
        return ReturnT.SUCCESS;
    }

    /**
     * 负荷集成商月补贴计算
     *
     * @param
     * @return
     * @throws Exception
     */
    @XxlJob("custMonthSubsidyCal")
    public ReturnT<String> custMonthSubsidyCals(String param) throws Exception {
        XxlJobLogger.log("负荷集成商月补贴计算");
        this.custMonthSubsidyCal(param);
        return ReturnT.SUCCESS;
    }

    public void generateConsEventSubsidy(String param) {
        log.info("用户补贴计算开始!");
        LocalDate currentDate = LocalDate.now().minusDays(1);
        if (param != null && !param.equals("")) {
            currentDate = LocalDate.parse(param);
        }

        EvaluTaskParam evaluTaskParam = new EvaluTaskParam();
        evaluTaskParam.setRegulateDate(currentDate);
        evaluTaskParam.setRegulateDateStr(simpleDateFormat.format(currentDate));
        evaluTaskParam.setSubsidyStatus("1");
        List<EvaluTask> evaluTasks = evaluTaskService.list(evaluTaskParam);
        if (ObjectUtil.isNull(evaluTasks) || evaluTasks.size() < 1) {
            log.error(">>> 服务器运行异常，{}", "事件信息不存在");
            return;
        }
        //查询事件信息
        LambdaQueryWrapper<Event> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Event::getRegulateDate, currentDate);
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
            log.error("无事件信息!");
            return;
        }
        ExecutorService executor = ExecutorBuilder.create()
                .setCorePoolSize(corePoolSize)
                .setMaxPoolSize(maxPoolSize)
                .setWorkQueue(new LinkedBlockingQueue<>(workQueue))
                .build();
        executor.execute(this.generateConsEventSubsidyNew(evaluTasks,eventIds,eventList));
        executor.shutdown();
    }

    public Runnable generateConsEventSubsidyNew(List<EvaluTask> evaluTasks, List<Long> eventIds,List<Event> eventList) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                List<DictData> regulationTimeDictDataList = dictTypeService.getDictDataByTypeCode("regulation_time");
                List<DictData> loadResponseDictDataList = dictTypeService.getDictDataByTypeCode("load_response");
                //查询事件参与所有用户
                List<ConsInvitation> consInvitations = consInvitationService.getConsInfoByEvents2(eventList);
                if (null == consInvitations || consInvitations.size() == 0) {
                    log.info("无用户邀约信息");
                    return;
                }
                //查询项目信息
                List<Project> projectList = projectService.list();
                List<ProjectDetail> projectDetailList = projectDetailService.list();
                //获取效果评估数据
                LambdaQueryWrapper<ConsEvaluation> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.in(ConsEvaluation::getEventId, eventIds);
                List<ConsEvaluation> evaluationList = consEvaluationService.list(queryWrapper);
                //查询事件补贴数据
                LambdaQueryWrapper<ConsSubsidy> queryWrapperSu = new LambdaQueryWrapper<>();
                queryWrapperSu.in(ConsSubsidy::getEventId, eventIds);
                List<ConsSubsidy> consSubsidyList = consSubsidyService.list(queryWrapperSu);
                List<EvaluTask> evaluTaskUpdateList = new ArrayList<>();
                List<ConsSubsidy> consSubsidyUpdateList = new ArrayList<>();
                List<ConsSubsidy> consSubsidyInsertList = new ArrayList<>();
                for (EvaluTask evaluTask : evaluTasks) {
                    Map<String, ProjectDetail> projectDetailMap = new HashMap<>();
                    //获取事件
                    List<Event> events = eventList.stream().filter(event1 -> event1.getEventId().equals(evaluTask.getEventId())).collect(Collectors.toList());
                    if (null == events || events.size() ==0) {
                        evaluTask.setSubsidyDesc("无事件信息");
                        evaluTask.setSubsidyStatus("3");
                        evaluTaskUpdateList.add(evaluTask);
                        continue;
                    }
                    Event event = events.get(0);
                    List<Project> projects = projectList.stream().filter(project1 -> project1.getProjectId().equals(event.getProjectId())).collect(Collectors.toList());
                    List<ProjectDetail> projectDetails = null;
                    if (null ==projects || projects.size()==0) {
                        evaluTask.setSubsidyDesc("无项目信息");
                        evaluTask.setSubsidyStatus("3");
                        evaluTaskUpdateList.add(evaluTask);
                        continue;
                    } else {
                        projectDetails = projectDetailList.stream().filter(projectDetail -> projectDetail.getProjectId().equals(event.getProjectId())).collect(Collectors.toList());
                    }
                    if (null != projectDetails && projectDetails.size()>0) {
                        for (ProjectDetail projectDetail : projectDetails) {
                            String timeType = projectDetail.getTimeType();
                            String responseType = projectDetail.getResponseType();
                            String type = timeType + "-" + responseType;
                            projectDetailMap.put(type, projectDetail);
                        }
                    }
                    Project project = projects.get(0);
                    String projectType = project.getProjectType();
                    String incentiveStandard = evaluTask.getIncentiveStandard();
                    List<ConsInvitation> eventInvitations = consInvitations.stream().filter(consInvitation ->
                            consInvitation.getEventId().equals(evaluTask.getEventId()) && consInvitation.getConsId().equals(evaluTask.getConsId())
                    ).collect(Collectors.toList());
                    if (null == eventInvitations || eventInvitations.size() == 0) {
                        evaluTask.setSubsidyDesc("无邀约信息");
                        evaluTask.setSubsidyStatus("3");
                        evaluTaskUpdateList.add(evaluTask);
                        continue;
                    }
                    ConsInvitation consInvitation = eventInvitations.get(0);
                    if (evaluTask.getEvaluNextdayStatus() != null && evaluTask.getEvaluNextdayStatus().equals(EvaluTaskEnum.CAL_COMPLETE.getCode())) {
                        if (evaluTask.getSubsidyStatus() == null || evaluTask.getSubsidyStatus().equals("") || !evaluTask.getSubsidyStatus().equals(EvaluTaskEnum.CAL_COMPLETE.getCode())) {
                            List<ConsEvaluation> evaluations = null;
                            if (null != evaluTask.getConsId() && null != evaluationList && evaluationList.size() > 0) {
                                evaluations = evaluationList.stream().filter(immediate -> immediate.getConsId().equals(evaluTask.getConsId())
                                        && immediate.getEventId().equals(evaluTask.getEventId())
                                ).collect(Collectors.toList());
                            }
                            if (ObjectUtil.isNull(evaluations)) {
                                evaluTask.setSubsidyDesc("无邀约信息");
                                evaluTask.setSubsidyStatus("3");
                                evaluTaskUpdateList.add(evaluTask);
                                continue;
                            }
                            ConsEvaluation consEvaluation = evaluations.get(0);
                            String isEffective = consEvaluation.getIsEffective();
                            boolean isCreate = false;
                            ConsSubsidy consSubsidy = null;
                            List<ConsSubsidy> consSubsidys = consSubsidyList.stream().filter(subsidy -> subsidy.getConsId().equals(evaluTask.getConsId())
                                    && subsidy.getEventId().equals(evaluTask.getEventId())
                            ).collect(Collectors.toList());
                            if(null!=consSubsidys && consSubsidys.size()>0) {
                                consSubsidy = consSubsidys.get(0);
                            } else {
                                consSubsidy = new ConsSubsidy();
                                consSubsidy.setEventId(event.getEventId());
                                consSubsidy.setConsId(evaluTask.getConsId());
                                isCreate = true;
                            }
                            consSubsidy.setJoinUserType(consInvitation.getJoinUserType());
                            consSubsidy.setActualCap(consEvaluation.getActualCap());
                            consSubsidy.setActualEnergy(consEvaluation.getActualEnergy());
                            consSubsidy.setContractPrice(consInvitation.getReplyPrice());
                            consSubsidy.setCalRule(incentiveStandard);

                            if (isEffective == null || isEffective.equals(YesOrNotEnum.N.getCode())) {
                                consSubsidy.setSubsidyAmount(new BigDecimal("0"));
                                consSubsidy.setSettledAmount(new BigDecimal("0"));
                                consSubsidy.setRemark("无效响应");
                            } else if (isEffective.equals(YesOrNotEnum.Y.getCode()) && ObjectUtil.isNull(consEvaluation.getConfirmCap())) {
                                consSubsidy.setSubsidyAmount(new BigDecimal("0"));
                                consSubsidy.setSettledAmount(new BigDecimal("0"));
                                consSubsidy.setRemark("用户响应容量未计算");
                            } else {
                                if (CalRuleEnum.ONE.getCode().equals(incentiveStandard)) {
                                    consSubsidy.setSubsidyAmount(NumberUtil.mul(consInvitation.getReplyCap(), consInvitation.getReplyPrice()));
                                    consSubsidy.setSettledAmount(NumberUtil.mul(consInvitation.getReplyCap(), consInvitation.getReplyPrice()));
                                } else if (CalRuleEnum.TWO.getCode().equals(incentiveStandard)) {
                                    String timeType = event.getTimeType();  //1约时；2实时
                                    String responseType = event.getResponseType();  //1削峰；2填谷
                                    Integer effectiveTime = consEvaluation.getEffectiveTime();
                                    BigDecimal replyCap = consInvitation.getReplyCap();
                                    BigDecimal actualCap = consEvaluation.getActualCap();
                                    BigDecimal price = BigDecimal.ZERO;
                                    BigDecimal time;
                                    BigDecimal powerRate;
                                    if (actualCap == null) {
                                        actualCap = BigDecimal.ZERO;
                                    }
                                    if (replyCap == null) {
                                        replyCap = BigDecimal.ZERO;
                                    }
                                    if (projectType.equals(ProjectTypeEnums.PRICE_TYPE.getCode())) {
                                        price = consInvitation.getReplyPrice();
                                    } else if (projectType.equals(ProjectTypeEnums.EXCITATION_TYPE.getCode())) {
                                        String type = timeType + "-" + responseType;
                                        ProjectDetail projectDetail = projectDetailMap.get(type);
                                        if (ObjectUtil.isNotNull(projectDetail)) {
                                            price = projectDetail.getPrice();
                                        }
                                    }

                                    consSubsidy.setContractPrice(price);
                                    if (null != effectiveTime) {
                                        time = DictUtil.getDictValue(regulationTimeDictDataList, new BigDecimal(effectiveTime));
                                        consSubsidy.setTimeCoefficient(time);
                                    } else {
                                        consSubsidy.setTimeCoefficient(BigDecimal.ZERO);
                                        time = BigDecimal.ZERO;
                                    }
                                    if (time.compareTo(new BigDecimal("0")) == 0) {
                                        consSubsidy.setRemark("有效响应时长小于60分钟");
                                    }

                                    BigDecimal val = NumberUtil.div(actualCap, replyCap);
                                    if (val.compareTo(new BigDecimal("1.2")) > 0) {
                                        actualCap = NumberUtil.mul(replyCap, new BigDecimal("1.2"));
                                    }
                                    powerRate = DictUtil.getDictValue(loadResponseDictDataList, val);
                                    if (powerRate.compareTo(new BigDecimal("0")) == 0) {
                                        consSubsidy.setRemark("响应负荷率不在0.8-1.2之间");
                                        consSubsidy.setRateCoefficient(BigDecimal.ZERO);
                                    } else {
                                        consSubsidy.setRateCoefficient(BigDecimal.ONE);
                                    }

                                    consSubsidy.setSubsidyAmount(NumberUtil.mul(actualCap, price, time, powerRate));
                                    consSubsidy.setSettledAmount(NumberUtil.mul(actualCap, price, time, powerRate));
                                }
                            }

                            if (isCreate) {
                                long id = IdWorker.getId();
                                consSubsidy.setSubsidyId(id);
                                evaluTask.setSubsidyId(id);
                                evaluTask.setSubsidyStatus(EvaluTaskEnum.CAL_COMPLETE.getCode());
                                consSubsidyInsertList.add(consSubsidy);
                            } else {
                                evaluTask.setSubsidyStatus(EvaluTaskEnum.CAL_COMPLETE.getCode());
                                consSubsidyUpdateList.add(consSubsidy);
                            }
                            evaluTaskUpdateList.add(evaluTask);
                        }
                    }
                }
                //更新效果评估任务表
                //更新
                PreparedStatement preparedStatement = null;
                Connection conn = null;
                PreparedStatement preparedStatement2 = null;
                Connection conn2 = null;
                PreparedStatement preparedStatement3 = null;
                Connection conn3 = null;
                try {
                    String url = dataurl;
                    String user = userName;
                    String password = datapassword;
                    //2）、加载驱动，不需要显示注册驱动
                    Class.forName(driver);
                    //更新同步邀约信息状态
                    String sql = "UPDATE dr_evalu_task set SUBSIDY_STATUS=?,SUBSIDY_ID=?,SUBSIDY_DESC=?,UPDATE_TIME=? where id=?";
                    if (null != evaluTaskUpdateList && evaluTaskUpdateList.size() > 0) {
                        //获取数据库连接
                        conn = DriverManager.getConnection(url, user, password);
                        conn.setAutoCommit(false);
                        preparedStatement = conn.prepareStatement(sql);
                        int i = 0;
                        for (EvaluTask evaluTask : evaluTaskUpdateList) {
                            preparedStatement.setString(1, evaluTask.getSubsidyStatus());
                            if(null!=evaluTask.getSubsidyId()) {
                                preparedStatement.setLong(2, evaluTask.getSubsidyId());
                            } else {
                                preparedStatement.setNull(2, Types.BIGINT);
                            }
                            if(null!=evaluTask.getSubsidyDesc()) {
                                preparedStatement.setString(3, evaluTask.getSubsidyDesc());
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
                    log.info("更新补贴任务表完成，共:" + evaluTaskUpdateList.size() + "条");
                    String sql2 = "INSERT INTO dr_cons_subsidy (subsidy_id,EVENT_ID,CONS_ID,actual_cap,actual_energy,CONTRACT_PRICE,subsidy_amount,cal_rule,CREATE_TIME," +
                            "settled_amount,remark,join_user_type,time_coefficient,rate_coefficient)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    if (null !=consSubsidyInsertList  && consSubsidyInsertList.size() > 0) {
                        //获取数据库连接
                        conn2 = DriverManager.getConnection(url, user, password);
                        conn2.setAutoCommit(false);
                        preparedStatement2 = conn2.prepareStatement(sql2);
                        int i = 0;
                        for (ConsSubsidy consSubsidyUpdateOrSave : consSubsidyInsertList) {
                            preparedStatement2.setLong(1, consSubsidyUpdateOrSave.getSubsidyId());
                            preparedStatement2.setLong(2, consSubsidyUpdateOrSave.getEventId());
                            preparedStatement2.setString(3, consSubsidyUpdateOrSave.getConsId());
                            preparedStatement2.setBigDecimal(4, consSubsidyUpdateOrSave.getActualCap());
                            preparedStatement2.setBigDecimal(5, consSubsidyUpdateOrSave.getActualEnergy());
                            preparedStatement2.setBigDecimal(6, consSubsidyUpdateOrSave.getContractPrice());
                            preparedStatement2.setBigDecimal(7, consSubsidyUpdateOrSave.getSubsidyAmount());
                            if(null!=consSubsidyUpdateOrSave.getCalRule()) {
                                preparedStatement2.setString(8, consSubsidyUpdateOrSave.getCalRule());
                            } else {
                                preparedStatement2.setNull(8, Types.VARCHAR);
                            }
                            preparedStatement2.setString(9,  dateFormat.format(new Date()));
                            preparedStatement2.setBigDecimal(10, consSubsidyUpdateOrSave.getSettledAmount());
                            preparedStatement2.setString(11,  consSubsidyUpdateOrSave.getRemark());
                            preparedStatement2.setString(12,  consSubsidyUpdateOrSave.getJoinUserType());
                            preparedStatement2.setBigDecimal(13, consSubsidyUpdateOrSave.getTimeCoefficient());
                            preparedStatement2.setBigDecimal(14, consSubsidyUpdateOrSave.getRateCoefficient());
                            preparedStatement2.addBatch();
                            if ((i + 1) % 1000 == 0 || i == consSubsidyInsertList.size() - 1) {
                                //每1000条提交一次
                                preparedStatement2.executeBatch();
                                //清空记录
                                preparedStatement2.clearBatch();
                            }
                            i++;
                        }

                    }
                    log.info("保存用户补贴数据完成，共:" + consSubsidyInsertList.size() + "条");
                    String sql3 = "UPDATE dr_cons_subsidy set settled_amount=?,EVENT_ID=?,CONS_ID=?,actual_cap=?,actual_energy=?,\n" +
                            "CONTRACT_PRICE=?,subsidy_amount=?,cal_rule=?,UPDATE_TIME=?,remark=?,time_coefficient=?,rate_coefficient=? where subsidy_id=?";
                    if (null !=consSubsidyUpdateList  && consSubsidyUpdateList.size() > 0) {
                        //获取数据库连接
                        conn3 = DriverManager.getConnection(url, user, password);
                        conn3.setAutoCommit(false);
                        preparedStatement3 = conn3.prepareStatement(sql3);
                        int i = 0;
                        for (ConsSubsidy consSubsidyUpdateOrSave : consSubsidyUpdateList) {
                            preparedStatement3.setBigDecimal(1, consSubsidyUpdateOrSave.getSettledAmount());
                            preparedStatement3.setLong(2, consSubsidyUpdateOrSave.getEventId());
                            preparedStatement3.setString(3, consSubsidyUpdateOrSave.getConsId());
                            preparedStatement3.setBigDecimal(4, consSubsidyUpdateOrSave.getActualCap());
                            preparedStatement3.setBigDecimal(5, consSubsidyUpdateOrSave.getActualEnergy());
                            preparedStatement3.setBigDecimal(6, consSubsidyUpdateOrSave.getContractPrice());
                            preparedStatement3.setBigDecimal(7, consSubsidyUpdateOrSave.getSubsidyAmount());
                            if(null!=consSubsidyUpdateOrSave.getCalRule()) {
                                preparedStatement3.setString(8, consSubsidyUpdateOrSave.getCalRule());
                            } else {
                                preparedStatement3.setNull(8, Types.VARCHAR);
                            }
                            preparedStatement3.setString(9,  dateFormat.format(new Date()));
                            preparedStatement3.setString(10, consSubsidyUpdateOrSave.getRemark());
                            preparedStatement3.setBigDecimal(11, consSubsidyUpdateOrSave.getTimeCoefficient());
                            preparedStatement3.setBigDecimal(12, consSubsidyUpdateOrSave.getRateCoefficient());
                            preparedStatement3.setLong(13, consSubsidyUpdateOrSave.getSubsidyId());
                            preparedStatement3.addBatch();
                            if ((i + 1) % 1000 == 0 || i == consSubsidyUpdateList.size() - 1) {
                                //每1000条提交一次
                                preparedStatement3.executeBatch();
                                //清空记录
                                preparedStatement3.clearBatch();
                            }
                            i++;
                        }

                    }
                    log.info("更新用户补贴数据完成，共:" + consSubsidyUpdateList.size() + "条");
                    if (null != conn) {
                        conn.commit();
                    }
                    if (null != conn2) {
                        conn2.commit();
                    }
                    if (null != conn3) {
                        conn3.commit();
                    }
                    log.info("用户补贴计算完成!");
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

    public void generateCustEventSubsidyNew(String param) {
        log.info("客户事件补贴计算开始!");
        LocalDate currentDate = LocalDate.now().minusDays(1);
        if (param != null && !param.equals("")) {
            currentDate = LocalDate.parse(param);
        }

        EvaluCustTaskParam evaluCustTaskParam = new EvaluCustTaskParam();
        evaluCustTaskParam.setRegulateDate(currentDate);
        evaluCustTaskParam.setRegulateDateStr(simpleDateFormat.format(currentDate));
        evaluCustTaskParam.setSubsidyStatus("1");
        List<EvaluCustTask> evaluCustTasks = evaluCustTaskService.list(evaluCustTaskParam);
        if (ObjectUtil.isNull(evaluCustTasks) || evaluCustTasks.size() < 1) {
            log.error(">>> 服务器运行异常，{}", "事件信息不存在");
            return;
        }
        //查询事件信息
        LambdaQueryWrapper<Event> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Event::getRegulateDate, currentDate);
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
            log.error("无事件信息!");
            return;
        }
        ExecutorService executor = ExecutorBuilder.create()
                .setCorePoolSize(corePoolSize)
                .setMaxPoolSize(maxPoolSize)
                .setWorkQueue(new LinkedBlockingQueue<>(workQueue))
                .build();
        executor.execute(this.generateCustEventSubsidyNew(evaluCustTasks,eventIds,eventList));
        executor.shutdown();
    }

    public Runnable generateCustEventSubsidyNew(List<EvaluCustTask> evaluTasks,List<Long> eventIds,List<Event> eventList) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                //查询事件参与所有用户
                List<ConsInvitation> consInvitations = consInvitationService.getConsInfoByEvents2(eventList);
                if (null == consInvitations || consInvitations.size() == 0) {
                    log.info("无用户邀约信息");
                    return;
                }
                //获取客户效果评估数据
                LambdaQueryWrapper<CustEvaluation> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.in(CustEvaluation::getEventId, eventIds);
                List<CustEvaluation> evaluationList = custEvaluationService.list(queryWrapper);
                if(null==evaluationList || evaluationList.size()==0) {
                    log.info("无客户效果评估数据");
                    return;
                }
                //获取用户效果评估数据
                LambdaQueryWrapper<ConsEvaluation> queryWrapper2 = new LambdaQueryWrapper<>();
                queryWrapper2.in(ConsEvaluation::getEventId, eventIds);
                List<ConsEvaluation> evaluationList2 = consEvaluationService.list(queryWrapper2);
                if(null==evaluationList2 || evaluationList2.size()==0) {
                    log.info("无用户效果评估数据");
                    return;
                }
                //查询事件补贴数据
                LambdaQueryWrapper<ConsSubsidy> queryWrapperSu = new LambdaQueryWrapper<>();
                queryWrapperSu.in(ConsSubsidy::getEventId, eventIds);
                List<ConsSubsidyInfo> consSubsidyList = new ArrayList<>();
                for(int i = 0;i<eventIds.size();i++) {
                    List<ConsSubsidyInfo> consSubsidyInfos = consSubsidyMapper.getConsSubsidyInfo(eventIds.get(i));
                    if(null!=consSubsidyInfos && consSubsidyInfos.size()>0) {
                        for(ConsSubsidyInfo consSubsidyInfo : consSubsidyInfos) {
                            consSubsidyInfo.setEventId(eventIds.get(i));
                            consSubsidyList.add(consSubsidyInfo);
                        }
                    }
                }
                if(null==consSubsidyList || consSubsidyList.size()==0) {
                    log.info("无事件补贴数据");
                }
                LambdaQueryWrapper<ConsSubsidy> queryWrapperSuCons = new LambdaQueryWrapper<>();
                queryWrapperSuCons.in(ConsSubsidy::getEventId, eventIds);
                List<ConsSubsidy> consSubsidyListCons = consSubsidyService.list(queryWrapperSuCons);
                //查询客户补贴
                LambdaQueryWrapper<CustSubsidy> queryWrapperCustSu = new LambdaQueryWrapper<>();
                queryWrapperCustSu.in(CustSubsidy::getEventId,eventIds);
                List<CustSubsidy> custSubsidyList = custSubsidyService.list(queryWrapperCustSu);
                LambdaQueryWrapper<EvaluTask> evaluTaskLambdaQueryWrapper = new LambdaQueryWrapper<>();
                evaluTaskLambdaQueryWrapper.in(EvaluTask::getEventId,eventIds);
                List<EvaluTask> evaluTaskList = evaluTaskService.list(evaluTaskLambdaQueryWrapper);
                List<EvaluCustTask> evaluTaskUpdateList = new ArrayList<>();
                List<CustSubsidy> consSubsidyUpdateList = new ArrayList<>();
                List<CustSubsidy> consSubsidyInsertList = new ArrayList<>();
                List<ConsSubsidy> consSubsidyUpdateList2 = new ArrayList<>();
                List<EvaluTask> evaluTasksUpdateList = new ArrayList<>();
                for (EvaluCustTask evaluCustTask : evaluTasks) {
                    Long eventId = evaluCustTask.getEventId();
                    Long custId = evaluCustTask.getCustId();
                    String incentiveStandard = evaluCustTask.getIncentiveStandard();
                    List<CustEvaluation> custEvaluations = evaluationList.stream().filter(evaluation -> evaluation.getEventId().equals(eventId)
                            && evaluation.getCustId().equals(custId)
                    ).collect(Collectors.toList());
                    if(null==custEvaluations || custEvaluations.size()==0) {
                        evaluCustTask.setSubsidyDesc("客户评估信息不存在");
                        evaluCustTask.setSubsidyStatus("3");
                        evaluTaskUpdateList.add(evaluCustTask);
                        continue;
                    }
                    CustEvaluation custEvaluation = custEvaluations.get(0);
                    List<ConsSubsidyInfo> consSubsidyInfos = consSubsidyList.stream().filter(evaluation -> evaluation.getEventId().equals(eventId)
                    ).collect(Collectors.toList());
                    Map<Long, List<ConsSubsidyInfo>> subsidyMap = new HashMap<>();
                    if (ObjectUtil.isNotNull(consSubsidyInfos) && consSubsidyInfos.size() > 0) {
                        for (ConsSubsidyInfo consSubsidyInfo : consSubsidyInfos) {
                            List<ConsSubsidyInfo> consSubsidies = subsidyMap.getOrDefault(consSubsidyInfo.getCustId(), new ArrayList<>());
                            consSubsidies.add(consSubsidyInfo);
                            subsidyMap.put(consSubsidyInfo.getCustId(), consSubsidies);
                        }
                    }
                    CustSubsidy custSubsidy = null;
                    List<CustSubsidy> custSubsidys = custSubsidyList.stream().filter(evaluation -> evaluation.getEventId().equals(eventId)
                            && evaluation.getCustId().equals(custId)
                    ).collect(Collectors.toList());
                    if(null!=custSubsidys && custSubsidys.size()>0) {
                        custSubsidy = custSubsidys.get(0);
                    }
                    if (EvaluTaskEnum.CAL_COMPLETE.getCode().equals(evaluCustTask.getEvaluNextdayStatus())) {
                        if (!evaluCustTask.getSubsidyStatus().equals(EvaluTaskEnum.CAL_COMPLETE.getCode())) {
                            List<ConsSubsidyInfo> values = subsidyMap.get(custId);
                            if ((ObjectUtil.isNull(values) || values.size() < 1)) {
                                evaluCustTask.setSubsidyStatus(EvaluTaskEnum.CAL_EXCEPTION.getCode());
                                evaluCustTask.setSubsidyDesc("无用户参与邀约");
                                evaluTaskUpdateList.add(evaluCustTask);
                            } else {
                                if(!"Y".equals(custEvaluation.getIsEffective())) {
                                    if (ObjectUtil.isNull(custSubsidy)) {
                                        custSubsidy = new CustSubsidy();
                                        custSubsidy.setEventId(eventId);
                                        custSubsidy.setCustId(custId);
                                        custSubsidy.setIntegrator(evaluCustTask.getIntegrator());
                                        custSubsidy.setCalRule(incentiveStandard);
                                        custSubsidy.setActualCap(custEvaluation.getActualCap());
                                        custSubsidy.setActualEnergy(custEvaluation.getActualEnergy());
                                        custSubsidy.setSubsidyAmount(new BigDecimal("0"));
                                        custSubsidy.setRemark(evaluCustTask.getEvaluNextdayDesc());
                                        long id = IdWorker.getId();
                                        custSubsidy.setSubsidyId(id);
                                        evaluCustTask.setSubsidyId(id);
                                        evaluCustTask.setSubsidyStatus(EvaluTaskEnum.CAL_COMPLETE.getCode());
                                        evaluCustTask.setSubsidyDesc("效果评估无效："+custEvaluation.getRemark());
                                        consSubsidyInsertList.add(custSubsidy);
                                        evaluTaskUpdateList.add(evaluCustTask);
                                    } else {
                                        custSubsidy.setSubsidyAmount(new BigDecimal("0"));
                                        custSubsidy.setRemark("效果评估无效："+custEvaluation.getRemark());
                                        consSubsidyUpdateList.add(custSubsidy);
                                        evaluCustTask.setSubsidyId(custSubsidy.getSubsidyId());
                                        evaluCustTask.setSubsidyDesc("效果评估无效："+custEvaluation.getRemark());
                                        evaluCustTask.setSubsidyStatus(EvaluTaskEnum.CAL_COMPLETE.getCode());
                                        evaluTaskUpdateList.add(evaluCustTask);
                                    }
                                } else {
                                    //有代理用户或者是直非集成商客户
                                    boolean isSubsidy = true;
                                    for (ConsSubsidyInfo consSubsidyInfo : values) {
                                        if (!consSubsidyInfo.getSubsidyStatus().equals(EvaluTaskEnum.CAL_COMPLETE.getCode())) {
                                            isSubsidy = false;
                                            break;
                                        }
                                    }
                                    if (isSubsidy) {
                                        BigDecimal subsidyAmount = new BigDecimal("0");
                                        //聚合商根据代理协议分成比例抽取
                                        for (ConsSubsidyInfo consSubsidyInfo : values) {
                                            ConsSubsidy consSubsidy = null;
                                            List<ConsSubsidy> consSubsidyList1 = consSubsidyListCons.stream().filter(evaluation -> evaluation.getEventId().equals(eventId)
                                                    && evaluation.getConsId().equals(consSubsidyInfo.getConsId())
                                            ).collect(Collectors.toList());
                                            if (null != consSubsidyList1 && consSubsidyList1.size() > 0) {
                                                consSubsidy = consSubsidyList1.get(0);
                                            }
                                            if (ObjectUtil.isNull(consSubsidy)) {
                                                continue;
                                            }
                                            //聚合商补贴提成
                                            if ("1".equals(evaluCustTask.getIntegrator())) {
                                                subsidyAmount = subsidyAmount.add(NumberUtil.mul(consSubsidyInfo.getExtractRatio(), consSubsidy.getSettledAmount()));
                                                //集成商的代理用户补贴调整
                                                consSubsidy.setSubsidyAmount(NumberUtil.mul(NumberUtil.sub(new BigDecimal("1"),consSubsidyInfo.getExtractRatio()), consSubsidy.getSettledAmount()));
                                                consSubsidyUpdateList2.add(consSubsidy);
                                                EvaluTask evaluTask = null;
                                                List<EvaluTask> evaluTaskss = evaluTaskList.stream().filter(evaluation -> evaluation.getEventId().equals(eventId)
                                                        && evaluation.getConsId().equals(consSubsidyInfo.getConsId())
                                                ).collect(Collectors.toList());
                                                if (null != evaluTaskss && evaluTaskss.size() > 0) {
                                                    evaluTask = evaluTaskss.get(0);
                                                }
                                                if (evaluTask != null) {
                                                    evaluTask.setSubsidyId(consSubsidy.getSubsidyId());
                                                    evaluTask.setSubsidyStatus(EvaluTaskEnum.CAL_COMPLETE.getCode());
                                                    evaluTasksUpdateList.add(evaluTask);
                                                }
                                            } else {
                                                //非集成商直接累加用户补贴
                                                subsidyAmount = subsidyAmount.add(consSubsidy.getSettledAmount());
                                            }
                                        }
                                        if (ObjectUtil.isNull(custSubsidy)) {
                                            custSubsidy = new CustSubsidy();
                                            custSubsidy.setEventId(eventId);
                                            custSubsidy.setCustId(custId);
                                            custSubsidy.setIntegrator(evaluCustTask.getIntegrator());
                                            custSubsidy.setCalRule(incentiveStandard);
                                            custSubsidy.setActualCap(custEvaluation.getActualCap());
                                            custSubsidy.setActualEnergy(custEvaluation.getActualEnergy());
                                            custSubsidy.setSubsidyAmount(subsidyAmount);
                                            long id = IdWorker.getId();
                                            custSubsidy.setSubsidyId(id);
                                            evaluCustTask.setSubsidyId(id);
                                            evaluCustTask.setSubsidyStatus(EvaluTaskEnum.CAL_COMPLETE.getCode());
                                            consSubsidyInsertList.add(custSubsidy);
                                            evaluTaskUpdateList.add(evaluCustTask);
                                        } else {
                                            custSubsidy.setSubsidyAmount(subsidyAmount);
                                            evaluCustTask.setSubsidyId(custSubsidy.getSubsidyId());
                                            evaluCustTask.setSubsidyStatus(EvaluTaskEnum.CAL_COMPLETE.getCode());
                                            consSubsidyUpdateList.add(custSubsidy);
                                            evaluTaskUpdateList.add(evaluCustTask);
                                        }

                                    } else {
                                        evaluCustTask.setSubsidyStatus(EvaluTaskEnum.CAL_NO.getCode());
                                        evaluTaskUpdateList.add(evaluCustTask);
                                    }
                                }
                            }
                        }
                    }
                }
                //更新效果评估任务表
                //更新
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
                    String url = dataurl;
                    String user = userName;
                    String password = datapassword;
                    //2）、加载驱动，不需要显示注册驱动
                    Class.forName(driver);
                    //更新同步邀约信息状态
                    String sql = "UPDATE dr_evalu_task set SUBSIDY_STATUS=?,SUBSIDY_ID=?,UPDATE_TIME=? where id=?";
                    if (null != evaluTasksUpdateList && evaluTasksUpdateList.size() > 0) {
                        //获取数据库连接
                        conn = DriverManager.getConnection(url, user, password);
                        conn.setAutoCommit(false);
                        preparedStatement = conn.prepareStatement(sql);
                        int i = 0;
                        for (EvaluTask evaluTask : evaluTasksUpdateList) {
                            preparedStatement.setString(1, evaluTask.getSubsidyStatus());
                            if(null!=evaluTask.getSubsidyId()) {
                                preparedStatement.setLong(2, evaluTask.getSubsidyId());
                            } else {
                                preparedStatement.setNull(2, Types.BIGINT);
                            }
                            preparedStatement.setString(3, dateFormat.format(new Date()));
                            preparedStatement.setLong(4,evaluTask.getId());
                            preparedStatement.addBatch();
                            if ((i + 1) % 1000 == 0 || i == evaluTasksUpdateList.size() - 1) {
                                //每1000条提交一次
                                preparedStatement.executeBatch();
                                //清空记录
                                preparedStatement.clearBatch();
                            }
                            i++;
                        }
                    }
                    log.info("更新用户补贴任务表完成，共:" + evaluTasksUpdateList.size() + "条");
                    String sql2 = "INSERT INTO dr_cust_subsidy (subsidy_id,EVENT_ID,CUST_ID,actual_cap,actual_energy,CONTRACT_PRICE,subsidy_amount," +
                            "cal_rule,REMARK,CREATE_TIME,integrator)VALUES(?,?,?,?,?,?,?,?,?,?,?)";
                    if (null !=consSubsidyInsertList  && consSubsidyInsertList.size() > 0) {
                        //获取数据库连接
                        conn2 = DriverManager.getConnection(url, user, password);
                        conn2.setAutoCommit(false);
                        preparedStatement2 = conn2.prepareStatement(sql2);
                        int i = 0;
                        for (CustSubsidy consSubsidyUpdateOrSave : consSubsidyInsertList) {
                            preparedStatement2.setLong(1, consSubsidyUpdateOrSave.getSubsidyId());
                            preparedStatement2.setLong(2, consSubsidyUpdateOrSave.getEventId());
                            preparedStatement2.setLong(3, consSubsidyUpdateOrSave.getCustId());
                            preparedStatement2.setBigDecimal(4, consSubsidyUpdateOrSave.getActualCap());
                            preparedStatement2.setBigDecimal(5, consSubsidyUpdateOrSave.getActualEnergy());
                            preparedStatement2.setBigDecimal(6, consSubsidyUpdateOrSave.getContractPrice());
                            preparedStatement2.setBigDecimal(7, consSubsidyUpdateOrSave.getSubsidyAmount());
                            if(null!=consSubsidyUpdateOrSave.getCalRule()) {
                                preparedStatement2.setString(8, consSubsidyUpdateOrSave.getCalRule());
                            } else {
                                preparedStatement2.setNull(8, Types.VARCHAR);
                            }
                            if(null!=consSubsidyUpdateOrSave.getRemark()) {
                                preparedStatement2.setString(9, consSubsidyUpdateOrSave.getRemark());
                            } else {
                                preparedStatement2.setNull(9, Types.VARCHAR);
                            }
                            preparedStatement2.setString(10,  dateFormat.format(new Date()));
                            if(null!=consSubsidyUpdateOrSave.getIntegrator()) {
                                preparedStatement2.setString(11, consSubsidyUpdateOrSave.getIntegrator());
                            } else {
                                preparedStatement2.setNull(11, Types.VARCHAR);
                            }
                            preparedStatement2.addBatch();
                            if ((i + 1) % 1000 == 0 || i == consSubsidyInsertList.size() - 1) {
                                //每1000条提交一次
                                preparedStatement2.executeBatch();
                                //清空记录
                                preparedStatement2.clearBatch();
                            }
                            i++;
                        }

                    }
                    log.info("保存客户补贴数据完成，共:" + consSubsidyInsertList.size() + "条");
                    String sql3 = "UPDATE dr_cust_subsidy set integrator=?,EVENT_ID=?,CUST_ID=?,actual_cap=?,actual_energy=?,\n" +
                            "CONTRACT_PRICE=?,subsidy_amount=?,cal_rule=?,REMARK=?,UPDATE_TIME=? where subsidy_id=?";
                    if (null !=consSubsidyUpdateList  && consSubsidyUpdateList.size() > 0) {
                        //获取数据库连接
                        conn3 = DriverManager.getConnection(url, user, password);
                        conn3.setAutoCommit(false);
                        preparedStatement3 = conn3.prepareStatement(sql3);
                        int i = 0;
                        for (CustSubsidy consSubsidyUpdateOrSave : consSubsidyUpdateList) {
                            if(null!=consSubsidyUpdateOrSave.getIntegrator()) {
                                preparedStatement3.setString(1, consSubsidyUpdateOrSave.getIntegrator());
                            } else {
                                preparedStatement3.setNull(1, Types.BIGINT);
                            }
                            preparedStatement3.setLong(2, consSubsidyUpdateOrSave.getEventId());
                            preparedStatement3.setLong(3, consSubsidyUpdateOrSave.getCustId());
                            preparedStatement3.setBigDecimal(4, consSubsidyUpdateOrSave.getActualCap());
                            preparedStatement3.setBigDecimal(5, consSubsidyUpdateOrSave.getActualEnergy());
                            preparedStatement3.setBigDecimal(6, consSubsidyUpdateOrSave.getContractPrice());
                            preparedStatement3.setBigDecimal(7, consSubsidyUpdateOrSave.getSubsidyAmount());
                            if(null!=consSubsidyUpdateOrSave.getCalRule()) {
                                preparedStatement3.setString(8, consSubsidyUpdateOrSave.getCalRule());
                            } else {
                                preparedStatement3.setNull(8, Types.VARCHAR);
                            }
                            if(null!=consSubsidyUpdateOrSave.getRemark()) {
                                preparedStatement3.setString(9, consSubsidyUpdateOrSave.getRemark());
                            } else {
                                preparedStatement3.setNull(9, Types.VARCHAR);
                            }
                            preparedStatement3.setString(10,  dateFormat.format(new Date()));
                            preparedStatement3.setLong(11, consSubsidyUpdateOrSave.getSubsidyId());
                            preparedStatement3.addBatch();
                            if ((i + 1) % 1000 == 0 || i == consSubsidyUpdateList.size() - 1) {
                                //每1000条提交一次
                                preparedStatement3.executeBatch();
                                //清空记录
                                preparedStatement3.clearBatch();
                            }
                            i++;
                        }

                    }
                    log.info("更新客户补贴数据完成，共:" + consSubsidyUpdateList.size() + "条");
                    String sql4 = "UPDATE dr_cons_subsidy set subsidy_amount=?,UPDATE_TIME=? where subsidy_id=?";
                    if (null !=consSubsidyUpdateList2  && consSubsidyUpdateList2.size() > 0) {
                        //获取数据库连接
                        conn4 = DriverManager.getConnection(url, user, password);
                        conn4.setAutoCommit(false);
                        preparedStatement4 = conn4.prepareStatement(sql4);
                        int i = 0;
                        for (ConsSubsidy consSubsidyUpdateOrSave : consSubsidyUpdateList2) {
                            if(null!=consSubsidyUpdateOrSave.getSubsidyAmount()) {
                                preparedStatement4.setBigDecimal(1, consSubsidyUpdateOrSave.getSubsidyAmount());
                            } else {
                                preparedStatement4.setNull(1, Types.BIGINT);
                            }
                            preparedStatement4.setString(2,  dateFormat.format(new Date()));
                            preparedStatement4.setLong(3, consSubsidyUpdateOrSave.getSubsidyId());
                            preparedStatement4.addBatch();
                            if ((i + 1) % 1000 == 0 || i == consSubsidyUpdateList2.size() - 1) {
                                //每1000条提交一次
                                preparedStatement4.executeBatch();
                                //清空记录
                                preparedStatement4.clearBatch();
                            }
                            i++;
                        }

                    }
                    log.info("更新用户补贴数据完成，共:" + consSubsidyUpdateList2.size() + "条");
                    String sql5 = "UPDATE dr_evalu_cust_task set SUBSIDY_STATUS=?,SUBSIDY_ID=?,SUBSIDY_DESC=?,UPDATE_TIME=? where id=?";
                    if (null != evaluTaskUpdateList && evaluTaskUpdateList.size() > 0) {
                        //获取数据库连接
                        conn5 = DriverManager.getConnection(url, user, password);
                        conn5.setAutoCommit(false);
                        preparedStatement5 = conn5.prepareStatement(sql5);
                        int i = 0;
                        for (EvaluCustTask evaluTask : evaluTaskUpdateList) {
                            preparedStatement5.setString(1, evaluTask.getSubsidyStatus());
                            if(null!=evaluTask.getSubsidyId()) {
                                preparedStatement5.setLong(2, evaluTask.getSubsidyId());
                            } else {
                                preparedStatement5.setNull(2, Types.BIGINT);
                            }
                            if(null!=evaluTask.getSubsidyDesc()) {
                                preparedStatement5.setString(3, evaluTask.getSubsidyDesc());
                            } else {
                                preparedStatement5.setNull(3, Types.BIGINT);
                            }
                            preparedStatement5.setString(4, dateFormat.format(new Date()));
                            preparedStatement5.setLong(5,evaluTask.getId());
                            preparedStatement5.addBatch();
                            if ((i + 1) % 1000 == 0 || i == evaluTaskUpdateList.size() - 1) {
                                //每1000条提交一次
                                preparedStatement5.executeBatch();
                                //清空记录
                                preparedStatement5.clearBatch();
                            }
                            i++;
                        }
                    }
                    log.info("更新客户补贴任务表完成，共:" + evaluTaskUpdateList.size() + "条");
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
                    log.info("客户补贴计算完成!");
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

    public void generateConsDailySubsidy(String param) {
        LocalDate currentDate = LocalDate.now().minusDays(1);
        if (param != null && !param.equals("")) {
            currentDate = LocalDate.parse(param);
        }
        //根据日期查询用户补贴汇总
        List<ConsSubsidyDaily> consSubsidyDailyList = consSubsidyDailyMapper.getSubsidyDailyTotal(simpleDateFormat.format(currentDate));
        //根据日期查询项目和事件关系
        List<ConsSubsidyDaily> consSubsidys = consSubsidyDailyMapper.getAllSubsidyDailyByDate(simpleDateFormat.format(currentDate));
        //根据日期查询日补贴记录
        LambdaQueryWrapper<ConsSubsidyDaily> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ConsSubsidyDaily::getSubsidyDate,simpleDateFormat.format(currentDate));
        List<ConsSubsidyDaily> consSubsidyDailies = consSubsidyDailyService.list(queryWrapper);
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
                    for(ConsSubsidyDaily event : events) {
                        eventIds = eventIds+","+ event.getEventId();
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
                    updateList.add(consSubsidyDaily);
                } else {
                    inserList.add(consSubsidyDaily);
                }
            }
        }
        if(null!=updateList && updateList.size()>0) {
            consSubsidyDailyService.updateBatchById(updateList);
        }
        log.info("更新用户日补贴完成，共："+updateList.size() + "条！");

        if(null!=inserList && inserList.size()>0) {
            consSubsidyDailyService.saveBatch(inserList);
        }
        log.info("新增用户日补贴完成，共："+inserList.size() + "条！");
    }

    public void generateCustDailySubsidy(String param) {
        LocalDate currentDate = LocalDate.now().minusDays(1);
        if (param != null && !param.equals("")) {
            currentDate = LocalDate.parse(param);
        }
        //根据日期查询用户补贴汇总
        List<CustSubsidyDaily> consSubsidyDailyList = custSubsidyDailyMapper.getSubsidyDailyTotal(simpleDateFormat.format(currentDate));
        //根据日期查询项目和事件关系
        List<CustSubsidyDaily> consSubsidys = custSubsidyDailyMapper.getAllSubsidyDailyByDate(simpleDateFormat.format(currentDate));
        //根据日期查询日补贴记录
        LambdaQueryWrapper<CustSubsidyDaily> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CustSubsidyDaily::getSubsidyDate,simpleDateFormat.format(currentDate));
        List<CustSubsidyDaily> consSubsidyDailies = custSubsidyDailyService.list(queryWrapper);
        List<CustSubsidyDaily> inserList = new ArrayList<>();
        List<CustSubsidyDaily> updateList = new ArrayList<>();
        if(null!=consSubsidyDailyList && consSubsidyDailyList.size()>0) {
            for(CustSubsidyDaily consSubsidyDaily : consSubsidyDailyList) {
                consSubsidyDaily.setSubTime(LocalDateTime.now());
                //根据项目事件对应关系，设置用户参与事件
                List<CustSubsidyDaily> events =consSubsidys.stream().filter(dayily -> dayily.getCustId().equals(consSubsidyDaily.getCustId())
                        && dayily.getProjectId().equals(consSubsidyDaily.getProjectId())
                ).collect(Collectors.toList());
                if(null!=events && events.size()>0) {
                    consSubsidyDaily.setEventNum(events.size());
                    String eventIds = "";
                    for(CustSubsidyDaily event : events) {
                        eventIds = eventIds+","+ event.getEventId();
                    }
                    //截取第一个逗号
                    eventIds = eventIds.substring(1);
                    consSubsidyDaily.setEventIds(eventIds);
                } else {
                    continue;
                }
                //查找日补贴是否已经存在
                List<CustSubsidyDaily> isExist = consSubsidyDailies.stream().filter(dayily -> dayily.getCustId().equals(consSubsidyDaily.getCustId())
                        && dayily.getProjectId().equals(consSubsidyDaily.getProjectId())
                ).collect(Collectors.toList());
                if(null!=isExist && isExist.size()>0) {
                    consSubsidyDaily.setId(isExist.get(0).getId());
                    updateList.add(consSubsidyDaily);
                } else {
                    inserList.add(consSubsidyDaily);
                }
            }
        }
        if(null!=updateList && updateList.size()>0) {
            custSubsidyDailyService.updateBatchById(updateList);
        }
        log.info("更新客户日补贴完成，共："+updateList.size() + "条！");

        if(null!=inserList && inserList.size()>0) {
            custSubsidyDailyService.saveBatch(inserList);
        }
        log.info("新增客户日补贴完成，共："+inserList.size() + "条！");

    }

    public void consMonthSubsidyCal(String param) {

        LocalDate currentDate = LocalDate.now();
        if (param != null && !param.equals("")) {
            currentDate = LocalDate.parse(param);
        }

        int year = currentDate.getYear();
        int month = currentDate.getMonthValue();
        month -= 1;
        if (month == 0) {
            month = 12;
            year -= 1;
        }
        String m = month > 9 ? (month+"") : "0" + month;
        String subsidyMonth = year + "-" + m;

        List<SubsidyMonthly> subsidyMonthlyList = subsidyMonthlyMapper.getMonthSubsidy(subsidyMonth);
        if (subsidyMonthlyList != null && subsidyMonthlyList.size() > 0) {
            return;
        }
        List<CustSubsidyMonthlyInfo> custSubsidyMonthlyInfos = subsidyMonthlyMapper.getConsSubsidyMonthly(subsidyMonth);
        List<SubsidyMonthly> subsidies = new ArrayList<>();
        for (CustSubsidyMonthlyInfo custSubsidyMonthlyInfo: custSubsidyMonthlyInfos) {
            SubsidyMonthly temp = new SubsidyMonthly();
            temp.setConsId(String.valueOf(custSubsidyMonthlyInfo.getConsId()));
            temp.setSubsidyMonth(subsidyMonth);
            temp.setTotalAmount(custSubsidyMonthlyInfo.getSettledAmount());
            subsidies.add(temp);
        }

        subsidyMonthlyService.saveBatch(subsidies);
    }

    public void custMonthSubsidyCal(String param) {

        LocalDate currentDate = LocalDate.now();
        if (param != null && !param.equals("")) {
            currentDate = LocalDate.parse(param);
        }

        int year = currentDate.getYear();
        int month = currentDate.getMonthValue();
        month -= 1;
        if (month == 0) {
            month = 12;
            year -= 1;
        }
        String m = month > 9 ? (month+"") : "0" + month;
        String subsidyMonth = year + "-" + m;

        List<SubsidyMonthlyCust> subsidyMonthlyCustList = subsidyMonthlyCustMapper.getMonthSubsidy(subsidyMonth);
        if (subsidyMonthlyCustList != null && subsidyMonthlyCustList.size() < 0) {
            return;
        }
        List<CustSubsidyMonthlyInfo> custSubsidyMonthlyInfos = subsidyMonthlyCustMapper.getCustSubsidyMonthly(subsidyMonth);
        List<SubsidyMonthlyCust> subsidies = new ArrayList<>();
        for (CustSubsidyMonthlyInfo custSubsidyMonthlyInfo: custSubsidyMonthlyInfos) {
            SubsidyMonthlyCust temp = new SubsidyMonthlyCust();
            temp.setCustId(String.valueOf(custSubsidyMonthlyInfo.getCustId()));
            temp.setSubsidyMonth(subsidyMonth);
            temp.setIntegrator(custSubsidyMonthlyInfo.getIntegrator());
            temp.setTotalAmount(custSubsidyMonthlyInfo.getSettledAmount());
            subsidies.add(temp);
        }

        subsidyMonthlyCustService.saveBatch(subsidies);
    }


}
