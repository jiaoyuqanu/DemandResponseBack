package com.xqxy.dr.modular.powerplant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.dr.modular.powerplant.DTO.DrDaAggregatorElectricityDTO;
import com.xqxy.dr.modular.powerplant.entity.DrDaAggregator;
import com.xqxy.dr.modular.powerplant.entity.DrDaAggregatorElectricity;
import com.xqxy.dr.modular.powerplant.mapper.DrDaAggregatorElectricityMapper;
import com.xqxy.dr.modular.powerplant.service.DrDaAggregatorElectricityService;
import com.xqxy.dr.modular.powerplant.service.DrDaAggregatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 目标聚合商表 服务实现类
 * </p>
 *
 * @author liqirui
 * @since 2021-12-09
 */
@Service
public class DrDaAggregatorElectricityServiceImpl extends ServiceImpl<DrDaAggregatorElectricityMapper, DrDaAggregatorElectricity> implements DrDaAggregatorElectricityService {

    @Resource
    private DrDaAggregatorElectricityMapper aggregatorElectricityMapper;

    @Autowired
    private DrDaAggregatorService drDaAggregatorService;

    /**
     * 查询目标聚合商 根据电力市场id
     * @param
     * @return
     */
    @Override
    public Page<DrDaAggregator> pageAggregatorByElectricityId(Page<DrDaAggregator> page, DrDaAggregatorElectricityDTO electricityDTO) {
        return aggregatorElectricityMapper.pageAggregatorByElectricityId(page,electricityDTO);
    }

    /**
     * 生成目标聚合商
     * @param
     * @return
     */
    @Override
    public void addAggregatorElectricity(Page<DrDaAggregator> page, DrDaAggregatorElectricityDTO electricityDTO) {
        // 根据电力市场id  查询是否已存在 对应  目标复合商
        QueryWrapper<DrDaAggregatorElectricity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("electric_market_id",electricityDTO.getElectricMarketId());
        List<DrDaAggregatorElectricity> list = this.list(queryWrapper);

        //不为空 则删除
        if(!CollectionUtils.isEmpty(list)){
            this.remove(queryWrapper);
        }

        //批量新增
        List<DrDaAggregatorElectricity> aggregatorElectricityList = new ArrayList<>();
        List<DrDaAggregator> aggregatorlist = drDaAggregatorService.list();
        if(!CollectionUtils.isEmpty(aggregatorlist)) {
            for (DrDaAggregator aggregator : aggregatorlist) {
                DrDaAggregatorElectricity drDaAggregatorElectricity = new DrDaAggregatorElectricity();
                drDaAggregatorElectricity.setAggregatorId(aggregator.getId());
                drDaAggregatorElectricity.setElectricMarketId(electricityDTO.getElectricMarketId());

                aggregatorElectricityList.add(drDaAggregatorElectricity);
            }
        }

        this.saveBatch(aggregatorElectricityList);
    }
}
