package com.xqxy.dr.modular.newloadmanagement.service;

import com.xqxy.dr.modular.newloadmanagement.param.ComprehensiveIndicatorsParam;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface UserRealTimeLoadService {

    List<Map> getAllLoad(ComprehensiveIndicatorsParam comprehensiveIndicatorsParam);

}
