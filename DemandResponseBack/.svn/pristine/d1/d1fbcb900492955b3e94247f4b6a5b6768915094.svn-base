package com.xqxy.dr.modular.powerplant.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.dr.modular.powerplant.entity.DaConsEffect;
import com.xqxy.dr.modular.powerplant.param.DaParam;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Caoj
 * @since 2021-12-08
 */
public interface DaConsEffectService extends IService<DaConsEffect> {

    /**
     * 代理用户效果信息清单分页
     *
     * @param daParam id(客户标识)
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.xqxy.dr.modular.powerplant.entity.DaConsEffect>
     * @date 12/9/2021 11:05
     * @author Caoj
     */
    Page<DaConsEffect> page(DaParam daParam);

    /**
     * 代理用户效果导出
     *
     * @param daParam 取custId客户标识
     * @date 12/21/2021 14:26
     * @author Caoj
     */
    void consEffectExport(DaParam daParam);

    /**
     * 代理用户负荷曲线
     *
     * @param daParam 出清价格参数
     * @return
     * @date 12/9/2021 11:05
     * @author shi
     */
    DaConsEffect curve(DaParam daParam);
}
