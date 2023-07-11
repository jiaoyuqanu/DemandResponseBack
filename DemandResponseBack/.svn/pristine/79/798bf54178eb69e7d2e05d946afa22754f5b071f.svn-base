package com.xqxy.dr.modular.newloadmanagement.controller;


import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.newloadmanagement.param.ComprehensiveIndicatorsParam;
import com.xqxy.dr.modular.newloadmanagement.service.EffecteEvaluationService;
import com.xqxy.dr.modular.newloadmanagement.util.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/sea-opu/effectevaluation")
@Slf4j
public class EffectEvaluationController {

    @Autowired
    private EffecteEvaluationService effecteEvaluationService;

    @RequestMapping(value = "/effectevaluationinfo",method = RequestMethod.POST)
    public ResponseResult effecteEvaluationInfo(@RequestBody ComprehensiveIndicatorsParam comprehensiveIndicatorsParam){
        List list = effecteEvaluationService.effectEvaluationInfor(comprehensiveIndicatorsParam);
        return new ResponseResult(200, "", list, true);

    }


}
