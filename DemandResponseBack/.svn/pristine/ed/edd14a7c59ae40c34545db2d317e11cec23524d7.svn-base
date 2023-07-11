package com.xqxy.dr.modular.event.controller;


import com.xqxy.core.annotion.BusinessLog;
import com.xqxy.core.enums.LogAnnotionOpTypeEnum;
import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.event.param.EventExecuteParam;
import com.xqxy.dr.modular.event.param.PlanParam;
import com.xqxy.dr.modular.event.service.EventExecuteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 电力用户执行信息 前端控制器
 * </p>
 *
 * @author Shen
 * @since 2021-10-27
 */
@Api(tags = "用户执行率API接口")
@RestController
@RequestMapping("/event/event-execute")
public class EventExecuteController {

    @Resource
    private EventExecuteService eventExecuteService;

    // @BusinessLog(title = "事件实时执行率监测", opType = LogAnnotionOpTypeEnum.QUERY)
    @ApiOperation(value = "事件实时执行率监测", notes = "事件实时执行率监测", produces = "application/json")
    @PostMapping("/eventMonitoring")
    public ResponseData eventMonitoring(@RequestBody @Validated(PlanParam.detail.class) EventExecuteParam eventExecuteParam) {
        return ResponseData.success(eventExecuteService.eventMonitoring(eventExecuteParam));
    }


    /**
     * @description: 用户执行率列表
     * @param:
     * @return:
     * @author: PengChuqing
     * @date: 2021/10/27 16:55
     */
    // @BusinessLog(title = "用户执行率列表", opType = LogAnnotionOpTypeEnum.QUERY)
    @ApiOperation(value = "用户执行率列表", notes = "用户执行率列表", produces = "application/json")
    @PostMapping("/list")
    public ResponseData list(@RequestBody EventExecuteParam eventExecuteParam) {
        return ResponseData.success(eventExecuteService.list(eventExecuteParam));
    }

    /**
     * @description: 下载需求响应执行情况报表
     * @param:
     * @return:
     * @author: PengChuqing
     * @date: 2021/10/27 16:55
     */
    // @BusinessLog(title = "用户执行率列表", opType = LogAnnotionOpTypeEnum.QUERY)
    @ApiOperation(value = "下载需求响应执行情况报表", notes = "下载需求响应执行情况报表", produces = "application/json")
    @PostMapping("/downloadExecteReport")
    public void downloadExecteReport(@RequestBody EventExecuteParam eventExecuteParam, HttpServletResponse response, HttpServletRequest request) {
        eventExecuteService.downloadExecteReport(eventExecuteParam,response,request);
    }

}

