package com.xqxy.dr.modular.powerplant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.dr.modular.powerplant.DTO.DrDaAggregatorElectricityDTO;
import com.xqxy.dr.modular.powerplant.entity.DrDaAggregator;
import com.xqxy.dr.modular.powerplant.entity.DrDaAggregatorElectricity;

/**
 * <p>
 * 目标聚合商表 Mapper 接口
 * </p>
 *
 * @author liqirui
 * @since 2021-12-09
 */
public interface DrDaAggregatorElectricityMapper extends BaseMapper<DrDaAggregatorElectricity> {

    Page<DrDaAggregator> pageAggregatorByElectricityId(Page<DrDaAggregator> page, DrDaAggregatorElectricityDTO electricityDTO);
}
