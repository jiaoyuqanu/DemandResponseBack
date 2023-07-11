package com.xqxy.sys.modular.dict.controller;


import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.sys.modular.dict.param.DictDataParam;
import com.xqxy.sys.modular.dict.service.DictDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 字典值 前端控制器
 * </p>
 *
 * @author xiao jun
 * @since 2021-03-11
 */
@Api(tags="字典值API接口")
@RestController
@RequestMapping("/dict/dict-data")
public class DictDataController {

    @Resource
    private DictDataService dictDataService;

    /**
     * 查询系统字典值
     *
     * @author xuyuxiang, fengshuonan
     * @date 2020/3/31 20:50
     */
    @ApiOperation(value="字典值分页查询", notes="字典值分页查询", produces="application/json")
    //@Permission
    @PostMapping("/page")
    public ResponseData page(@RequestBody DictDataParam dictDataParam) {
        return ResponseData.success(dictDataService.page(dictDataParam));
    }

    /**
     * 某个字典类型下所有的字典
     *
     * @author xuyuxiang, fengshuonan
     * @date 2020/3/31 21:03
     */
    @ApiOperation(value="字典值列表", notes="字典值列表", produces="application/json")
    //@Permission
    @PostMapping("/list")
    public ResponseData list(@Validated(DictDataParam.list.class) DictDataParam dictDataParam) {
        return ResponseData.success(dictDataService.list(dictDataParam));
    }

    /**
     * 查看系统字典值
     *
     * @author xuyuxiang, fengshuonan
     * @date 2020/3/31 20:51
     */
    @ApiOperation(value="字典值查看", notes="字典值查看", produces="application/json")
    //@Permission
    @PostMapping("/detail")
    public ResponseData detail(@Validated(DictDataParam.detail.class) DictDataParam dictDataParam) {
        return ResponseData.success(dictDataService.detail(dictDataParam));
    }

    /**
     * 添加系统字典值
     *
     * @author xuyuxiang, fengshuonan
     * @date 2020/3/31 20:50
     */
    @ApiOperation(value="字典值添加", notes="字典值添加", produces="application/json")
    //@Permission
    @PostMapping("/add")
    public ResponseData add(@RequestBody @Validated(DictDataParam.add.class) DictDataParam dictDataParam) {
        dictDataService.add(dictDataParam);
        return ResponseData.success();
    }

    /**
     * 删除系统字典值
     *
     * @author xuyuxiang, fengshuonan
     * @date 2020/3/31 20:50
     */
    @ApiOperation(value="字典值删除", notes="字典值删除", produces="application/json")
    //@Permission
    @PostMapping("/delete")
    public ResponseData delete(@RequestBody @Validated(DictDataParam.delete.class) DictDataParam dictDataParam) {
        dictDataService.delete(dictDataParam);
        return ResponseData.success();
    }

    /**
     * 编辑系统字典值
     *
     * @author xuyuxiang, fengshuonan
     * @date 2020/3/31 20:51
     */
    @ApiOperation(value="字典值删除", notes="字典值删除", produces="application/json")
    //@Permission
    @PostMapping("/edit")
    public ResponseData edit(@RequestBody @Validated(DictDataParam.edit.class) DictDataParam dictDataParam) {
        dictDataService.edit(dictDataParam);
        return ResponseData.success();
    }

    /**
     * 修改状态
     *
     * @author stylefeng
     * @date 2020/5/1 9:43
     */
    @ApiOperation(value="字典值修改状态", notes="字典值修改状态", produces="application/json")
    //@Permission
    @PostMapping("/changeStatus")
    public ResponseData changeStatus(@RequestBody @Validated(DictDataParam.changeStatus.class) DictDataParam dictDataParam) {
        dictDataService.changeStatus(dictDataParam);
        return ResponseData.success();
    }

}

