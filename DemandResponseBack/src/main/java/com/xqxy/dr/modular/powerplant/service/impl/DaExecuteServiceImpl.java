package com.xqxy.dr.modular.powerplant.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.dr.modular.powerplant.entity.DaExecute;
import com.xqxy.dr.modular.powerplant.mapper.DaExecuteMapper;
import com.xqxy.dr.modular.powerplant.service.DaExecuteService;
import org.springframework.stereotype.Service;

/**
 * 执行情况统计 服务实现类
 * @author lixiaojun
 * @since 2021-11-09
 */
@Service
public class DaExecuteServiceImpl extends ServiceImpl<DaExecuteMapper, DaExecute> implements DaExecuteService {

    @Override
    public Page<DaExecute> page(DaExecute param) {
        LambdaQueryWrapper<DaExecute> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(param)) {
            // 根据名稱查询
            if (ObjectUtil.isNotEmpty(param.getProvince())) {
                queryWrapper.like(DaExecute::getProvince, param.getProvince());
            }
            if (ObjectUtil.isNotEmpty(param.getCity())) {
                queryWrapper.like(DaExecute::getCity, param.getCity());
            }
            if (ObjectUtil.isNotEmpty(param.getDistrict())) {
                queryWrapper.like(DaExecute::getDistrict, param.getDistrict());
            }
        }
        //倒叙排列，最新的在前面
        queryWrapper.orderByDesc(DaExecute::getId);
        return this.page(param.getPage(),queryWrapper);
    }
    
}
