package com.xqxy.dr.modular.subsidy.controller;

import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.subsidy.param.SettlementRecParam;
import com.xqxy.dr.modular.subsidy.service.SettlementRecService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 响应补贴结算记录表 前端控制器
 * </p>
 *
 * @author hu xingxing
 * @since 2022-01-07
 */
@RestController
@RequestMapping("/subsidy/settlement-rec")
public class SettlementRecController {

    @Resource
    private SettlementRecService settlementRecService;

    @ApiOperation(value = "补贴结算记录分页查询", notes = "补贴结算记录分页查询", produces = "application/json")
    @PostMapping("/settlementPage")
    public ResponseData settlementPage(@RequestBody SettlementRecParam settlementRecParam) {

        return ResponseData.success(settlementRecService.settlementPage(settlementRecParam));
    }

    @ApiOperation(value = "补贴结算记录生成", notes = "补贴结算记录生成", produces = "application/json")
    @PostMapping("/createSettlementRec")
    public ResponseData createSettlementRec(@RequestBody SettlementRecParam settlementRecParam) {

        settlementRecService.createSettlementRec(settlementRecParam);
        return ResponseData.success();
    }

    @ApiOperation(value = "补贴结算记录发放", notes = "补贴结算记录发放", produces = "application/json")
    @PostMapping("/settlementRecPay")
    public ResponseData settlementRecPay(@RequestBody SettlementRecParam settlementRecParam) {

        settlementRecService.settlementRecPay(settlementRecParam);
        return ResponseData.success();
    }

    @ApiOperation(value = "电力用户补贴结算记录分页查询", notes = "补贴结算记录分页查询", produces = "application/json")
    @PostMapping("/custSettlementPage")
    public ResponseData custSettlementRecPage(@RequestBody SettlementRecParam settlementRecParam) {
        return ResponseData.success(settlementRecService.getCustSettlementRecPage(settlementRecParam));
    }
}

