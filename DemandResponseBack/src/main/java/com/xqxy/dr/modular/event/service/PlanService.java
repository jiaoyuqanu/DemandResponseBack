package com.xqxy.dr.modular.event.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.dr.modular.event.entity.Plan;
import com.xqxy.dr.modular.event.entity.PlanCons;
import com.xqxy.dr.modular.event.param.PlanParam;
import com.xqxy.dr.modular.project.params.ExamineParam;

/**
 * <p>
 * 需求响应方案 服务类
 * </p>
 *
 * @author PengChuqing
 * @since 2021-10-08
 */
public interface PlanService extends IService<Plan> {

    /**
     * @description: 需求响应方案生成
     * @param: 
     * @return: 
     * @author: PengChuqing
     * @date: 2021/10/8 15:52
     */
    void generatePlan(PlanParam planParam);

    /**
     * @description: 根据基线剔除规则剔除用户
     * @param: 
     * @return: 
     * @author: PengChuqing
     * @date: 2021/10/18 16:38
     */
    Page<PlanCons> eliminate(PlanParam planParam);

    /**
     * @description: 独立用户批量邀约
     * @param:
     * @return:
     * @author: PengChuqing
     * @date: 2021/10/18 16:38
     */
    void batchInvation(PlanParam planParam);

    /**
     * @description: 集成商批量邀约
     * @param:
     * @return:
     * @author: PengChuqing
     * @date: 2021/10/18 16:38
     */
    void batchCustInvation(PlanParam planParam);

    /**
     * 用户剔除
     * @param planParam
     */
    void deleteInvation(PlanParam planParam);

    /**
     * 集成商剔除
     * @param planParam
     */
    void deleteCustInvation(PlanParam planParam);

    /**
     * @description: 手动剔除方案参与用户
     * @param:
     * @return:
     * @author: PengChuqing
     * @date: 2021/10/18 16:53
     */
    void eliminateManual(PlanParam planParam);

    /**
     * @description: 手动剔除方案用户确认页面
     * @param: 
     * @return: 
     * @author: PengChuqing
     * @date: 2021/10/18 17:06
     */
    Page<PlanCons> confirmManual(PlanParam planParam);

    /**
     * 根据事件id查询事件方案
     * @param eventId
     * @return
     */
    Plan getByEventId(Long eventId);

    /**
     * @description: 删除事件关联的旧的方案
     * @param: 
     * @return: 
     * @author: PengChuqing
     * @date: 2021/10/19 17:21
     */
    void removePlanByEventId(Long eventId);

    /**
     * @description: 方案编制提交审核
     * @param:
     * @return:
     * @author: PengChuqing
     * @date: 2021/10/19 17:22
     */
    void submit(PlanParam planParam);

    /**
     * @description: 方案编制审核
     * @param:
     * @return:
     * @author: PengChuqing
     * @date: 2021/10/19 17:22
     */
    void examine(ExamineParam examineParam);

}
