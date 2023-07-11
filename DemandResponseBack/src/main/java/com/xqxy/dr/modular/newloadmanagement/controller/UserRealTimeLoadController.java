package com.xqxy.dr.modular.newloadmanagement.controller;


import com.xqxy.dr.modular.newloadmanagement.param.ComprehensiveIndicatorsParam;
import com.xqxy.dr.modular.newloadmanagement.service.UserRealTimeLoadService;
import com.xqxy.dr.modular.newloadmanagement.util.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sea-opu/userrealtimeload")
@Slf4j
public class UserRealTimeLoadController {

    @Autowired
    private UserRealTimeLoadService userRealTimeLoadService;

    @RequestMapping("/queryrealtimeload")
    public ResponseResult queryAllLoad(@RequestBody ComprehensiveIndicatorsParam comprehensiveIndicatorsParam){


        List<Map> allLoad = userRealTimeLoadService.getAllLoad(comprehensiveIndicatorsParam);

        return new ResponseResult(200, "", allLoad, true);
    }


}
