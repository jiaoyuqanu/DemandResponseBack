package com.xqxy.dr.modular.subsidy.service;

import com.xqxy.dr.modular.subsidy.entity.ConsSubsidyDaily;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.dr.modular.subsidy.param.ConsSubsidyDailyParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.dr.modular.subsidy.result.DailySubsidyInfo;
import com.xqxy.dr.modular.subsidy.result.MySubsidyInfo;

import java.time.LocalDate;

/**
 * <p>
 * 日补贴 服务类
 * </p>
 *
 * @author Shen
 * @since 2021-10-21
 */
public interface ConsSubsidyDailyService extends IService<ConsSubsidyDaily> {

    /**
     * 用户日补贴分页查询
     *
     * @param consSubsidyDailyParam
     * @return
     * @author hu xingxing
     * @date 2021-10-21 15:09
     */
    Page<DailySubsidyInfo> dailySubsidyPage(ConsSubsidyDailyParam consSubsidyDailyParam);

    /**
     * 用户日补贴导出
     *
     * @param consSubsidyDailyParam
     * @return
     * @author hu xingxing
     * @date 2021-10-21 15:09
     */
    void exportDailySubsidy(ConsSubsidyDailyParam consSubsidyDailyParam);

    /**
     * 根据用户标识和日期进行用户日补贴查询
     *
     * @param
     * @return
     * @author hu xingxing
     * @date 2021-10-25 10:20
     */
    ConsSubsidyDaily getConsDailySubsidy(String consId, LocalDate subsidyDate);

    /**
     * 我的补贴--新版
     *
     * @param consSubsidyDailyParam
     * @return
     * @author hu xingxing
     * @date 2021-10-21 15:09
     */
    Page<MySubsidyInfo> mySubsidy(ConsSubsidyDailyParam consSubsidyDailyParam);


}
