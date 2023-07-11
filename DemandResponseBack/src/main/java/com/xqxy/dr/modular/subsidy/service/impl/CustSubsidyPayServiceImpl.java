package com.xqxy.dr.modular.subsidy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.core.pojo.login.CurrenUserInfo;
import com.xqxy.core.util.SecurityUtils;
import com.xqxy.dr.modular.subsidy.entity.CustSubsidy;
import com.xqxy.dr.modular.subsidy.entity.CustSubsidyDaily;
import com.xqxy.dr.modular.subsidy.entity.CustSubsidyPay;
import com.xqxy.dr.modular.subsidy.mapper.CustSubsidyDailyMapper;
import com.xqxy.dr.modular.subsidy.mapper.CustSubsidyMapper;
import com.xqxy.dr.modular.subsidy.mapper.CustSubsidyPayMapper;
import com.xqxy.dr.modular.subsidy.param.CustConsSubsidyPayParam;
import com.xqxy.dr.modular.subsidy.param.CustSubsidyDailyParam;
import com.xqxy.dr.modular.subsidy.param.CustSubsidyPayParam;
import com.xqxy.dr.modular.subsidy.param.EventCustSubsidyPayParam;
import com.xqxy.dr.modular.subsidy.result.CustSubsidyByConsIds;
import com.xqxy.dr.modular.subsidy.result.EventCustSubsidyPayInfo;
import com.xqxy.dr.modular.subsidy.service.CustSubsidyPayService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.sys.modular.cust.service.ConsService;
import org.docx4j.wml.P;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * 客户激励费用发放 服务实现类
 * </p>
 *
 * @author hu xingxing
 * @since 2021-10-20
 */
@Service
public class CustSubsidyPayServiceImpl extends ServiceImpl<CustSubsidyPayMapper, CustSubsidyPay> implements CustSubsidyPayService {

    @Resource
    private CustSubsidyPayMapper custSubsidyPayMapper;

    @Resource
    private CustSubsidyDailyMapper custSubsidyDailyMapper;

    @Resource
    private CustSubsidyMapper custSubsidyMapper;
    @Resource
    private ConsService consService;


    @Override
    public List<CustSubsidyPay> custSubsidyPayByPayNo(String payNo) {

        LambdaQueryWrapper<CustSubsidyPay> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CustSubsidyPay::getPayNo, payNo);

        return this.list(queryWrapper);
    }

    @Override
    public Page<CustSubsidyPay> custSubsidyPayPage(CustSubsidyPayParam custSubsidyPayParam) {

        Map<String, Object> map = new HashMap<>();
        if (ObjectUtils.isNotNull(custSubsidyPayParam)) {
            if (ObjectUtils.isNotEmpty(custSubsidyPayParam.getPayNo())) {
                map.put("payNo", custSubsidyPayParam.getPayNo());
            }

            if (ObjectUtils.isNotEmpty(custSubsidyPayParam.getLegalName())) {
                map.put("legalName", custSubsidyPayParam.getLegalName());
            }

            if (ObjectUtils.isNotEmpty(custSubsidyPayParam.getCreditCode())) {
                map.put("creditCode", custSubsidyPayParam.getCreditCode());
            }
        }

        return custSubsidyPayMapper.custSubsidyPayPage(custSubsidyPayParam.getPage(), map);
    }

    @Override
    public Page<CustSubsidy> custSubsidyPayDetails(CustSubsidyPayParam custSubsidyPayParam) {
        CustSubsidyDailyParam custSubsidyDailyParam = new CustSubsidyDailyParam();
        Map<String, Object> map = new HashMap<>();
        if (ObjectUtils.isNotNull(custSubsidyPayParam)) {
            if (ObjectUtils.isNotEmpty(custSubsidyPayParam.getPayNo())) {
                custSubsidyDailyParam.setSettlementNo(custSubsidyPayParam.getPayNo());
            }

            if (ObjectUtils.isNotEmpty(custSubsidyPayParam.getCustId())) {
                custSubsidyDailyParam.setCustId(custSubsidyPayParam.getCustId());
                map.put("custId", custSubsidyPayParam.getCustId());
            }
        }

        Set<String> set = new HashSet<>();
        List<CustSubsidyDaily> custSubsidyDailies = custSubsidyDailyMapper.getCustSubsidyDailiesByCustIdAndSettlementNo(custSubsidyDailyParam);
        for (CustSubsidyDaily custSubsidyDaily : custSubsidyDailies) {
            String eventIds = custSubsidyDaily.getEventIds();
            String[] eventIdList = eventIds.split(",");
            set.addAll(Arrays.asList(eventIdList));
        }

        map.put("eventIds", set);
        Page page = custSubsidyMapper.custSubsidyPage(custSubsidyPayParam.getPage(), map);
        return page;
    }

    @Override
    public Page<EventCustSubsidyPayInfo> eventCustSubsidyPayPage(EventCustSubsidyPayParam eventCustSubsidyPayParam) {
        CurrenUserInfo currentUserInfoUTF8 = SecurityUtils.getCurrentUserInfoUTF8();
        if (currentUserInfoUTF8 != null) {
            eventCustSubsidyPayParam.setCustId(currentUserInfoUTF8.getId());
        } else {
            return new Page<>();
        }

        LambdaQueryWrapper<CustSubsidyDaily> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(CustSubsidyDaily::getSettlementNo, eventCustSubsidyPayParam.getSettlementNo());
        lambdaQueryWrapper.eq(CustSubsidyDaily::getCustId, eventCustSubsidyPayParam.getCustId());
        lambdaQueryWrapper.select(CustSubsidyDaily::getEventIds);
        List<String> eventIds = custSubsidyDailyMapper.selectList(lambdaQueryWrapper).stream().map(CustSubsidyDaily::getEventIds).flatMap(item -> Stream.of(item.split(","))).collect(Collectors.toList());
        eventCustSubsidyPayParam.setEventIds(eventIds);
        eventCustSubsidyPayParam.setConsIds(consService.getConsIdListByCust(Long.valueOf(eventCustSubsidyPayParam.getCustId())));

        return custSubsidyPayMapper.eventCustSubsidyPayPage(eventCustSubsidyPayParam.getPage(), eventCustSubsidyPayParam);
    }

    @Override
    public Page<CustSubsidyByConsIds> custConsSubsidyPayDetails(CustConsSubsidyPayParam custConsSubsidyPayParam) {
        CurrenUserInfo currentUserInfoUTF8 = SecurityUtils.getCurrentUserInfoUTF8();
        if (currentUserInfoUTF8 != null) {
            custConsSubsidyPayParam.setCustId(currentUserInfoUTF8.getId());
        } else {
            return new Page<>();
        }
        custConsSubsidyPayParam.setConsIds(consService.getConsIdListByCust(Long.valueOf(custConsSubsidyPayParam.getCustId())));
        Page<CustSubsidyByConsIds> page = custSubsidyMapper.custSubsidyByConsIdsPage(custConsSubsidyPayParam.getPage(), custConsSubsidyPayParam);
        return page;
    }


}
