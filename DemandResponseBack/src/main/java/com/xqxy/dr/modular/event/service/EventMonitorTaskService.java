package com.xqxy.dr.modular.event.service;

import com.xqxy.dr.modular.event.entity.Event;
import com.xqxy.dr.modular.event.entity.EventMonitorTask;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 事件监测任务表 服务类
 * </p>
 *
 * @author Shen
 * @since 2021-10-26
 */
public interface EventMonitorTaskService extends IService<EventMonitorTask> {

    void saveFromEvent(Event event);


    /**
     * @description: 更新监测事件的关联用户
     * @param:
     * @return:
     * @author: PengChuqing
     * @date: 2021/5/27 15:25
     */
    void updateConsIdByEventId(Long eventId, String consId);
}
