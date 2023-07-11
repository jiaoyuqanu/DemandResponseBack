package com.xqxy.dr.modular.event.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.dr.modular.event.entity.ConsInvitation;
import com.xqxy.dr.modular.event.entity.CustInvitation;
import com.xqxy.dr.modular.event.entity.Event;
import com.xqxy.sys.modular.cust.entity.Cust;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 客户邀约 Mapper 接口
 * </p>
 *
 * @author PengChuqing
 * @since 2021-10-09
 */
public interface CustInvitationMapper extends BaseMapper<CustInvitation> {

    Page<CustInvitation> selectPageVo(Page<Object> defaultPage,@Param(Constants.WRAPPER) QueryWrapper<Object> queryWrapper);

    List<Cust> listInvitationCustomer(String eventId);

    /**
     * @description: 获取邀约用户的最大反馈截止时间
     * @param:
     * @return:
     * @author: PengChuqing
     * @date: 2021/10/30 14:58
     */
    LocalDateTime getMaxDeadlineTimeByEventIdAndState(Long eventId,String synchToPlan);

    @Select(" select max(deadline_time) from dr_cust_invitation where event_id = #{eventId}")
    LocalDateTime getMaxDeadlineTimeByEventId(Long eventId);

    List<Event> getMaxDeadlineTimeByCon(@Param("eventList") List<Event> eventList);

    List<CustInvitation> getConsInfoByEvent(long eventId);

    List<CustInvitation> getConsInfoByEventAndConsId(@Param("eventId")long eventId,@Param("custId")Long custId);

    List<CustInvitation> getConsInfoByEvents(@Param("eventIds") List<Event> eventIds);

    Page<CustInvitation> getReplyPageCust(Page<Object> defaultPage, @Param(Constants.WRAPPER) Wrapper queryWrapper);
}
