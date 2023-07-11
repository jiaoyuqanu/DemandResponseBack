package com.xqxy.dr.modular.subsidy.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.dr.modular.subsidy.entity.ConsSubsidyPay;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Map;

/**
 * <p>
 * 用户激励费用发放 Mapper 接口
 * </p>
 *
 * @author hu xingxing
 * @since 2021-10-20
 */
public interface ConsSubsidyPayMapper extends BaseMapper<ConsSubsidyPay> {

    Page<ConsSubsidyPay> consSubsidyPayPage(Page<Object> defaultPage, Map<String, Object> map);
}
