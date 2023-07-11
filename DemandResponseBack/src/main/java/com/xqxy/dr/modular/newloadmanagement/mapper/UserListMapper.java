package com.xqxy.dr.modular.newloadmanagement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.dr.modular.newloadmanagement.entity.Drevent;
import com.xqxy.dr.modular.newloadmanagement.vo.EventLoadVo;
import com.xqxy.dr.modular.newloadmanagement.vo.ParamsVo;
import com.xqxy.dr.modular.newloadmanagement.vo.UserListVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description 未到位用户清单dao && 需求响当前时段执行事件及实时执行情况dao
 * @Author Rabbit
 * @Date 2022/6/10 10:34
 */
public interface UserListMapper extends BaseMapper<UserListVo> {
    /**
     * 查询未到位用户列表，返回分页对象以及每个用户最新的监测记录
     *
     * @param page        由于mybatis-plus的分页插件，所以需要传入Page对象
     * @param data        组织代码本级以及下级 (ORG_NO)
     * @param drEventList 事件id列表
     * @return List<UserListVo> 未到位用户列表
     * @author Rabbit
     */
    List<UserListVo> userListInfo(@Param("page") Page<UserListVo> page, @Param("data") List<String> data, @Param("drEventList") List<Drevent> drEventList);

    /**
     * 查询当前时间在执行的时间段内的事件id列表
     *
     * @param date 日期 yyyy-MM-dd
     * @param time 时间 HH:mm
     * @return List<Drevent> 事件列表
     * @author Rabbit
     */
    List<Drevent> eventList(@Param("date") String date, @Param("time") String time);

    /**
     * 应邀负荷 应邀户数（要添加条件 dr_plan_cons.IMPLEMENT = 'Y'） 应邀率
     *
     * @param data    组织代码本级以及下级 (ORG_NO)
     * @param eventId 事件id
     * @return EventLoadVo 事件及实时执行情况
     * @author Rabbit
     */
    EventLoadVo eventLoad(@Param("data") List<String> data, @Param("eventId") Long eventId);

    /**
     * 单独查询邀约户数（不需要添加条件 dr_plan_cons.IMPLEMENT = 'Y'）
     *
     * @param data    组织代码本级以及下级 (ORG_NO)
     * @param eventId 事件id
     * @return EventLoadVo 事件及实时执行情况
     * @author Rabbit
     */
    EventLoadVo eventLoadAll(@Param("data") List<String> data, @Param("eventId") Long eventId);

    /**
     * 实时负荷 执行率  orgId有值
     *
     * @param eventId 事件id
     * @param orgId   组织id 省或者市
     * @return EventLoadVo 事件及实时执行情况
     */
    EventLoadVo realTimeLoadAndExecutionRate(@Param("eventId") Long eventId, @Param("orgId") String orgId);

    /**
     * 实时负荷  orgId有值
     *
     * @param eventId 事件id
     * @param orgId   组织id 省或者市
     * @return ParamsVo
     */
    ParamsVo realTimeCapAndExecuteTime(@Param("eventId") Long eventId, @Param("orgId") String orgId);

    // /**
    //  * 实时负荷 执行率  orgId无值
    //  *
    //  * @param eventId 事件id
    //  * @param orgId   组织id 省或者市
    //  * @return EventLoadVo 事件及实时执行情况
    //  */
    // EventLoadVo realTimeLoadAndExecutionRate1(@Param("eventId") Long eventId, @Param("orgId") String orgId);
    //
    // /**
    //  * 实时负荷  orgId无值
    //  *
    //  * @param eventId 事件id
    //  * @param orgId   组织id 省或者市
    //  * @return ParamsVo
    //  */
    // ParamsVo realTimeCapAndExecuteTime1(@Param("eventId") Long eventId, @Param("orgId") String orgId);
}
