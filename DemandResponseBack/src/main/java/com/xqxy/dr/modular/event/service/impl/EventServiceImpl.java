package com.xqxy.dr.modular.event.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.thread.ExecutorBuilder;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.log.Log;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.core.client.SystemClient;
import com.xqxy.core.client.SystemClientService;
import com.xqxy.core.consts.PageConstant;
import com.xqxy.core.enums.YesOrNotEnum;
import com.xqxy.core.exception.ServiceException;
import com.xqxy.core.pojo.base.entity.BaseEntity;
import com.xqxy.core.pojo.login.CurrenUserInfo;
import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.core.util.SecurityUtils;
import com.xqxy.core.util.SmsUtils;
import com.xqxy.core.util.TimeUtil;
import com.xqxy.dr.modular.dispatch.entity.Dispatch;
import com.xqxy.dr.modular.dispatch.entity.OrgDemand;
import com.xqxy.dr.modular.dispatch.service.DispatchService;
import com.xqxy.dr.modular.dispatch.service.OrgDemandService;
import com.xqxy.dr.modular.evaluation.entity.ConsEvaluation;
import com.xqxy.dr.modular.evaluation.service.ConsEvaluationService;
import com.xqxy.dr.modular.event.VO.EventVO;
import com.xqxy.dr.modular.event.entity.*;
import com.xqxy.dr.modular.event.enums.CheckStatusEnum;
import com.xqxy.dr.modular.event.enums.EventExceptionEnum;
import com.xqxy.dr.modular.event.enums.EventResponseTypeEnum;
import com.xqxy.dr.modular.event.enums.EventStatusEnum;
import com.xqxy.dr.modular.event.mapper.EventMapper;
import com.xqxy.dr.modular.event.param.EventParam;
import com.xqxy.dr.modular.event.service.*;
import com.xqxy.dr.modular.project.entity.ConsContractInfo;
import com.xqxy.dr.modular.project.entity.Project;
import com.xqxy.dr.modular.project.enums.AdvanceNoticeEnums;
import com.xqxy.dr.modular.project.enums.ProjectTimeTypeEnum;
import com.xqxy.dr.modular.project.enums.ResponseTypeEnum;
import com.xqxy.dr.modular.project.result.Region;
import com.xqxy.dr.modular.project.service.ConsContractInfoService;
import com.xqxy.dr.modular.project.service.ProjectService;
import com.xqxy.dr.modular.strategy.CalculateStrategy;
import com.xqxy.dr.modular.strategy.CalculateStrategyContext;
import com.xqxy.dr.modular.subsidy.entity.CustSubsidyDaily;
import com.xqxy.dr.modular.subsidy.mapper.CustSubsidyDailyMapper;
import com.xqxy.sys.modular.calendar.entity.CalendarInfo;
import com.xqxy.sys.modular.calendar.enums.CalendarInfoTypeEnum;
import com.xqxy.sys.modular.calendar.param.CalendarInfoParam;
import com.xqxy.sys.modular.calendar.service.CalendarInfoService;
import com.xqxy.sys.modular.cust.result.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * 需求响应事件 服务实现类
 * </p>
 *
 * @author PengChuqing
 * @since 2021-10-08
 */
@Service
public class EventServiceImpl extends ServiceImpl<EventMapper, Event> implements EventService {

    private static final Log log = Log.get();

    @Value("${calculateStrategy}")
    private String calculateStrategyValue;

    @Resource
    private ProjectService projectService;

    @Resource
    private CustSubsidyDailyMapper custSubsidyDailyMapper;

    @Resource
    private BaselineLibraryService baselineLibraryService;

    @Resource
    private CalculateStrategyContext calculateStrategyContext;

    @Resource
    private ConsContractInfoService consContractInfoService;

    @Resource
    private EventMonitorTaskService eventMonitorTaskService;

    @Resource
    private ConsInvitationService consInvitationService;

    @Resource
    ConsBaselineService consBaselineService;

    @Resource
    SystemClientService systemClientService;

    @Resource
    private CalendarInfoService calendarInfoService;

    @Resource
    private EventService eventService;

    @Value("${spring.datasource.mysql.username}")
    private String userName;

    @Value("${spring.datasource.mysql.jdbc-url}")
    private String dataurl;

    @Value("${spring.datasource.mysql.password}")
    private String datapassword;

    @Value("${spring.datasource.mysql.driver-class-name}")
    private String driver;

    @Value("${executor.corePoolSize}")
    private int corePoolSize;

    @Value("${executor.maxPoolSize}")
    private int maxPoolSize;

    @Value("${executor.workQueue}")
    private int workQueue;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ConsEvaluationService consEvaluationService;

    @Autowired
    private PlanService planService;

    @Autowired
    private DispatchService dispatchService;

    @Override
    // @NeedSetValueField
    public Page<Event> page(EventParam eventParam) {
        LambdaQueryWrapper<Event> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(eventParam)) {
            // 根据事件编号模糊查询
            if (ObjectUtil.isNotEmpty(eventParam.getEventNo())) {
                queryWrapper.like(Event::getEventNo, eventParam.getEventNo());
            }
            //根据事件名称模糊查询
            if (ObjectUtil.isNotEmpty(eventParam.getEventName())) {
                queryWrapper.like(Event::getEventName, eventParam.getEventName());
            }
            //根据响应日期查询
            if (ObjectUtil.isNotEmpty(eventParam.getRegulateDate())) {
                queryWrapper.eq(Event::getRegulateDate, eventParam.getRegulateDate());
            }
            //根据事件状态查询
            if (ObjectUtil.isNotEmpty(eventParam.getEventStatus())) {
                queryWrapper.eq(Event::getEventStatus, eventParam.getEventStatus());
            }
            // 工作台 根据所属项目查询
            if (ObjectUtil.isNotEmpty(eventParam.getProjectId())) {
                queryWrapper.eq(Event::getProjectId, eventParam.getProjectId());
            }
            // 根据响应开始日期查询
            /*if (ObjectUtil.isNotEmpty(eventParam.getRegulateDate())) {
                queryWrapper.ge(Event::getRegulateDate, eventParam.getRegulateDate());
            }*/
            // 根据查询开始时间查询
            if (ObjectUtil.isNotEmpty(eventParam.getSearchBeginTime())) {
                queryWrapper.ge(Event::getStartTime, eventParam.getSearchBeginTime());
            }
            // 根据查询结束时间查询
            if (ObjectUtil.isNotEmpty(eventParam.getSearchEndTime())) {
                queryWrapper.le(Event::getEndTime, eventParam.getSearchEndTime());
            }
            //根据事件类型查询
            if (ObjectUtil.isNotEmpty(eventParam.getResponseType())) {
                queryWrapper.eq(Event::getResponseType, eventParam.getResponseType());
            }
            // 根据事件状态查询
            if (ObjectUtil.isNotEmpty(eventParam.getStatusList())) {
                queryWrapper.in(Event::getEventStatus, eventParam.getStatusList());
            }
            // 根据事件标识集合查询
            if (ObjectUtil.isNotEmpty(eventParam.getEventIdList())) {
                queryWrapper.in(Event::getEventId, eventParam.getEventIdList());
            }

            // 工作台 单位选择 事件下拉框接口
            if (ObjectUtil.isNotEmpty(eventParam.getRegulateRange())) {
                queryWrapper.like(Event::getRegulateRange, eventParam.getRegulateRange());
            }
            // 工作台 年份选择 事件下拉框接口
            if (ObjectUtil.isNotEmpty(eventParam.getYear())) {
                queryWrapper.like(Event::getCreateTime, eventParam.getYear());
            }
        }

        //根据排序升序排列，序号越小越在前
        // queryWrapper.orderByDesc(Event::getUpdateTime);
//        queryWrapper.orderByDesc(Event::getCreateTime);
        queryWrapper.orderByDesc(Event::getRegulateDate);
        Page<Event> eventPage = this.page(eventParam.getPage(), queryWrapper);
        List<Event> list = eventPage.getRecords();
        LocalDate localDate = LocalDate.now();
        List<Event> updateList = new ArrayList<>();
        if (null != list && list.size() > 0) {
            for (Event event : list) {
                //设置事件状态
                if (null != list) {
                    if (null != event && "01".equals(event.getEventStatus())) {
                        Integer endHour = 0;
                        Integer endMinute = 0;
                        if (null != event.getStartTime()) {
                            endHour = Integer.parseInt(event.getStartTime().substring(0, 2));
                            endMinute = Integer.parseInt(event.getStartTime().substring(3));
                        }
                        LocalTime startTime = LocalTime.of(endHour, endMinute);
                        startTime = startTime.plusMinutes(30);
                        if (event.getRegulateDate().compareTo(localDate) == 0) {
                            if (LocalTime.now().compareTo(startTime) > 0) {
                                event.setEventStatus("05");
                                updateList.add(event);
                            }
                        } else if (event.getRegulateDate().compareTo(localDate) < 0) {
                            event.setEventStatus("05");
                            updateList.add(event);
                        }
                    }
                }
            }
        }
        if (null != updateList && updateList.size() > 0) {
            this.updateBatchById(updateList);
        }
        //调度负荷单位转换
        if (null != list && list.size() > 0) {
            for (Event event : list) {
                if (null != event && null != (event.getRegulateCap())) {
                    event.setRegulateCap(NumberUtil.div(event.getRegulateCap(), 10000));
                }
            }
        }
        eventPage.setRecords(list);
        return eventPage;
    }

    @Override
    public Object list(EventParam eventParam) {
        LambdaQueryWrapper<Event> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(eventParam)) {
            // 根据项目编号查询查询
            if (ObjectUtil.isNotEmpty(eventParam.getProjectId())) {
                queryWrapper.like(Event::getProjectId, eventParam.getProjectId());
            }
            // 根据事件编号模糊查询
            if (ObjectUtil.isNotEmpty(eventParam.getEventNo())) {
                queryWrapper.like(Event::getEventNo, eventParam.getEventNo());
            }
            // 根据响应开始日期查询
            if (ObjectUtil.isNotEmpty(eventParam.getRegulateDate())) {
                queryWrapper.eq(Event::getRegulateDate, eventParam.getRegulateDate());
            }
            // 根据查询开始时间查询
            if (ObjectUtil.isNotEmpty(eventParam.getSearchBeginTime())) {
                queryWrapper.ge(Event::getStartTime, eventParam.getSearchBeginTime());
            }
            // 根据查询结束时间查询
            if (ObjectUtil.isNotEmpty(eventParam.getSearchEndTime())) {
                queryWrapper.le(Event::getEndTime, eventParam.getSearchEndTime());
            }
            //根据事件类型查询
            if (ObjectUtil.isNotEmpty(eventParam.getResponseType())) {
                queryWrapper.eq(Event::getResponseType, eventParam.getResponseType());
            }
            // 根据事件状态查询
            if (ObjectUtil.isNotEmpty(eventParam.getStatusList())) {
                queryWrapper.in(Event::getEventStatus, eventParam.getStatusList());
            }
            if (ObjectUtil.isNotEmpty(eventParam.getSynchToPlan())) {
                queryWrapper.in(Event::getSynchToPlan, eventParam.getSynchToPlan());
            }
        }
        //根据排序升序排列，序号越小越在前
        queryWrapper.orderByAsc(Event::getCreateTime);
        return this.list(queryWrapper);
    }

    @Override
    public Object listToPlan(EventParam eventParam) {
        LambdaQueryWrapper<Event> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(eventParam)) {
            // 根据项目编号查询查询
            if (ObjectUtil.isNotEmpty(eventParam.getProjectId())) {
                queryWrapper.like(Event::getProjectId, eventParam.getProjectId());
            }
            // 根据事件编号模糊查询
            if (ObjectUtil.isNotEmpty(eventParam.getEventNo())) {
                queryWrapper.like(Event::getEventNo, eventParam.getEventNo());
            }
            // 根据响应开始日期查询
            if (ObjectUtil.isNotEmpty(eventParam.getRegulateDate())) {
                queryWrapper.ge(Event::getRegulateDate, eventParam.getRegulateDate());
            }
            // 根据查询开始时间查询
            if (ObjectUtil.isNotEmpty(eventParam.getSearchBeginTime())) {
                queryWrapper.ge(Event::getStartTime, eventParam.getSearchBeginTime());
            }
            // 根据查询结束时间查询
            if (ObjectUtil.isNotEmpty(eventParam.getSearchEndTime())) {
                queryWrapper.le(Event::getEndTime, eventParam.getSearchEndTime());
            }
            //根据事件类型查询
            if (ObjectUtil.isNotEmpty(eventParam.getResponseType())) {
                queryWrapper.eq(Event::getResponseType, eventParam.getResponseType());
            }
            //根据时间类型查询
            if (ObjectUtil.isNotEmpty(eventParam.getTimeType())) {
                queryWrapper.eq(Event::getTimeType, eventParam.getTimeType());
            }
            // 根据事件状态查询
            if (ObjectUtil.isNotEmpty(eventParam.getStatusList())) {
                queryWrapper.in(Event::getEventStatus, eventParam.getStatusList());
            }
            if (ObjectUtil.isNotEmpty(eventParam.getSynchToPlan())) {
                queryWrapper.in(Event::getSynchToPlan, eventParam.getSynchToPlan());
            }
        }
        //根据排序升序排列，序号越小越在前
        queryWrapper.orderByAsc(Event::getCreateTime);
        return this.list(queryWrapper);
    }

    @Override
    public void delete(EventParam eventParam) {
        LambdaQueryWrapper<Event> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Event::getEventId, eventParam.getEventId());
        this.remove(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(EventParam eventParam) {
        // 查询事件所属项目
        Project project = projectService.getById(eventParam.getProjectId());
        //
        Event event = new Event();
        BeanUtil.copyProperties(eventParam, event);
        event.setRegulateDate(eventParam.getRegulateDate());
        // 将状态更新为”保存“
        event.setEventStatus(EventStatusEnum.STATUS_SAVE.getCode());
        event.setCheckStatus(CheckStatusEnum.UNSUBMITTED.getCode());
        //生成事件编号
        String year = String.valueOf(event.getRegulateDate().getYear());
        String lastfour = this.getMaxEventNo(year);
        year = year + lastfour;
        event.setEventNo(Long.parseLong(year));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String oneDay = formatter.format(event.getRegulateDate());
        if ("1".equals(event.getResponseType()) && "1".equals(event.getTimeType())) {
            event.setEventName(oneDay + "日" + event.getStartTime() + "-" + event.getEndTime() + EventResponseTypeEnum.PEAK_OF_INVITATION.getMessage() + "事件");
        }
        if ("1".equals(event.getResponseType()) && "2".equals(event.getTimeType())) {
            event.setEventName(oneDay + "日" + event.getStartTime() + "-" + event.getEndTime() + EventResponseTypeEnum.PEAK_OF_REALTIME.getMessage() + "事件");
        }
        if ("2".equals(event.getResponseType()) && "1".equals(event.getTimeType())) {
            event.setEventName(oneDay + "日" + event.getStartTime() + "-" + event.getEndTime() + EventResponseTypeEnum.FILL_OF_INVITATION.getMessage() + "事件");
        }
        if ("2".equals(event.getResponseType()) && "2".equals(event.getTimeType())) {
            event.setEventName(oneDay + "日" + event.getStartTime() + "-" + event.getEndTime() + EventResponseTypeEnum.FILL_OF_REALTIME.getMessage() + "事件");
        }
        //保存
        event.setSynchToPlan(YesOrNotEnum.N.getCode());
        // 插入一条基线库记录
        BaselineLibrary baselineLibrary = new BaselineLibrary();
        baselineLibrary.setGenerateDate(eventParam.getRegulateDate());
        baselineLibrary.setStartPeriod(event.getStartTime());
        baselineLibrary.setEndPeriod(event.getEndTime());
        baselineLibrary.setCalRule(project.getBaseLineCal());
        baselineLibrary.setGenType("1");
        baselineLibrary.setDescr(event.getEventName() + "默认基线"); // 基线描述
        baselineLibrary.setBaselineType("1"); //基线类型

        List<String> dateList = null;
        if ("2".equals(project.getBaseLineCal())) {
            dateList = this.sampleDates(event);
        } else {
            dateList = TimeUtil.lastSevenDays(event.getRegulateDate(), 7);
        }
        baselineLibrary.setSimplesDate(StringUtils.join(dateList.toArray(), ",")); // 样本负荷日期
        baselineLibraryService.save(baselineLibrary);
        // 没有需求对应的事件保存，有对应的事件，则更新
        event.setIncentiveStandard(project.getIncentiveStandard());
        event.setValidityJudgment(project.getValidityJudgment());
        event.setBaselinId(baselineLibrary.getBaselinId());
        event.setBaselineStatus("N");
        event.setBaselineNum(0);
        this.save(event);
        //将事件信息存入redis
        /*SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String data = dateFormat.format(new Date());
        List<JSONObject> list = new ArrayList<>();
        JSONObject map = new JSONObject();
        map.put("eventId",event.getEventId());
        map.put("startTime",event.getStartTime());
        map.put("endTime",event.getEndTime());
        list.add(map);
        redisTemplate.opsForList().rightPushAll(data, list.toArray());*/

        // 将事件关联的方案参与用户数据同步到基线任务表中
        ExecutorService executor = ExecutorBuilder.create().setCorePoolSize(corePoolSize).setMaxPoolSize(maxPoolSize).setWorkQueue(new LinkedBlockingQueue<>(workQueue)).build();
        executor.execute(this.synchBaselineTask(event, project, baselineLibrary.getBaselinId(), eventParam));
        executor.shutdown();
        return event.getEventId();
    }

    @Override
    public Long update(EventParam eventParam) {
        UpdateWrapper<Event> updateWrapper = new UpdateWrapper<>();
        if (null != eventParam.getEventStatus() && !"".equals(eventParam.getEventStatus())) {
            updateWrapper.eq("event_id", eventParam.getEventId()).set("event_status", eventParam.getEventStatus());
        }
        if (null != eventParam.getSubsidyPub() && !"".equals(eventParam.getSubsidyPub())) {
            updateWrapper.eq("event_id", eventParam.getEventId()).set("subsidy_pub", eventParam.getSubsidyPub());
        }
        boolean b = this.update(updateWrapper);
        return b ? eventParam.getEventId() : -1;

    }

    @Override
    public ResponseData updateEventAdvance(EventParam eventParam) {
        boolean b = false;
        Event event = this.getById(eventParam.getEventId());
        if (null == event) {
            log.info("事件信息不存在!");
            return ResponseData.fail("-1", "事件信息不存在!", "事件信息不存在!");
        }
        if ("04".equals(event.getEventStatus())) {
            log.info("事件已结束!");
            return ResponseData.fail("-1", "事件已结束!", "事件已结束!");
        }
        Long regulateId = event.getRegulateId();
        Plan plan = planService.getByEventId(eventParam.getEventId());
        if (null == plan) {
            log.info("方案信息不存在!");
            return ResponseData.fail("-1", "方案信息不存在!", "方案信息不存在!");
        }
        UpdateWrapper<Event> updateWrapper = new UpdateWrapper<>();
        UpdateWrapper<Plan> updatePlanWrapper = new UpdateWrapper<>();
        UpdateWrapper<Dispatch> updateDisWrapper = new UpdateWrapper<>();
        //提前终止
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String oneDay = formatter.format(event.getRegulateDate());
        String endTime = event.getEndTime();
        String startTime = event.getStartTime();
        if (null != eventParam.getEndTime() && !"".equals(eventParam.getEndTime())) {
            endTime = eventParam.getEndTime();
        }
        if (null != eventParam.getStartTime() && !"".equals(eventParam.getStartTime())) {
            startTime = eventParam.getStartTime();
        }
        if ("1".equals(event.getResponseType()) && "1".equals(event.getTimeType())) {
            event.setEventName(oneDay + "日" + startTime + "-" + endTime + EventResponseTypeEnum.PEAK_OF_INVITATION.getMessage() + "事件");
        }
        if ("1".equals(event.getResponseType()) && "2".equals(event.getTimeType())) {
            event.setEventName(oneDay + "日" + startTime + "-" + endTime + EventResponseTypeEnum.PEAK_OF_REALTIME.getMessage() + "事件");
        }
        if ("2".equals(event.getResponseType()) && "1".equals(event.getTimeType())) {
            event.setEventName(oneDay + "日" + startTime + "-" + endTime + EventResponseTypeEnum.FILL_OF_INVITATION.getMessage() + "事件");
        }
        if ("2".equals(event.getResponseType()) && "2".equals(event.getTimeType())) {
            event.setEventName(oneDay + "日" + startTime + "-" + endTime + EventResponseTypeEnum.FILL_OF_REALTIME.getMessage() + "事件");
        }
        updateWrapper.eq("event_id", eventParam.getEventId()).set("END_TIME", endTime);
        updateWrapper.eq("event_id", eventParam.getEventId()).set("START_TIME", startTime);
        updateWrapper.eq("event_id", eventParam.getEventId()).set("EVENT_NAME", event.getEventName());
        updatePlanWrapper.eq("plan_id", plan.getPlanId()).set("END_TIME", endTime);
        updatePlanWrapper.eq("plan_id", plan.getPlanId()).set("START_TIME", startTime);
        updateDisWrapper.eq("REGULATE_ID", regulateId).set("END_TIME", endTime);
        updateDisWrapper.eq("REGULATE_ID", regulateId).set("START_TIME", startTime);
        try {
            boolean c = this.update(updateWrapper);
            boolean d = planService.update(updatePlanWrapper);
            boolean e = dispatchService.update(updateDisWrapper);
            if (c && d && e) {
                b = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (b) {
            return ResponseData.success();
        } else {
            return ResponseData.fail("-1", "修改结束日期失败!", "修改结束日期失败!");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Runnable synchBaselineTask(Event event, Project project, Long baselineId, EventParam eventParam) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                PreparedStatement preparedStatement = null;
                Connection conn = null;
                String sql = "insert into dr_plan_baseline_task(BASELIN_ID,START_TIME,END_TIME,CONS_ID,BASELINE_STATUS,BASE_LINE_CAL,CREATE_USER,CREATE_TIME,FAIL_TIMES)\n" + "            values(?,?,?\n" + ",?,?,?,?,?,?)";
                PreparedStatement preparedStatement2 = null;
                Connection conn2 = null;
                String sql2 = "insert into dr_plan_baseline_task_all(BASELIN_ID,START_TIME,END_TIME,CONS_ID,BASELINE_STATUS,BASE_LINE_CAL,CREATE_USER,CREATE_TIME,FAIL_TIMES)\n" + "            values(?,?,?\n" + ",?,?,?,?,?,?)";
                List<ConsContractInfo> consContractList = eventParam.getConsContractInfos();
                // 将数据同步到基线任务表中
                try {
                    String url = dataurl;
                    String user = userName;
                    String password = datapassword;
                    //2）、加载驱动，不需要显示注册驱动
                    Class.forName(driver);
                    //获取数据库连接
                    conn = DriverManager.getConnection(url, user, password);
                    conn.setAutoCommit(false);
                    preparedStatement = conn.prepareStatement(sql);
                    //全部点计算基线任务
                    conn2 = DriverManager.getConnection(url, user, password);
                    conn2.setAutoCommit(false);
                    preparedStatement2 = conn2.prepareStatement(sql2);
                    if (null != consContractList && consContractList.size() > 0) {
                        for (int i = 0; i < consContractList.size(); i++) {
                            String consId = consContractList.get(i).getConsId();
                            preparedStatement.setLong(1, baselineId);
                            if (null != event.getStartTime()) {
                                preparedStatement.setString(2, event.getStartTime());
                            } else {
                                preparedStatement.setNull(2, Types.VARCHAR);
                            }
                            if (null != event.getEndTime()) {
                                preparedStatement.setString(3, event.getEndTime());
                            } else {
                                preparedStatement.setNull(3, Types.VARCHAR);
                            }
                            if (null != consContractList.get(i).getConsId()) {
                                preparedStatement.setString(4, consId);
                            } else {
                                preparedStatement.setNull(4, Types.VARCHAR);
                            }
                            preparedStatement.setString(5, "1");
                            if (null != project.getBaseLineCal()) {
                                preparedStatement.setString(6, project.getBaseLineCal());
                            } else {
                                preparedStatement.setNull(6, Types.VARCHAR);
                            }
                            preparedStatement.setString(7, "-1");
                            preparedStatement.setString(8, dateFormat.format(new Date()));
                            preparedStatement.setInt(9, 0);
                            preparedStatement.addBatch();
                            if ((i + 1) % 1000 == 0 || i == consContractList.size() - 1) {
                                //每1000条提交一次
                                preparedStatement.executeBatch();
                                //清空记录
                                preparedStatement.clearBatch();
                            }
                            preparedStatement2.setLong(1, baselineId);
                            if (null != event.getStartTime()) {
                                preparedStatement2.setString(2, event.getStartTime());
                            } else {
                                preparedStatement2.setNull(2, Types.VARCHAR);
                            }
                            if (null != event.getEndTime()) {
                                preparedStatement2.setString(3, event.getEndTime());
                            } else {
                                preparedStatement2.setNull(3, Types.VARCHAR);
                            }
                            if (null != consContractList.get(i).getConsId()) {
                                preparedStatement2.setString(4, consId);
                            } else {
                                preparedStatement2.setNull(4, Types.VARCHAR);
                            }
                            preparedStatement2.setString(5, "1");
                            if (null != project.getBaseLineCal()) {
                                preparedStatement2.setString(6, project.getBaseLineCal());
                            } else {
                                preparedStatement2.setNull(6, Types.VARCHAR);
                            }
                            preparedStatement2.setString(7, "-1");
                            preparedStatement2.setString(8, dateFormat.format(new Date()));
                            preparedStatement2.setInt(9, 0);
                            preparedStatement2.addBatch();
                            if ((i + 1) % 1000 == 0 || i == consContractList.size() - 1) {
                                //每1000条提交一次
                                preparedStatement2.executeBatch();
                                //清空记录
                                preparedStatement2.clearBatch();
                            }
                        }
                        conn.commit();
                        conn2.commit();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (null != conn) {
                        try {
                            conn.close();
                        } catch (SQLException e) {
                            e.getMessage();
                        }
                    }
                    if (null != preparedStatement) {
                        try {
                            preparedStatement.close();
                        } catch (SQLException e) {
                            e.getMessage();
                        }

                    }
                    if (null != conn2) {
                        try {
                            conn2.close();
                        } catch (SQLException e) {
                            e.getMessage();
                        }
                    }
                    if (null != preparedStatement2) {
                        try {
                            preparedStatement2.close();
                        } catch (SQLException e) {
                            e.getMessage();
                        }

                    }
                }

            }
        };
        return runnable;
    }

    private String getMaxEventNo(String year) {
        Long maxNo = this.getMaxNo(year);
        String str = "0001";
        if (ObjectUtil.isNull(maxNo)) {
            return str;
        }
        str = maxNo.toString();
        if (str.length() >= 4) {
            int length = str.length() - 4;
            return str.substring(length);
        } else {
            Long along = Long.parseLong(str);
            Format f1 = new DecimalFormat("0000");
            String count = f1.format(along);
            return count;
        }
    }

    @Override
    public Long getMaxNo(String year) {
        return getBaseMapper().getMaxNo(year);
    }

    @Override
    public void release(Long eventId, String deadlineTime, String replySource, Integer regulateMultiple, Integer endCondition) {
        if (null == deadlineTime && null == regulateMultiple) {
            throw new ServiceException(EventExceptionEnum.NO_TIME);
        } else {
            LocalDateTime firstRoundDeadlineTime = null;
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            if (null != deadlineTime) {
                firstRoundDeadlineTime = LocalDateTime.from(dateTimeFormatter.parse(deadlineTime));
            }
            Event event = eventService.getById(eventId);
            //只有待执行和发布状态可以发布,且事件开始之前
            if (!"06".equals(event.getEventStatus()) && !"02".equals(event.getEventStatus())) {
                throw new ServiceException(EventExceptionEnum.CANNOT_RELEASE);
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
            // 将方案表中的用户同步到邀约表中
            CalculateStrategy calculateStrategy = calculateStrategyContext.strategySelect(calculateStrategyValue);
            calculateStrategy.createFirstInvitationList(eventId, firstRoundDeadlineTime, replySource, regulateMultiple, endCondition);
        }
        // 生成发送短信
        // smsSendService.generateSms(String.valueOf(eventId), SmsTemplateTypeEnum.INVITATION_NOTICE_ONCE.getCode() );
    }

    @Override
    public List<Long> getEventIdBy(Long eventNo, Long reponseType) {
        LambdaQueryWrapper<Event> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(eventNo) && ObjectUtil.isNotEmpty(eventNo)) {
            System.out.println("here" + eventNo);
            lambdaQueryWrapper.eq(Event::getEventNo, eventNo);

        }
        if (ObjectUtil.isNotNull(reponseType) && ObjectUtil.isNotEmpty(reponseType)) {
            lambdaQueryWrapper.eq(Event::getResponseType, reponseType);

        }
        List<Event> eventList = this.list(lambdaQueryWrapper);
        List<Long> custIdList = eventList.stream().map(Event::getEventId).collect(Collectors.toList());
        System.out.println(custIdList);
        return custIdList;
    }

    @Override
    public Event detail(Long eventId) {
        Event event = this.getById(eventId);
        if (ObjectUtil.isNull(event)) {
            throw new ServiceException(EventExceptionEnum.EVENT_NOT_EXIST);
        } else {
            return event;
        }
    }

    @Override
    public Event detailBy(Long eventId, String consId) {
        Event event = this.getById(eventId);
        if (ObjectUtil.isNull(event)) {
            throw new ServiceException(EventExceptionEnum.EVENT_NOT_EXIST);
        } else {

            List<String> list = new ArrayList<>();
            JSONArray jsonArray = new JSONArray();
            jsonArray = JSONArray.parseArray(event.getRegulateRange());
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
            event.setList(list);
            event.setRegulateCap(NumberUtil.div(event.getRegulateCap(), 10000));
            List<ConsBaseline> consBaselines = consBaselineService.getConsBaseByEventId(eventId, consId);
            ConsBaseline consBaseline = null;
            if (null != consBaselines && consBaselines.size() > 0) {
                consBaseline = consBaselines.get(0);
            } else {
                consBaseline = new ConsBaseline();
            }
            event.setConsBaseline(consBaseline);
            ConsInvitation consInvitation = consInvitationService.queryConsInvitationByEventIdAndConsId(eventId, consId);
            event.setDeadlineTime(consInvitation.getDeadlineTime());
            return event;
        }
    }

    @Override
    public void secondInvitation(Long eventId, String deadlineTime) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime secondRoundDeadlineTime = LocalDateTime.from(dateTimeFormatter.parse(deadlineTime));
        // 修改未反馈用户的邀约截止时间
        Event event = this.getById(eventId);
        //截止时间
        LocalDateTime deadLineTime = consInvitationService.getMaxDeadlineTimeByEventId(eventId);
        if (ObjectUtil.isNull(deadLineTime) || LocalDateTime.now().isBefore(deadLineTime)) {
            throw new ServiceException(EventExceptionEnum.DEADLINE_TIME);
        }
        String startTime = event.getRegulateDate() + " " + event.getEndTime() + ":00";
        LocalDateTime eventStartTime = LocalDateTime.from(dateTimeFormatter.parse(startTime));
        if (eventStartTime.isBefore(secondRoundDeadlineTime)) {
            throw new ServiceException(EventExceptionEnum.EVENT_START);
        }
        consInvitationService.secondInvitaiton(eventId, secondRoundDeadlineTime);
        // 生成发送短信,(修改到前端调用接口发送短信)
        // smsSendService.generateSms(String.valueOf(eventId), SmsTemplateTypeEnum.INVITATION_NOTICE_TWICE.getCode());
    }

    @Resource
    private OrgDemandService orgDemandService;
    @Resource
    private SystemClient systemClient;


    public Event detail(EventParam eventParam) {
        Event event = getBaseMapper().getEventDetailWithPlan(eventParam.getEventId());
        String deadLineRime = getBaseMapper().getDeadLimeTime(eventParam.getEventId());
        if (null != deadLineRime) {
            event.setDeadlineTime(LocalDateTime.parse(deadLineRime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }

        JSONArray jsonArray = new JSONArray();
        List<List<String>> regulateRange = new ArrayList<>();
        List<String> regulateRangeStrList = new ArrayList<>();
        String regulateRangeStr = "";
        if (null != event) {
            event.setRegulateCap(NumberUtil.div(event.getRegulateCap(), 10000));
            String rangeType = event.getRangeType();

            CurrenUserInfo currentUserInfoUTF8 = SecurityUtils.getCurrentUserInfoUTF8();
            if (!"1".equals(currentUserInfoUTF8.getOrgTitle())) {
                JSONObject allOrgs = systemClient.queryAllOrg();
                JSONArray allOrgsData = allOrgs.getJSONArray("data");
                Map<String, JSONObject> orgIdMap = new HashMap<>();
                for (int i = 0; i < allOrgsData.size(); i++) {
                    JSONObject jsonObject = allOrgsData.getJSONObject(i);
                    orgIdMap.put(jsonObject.getString("id"), jsonObject);
                }
                String orgId = currentUserInfoUTF8.getOrgId();
                JSONObject orgInfo = orgIdMap.get(orgId);
                while (orgInfo != null && !"2".equals(orgInfo.getString("orgTitle"))) {
                    orgInfo = orgIdMap.get(orgInfo.getString("parentId"));
                    orgId = orgInfo.getString("id");
                }

                LambdaQueryWrapper<OrgDemand> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.eq(OrgDemand::getRegulateId, event.getRegulateId());
                lambdaQueryWrapper.eq(OrgDemand::getOrgId, orgId);
                List<OrgDemand> list = orgDemandService.list(lambdaQueryWrapper);
                if (ObjectUtil.isNotEmpty(list)) {
                    event.setRegulateGap(list.get(0).getGoal());
                }else {
                    event.setRegulateGap(null);
                }
            }

            if (null != rangeType && "1".equals(rangeType)) {
                List<Region> regions = systemClientService.queryAll();
                if (null != event.getRegulateRange()) {
                    jsonArray = JSONArray.parseArray(event.getRegulateRange());
                    if (null != jsonArray && jsonArray.size() > 0) {
                        for (int j = 0; j < jsonArray.size(); j++) {
                            JSONArray value = jsonArray.getJSONArray(j);
                            List<String> list = new ArrayList<>();
                            if (null != value && value.size() > 0) {
                                for (int i = 0; i < value.size(); i++) {
                                    list.add(value.get(i).toString());
                                    String code = value.get(i).toString();
                                    Optional<Region> optionalRegion = regions.stream().filter(item -> item.getId().equals(code)).findAny();
                                    if (optionalRegion.isPresent()) {
                                        Region region = optionalRegion.get();
                                        if (i == value.size() - 1) {
                                            if (null != region && null != region.getName()) {
                                                regulateRangeStr += region.getName() + "|";
                                            } else {
                                                if (null != regulateRangeStr && !"".equals(regulateRangeStr)) {
                                                    regulateRangeStr = regulateRangeStr.substring(0, regulateRangeStr.length() - 1);
                                                }
                                                regulateRangeStr += "|";
                                            }
                                        } else {
                                            if (null != region && null != region.getName()) {
                                                regulateRangeStr += region.getName() + ",";
                                            }
                                        }
                                    }

                                }
                                regulateRange.add(list);
                            }
                        }
                    }
                }
            } else if (null != rangeType && "2".equals(rangeType)) {
                Result result = systemClientService.getAllOrgs();
                JSONObject jsonObject = null;
                if (null != result) {
                    jsonObject = result.getData();
                }
                if (null != event.getRegulateRange()) {
                    jsonArray = JSONArray.parseArray(event.getRegulateRange());
                    if (null != jsonArray && jsonArray.size() > 0) {
                        for (int j = 0; j < jsonArray.size(); j++) {
                            JSONArray value = jsonArray.getJSONArray(j);
                            List<String> list = new ArrayList<>();
                            if (null != value && value.size() > 0) {
                                for (int i = 0; i < value.size(); i++) {
                                    list.add(value.get(i).toString());
                                    if (jsonObject.containsKey(value.get(i).toString())) {
                                        Object data = jsonObject.get(value.get(i).toString());
                                        JSONObject datas = (JSONObject) JSON.toJSON(data);
                                        if (i == value.size() - 1) {
                                            if (null != datas.get("name")) {
                                                regulateRangeStr += datas.get("name") + "|";
                                            } else {
                                                if (null != regulateRangeStr && !"".equals(regulateRangeStr)) {
                                                    regulateRangeStr = regulateRangeStr.substring(0, regulateRangeStr.length() - 1);
                                                }
                                                regulateRangeStr += "|";
                                            }
                                        } else {
                                            if (null != datas.get("name")) {
                                                regulateRangeStr += datas.get("name") + ",";
                                            }
                                        }
                                    }
                                }
                                regulateRange.add(list);
                            }
                        }
                    }
                }
            }
            if (null != regulateRangeStr && !"".equals(regulateRangeStr)) {
                if ("|".equals(regulateRangeStr.substring(regulateRangeStr.length() - 1))) {
                    regulateRangeStr = regulateRangeStr.substring(0, regulateRangeStr.length() - 1);
                }
            }
            event.setRegulateRangeStr(regulateRangeStr);
            event.setRegulateRangeList(regulateRange);
        }
        return event;
    }

    @Override
    public Page<BaselineLibrary> pageEventBaseline(EventParam eventParam) {
        return baselineLibraryService.pageEventBaseline(eventParam);
    }

    private Event queryEvent(EventParam eventParam) {
        Event event = this.getById(eventParam.getEventId());
        if (ObjectUtil.isNull(event)) {
            throw new ServiceException(EventExceptionEnum.EVENT_NOT_EXIST);
        }
        return event;
    }

    @Override
    public boolean judgeByStartDate(LocalDate cdrDate) {
        int n = baseMapper.countEventNumber(cdrDate);
        return n > 0 ? true : false;
    }

    @Override
    public List<Event> listBySettlementNo(String settlementNo) {
        LambdaQueryWrapper<CustSubsidyDaily> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(CustSubsidyDaily::getSettlementNo, settlementNo);
        lambdaQueryWrapper.select(CustSubsidyDaily::getEventIds);
        List<String> eventIds = custSubsidyDailyMapper.selectList(lambdaQueryWrapper).stream().map(CustSubsidyDaily::getEventIds).flatMap(item -> Stream.of(item.split(","))).collect(Collectors.toList());
        LambdaQueryWrapper<Event> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
        lambdaQueryWrapper1.in(Event::getEventId, eventIds);
        lambdaQueryWrapper1.orderByAsc(BaseEntity::getCreateTime);
        return getBaseMapper().selectList(lambdaQueryWrapper1);
    }

    @Override
    public void revokeEvent(String eventId) {
        Event event = baseMapper.selectById(eventId);
        if (event == null) {
            throw new ServiceException(EventExceptionEnum.EVENT_NOT_EXIST);
        }
        if (EventStatusEnum.STATUS_PUBLISH.getCode().equals(event.getEventStatus())) {
            LambdaUpdateWrapper<Event> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper.eq(Event::getEventId, eventId).set(Event::getEventStatus, EventStatusEnum.STATUS_THIRTEEN.getCode());
            if (baseMapper.update(null, lambdaUpdateWrapper) < 1) {
                throw new ServiceException(EventExceptionEnum.REVOKE_EVENT_FAIL);
            }
        } else {
            throw new ServiceException(EventExceptionEnum.REVOKE_EVENT_STATUS_ERROR);
        }
    }

    @Resource
    private EventMapper eventMapper;

    /**
     * 工作台事件信息
     *
     * @param eventId
     * @return
     */
    @Override
    public EventVO eventDetail(Long eventId) {
        JSONArray jsonArray = new JSONArray();
        List<List<String>> regulateRange = new ArrayList<>();
        String regulateRangeStr = "";

        EventVO event = eventMapper.eventDetail(eventId);
        if (null != event) {
            String rangeType = event.getRangeType();
            if (null != rangeType && "1".equals(rangeType)) {
                List<Region> regions = systemClientService.queryAll();
                if (null != event.getRegulateRange()) {
                    jsonArray = JSONArray.parseArray(event.getRegulateRange());
                    if (null != jsonArray && jsonArray.size() > 0) {
                        for (int j = 0; j < jsonArray.size(); j++) {
                            JSONArray value = jsonArray.getJSONArray(j);
                            List<String> list = new ArrayList<>();
                            if (null != value && value.size() > 0) {
                                for (int i = 0; i < value.size(); i++) {
                                    list.add(value.get(i).toString());
                                    String code = value.get(i).toString();
                                    Optional<Region> optionalRegion = regions.stream().filter(item -> item.getId().equals(code)).findAny();
                                    if (optionalRegion.isPresent()) {
                                        Region region = optionalRegion.get();
                                        if (i == value.size() - 1) {
                                            if (null != region && null != region.getName()) {
                                                regulateRangeStr += region.getName() + "|";
                                            } else {
                                                if (null != regulateRangeStr && !"".equals(regulateRangeStr)) {
                                                    regulateRangeStr = regulateRangeStr.substring(0, regulateRangeStr.length() - 1);
                                                }
                                                regulateRangeStr += "|";
                                            }
                                        } else {
                                            if (null != region && null != region.getName()) {
                                                regulateRangeStr += region.getName() + ",";
                                            }
                                        }
                                    }

                                }
                                regulateRange.add(list);
                            }
                        }
                    }
                }
            } else if (null != rangeType && "2".equals(rangeType)) {
                Result result = systemClientService.getAllOrgs();
                JSONObject jsonObject = null;
                if (null != result) {
                    jsonObject = result.getData();
                }
                if (null != event.getRegulateRange()) {
                    jsonArray = JSONArray.parseArray(event.getRegulateRange());
                    if (null != jsonArray && jsonArray.size() > 0) {
                        for (int j = 0; j < jsonArray.size(); j++) {
                            JSONArray value = jsonArray.getJSONArray(j);
                            List<String> list = new ArrayList<>();
                            if (null != value && value.size() > 0) {
                                for (int i = 0; i < value.size(); i++) {
                                    list.add(value.get(i).toString());
                                    if (jsonObject.containsKey(value.get(i).toString())) {
                                        Object data = jsonObject.get(value.get(i).toString());
                                        JSONObject datas = (JSONObject) JSON.toJSON(data);
                                        if (i == value.size() - 1) {
                                            if (null != datas.get("name")) {
                                                regulateRangeStr += datas.get("name") + "|";
                                            } else {
                                                if (null != regulateRangeStr && !"".equals(regulateRangeStr)) {
                                                    regulateRangeStr = regulateRangeStr.substring(0, regulateRangeStr.length() - 1);
                                                }
                                                regulateRangeStr += "|";
                                            }
                                        } else {
                                            if (null != datas.get("name")) {
                                                regulateRangeStr += datas.get("name") + ",";
                                            }
                                        }
                                    }
                                }
                                regulateRange.add(list);
                            }
                        }
                    }
                }
            }
            if (null != regulateRangeStr && !"".equals(regulateRangeStr)) {
                if ("|".equals(regulateRangeStr.substring(regulateRangeStr.length() - 1))) {
                    regulateRangeStr = regulateRangeStr.substring(0, regulateRangeStr.length() - 1);
                }
            }
            event.setRegulateRangeStr(regulateRangeStr);
            event.setRegulateRangeList(regulateRange);

            // 查询次日效果评估
            LambdaQueryWrapper<ConsEvaluation> consEvaluationQuery = new LambdaQueryWrapper<>();
            consEvaluationQuery.eq(ConsEvaluation::getEventId, eventId);
            consEvaluationQuery.eq(ConsEvaluation::getIsEffective, YesOrNotEnum.Y.getCode());
            List<ConsEvaluation> consEvaluations = consEvaluationService.list(consEvaluationQuery);
            if (CollectionUtils.isEmpty(consEvaluations)) {
                event.setActualCap(BigDecimal.ZERO);
                event.setEvaluationCount(0);
            } else {
                BigDecimal bigDecimal = consEvaluations.stream().map(ConsEvaluation::getActualCap).reduce(BigDecimal.ZERO, BigDecimal::add);
                event.setActualCap(bigDecimal);
                event.setEvaluationCount(consEvaluations.size());
            }
        }
        return event;
    }

    /**
     * 安徽基线
     * 1 邀约工作日筛选最近5个不参与响应的工作日的响应时段负荷曲线作为基线负荷样本
     * 2 邀约周末休息日2个，节假日样本数为3个
     * 3 实时取当天日期，实际计算基线取当前时间前两小时
     *
     * @param event
     * @return
     */
    public List<String> sampleDates(Event event) {
        List<LocalDate> localDateList = new ArrayList<>();
        //LocalDate invitionDate = LocalDate.now();
        LocalDate invitionDate = event.getRegulateDate();
        //调度前一天
        LocalDate lastDate = LocalDate.now();
        if (null != event && null != event.getTimeType()) {
            if ("1".equals(event.getTimeType())) {
                CalendarInfo calendarInfo = calendarInfoService.getByCdrDate(invitionDate);
                //工作日
                if (calendarInfo.getDateType().equals(CalendarInfoTypeEnum.TYPE_01.getCode())) {
                    CalendarInfoParam calendarInfoParam = new CalendarInfoParam();
                    calendarInfoParam.setDateType(calendarInfo.getDateType());
                    calendarInfoParam.setSearchEndDate(invitionDate);
                    int n = 1;
                    while (true) {
                        boolean is_break = false;
                        Page page = new Page<>(n, PageConstant.pageSize);
                        List<CalendarInfo> calendarInfoList = calendarInfoService.page(page, calendarInfoParam).getRecords();
                        if (calendarInfoList.size() < 1) {
                            break;
                        }
                        for (CalendarInfo temp : calendarInfoList) {
                            boolean isDrDate = eventService.judgeByStartDate(temp.getCdrDate());
                            boolean isBeforeToday = temp.getCdrDate().isBefore(lastDate);
                            if (!isDrDate && isBeforeToday) {
                                localDateList.add(temp.getCdrDate());
                            }
                            if (localDateList.size() >= 5) {
                                is_break = true;
                                break;
                            }
                        }
                        if (is_break) {
                            break;
                        }
                        n++;
                    }

                }
                //周末
                if (calendarInfo.getDateType().equals(CalendarInfoTypeEnum.TYPE_02.getCode())) {
                    CalendarInfoParam calendarInfoParam = new CalendarInfoParam();
                    calendarInfoParam.setDateType(calendarInfo.getDateType());
                    calendarInfoParam.setSearchEndDate(invitionDate);
                    int n = 1;
                    while (true) {
                        boolean is_break = false;
                        Page page = new Page<>(n, PageConstant.pageSize);
                        List<CalendarInfo> calendarInfoList = calendarInfoService.page(page, calendarInfoParam).getRecords();
                        if (calendarInfoList.size() < 1) {
                            break;
                        }
                        for (CalendarInfo temp : calendarInfoList) {
                            boolean isDrDate = eventService.judgeByStartDate(temp.getCdrDate());
                            boolean isBeforeToday = temp.getCdrDate().isBefore(lastDate);
                            if (!isDrDate && isBeforeToday) {
                                localDateList.add(temp.getCdrDate());
                            }
                            if (localDateList.size() >= 2) {
                                is_break = true;
                                break;
                            }
                        }
                        if (is_break) {
                            break;
                        }
                        n++;
                    }

                }
                //节假日
                if (calendarInfo.getDateType().equals(CalendarInfoTypeEnum.TYPE_03.getCode())) {
                    CalendarInfoParam calendarInfoParam = new CalendarInfoParam();
                    calendarInfoParam.setDateType(calendarInfo.getDateType());
                    calendarInfoParam.setSearchEndDate(invitionDate);
                    int n = 1;
                    while (true) {
                        boolean is_break = false;
                        Page page = new Page<>(n, PageConstant.pageSize);
                        List<CalendarInfo> calendarInfoList = calendarInfoService.page(page, calendarInfoParam).getRecords();
                        if (calendarInfoList.size() < 1) {
                            break;
                        }
                        for (CalendarInfo temp : calendarInfoList) {
                            boolean isDrDate = eventService.judgeByStartDate(temp.getCdrDate());
                            boolean isBeforeToday = temp.getCdrDate().isBefore(lastDate);
                            if (!isDrDate && isBeforeToday) {
                                localDateList.add(temp.getCdrDate());
                            }
                            if (localDateList.size() >= 3) {
                                is_break = true;
                                break;
                            }
                        }
                        if (is_break) {
                            break;
                        }
                        n++;
                    }

                }
            }
        } else if ("2".equals(event.getTimeType())) {
            //实时取当天日期
            localDateList.add(invitionDate);
        }
        List<String> times = new ArrayList<>();
        if (null != localDateList && localDateList.size() > 0) {
            for (LocalDate localDate : localDateList) {
                times.add(localDate.toString());
            }
        }
        return times;
    }

    @Override
    public String parseEventSmsContent(Long eventId, String smsContent) {
        if (StringUtils.isEmpty(smsContent)) {
            return "";
        }
        Event event = this.getById(eventId);
        Assert.notNull(event, "事件不存在");
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("duration", event.getRegulateDate() + " " +
                event.getStartTime() + "-" + event.getEndTime());
        paramsMap.put("regulateDate", event.getRegulateDate().toString());
        paramsMap.put("start", event.getStartTime());
        paramsMap.put("end", event.getEndTime());
        paramsMap.put("eventName", event.getEventName());
        paramsMap.put("eventNo", event.getEventNo().toString());
        paramsMap.put("responseType", ResponseTypeEnum.getMessageByCode(event.getResponseType()));
        paramsMap.put("timeType", ProjectTimeTypeEnum.getMessageByCode(event.getTimeType()));
        paramsMap.put("advanceType", AdvanceNoticeEnums.getMessageByCode(event.getAdvanceNoticeTime()));
        return SmsUtils.getSmsText(paramsMap, smsContent);
    }

    /**
     * 修改 事件电力缺口
     * @param eventParam
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void editEventRegulateCap(EventParam eventParam) {

        if(eventParam == null){
            throw new ServiceException(500,"传参不能为空");
        }

        Long eventId = eventParam.getEventId();
        BigDecimal regulateCap = eventParam.getRegulateCap();
        if(eventId == null){
            throw new ServiceException(500,"必传参数 事件编号 不能为空");
        }
        if(regulateCap == null){
            throw new ServiceException(500,"必传参数 电力缺口 不能为空");
        }
        Event event = eventService.getById(eventId);
        if(null == event){
            throw new ServiceException(500,"未查找到对应事件，请核查事件编号");
        }

        //修改事件电力缺口 修改 dr_demand 电力缺口
        eventMapper.editEventRegulateCap(eventParam);
    }
}
