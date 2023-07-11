package com.xqxy.dr.modular.event.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.core.annotion.BusinessLog;
import com.xqxy.core.enums.LogAnnotionOpTypeEnum;
import com.xqxy.core.enums.YesOrNotEnum;
import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.event.entity.PlanCons;
import com.xqxy.dr.modular.event.param.DeleteConsParam;
import com.xqxy.dr.modular.event.param.DeleteCustParam;
import com.xqxy.dr.modular.event.param.DeleteRuleParam;
import com.xqxy.dr.modular.event.param.PlanParam;
import com.xqxy.dr.modular.event.result.EliminatedStatistics;
import com.xqxy.dr.modular.event.service.PlanConsService;
import com.xqxy.dr.modular.event.service.PlanService;
import com.xqxy.dr.modular.project.params.ExamineParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 需求响应方案 前端控制器
 * </p>
 *
 * @author PengChuqing
 * @since 2021-10-08
 */
@Api(tags = "需求响应方案API接口")
@RestController
@RequestMapping("/event/plan")
public class PlanController {


    @Resource
    private PlanService planService;

    @Resource
    private PlanConsService planConsService;

    /**
     * @description: 需求响应方案生成
     * @param: 
     * @return: 
     * @author: PengChuqing
     * @date: 2021/10/18 16:00
     */
//    @BusinessLog(title = "需求响应方案生成", opType = LogAnnotionOpTypeEnum.ADD)
    @ApiOperation(value = "需求响应方案生成", notes = "需求响应方案生成", produces = "application/json")
    @PostMapping("/generatePlan")
    public ResponseData generatePlan(@RequestBody @Validated(PlanParam.add.class) PlanParam planParam) {
        planService.generatePlan(planParam);
        return  ResponseData.success("方案正在生成，请稍等几秒!","");
    }

    /**
     * @description: 根据基线剔除规则剔除方案用户
     * @param:
     * @return:
     * @author: PengChuqing
     * @date: 2021/10/18 16:36
     */
//    @BusinessLog(title = "根据基线剔除规则剔除方案用户", opType = LogAnnotionOpTypeEnum.ADD)
    @ApiOperation(value = "根据基线剔除规则剔除方案用户", notes = "根据基线剔除规则剔除方案用户", produces = "application/json")
    @PostMapping("/eliminate")
    public ResponseData eliminate(@RequestBody @Validated(PlanParam.autoDeleted.class) PlanParam planParam) {
        Page<PlanCons> eliminate = planService.eliminate(planParam);
        return  ResponseData.success(eliminate);
    }

    /**
     * @description: 用户批量邀约
     * @param:
     * @return:
     * @author: PengChuqing
     * @date: 2021/10/18 16:36
     */
//    @BusinessLog(title = "批量邀约", opType = LogAnnotionOpTypeEnum.ADD)
    @ApiOperation(value = "用户批量邀约", notes = "用户批量邀约", produces = "application/json")
    @PostMapping("/batchConsInvation")
    public ResponseData batchConsInvation(@RequestBody @Validated(PlanParam.batchInvation.class) PlanParam planParam) {
        planService.batchInvation(planParam);
        return  ResponseData.success();
    }

    /**
     * @description: 集成商批量邀约
     * @param:
     * @return:
     * @author: PengChuqing
     * @date: 2021/10/18 16:36
     */
//    @BusinessLog(title = "批量邀约", opType = LogAnnotionOpTypeEnum.ADD)
    @ApiOperation(value = "集成商批量邀约", notes = "集成商批量邀约", produces = "application/json")
    @PostMapping("/batchCustInvation")
    public ResponseData batchCustInvation(@RequestBody @Validated(PlanParam.batchInvation.class) PlanParam planParam) {
        planService.batchCustInvation(planParam);
        return  ResponseData.success();
    }

    /**
     * @description: 用户剔除列表
     * @param:
     * @return:
     * @author: PengChuqing
     * @date: 2021/10/18 16:36
     */
//    @BusinessLog(title = "用户剔除列表", opType = LogAnnotionOpTypeEnum.ADD)
    @ApiOperation(value = "用户剔除列表", notes = "用户剔除列表", produces = "application/json")
    @PostMapping("/consDeleteList")
    public ResponseData consDeleteList(@RequestBody DeleteConsParam deleteRuleParam) {
        return  ResponseData.success(planConsService.consDeleteList(deleteRuleParam));
    }

    /**
     * @description: 集成商剔除列表
     * @param:
     * @return:
     * @author: PengChuqing
     * @date: 2021/10/18 16:36
     */
//    @BusinessLog(title = "客户剔除列表", opType = LogAnnotionOpTypeEnum.ADD)
    @ApiOperation(value = "集成商剔除列表", notes = "集成商剔除列表", produces = "application/json")
    @PostMapping("/custDeleteList")
    public ResponseData custDeleteList(@RequestBody DeleteCustParam deleteRuleParam) {
        return  ResponseData.success(planConsService.custDeleteList(deleteRuleParam));
    }

    /**
     * @description: 用户确认剔除
     * @param:
     * @return:
     * @author: PengChuqing
     * @date: 2021/10/18 16:36
     */
//    @BusinessLog(title = "用户确认剔除", opType = LogAnnotionOpTypeEnum.ADD)
    @ApiOperation(value = "用户确认剔除", notes = "用户确认剔除", produces = "application/json")
    @PostMapping("/deleteConsInvation")
    public ResponseData deleteConsInvation(@RequestBody @Validated(PlanParam.deleteInvation.class) PlanParam planParam) {
        planService.deleteInvation(planParam);
        return  ResponseData.success();
    }

    /**
     * @description: 集成商确认剔除
     * @param:
     * @return:
     * @author: PengChuqing
     * @date: 2021/10/18 16:36
     */
//    @BusinessLog(title = "用户确认剔除", opType = LogAnnotionOpTypeEnum.ADD)
    @ApiOperation(value = "集成商确认剔除", notes = "集成商确认剔除", produces = "application/json")
    @PostMapping("/deleteCustInvation")
    public ResponseData deleteCustInvation(@RequestBody @Validated(PlanParam.deleteInvation.class) PlanParam planParam) {
        planService.deleteCustInvation(planParam);
        return  ResponseData.success();
    }

    /**
     * @description: 手动剔除方案用户确认页面
     * @param:
     * @return:
     * @author: PengChuqing
     * @date: 2021/10/18 16:57
     */
//    @BusinessLog(title = "手动剔除方案用户确认页面", opType = LogAnnotionOpTypeEnum.ADD)
    @ApiOperation(value = "手动剔除方案用户确认页面", notes = "手动剔除方案用户确认页面", produces = "application/json")
    @PostMapping("/confirmManual")
    public ResponseData confirmManual(@RequestBody @Validated(PlanParam.manualDeleted.class) PlanParam planParam) {
        Page<PlanCons> eliminatedStatistics = planService.confirmManual(planParam);
        return ResponseData.success(eliminatedStatistics);
    }

    /**
     * @description: 确认剔除
     * @param:
     * @return:
     * @author: PengChuqing
     * @date: 2021/10/18 16:51
     */
//    @BusinessLog(title = "确认剔除", opType = LogAnnotionOpTypeEnum.ADD)
    @ApiOperation(value = "确认剔除", notes = "确认剔除", produces = "application/json")
    @PostMapping("/eliminateManual")
    public ResponseData eliminateManual(@RequestBody @Validated(PlanParam.manualDeleted.class) PlanParam planParam) {
        planService.eliminateManual(planParam);
        return ResponseData.success();
    }

    /**
     * @description: 方案编制提交审核
     * @param: 
     * @return: 
     * @author: PengChuqing
     * @date: 2021/10/19 17:19
     */
//    @BusinessLog(title = "方案编制提交审核", opType = LogAnnotionOpTypeEnum.DETAIL)
    @ApiOperation(value = "方案编制提交审核", notes = "方案编制提交审核", produces = "application/json")
    @PostMapping("/submit")
    public ResponseData submit(@RequestBody @Validated(PlanParam.detail.class) PlanParam planParam) {
        planService.submit(planParam);
        return ResponseData.success();

    }

 
    /**
     * @description: 方案编制审核 
     * @param: 
     * @return: 
     * @author: PengChuqing
     * @date: 2021/10/19 17:21
     */
//    @BusinessLog(title = "方案编制审核", opType = LogAnnotionOpTypeEnum.DETAIL)
    @ApiOperation(value = "方案编制审核", notes = "方案编制审核", produces = "application/json")
    @PostMapping("/examine")
    public ResponseData examine(@RequestBody @Validated(ExamineParam.add.class) ExamineParam examineParam) {
        planService.examine(examineParam);
        return ResponseData.success();
    }

    



}

