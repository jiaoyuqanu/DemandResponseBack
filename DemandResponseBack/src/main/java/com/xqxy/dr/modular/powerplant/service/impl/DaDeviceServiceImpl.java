package com.xqxy.dr.modular.powerplant.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.dr.modular.powerplant.entity.DaDevice;
import com.xqxy.dr.modular.powerplant.mapper.DaDeviceMapper;
import com.xqxy.dr.modular.powerplant.service.DaDeviceService;
import org.springframework.stereotype.Service;

/**
 * 设备档案 服务实现类
 * @author lixiaojun
 * @since 2021-10-21
 */
@Service
public class DaDeviceServiceImpl extends ServiceImpl<DaDeviceMapper, DaDevice> implements DaDeviceService {

    @Override
    public Page<DaDevice> page(DaDevice param) {
        LambdaQueryWrapper<DaDevice> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(param)) {
            // 根据名稱查询
            if (ObjectUtil.isNotEmpty(param.getDeviceName())) {
                queryWrapper.like(DaDevice::getDeviceName, param.getDeviceName());
            }
            // 根据编码查询
            if (ObjectUtil.isNotEmpty(param.getJhzyId())) {
                queryWrapper.like(DaDevice::getJhzyId, param.getJhzyId());
            }
        }
        //倒叙排列，最新的在前面
        queryWrapper.orderByDesc(DaDevice::getCreateTime);
        return this.page(param.getPage(),queryWrapper);
    }

}
