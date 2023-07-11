package com.xqxy.dr.modular.event.controller;


import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.event.param.CustExecuteParam;
import com.xqxy.dr.modular.event.param.PlanParam;
import com.xqxy.dr.modular.event.service.CustExecuteService;
import com.xqxy.dr.modular.event.service.CustExecuteService;
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
 * 电力用户执行信息 前端控制器
 * </p>
 *
 * @author Shen
 * @since 2021-10-27
 */
@Api(tags = "客户执行率API接口")
@RestController
@RequestMapping("/event/cust-execute")
public class CustExecuteController {

    @Resource
    private CustExecuteService CustExecuteService;

    // @BusinessLog(title = "事件实时执行率监测", opType = LogAnnotionOpTypeEnum.QUERY)
    @ApiOperation(value = "事件实时执行率监测", notes = "事件实时执行率监测", produces = "application/json")
    @PostMapping("/eventMonitoring")
    public ResponseData eventMonitoring(@RequestBody @Validated(PlanParam.detail.class) CustExecuteParam CustExecuteParam) {
        return ResponseData.success(CustExecuteService.eventMonitoring(CustExecuteParam));
    }


    /**
     * @description: 客户执行率列表
     * @param:
     * @return:
     * @author: PengChuqing
     * @date: 2021/10/27 16:55
     */
    // @BusinessLog(title = "用户执行率列表", opType = LogAnnotionOpTypeEnum.QUERY)
    @ApiOperation(value = "客户执行率列表", notes = "客户执行率列表", produces = "application/json")
    @PostMapping("/list")
    public ResponseData list(@RequestBody CustExecuteParam CustExecuteParam) {
        return ResponseData.success(CustExecuteService.list(CustExecuteParam));
    }

    /**
     * @description: 客户负荷基本信息
     * @param:
     * @return:
     * @author: PengChuqing
     * @date: 2021/10/27 16:55
     */
    // @BusinessLog(title = "客户负荷基本信息", opType = LogAnnotionOpTypeEnum.QUERY)
    @ApiOperation(value = "客户负荷基本信息", notes = "客户负荷基本信息", produces = "application/json")
    @PostMapping("/getCustBaseByEventId")
    public ResponseData getCustBaseByEventId(@RequestBody CustExecuteParam CustExecuteParam) {
        return ResponseData.success(CustExecuteService.getCustBaseByEventId(CustExecuteParam));
    }

    /**
     * @description: 客户执行曲线
     * @param:
     * @return:
     * @author: PengChuqing
     * @date: 2021/10/27 16:55
     */
    // @BusinessLog(title = "客户负荷基本信息", opType = LogAnnotionOpTypeEnum.QUERY)
    @ApiOperation(value = "客户执行曲线", notes = "客户执行曲线", produces = "application/json")
    @PostMapping("/curveOfBaseAndTarget")
    public ResponseData curveOfBaseAndTarget(@RequestBody CustExecuteParam CustExecuteParam) {
        return ResponseData.success(CustExecuteService.curveOfBaseAndTarget(CustExecuteParam));
    }

}

