package com.xqxy.dr.modular.powerplant.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.dr.modular.powerplant.entity.DaOrgCurve;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 供电供电统调日负荷 服务类
 * </p>
 *
 * @author shi
 * @since 2021-12-08
 */
public interface DaOrgCurveService extends IService<DaOrgCurve> {

    /**
     * 供电统调日负荷信息
     *
     * @param
     * @date 10/26/2021 9:24
     * @author shi
     */
    Map<String, List<DaOrgCurve>> getCurve();
}
