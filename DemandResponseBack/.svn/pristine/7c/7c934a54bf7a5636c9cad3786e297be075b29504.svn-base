package com.xqxy.dr.modular.subsidy.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.dr.modular.subsidy.entity.CustSubsidy;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.dr.modular.subsidy.model.CustSubsidyModel;
import com.xqxy.dr.modular.subsidy.param.CustSubsidyDailyParam;
import com.xqxy.dr.modular.subsidy.param.CustSubsidyParam;

import java.util.List;

/**
 * <p>
 * 客户事件激励费用 服务类
 * </p>
 *
 * @author hu xingxing
 * @since 2021-10-20
 */
public interface CustSubsidyService extends IService<CustSubsidy> {

    /**
     * @description: 根据事件标识和用户标识查询客户事件激励费用
     * @param: eventId,custId 查询参数
     * @return:
     * @author: hu xingxing
     * @date: 2021-10-22 09:30
     */
    CustSubsidy getCustSubsidyByEventIdAndCustId(long eventId, long custId);


    /**
     * 根据事件标识和用户标识事件补贴分页查询
     * @param custSubsidyDailyParam
     * @return
     * @author shen
     * @date 2021-10-26 10:10
     */
    Page<CustSubsidy> page(CustSubsidyDailyParam custSubsidyDailyParam);


    /**
     * 根据条件查询事件补贴
     * @param custSubsidyDailyParam
     * @return
     * @author shen
     * @date 2021-10-27 09:58
     */
    List<CustSubsidy> list(CustSubsidyDailyParam custSubsidyDailyParam);


    /**
     * 根据事件标识进行客户事件补贴分页查询
     * @param custSubsidyParam
     * @return
     * @author hu xingxing
     * @date 2021-10-26 11:15
     */
    Page<CustSubsidy> pageCustByEventId(CustSubsidyParam custSubsidyParam);


    /**
     * 导出 根据事件标识进行客户事件补贴分页查询
     * @param custSubsidyParam
     * @return
     */
    List<CustSubsidyModel> exportCustByEventId(CustSubsidyParam custSubsidyParam);
}
