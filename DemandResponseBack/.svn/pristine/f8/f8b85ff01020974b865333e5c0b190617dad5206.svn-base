package com.xqxy.dr.modular.event.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.log.Log;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.alibaba.excel.write.metadata.fill.FillWrapper;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.core.client.SystemClientService;
import com.xqxy.core.exception.ServiceException;
import com.xqxy.core.pojo.login.CurrenUserInfo;
import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.core.util.SecurityUtils;
import com.xqxy.dr.modular.baseline.util.CurveUtil;
import com.xqxy.dr.modular.data.entity.ConsCurve;
import com.xqxy.dr.modular.data.entity.ConsCurveBase;
import com.xqxy.dr.modular.data.service.ConsCurveService;
import com.xqxy.dr.modular.data.service.ConsCurveTodayService;
import com.xqxy.dr.modular.device.entity.SysOrgs;
import com.xqxy.dr.modular.dispatch.entity.OrgDemand;
import com.xqxy.dr.modular.dispatch.service.OrgDemandService;
import com.xqxy.dr.modular.event.entity.*;
import com.xqxy.dr.modular.event.mapper.ConsInvitationMapper;
import com.xqxy.dr.modular.event.mapper.EventExecuteMapper;
import com.xqxy.dr.modular.event.param.EventExecuteParam;
import com.xqxy.dr.modular.event.result.EventCountVo;
import com.xqxy.dr.modular.event.result.EventExecuteReportResult;
import com.xqxy.dr.modular.event.service.*;
import com.xqxy.dr.modular.event.utils.OrgUtils;
import com.xqxy.dr.modular.newloadmanagement.vo.ExchPointCurve96Vo;
import com.xqxy.sys.modular.cust.enums.OrgTitleEnum;
import com.xqxy.sys.modular.sms.entity.SysSmsSend;
import com.xqxy.sys.modular.utils.JSONObjectToEntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 电力用户执行信息 服务实现类
 * </p>
 *
 * @author Shen
 * @since 2021-10-27
 */
@Service
public class EventExecuteServiceImpl extends ServiceImpl<EventExecuteMapper, EventExecute> implements EventExecuteService {
    private static final Log log = Log.get();

    @Resource
    private SystemClientService systemClientService;

    @Resource
    private EventService eventService;

    @Resource
    private OrgDemandService orgDemandService;

    @Resource
    private ConsInvitationMapper consInvitationMapper;

    @Resource
    private EventPowerBaseService eventPowerBaseService;

    @Resource
    private EventPowerService eventPowerService;

    @Resource
    private EventPowerDayService eventPowerDayService;

    @Resource
    private PlanService planService;

    @Resource
    private ConsBaselineAllService consBaselineAllService;

    @Resource
    private ConsCurveService consCurveService;

    @Resource
    private ConsCurveTodayService consCurveTodayService;

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public Page<EventExecute> eventMonitoring(EventExecuteParam eventExecuteParam) {
        if(eventExecuteParam != null){
            CurrenUserInfo currentUserInfoUTF8 = SecurityUtils.getCurrentUserInfoUTF8();
            if(currentUserInfoUTF8 != null){
                if(!OrgTitleEnum.PROVINCE.getCode().equals(currentUserInfoUTF8.getOrgTitle())){
                    String orgNo = currentUserInfoUTF8.getOrgId();
                    List<String> orgNos;
                    if (!StringUtils.isEmpty(orgNo)) {
                        //获取所有组织机构集合
                        ResponseData<List<String>> allNextOrgId = systemClientService.getAllNextOrgId(orgNo);
                        if (allNextOrgId != null) {
                            if ("000000".equals(allNextOrgId.getCode())) {
                                orgNos = allNextOrgId.getData();
                                eventExecuteParam.setOrgNos(orgNos);
                            }
                        }
                    }
                }
            }
        }
        return getBaseMapper().selectPageVo(eventExecuteParam.getPage(), eventExecuteParam);
    }

    @Override
    public List<EventExecute> list(EventExecuteParam eventExecuteParam) {
        LambdaQueryWrapper<EventExecute> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(eventExecuteParam)) {
            // 根据事件Id编号查询
            if (ObjectUtil.isNotEmpty(eventExecuteParam.getEventId())) {
                queryWrapper.eq(EventExecute::getEventId, eventExecuteParam.getEventId());
            }
            if (ObjectUtil.isNotEmpty(eventExecuteParam.getConsId())) {
                queryWrapper.eq(EventExecute::getConsId, eventExecuteParam.getConsId());
            }
            if (ObjectUtil.isNotEmpty(eventExecuteParam.getIsOut())) {
                queryWrapper.eq(EventExecute::getIsOut, eventExecuteParam.getIsOut());
            }
            if(ObjectUtil.isNotEmpty(eventExecuteParam.getSortColumn()) && ObjectUtil.isNotEmpty(eventExecuteParam.getOrder())){
                if(eventExecuteParam.getSortColumn().equals("executeCap") && eventExecuteParam.getOrder().equals("asc") ){
                    queryWrapper.orderByAsc(EventExecute::getExecuteCap);
                }else if(eventExecuteParam.getSortColumn().equals("executeCap") && eventExecuteParam.getOrder().equals("desc") ){
                    queryWrapper.orderByDesc(EventExecute::getExecuteCap);
                }

                if(eventExecuteParam.getSortColumn().equals("executeRate") && eventExecuteParam.getOrder().equals("asc") ){
                    queryWrapper.orderByAsc(EventExecute::getExecuteRate);
                }else if(eventExecuteParam.getSortColumn().equals("executeRate") && eventExecuteParam.getOrder().equals("desc") ){
                    queryWrapper.orderByDesc(EventExecute::getExecuteRate);
                }
            }
        }
        //根据监测时间倒序排列
        queryWrapper.orderByDesc(EventExecute::getExecuteTime);
        List<EventExecute> list = this.list(queryWrapper);
        if(!CollectionUtils.isEmpty(list)){
            for (EventExecute eventExecute : list) {
                if (eventExecute.getExecuteRate() != null) {
                    eventExecute.setExecuteRate(eventExecute.getExecuteRate().multiply(new BigDecimal(100)));
                }
            }
        }
        return list;
    }

    @Override
    public void downloadExecteReport(EventExecuteParam eventExecuteParam, HttpServletResponse response, HttpServletRequest request) {
        Long eventId = eventExecuteParam.getEventId();
        if(null==eventId) {
            log.info("无事件信息");
            export(response,new ArrayList<>());
            return;
        }
        //数据权限
        CurrenUserInfo currenUserInfo = SecurityUtils.getCurrentUserInfoUTF8();
        if (null == currenUserInfo) {
            log.info("无用户信息");
            export(response,new ArrayList<>());
            return;
        }
        LambdaQueryWrapper<Event> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Event::getEventId,eventId);
        lambdaQueryWrapper.eq(Event::getEventStatus,"04");
        Event event = eventService.getOne(lambdaQueryWrapper);
        if(null==event) {
            log.info("事件未结束或不存在");
            export(response,new ArrayList<>());
            return;
        }
        LocalDate regulateDate = event.getRegulateDate();
        Plan plan = planService.getByEventId(eventId);
        Long baseLineId = plan.getBaselinId();
        LocalDate localDate = LocalDate.now();
        String orgNo = currenUserInfo.getOrgId();
        String orgTitle = currenUserInfo.getOrgTitle();
        List<EventExecuteReportResult> eventExecuteReportResults = new ArrayList<>();
        JSONArray datas = null;
        List<SysOrgs> orgsList = new ArrayList<>();
        List<SysOrgs> orgsListDate = new ArrayList<>();
        OrgUtils orgUtils = new OrgUtils();
        //获取所有组织机构
        JSONObject result = systemClientService.queryAllOrg();
        if ("000000".equals(result.getString("code"))) {
            datas = result.getJSONArray("data");
            if (null != datas && datas.size() > 0) {
                for (Object object : datas) {
                    JSONObject jsonObject = (JSONObject) JSONObject.toJSON(object);
                    SysOrgs sysOrgs = JSONObjectToEntityUtils.JSONObjectToSysOrg(jsonObject);
                    orgsListDate.add(sysOrgs);
                }
            } else {
                log.warn("组织机构为空");
                export(response,new ArrayList<>());
                return;
            }
        } else {
            log.warn("组织机构不存在");
            export(response,new ArrayList<>());
            return;
        }
        if (null != orgNo && !"".equals(orgNo)) {
            if ("1".equals(orgTitle)) {
                orgsList = orgsListDate.stream().filter(n -> "2".equals(n.getOrgTitle()) || "1".equals(n.getOrgTitle())).collect(Collectors.toList());
            } else if("2".equals(orgTitle)) {
                orgsList = orgsListDate.stream().filter(n -> orgNo.equals(n.getOrgNo())).collect(Collectors.toList());
            } else {
                String orgId = orgUtils.getData3(datas, orgNo, null);
                orgsList = orgsListDate.stream().filter(n -> orgId.equals(n.getOrgNo())).collect(Collectors.toList());
            }
        } else {
            log.warn("无用户机构信息");
            export(response,new ArrayList<>());
            return;
        }
        //参与执行的所有用户
        List<String> cons = baseMapper.queryExeConsIdByEventId(plan.getPlanId());
        if(null==cons || cons.size()==0) {
            log.warn("无执行用户");
            export(response,new ArrayList<>());
            return;
        }
        //查询所有两高用户
        List<PlanCons> towHighCons = baseMapper.getTowHighCons(cons);
        List<String> consIds = towHighCons.stream().map(PlanCons::getConsId).collect(Collectors.toList());
        if(null==consIds || consIds.size()==0) {
            log.warn("无两高用户");
            export(response,new ArrayList<>());
            return;
        }
        //两高用户基线
        LambdaQueryWrapper<ConsBaselineAll> lambdaBaseLineQueryWrapper = new LambdaQueryWrapper<>();
        lambdaBaseLineQueryWrapper.eq(ConsBaselineAll::getBaselineLibId,baseLineId);
        lambdaBaseLineQueryWrapper.in(ConsBaselineAll::getConsId,consIds);
        List<ConsBaselineAll>  consBaselineAlls = consBaselineAllService.list(lambdaBaseLineQueryWrapper);
        List<ConsCurve> consCurveList = null;
        if(localDate.compareTo(regulateDate)==0) {
            //两高实时曲线
            consCurveList = consCurveService.getCurveByConsIdListAndDate3(consIds,dateTimeFormatter.format(regulateDate));
        } else {
            //两高用户历史曲线
            consCurveList = consCurveService.getCurveByConsIdListAndDate2(consIds,dateTimeFormatter.format(regulateDate));
        }
        List<OrgDemand> orgDemandList = orgDemandService.getByRegulateId(event.getRegulateId());
        if(null!=orgsList && orgsList.size()>0) {
            List<SysOrgs>  sysOrgs = orgsList.stream().filter(orgs -> orgs.getOrgTitle().equals("1")).collect(Collectors.toList());
            long i = 1;
            String time = event.getStartTime() + "-" +event.getEndTime();
            int startP = CurveUtil.covDateTimeToPoint(event.getStartTime());
            int endP = CurveUtil.covDateTimeToPoint(event.getEndTime());
            int pCount = endP - startP +1;
            //获取事件基线
            LambdaQueryWrapper<EventPowerBase> lambdaQueryWrapper2 = new LambdaQueryWrapper<>();
            lambdaQueryWrapper2.eq(EventPowerBase::getEventId, event.getEventId());
            List<EventPowerBase> eventPowerBases = eventPowerBaseService.list(lambdaQueryWrapper2);

            //获取事件实时曲线
            LambdaQueryWrapper<EventPower> lambdaQueryWrapper3 = new LambdaQueryWrapper<>();
            lambdaQueryWrapper3.eq(EventPower::getEventId, event.getEventId());
            List<EventPower> eventPowers = eventPowerService.list(lambdaQueryWrapper3);

            //获取事件历史曲线
            LambdaQueryWrapper<EventPowerDay> lambdaQueryWrapper4 = new LambdaQueryWrapper<>();
            lambdaQueryWrapper4.eq(EventPowerDay::getEventId, event.getEventId());
            List<EventPowerDay> eventPowerDays = eventPowerDayService.list(lambdaQueryWrapper4);
            //全身最大负荷时点对应实时负荷
            Map<String,Object> map = new HashMap<>();
            map.put("type","20");
            map.put("curveType","107");
            map.put("dataDate",regulateDate);
            List<ExchPointCurve96Vo> exchPointCurve96Vos = baseMapper.getMaxReduceCurve(map);
            //全身最大负荷时点对应历史负荷
            map.put("curveType","102");
            List<ExchPointCurve96Vo> exchPointDayCurve96Vos = baseMapper.getMaxReduceDayCurve(map);
            for(SysOrgs org : orgsList) {
                List<BigDecimal> baselinePoints = new ArrayList<>();
                List<BigDecimal> curvePoints = new ArrayList<>();
                EventExecuteReportResult eventExecuteReportResult = new EventExecuteReportResult();
                eventExecuteReportResult.setSort(i);
                eventExecuteReportResult.setOrgName(org.getName());
                eventExecuteReportResult.setPeriod(time);
                //调度目标
                if("1".equals(org.getOrgTitle())) {
                    //省级
                    eventExecuteReportResult.setTargetCap(NumberUtil.div(event.getRegulateCap(),10000));
                    //邀约应约负荷、人数
                    Map map2 = new HashMap();
                    map2.put("eventId",eventId);
                    EventCountVo consCount = consInvitationMapper.getConsCount2(map2);
                    if(null!=consCount) {
                        Integer count = consCount.getConsCount();
                        eventExecuteReportResult.setConsCount(count);
                        Integer demandConsCount = consCount.getDemandConsCount();
                        eventExecuteReportResult.setDemandConsCount(demandConsCount);
                        BigDecimal demandCap = consCount.getDemandCap();
                        if(null!=demandCap) {
                            eventExecuteReportResult.setDemandCap(NumberUtil.div(demandCap,10000));
                        }
                    }
                    //两高
                    if(null!=consBaselineAlls && consBaselineAlls.size()>0) {
                        for(int k= 0;k<consBaselineAlls.size();k++) {
                            for (int j = startP; j <= endP; j++) {
                                BigDecimal basePoint = (BigDecimal) ReflectUtil.getFieldValue(consBaselineAlls.get(k), "p" + j);
                                if (null !=basePoint) {
                                    baselinePoints.add(basePoint);
                                }
                            }
                        }

                    }
                    if(null!=consCurveList && consCurveList.size()>0) {
                        for(int k= 0;k<consCurveList.size();k++) {
                            for (int j = startP; j <= endP; j++) {
                                BigDecimal basePoint = (BigDecimal) ReflectUtil.getFieldValue(consCurveList.get(k), "p" + j);
                                if (null !=basePoint) {
                                    curvePoints.add(basePoint);
                                }
                            }
                        }
                    }
                    if(baselinePoints.size()>0 && curvePoints.size()>0) {
                        BigDecimal baselineSum = baselinePoints.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
                        BigDecimal curveSum = curvePoints.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
                        BigDecimal reduce = NumberUtil.sub(baselineSum,curveSum);
                        reduce = NumberUtil.div(reduce,10000);
                        eventExecuteReportResult.setTwoHighAvgResponseCap(NumberUtil.div(reduce,pCount));
                    }
                    if(null!=towHighCons) {
                        eventExecuteReportResult.setTwoHighConsCount(towHighCons.size());
                    }
                } else {
                    if(null!=orgDemandList && orgDemandList.size()>0) {
                        //市级
                        List<OrgDemand> orgDemands = orgDemandList.stream().filter(demand -> demand.getOrgId().equals(org.getId())).collect(Collectors.toList());
                        if (null != orgDemands && orgDemands.size() > 0) {
                            eventExecuteReportResult.setTargetCap(orgDemands.get(0).getGoal());
                        }
                        //查找市级机构所有子集
                        List<String> allNextOrgIdData = orgUtils.getData(datas, org.getId(), new ArrayList<>());
                        //邀约应约负荷、人数
                        Map map2 = new HashMap();
                        map2.put("eventId", eventId);
                        map2.put("orgs", allNextOrgIdData);
                        EventCountVo consCount = consInvitationMapper.getConsCount2(map2);
                        if (null != consCount) {
                            Integer count = consCount.getConsCount();
                            eventExecuteReportResult.setConsCount(count);
                            Integer demandConsCount = consCount.getDemandConsCount();
                            eventExecuteReportResult.setDemandConsCount(demandConsCount);
                            BigDecimal demandCap = consCount.getDemandCap();
                            if(null!=demandCap) {
                                eventExecuteReportResult.setDemandCap(NumberUtil.div(demandCap,10000));
                            }
                        }
                        //两高
                        if (null != towHighCons && towHighCons.size()>0) {
                            List<PlanCons> planConsCity = towHighCons.stream().filter(planCons -> allNextOrgIdData.contains(planCons.getOrgNo())).collect(Collectors.toList());
                            List<String> consCity = planConsCity.stream().map(PlanCons::getConsId).collect(Collectors.toList());
                            if (null != consBaselineAlls && consBaselineAlls.size() > 0 && null != consCity && consCity.size()>0) {
                                List<ConsBaselineAll> consBaselineAllsCity = consBaselineAlls.stream().filter(consBaselineAll -> consCity.contains(consBaselineAll.getConsId())).collect(Collectors.toList());
                                    for (int k = 0; k < consBaselineAllsCity.size(); k++) {
                                        for (int j = startP; j <= endP; j++) {
                                            BigDecimal basePoint = (BigDecimal) ReflectUtil.getFieldValue(consBaselineAllsCity.get(k), "p" + j);
                                            if (null != basePoint) {
                                                baselinePoints.add(basePoint);
                                            }
                                        }
                                    }

                            }
                            if (null != consCurveList && consCurveList.size() > 0 && null != consCity && consCity.size()>0) {
                                List<ConsCurve> consCurvesCity = consCurveList.stream().filter(consBaselineAll -> consCity.contains(consBaselineAll.getConsId())).collect(Collectors.toList());
                                for (int k = 0; k < consCurvesCity.size(); k++) {
                                    for (int j = startP; j <= endP; j++) {
                                        BigDecimal basePoint = (BigDecimal) ReflectUtil.getFieldValue(consCurvesCity.get(k), "p" + j);
                                        if (null != basePoint) {
                                            curvePoints.add(basePoint);
                                        }
                                    }
                                }
                            }
                            if (baselinePoints.size() > 0 && curvePoints.size() > 0) {
                                BigDecimal baselineSum = baselinePoints.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
                                BigDecimal curveSum = curvePoints.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
                                BigDecimal reduce = NumberUtil.sub(baselineSum, curveSum);
                                reduce = NumberUtil.div(reduce,10000);
                                eventExecuteReportResult.setTwoHighAvgResponseCap(NumberUtil.div(reduce, pCount));
                            }
                            if (null != consCity) {
                                eventExecuteReportResult.setTwoHighConsCount(consCity.size());
                            }
                        }
                    }

                }
                int maxPoint = 0;
                //最大压降负荷
                if(localDate.compareTo(regulateDate)==0) {
                    //国网省级实时曲线
                    if(null!=exchPointCurve96Vos && exchPointCurve96Vos.size()>0) {
                        List<ExchPointCurve96Vo> pointCurve96Vos = exchPointCurve96Vos.stream().filter(baseLineDetail -> baseLineDetail.getCurveOrgNo().equals(sysOrgs.get(0).getId())
                        ).collect(Collectors.toList());
                        if(null!=pointCurve96Vos && pointCurve96Vos.size()>0) {
                            Map<String,Object> temp = new HashMap<>();
                            for (int j = startP; j <= endP; j++) {
                                BigDecimal point = (BigDecimal) ReflectUtil.getFieldValue(pointCurve96Vos.get(0), "point" + j);
                                if(temp.containsKey("key")) {
                                    BigDecimal value = (BigDecimal) temp.get("value");
                                    if(point.compareTo(value)>0) {
                                        temp.put("key",j);
                                        temp.put("value",point);
                                    }
                                } else {
                                    temp.put("key",j);
                                    temp.put("value",point);
                                }
                            }
                            maxPoint = (int) temp.get("key");
                        }
                    }
                    //事件实时曲线
                    List<EventPower> eventPowerList = eventPowers.stream().filter(baseLineDetail -> baseLineDetail.getOrgNo().equals(org.getId())
                    ).collect(Collectors.toList());
                    //事件基线
                    List<EventPowerBase> eventPowerBaseList = eventPowerBases.stream().filter(baseLineDetail -> baseLineDetail.getOrgNo().equals(org.getId())
                    ).collect(Collectors.toList());
                    Map<String,Object> temp = new HashMap<>();
                    List<BigDecimal> avgs = new ArrayList<>();
                    for (int j = startP; j <= endP; j++) {
                        if (null != eventPowerList && eventPowerList.size() > 0 && null != eventPowerBaseList && eventPowerBaseList.size() > 0) {
                            BigDecimal pointValue = null;
                            //基线
                            BigDecimal fieldValue1 = (BigDecimal) ReflectUtil.getFieldValue(eventPowerBaseList.get(0), "p" + j);
                            //实时曲线
                            BigDecimal fieldValue2 = (BigDecimal) ReflectUtil.getFieldValue(eventPowerList.get(0), "p" + j);
                            if (null != fieldValue1 && null != fieldValue2) {
                                pointValue = NumberUtil.sub(fieldValue1, fieldValue2);
                                if("2".equals(event.getResponseType())) {
                                    //填谷
                                    pointValue = NumberUtil.mul(-1,pointValue);
                                }
                                avgs.add(pointValue);
                                if(temp.containsKey("key")) {
                                    BigDecimal value = (BigDecimal) temp.get("value");
                                    if(pointValue.compareTo(value)>0) {
                                        temp.put("key","p"+j);
                                        temp.put("value",pointValue);
                                    }
                                } else {
                                    temp.put("key","p"+j);
                                    temp.put("value",pointValue);
                                }
                            }
                            //计算国网省级最大点对应的需求响应压减负荷
                            if(j==maxPoint) {
                                if(temp.containsKey("key")) {
                                    String key = (String) temp.get("key");
                                    String timePeroid = CurveUtil.covCurvePointToDateTime("p"+maxPoint);
                                    eventExecuteReportResult.setMaxRealCapTime(timePeroid);
                                    if(null!=pointValue) {
                                        eventExecuteReportResult.setMaxRealCap(NumberUtil.div(pointValue,10000));
                                    }
                                }
                            }
                        }
                    }
                    if(avgs.size()>0) {
                        //平均压降负荷
                        BigDecimal sumPower = avgs.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
                        BigDecimal avgPower = NumberUtil.div(sumPower, avgs.size(), 2);
                        if(null!=avgPower) {
                            eventExecuteReportResult.setAvgResponseCap(NumberUtil.div(avgPower,10000));
                        }
                    }
                    if(temp.containsKey("key")) {
                        BigDecimal value = (BigDecimal) temp.get("value");
                        String key = (String) temp.get("key");
                        String timePeroid = CurveUtil.covCurvePointToDateTime(key);
                        if(null!=value) {
                            eventExecuteReportResult.setMaxResponseCap(NumberUtil.div(value,10000));
                        }
                        eventExecuteReportResult.setMaxResponseCapTime(timePeroid);
                    }
                } else {
                    ///国网省级历史曲线
                    if(null!=exchPointDayCurve96Vos && exchPointDayCurve96Vos.size()>0) {
                        List<ExchPointCurve96Vo> pointCurve96Vos = exchPointDayCurve96Vos.stream().filter(baseLineDetail -> baseLineDetail.getCurveOrgNo().equals(sysOrgs.get(0).getId())
                        ).collect(Collectors.toList());
                        if(null!=pointCurve96Vos && pointCurve96Vos.size()>0) {
                            Map<String,Object> temp = new HashMap<>();
                            for (int j = startP; j <= endP; j++) {
                                BigDecimal point = (BigDecimal) ReflectUtil.getFieldValue(pointCurve96Vos.get(0), "point" + j);
                                if(temp.containsKey("key")) {
                                    BigDecimal value = (BigDecimal) temp.get("value");
                                    if(point.compareTo(value)>0) {
                                        temp.put("key",j);
                                        temp.put("value",point);
                                    }
                                } else {
                                    temp.put("key",j);
                                    temp.put("value",point);
                                }
                            }
                            maxPoint = (int) temp.get("key");
                        }
                    }
                    //事件历史曲线
                    List<EventPowerDay> eventPowerList = eventPowerDays.stream().filter(baseLineDetail -> baseLineDetail.getOrgNo().equals(org.getId())
                    ).collect(Collectors.toList());
                    //事件基线
                    List<EventPowerBase> eventPowerBaseList = eventPowerBases.stream().filter(baseLineDetail -> baseLineDetail.getOrgNo().equals(org.getId())
                    ).collect(Collectors.toList());
                    Map<String,Object> temp = new HashMap<>();
                    List<BigDecimal> avgs = new ArrayList<>();
                    for (int j = startP; j <= endP; j++) {
                        if (null != eventPowerList && eventPowerList.size() > 0 && null != eventPowerBaseList && eventPowerBaseList.size() > 0) {
                            BigDecimal pointValue = null;
                            //基线
                            BigDecimal fieldValue1 = (BigDecimal) ReflectUtil.getFieldValue(eventPowerBaseList.get(0), "p" + j);
                            //实时曲线
                            BigDecimal fieldValue2 = (BigDecimal) ReflectUtil.getFieldValue(eventPowerList.get(0), "p" + j);
                            if (null != fieldValue1 && null != fieldValue2) {
                                pointValue = NumberUtil.sub(fieldValue1, fieldValue2);
                                if("2".equals(event.getResponseType())) {
                                    //填谷
                                    pointValue = NumberUtil.mul(-1,pointValue);
                                }
                                avgs.add(pointValue);
                                if(temp.containsKey("key")) {
                                    BigDecimal value = (BigDecimal) temp.get("value");
                                    if(pointValue.compareTo(value)>0) {
                                        temp.put("key","p"+j);
                                        temp.put("value",pointValue);
                                    }
                                } else {
                                    temp.put("key","p"+j);
                                    temp.put("value",pointValue);
                                }
                            }
                            //计算国网省级最大点对应的需求响应压减负荷
                            if(j==maxPoint) {
                                if(temp.containsKey("key")) {
                                    String key = (String) temp.get("key");
                                    String timePeroid = CurveUtil.covCurvePointToDateTime("p"+maxPoint);
                                    eventExecuteReportResult.setMaxRealCapTime(timePeroid);
                                    if(null!=pointValue) {
                                        eventExecuteReportResult.setMaxRealCap(NumberUtil.div(pointValue,10000));
                                    }
                                }
                            }
                        }
                    }
                    if(avgs.size()>0) {
                        //平均压降负荷
                        BigDecimal sumPower = avgs.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
                        BigDecimal avgPower = NumberUtil.div(sumPower, avgs.size(), 2);
                        if(null!=avgPower) {
                            eventExecuteReportResult.setAvgResponseCap(NumberUtil.div(avgPower,10000));
                        }
                    }
                    if(temp.containsKey("key")) {
                        //最大压降
                        BigDecimal value = (BigDecimal) temp.get("value");
                        String key = (String) temp.get("key");
                        String timePeroid = CurveUtil.covCurvePointToDateTime(key);
                        if(null!=value) {
                            eventExecuteReportResult.setMaxResponseCap(NumberUtil.div(value,10000));
                        }
                        eventExecuteReportResult.setMaxResponseCapTime(timePeroid);
                    }
                }
                eventExecuteReportResults.add(eventExecuteReportResult);
                i++;
            }
            export(response,eventExecuteReportResults);
            return;
        }
        export(response,new ArrayList<>());
    }

    //excel导出
    public void export(HttpServletResponse response,List<EventExecuteReportResult> eventExecuteReportResults) {
        try {
            //读取模板输入流
            InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("static/execute_report.xlsx");
            if(resourceAsStream == null){
                System.out.println("未读取到 模板");
            }

            //设置文件名称
            String fileName = URLEncoder.encode("需求响应执行情况报表" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()), "UTF-8");
            //设置文件类型
            response.setContentType("application/vnd.ms-excel");
            //设置编码格式
            response.setCharacterEncoding("utf-8");
            // https://www.jb51.net/article/30565.htm Content-Disposition 使用说明
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            //创建excel
            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).withTemplate(resourceAsStream).build();
            //创建sheet
            WriteSheet writeSheet = EasyExcel.writerSheet().build();
            FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();

            // 如果有多个list 模板上必须有{前缀.} 这里的前缀就是 a，然后多个list必须用 FillWrapper包裹
            excelWriter.fill(new FillWrapper("a", eventExecuteReportResults), fillConfig, writeSheet);
//
            Map map = new HashMap<>();
            map.put("total","");
            excelWriter.fill(map, writeSheet);

            // 别忘记关闭流
            excelWriter.finish();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
