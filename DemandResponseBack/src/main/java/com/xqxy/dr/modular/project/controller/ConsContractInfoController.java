package com.xqxy.dr.modular.project.controller;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.alibaba.excel.write.metadata.fill.FillWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.project.VO.DrConsContractDetailsVO;
import com.xqxy.dr.modular.project.entity.ConsContractDetail;
import com.xqxy.dr.modular.project.entity.ConsContractInfo;
import com.xqxy.dr.modular.project.params.ConsContractParam;
import com.xqxy.dr.modular.project.params.CustContractParam;
import com.xqxy.dr.modular.project.service.ConsContractInfoService;
import com.xqxy.dr.modular.project.service.CustContractInfoService;
import com.xqxy.sys.modular.cust.param.BusConfigParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户项目申报基本信息 前端控制器
 * </p>
 *
 * @author PengChuqing
 * @since 2021-10-13
 */
@Log4j2
@Api(tags = "用户项目申报API接口")
@RestController
@RequestMapping("/project/cons-contract-info")
public class ConsContractInfoController {
    @Resource
    private ConsContractInfoService consContractInfoService;

    @Resource
    private CustContractInfoService custContractInfoService;

    @ApiOperation(value = "新建签约", notes = "新建签约", produces = "application/json")
    @PostMapping("/addSigning")
    public ResponseData<?> addSigning(@RequestBody ConsContractParam consContractParam) {
        Long contractId = consContractInfoService.addSigning(consContractParam);
        return ResponseData.success(contractId);
    }

    @ApiOperation(value = "提交签约审核", notes = "提交签约审核", produces = "application/json")
    @PostMapping("/submitSigning")
    public ResponseData<?> submitSigning(@RequestBody ConsContractParam consContractParam) {
        consContractInfoService.submitSigning(consContractParam);
        return ResponseData.success();
    }

    /**
     * 查询用户签约明细列表
     *
     * @param consContractParam
     * @return
     */
    @ApiOperation(value = "查询用户签约明细列表", notes = "查询用户签约明细列表", produces = "application/json")
    @PostMapping("/queryDrConsContractDetails")
    public ResponseData queryDrConsDetails(@RequestBody ConsContractParam consContractParam) {
        Page<DrConsContractDetailsVO> page = new Page<>(consContractParam.getCurrent(), consContractParam.getSize());

        List<DrConsContractDetailsVO> list = consContractInfoService.queryDrConsContractDetails(page, consContractParam);
        page.setRecords(list);
        return ResponseData.success(page);
    }

    /**
     * 查询用户签约 详情根据id
     *
     * @param contractId
     * @return
     */
    @ApiOperation(value = "查询用户签约 详情根据id", notes = "查询用户签约 详情根据id", produces = "application/json")
    @GetMapping("/queryDrConsDetailByInfoId")
    public ResponseData queryDrConsDetailByInfoId(@RequestParam("contractId") Long contractId) {
        List<ConsContractDetail> list = consContractInfoService.queryDrConsDetailByInfoId(contractId);
        return ResponseData.success(list);
    }

    // @ApiOperation(value = "查询用户签约详情导出", notes = "查询用户签约详情导出", produces = "application/json")
    // @PostMapping("/exportDrConsDetails")
    // public void exportDrConsDetails(@RequestBody ConsContractParam consContractParam, HttpServletResponse response, HttpServletRequest request) {
    //     List<DrConsContractDetailsVO> list = consContractInfoService.exportDrConsDetails(consContractParam);
    //     ExportParams params = new ExportParams("用户签约详情", "用户签约详情", ExcelType.XSSF);
    //     HashMap<String, Object> map = new HashMap<>();
    //     map.put(NormalExcelConstants.DATA_LIST, list);
    //     map.put(NormalExcelConstants.CLASS, DrConsContractDetailsVO.class);
    //     map.put(NormalExcelConstants.PARAMS, params);
    //     map.put(NormalExcelConstants.FILE_NAME, "用户签约详情");
    //
    //     PoiBaseView.render(map, request, response, NormalExcelConstants.EASYPOI_EXCEL_VIEW);
    // }
    //
    @ApiOperation(value = "查询用户签约详情导出", notes = "查询用户签约详情导出", produces = "application/json")
    @PostMapping("/exportDrConsDetails")
    public void exportDrConsDetails(@RequestBody ConsContractParam consContractParam, HttpServletResponse response, HttpServletRequest request) {
        List<DrConsContractDetailsVO> list = consContractInfoService.exportDrConsDetails(consContractParam);
        try {
            // 读取模板输入流
            InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("static/consDetails_template.xlsx");

            if (resourceAsStream == null) {
                System.out.println("未读取到模板");
            }

            // 设置文件名称
            String fileName = URLEncoder.encode("Cons" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()), "UTF-8");
            // 设置文件类型
            response.setContentType("application/vnd.ms-excel");
            // 设置编码格式
            response.setCharacterEncoding("utf-8");
            // https://www.jb51.net/article/30565.htm Content-Disposition 使用说明
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            // 创建excel
            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).withTemplate(resourceAsStream).build();
            // 创建sheet
            WriteSheet writeSheet = EasyExcel.writerSheet().build();
            FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();

            // 如果有多个list 模板上必须有{前缀.} 这里的前缀就是 a，然后多个list必须用 FillWrapper包裹
            excelWriter.fill(new FillWrapper("a", list), fillConfig, writeSheet);
//
            Map map = new HashMap<>();
            excelWriter.fill(map, writeSheet);

            // 别忘记关闭流
            excelWriter.finish();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @ApiOperation(value = "用户签约", notes = "用户签约", produces = "application/json")
    @PostMapping("/consContract")
    public ResponseData<?> consContract(@RequestBody ConsContractParam consContractParam) {
        ResponseData responseData = consContractInfoService.consContract(consContractParam);
        return responseData;
    }

    @ApiOperation(value = "签约审核", notes = "签约审核", produces = "application/json")
    @PostMapping("/verifySigning")
    public ResponseData<?> verifySigning(@RequestBody @Valid BusConfigParam busConfigParam) {
        consContractInfoService.verifySigning(busConfigParam);
        return ResponseData.success();
    }

    @ApiOperation(value = "签约用户列表", notes = "签约用户列表", produces = "application/json")
    @PostMapping("/listConsContract")
    public ResponseData<?> listConsContract(@RequestBody CustContractParam custContractParam) {
        Page<ConsContractInfo> consContractInfoPage = consContractInfoService.listConsContract(custContractParam);
        return ResponseData.success(consContractInfoPage);
    }

    @ApiOperation(value = "审核签约列表", notes = "审核签约列表", produces = "application/json")
    @PostMapping("/listVerifyContract")
    public ResponseData<?> listVerifyContract(@RequestBody BusConfigParam busConfigParam) {
        Page<ConsContractInfo> consContractInfoPage = consContractInfoService.listVerifyContract(busConfigParam);
        return ResponseData.success(consContractInfoPage);
    }

    @ApiOperation(value = "展示项目签约容量", notes = "展示项目签约容量", produces = "application/json")
    @PostMapping("/listContractCap")
    public ResponseData<?> listContractCap(@RequestBody CustContractParam custContractParam) {
        return ResponseData.success(consContractInfoService.listContractCap(custContractParam));
    }

    @ApiOperation(value = "营销用户查看集成商代理的用户签约详情", notes = "营销用户查看集成商代理的用户签约详情", produces = "application/json")
    @PostMapping("/pageProxyContract")
    public ResponseData<?> pageProxyContract(@RequestBody CustContractParam custContractParam) {
        return ResponseData.success(consContractInfoService.pageProxyContract(custContractParam));
    }

    @GetMapping("consContract/templateExport")
    public void exportExcelTemplate(@RequestParam String projectId, HttpServletRequest request, HttpServletResponse response) {
        consContractInfoService.exportConsContractTemplate(request, response, projectId);
    }

    @PostMapping("consContract/importByExcel")
    public ResponseData importFromExcel(@RequestParam(required = false) String pageProjectId, MultipartFile file) throws Exception {
        Long projectId = consContractInfoService.importConsContractByExcel(pageProjectId, file);
        return ResponseData.success(projectId);
    }

    @ApiOperation(value = "签约清单详情", notes = "签约清单详情", produces = "application/json")
    @PostMapping("/pageContractDetail")
    public ResponseData<?> pageContractDetail(@RequestBody CustContractParam custContractParam) {
        return ResponseData.success(consContractInfoService.pageContractDetail(custContractParam));
    }
}

