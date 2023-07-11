package com.xqxy.dr.modular.dispatch.controller;


import com.xqxy.core.annotion.BusinessLog;
import com.xqxy.core.enums.LogAnnotionOpTypeEnum;
import com.xqxy.core.pojo.base.param.BaseParam;
import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.dispatch.param.DispatchAndSoltParam;
import com.xqxy.dr.modular.dispatch.param.DispatchEditorParam;
import com.xqxy.dr.modular.dispatch.param.DispatchParam;
import com.xqxy.dr.modular.dispatch.service.DispatchService;
import com.xqxy.dr.modular.project.utlis.PageUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 调度需求响应指令 前端控制器
 * </p>
 *
 * @author xiao jun
 * @since 2021-05-14
 */
@Api(tags="调度需求接口")
@RestController
@RequestMapping("/dispatch/dispatch")
public class DispatchController {

    @Resource
    private DispatchService dispatchService;

    /**
     * @description: 调度指令新增
     * @param:
     * @return:
     * @author: chenzhijun
     * @date: 2021/5/17 21:12
     */
    //@BusinessLog(title = "调度指令新增", opType = LogAnnotionOpTypeEnum.ADD)
    @ApiOperation(value = "调度指令新增", notes = "调度指令新增", produces = "application/json")
    @PostMapping(value = "/add")
    public ResponseData add(@RequestBody @Validated(DispatchAndSoltParam.add.class)DispatchAndSoltParam dispatchAndSoltParam) {
        return ResponseData.success(dispatchService.add(dispatchAndSoltParam));
    }


    /**
     * @description: 调度指令撤销
     * @param:
     * @return:
     * @author: chenzhijun
     * @date: 2021/5/17 21:13
     */
    //@BusinessLog(title = "调度指令撤销", opType = LogAnnotionOpTypeEnum.OTHER)
    @ApiOperation(value = "调度指令撤销", notes = "调度指令撤销", produces = "application/json")
    @PostMapping("/revoke")
    public ResponseData revoke(@RequestBody DispatchParam dispatchParam) {
        return ResponseData.success(dispatchService.remove(dispatchParam));
    }

    /**
     * @description: 调度指令删除
     * @param:
     * @return:
     * @author: chenzhijun
     * @date: 2021/5/17 21:13
     */
    //@BusinessLog(title = "调度指令删除", opType = LogAnnotionOpTypeEnum.OTHER)
    @ApiOperation(value = "调度指令删除", notes = "调度指令删除", produces = "application/json")
    @PostMapping("/delete")
    public ResponseData delete(@RequestBody @Validated(DispatchParam.delete.class)DispatchParam dispatchParam) {
        dispatchService.delete(dispatchParam);
        return ResponseData.success();
    }

    /**
     * @description: 编辑调度指令
     * @param: 
     * @return: 
     * @author: chenzhijun
     * @date: 2021/5/17 21:16
     */
    //@BusinessLog(title = "编辑调度指令", opType = LogAnnotionOpTypeEnum.EDIT)
    @ApiOperation(value = "编辑调度指令", notes = "编辑调度指令", produces = "application/json")
    @PostMapping("/edit")
    public ResponseData edit(@RequestBody @Validated(DispatchEditorParam.edit.class)DispatchEditorParam dispatchEditorParam) {
        dispatchService.edit(dispatchEditorParam);
        return ResponseData.success();
    }

    /**
     * @description: 调度指令分页
     * @param: 
     * @return: 
     * @author: chenzhijun
     * @date: 2021/5/17 21:16
     */
    //@BusinessLog(title = "调度指令分页", opType = LogAnnotionOpTypeEnum.QUERY)
    @ApiOperation(value = "调度指令分页", notes = "调度指令分页", produces = "application/json")
    @PostMapping("/page")
    public ResponseData page(@RequestBody(required = false) DispatchParam dispatchParam) {
        return ResponseData.success(dispatchService.page(dispatchParam));
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
     * @description: 指令下发
     * @param: 
     * @return: 
     * @author: chenzhijun
     * @date: 2021/5/20 16:39
     */
    //@BusinessLog(title = "指令下发", opType = LogAnnotionOpTypeEnum.OTHER)
    @ApiOperation(value = "指令下发", notes = "指令下发", produces = "application/json")
    @PostMapping("/issueDispatch")
    public ResponseData issueDispatch(@RequestBody DispatchEditorParam dispatchParam) {
        return dispatchService.issueDispatch(dispatchParam);
    }

    /**
     * @description: 翻译区域
     * @param:
     * @return:
     * @author: chenzhijun
     * @date: 2021/5/20 16:39
     */
    //@BusinessLog(title = "指令下发", opType = LogAnnotionOpTypeEnum.OTHER)
    @ApiOperation(value = "翻译区域", notes = "翻译区域", produces = "application/json")
    @PostMapping("/getRegulateRangeStr")
    public ResponseData getRegulateRangeStr(@RequestBody DispatchParam dispatchParam) {
        return ResponseData.success(dispatchService.getRegulateRangeStr(dispatchParam));
    }





}

