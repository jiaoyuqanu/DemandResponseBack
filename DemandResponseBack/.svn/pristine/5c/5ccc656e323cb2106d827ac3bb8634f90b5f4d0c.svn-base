package com.xqxy.dr.modular.event.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.dr.modular.baseline.entity.BaseLine;
import com.xqxy.dr.modular.event.entity.BaselineLibrary;
import com.xqxy.dr.modular.event.mapper.BaselineLibraryMapper;
import com.xqxy.dr.modular.event.param.EventParam;
import com.xqxy.dr.modular.event.service.BaselineLibraryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author PengChuqing
 * @since 2021-10-18
 */
@Service
public class BaselineLibraryServiceImpl extends ServiceImpl<BaselineLibraryMapper, BaselineLibrary> implements BaselineLibraryService {

    @Override
    public BaselineLibrary getByDateAndPeriod(LocalDate regulateDate, String startTime, String endTime) {
        LambdaUpdateWrapper<BaselineLibrary> queryWrapper = new LambdaUpdateWrapper<>();
        queryWrapper.eq(BaselineLibrary::getGenerateDate,regulateDate);
        queryWrapper.le(BaselineLibrary::getStartPeriod,startTime);
        queryWrapper.ge(BaselineLibrary::getEndPeriod,endTime);
        return this.getOne(queryWrapper);
    }

    @Override
    public Page<BaselineLibrary> pageEventBaseline(EventParam eventParam) {
        LambdaQueryWrapper<BaselineLibrary> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.le(BaselineLibrary::getStartPeriod,eventParam.getStartTime());
        queryWrapper.ge(BaselineLibrary::getEndPeriod,eventParam.getEndTime());
        return this.page(eventParam.getPage(),queryWrapper);
    }
}
