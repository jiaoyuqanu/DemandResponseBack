package com.xqxy.dr.modular.powerplant.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.dr.modular.powerplant.entity.DaMarket;
import com.xqxy.dr.modular.powerplant.mapper.DaMarketMapper;
import com.xqxy.dr.modular.powerplant.service.DaMarketService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 市场准入审核表 服务实现类
 * </p>
 *
 * @author lixiaojun
 * @since 2021-11-09
 */
@Service
public class DaMarketServiceImpl extends ServiceImpl<DaMarketMapper, DaMarket> implements DaMarketService {

    @Override
    public Page<DaMarket> page(DaMarket param) {
        LambdaQueryWrapper<DaMarket> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(param)) {
            // 根据名稱查询
            if (ObjectUtil.isNotEmpty(param.getName())) {
                queryWrapper.like(DaMarket::getName, param.getName());
            }
            // 根据编码查询
            if (ObjectUtil.isNotEmpty(param.getNum())) {
                queryWrapper.like(DaMarket::getNum, param.getNum());
            }
            // 根据状态查询
            if (ObjectUtil.isNotEmpty(param.getStatus())) {
                queryWrapper.eq(DaMarket::getStatus, param.getStatus());
            }
        }
        //倒叙排列，最新的在前面
        queryWrapper.orderByDesc(DaMarket::getCreateTime);
        return this.page(param.getPage(),queryWrapper);
    }

}
