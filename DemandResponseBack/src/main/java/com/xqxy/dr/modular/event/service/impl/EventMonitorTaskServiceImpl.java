package com.xqxy.dr.modular.event.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xqxy.core.exception.ServiceException;
import com.xqxy.dr.modular.event.entity.Event;
import com.xqxy.dr.modular.event.entity.EventMonitorTask;
import com.xqxy.dr.modular.event.enums.EventMonitorTastException;
import com.xqxy.dr.modular.event.mapper.EventMonitorTaskMapper;
import com.xqxy.dr.modular.event.service.EventMonitorTaskService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 事件监测任务表 服务实现类
 * </p>
 *
 * @author Shen
 * @since 2021-10-26
 */
@Service
public class EventMonitorTaskServiceImpl extends ServiceImpl<EventMonitorTaskMapper, EventMonitorTask> implements EventMonitorTaskService {

    @Override
    public void saveFromEvent(Event event) {
        EventMonitorTask eventMonitorTask = new EventMonitorTask();
        eventMonitorTask.setEventId(event.getEventId());
        eventMonitorTask.setStartDate(event.getRegulateDate());
        eventMonitorTask.setEndDate(event.getRegulateDate());
        eventMonitorTask.setStartPeriod(event.getStartTime());
        eventMonitorTask.setEndPeriod(event.getEndTime());
        this.save(eventMonitorTask);
    }

    @Override
    public void updateConsIdByEventId(Long eventId, String consId) {
        LambdaQueryWrapper<EventMonitorTask> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EventMonitorTask::getEventId, eventId);
        EventMonitorTask eventMonitor = this.getOne(wrapper);
        if (ObjectUtil.isNull(eventMonitor)) {
            throw new ServiceException(EventMonitorTastException.EVENT_MONITOR_NOT_EXIST);
        }
        StringBuilder eventConsIds = new StringBuilder(ObjectUtil.isNull(eventMonitor.getConsIds()) ? "":eventMonitor.getConsIds());
        eventConsIds.append(ObjectUtil.isNotNull(eventMonitor.getConsIds()) && !eventMonitor.getConsIds().equals("") ? "," : "").append(consId);
        eventMonitor.setConsIds(eventConsIds.toString());
        this.updateById(eventMonitor);
    }
}
