package com.xqxy.dr.modular.strategy;

import cn.hutool.log.Log;
import com.xqxy.dr.modular.data.entity.ConsCurve;
import com.xqxy.dr.modular.data.entity.ConsEnergyCurve;
import com.xqxy.dr.modular.data.mapper.CustomerMapper;
import com.xqxy.dr.modular.device.VO.EquipmentRecordVO;
import com.xqxy.eum.DataSourceEnum;
import com.xqxy.rentation.DsSwitcher;
import com.xqxy.sys.modular.cust.entity.Cons;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @ClassName PostgreSQLDataAccessStrategy
 * @Author dongdawei
 * @date 2021.11.18 17:49
 */
@Component
public class PostgreSQLDataAccessStrategy implements DataAccessStrategy {

    private static final Log log = Log.get();

    @Resource
    CustomerMapper customerMapper;

    @Override
    @DsSwitcher(DataSourceEnum.PG)
    public ConsCurve queryDayLoadCurveByConsNo(String elecConsNo, String dataDate) {
        List<ConsCurve> ConsCurves = customerMapper.queryDayLoadCurveByConsNo(elecConsNo, dataDate);
        ConsCurve ConsCurve = null;
        if (!CollectionUtils.isEmpty(ConsCurves)) {
            ConsCurve = ConsCurves.get(0);
        }
        return ConsCurve;
    }

    @Override
    @DsSwitcher(DataSourceEnum.PG)
    public List<ConsCurve> queryHistoryCurveList(String elecConsNo, String startDate, String endDate) {
        return customerMapper.queryHistoryCurveList(elecConsNo, startDate, endDate);
    }

    @Override
    @DsSwitcher(DataSourceEnum.PG)
    public ConsCurve queryTodayLoadCurveByConsNo(String elecConsNo, String dataDate) {
        List<ConsCurve> ConsCurves = customerMapper.queryDayLoadCurveByConsNo(elecConsNo, dataDate);
        ConsCurve ConsCurve = null;
        if (!CollectionUtils.isEmpty(ConsCurves)) {
            ConsCurve = ConsCurves.get(0);
        }
        return ConsCurve;
    }

    @Override
    @DsSwitcher(DataSourceEnum.PG)
    public List<ConsCurve> queryTodayCurveList(List<String> elecConsNoList, String dataDate) {
        List<ConsCurve> ConsCurves = new ArrayList<>();
        elecConsNoList.forEach(cons -> {
            List<ConsCurve> ConsCurvesInfo = customerMapper.queryDayLoadCurveByConsNo(cons, dataDate);
            if (!CollectionUtils.isEmpty(ConsCurvesInfo)) {
                ConsCurve ConsCurve = ConsCurvesInfo.get(0);
                ConsCurve.setConsId(cons);
                ConsCurves.add(ConsCurve);
                log.info(">>> 当前用户户号为:{}", cons);
            }
        });
        return ConsCurves;
    }

    @Override
    @DsSwitcher(DataSourceEnum.PG)
    public List<ConsCurve> queryHistoryCurveList(List<String> elecConsNolist, List<String> dataDateList) {
        List<ConsCurve> ConsCurves = new ArrayList<>();
        elecConsNolist.forEach(
                cons -> dataDateList.forEach(query -> {
                    List<ConsCurve> ConsCurvesInfo = customerMapper.queryDayLoadCurveByConsNo(cons, query);
                    if (!CollectionUtils.isEmpty(ConsCurvesInfo)) {
                        ConsCurve ConsCurve = ConsCurvesInfo.get(0);
                        ConsCurves.add(ConsCurve);
                    }
                })
        );
        return ConsCurves;
    }

    @Override
    @DsSwitcher(DataSourceEnum.PG)
    public List<ConsCurve> queryHistoryCurvePage(List<String> elecConsNolist, String dataDateList) {
        List<ConsCurve> ConsCurves = new ArrayList<>();
        elecConsNolist.forEach(cons -> {
            List<ConsCurve> ConsCurvesInfo = customerMapper.queryDayLoadCurveByConsNo(cons, dataDateList);
            if (!CollectionUtils.isEmpty(ConsCurvesInfo)) {
                ConsCurve ConsCurve = ConsCurvesInfo.get(0);
                ConsCurves.add(ConsCurve);
            }
        });
        return ConsCurves;
    }

    @Override
    @DsSwitcher(DataSourceEnum.PG)
    public List<ConsEnergyCurve> queryDayLoadEnergyByConsNo(List<String> consIdList, String dataDate) {

        return null;
    }

    @Override
    @DsSwitcher(DataSourceEnum.PG)
    public ConsEnergyCurve queryDayLoadEnergyByConsNo(String elecConsNo, String dataDate) {
        return customerMapper.queryDayLoadEnergyByConsNo(elecConsNo, dataDate);
    }

    @Override
    @DsSwitcher(DataSourceEnum.PG)
    public ConsEnergyCurve queryTodayLoadEnergyByConsNo(String elecConsNo, String dataDate) {
        return customerMapper.queryDayLoadEnergyByConsNo(elecConsNo, dataDate);
    }

    @Override
    @DsSwitcher(DataSourceEnum.PG)
    public Cons getConsFromMarketing(String elecNo, String consName, String orgNo) {
        // FIXME 实体类和返回字段不对应
        return customerMapper.getConsFromMarketing(elecNo, consName, orgNo);
    }

    @Override
    public List<EquipmentRecordVO> queryDeviceHistoryCurvePage(String deviceId, String date) {
        return null;
    }

    @Override
    public List<EquipmentRecordVO> queryDeviceRealTimeCurvePage(String deviceId) {
        return null;
    }
}
