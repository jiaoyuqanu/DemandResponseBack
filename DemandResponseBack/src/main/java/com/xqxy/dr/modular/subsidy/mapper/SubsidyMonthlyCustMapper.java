package com.xqxy.dr.modular.subsidy.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.dr.modular.subsidy.entity.SubsidyMonthlyCust;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xqxy.dr.modular.subsidy.result.CustSubsidyMonthlyInfo;
import com.xqxy.dr.modular.subsidy.result.MonthlySubsidyInfo;
import com.xqxy.sys.modular.cust.entity.Cust;
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
 * @since 2021-10-30
 */
public interface SubsidyMonthlyCustMapper extends BaseMapper<SubsidyMonthlyCust> {

    @Select("select cust_id custId,integrator, sum(settled_amount) settledAmount from dr_cust_subsidy_daily where substr(subsidy_date, 1, 7) = #{subsidyMonth} group by custId")
    List<CustSubsidyMonthlyInfo> getCustSubsidyMonthly(@Param("subsidyMonth") String subsidyMonth);

    @Select("select * from dr_subsidy_monthly_cust where subsidy_month = #{subsidyMonth}")
    List<SubsidyMonthlyCust> getMonthSubsidy(@Param("subsidyMonth") String subsidyMonth);

    @Select("select * from dr_cust where cust_name like concat('%', #{custName}, '%')")
    List<Cust> getCust(@Param("custName") String custName);

    List<MonthlySubsidyInfo> getCustMonthlySubsidy(Map<String, String> map);

    Page<SubsidyMonthlyCust> subsidyMonthlyCustPage(Page<Object> defaultPage, Map<String, Object> map);
}
