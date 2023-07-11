package com.xqxy.dr.modular.evaluation.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.dr.modular.evaluation.entity.EventPowerExecuteImmediate;
import com.xqxy.dr.modular.evaluation.param.EventPowerExecuteImmediateParam;

import java.util.List;

/**
 * <p>
 * 用户执行曲线-实时 服务类
 * </p>
 *
 * @author xiao jun
 * @since 2021-07-04
 */
public interface EventPowerExecuteImmediateService extends IService<EventPowerExecuteImmediate> {

    /**
     * @description: 获取当日效果评估的负荷曲线
     * @param:
     * @return:
     * @author: PengChuqing
     * @date: 2021/7/4 17:02
     */
    EventPowerExecuteImmediate detail(EventPowerExecuteImmediateParam executeParam);

    /**
     * @description: 当日效果评估负荷曲线列表
     * @param:
     * @return:
     * @author: PengChuqing
     * @date: 2021/7/8 10:29
     */
    List<EventPowerExecuteImmediate> list(EventPowerExecuteImmediateParam executeParam);

    /**
     * @description: 当日效果评估负荷曲线
     * @param:
     * @return:
     * @author: PengChuqing
     * @date: 2021/7/4 20:50
     */
    void export(EventPowerExecuteImmediateParam executeImmediateParam);

}
