package com.xqxy.dr.modular.newloadmanagement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.dr.modular.newloadmanagement.entity.Drproject;
import com.xqxy.dr.modular.newloadmanagement.mapper.DrProjectMapper;
import com.xqxy.dr.modular.newloadmanagement.param.ComprehensiveIndicatorsParam;
import com.xqxy.dr.modular.newloadmanagement.service.DrProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
public class DrProjectServiceImpl extends ServiceImpl<DrProjectMapper,Drproject> implements DrProjectService {

    @Override
    public List<Long> queryUseCount(ComprehensiveIndicatorsParam comprehensiveIndicatorsParam) {
        QueryWrapper<Drproject> queryWrapper = new QueryWrapper();
        if (comprehensiveIndicatorsParam.getQueryDate() == null){
            LocalDate now = LocalDate.now();
            LocalDate localDate = now.withDayOfMonth(now.getDayOfMonth()-1);
            comprehensiveIndicatorsParam.setQueryDate(localDate);
            queryWrapper.ge("end_date",comprehensiveIndicatorsParam.getQueryDate());
        }
        else {
            queryWrapper.le("begin_date",comprehensiveIndicatorsParam.getQueryDate());
            queryWrapper.ge("end_date",comprehensiveIndicatorsParam.getQueryDate());
        }

        List<Drproject> list = this.list(queryWrapper);
        List<Long> collect = list.stream().map(Drproject::getProjectId).collect(Collectors.toList());
        return collect;
    }
}
