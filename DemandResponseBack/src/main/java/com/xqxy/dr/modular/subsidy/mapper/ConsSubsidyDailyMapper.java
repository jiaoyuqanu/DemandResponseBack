package com.xqxy.dr.modular.subsidy.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.dr.modular.subsidy.entity.ConsSubsidyDaily;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xqxy.dr.modular.subsidy.param.ConsSubsidyDailyParam;
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
public interface ConsSubsidyDailyMapper extends BaseMapper<ConsSubsidyDaily> {

    List<DailySubsidyInfo> getConsDailySubsidy(Map<String, Object> map);

    Page<DailySubsidyInfo> getConsDaily(Page<Object> defaultPage, Map<String, Object> map);

    ConsSubsidyDaily consCountAmount(ConsSubsidyDailyParam consSubsidyDailyParam);

    void setConsSubsidyDailySettlementNo(ConsSubsidyDailyParam consSubsidyDailyParam);

    List<ConsSubsidyDaily> getConsAmountBySettlementNo(ConsSubsidyDailyParam consSubsidyDailyParam);

    List<ConsSubsidyDaily> getConsSubsidyDailiesByConsIdAndSettlementNo(ConsSubsidyDailyParam consSubsidyDailyParam);

    Page<MySubsidyInfo> consMySubsidy(Page<Object> defaultPage, Map<String, Object> map);

    /**
     * 根据日期查询日补贴汇总
     * @param date
     * @return
     */
    List<ConsSubsidyDaily> getSubsidyDailyTotal(String date);

    List<ConsSubsidyDaily> getSubsidyDailyTotalByCondition(@Param("subsidyDate") String date,@Param("consId") String consId);

    /**
     * 根据日期查询所有日补贴事件项目关系
     * @param date
     * @return
     */
    List<ConsSubsidyDaily> getAllSubsidyDailyByDate(String date);
}
