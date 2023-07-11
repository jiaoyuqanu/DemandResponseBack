package com.xqxy.executor.service.jobhandler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.common.utils.BiConsumer;
import com.xqxy.core.client.FileClient;
import com.xqxy.core.client.FileReq;
import com.xqxy.core.client.SystemClient;
import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.core.util.BeanUtil;
import com.xqxy.core.util.DateUtil;
import com.xqxy.core.util.IDGenerator;
import com.xqxy.core.util.LocalDateTimeUtil;
import com.xqxy.dr.modular.data.OrgNoCurve;
import com.xqxy.dr.modular.data.mapper.ConsCurveMapper;
import com.xqxy.dr.modular.evaluation.mapper.ConsEvaluationMapper;
import com.xqxy.dr.modular.evaluation.po.SespStatsPo;
import com.xqxy.dr.modular.project.mapper.ConsContractInfoMapper;
import com.xqxy.dr.modular.project.po.NewstConsContractInfo;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import feign.Response;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.docx4j.wml.P;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.support.AbstractLobCreatingPreparedStatementCallback;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 省级智慧能源服务平台实用化运行 监测数据集成方案 脚本
 */
@Log4j2
@Component
public class MonitoringDataTransformJob {

    @Value("${monitoringDataTransform.jdbcUrl:}")
    private String mdtfJdbcUrl;

    @Value("${monitoringDataTransform.username:}")
    private String mdtfUser;

    @Value("${monitoringDataTransform.password:}")
    private String mdtfPassword;

    @Resource
    private ConsContractInfoMapper consContractInfoMapper;
    @Resource
    private ConsEvaluationMapper consEvaluationMapper;

    private JdbcTemplate jdbcTemplate;
    @Resource
    private SystemClient systemClient;
    @Resource
    private DiscoveryClient discoveryClient;
    @Resource
    private FileClient fileClient;
    @Resource
    private ConsCurveMapper consCurveMapper;

    private IDGenerator idGenerator = new IDGenerator(0, 0);

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

    @XxlJob(value = "execSyncConsContract")
    public ReturnT<String> execSyncConsContract(String param) {
        List<NewstConsContractInfo> newstConsContractInfo = consContractInfoMapper.getNewstConsContractInfo();
        JSONObject jsonObject = systemClient.queryAllOrg();
        JSONArray data = jsonObject.getJSONArray("data");
        Map<String, String> orgNameMap = new HashMap<>();
        for (int i = 0; i < data.size(); i++) {
            JSONObject json = data.getJSONObject(i);
            orgNameMap.put(json.getString("id"), json.getString("name"));
        }
        jdbcTemplate.execute("delete from `sesp_demand_response_agr_detail` where 1=1");
        newstConsContractInfo.forEach(item -> {
            item.setOrgName(orgNameMap.get(item.getOrgNo()));
            InputStream fileInputStream = null;
            Integer fileLength = 0;
            if (StringUtils.isNotEmpty(item.getFileId())) {
                Response response = fileClient.download(new FileReq(item.getFileId(), item.getFileName()));
                try {
                    fileInputStream = response.body().asInputStream();
                    fileLength = response.body().length();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            String sql = "INSERT INTO `sesp_demand_response_agr_detail` (`ID`, `FILE_DOC`, `INVITATION_DR_ABILITY`, `ORG_NAME`, `ORG_NO`, `S_CONS_NAME`, `S_CONS_NO`, `SIGN_TIME`, `STATIS_YMD`) VALUES (?, ?, ?,?, ?, ?, ?, ?, ?)";
            InputStream finalFileInputStream = fileInputStream;
            Integer finalFileLength = fileLength;

            jdbcTemplate.execute(sql, new AbstractLobCreatingPreparedStatementCallback(new DefaultLobHandler()) {
                @Override
                protected void setValues(PreparedStatement ps, LobCreator lobCreator) throws SQLException, DataAccessException {
                    ps.setLong(1, idGenerator.getNextId());
                    if (finalFileLength != null && finalFileLength > 0) {
                        lobCreator.setBlobAsBinaryStream(ps, 2, finalFileInputStream, finalFileLength);
                    } else {
                        ps.setNull(2, Types.BLOB);
                    }
                    ps.setBigDecimal(3, item.getInvitationDrAbiltity());
                    ps.setString(4, item.getOrgName());
                    ps.setString(5, item.getOrgNo());
                    ps.setString(6, item.getConsName());
                    ps.setString(7, item.getConsId());
                    ps.setString(8, item.getSignTime());
                    ps.setString(9, DateUtil.formatDate(new Date(System.currentTimeMillis()), "yyyy-MM-dd"));
                }
            });
        });
        return ReturnT.SUCCESS;
    }

    @XxlJob("execSyncEvent")
    public ReturnT<String> execSyncEvent(String param) {
        try {

            LocalDate localDate = LocalDate.now().plusDays(-1);
            String sql = "select count(0) from sesp_demand_response_detail ";
            List<Integer> sizeList = jdbcTemplate.queryForList(sql, Integer.class);
            if (sizeList.size() == 0 || sizeList.get(0) == 0) {
                localDate = LocalDate.of(2020, 1, 1);
            }
            List<SespStatsPo> sespStatsPos = consEvaluationMapper.sespStats(LocalDateTimeUtil.localDateToDate(localDate), LocalDateTimeUtil.localDateToDate(LocalDate.now()));
            JSONObject jsonObject = systemClient.queryAllOrg();
            JSONArray data = jsonObject.getJSONArray("data");
            Map<String, String> orgNameMap = new HashMap<>();
            for (int i = 0; i < data.size(); i++) {
                JSONObject json = data.getJSONObject(i);
                orgNameMap.put(json.getString("id"), json.getString("name"));
            }
            String insertSql = "INSERT INTO `sesp_demand_response_detail` (`ID`, `BEGIN_RESPONSE_TIME`, `CUM_RESP_CAPACITY`, `END_RESPONSE_TIME`, `EVENT_NAME`, `EVENT_NO`, `EVENT_TYPE`, `IS_PARTAKE`, `ORG_NAME`, `ORG_NO`, `S_CONS_NAME`, `S_CONS_NO`, `STATIS_YMD`, `EVENT_RL`, `TARGET_RESP_CAPACITY`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            sespStatsPos.forEach(item -> {
                item.setOrgName(orgNameMap.get(item.getOrgNo()));
                String eventType = "00";
                //消峰
                if (item.getResponseType() == 1) {
                    if (item.getTimeType() == 1) {
                        eventType = "01";
                    }
                    if (item.getTimeType() == 2) {
                        eventType = "02";
                    }
                }
                //填谷
                if (item.getResponseType() == 2) {
                    if (item.getTimeType() == 1) {
                        eventType = "03";
                    }
                    if (item.getTimeType() == 2) {
                        eventType = "04";
                    }
                }
                String finalEventType = eventType;
                jdbcTemplate.execute(insertSql, new AbstractLobCreatingPreparedStatementCallback(new DefaultLobHandler()) {
                    @Override
                    protected void setValues(PreparedStatement preparedStatement, LobCreator lobCreator) throws SQLException, DataAccessException {
                        preparedStatement.setLong(1, idGenerator.getNextId());
                        preparedStatement.setString(2, DateUtil.formatDate(item.getRegulateDate(), "yyyy-MM-dd") + " " + item.getBeginResponseTime());
                        preparedStatement.setString(3, item.getCumRespCapacity());
                        preparedStatement.setString(4, DateUtil.formatDate(item.getRegulateDate(), "yyyy-MM-dd") + " " + item.getEndResponseTime());
                        preparedStatement.setString(5, item.getEventName());
                        preparedStatement.setString(6, item.getEventNo());
                        preparedStatement.setString(7, finalEventType);
                        preparedStatement.setString(8, item.getIsPartake());
                        preparedStatement.setString(9, item.getOrgName());
                        preparedStatement.setString(10, item.getOrgNo());
                        preparedStatement.setString(11, item.getSConsName());
                        preparedStatement.setString(12, item.getSConsNo());
                        preparedStatement.setString(13, item.getStatisYmd());
                        preparedStatement.setString(14, item.getEventRl());
                        preparedStatement.setString(15, item.getTargetRespCapacity());
                    }
                });
            });

            return ReturnT.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }


//    @PostConstruct
//    public void init2() {
//        this.getData("20", "102", 1, false);
//        this.getData("20", "102", 2, false);
//        this.getData("201", "102", 1, true);
//        this.getData("201", "102", 2, true);
//    }



    public static void main(String[] args) {
        for (int i = 1; i <= 96; i++) {
            System.out.printf("setValue(item1, item2, OrgNoCurve::setP%d, OrgNoCurve::getP%d);\n", i, i);
        }
    }

}
