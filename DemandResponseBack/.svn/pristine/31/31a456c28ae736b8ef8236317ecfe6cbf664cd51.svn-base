package com.xqxy.dr.modular.subsidy.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.xqxy.core.exception.ServiceException;
import com.xqxy.core.poi.POIExcelUtil;
import com.xqxy.core.pojo.login.CurrenUserInfo;
import com.xqxy.core.util.HttpServletUtil;
import com.xqxy.core.util.OrganizationUtil;
import com.xqxy.core.util.SecurityUtils;
import com.xqxy.dr.modular.subsidy.entity.ConsSubsidyDaily;
import com.xqxy.dr.modular.subsidy.mapper.ConsSubsidyDailyMapper;
import com.xqxy.dr.modular.subsidy.mapper.CustSubsidyDailyMapper;
import com.xqxy.dr.modular.subsidy.param.ConsSubsidyDailyParam;
import com.xqxy.dr.modular.subsidy.result.DailySubsidyInfo;
import com.xqxy.dr.modular.subsidy.result.MySubsidyInfo;
import com.xqxy.dr.modular.subsidy.service.ConsSubsidyDailyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.sys.modular.cust.entity.Cust;
import com.xqxy.sys.modular.cust.enums.ConsExceptionEnum;
import com.xqxy.sys.modular.cust.enums.IsAggregatorEnum;
import com.xqxy.sys.modular.cust.enums.OrgTitleEnum;
import com.xqxy.sys.modular.cust.service.CustService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 日补贴 服务实现类
 * </p>
 *
 * @author Shen
 * @since 2021-10-21
 */
@Service
public class ConsSubsidyDailyServiceImpl extends ServiceImpl<ConsSubsidyDailyMapper, ConsSubsidyDaily> implements ConsSubsidyDailyService {

    @Resource
    CustService custService;

    @Resource
    private ConsSubsidyDailyMapper consSubsidyDailyMapper;

    @Resource
    private CustSubsidyDailyMapper custSubsidyDailyMapper;

    @Override
    public Page<DailySubsidyInfo> dailySubsidyPage(ConsSubsidyDailyParam consSubsidyDailyParam) {

        CurrenUserInfo currentUserInfo = SecurityUtils.getCurrentUserInfoUTF8();
        Cust cust = custService.getById(currentUserInfo.getId());
        Page<DailySubsidyInfo> dailySubsidyInfos = new Page<>();
        Map<String, Object> map = new HashMap<>();

        if (ObjectUtil.isNotNull(consSubsidyDailyParam)) {
            if (ObjectUtil.isNotEmpty(consSubsidyDailyParam.getSubsidyStartDate())) {
                map.put("startDate", consSubsidyDailyParam.getSubsidyStartDate().toString());
            }
            if (ObjectUtil.isNotEmpty(consSubsidyDailyParam.getSubsidyEndDate())) {
                map.put("endDate", consSubsidyDailyParam.getSubsidyEndDate().toString());
            }
            if (ObjectUtil.isNotEmpty(consSubsidyDailyParam.getConsId())) {
                map.put("consId", String.valueOf(consSubsidyDailyParam.getConsId()));
            }
            if (ObjectUtil.isNotEmpty(consSubsidyDailyParam.getConsName())) {
                map.put("consName", String.valueOf(consSubsidyDailyParam.getConsName()));
            }
            if (ObjectUtil.isNotEmpty(consSubsidyDailyParam.getCustName())) {
                map.put("custName", String.valueOf(consSubsidyDailyParam.getCustName()));
            }
            if (ObjectUtil.isNotEmpty(consSubsidyDailyParam.getCreditCode())) {
                map.put("creditCode", String.valueOf(consSubsidyDailyParam.getCreditCode()));
            }
        }

        if (ObjectUtil.isNull(currentUserInfo.getId()) || ObjectUtil.isNull(cust)) {
            int integrator = consSubsidyDailyParam.getIntegrator();
            if (integrator == IsAggregatorEnum.NOT_AGGREGATOR.getCode()) {
                List<String> orgIds = OrganizationUtil.getAllOrgByOrgNo();
                map.put("orgIds", orgIds);

                dailySubsidyInfos = consSubsidyDailyMapper.getConsDaily(consSubsidyDailyParam.getPage(), map);
            } else {
                CurrenUserInfo currentUser = SecurityUtils.getCurrentUserInfoUTF8();
                if (currentUser != null) {
                    String orgTitle = currentUser.getOrgTitle();
                    if (orgTitle != null && orgTitle.equals(OrgTitleEnum.PROVINCE.getCode())) {
                        dailySubsidyInfos = custSubsidyDailyMapper.getCustDaily(consSubsidyDailyParam.getPage(), map);
                    }
                }
            }
        } else {
            map.put("custId", currentUserInfo.getId());
            Integer integrator = cust.getIntegrator();
            if (integrator == null) {
                dailySubsidyInfos = new Page<DailySubsidyInfo>();
            } else if (integrator.equals(IsAggregatorEnum.NOT_AGGREGATOR.getCode())) {
                dailySubsidyInfos = consSubsidyDailyMapper.getConsDaily(consSubsidyDailyParam.getPage(), map);
            } else {
                dailySubsidyInfos = custSubsidyDailyMapper.getCustDaily(consSubsidyDailyParam.getPage(), map);
            }
        }

        return dailySubsidyInfos;
    }

    @Override
    public void exportDailySubsidy(ConsSubsidyDailyParam consSubsidyDailyParam) {

        CurrenUserInfo currentUserInfo = SecurityUtils.getCurrentUserInfoUTF8();
        Cust cust = custService.getById(currentUserInfo.getId());
        List<DailySubsidyInfo> dailySubsidyInfos = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();

        if (ObjectUtil.isNotNull(consSubsidyDailyParam)) {
            if (ObjectUtil.isNotEmpty(consSubsidyDailyParam.getSubsidyStartDate())) {
                map.put("startDate", consSubsidyDailyParam.getSubsidyStartDate().toString());
            }
            if (ObjectUtil.isNotEmpty(consSubsidyDailyParam.getSubsidyEndDate())) {
                map.put("endDate", consSubsidyDailyParam.getSubsidyEndDate().toString());
            }
            if (ObjectUtil.isNotEmpty(consSubsidyDailyParam.getConsId())) {
                map.put("consId", String.valueOf(consSubsidyDailyParam.getConsId()));
            }
        }

        if (ObjectUtil.isNull(currentUserInfo.getId()) || ObjectUtil.isNull(cust)) {
            int integrator = consSubsidyDailyParam.getIntegrator();
            if (integrator == IsAggregatorEnum.NOT_AGGREGATOR.getCode()) {
                List<String> orgIds = OrganizationUtil.getAllOrgByOrgNo();
                map.put("orgIds", orgIds);

                dailySubsidyInfos = consSubsidyDailyMapper.getConsDailySubsidy(map);
            } else {
                CurrenUserInfo currentUser = SecurityUtils.getCurrentUserInfoUTF8();
                if (currentUser != null) {
                    String orgTitle = currentUser.getOrgTitle();
                    if (orgTitle != null && orgTitle.equals(OrgTitleEnum.PROVINCE.getCode())) {
                        dailySubsidyInfos = custSubsidyDailyMapper.getCustDailySubsidy(map);;
                    }
                }
            }
        } else {
            map.put("custId", currentUserInfo.getId());
            int integrator = cust.getIntegrator();
            if (integrator == IsAggregatorEnum.NOT_AGGREGATOR.getCode()) {
                dailySubsidyInfos = consSubsidyDailyMapper.getConsDailySubsidy(map);
            } else {
                dailySubsidyInfos = custSubsidyDailyMapper.getCustDailySubsidy(map);
            }
        }

        String excelName = "日补贴.xls";
        String titleName = excelName.split("\\.")[0];
        String sheetName = titleName;
        String[] titleRow = {"序号", "补贴日期", "用户名称", "电力营销户号", "联系方式", "结算金额(元)"};
        String[][] dataRows = new String[dailySubsidyInfos.size()][titleRow.length];
        int index = 1;
        for (DailySubsidyInfo dailySubsidyInfo : dailySubsidyInfos) {
            dataRows[index-1][0] = String.valueOf(index);
            dataRows[index-1][1] = dailySubsidyInfo.getSubsidyDate().toString();
            dataRows[index-1][2] = dailySubsidyInfo.getConsName();
            dataRows[index-1][3] = dailySubsidyInfo.getConsId();
            dataRows[index-1][4] = dailySubsidyInfo.getTel();
            dataRows[index-1][5] = String.valueOf(dailySubsidyInfo.getSettledAmount());
            index += 1;
        }

        POIExcelUtil.generatorExcel(excelName, titleName, sheetName, titleRow, dataRows, HttpServletUtil.getResponse());
    }

    @Override
    public ConsSubsidyDaily getConsDailySubsidy(String consId, LocalDate subsidyDate) {

        LambdaQueryWrapper<ConsSubsidyDaily> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(consId)) {
            queryWrapper.eq(ConsSubsidyDaily::getConsId, consId);
        }

        if (ObjectUtil.isNotNull(subsidyDate)) {
            queryWrapper.eq(ConsSubsidyDaily::getSubsidyDate, subsidyDate);
        }

        return this.getOne(queryWrapper);
    }

    @Override
    public Page<MySubsidyInfo> mySubsidy(ConsSubsidyDailyParam consSubsidyDailyParam) {

        CurrenUserInfo currentUserInfo = SecurityUtils.getCurrentUserInfoUTF8();
        Map<String, Object> map = new HashMap<>();
        String custId = null;

        if (currentUserInfo != null) {
            custId = currentUserInfo.getId();
        }

        Cust cust = custService.getById(custId);
        if(cust == null){
            throw new ServiceException(ConsExceptionEnum.CUST_NOT_EXIST);
        }
        map.put("custId", custId);
        map.put("eventNo", consSubsidyDailyParam.getEventNo());
        map.put("subsidyDate", consSubsidyDailyParam.getSubsidyDate());

        Integer integrator = cust.getIntegrator();
        if (integrator != null) {
            if (integrator == 0) {
                return consSubsidyDailyMapper.consMySubsidy(consSubsidyDailyParam.getPage(), map);
            } else if (integrator == 1) {
                return custSubsidyDailyMapper.custMySubsidy(consSubsidyDailyParam.getPage(), map);
            }
        }

        return new Page<>();
    }
}
