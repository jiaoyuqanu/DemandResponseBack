package com.xqxy.dr.modular.subsidy.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.dr.modular.subsidy.entity.SubsidyMonthly;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xqxy.dr.modular.subsidy.result.CustSubsidyMonthlyInfo;
import com.xqxy.dr.modular.subsidy.result.MonthlySubsidyInfo;
import com.xqxy.sys.modular.cust.entity.Cons;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 月补贴 Mapper 接口
 * </p>
 *
 * @author hu xingxing
 * @since 2021-10-28
 */
public interface SubsidyMonthlyMapper extends BaseMapper<SubsidyMonthly> {

    @Select("select cons_id consId, sum(settled_amount) settledAmount from dr_cons_subsidy_daily where substr(subsidy_date, 1, 7) = #{subsidyMonth} group by consId")
    List<CustSubsidyMonthlyInfo> getConsSubsidyMonthly(@Param("subsidyMonth") String subsidyMonth);

    @Select("select * from dr_subsidy_monthly where subsidy_month = #{subsidyMonth}")
    List<SubsidyMonthly> getMonthSubsidy(@Param("subsidyMonth") String subsidyMonth);

    @Select("select * from dr_cons where cons_name like concat('%', #{consName}, '%')")
    List<Cons> getCons(@Param("consName") String consName);

    List<MonthlySubsidyInfo> getConsMonthlySubsidy(Map<String, Object> map);

    Page<SubsidyMonthly> subsidyMonthlyPage(Page<Object> defaultPage, Map<String, Object> map);
}
