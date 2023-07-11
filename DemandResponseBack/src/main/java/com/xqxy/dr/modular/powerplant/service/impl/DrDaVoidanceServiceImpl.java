package com.xqxy.dr.modular.powerplant.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.dr.modular.powerplant.entity.DrDaVoidance;
import com.xqxy.dr.modular.powerplant.mapper.DrDaVoidanceMapper;
import com.xqxy.dr.modular.powerplant.param.DrDaVoidanceParam;
import com.xqxy.dr.modular.powerplant.service.DrDaVoidanceService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  出清 服务实现类
 * </p>
 *
 * @author czj
 * @since 2021-12-09
 */
@Service
public class DrDaVoidanceServiceImpl extends ServiceImpl<DrDaVoidanceMapper, DrDaVoidance> implements DrDaVoidanceService {

    @Override
    public Page<DrDaVoidance> page(DrDaVoidanceParam param) {
        LambdaQueryWrapper<DrDaVoidance> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotEmpty(param.getAggregatorNo())) {
            queryWrapper.like(DrDaVoidance::getAggregatorNo, param.getAggregatorNo());
        }
        if (ObjectUtil.isNotEmpty(param.getPeakDate())) {
            queryWrapper.like(DrDaVoidance::getPeakDate, param.getPeakDate());
        }
        if (ObjectUtil.isNotEmpty(param.getPeakTimeInterval())) {
            queryWrapper.like(DrDaVoidance::getPeakTimeInterval, param.getPeakTimeInterval());
        }
        queryWrapper.orderByDesc(DrDaVoidance::getCreateTime);
        return this.page(param.getPage(),queryWrapper);
    }
}
