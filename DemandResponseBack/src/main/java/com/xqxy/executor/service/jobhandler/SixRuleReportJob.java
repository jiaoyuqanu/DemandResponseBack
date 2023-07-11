package com.xqxy.executor.service.jobhandler;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.log.Log;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xqxy.core.client.SystemClientService;
import com.xqxy.dr.modular.baseline.util.CurveUtil;
import com.xqxy.dr.modular.device.entity.SysOrgs;
import com.xqxy.dr.modular.event.entity.Event;
import com.xqxy.dr.modular.event.entity.EventPower;
import com.xqxy.dr.modular.event.entity.EventPowerBase;
import com.xqxy.dr.modular.event.mapper.EventMapper;
import com.xqxy.dr.modular.event.service.EventPowerBaseService;
import com.xqxy.dr.modular.event.service.EventPowerService;
import com.xqxy.dr.modular.event.service.EventService;
import com.xqxy.dr.modular.event.utils.OrgUtils;
import com.xqxy.dr.modular.project.service.ConsContractInfoService;
import com.xqxy.dr.modular.strategy.Utils.StrategyUtils;
import com.xqxy.sys.modular.dict.param.DictTypeParam;
import com.xqxy.sys.modular.dict.service.DictTypeService;
import com.xqxy.sys.modular.utils.JSONObjectToEntityUtils;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.log.XxlJobLogger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @Description 事件基线任务
 * @ClassName EventJob
 * @Author lqr
 * @date 2021.05.11 14:35
 */
@Component
public class SixRuleReportJob {
    private static final Log log = Log.get();

    @Resource
    private DictTypeService dictTypeService;

    @Resource
    private ConsContractInfoService consContractInfoService;

    @Resource
    private EventService eventService;

    @Resource
    private EventPowerBaseService eventPowerBaseService;

    @Resource
    private EventMapper eventMapper;

    @Resource
    private SystemClientService systemClientService;

    @Resource
    private EventPowerService eventPowerService;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

    private final SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private final SimpleDateFormat dateFormat3 = new SimpleDateFormat("yyyy-MM-dd");


    //事件相关四大指标上报
    @XxlJob("fourRuleReport")
    public ReturnT<String> fourRuleReport(String param) throws Exception{
        XxlJobLogger.log("事件四大指标上报");
        this.saveFourRuleReport(param,"1");
        return ReturnT.SUCCESS;
    }

    //昨天事件相关四大指标上报
    @XxlJob("fourRuleReportLastDay")
    public ReturnT<String> fourRuleReportLastDay(String param) throws Exception{
        XxlJobLogger.log("昨日事件四大指标上报");
        this.saveFourRuleReport(param,"2");
        return ReturnT.SUCCESS;
    }

    //签约两大指标上报
    @XxlJob("towRuleReport")
    public ReturnT<String> towRuleReport(String param) throws Exception{
        XxlJobLogger.log("签约两大指标上报");
        this.saveTowRuleReport(param);
        return ReturnT.SUCCESS;
    }
    public void saveTowRuleReport(String param) {
        JSONObject data = new JSONObject();
        List<Map<String,Object>> records = new ArrayList<>();
        Date date = new Date();
        //新型负控url地址
        String url = "";
        String provinceCode = null;
        //获取市级组织机构
        List<SysOrgs> orgsList = null;
        JSONArray datas = null;
        List<SysOrgs> orgsListDate = new ArrayList<>();
        try {
            DictTypeParam dictTypeParam3 = new DictTypeParam();
            dictTypeParam3.setCode("province_code");
            List<Dict> list3 = dictTypeService.dropDown(dictTypeParam3);
            Object value2 = null;
            if(null!=list3 && list3.size()>0) {
                for(Dict dict : list3) {
                    if("anhui_org_code".equals(dict.get("code"))) {
                        value2 = dict.get("value");
                    }
                }
                if (null != value2) {
                    provinceCode = (String) value2;
                }
            } else {
                provinceCode = "34101";
                log.error("未配置省级供电单位编码!");
            }
            //查询数据字典url
            DictTypeParam dictTypeParam = new DictTypeParam();
            dictTypeParam.setCode("load_post_url");
            List<Dict> list = dictTypeService.dropDown(dictTypeParam);
            if(null!=list && list.size()>0) {
                Object value = list.get(0).get("value");
                if(null!=value) {
                    url = (String) value;
                    if(null!=url) {
                        url = url+"IndexData";
                    }
                }
                //获取最新项目下，审核通过的签约用户数（省级）
                Integer count = consContractInfoService.getApprovalConsCount(null);
                log.info("需求响应省级协议签订客户数:" + count + "户");
                if(count>=0) {
                    Map<String,Object> map = null;
                    map = setParam(provinceCode,"10103001","cons_count","需求响应省级协议签订客户数","02","00000",dateFormat2.format(date));
                    map.put("INDEX_VALUE",count);
                    records.add(map);
                } else {
                    Map<String,Object> map = null;
                    map = setParam(provinceCode,"10103001","cons_count","需求响应省级协议签订客户数","02","00000",dateFormat2.format(date));
                    map.put("INDEX_VALUE",0);
                    records.add(map);
                    log.info("无需求响应省级协议签订客户");
                }
                BigDecimal sumCap = consContractInfoService.getApprovalConstractCapSum(null);
                log.info("需求响应省级可调节负荷资源规模:" + sumCap + "万千瓦");
                if(sumCap.compareTo(BigDecimal.ZERO)>=0) {
                    Map<String,Object> map = null;
                    map = setParam(provinceCode,"10103002","contract_sum","需求响应省级可调节负荷资源规模(万千瓦)","02","00000",dateFormat2.format(date));
                    map.put("INDEX_VALUE",sumCap);
                    records.add(map);
                } else {
                    Map<String,Object> map = null;
                    map = setParam(provinceCode,"10103002","contract_sum","需求响应省级可调节负荷资源规模(万千瓦)","02","00000",dateFormat2.format(date));
                    map.put("INDEX_VALUE",new BigDecimal("0.00"));
                    records.add(map);
                }
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
                        return;
                    }
                    orgsList = orgsListDate.stream().filter(n -> "2".equals(n.getOrgTitle())).collect(Collectors.toList());
                } else {
                    log.warn("组织机构不存在");
                    return;
                }
                //查询市级签约用户数
            } else {
                log.error("未配置新型负控url");
            }
            //查询市级签约用户数
            if (null != orgsList && orgsList.size() > 0) {
                OrgUtils orgUtils = new OrgUtils();
                for (SysOrgs org : orgsList) {
                    String name = "";
                    List<String> orgs = orgUtils.getData(datas, org.getId(), new ArrayList<>());
                    List<SysOrgs> single = orgsListDate.stream().filter(n -> org.getId().equals(n.getId())).collect(Collectors.toList());
                    if(null!=single && single.size()>0) {
                        name = single.get(0).getName();
                    }
                    //获取最新项目下，审核通过的签约用户数（省级）
                    Integer count = consContractInfoService.getApprovalConsCount(orgs);
                    log.info("需求响应" + name + "协议签订客户数:" + count + "户");
                    Map<String,Object> map = null;
                    map = setParam(org.getId(),"10103001","cons_count","需求响应" + name + "协议签订客户数","02",provinceCode,dateFormat2.format(date));
                    map.put("INDEX_VALUE",count);
                    records.add(map);
                    BigDecimal sumCap = consContractInfoService.getApprovalConstractCapSum(orgs);
                    log.info("需求响应" + name + "可调节负荷资源规模:" + sumCap + "万千瓦");
                    Map<String,Object> map2 = null;
                    map2 = setParam(org.getId(),"10103002","contract_sum","需求响应"+ name +"可调节负荷资源规模(万千瓦)","02",provinceCode,dateFormat2.format(date));
                    if(BigDecimal.ZERO.compareTo(sumCap)>=0 || sumCap==null) {
                        map2.put("INDEX_VALUE",new BigDecimal("0.00"));
                    } else {
                        sumCap = accuracy(sumCap,1);
                        map2.put("INDEX_VALUE",sumCap);
                    }
                    records.add(map2);
                }
            }

            if(records.size()>0) {
                data.put("records",records);
                String res = null;
                res = HttpUtil.post(url,data.toString());
                if(null!=res) {
                    JSONObject jsonObject = JSONObject.parseObject(res);
                    if(null!=jsonObject) {
                        JSONArray jsonArray = jsonObject.getJSONArray("ID_List");
                        if(null!=jsonArray && jsonArray.size()>0) {
                            log.info("数据保存成功,返回id为:"+jsonArray.toJSONString());
                        }
                    }
                } else {
                    log.error("返回值为空，保存失败!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void saveFourRuleReport(String param,String dataType) {
        LocalDate localDate = null;
        Date date = null;
        if("1".equals(dataType)) {
            date = new Date();
            localDate = LocalDate.now();
        } else {
            date = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DATE,-1);
            date = calendar.getTime();
            localDate = LocalDate.now();
            localDate = localDate.minusDays(1);
        }
        if(null!=param && !"".equals(param)) {
            if(StrategyUtils.isDate(param)) {
                localDate = LocalDate.parse(param);
                Calendar cal = Calendar.getInstance();
                // 当前小时
                int hour = cal.get(Calendar.HOUR_OF_DAY);
                // 当前分钟
                int minute = cal.get(Calendar.MINUTE);
                int second = cal.get(Calendar.SECOND);
                String time = hour + ":" + minute + ":" + second;
                time =  param + " " + time;
                try {
                    date = dateFormat2.parse(time);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        JSONObject data = new JSONObject();
        List<Map<String,Object>> records = new ArrayList<>();
        //新型负控url地址
        String url = "";
        //1为程序造数据，2为从数据库获取
        String type = "";
        //获取市级组织机构
        List<SysOrgs> orgsList = null;
        JSONArray datas = null;
        List<SysOrgs> orgsListDate = new ArrayList<>();
        String provinceCode = null;
        try {
            DictTypeParam dictTypeParam3 = new DictTypeParam();
            dictTypeParam3.setCode("province_code");
            List<Dict> list3 = dictTypeService.dropDown(dictTypeParam3);
            Object value2 = null;
            if(null!=list3 && list3.size()>0) {
                for(Dict dict : list3) {
                    if("anhui_org_code".equals(dict.get("code"))) {
                        value2 = dict.get("value");
                    }
                }
                if (null != value2) {
                    provinceCode = (String) value2;
                }
            } else {
                provinceCode = "34101";
                log.error("未配置省级供电单位编码!");
            }
            //查询数据字典url
            DictTypeParam dictTypeParam = new DictTypeParam();
            dictTypeParam.setCode("load_post_url");
            List<Dict> list = dictTypeService.dropDown(dictTypeParam);
            if(null!=list && list.size()>0) {
                Object value = list.get(0).get("value");
                if(null!=value) {
                    url = (String) value;
                    if(null!=url) {
                        url = url+"IndexData";
                    }
                }
            } else {
                log.error("未配置新型负控url");
            }
            DictTypeParam dictTypeParam2 = new DictTypeParam();
            dictTypeParam2.setCode("execute_type");
            List<Dict> list2 = dictTypeService.dropDown(dictTypeParam2);
            if(null!=list2 && list2.size()>0) {
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
                        return;
                    }
                    orgsList = orgsListDate.stream().filter(n -> "2".equals(n.getOrgTitle())).collect(Collectors.toList());
                } else {
                    log.warn("组织机构不存在");
                    return;
                }
                Object value = list2.get(0).get("value");
                if(null!=value) {
                    type = (String) value;
                }
                if("1".equals(type)) {
                    //程序造数据
                    //需求响应省级实时压减负荷（万千瓦）
                    BigDecimal val = BigDecimal.valueOf(getByProcedure2());
                    val = accuracy(val,1);
                    Map<String,Object> map = null;
                    map = setParam(provinceCode,"10202001","real_reduce_Cap","需求响应省级实时压减负荷(万千瓦)","03","00000",dateFormat2.format(date));
                    map.put("INDEX_VALUE",val);
                    records.add(map);
                    //当日需求响应省级执行户数（户）
                    int val2 =getByProcedure();
                    Map<String,Object> map2 = null;
                    map2 = setParam(provinceCode,"10302001","total_execute_cons_count","当日需求响应省级执行户数","03","00000",dateFormat2.format(date));
                    map2.put("INDEX_VALUE",val2);
                    records.add(map2);
                    //当日需求响应省级最大压减负荷(万千瓦)
                    BigDecimal val3 = BigDecimal.valueOf(getByProcedure3());
                    val3 = accuracy(val3,1);
                    Map<String,Object> map3 = null;
                    map3 = setParam(provinceCode,"10302002","reduce_Cap","当日需求响应省级最大压减负荷(万千瓦)","03","00000",dateFormat2.format(date));
                    map3.put("INDEX_VALUE",val3);
                    records.add(map3);
                    //当日需求响应省级目标负荷平均执行到位率
                    BigDecimal val4 = BigDecimal.ZERO;
                    Map<String,Object> map4 = null;
                    map4 = setParam(provinceCode,"10302003","execute_rate","当日需求响应省级目标负荷平均执行到位率","03","00000",dateFormat2.format(date));
                    map4.put("INDEX_VALUE",val4);
                    records.add(map4);
                } else if("2".equals(type)) {
                    List<String> statusList = new ArrayList<>();
                    statusList.add("03");
                    statusList.add("04");
                    //数据库获取数据
                    LambdaQueryWrapper<Event> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                    lambdaQueryWrapper.eq(Event::getRegulateDate, localDate);
                    lambdaQueryWrapper.eq(Event::getTimeType, "1");
                    lambdaQueryWrapper.eq(Event::getResponseType, "1");
                    lambdaQueryWrapper.in(Event::getEventStatus,statusList);
                    List<Event> eventList = eventService.list(lambdaQueryWrapper);
                    //事件目标负荷累加
                    BigDecimal total = BigDecimal.ZERO;
                    List<Long> eventIds = new ArrayList<>();
                    if(null==eventList || eventList.size()==0) {
                       //造省级数据
                        //需求响应省级实时压减负荷（万千瓦）
                        boolean flag = true;
                        if(flag) {
                            BigDecimal val = new BigDecimal("0.00");
                            Map<String, Object> map = null;
                            map = setParam(provinceCode, "10202001", "real_reduce_Cap", "需求响应省级实时压减负荷(万千瓦)", "03", "00000",dateFormat2.format(date));
                            map.put("INDEX_VALUE", val);
                            records.add(map);
                            //当日需求响应省级执行户数（户）
                            int val2 = 0;
                            Map<String, Object> map2 = null;
                            map2 = setParam(provinceCode, "10302001", "total_execute_cons_count", "当日需求响应省级执行户数", "03", "00000",dateFormat2.format(date));
                            map2.put("INDEX_VALUE", val2);
                            records.add(map2);
                            //当日需求响应省级最大压减负荷(万千瓦)
                            BigDecimal val3 = new BigDecimal("0.00");
                            Map<String, Object> map3 = null;
                            map3 = setParam(provinceCode, "10302002", "reduce_Cap", "当日需求响应省级最大压减负荷(万千瓦)", "03", "00000",dateFormat2.format(date));
                            map3.put("INDEX_VALUE", val3);
                            records.add(map3);
                            //当日需求响应省级目标负荷平均执行到位率
                            BigDecimal val4 = new BigDecimal("0.0000");
                            Map<String, Object> map4 = null;
                            map4 = setParam(provinceCode, "10302003", "execute_rate", "当日需求响应省级目标负荷平均执行到位率", "03", "00000",dateFormat2.format(date));
                            map4.put("INDEX_VALUE", val4);
                            records.add(map4);
                        }
                        //造市级数据
                        if (null != orgsList && orgsList.size() > 0) {
                            OrgUtils orgUtils = new OrgUtils();
                            for (SysOrgs org : orgsList) {
                                String name = "";
                                List<String> orgs = orgUtils.getData(datas, org.getId(), new ArrayList<>());
                                List<SysOrgs> single = orgsListDate.stream().filter(n -> org.getId().equals(n.getId())).collect(Collectors.toList());
                                if (null != single && single.size() > 0) {
                                    name = single.get(0).getName();
                                }
                                //需求响应市级实时压减负荷（万千瓦）
                                Map<String, Object> mapCity = null;
                                mapCity = setParam(org.getId(), "10202001", "real_reduce_Cap", "需求响应" + name + "实时压减负荷(万千瓦)", "03", provinceCode,dateFormat2.format(date));
                                mapCity.put("INDEX_VALUE", new BigDecimal("0.00"));
                                records.add(mapCity);
                                //当日需求响应市级最大压减负荷(万千瓦)
                                Map<String, Object> map3 = null;
                                map3 = setParam(org.getId(), "10302002", "reduce_Cap", "当日需求响应" + name + "最大压减负荷(万千瓦)", "03", provinceCode,dateFormat2.format(date));
                                map3.put("INDEX_VALUE", new BigDecimal("0.00"));
                                records.add(map3);
                                log.error("pointsCity为空!");
                                //当日需求响应市级执行户数（户）
                                Map<String, Object> map2City = null;
                                map2City = setParam(org.getId(), "10302001", "total_execute_cons_count", "当日需求响应"+ name +"执行户数", "03", provinceCode,dateFormat2.format(date));
                                map2City.put("INDEX_VALUE", 0);
                                records.add(map2City);
                                //当日需求响应市级目标负荷平均执行到位率
                                Map<String, Object> map4 = null;
                                map4 = setParam(org.getId(), "10302003", "execute_rate", "当日需求响应" + name + "目标负荷平均执行到位率", "03", provinceCode,dateFormat2.format(date));
                                map4.put("INDEX_VALUE", new BigDecimal("0.0000"));
                                records.add(map4);
                                log.info("pointsCity为空或totalCity为0!");
                            }
                        }
                        log.info("无事件信息！");
                    } else {
                        for (Event event : eventList) {
                            eventIds.add(event.getEventId());
                        }
                        //全天所有事件点
                        List<BigDecimal> points = new ArrayList<>();
                        BigDecimal val = BigDecimal.ZERO;
                        BigDecimal valReal = BigDecimal.ZERO;
                        for (Event event : eventList) {
                            int startP = CurveUtil.covDateTimeToPoint(event.getStartTime());
                            int endP = CurveUtil.covDateTimeToPoint(event.getEndTime());
                            //获取事件基线
                            LambdaQueryWrapper<EventPowerBase> lambdaQueryWrapper2 = new LambdaQueryWrapper<>();
                            //lambdaQueryWrapper2.eq(EventPowerBase::getOrgNo,provinceCode);
                            //lambdaQueryWrapper2.in(EventPowerBase::getEventId,eventIds);
                            lambdaQueryWrapper2.eq(EventPowerBase::getEventId, event.getEventId());
                            List<EventPowerBase> eventPowerBases = eventPowerBaseService.list(lambdaQueryWrapper2);

                            //获取事件实时曲线
                            LambdaQueryWrapper<EventPower> lambdaQueryWrapper3 = new LambdaQueryWrapper<>();
                            //lambdaQueryWrapper3.eq(EventPower::getOrgNo,provinceCode);
                            //lambdaQueryWrapper3.in(EventPower::getEventId,eventIds);
                            lambdaQueryWrapper3.eq(EventPower::getEventId, event.getEventId());
                            List<EventPower> eventPowers = eventPowerService.list(lambdaQueryWrapper3);
                            if (null != eventPowerBases && eventPowerBases.size() > 0 && null != eventPowers && eventPowers.size() > 0) {
                                String codeP = provinceCode;
                                //省级事件曲线
                                List<EventPower> eventPowerList = eventPowers.stream().filter(baseLineDetail -> baseLineDetail.getOrgNo().equals(codeP)
                                ).collect(Collectors.toList());
                                //省级事件基线
                                List<EventPowerBase> eventPowerBaseList = eventPowerBases.stream().filter(baseLineDetail -> baseLineDetail.getOrgNo().equals(codeP)
                                ).collect(Collectors.toList());
                                Calendar cal = Calendar.getInstance();
                                // 当前小时
                                int hour = cal.get(Calendar.HOUR_OF_DAY);
                                // 当前分钟
                                int minute = cal.get(Calendar.MINUTE);
                                String time = hour + ":" + minute;
                                int point = CurveUtil.covDateTimeToPoint(time);
                                if (point < startP || point > endP) {
                                    for (int i = startP; i <= endP; i++) {
                                        if (null != eventPowerList && eventPowerList.size() > 0 && null != eventPowerBaseList && eventPowerBaseList.size() > 0) {
                                            //事件基线
                                            BigDecimal fieldValue1 = (BigDecimal) ReflectUtil.getFieldValue(eventPowerBaseList.get(0), "p" + i);
                                            //实时曲线
                                            BigDecimal fieldValue2 = (BigDecimal) ReflectUtil.getFieldValue(eventPowerList.get(0), "p" + i);
                                            if (null != fieldValue1 && null != fieldValue2) {
                                                BigDecimal pointValue = NumberUtil.sub(fieldValue1, fieldValue2);
                                                points.add(pointValue);
                                            } else {
                                                points.add(BigDecimal.ZERO);
                                            }
                                        } else {
                                            points.add(BigDecimal.ZERO);
                                        }
                                    }
                                    log.warn("当前点不在开始结束时间之间");
                                } else {
                                    for (int i = startP; i <= endP; i++) {
                                        if (null != eventPowerList && eventPowerList.size() > 0 && null != eventPowerBaseList && eventPowerBaseList.size() > 0) {
                                            //事件基线
                                            BigDecimal fieldValue1 = (BigDecimal) ReflectUtil.getFieldValue(eventPowerBaseList.get(0), "p" + i);
                                            //实时曲线
                                            BigDecimal fieldValue2 = (BigDecimal) ReflectUtil.getFieldValue(eventPowerList.get(0), "p" + i);
                                            if (null != fieldValue1 && null != fieldValue2) {
                                                BigDecimal pointValue = NumberUtil.sub(fieldValue1, fieldValue2);
                                                points.add(pointValue);
                                            } else {
                                                points.add(BigDecimal.ZERO);
                                            }
                                        } else {
                                            points.add(BigDecimal.ZERO);
                                        }
                                    }
                                    if (null != eventPowerList && eventPowerList.size() > 0 && null != eventPowerBaseList && eventPowerBaseList.size() > 0) {
                                        BigDecimal valf1 = (BigDecimal) ReflectUtil.getFieldValue(eventPowerBaseList.get(0), "p" + point);
                                        BigDecimal valf2 = (BigDecimal) ReflectUtil.getFieldValue(eventPowerList.get(0), "p" + point);

                                        if (null != valf1 && null != valf2) {
                                            val = NumberUtil.add(val,NumberUtil.sub(valf1, valf2));
                                            if(NumberUtil.sub(valf1, valf2).compareTo(BigDecimal.ZERO)>0) {
                                                valReal = NumberUtil.sub(valf1, valf2);
                                            }
                                        }
                                        val = NumberUtil.div(val, 10000);
                                        val = accuracy(val,1);
                                    }
                                }
                            } else {
                                log.error("无事件基线或事件实时曲线!");
                            }
                            total = NumberUtil.add(total, event.getRegulateCap());
                        }
                        //需求响应省级实时压减负荷（万千瓦）
                        Map<String, Object> map = null;
                        map = setParam(provinceCode, "10202001", "real_reduce_Cap", "需求响应省级实时压减负荷(万千瓦)", "03", "00000",dateFormat2.format(date));
                        if (BigDecimal.ZERO.compareTo(valReal) < 0) {
                            map.put("INDEX_VALUE", valReal);
                        } else {
                            map.put("INDEX_VALUE", new BigDecimal("0.00"));
                        }
                        records.add(map);
                        //当日需求响应省级最大压减负荷(万千瓦)
                        if (points.size() > 0) {
                            BigDecimal val3 = CollectionUtil.max(points);
                            val3 = NumberUtil.div(val3, 10000);
                            if (BigDecimal.ZERO.compareTo(val3) < 0) {
                                val3 = accuracy(val3,1);
                            } else {
                                val3 = new BigDecimal("0.00");
                            }
                            Map<String, Object> map3 = null;
                            map3 = setParam(provinceCode, "10302002", "reduce_Cap", "当日需求响应省级最大压减负荷(万千瓦)", "03", "00000",dateFormat2.format(date));
                            map3.put("INDEX_VALUE", val3);
                            records.add(map3);
                        } else {
                            Map<String, Object> map3 = null;
                            map3 = setParam(provinceCode, "10302002", "reduce_Cap", "当日需求响应省级最大压减负荷(万千瓦)", "03", "00000",dateFormat2.format(date));
                            map3.put("INDEX_VALUE", new BigDecimal("0.00"));
                            records.add(map3);
                            log.error("points为空!");
                        }

                        //当日需求响应省级执行户数（户）
                        Integer val2 = eventMapper.getConsCountByEvents(eventIds);
                        Map<String, Object> map2 = null;
                        map2 = setParam(provinceCode, "10302001", "total_execute_cons_count", "当日需求响应省级执行户数", "03", "00000",dateFormat2.format(date));
                        map2.put("INDEX_VALUE", val2);
                        records.add(map2);

                        //当日需求响应省级目标负荷平均执行到位率
                        if (total.compareTo(BigDecimal.ZERO) > 0 && null != points && points.size() > 0 ) {
                            BigDecimal sum = points.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
                            BigDecimal val4 = NumberUtil.div(sum, total);
                            val4 = NumberUtil.div(val4, points.size());
                            if (BigDecimal.ZERO.compareTo(val4) < 0) {
                                val4 = accuracy(val4,2);
                            } else {
                                val4 = new BigDecimal("0.0000");
                            }
                            Map<String, Object> map4 = null;
                            map4 = setParam(provinceCode, "10302003", "execute_rate", "当日需求响应省级目标负荷平均执行到位率", "03", "00000",dateFormat2.format(date));
                            map4.put("INDEX_VALUE", val4);
                            records.add(map4);
                        } else {
                            Map<String, Object> map4 = null;
                            map4 = setParam(provinceCode, "10302003", "execute_rate", "当日需求响应省级目标负荷平均执行到位率", "03", "00000",dateFormat2.format(date));
                            map4.put("INDEX_VALUE", new BigDecimal("0.0000"));
                            records.add(map4);
                            log.info("points为空或total为0!");
                        }
                        //市级需求响应数据处理
                        if (null != orgsList && orgsList.size() > 0) {
                            OrgUtils orgUtils = new OrgUtils();
                            for (SysOrgs org : orgsList) {
                                BigDecimal totalCity = BigDecimal.ZERO;
                                String name = "";
                                List<String> orgs = orgUtils.getData(datas, org.getId(), new ArrayList<>());
                                List<SysOrgs> single = orgsListDate.stream().filter(n -> org.getId().equals(n.getId())).collect(Collectors.toList());
                                if (null != single && single.size() > 0) {
                                    name = single.get(0).getName();
                                }
                                List<BigDecimal> pointsCity = new ArrayList<>();
                                BigDecimal valCity = new BigDecimal("0.00");
                                BigDecimal valCityReal = new BigDecimal("0.00");
                                for (Event event : eventList) {
                                    int startP = CurveUtil.covDateTimeToPoint(event.getStartTime());
                                    int endP = CurveUtil.covDateTimeToPoint(event.getEndTime());
                                    //获取事件基线
                                    LambdaQueryWrapper<EventPowerBase> lambdaQueryWrapper2 = new LambdaQueryWrapper<>();
                                    lambdaQueryWrapper2.eq(EventPowerBase::getEventId, event.getEventId());
                                    List<EventPowerBase> eventPowerBases = eventPowerBaseService.list(lambdaQueryWrapper2);

                                    //获取事件实时曲线
                                    LambdaQueryWrapper<EventPower> lambdaQueryWrapper3 = new LambdaQueryWrapper<>();
                                    lambdaQueryWrapper3.eq(EventPower::getEventId, event.getEventId());
                                    List<EventPower> eventPowers = eventPowerService.list(lambdaQueryWrapper3);
                                    if (null != eventPowerBases && eventPowerBases.size() > 0 && null != eventPowers && eventPowers.size() > 0) {
                                        String codeP = org.getId();
                                        //市级事件曲线
                                        List<EventPower> eventPowerList = eventPowers.stream().filter(baseLineDetail -> baseLineDetail.getOrgNo().equals(codeP)
                                        ).collect(Collectors.toList());
                                        //市级事件基线
                                        List<EventPowerBase> eventPowerBaseList = eventPowerBases.stream().filter(baseLineDetail -> baseLineDetail.getOrgNo().equals(codeP)
                                        ).collect(Collectors.toList());
                                        Calendar cal = Calendar.getInstance();
                                        cal.add(Calendar.MINUTE,-15);
                                        // 当前小时
                                        int hour = cal.get(Calendar.HOUR_OF_DAY);
                                        // 当前分钟
                                        int minute = cal.get(Calendar.MINUTE);
                                        String time = hour + ":" + minute;
                                        int point = CurveUtil.covDateTimeToPoint(time);
                                        if (point < startP || point > endP) {
                                            for (int i = startP; i <= endP; i++) {
                                                if (null != eventPowerList && eventPowerList.size() > 0 && null != eventPowerBaseList && eventPowerBaseList.size() > 0) {
                                                    //事件基线
                                                    BigDecimal fieldValue1 = (BigDecimal) ReflectUtil.getFieldValue(eventPowerBaseList.get(0), "p" + i);
                                                    //实时曲线
                                                    BigDecimal fieldValue2 = (BigDecimal) ReflectUtil.getFieldValue(eventPowerList.get(0), "p" + i);
                                                    if (null != fieldValue1 && null != fieldValue2) {
                                                        BigDecimal pointValue = NumberUtil.sub(fieldValue1, fieldValue2);
                                                        pointsCity.add(pointValue);
                                                    } else {
                                                        pointsCity.add(BigDecimal.ZERO);
                                                    }
                                                } else {
                                                    pointsCity.add(BigDecimal.ZERO);
                                                }
                                            }
                                            log.warn("当前点不在开始结束时间之间");
                                        } else {
                                            for (int i = startP; i <= endP; i++) {
                                                if (null != eventPowerList && eventPowerList.size() > 0 && null != eventPowerBaseList && eventPowerBaseList.size() > 0) {
                                                    //事件基线
                                                    BigDecimal fieldValue1 = (BigDecimal) ReflectUtil.getFieldValue(eventPowerBaseList.get(0), "p" + i);
                                                    //实时曲线
                                                    BigDecimal fieldValue2 = (BigDecimal) ReflectUtil.getFieldValue(eventPowerList.get(0), "p" + i);
                                                    if (null != fieldValue1 && null != fieldValue2) {
                                                        BigDecimal pointValue = NumberUtil.sub(fieldValue1, fieldValue2);
                                                        pointsCity.add(pointValue);
                                                    } else {
                                                        pointsCity.add(BigDecimal.ZERO);
                                                    }
                                                } else {
                                                    pointsCity.add(BigDecimal.ZERO);
                                                }
                                            }
                                            if (null != eventPowerList && eventPowerList.size() > 0 && null != eventPowerBaseList && eventPowerBaseList.size() > 0) {
                                                BigDecimal valf1 = (BigDecimal) ReflectUtil.getFieldValue(eventPowerBaseList.get(0), "p" + point);
                                                BigDecimal valf2 = (BigDecimal) ReflectUtil.getFieldValue(eventPowerList.get(0), "p" + point);
                                                if (null != valf1 && null != valf2) {
                                                    valCity = NumberUtil.add(valCity,NumberUtil.sub(valf1, valf2));
                                                    if(NumberUtil.sub(valf1, valf2).compareTo(BigDecimal.ZERO)>0) {
                                                        valCityReal = NumberUtil.sub(valf1, valf2);
                                                    }
                                                }
                                                valCity = NumberUtil.div(valCity, 10000);
                                                valCity = accuracy(valCity,1);
                                            }
                                        }
                                    } else {
                                        log.error("无事件基线或事件实时曲线!");
                                    }
                                    totalCity = NumberUtil.add(totalCity, event.getRegulateCap());
                                }
                                //需求响应市级实时压减负荷（万千瓦）
                                Map<String, Object> mapCity = null;
                                mapCity = setParam(org.getId(), "10202001", "real_reduce_Cap", "需求响应" + name + "实时压减负荷(万千瓦)", "03", provinceCode,dateFormat2.format(date));
                                if (BigDecimal.ZERO.compareTo(valCityReal) < 0) {
                                    mapCity.put("INDEX_VALUE", valCityReal);
                                } else {
                                    mapCity.put("INDEX_VALUE", new BigDecimal("0.00"));
                                }
                                records.add(mapCity);
                                //当日需求响应市级最大压减负荷(万千瓦)
                                if (pointsCity.size() > 0) {
                                    BigDecimal val3 = CollectionUtil.max(pointsCity);
                                    val3 = NumberUtil.div(val3, 10000);
                                    val3 = accuracy(val3,1);
                                    Map<String, Object> map3 = null;
                                    map3 = setParam(org.getId(), "10302002", "reduce_Cap", "当日需求响应" + name + "最大压减负荷(万千瓦)", "03", provinceCode,dateFormat2.format(date));
                                    if (BigDecimal.ZERO.compareTo(val3) < 0) {
                                        map3.put("INDEX_VALUE", val3);
                                    } else {
                                        map3.put("INDEX_VALUE", new BigDecimal("0.00"));
                                    }
                                    records.add(map3);
                                } else {
                                    Map<String, Object> map3 = null;
                                    map3 = setParam(org.getId(), "10302002", "reduce_Cap", "当日需求响应" + name + "最大压减负荷(万千瓦)", "03", provinceCode,dateFormat2.format(date));
                                    map3.put("INDEX_VALUE", new BigDecimal("0.00"));
                                    records.add(map3);
                                    log.error("pointsCity为空!");
                                }

                                //当日需求响应市级执行户数（户）
                                Integer val2City = eventMapper.getConsCountByEventsAndOrgs(eventIds, orgs);
                                Map<String, Object> map2City = null;
                                map2City = setParam(org.getId(), "10302001", "total_execute_cons_count", "当日需求响应" + name + "执行户数", "03", provinceCode,dateFormat2.format(date));
                                map2City.put("INDEX_VALUE", val2City);
                                records.add(map2City);

                                //当日需求响应市级目标负荷平均执行到位率
                                if (totalCity.compareTo(BigDecimal.ZERO) > 0 && (null!=pointsCity && pointsCity.size()>0)) {
                                    BigDecimal sum = pointsCity.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
                                    BigDecimal val4 = NumberUtil.div(sum, totalCity);
                                    val4 = NumberUtil.div(val4, points.size());
                                    val4 = accuracy(val4,2);
                                    Map<String, Object> map4 = null;
                                    map4 = setParam(org.getId(), "10302003", "execute_rate", "当日需求响应" + name + "目标负荷平均执行到位率", "03", provinceCode,dateFormat2.format(date));
                                    if (BigDecimal.ZERO.compareTo(val4) <= 0) {
                                        map4.put("INDEX_VALUE", val4);
                                    } else {
                                        map4.put("INDEX_VALUE", new BigDecimal("0.0000"));
                                    }
                                    records.add(map4);
                                } else {
                                    Map<String, Object> map4 = null;
                                    map4 = setParam(org.getId(), "10302003", "execute_rate", "当日需求响应" + name + "目标负荷平均执行到位率", "03", provinceCode,dateFormat2.format(date));
                                    map4.put("INDEX_VALUE", new BigDecimal("0.0000"));
                                    records.add(map4);
                                    log.info("valCity为空或totalCity为0!");
                                }

                            }
                        }
                    }
                } else {
                    log.error("6大指标数据获取方式配置错误!");
                }
                if(records.size()>0) {
                    data.put("records",records);
                    String res = null;
                    res = HttpUtil.post(url,data.toString());
                    if(null!=res) {
                        JSONObject jsonObject = JSONObject.parseObject(res);
                        if(null!=jsonObject) {
                            JSONArray jsonArray = jsonObject.getJSONArray("ID_List");
                            if(null!=jsonArray && jsonArray.size()>0) {
                                log.info("数据保存成功,返回id为:"+jsonArray.toJSONString());
                            }
                        }
                    } else {
                        log.error("返回值为空，保存失败!");
                    }
                }
            } else {
                log.error("未配置6大指标数据获取方式!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    //通过程序造数据
    public int getByProcedure() {
        //获取50以内随机整数
        int val = (int)(Math.random()*50+1);
        return val;
    }

    //通过程序造数据
    public double getByProcedure2() {
        //获取2以内随机小数
        Random a = new Random();
        double val = a.nextDouble()*2;
        return val;
    }

    //通过程序造数据
    public double getByProcedure3() {
        //获取20以内随机小数
        Random a = new Random();
        double val = a.nextDouble()*20;
        return val;
    }

    //通过程序造数据
    public double getByProcedure4() {
        //获取1以内随机小数
        Random a = new Random();
        double val = a.nextDouble();
        return val;
    }

    /**
     *
     * @param provinceCode 省编码
     * @param code  指标编码
     * @param indexCode  指标英文代码
     * @param name  指标名称
     * @param faeq  数据上报频率 01—实时；02—按日上报；03—15分钟/次；04—一周/次
     * @return
     */
    public Map<String,Object> setParam(String provinceCode,String code,String indexCode,String name,String faeq,String parentCode,String date) {
        Map<String,Object> map = new HashMap<>();
        String date2 = null;
        try {
            Date date1 = dateFormat3.parse(date);
            date2 = dateFormat3.format(date1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String id = provinceCode + "_" + code + "_" + dateFormat.format(new Date());
        //主键标识
        map.put("INDEX_ID",id);
        //省级编码
        map.put("INDEX_ORG_NO",provinceCode);
        //上级管理单位
        map.put("INDEX_P_ORG_NO", parentCode);
        //所属客户编码
        map.put("INDEX_CONS_NO","");
        //指标编码
        map.put("INDEX_NO",code);
        //指标英文编码
        map.put("INDEX_CODE",indexCode);
        //指标中文名称
        map.put("INDEX_NAME",name);
        //指标数据时间
        map.put("DATA_DATE",date2);
        //创建时间
        map.put("CREATE_TIME",date);
        //更新时间
        map.put("UPDATE_TIME",date);
        //上传时间
        map.put("UPLOAD_TIME",date);
        //数据来自哪个系统，01—有序用电；02—需求响应省级
        map.put("DATA_SOURCE","02");
        //是否必报指标 ，01—必须指标；02—可选指标
        map.put("IS_REQUIRED","01");
        //数据上报频率
        map.put("DATA_FREQ",faeq);
        return map;
    }

   /* //耗内存代码
    @XxlJob("testRuleReport")
    public ReturnT<String> testRuleReport(String param) throws Exception{
        XxlJobLogger.log("指标上报");
        log.info("执行开始执行!");
        long star = System.currentTimeMillis();
        List<ConsCurve> list = new ArrayList<>();
        List<ConsCurve> consCurves = consCurveService.list();
        ConsCurve consCurveBase1 = new ConsCurve();
        if(null!=consCurves && consCurves.size()>0) {
            consCurveBase1 = consCurves.get(0);
        }
        //查询数据字典url
        Integer number = 0;
        DictTypeParam dictTypeParam = new DictTypeParam();
        dictTypeParam.setCode("number_test");
        List<Dict> dicts = dictTypeService.dropDown(dictTypeParam);
        if (null != dicts && dicts.size() > 0) {
            Object value = dicts.get(0).get("value");
            if (null != value) {
                number = (Integer) value;
                if (null != number) {
                    number = 5000000;
                }
            }
        } else {
            number = 5000000;
        }
        for (int i = 0; i <= number; i++) {
            ConsCurve consCurveBase = new ConsCurve();
            BeanUtils.copyProperties(consCurveBase1, consCurveBase);
            consCurveBase.setDataDate(LocalDate.now());
            list.add(consCurveBase);
        }
        long end = System.currentTimeMillis();
        long time = (end-star)/1000;
        log.info("运行结束,耗时:" + time + "s");
        return ReturnT.SUCCESS;
    }*/
    /**
     * BigDecimal 精度  四舍五入
     *
     * @param param bigDecimal 类型
     * @param type  1---->保留2位，2----->保留4位（百分比）
     * @return BigDecimal
     */
    public BigDecimal accuracy(BigDecimal param, int type) {
        if (type == 1) {
            return new BigDecimal(param.toPlainString()).setScale(2, BigDecimal.ROUND_HALF_UP);
        } else {
            return new BigDecimal(param.toPlainString()).setScale(4, BigDecimal.ROUND_HALF_UP);
        }
    }

}
