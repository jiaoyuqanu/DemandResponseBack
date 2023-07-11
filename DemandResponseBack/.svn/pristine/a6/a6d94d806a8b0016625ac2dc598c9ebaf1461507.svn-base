package com.xqxy.dr.modular.powerplant.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.dr.modular.powerplant.entity.DaMarketVoidancePrice;
import com.xqxy.dr.modular.powerplant.param.DaParam;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Caoj
 * @since 2021-12-08
 */
public interface DaMarketVoidancePriceService extends IService<DaMarketVoidancePrice> {

    /**
     * 市场出清价格信息分页
     *
     * @param daParam 出清价格参数
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.xqxy.dr.modular.powerplant.entity.DaMarketVoidancePrice>
     * @date 12/9/2021 11:04
     * @author Caoj
     */
    Page<DaMarketVoidancePrice> page(DaParam daParam);

    /**
     * 市场出清价格审核
     *
     * @param daParam 出清价格参数
     * @date 12/9/2021 11:04
     * @author Caoj
     */
    void verify(DaParam daParam);
}
