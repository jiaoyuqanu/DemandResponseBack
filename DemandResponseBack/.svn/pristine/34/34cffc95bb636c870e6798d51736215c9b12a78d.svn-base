package com.xqxy.dr.modular.powerplant.controller;


import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.powerplant.entity.DaDevice;
import com.xqxy.dr.modular.powerplant.service.DaDeviceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 设备档案 前端控制器
 * @author lixiaojun
 * @since 2021-10-21
 */
@Api(tags = "需求相应-档案管理-设备档案")
@RestController
@RequestMapping("/powerplant/da-device")
public class DaDeviceController {
    
    @Resource
    private DaDeviceService daDeviceService;

    @ModelAttribute
    public DaDevice get(@RequestParam(required = false) Integer id){
        DaDevice entity = new DaDevice();
        if(id != null){
            entity = daDeviceService.getById(id);
        }
        return entity;
    }

    /**
     * 设备档案列表
     */
    @ApiOperation(value = "设备档案分页查询", notes = "设备档案分页查询")
    @PostMapping("/list")
    public ResponseData page(DaDevice entity) {
        return ResponseData.success(daDeviceService.page(entity));
    }

    /**
     * 添加
     */
    @ApiOperation(value = "添加设备档案", notes = "添加设备档案")
    @PostMapping("/add")
    public ResponseData add(DaDevice entity) {
        daDeviceService.save(entity);
        return ResponseData.success();
    }

    /**
     * 编辑
     */
    @ApiOperation(value = "保存设备档案", notes = "保存设备档案")
    @PostMapping("/update")
    public ResponseData update(DaDevice entity) {
        daDeviceService.updateById(entity);
        return ResponseData.success();
    }

    /**
     * 查看设备档案詳情
     */
    @ApiOperation(value = "设备档案詳情", notes = "设备档案詳情")
    @PostMapping("/detail")
    public ResponseData detail(Integer id) {
        return ResponseData.success(get(id));
    }

    /**
     * 删除设备档案
     */
    @ApiOperation(value = "设备档案删除", notes = "设备档案删除")
    @PostMapping("/delete")
    public ResponseData delete(String[] ids) {
        daDeviceService.removeByIds(CollectionUtils.arrayToList(ids));
        return ResponseData.success();
    }


}

