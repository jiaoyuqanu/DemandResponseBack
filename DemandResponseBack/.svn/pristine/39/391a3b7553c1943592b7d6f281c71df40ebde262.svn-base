package com.xqxy.dr.modular.subsidy.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.dr.modular.subsidy.entity.SubsidyMonthly;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.dr.modular.subsidy.param.SubsidyMonthlyParam;

/**
 * <p>
 * 月补贴 服务类
 * </p>
 *
 * @author hu xingxing
 * @since 2021-10-28
 */
public interface SubsidyMonthlyService extends IService<SubsidyMonthly> {

    //月补贴分页查询
    Page<SubsidyMonthly> pageSubsidyMonthly(SubsidyMonthlyParam subsidyMonthlyParam);

    //月补贴更新
    void subsidyMonthlyUpd(SubsidyMonthlyParam subsidyMonthlyParam);

    //用户月补贴导出
    void exportConsMonthlySubsidy(SubsidyMonthlyParam subsidyMonthlyParam);
}
