package com.xqxy.dr.modular.data.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.dr.modular.data.entity.ConsCurveToday;
import com.xqxy.dr.modular.data.mapper.ConsCurveTodayMapper;
import com.xqxy.dr.modular.data.service.ConsCurveTodayService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户当日功率曲线 服务实现类
 * </p>
 *
 * @author xiao jun
 * @since 2021-06-03
 */
@Service
public class ConsCurveTodayServiceImpl extends ServiceImpl<ConsCurveTodayMapper, ConsCurveToday> implements ConsCurveTodayService {

    @Override
    public ConsCurveToday getCurveByConsIdAndDate(String consId, String dataDate ) {
        LambdaQueryWrapper<ConsCurveToday> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ConsCurveToday::getConsId, consId);
        queryWrapper.eq(ConsCurveToday::getDataDate, dataDate);
        return this.getOne(queryWrapper);
    }
}
