package com.xqxy.dr.modular.event.mapper;

import com.xqxy.dr.modular.event.entity.Event;
import com.xqxy.dr.modular.newloadmanagement.service.EffecteEvaluationService;
import com.xqxy.dr.modular.newloadmanagement.vo.Point96Vo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface PanoramicMonitoringOfEventsMapper {

    Event queryEventInfo(Event event);

    List<String> queryDeadLineTime(Event event);

    Point96Vo queryBaseLine(Event event);

    Point96Vo queryRealLine(Event event);

    int invitedHouseholds(@Param(value = "event_id")String eventid, @Param(value = "org_nos")List orgNos);

    int implementHouseholds(@Param(value = "event_id")String ed, @Param(value = "org_nos")List<String> data,@Param(value = "planID") Long planId);

    int nominalLoad(Event event);

    int  numberOfQualifiedHouseholds(@Param(value = "event_id")String ed, @Param(value = "org_nos")List<String> data);

    int numberOfSubstandardHouseholds(@Param(value = "event_id")String ed, @Param(value = "org_nos")List<String> data);

    BigDecimal nowPressureDropLoad(@Param(value = "pi") String pi,@Param(value = "event_id")String eventid,@Param(value = "orgNo")String orgNo);

    BigDecimal schemeExecLoad(@Param(value = "event_id")String eventid );

}
