package com.xqxy.dr.modular.powerplant.controller;


import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.powerplant.common.TableEnum;
import com.xqxy.dr.modular.powerplant.entity.DaPowerplant;
import com.xqxy.dr.modular.powerplant.service.DaPowerplantService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 电厂档案 前端控制器
 * @author lixiaojun
 * @since 2021-10-21
 */
@Api(tags = "需求相应-档案管理-电厂档案")
@RestController
@RequestMapping("/powerplant/da-powerplant")
public class DaPowerplantController {

    @Resource
    private DaPowerplantService daPowerplantService;

    @ModelAttribute
    public DaPowerplant get(@RequestParam(required = false) Integer id){
        DaPowerplant entity = new DaPowerplant();
        if(id != null){
            entity = daPowerplantService.getById(id);
        }
        return entity;
    }

    /**
     * 电厂档案列表
     */
    @ApiOperation(value = "电厂档案分页查询", notes = "电厂档案分页查询")
    @PostMapping("/list")
    public ResponseData page(@RequestBody DaPowerplant entity) {
        return ResponseData.success(daPowerplantService.page(entity));
    }

    /**
     * 編輯
     */
    @ApiOperation(value = "编辑电厂档案", notes = "编辑电厂档案")
    @PostMapping("/update")
    public ResponseData update(@RequestBody DaPowerplant entity) {
        daPowerplantService.updateById(entity);
        return ResponseData.success();
    }

    /**
     * 添加
     */
    @ApiOperation(value = "添加电厂档案", notes = "添加电厂档案")
    @PostMapping("/add")
    public ResponseData add(@RequestBody DaPowerplant entity) {
        String num = daPowerplantService.getNum(TableEnum.DR_DA_POWERPLANT.toString());
        entity.setNum(num);
        daPowerplantService.save(entity);
        return ResponseData.success();
    }

    /**
     * 查看电厂档案詳情
     */
    @ApiOperation(value = "电厂档案詳情", notes = "电厂档案詳情")
    @PostMapping("/detail/{id}")
    public ResponseData detail(@PathVariable Integer id) {
        return ResponseData.success(get(id));
    }

    /**
     * 删除电厂档案
     */
    @ApiOperation(value = "电厂档案删除", notes = "电厂档案删除")
    @PostMapping("/delete/{ids}")
    public ResponseData delete(@PathVariable String[] ids) {
        daPowerplantService.removeByIds(CollectionUtils.arrayToList(ids));
        return ResponseData.success();
    }

//    @ApiOperation(value = "自动生成电厂编号（测试）", notes = "自动生成电厂编号（测试）")
//    @GetMapping("/getNum")
//    public ResponseData getNum() {
//        return ResponseData.success(daPowerplantService.getNum());
//    }


}

