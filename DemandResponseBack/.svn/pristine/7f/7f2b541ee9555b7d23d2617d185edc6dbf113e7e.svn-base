package com.xqxy.dr.modular.event.controller;


import com.xqxy.core.annotion.BusinessLog;
import com.xqxy.core.enums.LogAnnotionOpTypeEnum;
import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.event.param.ChangePowerBaseLineParam;
import com.xqxy.dr.modular.event.param.EventParam;
import com.xqxy.dr.modular.event.service.EventPowerBaseService;
import com.xqxy.dr.modular.event.service.EventPowerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 事件执行曲线 前端控制器
 * </p>
 *
 * @author Shen
 * @since 2021-10-26
 */
@Api(tags = "事件曲线API接口")
@RestController
@RequestMapping("/event/event-power")
public class EventPowerController {

    @Resource
    private EventPowerService eventPowerService;
    @Resource
    private EventPowerBaseService eventPowerBaseService;

    /**
     * @description: 执行监测主页区县
     * @param:
     * @return:
     * @author: PengChuqing
     * @date: 2021/10/26 20:08
     */
   //  @BusinessLog(title = "获取事件执行曲线", opType = LogAnnotionOpTypeEnum.QUERY)
    @ApiOperation(value = "获取事件执行曲线", notes = "获取事件执行曲线", produces = "application/json")
    @PostMapping("/getEventPower")
    public ResponseData getEventPower(@RequestBody EventParam eventParam) {
        return ResponseData.success(eventPowerService.getEventPower(eventParam));
    }

    @ApiOperation(value = "修改基线")
    @PostMapping("/changePowerBaseLine")
    public ResponseData changePowerBaseLine(@RequestBody ChangePowerBaseLineParam changePowerBaseLineParam){
        eventPowerBaseService.changePowerBaseLine(changePowerBaseLineParam);
        return ResponseData.success();
    }

}

