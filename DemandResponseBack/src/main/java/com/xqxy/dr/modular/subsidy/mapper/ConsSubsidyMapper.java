package com.xqxy.dr.modular.subsidy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.dr.modular.project.entity.ConsContractInfo;
import com.xqxy.dr.modular.subsidy.entity.ConsSubsidy;
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
public interface ConsSubsidyMapper extends BaseMapper<ConsSubsidy> {

    @Select("select  \n" +
            "            uc.cust_id custId, ci.cons_id consId, ci.extract_ratio/100 extractRatio, t.subsidy_status subsidyStatus  \n" +
            "            from  \n" +
            "            dr_user_cons_rela uc, dr_cons_contract_info ci, dr_evalu_task t, dr_event e  \n" +
            "            where  \n" +
            "            uc.cons_no = ci.cons_id  \n" +
            "            and e.project_id = ci.project_id  \n" +
            "            and ci.cons_id = t.cons_id  \n" +
            "            and rela_type = 2  \n" +
            "            and e.event_id = #{eventId}  \n" +
            "            and t.event_id = #{eventId}\t\t\t\n" +
            "\t\t\t\t\tunion all \n" +
            "\t\t\t\t\tselect  \n" +
            "            uc.cust_id custId, ci.cons_id consId, ci.extract_ratio/100 extractRatio, t.subsidy_status subsidyStatus  \n" +
            "            from  \n" +
            "            dr_cons uc, dr_cons_contract_info ci, dr_evalu_task t, dr_event e  \n" +
            "            where  \n" +
            "            uc.id = ci.cons_id  \n" +
            "            and e.project_id = ci.project_id  \n" +
            "            and ci.cons_id = t.cons_id  \n" +
            "            and e.event_id = #{eventId}  \n" +
            "            and t.event_id = #{eventId}\n" +
            "\t\t\t\t\t\tand uc.cust_id is not null\t")
    List<ConsSubsidyInfo> getConsSubsidyInfo(@Param("eventId") long eventId);

    List<ConsSubsidyInfo> getConsSubsidyInfos(@Param("events") List<Long> events);

    @Select("select event_id eventId, cons_id consId, subsidy_status subsidyStatus from dr_evalu_task where regulate_date = #{date}")
    List<ConsSubsidyInfo> getConsEventSubsidies(@Param("date") LocalDate date);

    @Select("select " +
            "uc.cust_id custId, uc.cons_no consId, ct.subsidy_status subsidyStatus " +
            "from " +
            "dr_user_cons_rela uc, dr_evalu_task t, dr_evalu_cust_task ct " +
            "where " +
            "uc.cons_no = t.cons_id " +
            "and uc.cust_id = ct.cust_id " +
            "and t.event_id = #{eventId} " +
            "and ct.event_id = #{eventId} " +
            "and uc.cons_no = #{consId} " +
            "group by t.event_id, t.cons_id")
    ConsSubsidyInfo isCalDailySubsidy(@Param("eventId") long eventId, @Param("consId") String consId);

    @Select("select * from dr_user_cons_rela where cons_no = #{consId}")
    List<UserConsRela> getProxy(@Param("consId") String consId);

    @Select("select id, cust_id custId, cons_no consNo, rela_type relaType from dr_user_cons_rela where cust_id = #{custId}")
    List<UserConsRela> userConsRelies(@Param("custId") long custId);

    @Select("select extract_ratio extractRatio from dr_event e, dr_cons_contract_info c where e.project_id = c.project_id and e.event_id = #{eventId} and c.cons_id = #{consId}")
    ConsContractInfo getExtractRatio(@Param("eventId") long eventId, @Param("consId") String consId);

    Page<ConsSubsidy> consSubsidyPage(Page<Object> defaultPage, Map<String, Object> map);

    Page<ConsSubsidy> dailySubsidyConsDetail(Page<Object> defaultPage, Map<String, Object> map);

    BigDecimal dailySubsidyConsTotal(@Param("map") Map<String, Object> map);

    Page<ConsSubsidy> dailySubsidyCustDetail(Page<Object> defaultPage, Map<String, Object> map);

    BigDecimal dailySubsidyCustTotal(@Param("map") Map<String, Object> map);

    Page<ConsSubsidy> consSubsidyByEventIdPage(Page<Object> defaultPage, Map<String, Object> map);

    Page<ConsSubsidy> consSubsidyByEventIdAndConsIdPage(Page<Object> defaultPage, Map<String, Object> map);

    Page<ConsNoSubsidy> consNoSubsidy(Page<Object> defaultPage, Map<String, Object> map);

    Map<String, Object> consNoSubsidyInfo(@Param("map") Map<String, Object> map);

    /**
     * 根据 项目id 和 非集成商 获得补贴总和
     *
     * @param projectId
     * @param orgNo
     * @param integrator
     * @return
     */
    BigDecimal getProjectSubsidyInfo(@Param("projectId") String projectId, @Param("orgNos") List<String> orgNo, @Param("integrator") Integer integrator);

    /**
     * 根据 项目id 获得 代理参与用户 补贴总和
     *
     * @param projectId
     * @param orgNo
     * @return
     */
    BigDecimal getProjectConsSubsidy(@Param("projectId") String projectId, @Param("orgNos") List<String> orgNo);


    /**
     * 导出 -- 根据事件标识进行用户事件补贴分页查询
     *
     * @param map
     * @return
     * @author lqr
     * @date 2022-6-18 11:00
     */
    List<ConsSubsidyModel> exportConsByEventId(@Param("map") Map<String, Object> map);

    ConsSubsidy getCustSubsidy(@Param("eventId") Long eventId,@Param("custId") Long custId);
}
