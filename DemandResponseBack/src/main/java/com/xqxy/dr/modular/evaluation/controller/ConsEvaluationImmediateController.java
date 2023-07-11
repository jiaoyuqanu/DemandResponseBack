package com.xqxy.dr.modular.evaluation.controller;


import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.evaluation.param.ConsEvaluationImmediateParam;
import com.xqxy.dr.modular.evaluation.param.ConsEvaluationParam;
import com.xqxy.dr.modular.evaluation.param.EvaluationImmediateParam;
import com.xqxy.dr.modular.evaluation.service.ConsEvaluationImmediateService;
import com.xqxy.dr.modular.evaluation.service.ConsEvaluationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 今日响应效果评估 前端控制器
 * </p>
 *
 * @author Peng
 * @since 2021-10-29
 */
@Api(tags = "用户当日效果评估API接口")
@RestController
@RequestMapping("/evaluation/cons-evaluation-immediate")
public class ConsEvaluationImmediateController {

    @Resource
    private ConsEvaluationImmediateService consEvaluationImmediateService;

    /**
     * @description: 用户当日效果评估分页查询
     * @param: 
     * @return: 
     * @author: PengChuqing
     * @date: 2021/10/29 10:13
     */
    @ApiOperation(value = "用户当日效果评估分页查询", notes = "用户当日效果评估分页查询", produces = "application/json")
    @PostMapping("/page")
    public ResponseData page(@RequestBody ConsEvaluationImmediateParam evaluationImmediateParam) {
        return ResponseData.success(consEvaluationImmediateService.page(evaluationImmediateParam));
    }



    @ApiOperation(value = "当日效果评估报表导出", notes = "当日效果评估报表导出", produces = "application/json")
    @PostMapping("/exportEvaluationImmediate")
    public void exportEvaluationImmediate(@RequestBody EvaluationImmediateParam evaluationImmediateParam) {
        consEvaluationImmediateService.exportEvaluationImmediate(evaluationImmediateParam);
    }
}

