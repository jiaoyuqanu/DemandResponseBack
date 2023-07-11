package com.xqxy.dr.modular.powerplant.controller;


import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.powerplant.param.DaParam;
import com.xqxy.dr.modular.powerplant.service.DaConsEffectService;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Caoj
 * @since 2021-12-08
 */
@RestController
@Api(tags = "代理用户效果API")
@RequestMapping("/powerplant/da-cons-effect")
public class DaConsEffectController {
    @Resource
    private DaConsEffectService daConsEffectService;

    @ApiOperation(value = "代理用户效果信息", notes = "代理用户效果信息")
    @PostMapping("/page")
    public ResponseData page(@RequestBody DaParam daParam) {
        return ResponseData.success(daConsEffectService.page(daParam));
    }

    @ApiOperation(value = "代理用户效果导出", notes = "代理用户效果导出")
    @PostMapping("/exportConsEffect")
    public void exportConsEffect(@RequestBody DaParam daParam) {
        daConsEffectService.consEffectExport(daParam);
    }

    @ApiOperation(value = "代理用户负荷曲线", notes = "代理用户负荷曲线")
    @PostMapping("/curve")
    public ResponseData curve(@RequestBody DaParam daParam) {
        if(StringUtil.isNullOrEmpty(daParam.getId()))
        {
            return ResponseData.fail("参数标识不能为空");
        }
        return ResponseData.success(daConsEffectService.curve(daParam));
    }
}
