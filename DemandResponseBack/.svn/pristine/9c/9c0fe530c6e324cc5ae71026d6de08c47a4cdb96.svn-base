package com.xqxy.dr.modular.powerplant.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.dr.modular.powerplant.entity.DaMarketVoidancePrice;
import com.xqxy.dr.modular.powerplant.mapper.DaMarketVoidancePriceMapper;
import com.xqxy.dr.modular.powerplant.param.DaParam;
import com.xqxy.dr.modular.powerplant.service.DaMarketVoidancePriceService;
import com.xqxy.dr.modular.project.enums.DeclareProjectCheckEnums;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Caoj
 * @since 2021-12-08
 */
@Service
@Slf4j
public class DaMarketVoidancePriceServiceImpl extends ServiceImpl<DaMarketVoidancePriceMapper, DaMarketVoidancePrice> implements DaMarketVoidancePriceService {

    @Override
    public Page<DaMarketVoidancePrice> page(DaParam daParam) {
        LambdaQueryWrapper<DaMarketVoidancePrice> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(daParam.getCustNo())) {
            lambdaQueryWrapper.like(DaMarketVoidancePrice::getCustNo, daParam.getCustNo());
        }
        if (ObjectUtil.isNotNull(daParam.getPickLoadDate())) {
            lambdaQueryWrapper.eq(DaMarketVoidancePrice::getPickLoadDate, daParam.getPickLoadDate());
        }
        if (ObjectUtil.isNotNull(daParam.getStartTime()) && ObjectUtil.isNotNull(daParam.getEndTime())) {
            lambdaQueryWrapper.gt(DaMarketVoidancePrice::getStartTime, daParam.getStartTime())
                    .lt(DaMarketVoidancePrice::getEndTime, daParam.getEndTime());
        }
        return page(new Page<>(daParam.getCurrent(), daParam.getSize()), lambdaQueryWrapper);
    }

    @Override
    public void verify(DaParam daParam) {
        LambdaUpdateWrapper<DaMarketVoidancePrice> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(DaMarketVoidancePrice::getCustId, daParam.getCustId());
        lambdaUpdateWrapper.set(DaMarketVoidancePrice::getState, DeclareProjectCheckEnums.VERIFYING.getCode());
        update(lambdaUpdateWrapper);
    }
}
