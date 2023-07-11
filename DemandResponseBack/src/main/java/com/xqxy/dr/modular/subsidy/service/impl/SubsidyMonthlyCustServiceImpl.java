package com.xqxy.dr.modular.subsidy.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.core.poi.POIExcelUtil;
import com.xqxy.core.pojo.login.CurrenUserInfo;
import com.xqxy.core.util.HttpServletUtil;
import com.xqxy.core.util.SecurityUtils;
import com.xqxy.dr.modular.subsidy.entity.SubsidyMonthlyCust;
import com.xqxy.dr.modular.subsidy.enums.SubsidyMonthlyEnum;
import com.xqxy.dr.modular.subsidy.mapper.SubsidyMonthlyCustMapper;
import com.xqxy.dr.modular.subsidy.param.SubsidyMonthlyCustParam;
import com.xqxy.dr.modular.subsidy.result.MonthlySubsidyInfo;
import com.xqxy.dr.modular.subsidy.service.SubsidyMonthlyCustService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.sys.modular.cust.enums.OrgTitleEnum;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 月补贴 服务实现类
 * </p>
 *
 * @author hu xingxing
 * @since 2021-10-30
 */
@Service
public class SubsidyMonthlyCustServiceImpl extends ServiceImpl<SubsidyMonthlyCustMapper, SubsidyMonthlyCust> implements SubsidyMonthlyCustService {

    @Resource
    private SubsidyMonthlyCustMapper subsidyMonthlyCustMapper;

    @Override
    public Page<SubsidyMonthlyCust> pageSubsidyMonthly(SubsidyMonthlyCustParam subsidyMonthlyCustParam) {

        CurrenUserInfo currentUser = SecurityUtils.getCurrentUserInfoUTF8();
        if (currentUser != null) {
            String orgTitle = currentUser.getOrgTitle();
            if (orgTitle != null && orgTitle.equals(OrgTitleEnum.PROVINCE.getCode())) {
                Map<String, Object> map = new HashMap<>();
                if (ObjectUtil.isNotEmpty(subsidyMonthlyCustParam.getSubsidyMonth())) {
                    map.put("subsidyMonth", subsidyMonthlyCustParam.getSubsidyMonth());
                }

                if (ObjectUtil.isNotEmpty(subsidyMonthlyCustParam.getCustName())) {
                    map.put("custName", subsidyMonthlyCustParam.getCustName());
                }

                return subsidyMonthlyCustMapper.subsidyMonthlyCustPage(subsidyMonthlyCustParam.getPage(), map);
            }
        }

        return new Page<>();
    }

    @Override
    public void subsidyMonthlyUpd(SubsidyMonthlyCustParam subsidyMonthlyCustParam) {

        if (ObjectUtil.isNotEmpty(subsidyMonthlyCustParam.getId())) {
            SubsidyMonthlyCust subsidyMonthlyCust = this.getById(subsidyMonthlyCustParam.getId());
            subsidyMonthlyCust.setStatus(SubsidyMonthlyEnum.FROZEN.getCode());
            this.updateById(subsidyMonthlyCust);
        }
    }

    @Override
    public void exportCustMonthlySubsidy(SubsidyMonthlyCustParam subsidyMonthlyCustParam) {

        Map<String, String> map = new HashMap<>();
        if (ObjectUtil.isNotNull(subsidyMonthlyCustParam)) {
            if (ObjectUtil.isNotEmpty(subsidyMonthlyCustParam.getSubsidyMonth())) {
                map.put("subsidyMonth", subsidyMonthlyCustParam.getSubsidyMonth());
            }
            if (ObjectUtil.isNotEmpty(subsidyMonthlyCustParam.getCustName())) {
                map.put("custName", subsidyMonthlyCustParam.getCustName());
            }
        }

        Map<String, String> stateMap = new HashMap<>();
        stateMap.put(SubsidyMonthlyEnum.NOPUBLIC.getCode(), SubsidyMonthlyEnum.NOPUBLIC.getMessage());
        stateMap.put(SubsidyMonthlyEnum.PUBLIC.getCode(), SubsidyMonthlyEnum.PUBLIC.getMessage());
        stateMap.put(SubsidyMonthlyEnum.FROZEN.getCode(), SubsidyMonthlyEnum.FROZEN.getMessage());

        List<MonthlySubsidyInfo> monthlySubsidyInfos = new ArrayList<>();
        CurrenUserInfo currentUser = SecurityUtils.getCurrentUserInfoUTF8();
        if (currentUser != null) {
            String orgTitle = currentUser.getOrgTitle();
            if (orgTitle != null && orgTitle.equals(OrgTitleEnum.PROVINCE.getCode())) {
                monthlySubsidyInfos = subsidyMonthlyCustMapper.getCustMonthlySubsidy(map);
            }
        }

        String excelName = "月补贴";
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
