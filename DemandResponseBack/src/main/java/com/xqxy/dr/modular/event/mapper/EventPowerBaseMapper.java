package com.xqxy.dr.modular.event.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xqxy.dr.modular.event.entity.EventPowerBase;
import com.xqxy.dr.modular.newloadmanagement.vo.Point96Vo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 用户功率曲线 Mapper 接口
 * </p>
 *
 * @author xiao jun
 * @since 2021-05-13
 */
public interface EventPowerBaseMapper extends BaseMapper<EventPowerBase> {
    Point96Vo selectPointValue(@Param("eventId") Long eventId, @Param("orgId") String orgId);
}
