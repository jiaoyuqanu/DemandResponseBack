package com.xqxy.dr.modular.upload.controller;


import com.xqxy.dr.modular.upload.entity.ContractInfo;
import com.xqxy.dr.modular.upload.service.IncidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 需求响应事件 前端控制器
 * </p>
 *
 */

@RestController
@RequestMapping("/incident")
public class IncidentController {

    @Autowired
    private IncidentService incidentService;

    @RequestMapping("/getincident")
    public List<ContractInfo> getIncident(){
        return incidentService.getIncident();
    }
}
