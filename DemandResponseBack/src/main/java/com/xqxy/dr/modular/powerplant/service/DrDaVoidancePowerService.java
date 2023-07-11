package com.xqxy.dr.modular.powerplant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.dr.modular.powerplant.entity.DrDaVoidancePower;
import com.xqxy.dr.modular.powerplant.param.DrDaVoidanceParam;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 出清功率曲线 服务类
 * </p>
 *
 * @author liqirui
 * @since 2021-12-09
 */
public interface DrDaVoidancePowerService extends IService<DrDaVoidancePower> {
    Map<String, Object> getVoidancePower(DrDaVoidanceParam param);

}
