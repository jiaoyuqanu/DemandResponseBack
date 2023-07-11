package com.xqxy.dr.modular.statistics.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.core.poi.POIExcelUtil;
import com.xqxy.core.util.ConsOrgUtils;
import com.xqxy.core.util.HttpServletUtil;
import com.xqxy.dr.modular.statistics.mapper.DrYearMapper;
import com.xqxy.dr.modular.statistics.param.StatisticalParam;
import com.xqxy.dr.modular.statistics.result.DrYearResult;
import com.xqxy.dr.modular.statistics.result.ProjectEffectResult;
import com.xqxy.dr.modular.statistics.service.DrYearService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DrYearServiceImpl implements DrYearService {

    @Resource
    private DrYearMapper drYearMapper;

    /**
     * 用户需求响应年度统计
     *
     * @return
     */
    @Override
    public Page<DrYearResult> drYearStatistics(StatisticalParam statisticalParam) {

        Page<DrYearResult> drYears = drYearMapper.drYearStatistics(statisticalParam.getPage(), statisticalParam);
        return drYears;
    }

    /**
     * 用户需求响应年度统计导出
     *
     * @return
     */
    @Override
    public void exportYear(StatisticalParam statisticalParam) {

        List<DrYearResult> drYears = drYearMapper.exportYear(statisticalParam);
        Map<String, Map<String, String>> cityOrg = ConsOrgUtils.getInstance().getCityOrg(drYears, DrYearResult::getConsId);
        String excelName = "用户需求响应年度统计.xls";
        String titleName = excelName.split("\\.")[0];
        String sheetName = titleName;
        String[] titleRow = {"序号", "年份", "市供电单位", "区县供电单位", "供电单位", "电力户号", "用户名称", "邀约次数", "参与次数", "到位次数", "最大响应负荷(kW)", "最大响应电量(kWh)"};
        String[][] dataRows = new String[drYears.size()][titleRow.length];
        int index = 1;
        for (DrYearResult drYear : drYears) {
            Map<String, String> orgMap = cityOrg.getOrDefault(drYear.getConsId(), new HashMap<>());
            Integer indexY = 0;
            dataRows[index - 1][indexY++] = String.valueOf(index);
            dataRows[index - 1][indexY++] = drYear.getRegulateDate();
//            dataRows[index - 1][2] = orgMap.getOrDefault(ConsOrgUtils.PROVINCE, "");
            dataRows[index - 1][indexY++] = orgMap.getOrDefault(ConsOrgUtils.CITY, "");
            dataRows[index - 1][indexY++] = orgMap.getOrDefault(ConsOrgUtils.AREA, "");
            dataRows[index - 1][indexY++] = orgMap.getOrDefault(ConsOrgUtils.ORG, "");
            dataRows[index - 1][indexY++] = drYear.getConsId();
            dataRows[index - 1][indexY++] = drYear.getConsName();
            dataRows[index - 1][indexY++] = String.valueOf(drYear.getInvitation());
            dataRows[index - 1][indexY++] = String.valueOf(drYear.getParticipate());
            dataRows[index - 1][indexY++] = String.valueOf(drYear.getEffective());
            dataRows[index - 1][indexY++] = String.valueOf(drYear.getActualCap());
            dataRows[index - 1][indexY++] = String.valueOf(drYear.getActualEnergy());
            index += 1;
        }

        POIExcelUtil.generatorExcel(excelName, titleName, sheetName, titleRow, dataRows, HttpServletUtil.getResponse());
    }
}
