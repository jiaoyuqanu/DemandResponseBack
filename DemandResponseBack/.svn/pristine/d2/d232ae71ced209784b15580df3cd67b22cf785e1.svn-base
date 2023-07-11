package com.xqxy.dr.modular.subsidy.controller;

import cn.afterturn.easypoi.entity.vo.NormalExcelConstants;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.view.PoiBaseView;
import com.xqxy.core.annotion.BusinessLog;
import com.xqxy.core.enums.LogAnnotionOpTypeEnum;
import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.adjustable.entity.DrDeviceAdjustablePotential;
import com.xqxy.dr.modular.subsidy.entity.CustSubsidy;
import com.xqxy.dr.modular.subsidy.model.CustSubsidyModel;
import com.xqxy.dr.modular.subsidy.param.CustSubsidyParam;
import com.xqxy.dr.modular.subsidy.service.CustSubsidyService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import com.xqxy.core.annotion.BusinessLog;
import com.xqxy.core.enums.LogAnnotionOpTypeEnum;
import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.subsidy.param.CustSubsidyDailyParam;
import com.xqxy.dr.modular.subsidy.service.CustSubsidyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 客户事件激励费用 前端控制器
 * </p>
 *
 * @author hu xingxing
 * @since 2021-10-20
 */
@Api(tags = "客户事件补贴API接口")
@RestController
@RequestMapping("/subsidy/cust-subsidy")
public class CustSubsidyController {


    @Resource
    CustSubsidyService custSubsidyService;

//    @BusinessLog(title = "客户事件补贴分页查询", opType = LogAnnotionOpTypeEnum.QUERY)
    @ApiOperation(value = "客户事件补贴分页查询", notes = "客户事件补贴分页查询", produces = "application/json")
    @PostMapping("/page")
    public ResponseData page(@RequestBody CustSubsidyDailyParam custSubsidyDailyParam) {
        return ResponseData.success(custSubsidyService.page(custSubsidyDailyParam));
    }

    /**
     * 根据事件标识进行客户事件补贴分页查询
     * @param custSubsidyParam
     * @return
     * @author hu xingxing
     * @date 2021-10-26 11:20
     */
    //@BusinessLog(title = "根据事件标识进行客户事件补贴分页查询", opType = LogAnnotionOpTypeEnum.QUERY)
    @ApiOperation(value = "根据事件标识进行客户事件补贴分页查询", notes = "根据事件标识进行客户事件补贴分页查询", produces = "application/json")
    @PostMapping("/pageCustByEventId")
    public ResponseData pageCustByEventId(@RequestBody CustSubsidyParam custSubsidyParam) {

        return ResponseData.success(custSubsidyService.pageCustByEventId(custSubsidyParam));
    }

    /**
     * 导出 根据事件标识进行客户事件补贴分页查询
     * @param custSubsidyParam
     * @return
     */
    @ApiOperation(value = "导出 根据事件标识进行客户事件补贴分页查询", notes = "导出 根据事件标识进行客户事件补贴分页查询", produces = "application/json")
    @PostMapping("/exportCustByEventId")
    public void exportCustByEventId(@RequestBody CustSubsidyParam custSubsidyParam, HttpServletResponse response, HttpServletRequest request) {
        List<CustSubsidyModel> list = custSubsidyService.exportCustByEventId(custSubsidyParam);

        ExportParams params = new ExportParams("客户事件补贴--导出", "客户事件补贴--导出", ExcelType.XSSF);
        HashMap<String,Object> map = new HashMap<>();
        map.put(NormalExcelConstants.DATA_LIST, list);
        map.put(NormalExcelConstants.CLASS, CustSubsidyModel.class);
        map.put(NormalExcelConstants.PARAMS, params);
        map.put(NormalExcelConstants.FILE_NAME, "客户事件补贴--导出");

        PoiBaseView.render(map, request, response, NormalExcelConstants.EASYPOI_EXCEL_VIEW);
    }

}

