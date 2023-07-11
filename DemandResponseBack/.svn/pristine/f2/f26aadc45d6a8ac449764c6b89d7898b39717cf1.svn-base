package com.xqxy.dr.modular.strategy;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xqxy.core.context.requestno.RequestNoContext;
import com.xqxy.core.exception.ServiceException;
import com.xqxy.core.log.RequestLogManager;
import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.data.entity.ConsCurve;
import com.xqxy.dr.modular.data.entity.ConsCurveToday;
import com.xqxy.dr.modular.data.entity.ConsEnergyCurve;
import com.xqxy.dr.modular.data.entity.PageArchives;
import com.xqxy.dr.modular.data.enums.ConsCurveExceptionEnum;
import com.xqxy.dr.modular.data.result.*;
import com.xqxy.dr.modular.data.result.centerResult.DataAccessHistoryCurve;
import com.xqxy.dr.modular.data.result.centerResult.DataAccessHistoryData;
import com.xqxy.dr.modular.data.result.centerResult.DataAccessHistoryResult;
import com.xqxy.dr.modular.data.result.energyResult.DataCenterEnergy;
import com.xqxy.dr.modular.data.result.energyResult.DataCenterEnergyResult;
import com.xqxy.dr.modular.data.result.energyResult.SimulationEnergyResult;
import com.xqxy.dr.modular.data.service.ConsCurveService;
import com.xqxy.dr.modular.data.service.ConsCurveTodayService;
import com.xqxy.dr.modular.device.VO.EquipmentRecordVO;
import com.xqxy.sys.modular.cust.entity.Cons;
import com.xqxy.sys.modular.log.entity.DcRequestLog;
import com.xqxy.sys.modular.log.service.DcRequestLogService;
import com.xqxy.sys.modular.utils.HttpClientUtil;
import com.xqxy.sys.modular.utils.SnowflakeIdWorker;
import net.sf.json.JSONArray;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description 模拟数据服务接口
 * @ClassName Simulation
 * @Author User
 * @date 2021.04.13 17:47
 */
@Component
public class SimulationDataAccessStrategy implements DataAccessStrategy {

    private static final Log log = Log.get();

    private final Integer pageNo = 1;

    private final Integer pageSize = 10;

    @Value("${dataAccessTodayUrl}")
    private String dataAccessTodayUrl;

    @Value("${dataAccessDayUrl}")
    private String dataAccessDayUrl;

    @Value("${dataAccessEnergyDayUrl}")
    private String dataAccessEnergyDayUrl;

    @Value("${dataAccessEnergyTodayUrl}")
    private String dataAccessEnergyTodayUrl;

    @Value("${centerRequestTimeOut}")
    private Integer centerRequestTimeOut;
    @Value("${consCurveUrl}")
    private String consCurveUrl;

    @Value("${deviceHistoryCurveUrl}")
    private String deviceHistoryCurveUrl;

    @Value("${deviceRealTimeCurveUrl}")
    private String deviceRealTimeCurveUrl;

    @Value("${consProfileAddress}")
    private String consProfileAddress;

    @Value("${profileAppcode}")
    private String appCode;

    @Resource
    DcRequestLogService dcRequestLogService;

    @Resource
    private ConsCurveTodayService consCurveTodayService;

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
    /**
     * 数据采集监测历史负荷分页
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
        String dataDateFormat=dataDate.replace("-","");
        // 所有参数去掉空格
        String consNoParam = elecConsNoList.toString().replace("[", "").replace("]", "").replaceAll("\\s*", "");

        // 添加户号和日期参数
        HashMap<String, Object> params = new HashMap<>();
        RequestField cons_no = new RequestField("cons_no", consNoParam,"in");
        RequestField start_dt = new RequestField("ymd", dataDateFormat, "=");

        List<RequestField> requestFields = Arrays.asList(cons_no, start_dt);
        params.put("esQueries", requestFields);
        params.put("pageSize", 100);
        params.put("pageNum", 1);

        JSONObject jsonObject = new JSONObject(params);

        log.info(">>> 用户采集监测历史负荷分页，请求号为：{}，用户采集监测历史负荷分页接口参数：{},用户采集监测历史负荷分页接口地址：{}", RequestNoContext.get(), jsonObject,dataAccessDayUrl);
        String body = HttpRequest.post(dataAccessDayUrl).timeout(centerRequestTimeOut).body(jsonObject.toJSONString()).execute().body();
        // String strBody = body.toLowerCase();

        //异步记录日志
//        RequestLogManager.me().executeRequestLog(dataAccessDayUrl, JSONObject.toJSONString(params).toString(), body);

        log.info(">>> 中台多户号单日期历史负荷接口，请求号为：{}，用户采集监测历史负荷分页返回数据：{}", RequestNoContext.get(), body);
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
                for (int i = 0; i <96; i++) {
                    int hours = i / 4;
                    StringBuilder pointString = new StringBuilder("t");
                    pointString.append(hours < 10 ? "0" : "").append(hours).append((15 * (i % 4)) < 10 ? "0" : "").append(15 * (i % 4));
                    BigDecimal fieldsValue = (BigDecimal) ReflectUtil.getFieldValue(historyCurve, pointString.toString());
                    ReflectUtil.setFieldValue(consCurve, "p" + (i + 1), fieldsValue);
                }
                String ymd = historyCurve.getYmd();
                System.out.println(ymd);
                LocalDate ofDate = LocalDate.of(Integer.parseInt(ymd.substring(0, 4)), Integer.parseInt(ymd.substring(4, 6)), Integer.parseInt(ymd.substring(6,8)));
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
    public ConsCurve queryDayLoadCurveByConsNo(String consNo, String dataDate) {
        if (StringUtils.isEmpty(consNo) || StringUtils.isEmpty(dataDate)) {
            ConsCurve consCurve = new ConsCurve();
            consCurve.setConsId(consNo);
            return consCurve;
        }
        LocalDate requestDate = LocalDate.parse(dataDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        // 日期请求参数不能早于当前时间
        if (LocalDate.now().isBefore(requestDate)) {
            ConsCurve consCurve = new ConsCurve();
            consCurve.setConsId(consNo);
            return consCurve;
        }
        // 定义请求的参数
        HashMap<String, Object> paramsMap = new HashMap<>();
        String replaceDate = dataDate.replace("-", "");



        HashMap<String, Object> params = new HashMap<>();
        RequestField cons_no = new RequestField("cons_no", consNo);
        RequestField data_dt = new RequestField("ymd", replaceDate);
        List<RequestField> requestFields = Arrays.asList(cons_no, data_dt);
        params.put("esQueries", requestFields);
        params.put("pageSize", 10);
        params.put("pageNum", 1);


        JSONObject jsonObject = new JSONObject(params);

        System.out.println(jsonObject);
        System.out.println(dataAccessDayUrl);
        String jsonString = HttpRequest.post(dataAccessDayUrl).timeout(centerRequestTimeOut).body(jsonObject.toJSONString()).execute().body();


        SimulationResult simulationResult = JSONUtil.toBean(jsonString, SimulationResult.class);

        System.out.println("here"+simulationResult.toString());
        DcRequestLog dcRequestLog = new DcRequestLog();
        dcRequestLog.setRequestUrl(dataAccessDayUrl + paramsMap);
        dcRequestLog.setResponseData(jsonString);
        dcRequestLogService.save(dcRequestLog);
        // log.info(">>> 日负荷数据：{}", simulationResult.getCurve().toString());
        SimulationResult.Data data = simulationResult.getData();
        List<DataAccessHistoryCurve> rows = null;
        if(null!=data) {
            rows = data.getList();
        }
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
        return simulationResult.getCurve();
    }

    @Override
    public List<ConsCurve> queryHistoryCurveList(String consNo, String startDate, String endDate) {

        String ymd = getYmd(startDate,endDate);

        String param = this.getdrConsCurveParam(consNo, ymd);
        net.sf.json.JSONObject data = HttpClientUtil.sendPost(consCurveUrl, param);

        List<ConsCurve> drConsCurves = this.drConsCurveToMap(data == null ? null : data.toString());
        return drConsCurves;
    }



    /**
     *  查询设备历史负荷
     * @param
     * @return
     */
    @Override
    public List<EquipmentRecordVO> queryDeviceHistoryCurvePage(String deviceId, String date) {
        String param = this.getdrDeviceCurveParam(deviceId, date);
        net.sf.json.JSONObject data = HttpClientUtil.sendPost(deviceHistoryCurveUrl, param);

        List<EquipmentRecordVO> list  = this.drDeviceCurveToMap(data == null ? null : data.toString());
        return list;
    }

    /**
     *  查询设备实时负荷
     * @param
     * @return
     */
    @Override
    public List<EquipmentRecordVO> queryDeviceRealTimeCurvePage(String deviceId) {
        String url = deviceRealTimeCurveUrl + "?equipmentNos=" + deviceId ;
        net.sf.json.JSONObject data = HttpClientUtil.sendGet(url, "");

        List<EquipmentRecordVO> list  = this.drDeviceCurveToMap(data == null ? null : data.toString());
        return list;
    }

    private List<EquipmentRecordVO> drDeviceCurveToMap(String value) {
        if (value == null) {
            return null;
        }

        List<EquipmentRecordVO> deviceList = new ArrayList<>();
//        log.info("数据开始json格式 value={}");
        net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(value);
//        log.info("数据开始 jsonObject 格式 jsonObject={}", jsonObject == null ? null : jsonObject.toString());
        String code = jsonObject.getString("code");
//        log.info("获取 code ={}", code);
        if ("0".equals(code)) {
            String data = jsonObject.getString("data");
            jsonObject = net.sf.json.JSONObject.fromObject(data);
            String list = jsonObject.getString("list");
            JSONArray jsonArray1 = JSONArray.fromObject(list);
            List<String> keys = null;
            for (int i = 0; i < jsonArray1.size(); i++) {
                net.sf.json.JSONObject jsonObject2 = jsonArray1.getJSONObject(i);
                Map map = (Map) net.sf.json.JSONObject.toBean(jsonObject2, Map.class);
                Map<String, BigDecimal> mapParam = new HashMap<String, BigDecimal>();
                EquipmentRecordVO equipmentRecordVO = new EquipmentRecordVO();
                keys = mapToList(map);

                if (keys == null || keys.size() != 96) {
                    keys = mapToList(map);
                }
                for (int x = 0; x < 96; x++) {
                    String key = keys.get(x) == null ? null : keys.get(x).replace("t","");
                    Integer hour = Integer.parseInt(key.substring(0, 2));
                    Integer min = Integer.parseInt(key.substring(2));
                    Integer i1 = (hour * 60 + min) / 15;
                    if(i1 == 0){
                        i1 = 96;
                    }

                    String strValue = map.get("t"+key).toString();
                    if (strValue.equals("null")) {
                        strValue =null;
                        continue;
                    }
                    mapParam.put("p" + (i1), BigDecimal.valueOf(Double.parseDouble(strValue)));
                }
                try {
                    BeanUtils.copyProperties(equipmentRecordVO, map);
                    BeanUtils.copyProperties(equipmentRecordVO, mapParam);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }

                String ymd = (String) map.get("ymd");
                equipmentRecordVO.setYmd(ymd);
                String equipmentNo = map.get("equipmentNo").toString();
                equipmentRecordVO.setEquipmentNo(equipmentNo);
                String dataId = map == null ? null : map.get("dataId").toString();
                equipmentRecordVO.setDataId(dataId);
                equipmentRecordVO.setId(SnowflakeIdWorker.generateId().toString());//雪花算法生成Id

                deviceList.add(equipmentRecordVO);
            }
        }
        return deviceList;
    }

    /**
     * 根据开始  结束时间，拼接其中所有日期返回
     * @param startDate
     * @param endDate
     * @return
     */
    private String getYmd(String startDate, String endDate) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        startDate = startDate.replace("-","");
        endDate = endDate.replace("-","");


        Calendar calendar = Calendar.getInstance();
        try {
            Date parse = sdf.parse(startDate);
            calendar.setTime(parse);
            System.out.println(sdf.format(parse));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        StringBuffer str = new StringBuffer();
        while(true){
            String format = sdf.format(calendar.getTime());
            if(format.compareTo(endDate) > 0){
                break;
            }
            str.append("," + format);
            calendar.add(Calendar.DAY_OF_MONTH,1);
        }

        return new String(str).substring(1);
    }

    /**
     * 功能：参数拼接
     */
    private String getdrConsCurveParam(String consNo, String date) {

        if (consNo == null) {
            return null;
        }
        //cons_no 1105561285 测试数据
        String param = "{\n" +
                "    \"esQueries\": [\n" +
                "        {\n" +
                "            \"fieldName\": \"cons_no\",\n" +
                "            \"fieldValue\": \"" + consNo + "\",\n" +
                "            \"operator\": \"=\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"fieldName\": \"ymd\",\n" +
                "            \"fieldValue\": \"" + date + "\",\n" +
                "            \"operator\": \"=\"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"pageSize\": 3000\n" +
                "}";
        return param == null ? null : param;
    }

    /**
     * 功能：参数拼接
     */
    private String getdrDeviceCurveParam(String consNo, String date) {

        if (consNo == null) {
            return null;
        }
        //cons_no 1105561285 测试数据
        String param = "{\n" +
                "    \"esQueries\": [\n" +
                "        {\n" +
                "            \"fieldName\": \"equipment_no\",\n" +
                "            \"fieldValue\": \"" + consNo + "\",\n" +
                "            \"operator\": \"=\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"fieldName\": \"ymd\",\n" +
                "            \"fieldValue\": \"" + date + "\",\n" +
                "            \"operator\": \"=\"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"pageSize\": 3000\n" +
                "}";
        return param == null ? null : param;
    }

    /**
     * mapParam key获取出来
     */
    private static List<String> mapToList(Map<String, Object> mapParam) {
        List<String> list = null;
        if (list == null || list.size() != 96) {
            Set<Map.Entry<String, Object>> entries = mapParam.entrySet();
            list = new ArrayList<String>();
            for (Map.Entry<String, Object> entry : entries) {
                String key = entry.getKey();
                //获取
                if (key != null && key.length() == 5) {
                    String substring = key.substring(0, 1);
                    String substring1 = key.substring(1, 4);
                    if (com.xqxy.sys.modular.utils.StringUtils.isString(key, "t") && com.xqxy.sys.modular.utils.StringUtils.isNumeric(substring1)) {
                        list.add(key);
                    }
                }
            }
            Collections.sort(list);
        }
        return list;
    }

    /**
     * 功能：drConsCurveToMap 格式装换
     */
    private List<ConsCurve> drConsCurveToMap(String value) {
        if (value == null) {
            return null;
        }

        List<ConsCurve> consCurves = new ArrayList<ConsCurve>();
//        log.info("数据开始json格式 value={}");
        net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(value);
//        log.info("数据开始 jsonObject 格式 jsonObject={}", jsonObject == null ? null : jsonObject.toString());
        String code = jsonObject.getString("code");
//        log.info("获取 code ={}", code);
        if ("0".equals(code)) {
            String data = jsonObject.getString("data");
            jsonObject = net.sf.json.JSONObject.fromObject(data);
            String list = jsonObject.getString("list");
            JSONArray jsonArray1 = JSONArray.fromObject(list);
            List<String> keys = null;
            for (int i = 0; i < jsonArray1.size(); i++) {
                net.sf.json.JSONObject jsonObject2 = jsonArray1.getJSONObject(i);
                Map map = (Map) net.sf.json.JSONObject.toBean(jsonObject2, Map.class);
                Map<String, BigDecimal> mapParam = new HashMap<String, BigDecimal>();
                ConsCurve consCurve = new ConsCurve();
                keys = mapToList(map);

                if (keys == null || keys.size() != 96) {
                    keys = mapToList(map);
                }
                for (int x = 0; x < 96; x++) {
                    String key = keys.get(x) == null ? null : keys.get(x).replace("t","");
                    Integer hour = Integer.parseInt(key.substring(0, 2));
                    Integer min = Integer.parseInt(key.substring(2));
                    Integer i1 = (hour * 60 + min) / 15;
                    if(i1 == 0){
                        i1 = 96;
                    }

                    String strValue = map.get("t"+key).toString();
                    if (strValue.equals("null")) {
                        strValue =null;
                        continue;
                    }
                    mapParam.put("p" + (i1), BigDecimal.valueOf(Double.parseDouble(strValue)));
                }
                try {
                    BeanUtils.copyProperties(consCurve, map);
                    BeanUtils.copyProperties(consCurve, mapParam);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }

                String ymd = (String) map.get("ymd");
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                try {
                    Date parse = sdf.parse(ymd);
                    consCurve.setDataDate(parse.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String consNo = map.get("consNo").toString();
                consCurve.setConsId(consNo);
                consCurve.setDataPointFlag("1");
                String id = map == null ? null : map.get("dataId").toString();
                consCurve.setSourceId(id);
                consCurve.setDataId(SnowflakeIdWorker.generateId());//雪花算法生成Id
                consCurve.setDataType("1");

                consCurves.add(consCurve);
            }
        }
        return consCurves;
    }


    @Override
    public ConsCurve queryTodayLoadCurveByConsNo(String consNo, String dataDate) {
        ConsCurve consCurve = new ConsCurve();
        if (StringUtils.isEmpty(consNo) || StringUtils.isEmpty(dataDate)) {
            consCurve.setConsId(consNo);
            return consCurve;
        }
        //从本地查询 用户实时负荷表
        LambdaQueryWrapper<ConsCurveToday> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ConsCurveToday::getConsId,consNo);
        queryWrapper.eq(ConsCurveToday::getDataDate,dataDate);
        List<ConsCurveToday> list = consCurveTodayService.list(queryWrapper);
        if(!CollectionUtils.isEmpty(list)){
            ConsCurveToday consCurveToday = list.get(0);

            for (int i = 1; i <= 96; i++) {
                String name = "p" + i;
                BigDecimal fieldValue = (BigDecimal) ReflectUtil.getFieldValue(consCurveToday, name);
                ReflectUtil.setFieldValue(consCurve,name,fieldValue);
            }

            ReflectUtil.setFieldValue(consCurve,"consId",ReflectUtil.getFieldValue(consCurveToday, "consId"));
            ReflectUtil.setFieldValue(consCurve,"dataDate",ReflectUtil.getFieldValue(consCurveToday, "dataDate"));
            return consCurve;
        }
        return consCurve;

    }

    @Override
    public List<ConsCurve> queryTodayCurveList(List<String> elecConsNoList, String dataDate) {
        // 将营销户号转换成需要的数据格式
        String elecNoList = elecConsNoList.toString().replace("[", "").replace("]", "").replaceAll("\\s*", "");
        LocalDate parse = LocalDate.parse(dataDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        if (!LocalDate.now().isEqual(parse)) {
            // 日期不是今天，调用历史接口数据。目前没有接口，直接返回null
        }

        HashMap<String, Object> params = new HashMap<>();
        params.put("pageNum", pageNo);
        params.put("pageSize", 100);
        params.put("consNos", elecNoList);
        String replaceDate = dataDate.replace("-", "");
//        params.put("statDate", replaceDate);
        // params.put("org_no",consNo);
        log.info(">>> 数据采集监测用户实时负荷接口，请求号为：{}，数据采集监测用户实时负荷接口入参：{},,数据采集监测用户实时负荷接口地址：{}", RequestNoContext.get(), params.toString(),dataAccessTodayUrl);
        String jsonString = HttpUtil.get(dataAccessTodayUrl, params);

        //异步记录日志
//        RequestLogManager.me().executeRequestLog(dataAccessTodayUrl, JSONObject.toJSONString(params).toString(), jsonString);

        log.info(">>> 数据采集监测用户实时负荷接口，请求号为：{}，数据采集监测用户实时负荷接口返回结果：{}", RequestNoContext.get(), jsonString);
        DataAccessRealtimeResult dataAccessRealtimeResult;
        try {
            dataAccessRealtimeResult = JSONUtil.toBean(jsonString, DataAccessRealtimeResult.class);
        } catch (Exception e) {
            throw new ServiceException(ConsCurveExceptionEnum.DATACENTER_EXCPETION);
        }
        // 返回结果
        ArrayList<ConsCurve> resultCurveList = new ArrayList<>();
        log.info("中台实时接口返回结果：{}" , dataAccessRealtimeResult);
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

            log.info(">>> 模拟数据实时接口，请求号为：{}，模拟数据实时接口：{}", RequestNoContext.get(), resultCurveList);
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

    @Override
    public List<ConsCurve> queryHistoryCurveList(List<String> elecConsNolist, List<String> dataDateList) {
        if (CollectionUtil.isEmpty(dataDateList) || CollectionUtil.isEmpty(elecConsNolist)) {
            return null;
        }
        // String consNo = elecConsNolist.get(0);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        ArrayList<ConsCurve> resultConsCurves = new ArrayList<>();
        for (String consNo : elecConsNolist) {
            for (String s : dataDateList) {
                ConsCurve consCurve = this.queryDayLoadCurveByConsNo(consNo, s);
                if(null!=consCurve) {
                    consCurve.setConsId(consNo);
                    TemporalAccessor parse = dateTimeFormatter.parse(s);
                    LocalDate from = LocalDate.from(parse);
                    consCurve.setDataDate(from);
                    resultConsCurves.add(consCurve);
                }
            }
        }
        return resultConsCurves;
    }

    @Override
    public List<ConsEnergyCurve> queryDayLoadEnergyByConsNo(List<String> consIdList, String dataDate) {
        if (CollectionUtil.isEmpty(consIdList) || StringUtils.isEmpty(dataDate)) {
            List<ConsEnergyCurve> consEnergyCurve = new ArrayList<>();
            return consEnergyCurve;
        }
        String elecNoList = consIdList.toString().replace("[", "").replace("]", "").replaceAll("\\s*", "");
        HashMap<String, Object> params = new HashMap<>();
        RequestField cons_no =new RequestField("cons_no", elecNoList,"in");
        String replaceDate = dataDate.replace("-", "");
        RequestField data_dt =new RequestField("data_dt", replaceDate,"=");
//        RequestField data_type = new RequestField("data_type", "1");
        List<RequestField> requestFields = Arrays.asList(cons_no, data_dt);
        params.put("esQueries", requestFields);
        params.put("sort",new SortField());
        params.put("pageSize", 1000);
        params.put("pageNum", 1);

        JSONObject jsonObject = new JSONObject(params);


        log.info(">>> 模拟历史电量接口，请求号为：{}，模拟历史电量接口请求参数：{}", RequestNoContext.get(), jsonObject);
        String body = HttpRequest.post(dataAccessEnergyDayUrl).timeout(centerRequestTimeOut).body(jsonObject.toJSONString()).execute().body();
        String strBody = body.toLowerCase();

        //异步记录日志
//        RequestLogManager.me().executeRequestLog(dataAccessEnergyDayUrl, JSONObject.toJSONString(params).toString(), strBody);
//
        log.info(">>> 模拟历史电量接口，请求号为：{}，模拟历史电量接口请求返回：{}", RequestNoContext.get(), strBody);
        DataCenterEnergyResult dataCenterEnergyResult = JSONUtil.toBean(strBody, DataCenterEnergyResult.class);
        if (dataCenterEnergyResult.getData().getList().size() < 1) {
            List<ConsEnergyCurve> consEnergyCurve=new ArrayList<>();
            return consEnergyCurve;
        }
        List<ConsEnergyCurve> consEnergyCurveList=new ArrayList<>();
        List<DataCenterEnergy> rows = dataCenterEnergyResult.getData().getList();
        if (CollectionUtil.isNotEmpty(rows)) {
            for (DataCenterEnergy dataCenterEnergy : rows) {
                ConsEnergyCurve consEnergyCurve = new ConsEnergyCurve();
                consEnergyCurve.setDataDate(LocalDate.parse(dataDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                consEnergyCurve.setConsNo(dataCenterEnergy.getConsno());
                consEnergyCurve.setDataPointFlag(dataCenterEnergy.getData_point_flag());
                for (int i = 1; i <= 96; i++) {
                    ReflectUtil.setFieldValue(consEnergyCurve, "e" + i, ReflectUtil.getFieldValue(dataCenterEnergy, "r" + i));
                }
                consEnergyCurveList.add(consEnergyCurve);
            }
        }
        return consEnergyCurveList;
    }

    @Override
    public ConsEnergyCurve queryDayLoadEnergyByConsNo(String elecConsNo, String dataDate) {
        return null;
    }

    @Override
    public ConsEnergyCurve queryTodayLoadEnergyByConsNo(String elecConsNo, String dataDate) {
        if (StringUtils.isEmpty(elecConsNo) || StringUtils.isEmpty(dataDate)) {
            ConsEnergyCurve consEnergyCurve = new ConsEnergyCurve();
            consEnergyCurve.setConsNo(elecConsNo);
            return consEnergyCurve;
        }
        // 定义请求的参数
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("consNo", elecConsNo);
        paramsMap.put("dataTime", dataDate);
        SimulationEnergyResult simulationEnergyResult = JSONUtil.toBean(HttpUtil.get(dataAccessEnergyDayUrl, paramsMap), SimulationEnergyResult.class);
        List<ConsEnergyCurve> dataList = simulationEnergyResult.getDataList();
        if (CollectionUtil.isNotEmpty(dataList)) {
            return dataList.get(0);
        }
        ConsEnergyCurve consEnergyCurve = new ConsEnergyCurve();
        // consEnergyCurve.setDataDate(dataDate);
        consEnergyCurve.setConsNo(elecConsNo);
        return consEnergyCurve;
    }


    @Override
    public Cons getConsFromMarketing(String elecNo, String consName, String orgNo) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("appCode",appCode);
        params.put("pageNum",pageNo);
        params.put("pageSize",pageSize);

        if(ObjectUtil.isNotNull(elecNo)) {
            params.put("consNos", elecNo);
        }
        if(ObjectUtil.isNotNull(consName)) {
            params.put("cons_name", consName);
        }
        if(ObjectUtil.isNotNull(orgNo)) {
            params.put("org_no", orgNo);
        }
        log.info(">>> 营销档案接口，请求号为：{}，请求参数 {}", RequestNoContext.get(), params.toString());
        String jsonText = HttpUtil.get(consProfileAddress, params);
        // 生成记录保存到数据库
        DcRequestLog dcRequestLog = new DcRequestLog();
        dcRequestLog.setRequestUrl(consProfileAddress + params);
        dcRequestLog.setResponseData(jsonText);
        dcRequestLogService.save(dcRequestLog);
        log.info(">>> 营销档案接口，请求号为：{}，返回Json对象 {}", jsonText);
        // log.info(">>> 营销档案接口，请求号为：{}，Json对象字符串 {}",jsonText.toString());
        ProfileResult profileResult = JSONUtil.toBean(jsonText, ProfileResult.class);
        if ("0".equals(profileResult.getCode())) {
            if (ObjectUtil.isNull(profileResult.getData())) {
                throw new ServiceException(Integer.parseInt(ResponseData.SUCCESSFUL_CODE), "找不到该用户档案信息");
            }
            PageArchives pageArchives = profileResult.getData();
            if (ObjectUtil.isNotNull(pageArchives)) {
                if (CollectionUtil.isNotEmpty(pageArchives.getList())) {
                    Cons cons = new Cons();
                    ProfileObject profileObject = pageArchives.getList().get(0);
                    cons.setId(profileObject.getElecConsNo());
                    cons.setConsName(profileObject.getConsName());
                    cons.setElecAddr(profileObject.getElecAddr());
                    cons.setBigTradeName(profileObject.getBigTradeName());
                    cons.setBigTradeCode(profileObject.getBigTradeCode());
                    cons.setTradeName(profileObject.getTradeName());
                    cons.setTradeCode(profileObject.getTradeCode());
                    cons.setContractCap(profileObject.getContractCap());
                    cons.setRunCap(profileObject.getRunCap());
                    cons.setTypeCode(profileObject.getTypeCode());
                    cons.setOrgName(profileObject.getOrgName());
                    cons.setOrgNo(profileObject.getOrgNo());
                    cons.setProvinceCode(profileObject.getProvinceCode());
                    cons.setCityCode(profileObject.getCityCode());
                    cons.setCountyCode(profileObject.getCountyCode());
                    cons.setStreetCode(profileObject.getStreetCode());
                    cons.setSubsName(profileObject.getSubsName());
                    cons.setSubsNo(profileObject.getSubsNo());
                    cons.setLineName(profileObject.getLineName());
                    cons.setLineNo(profileObject.getLineNo());
                    cons.setTgName(profileObject.getTgName());
                    cons.setTgNo(profileObject.getTgNo());
                    cons.setFirstContactInfo(profileObject.getFirstContactInfo());
                    cons.setFirstContactName(profileObject.getFirstContactName());
                    cons.setSecondContactInifo(profileObject.getSecondContactInifo());
                    cons.setSecondContactName(profileObject.getSecondContactName());
                    cons.setState(profileObject.getState());
                    return cons;
                }
            }
        } else {
            throw new ServiceException(Integer.parseInt(profileResult.getCode()), profileResult.getMessage());
        }
        return null;
    }
}
