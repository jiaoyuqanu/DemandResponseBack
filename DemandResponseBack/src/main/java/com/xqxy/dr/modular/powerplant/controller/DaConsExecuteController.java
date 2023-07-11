package com.xqxy.dr.modular.powerplant.controller;


import cn.afterturn.easypoi.entity.vo.NormalExcelConstants;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.view.PoiBaseView;
import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.powerplant.entity.DaConsExecute;
import com.xqxy.dr.modular.powerplant.param.DaConsExecuteParam;
import com.xqxy.dr.modular.powerplant.param.DaParam;
import com.xqxy.dr.modular.powerplant.service.DaConsExecuteService;
import com.xqxy.dr.modular.project.result.Region;
import com.xqxy.sys.modular.cust.entity.Cons;
import com.xqxy.sys.modular.cust.param.ConsParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author shi
 * @since 2021-12-15
 */
@RestController
@RequestMapping("/powerplant/da-cons-execute")
public class DaConsExecuteController {

    @Resource
    DaConsExecuteService daConsExecuteService;
    @ApiOperation(value = "单用户执行情况分页", notes = "单用户执行情况分页")
    @PostMapping("/page")
    public ResponseData page(@RequestBody DaConsExecuteParam daConsExecuteParam) {
        return ResponseData.success(daConsExecuteService.page(daConsExecuteParam));
    }

    /**
     * 辅助服务执行客户清单信息 导出
     * @param daConsExecuteParam
     * @return
     */
    @ApiOperation(value = "辅助服务执行客户清单信息导出", notes = "辅助服务执行客户清单信息导出", produces = "application/json")
    @PostMapping("/export")
    public ResponseData exportCons(DaConsExecuteParam daConsExecuteParam, HttpServletResponse response, HttpServletRequest request) {
        List<DaConsExecute> list = daConsExecuteService.list(daConsExecuteParam);
//        List<Region> regions = systemClient.queryAll();
//        Map<String, String> collect = regions.stream().collect(Collectors.toMap(Region::getId, Region::getName));
//
//        for (Cons cons : list) {
//            String provinceName= collect.get(cons.getProvinceCode());
//            String cityName= collect.get(cons.getCityCode());
//            String countyName= collect.get(cons.getCountyCode());
//            cons.setProvinceCode(provinceName);
//            cons.setCityCode(cityName);
//            cons.setCountyCode(countyName);
//        }
        ExportParams params = new ExportParams("辅助服务执行客户清单信息", "辅助服务执行客户清单信息", ExcelType.XSSF);
        HashMap<String,Object> map = new HashMap<>();
        map.put(NormalExcelConstants.DATA_LIST, list);
        map.put(NormalExcelConstants.CLASS, DaConsExecute.class);
        map.put(NormalExcelConstants.PARAMS, params);
        map.put(NormalExcelConstants.FILE_NAME, "辅助服务执行客户清单信息");

        PoiBaseView.render(map, request, response, NormalExcelConstants.EASYPOI_EXCEL_VIEW);

        return ResponseData.success();
    }

    /**
     * 区域执行情况统计
     * @param orgId
     * @return
     */
    @ApiOperation(value = "区域执行情况统计", notes = "区域执行情况统计", produces = "application/json")
    @PostMapping("/regionExecuteStatistics")
    public ResponseData exportCons(@RequestParam(name="orgId",required = false) String orgId) {

        return ResponseData.success(daConsExecuteService.regionExecuteStatistics(orgId));
    }
}

