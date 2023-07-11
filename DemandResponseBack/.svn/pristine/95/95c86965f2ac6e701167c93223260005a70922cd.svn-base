package com.xqxy.dr.modular.powerplant.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.dr.modular.powerplant.entity.DrDaAggregatorPrice;
import com.xqxy.dr.modular.powerplant.param.DrDaAggregatorPriceAddParam;
import com.xqxy.dr.modular.powerplant.param.DrDaAggregatorPriceParam;

/**
 * <p>
 * 负荷聚合商报价信息 服务类
 * </p>
 *
 * @author czj
 * @since 2021-12-09
 */
public interface DrDaAggregatorPriceService extends IService<DrDaAggregatorPrice> {
    Page<DrDaAggregatorPrice> page(DrDaAggregatorPriceParam param);

    void add(DrDaAggregatorPriceAddParam drDaAggregatorPriceAddParam);

    void submitInfo(DrDaAggregatorPriceParam param);

}
