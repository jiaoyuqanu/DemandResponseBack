package com.xqxy.dr.modular.subsidy.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.dr.modular.subsidy.entity.ConsSubsidy;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.dr.modular.subsidy.entity.ConsSubsidyDaily;
import com.xqxy.dr.modular.subsidy.entity.ConsSubsidyPay;
import com.xqxy.dr.modular.subsidy.model.ConsSubsidyModel;
import com.xqxy.dr.modular.subsidy.param.ConsSubsidyDailyParam;
import com.xqxy.dr.modular.subsidy.param.ConsSubsidyParam;
import com.xqxy.dr.modular.subsidy.result.ConsNoSubsidy;
import com.xqxy.dr.modular.subsidy.result.DailySubsidyInfo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户事件激励费用 服务类
 * </p>
 *
 * @author hu xingxing
 * @since 2021-10-20
 */
public interface ConsSubsidyService extends IService<ConsSubsidy> {

    /**
     * @description: 根据事件标识和用户标识查询用户事件补贴
     * @param: evalTaskParam 查询参数
     * @return:
     * @author: hu xingxing
     * @date: 2021-10-20 10:00
     */
    ConsSubsidy getConsSubsidyByEventIdAndConsId(long eventId, String consId);


    /**
     * 根据事件标识和用户标识事件补贴分页查询
     * @param consSubsidyDailyParam
     * @return
     * @author shen
     * @date 2021-10-21 16:51
     */
    Page<ConsSubsidy> page(ConsSubsidyDailyParam consSubsidyDailyParam);

    /**
     * 用户日补贴分页详情
     *
     * @param consSubsidyDailyParam
     * @return
     * @author hu xingxing
     * @date 2021-10-21 15:09
     */
    Page<DailySubsidyInfo> dailySubsidyConsDetail(ConsSubsidyDailyParam consSubsidyDailyParam);

    /**
     * 客户日补贴分页详情
     *
     * @param consSubsidyDailyParam
     * @return
     * @author hu xingxing
     * @date 2021-10-21 15:09
     */
    Page<DailySubsidyInfo> dailySubsidyCustDetail(ConsSubsidyDailyParam consSubsidyDailyParam);

    BigDecimal dailySubsidyConsTotal(ConsSubsidyDailyParam consSubsidyDailyParam);

    BigDecimal dailySubsidyCustTotal(ConsSubsidyDailyParam consSubsidyDailyParam);


    /**
     * 根据用户标识查询事件补贴
     * @param consSubsidyDailyParam
     * @return
     * @author shen
     * @date 2021-10-27 09:55
     */
    List<ConsSubsidy> list(ConsSubsidyDailyParam consSubsidyDailyParam);


    /**
     * 根据事件标识进行用户事件补贴分页查询
     * @param consSubsidyParam
     * @return
     * @author hu xingxing
     * @date 2021-10-26 11:10
     */
    Page<ConsSubsidy> pageConsByEventId(ConsSubsidyParam consSubsidyParam);

    /**
     * 根据事件标识和客户标识进行代理用户事件补贴分页查询
     * @param consSubsidyParam
     * @return
     * @author hu xingxing
     * @date 2021-10-26 14:00
     */
    Page<ConsSubsidy> pageConsByEventIdAndCustId(ConsSubsidyParam consSubsidyParam);

    /**
     * 我的补贴--户号补贴
     * @param consSubsidyParam
     * @return
     * @author hu xingxing
     * @date 2021-10-26 14:00
     */
    Page<ConsNoSubsidy> consNoSubsidy(ConsSubsidyParam consSubsidyParam);


    Object consNoSubsidyInfo(ConsSubsidyParam consSubsidyParam);

    /**
     * 导出 -- 根据事件标识进行用户事件补贴分页查询
     * @param consSubsidyParam
     * @return
     * @author lqr
     * @date 2022-6-18 11:00
     */
    List<ConsSubsidyModel> exportConsByEventId(ConsSubsidyParam consSubsidyParam);
}
