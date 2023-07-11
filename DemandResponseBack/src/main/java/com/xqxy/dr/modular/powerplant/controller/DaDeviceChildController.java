package com.xqxy.dr.modular.powerplant.controller;


import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.powerplant.entity.DaDeviceChild;
import com.xqxy.dr.modular.powerplant.service.DaDeviceChildService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 子子设备档案 前端控制器
 * @author lixiaojun
 * @since 2021-10-21
 */
@Api(tags = "需求相应-档案管理-子设备档案")
@RestController
@RequestMapping("/powerplant/da-device-child")
public class DaDeviceChildController {
    
    @Resource
    private DaDeviceChildService daDeviceChildService;

    @ModelAttribute
    public DaDeviceChild get(@RequestParam(required = false) Integer id){
        DaDeviceChild entity = new DaDeviceChild();
        if(id != null){
            entity = daDeviceChildService.getById(id);
        }
        return entity;
    }

    /**
     * 子设备档案列表
     */
    @ApiOperation(value = "子设备档案分页查询", notes = "子设备档案分页查询")
    @PostMapping("/list")
    public ResponseData page(DaDeviceChild entity) {
        return ResponseData.success(daDeviceChildService.page(entity));
    }

    /**
     * 添加
     */
    @ApiOperation(value = "添加子设备档案", notes = "添加子设备档案")
    @PostMapping("/add")
    public ResponseData add(DaDeviceChild entity) {
        daDeviceChildService.save(entity);
        return ResponseData.success();
    }

    /**
     * 编辑
     */
    @ApiOperation(value = "保存子设备档案", notes = "保存子设备档案")
    @PostMapping("/update")
    public ResponseData update(DaDeviceChild entity) {
        daDeviceChildService.updateById(entity);
        return ResponseData.success();
    }

    /**
     * 查看子设备档案詳情
     */
    @ApiOperation(value = "子设备档案詳情", notes = "子设备档案詳情")
    @PostMapping("/detail")
    public ResponseData detail(Integer id) {
        return ResponseData.success(get(id));
    }

    /**
     * 删除子设备档案
     */
    @ApiOperation(value = "子设备档案删除", notes = "子设备档案删除")
    @PostMapping("/delete")
    public ResponseData delete(String[] ids) {
        daDeviceChildService.removeByIds(CollectionUtils.arrayToList(ids));
        return ResponseData.success();
    }


}

