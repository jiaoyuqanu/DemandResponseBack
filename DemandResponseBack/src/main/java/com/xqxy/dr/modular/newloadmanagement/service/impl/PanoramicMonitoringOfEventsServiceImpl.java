package com.xqxy.dr.modular.newloadmanagement.service.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.xqxy.core.client.SystemClient;
import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.baseline.util.CurveUtil;
import com.xqxy.dr.modular.event.entity.Event;
import com.xqxy.dr.modular.event.mapper.PanoramicMonitoringOfEventsMapper;
import com.xqxy.dr.modular.newloadmanagement.mapper.EffectEventMapper;
import com.xqxy.dr.modular.newloadmanagement.service.DrConsService;
import com.xqxy.dr.modular.newloadmanagement.service.PanoramicMonitoringOfEventsService;
import com.xqxy.dr.modular.newloadmanagement.vo.Point96Vo;
import com.xqxy.sys.modular.dict.param.DictTypeParam;
import com.xqxy.sys.modular.dict.service.DictTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Service
public class PanoramicMonitoringOfEventsServiceImpl implements PanoramicMonitoringOfEventsService {


    @Autowired
    private PanoramicMonitoringOfEventsMapper panoramicMonitoringOfEventsMapper;

    @Resource
    private DictTypeService dictTypeService;

    @Autowired
    private EffectEventMapper eventMapperm;

    @Autowired
    private DrConsService drConsService;

    @Autowired
    private SystemClient systemClient;

    @Autowired
    private PanoramicMonitoringOfEventsService panoramicMonitoringOfEventsService;

    @Override
    public Event queryInfo(Event event) {
        Event event1 = panoramicMonitoringOfEventsMapper.queryEventInfo(event);
        List<String> s = panoramicMonitoringOfEventsMapper.queryDeadLineTime(event);

        if (ObjectUtil.isNotEmpty(event1) && ObjectUtil.isNotNull(event1)) {
            if (ObjectUtil.isNotEmpty(s) && ObjectUtil.isNotNull(s)) {
                event1.setDeadLineTime2(s.get(0));
            }
        }
        return event1;
    }

    @Override
    public Point96Vo queryBaseline(Event event) {
        return panoramicMonitoringOfEventsMapper.queryBaseLine(event);
    }

    @Override
    public Point96Vo queryRealline(Event event) {
        return panoramicMonitoringOfEventsMapper.queryRealLine(event);
    }

    @Override
    public List dataStatistics(Event event) {

        Map map = new HashMap();
        BigDecimal schemeExecLoad = null;
        if (event.getOrgId().equals(getProvinceCode())) {
            schemeExecLoad = panoramicMonitoringOfEventsMapper.schemeExecLoad(event.getEd());
        } else {
            schemeExecLoad = eventMapperm.schemeExecLoadCity(event.getEd(), event.getOrgId());
        }


        ResponseData<List<String>> allNextOrgId = systemClient.getAllNextOrgId(event.getOrgId());
        List<String> data = allNextOrgId.getData();

        int invitedHouseholdsCount = panoramicMonitoringOfEventsMapper.invitedHouseholds(event.getEd(), data);

        int implementHouseholdsCount = panoramicMonitoringOfEventsMapper.implementHouseholds(event.getEd(), data, event.getPlanId());

        int nominalLoad = panoramicMonitoringOfEventsMapper.nominalLoad(event);


        Point96Vo point96Vo = panoramicMonitoringOfEventsService.queryBaseline(event);
        Point96Vo point96Vo2 = panoramicMonitoringOfEventsService.queryRealline(event);
        BigDecimal baseLineTotal = new BigDecimal(0);
        BigDecimal realLineTotal = new BigDecimal(0);
        if(ObjectUtil.isNotNull(point96Vo) && ObjectUtil.isNotEmpty(point96Vo) && ObjectUtil.isNotNull(point96Vo2) && ObjectUtil.isNotEmpty(point96Vo2)){

            Map<String, BigDecimal> stringObjectMap = JSON.parseObject(JSON.toJSONString(point96Vo), new TypeReference<Map<String, BigDecimal>>() {
            });
             baseLineTotal = stringObjectMap.values().stream().reduce(BigDecimal::add).orElse(new BigDecimal("0"));

            Map<String, BigDecimal> stringObjectMap2 = JSON.parseObject(JSON.toJSONString(point96Vo2), new TypeReference<Map<String, BigDecimal>>() {
            });
             realLineTotal = stringObjectMap2.values().stream().reduce(BigDecimal::add).orElse(new BigDecimal("0"));
        }

        int numberOfQualifiedHouseholdsCount = panoramicMonitoringOfEventsMapper.numberOfQualifiedHouseholds(event.getEd(), data);

        int numberOfSubstandardHouseholdsCount = panoramicMonitoringOfEventsMapper.numberOfSubstandardHouseholds(event.getEd(), data);

//        LocalDateTime now = LocalDateTime.now();
//        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        LocalDateTime localDateTime = LocalDateTime.now();
//        String dateStr = localDateTime.format(fmt);
//        int i = CurveUtil.covDateTimeToPoint(dateStr);
        int i = CurveUtil.covDateTimeToPoint(LocalTime.now().getHour() + ":" + LocalTime.now().getMinute());


        BigDecimal nowPressureDropLoad = panoramicMonitoringOfEventsMapper.nowPressureDropLoad("p" + i, event.getEd(), event.getOrgId());


        Map<Integer, BigDecimal> map2 = new HashMap();
        for (int k = 1; k <= 96; k++) {
            BigDecimal fieldValue = (BigDecimal) ReflectUtil.getFieldValue(point96Vo, "P" + k);
            BigDecimal fieldValue2 = (BigDecimal) ReflectUtil.getFieldValue(point96Vo2, "P" + k);
            if (ObjectUtil.isNotEmpty(fieldValue) && ObjectUtil.isNotEmpty(fieldValue2)) {
                map2.put(k, fieldValue.subtract(fieldValue2));
            }
        }

        BigDecimal all = new BigDecimal(0);
        for (Map.Entry<Integer, BigDecimal> entry : map2.entrySet()) {
            all = all.add(entry.getValue());
        }
        BigDecimal allNominalLoad = new BigDecimal(nominalLoad);
        BigDecimal implementRate = new BigDecimal(0);
        if(allNominalLoad.compareTo(new BigDecimal(0))!=0){
             implementRate = all.divide(allNominalLoad);
        }


        BigDecimal max = new BigDecimal(0);
        for (Map.Entry<Integer, BigDecimal> entry : map2.entrySet()) {
            if (max.compareTo(entry.getValue()) == -1) {
                max = entry.getValue();
            }
        }

        int point = 1;
        for (Map.Entry<Integer, BigDecimal> entry : map2.entrySet()) {
            if (max.compareTo(entry.getValue()) == 0) {
                point = entry.getKey();
            }
        }
        String time = CurveUtil.covCurvePointToDateTime("p" + point);


        map.put("schemeExecLoad", schemeExecLoad);
        map.put("invitedHouseholdsCount", invitedHouseholdsCount);
        map.put("implementHouseholdsCount", implementHouseholdsCount);
        map.put("nominalLoad", nominalLoad);
        map.put("baseLineTotal", baseLineTotal);
        map.put("realLineTotal", realLineTotal);
        map.put("implementRate", implementRate);
        map.put("numberOfQualifiedHouseholdsCount", numberOfQualifiedHouseholdsCount);
        map.put("numberOfSubstandardHouseholdsCount", numberOfSubstandardHouseholdsCount);
        map.put("nowPressureDropLoad", nowPressureDropLoad);
        map.put("maxPressureDropLoad", max);
        map.put("time", time);


        List<Map> list = new ArrayList<>();
        list.add(map);

        return list;
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
}
