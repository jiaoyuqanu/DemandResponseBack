package com.xqxy.dr.modular.powerplant.controller;


import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.powerplant.param.DaParam;
import com.xqxy.dr.modular.powerplant.service.DaMarketVoidancePriceService;
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
@Api(tags = "市场出清费用")
@RequestMapping("/powerplant/da-market-voidance-price")
public class DaMarketVoidancePriceController {
    @Resource
    private DaMarketVoidancePriceService daMarketVoidancePriceService;

    @ApiOperation(value = "市场出清费用信息", notes = "市场出清费用信息")
    @PostMapping("/page")
    public ResponseData page(@RequestBody DaParam daParam) {
        return ResponseData.success(daMarketVoidancePriceService.page(daParam));
    }

    @ApiOperation(value = "市场出清费用审核", notes = "市场出清费用审核")
    @PostMapping("/verify")
    public ResponseData verify(@RequestBody DaParam daParam) {
        daMarketVoidancePriceService.verify(daParam);
        return ResponseData.success();
    }
}

