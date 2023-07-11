package com.xqxy.dr.modular.event.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.dr.modular.event.entity.*;

import java.util.List;
import java.util.Map;

/**
 * 用户邀约信息(DrEventInvitation)表服务接口
 *
 * @author makejava
 * @since 2021-07-14 21:37:51
 */
public interface DrEventInvitationService extends IService<DrEventInvitation> {

    /**
     * 通过ID查询单条数据
     *
     * @param invitationId 主键
     * @return 实例对象
     */
    DrEventInvitation queryById(Long invitationId);

    /**
     * 统计数据
     *
     * @param 主键
     * @return 实例对象
     */
    List<DrConsCountEntity> costData(Long eventId, Long cityCode);

    /**
     * 统计数据
     *
     * @param
     * @return 实例对象
     */
    List<DrConsCountEntity> costDataImmediate(Long eventId, Long cityCode);

    /**
     * 统计数据-导出数据
     *
     * @param
     * @return 实例对象
     */
    void exportCostDataImmediate(String excelName, Long eventId, Long cityCode);

    /**
     * 负荷集成商 统计
     *
     * @param 主键
     * @return 实例对象
     */
    IPage<Map<String, Object>> fhjcCostData(long current, long size, Long eventId);

    /**
     * 当日负荷集成商 统计
     *
     * @param 主键
     * @return 实例对象
     */
    IPage<Map<String, Object>> fhjcCostImmediateData(long current, long size, Long eventId);

    /**
     * 负荷集成商 统计 导出
     *
     * @param eventId 主键
     * @return 实例对象
     */
    void exportfhjcCostData(Long eventId, String excelName);

    /**
     * 当日负荷集成商 统计 导出
     *
     * @param eventId 主键
     * @return 实例对象
     */
    void exportfhjcCostImmediateData(Long eventId, String excelName);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<DrEventInvitation> queryAllByLimit(int offset, int limit);

    /**
     * 效果评估明细 -分页
     */
    IPage<DrEventInvitationEntity> getDrEventInvitationEntity(long current, long size, Long cityCode, Long eventId, String consType);


    /**
     * 效果评估明细 -導出报表
     */
    void exprotDrEventInvitationEntity(Long cityCode, Long eventId, String excelName);

    /**
     * 当日效果评估明细 -分页
     */
    IPage<DrEventInvitationEntity> getDrEventInvitationImmediateEntity(long current, long size, Long cityCode, Long eventId, String consType);

    /**
     * 效果评估明细导出
     */
    void exportDrEventInvitationEntity(Long cityCode, Long eventId, String consType, String excelName);

    /**
     * 当日效果评估明细导出
     */
    void exportDrEventImmediateEntity(Long cityCode, Long eventId, String consType, String excelName);

    /**
     * 负荷集成商效果评估明细
     */
    IPage<DrEventInvEntity> getDrEventInvEntity(long current, long size, Long eventId, String consId);

    /**
     * 当日负荷集成商效果评估明细
     */
    IPage<DrEventInvEntity> getDrEventInvEntityImmediate(long current, long size, Long eventId, String consId);

    /**
     * 负荷集成商效果评估明细导出
     */
    void exportDrEventInvEntity(Long eventId, String consId, String excelName);

    /**
     * 负荷集成商效果评估明细导出
     */
    void exportDrEventInvImmediateEntity(Long eventId, String consId, String excelName);

    /**
     * 新增数据
     *
     * @param drEventInvitation 实例对象
     * @return 实例对象
     */
    DrEventInvitation insert(DrEventInvitation drEventInvitation);

    /**
     * 修改数据
     *
     * @param drEventInvitation 实例对象
     * @return 实例对象
     */
    DrEventInvitation update(DrEventInvitation drEventInvitation);

    /**
     * 通过主键删除数据
     *
     * @param invitationId 主键
     * @return 是否成功
     */
    boolean deleteById(Long invitationId);

    List<String> queryCountByEventID(Long eventId);

    List<Event> workBencworkBenchOverviewEventNamehOverview(String year);

    /**
     * 用户参与情况统计
     */
    IPage<DrEventInvitationUser> getEventUser(long current, long size, String startDate, String endDate, Long eventId);

    /**
     * 用户参与情况统计导出
     */
    void exprotEventUser(String startDate, String endDate, Long eventId);

    /**
     * 业务运行统计
     */
    IPage<DrEventInvitationBusiness> getEventBusiness(long current, long size, String startDate, String endDate, String eventType, Long provinceCode, Long cityCode);

    /**
     * 用户参与情况统计导出
     */
    void exprotBusinessOperation(String startDate, String endDate, String eventType, Long provinceCode, Long cityCode);

    /**
     * 事件效果评估
     */
    IPage<DrEventInvitationEffectEval> getEffectEval(long current, long size, String startDate, String endDate, Long eventId, Long provinceCode, Long cityCode);

    /**
     * 事件效果统计当日
     * @param current
     * @param size
     * @param startDate
     * @param endDate
     * @param eventId
     * @param provinceCode
     * @param cityCode
     * @return
     */
    IPage<DrEventInvitationEffectEval> getEffectEvalImmediate(long current, long size, String startDate, String endDate, Long eventId, Long provinceCode, Long cityCode);

    /**
     * 导出 -- 事件运行统计 -- 次日
     * @param startDate
     * @param endDate
     * @param eventId
     * @param provinceCode
     * @param cityCode
     */
    void exprotEffectEval(String startDate, String endDate, Long eventId, Long provinceCode, Long cityCode);

    /**
     * 导出 -- 事件运行统计 -- 当日
     * @param startDate
     * @param endDate
     * @param eventId
     * @param provinceCode
     * @param cityCode
     */
    void exprotEffectEvalImmediate(String startDate, String endDate, Long eventId, Long provinceCode, Long cityCode);

    /**
     * 事件效果评估 次日用户明细
     */
    IPage<DrEventInvitationEffectEvalDetail> getEffectUsersDetail(long current, long size, Long cityCode);
    IPage<DrEventInvitationEffectEvalDetail> getEffectUsersDetailImmediate(long current, long size, Long cityCode);
}
