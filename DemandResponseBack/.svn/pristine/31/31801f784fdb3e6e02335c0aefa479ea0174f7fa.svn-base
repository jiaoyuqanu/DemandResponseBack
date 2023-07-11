package com.xqxy.dr.modular.subsidy.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.dr.modular.subsidy.entity.SubsidyMonthlyCust;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.dr.modular.subsidy.param.SubsidyMonthlyCustParam;

/**
 * <p>
 * 月补贴 服务类
 * </p>
 *
 * @author hu xingxing
 * @since 2021-10-30
 */
public interface SubsidyMonthlyCustService extends IService<SubsidyMonthlyCust> {

    //月补贴分页查询
    Page<SubsidyMonthlyCust> pageSubsidyMonthly(SubsidyMonthlyCustParam subsidyMonthlyCustParam);

    //月补贴更新
    void subsidyMonthlyUpd(SubsidyMonthlyCustParam subsidyMonthlyCustParam);

    //客户月补贴导出
    void exportCustMonthlySubsidy(SubsidyMonthlyCustParam subsidyMonthlyCustParam);
}
