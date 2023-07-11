package com.xqxy.dr.modular.subsidy.controller;


import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.subsidy.param.CustConsSubsidyPayParam;
import com.xqxy.dr.modular.subsidy.param.CustSubsidyPayParam;
import com.xqxy.dr.modular.subsidy.param.EventCustSubsidyPayParam;
import com.xqxy.dr.modular.subsidy.service.CustSubsidyPayService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 客户激励费用发放 前端控制器
 * </p>
 *
 * @author hu xingxing
 * @since 2021-10-20
 */
@RestController
@RequestMapping("/subsidy/cust-subsidy-pay")
public class CustSubsidyPayController {

    @Resource
    private CustSubsidyPayService custSubsidyPayService;

    @ApiOperation(value = "集成商发放分页查询", notes = "集成商发放分页查询", produces = "application/json")
    @PostMapping("/custSubsidyPayPage")
    public ResponseData custSubsidyPayPage(@RequestBody CustSubsidyPayParam custSubsidyPayParam) {

        return ResponseData.success(custSubsidyPayService.custSubsidyPayPage(custSubsidyPayParam));
    }

    @ApiOperation(value = "集成商发放详情", notes = "集成商发放详情", produces = "application/json")
    @PostMapping("/custSubsidyPayDetails")
    public ResponseData custSubsidyPayDetails(@RequestBody CustSubsidyPayParam custSubsidyPayParam) {

        return ResponseData.success(custSubsidyPayService.custSubsidyPayDetails(custSubsidyPayParam));
    }

    @ApiOperation(value = "事件补偿详情", notes = "事件补偿详情", produces = "application/json")
    @PostMapping("/eventCustSubsidyPayPage")
    public ResponseData eventCustSubsidyPayPage(@RequestBody EventCustSubsidyPayParam eventCustSubsidyPayParam) {
        return ResponseData.success(custSubsidyPayService.eventCustSubsidyPayPage(eventCustSubsidyPayParam));
    }

    @ApiOperation(value = "子用户补偿详情", notes = "子用户补偿详情", produces = "application/json")
    @PostMapping("/custConsSubsidyPayDetails")
    public ResponseData custConsSubsidyPayDetails(@RequestBody CustConsSubsidyPayParam custConsSubsidyPayParam) {
        return ResponseData.success(custSubsidyPayService.custConsSubsidyPayDetails(custConsSubsidyPayParam));
    }


}

