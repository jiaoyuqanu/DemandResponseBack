package com.xqxy.dr.modular.statistics.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.core.poi.POIExcelUtil;
import com.xqxy.core.util.ConsOrgUtils;
import com.xqxy.core.util.HttpServletUtil;
import com.xqxy.dr.modular.statistics.mapper.ProjectEffectMapper;
import com.xqxy.dr.modular.statistics.param.StatisticalParam;
import com.xqxy.dr.modular.statistics.result.AreaProjectResult;
import com.xqxy.dr.modular.statistics.result.ProjectEffectResult;
import com.xqxy.dr.modular.statistics.service.ProjectEffectService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProjectEffectServiceImpl implements ProjectEffectService {

    @Resource
    private ProjectEffectMapper projectEffectMapper;

    /**
     * 用户需求响应项目效果统计
     *
     * @return
     */
    @Override
    public Page<ProjectEffectResult> projectEffectStatistics(StatisticalParam statisticalParam) {

        Page<ProjectEffectResult> projectEffects = projectEffectMapper.projectEffectStatistics(statisticalParam.getPage(), statisticalParam);
        return projectEffects;
    }


    /**
     * 用户需求响应项目效果统计导出
     *
     * @return
     */
    @Override
    public void exportProject(StatisticalParam statisticalParam) {

        List<ProjectEffectResult> projects = projectEffectMapper.exportProject(statisticalParam);
        Map<String, Map<String, String>> cityOrg = ConsOrgUtils.getInstance().getCityOrg(projects, ProjectEffectResult::getConsId);
        String excelName = "用户需求响应项目效果统计.xls";
        String titleName = excelName.split("\\.")[0];
        String sheetName = titleName;
        String[] titleRow = {"序号", "市供电单位", "区县供电单位", "供电单位", "电力户号", "用户名称", "邀约次数", "参与次数", "到位次数", "最大响应负荷(kW)", "最大响应电量(kWh)"};
        String[][] dataRows = new String[projects.size()][titleRow.length];
        int index = 1;
        for (ProjectEffectResult project : projects) {
            Map<String, String> orgMap = cityOrg.getOrDefault(project.getConsId(), new HashMap<>());
            Integer indexY = 0;
            dataRows[index - 1][indexY++] = String.valueOf(index);
//            dataRows[index - 1][1] = orgMap.getOrDefault(ConsOrgUtils.PROVINCE, "");
            dataRows[index - 1][indexY++] = orgMap.getOrDefault(ConsOrgUtils.CITY, "");
            dataRows[index - 1][indexY++] = orgMap.getOrDefault(ConsOrgUtils.AREA, "");
            dataRows[index - 1][indexY++] = orgMap.getOrDefault(ConsOrgUtils.ORG, "");
            dataRows[index - 1][indexY++] = String.valueOf(project.getConsId());
            dataRows[index - 1][indexY++] = project.getConsName();
            dataRows[index - 1][indexY++] = String.valueOf(project.getInvitation());
            dataRows[index - 1][indexY++] = String.valueOf(project.getParticipate());
            dataRows[index - 1][indexY++] = String.valueOf(project.getEffective());
            dataRows[index - 1][indexY++] = String.valueOf(project.getActualCap());
            dataRows[index - 1][indexY++] = String.valueOf(project.getActualEnergy());
            index += 1;
        }

        POIExcelUtil.generatorExcel(excelName, titleName, sheetName, titleRow, dataRows, HttpServletUtil.getResponse());
    }
}
