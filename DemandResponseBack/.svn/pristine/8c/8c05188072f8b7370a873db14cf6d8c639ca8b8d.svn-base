package com.xqxy.dr.modular.subsidy.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.dr.modular.subsidy.entity.CustSubsidy;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xqxy.dr.modular.subsidy.model.CustSubsidyModel;
import com.xqxy.dr.modular.subsidy.param.CustConsSubsidyPayParam;
import com.xqxy.dr.modular.subsidy.result.ConsSubsidyInfo;
import com.xqxy.dr.modular.subsidy.result.CustSubsidyByConsIds;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 客户事件激励费用 Mapper 接口
 * </p>
 *
 * @author hu xingxing
 * @since 2021-10-20
 */
public interface CustSubsidyMapper extends BaseMapper<CustSubsidy> {

    @Select("select event_id eventId, cust_id custId, subsidy_status subsidyStatus from dr_evalu_cust_task where regulate_date = #{date} group by eventId, custId")
    List<ConsSubsidyInfo> getCustEventSubsidies(@Param("date") LocalDate date);

    Page<CustSubsidy> custSubsidyPage(Page<Object> defaultPage, @Param("map") Map<String, Object> map);

    Page<CustSubsidy> custSubsidyByEventIdPage(Page<Object> defaultPage, Map<String, Object> map);

    Page<CustSubsidyByConsIds> custSubsidyByConsIdsPage(Page<Object> defaultPage,@Param("param") CustConsSubsidyPayParam param);

    /**
     * 根据 项目id 查询集成商 的补贴
     * @param projectId
     * @param integrator
     * @return
     */
    BigDecimal getProjectCustSubsidy(@Param("projectId") String projectId, @Param("integrator") Integer integrator);


    /**
     * 导出 根据事件标识进行客户事件补贴分页查询
     * @param map
     * @return
     */
    List<CustSubsidyModel> exportCustByEventId(@Param("map") Map<String, Object> map);
}
