package com.xqxy.executor.service.jobhandler;

import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.log.Log;
import com.alibaba.cloud.commons.lang.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.xqxy.core.client.SystemClient;
import com.xqxy.dr.modular.baseline.util.CurveUtil;
import com.xqxy.dr.modular.device.entity.SysOrgs;
import com.xqxy.dr.modular.evaluation.entity.DrPlan;
import com.xqxy.dr.modular.newloadmanagement.mapper.BaselineAndMeasurementMapper;
import com.xqxy.dr.modular.newloadmanagement.service.DrConsService;
import com.xqxy.dr.modular.newloadmanagement.vo.DrEventTime;
import com.xqxy.dr.modular.newloadmanagement.vo.ExchPoint96Vo;
import com.xqxy.dr.modular.newloadmanagement.vo.Point96Vo;
import com.xqxy.sys.modular.dict.param.DictTypeParam;
import com.xqxy.sys.modular.dict.service.DictTypeService;
import com.xqxy.sys.modular.utils.JSONObjectToEntityUtils;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.log.XxlJobLogger;
import net.sf.cglib.core.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.integration.IntegrationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class BaselineAndMeasurementByJdbcJob {
    private static final Log log = Log.get();

    @Autowired
    private SystemClient systemClient;


//    @Value("jdbc:mysql://192.168.1.160:3306/sc_dataupload?autoReconnect=true&useUnicode=true&allowMultiQueries=true&characterEncoding=utf8&zeroDateTimeBehavior=CONVERT_TO_NULL&useSSL=false&serverTimezone=CTT")
//    private String mdtfJdbcUrl;
//    @Value("xqxy")
//    private String mdtfUser;
//    @Value("XQXY_qwe123")
//    private String mdtfPassword;


    @Value("${exchCurve.jdbcUrl:}")
    private String mdtfJdbcUrl;
    @Value("${exchCurve.username:}")
    private String mdtfUser;
    @Value("${exchCurve.password:}")
    private String mdtfPassword;


    @Autowired
    private DrConsService drConsService;
    @Autowired
    private BaselineAndMeasurementMapper baselineAndMeasurementMapper;


    @Resource
    private DictTypeService dictTypeService;


    private JdbcTemplate jdbcTemplate;

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


    List<String> curveIds = new LinkedList<>();
    List<String> curveIds2 = new LinkedList<>();
    List<String> curveIds3 = new LinkedList<>();

    private Map<String, List> getEventIDSByDate() {
        List<String> orgIds = null;
        List<String> provinceOrgId = null;
        JSONObject result = systemClient.queryAllOrg();
        List<SysOrgs> list = new ArrayList<>();
        List<SysOrgs> listProvince = new ArrayList<>();
        if ("000000".equals(result.getString("code"))) {
            JSONArray datas = result.getJSONArray("data");
            if (null != datas && datas.size() > 0) {
                for (Object obj : datas) {
                    JSONObject jsonObject = (JSONObject) JSONObject.toJSON(obj);
                    SysOrgs sysOrgs = JSONObjectToEntityUtils.JSONObjectToSysOrg(jsonObject);
                    list.add(sysOrgs);
                    listProvince.add(sysOrgs);
                }
            }
        }
        orgIds = list.stream().filter(s -> "2".equals(s.getOrgTitle())).map(SysOrgs::getId).collect(Collectors.toList());
        provinceOrgId = list.stream().filter(s -> "1".equals(s.getOrgTitle())).map(SysOrgs::getId).collect(Collectors.toList());

        for (String s : provinceOrgId) {
            orgIds.add(s);
        }

        LocalDate now = LocalDate.now();
        Map<String, List> map = new HashMap<>();
        List<Long> eventIds = baselineAndMeasurementMapper.getEventId(now);
        map.put("eventIds", eventIds);
        map.put("orgIds", orgIds);
        return map;
    }


    //获取基线数据
    private List<Point96Vo> getBaseLine(String orgId) {
        Map<String, List> eventIDSByDate = this.getEventIDSByDate();
        List<Point96Vo> baseLineList = new ArrayList<>();
        List<Long> consIds = null;
        if (eventIDSByDate.get("eventIds") != null && eventIDSByDate.get("eventIds").size() > 0) {
            List<DrPlan> planIds = baselineAndMeasurementMapper.getPlanIdAndBaseLineID(eventIDSByDate.get("eventIds"));
            if (planIds != null && planIds.size() > 0) {
                for (DrPlan planId : planIds) {
                    Point96Vo baseLine = new Point96Vo();
                    consIds = baselineAndMeasurementMapper.getConsId(planId.getPlanID());
                    LocalDate localDate = LocalDate.now();
                    if (consIds != null && consIds.size() > 0) {
                        baseLine = baselineAndMeasurementMapper.getBaseLine96Point(consIds, localDate, planId.getBaselinId());
                        if (baseLine != null) {
                            baseLine.setEventId(String.valueOf(planId.getRegulateId()));
                            baseLineList.add(baseLine);
                        }
                    }
                }
            }
        }
        return baseLineList;
    }

    //获取基线数据
    private List<Point96Vo> getBaseLineByParamTime(List eventId, LocalDate pDate) {
        Map<String, List> eventIDSByDate = this.getEventIDSByDate();
        List<Point96Vo> baseLineList = new ArrayList<>();
        Point96Vo baseLine = new Point96Vo();
        List<Long> consIds = null;
        if (eventId != null && eventId.size() > 0) {
            List<DrPlan> planIds = baselineAndMeasurementMapper.getPlanIdAndBaseLineID(eventId);
            if (planIds != null && planIds.size() > 0) {
                for (DrPlan planId : planIds) {

                    consIds = baselineAndMeasurementMapper.getConsId(planId.getPlanID());
                    if (consIds != null && consIds.size() > 0) {
                        baseLine = baselineAndMeasurementMapper.getBaseLine96Point(consIds, pDate, planId.getBaselinId());
                        if (baseLine != null) {
                            baseLine.setEventId(planId.getRegulateId());
                            baseLineList.add(baseLine);
                        }
                    }
                }
            }
        }
        return baseLineList;
    }


    //获取时间段基线数据
    private Point96Vo getTimeBaseLine(String eventId) {
        Map<String, List> eventIDSByDate = this.getEventIDSByDate();
        Point96Vo baseLine = new Point96Vo();
        baseLine = baselineAndMeasurementMapper.getTimeBaseLine(eventId);

        return baseLine;
    }


    private List<Point96Vo> getBaseLineByParamTimeAndEventId(String eventId, LocalDate pDate) {
        Map<String, List> eventIDSByDate = this.getEventIDSByDate();
        List<Point96Vo> baseLineList = new ArrayList<>();
        Point96Vo baseLine = null;
        List<Long> consIds = null;
        if (eventId != null) {
            List<DrPlan> planIds = baselineAndMeasurementMapper.getPlanIdAndBaseLineIDByEvent(eventId);
            if (planIds != null && planIds.size() > 0) {
                for (DrPlan planId : planIds) {
                    consIds = baselineAndMeasurementMapper.getConsId(planId.getPlanID());
                    if (consIds != null && consIds.size() > 0) {
                        baseLine = baselineAndMeasurementMapper.getBaseLine96Point(consIds, pDate, planId.getBaselinId());
                        baseLineList.add(baseLine);
                    }
                }
            }
        }
        return baseLineList;
    }


    //获取实时数据
    private List<Point96Vo> getRealTimeData(String orgId) {
//        Map<String, List> eventIDSByDate = this.getEventIDSByDate();
        List<Point96Vo> realTimeData = null;
        //todo 这里没有事件，需要加判断
        LocalDate now = LocalDate.now();

//        if(eventIDSByDate.get("eventIds")!=null && eventIDSByDate.get("eventIds").size() > 0){
//             realTimeData = baselineAndMeasurementMapper.getRealTimeData(eventIDSByDate.get("eventIds"), orgId);
//        }

        if (baselineAndMeasurementMapper.getEventId2(now) != null && baselineAndMeasurementMapper.getEventId2(now).size() > 0) {
            realTimeData = baselineAndMeasurementMapper.getRealTimeData(baselineAndMeasurementMapper.getEventId2(now), orgId);
        }
        return realTimeData;
    }


    private List<Point96Vo> getRealTimeDataByParamTime(String orgId, List eventId) {
        Map<String, List> eventIDSByDate = this.getEventIDSByDate();
        List<Point96Vo> realTimeData = null;
        if (eventId != null && eventId.size() > 0) {
            realTimeData = baselineAndMeasurementMapper.getRealTimeData(eventId, orgId);
        }
        return realTimeData;
    }

    private List<Point96Vo> getRealTimeDataByParamTimeAndEventId(String orgId, String eventId) {
        Map<String, List> eventIDSByDate = this.getEventIDSByDate();
        List<Point96Vo> realTimeData = null;
        if (eventId != null) {
            realTimeData = baselineAndMeasurementMapper.getRealTimeDataByEvent(eventId, orgId);
        }
        return realTimeData;
    }


    //获取实时冻结数据
    private List<Point96Vo> getFrozenRealData(String orgId) {
        Map<String, List> eventIDSByDate = this.getEventIDSByDate();
        List<Point96Vo> frozenData = null;
        LocalDate localDate = LocalDate.now();
        LocalDate localDate1 = localDate.plusDays(-1);
        List<Long> eventIds = baselineAndMeasurementMapper.getEventId2(localDate1);
        if (eventIds != null && eventIds.size() > 0) {
            frozenData = baselineAndMeasurementMapper.getFrozenData(eventIds, orgId);
        }
        return frozenData;
    }

    private List<Point96Vo> getFrozenRealData2(String orgId, List eventIds) {
        return baselineAndMeasurementMapper.getFrozenData(eventIds, orgId);
    }


    private List<Point96Vo> getFrozenDataByparamTime(String orgId, String param) {
        Map<String, List> eventIDSByDate = this.getEventIDSByDate();
        List<Point96Vo> frozenData = null;
        String[] strings = param.split("-");
        LocalDate pDate = LocalDate.of(Integer.parseInt(strings[0]), Integer.parseInt(strings[1]), Integer.parseInt(strings[2]));
        LocalDate localDate1 = pDate.plusDays(-1);
        List<Long> eventIds = baselineAndMeasurementMapper.getEventId(localDate1);
        if (eventIds != null && eventIds.size() > 0) {
            frozenData = baselineAndMeasurementMapper.getFrozenData(eventIds, orgId);
        }
        return frozenData;
    }


    @XxlJob("baselineData")
    public ReturnT<String> baselineData(String param) throws Exception {
        XxlJobLogger.log("基线数据----上报");
        this.saveBaselineToDatabase(param);
        return ReturnT.SUCCESS;
    }


    @XxlJob("realTimeData")
    public ReturnT<String> realTimeData(String param) throws Exception {
        XxlJobLogger.log("实时数据----上报");
        this.saveRealTimeToDatabase(param);
        return ReturnT.SUCCESS;
    }

    @XxlJob("depressData")
    public ReturnT<String> depressData(String param) throws Exception {
        XxlJobLogger.log("压减数据----上报");
        this.saveDepressToDatabase(param);
        return ReturnT.SUCCESS;
    }

    @XxlJob("frozenData")
    public ReturnT<String> frozenData(String param) throws Exception {
        XxlJobLogger.log("冻结数据----上报");
        this.saveFrozenToDatabase(param);
        return ReturnT.SUCCESS;
    }


    @XxlJob("baselineDataAndDepressDataUpdate")
    public ReturnT<String> baselineDataAndDepressDataUpdate(String param) throws Exception {
        XxlJobLogger.log("国网基线与压降数据修改----上报");
        this.saveBaselineDataAndDepressDataUpdate(param);
        return ReturnT.SUCCESS;
    }


    private Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(mdtfJdbcUrl, mdtfUser, mdtfPassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }


    private String getProvinceCode() {
        String provinceCode = null;
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
        }
        return provinceCode;
    }


    public void saveBaselineToDatabase(String param) {
        if (StringUtils.isNotBlank(param)) {
            if (param.equals("clear1")) {
                curveIds.clear();
            }
            if (param.equals("clear2")) {
                curveIds2.clear();
            }
            if (param.equals("clear3")) {
                curveIds2.clear();
            }

            Connection connection = getConnection();
            PreparedStatement preparedStatement = null;
            LocalDate pDate = null;
            try {
                String[] strings = param.split("-");
                pDate = LocalDate.of(Integer.parseInt(strings[0]), Integer.parseInt(strings[1]), Integer.parseInt(strings[2]));
                List<Long> eventIds = baselineAndMeasurementMapper.getEventId(pDate);
                List orgIds = getEventIDSByDate().get("orgIds");
                for (Object orgId : orgIds) {
                    if (getProvinceCode().equals(orgId.toString())) {
                        List<Point96Vo> baseLine = this.getBaseLineByParamTime(eventIds, pDate);
                        if (baseLine == null || baseLine.size() <= 0) {
                            continue;
                        }
                        int count = 0;
                        for (Point96Vo p : baseLine) {
                            count++;
                            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
                            String curveIdFlag = orgId.toString() + "_101_207_" + count + pDate.format(dateTimeFormatter);
                            if (curveIds == null || curveIds.size() <= 0 || !curveIds.contains(curveIdFlag)) {

                                String sql = "insert into exch_static_curve_data_org(curve_id, curve_org_no, curve_p_org_no, curve_cons_no, curve_tgt_type," +
                                        " curve_type, stats_freq, data_date, point1, point2, point3, point4, point5, point6, point7, point8, " +
                                        "point9,point10,point11, point12, point13, point14, point15, point16, point17, point18, point19, point20," +
                                        " point21, point22, point23, point24, point25, point26, point27, point28, point29, point30, point31, " +
                                        "point32, point33, point34, point35, point36, point37, point38, point39, point40, point41, point42, point43," +
                                        " point44, point45, point46, point47, point48, point49, point50, point51, point52, point53, point54, " +
                                        "point55, point56, point57, point58, point59, point60, point61, point62, point63, point64, point65, point66," +
                                        "point67, point68, point69, point70, point71, point72, point73, point74, point75, point76, point77, " +
                                        "point78, point79, point80, point81, point82, point83, point84, point85, point86, point87, point88, point89," +
                                        " point90, point91, point92, point93, point94, point95, point96, extend_flag, extend_value, create_time, update_time," +
                                        " upload_time) VALUES(" +
                                        "?,?,?,?,?,?,?,?,?,?," +
                                        "?,?,?,?,?,?,?,?,?,?," +
                                        "?,?,?,?,?,?,?,?,?,?," +
                                        "?,?,?,?,?,?,?,?,?,?," +
                                        "?,?,?,?,?,?,?,?,?,?," +
                                        "?,?,?,?,?,?,?,?,?,?," +
                                        "?,?,?,?,?,?,?,?,?,?," +
                                        "?,?,?,?,?,?,?,?,?,?," +
                                        "?,?,?,?,?,?,?,?,?,?," +
                                        "?,?,?,?,?,?,?,?,?,?," +
                                        "?,?,?,?,?,?,?,?,?)";
                                try {
                                    preparedStatement = connection.prepareStatement(sql);
                                    String provinceCode = this.getProvinceCode();
                                    if (orgId.toString().equals(provinceCode)) {
                                        String curveId = provinceCode + "_101_207_" + count + pDate.format(dateTimeFormatter);
                                        curveIds.add(curveId);
                                        preparedStatement.setString(1, curveId);
                                        preparedStatement.setString(2, orgId.toString());
                                        preparedStatement.setString(3, "00000");
                                        preparedStatement.setString(4, "");
                                        preparedStatement.setString(5, "207_" + count);
                                        preparedStatement.setString(6, "101");
                                        preparedStatement.setString(7, "1");
                                        preparedStatement.setDate(8, new java.sql.Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000));
                                        preparedStatement.setBigDecimal(9, p.getP1());
                                        preparedStatement.setBigDecimal(10, p.getP2());
                                        preparedStatement.setBigDecimal(11, p.getP3());
                                        preparedStatement.setBigDecimal(12, p.getP4());
                                        preparedStatement.setBigDecimal(13, p.getP5());
                                        preparedStatement.setBigDecimal(14, p.getP6());
                                        preparedStatement.setBigDecimal(15, p.getP7());
                                        preparedStatement.setBigDecimal(16, p.getP8());
                                        preparedStatement.setBigDecimal(17, p.getP9());
                                        preparedStatement.setBigDecimal(18, p.getP10());
                                        preparedStatement.setBigDecimal(19, p.getP11());
                                        preparedStatement.setBigDecimal(20, p.getP12());
                                        preparedStatement.setBigDecimal(21, p.getP13());
                                        preparedStatement.setBigDecimal(22, p.getP14());
                                        preparedStatement.setBigDecimal(23, p.getP15());
                                        preparedStatement.setBigDecimal(24, p.getP16());
                                        preparedStatement.setBigDecimal(25, p.getP17());
                                        preparedStatement.setBigDecimal(26, p.getP18());
                                        preparedStatement.setBigDecimal(27, p.getP19());
                                        preparedStatement.setBigDecimal(28, p.getP20());
                                        preparedStatement.setBigDecimal(29, p.getP21());
                                        preparedStatement.setBigDecimal(30, p.getP22());
                                        preparedStatement.setBigDecimal(31, p.getP23());
                                        preparedStatement.setBigDecimal(32, p.getP24());
                                        preparedStatement.setBigDecimal(33, p.getP25());
                                        preparedStatement.setBigDecimal(34, p.getP26());
                                        preparedStatement.setBigDecimal(35, p.getP27());
                                        preparedStatement.setBigDecimal(36, p.getP28());
                                        preparedStatement.setBigDecimal(37, p.getP29());
                                        preparedStatement.setBigDecimal(38, p.getP30());
                                        preparedStatement.setBigDecimal(39, p.getP31());
                                        preparedStatement.setBigDecimal(40, p.getP32());
                                        preparedStatement.setBigDecimal(41, p.getP33());
                                        preparedStatement.setBigDecimal(42, p.getP34());
                                        preparedStatement.setBigDecimal(43, p.getP35());
                                        preparedStatement.setBigDecimal(44, p.getP36());
                                        preparedStatement.setBigDecimal(45, p.getP37());
                                        preparedStatement.setBigDecimal(46, p.getP38());
                                        preparedStatement.setBigDecimal(47, p.getP39());
                                        preparedStatement.setBigDecimal(48, p.getP40());
                                        preparedStatement.setBigDecimal(49, p.getP41());
                                        preparedStatement.setBigDecimal(50, p.getP42());
                                        preparedStatement.setBigDecimal(51, p.getP43());
                                        preparedStatement.setBigDecimal(52, p.getP44());
                                        preparedStatement.setBigDecimal(53, p.getP45());
                                        preparedStatement.setBigDecimal(54, p.getP46());
                                        preparedStatement.setBigDecimal(55, p.getP47());
                                        preparedStatement.setBigDecimal(56, p.getP48());
                                        preparedStatement.setBigDecimal(57, p.getP49());
                                        preparedStatement.setBigDecimal(58, p.getP50());
                                        preparedStatement.setBigDecimal(59, p.getP51());
                                        preparedStatement.setBigDecimal(60, p.getP52());
                                        preparedStatement.setBigDecimal(61, p.getP53());
                                        preparedStatement.setBigDecimal(62, p.getP54());
                                        preparedStatement.setBigDecimal(63, p.getP55());
                                        preparedStatement.setBigDecimal(64, p.getP56());
                                        preparedStatement.setBigDecimal(65, p.getP57());
                                        preparedStatement.setBigDecimal(66, p.getP58());
                                        preparedStatement.setBigDecimal(67, p.getP59());
                                        preparedStatement.setBigDecimal(68, p.getP60());
                                        preparedStatement.setBigDecimal(69, p.getP61());
                                        preparedStatement.setBigDecimal(70, p.getP62());
                                        preparedStatement.setBigDecimal(71, p.getP63());
                                        preparedStatement.setBigDecimal(72, p.getP64());
                                        preparedStatement.setBigDecimal(73, p.getP65());
                                        preparedStatement.setBigDecimal(74, p.getP66());
                                        preparedStatement.setBigDecimal(75, p.getP67());
                                        preparedStatement.setBigDecimal(76, p.getP68());
                                        preparedStatement.setBigDecimal(77, p.getP69());
                                        preparedStatement.setBigDecimal(78, p.getP70());
                                        preparedStatement.setBigDecimal(79, p.getP71());
                                        preparedStatement.setBigDecimal(80, p.getP72());
                                        preparedStatement.setBigDecimal(81, p.getP73());
                                        preparedStatement.setBigDecimal(82, p.getP74());
                                        preparedStatement.setBigDecimal(83, p.getP75());
                                        preparedStatement.setBigDecimal(84, p.getP76());
                                        preparedStatement.setBigDecimal(85, p.getP77());
                                        preparedStatement.setBigDecimal(86, p.getP78());
                                        preparedStatement.setBigDecimal(87, p.getP79());
                                        preparedStatement.setBigDecimal(88, p.getP80());
                                        preparedStatement.setBigDecimal(89, p.getP81());
                                        preparedStatement.setBigDecimal(90, p.getP82());
                                        preparedStatement.setBigDecimal(91, p.getP83());
                                        preparedStatement.setBigDecimal(92, p.getP84());
                                        preparedStatement.setBigDecimal(93, p.getP85());
                                        preparedStatement.setBigDecimal(94, p.getP86());
                                        preparedStatement.setBigDecimal(95, p.getP87());
                                        preparedStatement.setBigDecimal(96, p.getP88());
                                        preparedStatement.setBigDecimal(97, p.getP89());
                                        preparedStatement.setBigDecimal(98, p.getP90());
                                        preparedStatement.setBigDecimal(99, p.getP91());
                                        preparedStatement.setBigDecimal(100, p.getP92());
                                        preparedStatement.setBigDecimal(101, p.getP93());
                                        preparedStatement.setBigDecimal(102, p.getP94());
                                        preparedStatement.setBigDecimal(103, p.getP95());
                                        preparedStatement.setBigDecimal(104, p.getP96());
                                        preparedStatement.setInt(105, 4);
                                        preparedStatement.setString(106, p.getEventId());
                                        preparedStatement.setDate(107, new java.sql.Date(System.currentTimeMillis()));
                                        preparedStatement.setDate(108, new java.sql.Date(System.currentTimeMillis()));
                                        preparedStatement.setDate(109, new java.sql.Date(System.currentTimeMillis()));

                                        preparedStatement.executeUpdate();

                                    } else {
                                        String curveId = orgId.toString() + "_101_207_" + count + pDate.format(dateTimeFormatter);
                                        curveIds.add(curveId);
                                        preparedStatement.setString(1, curveId);
                                        preparedStatement.setString(2, orgId.toString());
                                        preparedStatement.setString(3, provinceCode);
                                        preparedStatement.setString(4, "");
                                        preparedStatement.setString(5, "207_" + count);
                                        preparedStatement.setString(6, "101");
                                        preparedStatement.setString(7, "1");
                                        preparedStatement.setDate(8, new java.sql.Date(System.currentTimeMillis()));
                                        preparedStatement.setBigDecimal(9, p.getP1());
                                        preparedStatement.setBigDecimal(10, p.getP2());
                                        preparedStatement.setBigDecimal(11, p.getP3());
                                        preparedStatement.setBigDecimal(12, p.getP4());
                                        preparedStatement.setBigDecimal(13, p.getP5());
                                        preparedStatement.setBigDecimal(14, p.getP6());
                                        preparedStatement.setBigDecimal(15, p.getP7());
                                        preparedStatement.setBigDecimal(16, p.getP8());
                                        preparedStatement.setBigDecimal(17, p.getP9());
                                        preparedStatement.setBigDecimal(18, p.getP10());
                                        preparedStatement.setBigDecimal(19, p.getP11());
                                        preparedStatement.setBigDecimal(20, p.getP12());
                                        preparedStatement.setBigDecimal(21, p.getP13());
                                        preparedStatement.setBigDecimal(22, p.getP14());
                                        preparedStatement.setBigDecimal(23, p.getP15());
                                        preparedStatement.setBigDecimal(24, p.getP16());
                                        preparedStatement.setBigDecimal(25, p.getP17());
                                        preparedStatement.setBigDecimal(26, p.getP18());
                                        preparedStatement.setBigDecimal(27, p.getP19());
                                        preparedStatement.setBigDecimal(28, p.getP20());
                                        preparedStatement.setBigDecimal(29, p.getP21());
                                        preparedStatement.setBigDecimal(30, p.getP22());
                                        preparedStatement.setBigDecimal(31, p.getP23());
                                        preparedStatement.setBigDecimal(32, p.getP24());
                                        preparedStatement.setBigDecimal(33, p.getP25());
                                        preparedStatement.setBigDecimal(34, p.getP26());
                                        preparedStatement.setBigDecimal(35, p.getP27());
                                        preparedStatement.setBigDecimal(36, p.getP28());
                                        preparedStatement.setBigDecimal(37, p.getP29());
                                        preparedStatement.setBigDecimal(38, p.getP30());
                                        preparedStatement.setBigDecimal(39, p.getP31());
                                        preparedStatement.setBigDecimal(40, p.getP32());
                                        preparedStatement.setBigDecimal(41, p.getP33());
                                        preparedStatement.setBigDecimal(42, p.getP34());
                                        preparedStatement.setBigDecimal(43, p.getP35());
                                        preparedStatement.setBigDecimal(44, p.getP36());
                                        preparedStatement.setBigDecimal(45, p.getP37());
                                        preparedStatement.setBigDecimal(46, p.getP38());
                                        preparedStatement.setBigDecimal(47, p.getP39());
                                        preparedStatement.setBigDecimal(48, p.getP40());
                                        preparedStatement.setBigDecimal(49, p.getP41());
                                        preparedStatement.setBigDecimal(50, p.getP42());
                                        preparedStatement.setBigDecimal(51, p.getP43());
                                        preparedStatement.setBigDecimal(52, p.getP44());
                                        preparedStatement.setBigDecimal(53, p.getP45());
                                        preparedStatement.setBigDecimal(54, p.getP46());
                                        preparedStatement.setBigDecimal(55, p.getP47());
                                        preparedStatement.setBigDecimal(56, p.getP48());
                                        preparedStatement.setBigDecimal(57, p.getP49());
                                        preparedStatement.setBigDecimal(58, p.getP50());
                                        preparedStatement.setBigDecimal(59, p.getP51());
                                        preparedStatement.setBigDecimal(60, p.getP52());
                                        preparedStatement.setBigDecimal(61, p.getP53());
                                        preparedStatement.setBigDecimal(62, p.getP54());
                                        preparedStatement.setBigDecimal(63, p.getP55());
                                        preparedStatement.setBigDecimal(64, p.getP56());
                                        preparedStatement.setBigDecimal(65, p.getP57());
                                        preparedStatement.setBigDecimal(66, p.getP58());
                                        preparedStatement.setBigDecimal(67, p.getP59());
                                        preparedStatement.setBigDecimal(68, p.getP60());
                                        preparedStatement.setBigDecimal(69, p.getP61());
                                        preparedStatement.setBigDecimal(70, p.getP62());
                                        preparedStatement.setBigDecimal(71, p.getP63());
                                        preparedStatement.setBigDecimal(72, p.getP64());
                                        preparedStatement.setBigDecimal(73, p.getP65());
                                        preparedStatement.setBigDecimal(74, p.getP66());
                                        preparedStatement.setBigDecimal(75, p.getP67());
                                        preparedStatement.setBigDecimal(76, p.getP68());
                                        preparedStatement.setBigDecimal(77, p.getP69());
                                        preparedStatement.setBigDecimal(78, p.getP70());
                                        preparedStatement.setBigDecimal(79, p.getP71());
                                        preparedStatement.setBigDecimal(80, p.getP72());
                                        preparedStatement.setBigDecimal(81, p.getP73());
                                        preparedStatement.setBigDecimal(82, p.getP74());
                                        preparedStatement.setBigDecimal(83, p.getP75());
                                        preparedStatement.setBigDecimal(84, p.getP76());
                                        preparedStatement.setBigDecimal(85, p.getP77());
                                        preparedStatement.setBigDecimal(86, p.getP78());
                                        preparedStatement.setBigDecimal(87, p.getP79());
                                        preparedStatement.setBigDecimal(88, p.getP80());
                                        preparedStatement.setBigDecimal(89, p.getP81());
                                        preparedStatement.setBigDecimal(90, p.getP82());
                                        preparedStatement.setBigDecimal(91, p.getP83());
                                        preparedStatement.setBigDecimal(92, p.getP84());
                                        preparedStatement.setBigDecimal(93, p.getP85());
                                        preparedStatement.setBigDecimal(94, p.getP86());
                                        preparedStatement.setBigDecimal(95, p.getP87());
                                        preparedStatement.setBigDecimal(96, p.getP88());
                                        preparedStatement.setBigDecimal(97, p.getP89());
                                        preparedStatement.setBigDecimal(98, p.getP90());
                                        preparedStatement.setBigDecimal(99, p.getP91());
                                        preparedStatement.setBigDecimal(100, p.getP92());
                                        preparedStatement.setBigDecimal(101, p.getP93());
                                        preparedStatement.setBigDecimal(102, p.getP94());
                                        preparedStatement.setBigDecimal(103, p.getP95());
                                        preparedStatement.setBigDecimal(104, p.getP96());
                                        preparedStatement.setInt(105, 4);
                                        preparedStatement.setString(106, p.getEventId());
                                        preparedStatement.setDate(107, new java.sql.Date(System.currentTimeMillis()));
                                        preparedStatement.setDate(108, new java.sql.Date(System.currentTimeMillis()));
                                        preparedStatement.setDate(109, new java.sql.Date(System.currentTimeMillis()));
                                        preparedStatement.executeUpdate();
                                    }
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (preparedStatement != null) {
                        preparedStatement.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = null;
            try {
                List orgIds = getEventIDSByDate().get("orgIds");
                for (Object orgId : orgIds) {
                    if (getProvinceCode().equals(orgId.toString())) {
                        List<Point96Vo> baseLine = this.getBaseLine(orgId.toString());
                        if (baseLine == null || baseLine.size() <= 0) {
                            continue;
                        }
                        int count = 0;
                        for (Point96Vo p : baseLine) {
                            count++;
                            LocalDate localDate = LocalDate.now();
                            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
                            String curveIdFlag = orgId.toString() + "_101_207_" + count + localDate.format(dateTimeFormatter);
                            if (curveIds == null || curveIds.size() <= 0 || !curveIds.contains(curveIdFlag)) {

                                String sql = "insert into exch_static_curve_data_org(curve_id, curve_org_no, curve_p_org_no, curve_cons_no, curve_tgt_type," +
                                        " curve_type, stats_freq, data_date, point1, point2, point3, point4, point5, point6, point7, point8, " +
                                        "point9,point10,point11, point12, point13, point14, point15, point16, point17, point18, point19, point20," +
                                        " point21, point22, point23, point24, point25, point26, point27, point28, point29, point30, point31, " +
                                        "point32, point33, point34, point35, point36, point37, point38, point39, point40, point41, point42, point43," +
                                        " point44, point45, point46, point47, point48, point49, point50, point51, point52, point53, point54, " +
                                        "point55, point56, point57, point58, point59, point60, point61, point62, point63, point64, point65, point66," +
                                        "point67, point68, point69, point70, point71, point72, point73, point74, point75, point76, point77, " +
                                        "point78, point79, point80, point81, point82, point83, point84, point85, point86, point87, point88, point89," +
                                        " point90, point91, point92, point93, point94, point95, point96, extend_flag, extend_value, create_time, update_time," +
                                        " upload_time) VALUES(" +
                                        "?,?,?,?,?,?,?,?,?,?," +
                                        "?,?,?,?,?,?,?,?,?,?," +
                                        "?,?,?,?,?,?,?,?,?,?," +
                                        "?,?,?,?,?,?,?,?,?,?," +
                                        "?,?,?,?,?,?,?,?,?,?," +
                                        "?,?,?,?,?,?,?,?,?,?," +
                                        "?,?,?,?,?,?,?,?,?,?," +
                                        "?,?,?,?,?,?,?,?,?,?," +
                                        "?,?,?,?,?,?,?,?,?,?," +
                                        "?,?,?,?,?,?,?,?,?,?," +
                                        "?,?,?,?,?,?,?,?,?)";
                                try {
                                    preparedStatement = connection.prepareStatement(sql);
                                    String provinceCode = this.getProvinceCode();
                                    if (orgId.toString().equals(provinceCode)) {
                                        String curveId = provinceCode + "_101_207_" + count + localDate.format(dateTimeFormatter);
                                        curveIds.add(curveId);
                                        preparedStatement.setString(1, curveId);
                                        preparedStatement.setString(2, orgId.toString());
                                        preparedStatement.setString(3, "00000");
                                        preparedStatement.setString(4, "");
                                        preparedStatement.setString(5, "207_" + count);
                                        preparedStatement.setString(6, "101");
                                        preparedStatement.setString(7, "1");
                                        preparedStatement.setDate(8, new java.sql.Date(System.currentTimeMillis()));
                                        preparedStatement.setBigDecimal(9, p.getP1());
                                        preparedStatement.setBigDecimal(10, p.getP2());
                                        preparedStatement.setBigDecimal(11, p.getP3());
                                        preparedStatement.setBigDecimal(12, p.getP4());
                                        preparedStatement.setBigDecimal(13, p.getP5());
                                        preparedStatement.setBigDecimal(14, p.getP6());
                                        preparedStatement.setBigDecimal(15, p.getP7());
                                        preparedStatement.setBigDecimal(16, p.getP8());
                                        preparedStatement.setBigDecimal(17, p.getP9());
                                        preparedStatement.setBigDecimal(18, p.getP10());
                                        preparedStatement.setBigDecimal(19, p.getP11());
                                        preparedStatement.setBigDecimal(20, p.getP12());
                                        preparedStatement.setBigDecimal(21, p.getP13());
                                        preparedStatement.setBigDecimal(22, p.getP14());
                                        preparedStatement.setBigDecimal(23, p.getP15());
                                        preparedStatement.setBigDecimal(24, p.getP16());
                                        preparedStatement.setBigDecimal(25, p.getP17());
                                        preparedStatement.setBigDecimal(26, p.getP18());
                                        preparedStatement.setBigDecimal(27, p.getP19());
                                        preparedStatement.setBigDecimal(28, p.getP20());
                                        preparedStatement.setBigDecimal(29, p.getP21());
                                        preparedStatement.setBigDecimal(30, p.getP22());
                                        preparedStatement.setBigDecimal(31, p.getP23());
                                        preparedStatement.setBigDecimal(32, p.getP24());
                                        preparedStatement.setBigDecimal(33, p.getP25());
                                        preparedStatement.setBigDecimal(34, p.getP26());
                                        preparedStatement.setBigDecimal(35, p.getP27());
                                        preparedStatement.setBigDecimal(36, p.getP28());
                                        preparedStatement.setBigDecimal(37, p.getP29());
                                        preparedStatement.setBigDecimal(38, p.getP30());
                                        preparedStatement.setBigDecimal(39, p.getP31());
                                        preparedStatement.setBigDecimal(40, p.getP32());
                                        preparedStatement.setBigDecimal(41, p.getP33());
                                        preparedStatement.setBigDecimal(42, p.getP34());
                                        preparedStatement.setBigDecimal(43, p.getP35());
                                        preparedStatement.setBigDecimal(44, p.getP36());
                                        preparedStatement.setBigDecimal(45, p.getP37());
                                        preparedStatement.setBigDecimal(46, p.getP38());
                                        preparedStatement.setBigDecimal(47, p.getP39());
                                        preparedStatement.setBigDecimal(48, p.getP40());
                                        preparedStatement.setBigDecimal(49, p.getP41());
                                        preparedStatement.setBigDecimal(50, p.getP42());
                                        preparedStatement.setBigDecimal(51, p.getP43());
                                        preparedStatement.setBigDecimal(52, p.getP44());
                                        preparedStatement.setBigDecimal(53, p.getP45());
                                        preparedStatement.setBigDecimal(54, p.getP46());
                                        preparedStatement.setBigDecimal(55, p.getP47());
                                        preparedStatement.setBigDecimal(56, p.getP48());
                                        preparedStatement.setBigDecimal(57, p.getP49());
                                        preparedStatement.setBigDecimal(58, p.getP50());
                                        preparedStatement.setBigDecimal(59, p.getP51());
                                        preparedStatement.setBigDecimal(60, p.getP52());
                                        preparedStatement.setBigDecimal(61, p.getP53());
                                        preparedStatement.setBigDecimal(62, p.getP54());
                                        preparedStatement.setBigDecimal(63, p.getP55());
                                        preparedStatement.setBigDecimal(64, p.getP56());
                                        preparedStatement.setBigDecimal(65, p.getP57());
                                        preparedStatement.setBigDecimal(66, p.getP58());
                                        preparedStatement.setBigDecimal(67, p.getP59());
                                        preparedStatement.setBigDecimal(68, p.getP60());
                                        preparedStatement.setBigDecimal(69, p.getP61());
                                        preparedStatement.setBigDecimal(70, p.getP62());
                                        preparedStatement.setBigDecimal(71, p.getP63());
                                        preparedStatement.setBigDecimal(72, p.getP64());
                                        preparedStatement.setBigDecimal(73, p.getP65());
                                        preparedStatement.setBigDecimal(74, p.getP66());
                                        preparedStatement.setBigDecimal(75, p.getP67());
                                        preparedStatement.setBigDecimal(76, p.getP68());
                                        preparedStatement.setBigDecimal(77, p.getP69());
                                        preparedStatement.setBigDecimal(78, p.getP70());
                                        preparedStatement.setBigDecimal(79, p.getP71());
                                        preparedStatement.setBigDecimal(80, p.getP72());
                                        preparedStatement.setBigDecimal(81, p.getP73());
                                        preparedStatement.setBigDecimal(82, p.getP74());
                                        preparedStatement.setBigDecimal(83, p.getP75());
                                        preparedStatement.setBigDecimal(84, p.getP76());
                                        preparedStatement.setBigDecimal(85, p.getP77());
                                        preparedStatement.setBigDecimal(86, p.getP78());
                                        preparedStatement.setBigDecimal(87, p.getP79());
                                        preparedStatement.setBigDecimal(88, p.getP80());
                                        preparedStatement.setBigDecimal(89, p.getP81());
                                        preparedStatement.setBigDecimal(90, p.getP82());
                                        preparedStatement.setBigDecimal(91, p.getP83());
                                        preparedStatement.setBigDecimal(92, p.getP84());
                                        preparedStatement.setBigDecimal(93, p.getP85());
                                        preparedStatement.setBigDecimal(94, p.getP86());
                                        preparedStatement.setBigDecimal(95, p.getP87());
                                        preparedStatement.setBigDecimal(96, p.getP88());
                                        preparedStatement.setBigDecimal(97, p.getP89());
                                        preparedStatement.setBigDecimal(98, p.getP90());
                                        preparedStatement.setBigDecimal(99, p.getP91());
                                        preparedStatement.setBigDecimal(100, p.getP92());
                                        preparedStatement.setBigDecimal(101, p.getP93());
                                        preparedStatement.setBigDecimal(102, p.getP94());
                                        preparedStatement.setBigDecimal(103, p.getP95());
                                        preparedStatement.setBigDecimal(104, p.getP96());
                                        preparedStatement.setInt(105, 4);
                                        preparedStatement.setString(106, p.getEventId());
                                        preparedStatement.setDate(107, new java.sql.Date(System.currentTimeMillis()));
                                        preparedStatement.setDate(108, new java.sql.Date(System.currentTimeMillis()));
                                        preparedStatement.setDate(109, new java.sql.Date(System.currentTimeMillis()));

                                        preparedStatement.executeUpdate();
                                    } else {
                                        String curveId = orgId.toString() + "_101_207_" + count + localDate.format(dateTimeFormatter);
                                        curveIds.add(curveId);
                                        preparedStatement.setString(1, curveId);
                                        preparedStatement.setString(2, orgId.toString());
                                        preparedStatement.setString(3, provinceCode);
                                        preparedStatement.setString(4, "");
                                        preparedStatement.setString(5, "207_" + count);
                                        preparedStatement.setString(6, "101");
                                        preparedStatement.setString(7, "1");
                                        preparedStatement.setDate(8, new java.sql.Date(System.currentTimeMillis()));
                                        preparedStatement.setBigDecimal(9, p.getP1());
                                        preparedStatement.setBigDecimal(10, p.getP2());
                                        preparedStatement.setBigDecimal(11, p.getP3());
                                        preparedStatement.setBigDecimal(12, p.getP4());
                                        preparedStatement.setBigDecimal(13, p.getP5());
                                        preparedStatement.setBigDecimal(14, p.getP6());
                                        preparedStatement.setBigDecimal(15, p.getP7());
                                        preparedStatement.setBigDecimal(16, p.getP8());
                                        preparedStatement.setBigDecimal(17, p.getP9());
                                        preparedStatement.setBigDecimal(18, p.getP10());
                                        preparedStatement.setBigDecimal(19, p.getP11());
                                        preparedStatement.setBigDecimal(20, p.getP12());
                                        preparedStatement.setBigDecimal(21, p.getP13());
                                        preparedStatement.setBigDecimal(22, p.getP14());
                                        preparedStatement.setBigDecimal(23, p.getP15());
                                        preparedStatement.setBigDecimal(24, p.getP16());
                                        preparedStatement.setBigDecimal(25, p.getP17());
                                        preparedStatement.setBigDecimal(26, p.getP18());
                                        preparedStatement.setBigDecimal(27, p.getP19());
                                        preparedStatement.setBigDecimal(28, p.getP20());
                                        preparedStatement.setBigDecimal(29, p.getP21());
                                        preparedStatement.setBigDecimal(30, p.getP22());
                                        preparedStatement.setBigDecimal(31, p.getP23());
                                        preparedStatement.setBigDecimal(32, p.getP24());
                                        preparedStatement.setBigDecimal(33, p.getP25());
                                        preparedStatement.setBigDecimal(34, p.getP26());
                                        preparedStatement.setBigDecimal(35, p.getP27());
                                        preparedStatement.setBigDecimal(36, p.getP28());
                                        preparedStatement.setBigDecimal(37, p.getP29());
                                        preparedStatement.setBigDecimal(38, p.getP30());
                                        preparedStatement.setBigDecimal(39, p.getP31());
                                        preparedStatement.setBigDecimal(40, p.getP32());
                                        preparedStatement.setBigDecimal(41, p.getP33());
                                        preparedStatement.setBigDecimal(42, p.getP34());
                                        preparedStatement.setBigDecimal(43, p.getP35());
                                        preparedStatement.setBigDecimal(44, p.getP36());
                                        preparedStatement.setBigDecimal(45, p.getP37());
                                        preparedStatement.setBigDecimal(46, p.getP38());
                                        preparedStatement.setBigDecimal(47, p.getP39());
                                        preparedStatement.setBigDecimal(48, p.getP40());
                                        preparedStatement.setBigDecimal(49, p.getP41());
                                        preparedStatement.setBigDecimal(50, p.getP42());
                                        preparedStatement.setBigDecimal(51, p.getP43());
                                        preparedStatement.setBigDecimal(52, p.getP44());
                                        preparedStatement.setBigDecimal(53, p.getP45());
                                        preparedStatement.setBigDecimal(54, p.getP46());
                                        preparedStatement.setBigDecimal(55, p.getP47());
                                        preparedStatement.setBigDecimal(56, p.getP48());
                                        preparedStatement.setBigDecimal(57, p.getP49());
                                        preparedStatement.setBigDecimal(58, p.getP50());
                                        preparedStatement.setBigDecimal(59, p.getP51());
                                        preparedStatement.setBigDecimal(60, p.getP52());
                                        preparedStatement.setBigDecimal(61, p.getP53());
                                        preparedStatement.setBigDecimal(62, p.getP54());
                                        preparedStatement.setBigDecimal(63, p.getP55());
                                        preparedStatement.setBigDecimal(64, p.getP56());
                                        preparedStatement.setBigDecimal(65, p.getP57());
                                        preparedStatement.setBigDecimal(66, p.getP58());
                                        preparedStatement.setBigDecimal(67, p.getP59());
                                        preparedStatement.setBigDecimal(68, p.getP60());
                                        preparedStatement.setBigDecimal(69, p.getP61());
                                        preparedStatement.setBigDecimal(70, p.getP62());
                                        preparedStatement.setBigDecimal(71, p.getP63());
                                        preparedStatement.setBigDecimal(72, p.getP64());
                                        preparedStatement.setBigDecimal(73, p.getP65());
                                        preparedStatement.setBigDecimal(74, p.getP66());
                                        preparedStatement.setBigDecimal(75, p.getP67());
                                        preparedStatement.setBigDecimal(76, p.getP68());
                                        preparedStatement.setBigDecimal(77, p.getP69());
                                        preparedStatement.setBigDecimal(78, p.getP70());
                                        preparedStatement.setBigDecimal(79, p.getP71());
                                        preparedStatement.setBigDecimal(80, p.getP72());
                                        preparedStatement.setBigDecimal(81, p.getP73());
                                        preparedStatement.setBigDecimal(82, p.getP74());
                                        preparedStatement.setBigDecimal(83, p.getP75());
                                        preparedStatement.setBigDecimal(84, p.getP76());
                                        preparedStatement.setBigDecimal(85, p.getP77());
                                        preparedStatement.setBigDecimal(86, p.getP78());
                                        preparedStatement.setBigDecimal(87, p.getP79());
                                        preparedStatement.setBigDecimal(88, p.getP80());
                                        preparedStatement.setBigDecimal(89, p.getP81());
                                        preparedStatement.setBigDecimal(90, p.getP82());
                                        preparedStatement.setBigDecimal(91, p.getP83());
                                        preparedStatement.setBigDecimal(92, p.getP84());
                                        preparedStatement.setBigDecimal(93, p.getP85());
                                        preparedStatement.setBigDecimal(94, p.getP86());
                                        preparedStatement.setBigDecimal(95, p.getP87());
                                        preparedStatement.setBigDecimal(96, p.getP88());
                                        preparedStatement.setBigDecimal(97, p.getP89());
                                        preparedStatement.setBigDecimal(98, p.getP90());
                                        preparedStatement.setBigDecimal(99, p.getP91());
                                        preparedStatement.setBigDecimal(100, p.getP92());
                                        preparedStatement.setBigDecimal(101, p.getP93());
                                        preparedStatement.setBigDecimal(102, p.getP94());
                                        preparedStatement.setBigDecimal(103, p.getP95());
                                        preparedStatement.setBigDecimal(104, p.getP96());
                                        preparedStatement.setInt(105, 4);
                                        preparedStatement.setString(106, p.getEventId());
                                        preparedStatement.setDate(107, new java.sql.Date(System.currentTimeMillis()));
                                        preparedStatement.setDate(108, new java.sql.Date(System.currentTimeMillis()));
                                        preparedStatement.setDate(109, new java.sql.Date(System.currentTimeMillis()));
                                        preparedStatement.executeUpdate();
                                    }
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (preparedStatement != null) {
                        preparedStatement.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    private void saveRealTimeToDatabase(String param) {
        if (StringUtils.isNotBlank(param)) {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = null;
            PreparedStatement preparedStatement2 = null;
            LocalDate pDate = null;
            List orgIds = getEventIDSByDate().get("orgIds");
            try {
                String[] strings = param.split("-");
                pDate = LocalDate.of(Integer.parseInt(strings[0]), Integer.parseInt(strings[1]), Integer.parseInt(strings[2]));
                List<Long> eventIds = baselineAndMeasurementMapper.getEventId(pDate);
                for (Object orgId : orgIds) {
                    if (getProvinceCode().equals(orgId.toString())) {
                        if (eventIds != null && eventIds.size() > 0) {
                            List<Point96Vo> realTimeData = this.getRealTimeDataByParamTime(orgId.toString(), eventIds);
                            if (realTimeData == null || realTimeData.size() <= 0) {
                                continue;
                            }
                            int count = 0;
                            for (Point96Vo p : realTimeData) {
                                count++;
                                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
                                String curveIdFlag = orgId + "_102_207_" + count + pDate.format(dateTimeFormatter);
                                if (curveIds2 == null || curveIds2.size() <= 0 || !curveIds2.contains(curveIdFlag)) {
                                    String sql = "insert into exch_rt_curve_data_org VALUES(" +
                                            "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?" +
                                            ",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
                                            "?,?,?,?,?,?,?,?,?,?" +
                                            ",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
                                            "?,?,?,?,?,?,?,?,?,?,?,?,?)";
                                    String sql2 = "insert into exch_rt_curve_data_org(curve_id,curve_org_no,curve_tgt_type,curve_type,data_date,extend_value,create_time,update_time,upload_time) " +
                                            "VALUES(?,?,?,?,?,?,?,?,?)";
                                    try {
                                        preparedStatement = connection.prepareStatement(sql);
                                        preparedStatement2 = connection.prepareStatement(sql2);
                                        String provinceCode = this.getProvinceCode();
                                        if (orgId.toString().equals(provinceCode)) {
                                            String curveId = provinceCode + "_102_207_" + count + pDate.format(dateTimeFormatter);
                                            curveIds2.add(curveId);
                                            preparedStatement.setString(1, curveId);
                                            preparedStatement.setString(2, orgId.toString());
                                            preparedStatement.setString(3, "00000");
                                            preparedStatement.setString(4, "");
                                            preparedStatement.setString(5, "207_" + count);
                                            preparedStatement.setString(6, "102");
                                            preparedStatement.setString(7, "1");
                                            preparedStatement.setDate(8, new java.sql.Date(System.currentTimeMillis()));
                                            preparedStatement.setBigDecimal(9, p.getP1());
                                            preparedStatement.setBigDecimal(10, p.getP2());
                                            preparedStatement.setBigDecimal(11, p.getP3());
                                            preparedStatement.setBigDecimal(12, p.getP4());
                                            preparedStatement.setBigDecimal(13, p.getP5());
                                            preparedStatement.setBigDecimal(14, p.getP6());
                                            preparedStatement.setBigDecimal(15, p.getP7());
                                            preparedStatement.setBigDecimal(16, p.getP8());
                                            preparedStatement.setBigDecimal(17, p.getP9());
                                            preparedStatement.setBigDecimal(18, p.getP10());
                                            preparedStatement.setBigDecimal(19, p.getP11());
                                            preparedStatement.setBigDecimal(20, p.getP12());
                                            preparedStatement.setBigDecimal(21, p.getP13());
                                            preparedStatement.setBigDecimal(22, p.getP14());
                                            preparedStatement.setBigDecimal(23, p.getP15());
                                            preparedStatement.setBigDecimal(24, p.getP16());
                                            preparedStatement.setBigDecimal(25, p.getP17());
                                            preparedStatement.setBigDecimal(26, p.getP18());
                                            preparedStatement.setBigDecimal(27, p.getP19());
                                            preparedStatement.setBigDecimal(28, p.getP20());
                                            preparedStatement.setBigDecimal(29, p.getP21());
                                            preparedStatement.setBigDecimal(30, p.getP22());
                                            preparedStatement.setBigDecimal(31, p.getP23());
                                            preparedStatement.setBigDecimal(32, p.getP24());
                                            preparedStatement.setBigDecimal(33, p.getP25());
                                            preparedStatement.setBigDecimal(34, p.getP26());
                                            preparedStatement.setBigDecimal(35, p.getP27());
                                            preparedStatement.setBigDecimal(36, p.getP28());
                                            preparedStatement.setBigDecimal(37, p.getP29());
                                            preparedStatement.setBigDecimal(38, p.getP30());
                                            preparedStatement.setBigDecimal(39, p.getP31());
                                            preparedStatement.setBigDecimal(40, p.getP32());
                                            preparedStatement.setBigDecimal(41, p.getP33());
                                            preparedStatement.setBigDecimal(42, p.getP34());
                                            preparedStatement.setBigDecimal(43, p.getP35());
                                            preparedStatement.setBigDecimal(44, p.getP36());
                                            preparedStatement.setBigDecimal(45, p.getP37());
                                            preparedStatement.setBigDecimal(46, p.getP38());
                                            preparedStatement.setBigDecimal(47, p.getP39());
                                            preparedStatement.setBigDecimal(48, p.getP40());
                                            preparedStatement.setBigDecimal(49, p.getP41());
                                            preparedStatement.setBigDecimal(50, p.getP42());
                                            preparedStatement.setBigDecimal(51, p.getP43());
                                            preparedStatement.setBigDecimal(52, p.getP44());
                                            preparedStatement.setBigDecimal(53, p.getP45());
                                            preparedStatement.setBigDecimal(54, p.getP46());
                                            preparedStatement.setBigDecimal(55, p.getP47());
                                            preparedStatement.setBigDecimal(56, p.getP48());
                                            preparedStatement.setBigDecimal(57, p.getP49());
                                            preparedStatement.setBigDecimal(58, p.getP50());
                                            preparedStatement.setBigDecimal(59, p.getP51());
                                            preparedStatement.setBigDecimal(60, p.getP52());
                                            preparedStatement.setBigDecimal(61, p.getP53());
                                            preparedStatement.setBigDecimal(62, p.getP54());
                                            preparedStatement.setBigDecimal(63, p.getP55());
                                            preparedStatement.setBigDecimal(64, p.getP56());
                                            preparedStatement.setBigDecimal(65, p.getP57());
                                            preparedStatement.setBigDecimal(66, p.getP58());
                                            preparedStatement.setBigDecimal(67, p.getP59());
                                            preparedStatement.setBigDecimal(68, p.getP60());
                                            preparedStatement.setBigDecimal(69, p.getP61());
                                            preparedStatement.setBigDecimal(70, p.getP62());
                                            preparedStatement.setBigDecimal(71, p.getP63());
                                            preparedStatement.setBigDecimal(72, p.getP64());
                                            preparedStatement.setBigDecimal(73, p.getP65());
                                            preparedStatement.setBigDecimal(74, p.getP66());
                                            preparedStatement.setBigDecimal(75, p.getP67());
                                            preparedStatement.setBigDecimal(76, p.getP68());
                                            preparedStatement.setBigDecimal(77, p.getP69());
                                            preparedStatement.setBigDecimal(78, p.getP70());
                                            preparedStatement.setBigDecimal(79, p.getP71());
                                            preparedStatement.setBigDecimal(80, p.getP72());
                                            preparedStatement.setBigDecimal(81, p.getP73());
                                            preparedStatement.setBigDecimal(82, p.getP74());
                                            preparedStatement.setBigDecimal(83, p.getP75());
                                            preparedStatement.setBigDecimal(84, p.getP76());
                                            preparedStatement.setBigDecimal(85, p.getP77());
                                            preparedStatement.setBigDecimal(86, p.getP78());
                                            preparedStatement.setBigDecimal(87, p.getP79());
                                            preparedStatement.setBigDecimal(88, p.getP80());
                                            preparedStatement.setBigDecimal(89, p.getP81());
                                            preparedStatement.setBigDecimal(90, p.getP82());
                                            preparedStatement.setBigDecimal(91, p.getP83());
                                            preparedStatement.setBigDecimal(92, p.getP84());
                                            preparedStatement.setBigDecimal(93, p.getP85());
                                            preparedStatement.setBigDecimal(94, p.getP86());
                                            preparedStatement.setBigDecimal(95, p.getP87());
                                            preparedStatement.setBigDecimal(96, p.getP88());
                                            preparedStatement.setBigDecimal(97, p.getP89());
                                            preparedStatement.setBigDecimal(98, p.getP90());
                                            preparedStatement.setBigDecimal(99, p.getP91());
                                            preparedStatement.setBigDecimal(100, p.getP92());
                                            preparedStatement.setBigDecimal(101, p.getP93());
                                            preparedStatement.setBigDecimal(102, p.getP94());
                                            preparedStatement.setBigDecimal(103, p.getP95());
                                            preparedStatement.setBigDecimal(104, p.getP96());
                                            preparedStatement.setInt(105, 4);
                                            preparedStatement.setString(106, p.getEventId());
                                            preparedStatement.setDate(107, new java.sql.Date(System.currentTimeMillis()));
                                            preparedStatement.setDate(108, new java.sql.Date(System.currentTimeMillis()));
                                            preparedStatement.setDate(109, new java.sql.Date(System.currentTimeMillis()));

                                            preparedStatement.executeUpdate();

                                            String curveId2 = provinceCode + "_103_207_" + count + pDate.format(dateTimeFormatter);
                                            curveIds3.add(curveId2);
                                            preparedStatement2.setString(1, curveId2);
                                            preparedStatement2.setString(2, orgId.toString());
                                            preparedStatement2.setString(3, "207_" + count);
                                            preparedStatement2.setString(4, "103");
                                            preparedStatement2.setDate(5, new java.sql.Date(System.currentTimeMillis()));
                                            preparedStatement2.setString(6, p.getEventId());
                                            preparedStatement2.setDate(7, new java.sql.Date(System.currentTimeMillis()));
                                            preparedStatement2.setDate(8, new java.sql.Date(System.currentTimeMillis()));
                                            preparedStatement2.setDate(9, new java.sql.Date(System.currentTimeMillis()));
                                            preparedStatement2.executeUpdate();
                                        } else {
                                            String curveId = orgId + "_102_207_" + count + pDate.format(dateTimeFormatter);
                                            curveIds2.add(curveId);
                                            preparedStatement.setString(1, curveId);
                                            preparedStatement.setString(2, orgId.toString());
                                            preparedStatement.setString(3, orgId.toString());
                                            preparedStatement.setString(4, "");
                                            preparedStatement.setString(5, "207_" + count);
                                            preparedStatement.setString(6, "102");
                                            preparedStatement.setString(7, "1");
                                            preparedStatement.setDate(8, new java.sql.Date(System.currentTimeMillis()));
                                            preparedStatement.setBigDecimal(9, p.getP1());
                                            preparedStatement.setBigDecimal(10, p.getP2());
                                            preparedStatement.setBigDecimal(11, p.getP3());
                                            preparedStatement.setBigDecimal(12, p.getP4());
                                            preparedStatement.setBigDecimal(13, p.getP5());
                                            preparedStatement.setBigDecimal(14, p.getP6());
                                            preparedStatement.setBigDecimal(15, p.getP7());
                                            preparedStatement.setBigDecimal(16, p.getP8());
                                            preparedStatement.setBigDecimal(17, p.getP9());
                                            preparedStatement.setBigDecimal(18, p.getP10());
                                            preparedStatement.setBigDecimal(19, p.getP11());
                                            preparedStatement.setBigDecimal(20, p.getP12());
                                            preparedStatement.setBigDecimal(21, p.getP13());
                                            preparedStatement.setBigDecimal(22, p.getP14());
                                            preparedStatement.setBigDecimal(23, p.getP15());
                                            preparedStatement.setBigDecimal(24, p.getP16());
                                            preparedStatement.setBigDecimal(25, p.getP17());
                                            preparedStatement.setBigDecimal(26, p.getP18());
                                            preparedStatement.setBigDecimal(27, p.getP19());
                                            preparedStatement.setBigDecimal(28, p.getP20());
                                            preparedStatement.setBigDecimal(29, p.getP21());
                                            preparedStatement.setBigDecimal(30, p.getP22());
                                            preparedStatement.setBigDecimal(31, p.getP23());
                                            preparedStatement.setBigDecimal(32, p.getP24());
                                            preparedStatement.setBigDecimal(33, p.getP25());
                                            preparedStatement.setBigDecimal(34, p.getP26());
                                            preparedStatement.setBigDecimal(35, p.getP27());
                                            preparedStatement.setBigDecimal(36, p.getP28());
                                            preparedStatement.setBigDecimal(37, p.getP29());
                                            preparedStatement.setBigDecimal(38, p.getP30());
                                            preparedStatement.setBigDecimal(39, p.getP31());
                                            preparedStatement.setBigDecimal(40, p.getP32());
                                            preparedStatement.setBigDecimal(41, p.getP33());
                                            preparedStatement.setBigDecimal(42, p.getP34());
                                            preparedStatement.setBigDecimal(43, p.getP35());
                                            preparedStatement.setBigDecimal(44, p.getP36());
                                            preparedStatement.setBigDecimal(45, p.getP37());
                                            preparedStatement.setBigDecimal(46, p.getP38());
                                            preparedStatement.setBigDecimal(47, p.getP39());
                                            preparedStatement.setBigDecimal(48, p.getP40());
                                            preparedStatement.setBigDecimal(49, p.getP41());
                                            preparedStatement.setBigDecimal(50, p.getP42());
                                            preparedStatement.setBigDecimal(51, p.getP43());
                                            preparedStatement.setBigDecimal(52, p.getP44());
                                            preparedStatement.setBigDecimal(53, p.getP45());
                                            preparedStatement.setBigDecimal(54, p.getP46());
                                            preparedStatement.setBigDecimal(55, p.getP47());
                                            preparedStatement.setBigDecimal(56, p.getP48());
                                            preparedStatement.setBigDecimal(57, p.getP49());
                                            preparedStatement.setBigDecimal(58, p.getP50());
                                            preparedStatement.setBigDecimal(59, p.getP51());
                                            preparedStatement.setBigDecimal(60, p.getP52());
                                            preparedStatement.setBigDecimal(61, p.getP53());
                                            preparedStatement.setBigDecimal(62, p.getP54());
                                            preparedStatement.setBigDecimal(63, p.getP55());
                                            preparedStatement.setBigDecimal(64, p.getP56());
                                            preparedStatement.setBigDecimal(65, p.getP57());
                                            preparedStatement.setBigDecimal(66, p.getP58());
                                            preparedStatement.setBigDecimal(67, p.getP59());
                                            preparedStatement.setBigDecimal(68, p.getP60());
                                            preparedStatement.setBigDecimal(69, p.getP61());
                                            preparedStatement.setBigDecimal(70, p.getP62());
                                            preparedStatement.setBigDecimal(71, p.getP63());
                                            preparedStatement.setBigDecimal(72, p.getP64());
                                            preparedStatement.setBigDecimal(73, p.getP65());
                                            preparedStatement.setBigDecimal(74, p.getP66());
                                            preparedStatement.setBigDecimal(75, p.getP67());
                                            preparedStatement.setBigDecimal(76, p.getP68());
                                            preparedStatement.setBigDecimal(77, p.getP69());
                                            preparedStatement.setBigDecimal(78, p.getP70());
                                            preparedStatement.setBigDecimal(79, p.getP71());
                                            preparedStatement.setBigDecimal(80, p.getP72());
                                            preparedStatement.setBigDecimal(81, p.getP73());
                                            preparedStatement.setBigDecimal(82, p.getP74());
                                            preparedStatement.setBigDecimal(83, p.getP75());
                                            preparedStatement.setBigDecimal(84, p.getP76());
                                            preparedStatement.setBigDecimal(85, p.getP77());
                                            preparedStatement.setBigDecimal(86, p.getP78());
                                            preparedStatement.setBigDecimal(87, p.getP79());
                                            preparedStatement.setBigDecimal(88, p.getP80());
                                            preparedStatement.setBigDecimal(89, p.getP81());
                                            preparedStatement.setBigDecimal(90, p.getP82());
                                            preparedStatement.setBigDecimal(91, p.getP83());
                                            preparedStatement.setBigDecimal(92, p.getP84());
                                            preparedStatement.setBigDecimal(93, p.getP85());
                                            preparedStatement.setBigDecimal(94, p.getP86());
                                            preparedStatement.setBigDecimal(95, p.getP87());
                                            preparedStatement.setBigDecimal(96, p.getP88());
                                            preparedStatement.setBigDecimal(97, p.getP89());
                                            preparedStatement.setBigDecimal(98, p.getP90());
                                            preparedStatement.setBigDecimal(99, p.getP91());
                                            preparedStatement.setBigDecimal(100, p.getP92());
                                            preparedStatement.setBigDecimal(101, p.getP93());
                                            preparedStatement.setBigDecimal(102, p.getP94());
                                            preparedStatement.setBigDecimal(103, p.getP95());
                                            preparedStatement.setBigDecimal(104, p.getP96());
                                            preparedStatement.setInt(105, 4);
                                            preparedStatement.setString(106, p.getEventId());
                                            preparedStatement.setDate(107, new java.sql.Date(System.currentTimeMillis()));
                                            preparedStatement.setDate(108, new java.sql.Date(System.currentTimeMillis()));
                                            preparedStatement.setDate(109, new java.sql.Date(System.currentTimeMillis()));

                                            preparedStatement.executeUpdate();

                                            String curveId2 = orgId + "_103_207_" + count + pDate.format(dateTimeFormatter);
                                            curveIds3.add(curveId2);
                                            preparedStatement2.setString(1, curveId2);
                                            preparedStatement2.setString(2, orgId.toString());
                                            preparedStatement2.setString(3, "207_" + count);
                                            preparedStatement2.setString(4, "103");
                                            preparedStatement2.setDate(5, new java.sql.Date(System.currentTimeMillis()));
                                            preparedStatement2.setString(6, p.getEventId());
                                            preparedStatement2.setDate(7, new java.sql.Date(System.currentTimeMillis()));
                                            preparedStatement2.setDate(8, new java.sql.Date(System.currentTimeMillis()));
                                            preparedStatement2.setDate(9, new java.sql.Date(System.currentTimeMillis()));

                                            preparedStatement2.executeUpdate();
                                        }
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }

                                } else {
                                    for (String curve : curveIds2) {
                                        String sql = "update exch_rt_curve_data_org set point1=?, point2=?,point3=?,point4=?, point5=?,point6=?,point7=?, point8=?,point9=?,point10=?," +
                                                "point11=?, point12=?,point13=?,point14=?, point15=?,point16=?,point17=?, point18=?,point19=?,point20=?," +
                                                "point21=?, point22=?,point23=?,point24=?, point25=?,point26=?,point27=?, point28=?,point29=?,point30=?," +
                                                "point31=?, point32=?,point33=?,point34=?, point35=?,point36=?,point37=?, point38=?,point39=?,point40=?," +
                                                "point41=?, point42=?,point43=?,point44=?, point45=?,point46=?,point47=?, point48=?,point49=?,point50=?," +
                                                "point51=?, point52=?,point53=?,point54=?, point55=?,point56=?,point57=?, point58=?,point59=?,point60=?," +
                                                "point61=?, point62=?,point63=?,point64=?, point65=?,point66=?,point67=?, point68=?,point69=?,point70=?," +
                                                "point71=?, point72=?,point73=?,point74=?, point75=?,point76=?,point77=?, point78=?,point79=?,point80=?," +
                                                "point81=?, point82=?,point83=?,point84=?, point85=?,point86=?,point87=?, point88=?,point89=?,point90=?," +
                                                "point91=?, point92=?,point93=?,point94=?, point95=?,point96=? where curve_id = ? and curve_org_no=?";
                                        try {
                                            preparedStatement = connection.prepareStatement(sql);
                                            preparedStatement.setBigDecimal(1, p.getP1());
                                            preparedStatement.setBigDecimal(2, p.getP2());
                                            preparedStatement.setBigDecimal(3, p.getP3());
                                            preparedStatement.setBigDecimal(4, p.getP4());
                                            preparedStatement.setBigDecimal(5, p.getP5());
                                            preparedStatement.setBigDecimal(6, p.getP6());
                                            preparedStatement.setBigDecimal(7, p.getP7());
                                            preparedStatement.setBigDecimal(8, p.getP8());
                                            preparedStatement.setBigDecimal(9, p.getP9());
                                            preparedStatement.setBigDecimal(10, p.getP10());
                                            preparedStatement.setBigDecimal(11, p.getP11());
                                            preparedStatement.setBigDecimal(12, p.getP12());
                                            preparedStatement.setBigDecimal(13, p.getP13());
                                            preparedStatement.setBigDecimal(14, p.getP14());
                                            preparedStatement.setBigDecimal(15, p.getP15());
                                            preparedStatement.setBigDecimal(16, p.getP16());
                                            preparedStatement.setBigDecimal(17, p.getP17());
                                            preparedStatement.setBigDecimal(18, p.getP18());
                                            preparedStatement.setBigDecimal(19, p.getP19());
                                            preparedStatement.setBigDecimal(20, p.getP20());
                                            preparedStatement.setBigDecimal(21, p.getP21());
                                            preparedStatement.setBigDecimal(22, p.getP22());
                                            preparedStatement.setBigDecimal(23, p.getP23());
                                            preparedStatement.setBigDecimal(24, p.getP24());
                                            preparedStatement.setBigDecimal(25, p.getP25());
                                            preparedStatement.setBigDecimal(26, p.getP26());
                                            preparedStatement.setBigDecimal(27, p.getP27());
                                            preparedStatement.setBigDecimal(28, p.getP28());
                                            preparedStatement.setBigDecimal(29, p.getP29());
                                            preparedStatement.setBigDecimal(30, p.getP30());
                                            preparedStatement.setBigDecimal(31, p.getP31());
                                            preparedStatement.setBigDecimal(32, p.getP32());
                                            preparedStatement.setBigDecimal(33, p.getP33());
                                            preparedStatement.setBigDecimal(34, p.getP34());
                                            preparedStatement.setBigDecimal(35, p.getP35());
                                            preparedStatement.setBigDecimal(36, p.getP36());
                                            preparedStatement.setBigDecimal(37, p.getP37());
                                            preparedStatement.setBigDecimal(38, p.getP38());
                                            preparedStatement.setBigDecimal(39, p.getP39());
                                            preparedStatement.setBigDecimal(40, p.getP40());
                                            preparedStatement.setBigDecimal(41, p.getP41());
                                            preparedStatement.setBigDecimal(42, p.getP42());
                                            preparedStatement.setBigDecimal(43, p.getP43());
                                            preparedStatement.setBigDecimal(44, p.getP44());
                                            preparedStatement.setBigDecimal(45, p.getP45());
                                            preparedStatement.setBigDecimal(46, p.getP46());
                                            preparedStatement.setBigDecimal(47, p.getP47());
                                            preparedStatement.setBigDecimal(48, p.getP48());
                                            preparedStatement.setBigDecimal(49, p.getP49());
                                            preparedStatement.setBigDecimal(50, p.getP50());
                                            preparedStatement.setBigDecimal(51, p.getP51());
                                            preparedStatement.setBigDecimal(52, p.getP52());
                                            preparedStatement.setBigDecimal(53, p.getP53());
                                            preparedStatement.setBigDecimal(54, p.getP54());
                                            preparedStatement.setBigDecimal(55, p.getP55());
                                            preparedStatement.setBigDecimal(56, p.getP56());
                                            preparedStatement.setBigDecimal(57, p.getP57());
                                            preparedStatement.setBigDecimal(58, p.getP58());
                                            preparedStatement.setBigDecimal(59, p.getP59());
                                            preparedStatement.setBigDecimal(60, p.getP60());
                                            preparedStatement.setBigDecimal(61, p.getP61());
                                            preparedStatement.setBigDecimal(62, p.getP62());
                                            preparedStatement.setBigDecimal(63, p.getP63());
                                            preparedStatement.setBigDecimal(64, p.getP64());
                                            preparedStatement.setBigDecimal(65, p.getP65());
                                            preparedStatement.setBigDecimal(66, p.getP66());
                                            preparedStatement.setBigDecimal(67, p.getP67());
                                            preparedStatement.setBigDecimal(68, p.getP68());
                                            preparedStatement.setBigDecimal(69, p.getP69());
                                            preparedStatement.setBigDecimal(70, p.getP70());
                                            preparedStatement.setBigDecimal(71, p.getP71());
                                            preparedStatement.setBigDecimal(72, p.getP72());
                                            preparedStatement.setBigDecimal(73, p.getP73());
                                            preparedStatement.setBigDecimal(74, p.getP74());
                                            preparedStatement.setBigDecimal(75, p.getP75());
                                            preparedStatement.setBigDecimal(76, p.getP76());
                                            preparedStatement.setBigDecimal(77, p.getP77());
                                            preparedStatement.setBigDecimal(78, p.getP78());
                                            preparedStatement.setBigDecimal(79, p.getP79());
                                            preparedStatement.setBigDecimal(80, p.getP80());
                                            preparedStatement.setBigDecimal(81, p.getP81());
                                            preparedStatement.setBigDecimal(82, p.getP82());
                                            preparedStatement.setBigDecimal(83, p.getP83());
                                            preparedStatement.setBigDecimal(84, p.getP84());
                                            preparedStatement.setBigDecimal(85, p.getP85());
                                            preparedStatement.setBigDecimal(86, p.getP86());
                                            preparedStatement.setBigDecimal(87, p.getP87());
                                            preparedStatement.setBigDecimal(88, p.getP88());
                                            preparedStatement.setBigDecimal(89, p.getP89());
                                            preparedStatement.setBigDecimal(90, p.getP90());
                                            preparedStatement.setBigDecimal(91, p.getP91());
                                            preparedStatement.setBigDecimal(92, p.getP92());
                                            preparedStatement.setBigDecimal(93, p.getP93());
                                            preparedStatement.setBigDecimal(94, p.getP94());
                                            preparedStatement.setBigDecimal(95, p.getP95());
                                            preparedStatement.setBigDecimal(96, p.getP96());
                                            preparedStatement.setString(97, curve);
                                            preparedStatement.setString(98, orgId.toString());
                                            preparedStatement.executeUpdate();
                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (preparedStatement != null) {
                    try {
                        preparedStatement.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                if (preparedStatement2 != null) {
                    try {
                        preparedStatement2.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            Connection connection = getConnection();
            Connection connection2 = getConnection();
            PreparedStatement preparedStatement = null;
            PreparedStatement preparedStatement2 = null;
            List orgIds = getEventIDSByDate().get("orgIds");

            try {
                for (Object orgId : orgIds) {
                    if (getProvinceCode().equals(orgId.toString())) {
                        List<Point96Vo> realTimeData = this.getRealTimeData(orgId.toString());
                        if (realTimeData == null || realTimeData.size() <= 0) {
                            continue;
                        }
                        int count = 0;
                        for (Point96Vo p : realTimeData) {
                            count++;
                            LocalDate localDate = LocalDate.now();
                            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
                            String curveIdFlag = orgId + "_102_207_" + count + localDate.format(dateTimeFormatter);
                            if (curveIds2 == null || curveIds2.size() <= 0 || !curveIds2.contains(curveIdFlag)) {
                                String sql = "insert into exch_rt_curve_data_org VALUES(" +
                                        "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?" +
                                        ",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
                                        "?,?,?,?,?,?,?,?,?,?" +
                                        ",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
                                        "?,?,?,?,?,?,?,?,?,?,?,?,?)";
                                String sql2 = "insert into exch_rt_curve_data_org(curve_id,curve_org_no,curve_tgt_type,curve_type,data_date,extend_value,create_time,update_time,upload_time) " +
                                        "VALUES(?,?,?,?,?,?,?,?,?)";
                                try {
                                    preparedStatement = connection.prepareStatement(sql);
                                    preparedStatement2 = connection2.prepareStatement(sql2);
                                    String provinceCode = this.getProvinceCode();
                                    if (orgId.toString().equals(provinceCode)) {
                                        String curveId = provinceCode + "_102_207_" + count + localDate.format(dateTimeFormatter);
                                        curveIds2.add(curveId);
                                        preparedStatement.setString(1, curveId);
                                        preparedStatement.setString(2, orgId.toString());
                                        preparedStatement.setString(3, "00000");
                                        preparedStatement.setString(4, "");
                                        preparedStatement.setString(5, "207_" + count);
                                        preparedStatement.setString(6, "102");
                                        preparedStatement.setString(7, "1");
                                        preparedStatement.setDate(8, new java.sql.Date(System.currentTimeMillis()));
                                        preparedStatement.setBigDecimal(9, p.getP1());
                                        preparedStatement.setBigDecimal(10, p.getP2());
                                        preparedStatement.setBigDecimal(11, p.getP3());
                                        preparedStatement.setBigDecimal(12, p.getP4());
                                        preparedStatement.setBigDecimal(13, p.getP5());
                                        preparedStatement.setBigDecimal(14, p.getP6());
                                        preparedStatement.setBigDecimal(15, p.getP7());
                                        preparedStatement.setBigDecimal(16, p.getP8());
                                        preparedStatement.setBigDecimal(17, p.getP9());
                                        preparedStatement.setBigDecimal(18, p.getP10());
                                        preparedStatement.setBigDecimal(19, p.getP11());
                                        preparedStatement.setBigDecimal(20, p.getP12());
                                        preparedStatement.setBigDecimal(21, p.getP13());
                                        preparedStatement.setBigDecimal(22, p.getP14());
                                        preparedStatement.setBigDecimal(23, p.getP15());
                                        preparedStatement.setBigDecimal(24, p.getP16());
                                        preparedStatement.setBigDecimal(25, p.getP17());
                                        preparedStatement.setBigDecimal(26, p.getP18());
                                        preparedStatement.setBigDecimal(27, p.getP19());
                                        preparedStatement.setBigDecimal(28, p.getP20());
                                        preparedStatement.setBigDecimal(29, p.getP21());
                                        preparedStatement.setBigDecimal(30, p.getP22());
                                        preparedStatement.setBigDecimal(31, p.getP23());
                                        preparedStatement.setBigDecimal(32, p.getP24());
                                        preparedStatement.setBigDecimal(33, p.getP25());
                                        preparedStatement.setBigDecimal(34, p.getP26());
                                        preparedStatement.setBigDecimal(35, p.getP27());
                                        preparedStatement.setBigDecimal(36, p.getP28());
                                        preparedStatement.setBigDecimal(37, p.getP29());
                                        preparedStatement.setBigDecimal(38, p.getP30());
                                        preparedStatement.setBigDecimal(39, p.getP31());
                                        preparedStatement.setBigDecimal(40, p.getP32());
                                        preparedStatement.setBigDecimal(41, p.getP33());
                                        preparedStatement.setBigDecimal(42, p.getP34());
                                        preparedStatement.setBigDecimal(43, p.getP35());
                                        preparedStatement.setBigDecimal(44, p.getP36());
                                        preparedStatement.setBigDecimal(45, p.getP37());
                                        preparedStatement.setBigDecimal(46, p.getP38());
                                        preparedStatement.setBigDecimal(47, p.getP39());
                                        preparedStatement.setBigDecimal(48, p.getP40());
                                        preparedStatement.setBigDecimal(49, p.getP41());
                                        preparedStatement.setBigDecimal(50, p.getP42());
                                        preparedStatement.setBigDecimal(51, p.getP43());
                                        preparedStatement.setBigDecimal(52, p.getP44());
                                        preparedStatement.setBigDecimal(53, p.getP45());
                                        preparedStatement.setBigDecimal(54, p.getP46());
                                        preparedStatement.setBigDecimal(55, p.getP47());
                                        preparedStatement.setBigDecimal(56, p.getP48());
                                        preparedStatement.setBigDecimal(57, p.getP49());
                                        preparedStatement.setBigDecimal(58, p.getP50());
                                        preparedStatement.setBigDecimal(59, p.getP51());
                                        preparedStatement.setBigDecimal(60, p.getP52());
                                        preparedStatement.setBigDecimal(61, p.getP53());
                                        preparedStatement.setBigDecimal(62, p.getP54());
                                        preparedStatement.setBigDecimal(63, p.getP55());
                                        preparedStatement.setBigDecimal(64, p.getP56());
                                        preparedStatement.setBigDecimal(65, p.getP57());
                                        preparedStatement.setBigDecimal(66, p.getP58());
                                        preparedStatement.setBigDecimal(67, p.getP59());
                                        preparedStatement.setBigDecimal(68, p.getP60());
                                        preparedStatement.setBigDecimal(69, p.getP61());
                                        preparedStatement.setBigDecimal(70, p.getP62());
                                        preparedStatement.setBigDecimal(71, p.getP63());
                                        preparedStatement.setBigDecimal(72, p.getP64());
                                        preparedStatement.setBigDecimal(73, p.getP65());
                                        preparedStatement.setBigDecimal(74, p.getP66());
                                        preparedStatement.setBigDecimal(75, p.getP67());
                                        preparedStatement.setBigDecimal(76, p.getP68());
                                        preparedStatement.setBigDecimal(77, p.getP69());
                                        preparedStatement.setBigDecimal(78, p.getP70());
                                        preparedStatement.setBigDecimal(79, p.getP71());
                                        preparedStatement.setBigDecimal(80, p.getP72());
                                        preparedStatement.setBigDecimal(81, p.getP73());
                                        preparedStatement.setBigDecimal(82, p.getP74());
                                        preparedStatement.setBigDecimal(83, p.getP75());
                                        preparedStatement.setBigDecimal(84, p.getP76());
                                        preparedStatement.setBigDecimal(85, p.getP77());
                                        preparedStatement.setBigDecimal(86, p.getP78());
                                        preparedStatement.setBigDecimal(87, p.getP79());
                                        preparedStatement.setBigDecimal(88, p.getP80());
                                        preparedStatement.setBigDecimal(89, p.getP81());
                                        preparedStatement.setBigDecimal(90, p.getP82());
                                        preparedStatement.setBigDecimal(91, p.getP83());
                                        preparedStatement.setBigDecimal(92, p.getP84());
                                        preparedStatement.setBigDecimal(93, p.getP85());
                                        preparedStatement.setBigDecimal(94, p.getP86());
                                        preparedStatement.setBigDecimal(95, p.getP87());
                                        preparedStatement.setBigDecimal(96, p.getP88());
                                        preparedStatement.setBigDecimal(97, p.getP89());
                                        preparedStatement.setBigDecimal(98, p.getP90());
                                        preparedStatement.setBigDecimal(99, p.getP91());
                                        preparedStatement.setBigDecimal(100, p.getP92());
                                        preparedStatement.setBigDecimal(101, p.getP93());
                                        preparedStatement.setBigDecimal(102, p.getP94());
                                        preparedStatement.setBigDecimal(103, p.getP95());
                                        preparedStatement.setBigDecimal(104, p.getP96());
                                        preparedStatement.setInt(105, 4);
                                        preparedStatement.setString(106, p.getEventId());
                                        preparedStatement.setDate(107, new java.sql.Date(System.currentTimeMillis()));
                                        preparedStatement.setDate(108, new java.sql.Date(System.currentTimeMillis()));
                                        preparedStatement.setDate(109, new java.sql.Date(System.currentTimeMillis()));

                                        try {
                                            preparedStatement.executeUpdate();
                                        } catch (Exception e) {
                                            log.info("实时数据记录插入失败");
                                            String curveId2 = provinceCode + "_103_207_" + count + localDate.format(dateTimeFormatter);
                                            curveIds3.add(curveId2);
                                            preparedStatement2.setString(1, curveId2);
                                            preparedStatement2.setString(2, orgId.toString());
                                            preparedStatement2.setString(3, "207_" + count);
                                            preparedStatement2.setString(4, "103");
                                            preparedStatement2.setDate(5, new java.sql.Date(System.currentTimeMillis()));
                                            preparedStatement2.setString(6, p.getEventId());
                                            preparedStatement2.setDate(7, new java.sql.Date(System.currentTimeMillis()));
                                            preparedStatement2.setDate(8, new java.sql.Date(System.currentTimeMillis()));
                                            preparedStatement2.setDate(9, new java.sql.Date(System.currentTimeMillis()));
                                            try {
                                                preparedStatement2.executeUpdate();
                                            } catch (SQLException ex) {
                                            }
                                        }
                                        log.info("实时数据记录插入成功,id为:" + curveId);
                                        String curveId2 = provinceCode + "_103_207_" + count + localDate.format(dateTimeFormatter);
                                        curveIds3.add(curveId2);
                                        preparedStatement2.setString(1, curveId2);
                                        preparedStatement2.setString(2, orgId.toString());
                                        preparedStatement2.setString(3, "207_" + count);
                                        preparedStatement2.setString(4, "103");
                                        preparedStatement2.setDate(5, new java.sql.Date(System.currentTimeMillis()));
                                        preparedStatement2.setString(6, p.getEventId());
                                        preparedStatement2.setDate(7, new java.sql.Date(System.currentTimeMillis()));
                                        preparedStatement2.setDate(8, new java.sql.Date(System.currentTimeMillis()));
                                        preparedStatement2.setDate(9, new java.sql.Date(System.currentTimeMillis()));
                                        preparedStatement2.executeUpdate();
                                        log.info("压降数据记录插入成功,id为:" + curveId2);
                                    } else {
                                        String curveId = orgId + "_102_207_" + count + localDate.format(dateTimeFormatter);
                                        curveIds2.add(curveId);
                                        preparedStatement.setString(1, curveId);
                                        preparedStatement.setString(2, orgId.toString());
                                        preparedStatement.setString(3, orgId.toString());
                                        preparedStatement.setString(4, "");
                                        preparedStatement.setString(5, "207_" + count);
                                        preparedStatement.setString(6, "102");
                                        preparedStatement.setString(7, "1");
                                        preparedStatement.setDate(8, new java.sql.Date(System.currentTimeMillis()));
                                        preparedStatement.setBigDecimal(9, p.getP1());
                                        preparedStatement.setBigDecimal(10, p.getP2());
                                        preparedStatement.setBigDecimal(11, p.getP3());
                                        preparedStatement.setBigDecimal(12, p.getP4());
                                        preparedStatement.setBigDecimal(13, p.getP5());
                                        preparedStatement.setBigDecimal(14, p.getP6());
                                        preparedStatement.setBigDecimal(15, p.getP7());
                                        preparedStatement.setBigDecimal(16, p.getP8());
                                        preparedStatement.setBigDecimal(17, p.getP9());
                                        preparedStatement.setBigDecimal(18, p.getP10());
                                        preparedStatement.setBigDecimal(19, p.getP11());
                                        preparedStatement.setBigDecimal(20, p.getP12());
                                        preparedStatement.setBigDecimal(21, p.getP13());
                                        preparedStatement.setBigDecimal(22, p.getP14());
                                        preparedStatement.setBigDecimal(23, p.getP15());
                                        preparedStatement.setBigDecimal(24, p.getP16());
                                        preparedStatement.setBigDecimal(25, p.getP17());
                                        preparedStatement.setBigDecimal(26, p.getP18());
                                        preparedStatement.setBigDecimal(27, p.getP19());
                                        preparedStatement.setBigDecimal(28, p.getP20());
                                        preparedStatement.setBigDecimal(29, p.getP21());
                                        preparedStatement.setBigDecimal(30, p.getP22());
                                        preparedStatement.setBigDecimal(31, p.getP23());
                                        preparedStatement.setBigDecimal(32, p.getP24());
                                        preparedStatement.setBigDecimal(33, p.getP25());
                                        preparedStatement.setBigDecimal(34, p.getP26());
                                        preparedStatement.setBigDecimal(35, p.getP27());
                                        preparedStatement.setBigDecimal(36, p.getP28());
                                        preparedStatement.setBigDecimal(37, p.getP29());
                                        preparedStatement.setBigDecimal(38, p.getP30());
                                        preparedStatement.setBigDecimal(39, p.getP31());
                                        preparedStatement.setBigDecimal(40, p.getP32());
                                        preparedStatement.setBigDecimal(41, p.getP33());
                                        preparedStatement.setBigDecimal(42, p.getP34());
                                        preparedStatement.setBigDecimal(43, p.getP35());
                                        preparedStatement.setBigDecimal(44, p.getP36());
                                        preparedStatement.setBigDecimal(45, p.getP37());
                                        preparedStatement.setBigDecimal(46, p.getP38());
                                        preparedStatement.setBigDecimal(47, p.getP39());
                                        preparedStatement.setBigDecimal(48, p.getP40());
                                        preparedStatement.setBigDecimal(49, p.getP41());
                                        preparedStatement.setBigDecimal(50, p.getP42());
                                        preparedStatement.setBigDecimal(51, p.getP43());
                                        preparedStatement.setBigDecimal(52, p.getP44());
                                        preparedStatement.setBigDecimal(53, p.getP45());
                                        preparedStatement.setBigDecimal(54, p.getP46());
                                        preparedStatement.setBigDecimal(55, p.getP47());
                                        preparedStatement.setBigDecimal(56, p.getP48());
                                        preparedStatement.setBigDecimal(57, p.getP49());
                                        preparedStatement.setBigDecimal(58, p.getP50());
                                        preparedStatement.setBigDecimal(59, p.getP51());
                                        preparedStatement.setBigDecimal(60, p.getP52());
                                        preparedStatement.setBigDecimal(61, p.getP53());
                                        preparedStatement.setBigDecimal(62, p.getP54());
                                        preparedStatement.setBigDecimal(63, p.getP55());
                                        preparedStatement.setBigDecimal(64, p.getP56());
                                        preparedStatement.setBigDecimal(65, p.getP57());
                                        preparedStatement.setBigDecimal(66, p.getP58());
                                        preparedStatement.setBigDecimal(67, p.getP59());
                                        preparedStatement.setBigDecimal(68, p.getP60());
                                        preparedStatement.setBigDecimal(69, p.getP61());
                                        preparedStatement.setBigDecimal(70, p.getP62());
                                        preparedStatement.setBigDecimal(71, p.getP63());
                                        preparedStatement.setBigDecimal(72, p.getP64());
                                        preparedStatement.setBigDecimal(73, p.getP65());
                                        preparedStatement.setBigDecimal(74, p.getP66());
                                        preparedStatement.setBigDecimal(75, p.getP67());
                                        preparedStatement.setBigDecimal(76, p.getP68());
                                        preparedStatement.setBigDecimal(77, p.getP69());
                                        preparedStatement.setBigDecimal(78, p.getP70());
                                        preparedStatement.setBigDecimal(79, p.getP71());
                                        preparedStatement.setBigDecimal(80, p.getP72());
                                        preparedStatement.setBigDecimal(81, p.getP73());
                                        preparedStatement.setBigDecimal(82, p.getP74());
                                        preparedStatement.setBigDecimal(83, p.getP75());
                                        preparedStatement.setBigDecimal(84, p.getP76());
                                        preparedStatement.setBigDecimal(85, p.getP77());
                                        preparedStatement.setBigDecimal(86, p.getP78());
                                        preparedStatement.setBigDecimal(87, p.getP79());
                                        preparedStatement.setBigDecimal(88, p.getP80());
                                        preparedStatement.setBigDecimal(89, p.getP81());
                                        preparedStatement.setBigDecimal(90, p.getP82());
                                        preparedStatement.setBigDecimal(91, p.getP83());
                                        preparedStatement.setBigDecimal(92, p.getP84());
                                        preparedStatement.setBigDecimal(93, p.getP85());
                                        preparedStatement.setBigDecimal(94, p.getP86());
                                        preparedStatement.setBigDecimal(95, p.getP87());
                                        preparedStatement.setBigDecimal(96, p.getP88());
                                        preparedStatement.setBigDecimal(97, p.getP89());
                                        preparedStatement.setBigDecimal(98, p.getP90());
                                        preparedStatement.setBigDecimal(99, p.getP91());
                                        preparedStatement.setBigDecimal(100, p.getP92());
                                        preparedStatement.setBigDecimal(101, p.getP93());
                                        preparedStatement.setBigDecimal(102, p.getP94());
                                        preparedStatement.setBigDecimal(103, p.getP95());
                                        preparedStatement.setBigDecimal(104, p.getP96());
                                        preparedStatement.setInt(105, 4);
                                        preparedStatement.setString(106, p.getEventId());
                                        preparedStatement.setDate(107, new java.sql.Date(System.currentTimeMillis()));
                                        preparedStatement.setDate(108, new java.sql.Date(System.currentTimeMillis()));
                                        preparedStatement.setDate(109, new java.sql.Date(System.currentTimeMillis()));

                                        try {
                                            preparedStatement.executeUpdate();
                                        } catch (Exception e) {
                                            log.info("实时数据记录插入失败");
                                            String curveId2 = orgId + "_103_207_" + count + localDate.format(dateTimeFormatter);
                                            curveIds3.add(curveId2);
                                            preparedStatement2.setString(1, curveId2);
                                            preparedStatement2.setString(2, orgId.toString());
                                            preparedStatement2.setString(3, "207_" + count);
                                            preparedStatement2.setString(4, "103");
                                            preparedStatement2.setDate(5, new java.sql.Date(System.currentTimeMillis()));
                                            preparedStatement2.setString(6, p.getEventId());
                                            preparedStatement2.setDate(7, new java.sql.Date(System.currentTimeMillis()));
                                            preparedStatement2.setDate(8, new java.sql.Date(System.currentTimeMillis()));
                                            preparedStatement2.setDate(9, new java.sql.Date(System.currentTimeMillis()));
                                            preparedStatement2.executeUpdate();
                                        }
                                        log.info("实时数据记录插入成功,id为:" + curveId);
                                        String curveId2 = orgId + "_103_207_" + count + localDate.format(dateTimeFormatter);
                                        curveIds3.add(curveId2);
                                        preparedStatement2.setString(1, curveId2);
                                        preparedStatement2.setString(2, orgId.toString());
                                        preparedStatement2.setString(3, "207_" + count);
                                        preparedStatement2.setString(4, "103");
                                        preparedStatement2.setDate(5, new java.sql.Date(System.currentTimeMillis()));
                                        preparedStatement2.setString(6, p.getEventId());
                                        preparedStatement2.setDate(7, new java.sql.Date(System.currentTimeMillis()));
                                        preparedStatement2.setDate(8, new java.sql.Date(System.currentTimeMillis()));
                                        preparedStatement2.setDate(9, new java.sql.Date(System.currentTimeMillis()));

                                        preparedStatement2.executeUpdate();
                                        log.info("压降数据记录插入成功,id为:" + curveId2);
                                    }
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                for (String curve : curveIds2) {
                                    String sql = "update exch_rt_curve_data_org set point1=?, point2=?,point3=?,point4=?, point5=?,point6=?,point7=?, point8=?,point9=?,point10=?," +
                                            "point11=?, point12=?,point13=?,point14=?, point15=?,point16=?,point17=?, point18=?,point19=?,point20=?," +
                                            "point21=?, point22=?,point23=?,point24=?, point25=?,point26=?,point27=?, point28=?,point29=?,point30=?," +
                                            "point31=?, point32=?,point33=?,point34=?, point35=?,point36=?,point37=?, point38=?,point39=?,point40=?," +
                                            "point41=?, point42=?,point43=?,point44=?, point45=?,point46=?,point47=?, point48=?,point49=?,point50=?," +
                                            "point51=?, point52=?,point53=?,point54=?, point55=?,point56=?,point57=?, point58=?,point59=?,point60=?," +
                                            "point61=?, point62=?,point63=?,point64=?, point65=?,point66=?,point67=?, point68=?,point69=?,point70=?," +
                                            "point71=?, point72=?,point73=?,point74=?, point75=?,point76=?,point77=?, point78=?,point79=?,point80=?," +
                                            "point81=?, point82=?,point83=?,point84=?, point85=?,point86=?,point87=?, point88=?,point89=?,point90=?," +
                                            "point91=?, point92=?,point93=?,point94=?, point95=?,point96=? where curve_id = ? and curve_org_no=?";
                                    try {
                                        JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(p));
                                        LocalTime localTime = LocalTime.MIN;
                                        LocalTime nowTime = LocalTime.now();
                                        for (int j = 2; j <= 96; j++) {
                                            if (localTime.plusMinutes((j - 1) * 15).isAfter(nowTime)) {
                                                jsonObject.put("p" + j, null);
                                            } else {
                                                BigDecimal value1 = jsonObject.getBigDecimal("p" + (j - 1));
                                                BigDecimal value2 = jsonObject.getBigDecimal("p" + j);
                                                if (value1 == null) {
                                                    value1 = BigDecimal.ZERO;
                                                    jsonObject.put("p" + (j - 1), value1);
                                                }
                                                if (value2 == null) {
                                                    value2 = value1.add(BigDecimal.ZERO);
                                                }
                                                if (value1.subtract(value2).compareTo(BigDecimal.valueOf(100)) > 0) {
                                                    value2 = value1;
                                                }
                                                jsonObject.put("p" + j, value2);
                                            }
                                        }
                                        preparedStatement = connection.prepareStatement(sql);
                                        for (int j = 1; j <= 96; j++) {
                                            preparedStatement.setBigDecimal(j, jsonObject.getBigDecimal("p" + j));
                                        }
                                        preparedStatement.setString(97, curve);
                                        preparedStatement.setString(98, orgId.toString());
                                        preparedStatement.executeUpdate();
                                        log.info("实时数据更新成功,id:" + curve);
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (preparedStatement != null) {
                    try {
                        preparedStatement.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                if (preparedStatement2 != null) {
                    try {
                        preparedStatement2.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                if (connection2 != null) {
                    try {
                        connection2.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    private void saveDepressToDatabase(String param) {
        if (StringUtils.isNotBlank(param)) {
            Connection connection = getConnection();
            List orgIds = getEventIDSByDate().get("orgIds");
            String sql3 = "";
            List<Object[]> args = new ArrayList<>();
            PreparedStatement preparedStatement = null;
            LocalDate pDate = null;
            try {
                String[] strings = param.split("-");
                pDate = LocalDate.of(Integer.parseInt(strings[0]), Integer.parseInt(strings[1]), Integer.parseInt(strings[2]));
                List<Long> eventIds = baselineAndMeasurementMapper.getEventId(pDate);
                for (Object orgId : orgIds) {
                    if (getProvinceCode().equals(orgId.toString())) {
                        LocalDate now = LocalDate.now();
                        LocalDateTime localDateTime = LocalDateTime.now();
//                    int hour = localDateTime.getHour();
//                    int minute = localDateTime.getMinute();
//                    String time = String.valueOf(hour) + ":" + minute;
                        List<DrEventTime> eventTime = baselineAndMeasurementMapper.getEventTime(pDate, eventIds);
                        int i = CurveUtil.covDateTimeToPoint(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
                        List<ExchPoint96Vo> staticData = new ArrayList<>();
                        List<ExchPoint96Vo> rtData = new ArrayList<>();
                        int startPoint = 0;
                        int endPoint = 0;
                        int a = 0;
                        if (eventTime.size() > 0 && eventTime != null) {
                            for (DrEventTime drEventTime : eventTime) {
                                a++;
                                startPoint = CurveUtil.covDateTimeToPoint(drEventTime.getStartTime());
                                endPoint = CurveUtil.covDateTimeToPoint(drEventTime.getEndTime());

                                if (startPoint <= i) {
                                    StringBuffer sb = new StringBuffer("update exch_rt_curve_data_org set ");
                                    sb.append("point" + String.valueOf(startPoint) + "=? ");
                                    for (int j = 1; j <= (endPoint - startPoint); j++) {
                                        sb.append(",");
                                        sb.append("point" + String.valueOf(startPoint + j) + "=? ");
                                    }
                                    sb.append("where curve_id=?");
                                    String sql = sb.toString();
                                    if (getProvinceCode().equals(orgId.toString())) {
                                        List<Point96Vo> baseLine = this.getBaseLineByParamTimeAndEventId(drEventTime.getEventId(), pDate);
                                        List<Point96Vo> realTimeData = this.getRealTimeDataByParamTimeAndEventId(orgId.toString(), drEventTime.getEventId());
                                        int j = -1;
                                        Map<Integer, BigDecimal> map = new HashMap();
                                        for (Point96Vo p : baseLine) {
                                            j++;
                                            for (int k = 1; k <= 96; k++) {
                                                BigDecimal fieldValue = (BigDecimal) ReflectUtil.getFieldValue(p, "P" + k);
                                                BigDecimal fieldValue2 = (BigDecimal) ReflectUtil.getFieldValue(realTimeData.get(j), "P" + k);
                                                if (ObjectUtil.isNotEmpty(fieldValue) && ObjectUtil.isNotEmpty(fieldValue2)) {
                                                    map.put(k, fieldValue.subtract(fieldValue2));
                                                }
                                            }
                                        }

                                        try {
                                            preparedStatement = connection.prepareStatement(sql);
                                            preparedStatement.setBigDecimal(1, map.get(startPoint));
                                            int count = 1;
                                            for (int h = 1; h <= (endPoint - startPoint); h++) {
                                                preparedStatement.setBigDecimal(h + 1, map.get(startPoint + h));
                                                count++;
                                            }
                                            LocalDate localDate = LocalDate.now();
                                            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
                                            String curvid = orgId + "_103_207_" + a + localDate.format(dateTimeFormatter);
                                            preparedStatement.setString(count + 1, curvid);

                                            preparedStatement.executeUpdate();

                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }
                            }

                        }

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (preparedStatement != null) {
                    try {
                        preparedStatement.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            Connection connection = getConnection();
            List orgIds = getEventIDSByDate().get("orgIds");
//            List eventIds = getEventIDSByDate().get("eventIds");
            LocalDate loc = LocalDate.now();
            List<Long> eventIds = baselineAndMeasurementMapper.getEventId2(loc);
            String sql3 = "";
            List<Object[]> args = new ArrayList<>();
            PreparedStatement preparedStatement = null;
            try {
                for (Object orgId : orgIds) {
                    if (getProvinceCode().equals(orgId.toString())) {
                        LocalDate now = LocalDate.now();
//                        LocalDateTime localDateTime = LocalDateTime.now();
//                        int hour = localDateTime.getHour();
//                        int minute = localDateTime.getMinute();
//                        String time = String.valueOf(hour) + ":" + minute;
                        if (eventIds != null && eventIds.size() > 0) {
                            List<DrEventTime> eventTime = baselineAndMeasurementMapper.getEventTime(now, eventIds);
                            int i = CurveUtil.covDateTimeToPoint(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
                            List<ExchPoint96Vo> staticData = new ArrayList<>();
                            List<ExchPoint96Vo> rtData = new ArrayList<>();
                            int startPoint = 0;
                            int endPoint = 0;
                            int a = 0;
                            if (eventTime.size() > 0 && eventTime != null) {
                                for (DrEventTime drEventTime : eventTime) {
                                    a++;
                                    startPoint = CurveUtil.covDateTimeToPoint(drEventTime.getStartTime());
                                    endPoint = CurveUtil.covDateTimeToPoint(drEventTime.getEndTime());

                                    if (startPoint <= i) {
                                        StringBuffer sb = new StringBuffer("update exch_rt_curve_data_org set ");
                                        sb.append("point" + String.valueOf(startPoint) + "=? ");
                                        for (int j = 1; j <= (endPoint - startPoint); j++) {
                                            sb.append(",");
                                            sb.append("point" + String.valueOf(startPoint + j) + "=? ");
                                        }
                                        sb.append("where curve_id=?");
                                        String sql = sb.toString();
                                        if (getProvinceCode().equals(orgId.toString())) {
                                            List<Point96Vo> baseLine = this.getBaseLineByParamTimeAndEventId(drEventTime.getEventId(), now);
                                            List<Point96Vo> realTimeData = this.getRealTimeDataByParamTimeAndEventId(orgId.toString(), drEventTime.getEventId());

                                            int j = -1;
                                            Map<Integer, BigDecimal> map = new HashMap();
                                            for (Point96Vo p : baseLine) {
                                                j++;
                                                for (int k = 1; k <= 96; k++) {
                                                    BigDecimal fieldValue = (BigDecimal) ReflectUtil.getFieldValue(p, "P" + k);
                                                    BigDecimal fieldValue2 = (BigDecimal) ReflectUtil.getFieldValue(realTimeData.get(j), "P" + k);
                                                    if (ObjectUtil.isNotEmpty(fieldValue) && ObjectUtil.isNotEmpty(fieldValue2)) {
                                                        map.put(k, fieldValue.subtract(fieldValue2));
                                                    }
                                                }
                                            }

                                            try {
                                                preparedStatement = connection.prepareStatement(sql);
                                                preparedStatement.setBigDecimal(1, map.get(startPoint));
                                                int count = 1;
                                                for (int h = 1; h <= (endPoint - startPoint); h++) {
                                                    preparedStatement.setBigDecimal(h + 1, map.get(startPoint + h));
                                                    count++;
                                                }
                                                LocalDate localDate = LocalDate.now();
                                                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
                                                String curvid = orgId + "_103_207_" + a + localDate.format(dateTimeFormatter);
                                                preparedStatement.setString(count + 1, curvid);

                                                preparedStatement.executeUpdate();


                                            } catch (SQLException e) {
                                                e.printStackTrace();
                                            }

                                        }

                                    }

                                }

                            }
                        }


                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (preparedStatement != null) {
                    try {
                        preparedStatement.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    public void saveFrozenToDatabase(String param) {

        if (StringUtils.isNotBlank(param)) {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = null;
            PreparedStatement preparedStatement2 = null;
            PreparedStatement preparedStatement3 = null;
            LocalDate pDate = null;
            try {
                List orgIds = getEventIDSByDate().get("orgIds");
                String[] strings = param.split("-");
                pDate = LocalDate.of(Integer.parseInt(strings[0]), Integer.parseInt(strings[1]), Integer.parseInt(strings[2]));
                LocalDate localDate1 = pDate.plusDays(-1);
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

                for (Object orgId : orgIds) {
                    if (getProvinceCode().equals(orgId.toString())) {
                        int count = 0;
                        List<Point96Vo> frozenData = this.getFrozenDataByparamTime(orgId.toString(), param);
                        if (frozenData != null && frozenData.size() > 0) {
                            for (Point96Vo p : frozenData) {
                                count++;
                                String sql = "insert into exch_static_curve_data_org VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?" +
                                        ",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?" +
                                        ",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                                String sql2 = "select * from  exch_rt_curve_data_org where curve_id=?";
                                try {
                                    preparedStatement = connection.prepareStatement(sql);
                                    String curveId = orgId + "_102_207_" + count + localDate1.format(dateTimeFormatter);
                                    preparedStatement.setString(1, curveId);
                                    preparedStatement.setString(2, orgId.toString());
                                    preparedStatement.setString(3, "");
                                    preparedStatement.setString(4, "");
                                    preparedStatement.setString(5, "207_" + count);
                                    preparedStatement.setString(6, "102");
                                    preparedStatement.setString(7, "1");
                                    preparedStatement.setDate(8, new java.sql.Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24));
                                    preparedStatement.setBigDecimal(9, p.getP1());
                                    preparedStatement.setBigDecimal(10, p.getP2());
                                    preparedStatement.setBigDecimal(11, p.getP3());
                                    preparedStatement.setBigDecimal(12, p.getP4());
                                    preparedStatement.setBigDecimal(13, p.getP5());
                                    preparedStatement.setBigDecimal(14, p.getP6());
                                    preparedStatement.setBigDecimal(15, p.getP7());
                                    preparedStatement.setBigDecimal(16, p.getP8());
                                    preparedStatement.setBigDecimal(17, p.getP9());
                                    preparedStatement.setBigDecimal(18, p.getP10());
                                    preparedStatement.setBigDecimal(19, p.getP11());
                                    preparedStatement.setBigDecimal(20, p.getP12());
                                    preparedStatement.setBigDecimal(21, p.getP13());
                                    preparedStatement.setBigDecimal(22, p.getP14());
                                    preparedStatement.setBigDecimal(23, p.getP15());
                                    preparedStatement.setBigDecimal(24, p.getP16());
                                    preparedStatement.setBigDecimal(25, p.getP17());
                                    preparedStatement.setBigDecimal(26, p.getP18());
                                    preparedStatement.setBigDecimal(27, p.getP19());
                                    preparedStatement.setBigDecimal(28, p.getP20());
                                    preparedStatement.setBigDecimal(29, p.getP21());
                                    preparedStatement.setBigDecimal(30, p.getP22());
                                    preparedStatement.setBigDecimal(31, p.getP23());
                                    preparedStatement.setBigDecimal(32, p.getP24());
                                    preparedStatement.setBigDecimal(33, p.getP25());
                                    preparedStatement.setBigDecimal(34, p.getP26());
                                    preparedStatement.setBigDecimal(35, p.getP27());
                                    preparedStatement.setBigDecimal(36, p.getP28());
                                    preparedStatement.setBigDecimal(37, p.getP29());
                                    preparedStatement.setBigDecimal(38, p.getP30());
                                    preparedStatement.setBigDecimal(39, p.getP31());
                                    preparedStatement.setBigDecimal(40, p.getP32());
                                    preparedStatement.setBigDecimal(41, p.getP33());
                                    preparedStatement.setBigDecimal(42, p.getP34());
                                    preparedStatement.setBigDecimal(43, p.getP35());
                                    preparedStatement.setBigDecimal(44, p.getP36());
                                    preparedStatement.setBigDecimal(45, p.getP37());
                                    preparedStatement.setBigDecimal(46, p.getP38());
                                    preparedStatement.setBigDecimal(47, p.getP39());
                                    preparedStatement.setBigDecimal(48, p.getP40());
                                    preparedStatement.setBigDecimal(49, p.getP41());
                                    preparedStatement.setBigDecimal(50, p.getP42());
                                    preparedStatement.setBigDecimal(51, p.getP43());
                                    preparedStatement.setBigDecimal(52, p.getP44());
                                    preparedStatement.setBigDecimal(53, p.getP45());
                                    preparedStatement.setBigDecimal(54, p.getP46());
                                    preparedStatement.setBigDecimal(55, p.getP47());
                                    preparedStatement.setBigDecimal(56, p.getP48());
                                    preparedStatement.setBigDecimal(57, p.getP49());
                                    preparedStatement.setBigDecimal(58, p.getP50());
                                    preparedStatement.setBigDecimal(59, p.getP51());
                                    preparedStatement.setBigDecimal(60, p.getP52());
                                    preparedStatement.setBigDecimal(61, p.getP53());
                                    preparedStatement.setBigDecimal(62, p.getP54());
                                    preparedStatement.setBigDecimal(63, p.getP55());
                                    preparedStatement.setBigDecimal(64, p.getP56());
                                    preparedStatement.setBigDecimal(65, p.getP57());
                                    preparedStatement.setBigDecimal(66, p.getP58());
                                    preparedStatement.setBigDecimal(67, p.getP59());
                                    preparedStatement.setBigDecimal(68, p.getP60());
                                    preparedStatement.setBigDecimal(69, p.getP61());
                                    preparedStatement.setBigDecimal(70, p.getP62());
                                    preparedStatement.setBigDecimal(71, p.getP63());
                                    preparedStatement.setBigDecimal(72, p.getP64());
                                    preparedStatement.setBigDecimal(73, p.getP65());
                                    preparedStatement.setBigDecimal(74, p.getP66());
                                    preparedStatement.setBigDecimal(75, p.getP67());
                                    preparedStatement.setBigDecimal(76, p.getP68());
                                    preparedStatement.setBigDecimal(77, p.getP69());
                                    preparedStatement.setBigDecimal(78, p.getP70());
                                    preparedStatement.setBigDecimal(79, p.getP71());
                                    preparedStatement.setBigDecimal(80, p.getP72());
                                    preparedStatement.setBigDecimal(81, p.getP73());
                                    preparedStatement.setBigDecimal(82, p.getP74());
                                    preparedStatement.setBigDecimal(83, p.getP75());
                                    preparedStatement.setBigDecimal(84, p.getP76());
                                    preparedStatement.setBigDecimal(85, p.getP77());
                                    preparedStatement.setBigDecimal(86, p.getP78());
                                    preparedStatement.setBigDecimal(87, p.getP79());
                                    preparedStatement.setBigDecimal(88, p.getP80());
                                    preparedStatement.setBigDecimal(89, p.getP81());
                                    preparedStatement.setBigDecimal(90, p.getP82());
                                    preparedStatement.setBigDecimal(91, p.getP83());
                                    preparedStatement.setBigDecimal(92, p.getP84());
                                    preparedStatement.setBigDecimal(93, p.getP85());
                                    preparedStatement.setBigDecimal(94, p.getP86());
                                    preparedStatement.setBigDecimal(95, p.getP87());
                                    preparedStatement.setBigDecimal(96, p.getP88());
                                    preparedStatement.setBigDecimal(97, p.getP89());
                                    preparedStatement.setBigDecimal(98, p.getP90());
                                    preparedStatement.setBigDecimal(99, p.getP91());
                                    preparedStatement.setBigDecimal(100, p.getP92());
                                    preparedStatement.setBigDecimal(101, p.getP93());
                                    preparedStatement.setBigDecimal(102, p.getP94());
                                    preparedStatement.setBigDecimal(103, p.getP95());
                                    preparedStatement.setBigDecimal(104, p.getP96());
                                    preparedStatement.setInt(105, 4);
                                    preparedStatement.setString(106, p.getEventId());
                                    preparedStatement.setDate(107, new java.sql.Date(System.currentTimeMillis()));
                                    preparedStatement.setDate(108, new java.sql.Date(System.currentTimeMillis()));
                                    preparedStatement.setDate(109, new java.sql.Date(System.currentTimeMillis()));
                                    preparedStatement.executeUpdate();

                                    preparedStatement2 = connection.prepareStatement(sql2);
                                    String curvId = orgId + "_103_207_" + count + localDate1.format(dateTimeFormatter);
                                    preparedStatement2.setString(1, curvId);
                                    ResultSet resultSet = preparedStatement2.executeQuery();
//                                    resultSet.setFetchSize(10);


                                    preparedStatement3 = connection.prepareStatement(sql);

                                    while (resultSet.next()) {
                                        preparedStatement3.setString(1, resultSet.getString("curve_id"));
                                        preparedStatement3.setString(2, resultSet.getString("curve_org_no"));
                                        preparedStatement3.setString(3, resultSet.getString("curve_p_org_no"));
                                        preparedStatement3.setString(4, resultSet.getString("curve_cons_no"));
                                        preparedStatement3.setString(5, resultSet.getString("curve_tgt_type"));
                                        preparedStatement3.setString(6, resultSet.getString("curve_type"));
                                        preparedStatement3.setString(7, resultSet.getString("stats_freq"));
                                        preparedStatement3.setDate(8, resultSet.getDate("data_date"));
                                        preparedStatement3.setBigDecimal(9, resultSet.getBigDecimal("point1"));
                                        preparedStatement3.setBigDecimal(10, resultSet.getBigDecimal("point2"));
                                        preparedStatement3.setBigDecimal(11, resultSet.getBigDecimal("point3"));
                                        preparedStatement3.setBigDecimal(12, resultSet.getBigDecimal("point4"));
                                        preparedStatement3.setBigDecimal(13, resultSet.getBigDecimal("point5"));
                                        preparedStatement3.setBigDecimal(14, resultSet.getBigDecimal("point6"));
                                        preparedStatement3.setBigDecimal(15, resultSet.getBigDecimal("point7"));
                                        preparedStatement3.setBigDecimal(16, resultSet.getBigDecimal("point8"));
                                        preparedStatement3.setBigDecimal(17, resultSet.getBigDecimal("point9"));
                                        preparedStatement3.setBigDecimal(18, resultSet.getBigDecimal("point10"));
                                        preparedStatement3.setBigDecimal(19, resultSet.getBigDecimal("point11"));
                                        preparedStatement3.setBigDecimal(20, resultSet.getBigDecimal("point12"));
                                        preparedStatement3.setBigDecimal(21, resultSet.getBigDecimal("point13"));
                                        preparedStatement3.setBigDecimal(22, resultSet.getBigDecimal("point14"));
                                        preparedStatement3.setBigDecimal(23, resultSet.getBigDecimal("point15"));
                                        preparedStatement3.setBigDecimal(24, resultSet.getBigDecimal("point16"));
                                        preparedStatement3.setBigDecimal(25, resultSet.getBigDecimal("point17"));
                                        preparedStatement3.setBigDecimal(26, resultSet.getBigDecimal("point18"));
                                        preparedStatement3.setBigDecimal(27, resultSet.getBigDecimal("point19"));
                                        preparedStatement3.setBigDecimal(28, resultSet.getBigDecimal("point20"));
                                        preparedStatement3.setBigDecimal(29, resultSet.getBigDecimal("point21"));
                                        preparedStatement3.setBigDecimal(30, resultSet.getBigDecimal("point22"));
                                        preparedStatement3.setBigDecimal(31, resultSet.getBigDecimal("point23"));
                                        preparedStatement3.setBigDecimal(32, resultSet.getBigDecimal("point24"));
                                        preparedStatement3.setBigDecimal(33, resultSet.getBigDecimal("point25"));
                                        preparedStatement3.setBigDecimal(34, resultSet.getBigDecimal("point26"));
                                        preparedStatement3.setBigDecimal(35, resultSet.getBigDecimal("point27"));
                                        preparedStatement3.setBigDecimal(36, resultSet.getBigDecimal("point28"));
                                        preparedStatement3.setBigDecimal(37, resultSet.getBigDecimal("point29"));
                                        preparedStatement3.setBigDecimal(38, resultSet.getBigDecimal("point30"));
                                        preparedStatement3.setBigDecimal(39, resultSet.getBigDecimal("point31"));
                                        preparedStatement3.setBigDecimal(40, resultSet.getBigDecimal("point32"));
                                        preparedStatement3.setBigDecimal(41, resultSet.getBigDecimal("point33"));
                                        preparedStatement3.setBigDecimal(42, resultSet.getBigDecimal("point34"));
                                        preparedStatement3.setBigDecimal(43, resultSet.getBigDecimal("point35"));
                                        preparedStatement3.setBigDecimal(44, resultSet.getBigDecimal("point36"));
                                        preparedStatement3.setBigDecimal(45, resultSet.getBigDecimal("point37"));
                                        preparedStatement3.setBigDecimal(46, resultSet.getBigDecimal("point38"));
                                        preparedStatement3.setBigDecimal(47, resultSet.getBigDecimal("point39"));
                                        preparedStatement3.setBigDecimal(48, resultSet.getBigDecimal("point40"));
                                        preparedStatement3.setBigDecimal(49, resultSet.getBigDecimal("point41"));
                                        preparedStatement3.setBigDecimal(50, resultSet.getBigDecimal("point42"));
                                        preparedStatement3.setBigDecimal(51, resultSet.getBigDecimal("point43"));
                                        preparedStatement3.setBigDecimal(52, resultSet.getBigDecimal("point44"));
                                        preparedStatement3.setBigDecimal(53, resultSet.getBigDecimal("point45"));
                                        preparedStatement3.setBigDecimal(54, resultSet.getBigDecimal("point46"));
                                        preparedStatement3.setBigDecimal(55, resultSet.getBigDecimal("point47"));
                                        preparedStatement3.setBigDecimal(56, resultSet.getBigDecimal("point48"));
                                        preparedStatement3.setBigDecimal(57, resultSet.getBigDecimal("point49"));
                                        preparedStatement3.setBigDecimal(58, resultSet.getBigDecimal("point50"));
                                        preparedStatement3.setBigDecimal(59, resultSet.getBigDecimal("point51"));
                                        preparedStatement3.setBigDecimal(60, resultSet.getBigDecimal("point52"));
                                        preparedStatement3.setBigDecimal(61, resultSet.getBigDecimal("point53"));
                                        preparedStatement3.setBigDecimal(62, resultSet.getBigDecimal("point54"));
                                        preparedStatement3.setBigDecimal(63, resultSet.getBigDecimal("point55"));
                                        preparedStatement3.setBigDecimal(64, resultSet.getBigDecimal("point56"));
                                        preparedStatement3.setBigDecimal(65, resultSet.getBigDecimal("point57"));
                                        preparedStatement3.setBigDecimal(66, resultSet.getBigDecimal("point58"));
                                        preparedStatement3.setBigDecimal(67, resultSet.getBigDecimal("point59"));
                                        preparedStatement3.setBigDecimal(68, resultSet.getBigDecimal("point60"));
                                        preparedStatement3.setBigDecimal(69, resultSet.getBigDecimal("point61"));
                                        preparedStatement3.setBigDecimal(70, resultSet.getBigDecimal("point62"));
                                        preparedStatement3.setBigDecimal(71, resultSet.getBigDecimal("point63"));
                                        preparedStatement3.setBigDecimal(72, resultSet.getBigDecimal("point64"));
                                        preparedStatement3.setBigDecimal(73, resultSet.getBigDecimal("point65"));
                                        preparedStatement3.setBigDecimal(74, resultSet.getBigDecimal("point66"));
                                        preparedStatement3.setBigDecimal(75, resultSet.getBigDecimal("point67"));
                                        preparedStatement3.setBigDecimal(76, resultSet.getBigDecimal("point68"));
                                        preparedStatement3.setBigDecimal(77, resultSet.getBigDecimal("point69"));
                                        preparedStatement3.setBigDecimal(78, resultSet.getBigDecimal("point70"));
                                        preparedStatement3.setBigDecimal(79, resultSet.getBigDecimal("point71"));
                                        preparedStatement3.setBigDecimal(80, resultSet.getBigDecimal("point72"));
                                        preparedStatement3.setBigDecimal(81, resultSet.getBigDecimal("point73"));
                                        preparedStatement3.setBigDecimal(82, resultSet.getBigDecimal("point74"));
                                        preparedStatement3.setBigDecimal(83, resultSet.getBigDecimal("point75"));
                                        preparedStatement3.setBigDecimal(84, resultSet.getBigDecimal("point76"));
                                        preparedStatement3.setBigDecimal(85, resultSet.getBigDecimal("point77"));
                                        preparedStatement3.setBigDecimal(86, resultSet.getBigDecimal("point78"));
                                        preparedStatement3.setBigDecimal(87, resultSet.getBigDecimal("point79"));
                                        preparedStatement3.setBigDecimal(88, resultSet.getBigDecimal("point80"));
                                        preparedStatement3.setBigDecimal(89, resultSet.getBigDecimal("point81"));
                                        preparedStatement3.setBigDecimal(90, resultSet.getBigDecimal("point82"));
                                        preparedStatement3.setBigDecimal(91, resultSet.getBigDecimal("point83"));
                                        preparedStatement3.setBigDecimal(92, resultSet.getBigDecimal("point84"));
                                        preparedStatement3.setBigDecimal(93, resultSet.getBigDecimal("point85"));
                                        preparedStatement3.setBigDecimal(94, resultSet.getBigDecimal("point86"));
                                        preparedStatement3.setBigDecimal(95, resultSet.getBigDecimal("point87"));
                                        preparedStatement3.setBigDecimal(96, resultSet.getBigDecimal("point88"));
                                        preparedStatement3.setBigDecimal(97, resultSet.getBigDecimal("point89"));
                                        preparedStatement3.setBigDecimal(98, resultSet.getBigDecimal("point90"));
                                        preparedStatement3.setBigDecimal(99, resultSet.getBigDecimal("point91"));
                                        preparedStatement3.setBigDecimal(100, resultSet.getBigDecimal("point92"));
                                        preparedStatement3.setBigDecimal(101, resultSet.getBigDecimal("point93"));
                                        preparedStatement3.setBigDecimal(102, resultSet.getBigDecimal("point94"));
                                        preparedStatement3.setBigDecimal(103, resultSet.getBigDecimal("point95"));
                                        preparedStatement3.setBigDecimal(104, resultSet.getBigDecimal("point96"));
                                        preparedStatement3.setInt(105, resultSet.getInt("extend_flag"));
                                        preparedStatement3.setString(106, resultSet.getString("extend_value"));
                                        preparedStatement3.setDate(107, new java.sql.Date(System.currentTimeMillis()));
                                        preparedStatement3.setDate(108, new java.sql.Date(System.currentTimeMillis()));
                                        preparedStatement3.setDate(109, new java.sql.Date(System.currentTimeMillis()));
                                        preparedStatement3.executeUpdate();
                                    }
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {

                if (preparedStatement != null) {
                    try {
                        preparedStatement.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = null;
            PreparedStatement preparedStatement2 = null;
            PreparedStatement preparedStatement3 = null;
            try {
                List orgIds = getEventIDSByDate().get("orgIds");
                LocalDate localDate = LocalDate.now();
                LocalDate localDate1 = localDate.plusDays(-1);
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");


                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, -1);
                Date d = cal.getTime();

                for (Object orgId : orgIds) {
                    if (getProvinceCode().equals(orgId.toString())) {
                        int count = 0;
                        List<Point96Vo> frozenData = this.getFrozenRealData(orgId.toString());
                        if (frozenData != null && frozenData.size() > 0) {
                            for (Point96Vo p : frozenData) {
                                count++;
                                String sql = "insert into exch_static_curve_data_org VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?" +
                                        ",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?" +
                                        ",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                                String sql2 = "select * from  exch_rt_curve_data_org where curve_id=?";
                                try {
                                    preparedStatement = connection.prepareStatement(sql);
                                    String curveId = orgId + "_102_207_" + count + localDate1.format(dateTimeFormatter);
                                    preparedStatement.setString(1, curveId);
                                    preparedStatement.setString(2, orgId.toString());
                                    preparedStatement.setString(3, "");
                                    preparedStatement.setString(4, "");
                                    preparedStatement.setString(5, "207_" + count);
                                    preparedStatement.setString(6, "102");
                                    preparedStatement.setString(7, "1");
                                    preparedStatement.setDate(8, new java.sql.Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24));
                                    preparedStatement.setBigDecimal(9, p.getP1());
                                    preparedStatement.setBigDecimal(10, p.getP2());
                                    preparedStatement.setBigDecimal(11, p.getP3());
                                    preparedStatement.setBigDecimal(12, p.getP4());
                                    preparedStatement.setBigDecimal(13, p.getP5());
                                    preparedStatement.setBigDecimal(14, p.getP6());
                                    preparedStatement.setBigDecimal(15, p.getP7());
                                    preparedStatement.setBigDecimal(16, p.getP8());
                                    preparedStatement.setBigDecimal(17, p.getP9());
                                    preparedStatement.setBigDecimal(18, p.getP10());
                                    preparedStatement.setBigDecimal(19, p.getP11());
                                    preparedStatement.setBigDecimal(20, p.getP12());
                                    preparedStatement.setBigDecimal(21, p.getP13());
                                    preparedStatement.setBigDecimal(22, p.getP14());
                                    preparedStatement.setBigDecimal(23, p.getP15());
                                    preparedStatement.setBigDecimal(24, p.getP16());
                                    preparedStatement.setBigDecimal(25, p.getP17());
                                    preparedStatement.setBigDecimal(26, p.getP18());
                                    preparedStatement.setBigDecimal(27, p.getP19());
                                    preparedStatement.setBigDecimal(28, p.getP20());
                                    preparedStatement.setBigDecimal(29, p.getP21());
                                    preparedStatement.setBigDecimal(30, p.getP22());
                                    preparedStatement.setBigDecimal(31, p.getP23());
                                    preparedStatement.setBigDecimal(32, p.getP24());
                                    preparedStatement.setBigDecimal(33, p.getP25());
                                    preparedStatement.setBigDecimal(34, p.getP26());
                                    preparedStatement.setBigDecimal(35, p.getP27());
                                    preparedStatement.setBigDecimal(36, p.getP28());
                                    preparedStatement.setBigDecimal(37, p.getP29());
                                    preparedStatement.setBigDecimal(38, p.getP30());
                                    preparedStatement.setBigDecimal(39, p.getP31());
                                    preparedStatement.setBigDecimal(40, p.getP32());
                                    preparedStatement.setBigDecimal(41, p.getP33());
                                    preparedStatement.setBigDecimal(42, p.getP34());
                                    preparedStatement.setBigDecimal(43, p.getP35());
                                    preparedStatement.setBigDecimal(44, p.getP36());
                                    preparedStatement.setBigDecimal(45, p.getP37());
                                    preparedStatement.setBigDecimal(46, p.getP38());
                                    preparedStatement.setBigDecimal(47, p.getP39());
                                    preparedStatement.setBigDecimal(48, p.getP40());
                                    preparedStatement.setBigDecimal(49, p.getP41());
                                    preparedStatement.setBigDecimal(50, p.getP42());
                                    preparedStatement.setBigDecimal(51, p.getP43());
                                    preparedStatement.setBigDecimal(52, p.getP44());
                                    preparedStatement.setBigDecimal(53, p.getP45());
                                    preparedStatement.setBigDecimal(54, p.getP46());
                                    preparedStatement.setBigDecimal(55, p.getP47());
                                    preparedStatement.setBigDecimal(56, p.getP48());
                                    preparedStatement.setBigDecimal(57, p.getP49());
                                    preparedStatement.setBigDecimal(58, p.getP50());
                                    preparedStatement.setBigDecimal(59, p.getP51());
                                    preparedStatement.setBigDecimal(60, p.getP52());
                                    preparedStatement.setBigDecimal(61, p.getP53());
                                    preparedStatement.setBigDecimal(62, p.getP54());
                                    preparedStatement.setBigDecimal(63, p.getP55());
                                    preparedStatement.setBigDecimal(64, p.getP56());
                                    preparedStatement.setBigDecimal(65, p.getP57());
                                    preparedStatement.setBigDecimal(66, p.getP58());
                                    preparedStatement.setBigDecimal(67, p.getP59());
                                    preparedStatement.setBigDecimal(68, p.getP60());
                                    preparedStatement.setBigDecimal(69, p.getP61());
                                    preparedStatement.setBigDecimal(70, p.getP62());
                                    preparedStatement.setBigDecimal(71, p.getP63());
                                    preparedStatement.setBigDecimal(72, p.getP64());
                                    preparedStatement.setBigDecimal(73, p.getP65());
                                    preparedStatement.setBigDecimal(74, p.getP66());
                                    preparedStatement.setBigDecimal(75, p.getP67());
                                    preparedStatement.setBigDecimal(76, p.getP68());
                                    preparedStatement.setBigDecimal(77, p.getP69());
                                    preparedStatement.setBigDecimal(78, p.getP70());
                                    preparedStatement.setBigDecimal(79, p.getP71());
                                    preparedStatement.setBigDecimal(80, p.getP72());
                                    preparedStatement.setBigDecimal(81, p.getP73());
                                    preparedStatement.setBigDecimal(82, p.getP74());
                                    preparedStatement.setBigDecimal(83, p.getP75());
                                    preparedStatement.setBigDecimal(84, p.getP76());
                                    preparedStatement.setBigDecimal(85, p.getP77());
                                    preparedStatement.setBigDecimal(86, p.getP78());
                                    preparedStatement.setBigDecimal(87, p.getP79());
                                    preparedStatement.setBigDecimal(88, p.getP80());
                                    preparedStatement.setBigDecimal(89, p.getP81());
                                    preparedStatement.setBigDecimal(90, p.getP82());
                                    preparedStatement.setBigDecimal(91, p.getP83());
                                    preparedStatement.setBigDecimal(92, p.getP84());
                                    preparedStatement.setBigDecimal(93, p.getP85());
                                    preparedStatement.setBigDecimal(94, p.getP86());
                                    preparedStatement.setBigDecimal(95, p.getP87());
                                    preparedStatement.setBigDecimal(96, p.getP88());
                                    preparedStatement.setBigDecimal(97, p.getP89());
                                    preparedStatement.setBigDecimal(98, p.getP90());
                                    preparedStatement.setBigDecimal(99, p.getP91());
                                    preparedStatement.setBigDecimal(100, p.getP92());
                                    preparedStatement.setBigDecimal(101, p.getP93());
                                    preparedStatement.setBigDecimal(102, p.getP94());
                                    preparedStatement.setBigDecimal(103, p.getP95());
                                    preparedStatement.setBigDecimal(104, p.getP96());
                                    preparedStatement.setInt(105, 4);
                                    preparedStatement.setString(106, p.getEventId());
                                    preparedStatement.setDate(107, new java.sql.Date(System.currentTimeMillis()));
                                    preparedStatement.setDate(108, new java.sql.Date(System.currentTimeMillis()));
                                    preparedStatement.setDate(109, new java.sql.Date(System.currentTimeMillis()));
                                    preparedStatement.executeUpdate();

                                    preparedStatement2 = connection.prepareStatement(sql2);
                                    String curvId = orgId + "_103_207_" + count + localDate1.format(dateTimeFormatter);
                                    preparedStatement2.setString(1, curvId);
                                    ResultSet resultSet = preparedStatement2.executeQuery();

                                    preparedStatement3 = connection.prepareStatement(sql);

                                    while (resultSet.next()) {
                                        preparedStatement3.setString(1, resultSet.getString("curve_id"));
                                        preparedStatement3.setString(2, resultSet.getString("curve_org_no"));
                                        preparedStatement3.setString(3, resultSet.getString("curve_p_org_no"));
                                        preparedStatement3.setString(4, resultSet.getString("curve_cons_no"));
                                        preparedStatement3.setString(5, resultSet.getString("curve_tgt_type"));
                                        preparedStatement3.setString(6, resultSet.getString("curve_type"));
                                        preparedStatement3.setString(7, resultSet.getString("stats_freq"));
                                        preparedStatement3.setDate(8, resultSet.getDate("data_date"));
                                        preparedStatement3.setBigDecimal(9, resultSet.getBigDecimal("point1"));
                                        preparedStatement3.setBigDecimal(10, resultSet.getBigDecimal("point2"));
                                        preparedStatement3.setBigDecimal(11, resultSet.getBigDecimal("point3"));
                                        preparedStatement3.setBigDecimal(12, resultSet.getBigDecimal("point4"));
                                        preparedStatement3.setBigDecimal(13, resultSet.getBigDecimal("point5"));
                                        preparedStatement3.setBigDecimal(14, resultSet.getBigDecimal("point6"));
                                        preparedStatement3.setBigDecimal(15, resultSet.getBigDecimal("point7"));
                                        preparedStatement3.setBigDecimal(16, resultSet.getBigDecimal("point8"));
                                        preparedStatement3.setBigDecimal(17, resultSet.getBigDecimal("point9"));
                                        preparedStatement3.setBigDecimal(18, resultSet.getBigDecimal("point10"));
                                        preparedStatement3.setBigDecimal(19, resultSet.getBigDecimal("point11"));
                                        preparedStatement3.setBigDecimal(20, resultSet.getBigDecimal("point12"));
                                        preparedStatement3.setBigDecimal(21, resultSet.getBigDecimal("point13"));
                                        preparedStatement3.setBigDecimal(22, resultSet.getBigDecimal("point14"));
                                        preparedStatement3.setBigDecimal(23, resultSet.getBigDecimal("point15"));
                                        preparedStatement3.setBigDecimal(24, resultSet.getBigDecimal("point16"));
                                        preparedStatement3.setBigDecimal(25, resultSet.getBigDecimal("point17"));
                                        preparedStatement3.setBigDecimal(26, resultSet.getBigDecimal("point18"));
                                        preparedStatement3.setBigDecimal(27, resultSet.getBigDecimal("point19"));
                                        preparedStatement3.setBigDecimal(28, resultSet.getBigDecimal("point20"));
                                        preparedStatement3.setBigDecimal(29, resultSet.getBigDecimal("point21"));
                                        preparedStatement3.setBigDecimal(30, resultSet.getBigDecimal("point22"));
                                        preparedStatement3.setBigDecimal(31, resultSet.getBigDecimal("point23"));
                                        preparedStatement3.setBigDecimal(32, resultSet.getBigDecimal("point24"));
                                        preparedStatement3.setBigDecimal(33, resultSet.getBigDecimal("point25"));
                                        preparedStatement3.setBigDecimal(34, resultSet.getBigDecimal("point26"));
                                        preparedStatement3.setBigDecimal(35, resultSet.getBigDecimal("point27"));
                                        preparedStatement3.setBigDecimal(36, resultSet.getBigDecimal("point28"));
                                        preparedStatement3.setBigDecimal(37, resultSet.getBigDecimal("point29"));
                                        preparedStatement3.setBigDecimal(38, resultSet.getBigDecimal("point30"));
                                        preparedStatement3.setBigDecimal(39, resultSet.getBigDecimal("point31"));
                                        preparedStatement3.setBigDecimal(40, resultSet.getBigDecimal("point32"));
                                        preparedStatement3.setBigDecimal(41, resultSet.getBigDecimal("point33"));
                                        preparedStatement3.setBigDecimal(42, resultSet.getBigDecimal("point34"));
                                        preparedStatement3.setBigDecimal(43, resultSet.getBigDecimal("point35"));
                                        preparedStatement3.setBigDecimal(44, resultSet.getBigDecimal("point36"));
                                        preparedStatement3.setBigDecimal(45, resultSet.getBigDecimal("point37"));
                                        preparedStatement3.setBigDecimal(46, resultSet.getBigDecimal("point38"));
                                        preparedStatement3.setBigDecimal(47, resultSet.getBigDecimal("point39"));
                                        preparedStatement3.setBigDecimal(48, resultSet.getBigDecimal("point40"));
                                        preparedStatement3.setBigDecimal(49, resultSet.getBigDecimal("point41"));
                                        preparedStatement3.setBigDecimal(50, resultSet.getBigDecimal("point42"));
                                        preparedStatement3.setBigDecimal(51, resultSet.getBigDecimal("point43"));
                                        preparedStatement3.setBigDecimal(52, resultSet.getBigDecimal("point44"));
                                        preparedStatement3.setBigDecimal(53, resultSet.getBigDecimal("point45"));
                                        preparedStatement3.setBigDecimal(54, resultSet.getBigDecimal("point46"));
                                        preparedStatement3.setBigDecimal(55, resultSet.getBigDecimal("point47"));
                                        preparedStatement3.setBigDecimal(56, resultSet.getBigDecimal("point48"));
                                        preparedStatement3.setBigDecimal(57, resultSet.getBigDecimal("point49"));
                                        preparedStatement3.setBigDecimal(58, resultSet.getBigDecimal("point50"));
                                        preparedStatement3.setBigDecimal(59, resultSet.getBigDecimal("point51"));
                                        preparedStatement3.setBigDecimal(60, resultSet.getBigDecimal("point52"));
                                        preparedStatement3.setBigDecimal(61, resultSet.getBigDecimal("point53"));
                                        preparedStatement3.setBigDecimal(62, resultSet.getBigDecimal("point54"));
                                        preparedStatement3.setBigDecimal(63, resultSet.getBigDecimal("point55"));
                                        preparedStatement3.setBigDecimal(64, resultSet.getBigDecimal("point56"));
                                        preparedStatement3.setBigDecimal(65, resultSet.getBigDecimal("point57"));
                                        preparedStatement3.setBigDecimal(66, resultSet.getBigDecimal("point58"));
                                        preparedStatement3.setBigDecimal(67, resultSet.getBigDecimal("point59"));
                                        preparedStatement3.setBigDecimal(68, resultSet.getBigDecimal("point60"));
                                        preparedStatement3.setBigDecimal(69, resultSet.getBigDecimal("point61"));
                                        preparedStatement3.setBigDecimal(70, resultSet.getBigDecimal("point62"));
                                        preparedStatement3.setBigDecimal(71, resultSet.getBigDecimal("point63"));
                                        preparedStatement3.setBigDecimal(72, resultSet.getBigDecimal("point64"));
                                        preparedStatement3.setBigDecimal(73, resultSet.getBigDecimal("point65"));
                                        preparedStatement3.setBigDecimal(74, resultSet.getBigDecimal("point66"));
                                        preparedStatement3.setBigDecimal(75, resultSet.getBigDecimal("point67"));
                                        preparedStatement3.setBigDecimal(76, resultSet.getBigDecimal("point68"));
                                        preparedStatement3.setBigDecimal(77, resultSet.getBigDecimal("point69"));
                                        preparedStatement3.setBigDecimal(78, resultSet.getBigDecimal("point70"));
                                        preparedStatement3.setBigDecimal(79, resultSet.getBigDecimal("point71"));
                                        preparedStatement3.setBigDecimal(80, resultSet.getBigDecimal("point72"));
                                        preparedStatement3.setBigDecimal(81, resultSet.getBigDecimal("point73"));
                                        preparedStatement3.setBigDecimal(82, resultSet.getBigDecimal("point74"));
                                        preparedStatement3.setBigDecimal(83, resultSet.getBigDecimal("point75"));
                                        preparedStatement3.setBigDecimal(84, resultSet.getBigDecimal("point76"));
                                        preparedStatement3.setBigDecimal(85, resultSet.getBigDecimal("point77"));
                                        preparedStatement3.setBigDecimal(86, resultSet.getBigDecimal("point78"));
                                        preparedStatement3.setBigDecimal(87, resultSet.getBigDecimal("point79"));
                                        preparedStatement3.setBigDecimal(88, resultSet.getBigDecimal("point80"));
                                        preparedStatement3.setBigDecimal(89, resultSet.getBigDecimal("point81"));
                                        preparedStatement3.setBigDecimal(90, resultSet.getBigDecimal("point82"));
                                        preparedStatement3.setBigDecimal(91, resultSet.getBigDecimal("point83"));
                                        preparedStatement3.setBigDecimal(92, resultSet.getBigDecimal("point84"));
                                        preparedStatement3.setBigDecimal(93, resultSet.getBigDecimal("point85"));
                                        preparedStatement3.setBigDecimal(94, resultSet.getBigDecimal("point86"));
                                        preparedStatement3.setBigDecimal(95, resultSet.getBigDecimal("point87"));
                                        preparedStatement3.setBigDecimal(96, resultSet.getBigDecimal("point88"));
                                        preparedStatement3.setBigDecimal(97, resultSet.getBigDecimal("point89"));
                                        preparedStatement3.setBigDecimal(98, resultSet.getBigDecimal("point90"));
                                        preparedStatement3.setBigDecimal(99, resultSet.getBigDecimal("point91"));
                                        preparedStatement3.setBigDecimal(100, resultSet.getBigDecimal("point92"));
                                        preparedStatement3.setBigDecimal(101, resultSet.getBigDecimal("point93"));
                                        preparedStatement3.setBigDecimal(102, resultSet.getBigDecimal("point94"));
                                        preparedStatement3.setBigDecimal(103, resultSet.getBigDecimal("point95"));
                                        preparedStatement3.setBigDecimal(104, resultSet.getBigDecimal("point96"));
                                        preparedStatement3.setInt(105, resultSet.getInt("extend_flag"));
                                        preparedStatement3.setString(106, resultSet.getString("extend_value"));
                                        preparedStatement3.setDate(107, new java.sql.Date(System.currentTimeMillis()));
                                        preparedStatement3.setDate(108, new java.sql.Date(System.currentTimeMillis()));
                                        preparedStatement3.setDate(109, new java.sql.Date(System.currentTimeMillis()));

                                        preparedStatement3.executeUpdate();
                                    }


                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (preparedStatement != null) {
                    try {
                        preparedStatement.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if (preparedStatement2 != null) {
                    try {
                        preparedStatement2.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if (preparedStatement3 != null) {
                    try {
                        preparedStatement3.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    private void saveBaselineDataAndDepressDataUpdate(String param) {
        if (StringUtils.isNotBlank(param)) {

            Connection connection = getConnection();
            PreparedStatement preparedStatement = null;
            PreparedStatement preparedStatement2 = null;
            PreparedStatement preparedStatement3 = null;
            PreparedStatement preparedStatement4 = null;
            LocalDate pDate = null;
            String[] strings = param.split("&");
            String id = strings[0];
            Double point = Double.parseDouble(strings[1]);
            try {

                String sql = "update exch_static_curve_data_org set " +
                        "point1=point1+?, point2=point2+?,point3=point3+?,point4=point4+?, point5=point5+?,point6=point6+?,point7=point7+?, point8=point8+?,point9=point9+?,point10=point10+?," +
                        "point11=point11+?, point12=point12+?,point13=point13+?,point14=point14+?, point15=point15+?,point16=point16+?,point17=point17+?, point18=point18+?,point19=point19+?,point20=point20+?," +
                        "point21=point21+?, point22=point22+?,point23=point23+?,point24=point24+?, point25=point25+?,point26=point26+?,point27=point27+?, point28=point28+?,point29=point29+?,point30=point30+?," +
                        "point31=point31+?, point32=point32+?,point33=point33+?,point34=point34+?, point35=point35+?,point36=point36+?,point37=point37+?, point38=point38+?,point39=point39+?,point40=point40+?," +
                        "point41=point41+?, point42=point42+?,point43=point43+?,point44=point44+?, point45=point45+?,point46=point46+?,point47=point47+?, point48=point48+?,point49=point49+?,point50=point50+?," +
                        "point51=point51+?, point52=point52+?,point53=point53+?,point54=point54+?, point55=point55+?,point56=point56+?,point57=point57+?, point58=point58+?,point59=point59+?,point60=point60+?," +
                        "point61=point61+?, point62=point62+?,point63=point63+?,point64=point64+?, point65=point65+?,point66=point66+?,point67=point67+?, point68=point68+?,point69=point69+?,point70=point70+?," +
                        "point71=point71+?, point72=point72+?,point73=point73+?,point74=point74+?, point75=point75+?,point76=point76+?,point77=point77+?, point78=point78+?,point79=point79+?,point80=point80+?," +
                        "point81=point81+?, point82=point82+?,point83=point83+?,point84=point84+?, point85=point85+?,point86=point86+?,point87=point87+?, point88=point88+?,point89=point89+?,point90=point90+?," +
                        "point91=point91+?, point92=point92+?,point93=point93+?,point94=point94+?, point95=point95+?,point96=point96+?,update_time=now() where extend_value = ? and curve_type=? and curve_org_no=?";

                preparedStatement = connection.prepareStatement(sql);
                for (int i = 1; i <= 96; i++) {
                    preparedStatement.setDouble(i, point);
                }
                preparedStatement.setString(97, id);
                preparedStatement.setString(98, "101");
                preparedStatement.setString(99, "34101");

                preparedStatement.executeUpdate();


//                LocalDate now = LocalDate.now();
                DrEventTime eventTime = baselineAndMeasurementMapper.getEventTime2(id);

                int startPoint = CurveUtil.covDateTimeToPoint(eventTime.getStartTime());
                int endPoint = CurveUtil.covDateTimeToPoint(eventTime.getEndTime());

                Point96Vo timeBaseLine = getTimeBaseLine(id);

                StringBuffer sb = new StringBuffer("update exch_static_curve_data_org set ");
                sb.append("point" + String.valueOf(startPoint) + "=? ");
                for (int j = 1; j <= (endPoint - startPoint); j++) {
                    sb.append(",");
                    sb.append("point" + String.valueOf(startPoint + j) + "=? ");
                }
                sb.append("where extend_value=? and curve_type=?");
                String sql2 = sb.toString();


                Map<Integer, BigDecimal> map = new HashMap();

                for (int k = 1; k <= 96; k++) {
                    BigDecimal fieldValue = (BigDecimal) ReflectUtil.getFieldValue(timeBaseLine, "P" + k);
                    map.put(k, fieldValue);
                }


                try {
                    preparedStatement2 = connection.prepareStatement(sql2);
                    preparedStatement2.setBigDecimal(1, map.get(startPoint));
                    int count = 1;
                    for (int h = 1; h <= (endPoint - startPoint); h++) {
                        preparedStatement2.setBigDecimal(h + 1, map.get(startPoint + h));
                        count++;
                    }
                    preparedStatement2.setString(count + 1, id);
                    preparedStatement2.setString(count + 2, "101");

                    preparedStatement2.executeUpdate();


                    String sql3 = "select * from exch_static_curve_data_org where extend_value=? and curve_type=? and curve_org_no=?";
                    preparedStatement3 = connection.prepareStatement(sql3);
                    preparedStatement3.setString(1, id);
                    preparedStatement3.setString(2, "101");
                    preparedStatement3.setString(3, "34101");
                    ResultSet resultSet = preparedStatement3.executeQuery();
                    Point96Vo point96Vo = new Point96Vo();
                    while (resultSet.next()) {
                        point96Vo.setP1(resultSet.getBigDecimal("point1"));
                        point96Vo.setP2(resultSet.getBigDecimal("point2"));
                        point96Vo.setP3(resultSet.getBigDecimal("point3"));
                        point96Vo.setP4(resultSet.getBigDecimal("point4"));
                        point96Vo.setP5(resultSet.getBigDecimal("point5"));
                        point96Vo.setP6(resultSet.getBigDecimal("point6"));
                        point96Vo.setP7(resultSet.getBigDecimal("point7"));
                        point96Vo.setP8(resultSet.getBigDecimal("point8"));
                        point96Vo.setP9(resultSet.getBigDecimal("point9"));
                        point96Vo.setP10(resultSet.getBigDecimal("point10"));
                        point96Vo.setP11(resultSet.getBigDecimal("point11"));
                        point96Vo.setP12(resultSet.getBigDecimal("point12"));
                        point96Vo.setP13(resultSet.getBigDecimal("point13"));
                        point96Vo.setP14(resultSet.getBigDecimal("point14"));
                        point96Vo.setP15(resultSet.getBigDecimal("point15"));
                        point96Vo.setP16(resultSet.getBigDecimal("point16"));
                        point96Vo.setP17(resultSet.getBigDecimal("point17"));
                        point96Vo.setP18(resultSet.getBigDecimal("point18"));
                        point96Vo.setP19(resultSet.getBigDecimal("point19"));
                        point96Vo.setP20(resultSet.getBigDecimal("point20"));
                        point96Vo.setP21(resultSet.getBigDecimal("point21"));
                        point96Vo.setP22(resultSet.getBigDecimal("point22"));
                        point96Vo.setP23(resultSet.getBigDecimal("point23"));
                        point96Vo.setP24(resultSet.getBigDecimal("point24"));
                        point96Vo.setP25(resultSet.getBigDecimal("point25"));
                        point96Vo.setP26(resultSet.getBigDecimal("point26"));
                        point96Vo.setP27(resultSet.getBigDecimal("point27"));
                        point96Vo.setP28(resultSet.getBigDecimal("point28"));
                        point96Vo.setP29(resultSet.getBigDecimal("point29"));
                        point96Vo.setP30(resultSet.getBigDecimal("point30"));
                        point96Vo.setP31(resultSet.getBigDecimal("point31"));
                        point96Vo.setP32(resultSet.getBigDecimal("point32"));
                        point96Vo.setP33(resultSet.getBigDecimal("point33"));
                        point96Vo.setP34(resultSet.getBigDecimal("point34"));
                        point96Vo.setP35(resultSet.getBigDecimal("point35"));
                        point96Vo.setP36(resultSet.getBigDecimal("point36"));
                        point96Vo.setP37(resultSet.getBigDecimal("point37"));
                        point96Vo.setP38(resultSet.getBigDecimal("point38"));
                        point96Vo.setP39(resultSet.getBigDecimal("point39"));
                        point96Vo.setP40(resultSet.getBigDecimal("point40"));
                        point96Vo.setP41(resultSet.getBigDecimal("point41"));
                        point96Vo.setP42(resultSet.getBigDecimal("point42"));
                        point96Vo.setP43(resultSet.getBigDecimal("point43"));
                        point96Vo.setP44(resultSet.getBigDecimal("point44"));
                        point96Vo.setP45(resultSet.getBigDecimal("point45"));
                        point96Vo.setP46(resultSet.getBigDecimal("point46"));
                        point96Vo.setP47(resultSet.getBigDecimal("point47"));
                        point96Vo.setP48(resultSet.getBigDecimal("point48"));
                        point96Vo.setP49(resultSet.getBigDecimal("point49"));
                        point96Vo.setP50(resultSet.getBigDecimal("point50"));
                        point96Vo.setP51(resultSet.getBigDecimal("point51"));
                        point96Vo.setP52(resultSet.getBigDecimal("point52"));
                        point96Vo.setP53(resultSet.getBigDecimal("point53"));
                        point96Vo.setP54(resultSet.getBigDecimal("point54"));
                        point96Vo.setP55(resultSet.getBigDecimal("point55"));
                        point96Vo.setP56(resultSet.getBigDecimal("point56"));
                        point96Vo.setP57(resultSet.getBigDecimal("point57"));
                        point96Vo.setP58(resultSet.getBigDecimal("point58"));
                        point96Vo.setP59(resultSet.getBigDecimal("point59"));
                        point96Vo.setP60(resultSet.getBigDecimal("point60"));
                        point96Vo.setP61(resultSet.getBigDecimal("point61"));
                        point96Vo.setP62(resultSet.getBigDecimal("point62"));
                        point96Vo.setP63(resultSet.getBigDecimal("point63"));
                        point96Vo.setP64(resultSet.getBigDecimal("point64"));
                        point96Vo.setP65(resultSet.getBigDecimal("point65"));
                        point96Vo.setP66(resultSet.getBigDecimal("point66"));
                        point96Vo.setP67(resultSet.getBigDecimal("point67"));
                        point96Vo.setP68(resultSet.getBigDecimal("point68"));
                        point96Vo.setP69(resultSet.getBigDecimal("point69"));
                        point96Vo.setP70(resultSet.getBigDecimal("point70"));
                        point96Vo.setP71(resultSet.getBigDecimal("point71"));
                        point96Vo.setP72(resultSet.getBigDecimal("point72"));
                        point96Vo.setP73(resultSet.getBigDecimal("point73"));
                        point96Vo.setP74(resultSet.getBigDecimal("point74"));
                        point96Vo.setP75(resultSet.getBigDecimal("point75"));
                        point96Vo.setP76(resultSet.getBigDecimal("point76"));
                        point96Vo.setP77(resultSet.getBigDecimal("point77"));
                        point96Vo.setP78(resultSet.getBigDecimal("point78"));
                        point96Vo.setP79(resultSet.getBigDecimal("point79"));
                        point96Vo.setP80(resultSet.getBigDecimal("point80"));
                        point96Vo.setP81(resultSet.getBigDecimal("point81"));
                        point96Vo.setP82(resultSet.getBigDecimal("point82"));
                        point96Vo.setP83(resultSet.getBigDecimal("point83"));
                        point96Vo.setP84(resultSet.getBigDecimal("point84"));
                        point96Vo.setP85(resultSet.getBigDecimal("point85"));
                        point96Vo.setP86(resultSet.getBigDecimal("point86"));
                        point96Vo.setP87(resultSet.getBigDecimal("point87"));
                        point96Vo.setP88(resultSet.getBigDecimal("point88"));
                        point96Vo.setP89(resultSet.getBigDecimal("point89"));
                        point96Vo.setP90(resultSet.getBigDecimal("point90"));
                        point96Vo.setP91(resultSet.getBigDecimal("point91"));
                        point96Vo.setP92(resultSet.getBigDecimal("point92"));
                        point96Vo.setP93(resultSet.getBigDecimal("point93"));
                        point96Vo.setP94(resultSet.getBigDecimal("point94"));
                        point96Vo.setP95(resultSet.getBigDecimal("point95"));
                        point96Vo.setP96(resultSet.getBigDecimal("point96"));

                    }

                    List<Point96Vo> realTimeData = this.getRealTimeDataByParamTimeAndEventId("34101", id);

                    int j = -1;
                    Map<Integer, BigDecimal> map2 = new HashMap();
                    for (Point96Vo p : realTimeData) {
                        j++;
                        for (int k = 1; k <= 96; k++) {
                            BigDecimal fieldValue = (BigDecimal) ReflectUtil.getFieldValue(point96Vo, "P" + k);
                            BigDecimal fieldValue2 = (BigDecimal) ReflectUtil.getFieldValue(p, "P" + k);
                            if (ObjectUtil.isNotEmpty(fieldValue) && ObjectUtil.isNotEmpty(fieldValue2)) {
                                map2.put(k, fieldValue.subtract(fieldValue2));
                            }
                        }
                    }

                    try {
                        preparedStatement4 = connection.prepareStatement(sql2);
                        preparedStatement4.setBigDecimal(1, map2.get(startPoint));
                        int count2 = 1;
                        for (int h = 1; h <= (endPoint - startPoint); h++) {
                            preparedStatement4.setBigDecimal(h + 1, map2.get(startPoint + h));
                            count2++;
                        }
                        preparedStatement4.setString(count2 + 1, id);
                        preparedStatement4.setString(count2 + 2, "103");

                        preparedStatement4.executeUpdate();


                    } catch (SQLException e) {
                        e.printStackTrace();
                    }


                } catch (SQLException e) {
                    e.printStackTrace();
                }


            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (preparedStatement != null) {
                        preparedStatement.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    if (preparedStatement2 != null) {
                        preparedStatement2.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    if (preparedStatement3 != null) {
                        preparedStatement3.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    if (preparedStatement4 != null) {
                        preparedStatement4.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    public void saveBaselineToDatabase2(String param, String point) {
        Connection connection = getConnection();
        PreparedStatement preparedStatement = null;
        PreparedStatement preparedStatement2 = null;
        PreparedStatement preparedStatementa = null;
        PreparedStatement preparedStatementb = null;
        String dateStr = baselineAndMeasurementMapper.getTime(param);
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate pDate = LocalDate.parse(dateStr, df);

        ZoneId zone = ZoneId.systemDefault();
        Instant instant = pDate.atStartOfDay().atZone(zone).toInstant();
        java.util.Date da = Date.from(instant);
        long datems = da.getTime();
        java.sql.Date sqlDate = new java.sql.Date(datems);

        try {
            List<Long> eventIds = new ArrayList<>();
            eventIds.add(Long.valueOf(param));
            List orgIds = getEventIDSByDate().get("orgIds");
            for (Object orgId : orgIds) {
                if (getProvinceCode().equals(orgId.toString())) {
                    List<Point96Vo> baseLine = this.getBaseLineByParamTime(eventIds, pDate);
                    if (baseLine == null || baseLine.size() <= 0) {
                        continue;
                    }
                    int count = 0;
                    String sqla = "select count(*) from exch_static_curve_data_org where extend_value=? and curve_tgt_type=? and curve_type=?";

                    String sqlb = "delete  from exch_static_curve_data_org where extend_value=? and curve_tgt_type=? and (curve_type=? or curve_type=? or curve_type=? )";

                    preparedStatementa = connection.prepareStatement(sqla);
                    preparedStatementa.setString(1, param);
                    preparedStatementa.setString(2, "207_1");
                    preparedStatementa.setString(3, "101");

                    ResultSet resultSet = preparedStatementa.executeQuery(sqla);

                    while (resultSet.next()) {
                        preparedStatementb = connection.prepareStatement(sqlb);
                        preparedStatementb.setString(1, param);
                        preparedStatementb.setString(2, "207_1");
                        preparedStatementb.setString(3, "101");
                        preparedStatementb.setString(4, "102");
                        preparedStatementb.setString(5, "103");
                        int i = preparedStatementb.executeUpdate();
                    }
                    for (Point96Vo p : baseLine) {
                        count++;
                        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
                        String curveIdFlag = orgId.toString() + "_101_207_" + count + pDate.format(dateTimeFormatter);

                        String sql = "insert into exch_static_curve_data_org(curve_id, curve_org_no, curve_p_org_no, curve_cons_no, curve_tgt_type," +
                                " curve_type, stats_freq, data_date, point1, point2, point3, point4, point5, point6, point7, point8, " +
                                "point9,point10,point11, point12, point13, point14, point15, point16, point17, point18, point19, point20," +
                                " point21, point22, point23, point24, point25, point26, point27, point28, point29, point30, point31, " +
                                "point32, point33, point34, point35, point36, point37, point38, point39, point40, point41, point42, point43," +
                                " point44, point45, point46, point47, point48, point49, point50, point51, point52, point53, point54, " +
                                "point55, point56, point57, point58, point59, point60, point61, point62, point63, point64, point65, point66," +
                                "point67, point68, point69, point70, point71, point72, point73, point74, point75, point76, point77, " +
                                "point78, point79, point80, point81, point82, point83, point84, point85, point86, point87, point88, point89," +
                                " point90, point91, point92, point93, point94, point95, point96, extend_flag, extend_value, create_time, update_time," +
                                " upload_time) VALUES(" +
                                "?,?,?,?,?,?,?,?,?,?," +
                                "?,?,?,?,?,?,?,?,?,?," +
                                "?,?,?,?,?,?,?,?,?,?," +
                                "?,?,?,?,?,?,?,?,?,?," +
                                "?,?,?,?,?,?,?,?,?,?," +
                                "?,?,?,?,?,?,?,?,?,?," +
                                "?,?,?,?,?,?,?,?,?,?," +
                                "?,?,?,?,?,?,?,?,?,?," +
                                "?,?,?,?,?,?,?,?,?,?," +
                                "?,?,?,?,?,?,?,?,?,?," +
                                "?,?,?,?,?,?,?,?,?)";


                        try {
                            preparedStatement = connection.prepareStatement(sql);
                            String provinceCode = this.getProvinceCode();
                            if (orgId.toString().equals(provinceCode)) {
                                String curveId = provinceCode + "_101_207_" + count + pDate.format(dateTimeFormatter);
                                preparedStatement.setString(1, curveId);
                                preparedStatement.setString(2, orgId.toString());
                                preparedStatement.setString(3, "00000");
                                preparedStatement.setString(4, "");
                                preparedStatement.setString(5, "207_" + count);
                                preparedStatement.setString(6, "101");
                                preparedStatement.setString(7, "1");
                                preparedStatement.setDate(8, sqlDate);
                                preparedStatement.setBigDecimal(9, p.getP1().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(10, p.getP2().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(11, p.getP3().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(12, p.getP4().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(13, p.getP5().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(14, p.getP6().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(15, p.getP7().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(16, p.getP8().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(17, p.getP9().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(18, p.getP10().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(19, p.getP11().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(20, p.getP12().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(21, p.getP13().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(22, p.getP14().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(23, p.getP15().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(24, p.getP16().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(25, p.getP17().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(26, p.getP18().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(27, p.getP19().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(28, p.getP20().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(29, p.getP21().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(30, p.getP22().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(31, p.getP23().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(32, p.getP24().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(33, p.getP25().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(34, p.getP26().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(35, p.getP27().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(36, p.getP28().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(37, p.getP29().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(38, p.getP30().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(39, p.getP31().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(40, p.getP32().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(41, p.getP33().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(42, p.getP34().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(43, p.getP35().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(44, p.getP36().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(45, p.getP37().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(46, p.getP38().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(47, p.getP39().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(48, p.getP40().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(49, p.getP41().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(50, p.getP42().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(51, p.getP43().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(52, p.getP44().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(53, p.getP45().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(54, p.getP46().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(55, p.getP47().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(56, p.getP48().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(57, p.getP49().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(58, p.getP50().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(59, p.getP51().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(60, p.getP52().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(61, p.getP53().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(62, p.getP54().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(63, p.getP55().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(64, p.getP56().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(65, p.getP57().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(66, p.getP58().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(67, p.getP59().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(68, p.getP60().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(69, p.getP61().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(70, p.getP62().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(71, p.getP63().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(72, p.getP64().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(73, p.getP65().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(74, p.getP66().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(75, p.getP67().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(76, p.getP68().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(77, p.getP69().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(78, p.getP70().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(79, p.getP71().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(80, p.getP72().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(81, p.getP73().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(82, p.getP74().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(83, p.getP75().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(84, p.getP76().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(85, p.getP77().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(86, p.getP78().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(87, p.getP79().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(88, p.getP80().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(89, p.getP81().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(90, p.getP82().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(91, p.getP83().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(92, p.getP84().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(93, p.getP85().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(94, p.getP86().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(95, p.getP87().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(96, p.getP88().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(97, p.getP89().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(98, p.getP90().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(99, p.getP91().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(100, p.getP92().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(101, p.getP93().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(102, p.getP94().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(103, p.getP95().add(new BigDecimal(point)));
                                preparedStatement.setBigDecimal(104, p.getP96().add(new BigDecimal(point)));
                                preparedStatement.setInt(105, 4);
                                preparedStatement.setString(106, p.getEventId());
                                preparedStatement.setDate(107, new java.sql.Date(System.currentTimeMillis()));
                                preparedStatement.setDate(108, new java.sql.Date(System.currentTimeMillis()));
                                preparedStatement.setDate(109, new java.sql.Date(System.currentTimeMillis()));

                                preparedStatement.executeUpdate();


                                DrEventTime eventTime = baselineAndMeasurementMapper.getEventTime2(param);

                                int startPoint = CurveUtil.covDateTimeToPoint(eventTime.getStartTime());
                                int endPoint = CurveUtil.covDateTimeToPoint(eventTime.getEndTime());

                                Point96Vo timeBaseLine = getTimeBaseLine(param);

                                StringBuffer sb = new StringBuffer("update exch_static_curve_data_org set ");
                                sb.append("point" + String.valueOf(startPoint) + "=? ");
                                for (int j = 1; j <= (endPoint - startPoint); j++) {
                                    sb.append(",");
                                    sb.append("point" + String.valueOf(startPoint + j) + "=? ");
                                }
                                sb.append("where extend_value=? and curve_type=? and curve_tgt_type=? and curve_org_no=?");
                                String sql2 = sb.toString();

                                Map<Integer, BigDecimal> map = new HashMap();

                                for (int k = 1; k <= 96; k++) {
                                    BigDecimal fieldValue = (BigDecimal) ReflectUtil.getFieldValue(timeBaseLine, "P" + k);
                                    map.put(k, fieldValue);
                                }


                                preparedStatement2 = connection.prepareStatement(sql2);
                                preparedStatement2.setBigDecimal(1, map.get(startPoint));
                                int count2 = 1;
                                for (int h = 1; h <= (endPoint - startPoint); h++) {
                                    preparedStatement2.setBigDecimal(h + 1, map.get(startPoint + h));
                                    count2++;
                                }
                                preparedStatement2.setString(count2 + 1, param);
                                preparedStatement2.setString(count2 + 2, "101");
                                preparedStatement2.setString(count2 + 3, "207_1");
                                preparedStatement2.setString(count2 + 4, "34101");

                                preparedStatement2.executeUpdate();


                            } else {
                                String curveId = orgId.toString() + "_101_207_" + count + pDate.format(dateTimeFormatter);
                                preparedStatement.setString(1, curveId);
                                preparedStatement.setString(2, orgId.toString());
                                preparedStatement.setString(3, provinceCode);
                                preparedStatement.setString(4, "");
                                preparedStatement.setString(5, "207_" + count);
                                preparedStatement.setString(6, "101");
                                preparedStatement.setString(7, "1");
                                preparedStatement.setDate(8, new java.sql.Date(System.currentTimeMillis()));
                                preparedStatement.setBigDecimal(9, p.getP1());
                                preparedStatement.setBigDecimal(10, p.getP2());
                                preparedStatement.setBigDecimal(11, p.getP3());
                                preparedStatement.setBigDecimal(12, p.getP4());
                                preparedStatement.setBigDecimal(13, p.getP5());
                                preparedStatement.setBigDecimal(14, p.getP6());
                                preparedStatement.setBigDecimal(15, p.getP7());
                                preparedStatement.setBigDecimal(16, p.getP8());
                                preparedStatement.setBigDecimal(17, p.getP9());
                                preparedStatement.setBigDecimal(18, p.getP10());
                                preparedStatement.setBigDecimal(19, p.getP11());
                                preparedStatement.setBigDecimal(20, p.getP12());
                                preparedStatement.setBigDecimal(21, p.getP13());
                                preparedStatement.setBigDecimal(22, p.getP14());
                                preparedStatement.setBigDecimal(23, p.getP15());
                                preparedStatement.setBigDecimal(24, p.getP16());
                                preparedStatement.setBigDecimal(25, p.getP17());
                                preparedStatement.setBigDecimal(26, p.getP18());
                                preparedStatement.setBigDecimal(27, p.getP19());
                                preparedStatement.setBigDecimal(28, p.getP20());
                                preparedStatement.setBigDecimal(29, p.getP21());
                                preparedStatement.setBigDecimal(30, p.getP22());
                                preparedStatement.setBigDecimal(31, p.getP23());
                                preparedStatement.setBigDecimal(32, p.getP24());
                                preparedStatement.setBigDecimal(33, p.getP25());
                                preparedStatement.setBigDecimal(34, p.getP26());
                                preparedStatement.setBigDecimal(35, p.getP27());
                                preparedStatement.setBigDecimal(36, p.getP28());
                                preparedStatement.setBigDecimal(37, p.getP29());
                                preparedStatement.setBigDecimal(38, p.getP30());
                                preparedStatement.setBigDecimal(39, p.getP31());
                                preparedStatement.setBigDecimal(40, p.getP32());
                                preparedStatement.setBigDecimal(41, p.getP33());
                                preparedStatement.setBigDecimal(42, p.getP34());
                                preparedStatement.setBigDecimal(43, p.getP35());
                                preparedStatement.setBigDecimal(44, p.getP36());
                                preparedStatement.setBigDecimal(45, p.getP37());
                                preparedStatement.setBigDecimal(46, p.getP38());
                                preparedStatement.setBigDecimal(47, p.getP39());
                                preparedStatement.setBigDecimal(48, p.getP40());
                                preparedStatement.setBigDecimal(49, p.getP41());
                                preparedStatement.setBigDecimal(50, p.getP42());
                                preparedStatement.setBigDecimal(51, p.getP43());
                                preparedStatement.setBigDecimal(52, p.getP44());
                                preparedStatement.setBigDecimal(53, p.getP45());
                                preparedStatement.setBigDecimal(54, p.getP46());
                                preparedStatement.setBigDecimal(55, p.getP47());
                                preparedStatement.setBigDecimal(56, p.getP48());
                                preparedStatement.setBigDecimal(57, p.getP49());
                                preparedStatement.setBigDecimal(58, p.getP50());
                                preparedStatement.setBigDecimal(59, p.getP51());
                                preparedStatement.setBigDecimal(60, p.getP52());
                                preparedStatement.setBigDecimal(61, p.getP53());
                                preparedStatement.setBigDecimal(62, p.getP54());
                                preparedStatement.setBigDecimal(63, p.getP55());
                                preparedStatement.setBigDecimal(64, p.getP56());
                                preparedStatement.setBigDecimal(65, p.getP57());
                                preparedStatement.setBigDecimal(66, p.getP58());
                                preparedStatement.setBigDecimal(67, p.getP59());
                                preparedStatement.setBigDecimal(68, p.getP60());
                                preparedStatement.setBigDecimal(69, p.getP61());
                                preparedStatement.setBigDecimal(70, p.getP62());
                                preparedStatement.setBigDecimal(71, p.getP63());
                                preparedStatement.setBigDecimal(72, p.getP64());
                                preparedStatement.setBigDecimal(73, p.getP65());
                                preparedStatement.setBigDecimal(74, p.getP66());
                                preparedStatement.setBigDecimal(75, p.getP67());
                                preparedStatement.setBigDecimal(76, p.getP68());
                                preparedStatement.setBigDecimal(77, p.getP69());
                                preparedStatement.setBigDecimal(78, p.getP70());
                                preparedStatement.setBigDecimal(79, p.getP71());
                                preparedStatement.setBigDecimal(80, p.getP72());
                                preparedStatement.setBigDecimal(81, p.getP73());
                                preparedStatement.setBigDecimal(82, p.getP74());
                                preparedStatement.setBigDecimal(83, p.getP75());
                                preparedStatement.setBigDecimal(84, p.getP76());
                                preparedStatement.setBigDecimal(85, p.getP77());
                                preparedStatement.setBigDecimal(86, p.getP78());
                                preparedStatement.setBigDecimal(87, p.getP79());
                                preparedStatement.setBigDecimal(88, p.getP80());
                                preparedStatement.setBigDecimal(89, p.getP81());
                                preparedStatement.setBigDecimal(90, p.getP82());
                                preparedStatement.setBigDecimal(91, p.getP83());
                                preparedStatement.setBigDecimal(92, p.getP84());
                                preparedStatement.setBigDecimal(93, p.getP85());
                                preparedStatement.setBigDecimal(94, p.getP86());
                                preparedStatement.setBigDecimal(95, p.getP87());
                                preparedStatement.setBigDecimal(96, p.getP88());
                                preparedStatement.setBigDecimal(97, p.getP89());
                                preparedStatement.setBigDecimal(98, p.getP90());
                                preparedStatement.setBigDecimal(99, p.getP91());
                                preparedStatement.setBigDecimal(100, p.getP92());
                                preparedStatement.setBigDecimal(101, p.getP93());
                                preparedStatement.setBigDecimal(102, p.getP94());
                                preparedStatement.setBigDecimal(103, p.getP95());
                                preparedStatement.setBigDecimal(104, p.getP96());
                                preparedStatement.setInt(105, 4);
                                preparedStatement.setString(106, p.getEventId());
                                preparedStatement.setDate(107, new java.sql.Date(System.currentTimeMillis()));
                                preparedStatement.setDate(108, new java.sql.Date(System.currentTimeMillis()));
                                preparedStatement.setDate(109, new java.sql.Date(System.currentTimeMillis()));
                                preparedStatement.executeUpdate();
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                    }


                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


    }


    public void saveFrozenToDatabase2(String param) {

        Connection connection = getConnection();
        PreparedStatement preparedStatement = null;
        PreparedStatement preparedStatement2 = null;
        PreparedStatement preparedStatement3 = null;
        PreparedStatement preparedStatement4 = null;
        PreparedStatement preparedStatement5 = null;

        String dateStr = baselineAndMeasurementMapper.getTime(param);
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate pDate = LocalDate.parse(dateStr, df);

        ZoneId zone = ZoneId.systemDefault();
        Instant instant = pDate.atStartOfDay().atZone(zone).toInstant();
        java.util.Date da = Date.from(instant);
        long datems = da.getTime();
        java.sql.Date sqlDate = new java.sql.Date(datems);


        try {
            List orgIds = getEventIDSByDate().get("orgIds");
//            LocalDate localDate = LocalDate.now();
//            LocalDate localDate1 = localDate.plusDays(-1);
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");


            List<Long> eventIds = new ArrayList<>();
            eventIds.add(Long.valueOf(param));

            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -1);
            Date d = cal.getTime();

            for (Object orgId : orgIds) {
                if (getProvinceCode().equals(orgId.toString())) {
                    int count = 0;
                    List<Point96Vo> frozenData = this.getFrozenRealData2(orgId.toString(), eventIds);
                    if (frozenData != null && frozenData.size() > 0) {
                        for (Point96Vo p : frozenData) {
                            count++;
                            String sql = "insert into exch_static_curve_data_org VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?" +
                                    ",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?" +
                                    ",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                            String sql2 = "select * from  exch_rt_curve_data_org where curve_id=?";


                            DrEventTime eventTime = baselineAndMeasurementMapper.getEventTime2(param);

                            int startPoint = CurveUtil.covDateTimeToPoint(eventTime.getStartTime());
                            int endPoint = CurveUtil.covDateTimeToPoint(eventTime.getEndTime());

                            Point96Vo timeBaseLine = getTimeBaseLine(param);

                            StringBuffer sb = new StringBuffer("update exch_static_curve_data_org set ");
                            sb.append("point" + String.valueOf(startPoint) + "=? ");
                            for (int j = 1; j <= (endPoint - startPoint); j++) {
                                sb.append(",");
                                sb.append("point" + String.valueOf(startPoint + j) + "=? ");
                            }
                            sb.append("where extend_value=? and curve_type=?");
                            String sql3 = sb.toString();


                            try {
                                preparedStatement = connection.prepareStatement(sql);
                                String curveId = orgId + "_102_207_" + count + pDate.format(dateTimeFormatter);
                                preparedStatement.setString(1, curveId);
                                preparedStatement.setString(2, orgId.toString());
                                preparedStatement.setString(3, "");
                                preparedStatement.setString(4, "");
                                preparedStatement.setString(5, "207_" + count);
                                preparedStatement.setString(6, "102");
                                preparedStatement.setString(7, "1");
                                preparedStatement.setDate(8, sqlDate);
                                preparedStatement.setBigDecimal(9, p.getP1());
                                preparedStatement.setBigDecimal(10, p.getP2());
                                preparedStatement.setBigDecimal(11, p.getP3());
                                preparedStatement.setBigDecimal(12, p.getP4());
                                preparedStatement.setBigDecimal(13, p.getP5());
                                preparedStatement.setBigDecimal(14, p.getP6());
                                preparedStatement.setBigDecimal(15, p.getP7());
                                preparedStatement.setBigDecimal(16, p.getP8());
                                preparedStatement.setBigDecimal(17, p.getP9());
                                preparedStatement.setBigDecimal(18, p.getP10());
                                preparedStatement.setBigDecimal(19, p.getP11());
                                preparedStatement.setBigDecimal(20, p.getP12());
                                preparedStatement.setBigDecimal(21, p.getP13());
                                preparedStatement.setBigDecimal(22, p.getP14());
                                preparedStatement.setBigDecimal(23, p.getP15());
                                preparedStatement.setBigDecimal(24, p.getP16());
                                preparedStatement.setBigDecimal(25, p.getP17());
                                preparedStatement.setBigDecimal(26, p.getP18());
                                preparedStatement.setBigDecimal(27, p.getP19());
                                preparedStatement.setBigDecimal(28, p.getP20());
                                preparedStatement.setBigDecimal(29, p.getP21());
                                preparedStatement.setBigDecimal(30, p.getP22());
                                preparedStatement.setBigDecimal(31, p.getP23());
                                preparedStatement.setBigDecimal(32, p.getP24());
                                preparedStatement.setBigDecimal(33, p.getP25());
                                preparedStatement.setBigDecimal(34, p.getP26());
                                preparedStatement.setBigDecimal(35, p.getP27());
                                preparedStatement.setBigDecimal(36, p.getP28());
                                preparedStatement.setBigDecimal(37, p.getP29());
                                preparedStatement.setBigDecimal(38, p.getP30());
                                preparedStatement.setBigDecimal(39, p.getP31());
                                preparedStatement.setBigDecimal(40, p.getP32());
                                preparedStatement.setBigDecimal(41, p.getP33());
                                preparedStatement.setBigDecimal(42, p.getP34());
                                preparedStatement.setBigDecimal(43, p.getP35());
                                preparedStatement.setBigDecimal(44, p.getP36());
                                preparedStatement.setBigDecimal(45, p.getP37());
                                preparedStatement.setBigDecimal(46, p.getP38());
                                preparedStatement.setBigDecimal(47, p.getP39());
                                preparedStatement.setBigDecimal(48, p.getP40());
                                preparedStatement.setBigDecimal(49, p.getP41());
                                preparedStatement.setBigDecimal(50, p.getP42());
                                preparedStatement.setBigDecimal(51, p.getP43());
                                preparedStatement.setBigDecimal(52, p.getP44());
                                preparedStatement.setBigDecimal(53, p.getP45());
                                preparedStatement.setBigDecimal(54, p.getP46());
                                preparedStatement.setBigDecimal(55, p.getP47());
                                preparedStatement.setBigDecimal(56, p.getP48());
                                preparedStatement.setBigDecimal(57, p.getP49());
                                preparedStatement.setBigDecimal(58, p.getP50());
                                preparedStatement.setBigDecimal(59, p.getP51());
                                preparedStatement.setBigDecimal(60, p.getP52());
                                preparedStatement.setBigDecimal(61, p.getP53());
                                preparedStatement.setBigDecimal(62, p.getP54());
                                preparedStatement.setBigDecimal(63, p.getP55());
                                preparedStatement.setBigDecimal(64, p.getP56());
                                preparedStatement.setBigDecimal(65, p.getP57());
                                preparedStatement.setBigDecimal(66, p.getP58());
                                preparedStatement.setBigDecimal(67, p.getP59());
                                preparedStatement.setBigDecimal(68, p.getP60());
                                preparedStatement.setBigDecimal(69, p.getP61());
                                preparedStatement.setBigDecimal(70, p.getP62());
                                preparedStatement.setBigDecimal(71, p.getP63());
                                preparedStatement.setBigDecimal(72, p.getP64());
                                preparedStatement.setBigDecimal(73, p.getP65());
                                preparedStatement.setBigDecimal(74, p.getP66());
                                preparedStatement.setBigDecimal(75, p.getP67());
                                preparedStatement.setBigDecimal(76, p.getP68());
                                preparedStatement.setBigDecimal(77, p.getP69());
                                preparedStatement.setBigDecimal(78, p.getP70());
                                preparedStatement.setBigDecimal(79, p.getP71());
                                preparedStatement.setBigDecimal(80, p.getP72());
                                preparedStatement.setBigDecimal(81, p.getP73());
                                preparedStatement.setBigDecimal(82, p.getP74());
                                preparedStatement.setBigDecimal(83, p.getP75());
                                preparedStatement.setBigDecimal(84, p.getP76());
                                preparedStatement.setBigDecimal(85, p.getP77());
                                preparedStatement.setBigDecimal(86, p.getP78());
                                preparedStatement.setBigDecimal(87, p.getP79());
                                preparedStatement.setBigDecimal(88, p.getP80());
                                preparedStatement.setBigDecimal(89, p.getP81());
                                preparedStatement.setBigDecimal(90, p.getP82());
                                preparedStatement.setBigDecimal(91, p.getP83());
                                preparedStatement.setBigDecimal(92, p.getP84());
                                preparedStatement.setBigDecimal(93, p.getP85());
                                preparedStatement.setBigDecimal(94, p.getP86());
                                preparedStatement.setBigDecimal(95, p.getP87());
                                preparedStatement.setBigDecimal(96, p.getP88());
                                preparedStatement.setBigDecimal(97, p.getP89());
                                preparedStatement.setBigDecimal(98, p.getP90());
                                preparedStatement.setBigDecimal(99, p.getP91());
                                preparedStatement.setBigDecimal(100, p.getP92());
                                preparedStatement.setBigDecimal(101, p.getP93());
                                preparedStatement.setBigDecimal(102, p.getP94());
                                preparedStatement.setBigDecimal(103, p.getP95());
                                preparedStatement.setBigDecimal(104, p.getP96());
                                preparedStatement.setInt(105, 4);
                                preparedStatement.setString(106, p.getEventId());
                                preparedStatement.setDate(107, new java.sql.Date(System.currentTimeMillis()));
                                preparedStatement.setDate(108, new java.sql.Date(System.currentTimeMillis()));
                                preparedStatement.setDate(109, new java.sql.Date(System.currentTimeMillis()));
                                preparedStatement.executeUpdate();

                                preparedStatement2 = connection.prepareStatement(sql2);
                                String curvId = orgId + "_103_207_" + count + pDate.format(dateTimeFormatter);
                                preparedStatement2.setString(1, curvId);
                                ResultSet resultSet = preparedStatement2.executeQuery();

                                preparedStatement3 = connection.prepareStatement(sql);

                                while (resultSet.next()) {
                                    preparedStatement3.setString(1, resultSet.getString("curve_id"));
                                    preparedStatement3.setString(2, resultSet.getString("curve_org_no"));
                                    preparedStatement3.setString(3, resultSet.getString("curve_p_org_no"));
                                    preparedStatement3.setString(4, resultSet.getString("curve_cons_no"));
                                    preparedStatement3.setString(5, resultSet.getString("curve_tgt_type"));
                                    preparedStatement3.setString(6, resultSet.getString("curve_type"));
                                    preparedStatement3.setString(7, resultSet.getString("stats_freq"));
                                    preparedStatement3.setDate(8, resultSet.getDate("data_date"));
                                    preparedStatement3.setBigDecimal(9, resultSet.getBigDecimal("point1"));
                                    preparedStatement3.setBigDecimal(10, resultSet.getBigDecimal("point2"));
                                    preparedStatement3.setBigDecimal(11, resultSet.getBigDecimal("point3"));
                                    preparedStatement3.setBigDecimal(12, resultSet.getBigDecimal("point4"));
                                    preparedStatement3.setBigDecimal(13, resultSet.getBigDecimal("point5"));
                                    preparedStatement3.setBigDecimal(14, resultSet.getBigDecimal("point6"));
                                    preparedStatement3.setBigDecimal(15, resultSet.getBigDecimal("point7"));
                                    preparedStatement3.setBigDecimal(16, resultSet.getBigDecimal("point8"));
                                    preparedStatement3.setBigDecimal(17, resultSet.getBigDecimal("point9"));
                                    preparedStatement3.setBigDecimal(18, resultSet.getBigDecimal("point10"));
                                    preparedStatement3.setBigDecimal(19, resultSet.getBigDecimal("point11"));
                                    preparedStatement3.setBigDecimal(20, resultSet.getBigDecimal("point12"));
                                    preparedStatement3.setBigDecimal(21, resultSet.getBigDecimal("point13"));
                                    preparedStatement3.setBigDecimal(22, resultSet.getBigDecimal("point14"));
                                    preparedStatement3.setBigDecimal(23, resultSet.getBigDecimal("point15"));
                                    preparedStatement3.setBigDecimal(24, resultSet.getBigDecimal("point16"));
                                    preparedStatement3.setBigDecimal(25, resultSet.getBigDecimal("point17"));
                                    preparedStatement3.setBigDecimal(26, resultSet.getBigDecimal("point18"));
                                    preparedStatement3.setBigDecimal(27, resultSet.getBigDecimal("point19"));
                                    preparedStatement3.setBigDecimal(28, resultSet.getBigDecimal("point20"));
                                    preparedStatement3.setBigDecimal(29, resultSet.getBigDecimal("point21"));
                                    preparedStatement3.setBigDecimal(30, resultSet.getBigDecimal("point22"));
                                    preparedStatement3.setBigDecimal(31, resultSet.getBigDecimal("point23"));
                                    preparedStatement3.setBigDecimal(32, resultSet.getBigDecimal("point24"));
                                    preparedStatement3.setBigDecimal(33, resultSet.getBigDecimal("point25"));
                                    preparedStatement3.setBigDecimal(34, resultSet.getBigDecimal("point26"));
                                    preparedStatement3.setBigDecimal(35, resultSet.getBigDecimal("point27"));
                                    preparedStatement3.setBigDecimal(36, resultSet.getBigDecimal("point28"));
                                    preparedStatement3.setBigDecimal(37, resultSet.getBigDecimal("point29"));
                                    preparedStatement3.setBigDecimal(38, resultSet.getBigDecimal("point30"));
                                    preparedStatement3.setBigDecimal(39, resultSet.getBigDecimal("point31"));
                                    preparedStatement3.setBigDecimal(40, resultSet.getBigDecimal("point32"));
                                    preparedStatement3.setBigDecimal(41, resultSet.getBigDecimal("point33"));
                                    preparedStatement3.setBigDecimal(42, resultSet.getBigDecimal("point34"));
                                    preparedStatement3.setBigDecimal(43, resultSet.getBigDecimal("point35"));
                                    preparedStatement3.setBigDecimal(44, resultSet.getBigDecimal("point36"));
                                    preparedStatement3.setBigDecimal(45, resultSet.getBigDecimal("point37"));
                                    preparedStatement3.setBigDecimal(46, resultSet.getBigDecimal("point38"));
                                    preparedStatement3.setBigDecimal(47, resultSet.getBigDecimal("point39"));
                                    preparedStatement3.setBigDecimal(48, resultSet.getBigDecimal("point40"));
                                    preparedStatement3.setBigDecimal(49, resultSet.getBigDecimal("point41"));
                                    preparedStatement3.setBigDecimal(50, resultSet.getBigDecimal("point42"));
                                    preparedStatement3.setBigDecimal(51, resultSet.getBigDecimal("point43"));
                                    preparedStatement3.setBigDecimal(52, resultSet.getBigDecimal("point44"));
                                    preparedStatement3.setBigDecimal(53, resultSet.getBigDecimal("point45"));
                                    preparedStatement3.setBigDecimal(54, resultSet.getBigDecimal("point46"));
                                    preparedStatement3.setBigDecimal(55, resultSet.getBigDecimal("point47"));
                                    preparedStatement3.setBigDecimal(56, resultSet.getBigDecimal("point48"));
                                    preparedStatement3.setBigDecimal(57, resultSet.getBigDecimal("point49"));
                                    preparedStatement3.setBigDecimal(58, resultSet.getBigDecimal("point50"));
                                    preparedStatement3.setBigDecimal(59, resultSet.getBigDecimal("point51"));
                                    preparedStatement3.setBigDecimal(60, resultSet.getBigDecimal("point52"));
                                    preparedStatement3.setBigDecimal(61, resultSet.getBigDecimal("point53"));
                                    preparedStatement3.setBigDecimal(62, resultSet.getBigDecimal("point54"));
                                    preparedStatement3.setBigDecimal(63, resultSet.getBigDecimal("point55"));
                                    preparedStatement3.setBigDecimal(64, resultSet.getBigDecimal("point56"));
                                    preparedStatement3.setBigDecimal(65, resultSet.getBigDecimal("point57"));
                                    preparedStatement3.setBigDecimal(66, resultSet.getBigDecimal("point58"));
                                    preparedStatement3.setBigDecimal(67, resultSet.getBigDecimal("point59"));
                                    preparedStatement3.setBigDecimal(68, resultSet.getBigDecimal("point60"));
                                    preparedStatement3.setBigDecimal(69, resultSet.getBigDecimal("point61"));
                                    preparedStatement3.setBigDecimal(70, resultSet.getBigDecimal("point62"));
                                    preparedStatement3.setBigDecimal(71, resultSet.getBigDecimal("point63"));
                                    preparedStatement3.setBigDecimal(72, resultSet.getBigDecimal("point64"));
                                    preparedStatement3.setBigDecimal(73, resultSet.getBigDecimal("point65"));
                                    preparedStatement3.setBigDecimal(74, resultSet.getBigDecimal("point66"));
                                    preparedStatement3.setBigDecimal(75, resultSet.getBigDecimal("point67"));
                                    preparedStatement3.setBigDecimal(76, resultSet.getBigDecimal("point68"));
                                    preparedStatement3.setBigDecimal(77, resultSet.getBigDecimal("point69"));
                                    preparedStatement3.setBigDecimal(78, resultSet.getBigDecimal("point70"));
                                    preparedStatement3.setBigDecimal(79, resultSet.getBigDecimal("point71"));
                                    preparedStatement3.setBigDecimal(80, resultSet.getBigDecimal("point72"));
                                    preparedStatement3.setBigDecimal(81, resultSet.getBigDecimal("point73"));
                                    preparedStatement3.setBigDecimal(82, resultSet.getBigDecimal("point74"));
                                    preparedStatement3.setBigDecimal(83, resultSet.getBigDecimal("point75"));
                                    preparedStatement3.setBigDecimal(84, resultSet.getBigDecimal("point76"));
                                    preparedStatement3.setBigDecimal(85, resultSet.getBigDecimal("point77"));
                                    preparedStatement3.setBigDecimal(86, resultSet.getBigDecimal("point78"));
                                    preparedStatement3.setBigDecimal(87, resultSet.getBigDecimal("point79"));
                                    preparedStatement3.setBigDecimal(88, resultSet.getBigDecimal("point80"));
                                    preparedStatement3.setBigDecimal(89, resultSet.getBigDecimal("point81"));
                                    preparedStatement3.setBigDecimal(90, resultSet.getBigDecimal("point82"));
                                    preparedStatement3.setBigDecimal(91, resultSet.getBigDecimal("point83"));
                                    preparedStatement3.setBigDecimal(92, resultSet.getBigDecimal("point84"));
                                    preparedStatement3.setBigDecimal(93, resultSet.getBigDecimal("point85"));
                                    preparedStatement3.setBigDecimal(94, resultSet.getBigDecimal("point86"));
                                    preparedStatement3.setBigDecimal(95, resultSet.getBigDecimal("point87"));
                                    preparedStatement3.setBigDecimal(96, resultSet.getBigDecimal("point88"));
                                    preparedStatement3.setBigDecimal(97, resultSet.getBigDecimal("point89"));
                                    preparedStatement3.setBigDecimal(98, resultSet.getBigDecimal("point90"));
                                    preparedStatement3.setBigDecimal(99, resultSet.getBigDecimal("point91"));
                                    preparedStatement3.setBigDecimal(100, resultSet.getBigDecimal("point92"));
                                    preparedStatement3.setBigDecimal(101, resultSet.getBigDecimal("point93"));
                                    preparedStatement3.setBigDecimal(102, resultSet.getBigDecimal("point94"));
                                    preparedStatement3.setBigDecimal(103, resultSet.getBigDecimal("point95"));
                                    preparedStatement3.setBigDecimal(104, resultSet.getBigDecimal("point96"));
                                    preparedStatement3.setInt(105, resultSet.getInt("extend_flag"));
                                    preparedStatement3.setString(106, resultSet.getString("extend_value"));
                                    preparedStatement3.setDate(107, new java.sql.Date(System.currentTimeMillis()));
                                    preparedStatement3.setDate(108, new java.sql.Date(System.currentTimeMillis()));
                                    preparedStatement3.setDate(109, new java.sql.Date(System.currentTimeMillis()));

                                    preparedStatement3.executeUpdate();
                                }


                                String sql4 = "select * from exch_static_curve_data_org where extend_value=? and curve_type=? and curve_org_no=?";
                                preparedStatement4 = connection.prepareStatement(sql4);
                                preparedStatement4.setString(1, param);
                                preparedStatement4.setString(2, "101");
                                preparedStatement4.setString(3, "34101");
                                ResultSet resultSet2 = preparedStatement4.executeQuery();
                                Point96Vo point96Vo = new Point96Vo();
                                while (resultSet2.next()) {
                                    point96Vo.setP1(resultSet2.getBigDecimal("point1"));
                                    point96Vo.setP2(resultSet2.getBigDecimal("point2"));
                                    point96Vo.setP3(resultSet2.getBigDecimal("point3"));
                                    point96Vo.setP4(resultSet2.getBigDecimal("point4"));
                                    point96Vo.setP5(resultSet2.getBigDecimal("point5"));
                                    point96Vo.setP6(resultSet2.getBigDecimal("point6"));
                                    point96Vo.setP7(resultSet2.getBigDecimal("point7"));
                                    point96Vo.setP8(resultSet2.getBigDecimal("point8"));
                                    point96Vo.setP9(resultSet2.getBigDecimal("point9"));
                                    point96Vo.setP10(resultSet2.getBigDecimal("point10"));
                                    point96Vo.setP11(resultSet2.getBigDecimal("point11"));
                                    point96Vo.setP12(resultSet2.getBigDecimal("point12"));
                                    point96Vo.setP13(resultSet2.getBigDecimal("point13"));
                                    point96Vo.setP14(resultSet2.getBigDecimal("point14"));
                                    point96Vo.setP15(resultSet2.getBigDecimal("point15"));
                                    point96Vo.setP16(resultSet2.getBigDecimal("point16"));
                                    point96Vo.setP17(resultSet2.getBigDecimal("point17"));
                                    point96Vo.setP18(resultSet2.getBigDecimal("point18"));
                                    point96Vo.setP19(resultSet2.getBigDecimal("point19"));
                                    point96Vo.setP20(resultSet2.getBigDecimal("point20"));
                                    point96Vo.setP21(resultSet2.getBigDecimal("point21"));
                                    point96Vo.setP22(resultSet2.getBigDecimal("point22"));
                                    point96Vo.setP23(resultSet2.getBigDecimal("point23"));
                                    point96Vo.setP24(resultSet2.getBigDecimal("point24"));
                                    point96Vo.setP25(resultSet2.getBigDecimal("point25"));
                                    point96Vo.setP26(resultSet2.getBigDecimal("point26"));
                                    point96Vo.setP27(resultSet2.getBigDecimal("point27"));
                                    point96Vo.setP28(resultSet2.getBigDecimal("point28"));
                                    point96Vo.setP29(resultSet2.getBigDecimal("point29"));
                                    point96Vo.setP30(resultSet2.getBigDecimal("point30"));
                                    point96Vo.setP31(resultSet2.getBigDecimal("point31"));
                                    point96Vo.setP32(resultSet2.getBigDecimal("point32"));
                                    point96Vo.setP33(resultSet2.getBigDecimal("point33"));
                                    point96Vo.setP34(resultSet2.getBigDecimal("point34"));
                                    point96Vo.setP35(resultSet2.getBigDecimal("point35"));
                                    point96Vo.setP36(resultSet2.getBigDecimal("point36"));
                                    point96Vo.setP37(resultSet2.getBigDecimal("point37"));
                                    point96Vo.setP38(resultSet2.getBigDecimal("point38"));
                                    point96Vo.setP39(resultSet2.getBigDecimal("point39"));
                                    point96Vo.setP40(resultSet2.getBigDecimal("point40"));
                                    point96Vo.setP41(resultSet2.getBigDecimal("point41"));
                                    point96Vo.setP42(resultSet2.getBigDecimal("point42"));
                                    point96Vo.setP43(resultSet2.getBigDecimal("point43"));
                                    point96Vo.setP44(resultSet2.getBigDecimal("point44"));
                                    point96Vo.setP45(resultSet2.getBigDecimal("point45"));
                                    point96Vo.setP46(resultSet2.getBigDecimal("point46"));
                                    point96Vo.setP47(resultSet2.getBigDecimal("point47"));
                                    point96Vo.setP48(resultSet2.getBigDecimal("point48"));
                                    point96Vo.setP49(resultSet2.getBigDecimal("point49"));
                                    point96Vo.setP50(resultSet2.getBigDecimal("point50"));
                                    point96Vo.setP51(resultSet2.getBigDecimal("point51"));
                                    point96Vo.setP52(resultSet2.getBigDecimal("point52"));
                                    point96Vo.setP53(resultSet2.getBigDecimal("point53"));
                                    point96Vo.setP54(resultSet2.getBigDecimal("point54"));
                                    point96Vo.setP55(resultSet2.getBigDecimal("point55"));
                                    point96Vo.setP56(resultSet2.getBigDecimal("point56"));
                                    point96Vo.setP57(resultSet2.getBigDecimal("point57"));
                                    point96Vo.setP58(resultSet2.getBigDecimal("point58"));
                                    point96Vo.setP59(resultSet2.getBigDecimal("point59"));
                                    point96Vo.setP60(resultSet2.getBigDecimal("point60"));
                                    point96Vo.setP61(resultSet2.getBigDecimal("point61"));
                                    point96Vo.setP62(resultSet2.getBigDecimal("point62"));
                                    point96Vo.setP63(resultSet2.getBigDecimal("point63"));
                                    point96Vo.setP64(resultSet2.getBigDecimal("point64"));
                                    point96Vo.setP65(resultSet2.getBigDecimal("point65"));
                                    point96Vo.setP66(resultSet2.getBigDecimal("point66"));
                                    point96Vo.setP67(resultSet2.getBigDecimal("point67"));
                                    point96Vo.setP68(resultSet2.getBigDecimal("point68"));
                                    point96Vo.setP69(resultSet2.getBigDecimal("point69"));
                                    point96Vo.setP70(resultSet2.getBigDecimal("point70"));
                                    point96Vo.setP71(resultSet2.getBigDecimal("point71"));
                                    point96Vo.setP72(resultSet2.getBigDecimal("point72"));
                                    point96Vo.setP73(resultSet2.getBigDecimal("point73"));
                                    point96Vo.setP74(resultSet2.getBigDecimal("point74"));
                                    point96Vo.setP75(resultSet2.getBigDecimal("point75"));
                                    point96Vo.setP76(resultSet2.getBigDecimal("point76"));
                                    point96Vo.setP77(resultSet2.getBigDecimal("point77"));
                                    point96Vo.setP78(resultSet2.getBigDecimal("point78"));
                                    point96Vo.setP79(resultSet2.getBigDecimal("point79"));
                                    point96Vo.setP80(resultSet2.getBigDecimal("point80"));
                                    point96Vo.setP81(resultSet2.getBigDecimal("point81"));
                                    point96Vo.setP82(resultSet2.getBigDecimal("point82"));
                                    point96Vo.setP83(resultSet2.getBigDecimal("point83"));
                                    point96Vo.setP84(resultSet2.getBigDecimal("point84"));
                                    point96Vo.setP85(resultSet2.getBigDecimal("point85"));
                                    point96Vo.setP86(resultSet2.getBigDecimal("point86"));
                                    point96Vo.setP87(resultSet2.getBigDecimal("point87"));
                                    point96Vo.setP88(resultSet2.getBigDecimal("point88"));
                                    point96Vo.setP89(resultSet2.getBigDecimal("point89"));
                                    point96Vo.setP90(resultSet2.getBigDecimal("point90"));
                                    point96Vo.setP91(resultSet2.getBigDecimal("point91"));
                                    point96Vo.setP92(resultSet2.getBigDecimal("point92"));
                                    point96Vo.setP93(resultSet2.getBigDecimal("point93"));
                                    point96Vo.setP94(resultSet2.getBigDecimal("point94"));
                                    point96Vo.setP95(resultSet2.getBigDecimal("point95"));
                                    point96Vo.setP96(resultSet2.getBigDecimal("point96"));

                                }

                                List<Point96Vo> realTimeData = this.getRealTimeDataByParamTimeAndEventId("34101", param);

                                int j = -1;
                                Map<Integer, BigDecimal> map2 = new HashMap();
                                for (Point96Vo pp : realTimeData) {
                                    j++;
                                    for (int k = 1; k <= 96; k++) {
                                        BigDecimal fieldValue = (BigDecimal) ReflectUtil.getFieldValue(point96Vo, "P" + k);
                                        BigDecimal fieldValue2 = (BigDecimal) ReflectUtil.getFieldValue(pp, "P" + k);
                                        if (ObjectUtil.isNotEmpty(fieldValue) && ObjectUtil.isNotEmpty(fieldValue2)) {
                                            map2.put(k, fieldValue.subtract(fieldValue2));
                                        }
                                    }
                                }


                                StringBuffer sb2 = new StringBuffer("update exch_static_curve_data_org set ");
                                sb2.append("point" + String.valueOf(startPoint) + "=? ");
                                for (int jj = 1; jj <= (endPoint - startPoint); jj++) {
                                    sb2.append(",");
                                    sb2.append("point" + String.valueOf(startPoint + jj) + "=? ");
                                }
                                sb2.append("where curve_id=? and curve_type=?");
                                String sql6 = sb2.toString();

                                preparedStatement5 = connection.prepareStatement(sql6);
                                preparedStatement5.setBigDecimal(1, map2.get(startPoint));
                                int count2 = 1;
                                for (int h = 1; h <= (endPoint - startPoint); h++) {
                                    preparedStatement5.setBigDecimal(h + 1, map2.get(startPoint + h));
                                    count2++;
                                }
                                preparedStatement5.setString(count2 + 1, curvId);
                                preparedStatement5.setString(count2 + 2, "103");

                                preparedStatement5.executeUpdate();


                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (preparedStatement2 != null) {
                try {
                    preparedStatement2.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (preparedStatement3 != null) {
                try {
                    preparedStatement3.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}