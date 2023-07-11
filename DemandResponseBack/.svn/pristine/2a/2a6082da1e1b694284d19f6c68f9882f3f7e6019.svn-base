package com.xqxy.executor.service.jobhandler;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.log.Log;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xqxy.core.client.SystemClientService;
import com.xqxy.dr.modular.device.entity.SysOrgs;
import com.xqxy.dr.modular.event.mapper.EventMapper;
import com.xqxy.dr.modular.event.mapper.PlanConsMapper;
import com.xqxy.executor.entity.Schedule;
import com.xqxy.executor.entity.ScheduleOfCustomers;
import com.xqxy.sys.modular.dict.param.DictTypeParam;
import com.xqxy.sys.modular.dict.service.DictTypeService;
import com.xqxy.sys.modular.utils.JSONObjectToEntityUtils;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.log.XxlJobLogger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;
import javax.validation.constraints.NotNull;
import java.io.StringReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description 需求响应计划表、需求响应计划客户明细表
 * @Author Rabbit
 * @Date 2022/7/4 9:47
 */
@Component
public class ScheduleAndScheduleOfCustomersJob {
    private static final Log log = Log.get();

    @Value("${exchCurve.jdbcUrl:}")
    private String mdtfJdbcUrl;

    @Value("${exchCurve.username:}")
    private String mdtfUser;

    @Value("${exchCurve.password:}")
    private String mdtfPassword;

    private JdbcTemplate jdbcTemplate;

    @Resource
    private SystemClientService systemClientService;

    @Resource
    private DictTypeService dictTypeService;

    @Resource
    private EventMapper eventMapper;

    @Resource
    private PlanConsMapper planConsMapper;

    @PostConstruct
    public void init() {
        DataSource dataSource = DataSourceBuilder.create()
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .url(mdtfJdbcUrl)
                .username(mdtfUser)
                .password(mdtfPassword)
                .build();
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * 需求响应计划表、需求响应计划客户明细表--------------->日前邀约削峰 3:3推送
     *
     * @param param
     * @return
     * @throws Exception
     */
    @XxlJob("daysAgoScheduleAndScheduleOfCustomers")
    public ReturnT<String> daysAgoScheduleAndScheduleOfCustomers(String param) throws Exception {
        XxlJobLogger.log("需求响应计划表、需求响应计划客户明细表--------------->日前邀约削峰");
        // 创建一个没有默认值的空属性列表
        /*
          格式:
          key1=value1
          key2=value2
          ...
         */
        Properties properties = new Properties();
        // load--->以简单的面向行的格式从输入字符流中读取属性列表（键和元素对）。
        // reader-->输入字符流;创建一个新的字符串阅读器参数：s–->提供字符流的字符串。
        properties.load(new StringReader(param));
        // getProperty--->获取单个参数或者多个参数（参数需要根据key获取，多参之间逗号分割）  顺序与方法中参数保持一致
        // 在此属性列表中搜索具有指定键的属性。如果在该属性列表中未找到该键，则递归地检查默认属性列表及其默认值。如果未找到该属性，则该方法返回null 。
        // 参数：key - 属性键。
        // 回报：此属性列表中具有指定键值的值。
        log.info("currentDate：" + properties.getProperty("currentDate"));
        this.saveDaysAgoScheduleAndScheduleOfCustomers(properties.getProperty("currentDate"), properties.getProperty("eventId"));
        return ReturnT.SUCCESS;
    }

    /**
     * 需求响应计划表、需求响应计划客户明细表--------------->日内邀约削峰 每15分钟推一次
     *
     * @param param
     * @return
     * @throws Exception
     */
    @XxlJob("intradayScheduleAndScheduleOfCustomers")
    public ReturnT<String> intradayScheduleAndScheduleOfCustomers(String param) throws Exception {
        XxlJobLogger.log("需求响应计划表、需求响应计划客户明细表--------------->日内邀约削峰");
        this.saveIntradayScheduleAndScheduleOfCustomers(param);
        return ReturnT.SUCCESS;
    }

    /**
     * 需求响应计划表、需求响应计划客户明细表--------------->实时邀约削峰 每15分钟推一次
     *
     * @param param
     * @return
     * @throws Exception
     */
    @XxlJob("realTimeScheduleAndScheduleOfCustomers")
    public ReturnT<String> realTimeScheduleAndScheduleOfCustomers(String param) throws Exception {
        XxlJobLogger.log("需求响应计划表、需求响应计划客户明细表--------------->实时邀约削峰");
        this.saveRealTimeScheduleAndScheduleOfCustomers(param);
        return ReturnT.SUCCESS;
    }

    /**
     * 需求响应计划表、需求响应计划客户明细表--------------->实时邀约削峰
     *
     * @param param
     */
    private void saveRealTimeScheduleAndScheduleOfCustomers(String param) {
        log.info("需求响应计划表、需求响应计划客户明细表--------------->实时邀约削峰开始执行！！！！！！");
        // 计划表
        String sked = "exch_demand_response_plan_info";
        // 计划客户明细表
        String planSpecification = "exch_demand_response_plan_cons_rela";

        // 计划表数据
        List<Map<String, Object>> records = new ArrayList<>();
        // 计划客户明细表数据
        List<Map<String, Object>> records1 = new ArrayList<>();

        // 省级编码
        String provinceCode = null;
        // 获取市级组织机构
        List<SysOrgs> orgsList = null;
        List<SysOrgs> orgsList1 = null;
        JSONArray datas = null;
        List<SysOrgs> orgsListDate = new ArrayList<>();
        try {
            // 获取省级编码
            DictTypeParam dictTypeParam3 = new DictTypeParam();
            dictTypeParam3.setCode("province_code");
            List<Dict> list3 = dictTypeService.dropDown(dictTypeParam3);
            Object value2 = null;
            if (null != list3 && list3.size() > 0) {
                for (Dict dict : list3) {
                    if ("anhui_org_code".equals(dict.get("code"))) {
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
            // 获取所有组织机构
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
                // 筛选省级组织机构 orgTitle-->1--->省级
                orgsList1 = orgsListDate.stream().filter(n -> "1".equals(n.getOrgTitle())).collect(Collectors.toList());
                // 筛选市级组织机构 orgTitle----->2---->市级
                orgsList = orgsListDate.stream().filter(n -> "2".equals(n.getOrgTitle())).collect(Collectors.toList());
            } else {
                log.warn("组织机构不存在");
                return;
            }
            SysOrgs sysOrgs = orgsList1.get(0);
            String name = null;
            if (sysOrgs.getId().equals(provinceCode)) name = sysOrgs.getName();
            List<Schedule> realTimeSchedule = eventMapper.realTimeSchedule();
            if (CollectionUtils.isEmpty(realTimeSchedule) && realTimeSchedule.size() == 0) {
                log.info("无需求响应计划表----->实时邀约削峰，所以俩张表不存入数据，任务执行结束！！！！！！");
                return;
            } else {
                for (Schedule schedule : realTimeSchedule) {
                    schedule.setRegulation(accuracy(schedule.getRegulation(), 1));
                    schedule.setProvinceCode(provinceCode);
                    schedule.setParentCode("00000");
                    Map<String, Object> map = null;
                    map = setSchedule(schedule, 1, 1, name);
                    records.add(map);
                    String id = String.valueOf(map.get("ID"));
                    List<ScheduleOfCustomers> realTimeScheduleOfCustomers = planConsMapper.realTimeScheduleOfCustomers(String.valueOf(map.get("PLAN_NO")));
                    for (ScheduleOfCustomers scheduleOfCustomers : realTimeScheduleOfCustomers) {
                        // 翻译行业类型
                        DictTypeParam dictTypeParam = new DictTypeParam();
                        dictTypeParam.setCode("HYFL_TRANS");
                        List<Dict> list = dictTypeService.dropDown(dictTypeParam);
                        Object value = null;
                        String YKL = null;
                        if (null != list && list.size() > 0) {
                            for (Dict dict : list) {
                                if (scheduleOfCustomers.getClassification().equals(dict.get("code"))) {
                                    value = dict.get("value");
                                } else {
                                    value = "13";
                                }
                            }
                            if (null != value) {
                                YKL = (String) value;
                            }
                        } else {
                            log.error("未配置该行业类分!");
                        }
                        scheduleOfCustomers.setContractedLoad(accuracy(scheduleOfCustomers.getContractedLoad(), 1));
                        scheduleOfCustomers.setResponseLoad(accuracy(scheduleOfCustomers.getResponseLoad(), 1));
                        scheduleOfCustomers.setProvinceCode(provinceCode);
                        scheduleOfCustomers.setParentCode("00000");
                        Map<String, Object> map1 = null;
                        map1 = setScheduleOfCustomers(scheduleOfCustomers, id, 1, name, YKL);
                        records1.add(map1);
                    }
                }
                log.info("需求响应计划表总条数----->实时邀约削峰: " + records.size());
                log.info("需求响应计划客户明细表总条数----->实时邀约削峰: " + records1.size());
                log.info("需求响应计划表----->实时邀约削峰: " + records);
                log.info("需求响应计划客户明细表----->实时邀约削峰: " + records1);
                if (records.size() > 0) {
                    records.forEach(item -> {
                        String sql = String.format("select id from %s where plan_no='%s'", sked, item.get("PLAN_NO"));
                        String idValue = jdbcTemplate.query(sql, rs -> {
                            if (rs.next()) {
                                return rs.getObject(1, String.class);
                            }
                            return null;
                        });
                        log.info("数据库是否插入过实时邀约削峰计划表数据：" + idValue);
                        String execSql = null;
                        if (idValue == null) {
                            execSql = generalInsertSql(sked, item);
                        } else {
                            item.remove("CREATE_TIME");
                            item.put("id", idValue);
                            execSql = generalUpdateSql(sked, item, "id");
                        }
                        log.info("execSql=> {}", execSql);
                        jdbcTemplate.execute(execSql);
                    });
                }
                if (records1.size() > 0) {
                    records1.forEach(item -> {
                        String sql = String.format("select id from %s where plan_no='%s' and cons_no='%s'", planSpecification, item.get("PLAN_NO"), item.get("CONS_NO"));
                        String idValue = jdbcTemplate.query(sql, rs -> {
                            if (rs.next()) {
                                return rs.getObject(1, String.class);
                            }
                            return null;
                        });
                        log.info("数据库是否插入过实时邀约削峰计划客户明细表数据：" + idValue);
                        String execSql = null;
                        if (idValue == null) {
                            execSql = generalInsertSql(planSpecification, item);
                        } else {
                            item.remove("CREATE_TIME");
                            item.put("id", idValue);
                            execSql = generalUpdateSql(planSpecification, item, "id");
                        }
                        log.info("execSql=> {}", execSql);
                        jdbcTemplate.execute(execSql);
                    });
                }
            }
            log.info("需求响应计划表、需求响应计划客户明细表----->实时邀约削峰执行结束！！！！！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 需求响应计划表、需求响应计划客户明细表----->日内邀约削峰
     *
     * @param param
     */
    private void saveIntradayScheduleAndScheduleOfCustomers(String param) {
        log.info("需求响应计划表、需求响应计划客户明细表----->日内邀约削峰开始执行！！！！！");
        // 计划表
        String sked = "exch_demand_response_plan_info";
        // 计划客户明细表
        String planSpecification = "exch_demand_response_plan_cons_rela";

        // 计划表数据
        List<Map<String, Object>> records = new ArrayList<>();
        // 计划客户明细表数据
        List<Map<String, Object>> records1 = new ArrayList<>();

        // 省级编码
        String provinceCode = null;
        // 获取市级组织机构
        List<SysOrgs> orgsList = null;
        List<SysOrgs> orgsList1 = null;
        JSONArray datas = null;
        List<SysOrgs> orgsListDate = new ArrayList<>();
        try {
            // 获取省级编码
            DictTypeParam dictTypeParam3 = new DictTypeParam();
            dictTypeParam3.setCode("province_code");
            List<Dict> list3 = dictTypeService.dropDown(dictTypeParam3);
            Object value2 = null;
            if (null != list3 && list3.size() > 0) {
                for (Dict dict : list3) {
                    if ("anhui_org_code".equals(dict.get("code"))) {
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
            // 获取所有组织机构
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
                // 筛选省级组织机构 orgTitle-->1--->省级
                orgsList1 = orgsListDate.stream().filter(n -> "1".equals(n.getOrgTitle())).collect(Collectors.toList());
                // 筛选市级组织机构 orgTitle----->2---->市级
                orgsList = orgsListDate.stream().filter(n -> "2".equals(n.getOrgTitle())).collect(Collectors.toList());
            } else {
                log.warn("组织机构不存在");
                return;
            }
            SysOrgs sysOrgs = orgsList1.get(0);
            String name = null;
            if (sysOrgs.getId().equals(provinceCode)) name = sysOrgs.getName();
            List<Schedule> intradaySchedule = eventMapper.intradaySchedule();
            if (CollectionUtils.isEmpty(intradaySchedule) && intradaySchedule.size() == 0) {
                log.info("无需求响应计划表----->日内邀约削峰，所以俩张表不存入数据，任务执行结束！！！！！！");
                return;
            } else {
                for (Schedule schedule : intradaySchedule) {
                    schedule.setRegulation(accuracy(schedule.getRegulation(), 1));
                    schedule.setProvinceCode(provinceCode);
                    schedule.setParentCode("00000");
                    Map<String, Object> map = null;
                    map = setSchedule(schedule, 2, 1, name);
                    records.add(map);
                    String id = String.valueOf(map.get("ID"));
                    List<ScheduleOfCustomers> intradayScheduleOfCustomers = planConsMapper.intradayScheduleOfCustomers(String.valueOf(map.get("PLAN_NO")));
                    for (ScheduleOfCustomers scheduleOfCustomers : intradayScheduleOfCustomers) {
                        // 翻译行业类型
                        DictTypeParam dictTypeParam = new DictTypeParam();
                        dictTypeParam.setCode("HYFL_TRANS");
                        List<Dict> list = dictTypeService.dropDown(dictTypeParam);
                        Object value = null;
                        String YKL = null;
                        if (null != list && list.size() > 0) {
                            for (Dict dict : list) {
                                if (scheduleOfCustomers.getClassification().equals(dict.get("code"))) {
                                    value = dict.get("value");
                                } else {
                                    value = "13";
                                }
                            }
                            if (null != value) {
                                YKL = (String) value;
                            }
                        } else {
                            log.error("未配置该行业类分!");
                        }
                        scheduleOfCustomers.setContractedLoad(accuracy(scheduleOfCustomers.getContractedLoad(), 1));
                        scheduleOfCustomers.setResponseLoad(accuracy(scheduleOfCustomers.getResponseLoad(), 1));
                        scheduleOfCustomers.setProvinceCode(provinceCode);
                        scheduleOfCustomers.setParentCode("00000");
                        Map<String, Object> map1 = null;
                        map1 = setScheduleOfCustomers(scheduleOfCustomers, id, 1, name, YKL);
                        records1.add(map1);
                    }
                }
                log.info("需求响应计划表总条数----->日内邀约削峰: " + records.size());
                log.info("需求响应计划客户明细表总条数----->日内邀约削峰: " + records1.size());
                log.info("需求响应计划表----->日内邀约削峰: " + records);
                log.info("需求响应计划客户明细表----->日内邀约削峰: " + records1);
                if (records.size() > 0) {
                    records.forEach(item -> {
                        String sql = String.format("select id from %s where plan_no='%s'", sked, item.get("PLAN_NO"));
                        String idValue = jdbcTemplate.query(sql, rs -> {
                            if (rs.next()) {
                                return rs.getObject(1, String.class);
                            }
                            return null;
                        });
                        log.info("数据库是否插入过日内邀约削峰计划表数据：" + idValue);
                        String execSql = null;
                        if (idValue == null) {
                            execSql = generalInsertSql(sked, item);
                        } else {
                            item.remove("CREATE_TIME");
                            item.put("id", idValue);
                            execSql = generalUpdateSql(sked, item, "id");
                        }
                        log.info("execSql=> {}", execSql);
                        jdbcTemplate.execute(execSql);
                    });
                }
                if (records1.size() > 0) {
                    records1.forEach(item -> {
                        String sql = String.format("select id from %s where plan_no='%s' and cons_no='%s'", planSpecification, item.get("PLAN_NO"), item.get("CONS_NO"));
                        String idValue = jdbcTemplate.query(sql, rs -> {
                            if (rs.next()) {
                                return rs.getObject(1, String.class);
                            }
                            return null;
                        });
                        log.info("数据库是否插入过日内邀约削峰计划客户明细表数据：" + idValue);
                        String execSql = null;
                        if (idValue == null) {
                            execSql = generalInsertSql(planSpecification, item);
                        } else {
                            item.remove("CREATE_TIME");
                            item.put("id", idValue);
                            execSql = generalUpdateSql(planSpecification, item, "id");
                        }
                        log.info("execSql=> {}", execSql);
                        jdbcTemplate.execute(execSql);
                    });
                }
            }
            log.info("需求响应计划表、需求响应计划客户明细表----->日内邀约削峰执行结束！！！！！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 需求响应计划表、需求响应计划客户明细表----->日前邀约削峰
     *
     * @param currentDate 当前日期（yyyy-MM-dd）
     */
    public void saveDaysAgoScheduleAndScheduleOfCustomers(String currentDate, String eventId) {
        log.info("需求响应计划表、需求响应计划客户明细表----->日前邀约削峰开始执行！！！！！");
        // 计划表
        String sked = "exch_demand_response_plan_info";
        // 计划客户明细表
        String planSpecification = "exch_demand_response_plan_cons_rela";

        // 计划表数据
        List<Map<String, Object>> records = new ArrayList<>();
        // 计划客户明细表数据
        List<Map<String, Object>> records1 = new ArrayList<>();

        // 省级编码
        String provinceCode = null;
        // 获取市级组织机构
        List<SysOrgs> orgsList = null;
        List<SysOrgs> orgsList1 = null;
        JSONArray datas = null;
        List<SysOrgs> orgsListDate = new ArrayList<>();
        try {
            // 获取省级编码
            DictTypeParam dictTypeParam3 = new DictTypeParam();
            dictTypeParam3.setCode("province_code");
            List<Dict> list3 = dictTypeService.dropDown(dictTypeParam3);
            Object value2 = null;
            if (null != list3 && list3.size() > 0) {
                for (Dict dict : list3) {
                    if ("anhui_org_code".equals(dict.get("code"))) {
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
            // 获取所有组织机构
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
                // 筛选省级组织机构 orgTitle-->1--->省级
                orgsList1 = orgsListDate.stream().filter(n -> "1".equals(n.getOrgTitle())).collect(Collectors.toList());
                // 筛选市级组织机构 orgTitle----->2---->市级
                orgsList = orgsListDate.stream().filter(n -> "2".equals(n.getOrgTitle())).collect(Collectors.toList());
            } else {
                log.warn("组织机构不存在");
                return;
            }
            SysOrgs sysOrgs = orgsList1.get(0);
            String name = null;
            if (sysOrgs.getId().equals(provinceCode)) name = sysOrgs.getName();
            List<Schedule> daysAgoSchedule = null;
            // 若定时任务传参 则使用这个参数查询，否则使用默认规则进行
            if (ObjectUtil.isNotEmpty(eventId)) {
                log.info("需求响应计划表、需求响应计划客户明细表----->日前邀约削峰定时任务手动推送指定事件计划参数：{}", eventId);
                daysAgoSchedule = eventMapper.daysAgoSchedule(null, eventId);
            } else if (ObjectUtil.isEmpty(currentDate)) {
                daysAgoSchedule = eventMapper.daysAgoSchedule(timeFormat(1), null);
            } else {
                log.info("需求响应计划表、需求响应计划客户明细表----->日前邀约削峰定时任务手动推送今天计划参数：" + currentDate);
                daysAgoSchedule = eventMapper.daysAgoSchedule(currentDate, null);
            }
            if (CollectionUtils.isEmpty(daysAgoSchedule) && daysAgoSchedule.size() == 0) {
                log.info("无需求响应计划表----->日前邀约削峰，所以俩张表不存入数据，任务执行结束！！！！！！");
                return;
            } else {
                for (Schedule schedule : daysAgoSchedule) {
                    schedule.setRegulation(accuracy(schedule.getRegulation(), 1));
                    schedule.setProvinceCode(provinceCode);
                    schedule.setParentCode("00000");
                    Map<String, Object> map = null;
                    map = setSchedule(schedule, 3, 1, name);
                    records.add(map);
                    String id = String.valueOf(map.get("ID"));
                    List<ScheduleOfCustomers> daysAgoScheduleOfCustomers = null;
                    if (ObjectUtil.isNotEmpty(eventId)) {
                        daysAgoScheduleOfCustomers = planConsMapper.daysAgoScheduleOfCustomers(null, String.valueOf(map.get("PLAN_NO")));
                    } else if (ObjectUtil.isEmpty(currentDate)) {
                        daysAgoScheduleOfCustomers = planConsMapper.daysAgoScheduleOfCustomers(timeFormat(1), String.valueOf(map.get("PLAN_NO")));
                    } else {
                        daysAgoScheduleOfCustomers = planConsMapper.daysAgoScheduleOfCustomers(currentDate, String.valueOf(map.get("PLAN_NO")));
                    }
                    for (ScheduleOfCustomers scheduleOfCustomers : daysAgoScheduleOfCustomers) {
                        // 翻译行业类型
                        DictTypeParam dictTypeParam = new DictTypeParam();
                        dictTypeParam.setCode("HYFL_TRANS");
                        List<Dict> list = dictTypeService.dropDown(dictTypeParam);
                        Object value = null;
                        String YKL = null;
                        if (null != list && list.size() > 0) {
                            for (Dict dict : list) {
                                if (scheduleOfCustomers.getClassification().equals(dict.get("code"))) {
                                    value = dict.get("value");
                                } else {
                                    value = "13";
                                }
                            }
                            if (null != value) {
                                YKL = (String) value;
                            }
                        } else {
                            log.error("未配置该行业类分!");
                        }
                        scheduleOfCustomers.setContractedLoad(accuracy(scheduleOfCustomers.getContractedLoad(), 1));
                        scheduleOfCustomers.setResponseLoad(accuracy(scheduleOfCustomers.getResponseLoad(), 1));
                        scheduleOfCustomers.setProvinceCode(provinceCode);
                        scheduleOfCustomers.setParentCode("00000");
                        Map<String, Object> map1 = null;
                        map1 = setScheduleOfCustomers(scheduleOfCustomers, id, 1, name, YKL);
                        records1.add(map1);
                    }
                }
                log.info("需求响应计划表总条数----->日前邀约削峰: " + records.size());
                log.info("需求响应计划客户明细表总条数----->日前邀约削峰: " + records1.size());
                log.info("需求响应计划表----->日前邀约削峰: " + records);
                log.info("需求响应计划客户明细表----->日前邀约削峰: " + records1);
                if (records.size() > 0) {
                    records.forEach(item -> {
                        String sql = String.format("select id from %s where plan_no='%s'", sked, item.get("PLAN_NO"));
                        String idValue = jdbcTemplate.query(sql, rs -> {
                            if (rs.next()) {
                                return rs.getObject(1, String.class);
                            }
                            return null;
                        });
                        log.info("数据库是否插入过日前邀约削峰计划表数据：" + idValue);
                        String execSql = null;
                        if (idValue == null) {
                            execSql = generalInsertSql(sked, item);
                        } else {
                            item.remove("CREATE_TIME");
                            item.put("id", idValue);
                            execSql = generalUpdateSql(sked, item, "id");
                        }
                        log.info("execSql=> {}", execSql);
                        jdbcTemplate.execute(execSql);
                    });
                }
                if (records1.size() > 0) {
                    records1.forEach(item -> {
                        String sql = String.format("select id from %s where plan_no='%s' and cons_no='%s'", planSpecification, item.get("PLAN_NO"), item.get("CONS_NO"));
                        String idValue = jdbcTemplate.query(sql, rs -> {
                            if (rs.next()) {
                                return rs.getObject(1, String.class);
                            }
                            return null;
                        });
                        log.info("数据库是否插入过日前邀约削峰计划客户明细表数据：" + idValue);
                        String execSql = null;
                        if (idValue == null) {
                            execSql = generalInsertSql(planSpecification, item);
                        } else {
                            item.remove("CREATE_TIME");
                            item.put("id", idValue);
                            execSql = generalUpdateSql(planSpecification, item, "id");
                        }
                        log.info("execSql=> {}", execSql);
                        jdbcTemplate.execute(execSql);
                    });
                }
            }
            log.info("需求响应计划表、需求响应计划客户明细表----->日前邀约削峰执行结束！！！！！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 新增
     *
     * @param tableName 表名称
     * @param fields    数据
     * @return
     */
    @NotNull(message = "逐条新增")
    private String generalInsertSql(@NotNull(message = "表名称") String tableName, @NotNull(message = "数据") Map<String, Object> fields) {
        StringBuilder sb1 = new StringBuilder("");
        StringBuilder sb2 = new StringBuilder("");
        Iterator<Map.Entry<String, Object>> iterator = fields.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> next = iterator.next();
            sb1.append(next.getKey());
            if (next.getValue() == null) {
                sb2.append("null");
            } else {
                sb2.append("'").append(next.getValue()).append("'");
            }
            if (iterator.hasNext()) {
                sb1.append(",");
                sb2.append(",");
            }
        }
        return String.format("insert into %s (%s) values (%s)", tableName, sb1, sb2);
    }

    /**
     * 修改
     *
     * @param tableName 表名称
     * @param fields    数据
     * @param idKey     主键名称
     * @return
     */
    @NotNull(message = "逐条修改")
    private String generalUpdateSql(@NotNull(message = "表名称") String tableName, @NotNull(message = "数据") Map<String, Object> fields, @NotNull(message = "主键名称") String idKey) {
        String setSql = fields.entrySet().stream().filter(item -> !idKey.equalsIgnoreCase(item.getKey()))
                .map(item -> {
                    if (item.getValue() != null) {
                        return item.getKey() + "='" + item.getValue() + "'";
                    } else {
                        return item.getKey() + "=null";
                    }
                }).collect(Collectors.joining(","));
        return String.format("update %s set %s where %s='%s'", tableName, setSql, idKey, fields.get(idKey));
    }

    /**
     * BigDecimal 精度  四舍五入
     *
     * @param param BigDecimal 类型
     * @param type  1---->保留2位，2----->保留4位（百分比）
     * @return BigDecimal
     */
    @NotNull(message = "BigDecimal 精度  四舍五入模式")
    private BigDecimal accuracy(@NotNull(message = "BigDecimal 类型") BigDecimal param, @NotNull(message = "1---->保留2位，其他数值----->保留4位（百分比）") int type) {
        if (type == 1) {
            return new BigDecimal(param.toPlainString()).setScale(2, BigDecimal.ROUND_HALF_UP);
        } else {
            return new BigDecimal(param.toPlainString()).setScale(4, BigDecimal.ROUND_HALF_UP);
        }
    }

    /**
     * 需求响应计划表
     *
     * @param schedule
     * @param responseType 响应类型 （1：实时需求响应，2：日内需求响应，3：日前需求响应）
     * @param isItValid    有效性标识：0:否,1:是； 全填----->1
     * @param name         管理单位名称
     * @return
     */
    @NotNull(message = "需求响应计划表封装方法")
    private Map<String, Object> setSchedule(@NotNull(message = "应传入需求响应计划表类") Schedule schedule, @NotNull(message = "响应类型 （1：实时需求响应，2：日内需求响应，3：日前需求响应）") Integer responseType, @NotNull(message = "有效性标识：0:否,1:是；") Integer isItValid, @NotNull(message = "管理单位名称") String name) {
        Map<String, Object> map = new HashMap<>();
        String id = schedule.getProvinceCode() + "_" + schedule.getEventId();
        // ID生成规则：省级管理单位编码拼接各省负荷管理系统提供需求响应计划编号，使用“_”连接；
        map.put("ID", id);
        // 计划编号
        map.put("PLAN_NO", schedule.getEventId());
        // 计划名称
        map.put("PLAN_NAME", schedule.getEventName());
        // 管理单位编码
        map.put("ORG_NO", schedule.getProvinceCode());
        // 管理单位名称
        map.put("ORG_NAME", name);
        // 上级管理单位编码  管理单位省级----->00000  市级------> 省级编码
        map.put("P_ORG_NO", schedule.getParentCode());
        // 上级管理单位名称
        map.put("P_ORG_NAME", "国家电网公司");
        // 执行开始时间 yyyy-MM-dd HH:mm:ss
        map.put("EXEC_STA_TIME", schedule.getExecutionStart());
        // 执行结束时间 yyyy-MM-dd HH:mm:ss
        map.put("EXEC_END_TIME", schedule.getEndOfExecution());
        // 调控目标（万kW）
        map.put("REGULATION_TARGET", schedule.getRegulation());
        // 需求响应类型（1：实时需求响应，2：日内需求响应，3：日前需求响应）
        map.put("DR_TYPE", responseType);
        // 有效性标识  有效性标识：0:否,1:是；
        map.put("IS_EFFECTIVE", isItValid);
        // 数据创建时间
        map.put("CREATE_TIME", timeFormat(2));
        // 数据更新时间
        map.put("UPDATE_TIME", timeFormat(2));
        // 数据上送时间
        map.put("UPLOAD_TIME", timeFormat(2));
        return map;
    }

    /**
     * 需求响应计划客户明细表
     *
     * @param scheduleOfCustomers
     * @param scheduleId          需求响应计划表ID
     * @param isItValid           有效性标识：0:否,1:是； 全填----->1
     * @param name                管理单位名称
     * @param YKL                 行业分类
     * @return
     */
    @NotNull(message = "需求响应计划客户明细表封装方法")
    private Map<String, Object> setScheduleOfCustomers(@NotNull(message = "应传入需求响应计划客户明细表类") ScheduleOfCustomers scheduleOfCustomers, @NotNull(message = "需求响应计划表plan_no") String scheduleId, @NotNull(message = "有效性标识：0:否,1:是；") Integer isItValid, @NotNull(message = "管理单位名称") String name, @NotNull(message = "翻译行业分类为标准分类") String YKL) {
        Map<String, Object> map = new HashMap<>();
        // 本条记录唯一标识 ID生成规则：需求响应计划信息表中ID与客户编号和UUID三个数据使用“_”拼接;
        // 简化的UUID，去掉了横线，使用性能更好的ThreadLocalRandom生成UUID    IdUtil.fastSimpleUUID()
        String id = scheduleId + "_" + scheduleOfCustomers.getConsNo() + "_" + IdUtil.fastSimpleUUID();
        map.put("ID", id);
        // 计划编号
        map.put("PLAN_NO", scheduleOfCustomers.getEventId());
        // 计划名称
        map.put("PLAN_NAME", scheduleOfCustomers.getEventName());
        // 管理单位编码
        map.put("ORG_NO", scheduleOfCustomers.getProvinceCode());
        // 管理单位名称
        map.put("ORG_NAME", name);
        // 上级管理单位编码
        map.put("P_ORG_NO", scheduleOfCustomers.getParentCode());
        // 上级管理单位名称
        map.put("P_ORG_NAME", "国家电网公司");
        // 客户编号
        map.put("CONS_NO", scheduleOfCustomers.getConsNo());
        // 行业类别
        map.put("TRADE_CODE", YKL);
        // 年度签约负荷（kW）
        map.put("YEAR_REG_LOAD", scheduleOfCustomers.getContractedLoad());
        // 应邀响应负荷（kW）
        map.put("INVIT_RESP_LOAD", scheduleOfCustomers.getResponseLoad());
        // 有效性标识 有效性标识：0:否,1:是；
        map.put("IS_EFFECTIVE", isItValid);
        // 数据创建时间
        map.put("CREATE_TIME", timeFormat(2));
        // 数据更新时间
        map.put("UPDATE_TIME", timeFormat(2));
        // 数据上送时间
        map.put("UPLOAD_TIME", timeFormat(2));
        return map;
    }


    /**
     * 时间
     *
     * @param type 类型 1-------->当前日前(yyyy-MM-dd)-1，也就是昨天、其他数值------->当前时间转化为 yyyy-MM-dd HH:mm:ss 格式
     * @return 时间字符串
     */
    private String timeFormat(@NotNull(message = "type不能传入null,应传入数值。") int type) {
        if (type == 1) {
            return LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } else {
            return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
    }
}