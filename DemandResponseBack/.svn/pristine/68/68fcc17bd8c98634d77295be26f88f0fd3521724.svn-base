package com.xqxy.dr.modular.newloadmanagement.mapper;

import com.xqxy.dr.modular.newloadmanagement.entity.*;
import com.xqxy.dr.modular.newloadmanagement.po.DemandEvaluationPo;
import com.xqxy.dr.modular.newloadmanagement.vo.DrEventTime;
import com.xqxy.dr.modular.newloadmanagement.vo.OrgDemand64PointVo;
import com.xqxy.dr.modular.newloadmanagement.vo.Point96Vo;
import com.xqxy.dr.modular.newloadmanagement.vo.RegionContractInfoVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Mapper
public interface EffectEventMapper {

    List getEventId(String orgNo);

    List getEventId2(@Param(value = "event_id") List eventid, @Param(value = "time")LocalDate time);

    List peakLoadEventId(@Param(value = "event_id") List eventid);

    List peakLoadEventId2(@Param(value = "event_id") String eventid);

    BigDecimal maxPeakLoad(@Param(value = "event_id") List eventid,@Param(value = "org_id") String org_id);

    BigDecimal maxPeakLoad2(@Param(value = "event_id") String eventid);

    List grainLoadEventId(@Param(value = "event_id") List eventide);

    List grainLoadEventId2(@Param(value = "event_id") String eventide);

    BigDecimal maxGrainLoad(@Param(value = "event_id") List eventid,@Param(value = "org_id") String org_id);

    BigDecimal maxGrainLoad2(@Param(value = "event_id") String eventid);

    List<DrEventTime> getTime(@Param(value = "event_id")List eventid);

    DrEventTime getTime2(@Param(value = "event_id")String eventid);

    Integer userCount(@Param(value = "IDs")List orgNos,@Param(value = "planId")List planId);


    BigDecimal schemeExecLoad(@Param(value = "event_id")String eventid );

    Integer execCount(@Param(value = "event_id")String eventid,@Param(value = "org_nos")List orgNos );

    Point96Vo lowerMap(@Param(value = "event_id")String eventid, @Param(value = "piList")List piList,@Param(value = "orgNo")String orgNo);


    BigDecimal actualEnergy(@Param(value = "event_id")String eventid,@Param(value = "org_no")String orgNo );

    BigDecimal schemeExecLoadCity(@Param(value = "event_id")String eventid,@Param(value = "org_id")String orgNo);
}
