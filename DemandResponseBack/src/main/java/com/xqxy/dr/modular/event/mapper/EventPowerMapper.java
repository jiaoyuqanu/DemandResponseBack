package com.xqxy.dr.modular.event.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xqxy.dr.modular.data.entity.ConsCurve;
import com.xqxy.dr.modular.event.entity.Event;
import com.xqxy.dr.modular.event.entity.EventPower;
import com.xqxy.dr.modular.event.entity.EventPowerSample;
import com.xqxy.dr.modular.event.param.EventParam;
import com.xqxy.dr.modular.event.result.EventPowerResult;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户功率曲线 Mapper 接口
 * </p>
 *
 * @author xiao jun
 * @since 2021-05-13
 */
public interface EventPowerMapper extends BaseMapper<EventPower> {


    EventPowerResult getEventPowerBase(EventParam eventParam);

    EventPowerResult getEventPowerToday(EventParam eventParam);

    Event getEvent(String regulateDate);

    List<ConsCurve> getCurveHistory(@Param("map") Map<String,Object> map);

    /**
     * 根据 org 和 event 查询 事件监测曲线
     * @param eventParam
     * @return
     */
    List<EventPowerResult> getEventPowerByEventAndOrg(EventParam eventParam);

}
