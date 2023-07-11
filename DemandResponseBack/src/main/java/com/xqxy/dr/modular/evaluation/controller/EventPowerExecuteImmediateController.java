package com.xqxy.dr.modular.evaluation.controller;


import com.xqxy.core.annotion.BusinessLog;
import com.xqxy.core.enums.LogAnnotionOpTypeEnum;
import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.evaluation.entity.EventPowerExecuteImmediate;
import com.xqxy.dr.modular.evaluation.param.EventPowerExecuteImmediateParam;
import com.xqxy.dr.modular.evaluation.service.EventPowerExecuteImmediateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 用户执行曲线-实时 前端控制器
 * </p>
 *
 * @author Peng
 * @since 2021-11-01
 */
@Api(tags = "用户执行曲线-实时API接口")
@RestController
@RequestMapping("/evaluation/event-power-execute-immediate")
public class EventPowerExecuteImmediateController {

    @Resource
    private EventPowerExecuteImmediateService eventPowerExecuteImmediateService;

    /**
     * @description: 获取当日效果评估的负荷曲线
     * @param:
     * @return:
     * @author: PengChuqing
     * @date: 2021/7/4 17:01
     */
    @ApiOperation(value = "获取效果评估的历史负荷曲线", notes = "获取效果评估的历史负荷曲线", produces = "application/json")
    @PostMapping("/detail")
    public ResponseData detail(@RequestBody EventPowerExecuteImmediateParam executeParam) {
        EventPowerExecuteImmediate detail = eventPowerExecuteImmediateService.detail(executeParam);
        return ResponseData.success(detail);
    }

    @BusinessLog(title = "当日效果评估曲线导出", opType = LogAnnotionOpTypeEnum.EXPORT)
    @ApiOperation(value = "当日效果评估曲线导出", notes = "当日效果评估曲线导出", produces = "application/json")
    @PostMapping("/export")
    public void export(@RequestBody EventPowerExecuteImmediateParam executeImmediateParam) {
        eventPowerExecuteImmediateService.export(executeImmediateParam);
    }

}

