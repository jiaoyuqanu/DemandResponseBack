package com.xqxy.sys.modular.log.controller;


import com.xqxy.core.annotion.BusinessLog;
import com.xqxy.core.enums.LogAnnotionOpTypeEnum;
import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.sys.modular.log.param.DrRequestLogParam;
import com.xqxy.sys.modular.log.service.DcRequestLogService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xiao jun
 * @since 2021-06-21
 */
@RestController
@RequestMapping("/log/dc-request-log")
public class DcRequestLogController {

    @Resource
    private DcRequestLogService dcRequestLogService;

    /**
     * @description: 中台请求日志分页查询
     * @param:
     * @return:
     * @author: PengChuqing
     * @date: 2021/6/21 20:17
     */
    @BusinessLog(title = "中台请求日志分页查询", opType = LogAnnotionOpTypeEnum.QUERY)
    @ApiOperation(value = "中台请求日志分页查询", notes = "中台请求日志分页查询", produces = "application/json")
    @PostMapping("/page")
    public ResponseData page(@RequestBody DrRequestLogParam drRequestLogParam) {
        return ResponseData.success(dcRequestLogService.page(drRequestLogParam));
    }

}

