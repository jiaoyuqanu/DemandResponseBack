package com.xqxy.dr.modular.subsidy.controller;


import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.subsidy.param.ConsSubsidyPayParam;
import com.xqxy.dr.modular.subsidy.service.ConsSubsidyPayService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 用户激励费用发放 前端控制器
 * </p>
 *
 * @author hu xingxing
 * @since 2021-10-20
 */
@RestController
@RequestMapping("/subsidy/cons-subsidy-pay")
public class ConsSubsidyPayController {

    @Resource
    private ConsSubsidyPayService consSubsidyPayService;

    @ApiOperation(value = "用户发放分页查询", notes = "用户发放分页查询", produces = "application/json")
    @PostMapping("/consSubsidyPayPage")
    public ResponseData consSubsidyPayPage(@RequestBody ConsSubsidyPayParam consSubsidyPayParam) {

        return ResponseData.success(consSubsidyPayService.consSubsidyPayPage(consSubsidyPayParam));
    }

    @ApiOperation(value = "用户发放详情", notes = "用户发放详情", produces = "application/json")
    @PostMapping("/consSubsidyPayDetails")
    public ResponseData consSubsidyPayDetails(@RequestBody ConsSubsidyPayParam consSubsidyPayParam) {

        return ResponseData.success(consSubsidyPayService.consSubsidyPayDetails(consSubsidyPayParam));
    }
}

