package com.xqxy.dr.modular.powerplant.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.dr.modular.powerplant.entity.DaConsIncome;
import com.xqxy.dr.modular.powerplant.mapper.DaConsIncomeMapper;
import com.xqxy.dr.modular.powerplant.param.DaParam;
import com.xqxy.dr.modular.powerplant.service.DaConsIncomeService;
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
public class DaConsIncomeServiceImpl extends ServiceImpl<DaConsIncomeMapper, DaConsIncome> implements DaConsIncomeService {

    @Override
    public Page<DaConsIncome> page(DaParam daParam) {
        LambdaQueryWrapper<DaConsIncome> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(daParam.getId())) {
            lambdaQueryWrapper.eq(DaConsIncome::getConsId, daParam.getId());
        }
        return page(new Page<>(daParam.getCurrent(), daParam.getSize()), lambdaQueryWrapper);
    }
}
