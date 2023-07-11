package com.xqxy.dr.modular.newloadmanagement.service.impl;


//import com.alibaba.fastjson.JSONObject;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.xqxy.core.client.SystemClient;
import com.xqxy.core.exception.ServiceException;
import com.xqxy.core.pojo.base.entity.BaseEntity;
import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.core.util.DateUtil;
import com.xqxy.dr.modular.dispatch.entity.Dispatch;
import com.xqxy.dr.modular.dispatch.mapper.DispatchMapper;
import com.xqxy.dr.modular.evaluation.entity.ConsEvaluation;
import com.xqxy.dr.modular.evaluation.mapper.ConsEvaluationMapper;
import com.xqxy.dr.modular.event.entity.Event;
import com.xqxy.dr.modular.event.mapper.EventMapper;
import com.xqxy.dr.modular.event.param.MyRepresentation;
import com.xqxy.dr.modular.event.service.EventService;
import com.xqxy.dr.modular.newloadmanagement.entity.*;
import com.xqxy.dr.modular.newloadmanagement.mapper.DemandMapper;
import com.xqxy.dr.modular.newloadmanagement.param.*;
import com.xqxy.dr.modular.newloadmanagement.po.DemandEvaluationPo;
import com.xqxy.dr.modular.newloadmanagement.service.DemandService;
import com.xqxy.dr.modular.newloadmanagement.vo.*;
import com.xqxy.dr.modular.project.entity.DrOrgGoal;
import com.xqxy.dr.modular.project.service.DrOrgGoalService;
import com.xqxy.dr.modular.subsidy.entity.SubsidyAppeal;
import com.xqxy.dr.modular.subsidy.param.SubsidyAppealParam;
import com.xqxy.sys.modular.cust.entity.Cons;
import com.xqxy.sys.modular.cust.service.ConsService;
import com.xqxy.sys.modular.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
@Slf4j
public class DemandServiceImpl implements DemandService {

    private static SimpleDateFormat DataTimeFormate2 = new SimpleDateFormat("yyyy-MM-dd");

    @Value("${userRecordUrl}")
    private String userRecordUrl;

    @Autowired
    private DemandMapper userMapper;
    @Resource
    private ConsEvaluationMapper consEvaluationMapper;
    @Autowired
    private EventMapper eventMapper;

    //调度指令状态(事件已生成)
    private static final String HAS_GENERATED = "02";
    //调度指令状态(已下发)
    private static final String HAS_ISSUED = "03";

    //1.1用户基础档案
    @Override
    public List<Drcons> userBaseProfile(String createTime) {
        return userMapper.userBaseProfile(createTime);
    }

    //1.2需求响应可调节潜力
    @Override
    public Map<String, Object> adjustablePotential(AdjustablePotentialParam adjustablePotential) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> paraMap = new HashMap<>();
        paraMap.put("beginDate", adjustablePotential.getBeginDate());
        paraMap.put("endDate", adjustablePotential.getEndDate());
        log.info("paraMap-->" + paraMap);
        List<AdjustPotential> adjustPotentials = userMapper.adjustablePotential(paraMap);
        log.info("adjustPotentials" + adjustPotentials);
        if (adjustPotentials.size() > 0 || !adjustPotentials.isEmpty()) {
            AdjustPotential a = adjustPotentials.get(0);
            log.info("a1->" + a.contractCap);
            log.info("a2->" + a.getContractCap());
            map.put("contractCap", a.getContractCap());
            map.put("drRtLoad", a.getDrRtLoad());
            map.put("orgNo", adjustablePotential.getOrgNo());
        }

        return map;
    }

    //1.3负荷调控指令信息 接收数据插入数据库插入(包括修改)
    @Override
    public void orderInformation(DemandParam demandParam) throws ServiceException{
        //根据传入regulateId查询指令信息，这个regulateId是负控传的，对应stregyId
        List<Drdemand> drdemandRecords = userMapper.selectDrdemandByStregyId(demandParam.getRegulateId());
        demandParam.regularTime();
        demandParam.regularRangeType();
        //指令信息不存在则新增，存在则根据事件状态判断是否可以修改
        if (drdemandRecords != null && drdemandRecords.size()>0) {
            Drdemand currentDrdemand = drdemandRecords.get(0);
            List<Event> events = eventMapper.selectEventByRegulateId(currentDrdemand.getRegulateId());
            //事件存在需要根据事件状态判断 重写修改内容，不存在则直接修改调度指令信息
            if (events != null && events.size()>0) {
                Event currentEvent = events.get(0);
                //调度指令信息和事件信息 需要变更的属性重新赋值
                currentDrdemand = reAssignCurrentDemand(demandParam,currentDrdemand,currentEvent.getEventStatus());
                currentEvent = reAssignCurrentEvent(demandParam,currentEvent);
                //更新event信息
                eventMapper.updateRegulateEvent(currentEvent);
            }else{
                Long originalId = currentDrdemand.getRegulateId();
                BeanUtils.copyProperties(demandParam,currentDrdemand);
                currentDrdemand.setStregyId(demandParam.getRegulateId());
                currentDrdemand.setRegulateId(originalId);
            }
            userMapper.updateOrderInfoByStregyId(currentDrdemand);
        }else {
            DateTimeFormatter simpleDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime dateTime = LocalDateTime.now();
            //生成指令编号规则为当前年月日时分秒+时段编号,时段编号后面产生
            StringBuffer regulateNo = new StringBuffer();
            Calendar date = Calendar.getInstance();
            regulateNo.append(date.get(Calendar.YEAR))
                    .append(date.get(Calendar.MONTH) + 1)
                    .append(date.get(Calendar.DAY_OF_MONTH))
                    .append(date.get(Calendar.HOUR_OF_DAY))
                    .append(date.get(Calendar.MINUTE))
                    .append(date.get(Calendar.SECOND));

            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("stregyId", demandParam.getRegulateId());
            paramMap.put("rangeType", demandParam.getRangeType());
            paramMap.put("regulateRange", demandParam.getRegulateRange());
            if (null != demandParam.getRegulateCap()) {
                paramMap.put("regulateCap", demandParam.getRegulateCap());
            } else {
                paramMap.put("regulateCap", demandParam.getRegulateCap());
            }
            paramMap.put("startTime", demandParam.getStartTime());
            paramMap.put("endTime", demandParam.getEndTime());
            paramMap.put("regulateNo", regulateNo.toString());
            paramMap.put("status", "01");
            paramMap.put("createTime", simpleDateFormat.format(dateTime));
            paramMap.put("regulateTime", demandParam.getRegulateDate());
            if (null != demandParam.getResponseType()) {
                paramMap.put("responseType", demandParam.getResponseType());
            } else {
                paramMap.put("responseType", "1");
            }
            //判断实时还是约时,调度日期加上开始日期，大于当前6小时为约时
            //判断提前通知时间类型，隔天为日前，同一天1小时至24小时内为小时级，同一天，60分钟以内为分钟级
            Integer endHour = 0;
            Integer endMinute = 0;
            if (null != demandParam.getStartTime()) {
                endHour = Integer.parseInt(demandParam.getStartTime().substring(0, 2));
                endMinute = Integer.parseInt(demandParam.getStartTime().substring(3, 5));
            }
            LocalTime startTime = LocalTime.of(endHour, endMinute);
            LocalDate regulateTime = LocalDate.parse(demandParam.getRegulateDate());
            LocalDate localDate = LocalDate.now();
            if (regulateTime.compareTo(localDate) == 0) {
                if (LocalTime.now().plusHours(6).compareTo(startTime) < 0) {
                    paramMap.put("timeType", "1");
                } else {
                    paramMap.put("timeType", "2");
                }
                if (LocalTime.now().plusMinutes(60).compareTo(startTime) >= 0) {
                    paramMap.put("advanceNoticeTime", "3");
                } else {
                    paramMap.put("advanceNoticeTime", "2");
                }
            } else if (regulateTime.compareTo(localDate) > 0) {
                paramMap.put("timeType", "1");
                paramMap.put("advanceNoticeTime", "1");
            } else {
                return;
            }
            userMapper.orderInformation(paramMap);
        }
    }

    /**
     * 根据事件状态重新赋值demand需要修改的属性
     * @param demandParam
     * @param currentDrdemand
     * @param eventStatus 01:保存，02待执行，03执行中，04结束，05废弃,06以发布，07撤销
     * @return
     * @throws ServiceException
     */
    private Drdemand reAssignCurrentDemand(DemandParam demandParam,Drdemand currentDrdemand,String eventStatus)throws ServiceException {
        switch (eventStatus){
            case "01":
                currentDrdemand.setRegulateCap(demandParam.getRegulateCap());
                currentDrdemand.setRangeType(demandParam.getRangeType());
                currentDrdemand.setRegulateRange(demandParam.getRegulateRange());
                break;
            case "02":
            case "03":
            case "06":
                currentDrdemand.setRegulateCap(demandParam.getRegulateCap());
                break;
            default:
                throw new ServiceException(-1,"指令对应事件已结束、废弃或撤销，无法修改");
        }
        return currentDrdemand;
    }

    /**
     * 根据事件状态重新赋值事件表需要修改的属性
     * @param demandParam
     * @param currentEvent eventStatus 01:保存，02待执行，03执行中，04结束，05废弃,06以发布，07撤销
     * @return
     */
    private Event reAssignCurrentEvent(DemandParam demandParam,Event currentEvent) {
        switch (currentEvent.getEventStatus()){
            case "01":
                currentEvent.setRegulateCap(demandParam.getRegulateCap());
                currentEvent.setRangeType(demandParam.getRangeType());
                currentEvent.setRegulateRange(demandParam.getRegulateRange());
                break;
            case "02":
            case "06":
                currentEvent.setRegulateCap(demandParam.getRegulateCap());
                break;
            case "03":
                currentEvent.setEndTime(demandParam.getEndTime());
                break;
            default:
                throw new ServiceException(-1,"指令对应事件已结束、废弃或撤销，无法修改");
        }
        return currentEvent;
    }

    //1.4方案用户清单
    @Override
    public String userRecord(List<UserRecordParam> list) {
        //List<UserRecordParam> userRecordParamList = new ArrayList<>();
        // log.info("userRecordParamList"+userRecordParamList);
        Map<String, Object> map = new HashMap<>();
        map.put("data", list);
        log.info("map" + map);
        log.info("userRecordUrl-->" + userRecordUrl);
        JSONObject jsonObject = HttpClientUtil.sendPost(userRecordUrl, map);
//        String a = "{\"code\": \"00000\",\n" +
//                "\t\"mesg\": \"处理成功\",\n" +
//                "\t\"time\": \"2022-02-21T08:01:13.091Z\",\n" +
//                "\t\"data\": {}}";
//        com.alibaba.fastjson.JSONObject jsonObject = JSONObject.parseObject(a);
        log.info("jsonObject" + jsonObject);
        log.info(jsonObject.getString("code"));
        log.info("返回的数据jsonObject" + jsonObject);
        if (jsonObject.isEmpty() || !ResponseData.SUCCESSFUL_CODE.equals(jsonObject.getString("code"))) {
            return "调用新型负控方案用户清单返回失败";
        }
        return "调用新型负控方案用户清单返回成功";
    }

    //1.6未到位用户清单
    @Override
    public List<Drevent> outUserList(String regulateId) {
        return userMapper.outUserList(regulateId);
    }

    //1.7方案执行效果
    @Override
    public Drevent planExecutionEffect(String regulateId) {

        Drevent drevent = userMapper.planExecutionEffect(regulateId);
        log.info("drevent-->" + drevent);
        Drevent drevent2 = userMapper.planExecutionEffectValue(regulateId);
        log.info("drevent2-->" + drevent2.getExecuteQuantity());
        drevent.setExecuteQuantity(drevent2.getExecuteQuantity());
        drevent.setNoExecuteQuantity(drevent.getPlanQuantity() - drevent2.getExecuteQuantity());
        log.info("处理后的数据drevent-->" + drevent);
        return drevent;
    }

    //1.8方案执行曲线
    @Override
    public List<PlanCurve> planExecutionCurve(String regulateId) {
        return userMapper.planExecutionCurve(regulateId);
    }

    //1.9用户执行曲线
    @Override
    public List<DrConsCurve> userExecutionCurve(UserExecutionCurveParam userExecutionCurve) {
        Map<String, Object> paramMap = new HashMap<>();
        String dataDate = null;
        log.info(userExecutionCurve.getDataDate());
        if (userExecutionCurve.getDataDate() == null || userExecutionCurve.getDataDate().length() <= 0) {
            log.info("111");
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, 0);
            dataDate = DataTimeFormate2.format(calendar.getTime());
            paramMap.put("consId", userExecutionCurve.getConsId());
            paramMap.put("dataDate", dataDate);
        } else {
            log.info("222");
            paramMap.put("consId", userExecutionCurve.getConsId());
            paramMap.put("dataDate", userExecutionCurve.getDataDate());
        }
        log.info("paramMap-->" + paramMap);
        return userMapper.userExecutionCurve(paramMap);
    }

    @Override
    public List<UserLastExecInfoVo> userLastExecInfo(UserLastExecInfoParam userLastExecInfoParam) {
        if (CollectionUtils.isEmpty(userLastExecInfoParam.getConsNos())) {
            return new ArrayList<>();
        }
        return userLastExecInfoParam.getConsNos().stream().map(consNo -> {
            LambdaQueryWrapper<ConsEvaluation> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(ConsEvaluation::getConsId, consNo);
            lambdaQueryWrapper.last("limit 1");
            ConsEvaluation consEvaluation = consEvaluationMapper.selectOne(lambdaQueryWrapper);
            UserLastExecInfoVo userLastExecInfoVo = new UserLastExecInfoVo();
            userLastExecInfoVo.setConsNo(consNo);
            if (consEvaluation == null) {
                userLastExecInfoVo.setLastDemandRespTime(null);
                userLastExecInfoVo.setLastDemandRespStatus(0);
                return userLastExecInfoVo;
            }
            userLastExecInfoVo.setLastDemandRespTime(consEvaluation.getCreateTime());
            userLastExecInfoVo.setLastDemandRespStatus("Y".equals(consEvaluation.getIsEffective()) ? 1 : 0);
            return userLastExecInfoVo;
        }).collect(Collectors.toList());

    }

    @Resource
    private ConsService consService;
    @Resource
    private SystemClient systemClient;
    @Resource
    private EventService eventService;

    @Override
    public Page<UserConsProfileVo> userConsProfile(UserConsProfileParam userConsProfileParam) {
        LambdaQueryWrapper<Cons> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ObjectUtil.isNotEmpty(userConsProfileParam.getConsNo()), Cons::getId, userConsProfileParam.getConsNo());
        lambdaQueryWrapper.orderByAsc(BaseEntity::getCreateTime);
        Page<Cons> list = consService.page(new Page(userConsProfileParam.getPage(), userConsProfileParam.getLimit()), lambdaQueryWrapper);
        return pageTo(list, item -> {
            UserConsProfileVo userConsProfileVo = new UserConsProfileVo();
            BeanUtils.copyProperties(item, userConsProfileVo);
            return userConsProfileVo;
        });
    }

    @Override
    public RegionContractInfoVo getRegionContractInfo(RegionContractInfoParam regionContractInfoParam) {
        return userMapper.getRegionContractInfo(systemClient.getAllNextOrgId(regionContractInfoParam.getOrgId())
                .getData());
    }

    @Override
    public DemandEvaluationVo getDemandEvaluation(DemandEvaluationParam demandEvaluationParam) {
        List<String> orgIds = systemClient.getAllNextOrgId(demandEvaluationParam.getOrgId()).getData();
        Date startTime = demandEvaluationParam.getStartTime();
        Date endTime = demandEvaluationParam.getEndTime();
        if (demandEvaluationParam.getFlag() == 1) {
            startTime = demandEvaluationParam.getQueryDate();
            endTime = demandEvaluationParam.getQueryDate();
        }
        List<DemandEvaluationPo> demandEvaluation = userMapper.getDemandEvaluation(orgIds, DateUtil.formatDate(startTime, "yyyy-MM-dd"), DateUtil.formatDate(endTime, "yyyy-MM-dd"));
        DemandEvaluationVo demandEvaluationVo = new DemandEvaluationVo();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        if (demandEvaluationParam.getFlag() == 1) {
            demandEvaluationVo.setDemandTime(demandEvaluation.stream()
                    .map(item -> item.getStartTime() + "-" + item.getEndTime())
                    .distinct()
                    .collect(Collectors.joining(",")));
            BigDecimal demandGapLoad = demandEvaluation.stream()
                    .map(DemandEvaluationPo::getRegulateCap)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            demandEvaluationVo.setDemandGapLoad(demandGapLoad);
        } else {
            demandEvaluationVo.setDemandDay(demandEvaluation.stream()
                    .map(DemandEvaluationPo::getRegulateDate)
                    .distinct()
                    .count());
            demandEvaluationVo.setDemandCumTime(demandEvaluation.stream().map(item -> {
                LocalTime start = LocalTime.parse(item.getStartTime(), dateTimeFormatter);
                LocalTime end = LocalTime.parse(item.getEndTime(), dateTimeFormatter);
                return end.toSecondOfDay() - start.toSecondOfDay();
            }).reduce(0, Integer::sum) / 3600 + "");
        }
        demandEvaluationVo.setDemandCumHouse(demandEvaluation.stream()
                .map(DemandEvaluationPo::getConsSize)
                .filter(Objects::nonNull)
                .reduce(0, Integer::sum));
        demandEvaluationVo.setDemandCumRegulation(demandEvaluation.stream()
                .map(DemandEvaluationPo::getCbfCap)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        demandEvaluationVo.setDemandCumDate(demandEvaluation.stream()
                .filter(Objects::nonNull)
                .filter(item -> item.getCbfCap() != null)
                .max(Comparator.comparing(DemandEvaluationPo::getCbfCap))
                .orElse(new DemandEvaluationPo())
                .getRegulateDate());
        return demandEvaluationVo;
    }

    @Resource
    private DrOrgGoalService drOrgGoalService;

    @Override
    public OrgDemand64PointVo getOrgDemand64Point(OrgDemand64PointParam orgDemand64PointParam) {
        List<String> orgIds = systemClient.getAllNextOrgId(orgDemand64PointParam.getOrgId()).getData();
        com.alibaba.fastjson.JSONObject orgName = systemClient.getOrgName(orgDemand64PointParam.getOrgId());
        List<String> keysIds = IntStream.rangeClosed(1, 96).mapToObj(item -> "P" + item).collect(Collectors.toList());
        Boolean isToday = true;
        String queryDate = null;
        LocalDate localDate = null;
        if (ObjectUtil.isNotEmpty(orgDemand64PointParam.getQueryDate())) {
            localDate = LocalDate.parse(orgDemand64PointParam.getQueryDate());
            isToday = localDate.equals(LocalDate.now());
        } else {
            localDate = LocalDate.now();
        }
        queryDate = localDate.toString();

        String goalKey = "sGoal";
        QueryWrapper<DrOrgGoal> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(DrOrgGoal::getOrgId, orgIds)
                .eq(DrOrgGoal::getYear, localDate.getYear());
        queryWrapper.select("sum(`goal`) as " + goalKey);
        Map<String, Object> goalServiceMap = drOrgGoalService.getMap(queryWrapper);
        OrgDemand64PointVo orgDemand64Point = userMapper.getOrgDemand64Point(isToday, queryDate, orgIds, keysIds);
        orgDemand64Point.setOrgName(orgName.getJSONObject("data").getString("name"));
        if (goalServiceMap != null) {
            String goalValue = goalServiceMap.getOrDefault(goalKey, "0").toString();
            orgDemand64Point.setTarget(BigDecimal.valueOf(Double.valueOf(goalValue)));
        } else {
            orgDemand64Point.setTarget(BigDecimal.ZERO);
        }
        return orgDemand64Point;
//        return userMapper.getOrgDemand64Point();
    }


    private static <T, R> Page<T> pageTo(Page<R> oldPage, Function<R, T> function) {
        List<T> list = oldPage.getRecords().stream().map(function).collect(Collectors.toList());
        Page<T> newPage = (Page<T>) oldPage;
        newPage.setRecords(list);
        return newPage;
    }


    @Override
    public void termination(DemandParam demandParam) throws ServiceException {
        Event dispatch = new Event();

        List<Event> dispatches = userMapper.selectEventByRegulateId(demandParam.getRegulateId());
        if (dispatches.size() > 0) {
            Event entity = dispatches.get(0);
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(entity.getRegulateDate());
            stringBuffer.append(" ");
            stringBuffer.append(entity.getStartTime());
            String startTime = stringBuffer.toString();

            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            //调控日期+响应开始时间
            LocalDateTime ldt1 = LocalDateTime.parse(startTime, df);
            //当前时间
            String format = LocalDateTime.now().format(df);
            LocalDateTime ldt2 = LocalDateTime.parse(format, df);
            Duration duration = Duration.between(ldt2, ldt1);
            //计算时间差
            Long days = duration.toMinutes();
            //当前时间处于响应开始时间开始前 （大于1个小时），可以撤销
            if (days > 60 && ldt2.isBefore(ldt1)) {
                if (entity.getEventStatus().equals("03")) {
                    dispatch.setEventStatus("07");
                    LambdaUpdateWrapper<Event> updateWrapper = new LambdaUpdateWrapper<>();
                    updateWrapper.eq(Event::getRegulateId, entity.getRegulateId());
                    eventMapper.update(dispatch, updateWrapper);
                } else {
                    throw new ServiceException(99999, "撤销失败，仅执行中状态，可以撤销！");
                }
            }else {
                throw new ServiceException(99999, "撤销失败，当前时间处于响应开始时间开始前一个小时(大于1个小时)，可以撤销！");
            }
        } else {
            throw new ServiceException(99999, "撤销失败，数据不存在！");
        }
    }
}
