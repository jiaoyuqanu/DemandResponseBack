package com.xqxy.dr.modular.powerplant.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.dr.modular.powerplant.entity.DrDaVoidance;
import com.xqxy.dr.modular.powerplant.param.DrDaVoidanceParam;

/**
 * <p>
 *  出清 服务类
 * </p>
 *
 * @author czj
 * @since 2021-12-09
 */
public interface DrDaVoidanceService extends IService<DrDaVoidance> {
    Page<DrDaVoidance> page(DrDaVoidanceParam param);

}
