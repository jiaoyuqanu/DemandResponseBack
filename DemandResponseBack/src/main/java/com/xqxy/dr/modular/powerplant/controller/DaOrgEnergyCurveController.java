package com.xqxy.dr.modular.powerplant.controller;


import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.powerplant.service.DaOrgEnergyCurveService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 用户总电能量曲线 前端控制器
 * </p>
 *
 * @author shi
 * @since 2021-12-08
 */
@RestController
@RequestMapping("/powerplant/da-org-energy-curve")
public class DaOrgEnergyCurveController {

    @Resource
    DaOrgEnergyCurveService daOrgEnergyCurveService;
    @ApiOperation(value = "供电统调日电量信息", notes = "供电统调日电量信息", produces = "application/json")
    @PostMapping("/getEnergyCurve")
    public ResponseData list() {
        return ResponseData.success(daOrgEnergyCurveService.getEnergyCurve());
    }

}

