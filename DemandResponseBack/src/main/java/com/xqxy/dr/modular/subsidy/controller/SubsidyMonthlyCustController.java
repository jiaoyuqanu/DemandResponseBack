package com.xqxy.dr.modular.subsidy.controller;

import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.subsidy.param.SubsidyMonthlyCustParam;
import com.xqxy.dr.modular.subsidy.service.SubsidyMonthlyCustService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * kehu月补贴 前端控制器
 * </p>
 *
 * @author hu xingxing
 * @since 2021-10-30
 */
@Api(tags = "客户月补贴API接口")
@RestController
@RequestMapping("/subsidy/subsidy-monthly-cust")
public class SubsidyMonthlyCustController {

    @Resource
    private SubsidyMonthlyCustService subsidyMonthlyCustService;

    /**
     * 月补贴分页查询
     * @param subsidyMonthlyCustParam
     * @return
     * @author hu xingxing
     * @date 2021-10-28 16:50
     */
    //@BusinessLog(title = "月补贴分页查询", opType = LogAnnotionOpTypeEnum.QUERY)
    @ApiOperation(value = "月补贴分页查询", notes = "月补贴分页查询", produces = "application/json")
    @PostMapping("/pageSubsidyMonthly")
    public ResponseData pageSubsidyMonthly(@RequestBody SubsidyMonthlyCustParam subsidyMonthlyCustParam) {

        return ResponseData.success(subsidyMonthlyCustService.pageSubsidyMonthly(subsidyMonthlyCustParam));
    }

    /**
     * 月补贴状更新
     * @param subsidyMonthlyCustParam
     * @return
     * @author hu xingxing
     * @date 2021-10-28 16:50
     */
    //@BusinessLog(title = "月补贴状更新", opType = LogAnnotionOpTypeEnum.QUERY)
    @ApiOperation(value = "月补贴状更新", notes = "月补贴状更新", produces = "application/json")
    @PostMapping("/subsidyMonthlyUpd")
    public ResponseData subsidyMonthlyUpd(@RequestBody SubsidyMonthlyCustParam subsidyMonthlyCustParam) {

        subsidyMonthlyCustService.subsidyMonthlyUpd(subsidyMonthlyCustParam);
        return ResponseData.success();
    }

    /**
     * 客户月补贴导出
     * @param subsidyMonthlyCustParam
     * @return
     * @author hu xingxing
     * @date 2021-10-28 16:50
     */
    @ApiOperation(value = "客户月补贴导出", notes = "客户月补贴导出", produces = "application/json")
    @PostMapping("/exportCustMonthlySubsidy")
    public void exportCustMonthlySubsidy(@RequestBody SubsidyMonthlyCustParam subsidyMonthlyCustParam) {

        subsidyMonthlyCustService.exportCustMonthlySubsidy(subsidyMonthlyCustParam);
    }
}

