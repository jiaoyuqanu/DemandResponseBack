package com.xqxy.dr.modular.strategy;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xqxy.core.context.requestno.RequestNoContext;
import com.xqxy.core.exception.ServiceException;
import com.xqxy.core.log.RequestLogManager;
import com.xqxy.dr.modular.data.entity.ConsCurve;
import com.xqxy.dr.modular.data.entity.ConsEnergyCurve;
import com.xqxy.dr.modular.data.enums.ConsCurveExceptionEnum;
import com.xqxy.dr.modular.data.result.DataAccessRealtimeCurve;
import com.xqxy.dr.modular.data.result.DataAccessRealtimeData;
import com.xqxy.dr.modular.data.result.DataAccessRealtimeResult;
import com.xqxy.dr.modular.data.result.centerResult.DataAccessHistoryCurve;
import com.xqxy.dr.modular.data.result.centerResult.DataAccessHistoryData;
import com.xqxy.dr.modular.data.result.centerResult.DataAccessHistoryResult;
import com.xqxy.dr.modular.data.result.energyResult.DataCenterEnergy;
import com.xqxy.dr.modular.data.result.energyResult.DataCenterEnergyResult;
import com.xqxy.dr.modular.device.VO.EquipmentRecordVO;
import com.xqxy.sys.modular.cust.entity.Cons;
import com.xqxy.sys.modular.log.service.DcRequestLogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @Description
 * @ClassName DataCenter
 * @Author User
 * @date 2021.04.13 17:51
 */
@Component
public class DataCenterDataAccessStrategy implements DataAccessStrategy {

    private static final Log log = Log.get();

    private final Integer pageNo = 1;

    private final Integer pageSize = 10;


    @Value("${dataAccessTodayUrl}")
    private String dataAccessTodayUrl;

    @Value("${dataAccessDayUrl}")
    private String dataAccessDayUrl;

    /**
     * 中台多户号单日期历史电量
     */
    @Value("${dataAccessEnergyDayUrl}")
    private String dataAccessEnergyDayUrl;

    @Value("${dataAccessEnergyTodayUrl}")
    private String dataAccessEnergyTodayUrl;

    @Value("${profileAppcode}")
    private String appCode;

    @Value("${centerRequestTimeOut}")
    private Integer centerRequestTimeOut;

    /**
     * 中台多户号多日期历史负荷
     */
    @Value("${dataAccessHistoryListUrl}")
    private String dataAccessHistoryListUrl;

    @Resource
    DcRequestLogService dcRequestLogService;

    class RequestField {
        String fieldName;
        String fieldValue;
        String operator = "=";

        RequestField(String fieldName, String fieldValue) {
            this.fieldName = fieldName;
            this.fieldValue = fieldValue;
        }

        RequestField(String fieldName, String fieldValue, String operator) {
            this.fieldName = fieldName;
            this.fieldValue = fieldValue;
            this.operator = operator;
        }

        public void setOperator(String operator) {
            this.operator = operator;
        }

        public String getFieldName() {
            return fieldName;
        }

        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

        public String getFieldValue() {
            return fieldValue;
        }

        public void setFieldValue(String fieldValue) {
            this.fieldValue = fieldValue;
        }

        public String getOperator() {
            return operator;
        }

        @Override
        public String toString() {
            return "RequestField{" +
                    "fieldName='" + fieldName + '\'' +
                    ", fieldValue='" + fieldValue + '\'' +
                    ", operator='" + operator + '\'' +
                    '}';
        }
    }

    class SortField {
        String cons_no = "ASC";

        public String getCons_no() {
            return cons_no;
        }

        public void setCons_no(String cons_no) {
            this.cons_no = cons_no;
        }

        @Override
        public String toString() {
            return "SortField{" +
                    "cons_no='" + cons_no + '\'' +
                    '}';
        }
    }

    /*@Override
    public ConsCurve queryDayLoadCurveByConsNo(String consNo, String dataDate) {

        if (StringUtils.isEmpty(consNo) || StringUtils.isEmpty(dataDate)) {
            ConsCurve consCurve = new ConsCurve();
            consCurve.setConsNo(consNo);
            return consCurve;
        }

        HashMap<String, Object> params = new HashMap<>();
        RequestField cons_no = new RequestField("cons_no", consNo);
        RequestField data_dt = new RequestField("data_dt", dataDate);
        List<RequestField> requestFields = Arrays.asList(cons_no, data_dt);
        params.put("esQueries", requestFields);
        params.put("sort", new SortField());
        params.put("pageSize", 10);

        JSONObject jsonObject = new JSONObject(params);

        log.info(">>> 中台历史负荷接口，请求号为：{}，中台历史负荷请求参数：{}", RequestNoContext.get(), jsonObject);
        String body = HttpRequest.post(dataAccessDayUrl).timeout(centerRequestTimeOut).body(jsonObject.toJSONString()).execute().body();
        String strBody = body.toLowerCase();
        log.info(">>> 中台历史负荷接口，请求号为：{}，中台历史负荷请求：{}", RequestNoContext.get(), strBody);
        DataAccessResult dataAccessResult = JSONUtil.toBean(strBody, DataAccessResult.class);
        *//* DataAccessResult dataAccessResult = JSONUtil.toBean(HttpUtil.post(dataAccessDayUrl, jsonObject), DataAccessResult.class);*//*
        // log.info("中台历史负荷请求结果：{}" , dataAccessResult);
        if (dataAccessResult.getData().getList().size() < 1) {
            ConsCurve consCurve = new ConsCurve();
            consCurve.setConsNo(consNo);
            consCurve.setDataDate(LocalDate.parse(dataDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            return consCurve;
        }
        List<DataAccessCurve> list = dataAccessResult.getData().getList();
        List<ConsCurve> consCurveList = list.stream().map(dataAccessCurve -> {
            ConsCurve consCurve = new ConsCurve();
            BeanUtil.copyProperties(dataAccessCurve, consCurve);
            ReflectUtil.setFieldValue(consCurve, "consNo", dataAccessCurve.getCons_no());
            ReflectUtil.setFieldValue(consCurve, "dataDate", dataAccessCurve.getData_dt());
            return consCurve;
        }).collect(Collectors.toList());
        return consCurveList.get(0);
    }*/


    @Override
    public ConsCurve queryDayLoadCurveByConsNo(String consNo, String dataDate) {

        if (StringUtils.isEmpty(consNo) || StringUtils.isEmpty(dataDate)) {
            ConsCurve consCurve = new ConsCurve();
            consCurve.setConsId(consNo);
            return consCurve;
        }

        HashMap<String, Object> params = new HashMap<>();
        RequestField cons_no = new RequestField("cons_no", consNo);
        String replaceDate = dataDate.replace("-", "");
        RequestField data_dt = new RequestField("ymd", replaceDate);
        List<RequestField> requestFields = Arrays.asList(cons_no, data_dt);
        params.put("esQueries", requestFields);
        params.put("pageSize", 10);

        JSONObject jsonObject = new JSONObject(params);

        log.info(">>> 中台历史负荷接口，请求号为：{}，中台历史负荷请求参数：{}", RequestNoContext.get(), jsonObject);
        String body = HttpRequest.post(dataAccessDayUrl).timeout(centerRequestTimeOut).body(jsonObject.toJSONString()).execute().body();

        //异步记录日志
        RequestLogManager.me().executeRequestLog(dataAccessDayUrl, JSONObject.toJSONString(params), body);

        // String strBody = body.toLowerCase();
        log.info(">>> 中台历史负荷接口，请求号为：{}，中台历史负荷请求返回：{}", RequestNoContext.get(), body);
        DataAccessHistoryResult dataAccessHistoryResult;
        try {
            dataAccessHistoryResult = JSONUtil.toBean(body, DataAccessHistoryResult.class);
        } catch (Exception e) {
            throw new ServiceException(ConsCurveExceptionEnum.DATACENTER_EXCPETION);
        }
        DataAccessHistoryData data = dataAccessHistoryResult.getData();
        List<DataAccessHistoryCurve> rows = data.getList();
        if (CollectionUtil.isNotEmpty(rows)) {
            DataAccessHistoryCurve dataAccessHistoryCurve = rows.get(0);
            ConsCurve consCurve = new ConsCurve();
            for (int i = 0; i <= 95; i++) {
                int hours = i / 4;
                StringBuilder pointString = new StringBuilder("t");
                pointString.append(hours < 10 ? "0" : "").append(hours).append((15 * (i % 4)) < 10 ? "0" : "").append(15 * (i % 4));
                BigDecimal fieldsValue = (BigDecimal) ReflectUtil.getFieldValue(dataAccessHistoryCurve, pointString.toString());
                ReflectUtil.setFieldValue(consCurve, "p" + (i + 1), fieldsValue);
            }
            consCurve.setConsId(consNo);
            // consCurve.setConsId();
            consCurve.setDataDate(LocalDate.parse(dataDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            log.info(">>> 中台历史接口，请求号为：{}，中台历史转换结果：{}", RequestNoContext.get(), consCurve);
            //consCurve.setDataPointFlag(dataAccessRealtimeCurve.get);
            return consCurve;
        }
        LocalDate parse = LocalDate.parse(dataDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        ConsCurve consCurve = new ConsCurve();
        consCurve.setConsId(consNo);
        consCurve.setDataDate(parse);
        return consCurve;
    }

    @Override
    public List<ConsCurve> queryHistoryCurveList(String elecConsNo, String startDate, String endDate) {

        if (StringUtils.isEmpty(elecConsNo) || StringUtils.isEmpty(startDate) || StringUtils.isEmpty(endDate)) {
            return null;
        }

        HashMap<String, Object> params = new HashMap<>();
        RequestField cons_no = new RequestField("cons_no", elecConsNo);
        String replaceStartDate = startDate.replace("-", "");
        String replaceEndDate = endDate.replace("-", "");
        RequestField start_dt = new RequestField("ymd", replaceStartDate, ">=");
        RequestField end_dt = new RequestField("ymd", replaceEndDate, "<=");
        List<RequestField> requestFields = Arrays.asList(cons_no, start_dt, end_dt);
        params.put("esQueries", requestFields);
        params.put("pageSize", 100);

        JSONObject sort = new JSONObject();
        sort.put("ymd", "ASC");
        params.put("sort", sort);

        JSONObject jsonObject = new JSONObject(params);

        log.info(">>> 中台历史负荷接口，请求号为：{}，中台历史负荷请求参数：{}", RequestNoContext.get(), jsonObject);
        String body = HttpRequest.post(dataAccessDayUrl).timeout(centerRequestTimeOut).body(jsonObject.toJSONString()).execute().body();
        // String strBody = body.toLowerCase();

        //异步记录日志
        RequestLogManager.me().executeRequestLog(dataAccessDayUrl, JSONObject.toJSONString(params), body);

        log.info(">>> 中台历史负荷接口，请求号为：{}，中台历史负荷请求返回：{}", RequestNoContext.get(), body);
        DataAccessHistoryResult dataAccessHistoryResult;
        try {
            dataAccessHistoryResult = JSONUtil.toBean(body, DataAccessHistoryResult.class);
        } catch (Exception e) {
            throw new ServiceException(ConsCurveExceptionEnum.DATACENTER_EXCPETION);
        }
        DataAccessHistoryData data = dataAccessHistoryResult.getData();
        List<DataAccessHistoryCurve> rows = data.getList();
        List<ConsCurve> resultList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(rows)) {
            rows.stream().forEach(historyCurve -> {
                ConsCurve consCurve = new ConsCurve();
                for (int i = 0; i <= 95; i++) {
                    int hours = i / 4;
                    StringBuilder pointString = new StringBuilder("t");
                    pointString.append(hours < 10 ? "0" : "").append(hours).append((15 * (i % 4)) < 10 ? "0" : "").append(15 * (i % 4));
                    BigDecimal fieldsValue = (BigDecimal) ReflectUtil.getFieldValue(historyCurve, pointString.toString());
                    ReflectUtil.setFieldValue(consCurve, "p" + (i + 1), fieldsValue);
                }
                String ymd = historyCurve.getYmd();
                LocalDate ofDate = LocalDate.of(Integer.parseInt(ymd.substring(0, 4)), Integer.parseInt(ymd.substring(4, 6)), Integer.parseInt(ymd.substring(6, 8)));
                consCurve.setDataDate(ofDate);
                consCurve.setConsId(historyCurve.getConsNo());
                log.info(">>> 中台历史接口，请求号为：{}，中台历史转换结果：{}", RequestNoContext.get(), consCurve);
                resultList.add(consCurve);
            });
            // 删除历史接口的重复数据
            TemporalAccessor startAccessor = DateTimeFormatter.ofPattern("yyyy-MM-dd").parse(startDate);
            TemporalAccessor endAccessor = DateTimeFormatter.ofPattern("yyyy-MM-dd").parse(endDate);
            LocalDate startTimes = LocalDate.from(startAccessor);
            LocalDate endTimes = LocalDate.from(endAccessor);
            endTimes = endTimes.plusDays(1);
            while (startTimes.isBefore(endTimes)) {
                List<LocalDate> dateList = resultList.stream().map(ConsCurve::getDataDate).collect(Collectors.toList());
                if (!dateList.contains(startTimes)) {
                    ConsCurve tempCurve = new ConsCurve();
                    tempCurve.setConsId(elecConsNo);
                    tempCurve.setDataDate(startTimes);
                    resultList.add(tempCurve);
                }
                startTimes = startTimes.plusDays(1);
            }
            //consCurve.setDataPointFlag(dataAccessRealtimeCurve.get);
            // 根据日期去除重复元素
            List<ConsCurve> collect = resultList.stream().filter(distinctByKey(curve -> curve.getDataDate())).collect(Collectors.toList());
            // 根据日期排序
            List<ConsCurve> collectResult = collect.stream().sorted((c1, c2) -> {
                int dateValue = c1.getDataDate().compareTo(c2.getDataDate());
                if (dateValue != 0) {// 如果日期不相等，直接返回比较结果
                    return dateValue;
                } else { // 如果日期相等，再根据p1值比较
                    return c1.getP1().compareTo(c2.getP1());
                }
            }).collect(Collectors.toList());

            return collectResult;
        }
        return null;
    }

    /**
     * 通过对象属性去重
     *
     * @param keyExtractor
     * @param <T>
     * @return
     */
    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }


    @Override
    public ConsCurve queryTodayLoadCurveByConsNo(String consNo, String dataDate) {

        if (StringUtils.isEmpty(consNo) || StringUtils.isEmpty(dataDate)) {
            ConsCurve consCurve = new ConsCurve();
            consCurve.setConsId(consNo);
            return consCurve;
        }

        LocalDate parse = LocalDate.parse(dataDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        if (!LocalDate.now().isEqual(parse)) {
            // 日期不是今天，调用历史接口数据
            return this.queryDayLoadCurveByConsNo(consNo, dataDate);
        }

        HashMap<String, Object> params = new HashMap<>();
        params.put("appCode", appCode);
        params.put("pageNum", pageNo);
        params.put("pageSize", pageSize);
        params.put("cons_no", "," + consNo);
        // params.put("org_no", "43");
        String replaceDate = dataDate.replace("-", "");
        params.put("statDate", replaceDate);
        // params.put("org_no",consNo);
        log.info(">>> 中台实时接口，请求号为：{}，中台实时接口入参：{}", RequestNoContext.get(), params.toString());
        String jsonString = HttpUtil.get(dataAccessTodayUrl, params);

        //异步记录日志
        RequestLogManager.me().executeRequestLog(dataAccessTodayUrl, JSONObject.toJSONString(params), jsonString);

        log.info(">>> 中台实时接口，请求号为：{}，中台实时返回结果：{}", RequestNoContext.get(), jsonString);
        DataAccessRealtimeResult dataAccessRealtimeResult;
        try {
            dataAccessRealtimeResult = JSONUtil.toBean(jsonString, DataAccessRealtimeResult.class);
        } catch (Exception e) {
            throw new ServiceException(ConsCurveExceptionEnum.DATACENTER_EXCPETION);
        }
        // log.info("中台实时接口返回结果：{}" , dataAccessRealtimeResult);
        DataAccessRealtimeData data = dataAccessRealtimeResult.getData();
        List<DataAccessRealtimeCurve> rows = data.getList();
        if (CollectionUtil.isNotEmpty(rows)) {
            DataAccessRealtimeCurve dataAccessRealtimeCurve = rows.get(0);
            ConsCurve consCurve = new ConsCurve();
            for (int i = 0; i <= 95; i++) {
                int hours = i / 4;
                StringBuilder pointString = new StringBuilder("t");
                pointString.append(hours < 10 ? "0" : "").append(hours).append((15 * (i % 4)) < 10 ? "0" : "").append(15 * (i % 4));
                BigDecimal fieldsValue = (BigDecimal) ReflectUtil.getFieldValue(dataAccessRealtimeCurve, pointString.toString());
                ReflectUtil.setFieldValue(consCurve, "p" + (i + 1), fieldsValue);
            }
            consCurve.setConsId(consNo);
            // consCurve.setConsId();
            consCurve.setDataDate(dataAccessRealtimeCurve.getData_dt());
            log.info(">>> 模拟数据实时接口，请求号为：{}，模拟数据实时转换结果：{}", RequestNoContext.get(), consCurve);
            // 当前事件点的前两个点，如果为零就设为null，否则正常显示
            LocalDateTime currentTime = LocalDateTime.now();
            int currentPoint = (currentTime.getHour() * 4) + (currentTime.getMinute() / 15) + 1;

            for(int temp = currentPoint + 1; temp < 97; temp++) {
                if(temp > 96) {
                    break;
                }
                ReflectUtil.setFieldValue(consCurve,"p" + temp,null);
            }
            if(currentPoint > 0 && currentPoint < 97) {
                // 当前点数为0，直接设置为null
                BigDecimal currentPointField =  (BigDecimal)ReflectUtil.getFieldValue(consCurve, "p" + currentPoint);
                if(BigDecimal.ZERO.compareTo(currentPointField) == 0) {
                    ReflectUtil.setFieldValue(consCurve,"p" + currentPoint,null);
                    // 前一个点
                    currentPoint -= 1;
                    if(currentPoint > 0 && currentPoint < 97 ) {
                        BigDecimal currentPointFieldTwo =  (BigDecimal)ReflectUtil.getFieldValue(consCurve, "p" + currentPoint);
                        if(BigDecimal.ZERO.compareTo(currentPointFieldTwo) == 0) {
                            ReflectUtil.setFieldValue(consCurve,"p" + currentPoint,null);
                        }
                    }
                }
            }
            //consCurve.setDataPointFlag(dataAccessRealtimeCurve.get);
            return consCurve;
        }
        ConsCurve consCurve = new ConsCurve();
        consCurve.setConsId(consNo);
        consCurve.setDataDate(parse);
        return consCurve;
    }

    @Override
    public List<ConsCurve> queryTodayCurveList(List<String> elecConsNoList, String dataDate) {
        // 将营销户号转换成中台需要的数据格式
        String elecNoList = elecConsNoList.toString().replace("[", ",").replace("]", "").replaceAll("\\s*", "");
        LocalDate parse = LocalDate.parse(dataDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        if (!LocalDate.now().isEqual(parse)) {
            // 日期不是今天，调用历史接口数据。目前没有接口，直接返回null
        }

        HashMap<String, Object> params = new HashMap<>();
        params.put("appCode", appCode);
        params.put("pageNum", pageNo);
        params.put("pageSize", 100);
        params.put("cons_no", elecNoList);
        // params.put("org_no", "43");
        String replaceDate = dataDate.replace("-", "");
        params.put("statDate", replaceDate);
        // params.put("org_no",consNo);
        log.info(">>> 中台实时接口多户号集合，请求号为：{}，中台实时接口入参：{}", RequestNoContext.get(), params.toString());
        String jsonString = HttpUtil.get(dataAccessTodayUrl, params);

        //异步记录日志
//        RequestLogManager.me().executeRequestLog(dataAccessTodayUrl, JSONObject.toJSONString(params).toString(), jsonString);

        log.info(">>> 中台实时接口多户号集合，请求号为：{}，中台实时返回结果：{}", RequestNoContext.get(), jsonString);
        DataAccessRealtimeResult dataAccessRealtimeResult;
        try {
            dataAccessRealtimeResult = JSONUtil.toBean(jsonString, DataAccessRealtimeResult.class);
        } catch (Exception e) {
            throw new ServiceException(ConsCurveExceptionEnum.DATACENTER_EXCPETION);
        }
        // 返回结果
        ArrayList<ConsCurve> resultCurveList = new ArrayList<>();
        // log.info("中台实时接口返回结果：{}" , dataAccessRealtimeResult);
        DataAccessRealtimeData data = dataAccessRealtimeResult.getData();
        List<DataAccessRealtimeCurve> rows = data.getList();
        if (CollectionUtil.isNotEmpty(rows)) {
            for (DataAccessRealtimeCurve dataAccessRealtimeCurve : rows) {
                ConsCurve consCurve = new ConsCurve();
                for (int i = 0; i <= 95; i++) {
                    int hours = i / 4;
                    StringBuilder pointString = new StringBuilder("t");
                    pointString.append(hours < 10 ? "0" : "").append(hours).append((15 * (i % 4)) < 10 ? "0" : "").append(15 * (i % 4));
                    BigDecimal fieldsValue = (BigDecimal) ReflectUtil.getFieldValue(dataAccessRealtimeCurve, pointString.toString());
                    ReflectUtil.setFieldValue(consCurve, "p" + (i + 1), fieldsValue);
                }
                consCurve.setConsId(dataAccessRealtimeCurve.getConsNo());
                consCurve.setDataDate(dataAccessRealtimeCurve.getData_dt());
                resultCurveList.add(consCurve);
            }

            log.info(">>> 中台实时接口，请求号为：{}，中台实时转换结果：{}", RequestNoContext.get(), resultCurveList);
            // 当前事件点的前两个点，如果为零就设为null，否则正常显示
        }

        // 如果有的用户没有查出来，则补充一个为null的曲线
        List<String> collect = resultCurveList.stream().map(ConsCurve::getConsId).collect(Collectors.toList());
        elecConsNoList.stream().forEach(c -> {
            if(!collect.contains(c)) {
                ConsCurve curve = new ConsCurve();
                curve.setConsId(c);
                curve.setDataDate(parse);
                resultCurveList.add(curve);
            }
        });

        return resultCurveList;
    }

    /**
     * 中台历史接口多户号多日期
     * @param elecConsNoList
     * @param dataDateList
     * @return
     */
    @Override
    public List<ConsCurve> queryHistoryCurveList(List<String> elecConsNoList, List<String> dataDateList) {
        if (CollectionUtil.isEmpty(elecConsNoList) || CollectionUtil.isEmpty(dataDateList)) {
            return null;
        }
        // 日期参数去掉横杠
        // 所有参数去掉空格
        String consNoParam = elecConsNoList.toString().replace("[", "").replace("]", "").replaceAll("\\s*", "");
        String dateParams = dataDateList.toString().replace("[", "").replace("]", "").replace("-","").replaceAll("\\s*", "");

        // 添加户号和日期参数
        HashMap<String, Object> params = new HashMap<>();
        RequestField cons_no = new RequestField("cons_no", consNoParam,"in");
        RequestField start_dt = new RequestField("ymd", dateParams, "in");

        List<RequestField> requestFields = Arrays.asList(cons_no, start_dt);
        params.put("esQueries", requestFields);
        params.put("pageSize", 100);
        //params.put("pageNum", 1);

        // 按照日期升序排序
        //JSONObject sort = new JSONObject();
        //sort.put("ymd", "ASC");
        //params.put("sort", sort);

        JSONObject jsonObject = new JSONObject(params);

        log.info(">>> 中台多户号多日期历史负荷接口，请求号为：{}，中台历史负荷请求参数：{}", RequestNoContext.get(), jsonObject);
        String body = HttpRequest.post(dataAccessHistoryListUrl).timeout(centerRequestTimeOut).body(jsonObject.toJSONString()).execute().body();
        // String strBody = body.toLowerCase();

        //异步记录日志
        RequestLogManager.me().executeRequestLog(dataAccessHistoryListUrl, JSONObject.toJSONString(params), body);

        log.info(">>> 中台多户号多日期历史负荷接口，请求号为：{}，中台历史负荷请求返回：{}", RequestNoContext.get(), body);
        DataAccessHistoryResult dataAccessHistoryResult;
        //JSONObject jsonObject1;
        try {
            dataAccessHistoryResult = JSONUtil.toBean(body, DataAccessHistoryResult.class);
            //jsonObject1 = JSON.parseObject(body);
        } catch (Exception e) {
            throw new ServiceException(ConsCurveExceptionEnum.DATACENTER_EXCPETION);
        }
        DataAccessHistoryData data = null;
        //JSONArray rows = new JSONArray();
        if(null !=dataAccessHistoryResult && null!= dataAccessHistoryResult.getData()) {
            //rows = dataAccessHistoryResult.getJSONArray("data");
            data = dataAccessHistoryResult.getData();
        } else {
            return null;
        }
        List<DataAccessHistoryCurve> rows=null;
        if(null != data) {
            rows = data.getList();
        } else {
            return null;
        }
        List<ConsCurve> resultList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(rows)) {
            //rows.stream().forEach(historyCurve -> {
            for(DataAccessHistoryCurve historyCurve : rows) {
                ConsCurve consCurve = new ConsCurve();
                for (int i = 0; i <= 95; i++) {
                    int hours = i / 4;
                    StringBuilder pointString = new StringBuilder("t");
                    pointString.append(hours < 10 ? "0" : "").append(hours).append((15 * (i % 4)) < 10 ? "0" : "").append(15 * (i % 4));
                    BigDecimal fieldsValue = (BigDecimal) ReflectUtil.getFieldValue(historyCurve, pointString.toString());
                    ReflectUtil.setFieldValue(consCurve, "p" + (i + 1), fieldsValue);
                }
                String ymd = historyCurve.getYmd();
                LocalDate ofDate = LocalDate.of(Integer.parseInt(ymd.substring(0, 4)), Integer.parseInt(ymd.substring(4, 6)), Integer.parseInt(ymd.substring(6, 8)));
                consCurve.setDataDate(ofDate);
                consCurve.setConsId(historyCurve.getConsNo());
                log.info(">>> 中台多户号多日期历史接口，请求号为：{}，单条结果：{}", RequestNoContext.get(), consCurve);
                resultList.add(consCurve);
            }
            //});

            log.info(">>> 中台多户号多日期历史接口，请求号为：{}，总结果：{}", RequestNoContext.get(), resultList);
            //consCurve.setDataPointFlag(dataAccessRealtimeCurve.get);
            // 根据日期去除重复元素
            // List<ConsCurve> noRepearResult = resultList.stream().filter(distinctByKey(curve -> curve.getDataDate())).collect(Collectors.toList());

            return resultList;
        }
        return null;
    }


    /**
     * 中台历史接口多户号单日期
     * @param elecConsNoList
     * @param dataDate
     * @return
     */
    @Override
    public List<ConsCurve> queryHistoryCurvePage(List<String> elecConsNoList, String dataDate) {
        if (CollectionUtil.isEmpty(elecConsNoList) || ObjectUtil.isEmpty(dataDate)) {
            return null;
        }
        // 日期参数去掉横杠
        // 所有参数去掉空格
        String consNoParam = elecConsNoList.toString().replace("[", "").replace("]", "").replaceAll("\\s*", "");

        // 添加户号和日期参数
        HashMap<String, Object> params = new HashMap<>();
        RequestField cons_no = new RequestField("cons_no", consNoParam,"in");
        RequestField start_dt = new RequestField("ymd", dataDate, "=");

        List<RequestField> requestFields = Arrays.asList(cons_no, start_dt);
        params.put("esQueries", requestFields);
        params.put("pageSize", 100);
        params.put("pageNum", 1);

        JSONObject jsonObject = new JSONObject(params);

        log.info(">>> 中台多户号单日期历史负荷接口，请求号为：{}，中台历史负荷请求参数：{}", RequestNoContext.get(), jsonObject);
        String body = HttpRequest.post(dataAccessHistoryListUrl).timeout(centerRequestTimeOut).body(jsonObject.toJSONString()).execute().body();
        // String strBody = body.toLowerCase();

        //异步记录日志
        RequestLogManager.me().executeRequestLog(dataAccessHistoryListUrl, JSONObject.toJSONString(params), body);

        log.info(">>> 中台多户号单日期历史负荷接口，请求号为：{}，中台历史负荷请求返回：{}", RequestNoContext.get(), body);
        DataAccessHistoryResult dataAccessHistoryResult;
        //JSONObject jsonObject1;
        try {
            dataAccessHistoryResult = JSONUtil.toBean(body, DataAccessHistoryResult.class);
            //jsonObject1 = JSON.parseObject(body);
        } catch (Exception e) {
            throw new ServiceException(ConsCurveExceptionEnum.DATACENTER_EXCPETION);
        }
        DataAccessHistoryData data = null;
        //JSONArray rows = new JSONArray();
        if(null !=dataAccessHistoryResult && null!= dataAccessHistoryResult.getData()) {
            //rows = dataAccessHistoryResult.getJSONArray("data");
            data = dataAccessHistoryResult.getData();
        } else {
            return null;
        }
        List<DataAccessHistoryCurve> rows=null;
        if(null != data) {
            rows = data.getList();
        } else {
            return null;
        }
        List<ConsCurve> resultList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(rows)) {
            rows.stream().forEach(historyCurve -> {
                ConsCurve consCurve = new ConsCurve();
                for (int i = 0; i <= 95; i++) {
                    int hours = i / 4;
                    StringBuilder pointString = new StringBuilder("t");
                    pointString.append(hours < 10 ? "0" : "").append(hours).append((15 * (i % 4)) < 10 ? "0" : "").append(15 * (i % 4));
                    BigDecimal fieldsValue = (BigDecimal) ReflectUtil.getFieldValue(historyCurve, pointString.toString());
                    ReflectUtil.setFieldValue(consCurve, "p" + (i + 1), fieldsValue);
                }
                String ymd = historyCurve.getYmd();
                LocalDate ofDate = LocalDate.of(Integer.parseInt(ymd.substring(0, 4)), Integer.parseInt(ymd.substring(4, 6)), Integer.parseInt(ymd.substring(6, 8)));
                consCurve.setDataDate(ofDate);
                consCurve.setConsId(historyCurve.getConsNo());
                log.info(">>> 中台多户号单日期历史负荷接口，请求号为：{}，单条结果：{}", RequestNoContext.get(), consCurve);
                resultList.add(consCurve);
            });

            log.info(">>> 中台多户号多日期历史接口，请求号为：{}，总结果：{}", RequestNoContext.get(), resultList);
            //consCurve.setDataPointFlag(dataAccessRealtimeCurve.get);
            // 根据日期去除重复元素
            // List<ConsCurve> noRepearResult = resultList.stream().filter(distinctByKey(curve -> curve.getDataDate())).collect(Collectors.toList());

            return resultList;
        }
        return null;
    }


    @Override
    public List<ConsEnergyCurve> queryDayLoadEnergyByConsNo(List<String> consIdList, String dataDate) {

        return null;
    }

    @Override
    public ConsEnergyCurve queryDayLoadEnergyByConsNo(String elecConsNo, String dataDate) {
        return null;
    }

    @Override
    public ConsEnergyCurve queryTodayLoadEnergyByConsNo(String elecConsNo, String dataDate) {
        return null;
    }

    @Override
    public Cons getConsFromMarketing(String elecNo, String consName, String orgNo) {

        return null;
    }

    @Override
    public List<EquipmentRecordVO> queryDeviceHistoryCurvePage(String deviceId, String date) {
        return null;
    }

    @Override
    public List<EquipmentRecordVO> queryDeviceRealTimeCurvePage(String deviceId) {
        return null;
    }

}
