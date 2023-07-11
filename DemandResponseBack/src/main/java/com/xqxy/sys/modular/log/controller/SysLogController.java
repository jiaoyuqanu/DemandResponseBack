package com.xqxy.sys.modular.log.controller;

import com.xqxy.core.annotion.BusinessLog;
import com.xqxy.core.enums.LogAnnotionOpTypeEnum;
import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.sys.modular.log.param.SysOpLogParam;
import com.xqxy.sys.modular.log.service.SysOpLogService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 系统日志控制器
 *
 * @author xuyuxiang
 * @date 2020/3/19 21:14
 */
@RestController
public class SysLogController {

    @Resource
    private SysOpLogService sysOpLogService;

    /**
     * @description: 彭储清
     * @param: 
     * @return: 
     * @author: PengChuqing
     * @date: 2021/6/23 10:25
     */
    //@Permission
    @BusinessLog(title = "操作日志分页", opType = LogAnnotionOpTypeEnum.QUERY)
    @ApiOperation(value = "操作日志分页", notes = "操作日志分页", produces = "application/json")
    @PostMapping("/sysOpLog/page")
    public ResponseData opLogPage(@RequestBody SysOpLogParam sysOpLogParam) {
        return ResponseData.success(sysOpLogService.page(sysOpLogParam));
    }

    /**
     * 清空操作日志
     *
     * @author xuyuxiang
     * @date 2020/3/23 16:28
     */
    //@Permission
    @PostMapping("/sysOpLog/delete")
    @BusinessLog(title = "操作日志_清空", opType = LogAnnotionOpTypeEnum.CLEAN)
    public ResponseData opLogDelete() {
        sysOpLogService.delete();
        return ResponseData.success();
    }
}
