package com.xqxy.dr.modular.powerplant.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.dr.modular.powerplant.entity.DaPowerplant;
import com.xqxy.dr.modular.powerplant.mapper.DaPowerplantMapper;
import com.xqxy.dr.modular.powerplant.service.DaPowerplantService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 电厂档案 服务实现类
 * </p>
 *
 * @author xiao jun
 * @since 2021-10-21
 */
@Service
public class DaPowerplantServiceImpl extends ServiceImpl<DaPowerplantMapper, DaPowerplant> implements DaPowerplantService {

    @Override
    public Page<DaPowerplant> page(DaPowerplant param) {
        LambdaQueryWrapper<DaPowerplant> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(param)) {
            // 根据名稱查询
            if (ObjectUtil.isNotEmpty(param.getName())) {
                queryWrapper.like(DaPowerplant::getName, param.getName());
            }
            // 根据编码查询
            if (ObjectUtil.isNotEmpty(param.getNum())) {
                queryWrapper.like(DaPowerplant::getNum, param.getNum());
            }
            // 根据编码查询
            if (ObjectUtil.isNotEmpty(param.getStatus())) {
                queryWrapper.eq(DaPowerplant::getStatus, param.getStatus());
            }
        }
        //倒叙排列，最新的在前面
        queryWrapper.orderByDesc(DaPowerplant::getCreateTime);
        return this.page(param.getPage(),queryWrapper);
    }

    @Override
    public String getNum(String tableName) {
        return this.baseMapper.getNum(tableName);
    }
}
