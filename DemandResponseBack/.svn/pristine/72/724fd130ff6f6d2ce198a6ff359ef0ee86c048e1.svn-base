package com.xqxy.dr.modular.powerplant.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.dr.modular.powerplant.entity.DrDaAggregatorPrice;
import com.xqxy.dr.modular.powerplant.mapper.DrDaAggregatorPriceMapper;
import com.xqxy.dr.modular.powerplant.param.DrDaAggregatorPriceAddParam;
import com.xqxy.dr.modular.powerplant.param.DrDaAggregatorPriceParam;
import com.xqxy.dr.modular.powerplant.service.DrDaAggregatorPriceService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 负荷聚合商报价信息 服务实现类
 * </p>
 *
 * @author czj
 * @since 2021-12-10
 */
@Service
public class DrDaAggregatorPriceServiceImpl extends ServiceImpl<DrDaAggregatorPriceMapper, DrDaAggregatorPrice> implements DrDaAggregatorPriceService {

    @Override
    public Page<DrDaAggregatorPrice> page(DrDaAggregatorPriceParam param) {
        LambdaQueryWrapper<DrDaAggregatorPrice> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotEmpty(param.getElectricBidNoticeId())) {
            queryWrapper.eq(DrDaAggregatorPrice::getElectricBidNoticeId, param.getElectricBidNoticeId());
        }
        if (ObjectUtil.isNotEmpty(param.getDeclareName())) {
            queryWrapper.like(DrDaAggregatorPrice::getDeclareName, param.getDeclareName());
        }
        queryWrapper.orderByDesc(DrDaAggregatorPrice::getId);
        return this.page(param.getPage(),queryWrapper);
    }

    @Override
    public void add(DrDaAggregatorPriceAddParam drDaAggregatorPriceAddParam) {
        DrDaAggregatorPrice drDaAggregatorPrice = new DrDaAggregatorPrice();
        BeanUtils.copyProperties(drDaAggregatorPriceAddParam,drDaAggregatorPrice);
        drDaAggregatorPrice.setStatus("0");
        this.save(drDaAggregatorPrice);
    }

    @Override
    public void submitInfo(DrDaAggregatorPriceParam param) {
        List<DrDaAggregatorPrice> list = param.getDrDaAggregatorPriceList();
        if(null!=list && list.size()>0) {
            for(DrDaAggregatorPrice drDaAggregatorPrice : list) {
                if(null!=drDaAggregatorPrice) {
                    drDaAggregatorPrice.setStatus("1");
                }
            }
        }
        this.updateBatchById(list);
    }

}
