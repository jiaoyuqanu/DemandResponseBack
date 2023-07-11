package com.xqxy.dr.modular.subsidy.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xqxy.dr.modular.subsidy.entity.CustSubsidyDaily;
import com.xqxy.dr.modular.subsidy.mapper.CustSubsidyDailyMapper;
import com.xqxy.dr.modular.subsidy.service.CustSubsidyDailyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * 日补贴 服务实现类
 * </p>
 *
 * @author Shen
 * @since 2021-10-21
 */
@Service
public class CustSubsidyDailyServiceImpl extends ServiceImpl<CustSubsidyDailyMapper, CustSubsidyDaily> implements CustSubsidyDailyService {

    @Override
    public CustSubsidyDaily getCustDailySubsidy(long custId, LocalDate subsidyDate) {

        LambdaQueryWrapper<CustSubsidyDaily> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(custId)) {
            queryWrapper.eq(CustSubsidyDaily::getCustId, custId);
        }

        if (ObjectUtil.isNotNull(subsidyDate)) {
            queryWrapper.eq(CustSubsidyDaily::getSubsidyDate, subsidyDate);
        }

        return this.getOne(queryWrapper);
    }

}
