package com.xqxy.dr.modular.subsidy.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.dr.modular.subsidy.entity.CustSubsidy;
import com.xqxy.dr.modular.subsidy.entity.CustSubsidyPay;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.dr.modular.subsidy.param.CustConsSubsidyPayParam;
import com.xqxy.dr.modular.subsidy.param.CustSubsidyPayParam;
import com.xqxy.dr.modular.subsidy.param.EventCustSubsidyPayParam;
import com.xqxy.dr.modular.subsidy.result.CustSubsidyByConsIds;
import com.xqxy.dr.modular.subsidy.result.EventCustSubsidyPayInfo;

import java.util.List;

/**
 * <p>
 * 客户激励费用发放 服务类
 * </p>
 *
 * @author hu xingxing
 * @since 2021-10-20
 */
public interface CustSubsidyPayService extends IService<CustSubsidyPay> {

    List<CustSubsidyPay> custSubsidyPayByPayNo(String payNo);

    Page<CustSubsidyPay> custSubsidyPayPage(CustSubsidyPayParam custSubsidyPayParam);

    Page<CustSubsidy> custSubsidyPayDetails(CustSubsidyPayParam custSubsidyPayParam);

    Page<EventCustSubsidyPayInfo> eventCustSubsidyPayPage(EventCustSubsidyPayParam eventCustSubsidyPayParam);

    Page<CustSubsidyByConsIds> custConsSubsidyPayDetails(CustConsSubsidyPayParam custConsSubsidyPayParam);
}
