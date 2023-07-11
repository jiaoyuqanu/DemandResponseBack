package com.xqxy.dr.modular.powerplant.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.dr.modular.powerplant.entity.TradeNotice;
import com.xqxy.dr.modular.powerplant.mapper.TradeNoticeMapper;
import com.xqxy.dr.modular.powerplant.service.TradeNoticeService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 交易公示 服务实现类
 * </p>
 *
 * @author xiao jun
 * @since 2021-10-22
 */
@Service
public class TradeNoticeServiceImpl extends ServiceImpl<TradeNoticeMapper, TradeNotice> implements TradeNoticeService {

    @Override
    public Page<TradeNotice> page(TradeNotice param) {
        LambdaQueryWrapper<TradeNotice> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(param)) {
            // 根据名稱查询
            if (ObjectUtil.isNotEmpty(param.getTitle())) {
                queryWrapper.like(TradeNotice::getTitle, param.getTitle());
            }
        }
        //倒叙排列，最新的在前面
        queryWrapper.orderByDesc(TradeNotice::getCreateTime);
        return this.page(param.getPage(),queryWrapper);
    }

}
