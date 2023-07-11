package com.xqxy.dr.modular.powerplant.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.dr.modular.powerplant.entity.DaPowerplant;

/**
 * 电厂档案 服务类
 * @author lixiaojun
 * @since 2021-10-21
 */
public interface DaPowerplantService extends IService<DaPowerplant> {

    /**
     * 查询电厂档案
     * @param param 查询参数
     * @return 查询分页结果
     * @date 2021-10-21 15:49
     */
    Page<DaPowerplant> page(DaPowerplant param);

    /**
     * 获取编号
     * @return String num
     */
    String getNum(String tableName);

}
