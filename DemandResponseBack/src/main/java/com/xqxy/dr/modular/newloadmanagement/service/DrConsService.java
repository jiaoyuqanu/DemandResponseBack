package com.xqxy.dr.modular.newloadmanagement.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.dr.modular.event.entity.Event;
import com.xqxy.dr.modular.newloadmanagement.entity.Drcons;
import com.xqxy.dr.modular.newloadmanagement.param.ComprehensiveIndicatorsParam;

import java.util.List;
import java.util.Map;

public interface DrConsService  {

    Map getOrgId(ComprehensiveIndicatorsParam comprehensiveIndicatorsParam);

    Map getOrgId2(Event event);

}
