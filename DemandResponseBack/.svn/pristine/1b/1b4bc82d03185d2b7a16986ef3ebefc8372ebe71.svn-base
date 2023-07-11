package com.xqxy.dr.modular.event.controller;


import com.xqxy.core.annotion.BusinessLog;
import com.xqxy.core.enums.LogAnnotionOpTypeEnum;
import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.event.service.BaselineLibraryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author PengChuqing
 * @since 2021-10-18
 */
@Api(tags = "基线库API接口")
@RestController
@RequestMapping("/event/baseline-library")
public class BaselineLibraryController {

    @Resource
    private BaselineLibraryService baselineLibraryService;


    @BusinessLog(title = "通过基线库id查询基线库详情", opType = LogAnnotionOpTypeEnum.ADD)
    @ApiOperation(value = "通过基线库id查询基线库详情", notes = "通过基线库id查询基线库详情", produces = "application/json")
    @GetMapping("/detail")
    public ResponseData detail(@RequestParam(name = "baselineCapId", required = true) Long baselineCapId) {
        return  ResponseData.success(baselineLibraryService.getById(baselineCapId));
    }

}

