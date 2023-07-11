package com.xqxy.dr.modular.powerplant.controller;


import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.powerplant.entity.DaUser;
import com.xqxy.dr.modular.powerplant.service.DaUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 虚拟电厂-用户档案 前端控制器
 * @author lixiaojun
 * @since 2021-10-21
 */
@Api(tags = "需求相应-档案管理-用户档案")
@RestController
@RequestMapping("/powerplant/da-user")
public class DaUserController {

    @Resource
    private DaUserService daUserService;

    @ModelAttribute
    public DaUser get(@RequestParam(required = false) Integer id){
       DaUser entity = new DaUser();
       if(id != null){
           entity = daUserService.getById(id);
       }
       return entity;
    }

    /**
     * 用户档案列表
     */
    @ApiOperation(value = "用户档案分页查询", notes = "用户档案分页查询")
    @PostMapping("/list")
    public ResponseData page(@RequestBody DaUser entity) {
        return ResponseData.success(daUserService.page(entity));
    }

    /**
     * 添加
     */
    @ApiOperation(value = "添加用户档案", notes = "添加用户档案")
    @PostMapping("/add")
    public ResponseData add(@RequestBody DaUser entity) {
        daUserService.save(entity);
        return ResponseData.success();
    }

    /**
     * 编辑
     */
    @ApiOperation(value = "保存用户档案", notes = "保存用户档案")
    @PostMapping("/update")
    public ResponseData update(@RequestBody DaUser entity) {
        daUserService.updateById(entity);
        return ResponseData.success();
    }

    /**
     * 查看用户档案詳情
     */
    @ApiOperation(value = "用户档案詳情", notes = "用户档案詳情")
    @PostMapping("/detail/{id}")
    public ResponseData detail(@PathVariable Integer id) {
        return ResponseData.success(get(id));
    }

    /**
     * 删除用户档案
     */
    @ApiOperation(value = "用户档案删除", notes = "用户档案删除")
    @PostMapping("/delete/{ids}")
    public ResponseData delete(@PathVariable String[] ids) {
        daUserService.removeByIds(CollectionUtils.arrayToList(ids));
        return ResponseData.success();
    }

}

