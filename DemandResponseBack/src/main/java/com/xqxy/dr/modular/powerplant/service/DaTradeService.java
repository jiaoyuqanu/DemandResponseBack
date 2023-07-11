package com.xqxy.dr.modular.powerplant.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.dr.modular.powerplant.entity.DaTrade;

/**
 * 交易提交表 服务类
 * @author lixiaojun
 * @since 2021-11-09
 */
public interface DaTradeService extends IService<DaTrade> {

    /**
     * 查询
     * @param param 查询参数
     * @return 查询分页结果
     * @date 2021-10-21 15:49
     */
    Page<DaTrade> page(DaTrade param);

}
