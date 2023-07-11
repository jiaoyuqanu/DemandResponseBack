package com.xqxy.dr.modular.statistics.controller;


import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.statistics.param.StatisticalParam;
import com.xqxy.dr.modular.statistics.service.ProjectEffectService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/projectEffect")
public class ProjectEffectController {

    @Resource
    private ProjectEffectService projectEffectService;

    /**
     *  用户需求响应项目效果统计
     * @return
     */
    @ApiOperation(value = "用户需求响应项目效果统计", notes = "用户需求响应项目效果统计", produces = "application/json")
    @PostMapping("/projectEffectStatistics")
    public ResponseData projectEffectStatistics(@RequestBody StatisticalParam statisticalParam) {

        return ResponseData.success(projectEffectService.projectEffectStatistics(statisticalParam));
    }

    /**
     *  用户需求响应项目效果统计导出
     * @return
     */
    @ApiOperation(value = "用户需求响应项目效果统计导出", notes = "用户需求响应项目效果统计导出", produces = "application/json")
    @PostMapping("/exportProject")
    public void exportProject(@RequestBody StatisticalParam statisticalParam) {

        projectEffectService.exportProject(statisticalParam);
    }

}
