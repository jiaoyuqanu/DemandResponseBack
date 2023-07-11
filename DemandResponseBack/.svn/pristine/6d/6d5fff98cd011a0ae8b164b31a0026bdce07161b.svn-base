package com.xqxy.dr.modular.event.controller;


import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.xqxy.core.annotion.BusinessLog;
import com.xqxy.core.enums.LogAnnotionOpTypeEnum;
import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.core.util.JSONRespUtil;
import com.xqxy.dr.modular.event.entity.ConsInvitation;
import com.xqxy.dr.modular.event.param.ConsInvitationParam;
import com.xqxy.dr.modular.event.param.EventParam;
import com.xqxy.dr.modular.event.param.MyRepresentation;
import com.xqxy.dr.modular.event.service.ConsInvitationService;
import com.xqxy.dr.modular.event.service.CustInvitationService;
import com.xqxy.dr.modular.event.service.EventService;
import com.xqxy.dr.modular.event.service.serviceFactory.AppealExamineServiceBeanFactory;
import com.xqxy.dr.modular.subsidy.entity.SubsidyAppeal;
import com.xqxy.sys.modular.cust.param.CustParam;
import com.xqxy.sys.modular.cust.service.ConsService;
import com.xqxy.sys.modular.cust.service.CustService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import com.xqxy.dr.modular.data.entity.ConsCurve;
import com.xqxy.dr.modular.event.param.CustInvitationParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


import javax.annotation.Resource;

import java.util.List;

/**
 * <p>
 * 用户邀约 前端控制器
 * </p>
 *
 * @author PengChuqing
 * @since 2021-10-09
 */
@Api(tags = "用户邀约API接口")
@RestController
@RequestMapping("/event/cons-invitation")
public class ConsInvitationController {

    @Resource
    private ConsInvitationService consInvitationService;
    @Resource
    private CustInvitationService custInvitationService;
    @Resource
    private ConsService consService;

    @Autowired
    private CustService custService;

    /**
     * @description: 用户邀约信息分页查询
     * @param:
     * @return:
     * @author: shi
     * @date: 2021/10/22 14:27
     */
    @BusinessLog(title = "用户邀约信息分页查询", opType = LogAnnotionOpTypeEnum.QUERY)
    @ApiOperation(value = "用户邀约信息分页查询", notes = "用户邀约信息分页查询", produces = "application/json")
    @PostMapping("/invitationPage")
    public ResponseData invitationPage(@RequestBody ConsInvitationParam consInvitationParam) {
        return ResponseData.success(consInvitationService.invitationPage(consInvitationParam));
    }


    @BusinessLog(title = "我的申述分页查询", opType = LogAnnotionOpTypeEnum.QUERY)
    @ApiOperation(value = "我的申述分页查询", notes = "我的申述分页查询", produces = "application/json")
    @PostMapping("/myRepresentationPage")
    public ResponseData myRepresentationPage(@RequestBody ConsInvitationParam consInvitationParam) {
        return ResponseData.success(consInvitationService.myRepresentationPage(consInvitationParam));
    }


    @BusinessLog(title = "根据ID查询我的申述", opType = LogAnnotionOpTypeEnum.QUERY)
    @ApiOperation(value = "根据ID查询我的申述", notes = "根据ID查询我的申述", produces = "application/json")
    @PostMapping("/getMyRepresentationById")
    public ResponseData getMyRepresentationById(@RequestBody MyRepresentation consInvitationParam) {
        return ResponseData.success(consInvitationService.getMyRepresentationById(consInvitationParam));
    }


    @BusinessLog(title = "根据ID修改我的申述", opType = LogAnnotionOpTypeEnum.QUERY)
    @ApiOperation(value = "根据ID修改我的申述", notes = "根据ID修改我的申述", produces = "application/json")
    @PostMapping("/updateMyRepresentationById")
    public ResponseData updateMyRepresentationById(@RequestBody MyRepresentation consInvitationParam) {
        consInvitationService.updateMyRepresentationById(consInvitationParam);
        return ResponseData.success("提交成功！");
    }


    /**
     * @description: 事件全景-用户反馈分页
     * @param:
     * @return:
     * @author: PengChuqing
     * @date: 2021/11/4 14:38
     */
    @ApiOperation(value = "事件全景-用户反馈分页", notes = "事件全景-用户反馈分页", produces = "application/json")
    @PostMapping("/replyPage")
    public ResponseData replyPage(@RequestBody ConsInvitationParam consInvitationParam) {
        return ResponseData.success(consInvitationService.page(consInvitationParam));
    }

    /**
     * @description: 事件全景-用户反馈分页
     * @param:
     * @return:
     * @author: PengChuqing
     * @date: 2021/11/4 14:38
     */
    @ApiOperation(value = "事件全景-用户反馈分页", notes = "事件全景-用户反馈分页", produces = "application/json")
    @PostMapping("/replyPageCust")
    public ResponseData replyPageCust(@RequestBody CustInvitationParam custInvitationParam) {
        return ResponseData.success(custInvitationService.page(custInvitationParam));
    }

    /**
     * @description: 事件全景-用户反馈分页
     * @param:
     * @return:
     * @author: PengChuqing
     * @date: 2021/11/4 14:38
     */
    @ApiOperation(value = "事件全景-用户反馈分页", notes = "事件全景-用户反馈分页", produces = "application/json")
    @PostMapping("/replyPageProxy")
    public ResponseData replyPageProxy(@RequestBody CustInvitationParam custInvitationParam) {
        ConsInvitationParam consInvitationParam = new ConsInvitationParam();
        CustParam custParam = new CustParam();
        custParam.setCustId(custInvitationParam.getCustId().toString());
        consInvitationParam.setConsIdList(consService.getConsIdlistByCustId(custParam));
        consInvitationParam.setEventId(custInvitationParam.getEventId());
        return ResponseData.success(consInvitationService.replyPageProxy(consInvitationParam));
    }

    /**
     * @description: 用户邀约清单
     * @param:
     * @return:
     * @author: shi
     * @date: 2021/10/26 14:27
     */
    @BusinessLog(title = "用户邀约清单", opType = LogAnnotionOpTypeEnum.QUERY)
    @ApiOperation(value = "用户邀约清单", notes = "用户邀约清单", produces = "application/json")
    @PostMapping("/invitationList")
    public ResponseData invitationList(@RequestParam(name = "eventId", required = true) Long eventId) {
        return ResponseData.success(consInvitationService.invitationList(eventId));
    }

    /**
     * @description: 集成商代理用户邀约详情
     * @param:
     * @return:
     * @author: PengChuqing
     * @date: 2021/10/22 15:53
     */
    @BusinessLog(title = "集成商代理用户邀约详情", opType = LogAnnotionOpTypeEnum.QUERY)
    @ApiOperation(value = "集成商代理用户邀约详情", notes = "集成商代理用户邀约详情", produces = "application/json")
    @PostMapping("/proxyPage")
    public ResponseData proxyPage(@RequestBody CustInvitationParam custInvitationParam) {
        return ResponseData.success(consInvitationService.proxyPage(custInvitationParam));
    }

    /**
     * @description: 邀约反馈跟踪曲线
     * @param:
     * @return:
     * @author: PengChuqing
     * @date: 2021/10/25 15:43
     */
    @BusinessLog(title = "邀约反馈跟踪曲线", opType = LogAnnotionOpTypeEnum.QUERY)
    @ApiOperation(value = "邀约反馈跟踪曲线", notes = "邀约反馈跟踪曲线", produces = "application/json")
    @PostMapping("/trackInvitationReply")
    public ResponseData trackInvitationReply(@RequestBody ConsInvitationParam consInvitationParam) {
        List<ConsCurve> trackInvitationCurve = consInvitationService.trackInvitationReply(consInvitationParam);
        return ResponseData.success(trackInvitationCurve);
    }

    @Autowired
    private EventService eventService;

    /**
     * @description: 用户提交反馈接口
     * @param:
     * @return:
     * @author: shi
     * @date: 2021/10/26 19:43
     */
    @BusinessLog(title = "用户提交反馈接口", opType = LogAnnotionOpTypeEnum.QUERY)
    @ApiOperation(value = "用户提交反馈接口", notes = "用户提交反馈接口", produces = "application/json")
    @PostMapping("/submitFeedback")
    public ResponseData submitFeedback(@RequestBody ConsInvitationParam consInvitationParam) {
        if (ObjectUtil.isNotNull(consInvitationParam.getEventId()) || ObjectUtil.isNotNull(consInvitationParam.getConsId())) {
            return consInvitationService.submitFeedback(consInvitationParam);
        } else {
            return ResponseData.fail("-1", "参数事件和用户标识不能为空", "参数事件和用户标识不能为空");
        }
    }

    /**
     * @description: 邀约详情
     * @param:
     * @return:
     * @author: shi
     * @date: 2021/10/26 19:43
     */
    @BusinessLog(title = "邀约详情", opType = LogAnnotionOpTypeEnum.QUERY)
    @ApiOperation(value = "用户反馈邀约详情", notes = "用户反馈邀约详情", produces = "application/json")
    @PostMapping("/detail")
    public ResponseData detail(@RequestBody EventParam eventParam) {
        return ResponseData.success(consInvitationService.detail(eventParam));
    }

    /**
     * @description: 邀约详情
     * @param:
     * @return:
     * @author: shi
     * @date: 2021/10/26 19:43
     */
    @BusinessLog(title = "邀约详情", opType = LogAnnotionOpTypeEnum.QUERY)
    @ApiOperation(value = "用户反馈邀约详情", notes = "用户反馈邀约详情", produces = "application/json")
    @PostMapping("/appDetail")
    public ResponseData appDetail(@RequestBody EventParam eventParam) {
        ConsInvitation detail = consInvitationService.detail(eventParam);
        JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(detail));
        return ResponseData.success(JSONRespUtil.removeP(jsonObject, 1));
    }


    /**
     * @description: 获取当前事件的一条邀约信息
     * @param:
     * @return:
     * @author: PengChuqing
     * @date: 2021/5/14 10:31
     */
    @BusinessLog(title = "一条用户邀约信息", opType = LogAnnotionOpTypeEnum.DETAIL)
    @ApiOperation(value = "一条用户邀约信息", notes = "一条用户邀约信息", produces = "application/json")
    @PostMapping("/firstInvitationByEventId")
    public ResponseData firstInvitationByEventId(@RequestBody EventParam eventParam) {
        return ResponseData.success(consInvitationService.firstInvitationByEventId(eventParam.getEventId()));
    }

    /**
     * @description: 用户邀约信息结果
     * @param:
     * @return:
     * @author: PengChuqing
     * @date: 2021/11/13 15:29
     */
    @BusinessLog(title = "用户邀约信息结果", opType = LogAnnotionOpTypeEnum.DETAIL)
    @ApiOperation(value = "用户邀约信息结果", notes = "用户邀约信息结果", produces = "application/json")
    @PostMapping("/result")
    public ResponseData result(@RequestBody EventParam eventParam) {
        return ResponseData.success(consInvitationService.result(eventParam.getEventId()));
    }

    @BusinessLog(title = "用户反馈---应邀户数", opType = LogAnnotionOpTypeEnum.EXPORT)
    @ApiOperation(value = "用户反馈---应邀户数", notes = "用户反馈---应邀户数", produces = "application/json")
    @PostMapping("/invitationParticipateCount")
    public ResponseData invitationParticipateCount(@RequestBody EventParam eventParam) {
        return ResponseData.success(consInvitationService.invitationParticipateCount(eventParam.getEventId()));
    }

    @ApiOperation(value = "用户角色操作审核申诉记录提交", notes = "用户角色操作审核申诉记录提交", produces = "application/json")
    @PostMapping("/examineAppealSubmit")
    public ResponseData examineAppeal(@RequestBody MyRepresentation myRepresentation){        
	    try {
            AppealExamineServiceBeanFactory.getAppealServiceMap().get(myRepresentation.getDistinguish()).appealExamineSubmit(myRepresentation);
        } catch (Exception e) {
            return ResponseData.fail("500",e.getMessage(),e.getMessage());
        }
        return ResponseData.success();
    }

    @ApiOperation(value = "用户角色操作审核申诉记录撤销", notes = "用户角色操作审核申诉记录撤销", produces = "application/json")
    @PostMapping("/examineAppealCancel")
    public ResponseData examineAppealCancel(@RequestBody MyRepresentation myRepresentation) {
        try {
            AppealExamineServiceBeanFactory.getAppealServiceMap().get(myRepresentation.getDistinguish()).appealExamineCancel(myRepresentation);
        } catch (Exception e) {
            return ResponseData.fail("500",e.getMessage(),e.getMessage());
        }
        return ResponseData.success();
    }

    @ApiOperation(value = "用户角色操作审核申诉记录查看详情", notes = "用户角色操作审核申诉记录查看详情", produces = "application/json")
    @PostMapping("/examineAppealDetail")
    public ResponseData examineAppealDetail(@RequestBody MyRepresentation myRepresentation) {
        SubsidyAppeal subsidyAppeal = new SubsidyAppeal();
        try {
            subsidyAppeal = AppealExamineServiceBeanFactory.getAppealServiceMap().get(myRepresentation.getDistinguish()).appealExamineDetail(myRepresentation);
        } catch (Exception e) {
            return ResponseData.fail("500",e.getMessage(),e.getMessage());
        }
        return ResponseData.success(subsidyAppeal);
    }

    @ApiOperation(value = "用户角色操作审核申诉驳回", notes = "用户角色操作审核申诉驳回", produces = "application/json")
    @PostMapping("/examineAppealAbort")
    public ResponseData examineAppealAbort(@RequestBody MyRepresentation myRepresentation) {
        try {
            AppealExamineServiceBeanFactory.getAppealServiceMap().get(myRepresentation.getDistinguish()).appealExamineTurnDown(myRepresentation);
        } catch (Exception e) {
            return ResponseData.fail("500",e.getMessage(),e.getMessage());
        }
        return ResponseData.success();
    }
}

