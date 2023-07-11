package com.xqxy.sys.modular.dict.controller;


import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.sys.modular.dict.param.DictDataParam;
import com.xqxy.sys.modular.dict.param.DictTypeParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 字典类型 前端控制器
 * </p>
 *
 * @author xiao jun
 * @since 2021-03-11
 */
@Api(tags="字典类型API接口")
@RestController
@RequestMapping("/dict/dict-type")
public class DictTypeController {

    @Resource
    private com.xqxy.sys.modular.dict.service.DictTypeService DictTypeService;

    /**
     * 分页查询系统字典类型
     *
     * @author xuyuxiang，fengshuonan
     * @date 2020/3/31 20:30
     */
    @ApiOperation(value="字典类型分页查询", notes="字典类型分页查询", produces="application/json")
    //@Permission
    @PostMapping("/page")
    public ResponseData page(@RequestBody DictTypeParam dictTypeParam) {
        return ResponseData.success(DictTypeService.page(dictTypeParam));
    }

    /**
     * 获取字典类型列表
     *
     * @author xuyuxiang，fengshuonan
     * @date 2020/3/31 21:03
     */
    @ApiOperation(value="字典类型列表", notes="字典类型列表", produces="application/json")
    //@Permission
    @PostMapping("/list")
    public ResponseData list(@RequestBody DictTypeParam dictTypeParam) {
        return ResponseData.success(DictTypeService.list(dictTypeParam));
    }

    /**
     * 获取字典类型下所有字典，举例，返回格式为：[{code:"M",value:"男"},{code:"F",value:"女"}]
     *
     * @author xuyuxiang，fengshuonan yubaoshan
     * @date 2020/3/31 21:18
     */
    @RequestMapping("/dropDown")
    public ResponseData dropDown(@Validated(DictTypeParam.dropDown.class) DictTypeParam dictTypeParam) {
        return ResponseData.success(DictTypeService.dropDown(dictTypeParam));
    }

    @GetMapping("/dropDown_v2/{code}")
    public ResponseData dropDownv2(@PathVariable String code) {
        DictTypeParam dictTypeParam = new DictTypeParam();
        dictTypeParam.setCode(code);
        return ResponseData.success(DictTypeService.dropDown(dictTypeParam));
    }

    /**
     * 查看系统字典类型
     *
     * @author xuyuxiang，fengshuonan
     * @date 2020/3/31 20:31
     */
    @ApiOperation(value="字典类型查看", notes="字典类型查看", produces="application/json")
    //@Permission
    @PostMapping("/detail")
    public ResponseData detail(@Validated(DictTypeParam.detail.class) DictTypeParam dictTypeParam) {
        return ResponseData.success(DictTypeService.detail(dictTypeParam));
    }

    /**
     * 添加系统字典类型
     *
     * @author xuyuxiang，fengshuonan
     * @date 2020/3/31 20:30
     */
    @ApiOperation(value="字典类型添加", notes="字典类型添加", produces="application/json")
    //@Permission
    @PostMapping("/add")
    public ResponseData add(@RequestBody @Validated(DictTypeParam.add.class) DictTypeParam dictTypeParam) {
        DictTypeService.add(dictTypeParam);
        return ResponseData.success();
    }

    /**
     * 删除系统字典类型
     *
     * @author xuyuxiang，fengshuonan
     * @date 2020/3/31 20:30
     */
    @ApiOperation(value="字典类型删除", notes="字典类型删除", produces="application/json")
    //@Permission
    @PostMapping("/delete")
    public ResponseData delete(@RequestBody @Validated(DictTypeParam.delete.class) DictTypeParam dictTypeParam) {
        DictTypeService.delete(dictTypeParam);
        return ResponseData.success();
    }

    /**
     * 编辑系统字典类型
     *
     * @author xuyuxiang，fengshuonan
     * @date 2020/3/31 20:31
     */
    @ApiOperation(value="字典类型编辑", notes="字典类型编辑", produces="application/json")
    //@Permission
    @PostMapping("/edit")
    public ResponseData edit(@RequestBody @Validated(DictTypeParam.edit.class) DictTypeParam dictTypeParam) {
        DictTypeService.edit(dictTypeParam);
        return ResponseData.success();
    }

    /**
     * 修改状态
     *
     * @author stylefeng
     * @date 2020/4/30 22:20
     */
    @ApiOperation(value="字典类型修改状态", notes="字典类型修改状态", produces="application/json")
    //@Permission
    @PostMapping("/changeStatus")
    public ResponseData changeStatus(@RequestBody @Validated(DictTypeParam.changeStatus.class) DictTypeParam dictTypeParam) {
        DictTypeService.changeStatus(dictTypeParam);
        return ResponseData.success();
    }

    /**
     * 系统字典类型与字典类型构造的树
     *
     * @author stylefeng
     * @date 2020/4/30 22:20
     */
    @ApiOperation(value="字典类型树", notes="字典类型树", produces="application/json")
    @PostMapping("/tree")
    public ResponseData tree() {
        return ResponseData.success(DictTypeService.tree());
    }

    /**
     * 根据父级id查层级字典类型下子集
     *
     * @author chenzhijun, fengshuonan
     * @date 2020/3/31 21:03
     */
    @ApiOperation(value="字典值列表", notes="字典值列表", produces="application/json")
    //@Permission
    @RequestMapping("/getClassifyList")
    public ResponseData getClassifyList(@Validated(DictTypeParam.dropDown.class) DictTypeParam dictTypeParam) {
        return ResponseData.success(DictTypeService.getClassifyList(dictTypeParam));
    }

    /**
     * 根据type表 code查层级字典类型下所有的字典
     *
     * @author chenzhijun, fengshuonan
     * @date 2020/3/31 21:03
     */
    @ApiOperation(value="字典值列表", notes="字典值列表", produces="application/json")
    //@Permission
    @RequestMapping("/getTree")
    public ResponseData getTree(@Validated(DictTypeParam.dropDown.class) DictTypeParam dictTypeParam) {
        return ResponseData.success(DictTypeService.getTree(dictTypeParam));
    }

}

