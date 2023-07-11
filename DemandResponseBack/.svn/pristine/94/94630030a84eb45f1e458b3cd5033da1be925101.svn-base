package com.xqxy.dr.modular.newloadmanagement.service.impl;

import com.xqxy.dr.modular.baseline.util.CurveUtil;
import com.xqxy.dr.modular.newloadmanagement.mapper.DrConsMapper;
import com.xqxy.dr.modular.newloadmanagement.mapper.UserRealTimeLoadMapper;
import com.xqxy.dr.modular.newloadmanagement.param.ComprehensiveIndicatorsParam;
import com.xqxy.dr.modular.newloadmanagement.service.DrConsService;
import com.xqxy.dr.modular.newloadmanagement.service.UserRealTimeLoadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserRealTimeLoadServiceImpl implements UserRealTimeLoadService {

    @Autowired
    private UserRealTimeLoadMapper userRealTimeLoadMapper;

    @Autowired
    private DrConsService drConsService;

    @Autowired
    private DrConsMapper drConsMapper;

    @Override
    public List getAllLoad(ComprehensiveIndicatorsParam comprehensiveIndicatorsParam) {

            BigDecimal realLoad=null;
        List list = new ArrayList();
        Map<String, List> orgIdMap = drConsService.getOrgId(comprehensiveIndicatorsParam);
        for (Map.Entry<String,List> entry : orgIdMap.entrySet()) {
            List<String> consId = drConsMapper.consIds(entry.getValue());
            if(null!=consId && consId.size()>0){
                if(comprehensiveIndicatorsParam.getQueryDate()==null){
                    int i = CurveUtil.covDateTimeToPoint(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
                    String pi="p"+i;
                    realLoad = userRealTimeLoadMapper.realLoad(pi,consId, LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                }else {
                    int i = CurveUtil.covDateTimeToPoint(comprehensiveIndicatorsParam.getQueryDate().format(DateTimeFormatter.ofPattern("HH:mm")));
                     String pi="p"+i;
                    realLoad = userRealTimeLoadMapper.realLoad(pi,consId,comprehensiveIndicatorsParam.getQueryDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                }
            }

            Map map = new HashMap();
            map.put("orgId",entry.getKey());
            map.put("realLoad",realLoad);
            list.add(map);
        }
        return list;
    }
}
