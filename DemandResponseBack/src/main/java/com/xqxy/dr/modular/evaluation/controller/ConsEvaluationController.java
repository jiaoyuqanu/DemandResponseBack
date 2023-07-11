package com.xqxy.dr.modular.evaluation.controller;

import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.evaluation.service.ConsEvaluationService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xqxy.dr.modular.evaluation.param.ConsEvaluationParam;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


import javax.annotation.Resource;


/**
 * <p>
 * 用户执行效果评估 前端控制器
 * </p>
 *
 * @author hu xingxing
 * @since 2021-10-20
 */
@Api(tags = "用户次日效果评估API接口")
@RestController
@RequestMapping("/evaluation/cons-evaluation")
public class ConsEvaluationController {

    @Resource
    ConsEvaluationService consEvaluationService;

//    @BusinessLog(title = "用户执行效果评估", opType = LogAnnotionOpTypeEnum.QUERY)
    @ApiOperation(value = "用户执行效果评估", notes = "用户执行效果评估", produces = "application/json")
    @GetMapping("/detail")
    public ResponseData detail(@RequestParam(required = true,name = "eventId") Long eventId,@RequestParam(required = true,name = "consId") String consId) {
        return ResponseData.success(consEvaluationService.getConsEvaluationByEventIdAndConsId(eventId,consId));
    }


    @ApiOperation(value = "用户效果评估分页查询", notes = "响应效果评估分页查询", produces = "application/json")
    @PostMapping("/page")
    public ResponseData page(@RequestBody ConsEvaluationParam consEvaluationParam) {
        return ResponseData.success(consEvaluationService.page(consEvaluationParam));
    }


    @ApiOperation(value = "次日效果评估报表导出", notes = "当日效果评估报表导出", produces = "application/json")
    @PostMapping("/exportEvaluationImmediate")
    public void exportEvaluationImmediate(@RequestBody ConsEvaluationParam consEvaluationParam) {
        consEvaluationService.exportEvaluationImmediate(consEvaluationParam);
    }

    @ApiOperation(value = "用户事件执行曲线", notes = "用户事件执行曲线", produces = "application/json")
    @PostMapping("/implementCurve")
    public ResponseData implementCurve(@RequestBody ConsEvaluationParam consEvaluationParam) {
       return ResponseData.success(consEvaluationService.implementCurve(consEvaluationParam));
    }



}

