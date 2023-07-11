package com.xqxy.dr.modular.event.service;

import com.xqxy.dr.modular.event.entity.Event;
import com.xqxy.dr.modular.event.entity.EventPower;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.dr.modular.event.param.EventParam;
import com.xqxy.dr.modular.event.result.EventPowerResult;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 事件执行曲线 服务类
 * </p>
 *
 * @author Shen
 * @since 2021-10-26
 */
public interface EventPowerService extends IService<EventPower> {

    /**
     * @description: 获取事件的曲线
     * @param:
     * @return:
     * @author: huxingxing
     * @date: 2021/6/30 22:35
     */
    Map<String, Object> getEventPower(EventParam eventParam);

    void getPeriod(EventPowerResult basePower, Event event);
}
