package com.xqxy.dr.modular.powerplant.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.powerplant.DTO.DrDaAggregatorDTO;
import com.xqxy.dr.modular.powerplant.entity.DaConsEffect;
import com.xqxy.dr.modular.powerplant.entity.DrDaAggregator;
import com.xqxy.dr.modular.powerplant.service.DrDaAggregatorService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 负荷聚合商信息表 前端控制器
 * </p>
 *
 * @author liqirui
 * @since 2021-12-09
 */
@RestController
@RequestMapping("/drDaAggregator")
public class DrDaAggregatorController {
    @Autowired
    private DrDaAggregatorService drDaAggregatorService;


    /**
     * 『负荷聚合商信息清单』 分页查询
     * @param
     * @return
     */
    @ApiOperation(value = "『负荷聚合商信息清单』 分页查询", notes = "『负荷聚合商信息清单』 分页查询", produces = "application/json")
    @PostMapping(value = "/pageAggregator")
    public ResponseData pageAggregator(@RequestBody DrDaAggregatorDTO drDaAggregatorDTO) {
        Page<DrDaAggregator> page = new Page<>(drDaAggregatorDTO.getCurrent(),drDaAggregatorDTO.getSize());
        Page<DrDaAggregator> pageAggregator = drDaAggregatorService.pageAggregator(page,drDaAggregatorDTO);
        return ResponseData.success(pageAggregator);
    }

    /**
     * 『负荷聚合商信息清单』 新增
     * @param
     * @return
     */
    @ApiOperation(value = "『负荷聚合商信息清单』 新增", notes = "『负荷聚合商信息清单』 新增", produces = "application/json")
    @PostMapping(value = "/addAggregator")
    public ResponseData addAggregator(@RequestBody DrDaAggregator drDaAggregator) {
        ResponseData responseData = drDaAggregatorService.addAggregator(drDaAggregator);
        return responseData;
    }

    /**
     * 根据负荷聚合商id 查询对应的代理用户
     * @param
     * @return
     */
    @ApiOperation(value = "根据负荷聚合商id 查询对应的代理用户", notes = "根据负荷聚合商id 查询对应的代理用户", produces = "application/json")
    @PostMapping(value = "/pageDaConsByAggregatorId")
    public ResponseData pageDaConsByAggregatorId(@RequestBody DrDaAggregatorDTO drDaAggregatorDTO) {
        Page<DaConsEffect> page = new Page<>(drDaAggregatorDTO.getCurrent(),drDaAggregatorDTO.getSize());
        Page<DaConsEffect> DaAggregatorCons= drDaAggregatorService.pageDaConsByAggregatorId(page,drDaAggregatorDTO);
        return ResponseData.success(DaAggregatorCons);
    }

    /**
     * 『负荷聚合商信息清单』 审核
     * @param
     * @return
     */
    @ApiOperation(value = "『负荷聚合商信息清单』 审核", notes = "『负荷聚合商信息清单』 审核", produces = "application/json")
    @PostMapping(value = "/editAggregator")
    public ResponseData editAggregator(@RequestBody DrDaAggregator drDaAggregator) {
        ResponseData responseData = drDaAggregatorService.editAggregator(drDaAggregator);
        return responseData;
    }

    /**
     * 『负荷聚合商信息清单』 下拉框模糊查询
     * @param
     * @return
     */
    @ApiOperation(value = "『负荷聚合商信息清单』 下拉框模糊查询", notes = "『负荷聚合商信息清单』 下拉框模糊查询", produces = "application/json")
    @PostMapping(value = "/listAggregator")
    public ResponseData listAggregator(@RequestBody DrDaAggregatorDTO drDaAggregatorDTO) {
        List<DrDaAggregator> list = drDaAggregatorService.listAggregator(drDaAggregatorDTO);
        return ResponseData.success(list);
    }
    /**
     * 『该供电单位负荷聚合商信息清单』 分页查询
     * @param
     * @return
     */
    @ApiOperation(value = "『该供电单位负荷聚合商信息清单』 分页查询", notes = "『该供电单位负荷聚合商信息清单』 分页查询", produces = "application/json")
    @PostMapping(value = "/pageAggregatorByOrg")
    public ResponseData pageAggregatorByOrg(@RequestBody DrDaAggregatorDTO drDaAggregatorDTO) {
        Page<DrDaAggregator> page = new Page<>(drDaAggregatorDTO.getCurrent(),drDaAggregatorDTO.getSize());
        Page<DrDaAggregator> pageAggregator = drDaAggregatorService.pageAggregatorByOrg(page,drDaAggregatorDTO);
        return ResponseData.success(pageAggregator);
    }
}

