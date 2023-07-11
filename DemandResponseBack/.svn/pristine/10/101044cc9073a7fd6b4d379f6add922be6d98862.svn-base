package com.xqxy.dr.modular.project.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xqxy.core.exception.ServiceException;
import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.project.entity.ConsContractDetail;
import com.xqxy.dr.modular.project.entity.ProjectDetail;
import com.xqxy.dr.modular.project.params.ConsContractParam;
import com.xqxy.dr.modular.project.params.CustContractParam;
import com.xqxy.dr.modular.project.params.ProjectParam;
import com.xqxy.dr.modular.project.service.ConsContractDetailService;
import com.xqxy.dr.modular.project.service.ProjectDetailService;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 项目明细 前端控制器
 * </p>
 *
 * @author PengChuqing
 * @since 2021-10-11
 */
@RestController
@RequestMapping("/project/project-detail")
public class ProjectDetailController {
    @Resource
    private ProjectDetailService projectDetailService;

    @Resource
    private ConsContractDetailService consContractDetailService;

    @ApiOperation(value = "查看项目详情", notes = "查看项目详情", produces = "application/json")
    @PostMapping("/projectDetail")
    public ResponseData projectDetail(@RequestBody ProjectParam projectParam) {
        List<ProjectDetail> projectDetails = projectDetailService.listByProjectId(projectParam.getProjectId());
        if (CollectionUtils.isEmpty(projectDetails)) {
            throw new ServiceException(-1, "找不到项目详情");
        }
        return ResponseData.success(projectDetails);
    }

    @ApiOperation(value = "查看用户签约详情", notes = "查看用户签约详情", produces = "application/json")
    @PostMapping("/consDeclareDetail")
    public ResponseData consDeclareDetail(@RequestBody CustContractParam custContractParam) {
        Map<String, Object> map = projectDetailService.consDeclareDetail(custContractParam);
        return ResponseData.success(map);
    }

    @ApiOperation(value = "查看用户签约项目明细", notes = "查看用户签约项目明细", produces = "application/json")
    @PostMapping("/listContractDetail")
    public ResponseData<?> listContractDetail(@RequestBody ConsContractParam consContractParam) {
        return ResponseData.success(projectDetailService.listContractDetail(consContractParam));
    }

    @ApiOperation(value = "查看用户签约项目明细", notes = "查看用户签约项目明细", produces = "application/json")
    @PostMapping("/listVerifyContractDetail")
    public ResponseData<?> listVerifyContractDetail(@RequestBody ConsContractParam consContractParam) {
        LambdaQueryWrapper<ConsContractDetail> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ConsContractDetail::getContractId, consContractParam.getContractId());
        return ResponseData.success(consContractDetailService.list(lambdaQueryWrapper));
    }
}

