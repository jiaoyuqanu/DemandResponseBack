package com.xqxy.dr.modular.evaluation.controller;


import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.evaluation.param.CustEvaluationImmediateParam;
import com.xqxy.dr.modular.evaluation.param.CustEvaluationParam;
import com.xqxy.dr.modular.evaluation.service.CustEvaluationImmediateService;
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
 * 客户当日执行效果评估 前端控制器
 * </p>
 *
 * @author Peng
 * @since 2021-10-29
 */
@Api(tags = "客户当日效果评估API接口")
@RestController
@RequestMapping("/evaluation/cust-evaluation-immediate")
public class CustEvaluationImmediateController {

    @Resource
    private CustEvaluationImmediateService custEvaluationImmediateService;

    /**
     * @description: 客户当日效果评估分页查询
     * @param: 
     * @return: 
     * @author: PengChuqing
     * @date: 2021/10/29 10:15
     */
    @ApiOperation(value = "客户当日效果评估分页查询", notes = "客户当日效果评估分页查询", produces = "application/json")
    @PostMapping("/page")
    public ResponseData page(@RequestBody CustEvaluationImmediateParam custEvaluationImmeParam) {
        return ResponseData.success(custEvaluationImmediateService.page(custEvaluationImmeParam));
    }

    /**
     * @description: 客户的代理用户当日效果评估分页查询
     * @param: 
     * @return: 
     * @author: PengChuqing
     * @date: 2021/10/29 10:30
     */
    @ApiOperation(value = "客户的代理用户当日效果评估分页查询", notes = "客户的代理用户当日效果评估分页查询", produces = "application/json")
    @PostMapping("/pageProxy")
    public ResponseData pageProxy(@RequestBody  CustEvaluationImmediateParam custEvaluationImmeParam) {
        return ResponseData.success(custEvaluationImmediateService.pageProxy(custEvaluationImmeParam));
    }


}

