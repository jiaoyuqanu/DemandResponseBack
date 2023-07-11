package com.xqxy.dr.modular.newloadmanagement.service.impl;

import com.xqxy.dr.modular.newloadmanagement.mapper.DrConsMapper;
import com.xqxy.dr.modular.newloadmanagement.mapper.UserDeclarationMapper;
import com.xqxy.dr.modular.newloadmanagement.param.ComprehensiveIndicatorsParam;
import com.xqxy.dr.modular.newloadmanagement.service.ComprehensiveIndicatorsService;
import com.xqxy.dr.modular.newloadmanagement.service.DrConsService;
import com.xqxy.dr.modular.newloadmanagement.service.DrProjectService;
import org.apache.poi.hpsf.Decimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ComprehensiveIndicatorsImpl implements ComprehensiveIndicatorsService {

    @Autowired
    private UserDeclarationMapper userDeclarationMapper;

    @Autowired
    private DrProjectService drProjectService;

    @Autowired
    private DrConsService drConsService;

    @Autowired
    private DrConsMapper drConsMapper;

    @Override
    public List getIndicators(ComprehensiveIndicatorsParam comprehensiveIndicatorsParam) {

        List list = new ArrayList();
        BigDecimal sumContractCap=null;
        Integer userCount=null;
        List<Long> pros = drProjectService.queryUseCount(comprehensiveIndicatorsParam);
        Map<String,List> orgIdMap = drConsService.getOrgId(comprehensiveIndicatorsParam);
        for (Map.Entry<String,List> entry : orgIdMap.entrySet()) {
            Map map = new HashMap();
            List<String> orgIds = drConsMapper.consIds(entry.getValue());
            if(null!=orgIds && orgIds.size()>0){
                if(null!=pros && pros.size()>0){
                    userCount = userDeclarationMapper.userCount(pros,orgIds);
                    List<String> contractIds = userDeclarationMapper.contractId(pros, orgIds);
                    if(null!=contractIds && contractIds.size()>0){
                        sumContractCap = userDeclarationMapper.sumContractCap(contractIds);
                    }
                }
            }
            map.put("orgId",entry.getKey());
            map.put("userCount",userCount);
            map.put("sumContractCap",sumContractCap);
            list.add(map);
        }
        return list;
    }

}
