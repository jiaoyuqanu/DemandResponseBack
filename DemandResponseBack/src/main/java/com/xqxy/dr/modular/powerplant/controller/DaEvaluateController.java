package com.xqxy.dr.modular.powerplant.controller;


import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.powerplant.entity.DaEvaluate;
import com.xqxy.dr.modular.powerplant.service.DaEvaluateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 评估表 前端控制器
 * @author lixiaojun
 * @since 2021-10-27
 */
@Api(tags = "需求相应-档案管理-评估")
@RestController
@RequestMapping("/powerplant/da-evaluate")
public class DaEvaluateController {

    @Resource
    private DaEvaluateService daEvaluateService;

    @ModelAttribute
    public DaEvaluate get(@RequestParam(required = false) Integer id){
        DaEvaluate entity = new DaEvaluate();
        if(id != null){
            entity = daEvaluateService.getById(id);
        }
        return entity;
    }

    /**
     * 评估列表
     */
    @ApiOperation(value = "评估分页查询", notes = "评估分页查询")
    @PostMapping("/list")
    public ResponseData page(@RequestBody DaEvaluate entity) {
        return ResponseData.success(daEvaluateService.page(entity));
    }

    /**
     * 編輯
     */
    @ApiOperation(value = "编辑评估", notes = "编辑评估")
    @PostMapping("/update")
    public ResponseData update(@RequestBody DaEvaluate entity) {
        daEvaluateService.updateById(entity);
        return ResponseData.success();
    }

    /**
     * 添加
     */
    @ApiOperation(value = "添加评估", notes = "添加评估")
    @PostMapping("/add")
    public ResponseData add(@RequestBody DaEvaluate entity) {
        daEvaluateService.save(entity);
        return ResponseData.success();
    }

    /**
     * 查看评估詳情
     */
    @ApiOperation(value = "评估詳情", notes = "评估詳情")
    @PostMapping("/detail/{id}")
    public ResponseData detail(@PathVariable Integer id) {
        return ResponseData.success(get(id));
    }

    /**
     * 删除评估
     */
    @ApiOperation(value = "评估删除", notes = "评估删除")
    @PostMapping("/delete/{ids}")
    public ResponseData delete(@PathVariable String[] ids) {
        daEvaluateService.removeByIds(CollectionUtils.arrayToList(ids));
        return ResponseData.success();
    }

    
}

