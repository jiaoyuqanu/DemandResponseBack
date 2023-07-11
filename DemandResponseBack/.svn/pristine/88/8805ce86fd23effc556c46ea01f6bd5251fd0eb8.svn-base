package com.xqxy.dr.modular.powerplant.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.dr.modular.powerplant.entity.DaSettlement;
import com.xqxy.dr.modular.powerplant.mapper.DaSettlementMapper;
import com.xqxy.dr.modular.powerplant.service.DaSettlementService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 费用结算 服务实现类
 * </p>
 *
 * @author lixiaojun
 * @since 2021-11-09
 */
@Service
public class DaSettlementServiceImpl extends ServiceImpl<DaSettlementMapper, DaSettlement> implements DaSettlementService {

    @Override
    public Page<DaSettlement> page(DaSettlement param) {
        LambdaQueryWrapper<DaSettlement> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(param)) {
            // 根据名稱查询
            if (ObjectUtil.isNotEmpty(param.getName())) {
                queryWrapper.like(DaSettlement::getName, param.getName());
            }
            // 根据编码查询
            if (ObjectUtil.isNotEmpty(param.getNum())) {
                queryWrapper.like(DaSettlement::getNum, param.getNum());
            }
        }
        //倒叙排列，最新的在前面
        queryWrapper.orderByDesc(DaSettlement::getId);
        return this.page(param.getPage(),queryWrapper);
    }

}
