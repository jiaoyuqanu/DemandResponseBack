package com.xqxy.dr.modular.powerplant.controller;


import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.powerplant.entity.DaJz;
import com.xqxy.dr.modular.powerplant.mapper.DaJzMapper;
import com.xqxy.dr.modular.powerplant.service.DaJzService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 机组档案 前端控制器
 * @author lixiaojun
 * @since 2021-10-21
 */
@Api(tags = "需求相应-档案管理-机组档案")
@RestController
@RequestMapping("/powerplant/da-jz")
public class DaJzController {

    @Resource
    private DaJzService daJzService;

    @Resource
    private DaJzMapper daJzMapper;

    @ModelAttribute
    public DaJz get(@RequestParam(required = false) Integer id){
        DaJz entity = new DaJz();
        if(id != null){
            entity = daJzService.getById(id);
        }
        return entity;
    }

    /**
     * 机组档案列表
     */
    @ApiOperation(value = "机组档案分页查询", notes = "机组档案分页查询")
    @PostMapping("/list")
    public ResponseData page(@RequestBody DaJz entity) {
        return ResponseData.success(daJzService.page(entity));
    }

    /**
     * 編輯
     */
    @ApiOperation(value = "编辑机组档案", notes = "编辑机组档案")
    @PostMapping("/update")
    public ResponseData update(@RequestBody DaJz entity) {
        daJzService.updateById(entity);
        return ResponseData.success();
    }

    /**
     * 添加
     */
    @ApiOperation(value = "添加机组档案", notes = "添加机组档案")
    @PostMapping("/add")
    public ResponseData add(@RequestBody DaJz entity) {
//        entity.setNum(RandomStringUtils.random(4));
        daJzService.save(entity);
        return ResponseData.success();
    }


    /**
     * 查看机组档案詳情
     */
    @ApiOperation(value = "机组档案詳情", notes = "机组档案詳情")
    @PostMapping("/detail/{id}")
    public ResponseData detail(@PathVariable Integer id) {
        return ResponseData.success(get(id));
    }

    /**
     * 删除机组档案
     */
    @ApiOperation(value = "机组档案删除", notes = "机组档案删除")
    @PostMapping("/delete/{ids}")
    public ResponseData delete(@PathVariable String[] ids) {
        daJzService.removeByIds(CollectionUtils.arrayToList(ids));
        return ResponseData.success();
    }

}