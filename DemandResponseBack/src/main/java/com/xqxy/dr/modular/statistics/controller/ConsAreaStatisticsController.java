package com.xqxy.dr.modular.statistics.controller;


import cn.afterturn.easypoi.entity.vo.NormalExcelConstants;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.view.PoiBaseView;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.core.exception.ServiceException;
import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.adjustable.entity.DrDeviceAdjustablePotential;
import com.xqxy.dr.modular.statistics.VO.ConsAreaStatisticsVO;
import com.xqxy.dr.modular.statistics.entity.EventStatistics;
import com.xqxy.dr.modular.statistics.param.StatisticalParam;
import com.xqxy.dr.modular.statistics.result.EventStatisticsResult;
import com.xqxy.dr.modular.statistics.service.ConsAreaStatisticsService;
import com.xqxy.sys.modular.cust.result.ConsStatisticsResult;
import com.xqxy.sys.modular.cust.result.StatisticsByTypeResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/statistics")
public class ConsAreaStatisticsController {

    @Resource
    private ConsAreaStatisticsService consAreaStatisticsService;

    /**
     *  签约用户的地区统计
     * @return
     */
    @ApiOperation(value = "签约用户的地区统计", notes = "签约用户的地区统计", produces = "application/json")
    @PostMapping("/consAreaStatistics")
    public ResponseData consAreaStatistics(@RequestBody StatisticalParam statisticalParam) {
        if(statisticalParam == null || statisticalParam.getProjectId() == null){
            throw new ServiceException(500,"必传参数为空");
        }
        Map<String,Integer> map = consAreaStatisticsService.consAreaStatistics(statisticalParam.getProjectId());
        return ResponseData.success(map);
    }

    /**
     *  各地市用户类型统计 --- 签约数据
     * @return
     */
    @ApiOperation(value = "各地市用户类型统计 --- 签约数据", notes = "各地市用户类型统计 --- 签约数据", produces = "application/json")
    @PostMapping("/consCityStatistics")
    public ResponseData consCityStatistics(@RequestBody StatisticalParam statisticalParam) {
        if(statisticalParam == null || statisticalParam.getProjectId() == null){
            throw new ServiceException(500,"必传参数为空");
        }
        List<ConsStatisticsResult> list = consAreaStatisticsService.consCityStatistics(statisticalParam.getProjectId());
        return ResponseData.success(list);
    }

    /**
     * 导出 各地市用户类型统计 --- 签约数据
     * @return
     */
    @ApiOperation(value = "导出 各地市用户类型统计 --- 签约数据", notes = "导出 各地市用户类型统计 --- 签约数据", produces = "application/json")
    @PostMapping("/exportConsCityStatistics")
    public void exportConsCityStatistics(@RequestBody StatisticalParam statisticalParam, HttpServletResponse response, HttpServletRequest request) {
        if(statisticalParam == null || statisticalParam.getProjectId() == null){
            throw new ServiceException(500,"必传参数为空");
        }

        List<ConsStatisticsResult> list = consAreaStatisticsService.consCityStatistics(statisticalParam.getProjectId());
        ExportParams params = new ExportParams("各地市用户类型统计---签约数据", "各地市用户类型统计---签约数据", ExcelType.XSSF);
        HashMap<String,Object> map = new HashMap<>();
        map.put(NormalExcelConstants.DATA_LIST, list);
        map.put(NormalExcelConstants.CLASS, ConsStatisticsResult.class);
        map.put(NormalExcelConstants.PARAMS, params);
        map.put(NormalExcelConstants.FILE_NAME, "各地市用户类型统计---签约数据");

        PoiBaseView.render(map, request, response, NormalExcelConstants.EASYPOI_EXCEL_VIEW);
    }

    /**
     *  各地市用户类型统计 --- 注册数据
     * @return
     */
    @ApiOperation(value = "各地市用户类型统计 --- 注册数据", notes = "各地市用户类型统计 --- 注册数据", produces = "application/json")
    @PostMapping("/consCityStatisticsAll")
    public ResponseData consCityStatisticsAll(@RequestBody StatisticalParam statisticalParam) {
        if(statisticalParam == null || statisticalParam.getProjectId() == null){
            throw new ServiceException(500,"必传参数为空");
        }
        List<ConsStatisticsResult> list = consAreaStatisticsService.consCityStatisticsAll(statisticalParam.getProjectId());
        return ResponseData.success(list);
    }



    /**
     * 导出 各地市用户类型统计 --- 签约数据
     * @return
     */
    @ApiOperation(value = "导出 各地市用户类型统计 --- 注册数据", notes = "导出 各地市用户类型统计 --- 注册数据", produces = "application/json")
    @PostMapping("/exportConsCityStatisticsAll")
    public void exportConsCityStatisticsAll(@RequestBody StatisticalParam statisticalParam, HttpServletResponse response, HttpServletRequest request) {
        if(statisticalParam == null || statisticalParam.getProjectId() == null){
            throw new ServiceException(500,"必传参数为空");
        }

        List<ConsStatisticsResult> list = consAreaStatisticsService.consCityStatisticsAll(statisticalParam.getProjectId());
        ExportParams params = new ExportParams("各地市用户类型统计---注册数据", "各地市用户类型统计---注册数据", ExcelType.XSSF);
        HashMap<String,Object> map = new HashMap<>();
        map.put(NormalExcelConstants.DATA_LIST, list);
        map.put(NormalExcelConstants.CLASS, ConsStatisticsResult.class);
        map.put(NormalExcelConstants.PARAMS, params);
        map.put(NormalExcelConstants.FILE_NAME, "各地市用户类型统计---注册数据");

        PoiBaseView.render(map, request, response, NormalExcelConstants.EASYPOI_EXCEL_VIEW);
    }

    /**
     * 统计 每个月用户签约详情 数量
     */
    @ApiOperation(value = "统计 每个月用户签约详情 数量", notes = "统计 每个月用户签约详情 数量", produces = "application/json")
    @PostMapping("/consStatisticsMonthCount")
    public ResponseData consStatisticsMonthCount(@RequestBody StatisticalParam statisticalParam) {
        Map<String,List> map = consAreaStatisticsService.consStatisticsMonthCount(statisticalParam);
        return ResponseData.success(map);
    }

    /**
     * 统计 每个月用户签约详情 签约负荷
     */
    @ApiOperation(value = "统计 每个月用户签约详情 签约负荷", notes = "统计 每个月用户签约详情 签约负荷", produces = "application/json")
    @PostMapping("/consStatisticsCapCount")
    public ResponseData consStatisticsCapCount(@RequestBody StatisticalParam statisticalParam) {
        Map<String,List> map = consAreaStatisticsService.consStatisticsCapCount(statisticalParam);
        return ResponseData.success(map);
    }

    /**
     * 统计 执行效果统计 事件执行统计
     */
    @ApiOperation(value = "统计 执行效果统计 事件执行统计", notes = "统计 执行效果统计 事件执行统计", produces = "application/json")
    @PostMapping("/pageEventStatistics")
    public ResponseData pageEventStatistics(@RequestBody StatisticalParam statisticalParam) {
        Page<EventStatistics> page = consAreaStatisticsService.pageEventStatistics(statisticalParam);
        return ResponseData.success(page);
    }
}
