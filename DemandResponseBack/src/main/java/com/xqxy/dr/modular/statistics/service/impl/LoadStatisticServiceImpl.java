package com.xqxy.dr.modular.statistics.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.dr.modular.event.param.EventParam;
import com.xqxy.dr.modular.statistics.entity.LoadStatistic;
import com.xqxy.dr.modular.statistics.mapper.LoadStatisticMapper;
import com.xqxy.dr.modular.statistics.service.LoadStatisticService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author shi
 * @since 2022-02-22
 */
@Service
public class LoadStatisticServiceImpl extends ServiceImpl<LoadStatisticMapper, LoadStatistic> implements LoadStatisticService {

    public Page<LoadStatistic> page(EventParam eventParam) {
        LambdaQueryWrapper<LoadStatistic> loadStatisticLambdaQueryWrapper=new LambdaQueryWrapper<>();
        if(ObjectUtil.isNotNull(eventParam)) {
            if(ObjectUtil.isNotEmpty(eventParam.getProjectId())) {
                loadStatisticLambdaQueryWrapper.eq(LoadStatistic::getProjectId,eventParam.getProjectId());
            }
            if(ObjectUtil.isNotEmpty(eventParam.getRegulateDate())) {
                loadStatisticLambdaQueryWrapper.eq(LoadStatistic::getStatistilDate,eventParam.getRegulateDate());
            }
        }
        Page<LoadStatistic> page = this.page(eventParam.getPage(), loadStatisticLambdaQueryWrapper);

        List<LoadStatistic> loadStatistics = page.getRecords();
        if(!CollectionUtils.isEmpty(loadStatistics)){
            for (LoadStatistic loadStatistic : loadStatistics) {

                BigDecimal contractCap = loadStatistic.getContractCap();
                if(contractCap == null){
                    contractCap = BigDecimal.ZERO;
                }else {
                    contractCap = contractCap.divide(new BigDecimal(10000));
                }
                loadStatistic.setContractCap(contractCap);

                BigDecimal spareCapacity = loadStatistic.getSpareCapacity();
                if(spareCapacity == null){
                    spareCapacity = BigDecimal.ZERO;
                }else {
                    spareCapacity = spareCapacity.divide(new BigDecimal(10000));
                }
                loadStatistic.setSpareCapacity(spareCapacity);
            }
            page.setRecords(loadStatistics);
        }
        return  page;

    }
    public List<LoadStatistic> list(EventParam eventParam)
    {
        LambdaQueryWrapper<LoadStatistic> loadStatisticLambdaQueryWrapper=new LambdaQueryWrapper<>();
        if(ObjectUtil.isNotNull(eventParam))
        {
            if(ObjectUtil.isNotEmpty(eventParam.getProjectId()))
            {
                loadStatisticLambdaQueryWrapper.eq(LoadStatistic::getProjectId,eventParam.getProjectId());
            }
            if(ObjectUtil.isNotEmpty(eventParam.getRegulateDate()))
            {
                loadStatisticLambdaQueryWrapper.eq(LoadStatistic::getStatistilDate,eventParam.getRegulateDate());
            }
        }
        return  this.list(loadStatisticLambdaQueryWrapper);

    }


}
