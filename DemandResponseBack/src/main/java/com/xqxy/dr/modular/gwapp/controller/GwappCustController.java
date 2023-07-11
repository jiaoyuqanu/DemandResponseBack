package com.xqxy.dr.modular.gwapp.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.gwapp.entity.GwappCons;
import com.xqxy.dr.modular.gwapp.param.GwappCustAuditParam;
import com.xqxy.dr.modular.gwapp.param.GwappCustPageQuery;
import com.xqxy.dr.modular.gwapp.param.GwappRegistry;
import com.xqxy.dr.modular.gwapp.param.GwappInfoParam;
import com.xqxy.dr.modular.gwapp.service.IGwappConsService;
import com.xqxy.dr.modular.gwapp.service.IGwappCustService;
import com.xqxy.dr.modular.gwapp.vo.GwappCustInfoVo;
import com.xqxy.dr.modular.gwapp.vo.GwappCustPageVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 客户(对应注册用户) 前端控制器
 * </p>
 *
 * @author Yechs
 * @since 2022-05-20
 */
@Api(tags = "国网app对接用户")
@RestController
@RequestMapping("/gwapp/cust")
public class GwappCustController {

    @Resource
    private IGwappCustService gwappCustService;
    @Resource
    private IGwappConsService gwappConsService;

    @PostMapping("registry")
    public ResponseData register(@RequestBody GwappRegistry gwappRegistry) {
        gwappCustService.gwappRegistry(gwappRegistry);
        return ResponseData.success();
    }

    @PostMapping("auditInfo")
    public ResponseData auditInfo(@RequestBody @Validated GwappInfoParam gwappInfoParam) {
        return ResponseData.success(gwappCustService.getInfoByTel(gwappInfoParam.getTel()));
    }


    @ApiOperation(value = "获取国网对接用户审核列表", notes = "获取国网对接用户审核列表", produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping("auditPage")
    public ResponseData<Page<GwappCustPageVo>> getAuditPage(@RequestBody @Validated GwappCustPageQuery gwappCustPageQuery) {
        return ResponseData.success(gwappCustService.queryPage(gwappCustPageQuery));
    }

    @ApiOperation(value = "获取国网对接用户详情", notes = "获取国网对接用户详情", produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping("{custId}")
    public ResponseData<GwappCustInfoVo> getCustInfoById(@PathVariable @NotNull(message = "用户id不能为空") Long custId) {
        return ResponseData.success(gwappCustService.getGwAppCustInfo(custId));
    }

    @ApiOperation(value = "获取国网对接用户详情", notes = "获取国网对接用户详情", produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping("{custId}/cons/list")
    public ResponseData<List<GwappCons>> getConsListByCustId(@PathVariable @NotNull(message = "用户id不能为空") Long custId) {
        return ResponseData.success(gwappConsService.getConsByCustId(custId));
    }

    @ApiOperation(value = "电力用户校核", notes = "电力用户校核", produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping("{custId}/cons/check")
    public ResponseData<List<GwappCons>> checkConsList(@PathVariable @NotNull(message = "用户id不能为空") Long custId) {
        return ResponseData.success(gwappConsService.checkCons(custId));
    }

    @ApiOperation(value = "国网对接用户审核", notes = "国网对接用户审核", produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping("{custId}/audit")
    public ResponseData auditGwappCust(@PathVariable String custId, @RequestBody @Validated GwappCustAuditParam gwappCustAuditParam) {
        gwappCustService.auditGwappCust(Long.valueOf(custId), gwappCustAuditParam);
        return ResponseData.success();
    }

}
