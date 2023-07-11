package com.xqxy.dr.modular.event.controller;


import cn.afterturn.easypoi.entity.vo.NormalExcelConstants;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.view.PoiBaseView;
import com.xqxy.core.annotion.BusinessLog;
import com.xqxy.core.enums.LogAnnotionOpTypeEnum;
import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.event.VO.ConsBaseLineExcelVo;
import com.xqxy.dr.modular.event.entity.ConsExecuteStatistic;
import com.xqxy.dr.modular.event.param.ConsBaselineParam;
import com.xqxy.dr.modular.event.service.ConsBaselineAllService;
import com.xqxy.dr.modular.event.service.ConsBaselineService;
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
 * 前端控制器
 * </p>
 *
 * @author PengChuqing
 * @since 2021-10-18
 */
@Api(tags = "用户基线API接口")
@RestController
@RequestMapping("/event/cons-baseline")
public class ConsBaselineController {

    @Resource
    private ConsBaselineService consBaselineService;
    @Resource
    private ConsBaselineAllService consBaselineAllService;

    @BusinessLog(title = "执行监测——用户基线明细", opType = LogAnnotionOpTypeEnum.ADD)
    @ApiOperation(value = "执行监测——用户基线明细", notes = "执行监测——用户基线明细", produces = "application/json")
    @PostMapping("/getConsBaseByEventId")
    public ResponseData getConsBaseByEventId(@RequestParam(name = "eventId", required = true) Long eventId, @RequestParam(name = "consId", required = true) String consId) {
        return ResponseData.success(consBaselineService.getBaselineAllByEventId(eventId, consId));
    }

    @ApiOperation(value = "用户基线——基线库导出", notes = "执行监测——基线库导出")
    @PostMapping("exportBaseLine")
    public void exportBaseLine(HttpServletRequest request, HttpServletResponse response, @RequestBody ConsBaselineParam consBaselineParam) {
        List<ConsBaseLineExcelVo> consBaseLineExcelVos = consBaselineService.exportListToExcel(consBaselineParam);

        HashMap<String, Object> map = new HashMap<>();
        map.put(NormalExcelConstants.FILE_NAME, "基线库导出");
//        map.put(NormalExcelConstants.MAP_LIST, sheetList);
        map.put(NormalExcelConstants.CLASS, ConsBaseLineExcelVo.class);
        map.put(NormalExcelConstants.PARAMS, new ExportParams("基线库导出", "基线库导出", ExcelType.XSSF));
        map.put(NormalExcelConstants.DATA_LIST, consBaseLineExcelVos);
        PoiBaseView.render(map, request, response, NormalExcelConstants.EASYPOI_EXCEL_VIEW);
    }

    @ApiOperation(value = "用户基线——基线库导出", notes = "执行监测——基线库导出")
    @PostMapping("exportBaseLineAll")
    public void exportBaseLineAll(HttpServletRequest request, HttpServletResponse response, @RequestBody ConsBaselineParam consBaselineParam) {
        List<ConsBaseLineExcelVo> consBaseLineExcelVos = consBaselineAllService.exportListToExcel(consBaselineParam);

        HashMap<String, Object> map = new HashMap<>();
        map.put(NormalExcelConstants.FILE_NAME, "基线库导出");
//        map.put(NormalExcelConstants.MAP_LIST, sheetList);
        map.put(NormalExcelConstants.CLASS, ConsBaseLineExcelVo.class);
        map.put(NormalExcelConstants.PARAMS, new ExportParams("基线库导出", "基线库导出", ExcelType.XSSF));
        map.put(NormalExcelConstants.DATA_LIST, consBaseLineExcelVos);
        PoiBaseView.render(map, request, response, NormalExcelConstants.EASYPOI_EXCEL_VIEW);
    }

}

