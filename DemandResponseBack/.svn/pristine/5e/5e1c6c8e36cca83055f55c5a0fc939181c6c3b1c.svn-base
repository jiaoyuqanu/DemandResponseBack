package com.xqxy.dr.modular.powerplant.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.dr.modular.powerplant.entity.DaPlan;
import com.xqxy.dr.modular.powerplant.mapper.DaPlanMapper;
import com.xqxy.dr.modular.powerplant.service.DaPlanService;
import org.springframework.stereotype.Service;

/**
 * 公布出清计划表 服务实现类
 * @author lixiaojun
 * @since 2021-11-09
 */
@Service
public class DaPlanServiceImpl extends ServiceImpl<DaPlanMapper, DaPlan> implements DaPlanService {

    @Override
    public Page<DaPlan> page(DaPlan param) {
        LambdaQueryWrapper<DaPlan> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(param)) {
            // 根据名稱查询
            if (ObjectUtil.isNotEmpty(param.getName())) {
                queryWrapper.like(DaPlan::getName, param.getName());
            }
            // 根据编码查询
            if (ObjectUtil.isNotEmpty(param.getNum())) {
                queryWrapper.like(DaPlan::getNum, param.getNum());
            }
        }
        //倒叙排列，最新的在前面
        queryWrapper.orderByDesc(DaPlan::getId);
        return this.page(param.getPage(),queryWrapper);
    }

}
