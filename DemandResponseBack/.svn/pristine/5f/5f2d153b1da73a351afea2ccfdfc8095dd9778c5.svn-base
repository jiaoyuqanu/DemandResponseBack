package com.xqxy.executor.service.jobhandler;

import com.alibaba.fastjson.JSONObject;
import com.xqxy.core.client.FileClient;
import com.xqxy.core.client.SystemClient;
import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.data.OrgNoCurve;
import com.xqxy.dr.modular.data.mapper.ConsCurveMapper;
import com.xqxy.dr.modular.evaluation.mapper.ConsEvaluationMapper;
import com.xqxy.dr.modular.project.mapper.ConsContractInfoMapper;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@Component
public class ExceCurveJob {

    @Value("${exchCurve.jdbcUrl:}")
    private String mdtfJdbcUrl;

    @Value("${exchCurve.username:}")
    private String mdtfUser;

    @Value("${exchCurve.password:}")
    private String mdtfPassword;

    private JdbcTemplate jdbcTemplate;
    @Resource
    private SystemClient systemClient;
    @Resource
    private ConsCurveMapper consCurveMapper;


    @PostConstruct
    public void init() {
        DataSource dataSource = DataSourceBuilder.create()
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .url(mdtfJdbcUrl)
                .username(mdtfUser)
                .password(mdtfPassword)
                .build();
        this.jdbcTemplate = new JdbcTemplate(dataSource);
//        System.out.println(this.jdbcTemplate.queryForList(""));
    }


    @XxlJob("execOrgCurve")
    public ReturnT<String> execOrgCurve(String param) throws IOException {
        Properties properties = new Properties();
        properties.load(new StringReader(param));
        this.getData(properties.getProperty("curveTgtType"), properties.getProperty("curveType"),
                Integer.valueOf(properties.getProperty("dayType")), Boolean.valueOf(properties.getProperty("isDoubleHigh")));
        return ReturnT.SUCCESS;
    }

    public void getData(String curveTgtType, String curveType, Integer dayType, Boolean isDoubleHigh) {
        Map<String, String> orgParent = new HashMap<>();
        for (int i = 1; i <= 17; i++) {
            String orgNo = String.format("344%02d", i);
            List<String> allNextOrg = getAllNextOrg(orgNo);
            allNextOrg.forEach(item -> orgParent.put(item, orgNo));
        }
        log.info(orgParent.toString());

        Boolean isToday;
        String date = null, start = null, end = null, tableName = null;
        if (dayType == 1) {
            isToday = Boolean.TRUE;
            date = LocalDate.now().toString();
            tableName = "exch_rt_curve_data_org";
        } else {
            isToday = Boolean.FALSE;
            LocalDate localDate = LocalDate.now();
            start = localDate.plusDays(-3 ).toString();
            end = localDate.plusDays(-1).toString();
            tableName = "exch_static_curve_data_org";
        }

        List<OrgNoCurve> orgNoCurves = consCurveMapper.getOrgNoCurve(isToday, isDoubleHigh, date, start, end);
        log.info(JSONObject.toJSONString(orgNoCurves));
        List<JSONObject> newOrgCurves = orgNoCurves.stream().filter(item -> StringUtils.isNotEmpty(item.getOrgNo()))
                .map(item -> {
                    item.setPOrgNo(orgParent.get(item.getOrgNo()));
                    return JSONObject.parseObject(JSONObject.toJSONString(item));
                })
                .filter(item -> StringUtils.isNotEmpty(item.getString("pOrgNo"))).collect(Collectors.toList());
        Map<String, JSONObject> orgNoCurveMap = newOrgCurves.stream().collect(Collectors.groupingBy(item -> item.getString("pOrgNo"),
                Collectors.reducing(new JSONObject(), (item1, item2) -> {
                    JSONObject jsonObject = setValue(item1, item2);
                    jsonObject.put("pOrgNo", "34101");
                    return jsonObject;
                })));
        JSONObject province = orgNoCurveMap.entrySet().stream().map(Map.Entry::getValue).reduce(new JSONObject(), (item1, item2) -> setValue(item1, item2));
        province.put("pOrgNo", "00000");
        province.put("orgNo", "34101");
        orgNoCurveMap.put("34101", province);
        log.info(JSONObject.toJSONString(orgNoCurveMap));

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        DateTimeFormatter dateTimeFormatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalTime nowTime = LocalTime.now().withNano(0).withSecond(0);
        LocalTime nowOffTime = nowTime.plusMinutes(-20);

        List<Map<String, Object>> values = orgNoCurveMap.entrySet().stream().map(item -> {
            Map<String, Object> map = new HashMap<>();
            JSONObject jsonObject = item.getValue();
            map.put("curve_id", item.getKey() + "_" + curveType + "_" + LocalDateTime.now().format(dateTimeFormatter));
            map.put("CURVE_ORG_NO", item.getKey());
            map.put("CURVE_P_ORG_NO", item.getValue().getString("pOrgNo"));
            map.put("CURVE_TYPE", curveType);
            map.put("CURVE_TGT_TYPE", curveTgtType);
            map.put("stats_freq", 1);
            map.put("data_date", LocalDate.now().toString());
            map.put("EXTEND_FLAG", 0);
            map.put("CREATE_TIME", LocalDateTime.now().format(dateTimeFormatter2));
            map.put("UPDATE_TIME", LocalDateTime.now().format(dateTimeFormatter2));
            LocalTime localTime = LocalTime.MIN;

            for (int i = 1; i <= 96; i++) {
                BigDecimal pNValue = jsonObject.getBigDecimal("p" + i);
                if (isToday && localTime.plusMinutes(15 * (i - 1)).isAfter(nowOffTime)) {
                    map.put("point" + i, null);
                } else {
                    if (pNValue != null) {
                        if (!isToday) {
                            pNValue = pNValue.divide(BigDecimal.valueOf(3), 2, RoundingMode.HALF_UP);
                        }
                        map.put("point" + i, pNValue.movePointLeft(4).setScale(2, RoundingMode.HALF_UP));
                    } else {
                        map.put("point" + i, BigDecimal.ZERO);
                    }
                }
            }
            return map;
        }).collect(Collectors.toList());

        System.out.println(values);
        String finalTableName = tableName;
        values.forEach(item -> {
            String sql = String.format("select curve_id from %s where CURVE_ORG_NO='%s' and CURVE_TGT_TYPE='%s' and CURVE_TYPE='%s' and DATA_DATE='%s'", finalTableName,
                    item.get("CURVE_ORG_NO"), item.get("CURVE_TGT_TYPE"), item.get("CURVE_TYPE"), item.get("data_date"));
            String idValue = jdbcTemplate.query(sql, new ResultSetExtractor<String>() {
                @Override
                public String extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                    if (resultSet.next()) {
                        return resultSet.getObject(1, String.class);
                    }
                    return null;
                }
            });
            String execSql = null;
            if (idValue == null) {
                execSql = generalInsertSql(finalTableName, item);
            } else {
                item.remove("CREATE_TIME");
                item.put("curve_id", idValue);
                execSql = generalUpdateSql(finalTableName, item, "curve_id");
            }
            log.info("execSql=> {}", execSql);
            jdbcTemplate.execute(execSql);
        });
    }

    public JSONObject setValue(JSONObject item1, JSONObject item2) {
        JSONObject jsonObject = new JSONObject();
        for (int i = 1; i <= 96; i++) {
            BigDecimal value1 = item1.getBigDecimal("p" + i);
            BigDecimal value2 = item2.getBigDecimal("p" + i);
            if (value1 == null) {
                value1 = BigDecimal.ZERO;
            }
            if (value2 == null) {
                value2 = BigDecimal.ZERO;
            }
            BigDecimal value = value1.add(value2);
            if (value.equals(BigDecimal.ZERO) && i > 1) {
                value = jsonObject.getBigDecimal("p" + (i - 1));
            }
            jsonObject.put("p" + i, value);
        }
        return jsonObject;
    }

    public List<String> getAllNextOrg(String orgNo) {
        ResponseData<List<String>> allOrgs = systemClient.getAllNextOrgId(orgNo);
        return allOrgs.getData();
    }

    private String generalInsertSql(String tableName, Map<String, Object> fields) {
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

    private String generalUpdateSql(String tableName, Map<String, Object> fields, String idKey) {
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


}
