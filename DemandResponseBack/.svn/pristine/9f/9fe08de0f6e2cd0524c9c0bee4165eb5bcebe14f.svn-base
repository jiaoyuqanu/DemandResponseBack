package com.xqxy.executor.service.jobhandler;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.log.Log;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xqxy.core.client.SystemClientService;
import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.baseline.util.CurveUtil;
import com.xqxy.dr.modular.data.dto.WorkProjectInfoDTO;
import com.xqxy.dr.modular.device.entity.SysOrgs;
import com.xqxy.dr.modular.event.entity.Event;
import com.xqxy.dr.modular.event.entity.EventPower;
import com.xqxy.dr.modular.event.entity.EventPowerBase;
import com.xqxy.dr.modular.event.mapper.EventMapper;
import com.xqxy.dr.modular.event.mapper.PlanConsMapper;
import com.xqxy.dr.modular.event.service.EventPowerBaseService;
import com.xqxy.dr.modular.event.service.EventPowerService;
import com.xqxy.dr.modular.event.service.EventService;
import com.xqxy.dr.modular.eventEvaluation.mapper.EventEvaluationMapper;
import com.xqxy.dr.modular.project.mapper.ConsContractDetailMapper;
import com.xqxy.dr.modular.project.mapper.ConsContractInfoMapper;
import com.xqxy.dr.modular.project.mapper.ProjectMapper;
import com.xqxy.dr.modular.project.service.ConsContractInfoService;
import com.xqxy.executor.entity.ExecuteMaximumLoad;
import com.xqxy.sys.modular.cust.mapper.ConsMapper;
import com.xqxy.sys.modular.cust.mapper.CustMapper;
import com.xqxy.sys.modular.cust.mapper.UserConsRelaMapper;
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

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;
import javax.validation.constraints.NotNull;
import java.io.StringReader;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description 十六大指标
 * @Author Rabbit
 * @Date 2022/6/27 16:19
 */
@Component
public class SixteenIndicatorsJob {
    private static final Log log = Log.get();

    @Value("${exchCurve.jdbcUrl:}")
    private String mdtfJdbcUrl;

    @Value("${exchCurve.username:}")
    private String mdtfUser;

    @Value("${exchCurve.password:}")
    private String mdtfPassword;

    private JdbcTemplate jdbcTemplate;

    @Resource
    private CustMapper custMapper;

    @Resource
    private UserConsRelaMapper userConsRelaMapper;

    @Resource
    private ConsContractDetailMapper consContractDetailMapper;

    @Resource
    private DictTypeService dictTypeService;

    @Resource
    private SystemClientService systemClientService;

    @Resource
    private SystemClientService systemClient;

    @Resource
    private EventMapper eventMapper;

    @Resource
    private PlanConsMapper planConsMapper;

    @Resource
    private EventEvaluationMapper eventEvaluationMapper;

    @Resource
    private ConsContractInfoMapper consContractInfoMapper;

    @Resource
    private ProjectMapper projectMapper;

    @Resource
    private ConsMapper consMapper;

    @Resource
    private EventService eventService;

    @Resource
    private ConsContractInfoService consContractInfoService;

    @Resource
    private EventPowerBaseService eventPowerBaseService;

    @Resource
    private EventPowerService eventPowerService;

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
     * 资源指标----负荷聚合商数量（户)  负荷聚合商聚合客户数（户） 负荷聚合商聚合负荷规模（万kW）上报
     */
    @XxlJob("threeLoadAggregator")
    public ReturnT<String> threeLoadAggregator(String param) throws Exception {
        XxlJobLogger.log("资源指标----负荷聚合商数量（户) 、 负荷聚合商聚合客户数（户）、 负荷聚合商聚合负荷规模（万kW）上报");
        this.saveThreeLoadAggregator(param);
        return ReturnT.SUCCESS;
    }

    /**
     * 统计指标（当日与年度）----当日需求响应合计影响电量（万kWh）0、本年度需求响应累计执行天数（天）事件根据执行时间年份 并且事件执行结束 通过执行时间去重或者分组、本年度需求响应累计执行户次（户次）
     */
    @XxlJob("currentDayOrYearOfStatisticalIndicators")
    public ReturnT<String> currentDayOrYearOfStatisticalIndicators(String param) throws Exception {
        XxlJobLogger.log("统计指标（当日与年度）----当日需求响应合计影响电量（万kWh）、本年度需求响应累计执行天数（天）、本年度需求响应累计执行户次（户次）");
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
        log.info("统计指标（当日与年度）----当日需求响应合计影响电量（万kWh）、本年度需求响应累计执行天数（天）、本年度需求响应累计执行户次（户次）currentDate：" + properties.getProperty("currentDate"));
        this.saveCurrentDayOrYearOfStatisticalIndicators(properties.getProperty("currentDate"));
        return ReturnT.SUCCESS;
    }

    /**
     * 日前需求响应可调负荷规模（万kW）、日前需求响应户数（户）
     * 日内需求响应可调负荷规模（万kW）、日内需求响应户数（户）
     * 实时需求响应可调负荷规模（万kW）、实时需求响应户数（户）
     */
    @XxlJob("dayAheadDayInRealTime")
    public ReturnT<String> dayAheadDayInRealTime(String param) throws Exception {
        XxlJobLogger.log("日前需求响应可调负荷规模（万kW）、日前需求响应户数（户）、日内需求响应可调负荷规模（万kW）、日内需求响应户数（户）、 实时需求响应可调负荷规模（万kW）、实时需求响应户数（户）");
        this.saveDayAheadDayInRealTime(param);
        return ReturnT.SUCCESS;
    }

    /**
     * 需求响应不同分类用户客户数（户）	10106001      1工业用户，2楼宇用户，3分布式电源、4储能用户5自备电厂用户、6电动汽车充电桩（站）、7其它。（有户号的户数，不是指负荷聚合商户数，需将负荷聚合商聚合的客户分解至各类别中）
     * 需求响应不同分类用户签约容量（万kW）	10106002  1工业用户，2楼宇用户，3分布式电源、4储能用户5自备电厂用户、6电动汽车充电桩（站）、7其它
     */
    @XxlJob("numberOfUsersContractedCapacity")
    public ReturnT<String> numberOfUsersContractedCapacity(String param) throws Exception {
        XxlJobLogger.log("需求响应不同分类用户客户数（户）、需求响应不同分类用户签约容量（万kW）");
        this.saveNumberOfUsersContractedCapacity(param);
        return ReturnT.SUCCESS;
    }

    /**
     * 统计指标-------->本年度需求响应（削峰）执行最大负荷（万kW）
     *
     * @param param
     * @return
     * @throws Exception
     */
    @XxlJob("executeMaximumLoad")
    public ReturnT<String> executeMaximumLoad(String param) throws Exception {
        XxlJobLogger.log("本年度需求响应（削峰）执行最大负荷（万kW）");
        this.saveExecuteMaximumLoad(param);
        return ReturnT.SUCCESS;
    }

    /**
     * 统计指标-------->本年度需求响应累计响应电量（万kWh）
     *
     * @param param
     * @return
     * @throws Exception
     */
    @XxlJob("cumulative")
    public ReturnT<String> cumulative(String param) throws Exception {
        XxlJobLogger.log("本年度需求响应累计响应电量（万kWh）");
        Properties properties = new Properties();
        properties.load(new StringReader(param));
        log.info("本年度需求响应累计响应电量（万kWh）currentDate：" + properties.getProperty("currentDate"));
        this.saveCumulative(properties.getProperty("currentDate"));
        return ReturnT.SUCCESS;
    }

    /**
     * 统计指标-------->本年度需求响应累计响应电量（万kWh）
     *
     * @param currentDate 当前日期 必须与currentDayOrYearOfStatisticalIndicators定时任务一致
     */
    private void saveCumulative(String currentDate) {
        log.info("统计指标（当日与年度）1种开始执行--本年度需求响应累计响应电量（万kWh）");
        List<Map<String, Object>> records = new ArrayList<>();
        // 省级编码
        String provinceCode = null;
        // 获取市级组织机构
        List<SysOrgs> orgsList = null;
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

            // 省本年度需求响应累计响应电量（万kWh）10501004
            // 使用%%对%进行转义
            String sql1 = String.format("select IFNULL(sum(index_value),0) from exch_index_data where index_no='10302004' and index_org_no='%s' and create_time like concat('%d','%%')", provinceCode, LocalDate.now().getYear());
            BigDecimal indexValue = jdbcTemplate.query(sql1, rs -> {
                if (rs.next()) {
                    return rs.getObject(1, BigDecimal.class);
                }
                return null;
            });
            BigDecimal accuracy2 = accuracy(indexValue, 1);
            log.info("省本年度需求响应累计响应电量（万kWh） 10501004 数据：" + accuracy2);
            Map<String, Object> map1 = null;
            map1 = setParam(provinceCode, "00000", "10501004", "本年度需求响应累计响应电量（万kWh）");
            map1.put("INDEX_VALUE", accuracy2);
            if (ObjectUtil.isNotEmpty(currentDate)) {
                map1.put("DATA_DATE", DateUtil.parseLocalDateTime(currentDate, "yyyy-MM-dd").plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            }
            records.add(map1);

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
                // 获取市组织机构
                orgsList = orgsListDate.stream().filter(n -> "2".equals(n.getOrgTitle())).collect(Collectors.toList());
            } else {
                log.warn("组织机构不存在");
                return;
            }
            List<String> id = new ArrayList<>();
            if (ObjectUtil.isNotEmpty(orgsList) && orgsList.size() > 0) {
                orgsList.forEach(n -> id.add(n.getId()));
            }
            for (String s : id) {
                ResponseData<List<String>> allNextOrgId = systemClient.getAllNextOrgId(s);
                List<String> orgNoList = allNextOrgId.getData();

                // 市本年度需求响应累计响应电量（万kWh）10501004
                // 使用%%对%进行转义
                String sql2 = String.format("select IFNULL(sum(index_value),0) from exch_index_data where index_no='10302004' and index_org_no='%s' and create_time like concat('%d','%%')", s, LocalDate.now().getYear());
                BigDecimal indexValue1 = jdbcTemplate.query(sql2, rs -> {
                    if (rs.next()) {
                        return rs.getObject(1, BigDecimal.class);
                    }
                    return null;
                });
                BigDecimal accuracy3 = accuracy(indexValue1, 1);
                log.info("本年度需求响应累计响应电量（万kWh） 10501004 数据：" + accuracy3);
                Map<String, Object> map7 = null;
                map7 = setParam(s, provinceCode, "10501004", "本年度需求响应累计响应电量（万kWh）");
                map7.put("INDEX_VALUE", accuracy3);
                if (ObjectUtil.isNotEmpty(currentDate)) {
                    map7.put("DATA_DATE", DateUtil.parseLocalDateTime(currentDate, "yyyy-MM-dd").plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                }
                records.add(map7);
            }
            log.info("统计指标（当日与年度）1种--本年度需求响应累计响应电量（万kWh）：" + records.size() + "条。");
            log.info("统计指标（当日与年度）1种--本年度需求响应累计响应电量（万kWh）：" + records.toString());
            if (records.size() > 0) {
                records.forEach(item -> {
                    log.info("统计指标（当日与年度）3种：" + item.get("INDEX_ORG_NO") + "\t" + item.get("INDEX_NO") + "\t" + item.get("DATA_DATE") + "\t" + "当前时间：" + item.get("UPDATE_TIME"));
                    String sql = String.format("select index_id from %s where index_org_no='%s' and index_no='%s' and data_date='%s'", "exch_index_data", item.get("INDEX_ORG_NO"), item.get("INDEX_NO"), item.get("DATA_DATE"));
                    String idValue = jdbcTemplate.query(sql, rs -> {
                        if (rs.next()) {
                            return rs.getObject(1, String.class);
                        }
                        return null;
                    });
                    log.info("数据库当天是否插入过数据：" + idValue);
                    String execSql = null;
                    if (idValue == null) {
                        execSql = generalInsertSql(item);
                    } else {
                        item.remove("CREATE_TIME");
                        item.put("index_id", idValue);
                        execSql = generalUpdateSql(item, "index_id");
                    }
                    log.info("execSql=> {}", execSql);
                    jdbcTemplate.execute(execSql);
                });
            }
            log.info("统计指标（当日与年度）1种执行结束--本年度需求响应累计响应电量（万kWh）");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 本年度需求响应（削峰）执行最大负荷（万kW）
     *
     * @param param
     */
    private void saveExecuteMaximumLoad(String param) {
        log.info("统计指标（当日与年度）1种开始执行--本年度需求响应（削峰）执行最大负荷（万kW）");
        List<Map<String, Object>> records = new ArrayList<>();
        // 省级编码
        String provinceCode = null;
        // 获取市级组织机构
        List<SysOrgs> orgsList = null;
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

            // 省本年度需求响应（削峰）执行最大负荷（万kW） 10501003
            if (ObjectUtil.isNotEmpty(param)) {
                log.info("造省本年度需求响应（削峰）执行最大负荷" + accuracy(new BigDecimal(param), 1) + "万kw");
                Map<String, Object> map4 = null;
                map4 = setParam(provinceCode, "00000", "10501003", "本年度需求响应（削峰）执行最大负荷（万kW）");
                String[] params = param.split("&");
                map4.put("INDEX_VALUE", accuracy(new BigDecimal(params[0]), 1));
                map4.put("DATA_DATE", params[1]);
                records.add(map4);
            } else {
                List<String> provincialCode = new ArrayList<>();
                provincialCode.add(provinceCode);
                ExecuteMaximumLoad executeMaximumLoad = eventEvaluationMapper.executeMaximumLoad(provincialCode);
                BigDecimal accuracy = accuracy(executeMaximumLoad.getLoads(), 1);
                log.info("省本年度需求响应（削峰）执行最大负荷" + accuracy + "万kw");
                if (BigDecimal.ZERO.compareTo(accuracy) > 0) {
                    Map<String, Object> map4 = null;
                    map4 = setParam(provinceCode, "00000", "10501003", "本年度需求响应（削峰）执行最大负荷（万kW）");
                    map4.put("INDEX_VALUE", new BigDecimal("0.00"));
                    // map4.put("DATA_DATE", executeMaximumLoad.getTimes().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    records.add(map4);
                    log.info("无省本年度需求响应（削峰）执行最大负荷（万kW）");
                } else if (BigDecimal.ZERO.compareTo(accuracy) == 0) {
                    Map<String, Object> map4 = null;
                    map4 = setParam(provinceCode, "00000", "10501003", "本年度需求响应（削峰）执行最大负荷（万kW）");
                    map4.put("INDEX_VALUE", new BigDecimal("0.00"));
                    // map4.put("DATA_DATE", executeMaximumLoad.getTimes().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    records.add(map4);
                } else {
                    Map<String, Object> map4 = null;
                    map4 = setParam(provinceCode, "00000", "10501003", "本年度需求响应（削峰）执行最大负荷（万kW）");
                    map4.put("INDEX_VALUE", accuracy);
                    // map4.put("DATA_DATE", executeMaximumLoad.getTimes().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    records.add(map4);
                }
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
                // 获取市组织机构
                orgsList = orgsListDate.stream().filter(n -> "2".equals(n.getOrgTitle())).collect(Collectors.toList());
            } else {
                log.warn("组织机构不存在");
                return;
            }
            List<String> id = new ArrayList<>();
            if (ObjectUtil.isNotEmpty(orgsList) && orgsList.size() > 0) {
                orgsList.forEach(n -> id.add(n.getId()));
            }
            for (String s : id) {
                ResponseData<List<String>> allNextOrgId = systemClient.getAllNextOrgId(s);
                List<String> orgNoList = allNextOrgId.getData();

                // 市本年度需求响应（削峰）执行最大负荷（万kW） 10501003
                ExecuteMaximumLoad executeMaximumLoad1 = eventEvaluationMapper.executeMaximumLoad(orgNoList);
                BigDecimal accuracy1 = accuracy(executeMaximumLoad1.getLoads(), 1);
                log.info("市本年度需求响应（削峰）执行最大负荷" + accuracy1 + "万kw");
                if (BigDecimal.ZERO.compareTo(accuracy1) > 0) {
                    Map<String, Object> map9 = null;
                    map9 = setParam(s, provinceCode, "10501003", "本年度需求响应（削峰）执行最大负荷（万kW）");
                    map9.put("INDEX_VALUE", new BigDecimal("0.00"));
                    // map9.put("DATA_DATE", executeMaximumLoad1.getTimes().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    records.add(map9);
                    log.info("无市本年度需求响应（削峰）执行最大负荷（万kW）");
                } else if (BigDecimal.ZERO.compareTo(accuracy1) == 0) {
                    Map<String, Object> map9 = null;
                    map9 = setParam(s, provinceCode, "10501003", "本年度需求响应（削峰）执行最大负荷（万kW）");
                    map9.put("INDEX_VALUE", new BigDecimal("0.00"));
                    // map9.put("DATA_DATE", executeMaximumLoad1.getTimes().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    records.add(map9);
                } else {
                    Map<String, Object> map9 = null;
                    map9 = setParam(s, provinceCode, "10501003", "本年度需求响应（削峰）执行最大负荷（万kW）");
                    map9.put("INDEX_VALUE", accuracy1);
                    // map9.put("DATA_DATE", executeMaximumLoad1.getTimes().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    records.add(map9);
                }
            }
            log.info("统计指标（当日与年度）1种--本年度需求响应（削峰）执行最大负荷（万kW）共：" + records.size() + "条。");
            log.info("统计指标（当日与年度）1种--本年度需求响应（削峰）执行最大负荷（万kW）：" + records.toString());
            if (records.size() > 0) {
                records.forEach(item -> {
                    // log.info("统计指标（当日与年度）1种：" + item.get("INDEX_ORG_NO") + "\t" + item.get("INDEX_NO") + "\t" + item.get("INDEX_ID") + "\t" + "当前时间：" + item.get("UPDATE_TIME"));
                    // String sql = String.format("select index_id from %s where index_org_no='%s' and index_no='%s' and index_id='%s'", "exch_index_data", item.get("INDEX_ORG_NO"), item.get("INDEX_NO"), item.get("INDEX_ID"));
                    log.info("统计指标（当日与年度）1种：" + item.get("INDEX_ORG_NO") + "\t" + item.get("INDEX_NO") + "\t" + item.get("DATA_DATE") + "\t" + "当前时间：" + item.get("UPDATE_TIME"));
                    String sql = String.format("select index_id from %s where index_org_no='%s' and index_no='%s' and data_date='%s'", "exch_index_data", item.get("INDEX_ORG_NO"), item.get("INDEX_NO"), item.get("DATA_DATE"));
                    String idValue = jdbcTemplate.query(sql, rs -> {
                        if (rs.next()) {
                            return rs.getObject(1, String.class);
                        }
                        return null;
                    });
                    log.info("数据库当天是否插入过数据：" + idValue);
                    String execSql = null;
                    if (idValue == null) {
                        execSql = generalInsertSql(item);
                    } else {
                        item.remove("CREATE_TIME");
                        item.put("index_id", idValue);
                        execSql = generalUpdateSql(item, "index_id");
                    }
                    log.info("execSql=> {}", execSql);
                    jdbcTemplate.execute(execSql);
                });
            }
            log.info("统计指标（当日与年度）1种执行结束--本年度需求响应（削峰）执行最大负荷（万kW）");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 需求响应不同分类用户客户数（户）	    10106001      1工业用户、2楼宇用户、3分布式电源、4储能用户、5自备电厂用户、6电动汽车充电桩（站）、7其它。（有户号的户数，不是指负荷聚合商户数，需将负荷聚合商聚合的客户分解至各类别中）
     * 需求响应不同分类用户签约容量（万kW）	10106002      1工业用户、2楼宇用户、3分布式电源、4储能用户、5自备电厂用户、6电动汽车充电桩（站）、7其它。
     *
     * @param param
     */
    private void saveNumberOfUsersContractedCapacity(String param) {
        log.info("不同分类用户数与签约容量2类指标开始执行！！！！");
        JSONObject data = new JSONObject();
        JSONObject data1 = new JSONObject();
        List<Map<String, Object>> records = new ArrayList<>();
        // 省级编码
        String provinceCode = null;
        // 获取市级组织机构
        List<SysOrgs> orgsList = null;
        JSONArray datas = null;
        List<SysOrgs> orgsListDate = new ArrayList<>();

        // index_value格式使用
        List<String> valueList = new ArrayList<>();
        List<String> valueList1 = new ArrayList<>();
        Map<String, Object> mapLayout = null;
        Map<String, Object> mapLayout1 = null;
        List<Map<String, Object>> containerList = new ArrayList<>();
        List<Map<String, Object>> containerList1 = new ArrayList<>();
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
            // 获取最近项目id
            String projectId = projectMapper.recentProjects();
            log.info("最近项目id：" + projectId);

            // 需求响应不同分类用户客户数（户）	10106001	省级/地市级
            // 100--->工业用户、200--->楼宇用户、这两个除外剩余归入其他   1---->查询单个行业用户数、  其他数字---->除传入的两个行业类型剩下的用户数 若customerType1和customerType2都传入值，请为methodType传2
            // 省工业用户数量
            Integer industry = customerTypeCount("100", null, 1, null);
            String industryStr = String.valueOf(industry);
            log.info("省工业用户数量：" + industryStr);
            // 省楼宇用户数量
            Integer building = customerTypeCount("200", null, 1, null);
            String buildingStr = String.valueOf(building);
            log.info("省楼宇用户：" + buildingStr);
            // 省其他用户数量
            Integer other = customerTypeCount("100", "200", 2, null);
            String otherStr = String.valueOf(other);
            log.info("省除工业、楼宇外剩余归入其他用户数量：" + otherStr);
            // 1工业用户、2楼宇用户、3分布式电源、4储能用户、5自备电厂用户、6电动汽车充电桩（站）、7其它
            valueList.add(industryStr);
            valueList.add(buildingStr);
            valueList.add("0");
            valueList.add("0");
            valueList.add("0");
            valueList.add("0");
            valueList.add(otherStr);
            for (int i = 0; i < valueList.size(); i++) {
                mapLayout = setFormat(String.valueOf(i + 1), "customers", valueList.get(i));
                containerList.add(mapLayout);
            }
            data.put("org_no", provinceCode);
            data.put("cons_dist", containerList);
            Map<String, Object> map = null;
            map = setParam(provinceCode, "00000", "10106001", "需求响应不同分类用户客户数（户）");
            map.put("INDEX_VALUE", data);
            records.add(map);

            // 需求响应不同分类用户签约容量（万kW）	10106002	省级/地市级
            // 工业用户签约容量（万kW）
            BigDecimal bigDecimal = userContractedCapacity("100", null, 1, null, projectId);
            BigDecimal accuracy = accuracy(bigDecimal, 1);
            String s4 = String.valueOf(accuracy);
            log.info("省工业用户签约容量：" + s4 + "万kw");
            BigDecimal bigDecimal1 = userContractedCapacity("200", null, 1, null, projectId);
            BigDecimal accuracy1 = accuracy(bigDecimal1, 1);
            String s5 = String.valueOf(accuracy1);
            log.info("省楼宇用户签约容量：" + s5 + "万kw");
            BigDecimal bigDecimal2 = userContractedCapacity("100", "200", 2, null, projectId);
            BigDecimal accuracy2 = accuracy(bigDecimal2, 1);
            String s6 = String.valueOf(accuracy2);
            log.info("省其他用户签约容量：" + accuracy2 + "万kw");
            valueList1.add(s4);
            valueList1.add(s5);
            valueList1.add("0");
            valueList1.add("0");
            valueList1.add("0");
            valueList1.add("0");
            valueList1.add(s6);
            for (int i = 0; i < valueList1.size(); i++) {
                mapLayout1 = setFormat(String.valueOf(i + 1), "load", valueList1.get(i));
                containerList1.add(mapLayout1);
            }
            data1.put("org_no", provinceCode);
            data1.put("cons_dist", containerList1);
            Map<String, Object> map1 = null;
            map1 = setParam(provinceCode, "00000", "10106002", "需求响应不同分类用户签约容量（万kW）");
            map1.put("INDEX_VALUE", data1);
            records.add(map1);

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
                // 获取市组织机构
                orgsList = orgsListDate.stream().filter(n -> "2".equals(n.getOrgTitle())).collect(Collectors.toList());
            } else {
                log.warn("组织机构不存在");
                return;
            }
            List<String> id = new ArrayList<>();
            if (ObjectUtil.isNotEmpty(orgsList) && orgsList.size() > 0) {
                orgsList.forEach(n -> id.add(n.getId()));
            }

            for (String s : id) {
                // index_value格式使用
                List<String> valueList2 = new ArrayList<>();
                List<String> valueList3 = new ArrayList<>();
                Map<String, Object> mapLayout2 = null;
                Map<String, Object> mapLayout3 = null;
                List<Map<String, Object>> containerList2 = new ArrayList<>();
                List<Map<String, Object>> containerList3 = new ArrayList<>();
                JSONObject data2 = new JSONObject();
                JSONObject data3 = new JSONObject();
                ResponseData<List<String>> allNextOrgId = systemClient.getAllNextOrgId(s);
                List<String> orgNoList = allNextOrgId.getData();
                log.info("orgNoList: " + orgNoList);

                // 需求响应不同分类用户客户数（户）	10106001	省级/地市级
                // 100--->工业用户、200--->楼宇用户、这两个除外剩余归入其他   1---->查询单个行业用户数、  其他数字---->除传入的两个行业类型剩下的用户数 若customerType1和customerType2都传入值，请为methodType传2
                // 市工业用户数量
                Integer industry1 = customerTypeCount("100", null, 1, orgNoList);
                String s1 = String.valueOf(industry1);
                log.info("市工业用户数量：" + s1);
                // 市楼宇用户数量
                Integer building1 = customerTypeCount("200", null, 1, orgNoList);
                String s2 = String.valueOf(building1);
                log.info("市楼宇用户：" + s2);
                // 省其他用户数量
                Integer other1 = customerTypeCount("100", "200", 2, orgNoList);
                String s3 = String.valueOf(other1);
                log.info("市除工业、楼宇外剩余归入其他用户数量：" + s3);
                // 1工业用户、2楼宇用户、3分布式电源、4储能用户、5自备电厂用户、6电动汽车充电桩（站）、7其它
                valueList2.add(s1);
                valueList2.add(s2);
                valueList2.add("0");
                valueList2.add("0");
                valueList2.add("0");
                valueList2.add("0");
                valueList2.add(s3);
                for (int i = 0; i < valueList2.size(); i++) {
                    mapLayout2 = setFormat(String.valueOf(i + 1), "customers", valueList2.get(i));
                    containerList2.add(mapLayout2);
                }
                data2.put("org_no", s);
                data2.put("cons_dist", containerList2);
                Map<String, Object> map2 = null;
                map2 = setParam(s, provinceCode, "10106001", "需求响应不同分类用户客户数（户）");
                map2.put("INDEX_VALUE", data2);
                records.add(map2);

                // 需求响应不同分类用户签约容量（万kW）	10106002	省级/地市级
                // 工业用户签约容量（万kW）
                BigDecimal bigDecimal3 = userContractedCapacity("100", null, 1, orgNoList, projectId);
                BigDecimal accuracy3 = accuracy(bigDecimal3, 1);
                String s7 = String.valueOf(accuracy3);
                log.info("市工业用户签约容量：" + s7 + "万kw");
                BigDecimal bigDecimal4 = userContractedCapacity("200", null, 1, orgNoList, projectId);
                BigDecimal accuracy4 = accuracy(bigDecimal4, 1);
                String s8 = String.valueOf(accuracy4);
                log.info("市楼宇用户签约容量：" + s8 + "万kw");
                BigDecimal bigDecimal5 = userContractedCapacity("100", "200", 2, orgNoList, projectId);
                BigDecimal accuracy5 = accuracy(bigDecimal5, 1);
                String s9 = String.valueOf(accuracy5);
                log.info("市其他用户签约容量：" + accuracy5 + "万kw");
                valueList3.add(s7);
                valueList3.add(s8);
                valueList3.add("0");
                valueList3.add("0");
                valueList3.add("0");
                valueList3.add("0");
                valueList3.add(s9);
                for (int i = 0; i < valueList3.size(); i++) {
                    mapLayout3 = setFormat(String.valueOf(i + 1), "load", valueList3.get(i));
                    containerList3.add(mapLayout3);
                }
                data3.put("org_no", s);
                data3.put("cons_dist", containerList3);
                Map<String, Object> map3 = null;
                map3 = setParam(s, provinceCode, "10106002", "需求响应不同分类用户签约容量（万kW）");
                map3.put("INDEX_VALUE", data3);
                records.add(map3);
            }
            log.info("不同分类用户数与签约容量2类指标共：" + records.size() + "条。");
            log.info("不同分类用户数与签约容量2类指标：" + records);
            if (records.size() > 0) {
                records.forEach(item -> {
                    log.info("不同分类用户数与签约容量2类指标：" + item.get("INDEX_ORG_NO") + "\t" + item.get("INDEX_NO") + "\t" + item.get("DATA_DATE") + "\t" + "当前时间：" + item.get("UPDATE_TIME"));
                    String sql = String.format("select index_id from %s where index_org_no='%s' and index_no='%s' and data_date='%s'", "exch_index_data", item.get("INDEX_ORG_NO"), item.get("INDEX_NO"), item.get("DATA_DATE"));
                    String idValue = jdbcTemplate.query(sql, rs -> {
                        if (rs.next()) {
                            return rs.getObject(1, String.class);
                        }
                        return null;
                    });
                    log.info("数据库当天是否插入过数据：" + idValue);
                    String execSql = null;
                    if (idValue == null) {
                        execSql = generalInsertSql(item);
                    } else {
                        item.remove("CREATE_TIME");
                        item.put("index_id", idValue);
                        execSql = generalUpdateSql(item, "index_id");
                    }
                    log.info("execSql=> {}", execSql);
                    jdbcTemplate.execute(execSql);
                });
            }
            log.info("不同分类用户数与签约容量2类指标执行完成！！！！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 日前需求响应可调负荷规模（万kW）、日前需求响应户数（户）
     * 日内需求响应可调负荷规模（万kW）、日内需求响应户数（户）
     * 实时需求响应可调负荷规模（万kW）、实时需求响应户数（户）
     *
     * @param param
     */
    private void saveDayAheadDayInRealTime(String param) {
        log.info("日前日内实时6类开始执行！！！");
        List<Map<String, Object>> records = new ArrayList<>();
        // 省级编码
        String provinceCode = null;
        // 获取市级组织机构
        List<SysOrgs> orgsList = null;
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
            // 获取最近项目id
            String projectId = projectMapper.recentProjects();
            log.info("最近项目id：" + projectId);
            // 省级
            List<WorkProjectInfoDTO> workPageCapDetail = consContractInfoMapper.getWorkPageCapDetail1(projectId, null);
            // 省日前需求响应可调负荷规模（万kW）	10304002 正确方法
/*            BigDecimal aFewDaysAgo = getContractCap(workPageCapDetail, 1, 1, 1);
            BigDecimal accuracy1 = accuracy(aFewDaysAgo, 1);*/
            // TODO 错误方法满足国网数据要求
            BigDecimal accuracy1 = accuracy(consContractInfoService.getApprovalConstractCapSum(null), 1);
            log.info("省日前需求响应可调负荷规模：" + accuracy1 + "万kw");
            if (accuracy1.compareTo(BigDecimal.ZERO) >= 0) {
                Map<String, Object> map = null;
                map = setParam(provinceCode, "00000", "10304002", "日前需求响应可调负荷规模（万kW）");
                map.put("INDEX_VALUE", accuracy1);
                records.add(map);
            } else {
                Map<String, Object> map = null;
                map = setParam(provinceCode, "00000", "10304002", "日前需求响应可调负荷规模（万kW）");
                map.put("INDEX_VALUE", new BigDecimal("0.00"));
                records.add(map);
                log.info("无省日前需求响应可调负荷规模（万kW）");
            }
            // 省日前需求响应户数（户）	10304001   正确方法
            // Integer households1 = getHouseholds(projectId, "1", "1", "1", null);
            // TODO 错误方法满足国网数据要求
            Integer households1 = consContractInfoService.getApprovalConsCount(null);
            ;
            log.info("省日前需求响应户数：" + households1 + "户");
            if (ObjectUtil.isNotEmpty(households1)) {
                Map<String, Object> map = null;
                map = setParam(provinceCode, "00000", "10304001", "日前需求响应户数（户）");
                map.put("INDEX_VALUE", households1);
                records.add(map);
            } else {
                Map<String, Object> map = null;
                map = setParam(provinceCode, "00000", "10304001", "日前需求响应户数（户）");
                map.put("INDEX_VALUE", 0);
                log.info("无省日前需求响应户数（户）");
                records.add(map);
            }

            // 省日内需求响应可调负荷规模（万kW）	10305002
            BigDecimal intraday = getContractCap(workPageCapDetail, 1, 1, 2);
            BigDecimal accuracy2 = accuracy(intraday, 1);
            log.info("省日内需求响应可调负荷规模：" + accuracy2 + "万kw");
            if (accuracy2.compareTo(BigDecimal.ZERO) >= 0) {
                Map<String, Object> map = null;
                map = setParam(provinceCode, "00000", "10305002", "日内需求响应可调负荷规模（万kW）");
                map.put("INDEX_VALUE", accuracy2);
                records.add(map);
            } else {
                Map<String, Object> map = null;
                map = setParam(provinceCode, "00000", "10305002", "日内需求响应可调负荷规模（万kW）");
                map.put("INDEX_VALUE", new BigDecimal("0.00"));
                records.add(map);
                log.info("无省日内需求响应可调负荷规模（万kw）");
            }
            // 省日内需求响应户数（户）	10305001
            Integer households2 = getHouseholds(projectId, "1", "1", "2", null);
            log.info("省日内需求响应户数：" + households2 + "户");
            if (ObjectUtil.isNotEmpty(households2)) {
                Map<String, Object> map = null;
                map = setParam(provinceCode, "00000", "10305001", "日内需求响应户数（户）");
                map.put("INDEX_VALUE", households2);
                records.add(map);
            } else {
                Map<String, Object> map = null;
                map = setParam(provinceCode, "00000", "10305001", "日内需求响应户数（户）");
                map.put("INDEX_VALUE", 0);
                records.add(map);
                log.info("无省日内需求响应户数（户）");
            }

            // 省实时需求响应可调负荷规模（万kW）	10306002
            BigDecimal realTime = getContractCap(workPageCapDetail, 1, 1, 3);
            BigDecimal accuracy3 = accuracy(realTime, 1);
            log.info("省实时需求响应可调负荷规模：" + accuracy3 + "万kw");
            if (accuracy3.compareTo(BigDecimal.ZERO) >= 0) {
                Map<String, Object> map = null;
                map = setParam(provinceCode, "00000", "10306002", "实时需求响应可调负荷规模（万kW）");
                map.put("INDEX_VALUE", accuracy3);
                records.add(map);
            } else {
                Map<String, Object> map = null;
                map = setParam(provinceCode, "00000", "10306002", "实时需求响应可调负荷规模（万kW）");
                map.put("INDEX_VALUE", new BigDecimal("0.00"));
                records.add(map);
                log.info("无省实时需求响应可调负荷规模（万kW）");
            }
            // 省实时需求响应户数（户）	10306001
            Integer households3 = getHouseholds(projectId, "1", "1", "3", null);
            log.info("省实时需求响应户数：" + households3 + "户");
            if (ObjectUtil.isNotEmpty(households3)) {
                Map<String, Object> map = null;
                map = setParam(provinceCode, "00000", "10306001", "实时需求响应户数（户）");
                map.put("INDEX_VALUE", households3);
                records.add(map);
            } else {
                Map<String, Object> map = null;
                map = setParam(provinceCode, "00000", "10306001", "实时需求响应户数（户）");
                map.put("INDEX_VALUE", 0);
                records.add(map);
                log.info("无省实时需求响应户数（户）");
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
                // 获取市组织机构
                orgsList = orgsListDate.stream().filter(n -> "2".equals(n.getOrgTitle())).collect(Collectors.toList());
            } else {
                log.warn("组织机构不存在");
                return;
            }
            List<String> id = new ArrayList<>();
            if (ObjectUtil.isNotEmpty(orgsList) && orgsList.size() > 0) {
                orgsList.forEach(n -> id.add(n.getId()));
            }

            for (String s : id) {
                ResponseData<List<String>> allNextOrgId = systemClient.getAllNextOrgId(s);
                List<String> orgNoList = allNextOrgId.getData();
                log.info("orgNoList: " + orgNoList);

                // 市级
                List<WorkProjectInfoDTO> workPageCapDetail1 = consContractInfoMapper.getWorkPageCapDetail1(projectId, orgNoList);
                // 市日前需求响应可调负荷规模（万kW）	10304002
                BigDecimal aFewDaysAgo1 = getContractCap(workPageCapDetail1, 1, 1, 1);
                BigDecimal accuracy4 = accuracy(aFewDaysAgo1, 1);
                log.info("市日前需求响应可调负荷规模：" + accuracy4 + "万kw");
                if (accuracy4.compareTo(BigDecimal.ZERO) >= 0) {
                    Map<String, Object> map = null;
                    map = setParam(s, provinceCode, "10304002", "日前需求响应可调负荷规模（万kW）");
                    map.put("INDEX_VALUE", accuracy4);
                    records.add(map);
                } else {
                    Map<String, Object> map = null;
                    map = setParam(s, provinceCode, "10304002", "日前需求响应可调负荷规模（万kW）");
                    map.put("INDEX_VALUE", new BigDecimal("0.00"));
                    records.add(map);
                    log.info("无市日前需求响应可调负荷规模（万kW）");
                }
                // 市日前需求响应户数（户）	10304001
                Integer households4 = getHouseholds(projectId, "1", "1", "1", orgNoList);
                log.info("市日前需求响应户数：" + households4 + "户");
                if (ObjectUtil.isNotEmpty(households4)) {
                    Map<String, Object> map = null;
                    map = setParam(s, provinceCode, "10304001", "日前需求响应户数（户）");
                    map.put("INDEX_VALUE", households4);
                    records.add(map);
                } else {
                    Map<String, Object> map = null;
                    map = setParam(s, provinceCode, "10304001", "日前需求响应户数（户）");
                    map.put("INDEX_VALUE", 0);
                    records.add(map);
                    log.info("无市日前需求响应户数（户）");
                }

                // 市日内需求响应可调负荷规模（万kW）	10305002
                BigDecimal intraday1 = getContractCap(workPageCapDetail1, 1, 1, 2);
                BigDecimal accuracy5 = accuracy(intraday1, 1);
                log.info("市日内需求响应可调负荷规模：" + accuracy5 + "万kw");
                if (accuracy5.compareTo(BigDecimal.ZERO) >= 0) {
                    Map<String, Object> map = null;
                    map = setParam(s, provinceCode, "10305002", "日内需求响应可调负荷规模（万kW）");
                    map.put("INDEX_VALUE", accuracy5);
                    records.add(map);
                } else {
                    Map<String, Object> map = null;
                    map = setParam(s, provinceCode, "10305002", "日内需求响应可调负荷规模（万kW）");
                    map.put("INDEX_VALUE", new BigDecimal("0.00"));
                    records.add(map);
                    log.info("无市日内需求响应可调负荷规模（万kw）");
                }
                // 市日内需求响应户数（户）	10305001
                Integer households5 = getHouseholds(projectId, "1", "1", "2", orgNoList);
                log.info("市日内需求响应户数：" + households5 + "户");
                if (ObjectUtil.isNotEmpty(households5)) {
                    Map<String, Object> map = null;
                    map = setParam(s, provinceCode, "10305001", "日内需求响应户数（户）");
                    map.put("INDEX_VALUE", households5);
                    records.add(map);
                } else {
                    Map<String, Object> map = null;
                    map = setParam(s, provinceCode, "10305001", "日内需求响应户数（户）");
                    map.put("INDEX_VALUE", 0);
                    records.add(map);
                    log.info("无市日内需求响应户数（户）");
                }

                // 市实时需求响应可调负荷规模（万kW）	10306002
                BigDecimal realTime1 = getContractCap(workPageCapDetail1, 1, 1, 3);
                BigDecimal accuracy6 = accuracy(realTime1, 1);
                log.info("市实时需求响应可调负荷规模：" + accuracy6 + "万kw");
                if (accuracy6.compareTo(BigDecimal.ZERO) >= 0) {
                    Map<String, Object> map = null;
                    map = setParam(s, provinceCode, "10306002", "实时需求响应可调负荷规模（万kW）");
                    map.put("INDEX_VALUE", accuracy6);
                    records.add(map);
                } else {
                    Map<String, Object> map = null;
                    map = setParam(s, provinceCode, "10306002", "实时需求响应可调负荷规模（万kW）");
                    map.put("INDEX_VALUE", new BigDecimal("0.00"));
                    records.add(map);
                    log.info("无市实时需求响应可调负荷规模（万kW）");
                }
                // 市实时需求响应户数（户）	10306001
                Integer households6 = getHouseholds(projectId, "1", "1", "3", orgNoList);
                log.info("市实时需求响应户数：" + households6 + "户");
                if (ObjectUtil.isNotEmpty(households6)) {
                    Map<String, Object> map = null;
                    map = setParam(s, provinceCode, "10306001", "实时需求响应户数（户）");
                    map.put("INDEX_VALUE", households6);
                    records.add(map);
                } else {
                    Map<String, Object> map = null;
                    map = setParam(s, provinceCode, "10306001", "实时需求响应户数（户）");
                    map.put("INDEX_VALUE", 0);
                    records.add(map);
                    log.info("无市实时需求响应户数（户）");
                }

            }
            log.info("日前日内实时6类共：" + records.size() + "条。");
            log.info("日前日内实时6类：" + records.toString());
            if (records.size() > 0) {
                records.forEach(item -> {
                    log.info("日前日内实时6类：" + item.get("INDEX_ORG_NO") + "\t" + item.get("INDEX_NO") + "\t" + item.get("DATA_DATE") + "\t" + "当前时间：" + item.get("UPDATE_TIME"));
                    String sql = String.format("select index_id from %s where index_org_no='%s' and index_no='%s' and data_date='%s'", "exch_index_data", item.get("INDEX_ORG_NO"), item.get("INDEX_NO"), item.get("DATA_DATE"));
                    String idValue = jdbcTemplate.query(sql, rs -> {
                        if (rs.next()) {
                            return rs.getObject(1, String.class);
                        }
                        return null;
                    });
                    log.info("数据库当天是否插入过数据：" + idValue);
                    String execSql = null;
                    if (idValue == null) {
                        execSql = generalInsertSql(item);
                    } else {
                        item.remove("CREATE_TIME");
                        item.put("index_id", idValue);
                        execSql = generalUpdateSql(item, "index_id");
                    }
                    log.info("execSql=> {}", execSql);
                    jdbcTemplate.execute(execSql);
                });
            }
            log.info("日前日内实时6类执行完成！！！！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 统计指标（当日与年度）----当日需求响应合计影响电量（万kWh）0、本年度需求响应累计执行天数（天）事件根据执行时间年份 并且事件执行结束 通过执行时间去重或者分组、本年度需求响应累计执行户次（户次）
     *
     * @param currentDate 当前日前（yyyy-MM-dd）
     */
    private void saveCurrentDayOrYearOfStatisticalIndicators(String currentDate) {
        log.info("统计指标（当日与年度）3种开始执行--当日需求响应合计影响电量（万kWh）0、本年度需求响应累计执行天数（天）、本年度需求响应累计执行户次（户次）");
        List<Map<String, Object>> records = new ArrayList<>();
        // 省级编码
        String provinceCode = null;
        // 获取市级组织机构
        List<SysOrgs> orgsList = null;
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
            // 省当日需求响应合计影响电量（万kWh）10302004
            LambdaQueryWrapper<Event> lambdaQueryWrapper = Wrappers.lambdaQuery();
            lambdaQueryWrapper.select(Event::getEventId, Event::getRegulateDate, Event::getStartTime, Event::getEndTime);
            if (ObjectUtil.isNotEmpty(currentDate)) {
                log.info("该定时任务平台参数(当前日期)currentDate：" + currentDate);
                lambdaQueryWrapper.eq(Event::getRegulateDate, currentDate);
            } else {
                lambdaQueryWrapper.eq(Event::getRegulateDate, timeFormat(1));
            }
            lambdaQueryWrapper.eq(Event::getEventStatus, "04");
            List<Event> eventList = eventService.list(lambdaQueryWrapper);
            log.info("省当日需求响应合计影响电量指标查询到的事件：" + eventList);
            BigDecimal accuracyValue = BigDecimal.ZERO;
            if (CollectionUtil.isNotEmpty(eventList) && eventList.size() > 0) {
                // 省当日需求响应合计影响电量（万kWh）
                BigDecimal affectThePower = BigDecimal.ZERO;
                List<String> eventTimes = new ArrayList<>();
                // 时间调度时间
                eventList.forEach(t -> eventTimes.add(t.getRegulateDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));

                for (Event event : eventList) {
                    String format = event.getRegulateDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    String startTime = event.getStartTime();
                    String endTime = event.getEndTime();
                    String start = format + " " + startTime + ":00";
                    String end = format + " " + endTime + ":00";
                    LocalDateTime startTime1 = LocalDateTime.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    LocalDateTime endTime1 = LocalDateTime.parse(end, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    log.info("事件ID为：" + event.getEventId() + ",执行开始时间：" + startTime1 + "执行结束时间：" + endTime1);
                    long minutes = Duration.between(startTime1, endTime1).toMinutes();
                    // 转为小时
                    Double lengthOfTime = (double) minutes / 60;
                    log.info("响应时长：" + lengthOfTime);
                    Long eventId = event.getEventId();
                    // 实时响应负荷
                    LambdaQueryWrapper<EventPower> lambdaQueryWrapper2 = Wrappers.lambdaQuery();
                    lambdaQueryWrapper2.select(EventPower::getP1, EventPower::getP2, EventPower::getP3, EventPower::getP4, EventPower::getP5, EventPower::getP6, EventPower::getP7, EventPower::getP8, EventPower::getP9, EventPower::getP10, EventPower::getP11, EventPower::getP12, EventPower::getP13, EventPower::getP14, EventPower::getP15, EventPower::getP16, EventPower::getP17, EventPower::getP18, EventPower::getP19, EventPower::getP20, EventPower::getP21, EventPower::getP22, EventPower::getP23, EventPower::getP24, EventPower::getP25, EventPower::getP26, EventPower::getP27, EventPower::getP28, EventPower::getP29, EventPower::getP30, EventPower::getP31, EventPower::getP32, EventPower::getP33, EventPower::getP34, EventPower::getP35, EventPower::getP36, EventPower::getP37, EventPower::getP38, EventPower::getP39, EventPower::getP40, EventPower::getP41, EventPower::getP42, EventPower::getP43, EventPower::getP44, EventPower::getP45, EventPower::getP46, EventPower::getP47, EventPower::getP48, EventPower::getP49, EventPower::getP50, EventPower::getP51, EventPower::getP52, EventPower::getP53, EventPower::getP54, EventPower::getP55, EventPower::getP56, EventPower::getP57, EventPower::getP58, EventPower::getP59, EventPower::getP60, EventPower::getP61, EventPower::getP62, EventPower::getP63, EventPower::getP64, EventPower::getP65, EventPower::getP66, EventPower::getP67, EventPower::getP68, EventPower::getP69, EventPower::getP70, EventPower::getP71, EventPower::getP72, EventPower::getP73, EventPower::getP74, EventPower::getP75, EventPower::getP76, EventPower::getP77, EventPower::getP78, EventPower::getP79, EventPower::getP80, EventPower::getP81, EventPower::getP82, EventPower::getP83, EventPower::getP84, EventPower::getP85, EventPower::getP86, EventPower::getP87, EventPower::getP88, EventPower::getP89, EventPower::getP90, EventPower::getP91, EventPower::getP92, EventPower::getP93, EventPower::getP94, EventPower::getP95, EventPower::getP96);

                    lambdaQueryWrapper2.eq(EventPower::getEventId, eventId);
                    lambdaQueryWrapper2.eq(EventPower::getOrgNo, provinceCode);
                    EventPower eventPowerOne = eventPowerService.getOne(lambdaQueryWrapper2);
                    log.info("实时负荷：" + eventPowerOne);
                    // 基线
                    LambdaQueryWrapper<EventPowerBase> lambdaQueryWrapper1 = Wrappers.lambdaQuery();
                    lambdaQueryWrapper1.select(EventPowerBase::getP1, EventPowerBase::getP2, EventPowerBase::getP3, EventPowerBase::getP4, EventPowerBase::getP5, EventPowerBase::getP6, EventPowerBase::getP7, EventPowerBase::getP8, EventPowerBase::getP9, EventPowerBase::getP10, EventPowerBase::getP11, EventPowerBase::getP12, EventPowerBase::getP13, EventPowerBase::getP14, EventPowerBase::getP15, EventPowerBase::getP16, EventPowerBase::getP17, EventPowerBase::getP18, EventPowerBase::getP19, EventPowerBase::getP20, EventPowerBase::getP21, EventPowerBase::getP22, EventPowerBase::getP23, EventPowerBase::getP24, EventPowerBase::getP25, EventPowerBase::getP26, EventPowerBase::getP27, EventPowerBase::getP28, EventPowerBase::getP29, EventPowerBase::getP30, EventPowerBase::getP31, EventPowerBase::getP32, EventPowerBase::getP33, EventPowerBase::getP34, EventPowerBase::getP35, EventPowerBase::getP36, EventPowerBase::getP37, EventPowerBase::getP38, EventPowerBase::getP39, EventPowerBase::getP40, EventPowerBase::getP41, EventPowerBase::getP42, EventPowerBase::getP43, EventPowerBase::getP44, EventPowerBase::getP45, EventPowerBase::getP46, EventPowerBase::getP47, EventPowerBase::getP48, EventPowerBase::getP49, EventPowerBase::getP50, EventPowerBase::getP51, EventPowerBase::getP52, EventPowerBase::getP53, EventPowerBase::getP54, EventPowerBase::getP55, EventPowerBase::getP56, EventPowerBase::getP57, EventPowerBase::getP58, EventPowerBase::getP59, EventPowerBase::getP60, EventPowerBase::getP61, EventPowerBase::getP62, EventPowerBase::getP63, EventPowerBase::getP64, EventPowerBase::getP65, EventPowerBase::getP66, EventPowerBase::getP67, EventPowerBase::getP68, EventPowerBase::getP69, EventPowerBase::getP70, EventPowerBase::getP71, EventPowerBase::getP72, EventPowerBase::getP73, EventPowerBase::getP74, EventPowerBase::getP75, EventPowerBase::getP76, EventPowerBase::getP77, EventPowerBase::getP78, EventPowerBase::getP79, EventPowerBase::getP80, EventPowerBase::getP81, EventPowerBase::getP82, EventPowerBase::getP83, EventPowerBase::getP84, EventPowerBase::getP85, EventPowerBase::getP86, EventPowerBase::getP87, EventPowerBase::getP88, EventPowerBase::getP89, EventPowerBase::getP90, EventPowerBase::getP91, EventPowerBase::getP92, EventPowerBase::getP93, EventPowerBase::getP94, EventPowerBase::getP95, EventPowerBase::getP96);
                    lambdaQueryWrapper1.eq(EventPowerBase::getEventId, eventId);
                    lambdaQueryWrapper1.eq(EventPowerBase::getOrgNo, provinceCode);
                    EventPowerBase eventPowerBaseOne = eventPowerBaseService.getOne(lambdaQueryWrapper1);
                    log.info("基线负荷：" + eventPowerBaseOne);
                    // 时间段总实时响应负荷
                    BigDecimal eventPowerValue = BigDecimal.ZERO;
                    // 时间段总基线负荷
                    BigDecimal eventPowerBaseValue = BigDecimal.ZERO;
                    // 该时间段点数量
                    int count = 0;
                    for (int i = 1; i <= 96; i++) {
                        // 通过当前时间 HH:mm 获取点数   1-96点 每15分钟一点
                        int startTimePoint = CurveUtil.covDateTimeToPoint(startTime);
                        int endTimePoint = CurveUtil.covDateTimeToPoint(endTime);
                        log.info("开始时间点：" + startTimePoint + "结束时间点：" + endTimePoint);
                        BigDecimal fieldValue1 = (BigDecimal) ReflectUtil.getFieldValue(eventPowerOne, "p" + i);
                        BigDecimal fieldValue2 = (BigDecimal) ReflectUtil.getFieldValue(eventPowerBaseOne, "p" + i);
                        if (ObjectUtil.isNotEmpty(fieldValue1) && ObjectUtil.isNotEmpty(fieldValue2) && i >= startTimePoint && i <= endTimePoint) {
                            count = count + 1;
                            eventPowerValue = eventPowerValue.add(fieldValue1);
                            eventPowerBaseValue = eventPowerBaseValue.add(fieldValue2);
                        }
                    }
                    log.info("时间段总点数：" + count);
                    log.info("时间段总实时响应负荷：" + eventPowerValue);
                    log.info("时间段总基线负荷：" + eventPowerBaseValue);
                    // ((总基线-总实时)/10000)/lengthOfTime
                    BigDecimal subtract = eventPowerBaseValue.subtract(eventPowerValue);
                    BigDecimal divideKw = subtract.divide(BigDecimal.valueOf(10000));
                    BigDecimal div = NumberUtil.div(divideKw, count);
                    affectThePower = NumberUtil.add(affectThePower, NumberUtil.mul(div, lengthOfTime));
                }
                accuracyValue = accuracyValue.add(accuracy(affectThePower, 1));
                log.info("省当日需求响应合计影响电量：" + accuracyValue + "万kw");
                Map<String, Object> map = null;
                map = setParamDate(provinceCode, "00000", "10302004", "当日需求响应合计影响电量（万kWh）", eventTimes.get(0));
                map.put("INDEX_VALUE", accuracyValue);
                records.add(map);
            } else {
                Map<String, Object> map = null;
                if (ObjectUtil.isNotEmpty(currentDate)) {
                    map = setParamDate(provinceCode, "00000", "10302004", "当日需求响应合计影响电量（万kWh）", currentDate);
                } else {
                    map = setParamDate(provinceCode, "00000", "10302004", "当日需求响应合计影响电量（万kWh）", timeFormat(1));
                }
                map.put("INDEX_VALUE", new BigDecimal("0.00"));
                records.add(map);
            }

            // 省本年度需求响应累计执行天数（天）10501001   省市一样
            int annualExecutionDays = eventMapper.annualExecutionDays();
            log.info("省本年度需求响应累计执行天数：" + annualExecutionDays + "天");
            if (annualExecutionDays >= 0) {
                Map<String, Object> map2 = null;
                map2 = setParam(provinceCode, "00000", "10501001", "本年度需求响应累计执行天数（天）");
                map2.put("INDEX_VALUE", annualExecutionDays);
                if (ObjectUtil.isNotEmpty(currentDate)) {
                    map2.put("DATA_DATE", DateUtil.parseLocalDateTime(currentDate, "yyyy-MM-dd").plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                }
                records.add(map2);
            } else {
                Map<String, Object> map2 = null;
                map2 = setParam(provinceCode, "00000", "10501001", "本年度需求响应累计执行天数（天）");
                map2.put("INDEX_VALUE", 0);
                if (ObjectUtil.isNotEmpty(currentDate)) {
                    map2.put("DATA_DATE", DateUtil.parseLocalDateTime(currentDate, "yyyy-MM-dd").plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                }
                records.add(map2);
                log.info("无省本年度需求响应累计执行天数（天）");
            }
            // 省本年度需求响应累计执行户次（户次）10501002
            int annualExecutionAccount = planConsMapper.annualExecutionAccount(null);
            log.info("省本年度需求响应累计执行户次：" + annualExecutionAccount + "户次");
            if (annualExecutionAccount >= 0) {
                Map<String, Object> map3 = null;
                map3 = setParam(provinceCode, "00000", "10501002", "本年度需求响应累计执行户次（户次）");
                map3.put("INDEX_VALUE", annualExecutionAccount);
                if (ObjectUtil.isNotEmpty(currentDate)) {
                    map3.put("DATA_DATE", DateUtil.parseLocalDateTime(currentDate, "yyyy-MM-dd").plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                }
                records.add(map3);
            } else {
                Map<String, Object> map3 = null;
                map3 = setParam(provinceCode, "00000", "10501002", "本年度需求响应累计执行户次（户次）");
                map3.put("INDEX_VALUE", 0);
                if (ObjectUtil.isNotEmpty(currentDate)) {
                    map3.put("DATA_DATE", DateUtil.parseLocalDateTime(currentDate, "yyyy-MM-dd").plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                }
                records.add(map3);
                log.info("无省本年度需求响应累计执行户次（户次）");
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
                // 获取市组织机构
                orgsList = orgsListDate.stream().filter(n -> "2".equals(n.getOrgTitle())).collect(Collectors.toList());
            } else {
                log.warn("组织机构不存在");
                return;
            }
            List<String> id = new ArrayList<>();
            if (ObjectUtil.isNotEmpty(orgsList) && orgsList.size() > 0) {
                orgsList.forEach(n -> id.add(n.getId()));
            }
            for (String s : id) {
                ResponseData<List<String>> allNextOrgId = systemClient.getAllNextOrgId(s);
                List<String> orgNoList = allNextOrgId.getData();

                // 市本年度需求响应累计执行户次（户次） 10501002
                int annualExecutionAccount1 = planConsMapper.annualExecutionAccount(orgNoList);
                log.info("市本年度需求响应累计执行户次：" + annualExecutionAccount1 + "户次");
                if (annualExecutionAccount1 >= 0) {
                    Map<String, Object> map5 = null;
                    map5 = setParam(s, provinceCode, "10501002", "本年度需求响应累计执行户次（户次）");
                    map5.put("INDEX_VALUE", annualExecutionAccount1);
                    if (ObjectUtil.isNotEmpty(currentDate)) {
                        map5.put("DATA_DATE", DateUtil.parseLocalDateTime(currentDate, "yyyy-MM-dd").plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    }
                    records.add(map5);
                } else {
                    Map<String, Object> map5 = null;
                    map5 = setParam(s, provinceCode, "10501002", "本年度需求响应累计执行户次（户次）");
                    map5.put("INDEX_VALUE", 0);
                    if (ObjectUtil.isNotEmpty(currentDate)) {
                        map5.put("DATA_DATE", DateUtil.parseLocalDateTime(currentDate, "yyyy-MM-dd").plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    }
                    records.add(map5);
                    log.info("无市本年度需求响应累计执行户次（户次）");
                }

                // 市当日需求响应合计影响电量（万kWh）10302004
                BigDecimal accuracyValue1 = BigDecimal.ZERO;
                if (CollectionUtil.isNotEmpty(eventList) && eventList.size() > 0) {
                    // 市当日需求响应合计影响电量（万kWh）
                    BigDecimal affectThePower = BigDecimal.ZERO;
                    List<String> eventTimes = new ArrayList<>();
                    // 时间调度时间
                    eventList.forEach(t -> eventTimes.add(t.getRegulateDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
                    for (Event event : eventList) {
                        String format = event.getRegulateDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                        String startTime = event.getStartTime();
                        String endTime = event.getEndTime();
                        String start = format + " " + startTime + ":00";
                        String end = format + " " + endTime + ":00";
                        LocalDateTime startTime1 = LocalDateTime.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                        LocalDateTime endTime1 = LocalDateTime.parse(end, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                        System.out.println("事件ID为：" + event.getEventId() + ",执行开始时间：" + startTime1 + "执行结束时间：" + endTime1);
                        long minutes = Duration.between(startTime1, endTime1).toMinutes();
                        // 转为小时
                        Double lengthOfTime = (double) minutes / 60;
                        Long eventId = event.getEventId();
                        // 实时响应负荷
                        LambdaQueryWrapper<EventPower> lambdaQueryWrapper2 = Wrappers.lambdaQuery();
                        lambdaQueryWrapper2.select(EventPower::getP1, EventPower::getP2, EventPower::getP3, EventPower::getP4, EventPower::getP5, EventPower::getP6, EventPower::getP7, EventPower::getP8, EventPower::getP9, EventPower::getP10, EventPower::getP11, EventPower::getP12, EventPower::getP13, EventPower::getP14, EventPower::getP15, EventPower::getP16, EventPower::getP17, EventPower::getP18, EventPower::getP19, EventPower::getP20, EventPower::getP21, EventPower::getP22, EventPower::getP23, EventPower::getP24, EventPower::getP25, EventPower::getP26, EventPower::getP27, EventPower::getP28, EventPower::getP29, EventPower::getP30, EventPower::getP31, EventPower::getP32, EventPower::getP33, EventPower::getP34, EventPower::getP35, EventPower::getP36, EventPower::getP37, EventPower::getP38, EventPower::getP39, EventPower::getP40, EventPower::getP41, EventPower::getP42, EventPower::getP43, EventPower::getP44, EventPower::getP45, EventPower::getP46, EventPower::getP47, EventPower::getP48, EventPower::getP49, EventPower::getP50, EventPower::getP51, EventPower::getP52, EventPower::getP53, EventPower::getP54, EventPower::getP55, EventPower::getP56, EventPower::getP57, EventPower::getP58, EventPower::getP59, EventPower::getP60, EventPower::getP61, EventPower::getP62, EventPower::getP63, EventPower::getP64, EventPower::getP65, EventPower::getP66, EventPower::getP67, EventPower::getP68, EventPower::getP69, EventPower::getP70, EventPower::getP71, EventPower::getP72, EventPower::getP73, EventPower::getP74, EventPower::getP75, EventPower::getP76, EventPower::getP77, EventPower::getP78, EventPower::getP79, EventPower::getP80, EventPower::getP81, EventPower::getP82, EventPower::getP83, EventPower::getP84, EventPower::getP85, EventPower::getP86, EventPower::getP87, EventPower::getP88, EventPower::getP89, EventPower::getP90, EventPower::getP91, EventPower::getP92, EventPower::getP93, EventPower::getP94, EventPower::getP95, EventPower::getP96);

                        lambdaQueryWrapper2.eq(EventPower::getEventId, eventId);
                        lambdaQueryWrapper2.eq(EventPower::getOrgNo, s);
                        EventPower eventPowerOne = eventPowerService.getOne(lambdaQueryWrapper2);
                        log.info("实时负荷：" + eventPowerOne);
                        // 基线
                        LambdaQueryWrapper<EventPowerBase> lambdaQueryWrapper1 = Wrappers.lambdaQuery();
                        lambdaQueryWrapper1.select(EventPowerBase::getP1, EventPowerBase::getP2, EventPowerBase::getP3, EventPowerBase::getP4, EventPowerBase::getP5, EventPowerBase::getP6, EventPowerBase::getP7, EventPowerBase::getP8, EventPowerBase::getP9, EventPowerBase::getP10, EventPowerBase::getP11, EventPowerBase::getP12, EventPowerBase::getP13, EventPowerBase::getP14, EventPowerBase::getP15, EventPowerBase::getP16, EventPowerBase::getP17, EventPowerBase::getP18, EventPowerBase::getP19, EventPowerBase::getP20, EventPowerBase::getP21, EventPowerBase::getP22, EventPowerBase::getP23, EventPowerBase::getP24, EventPowerBase::getP25, EventPowerBase::getP26, EventPowerBase::getP27, EventPowerBase::getP28, EventPowerBase::getP29, EventPowerBase::getP30, EventPowerBase::getP31, EventPowerBase::getP32, EventPowerBase::getP33, EventPowerBase::getP34, EventPowerBase::getP35, EventPowerBase::getP36, EventPowerBase::getP37, EventPowerBase::getP38, EventPowerBase::getP39, EventPowerBase::getP40, EventPowerBase::getP41, EventPowerBase::getP42, EventPowerBase::getP43, EventPowerBase::getP44, EventPowerBase::getP45, EventPowerBase::getP46, EventPowerBase::getP47, EventPowerBase::getP48, EventPowerBase::getP49, EventPowerBase::getP50, EventPowerBase::getP51, EventPowerBase::getP52, EventPowerBase::getP53, EventPowerBase::getP54, EventPowerBase::getP55, EventPowerBase::getP56, EventPowerBase::getP57, EventPowerBase::getP58, EventPowerBase::getP59, EventPowerBase::getP60, EventPowerBase::getP61, EventPowerBase::getP62, EventPowerBase::getP63, EventPowerBase::getP64, EventPowerBase::getP65, EventPowerBase::getP66, EventPowerBase::getP67, EventPowerBase::getP68, EventPowerBase::getP69, EventPowerBase::getP70, EventPowerBase::getP71, EventPowerBase::getP72, EventPowerBase::getP73, EventPowerBase::getP74, EventPowerBase::getP75, EventPowerBase::getP76, EventPowerBase::getP77, EventPowerBase::getP78, EventPowerBase::getP79, EventPowerBase::getP80, EventPowerBase::getP81, EventPowerBase::getP82, EventPowerBase::getP83, EventPowerBase::getP84, EventPowerBase::getP85, EventPowerBase::getP86, EventPowerBase::getP87, EventPowerBase::getP88, EventPowerBase::getP89, EventPowerBase::getP90, EventPowerBase::getP91, EventPowerBase::getP92, EventPowerBase::getP93, EventPowerBase::getP94, EventPowerBase::getP95, EventPowerBase::getP96);
                        lambdaQueryWrapper1.eq(EventPowerBase::getEventId, eventId);
                        lambdaQueryWrapper1.eq(EventPowerBase::getOrgNo, s);
                        EventPowerBase eventPowerBaseOne = eventPowerBaseService.getOne(lambdaQueryWrapper1);
                        log.info("基线负荷：" + eventPowerBaseOne);
                        // 时间段总实时响应负荷
                        BigDecimal eventPowerValue = BigDecimal.ZERO;
                        // 时间段总基线负荷
                        BigDecimal eventPowerBaseValue = BigDecimal.ZERO;
                        // 该时间段点数量
                        int count = 0;
                        for (int i = 1; i <= 96; i++) {
                            // 通过当前时间 HH:mm 获取点数   1-96点 每15分钟一点
                            int startTimePoint = CurveUtil.covDateTimeToPoint(startTime);
                            int endTimePoint = CurveUtil.covDateTimeToPoint(endTime);
                            log.info("开始时间点：" + startTimePoint + "结束时间点：" + endTimePoint);
                            BigDecimal fieldValue1 = (BigDecimal) ReflectUtil.getFieldValue(eventPowerOne, "p" + i);
                            BigDecimal fieldValue2 = (BigDecimal) ReflectUtil.getFieldValue(eventPowerBaseOne, "p" + i);
                            if (ObjectUtil.isNotEmpty(fieldValue1) && ObjectUtil.isNotEmpty(fieldValue2) && i >= startTimePoint && i <= endTimePoint) {
                                count = count + 1;
                                eventPowerValue = eventPowerValue.add(fieldValue1);
                                eventPowerBaseValue = eventPowerBaseValue.add(fieldValue2);
                            }
                        }
                        log.info("时间段总点数：" + count);
                        log.info("时间段总实时响应负荷：" + eventPowerValue);
                        log.info("时间段总基线负荷：" + eventPowerBaseValue);
                        // ((总基线-总实时)/10000)/lengthOfTime
                        BigDecimal subtract = eventPowerBaseValue.subtract(eventPowerValue);
                        BigDecimal divideKw = subtract.divide(BigDecimal.valueOf(10000));
                        BigDecimal div = NumberUtil.div(divideKw, count);
                        affectThePower = NumberUtil.add(affectThePower, NumberUtil.mul(div, lengthOfTime));
                    }
                    accuracyValue1 = accuracyValue1.add(accuracy(affectThePower, 1));
                    log.info("市当日需求响应合计影响电量：" + accuracyValue1 + "万kw");
                    Map<String, Object> map = null;
                    map = setParamDate(s, provinceCode, "10302004", "当日需求响应合计影响电量（万kWh）", eventTimes.get(0));
                    map.put("INDEX_VALUE", accuracyValue1);
                    records.add(map);
                } else {
                    Map<String, Object> map6 = null;
                    if (ObjectUtil.isNotEmpty(currentDate)) {
                        map6 = setParamDate(s, provinceCode, "10302004", "当日需求响应合计影响电量（万kWh）", currentDate);
                    } else {
                        map6 = setParamDate(s, provinceCode, "10302004", "当日需求响应合计影响电量（万kWh）", timeFormat(1));
                    }
                    map6.put("INDEX_VALUE", new BigDecimal("0.00"));
                    records.add(map6);
                }

                // 市本年度需求响应累计执行天数（天）10501001   省市一样
                int annualExecutionDays1 = eventMapper.annualExecutionDays();
                log.info("市本年度需求响应累计执行天数：" + annualExecutionDays1 + "天");
                if (annualExecutionDays >= 0) {
                    Map<String, Object> map8 = null;
                    map8 = setParam(s, provinceCode, "10501001", "本年度需求响应累计执行天数（天）");
                    map8.put("INDEX_VALUE", annualExecutionDays1);
                    if (ObjectUtil.isNotEmpty(currentDate)) {
                        map8.put("DATA_DATE", DateUtil.parseLocalDateTime(currentDate, "yyyy-MM-dd").plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    }
                    records.add(map8);
                } else {
                    Map<String, Object> map8 = null;
                    map8 = setParam(s, provinceCode, "10501001", "本年度需求响应累计执行天数（天）");
                    map8.put("INDEX_VALUE", 0);
                    if (ObjectUtil.isNotEmpty(currentDate)) {
                        map8.put("DATA_DATE", DateUtil.parseLocalDateTime(currentDate, "yyyy-MM-dd").plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    }
                    records.add(map8);
                    log.info("无市本年度需求响应累计执行天数（天）");
                }
            }
            log.info("统计指标（当日与年度）3种--当日需求响应合计影响电量（万kWh）0、本年度需求响应累计执行天数（天）、本年度需求响应累计执行户次（户次）：" + records.size() + "条。");
            log.info("统计指标（当日与年度）3种--当日需求响应合计影响电量（万kWh）0、本年度需求响应累计执行天数（天）、本年度需求响应累计执行户次（户次）：" + records.toString());
            if (records.size() > 0) {
                records.forEach(item -> {
                    log.info("统计指标（当日与年度）3种：" + item.get("INDEX_ORG_NO") + "\t" + item.get("INDEX_NO") + "\t" + item.get("DATA_DATE") + "\t" + "当前时间：" + item.get("UPDATE_TIME"));
                    String sql = String.format("select index_id from %s where index_org_no='%s' and index_no='%s' and data_date='%s'", "exch_index_data", item.get("INDEX_ORG_NO"), item.get("INDEX_NO"), item.get("DATA_DATE"));
                    String idValue = jdbcTemplate.query(sql, rs -> {
                        if (rs.next()) {
                            return rs.getObject(1, String.class);
                        }
                        return null;
                    });
                    log.info("数据库当天是否插入过数据：" + idValue);
                    String execSql = null;
                    if (idValue == null) {
                        execSql = generalInsertSql(item);
                    } else {
                        item.remove("CREATE_TIME");
                        item.put("index_id", idValue);
                        execSql = generalUpdateSql(item, "index_id");
                    }
                    log.info("execSql=> {}", execSql);
                    jdbcTemplate.execute(execSql);
                });
            }
            log.info("统计指标（当日与年度）4种执行结束--当日需求响应合计影响电量（万kWh）0、本年度需求响应累计执行天数（天）、本年度需求响应累计执行户次（户次）");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 资源指标----负荷聚合商数量（户)  负荷聚合商聚合客户数（户） 负荷聚合商聚合负荷规模（万kW）上报
     */
    private void saveThreeLoadAggregator(String param) {
        log.info("负荷聚合商3种开始执行！！！");
        List<Map<String, Object>> records = new ArrayList<>();
        // 省级编码
        String provinceCode = null;
        // 获取市级组织机构
        List<SysOrgs> orgsList = null;
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

            // 负荷聚合商归省级所有  市级没有负荷聚合商
            // 省负荷聚合商数量（户) 34101  10107003
            int custCount = custMapper.custCount();
            log.info("省级负荷聚合商数量（户):" + custCount + "户");
            if (custCount >= 0) {
                Map<String, Object> map = null;
                map = setParam(provinceCode, "00000", "10107003", "省负荷聚合商数量（户)");
                map.put("INDEX_VALUE", custCount);
                records.add(map);
            } else {
                Map<String, Object> map = null;
                map = setParam(provinceCode, "00000", "10107003", "省负荷聚合商数量（户)");
                map.put("INDEX_VALUE", 0);
                records.add(map);
                log.info("无负荷聚合商数量");
            }

            // 省负荷聚合商聚合客户数（户）34101  10107001
            int userConsRelaCount = userConsRelaMapper.userConsRelaCount();
            log.info("省级负荷聚合商聚合客户数（户）:" + userConsRelaCount + "户");
            if (userConsRelaCount >= 0) {
                Map<String, Object> map = null;
                map = setParam(provinceCode, "00000", "10107001", "负荷聚合商聚合客户数（户）");
                map.put("INDEX_VALUE", userConsRelaCount);
                records.add(map);
            } else {
                Map<String, Object> map = null;
                map = setParam(provinceCode, "00000", "10107001", "负荷聚合商聚合客户数（户）");
                map.put("INDEX_VALUE", 0);
                records.add(map);
                log.info("无省负荷聚合商聚合客户数");
            }

            // 省负荷聚合商聚合负荷规模（万kW）34101  10107002
            BigDecimal consContractDetailCount = consContractDetailMapper.consContractDetailCount();
            BigDecimal accuracy = accuracy(consContractDetailCount, 1);
            log.info("省级负荷聚合商聚合负荷规模（万kW）:" + accuracy + "万kw");
            if (BigDecimal.ZERO.compareTo(accuracy) < 0) {
                Map<String, Object> map = null;
                map = setParam(provinceCode, "00000", "10107002", "负荷聚合商聚合负荷规模（万kW）");
                map.put("INDEX_VALUE", accuracy);
                records.add(map);
            } else if (BigDecimal.ZERO.compareTo(accuracy) == 0) {
                Map<String, Object> map = null;
                map = setParam(provinceCode, "00000", "10107002", "负荷聚合商聚合负荷规模（万kW）");
                map.put("INDEX_VALUE", new BigDecimal("0.00"));
                records.add(map);
            } else {
                Map<String, Object> map = null;
                map = setParam(provinceCode, "00000", "10107002", "负荷聚合商聚合负荷规模（万kW）");
                map.put("INDEX_VALUE", new BigDecimal("0.00"));
                records.add(map);
                log.info("无省负荷聚合商聚合负荷规模（万kW）");
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
                // 获取市组织机构
                orgsList = orgsListDate.stream().filter(n -> "2".equals(n.getOrgTitle())).collect(Collectors.toList());
            } else {
                log.warn("组织机构不存在");
                return;
            }
            List<String> id = new ArrayList<>();
            if (ObjectUtil.isNotEmpty(orgsList) && orgsList.size() > 0) {
                orgsList.forEach(n -> id.add(n.getId()));
            }
            for (String o : id) {
                // 市负荷聚合商数量（户) 10107003
                Map<String, Object> map = null;
                map = setParam(o, provinceCode, "10107003", "负荷聚合商数量（户)");
                map.put("INDEX_VALUE", 0);
                records.add(map);

                // 市负荷聚合商聚合客户数（户） 10107001
                Map<String, Object> map1 = null;
                map1 = setParam(o, provinceCode, "10107001", "负荷聚合商聚合客户数（户）");
                map1.put("INDEX_VALUE", 0);
                records.add(map1);

                // 市负荷聚合商聚合负荷规模（万kW） 10107002
                Map<String, Object> map2 = null;
                map2 = setParam(o, provinceCode, "10107002", "负荷聚合商聚合负荷规模（万kW）");
                map2.put("INDEX_VALUE", new BigDecimal("0.00"));
                records.add(map2);
            }
            log.info("负荷聚合商3种共：" + records.size() + "条。");
            log.info("负荷聚合商3种：" + records.toString());
            if (records.size() > 0) {
                records.forEach(item -> {
                    log.info("负荷聚合商3种：" + item.get("INDEX_ORG_NO") + "\t" + item.get("INDEX_NO") + "\t" + item.get("DATA_DATE") + "\t" + "当前时间：" + item.get("UPDATE_TIME"));
                    String sql = String.format("select index_id from %s where index_org_no='%s' and index_no='%s' and data_date='%s'", "exch_index_data", item.get("INDEX_ORG_NO"), item.get("INDEX_NO"), item.get("DATA_DATE"));
                    String idValue = jdbcTemplate.query(sql, rs -> {
                        if (rs.next()) {
                            return rs.getObject(1, String.class);
                        }
                        return null;
                    });
                    log.info("数据库当天是否插入过数据：" + idValue);
                    String execSql = null;
                    if (idValue == null) {
                        execSql = generalInsertSql(item);
                    } else {
                        item.remove("CREATE_TIME");
                        item.put("index_id", idValue);
                        execSql = generalUpdateSql(item, "index_id");
                    }
                    log.info("execSql=> {}", execSql);
                    jdbcTemplate.execute(execSql);
                });
            }
            log.info("负荷聚合商3种执行完成！！！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 不同分类行业签约容量
     *
     * @param customerType1 100--->工业用户、200--->楼宇用户、若customerType1和customerType2都传入值，请为methodType传2
     * @param customerType2 100--->工业用户、200--->楼宇用户、若customerType1和customerType2都传入值，请为methodType传2
     * @param methodType    1---->查询单个行业签约容量、  其他数字---->除传入的两个行业类型剩下的行业签约容量
     * @param orgNoList     供电编码
     * @return
     */
    private BigDecimal userContractedCapacity(String customerType1, String customerType2, int methodType, List<String> orgNoList, String projectId) {
        if (methodType == 1) {
            return consContractDetailMapper.userContractedCapacity(customerType1, orgNoList, projectId);
        } else {
            return consContractDetailMapper.userContractedCapacity1(customerType1, customerType2, orgNoList, projectId);
        }
    }

    /**
     * 不同分类行业用户
     *
     * @param customerType1 100--->工业用户、200--->楼宇用户、若customerType1和customerType2都传入值，请为methodType传2
     * @param customerType2 100--->工业用户、200--->楼宇用户、若customerType1和customerType2都传入值，请为methodType传2
     * @param methodType    1---->查询单个行业用户数、  其他数字---->除传入的两个行业类型剩下的用户数
     * @param orgNoList     供电编码
     * @return 数量
     */
    private Integer customerTypeCount(String customerType1, String customerType2, int methodType, List<String> orgNoList) {
        if (methodType == 1) {
            return consMapper.customerTypeCount(customerType1, orgNoList);
        } else {
            return consMapper.customerTypeCount1(customerType1, customerType2, orgNoList);
        }
    }

    private String generalInsertSql(Map<String, Object> fields) {
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
        return String.format("insert into %s (%s) values (%s)", "exch_index_data", sb1, sb2);
    }

    private String generalUpdateSql(Map<String, Object> fields, String idKey) {
        String setSql = fields.entrySet().stream().filter(item -> !idKey.equalsIgnoreCase(item.getKey()))
                .map(item -> {
                    if (item.getValue() != null) {
                        return item.getKey() + "='" + item.getValue() + "'";
                    } else {
                        return item.getKey() + "=null";
                    }
                }).collect(Collectors.joining(","));
        return String.format("update %s set %s where %s='%s'", "exch_index_data", setSql, idKey, fields.get(idKey));
    }


    /**
     * BigDecimal 精度  四舍五入
     *
     * @param param bigDecimal 类型
     * @param type  1---->保留2位，2----->保留4位（百分比）
     * @return BigDecimal
     */
    private BigDecimal accuracy(BigDecimal param, int type) {
        if (type == 1) {
            return new BigDecimal(param.toPlainString()).setScale(2, BigDecimal.ROUND_HALF_UP);
        } else {
            return new BigDecimal(param.toPlainString()).setScale(4, BigDecimal.ROUND_HALF_UP);
        }
    }

    /**
     * @param userType 1工业用户、2楼宇用户、3分布式电源、4储能用户、5自备电厂用户、6电动汽车充电桩（站）、7其它
     * @param dataName customers-->需求响应不同分类用户客户数（户）、load-->需求响应不同分类用户签约容量（万kW）
     * @param value    对应类型值
     * @return
     */
    private Map<String, Object> setFormat(String userType, String dataName, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put("cuser_type", userType);
        map.put("cons_" + dataName, value);
        return map;
    }

    /**
     * @param provinceCode 所属管理单位编码
     * @param code         指标编码
     * @param parentCode   上级管理单位
     * @param name         指标名称
     * @return
     */
    private Map<String, Object> setParam(String provinceCode, String parentCode, String code, String name) {
        Map<String, Object> map = new HashMap<>();
        /**
         * 获取当前日期 返回当前时间 年月日格式  yyyy-MM-dd
         */
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        /**
         * 获取当前日期 返回当前时间 年月日格式  yyyy-MM-dd
         */
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        /**
         * 获取当前日期 返回当前时间 年月日 时分秒格式  yyyy-MM-dd HH:mm:ss
         */
        String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String id = provinceCode + "_" + code + "_" + date;
        // 主键标识
        map.put("INDEX_ID", id);
        // 所属管理单位编码
        map.put("INDEX_ORG_NO", provinceCode);
        // 上级管理单位
        map.put("INDEX_P_ORG_NO", parentCode);
        // 所属客户编码
        map.put("INDEX_CONS_NO", "");
        // 指标编码
        map.put("INDEX_NO", code);
        // 指标英文编码
        map.put("INDEX_CODE", "");
        // 指标中文名称
        map.put("INDEX_NAME", name);
        // 指标数据时间
        map.put("DATA_DATE", time);
        // 创建时间
        map.put("CREATE_TIME", dateTime);
        // 更新时间
        map.put("UPDATE_TIME", dateTime);
        // 上传时间
        map.put("UPLOAD_TIME", dateTime);
        return map;
    }

    /**
     * @param provinceCode 所属管理单位编码
     * @param code         指标编码
     * @param parentCode   上级管理单位
     * @param name         指标名称
     * @param time         事件调度时间
     * @return
     */
    private Map<String, Object> setParamDate(String provinceCode, String parentCode, String code, String name, String time) {
        Map<String, Object> map = new HashMap<>();
        /**
         * 获取当前日期 返回当前时间 年月日格式  yyyy-MM-dd
         */
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        /**
         * 获取当前日期 返回当前时间 年月日 时分秒格式  yyyy-MM-dd HH:mm:ss
         */
        String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String id = provinceCode + "_" + code + "_" + date;
        // 主键标识
        map.put("INDEX_ID", id);
        // 所属管理单位编码
        map.put("INDEX_ORG_NO", provinceCode);
        // 上级管理单位
        map.put("INDEX_P_ORG_NO", parentCode);
        // 所属客户编码
        map.put("INDEX_CONS_NO", "");
        // 指标编码
        map.put("INDEX_NO", code);
        // 指标英文编码
        map.put("INDEX_CODE", "");
        // 指标中文名称
        map.put("INDEX_NAME", name);
        // 指标数据时间
        map.put("DATA_DATE", time);
        // 创建时间
        map.put("CREATE_TIME", dateTime);
        // 更新时间
        map.put("UPDATE_TIME", dateTime);
        // 上传时间
        map.put("UPLOAD_TIME", dateTime);
        return map;
    }

    /**
     * 日前需求响应户数（户） 10304001
     * 日内需求响应户数（户） 10305001
     * 实时需求响应户数（户） 10306001
     *
     * @param projectId         项目id
     * @param responseType      响应类型 1削峰，2填谷
     * @param timeType          时间类型 1邀约，2实时
     * @param advanceNoticeTime 提前通知时间 1日前，2小时级，3分钟级，4秒级
     * @param orgNoList         供电单位编码列表
     * @return 客户数量
     */
    private Integer getHouseholds(String projectId, String responseType, String timeType, String advanceNoticeTime, List<String> orgNoList) {
        Integer households = consMapper.getHouseholds(projectId, responseType, timeType, advanceNoticeTime, orgNoList);
        if (ObjectUtil.isEmpty(households)) {
            return 0;
        } else {
            return households == null ? 0 : households;
        }
    }

    /**
     * 日前需求响应可调负荷规模（万kW）	10304002
     * 日内需求响应可调负荷规模（万kW）	10305002
     * 实时需求响应可调负荷规模（万kW）	10306002
     */
    private BigDecimal getContractCap(List<WorkProjectInfoDTO> workPageCapDetail, Integer responseType, Integer timeType, Integer advanceNoticeTime) {
        List<WorkProjectInfoDTO> workProjectInfoDTOS = workPageCapDetail.stream()
                .filter(item -> Objects.equals(item.getResponseType(), responseType) && Objects.equals(item.getTimeType(), timeType) && (advanceNoticeTime == null || Objects.equals(item.getAdvanceNoticeTime(), advanceNoticeTime)))
                .collect(Collectors.toList());
        if (workProjectInfoDTOS == null || workProjectInfoDTOS.size() == 0) {
            return BigDecimal.valueOf(0);
        } else {
            BigDecimal contractCap = workProjectInfoDTOS.get(0).getContractCap();
            return contractCap == null ? BigDecimal.ZERO : contractCap;
        }
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