package com.xqxy.dr.modular.upload.mapper;

import com.xqxy.dr.modular.subsidy.entity.ConsSubsidyPay;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 补贴发放记录 Mapper 接口
 * </p>
 *
 */
@Mapper
public interface SubsidyPayMapper {

    List<ConsSubsidyPay> getConSubsidy();
}
