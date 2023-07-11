package com.xqxy.dr.modular.powerplant.controller;


import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.powerplant.entity.DaExecute;
import com.xqxy.dr.modular.powerplant.service.DaExecuteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 执行情况统计 前端控制器
 * @author lixiaojun
 * @since 2021-11-09
 */
@Api(tags = "需求相应-虚拟电厂-执行情况统计")
@RestController
@RequestMapping("/powerplant/da-execute")
public class DaExecuteController {

    @Resource
    private DaExecuteService daExecuteService;

    /**
     * 列表
     */
    @ApiOperation(value = "执行情况分页查询", notes = "执行情况分页查询")
    @PostMapping("/list")
    public ResponseData page(@RequestBody DaExecute entity) {
        return ResponseData.success(daExecuteService.page(entity));
    }
    
}

