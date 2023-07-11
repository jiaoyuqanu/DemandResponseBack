package com.xqxy.dr.modular.upload.service.impl;


import com.xqxy.dr.modular.upload.entity.ContractInfo;
import com.xqxy.dr.modular.upload.mapper.IncidentMapper;
import com.xqxy.dr.modular.upload.service.IncidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 需求响应事件 服务实现类
 * </p>
 *
 */
@Service
public class IncidentServiceImpl implements IncidentService {

    @Autowired
    private IncidentMapper incidentMapper;

    @Override
    public List<ContractInfo> getIncident(){
        return incidentMapper.getIncident();
    }
}
