package com.xqxy.dr.modular.subsidy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.dr.modular.subsidy.entity.ConsSubsidy;
import com.xqxy.dr.modular.subsidy.entity.ConsSubsidyDaily;
import com.xqxy.dr.modular.subsidy.entity.ConsSubsidyPay;
import com.xqxy.dr.modular.subsidy.mapper.ConsSubsidyDailyMapper;
import com.xqxy.dr.modular.subsidy.mapper.ConsSubsidyMapper;
import com.xqxy.dr.modular.subsidy.mapper.ConsSubsidyPayMapper;
import com.xqxy.dr.modular.subsidy.param.ConsSubsidyDailyParam;
import com.xqxy.dr.modular.subsidy.param.ConsSubsidyPayParam;
import com.xqxy.dr.modular.subsidy.service.ConsSubsidyPayService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 * 用户激励费用发放 服务实现类
 * </p>
 *
 * @author hu xingxing
 * @since 2021-10-20
 */
@Service
public class ConsSubsidyPayServiceImpl extends ServiceImpl<ConsSubsidyPayMapper, ConsSubsidyPay> implements ConsSubsidyPayService {

    @Resource
    private ConsSubsidyPayMapper consSubsidyPayMapper;

    @Resource
    private ConsSubsidyDailyMapper consSubsidyDailyMapper;

    @Resource
    private ConsSubsidyMapper consSubsidyMapper;

    @Override
    public List<ConsSubsidyPay> consSubsidyPayByPayNo(String payNo) {

        LambdaQueryWrapper<ConsSubsidyPay> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ConsSubsidyPay::getPayNo, payNo);

        return this.list(queryWrapper);
    }

    @Override
    public Page<ConsSubsidyPay> consSubsidyPayPage(ConsSubsidyPayParam consSubsidyPayParam) {

        Map<String, Object> map = new HashMap<>();
        if (ObjectUtils.isNotNull(consSubsidyPayParam)) {
            if (ObjectUtils.isNotEmpty(consSubsidyPayParam.getPayNo())) {
                map.put("payNo", consSubsidyPayParam.getPayNo());
            }

            if (ObjectUtils.isNotEmpty(consSubsidyPayParam.getConsId())) {
                map.put("consId", consSubsidyPayParam.getConsId());
            }

            if (ObjectUtils.isNotEmpty(consSubsidyPayParam.getConsName())) {
                map.put("consName", consSubsidyPayParam.getConsName());
            }

            if (ObjectUtils.isNotEmpty(consSubsidyPayParam.getOrgNo())) {
                map.put("orgNo", consSubsidyPayParam.getOrgNo());
            }
        }

        return consSubsidyPayMapper.consSubsidyPayPage(consSubsidyPayParam.getPage(), map);
    }

    @Override
    public Page<ConsSubsidy> consSubsidyPayDetails(ConsSubsidyPayParam consSubsidyPayParam) {

        ConsSubsidyDailyParam consSubsidyDailyParam = new ConsSubsidyDailyParam();
        Map<String, Object> map = new HashMap<>();
        if (ObjectUtils.isNotNull(consSubsidyPayParam)) {
            if (ObjectUtils.isNotEmpty(consSubsidyPayParam.getPayNo())) {
                consSubsidyDailyParam.setSettlementNo(consSubsidyPayParam.getPayNo());
            }

            if (ObjectUtils.isNotEmpty(consSubsidyPayParam.getConsId())) {
                consSubsidyDailyParam.setConsId(consSubsidyPayParam.getConsId());
                map.put("consId", consSubsidyPayParam.getConsId());
            }
        }

        Set<String> set = new HashSet<>();
        List<ConsSubsidyDaily> consSubsidyDailies = consSubsidyDailyMapper.getConsSubsidyDailiesByConsIdAndSettlementNo(consSubsidyDailyParam);
        for (ConsSubsidyDaily consSubsidyDaily : consSubsidyDailies) {
            String eventIds = consSubsidyDaily.getEventIds();
            String[] eventIdList = eventIds.split(",");
            set.addAll(Arrays.asList(eventIdList));
        }

        map.put("eventIds", set);
        return consSubsidyMapper.consSubsidyPage(consSubsidyPayParam.getPage(), map);
    }
}
