package com.xqxy.dr.modular.subsidy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.dr.modular.project.entity.ConsContractInfo;
import com.xqxy.dr.modular.subsidy.entity.ConsSubsidy;
import com.xqxy.dr.modular.subsidy.entity.ConsSubsidyAppeal;
import com.xqxy.dr.modular.subsidy.model.ConsSubsidyModel;
import com.xqxy.dr.modular.subsidy.result.ConsNoSubsidy;
import com.xqxy.dr.modular.subsidy.result.ConsSubsidyInfo;
import com.xqxy.sys.modular.cust.entity.UserConsRela;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户事件激励费用 Mapper 接口
 * </p>
 *
 * @author hu xingxing
 * @since 2021-10-20
 */
public interface ConsSubsidyAppealMapper extends BaseMapper<ConsSubsidyAppeal> {


}
