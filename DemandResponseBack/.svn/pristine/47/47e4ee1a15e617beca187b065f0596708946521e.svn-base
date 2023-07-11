package com.xqxy.dr.modular.powerplant.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.dr.modular.powerplant.entity.DaEvaluate;
import com.xqxy.dr.modular.powerplant.mapper.DaEvaluateMapper;
import com.xqxy.dr.modular.powerplant.service.DaEvaluateService;
import org.springframework.stereotype.Service;

/**
 * 评估表 服务实现类
 * @author lixiaojun
 * @since 2021-10-27
 */
@Service
public class DaEvaluateServiceImpl extends ServiceImpl<DaEvaluateMapper, DaEvaluate> implements DaEvaluateService {

    @Override
    public Page<DaEvaluate> page(DaEvaluate param) {
        LambdaQueryWrapper<DaEvaluate> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(param)) {
            // 根据名稱查询
            if (ObjectUtil.isNotEmpty(param.getTargetId())) {
                queryWrapper.like(DaEvaluate::getTargetId, param.getTargetId());
            }
            // 根据编码查询
            if (ObjectUtil.isNotEmpty(param.getTargetType())) {
                queryWrapper.like(DaEvaluate::getTargetType, param.getTargetType());
            }
        }
        //倒叙排列，最新的在前面
        queryWrapper.orderByDesc(DaEvaluate::getCreateTime);
        return this.page(param.getPage(),queryWrapper);
    }
}
