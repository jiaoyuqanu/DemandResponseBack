package com.xqxy.dr.modular.powerplant.controller;


import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.powerplant.entity.DaSettlement;
import com.xqxy.dr.modular.powerplant.service.DaSettlementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 费用结算 前端控制器
 * @author lixiaojun
 * @since 2021-11-09
 */
@Api(tags = "需求相应-虚拟电厂-费用结算")
@RestController
@RequestMapping("/powerplant/da-settlement")
public class DaSettlementController {

    @Resource
    private DaSettlementService daSettlementService;

    /**
     * 市场准入列表
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @PostMapping("/list")
    public ResponseData page(@RequestBody DaSettlement entity) {
        return ResponseData.success(daSettlementService.page(entity));
    }


}

