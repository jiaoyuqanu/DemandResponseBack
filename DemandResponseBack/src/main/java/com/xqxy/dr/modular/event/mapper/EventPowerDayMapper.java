package com.xqxy.dr.modular.event.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xqxy.dr.modular.event.entity.EventPowerDay;
import com.xqxy.dr.modular.event.param.EventParam;
import com.xqxy.dr.modular.event.result.EventPowerResult;

import java.util.List;

/**
 * <p>
 * 日冻结事件执行曲线 Mapper 接口
 * </p>
 *
 * @author liqirui
 * @since 2022-04-29
 */
public interface EventPowerDayMapper extends BaseMapper<EventPowerDay> {

    /**
     * 根据事件id 和 时间 查询冻结曲线
     * @param eventParam
     * @return
     */
    EventPowerResult getEventPowerYesterDay(EventParam eventParam);

    /**
     * 根据 org 和 event 查询
     * @param eventParam
     * @return
     */
    List<EventPowerResult> getEventPowerByEventAndOrg(EventParam eventParam);
}
