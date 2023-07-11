package com.xqxy.dr.modular.powerplant.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.dr.modular.powerplant.entity.DaTrade;
import com.xqxy.dr.modular.powerplant.mapper.DaTradeMapper;
import com.xqxy.dr.modular.powerplant.service.DaTradeService;
import org.springframework.stereotype.Service;

/**
 * 交易提交表 服务实现类
 * @author lixiaojun
 * @since 2021-11-09
 */
@Service
public class DaTradeServiceImpl extends ServiceImpl<DaTradeMapper, DaTrade> implements DaTradeService {
    
    @Override
    public Page<DaTrade> page(DaTrade param) {
        LambdaQueryWrapper<DaTrade> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(param)) {
            // 根据名稱查询
            if (ObjectUtil.isNotEmpty(param.getName())) {
                queryWrapper.like(DaTrade::getName, param.getName());
            }
            // 根据编码查询
            if (ObjectUtil.isNotEmpty(param.getNum())) {
                queryWrapper.like(DaTrade::getNum, param.getNum());
            }
        }
        //倒叙排列，最新的在前面
        queryWrapper.orderByDesc(DaTrade::getId);
        return this.page(param.getPage(),queryWrapper);
    }
    
}
