package com.xqxy.dr.modular.powerplant.controller;


import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.powerplant.param.DaParam;
import com.xqxy.dr.modular.powerplant.service.DaConsIncomeService;
import io.swagger.annotations.Api;
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
 * @author Caoj
 * @since 2021-12-08
 */
@RestController
@Api(tags = "代理用户分时收益")
@RequestMapping("/powerplant/da-cons-income")
public class DaConsIncomeController {
    @Resource
    private DaConsIncomeService daConsIncomeService;

    @ApiOperation(value = "代理用户分时收益信息", notes = "代理用户分时收益信息")
    @PostMapping("/page")
    public ResponseData page(@RequestBody DaParam daParam) {
        return ResponseData.success(daConsIncomeService.page(daParam));
    }
}

