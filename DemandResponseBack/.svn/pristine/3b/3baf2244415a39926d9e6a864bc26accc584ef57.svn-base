package com.xqxy.sys.modular.cust.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.dr.modular.statistics.VO.ConsBigClassCodeVO;
import com.xqxy.sys.modular.cust.entity.Cons;
import com.xqxy.sys.modular.cust.param.ConsParam;
import com.xqxy.sys.modular.cust.result.ConsMonitor;
import com.xqxy.sys.modular.cust.result.ConsStatisticsResult;
import com.xqxy.sys.modular.cust.result.StatisticsByTypeResult;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 系统用户mapper接口
 *
 * @author xuyuxiang
 * @date 2020/3/11 17:49
 */
public interface ConsMapper extends BaseMapper<Cons> {

    /**
     * 代理用户列表分页
     *
     * @param page      分页
     * @param consParam 用户参数
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.xqxy.sys.modular.cust.entity.Cons>
     * @date 10/18/2021 19:10
     * @author Caoj
     */
    Page<Cons> pageProxyConsList(Page<?> page, ConsParam consParam);


    @Select("SELECT ucr.ID id,ucr.cons_no consNo, dc.CONS_NAME consName FROM dr_user_cons_rela ucr LEFT JOIN dr_cons dc ON  dc.ID = ucr.cons_no")
    List<Map<String, Object>> getConsOrCust();

    @Select("<script>" +
            "SELECT  dc.id consId ,dc.cons_name consName ,dc.CONTRACT_CAP contractCap,IFNULL(dpc.inCount,0) involveCount,IFNULL(b.signontractCap,0) signontractCap from dr_cons dc \n" +
            "           \n" +
            "             left join    \n" +
            "            (select CONS_ID,count(INVOLVED_IN='Y') inCount from dr_plan_cons GROUP BY cons_id) dpc             \n" +
            "            on dc.id=dpc.CONS_ID\n" +
            "            \n" +
            "left join \n" +
            "(select cons_id,sum(CONTRACT_CAP)signontractCap from \n" +
            "(select dcci.cons_id,dccd.CONTRACT_CAP from dr_cons_contract_info dcci\n" +
            " left join dr_cons_contract_detail dccd \n" +
            "            on dccd.contract_id=dcci.contract_id )a \n" +
            "group by a.cons_id)b\n" +
            "\n" +
            "on dc.id=b.cons_id\n" +
            "where\n " +
            "dc.ORG_NO IN\n" +
            "   <foreach collection=\"consParam.orgIds\" item=\"orgId\" separator=\",\" open=\"(\" close=\")\">\n" +
            "       #{orgId}\n" +
            "   </foreach>\n" +
            "<if test=\"consParam.consId != null and consParam.consId != ''\">\n" +
            "            AND dc.id like concat('%', #{consParam.consId}, '%')\n" +
            "        </if>" +
            "<if test=\"consParam.consName != null and consParam.consName != ''\">\n" +
            "            AND dc.cons_name like concat('%', #{consParam.consName}, '%')\n" +
            "        </if>" +
            "</script>")
    Page<ConsMonitor> consMonitor(Page<?> page, @Param("consParam") ConsParam consParam);

    @Select("<script>" +
            "select dc.CITY_CODE regionCode,\n" + "count(id) totalCount," +
            "            count(BIG_TRADE_CODE=300 or null) residentCount,\n" +
            "            count(BIG_TRADE_CODE=100 or null) industrialCount,\n" +
            "            count(BIG_TRADE_CODE=200 or null) buildingCount,\n" +
            "            count(BIG_TRADE_CODE=400 or null) emergingLoadUser,\n" +
            "            count(BIG_TRADE_CODE=500 or null) agriculCount,\n" +
            "            count(BIG_TRADE_CODE=600 or null) otherCount\n" +
            "\n" +
            "             from dr_cons  dc\n" +
            "  \n" +
            "            where BIG_TRADE_CODE is not null and CITY_CODE is not null\n" +
            "            group by dc.CITY_CODE order by city_code" +
            "</script>")
    List<StatisticsByTypeResult> statisticsByType();

    @Select("<script>" +
            "\n" +
            "select dc.*,dc.BIG_TRADE_CODE bigClassCode from dr_cons dc\n" +
            "where BIG_TRADE_CODE is not null\n" +
            "<if test=\"regionCode != null and regionCode != ''and regionLevel ==1\">\n" +
            "            AND dc.province_code = #{regionCode}\n" +
            "        </if>" +
            "<if test=\"regionLevel==2 and  regionCode != null and regionCode != ''\">\n" +
            "            AND dc.city_code = #{regionCode}\n" +
            "        </if>" +
            "</script>")
    Page<Cons> statisticsByTypeDetail(Page<?> page, @Param("regionCode") String regionCode, @Param("regionLevel") String regionLevel);

    List<Cons> getConInfo();

    /**
     * 按照用户大类分组 并统计用户个数
     *
     * @return
     */
    List<ConsBigClassCodeVO> groupConsBigClassCode();

    /**
     * 查询各地市通过审核 用户类型统计
     *
     * @return
     */
    List<ConsStatisticsResult> contractStatistics(@Param("projectId") Long projectId, @Param("checkStatus") String checkStatus);


    /**
     * 各地市用户类型统计 --- 签约数据 -- 明细
     *
     * @param consParam
     * @return
     */
    Page<Cons> consStatisticsDetail(@Param("page") Page page, @Param("consParam") ConsParam consParam);

    /**
     * 日前需求响应户数（户） 10304001
     * 日内需求响应户数（户） 10305001
     * 实时需求响应户数（户） 10306001
     *
     * @param projectId         项目id
     * @param responseType      响应类型 1削峰，2填谷
     * @param timeType          时间类型 1邀约，2实时
     * @param advanceNoticeTime 提前通知时间 1日前，2小时级，3分钟级，4秒级
     * @param orgNoList         供电单位编码列表
     * @return 客户数量
     */
    Integer getHouseholds(@Param("projectId") String projectId, @Param("responseType") String responseType, @Param("timeType") String timeType, @Param("advanceNoticeTime") String advanceNoticeTime, @Param("orgNoList") List<String> orgNoList);

    /**
     * 1---->查询单个行业用户数、
     *
     * @param customerType1 行业类型
     * @param orgNoList     供电编码
     * @return 数量
     */
    Integer customerTypeCount(@Param("customerType1") String customerType1, @Param("orgNoList") List<String> orgNoList);

    /**
     * 2---->除传入的两个行业类型剩下的用户数
     *
     * @param customerType1 行业类型
     * @param customerType2 行业类型
     * @param orgNoList     供电编码
     * @return 数量
     */
    Integer customerTypeCount1(@Param("customerType1") String customerType1, @Param("customerType2") String customerType2, @Param("orgNoList") List<String> orgNoList);
}
