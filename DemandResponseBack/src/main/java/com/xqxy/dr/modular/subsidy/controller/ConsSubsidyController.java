package com.xqxy.dr.modular.subsidy.controller;


import cn.afterturn.easypoi.entity.vo.NormalExcelConstants;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.view.PoiBaseView;
import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.subsidy.model.ConsSubsidyModel;
import com.xqxy.dr.modular.subsidy.param.ConsSubsidyDailyParam;
import com.xqxy.dr.modular.subsidy.param.ConsSubsidyParam;
import com.xqxy.dr.modular.subsidy.service.ConsSubsidyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 用户事件激励费用 前端控制器
 * </p>
 *
 * @author hu xingxing
 * @since 2021-10-20
 */
@Api(tags = "事件补贴API接口")
@RestController
@RequestMapping("/subsidy/cons-subsidy")
public class ConsSubsidyController {

    @Resource
    ConsSubsidyService consSubsidyService;

    /**
     * 根据事件标识和用户标识事件补贴分页查询
     * @param consSubsidyDailyParam
     * @return
     * @author shen
     * @date 2021-10-21 16:58
     */
//    @BusinessLog(title = "事件补贴分页查询", opType = LogAnnotionOpTypeEnum.QUERY)
    @ApiOperation(value = "事件补贴分页查询", notes = "事件补贴分页查询", produces = "application/json")
    @PostMapping("/page")
    public ResponseData page(@RequestBody ConsSubsidyDailyParam consSubsidyDailyParam) {
        return ResponseData.success(consSubsidyService.page(consSubsidyDailyParam));
    }

    @ApiOperation(value = "用户日补贴详情", notes = "用户日补贴详情", produces = "application/json")
    @PostMapping("/dailySubsidyConsDetail")
    public ResponseData dailySubsidyConsDetail(@RequestBody ConsSubsidyDailyParam consSubsidyDailyParam) {
        return ResponseData.success(consSubsidyService.dailySubsidyConsDetail(consSubsidyDailyParam));
    }

    @ApiOperation(value = "客户日补贴详情", notes = "用客户日补贴详情", produces = "application/json")
    @PostMapping("/dailySubsidyCustDetail")
    public ResponseData dailySubsidyCustDetail(@RequestBody ConsSubsidyDailyParam consSubsidyDailyParam) {
        return ResponseData.success(consSubsidyService.dailySubsidyCustDetail(consSubsidyDailyParam));
    }

    @ApiOperation(value = "用户日补贴总计", notes = "用户日补贴总计", produces = "application/json")
    @PostMapping("/dailySubsidyConsTotal")
    public ResponseData dailySubsidyConsTotal(@RequestBody ConsSubsidyDailyParam consSubsidyDailyParam) {
        return ResponseData.success(consSubsidyService.dailySubsidyConsTotal(consSubsidyDailyParam));
    }

    @ApiOperation(value = "客户日补贴总计", notes = "客户日补贴总计", produces = "application/json")
    @PostMapping("/dailySubsidyCustTotal")
    public ResponseData dailySubsidyCustTotal(@RequestBody ConsSubsidyDailyParam consSubsidyDailyParam) {
        return ResponseData.success(consSubsidyService.dailySubsidyCustTotal(consSubsidyDailyParam));
    }

    /**
     * 根据事件标识进行用户事件补贴分页查询
     * @param consSubsidyParam
     * @return
     * @author hu xingxing
     * @date 2021-10-26 11:00
     */
    //@BusinessLog(title = "根据事件标识进行用户事件补贴分页查询", opType = LogAnnotionOpTypeEnum.QUERY)
    @ApiOperation(value = "根据事件标识进行用户事件补贴分页查询", notes = "根据事件标识进行用户事件补贴分页查询", produces = "application/json")
    @PostMapping("/pageConsByEventId")
    public ResponseData pageConsByEventId(@RequestBody ConsSubsidyParam consSubsidyParam) {

        return ResponseData.success(consSubsidyService.pageConsByEventId(consSubsidyParam));
    }

    /**
     * 导出 -- 根据事件标识进行用户事件补贴分页查询
     * @param consSubsidyParam
     * @return
     * @author lqr
     * @date 2022-6-18 11:00
     */
    @ApiOperation(value = "导出 -- 根据事件标识进行用户事件补贴分页查询", notes = "导出 -- 根据事件标识进行用户事件补贴分页查询", produces = "application/json")
    @PostMapping("/exportConsByEventId")
    public void exportConsByEventId(@RequestBody ConsSubsidyParam consSubsidyParam, HttpServletResponse response, HttpServletRequest request) {
        List<ConsSubsidyModel> list = consSubsidyService.exportConsByEventId(consSubsidyParam);

        ExportParams params = new ExportParams("电力用户事件补贴--导出", "电力用户事件补贴--导出", ExcelType.XSSF);
        HashMap<String,Object> map = new HashMap<>();
        map.put(NormalExcelConstants.DATA_LIST, list);
        map.put(NormalExcelConstants.CLASS, ConsSubsidyModel.class);
        map.put(NormalExcelConstants.PARAMS, params);
        map.put(NormalExcelConstants.FILE_NAME, "电力用户事件补贴--导出");

        PoiBaseView.render(map, request, response, NormalExcelConstants.EASYPOI_EXCEL_VIEW);
    }

    /**
     * 根据事件标识和客户标识进行代理用户事件补贴分页查询
     * @param consSubsidyParam
     * @return
     * @author hu xingxing
     * @date 2021-10-26 14:00
     */
    //@BusinessLog(title = "根据事件标识和客户标识进行代理用户事件补贴分页查询", opType = LogAnnotionOpTypeEnum.QUERY)
    @ApiOperation(value = "根据事件标识和客户标识进行代理用户事件补贴分页查询", notes = "根据事件标识和客户标识进行代理用户事件补贴分页查询", produces = "application/json")
    @PostMapping("/pageConsByEventIdAndCustId")
    public ResponseData pageConsByEventIdAndCustId(@RequestBody ConsSubsidyParam consSubsidyParam) {

        return ResponseData.success(consSubsidyService.pageConsByEventIdAndCustId(consSubsidyParam));
    }

    /**
     * 我的补贴--户号补贴
     * @param consSubsidyParam
     * @return
     * @author hu xingxing
     * @date 2021-10-26 14:00
     */
    @ApiOperation(value = "我的补贴--户号补贴", notes = "我的补贴--户号补贴", produces = "application/json")
    @PostMapping("/consNoSubsidy")
    public ResponseData consNoSubsidy(@RequestBody ConsSubsidyParam consSubsidyParam) {

        return ResponseData.success(consSubsidyService.consNoSubsidy(consSubsidyParam));
    }


    /**
     * 我的补贴--户号补贴统计
     * @param consSubsidyParam
     * @return
     * @author hu xingxing
     * @date 2021-10-26 14:00
     */
    @ApiOperation(value = "我的补贴--户号补贴", notes = "我的补贴--户号补贴", produces = "application/json")
    @PostMapping("/consNoSubsidyInfo")
    public ResponseData consNoSubsidyInfo(@RequestBody ConsSubsidyParam consSubsidyParam) {
        return ResponseData.success(consSubsidyService.consNoSubsidyInfo(consSubsidyParam));
    }
}

