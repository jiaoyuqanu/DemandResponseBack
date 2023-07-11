package com.xqxy.dr.modular.evaluation.controller;


import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.evaluation.param.ConsEvaluationParam;
import com.xqxy.dr.modular.evaluation.param.CustEvaluationParam;
import com.xqxy.dr.modular.evaluation.service.ConsEvaluationService;
import com.xqxy.dr.modular.evaluation.service.CustEvaluationService;
import com.xqxy.dr.modular.project.params.ProjectParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 客户执行效果评估 前端控制器
 * </p>
 *
 * @author hu xingxing
 * @since 2021-10-22
 */
@Api(tags = "客户次日效果评估API接口")
@RestController
@RequestMapping("/evaluation/cust-evaluation")
public class CustEvaluationController {

    @Resource
    private CustEvaluationService custEvaluationService;

    @ApiOperation(value = "客户效果评估分页查询", notes = "客户效果评估分页查询", produces = "application/json")
    @PostMapping("/page")
    public ResponseData page(@RequestBody CustEvaluationParam custEvaluationParam) {
        return ResponseData.success(custEvaluationService.page(custEvaluationParam));
    }

    @ApiOperation(value = "客户的代理用户效果评估分页查询", notes = "客户的代理用户效果评估分页查询", produces = "application/json")
    @PostMapping("/pageProxy")
    public ResponseData pageProxy(@RequestBody CustEvaluationParam custEvaluationParam) {
        return ResponseData.success(custEvaluationService.pageProxy(custEvaluationParam));
    }



}

