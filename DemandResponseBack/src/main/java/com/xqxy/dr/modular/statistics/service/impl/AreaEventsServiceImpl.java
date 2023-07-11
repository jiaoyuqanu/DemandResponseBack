package com.xqxy.dr.modular.statistics.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.core.poi.POIExcelUtil;
import com.xqxy.core.util.HttpServletUtil;
import com.xqxy.dr.modular.statistics.mapper.AreaEventsMapper;
import com.xqxy.dr.modular.statistics.param.StatisticalParam;
import com.xqxy.dr.modular.statistics.result.AreaProjectResult;
import com.xqxy.dr.modular.statistics.service.AreaEventsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AreaEventsServiceImpl implements AreaEventsService {

    @Resource
    private AreaEventsMapper areaEventsMapper;

    /**
     *  地区事件执行效果统计
     * @return
     */
    @Override
    public Page<AreaProjectResult> areaEventsStatistics(StatisticalParam statisticalParam) {

        Page<AreaProjectResult> areaProjects = areaEventsMapper.areaEventsStatistics(statisticalParam.getPage());
        return areaProjects;
    }

    @Override
    public void exportRegion() {

        List<AreaProjectResult> regions = areaEventsMapper.exportRegion();
        String excelName = "地区事件执行效果统计.xls";
        String titleName = excelName.split("\\.")[0];
        String sheetName = titleName;
        String[] titleRow = {"序号", "事件编号", "事件名称", "目标容量(kW)", "实际响应负荷(kW)", "实际响应电量(kWh)", "有效响应用户数量(单位：户)", "达标率"};
        String[][] dataRows = new String[regions.size()][titleRow.length];
        int index = 1;
        for (AreaProjectResult region : regions) {
            dataRows[index-1][0] = String.valueOf(index);
            dataRows[index-1][1] = String.valueOf(region.getEventNo());
            dataRows[index-1][2] = String.valueOf(region.getEventName());
            dataRows[index-1][3] = String.valueOf(region.getRegulateCap());
            dataRows[index-1][4] = String.valueOf(region.getActualCap());
            dataRows[index-1][5] = String.valueOf(region.getActualEnergy());
            dataRows[index-1][6] = String.valueOf(region.getConsNum());
            dataRows[index-1][7] = region.getComplateRate();
            index += 1;
        }

        POIExcelUtil.generatorExcel(excelName, titleName, sheetName, titleRow, dataRows, HttpServletUtil.getResponse());
    }
}
