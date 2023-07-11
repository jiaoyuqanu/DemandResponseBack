package com.xqxy.dr.modular.subsidy.controller;

import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.subsidy.param.SubsidyMonthlyParam;
import com.xqxy.dr.modular.subsidy.service.SubsidyMonthlyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 月补贴 前端控制器
 * </p>
 *
 * @author hu xingxing
 * @since 2021-10-28
 */
@Api(tags = "月补贴API接口")
@RestController
@RequestMapping("/subsidy/subsidy-monthly")
public class SubsidyMonthlyController {

    @Resource
    private SubsidyMonthlyService subsidyMonthlyService;

    /**
     * 月补贴分页查询
     * @param subsidyMonthlyParam
     * @return
     * @author hu xingxing
     * @date 2021-10-28 16:50
     */
    //@BusinessLog(title = "月补贴分页查询", opType = LogAnnotionOpTypeEnum.QUERY)
    @ApiOperation(value = "月补贴分页查询", notes = "月补贴分页查询", produces = "application/json")
    @PostMapping("/pageSubsidyMonthly")
    public ResponseData pageSubsidyMonthly(@RequestBody SubsidyMonthlyParam subsidyMonthlyParam) {

        return ResponseData.success(subsidyMonthlyService.pageSubsidyMonthly(subsidyMonthlyParam));
    }

    /**
     * 月补贴状态更新
     * @param subsidyMonthlyParam
     * @return
     * @author hu xingxing
     * @date 2021-10-28 16:50
     */
    //@BusinessLog(title = "月补贴状更新", opType = LogAnnotionOpTypeEnum.QUERY)
    @ApiOperation(value = "月补贴状态更新", notes = "月补贴状态更新", produces = "application/json")
    @PostMapping("/subsidyMonthlyUpd")
    public ResponseData subsidyMonthlyUpd(@RequestBody SubsidyMonthlyParam subsidyMonthlyParam) {

        subsidyMonthlyService.subsidyMonthlyUpd(subsidyMonthlyParam);
        return ResponseData.success();
    }

    /**
     * 用户月补贴导出
     * @param subsidyMonthlyParam
     * @return
     * @author hu xingxing
     * @date 2021-10-28 16:50
     */
    @ApiOperation(value = "用户月补贴导出", notes = "用户月补贴导出", produces = "application/json")
    @PostMapping("/exportConsMonthlySubsidy")
    public void exportConsMonthlySubsidy(@RequestBody SubsidyMonthlyParam subsidyMonthlyParam) {

        subsidyMonthlyService.exportConsMonthlySubsidy(subsidyMonthlyParam);
    }
}

