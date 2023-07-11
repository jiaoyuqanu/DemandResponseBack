package com.xqxy.dr.modular.subsidy.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.dr.modular.subsidy.entity.CustSubsidyPay;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xqxy.dr.modular.subsidy.param.EventCustSubsidyPayParam;
import com.xqxy.dr.modular.subsidy.result.EventCustSubsidyPayInfo;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * <p>
 * 客户激励费用发放 Mapper 接口
 * </p>
 *
 * @author hu xingxing
 * @since 2021-10-20
 */
public interface CustSubsidyPayMapper extends BaseMapper<CustSubsidyPay> {

    Page<CustSubsidyPay> custSubsidyPayPage(Page<Object> defaultPage, Map<String, Object> map);

    Page<EventCustSubsidyPayInfo> eventCustSubsidyPayPage(Page<Object> defaultPage,@Param("param") EventCustSubsidyPayParam param);
}
