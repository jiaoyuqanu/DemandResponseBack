package com.xqxy.dr.modular.powerplant.controller;


import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.powerplant.entity.TradeNotice;
import com.xqxy.dr.modular.powerplant.service.TradeNoticeService;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 交易公示 前端控制器
 * @author lixiaojun
 * @since 2021-10-22
 */
@RestController
@RequestMapping("/powerplant/trade-notice")
public class TradeNoticeController {
    
    @Resource
    private TradeNoticeService tradeNoticeService;

    @ModelAttribute
    public TradeNotice get(@RequestParam(required = false) Integer id){
        TradeNotice entity = new TradeNotice();
        if(id != null){
            entity = tradeNoticeService.getById(id);
        }
        return entity;
    }

    /**
     * 交易公示列表
     */
    @ApiOperation(value = "交易公示分页查询", notes = "交易公示分页查询")
    @PostMapping("/list")
    public ResponseData page(TradeNotice entity) {
        return ResponseData.success(tradeNoticeService.page(entity));
    }

    /**
     * 添加
     */
    @ApiOperation(value = "添加交易公示", notes = "添加交易公示")
    @PostMapping("/add")
    public ResponseData add(TradeNotice entity) {
        tradeNoticeService.save(entity);
        return ResponseData.success();
    }

    /**
     * 编辑
     */
    @ApiOperation(value = "保存交易公示", notes = "保存交易公示")
    @PostMapping("/update")
    public ResponseData update(TradeNotice entity) {
        tradeNoticeService.updateById(entity);
        return ResponseData.success();
    }

    /**
     * 查看交易公示詳情
     */
    @ApiOperation(value = "交易公示詳情", notes = "交易公示詳情")
    @PostMapping("/detail")
    public ResponseData detail(Integer id) {
        return ResponseData.success(get(id));
    }

    /**
     * 删除交易公示
     */
    @ApiOperation(value = "交易公示删除", notes = "交易公示删除")
    @PostMapping("/delete")
    public ResponseData delete(String[] ids) {
        tradeNoticeService.removeByIds(CollectionUtils.arrayToList(ids));
        return ResponseData.success();
    }

}

