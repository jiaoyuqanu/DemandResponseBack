package com.xqxy.dr.modular.event.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xqxy.core.annotion.BusinessLog;
import com.xqxy.core.enums.LogAnnotionOpTypeEnum;
import com.xqxy.core.pojo.response.ResponseData;
//import com.xqxy.core.pojo.response.SuccessResponseData;
import com.xqxy.dr.modular.event.entity.*;
import com.xqxy.dr.modular.event.param.BuinessOperationParam;
import com.xqxy.dr.modular.event.param.EventUserParam;
import com.xqxy.dr.modular.event.service.DrEventInvitationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 用户邀约信息(DrEventInvitation)表控制层
 *
 * @author makejava
 * @since 2021-07-14 21:37:53
 */
@Api(tags = "用户邀约信息API接口")
@RestController
@RequestMapping("/event/drEventInvitation")
public class DrEventInvitationController {
    /**
     * 服务对象
     */
    @Resource
    private DrEventInvitationService drEventInvitationService;


 /*   @BusinessLog(title = "效果评估统计", opType = LogAnnotionOpTypeEnum.QUERY)
    @ApiOperation(value = "效果评估统计查询", notes = "效果评估统计查询", produces = "application/json")
    @PostMapping("/costData")
    public ResponseData costData(@RequestParam(name = "eventId", required = true) Long eventId,
                                 @RequestParam(name = "cityCode", required = false) Long cityCode) {
        List<DrConsCountEntity> drConsCountEntities = this.drEventInvitationService.costData(eventId, cityCode);
        return ResponseData.success(drConsCountEntities);
//        return new SuccessResponseData(drConsCountEntities);
    }*/

    @BusinessLog(title = "效果评估统计明细", opType = LogAnnotionOpTypeEnum.QUERY)
    @ApiOperation(value = "效果评估统计明细查询", notes = "效果评估统计明细查询", produces = "application/json")
    @PostMapping("/getDrEventInvitationDetailed")
    public ResponseData getDrEventInvitationDetailed(@RequestParam(name = "current", required = true) long current,
                                                     @RequestParam(name = "size", required = true) long size,
                                                     @RequestParam(name = "eventId", required = true) Long eventId,
                                                     @RequestParam(name = "cityCode", required = true) Long cityCode,
                                                     @RequestParam(name = "consType", required = false) String consType) {
        IPage<DrEventInvitationEntity> iPage = this.drEventInvitationService.getDrEventInvitationEntity(current, size, cityCode, eventId, consType);
        return ResponseData.success(iPage);
//        return new SuccessResponseData(iPage);
    }


    @BusinessLog(title = "负荷集成商效果评估统计", opType = LogAnnotionOpTypeEnum.QUERY)
    @ApiOperation(value = "负荷集成商效果评估统计查询", notes = "负荷集成商效果评估统计查询", produces = "application/json")
    @PostMapping("/consNoCostData")
    public ResponseData consNoCostData(@RequestParam(name = "current", required = true) long current,
                                       @RequestParam(name = "size", required = true) long size,
                                       @RequestParam(name = "eventId", required = true) Long eventId) {
        IPage<Map<String, Object>> mapIPage = this.drEventInvitationService.fhjcCostData(current, size, eventId);
        return ResponseData.success(mapIPage);
//        return new SuccessResponseData(mapIPage);
    }


    @BusinessLog(title = "负荷集成商效果评估明细", opType = LogAnnotionOpTypeEnum.QUERY)
    @ApiOperation(value = "负荷集成商效果评估明细查询", notes = "负荷集成商效果评估明细查询", produces = "application/json")
    @PostMapping("/getDrEventInvDetailed")
    public ResponseData getDrEventInvDetailed(@RequestParam(name = "current", required = true) long current,
                                              @RequestParam(name = "size", required = true) long size,
                                              @RequestParam(name = "eventId", required = true) Long eventId,
                                              @RequestParam(name = "consId", required = true) String consId) {
        IPage<DrEventInvEntity> drEventInvEntity = this.drEventInvitationService.getDrEventInvEntity(current, size, eventId, consId);
        return ResponseData.success(drEventInvEntity);
//        return new SuccessResponseData(drEventInvEntity);
    }

    @BusinessLog(title = "负荷集成商效果评估导出", opType = LogAnnotionOpTypeEnum.EXPORT)
    @ApiOperation(value = "负荷集成商效果评估明细导出", notes = "负荷集成商效果评估明细查询", produces = "application/json")
    @PostMapping("/exprotDrEventInvDetailed")
    public void exprotDrEventInvDetailed(@RequestParam(name = "eventId", required = true) Long eventId,
                                         @RequestParam(name = "excelName", required = true) String excelName,
                                         @RequestParam(name = "consId", required = true) String consId) {
        this.drEventInvitationService.exportDrEventInvEntity(eventId, consId, excelName);

    }


/*    @BusinessLog(title = "当日效果评估统计", opType = LogAnnotionOpTypeEnum.QUERY)
    @ApiOperation(value = "当日效果评估统计查询", notes = "当日效果评估统计查询", produces = "application/json")
    @PostMapping("/costDataImmediate")
    public ResponseData costDataInvitation(@RequestParam(name = "eventId", required = true) Long eventId,
                                           @RequestParam(name = "cityCode", required = false) Long cityCode) {
        List<DrConsCountEntity> drConsCountEntities = this.drEventInvitationService.costDataImmediate(eventId, cityCode);
        return ResponseData.success(drConsCountEntities);
        //        return new SuccessResponseData(drConsCountEntities);
    }*/

    @BusinessLog(title = "当日效果评估统计明细", opType = LogAnnotionOpTypeEnum.QUERY)
    @ApiOperation(value = "当日效果评估统计明细查询", notes = "当日效果评估统计明细查询", produces = "application/json")
    @PostMapping("/getDrEventInvitationImmediateDetailed")
    public ResponseData getDrEventInvitationImmediateDetailed(@RequestParam(name = "current", required = true) long current,
                                                              @RequestParam(name = "size", required = true) long size,
                                                              @RequestParam(name = "eventId", required = true) Long eventId,
                                                              @RequestParam(name = "cityCode", required = true) Long cityCode,
                                                              @RequestParam(name = "consType", required = false) String consType) {
        IPage<DrEventInvitationEntity> iPage = this.drEventInvitationService.getDrEventInvitationImmediateEntity(current, size, cityCode, eventId, consType);
        return ResponseData.success(iPage);
//        return new SuccessResponseData(iPage);
    }

/*    @BusinessLog(title = "效果评估统计首页导出", opType = LogAnnotionOpTypeEnum.EXPORT)
    @ApiOperation(value = "效果评估统计首页导出", notes = "效果评估统计首页导出", produces = "application/json")
    @PostMapping("/exprotDrEventInvitation")
    public void exprotDrEventInvitation(@RequestParam(name = "excelName", required = true) String excelName,
                                        @RequestParam(name = "eventId", required = true) Long eventId,
                                        @RequestParam(name = "cityCode", required = true) Long cityCode) {
        this.drEventInvitationService.exprotDrEventInvitationEntity(cityCode, eventId, excelName);
    }*/

    @BusinessLog(title = "效果评估统计明细导出", opType = LogAnnotionOpTypeEnum.EXPORT)
    @ApiOperation(value = "效果评估统计明细导出", notes = "效果评估统计明细导出", produces = "application/json")
    @PostMapping("/exportEventInvitationDetailed")
    public void exportEventInvitationDetailed(@RequestParam(name = "eventId", required = true) Long eventId,
                                              @RequestParam(name = "cityCode", required = true) Long cityCode,
                                              @RequestParam(name = "excelName", required = true) String excelName,
                                              @RequestParam(name = "consType", required = false) String consType) {

        drEventInvitationService.exportDrEventInvitationEntity(cityCode, eventId, consType, excelName);

    }


    @BusinessLog(title = "负荷集成商效果评估统计首页导出", opType = LogAnnotionOpTypeEnum.EXPORT)
    @ApiOperation(value = "负荷集成商效果评估统计首页导出", notes = "负荷集成商效果评估统计首页导出", produces = "application/json")
    @PostMapping("/exprotConsNoCostData")
    public void exprotConsNoCostData(@RequestParam(name = "excelName", required = true) String excelName,
                                     @RequestParam(name = "eventId", required = true) Long eventId) {
        this.drEventInvitationService.exportfhjcCostData(eventId, excelName);

    }


/*    @BusinessLog(title = "当日效果评估统计首页导出", opType = LogAnnotionOpTypeEnum.EXPORT)
    @ApiOperation(value = "当日效果评估统计首页导出", notes = "当日效果评估统计首页导出", produces = "application/json")
    @PostMapping("/exprotCostDataInvitation")
    public void exprotCostDataInvitation(@RequestParam(name = "excelName", required = true) String excelName,
                                         @RequestParam(name = "eventId", required = true) Long eventId,
                                         @RequestParam(name = "cityCode", required = false) Long cityCode) {
        this.drEventInvitationService.exportCostDataImmediate(excelName, eventId, cityCode);
    }*/


    @BusinessLog(title = "当日效果评估统计明细导出", opType = LogAnnotionOpTypeEnum.EXPORT)
    @ApiOperation(value = "当日效果评估统计明细导出", notes = "效果评估统计明细导出", produces = "application/json")
    @PostMapping("/exportCostEventImmediateDetailed")
    public void exportCostEventImmediateDetailed(@RequestParam(name = "eventId", required = true) Long eventId,
                                                 @RequestParam(name = "cityCode", required = true) Long cityCode,
                                                 @RequestParam(name = "excelName", required = true) String excelName,
                                                 @RequestParam(name = "consType", required = false) String consType) {

        drEventInvitationService.exportDrEventImmediateEntity(cityCode, eventId, consType, excelName);

    }

    @BusinessLog(title = "当日负荷集成商效果评估统计", opType = LogAnnotionOpTypeEnum.QUERY)
    @ApiOperation(value = "当日负荷集成商效果评估统计查询", notes = "当日负荷集成商效果评估统计查询", produces = "application/json")
    @PostMapping("/consNoCostImmediateData")
    public ResponseData consNoCostImmediateData(@RequestParam(name = "current", required = true) long current,
                                                @RequestParam(name = "size", required = true) long size,
                                                @RequestParam(name = "eventId", required = true) Long eventId) {
        IPage<Map<String, Object>> mapIPage = this.drEventInvitationService.fhjcCostImmediateData(current, size, eventId);
        return ResponseData.success(mapIPage);
//        return new SuccessResponseData(mapIPage);
    }


    @BusinessLog(title = "当日负荷集成商效果评估明细", opType = LogAnnotionOpTypeEnum.QUERY)
    @ApiOperation(value = "当日负荷集成商效果评估明细查询", notes = "当日负荷集成商效果评估明细查询", produces = "application/json")
    @PostMapping("/getDrEventInvImmediateDetailed")
    public ResponseData getDrEventInvImmediateDetailed(@RequestParam(name = "current", required = true) long current,
                                                       @RequestParam(name = "size", required = true) long size,
                                                       @RequestParam(name = "eventId", required = true) Long eventId,
                                                       @RequestParam(name = "consId", required = true) String consId) {
        IPage<DrEventInvEntity> drEventInvEntity = this.drEventInvitationService.getDrEventInvEntityImmediate(current, size, eventId, consId);
        return ResponseData.success(drEventInvEntity);
//        return new SuccessResponseData(drEventInvEntity);
    }

    @BusinessLog(title = "当日负荷集成商效果评估统计首页导出", opType = LogAnnotionOpTypeEnum.EXPORT)
    @ApiOperation(value = "当日负荷集成商效果评估统计首页导出", notes = "当日负荷集成商效果评估统计首页导出", produces = "application/json")
    @PostMapping("/exprotConsNoCostImmediateData")
    public void exprotConsNoCostImmediateData(@RequestParam(name = "excelName", required = true) String excelName,
                                              @RequestParam(name = "eventId", required = true) Long eventId) {
        this.drEventInvitationService.exportfhjcCostImmediateData(eventId, excelName);

    }

    @BusinessLog(title = "当日负荷集成商效果评估导出", opType = LogAnnotionOpTypeEnum.EXPORT)
    @ApiOperation(value = "当日负荷集成商效果评估明细导出", notes = "当日负荷集成商效果评估明细查询", produces = "application/json")
    @PostMapping("/exprotDrEventInvImmediateDetailed")
    public void exprotDrEventInvImmediateDetailed(@RequestParam(name = "eventId", required = true) Long eventId,
                                                  @RequestParam(name = "excelName", required = true) String excelName,
                                                  @RequestParam(name = "consId", required = true) String consId) {
        this.drEventInvitationService.exportDrEventInvImmediateEntity(eventId, consId, excelName);

    }


    @BusinessLog(title = "领导工作台日前邀约事件名称", opType = LogAnnotionOpTypeEnum.QUERY)
    @ApiOperation(value = "领导工作台日前邀约事件名称", notes = "领导工作台日前邀约事件名称", produces = "application/json")
    @PostMapping("/workBenchOverviewEventName")
    public ResponseData workBenchOverviewEventName(@RequestBody EventUserParam eventUserParam) {
        String year = null;
        if(null!=eventUserParam) {
            year = eventUserParam.getYear();
        }
        return ResponseData.success(drEventInvitationService.workBencworkBenchOverviewEventNamehOverview(year));
//        return new SuccessResponseData(eventInvitationService.workBencworkBenchOverviewEventNamehOverview(year));
    }

    @BusinessLog(title = "用户参与情况统计", opType = LogAnnotionOpTypeEnum.QUERY)
    @ApiOperation(value = "用户参与情况统计", notes = "用户参与情况统计", produces = "application/json")
    @PostMapping("/getEventUser")
    public ResponseData getEventUser(@RequestBody @Validated(EventUserParam.page.class) EventUserParam eventUserParam) {
        IPage<DrEventInvitationUser> drEventUser = this.drEventInvitationService.getEventUser(eventUserParam.getCurrent(), eventUserParam.getSize(),
                eventUserParam.getStartDate(), eventUserParam.getEndDate(), eventUserParam.getEventId());
        return ResponseData.success(drEventUser);
    }

    @BusinessLog(title = "用户参与情况统计导出", opType = LogAnnotionOpTypeEnum.EXPORT)
    @ApiOperation(value = "用户参与情况统计导出", notes = "用户参与情况统计导出", produces = "application/json")
    @PostMapping("/exprotEventUser")
    public void exprotEventUser(@RequestBody @Validated(EventUserParam.page.class) EventUserParam eventUserParam) {
        this.drEventInvitationService.exprotEventUser(eventUserParam.getStartDate(), eventUserParam.getEndDate(), eventUserParam.getEventId());

    }

    @BusinessLog(title = "业务运行统计", opType = LogAnnotionOpTypeEnum.QUERY)
    @ApiOperation(value = "业务运行统计", notes = "业务运行统计", produces = "application/json")
    @PostMapping("/getBusinessOperation")
    public ResponseData getBusinessOperation(@RequestBody @Validated(BuinessOperationParam.page.class) BuinessOperationParam buinessOperationParam) {
        IPage<DrEventInvitationBusiness> drEventBusiness = this.drEventInvitationService.getEventBusiness(buinessOperationParam.getCurrent(),
                buinessOperationParam.getSize(), buinessOperationParam.getStartDate(),
                buinessOperationParam.getEndDate(), buinessOperationParam.getEventType(),buinessOperationParam.getProvinceCode(),buinessOperationParam.getCityCode());
        return ResponseData.success(drEventBusiness);
    }

    @BusinessLog(title = "业务运行统计导出", opType = LogAnnotionOpTypeEnum.EXPORT)
    @ApiOperation(value = "业务运行统计导出", notes = "业务运行统计导出", produces = "application/json")
    @PostMapping("/exprotBusinessOperation")
    public void exprotBusinessOperation(@RequestBody @Validated(BuinessOperationParam.page.class) BuinessOperationParam buinessOperationParam) {
        this.drEventInvitationService.exprotBusinessOperation(buinessOperationParam.getStartDate(), buinessOperationParam.getEndDate(),
                buinessOperationParam.getEventType(), buinessOperationParam.getProvinceCode(), buinessOperationParam.getCityCode());

    }

    @BusinessLog(title = "事件效果统计", opType = LogAnnotionOpTypeEnum.QUERY)
    @ApiOperation(value = "事件效果统计", notes = "事件效果统计", produces = "application/json")
    @PostMapping("/getEffectEval")
    public ResponseData getEffectEval(@RequestBody BuinessOperationParam buinessOperationParam) {
        IPage<DrEventInvitationEffectEval> drEventBusiness = this.drEventInvitationService.getEffectEval(buinessOperationParam.getCurrent(), buinessOperationParam.getSize(),
                buinessOperationParam.getStartDate(), buinessOperationParam.getEndDate(), buinessOperationParam.getEventId(),
                buinessOperationParam.getProvinceCode(),buinessOperationParam.getCityCode());
        return ResponseData.success(drEventBusiness);
    }

    @BusinessLog(title = "事件效果统计导出", opType = LogAnnotionOpTypeEnum.EXPORT)
    @ApiOperation(value = "事件效果统计导出", notes = "事件效果统计导出", produces = "application/json")
    @PostMapping("/exprotEffectEval")
    public void exprotEffectEval(@RequestBody BuinessOperationParam buinessOperationParam) {
        if(null!=buinessOperationParam) {
            this.drEventInvitationService.exprotEffectEval(buinessOperationParam.getStartDate(), buinessOperationParam.getEndDate(),
                    buinessOperationParam.getEventId(), buinessOperationParam.getProvinceCode(), buinessOperationParam.getCityCode());
        } else {
            this.drEventInvitationService.exprotEffectEval(null, null,
                    null, null, null);
        }

    }

    @BusinessLog(title = "事件效果统计导出-当日", opType = LogAnnotionOpTypeEnum.EXPORT)
    @ApiOperation(value = "事件效果统计导出-当日", notes = "事件效果统计导出-当日", produces = "application/json")
    @PostMapping("/exprotEffectEvalImmediate")
    public void exprotEffectEvalImmediate(@RequestBody BuinessOperationParam buinessOperationParam) {
        if(null!=buinessOperationParam) {
            this.drEventInvitationService.exprotEffectEvalImmediate(buinessOperationParam.getStartDate(), buinessOperationParam.getEndDate(),
                    buinessOperationParam.getEventId(), buinessOperationParam.getProvinceCode(), buinessOperationParam.getCityCode());
        }else {
            this.drEventInvitationService.exprotEffectEvalImmediate(null, null,
                    null, null, null);
        }

    }

    @BusinessLog(title = "事件效果统计当日", opType = LogAnnotionOpTypeEnum.QUERY)
    @ApiOperation(value = "事件效果统计当日", notes = "事件效果统计当日", produces = "application/json")
    @PostMapping("/getEffectEvalImmediate")
    public ResponseData getEffectEvalImmediate(@RequestBody BuinessOperationParam buinessOperationParam) {
        IPage<DrEventInvitationEffectEval> drEventBusiness = this.drEventInvitationService.getEffectEvalImmediate(buinessOperationParam.getCurrent(), buinessOperationParam.getSize(),
                buinessOperationParam.getStartDate(), buinessOperationParam.getEndDate(), buinessOperationParam.getEventId(),
                buinessOperationParam.getProvinceCode(),buinessOperationParam.getCityCode());
        return ResponseData.success(drEventBusiness);
    }

    @BusinessLog(title = "事件效果统计次日明细", opType = LogAnnotionOpTypeEnum.QUERY)
    @ApiOperation(value = "事件效果统计次日明细", notes = "事件效果统计次日明细", produces = "application/json")
    @PostMapping("/getEffectUsersDetail")
    public ResponseData getEffectUsersDetail(@RequestBody BuinessOperationParam buinessOperationParam) {
        IPage<DrEventInvitationEffectEvalDetail> drEventBusiness = this.drEventInvitationService.getEffectUsersDetail(buinessOperationParam.getCurrent(),
                buinessOperationParam.getSize(), buinessOperationParam.getEventId());
        return ResponseData.success(drEventBusiness);
    }

    @BusinessLog(title = "事件效果统计当日明细", opType = LogAnnotionOpTypeEnum.QUERY)
    @ApiOperation(value = "事件效果统计当日明细", notes = "事件效果统计当日明细", produces = "application/json")
    @PostMapping("/getEffectUsersDetailImmediate")
    public ResponseData getEffectUsersDetailImmediate(@RequestBody BuinessOperationParam buinessOperationParam) {
        IPage<DrEventInvitationEffectEvalDetail> drEventBusiness = this.drEventInvitationService.getEffectUsersDetailImmediate(buinessOperationParam.getCurrent(),
                buinessOperationParam.getSize(), buinessOperationParam.getEventId());
        return ResponseData.success(drEventBusiness);
    }
}
