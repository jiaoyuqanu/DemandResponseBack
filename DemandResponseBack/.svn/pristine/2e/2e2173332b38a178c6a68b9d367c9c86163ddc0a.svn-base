package com.xqxy.dr.modular.newloadmanagement.controller;


import com.xqxy.dr.modular.newloadmanagement.param.ComprehensiveIndicatorsParam;
import com.xqxy.dr.modular.newloadmanagement.param.DemandEvaluationParam;

import com.xqxy.dr.modular.newloadmanagement.service.ComprehensiveIndicatorsService;
import com.xqxy.dr.modular.newloadmanagement.util.ResponseResult;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description 需求响应综合指标数据
 * @Author jyq
 * @Date 2022/6/20
 */
@RestController
@RequestMapping("/sea-opu/compositeIndicator")
@Slf4j
public class CompositeIndicatorController {

    @Autowired
    private ComprehensiveIndicatorsService comprehensiveIndicatorsService;

     @RequestMapping("/querycompositeindicator")
     public ResponseResult  queryCompositeIndicator(@RequestBody ComprehensiveIndicatorsParam comprehensiveIndicatorsParam){


         List data = comprehensiveIndicatorsService.getIndicators(comprehensiveIndicatorsParam);

         return new ResponseResult(200, "", data, true);
     }

}
