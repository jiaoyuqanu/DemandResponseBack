package com.xqxy.dr.modular.event.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.dr.modular.data.entity.ConsCurve;
import com.xqxy.dr.modular.data.param.CustCurveParam;
import com.xqxy.dr.modular.event.entity.PlanCons;
import com.xqxy.dr.modular.event.entity.PlanCust;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.dr.modular.event.param.PlanConsParam;
import com.xqxy.dr.modular.event.param.PlanCustParam;

import java.util.List;

/**
 * <p>
 * 方案参与客户 服务类
 * </p>
 *
 * @author PengChuqing
 * @since 2021-10-09
 */
public interface PlanCustService extends IService<PlanCust> {

    /**
     * @description: 未剔除方案客户列表
     * @param: 
     * @return: 
     * @author: PengChuqing
     * @date: 2021/10/21 14:14
     */
    List<PlanCust> listNotDeleted(PlanCustParam planCustParam);

    /**
     * @description: 剔除方案参与客户
     * @param: 
     * @return: 
     * @author: PengChuqing
     * @date: 2021/11/1 15:01
     */
    void eliminateByCustIds(List<Long> aggreListByConsId, Long planId);

    List<PlanCust> list(PlanCustParam planCustParam);

    /**
     * @description: 执行监测-客户监测-分页
     * @param:
     * @return:
     * @author: PengChuqing
     * @date: 2021/10/27 10:06
     */
    Page<PlanCust> pageCustMonitor(PlanCustParam planCustParam);

    /**
     * 统计单个聚合商执行曲线
     * @param custCurveParam
     * @return
     */
    ConsCurve getCustCurveToday(CustCurveParam custCurveParam);

    /**
     * 单个聚合商历史曲线
     * @param custCurveParam
     * @return
     */
    ConsCurve getCustCurveHistory(CustCurveParam custCurveParam);

    List<PlanCust> getPlanNoDe(List<Long> eventIds);

}
