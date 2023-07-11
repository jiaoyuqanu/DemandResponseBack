package com.xqxy.dr.modular.subsidy.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.dr.modular.subsidy.entity.ConsSubsidy;
import com.xqxy.dr.modular.subsidy.entity.ConsSubsidyPay;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.dr.modular.subsidy.param.ConsSubsidyPayParam;

import java.util.List;

/**
 * <p>
 * 用户激励费用发放 服务类
 * </p>
 *
 * @author hu xingxing
 * @since 2021-10-20
 */
public interface ConsSubsidyPayService extends IService<ConsSubsidyPay> {

    List<ConsSubsidyPay> consSubsidyPayByPayNo(String payNo);

    Page<ConsSubsidyPay> consSubsidyPayPage(ConsSubsidyPayParam consSubsidyPayParam);

    Page<ConsSubsidy> consSubsidyPayDetails(ConsSubsidyPayParam consSubsidyPayParam);
}
