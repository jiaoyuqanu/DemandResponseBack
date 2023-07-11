package com.xqxy.dr.modular.data.controller;


import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.data.entity.ConsEnergyCurve;
import com.xqxy.dr.modular.data.param.ConsAndDate;
import com.xqxy.dr.modular.data.service.ConsEnergyCurveService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 用户总电能量曲线 前端控制器
 * </p>
 *
 * @author xiao jun
 * @since 2021-05-13
 */
@RestController
@RequestMapping("/data/cons-energy-curve")
public class ConsEnergyCurveController {

    @Resource
    private ConsEnergyCurveService consEnergyCurveService;

    /**
     * @description: 实时电量曲线
     * @param:
     * @return:
     * @author: PengChuqing
     * @date: 2021/5/17 11:10
     */
    @ApiOperation(value = "实时电量曲线", notes = "实时电量曲线", produces = "application/json")
    @PostMapping("/getRealtimeCurve")
    public ResponseData getRealtimeCurve(@RequestBody ConsAndDate consAndDate) {
        ConsEnergyCurve realtimeCurve = consEnergyCurveService.getRealtimeCurve(consAndDate);
        return ResponseData.success(realtimeCurve);
    }

    /**
     * @description: 历史电量曲线
     * @param:
     * @return:
     * @author: PengChuqing
     * @date: 2021/5/17 11:11
     */
    @ApiOperation(value = "历史电量曲线", notes = "历史电量曲线", produces = "application/json")
    @PostMapping("/getHistoryCurve")
    public ResponseData getHistoryCurve(@RequestBody ConsAndDate consAndDate) {
        ConsEnergyCurve historyCurve = consEnergyCurveService.getHistoryCurve(consAndDate);
        return ResponseData.success(historyCurve);
    }

    @ApiOperation(value = "采集监测历史电量监测", notes = "采集监测历史电量监测", produces = "application/json")
    @PostMapping("/energyMonitorList")
    public ResponseData energyMonitorList(@RequestBody ConsAndDate consAndDate) {
        /*String format = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now());
        consAndDate.setDataDate(format);*/
        return ResponseData.success(consEnergyCurveService.energyMonitorList(consAndDate));
    }

}

