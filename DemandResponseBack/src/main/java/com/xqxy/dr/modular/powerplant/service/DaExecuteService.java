package com.xqxy.dr.modular.powerplant.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.core.pojo.page.PageResult;
import com.xqxy.dr.modular.powerplant.entity.DaExecute;

/**
 * <p>
 * 执行情况统计 服务类
 * </p>
 *
 * @author lixiaojun
 * @since 2021-11-09
 */
public interface DaExecuteService extends IService<DaExecute> {

    /**
     * 查询
     * @param param 查询参数
     * @return 查询分页结果
     * @date 2021-10-21 15:49
     */
    Page<DaExecute> page(DaExecute param);

}
