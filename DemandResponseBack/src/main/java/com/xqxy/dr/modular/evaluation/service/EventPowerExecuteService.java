package com.xqxy.dr.modular.evaluation.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.dr.modular.evaluation.entity.EventPowerExecute;
import com.xqxy.dr.modular.evaluation.param.EventPowerExecuteParam;

import java.util.List;

/**
 * <p>
 * 用户执行曲线 服务类
 * </p>
 *
 * @author xiao jun
 * @since 2021-07-04
 */
public interface EventPowerExecuteService extends IService<EventPowerExecute> {

    /**
     * 查询负荷详情
     * @param executeParam
     * @return
     */
    EventPowerExecute detail(EventPowerExecuteParam executeParam);

    /**
     * @description: 次日负荷详情
     * @param:
     * @return:
     * @author: huxingxing
     * @date: 2021/7/8 20:23
     */
    List<EventPowerExecute> list(EventPowerExecuteParam executeParam);

    /**
     * @description: 次日负荷导出
     * @param:
     * @return:
     * @author: PengChuqing
     * @date: 2021/7/4 20:23
     */
    void export(EventPowerExecuteParam executeParam);

}
