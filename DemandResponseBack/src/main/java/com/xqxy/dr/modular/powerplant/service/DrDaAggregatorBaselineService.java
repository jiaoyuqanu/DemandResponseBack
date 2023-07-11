package com.xqxy.dr.modular.powerplant.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.dr.modular.powerplant.entity.DrDaAggregatorBaseline;
import com.xqxy.dr.modular.powerplant.param.DrDaAggregatorBaseLineParam;
import com.xqxy.dr.modular.powerplant.param.DrDaAggregatorPriceAddParam;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 负荷聚合商次日负荷基线 服务类
 * </p>
 *
 * @author czj
 * @since 2021-12-09
 */
public interface DrDaAggregatorBaselineService extends IService<DrDaAggregatorBaseline> {
    Map<String,Object> getDrDaAggregatorBaseline(DrDaAggregatorBaseLineParam param);
    void submitBaseLine(DrDaAggregatorPriceAddParam param);
}
