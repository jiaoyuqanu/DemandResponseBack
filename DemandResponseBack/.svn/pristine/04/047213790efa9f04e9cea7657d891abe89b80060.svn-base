package com.xqxy.dr.modular.project.controller;


import cn.afterturn.easypoi.entity.vo.NormalExcelConstants;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.view.PoiBaseView;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.core.util.SecurityUtils;
import com.xqxy.dr.modular.project.VO.DrConsContractDetailsVO;
import com.xqxy.dr.modular.project.entity.CustContractInfo;
import com.xqxy.dr.modular.project.params.ConsContractParam;
import com.xqxy.dr.modular.project.params.CustContractParam;
import com.xqxy.dr.modular.project.params.ProjectParam;
import com.xqxy.dr.modular.project.service.CustContractInfoService;
import com.xqxy.sys.modular.cust.entity.Cust;
import com.xqxy.sys.modular.cust.enums.CustStatusEnum;
import com.xqxy.sys.modular.cust.service.CustService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 客户项目申报基本信息 前端控制器
 * </p>
 *
 * @author PengChuqing
 * @since 2021-10-13
 */
@RestController
@RequestMapping("/project/cust-contract-info")
public class CustContractInfoController {

    @Autowired
    private CustContractInfoService custContractInfoService;

    @Autowired
    private CustService custService;

    @ApiOperation(value = "查询客户签约详情", notes = "查询客户签约详情", produces = "application/json")
    @PostMapping("/queryDrCustContractDetails")
    public ResponseData queryDrConsDetails(@RequestBody ConsContractParam consContractParam) {
        Page<DrConsContractDetailsVO> page = new Page<>(consContractParam.getCurrent(),consContractParam.getSize());
        List<DrConsContractDetailsVO> list = custContractInfoService.queryDrCustContractDetails(page,consContractParam);

        page.setRecords(list);
        return ResponseData.success(page);
    }

    @ApiOperation(value = "查询客户签约详情导出", notes = "查询客户签约详情导出", produces = "application/json")
    @PostMapping("/exportDrCustDetails")
    public void exportDrConsDetails(@RequestBody ConsContractParam consContractParam, HttpServletResponse response, HttpServletRequest request) {
        List<DrConsContractDetailsVO> list = custContractInfoService.exportDrConsDetails(consContractParam);

        ExportParams params = new ExportParams("客户签约详情", "客户签约详情", ExcelType.XSSF);
        HashMap<String,Object> map = new HashMap<>();
        map.put(NormalExcelConstants.DATA_LIST, list);
        map.put(NormalExcelConstants.CLASS, DrConsContractDetailsVO.class);
        map.put(NormalExcelConstants.PARAMS, params);
        map.put(NormalExcelConstants.FILE_NAME, "客户签约详情");

        PoiBaseView.render(map, request, response, NormalExcelConstants.EASYPOI_EXCEL_VIEW);

    }

    @ApiOperation(value = "客户签约列表", notes = "客户签约列表", produces = "application/json")
    @PostMapping("/pageDeclareProject")
    public ResponseData<?> pageDeclareProject(@RequestBody ProjectParam projectParam) {
        Page<CustContractInfo> custContractInfoPage = custContractInfoService.pageDeclareProject(projectParam);
        return ResponseData.success(custContractInfoPage);
    }

    @ApiOperation(value = "删除客户签约", notes = "删除客户签约", produces = "application/json")
    @PostMapping("/deleteCustContract")
    public ResponseData<?> deleteCustContract(@RequestBody CustContractParam custContractParam) {
        custContractInfoService.deleteCustContract(custContractParam);
        return ResponseData.success();
    }

    @ApiOperation(value = "删除用户签约", notes = "删除用户签约", produces = "application/json")
    @PostMapping("/deleteConsContract")
    public ResponseData<?> deleteConsContract(@RequestBody ConsContractParam consContractParam) {
        custContractInfoService.deleteConsContract(consContractParam);
        return ResponseData.success();
    }

    @ApiOperation(value = "删除签约明细", notes = "删除签约明细", produces = "application/json")
    @PostMapping("/deleteContractDetail")
    public ResponseData<?> deleteContractDetail(@RequestBody ConsContractParam consContractParam) {
        custContractInfoService.deleteContractDetail(consContractParam);
        return ResponseData.success();
    }

    @ApiOperation(value = "签约撤销", notes = "签约撤销", produces = "application/json")
    @PostMapping("/recallSigning")
    public ResponseData<?> recallSigning(@RequestBody CustContractParam custContractParam) {
        custContractInfoService.recallSigning(custContractParam.getContractId());
        return ResponseData.success();
    }
}