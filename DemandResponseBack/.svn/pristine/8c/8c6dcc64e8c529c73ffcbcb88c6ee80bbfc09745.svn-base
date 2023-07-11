package com.xqxy.dr.modular.event.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.dr.modular.event.entity.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 用户邀约信息(DrEventInvitation)表数据库访问层
 *
 * @author makejava
 * @since 2021-07-14 21:37:50
 */

public interface DrEventInvitationMapper extends BaseMapper<DrEventInvitation> {

    /**
     * 通过ID查询单条数据
     *
     * @param invitationId 主键
     * @return 实例对象
     */
    @Select("  select " +
            "          invitation_id, event_id, cons_id, invitation_time, max_load_baseline, min_load_baseline, avg_load_baseline, invitation_cap, subsidy_price, deadline_time, is_participate, reply_cap, reply_time, reply_source, invitation_round, invitation_sequence, exception_remark, create_time, create_user, update_time, update_user, cal_time, cal_status, cal_exception, eval_time, eval_status, eval_exception, immediate_eval_time, immediate_eval_status, immediate_eval_exception, sub_time, sub_status, sub_exception " +
            "        from dr_event_invitation " +
            "        where invitation_id = #{invitationId}")
    DrEventInvitation queryById(Long invitationId);

    @Select("<script>" +
            "SELECT " +
            " a.event_id, " +
            " a.inv, " +
            " a.eva, " +
            " a.per1, " +
            " a.reply_cap, " +
            " round( a.actual_cap, 0 ) actual_cap, " +
            " a.per2, " +
            " a.city_code, " +
            " b.name cityName " +
            "FROM " +
            " view_cons_count a, " +
            " sc_admin.t_pub_region b " +
            "WHERE b.id = a.city_code" +
            "            <if test=\"eventId != null\"> " +
            "               and a.event_id=#{eventId} " +
            "            </if> " +
            "            <if test=\"cityCode != null\"> " +
            "               and a.city_code=#{cityCode} " +
            "            </if> " +
            "order by b.id asc" +
            " </script>")
    List<DrConsCountEntity> costData(Long eventId, Long cityCode);


    @Select("<script>" +
            "select a.event_id, " +
            "       a.inv, " +
            "       a.eva, " +
            "       a.per1, " +
            "       a.reply_cap,  " +
            "       round(a.actual_cap,0) actual_cap, " +
            //"       a.actual_cap, " +
            "       a.per2, " +
            "       a.city_code, " +
            "       b.name cityName " +

            "  from view_cons_count_immediate a," +
            "       sc_admin.t_pub_region b " +
            "  where b.id = a.city_code" +
            "            <if test=\"eventId != null\"> " +
            "               and a.event_id=#{eventId} " +
            "            </if> " +
            "            <if test=\"cityCode != null\"> " +
            "               and a.city_code=#{cityCode} " +
            "            </if> " +
            "order by b.id asc" +
            " </script>")
    List<DrConsCountEntity> costDataImmediate(Long eventId, Long cityCode);

    @Select("<script> " +
            "SELECT " +
            "  c.is_effective, " +
            "  b.cons_name, " +
            " a.* " +
            "FROM " +
            " dr_event_participate a, " +
            " dr_evaluation c, " +
            " dr_cons b " +
            "WHERE " +
            " a.cons_id = b.cons_id" +
            "    and a.cons_id = c.cons_id  " +
            "    and a.event_id = c.event_id  " +
            " <if test=\"eventId != null\"> " +
            "    and a.event_id=#{eventId} " +
            " </if> " +
            "</script>")
    List<Map<String, Object>> exprotfhjcsCostData(Long eventId);

    @Select("<script> " +
            "SELECT " +
            "  c.is_effective, " +
            "  b.cons_name, " +
            " a.* " +
            "FROM " +
            " dr_event_participate a, " +
            " dr_evaluation_immediate c, " +
            " dr_cons b " +
            "WHERE " +
            " a.cons_id = b.cons_id" +
            "    and a.cons_id = c.cons_id  " +
            "    and a.event_id = c.event_id  " +
            " <if test=\"eventId != null\"> " +
            "    and a.event_id=#{eventId} " +
            " </if> " +
            "</script>")
    List<Map<String, Object>> exprotfhjcsCostImmediateData(Long eventId);


    @Select("<script> " +
            "SELECT " +
            " dc.CUST_NAME cons_name, " +
            " dc.id cons_id, " +
            " dci.EVENT_ID event_id, " +
            " dce.is_effective is_effective  " +
            "FROM " +
            " dr_cust dc, " +
            " dr_cust_invitation dci , " +
            " dr_cust_evaluation dce " +
            "WHERE " +
            " dc.id = dci.CUST_ID " +
            " and dci.CUST_ID = dce.CUST_ID" +
            " <if test=\"eventId != null\"> " +
            "    and a.event_id=#{eventId} " +
            " </if> " +
            "</script>")
    IPage<Map<String, Object>> fhjcsCostDataPage(Page pageParam, Long eventId);

    @Select("<script> " +
            "SELECT " +
            " dc.CUST_NAME cons_name, " +
            " dc.id cons_id, " +
            " dci.EVENT_ID event_id, " +
            " dcei.is_effective is_effective  " +
            "FROM " +
            " dr_cust dc, " +
            " dr_cust_invitation dci , " +
            " dr_cust_evaluation_immediate dcei " +
            "WHERE " +
            " dc.id = dci.CUST_ID " +
            " and dci.CUST_ID = dcei.CUST_ID" +
            " <if test=\"eventId != null\"> " +
            "    and dci.EVENT_ID=#{eventId} " +
            " </if> " +
            "</script>")
    IPage<Map<String, Object>> fhjcsCostImmediateDataPage(Page pageParam, Long eventId);

    /**
     * 聚合商应邀用户
     *
     * @param
     * @return 实例对象
     */
    @Select("<script> " +
            "SELECT " +
            " count( x.CUST_ID ) yyhs  " +
            "FROM " +
            " ( " +
            " SELECT " +
            "  b.CUST_ID  " +
            " FROM " +
            "  dr_cust_contract_info a, " +
            "  dr_cust_invitation b  " +
            " WHERE " +
            " b.CUST_ID = a.CUST_ID " +
            " AND b.is_participate = 'Y' " +
            " AND b.event_id = #{eventId} " +
            " AND b.CUST_ID =#{consId} ) x" +
            "</script>")
    BigDecimal yyhsFHJCS(String consId, Long eventId);


    /**
     * 应邀负荷
     *
     * @param
     * @return 实例对象
     */
    @Select("<script> " +
            " SELECT " +
            " sum( x.reply_cap ) yyhs  " +
            " FROM " +
            " (  " +
            " SELECT  " +
            " b.reply_cap  " +
            " FROM  " +
            " dr_cust_contract_info a,  " +
            " dr_cust_invitation b  " +
            " WHERE  " +
            " b.CUST_ID = a.CUST_ID  " +
            " AND b.is_participate = 'Y'  " +
            " AND b.event_id = #{eventId} " +
            " AND b.CUST_ID =#{consId} ) x" +
            " </script>")
    BigDecimal yyfuFHJCS(String consId, Long eventId);

    /**
     * 负荷集成商效果评估明细
     */
    @Select("<script> " +
            "select    " +
            "  (select b.region_name from sys_region b where a.city_code =b.region_code) city_Name, " +
            "  (select b.region_name from sys_region b where a.county_code =b.region_code) county_Name, " +
            "  a.elec_cons_no  cons_no, " +
            "  a.cons_name  cons_name, " +
            "  c.reply_cap  reply_cap, " +
            "  c.avg_load_baseline avg_load_baseline, " +
            "  c.max_load_baseline max_load_baseline, " +
            "  c.avg_load_actual avg_load_actual , " +
            "  c.min_load_actual min_load_actual, " +
            "  c.max_load_actual max_load_actual, " +
            "  c.max_load_baseline -c.max_load_actual  min , " +
            //"  c.max_load_actual -c.max_load_baseline min, " +
            "  c.actual_cap actual_cap, " +
            "  c.is_effective " +
            " from dr_cons_declare  a , dr_event_invitation b ,dr_evaluation c " +
            " where 1=1  " +
            "and b.cons_id =c.cons_id " +
            "and b.event_id =c.event_id " +
            "and b.cons_id =a.cons_id " +
            " <if test=\"consId != null\"> " +
            "and a.aggregate_cons_id = #{consId} " +
            " </if> " +
            " <if test=\"eventId != null\"> " +
            "  and b.event_id =  #{eventId} " +
            " </if> " +
            "</script>")
    List<DrEventInvEntity> getDrEventInvEntity(Long eventId, String consId);

    /**
     * 当日 荷集成商效果评估明细
     */
    @Select("<script> " +
            "select    " +
            " a.county_code, " +
            " a.city_code, " +
            "  a.elec_cons_no  cons_no, " +
            "  a.cons_name  cons_name, " +
            "  c.reply_cap  reply_cap, " +
            "  c.avg_load_baseline avg_load_baseline, " +
            "  c.max_load_baseline max_load_baseline, " +
            "  c.avg_load_actual avg_load_actual , " +
            "  c.min_load_actual min_load_actual, " +
            "  c.max_load_actual max_load_actual, " +
            "  c.max_load_baseline -c.max_load_actual  min , " +
            "  c.actual_cap actual_cap, " +
            "  c.is_effective " +
            " from dr_cons_declare  a , dr_event_invitation b ,dr_evaluation_immediate c " +
            " where 1=1  " +
            "and b.cons_id =c.cons_id " +
            "and b.event_id =c.event_id " +
            "and b.cons_id =a.cons_id " +
            " <if test=\"consId != null\"> " +
            "and a.aggregate_cons_id = #{consId} " +
            " </if> " +
            " <if test=\"eventId != null\"> " +
            "  and b.event_id =  #{eventId} " +
            " </if> " +
            "</script>")
    List<DrEventInvEntity> getDrEventInvImmediateEntity(Long eventId, String consId);

    /**
     * 负荷集成商效果评估明细--分頁
     */
    @Select("<script> " +
            "SELECT " +
            " a.county_code, " +
            " a.city_code, " +
            " a.id cons_no, " +
            " a.CUST_NAME cons_name, " +
            " c.reply_cap reply_cap, " +
            " c.avg_load_baseline avg_load_baseline, " +
            " c.max_load_baseline max_load_baseline, " +
            " c.avg_load_actual avg_load_actual, " +
            " c.min_load_actual min_load_actual, " +
            " c.max_load_actual max_load_actual, " +
            " c.max_load_baseline - c.max_load_actual min, " +
            " c.actual_cap actual_cap, " +
            " c.is_effective  " +
            "FROM " +
            " dr_cust a, " +
            " dr_cust_invitation b, " +
            " dr_cust_evaluation c  " +
            "WHERE " +
            "  b.CUST_ID = c.CUST_ID " +
            " AND b.event_id = c.event_id  " +
            " AND b.CUST_ID = a.id" +
            " <if test=\"consId != null\"> " +
            "and a.id = #{consId} " +
            " </if> " +
            " <if test=\"eventId != null\"> " +
            "  and b.event_id =  #{eventId} " +
            " </if> " +
            "</script>")
    IPage<DrEventInvEntity> getDrEventInvEntityPage(Page pageParam, Long eventId, String consId);


    /**
     * 负荷集成商效果评估明细--分頁
     */
    @Select("<script> " +
            "SELECT " +
            " a.city_code, " +
            " a.county_code, " +
            " a.id cons_no, " +
            " a.CUST_NAME cons_name, " +
            " c.reply_cap reply_cap, " +
            " c.avg_load_baseline avg_load_baseline, " +
            " c.max_load_baseline max_load_baseline, " +
            " c.avg_load_actual avg_load_actual, " +
            " c.min_load_actual min_load_actual, " +
            " c.max_load_actual max_load_actual, " +
            " c.max_load_baseline - c.max_load_actual min, " +
            " c.actual_cap actual_cap, " +
            " c.is_effective  " +
            "FROM " +
            " dr_cust a, " +
            " dr_cust_invitation b, " +
            " dr_cust_evaluation c  " +
            "WHERE " +
            "  b.CUST_ID = c.CUST_ID " +
            " AND b.event_id = c.event_id  " +
            " AND b.CUST_ID = a.id" +
            " <if test=\"consId != null\"> " +
            "and a.id = #{consId} " +
            " </if> " +
            " <if test=\"eventId != null\"> " +
            "  and b.event_id =  #{eventId} " +
            " </if> " +
            "</script>")
    IPage<DrEventInvEntity> getDrEventInvEntityImmediatePage(Page pageParam, Long eventId, String consId);


    /**
     * 有效响应户数
     *
     * @param
     * @return 实例对象
     */
    @Select("<script> " +
            "SELECT " +
            " count( x.invitation_id ) yyhs  " +
            "FROM " +
            " (SELECT " +
            " b.invitation_id  " +
            " FROM " +
            " dr_cust_contract_info a, " +
            " dr_cust_invitation b, " +
            " dr_cust_evaluation c  " +
            " WHERE " +
            " b.CUST_ID = a.CUST_ID  " +
            " AND b.is_participate = 'Y'  " +
            " AND c.event_id = b.event_id  " +
            " AND c.CUST_ID = b.CUST_ID  " +
            " AND c.is_effective = 'Y'  " +
            " AND b.event_id = #{eventId} " +
            " AND a.CUST_ID =#{consId} ) x"
            + "</script>")
    BigDecimal yxxyhsFHJCS(String consId, Long eventId);


    /**
     * 当日有效响应户数
     *
     * @param
     * @return 实例对象
     */
    @Select("<script> " +
            "SELECT " +
            " count( x.invitation_id ) yyhs  " +
            "FROM " +
            " (SELECT " +
            " b.invitation_id  " +
            " FROM " +
            " dr_cust_contract_info a, " +
            " dr_cust_invitation b, " +
            " dr_cust_evaluation_immediate c  " +
            " WHERE " +
            " b.CUST_ID = a.CUST_ID  " +
            " AND b.is_participate = 'Y'  " +
            " AND c.event_id = b.event_id  " +
            " AND c.CUST_ID = b.CUST_ID  " +
            " AND c.is_effective = 'Y'  " +
            " AND b.event_id = #{eventId} " +
            " AND a.CUST_ID =#{consId} ) x"
            + "</script>")
    BigDecimal yxxyhsFHJCSImmediate(String consId, Long eventId);


    /**
     * 有效响应负荷
     *
     * @param
     * @return 实例对象
     */
    @Select("<script> " +
            "SELECT " +
            " sum( x.reply_cap ) yyhs  " +
            "FROM " +
            " ( " +
            " SELECT " +
            "  b.reply_cap  " +
            " FROM " +
            "  dr_cust_contract_info a, " +
            "  dr_cust_invitation b, " +
            "  dr_cust_evaluation c  " +
            " WHERE " +
            "  b.CUST_ID = a.CUST_ID  " +
            "  AND b.is_participate = 'Y'  " +
            "  AND c.event_id = b.event_id  " +
            "  AND c.CUST_ID = b.CUST_ID  " +
            " AND c.is_effective = 'Y'  " +
            "AND b.event_id = #{eventId} " +
            " AND a.CUST_ID =#{consId} ) x"
            + "</script>")
    BigDecimal yxxyfhFHJCS(String consId, Long eventId);


    /**
     * 当日有效响应负荷
     *
     * @param
     * @return 实例对象
     */
    @Select("<script> " +
            "SELECT " +
            " sum( x.reply_cap ) yyhs  " +
            "FROM " +
            " ( " +
            " SELECT " +
            "  b.reply_cap  " +
            " FROM " +
            "  dr_cust_contract_info a, " +
            "  dr_cust_invitation b, " +
            "  dr_cust_evaluation_immediate c  " +
            " WHERE " +
            "  b.CUST_ID = a.CUST_ID  " +
            "  AND b.is_participate = 'Y'  " +
            "  AND c.event_id = b.event_id  " +
            "  AND c.CUST_ID = b.CUST_ID  " +
            " AND c.is_effective = 'Y'  " +
            " AND b.event_id = #{eventId} " +
            " AND a.CUST_ID =#{consId} ) x"
            + "</script>")
    BigDecimal yxxyfhFHJCSImmediate(String consId, Long eventId);

    /**
     * 根據 eventId 統計 --7
     * 应邀负荷
     */
    @Select("SELECT " +
            " sum(b.actual_cap) yyfh " +
            "FROM " +
            " dr_event_participate a, " +
            " dr_evaluation b, " +
            " dr_cons c " +
            "WHERE " +
            "a.event_id = #{eventId} " +
            "AND a.is_participate = 'Y' " +
            "AND a.cons_id = c.cons_id " +
            "AND a.cons_id = b.cons_id " +
            "AND a.cons_id IS NOT NULL ")
    BigDecimal yyfh(Long eventId);


    /**
     * 根據 eventId 統計 --8
     * 有效响应负荷
     */
    @Select("SELECT " +
            " sum(b.actual_cap) yxxyfh " +
            "FROM " +
            " dr_event_participate a, " +
            " dr_evaluation b, " +
            " dr_cons c " +
            "WHERE " +
            "a.event_id = #{eventId} " +
            "AND a.is_participate = 'Y' " +
            "AND b.is_effective = 'Y' " +
            "AND a.cons_id = c.cons_id " +
            "AND a.cons_id = b.cons_id " +
            "AND a.cons_id IS NOT NULL ")
    BigDecimal yxxyfh(Long eventId);


    /**
     * 效果评估明细 分页
     */
    @Select("<script>" +
            "SELECT DISTINCT " +
            " a.city_code, " +
            " a.county_code, " +
            " ( CASE c.partic_type WHEN '1' THEN '直接参与用户' WHEN '2' THEN '代理用户' END ) cons_type, " +
            " a.id cons_no, " +
            " a.cons_name cons_name, " +
            " b.reply_cap reply_cap, " +
            " b.avg_load_baseline avg_load_baseline, " +
            " b.max_load_baseline max_load_baseline, " +
            " b.avg_load_actual avg_load_actual, " +
            " b.max_load_actual max_load_actual, " +
            " b.max_load_baseline - b.max_load_actual min, " +
//            " b.max_load_actual - b.max_load_baseline min, " +
            " b.actual_cap actual_cap, " +
            " b.is_effective is_effective  " +
            "FROM " +
            " dr_cons a, " +
            " dr_cons_contract_info c, " +
            " dr_cons_invitation d, " +
            " dr_cons_evaluation b  " +
            "WHERE " +
            " a.id = b.cons_id  " +
            " and a.id = c.CONS_ID " +
            " AND d.cons_id = b.cons_id  " +
            " AND d.event_id = b.event_id  " +
            " AND d.is_participate = 'Y'  " +
            " AND b.is_effective = 'Y'" +
            " <if test=\"cityCode != null and cityCode != ''\"> " +
            "and a.city_code = #{cityCode} " +
            " </if> " +
            " <if test=\"eventId != null and eventId != ''\"> " +
            "and d.event_id=#{eventId} " +
            " </if> " +
            " <if test=\"consType != null and consType != ''\"> " +
            "     and c.partic_type = #{consType} " +
            " </if> " +
            //   "  limit #{pageParam.current} , limit #{pageParam.size}"+
            "</script>")
    IPage<DrEventInvitationEntity> getDrEventInvitationEntityPage(Page pageParam, Long cityCode, Long eventId, String consType);


    /**
     * 效果评估明细 导出报表
     */
    @Select("<script>" +
            "select DISTINCT " +
            "  (select b.region_name from sys_region b where a.city_code =b.region_code) cityName, " +
            "  (select b.region_name from sys_region b where a.county_code =b.region_code) countyName, " +
            "  (CASE a.cons_type " +
            "  WHEN '1' THEN '集成商' " +
            "  WHEN '2' THEN '直接参与用户' " +
            "  WHEN '3' THEN '代理用户' " +
            "    end ) cons_type, " +
            "  a.elec_cons_no cons_no, " +
            "  a.cons_name cons_name , " +
            "  b.reply_cap reply_cap , " +
            "  b.avg_load_baseline avg_load_baseline, " +
            "  b.max_load_baseline max_load_baseline, " +
            "  b.avg_load_actual avg_load_actual, " +
            "  b.max_load_actual max_load_actual, " +
            "  b.max_load_baseline -b.max_load_actual  min , " +
            //"  b.max_load_actual -b.max_load_baseline min , " +
            "  b.actual_cap actual_cap, " +
            "  b.is_effective is_effective " +
            "from dr_cons a, " +
            "   dr_cons_declare c, " +
            "     dr_event_invitation d, " +
            "     dr_evaluation b  " +
            "where a.cons_id =b.cons_id " +
            "and c.elec_cons_no =a.elec_cons_no " +
            "and d.cons_id=b.cons_id " +
            "and d.event_id =b.event_id " +
            "and d.is_participate='Y' " +
            // "and b.is_effective ='Y' " +
            " <if test=\"cityCode != null and cityCode != '' \"> " +
            "and a.city_code = #{cityCode} " +
            " </if> " +
            "and d.event_id=#{eventId} " +
            " <if test=\"consType != null and consType != ''\"> " +
            "     and a.cons_type = #{consType} " +
            " </if> " +
            //   "  limit #{pageParam.current} , limit #{pageParam.size}"+
            "</script>")
    List<DrEventInvitationEntity> exprotDrEventInvitationEntityPage(Long cityCode, Long eventId, String consType);

    /**
     * 当日效果评估明细 分页
     */
    @Select("<script>" +
            "SELECT DISTINCT " +
            " a.city_code , " +
            " a.county_code , " +
            " ( CASE c.partic_type WHEN '1' THEN '直接参与用户' WHEN '2' THEN '代理用户' END ) cons_type, " +
            " a.id cons_no, " +
            " a.cons_name cons_name, " +
            " b.reply_cap reply_cap, " +
            " b.avg_load_baseline avg_load_baseline, " +
            " b.max_load_baseline max_load_baseline, " +
            " b.avg_load_actual avg_load_actual, " +
            " b.max_load_actual max_load_actual, " +
            " b.max_load_baseline - b.max_load_actual min, " +
//            " b.max_load_actual - b.max_load_baseline min, " +
            " b.actual_cap actual_cap, " +
            " b.is_effective is_effective  " +
            "FROM " +
            " dr_cons a, " +
            " dr_cons_contract_info c, " +
            " dr_cons_invitation d, " +
            " dr_cons_evaluation_immediate b  " +
            "WHERE " +
            " a.id = b.cons_id  " +
            " and a.id = c.CONS_ID " +
            " AND d.cons_id = b.cons_id  " +
            " AND d.event_id = b.event_id  " +
            " AND d.is_participate = 'Y'  " +
            " AND b.is_effective = 'Y'" +
            " <if test=\"cityCode != null and cityCode != ''\"> " +
            "and a.city_code = #{cityCode} " +
            " </if> " +
            " <if test=\"eventId != null and eventId != ''\"> " +
            "and d.event_id=#{eventId} " +
            " </if> " +
            " <if test=\"consType != null and consType != ''\"> " +
            "     and c.partic_type = #{consType} " +
            " </if> " +
            //   "  limit #{pageParam.current} , limit #{pageParam.size}"+
            "</script>")
    IPage<DrEventInvitationEntity> getDrEventInvitationImmediateEntityPage(Page pageParam, Long cityCode, Long eventId, String consType);

    /**
     * 当日效果评估明细 分页
     */
    @Select("<script>" +
            "select DISTINCT " +
            "  (select b.region_name from sys_region b where a.city_code =b.region_code) cityName, " +
            "  (select b.region_name from sys_region b where a.county_code =b.region_code) countyName, " +
            "  (CASE a.cons_type " +
            "  WHEN '1' THEN '集成商' " +
            "  WHEN '2' THEN '直接参与用户' " +
            "  WHEN '3' THEN '代理用户' " +
            "    end ) cons_type, " +
            "  a.elec_cons_no cons_no, " +
            "  a.cons_name cons_name , " +
            "  b.reply_cap reply_cap , " +
            "  b.avg_load_baseline avg_load_baseline, " +
            "  b.max_load_baseline max_load_baseline, " +
            "  b.avg_load_actual avg_load_actual, " +
            "  b.max_load_actual max_load_actual, " +
            "  b.max_load_baseline -b.max_load_actual  min , " +
            //"  b.max_load_actual -b.max_load_baseline min , " +
            "  b.actual_cap actual_cap, " +
            "  b.is_effective is_effective " +
            "from dr_cons a, " +
            "   dr_cons_declare c, " +
            "     dr_event_invitation d, " +
            "     dr_evaluation_immediate b  " +
            "where a.cons_id =b.cons_id " +
            "and c.elec_cons_no =a.elec_cons_no " +
            "and d.cons_id=b.cons_id " +
            "and d.event_id =b.event_id " +
            "and d.is_participate='Y' " +
            // "and b.is_effective ='Y' " +
            " <if test=\"cityCode != null and cityCode != '' \"> " +
            "and a.city_code = #{cityCode} " +
            " </if> " +
            "and d.event_id=#{eventId} " +
            " <if test=\"consType != null and consType != '' \"> " +
            "     and a.cons_type = #{consType} " +
            " </if> " +
            //   "  limit #{pageParam.current} , limit #{pageParam.size}"+
            "</script>")
    List<DrEventInvitationEntity> exprotDrEventInvitationImmediateEntity(Long cityCode, Long eventId, String consType);

    /**
     * 效果评估明细
     */
    @Select("<script>" +
            "select DISTINCT " +
            "  (select b.region_name from sys_region b where a.city_code =b.region_code) cityName, " +
            "  (select b.region_name from sys_region b where a.county_code =b.region_code) countyName, " +
            "  (CASE a.cons_type " +
            "  WHEN '1' THEN '集成商' " +
            "  WHEN '2' THEN '直接参与用户' " +
            "  WHEN '3' THEN '代理用户' " +
            "    end ) cons_type, " +
            "  a.elec_cons_no cons_no, " +
            "  a.cons_name cons_name , " +
            "  b.reply_cap reply_cap , " +
            "  b.avg_load_baseline avg_load_baseline, " +
            "  b.max_load_baseline max_load_baseline, " +
            "  b.avg_load_actual avg_load_actual, " +
            "  b.max_load_actual max_load_actual, " +
            "  b.max_load_baseline -b.max_load_actual  min , " +
            //"  b.max_load_actual -b.max_load_baseline min , " +
            "  b.actual_cap actual_cap, " +
            "  b.is_effective is_effective " +
            "from dr_cons a, " +
            "   dr_cons_declare c, " +
            "     dr_event_invitation d, " +
            "     dr_evaluation b  " +
            "where a.cons_id =b.cons_id " +
            "and c.elec_cons_no =a.elec_cons_no " +
            "and d.cons_id=b.cons_id " +
            "and d.event_id =b.event_id " +
            "and d.is_participate='Y' " +
            // "and b.is_effective ='Y' " +
            " <if test=\"cityCode != null and cityCode != '' \"> " +
            "and a.city_code = #{cityCode} " +
            " </if> " +
            "and d.event_id=#{eventId} " +
            " <if test=\"consType != null and consType != '' \"> " +
            "     and a.cons_type = #{consType} " +
            " </if> " +
            "</script>")
    List<DrEventInvitationEntity> getDrEventInvitationEntity(Long cityCode, Long eventId, String consType);

    @Select("SELECT  " +
            "  c.cons_id,a.start_time,a.end_time,a.period_time_id " +
            "  FROM " +
            "   dr_period_time_template a, " +
            "   dr_event b, " +
            "   dr_event_invitation c " +
            "  WHERE  " +
            "    a.effective_month = DATE_FORMAT(b.start_date, '%Y%m') " +
            "  AND a.effective_month = DATE_FORMAT(b.end_date, '%Y%m') " +
            "  AND a.start_time = b.start_period " +
            "  AND a.end_time = b.end_period " +
            "            <if test=\"periodTimeId != null\"> " +
            "                and a.period_time_id = #{periodTimeId} " +
            "            </if> " +
            " ORDER BY c.cons_id desc,start_time asc ")
    List<Map<String, String>> getEntity(Long periodTimeId);


    /**
     * 查询湖南市级 region_code
     *
     * @return 对象列表
     */
    @Select("select region_code from  sys_region  where region_level in (1 ,2) order by region_code asc")
    List<Long> getRegionCode();


    /**
     * 查询湖南市级 region_code
     *
     * @return 对象列表
     */
    @Select("select region_name from  sys_region  where region_code = #{region_code}")
    String getRegionName(Long regionCode);


    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<DrEventInvitation> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param drEventInvitation 实例对象
     * @return 对象列表
     */
    List<DrEventInvitation> queryAll(DrEventInvitation drEventInvitation);

    /**
     * 新增数据
     *
     * @param drEventInvitation 实例对象
     * @return 影响行数
     */
    int insert(DrEventInvitation drEventInvitation);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<DrEventInvitation> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<DrEventInvitation> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<DrEventInvitation> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<DrEventInvitation> entities);

    /**
     * 修改数据
     *
     * @param drEventInvitation 实例对象
     * @return 影响行数
     */
    int update(DrEventInvitation drEventInvitation);

    /**
     * 通过主键删除数据
     *
     * @param invitationId 主键
     * @return 影响行数
     */
    int deleteById(Long invitationId);

    /**
     * 根据事件id查询
     */
    @Select("select * from dr_event a where a.event_id =#{eventId} ")
    List<Map<String, Object>> getDrEventById(Long eventId);

    @Select("SELECT cons_id FROM `dr_event_invitation` " +
            "WHERE " +
            "is_participate='Y' " +
            "AND " +
            "event_id=#{eventId}")
    List<String> queryCountByEventID(Long eventId);


    @Select("select * from dr_event where DATE_FORMAT(REGULATE_DATE,'%Y') = '${year}'")
    List<Event> getEventByYear(@Param("year") String year);


    /**
     * 用户参与情况统计
     */
    IPage<DrEventInvitationUser> getEventUser(Page pageParam,@Param("startDate") String startDate,
                        @Param("endDate") String endDate,
                        @Param("eventId") Long eventId);

    /**
     * 用户参与情况统计导出
     */
    List<DrEventInvitationUser> exprotEventUser(String startDate, String endDate, Long eventId);

    /**
     * 业务运行统计--查询地市
     */
    IPage<DrEventInvitationBusiness> getEventBusiness(Page pageParam,@Param("provinceCode") Long provinceCode,@Param("cityCode") Long cityCode);

    /**
     * 业务运行统计--查询时间范围和类型内的事件
     */
    List<Map> getEventBusinessEvent(String startDate, String endDate, String eventType);
    List<Map> getCapEnergy(String startDate, String endDate, Long eventId);

    /**
     * 事件效果评估 次日
     */
    List<DrEventInvitationEffectEval> getEffectEval(Page pageParam,@Param("startDate") String startDate,@Param("endDate") String endDate,@Param("eventId") Long eventId,@Param("cityCode") Long cityCode);


    /**
     * 导出 -- 事件效果评估 -- 次日
     * @param startDate
     * @param endDate
     * @param eventId
     * @param cityCode
     * @return
     */
    List<DrEventInvitationEffectEval> exprotffectEval(@Param("startDate") String startDate,@Param("endDate") String endDate,@Param("eventId") Long eventId,@Param("cityCode") Long cityCode);
    List<DrEventInvitationEffectEval> getEffectEvalImmediate(Page pageParam,@Param("startDate") String startDate,@Param("endDate") String endDate,@Param("eventId") Long eventId,@Param("cityCode") Long cityCode);

    /**
     * 导出 -- 事件效果评估 -- 当日
     * @param startDate
     * @param endDate
     * @param eventId
     * @param cityCode
     * @return
     */
    List<DrEventInvitationEffectEval> exprotEffectEvalImmediate(@Param("startDate") String startDate,@Param("endDate") String endDate,@Param("eventId") Long eventId,@Param("cityCode") Long cityCode);
    List<Map> getactualUsers(Long eventId);
    List<Map> getEffectUsers(Long eventId);
    List<Map> getEffectUsersImmediate(Long eventId);

    /**
     * 事件效果评估 次日用户明细
     */
    IPage<DrEventInvitationEffectEvalDetail> getEffectUsersDetail(Page pageParam, @Param("eventId") Long eventId);
    IPage<DrEventInvitationEffectEvalDetail> getEffectUsersDetailImmediate(Page pageParam, @Param("eventId") Long eventId);
}

