package com.xqxy.dr.modular.newloadmanagement.service.impl;

import cn.hutool.core.lang.Dict;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.xqxy.dr.modular.baseline.util.CurveUtil;
import com.xqxy.dr.modular.newloadmanagement.mapper.DrConsMapper;
import com.xqxy.dr.modular.newloadmanagement.mapper.EffectEventMapper;
import com.xqxy.dr.modular.newloadmanagement.param.ComprehensiveIndicatorsParam;
import com.xqxy.dr.modular.newloadmanagement.service.DrConsService;
import com.xqxy.dr.modular.newloadmanagement.service.EffecteEvaluationService;
import com.xqxy.dr.modular.newloadmanagement.vo.DrEventTime;
import com.xqxy.dr.modular.newloadmanagement.vo.Point96Vo;
import com.xqxy.sys.modular.dict.param.DictTypeParam;
import com.xqxy.sys.modular.dict.service.DictTypeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EffecteEvaluationServiceImpl implements EffecteEvaluationService {


    @Autowired
    private EffectEventMapper eventMapperm;

    @Autowired
    private DrConsService drConsService;

    @Autowired
    private DrConsMapper drConsMapper;


    @Resource
    private DictTypeService dictTypeService;




    @Override
    public List effectEvaluationInfor(ComprehensiveIndicatorsParam comprehensiveIndicatorsPara) {

        List list = new ArrayList();
        Map<String, List> orgIdMap = drConsService.getOrgId(comprehensiveIndicatorsPara);
        for (Map.Entry<String, List> entry : orgIdMap.entrySet()) {
            List eventId2 = null;
            String timeList = null;
            List timeList2 = null;
            Integer userCount = null;
            BigDecimal maxPeakLoad = null;
            BigDecimal maxGrainLoad = null;
            BigDecimal peakElectric = null;
            Long timeLong = null;
            String times = null;
            BigDecimal timeLong2 = null;

            Map map = new HashMap();
            List eventId = eventMapperm.getEventId(entry.getKey());
            if (eventId != null && eventId.size() > 0) {
                if (comprehensiveIndicatorsPara.getQueryDate() == null) {
                    LocalDate now = LocalDate.now();
                    LocalDate localDate = null;
                    if (now.getDayOfMonth() > 1) {
                        localDate = now.withDayOfMonth(now.getDayOfMonth() - 1);
                    } else {
                        localDate = now.withMonth(now.getMonthValue() - 1).with(TemporalAdjusters.lastDayOfMonth());
                    }
                    eventId2 = eventMapperm.getEventId2(eventId, localDate);
                } else {
                    eventId2 = eventMapperm.getEventId2(eventId, comprehensiveIndicatorsPara.getQueryDate());
                }

                if (eventId2 != null && eventId2.size() > 0) {
                    DrEventTime time = eventMapperm.getTime2(eventId2.get(0).toString());
                    if (time != null) {
                        times = time.getStartTime() + "-" + time.getEndTime();

                        String endTime = time.getEndTime();
                        String startTime = time.getStartTime();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                        try {
                            Date endT = simpleDateFormat.parse(endTime);
                            Date strT = simpleDateFormat.parse(startTime);
                            timeLong = endT.getTime() - strT.getTime();
                            timeLong2 = new BigDecimal(Double.valueOf(timeLong) / (1000 * 60 * 60)).setScale(2, RoundingMode.HALF_UP);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                    List peakLoadEventId = eventMapperm.peakLoadEventId2(eventId2.get(0).toString());
                    if (peakLoadEventId != null && peakLoadEventId.size() > 0) {
                        maxPeakLoad = eventMapperm.maxPeakLoad(peakLoadEventId,entry.getKey());
                    }
                    List grainLoadEventId = eventMapperm.grainLoadEventId2(eventId2.get(0).toString());
                    if (grainLoadEventId != null && grainLoadEventId.size() > 0) {
                        maxGrainLoad = eventMapperm.maxGrainLoad(grainLoadEventId,entry.getKey());
                    }

//                    List<DrEventTime> time = eventMapperm.getTime(eventId2);
//                    for (DrEventTime drEventTime : time) {
//                        if(null!=drEventTime && null!=drEventTime.getStartTime()&&null!=drEventTime.getEndTime()){
//                                String times=drEventTime.getStartTime()+"-"+drEventTime.getEndTime();
//                                timeList2 = new ArrayList();
//                                timeList2.add(times);
//                        }
//                    }
//                    timeList = timeList2.get(0).toString();
//                    String endTime = time.get(0).getEndTime();
//                    String startTime = time.get(0).getStartTime();
//                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
//                    try {
//                        Date endT = simpleDateFormat.parse(endTime);
//                        Date strT = simpleDateFormat.parse(startTime);
//                        timeLong = endT.getTime()-strT.getTime();
//
//                        map.put("elecutionTime",timeLong);
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//
//                    List peakLoadEventId = eventMapperm.peakLoadEventId(eventId2);
//                    if (peakLoadEventId != null && peakLoadEventId.size()>0){
//                        maxPeakLoad = eventMapperm.maxPeakLoad(peakLoadEventId);
//                    }
//                    List grainLoadEventId = eventMapperm.grainLoadEventId(eventId2);
//                    if (grainLoadEventId != null && grainLoadEventId.size()>0){
//                        maxGrainLoad = eventMapperm.maxGrainLoad(grainLoadEventId);
//                    }

                    List<String> planId = drConsMapper.planId(eventId2.get(0).toString());
                    List<String> consIds = drConsMapper.consIds(entry.getValue());
                    if (null != consIds && consIds.size() > 0) {
                        userCount = eventMapperm.userCount(consIds, planId);
                    }
                    BigDecimal schemeExecLoad = null;
                    if(entry.getKey().equals(getProvinceCode())){
                         schemeExecLoad = eventMapperm.schemeExecLoad(eventId2.get(0).toString());
                    }else {
                        schemeExecLoad = eventMapperm.schemeExecLoadCity(eventId2.get(0).toString(),entry.getKey());
                    }

                    Integer execCount = eventMapperm.execCount(eventId2.get(0).toString(),entry.getValue());
//                    time.setStartTime("11:12");//p45
//                    time.setStartTime("11:15");//p46
//                    time.setEndTime("11:55");
                    int iStrat = CurveUtil.covDateTimeToPoint(time.getStartTime());
                    int iEnd = CurveUtil.covDateTimeToPoint(time.getEndTime());
                    List piList = new ArrayList();
                    for (int i = iStrat; i <= iEnd; i++) {
                        piList.add("p" + i);
                    }
                    if (!time.getStartTime().split(":")[1].equals("00")
                            && !time.getStartTime().split(":")[1].equals("15")
                            && !time.getStartTime().split(":")[1].equals("30")
                            && !time.getStartTime().split(":")[1].equals("45")
                    ){
                        piList.remove(0);
                    }

                    peakElectric = eventMapperm.actualEnergy(eventId2.get(0).toString(),entry.getKey());




                    Point96Vo point96Vo = eventMapperm.lowerMap(eventId2.get(0).toString(), piList, entry.getKey());

                    Map map1 = new HashMap();
                    if(point96Vo!=null){
                        String[] timeSplit = time.getStartTime().split(":");
                        String tm = null;

                        if (Integer.parseInt(timeSplit[1]) > 0 && Integer.parseInt(timeSplit[1]) <= 15) {
                            tm = timeSplit[0] + "15";
                        } else if (Integer.parseInt(timeSplit[1]) > 15 && Integer.parseInt(timeSplit[1]) <= 30) {
                            tm = timeSplit[0] + "30";
                        } else if (Integer.parseInt(timeSplit[1]) > 30 && Integer.parseInt(timeSplit[1]) <= 45) {
                            tm = timeSplit[0] + "45";
                        }else if (Integer.parseInt(timeSplit[1]) > 45    && Integer.parseInt(timeSplit[1]) < 60) {
                            tm = (Integer.parseInt(timeSplit[0])+1) + "00";
                            if(tm.length()==3){
                                tm="0"+tm;
                            }
                        } else {
                            tm = timeSplit[0] + "00";
                        }

                        Map toJSON = (Map) JSON.toJSON(point96Vo);
                        JSONObject jsonObject = new JSONObject();

                        for (Object pi : piList) {
                            BigDecimal piValue = (BigDecimal) toJSON.get(pi);
                            map1.put(tm, piValue);
                            if (tm.substring(2, 4).equals("45")) {
                                tm = (Integer.parseInt(tm.substring(0, 2)) +1) + "00";
                            } else {
                                tm = (Integer.parseInt(tm) + 15) + "";
                            }
                        }
                    }

                    if (eventId2 != null) {
                        map.put("orgId", entry.getKey());
                        map.put("eventId", (eventId2 == null || eventId2.size() <= 0) ? null : eventId2.get(0).toString());
                        map.put("elecutionDate", times);
                        map.put("elecutionTime", timeLong2);
                        map.put("count", userCount);
                        map.put("peakElectric", peakElectric);
                        map.put("fillElectric", null);
                        map.put("maxPeakLoad", maxPeakLoad);
                        map.put("maxFillLoad", maxGrainLoad);
                        map.put("schemeUsers", userCount);
                        map.put("schemeExecLoad", schemeExecLoad);
                        map.put("execCount", execCount);
                        map.put("lowerMap",map1);
                        list.add(map);
                    }
                }
            }
        }
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
