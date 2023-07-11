package com.xqxy.dr.modular.powerplant.controller;


import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.powerplant.common.TableEnum;
import com.xqxy.dr.modular.powerplant.entity.DaMarket;
import com.xqxy.dr.modular.powerplant.entity.DaTrade;
import com.xqxy.dr.modular.powerplant.service.DaMarketService;
import com.xqxy.dr.modular.powerplant.service.DaPowerplantService;
import com.xqxy.dr.modular.powerplant.service.DaTradeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 市场准入审核表 前端控制器
 * @author lixiaojun
 * @since 2021-11-09
 */
@Api(tags = "需求相应-虚拟电厂-市场准入")
@RestController
@RequestMapping("/powerplant/da-market")
public class DaMarketController {

    @Resource
    private DaMarketService daMarketService;

    @Resource
    private DaPowerplantService daPowerplantService;

    @Resource
    private DaTradeService daTradeService;

    @ModelAttribute
    public DaMarket get(@RequestParam(required = false) Integer id){
        DaMarket entity = new DaMarket();
        if(id != null){
            entity = daMarketService.getById(id);
        }
        return entity;
    }

    /**
     * 市场准入列表
     */
    @ApiOperation(value = "市场准入分页查询", notes = "市场准入分页查询")
    @PostMapping("/list")
    public ResponseData page(@RequestBody DaMarket entity) {
        return ResponseData.success(daMarketService.page(entity));
    }

    /**
     * 編輯
     */
    @ApiOperation(value = "编辑市场准入", notes = "编辑市场准入")
    @PostMapping("/update")
    @Transactional
    public ResponseData update(@RequestBody DaMarket entity) {
        daMarketService.updateById(entity);
        return ResponseData.success();
    }

    /**
     * 添加
     */
    @ApiOperation(value = "添加市场准入", notes = "添加市场准入")
    @PostMapping("/add")
    public ResponseData add(@RequestBody DaMarket entity) {
        String num = daPowerplantService.getNum(TableEnum.DR_DA_MARKET.toString());
        entity.setNum(num);
        daMarketService.save(entity);
        daTradeService.save(new DaTrade(entity));
        return ResponseData.success();
    }

    /**
     * 查看市场准入詳情
     */
    @ApiOperation(value = "市场准入詳情", notes = "市场准入詳情")
    @PostMapping("/detail/{id}")
    public ResponseData detail(@PathVariable Integer id) {
        return ResponseData.success(get(id));
    }

    /**
     * 删除市场准入
     */
    @ApiOperation(value = "市场准入删除", notes = "市场准入删除")
    @PostMapping("/delete/{ids}")
    public ResponseData delete(@PathVariable String[] ids) {
        daMarketService.removeByIds(CollectionUtils.arrayToList(ids));
        return ResponseData.success();
    }
}

