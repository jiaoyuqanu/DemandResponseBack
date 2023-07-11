package com.xqxy.dr.modular.custConfig.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.adjustable.VO.DrConsAdjustablePotentialVO;
import com.xqxy.dr.modular.custConfig.DTO.DrCustSysConfigDTO;
import com.xqxy.dr.modular.custConfig.entity.DrCustSysConfig;
import com.xqxy.dr.modular.custConfig.service.DrCustSysConfigService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liqirui
 * @since 2021-11-12
 */
@RestController
@RequestMapping("/custConfig")
public class DrCustSysConfigController {

    @Autowired
    private DrCustSysConfigService custSysConfigService;

    /**
     * 客户对接 分页查询
     * @param drCustSysConfigDTO
     * @return
     */
    @ApiOperation(value = "客户对接 分页查询", notes = "客户对接 分页查询", produces = "application/json")
    @PostMapping(value = "/pageCustConfig")
    public ResponseData pageCustConfig(@RequestBody DrCustSysConfigDTO drCustSysConfigDTO) {
        Page<DrCustSysConfig> page = new Page<>(drCustSysConfigDTO.getCurrent(),drCustSysConfigDTO.getSize());
        Page<DrCustSysConfig> pageDrCustSysConfig = custSysConfigService.pageCustConfig(page,drCustSysConfigDTO);
        return ResponseData.success(pageDrCustSysConfig);
    }

    /**
     * 客户对接 新增
     * @param drCustSysConfig
     * @return
     */
    @ApiOperation(value = "客户对接 新增", notes = "客户对接 新增", produces = "application/json")
    @PostMapping(value = "/addCustConfig")
    public ResponseData addCustConfig(@RequestBody DrCustSysConfig drCustSysConfig) {
        custSysConfigService.addCustConfig(drCustSysConfig);
        return ResponseData.success();
    }

    /**
     * 客户对接 修改
     * @param drCustSysConfig
     * @return
     */
    @ApiOperation(value = "客户对接 修改", notes = "客户对接 修改", produces = "application/json")
    @PostMapping(value = "/editCustConfig")
    public ResponseData editCustConfig(@RequestBody DrCustSysConfig drCustSysConfig) {
        custSysConfigService.editCustConfig(drCustSysConfig);
        return ResponseData.success();
    }

    /**
     * 客户对接 删除
     * @param id
     * @return
     */
    @ApiOperation(value = "客户对接 删除", notes = "客户对接 删除", produces = "application/json")
    @GetMapping(value = "/deleteCustConfig/{id}")
    public ResponseData deleteCustConfig(@PathVariable("id") Long id) {
        custSysConfigService.deleteCustConfig(id);
        return ResponseData.success();
    }

}

