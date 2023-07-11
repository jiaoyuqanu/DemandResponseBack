package com.xqxy.dr.modular.powerplant.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.dr.modular.powerplant.entity.DaUser;

/**
 * 虚拟电厂-用户档案 服务类
 * @author lixiaojun
 * @since 2021-10-21
 */
public interface DaUserService extends IService<DaUser> {

    /**
     * 查询用户档案
     * @param param 查询参数
     * @return 查询分页结果
     * @date 2021-10-21 15:49
     */
    Page<DaUser> page(DaUser param);

}
