package com.xqxy.dr.modular.powerplant.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.dr.modular.powerplant.entity.DaMarket;

/**
 * <p>
 * 市场准入审核表 服务类
 * </p>
 *
 * @author lixiaojun
 * @since 2021-11-09
 */
public interface DaMarketService extends IService<DaMarket> {

    /**
     * 查询
     * @param param 查询参数
     * @return 查询分页结果
     * @date 2021-10-21 15:49
     */
    Page<DaMarket> page(DaMarket param);

}
