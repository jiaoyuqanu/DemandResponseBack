package com.xqxy.dr.modular.data.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xqxy.dr.modular.data.OrgNoCurve;
import com.xqxy.dr.modular.data.entity.ConsCurve;
import com.xqxy.dr.modular.data.entity.ConsEnergyCurve;
import com.xqxy.dr.modular.event.entity.EventPowerSample;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 用户功率曲线 Mapper 接口
 * </p>
 *
 * @author Shen
 * @since 2021-10-25
 */
public interface ConsCurveMapper extends BaseMapper<ConsCurve> {

    /**
     * 历史数据服务 根据电力营销户号查询指定日期的负荷数
     *
     * @param consNo
     * @param dataDate
     * @return
     */
    ConsCurve queryConsCurveHistory(@Param("consNo") String consNo, @Param("dataDate") String dataDate);

    /**
     * 历史数据服务 根据电力营销户号查询指定日期范围内的负荷数据 单个用户
     *
     * @param consNo
     * @param startDate
     * @param endDate
     * @return
     */
    List<ConsCurve> queryConsCurveHistoryDateScope(@Param("consNo") String consNo, @Param("startDate") String startDate, @Param("endDate") String endDate);

    /**
     * 查询实时数据负荷 一个用户
     *
     * @param consNo
     * @param dataDate
     * @return
     */
    ConsCurve queryConsCurveRealLoad(@Param("consNo") String consNo, @Param("dataDate") String dataDate);


    /**
     * 查询实时数据负荷  多个用户
     *
     * @param consNoList
     * @param dataDate
     * @return
     */
    List<ConsCurve> queryConsCurveRealLoadList(@Param("consNoList") List<String> consNoList, @Param("dataDate") String dataDate);

    /**
     * 历史数据服务，多户号查询 多日期
     *
     * @param elecConsNolist
     * @param dataDateList
     * @return
     */
    List<ConsCurve> queryHistoryCurveDateList(@Param("consNoList") List<String> elecConsNolist, @Param("dataDateList") List<String> dataDateList);

    /**
     * 历史数据服务，多户号查询
     *
     * @param elecConsNolist
     * @param dataDate
     * @return
     */
    List<ConsCurve> queryHistoryCurvePage(@Param("consNoList") List<String> elecConsNolist, @Param("dataDate") String dataDate);


    /**
     * 查询实时数据负荷ConsEnergyCurve  一个用户
     *
     * @param consNo
     * @param dataDate
     * @return
     */
    ConsEnergyCurve queryConsEnergyCurveRealLoad(@Param("consNo") String consNo, @Param("dataDate") String dataDate);

    /**
     * @description: 负荷预测 ，根据时间和户号集合 返回一条 sum(p1),sum(p2),sum(p3), 的 历史负荷数据
     * @param:
     * @return:
     * @author: liqirui
     * @date: 2022/1/15 13:39
     */
    ConsCurve getCurveByConsIdListAndDate(@Param("condIdList") List<String> condIdList, @Param("date") String date);

    List<ConsCurve> getCurveByConsIdListAndDate2(@Param("condIdList") List<String> condIdList, @Param("date") String date);

    List<ConsCurve> getCurveByConsIdListAndDate3(@Param("condIdList") List<String> condIdList, @Param("date") String date);

    /**
     * 累加用户样本日期的曲线值
     *
     * @param simpList
     * @return
     */
    List<ConsCurve> getCurveAllByDate(@Param("simpList") List<String> simpList, @Param("size") Integer size);

    List<OrgNoCurve> getOrgNoCurve(@Param("isToday") Boolean isToday, @Param("isDoubleHigh") Boolean isDoubleHigh,
                                   @Param("date") String date, @Param("start") String start, @Param("end") String end);

    List<EventPowerSample> getCurveAllAmendByDate(@Param("simpList") List<String> simpList,@Param("cons") List<String> cons,@Param("baselinId") Long baselinId);

    List<EventPowerSample> getCurveAmendByDate(@Param("simpList") List<String> simpList,@Param("cons") List<String> cons,@Param("baselinId") Long baselinId);

}
