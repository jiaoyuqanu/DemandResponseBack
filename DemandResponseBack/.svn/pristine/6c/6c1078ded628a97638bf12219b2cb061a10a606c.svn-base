package com.xqxy.dr.modular.powerplant.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.powerplant.DTO.DrDaElectricMarketDTO;
import com.xqxy.dr.modular.powerplant.entity.DrDaElectricMarket;
import com.xqxy.dr.modular.powerplant.service.DrDaElectricMarketService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 电力市场需求信息表 前端控制器
 * </p>
 *
 * @author liqirui
 * @since 2021-12-09
 */
@RestController
@RequestMapping("/drDaElectricMarket")
public class DrDaElectricMarketController {

    @Autowired
    private DrDaElectricMarketService drDaElectricMarketService;

    /**
     * 『电力市场需求信息』 分页
     * @param
     * @return
     */
    @ApiOperation(value = "『电力市场需求信息』 分页", notes = "『电力市场需求信息』 分页", produces = "application/json")
    @PostMapping(value = "/pageElectricMarket")
    public ResponseData pageElectricMarket(@RequestBody DrDaElectricMarketDTO daElectricMarketDTO) {
        Page<DrDaElectricMarket> page = new Page<>(daElectricMarketDTO.getCurrent(),daElectricMarketDTO.getSize());
        Page<DrDaElectricMarket> pageElectric= drDaElectricMarketService.pageElectricMarket(page,daElectricMarketDTO);
        return ResponseData.success(pageElectric);
    }


}

