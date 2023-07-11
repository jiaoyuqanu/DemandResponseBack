package com.xqxy.dr.modular.powerplant.controller;


import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.powerplant.param.DrDaAggregatorPriceParam;
import com.xqxy.dr.modular.powerplant.param.DrDaElectricBidNoticeParam;
import com.xqxy.dr.modular.powerplant.param.DrDaVoidanceParam;
import com.xqxy.dr.modular.powerplant.service.DrDaDeclarePowerService;
import com.xqxy.dr.modular.powerplant.service.DrDaVoidancePowerService;
import com.xqxy.dr.modular.powerplant.service.DrDaVoidanceService;
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
 *  出清结果 前端控制器
 * </p>
 *
 * @author czj
 * @since 2021-12-09
 */
@Api(tags="市场出清结果信息")
@RestController
@RequestMapping("/drDaVoidance")
public class DrDaVoidanceController {
    @Autowired
    private DrDaVoidanceService drDaVoidanceService;

    @Autowired
    private DrDaVoidancePowerService drDaVoidancePowerService;

    @Autowired
    private DrDaDeclarePowerService drDaDeclarePowerService;

    @ApiOperation(value = "市场出清结果信息", notes = "市场出清结果信息")
    @PostMapping("/page")
    public ResponseData page(@RequestBody DrDaVoidanceParam param) {
        return ResponseData.success(drDaVoidanceService.page(param));
    }

    @ApiOperation(value = "查询出清功率曲线", notes = "查询出清功率曲线")
    @PostMapping("/getVoidancePower")
    public ResponseData getVoidancePower(@RequestBody @Validated(DrDaVoidanceParam.detail.class) DrDaVoidanceParam param) {
        return ResponseData.success(drDaVoidancePowerService.getVoidancePower(param));
    }

    @ApiOperation(value = "查询申报功率曲线", notes = "查询申报功率曲线")
    @PostMapping("/getDeclarePower")
    public ResponseData getDeclarePower(@RequestBody @Validated(DrDaVoidanceParam.detail.class) DrDaVoidanceParam param) {
        return ResponseData.success(drDaDeclarePowerService.getDeclarePower(param));
    }
}

