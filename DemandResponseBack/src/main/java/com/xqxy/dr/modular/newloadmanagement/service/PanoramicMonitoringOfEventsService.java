package com.xqxy.dr.modular.newloadmanagement.service;

import com.xqxy.dr.modular.event.entity.Event;
import com.xqxy.dr.modular.newloadmanagement.vo.Point96Vo;

import java.util.List;

public interface PanoramicMonitoringOfEventsService {

    Event queryInfo(Event event);

    Point96Vo queryBaseline(Event event);

    Point96Vo queryRealline(Event event);

    List dataStatistics(Event event);
}
