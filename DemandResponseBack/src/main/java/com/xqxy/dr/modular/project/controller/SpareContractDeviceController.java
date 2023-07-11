package com.xqxy.dr.modular.project.controller;


import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.project.entity.SpareContractDevice;
import com.xqxy.dr.modular.project.params.SpareContractDeviceParam;
import com.xqxy.dr.modular.project.service.SpareContractDeviceService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 备用容量签约设备表 前端控制器
 * </p>
 *
 * @author Caoj
 * @since 2022-01-05
 */
@RestController
@RequestMapping("/project/spare-contract-device")
public class SpareContractDeviceController {
    @Resource
    private SpareContractDeviceService spareContractDeviceService;

    @ApiOperation(value = "设备签约列表", notes = "设备签约列表", produces = "application/json")
    @PostMapping("/queryDeviceContract")
    public ResponseData<?> queryDeviceContract(@RequestBody SpareContractDeviceParam spareContractDeviceParam) {
        return ResponseData.success(spareContractDeviceService.listDeviceContract(spareContractDeviceParam));
    }

    @ApiOperation(value = "设备签约添加", notes = "设备签约添加", produces = "application/json")
    @PostMapping("/deviceContract")
    public ResponseData<?> deviceContract(@RequestBody SpareContractDeviceParam spareContractDeviceParam) {
        spareContractDeviceService.deviceContract(spareContractDeviceParam);
        return ResponseData.success();
    }
}

