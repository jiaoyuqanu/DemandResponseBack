package com.xqxy.dr.modular.powerplant.controller;


import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.powerplant.entity.DaTrade;
import com.xqxy.dr.modular.powerplant.service.DaTradeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 交易提交表 前端控制器
 * @author lixiaojun
 * @since 2021-11-09
 */
@Api(tags = "需求相应-虚拟电厂-交易提交")
@RestController
@RequestMapping("/powerplant/da-trade")
public class DaTradeController {
    
    @Resource
    private DaTradeService daTradeService;

    @ModelAttribute
    public DaTrade get(@RequestParam(required = false) Integer id){
        DaTrade entity = new DaTrade();
        if(id != null){
            entity = daTradeService.getById(id);
        }
        return entity;
    }

    /**
     * 列表
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @PostMapping("/list")
    public ResponseData page(@RequestBody DaTrade entity) {
        return ResponseData.success(daTradeService.page(entity));
    }

    /**
     * 編輯
     */
    @ApiOperation(value = "编辑交易提交", notes = "编辑交易提交")
    @PostMapping("/update")
    @Transactional
    public ResponseData update(@RequestBody DaTrade entity) {
        daTradeService.updateById(entity);
        return ResponseData.success();
    }

    /**
     * 查看交易提交詳情
     */
    @ApiOperation(value = "交易提交詳情", notes = "交易提交詳情")
    @PostMapping("/detail/{id}")
    public ResponseData detail(@PathVariable Integer id) {
        return ResponseData.success(get(id));
    }

    /**
     * 删除交易提交
     */
    @ApiOperation(value = "交易提交删除", notes = "交易提交删除")
    @PostMapping("/delete/{ids}")
    public ResponseData delete(@PathVariable String[] ids) {
        daTradeService.removeByIds(CollectionUtils.arrayToList(ids));
        return ResponseData.success();
    }
    
}

