package com.xqxy.dr.modular.powerplant.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.powerplant.DTO.DrDaAggregatorElectricityDTO;
import com.xqxy.dr.modular.powerplant.entity.DrDaAggregator;
import com.xqxy.dr.modular.powerplant.service.DrDaAggregatorElectricityService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 目标聚合商表 前端控制器
 * </p>
 *
 * @author liqirui
 * @since 2021-12-09
 */
@RestController
@RequestMapping("/drDaAggregatorElectricity")
public class DrDaAggregatorElectricityController {

    @Autowired
    private DrDaAggregatorElectricityService drDaAggregatorElectricityService;


    /**
     * 查询目标聚合商 根据电力市场id
     * @param
     * @return
     */
    @ApiOperation(value = "查询目标聚合商 根据电力市场id", notes = "查询目标聚合商 根据电力市场id", produces = "application/json")
    @PostMapping(value = "/pageAggregatorByElectricityId")
    public ResponseData pageAggregatorByElectricityId(@RequestBody DrDaAggregatorElectricityDTO electricityDTO) {
        Page<DrDaAggregator> page = new Page<>(electricityDTO.getCurrent(),electricityDTO.getSize());
        Page<DrDaAggregator> pageAggregator = drDaAggregatorElectricityService.pageAggregatorByElectricityId(page,electricityDTO);
        return ResponseData.success(pageAggregator);
    }


    /**
     * 生成目标聚合商
     * @param
     * @return
     */
    @ApiOperation(value = "生成目标聚合商", notes = "生成目标聚合商", produces = "application/json")
    @PostMapping(value = "/addAggregatorElectricity")
    public ResponseData addAggregatorElectricity(@RequestBody DrDaAggregatorElectricityDTO electricityDTO) {
        Page<DrDaAggregator> page = new Page<>(electricityDTO.getCurrent(),electricityDTO.getSize());
        drDaAggregatorElectricityService.addAggregatorElectricity(page,electricityDTO);
        return ResponseData.success();
    }

}

