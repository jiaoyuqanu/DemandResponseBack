package com.xqxy.dr.modular.device.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.adjustable.entity.DrDeviceAdjustablePotential;
import com.xqxy.dr.modular.adjustable.service.DrDeviceAdjustablePotentialService;
import com.xqxy.dr.modular.device.entity.DeviceMonitor;
import com.xqxy.dr.modular.device.param.DeviceMonitorParam;
import com.xqxy.dr.modular.device.service.DeviceMonitorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "设备检测 API接口")
@RestController
@RequestMapping("/device/deviceMonitor")
public class DeviceMonitorController {
   @Resource
   private DeviceMonitorService deviceMonitorService;

    @Autowired
    private DrDeviceAdjustablePotentialService drDeviceAdjustablePotentialService;

    @ApiOperation(value = "设备监测分页查询", notes = "设备检测分页查询", produces = "application/json")
    @PostMapping("/page")
    public ResponseData page(@RequestBody DeviceMonitorParam monitorParam) {
        Page<DeviceMonitor> page = new Page<>(monitorParam.getCurrent(),monitorParam.getSize());

        List<DeviceMonitor> list = deviceMonitorService.page(page,monitorParam);
        page.setRecords(list);
        return ResponseData.success(page);
    }

    /**
     * 设备监测详情展示
     * @param monitorParam
     * @return
     */
    @ApiOperation(value = "设备监测详情展示", notes = "设备监测详情展示", produces = "application/json")
    @PostMapping("/detailBydeviceId")
    public ResponseData detailBydeviceId(@RequestBody DeviceMonitorParam monitorParam) {
        Page<DrDeviceAdjustablePotential> page = new Page<>(monitorParam.getCurrent(),monitorParam.getSize());

        Page<DrDeviceAdjustablePotential> deviceMonitorPage = drDeviceAdjustablePotentialService.detailBydeviceId(page,monitorParam);
        return ResponseData.success(deviceMonitorPage);
    }

    /**
     * 设备监测 查询历史负荷
     * @param
     * @return
     */
    @ApiOperation(value = "设备监测 查询历史负荷", notes = "设备监测 查询历史负荷", produces = "application/json")
    @GetMapping("/getCurveByDeviceAndDate")
    public ResponseData getCurveByDeviceAndDate(@RequestParam("deviceId")String deviceId,@RequestParam("date")String date) {
        List<String> list = drDeviceAdjustablePotentialService.getCurveByDeviceAndDate(deviceId,date);
        return ResponseData.success(list);
    }



    @ApiOperation(value = "资源总览", notes = "资源总览", produces = "application/json")
    @GetMapping("/resourceOverview")
    public ResponseData resourceOverview() {
        return ResponseData.success(deviceMonitorService.resourceOverview());
    }

    @ApiOperation(value = "用户占比", notes = "用户占比", produces = "application/json")
    @GetMapping("/proportionOfUsers")
    public ResponseData proportionOfUsers() {
        return ResponseData.success(deviceMonitorService.proportionOfUsers());
    }

    @ApiOperation(value = "各地市可调负荷", notes = "各地市可调负荷", produces = "application/json")
    @GetMapping("regionalResources")
    public ResponseData regionalResources() {
        return ResponseData.success(deviceMonitorService.regionalResources());
    }
}
