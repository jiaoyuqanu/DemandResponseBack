package com.xqxy.dr.modular.powerplant.controller;


import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.powerplant.param.DrDaAggregatorBaseLineParam;
import com.xqxy.dr.modular.powerplant.param.DrDaAggregatorPriceAddParam;
import com.xqxy.dr.modular.powerplant.param.DrDaAggregatorPriceParam;
import com.xqxy.dr.modular.powerplant.service.DrDaAggregatorBaselineService;
import com.xqxy.dr.modular.powerplant.service.DrDaAggregatorPriceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 负荷聚合商报价信息 前端控制器
 * </p>
 *
 * @author czj
 * @since 2021-12-09
 */
@Api(tags="负荷聚合商报价信息")
@RestController
@RequestMapping("/drDaAggregatorPrice")
public class DrDaAggregatorPriceController {
    @Autowired
    private DrDaAggregatorPriceService drDaAggregatorPriceService;

    @Autowired
    private DrDaAggregatorBaselineService drDaAggregatorBaselineService;

    @ApiOperation(value = "查看负荷聚合商列表", notes = "查看负荷聚合商列表")
    @PostMapping("/page")
    public ResponseData page(@RequestBody @Validated(DrDaAggregatorPriceParam.page.class)DrDaAggregatorPriceParam param) {
        return ResponseData.success(drDaAggregatorPriceService.page(param));
    }

    @ApiOperation(value = "提交负荷聚合商信息", notes = "提交负荷聚合商信息")
    @PostMapping("/submitInfo")
    public ResponseData submitInfo(@RequestBody DrDaAggregatorPriceParam param) {
        if(null==param || null==param.getDrDaAggregatorPriceList() || param.getDrDaAggregatorPriceList().size()==0) {
            return ResponseData.fail("负荷聚合商信息为空");
        }
        drDaAggregatorPriceService.submitInfo(param);
        return ResponseData.success();
    }

    @ApiOperation(value = "填报负荷聚合商报价信息", notes = "填报负荷聚合商报价信息")
    @PostMapping("/add")
    public ResponseData add(@RequestBody @Validated(DrDaAggregatorPriceAddParam.add.class)DrDaAggregatorPriceAddParam param) {
        drDaAggregatorPriceService.add(param);
        return ResponseData.success();
    }

    @ApiOperation(value = "获取日负荷基线图表", notes = "获取日负荷基线图表")
    @PostMapping("/getDrDaAggregatorBaseline")
    public ResponseData getDrDaAggregatorBaseline(@RequestBody @Validated(DrDaAggregatorBaseLineParam.detail.class)DrDaAggregatorBaseLineParam param) {
        return ResponseData.success(drDaAggregatorBaselineService.getDrDaAggregatorBaseline(param));
    }

    @ApiOperation(value = "提交负荷基线", notes = "提交负荷基线")
    @PostMapping("/submitBaseLine")
    public ResponseData submitBaseLine(@RequestBody @Validated(DrDaAggregatorPriceAddParam.edit.class)DrDaAggregatorPriceAddParam param) {
        if(null!=param.getId()) {
            drDaAggregatorBaselineService.submitBaseLine(param);
            return ResponseData.success();
        } else {
            return ResponseData.fail("id为空，提交失败");
        }
    }

}

