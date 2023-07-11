package com.xqxy.dr.modular.subsidy.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.dr.modular.subsidy.entity.ConsSubsidyDaily;
import com.xqxy.dr.modular.subsidy.entity.CustSubsidyDaily;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xqxy.dr.modular.subsidy.param.CustSubsidyDailyParam;
import com.xqxy.dr.modular.subsidy.result.DailySubsidyInfo;
import com.xqxy.dr.modular.subsidy.result.MySubsidyInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 日补贴 Mapper 接口
 * </p>
 *
 * @author Shen
 * @since 2021-10-21
 */
public interface CustSubsidyDailyMapper extends BaseMapper<CustSubsidyDaily> {

    List<DailySubsidyInfo> getCustDailySubsidy(Map<String, Object> map);

    Page<DailySubsidyInfo> getCustDaily(Page<Object> defaultPage, Map<String, Object> map);

    CustSubsidyDaily custCountAmount(CustSubsidyDailyParam custSubsidyDailyParam);

    void setCustSubsidyDailySettlementNo(CustSubsidyDailyParam custSubsidyDailyParam);

    List<CustSubsidyDaily> getCustAmountBySettlementNo(CustSubsidyDailyParam custSubsidyDailyParam);

    List<CustSubsidyDaily> getCustSubsidyDailiesByCustIdAndSettlementNo(CustSubsidyDailyParam custSubsidyDailyParam);

    Page<MySubsidyInfo> custMySubsidy(Page<Object> defaultPage, Map<String, Object> map);

    /**
     * 根据日期查询日补贴汇总
     * @param date
     * @return
     */
    List<CustSubsidyDaily> getSubsidyDailyTotal(String date);

    /**
     * 根据日期查询日补贴汇总
     * @param date
     * @return
     */
    List<CustSubsidyDaily> getSubsidyDailyTotalByCondition(@Param("subsidyDate") String date,@Param("custId") Long custId);

    /**
     * 根据日期查询所有日补贴事件项目关系
     * @param date
     * @return
     */
    List<CustSubsidyDaily> getAllSubsidyDailyByDate(String date);
}
