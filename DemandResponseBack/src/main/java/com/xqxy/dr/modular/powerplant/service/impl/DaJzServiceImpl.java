package com.xqxy.dr.modular.powerplant.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.dr.modular.powerplant.entity.DaJz;
import com.xqxy.dr.modular.powerplant.mapper.DaJzMapper;
import com.xqxy.dr.modular.powerplant.service.DaJzService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 机组档案 服务实现类
 * </p>
 *
 * @author xiao jun
 * @since 2021-10-21
 */
@Service
public class DaJzServiceImpl extends ServiceImpl<DaJzMapper, DaJz> implements DaJzService {

    @Override
    public Page<DaJz> page(DaJz param) {
        LambdaQueryWrapper<DaJz> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(param)) {
            // 根据名稱查询
            if (ObjectUtil.isNotEmpty(param.getName())) {
                queryWrapper.like(DaJz::getName, param.getName());
            }
            // 根据编码查询
            if (ObjectUtil.isNotEmpty(param.getNum())) {
                queryWrapper.like(DaJz::getNum, param.getNum());
            }
            // 根据虚拟电厂id查询
            if (ObjectUtil.isNotEmpty(param.getPowerplantId())) {
                queryWrapper.eq(DaJz::getPowerplantId, param.getPowerplantId());
            }
            // 根据虚拟电厂名称查询
            if (ObjectUtil.isNotEmpty(param.getPowerplantName())) {
                queryWrapper.like(DaJz::getPowerplantName, param.getPowerplantName());
            }
        }
        //倒叙排列，最新的在前面
        queryWrapper.orderByDesc(DaJz::getCreateTime);
        return this.page(param.getPage(),queryWrapper);
    }
}
