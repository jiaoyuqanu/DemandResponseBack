package com.xqxy.dr.modular.powerplant.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.dr.modular.powerplant.entity.DaConsIncome;
import com.xqxy.dr.modular.powerplant.param.DaParam;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Caoj
 * @since 2021-12-08
 */
public interface DaConsIncomeService extends IService<DaConsIncome> {

    /**
     * 代理用户的分时收益信息
     * 
     * @param daParam 出清价格参数
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.xqxy.dr.modular.powerplant.entity.DaConsIncome>
     * @date 12/9/2021 11:06
     * @author Caoj
     */
    Page<DaConsIncome> page(DaParam daParam);
}
