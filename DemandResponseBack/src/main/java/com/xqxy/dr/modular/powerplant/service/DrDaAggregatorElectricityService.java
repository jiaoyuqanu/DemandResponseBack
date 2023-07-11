package com.xqxy.dr.modular.powerplant.service;

//import com.xqxy.dr.modular.powerplant.entity.DrDaAggregatorElectricity;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.dr.modular.powerplant.DTO.DrDaAggregatorElectricityDTO;
import com.xqxy.dr.modular.powerplant.entity.DrDaAggregator;
import com.xqxy.dr.modular.powerplant.entity.DrDaAggregatorElectricity;

/**
 * <p>
 * 目标聚合商表 服务类
 * </p>
 *
 * @author liqirui
 * @since 2021-12-09
 */
public interface DrDaAggregatorElectricityService extends IService<DrDaAggregatorElectricity> {


    /**
     * 查询目标聚合商 根据电力市场id
     * @param
     * @return
     */
    Page<DrDaAggregator> pageAggregatorByElectricityId(Page<DrDaAggregator> page, DrDaAggregatorElectricityDTO electricityDTO);

    /**
     * 生成目标聚合商
     * @param
     * @return
     */
    void addAggregatorElectricity(Page<DrDaAggregator> page, DrDaAggregatorElectricityDTO electricityDTO);
}
