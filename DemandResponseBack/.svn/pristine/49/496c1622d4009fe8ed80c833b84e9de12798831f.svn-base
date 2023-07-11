package com.xqxy.dr.modular.powerplant.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.dr.modular.powerplant.entity.DrDaElectricBidNotice;
import com.xqxy.dr.modular.powerplant.mapper.DrDaElectricBidNoticeMapper;
import com.xqxy.dr.modular.powerplant.param.DrDaElectricBidNoticeParam;
import com.xqxy.dr.modular.powerplant.service.DrDaElectricBidNoticeService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 电力市场竞价公告信息 服务实现类
 * </p>
 *
 * @author liqirui
 * @since 2021-12-09
 */
@Service
public class DrDaElectricBidNoticeServiceImpl extends ServiceImpl<DrDaElectricBidNoticeMapper, DrDaElectricBidNotice> implements DrDaElectricBidNoticeService {

    @Override
    public Page<DrDaElectricBidNotice> page(DrDaElectricBidNoticeParam param) {
        LambdaQueryWrapper<DrDaElectricBidNotice> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotEmpty(param.getReleaseTime())) {
            queryWrapper.like(DrDaElectricBidNotice::getReleaseTime, param.getReleaseTime());
        }
        if (ObjectUtil.isNotEmpty(param.getElectricTypeCode())) {
            queryWrapper.eq(DrDaElectricBidNotice::getElectricTypeCode, param.getElectricTypeCode());
        }
        queryWrapper.orderByDesc(DrDaElectricBidNotice::getReleaseTime);
        return this.page(param.getPage(),queryWrapper);
    }
}
