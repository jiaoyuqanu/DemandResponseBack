package com.xqxy.dr.modular.powerplant.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.dr.modular.powerplant.entity.DaUser;
import com.xqxy.dr.modular.powerplant.mapper.DaUserMapper;
import com.xqxy.dr.modular.powerplant.service.DaUserService;
import org.springframework.stereotype.Service;

/**
 * 虚拟电厂-用户档案 服务实现类
 * @author lixiaojun
 * @since 2021-10-21
 */
@Service
public class DaUserServiceImpl extends ServiceImpl<DaUserMapper, DaUser> implements DaUserService {

    @Override
    public Page<DaUser> page(DaUser param) {
        LambdaQueryWrapper<DaUser> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(param)) {
            // 根据用户名稱查询
            if (ObjectUtil.isNotEmpty(param.getAccountName())) {
                queryWrapper.like(DaUser::getName, param.getAccountName());
            }
            // 根据銀行账号查询
            if (ObjectUtil.isNotEmpty(param.getAccount())) {
                queryWrapper.like(DaUser::getAccount, param.getAccount());
            }
            // 根据城市查询
            if (ObjectUtil.isNotEmpty(param.getPhone())) {
                queryWrapper.like(DaUser::getPhone, param.getPhone());
            }
            // 根据机组id
            if (ObjectUtil.isNotEmpty(param.getJzId())) {
                queryWrapper.eq(DaUser::getJzId, param.getJzId());
            }
            // 根据电厂id查询
            if (ObjectUtil.isNotEmpty(param.getPowerplantId())) {
                queryWrapper.eq(DaUser::getPowerplantId, param.getPowerplantId());
            }
            // 根据电厂id查询
            if (ObjectUtil.isNotEmpty(param.getJzName())) {
                queryWrapper.like(DaUser::getJzName, param.getJzName());
            }
            // 根据电厂id查询
            if (ObjectUtil.isNotEmpty(param.getPowerplantName())) {
                queryWrapper.like(DaUser::getPowerplantName, param.getPowerplantName());
            }

        }
        //倒叙排列，最新的在前面
        queryWrapper.orderByDesc(DaUser::getCreateTime);
        return this.page(param.getPage(),queryWrapper);
    }
}
