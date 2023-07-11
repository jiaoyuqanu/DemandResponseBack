package com.xqxy.dr.modular.subsidy.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.core.annotion.BusinessLog;
import com.xqxy.core.enums.LogAnnotionOpTypeEnum;
import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.subsidy.entity.ConsSubsidyDaily;
import com.xqxy.dr.modular.subsidy.param.ConsSubsidyDailyParam;
import com.xqxy.dr.modular.subsidy.result.MySubsidyInfo;
import com.xqxy.dr.modular.subsidy.service.ConsSubsidyDailyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 日补贴 前端控制器
 * </p>
 *
 * @author Shen
 * @since 2021-10-21
 */
@Api(tags = "用户日补贴API接口")
@RestController
@RequestMapping("/subsidy/cons-subsidy-daily")
public class ConsSubsidyDailyController {

    @Resource
    ConsSubsidyDailyService consSubsidyDailyService;

    @ApiOperation(value = "用户日补贴分页查询", notes = "用户日补贴分页查询", produces = "application/json")
    @PostMapping("/dailySubsidyPage")
    public ResponseData dailySubsidyPage(@RequestBody ConsSubsidyDailyParam consSubsidyDailyParam) {
        return ResponseData.success(consSubsidyDailyService.dailySubsidyPage(consSubsidyDailyParam));
    }

    @ApiOperation(value = "用户日补贴导出", notes = "用户日补贴导出", produces = "application/json")
    @PostMapping("/exportDailySubsidy")
    public void exportDailySubsidy(@RequestBody ConsSubsidyDailyParam consSubsidyDailyParam) {
        consSubsidyDailyService.exportDailySubsidy(consSubsidyDailyParam);
    }

    @ApiOperation(value = "我的补贴--新版", notes = "我的补贴--新版", produces = "application/json")
    @PostMapping("/mySubsidy")
    public ResponseData mySubsidy(@RequestBody ConsSubsidyDailyParam consSubsidyDailyParam) {
        Page<MySubsidyInfo> mySubsidyInfoPage = consSubsidyDailyService.mySubsidy(consSubsidyDailyParam);
        return ResponseData.success(mySubsidyInfoPage);
    }

}

