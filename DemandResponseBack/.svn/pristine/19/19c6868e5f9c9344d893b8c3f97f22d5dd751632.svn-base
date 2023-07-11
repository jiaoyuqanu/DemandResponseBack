package com.xqxy.dr.modular.subsidy.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.core.poi.POIExcelUtil;
import com.xqxy.core.util.HttpServletUtil;
import com.xqxy.core.util.OrganizationUtil;
import com.xqxy.dr.modular.subsidy.entity.SubsidyMonthly;
import com.xqxy.dr.modular.subsidy.enums.SubsidyMonthlyEnum;
import com.xqxy.dr.modular.subsidy.mapper.SubsidyMonthlyMapper;
import com.xqxy.dr.modular.subsidy.param.SubsidyMonthlyParam;
import com.xqxy.dr.modular.subsidy.result.MonthlySubsidyInfo;
import com.xqxy.dr.modular.subsidy.service.SubsidyMonthlyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 月补贴 服务实现类
 * </p>
 *
 * @author hu xingxing
 * @since 2021-10-28
 */
@Service
public class SubsidyMonthlyServiceImpl extends ServiceImpl<SubsidyMonthlyMapper, SubsidyMonthly> implements SubsidyMonthlyService {

    @Resource
    private SubsidyMonthlyMapper subsidyMonthlyMapper;

    @Override
    public Page<SubsidyMonthly> pageSubsidyMonthly(SubsidyMonthlyParam subsidyMonthlyParam) {

        Map<String, Object> map = new HashMap<>();
        List<String> orgIds = OrganizationUtil.getAllOrgByOrgNo();
        map.put("orgIds", orgIds);

        if (ObjectUtil.isNotEmpty(subsidyMonthlyParam.getSubsidyMonth())) {
            map.put("subsidyMonth", subsidyMonthlyParam.getSubsidyMonth());
        }

        if (ObjectUtil.isNotEmpty(subsidyMonthlyParam.getConsName())) {
            map.put("consName", subsidyMonthlyParam.getConsName());
        }

        return subsidyMonthlyMapper.subsidyMonthlyPage(subsidyMonthlyParam.getPage(), map);
    }

    @Override
    public void subsidyMonthlyUpd(SubsidyMonthlyParam subsidyMonthlyParam) {

        if (ObjectUtil.isNotEmpty(subsidyMonthlyParam.getId())) {
            SubsidyMonthly subsidyMonthly = this.getById(subsidyMonthlyParam.getId());
            subsidyMonthly.setStatus(SubsidyMonthlyEnum.FROZEN.getCode());
            this.updateById(subsidyMonthly);
        }
    }

    @Override
    public void exportConsMonthlySubsidy(SubsidyMonthlyParam subsidyMonthlyParam) {

        Map<String, Object> map = new HashMap<>();
        List<String> orgIds = OrganizationUtil.getAllOrgByOrgNo();
        map.put("orgIds", orgIds);

        if (ObjectUtil.isNotNull(subsidyMonthlyParam)) {
            if (ObjectUtil.isNotEmpty(subsidyMonthlyParam.getSubsidyMonth())) {
                map.put("subsidyMonth", subsidyMonthlyParam.getSubsidyMonth());
            }
            if (ObjectUtil.isNotEmpty(subsidyMonthlyParam.getConsName())) {
                map.put("consName", subsidyMonthlyParam.getConsName());
            }
        }

        Map<String, String> stateMap = new HashMap<>();
        stateMap.put(SubsidyMonthlyEnum.NOPUBLIC.getCode(), SubsidyMonthlyEnum.NOPUBLIC.getMessage());
        stateMap.put(SubsidyMonthlyEnum.PUBLIC.getCode(), SubsidyMonthlyEnum.PUBLIC.getMessage());
        stateMap.put(SubsidyMonthlyEnum.FROZEN.getCode(), SubsidyMonthlyEnum.FROZEN.getMessage());


        List<MonthlySubsidyInfo> monthlySubsidyInfos = subsidyMonthlyMapper.getConsMonthlySubsidy(map);
        String excelName = "月补贴.xls";
        String titleName = excelName.split("\\.")[0];
        String sheetName = titleName;
        String[] titleRow = {"序号", "补贴月份", "用户名称", "联系方式", "结算金额(元)", "状态"};
        String[][] dataRows = new String[monthlySubsidyInfos.size()][titleRow.length];
        int index = 1;

        for (MonthlySubsidyInfo monthlySubsidyInfo : monthlySubsidyInfos) {
            dataRows[index-1][0] = String.valueOf(index);
            dataRows[index-1][1] = monthlySubsidyInfo.getSubsidyMonth();
            dataRows[index-1][2] = monthlySubsidyInfo.getConsName();
            dataRows[index-1][3] = monthlySubsidyInfo.getTel();
            dataRows[index-1][4] = String.valueOf(monthlySubsidyInfo.getSettledAmount());
            dataRows[index-1][5] = stateMap.get(monthlySubsidyInfo.getStatus());
            index += 1;
        }

        POIExcelUtil.generatorExcel(excelName, titleName, sheetName, titleRow, dataRows, HttpServletUtil.getResponse());
    }
}
