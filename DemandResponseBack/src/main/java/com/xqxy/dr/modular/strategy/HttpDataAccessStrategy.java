package com.xqxy.dr.modular.strategy;

import com.xqxy.dr.modular.data.entity.ConsCurve;
import com.xqxy.dr.modular.data.entity.ConsEnergyCurve;
import com.xqxy.dr.modular.device.VO.EquipmentRecordVO;
import com.xqxy.sys.modular.cust.entity.Cons;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HttpDataAccessStrategy implements DataAccessStrategy {

    @Override
    public ConsCurve queryDayLoadCurveByConsNo(String elecConsNo, String dataDate) {
        return null;
    }

    @Override
    public List<ConsCurve> queryHistoryCurveList(String elecConsNo, String startDate, String endDate) {
        return null;
    }

    @Override
    public ConsCurve queryTodayLoadCurveByConsNo(String elecConsNo, String dataDate) {
        return null;
    }

    @Override
    public List<ConsCurve> queryTodayCurveList(List<String> elecConsNoList, String dataDate) {
        return null;
    }

    @Override
    public List<ConsCurve> queryHistoryCurveList(List<String> elecConsNolist, List<String> dataDateList) {
        return null;
    }

    @Override
    public List<ConsCurve> queryHistoryCurvePage(List<String> elecConsNolist, String dataDateList) {
        return null;
    }

    @Override
    public List<ConsEnergyCurve> queryDayLoadEnergyByConsNo(List<String> consIdList, String dataDate) {
        return null;
    }

    @Override
    public ConsEnergyCurve queryDayLoadEnergyByConsNo(String elecConsNo, String dataDate) {
        return null;
    }

    @Override
    public ConsEnergyCurve queryTodayLoadEnergyByConsNo(String elecConsNo, String dataDate) {
        return null;
    }

    @Override
    public Cons getConsFromMarketing(String elecNo, String consName, String orgNo) {
        return null;
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
