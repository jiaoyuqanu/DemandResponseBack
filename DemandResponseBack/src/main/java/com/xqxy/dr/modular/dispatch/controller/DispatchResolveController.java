package com.xqxy.dr.modular.dispatch.controller;


import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.dispatch.param.DispatchAndSoltParam;
import com.xqxy.dr.modular.dispatch.param.DispatchEditorParam;
import com.xqxy.dr.modular.dispatch.param.DispatchParam;
import com.xqxy.dr.modular.dispatch.service.DispatchService;
import com.xqxy.dr.modular.event.param.EventParam;
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
 * 调度需求响应指令 前端控制器
 * </p>
 *
 * @author xiao jun
 * @since 2021-05-14
 */
@Api(tags="调度指令分解接口")
@RestController
@RequestMapping("/dispatchres/dispatchres")
public class DispatchResolveController {

    @Resource
    private DispatchService dispatchService;

    /**
     * @description: 负荷调度事件列表
     * @param:
     * @return:
     * @author: chenzhijun
     * @date: 2021/5/17 21:16
     */
    //@BusinessLog(title = "调度指令分页", opType = LogAnnotionOpTypeEnum.QUERY)
    @ApiOperation(value = "负荷调度事件列表", notes = "负荷调度事件列表", produces = "application/json")
    @PostMapping("/page")
    public ResponseData getDispatchResPageList(@RequestBody(required = false) DispatchParam dispatchParam) {
        return ResponseData.success(dispatchService.getDispatchResPageList(dispatchParam));
    }

    /**
     * @description: 查看调度指令和时段关联的事件
     * @param:
     * @return:
     * @author: chenzhijun
     * @date: 2021/5/18 9:19
     */
    //@BusinessLog(title = "查看调度指令详情", opType = LogAnnotionOpTypeEnum.DETAIL)
    @ApiOperation(value = "查看调度指令详情", notes = "查看调度指令详情", produces = "application/json")
    @PostMapping("/getDispatchById")
    public ResponseData getDispatchById(@RequestBody @Validated(DispatchParam.detail.class) DispatchParam dispatchParam) {
        return ResponseData.success(dispatchService.getDispatchById(dispatchParam));
    }

    /**
     * @description: 发起需求响应
     * @param: 
     * @return: 
     * @author: chenzhijun
     * @date: 2021/10/29 16:39
     */
    //@BusinessLog(title = "发起需求响应", opType = LogAnnotionOpTypeEnum.OTHER)
    @ApiOperation(value = "发起需求响应", notes = "发起需求响应", produces = "application/json")
    @PostMapping("/addDispatchEvent")
    public ResponseData addDispatchEvent(@RequestBody  DispatchParam dispatchParam) {
        return dispatchService.addDispatchEvent(dispatchParam);
    }

    /**
     * @description: 推送基线和户号给负控
     * @param:
     * @return:
     * @author: chenzhijun
     * @date: 2021/10/29 16:39
     */
    //@BusinessLog(title = "推送基线和户号", opType = LogAnnotionOpTypeEnum.OTHER)
    @ApiOperation(value = "推送基线和户号给负控", notes = "推送基线和户号给负控", produces = "application/json")
    @PostMapping("/sendBaseLineAndCons")
    public ResponseData sendBaseLineAndCons(@RequestBody EventParam eventParam) {
        return dispatchService.sendBaseLineAndCons(eventParam);
    }

    /**
     * @description: 推送地市目标
     * @param:
     * @return:
     * @author: chenzhijun
     * @date: 2021/10/29 16:39
     */
    //@BusinessLog(title = "推送基线和户号", opType = LogAnnotionOpTypeEnum.OTHER)
    @ApiOperation(value = "推送地市目标", notes = "推送地市目标", produces = "application/json")
    @PostMapping("/sendCityTarget")
    public ResponseData sendCityTarget(@RequestBody EventParam eventParam) {
        return dispatchService.sendCityTarget(eventParam);
    }

}

