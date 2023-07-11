package com.xqxy.dr.modular.powerplant.controller;


import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.powerplant.entity.DaPlan;
import com.xqxy.dr.modular.powerplant.service.DaPlanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 公布出清计划表 前端控制器
 * @author lixiaojun
 * @since 2021-11-09
 */
@Api(tags = "需求相应-虚拟电厂-出清计划")
@RestController
@RequestMapping("/powerplant/da-plan")
public class DaPlanController {

    @Resource
    private DaPlanService daPlanService;

    /**
     * 市场准入列表
     */
    @ApiOperation(value = "出清计划分页查询", notes = "出清计划分页查询")
    @PostMapping("/list")
    public ResponseData page(@RequestBody DaPlan entity) {
        return ResponseData.success(daPlanService.page(entity));
    }


}

