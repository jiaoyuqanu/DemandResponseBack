package com.xqxy.dr.modular.powerplant.controller;


import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.powerplant.service.DaOrgCurveService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 用户功率曲线 前端控制器
 * </p>
 *
 * @author shi
 * @since 2021-12-08
 */
@RestController
@RequestMapping("/powerplant/da-org-curve")
public class DaOrgCurveController {

    @Resource
    DaOrgCurveService daOrgCurveService;
    @ApiOperation(value = "供电统调日负荷信息", notes = "供电统调日负荷信息", produces = "application/json")
    @PostMapping("/getCurve")
    public ResponseData list() {
        return ResponseData.success(daOrgCurveService.getCurve());
    }

}

