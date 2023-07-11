package com.xqxy.dr.modular.newloadmanagement.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.event.entity.Event;
import com.xqxy.dr.modular.event.result.EventExecuteCurveResult;
import com.xqxy.dr.modular.newloadmanagement.service.PanoramicMonitoringOfEventsService;
import com.xqxy.dr.modular.newloadmanagement.vo.Point96Vo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/panoramicMonitoringOfEvents")
public class PanoramicMonitoringOfEvents {


    @Autowired
    private PanoramicMonitoringOfEventsService panoramicMonitoringOfEventsService;

    @PostMapping("/queryInfo")
    public ResponseData queryEventInfo(@RequestBody Event event) {

        Event event1 = panoramicMonitoringOfEventsService.queryInfo(event);

        return  ResponseData.success(event1);
    }


    @PostMapping("/executeCurve")
    public ResponseData queryCurve(@RequestBody Event event) {

        EventExecuteCurveResult eventExecuteCurveResult = null;
        if(event.getAllDayTime().equals("2")){
            Point96Vo point96Vo = panoramicMonitoringOfEventsService.queryBaseline(event);

            Point96Vo point96Vo2 = panoramicMonitoringOfEventsService.queryRealline(event);
            if(ObjectUtil.isNotNull(point96Vo) && ObjectUtil.isNotEmpty(point96Vo)){
                eventExecuteCurveResult.setBaseline(point96Vo);
            }else {
                point96Vo.setP1(point96Vo.getP25());
                point96Vo.setP2(point96Vo.getP26());
                point96Vo.setP3(point96Vo.getP27());
                point96Vo.setP4(point96Vo.getP28());
                point96Vo.setP5(point96Vo.getP29());
                point96Vo.setP6(point96Vo.getP30());
                point96Vo.setP7(point96Vo.getP31());
                point96Vo.setP8(point96Vo.getP32());
                point96Vo.setP9(point96Vo.getP33());
                point96Vo.setP10(point96Vo.getP34());
                point96Vo.setP11(point96Vo.getP35());
                point96Vo.setP12(point96Vo.getP36());
                point96Vo.setP13(point96Vo.getP37());
                point96Vo.setP14(point96Vo.getP38());
                point96Vo.setP15(point96Vo.getP39());
                point96Vo.setP16(point96Vo.getP40());
                point96Vo.setP17(point96Vo.getP41());
                point96Vo.setP18(point96Vo.getP42());
                point96Vo.setP19(point96Vo.getP43());
                point96Vo.setP20(point96Vo.getP44());
                point96Vo.setP21(point96Vo.getP45());
                point96Vo.setP22(point96Vo.getP46());
                point96Vo.setP23(point96Vo.getP47());
                point96Vo.setP24(point96Vo.getP48());
                eventExecuteCurveResult.setBaseline(point96Vo);
            }
            if(ObjectUtil.isNotNull(point96Vo2) && ObjectUtil.isNotEmpty(point96Vo2)){
                eventExecuteCurveResult.setBaseline(point96Vo2);
            }else {
                point96Vo2.setP1(point96Vo2.getP25());
                point96Vo2.setP2(point96Vo2.getP26());
                point96Vo2.setP3(point96Vo2.getP27());
                point96Vo2.setP4(point96Vo2.getP28());
                point96Vo2.setP5(point96Vo2.getP29());
                point96Vo2.setP6(point96Vo2.getP30());
                point96Vo2.setP7(point96Vo2.getP31());
                point96Vo2.setP8(point96Vo2.getP32());
                point96Vo2.setP9(point96Vo2.getP33());
                point96Vo2.setP10(point96Vo2.getP34());
                point96Vo2.setP11(point96Vo2.getP35());
                point96Vo2.setP12(point96Vo2.getP36());
                point96Vo2.setP13(point96Vo2.getP37());
                point96Vo2.setP14(point96Vo2.getP38());
                point96Vo2.setP15(point96Vo2.getP39());
                point96Vo2.setP16(point96Vo2.getP40());
                point96Vo2.setP17(point96Vo2.getP41());
                point96Vo2.setP18(point96Vo2.getP42());
                point96Vo2.setP19(point96Vo2.getP43());
                point96Vo2.setP20(point96Vo2.getP44());
                point96Vo2.setP21(point96Vo2.getP45());
                point96Vo2.setP22(point96Vo2.getP46());
                point96Vo2.setP23(point96Vo2.getP47());
                point96Vo2.setP24(point96Vo2.getP48());
                eventExecuteCurveResult.setBaseline(point96Vo2);
            }




        }else {
            Point96Vo point96Vo = panoramicMonitoringOfEventsService.queryBaseline(event);
            Point96Vo point96Vo2 = panoramicMonitoringOfEventsService.queryRealline(event);

            eventExecuteCurveResult = new EventExecuteCurveResult();

            eventExecuteCurveResult.setBaseline(point96Vo);
            eventExecuteCurveResult.setRealLine(point96Vo2);

            if(ObjectUtil.isNotNull(point96Vo) && ObjectUtil.isNotEmpty(point96Vo) && ObjectUtil.isNotNull(point96Vo2) && ObjectUtil.isNotEmpty(point96Vo2) ){
                Map<String, BigDecimal> stringObjectMap = JSON.parseObject(JSON.toJSONString(point96Vo ), new TypeReference<Map<String, BigDecimal>>() {
                });
                BigDecimal targetTotal = stringObjectMap.values().stream().reduce(BigDecimal::add).orElse(new BigDecimal("0"));
                eventExecuteCurveResult.setBaseLineCumulative(targetTotal);

                Map<String, BigDecimal> stringObjectMap2 = JSON.parseObject(JSON.toJSONString(point96Vo2 ), new TypeReference<Map<String, BigDecimal>>() {
                });
                BigDecimal targetTotal2 = stringObjectMap2.values().stream().reduce(BigDecimal::add).orElse(new BigDecimal("0"));
                eventExecuteCurveResult.setRealLineCumulative(targetTotal2);

                eventExecuteCurveResult.setPressureLineCumulative(targetTotal.subtract(targetTotal2));
            }
        }

        return  ResponseData.success(eventExecuteCurveResult);
    }


    @PostMapping("/dataStatistics")
    public ResponseData dataStatistics(@RequestBody Event event) {

        List list = panoramicMonitoringOfEventsService.dataStatistics(event);

        return  ResponseData.success(list);
    }






}
