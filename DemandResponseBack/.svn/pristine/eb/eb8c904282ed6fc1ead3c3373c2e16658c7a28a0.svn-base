package com.xqxy.dr.modular.event.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.dr.modular.event.entity.EventPowerSample;

import java.util.List;

/**
 * <p>
 * 样本负荷曲线 服务类
 * </p>
 *
 * @author xiao jun
 * @since 2021-05-13
 */
public interface EventPowerSampleService extends IService<EventPowerSample> {

    /**
     * @param eventPowerSampleParam
     */
    void delete(EventPowerSample eventPowerSampleParam);


    /**
     * @param eventId
     * @param consId
     * @return
     */
    List<EventPowerSample> list(Long eventId, String consId);

    /**
     * @param eventId
     * @param consId
     */
    void export(Long eventId, String consId);

    List<EventPowerSample> getEventSimpInfo();
}
