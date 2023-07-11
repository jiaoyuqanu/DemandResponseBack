package com.xqxy.dr.modular.statistics.controller;


import cn.afterturn.easypoi.entity.vo.NormalExcelConstants;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.view.PoiBaseView;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.xqxy.core.client.SystemClient;
import com.xqxy.core.client.SystemClientService;
import com.xqxy.core.exception.ServiceException;
import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.event.param.EventParam;
import com.xqxy.dr.modular.project.enums.PlanExceptionEnum;
import com.xqxy.dr.modular.project.result.Region;
import com.xqxy.dr.modular.statistics.entity.LoadStatistic;
import com.xqxy.dr.modular.statistics.service.LoadStatisticService;
import com.xqxy.sys.modular.cust.entity.Cons;
import com.xqxy.sys.modular.cust.param.ConsParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author shi
 * @since 2022-02-22
 */
@RestController
@RequestMapping("/statistics/load-statistic")
public class LoadStatisticController {

    @Resource
    LoadStatisticService loadStatisticService;

    @Resource
    SystemClientService systemClientService;

    /**
     * @description: 负荷监测日报查询
     * @param:
     * @return:
     * @author: shi
     * @date: 2022/2/22 14:42
     */
    @ApiOperation(value = "负荷监测日报查询", notes = "负荷监测日报查询", produces = "application/json")
    @PostMapping("/page")
    public ResponseData page(@RequestBody EventParam eventParam) {
        return  ResponseData.success(loadStatisticService.page(eventParam));
    }

    /**
     * @description: 负荷监测日报导出
     * @param:
     * @return:
     * @author: shi
     * @date: 2022/2/22 14:42
     */
    @ApiOperation(value = "负荷监测日报导出", notes = "负荷监测日报导出", produces = "application/json")
    @PostMapping("/export")
    public ResponseData exportCons(@RequestBody EventParam eventParam, HttpServletResponse response, HttpServletRequest request) {
        List<LoadStatistic> list = loadStatisticService.list(eventParam);
        JSONObject result = systemClientService.queryAllOrg();
        JSONArray data = null;
        if(null!=result) {
            data = result.getJSONArray("data");
        } else {
            throw new ServiceException(PlanExceptionEnum.NO_ORG_INFO);
        }
//        Map<String, String> collect = regions.stream().collect(Collectors.toMap(Region::getId, Region::getName));
        for(LoadStatistic loadStatistic:list) {
           String successRate= loadStatistic.getSuccessRate();
           Integer rate=  Integer.valueOf(successRate.split("\\.")[0]);
            loadStatistic.setSuccessRate(String.valueOf(rate*100)+"%");
            for(Object object:data)
            {
                JSONObject jsonObject=(JSONObject)JSONObject.toJSON(object);
                if(String.valueOf(jsonObject.get("id")).equals(loadStatistic.getOrgNo()))
                {
                    loadStatistic.setOrgNo(String.valueOf(jsonObject.get("name")));
                }
            }
        }

        String time = "";
        if(eventParam.getRegulateDate() != null){
            time = eventParam.getRegulateDate().toString();
        }else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            time = sdf.format(new Date());
        }

        ExportParams params = new ExportParams("负荷监测日报", "负荷监测日报", ExcelType.XSSF);
        HashMap<String,Object> map = new HashMap<>();
        map.put(NormalExcelConstants.DATA_LIST, list);
        map.put(NormalExcelConstants.CLASS, LoadStatistic.class);
        map.put(NormalExcelConstants.PARAMS, params);
        map.put(NormalExcelConstants.FILE_NAME, "监测日报-"+time);

        PoiBaseView.render(map, request, response, NormalExcelConstants.EASYPOI_EXCEL_VIEW);

        return ResponseData.success();
    }

}

