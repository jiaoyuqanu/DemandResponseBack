package com.xqxy.dr.modular.event.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.baseline.entity.BaseLine;
import com.xqxy.dr.modular.event.VO.EventVO;
import com.xqxy.dr.modular.event.entity.BaselineLibrary;
import com.xqxy.dr.modular.event.entity.Event;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.dr.modular.event.param.EventParam;
import com.xqxy.dr.modular.workbench.VO.ContractProjecttDetailVO;
import com.xqxy.dr.modular.workbench.param.WorkbenchParam;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 需求响应事件 服务类
 * </p>
 *
 * @author PengChuqing
 * @since 2021-10-08
 */
public interface EventService extends IService<Event> {

    /**
     * @description: 事件分页查询
     * @param: eventParam 查询参数
     * @return: 查询分页结果
     * @author: PengChuqing
     * @date: 2021/10/8 10:59
     */
    Page<Event> page(EventParam eventParam);

    /**
     * @description: 需求响应事件列表
     * @param:
     * @return:
     * @author: PengChuqing
     * @date: 2021/10/8 14:43
     */
    Object list(EventParam EventParam);

    /**
     * @description: 需求响应事件列表
     * @param:
     * @return:
     * @author: PengChuqing
     * @date: 2021/10/8 14:43
     */
    Object listToPlan(EventParam EventParam);


    /**
     * @description: 需求响应事件删除
     * @param:
     * @return:
     * @author: PengChuqing
     * @date: 2021/10/8 14:43
     */
    void delete(EventParam EventParam);

    /**
     * @description: 需求响应事件添加
     * @param:
     * @return:
     * @author: PengChuqing
     * @date: 2021/10/8 14:43
     */
    Long add(EventParam EventParam);

    /**
     * @description: 需求响应事件修改
     * @param EventParam
     * @return
     */
    Long update(EventParam EventParam);

    /**
     * @description: 需求响应事件提前结束
     * @param eventParam
     * @return
     */
    ResponseData updateEventAdvance(EventParam eventParam);

    /**
     * @description: 获取当前年份所有事件的最大编号
     * @param:
     * @return:
     * @author: PengChuqing
     * @date: 2021/10/8 15:27
     */
    Long getMaxNo(String year);

    /**
     * @description: 事件发布
     * @param:
     * @return:
     * @author: PengChuqing
     * @date: 2021/10/9 14:16
     */
    void release(Long eventId,String deadlineTime,String replySource,Integer regulateMultiple,Integer endCondition);

    List<Long> getEventIdBy(Long eventNo,Long reponseType);

    Event detail(Long eventId);

    /**
     * @description: 二次邀约
     * @param:
     * @return:
     * @author: PengChuqing
     * @date: 2021/11/3 10:18
     */
    void secondInvitation(Long eventId, String deadlineTime);


    Event detailBy(Long eventId,String consId);

    Event detail(EventParam eventParam);

    /**
     * @description: 基线时段范围大于事件时间范围的分页
     * @param:
     * @return:
     * @author: PengChuqing
     * @date: 2021/11/18 15:03
     */
    Page<BaselineLibrary> pageEventBaseline(EventParam eventParam);

    /**
     * 指定日期是否响应日
     * @param cdrDate
     * @return
     */
    boolean judgeByStartDate(LocalDate cdrDate);

    List<Event> listBySettlementNo(String settlementNo);

    void revokeEvent(String eventId);

    /**
     * 工作台事件信息
     * @param eventId
     * @return
     */
    EventVO eventDetail(Long eventId);

    String parseEventSmsContent(Long eventId, String smsContent);


    /**
     * 修改 事件电力缺口
     * @param eventParam
     * @return
     */
    void editEventRegulateCap(EventParam eventParam);
}
