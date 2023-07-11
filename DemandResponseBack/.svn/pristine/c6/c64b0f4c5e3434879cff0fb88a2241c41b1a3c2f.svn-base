package com.xqxy.dr.modular.event.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.core.annotion.BusinessLog;
import com.xqxy.core.enums.LogAnnotionOpTypeEnum;
import com.xqxy.core.pojo.base.param.BaseParam;
import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.baseline.param.BaseLineParam;
import com.xqxy.dr.modular.event.enums.PlanExecuteEnum;
import com.xqxy.dr.modular.event.param.EventParam;
import com.xqxy.dr.modular.event.param.ExecutePlanConsParam;
import com.xqxy.dr.modular.event.param.PlanConsParam;
import com.xqxy.dr.modular.event.result.EventConsOrCustResult;
import com.xqxy.dr.modular.event.service.EventService;
import com.xqxy.dr.modular.event.service.PlanConsService;
import com.xqxy.dr.modular.strategy.CalculateStrategy;
import com.xqxy.dr.modular.strategy.CalculateStrategyContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * <p>
 * 需求响应事件 前端控制器
 * </p>
 *
 * @author PengChuqing
 * @since 2021-10-08
 */
@Api(tags = "需求响应事件API接口")
@RestController
@RequestMapping("/event/event")
public class EventController {

    @Resource
    private EventService eventService;


    @Value("${calculateStrategy}")
    private String calculateStrategyValue;

    @Resource
    private CalculateStrategyContext calculateStrategyContext;

    @Resource
    private PlanConsService planConsService;


    /**
     * @description: 需求响应事件分页查询
     * @param:
     * @return:
     * @author: PengChuqing
     * @date: 2021/10/8 14:42
     */
//    @BusinessLog(title = "需求响应事件分页查询", opType = LogAnnotionOpTypeEnum.QUERY)
    @ApiOperation(value = "需求响应事件分页查询", notes = "需求响应事件分页查询", produces = "application/json")
    @PostMapping("/page")
    public ResponseData page(@RequestBody EventParam eventParam) {
        return ResponseData.success(eventService.page(eventParam));
    }

    /**
     * @description: 需求响应事件列表
     * @param:
     * @return:
     * @author: PengChuqing
     * @date: 2021/10/8 14:42
     */
//    @BusinessLog(title = "需求响应事件列表", opType = LogAnnotionOpTypeEnum.QUERY)
    @ApiOperation(value = "需求响应事件列表", notes = "需求响应事件列表", produces = "application/json")
    @PostMapping("/list")
    public ResponseData list(@RequestBody EventParam EventParam) {
        return ResponseData.success(eventService.list(EventParam));
    }

    /**
     * @description: 需求响应事件删除
     * @param:
     * @return:
     * @author: PengChuqing
     * @date: 2021/10/8 14:42
     */
//    @BusinessLog(title = "需求响应事件删除", opType = LogAnnotionOpTypeEnum.DELETE)
    @ApiOperation(value = "需求响应事件删除", notes = "需求响应事件删除", produces = "application/json")
    @PostMapping("/delete")
    public ResponseData delete(@RequestBody @Validated(EventParam.delete.class) EventParam EventParam) {
        eventService.delete(EventParam);
        return ResponseData.success();
    }

    /**
     * @description: 需求响应事件添加
     * @param:
     * @return:
     * @author: PengChuqing
     * @date: 2021/10/8 14:43
     */
//    @BusinessLog(title = "需求响应事件添加", opType = LogAnnotionOpTypeEnum.ADD)
    @ApiOperation(value = "需求响应事件添加", notes = "需求响应事件添加", produces = "application/json")
    @PostMapping("/add")
    public ResponseData add(@RequestBody @Validated(EventParam.add.class) EventParam EventParam) {
        eventService.add(EventParam);
        return ResponseData.success();
    }

    /**
     * 需求响应事件修改
     *
     * @param EventParam
     * @return
     */
    @ApiOperation(value = "需求响应事件修改", notes = "需求响应事件修改", produces = "application/json")
    @PostMapping("/update")
    public ResponseData update(@RequestBody @Validated(EventParam.updateInfo.class) EventParam EventParam) {
        eventService.update(EventParam);
        return ResponseData.success();
    }

    /**
     * 需求响应事件提前结束
     *
     * @param eventParam
     * @return
     */
    @BusinessLog(title = "需求响应事件提前结束", opType = LogAnnotionOpTypeEnum.UPDATE)
    @ApiOperation(value = "需求响应事件提前结束", notes = "需求响应事件提前结束", produces = "application/json")
    @PostMapping("/updateEventAdvance")
    public ResponseData updateEventAdvance(@RequestBody @Validated(EventParam.updateInfo.class) EventParam eventParam) {
        if ((null == eventParam.getEndTime() || "".equals(eventParam.getEndTime())) && (null == eventParam.getStartTime() || "".equals(eventParam.getStartTime()))) {
            return ResponseData.fail("-1", "未输入开始结束时间!", "未输入开始结束时间!");
        }
        ResponseData responseData = eventService.updateEventAdvance(eventParam);
        return responseData;
    }

    //    @BusinessLog(title = "事件发布", opType = LogAnnotionOpTypeEnum.ADD)
    @ApiOperation(value = "事件发布", notes = "事件发布", produces = "application/json")
    @PostMapping("/release")
    public ResponseData release(@RequestParam(name = "eventId", required = true) Long eventId, @RequestParam(name = "deadlineTime", required = false) String deadlineTime,
                                @RequestParam(name = "replySource", required = true) String replySource, @RequestParam(name = "regulateMultiple", required = true) Integer regulateMultiple,
                                @RequestParam(name = "endCondition", required = false) Integer endCondition
    ) {
        eventService.release(eventId, deadlineTime, replySource, regulateMultiple, endCondition);
        return ResponseData.success();
    }


    @ApiOperation(value = "二次邀约", notes = "二次邀约", produces = "application/json")
    @PostMapping("/secondInvitation")
    public ResponseData secondInvitation(@RequestParam(name = "eventId", required = true) Long eventId, @RequestParam(name = "deadlineTime", required = true) String deadlineTime) {
        eventService.secondInvitation(eventId, deadlineTime);
        return ResponseData.success();
    }

    /**
     * @description: 需求响应事件添加
     * @param:
     * @return:
     * @author: shi
     * @date: 2021/10/26 14:43
     */
    @BusinessLog(title = "需求响应事件详情", opType = LogAnnotionOpTypeEnum.ADD)
    @ApiOperation(value = "需求响应事件详情", notes = "需求响应事件详情", produces = "application/json")
    @GetMapping("/detail")
    public ResponseData detail(@RequestParam(name = "eventId", required = true) Long eventId, @RequestParam(name = "consId", required = true) String consId) {
        return ResponseData.success(eventService.detailBy(eventId, consId));
    }

    @BusinessLog(title = "需求响应事件查看", opType = LogAnnotionOpTypeEnum.DETAIL)
    @ApiOperation(value = "需求响应事件查看", notes = "需求响应事件查看", produces = "application/json")
    @PostMapping("/monitorDetail")
    public ResponseData monitorDetail(@RequestBody @Validated(EventParam.detail.class) EventParam eventParam) {
        return ResponseData.success(eventService.detail(eventParam));
    }


    /**
     * @description: 基线时段范围大于事件时间范围的分页
     * @param:
     * @return:
     * @author: PengChuqing
     * @date: 2021/11/18 15:01
     */
    @BusinessLog(title = "需求响应事件可选择基线分页", opType = LogAnnotionOpTypeEnum.DETAIL)
    @ApiOperation(value = "需求响应事件可选择基线分页", notes = "需求响应事件可选择基线分页", produces = "application/json")
    @PostMapping("/pageEventBaseline")
    public ResponseData pageEventBaseline(@RequestBody EventParam eventParam) {
        return ResponseData.success(eventService.pageEventBaseline(eventParam));
    }


   /* @PostMapping("/testSynch")
    public ResponseData testSynch() {
        CalculateStrategy calculateStrategy = calculateStrategyContext.strategySelect(calculateStrategyValue);
        calculateStrategy.synchroInvitationToPlan("");
        return ResponseData.success();
    }*/

    @BusinessLog(title = "执行方案编制", opType = LogAnnotionOpTypeEnum.UPDATE)
    @ApiOperation(value = "执行方案编制", notes = "执行方案编制", produces = "application/json")
    @PostMapping("/executePlan")
    public ResponseData executePlan(@RequestBody @Validated(BaseParam.edit.class) ExecutePlanConsParam planConsParam) {
        String msg = planConsService.executePlan(planConsParam);
        if ("2".equals(msg)) {
            return ResponseData.success(msg);
        } else {
            if ("1".equals(msg)) {
                return ResponseData.fail("-1", PlanExecuteEnum.NOCONS.getMessage(), PlanExecuteEnum.NOCONS.getMessage());
            } else if ("3".equals(msg)) {
                return ResponseData.fail("-1", PlanExecuteEnum.NOCAP.getMessage(), PlanExecuteEnum.NOCAP.getMessage());
            } else if ("4".equals(msg)) {
                return ResponseData.fail("-1", PlanExecuteEnum.OVERTIME.getMessage(), PlanExecuteEnum.OVERTIME.getMessage());
            } else {
                return ResponseData.fail();
            }
        }
    }

    @BusinessLog(title = "查询参与执行方案用户", opType = LogAnnotionOpTypeEnum.QUERY)
    @ApiOperation(value = "查询参与执行方案用户", notes = "查询参与执行方案用户", produces = "application/json")
    @PostMapping("/queryExecuteConsAndCust")
    public ResponseData queryExecuteConsOrCust(@RequestBody @Validated(BaseLineParam.page.class) ExecutePlanConsParam planConsParam) {
        if (null != planConsParam && "1".equals(planConsParam.getIntegrator())) {
            return ResponseData.success(planConsService.queryExecuteCust(planConsParam));
        } else if (null != planConsParam && "0".equals(planConsParam.getIntegrator())) {
            return ResponseData.success(planConsService.queryExecuteCons(planConsParam));
        } else {
            return ResponseData.success(new Page<EventConsOrCustResult>());
        }
    }

    @BusinessLog(title = "查询参与执行方案汇总", opType = LogAnnotionOpTypeEnum.QUERY)
    @ApiOperation(value = "查询参与执行方案汇总", notes = "查询参与执行方案汇总", produces = "application/json")
    @PostMapping("/queryExecutePlanTotal")
    public ResponseData queryExecutePlanTotal(@RequestBody @Validated(BaseLineParam.detail.class) ExecutePlanConsParam planConsParam) {
        return ResponseData.success(planConsService.queryExecutePlanTotal(planConsParam));
    }

    /**
     * @description: 需求响应事件列表
     * @param:
     * @return:
     * @author: PengChuqing
     * @date: 2021/10/8 14:42
     */
//    @BusinessLog(title = "需求响应事件列表", opType = LogAnnotionOpTypeEnum.QUERY)
    @ApiOperation(value = "根据结算编号查询事件列表", notes = "根据结算编号查询事件列表", produces = "application/json")
    @GetMapping("/listBySettlementNo")
    @ApiImplicitParam(value = "settlementNo", name = "结算编号")
    public ResponseData listBySettlementNo(String settlementNo) {
        return ResponseData.success(eventService.listBySettlementNo(settlementNo));
    }

    /**
     * @param eventId 事件id
     * @description 撤销事件
     */
    @PostMapping("revokeEvent")
    public void revokeEvent(@RequestParam String eventId) {
        eventService.revokeEvent(eventId);
    }

    @PostMapping("parseEventSmsContent")
    public ResponseData<String> parseEventSmsContent(@RequestBody EventParam eventParam) {
        return ResponseData.success(eventService.parseEventSmsContent(eventParam.getEventId(), eventParam.getSmsContent()));
    }

    /**
     * 修改 事件电力缺口
     * @param eventParam
     * @return
     */
    @PostMapping("editEventRegulateCap")
    public ResponseData editEventRegulateCap(@RequestBody EventParam eventParam) {
        eventService.editEventRegulateCap(eventParam);
        return ResponseData.success();
    }
}

