package com.xqxy.dr.modular.strategy;

import com.xqxy.dr.modular.data.entity.ConsCurve;
import com.xqxy.dr.modular.data.entity.ConsEnergyCurve;
import com.xqxy.dr.modular.device.VO.EquipmentRecordVO;
import com.xqxy.sys.modular.cust.entity.Cons;

import java.util.List;

/**
 * @Description
 * @ClassName DataAccess
 * @Author User
 * @date 2021.04.13 17:10
 */
public interface DataAccessStrategy {

    /**
     * 历史数据服务
     * 根据电力营销户号查询指定日期的负荷数据
     * @param elecConsNo
     * @param dataDate
     * @return
     */
    ConsCurve queryDayLoadCurveByConsNo(String elecConsNo, String dataDate);

    /**
     * 历史数据服务
     * 根据电力营销户号查询指定日期范围内的负荷数据
     * @param elecConsNo
     * @param startDate,endDate
     * @return
     */
    List<ConsCurve> queryHistoryCurveList(String elecConsNo, String startDate, String endDate);

    /**
     * 实时数据服务
     * 根据电力营销户号查询指定日期的负荷数据
     * @param elecConsNo
     * @param dataDate
     * @return
     */
    ConsCurve queryTodayLoadCurveByConsNo(String elecConsNo, String dataDate);

    /**
     * 根据电力营销户号集合查询指定日期的负荷数据集合
     * @param elecConsNoList
     * @param dataDate
     * @return
     */
    List<ConsCurve> queryTodayCurveList(List<String> elecConsNoList, String dataDate);

    /**
     * 历史数据服务，多户号查询
     * @param elecConsNolist
     * @param dataDateList
     * @return
     */
    List<ConsCurve> queryHistoryCurveList(List<String> elecConsNolist, List<String> dataDateList);

    List<ConsCurve> queryHistoryCurvePage(List<String> elecConsNolist ,String dataDateList);

    /**
     * 实时数据服务
     * 根据电力营销户号查询指定日期的负荷电量
     * @param consIdList
     * @param dataDate
     * @return
     */

     List<ConsEnergyCurve> queryDayLoadEnergyByConsNo(List<String> consIdList, String dataDate);

    /**
     * 实时数据服务
     * 根据电力营销户号查询指定日期的负荷电量
     * @param elecConsNo
     * @param dataDate
     * @return
     */

    ConsEnergyCurve queryDayLoadEnergyByConsNo(String elecConsNo, String dataDate);



    /**
     * 实时数据服务
     * 根据电力营销户号查询指定日期的负荷电量
     * @param elecConsNo
     * @param dataDate
     * @return
     */
     ConsEnergyCurve queryTodayLoadEnergyByConsNo(String elecConsNo, String dataDate);

    /**
     * @description: 从营销档案获取用户数据
     * @param: elecNo 营销户号
     * @param consName 用户名
     * @param orgNo 供电单位
     * @return:
     * @author: PengChuqing
     * @date: 2021/5/20 19:15
     */
    Cons getConsFromMarketing(String elecNo, String consName, String orgNo);

    /**
     * 查询设备历史负荷
     * @param
     * @return
     */
    List<EquipmentRecordVO> queryDeviceHistoryCurvePage(String deviceId, String date);

    /**
     * 查询设备实时负荷
     * @param
     * @return
     */
    List<EquipmentRecordVO> queryDeviceRealTimeCurvePage(String deviceId);
}
