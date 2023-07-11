package com.xqxy.dr.modular.strategy;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.xqxy.dr.modular.data.entity.ConsCurve;
import com.xqxy.dr.modular.data.entity.ConsEnergyCurve;
import com.xqxy.dr.modular.data.mapper.ConsCurveMapper;
import com.xqxy.dr.modular.device.VO.EquipmentRecordVO;
import com.xqxy.sys.modular.cust.entity.Cons;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.zip.DataFormatException;

/**
 * 获取中台数据
 */
@Component
@Slf4j
public class HttpGetDataAccessStrategy implements DataAccessStrategy {

    // 实时负荷历史数据
    @Value("${getRealTimeLoadHistoryUrl}")
    private String getRealTimeLoadHistoryUrl;

    // 实时负荷历史数据指定时间范围
    @Value("${getRealTimeLoadHistoryScopeUrl}")
    private String getRealTimeLoadHistoryScopeUrl;

    // 从营销档案获取用户数据
    @Value("${getMarketingArchivesUrl}")
    private String getMarketingArchivesUrl;

    @Autowired
    private ConsCurveMapper consCurveMapper;

    /**
     * 历史数据服务 根据电力营销户号查询指定日期的负荷数 单个用户
     * @param elecConsNo
     * @param dataDate
     * @return
     */
    @Override
    public ConsCurve queryDayLoadCurveByConsNo(String elecConsNo, String dataDate) {
        log.info("历史数据服务 根据电力营销户号查询指定日期的负荷数 单个用户 elecConsNo：{}， dataDate： {}", elecConsNo, dataDate);
        Map<String, Object> params = new HashMap<>();
        params.put("appCode", "1d266655d1f14363927cc655fcd52cb7");
        params.put("cons_no", elecConsNo);
        params.put("data_date", dataDate);
        String s = HttpUtil.get(getRealTimeLoadHistoryUrl, params);
        System.out.println(s);
        Map map = JSONUtil.toBean(s, Map.class);
        List data = (List) map.get("data");
        ConsCurve consCurve = getConsCurveInfo(data);
        log.info("历史数据服务 根据电力营销户号查询指定日期的负荷数 单个用户返回 consCurve： {}", consCurve);
        return consCurve;
    }

    /**
     * 历史数据服务
     * 根据电力营销户号查询指定日期范围内的负荷数据
     * @param elecConsNo
     * @param startDate,endDate
     * @return
     */
    @Override
    public List<ConsCurve> queryHistoryCurveList(String elecConsNo, String startDate, String endDate) {
        log.info("历史数据服务 根据电力营销户号查询指定日期范围内的负荷数据 单个用户 elecConsNo：{}， startDate： {}， endDate： {}", elecConsNo, startDate, endDate);
        Map<String, Object> params = new HashMap<>();
        params.put("appCode", "1d266655d1f14363927cc655fcd52cb7");
        params.put("cons_no", elecConsNo);
        params.put("start_date", startDate);
        params.put("end_time", endDate);
        String s = HttpUtil.get(getRealTimeLoadHistoryScopeUrl, params);
        System.out.println(s);
        Map map = JSONUtil.toBean(s, Map.class);
        List data = (List) map.get("data");
        List<ConsCurve> consCurveList = getConsCurveList(data);
        log.info("历史数据服务 根据电力营销户号查询指定日期范围内的负荷数据 单个用户返回 consCurveList： {}", consCurveList);
        return consCurveList;
    }

    /**
     * 实时数据服务
     * 根据电力营销户号查询指定日期的负荷数据
     * @param elecConsNo
     * @param dataDate
     * @return
     */
    @Override
    public ConsCurve queryTodayLoadCurveByConsNo(String elecConsNo, String dataDate) {
        log.info("实时数据根据电力营销户号查询指定日期的负荷数据单个用户 elecConsNo：{}， dataDate： {}", elecConsNo, dataDate);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ConsCurve consCurve = new ConsCurve();
        try {
            String date = dateFormat.format(format.parse(dataDate));
            consCurve = consCurveMapper.queryConsCurveRealLoad(elecConsNo, date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        log.info("实时数据根据电力营销户号查询指定日期的负荷数据单个用户返回 consCurve： {}", consCurve.toString());
        return consCurve;
    }

    /**
     * 根据电力营销户号集合查询指定日期的负荷数据集合 多个用户
     * @param elecConsNoList
     * @param dataDate
     * @return
     */
    @Override
    public List<ConsCurve> queryTodayCurveList(List<String> elecConsNoList, String dataDate) {
        log.info("根据电力营销户号集合查询指定日期的负荷数据集合 多个用户 elecConsNoList：{}， dataDate： {}", elecConsNoList, dataDate);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<ConsCurve> consCurveList = new ArrayList<>();
        try {
            String date = dateFormat.format(format.parse(dataDate));
            consCurveList = consCurveMapper.queryConsCurveRealLoadList(elecConsNoList, date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        log.info("根据电力营销户号集合查询指定日期的负荷数据集合 多个用户返回 consCurveList： {}", consCurveList.toString());
        return consCurveList;
    }

    /**
     * 历史数据服务，多户号查询
     * @param elecConsNolist
     * @param dataDateList
     * @return
     */
    @Override
    public List<ConsCurve> queryHistoryCurveList(List<String> elecConsNolist, List<String> dataDateList) {
        log.info("历史数据服务，多户号查询 elecConsNolist：{}， dataDateList： {}", elecConsNolist, dataDateList);
        String no = StringUtils.EMPTY;
        if (CollectionUtils.isNotEmpty(elecConsNolist)) {
            for (String str : elecConsNolist) {
                no += str + ",";
            }
        }
        String date = StringUtils.EMPTY;
        if (CollectionUtils.isNotEmpty(dataDateList)) {
            for (String str : dataDateList) {
                date += str + ",";
            }
        }
        Map<String, Object> params = new HashMap<>();
        params.put("appCode", "1d266655d1f14363927cc655fcd52cb7");
        params.put("cons_no", no);
        params.put("data_date", date);
        String s = HttpUtil.get(getRealTimeLoadHistoryUrl, params);
        System.out.println(s);
        Map map = JSONUtil.toBean(s, Map.class);
        List data = (List) map.get("data");
        List<ConsCurve> consCurveList = getConsCurveList(data);
        log.info("历史数据服务，多户号查询返回 consCurveList： {}", consCurveList);
        return consCurveList;
    }

    @Override
    public List<ConsCurve> queryHistoryCurvePage(List<String> elecConsNolist, String dataDateList) {
        log.info("历史数据服务，多户号查询 elecConsNolist：{}， dataDateList： {}", elecConsNolist, dataDateList);
        String no = StringUtils.EMPTY;
        if (CollectionUtils.isNotEmpty(elecConsNolist)) {
            for (String str : elecConsNolist) {
                no += str + ",";
            }
        }
        Map<String, Object> params = new HashMap<>();
        params.put("appCode", "1d266655d1f14363927cc655fcd52cb7");
        params.put("cons_no", no);
        params.put("data_date", dataDateList);
        String s = HttpUtil.get(getRealTimeLoadHistoryUrl, params);
        System.out.println(s);
        Map map = JSONUtil.toBean(s, Map.class);
        List data = (List) map.get("data");
        List<ConsCurve> consCurveList = getConsCurveList(data);
        log.info("历史数据服务，多户号查询返回 consCurveList： {}", consCurveList);
        return consCurveList;
    }

    @Override
    public List<ConsEnergyCurve> queryDayLoadEnergyByConsNo(List<String> consIdList, String dataDate) {
        return null;
    }

    /**
     * 实时数据服务
     * 根据电力营销户号查询指定日期的负荷电量
     * @param elecConsNo
     * @param dataDate
     * @return
     */
    @Override
    public ConsEnergyCurve queryDayLoadEnergyByConsNo(String elecConsNo, String dataDate) {
        log.info("实时数据单个用户queryDayLoadEnergyByConsNo elecConsNo：{}， dataDate： {}", elecConsNo, dataDate);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ConsEnergyCurve consEnergyCurve = new ConsEnergyCurve();
        try {
            String date = dateFormat.format(format.parse(dataDate));
            consEnergyCurve = consCurveMapper.queryConsEnergyCurveRealLoad(elecConsNo, date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        log.info("实时数据单个用户返回queryDayLoadEnergyByConsNo consEnergyCurve： {}", consEnergyCurve.toString());
        return consEnergyCurve;
    }

    /**
     * 实时数据服务
     * 根据电力营销户号查询指定日期的负荷电量
     * @param elecConsNo
     * @param dataDate
     * @return
     */
    @Override
    public ConsEnergyCurve queryTodayLoadEnergyByConsNo(String elecConsNo, String dataDate) {
        log.info("实时数据单个用户queryTodayLoadEnergyByConsNo elecConsNo：{}， dataDate： {}", elecConsNo, dataDate);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ConsEnergyCurve consEnergyCurve = new ConsEnergyCurve();
        try {
            String date = dateFormat.format(format.parse(dataDate));
            consEnergyCurve = consCurveMapper.queryConsEnergyCurveRealLoad(elecConsNo, date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        log.info("实时数据单个用户返回queryTodayLoadEnergyByConsNo consEnergyCurve： {}", consEnergyCurve.toString());
        return consEnergyCurve;
    }

    /**
     * @description: 从营销档案获取用户数据
     * @param: elecNo 营销户号
     * @param consName 用户名
     * @param orgNo 供电单位
     * @return:
     * @author: PengChuqing
     * @date: 2021/5/20 19:15
     */
    @Override
    public Cons getConsFromMarketing(String elecNo, String consName, String orgNo) {
        log.info("营销档案获取用户数据 elecNo：{}， consName： {}， orgNo： {}", elecNo, consName, orgNo);
        Map<String, Object> params = new HashMap<>();
        params.put("appCode", "1d266655d1f14363927cc655fcd52cb7");
        params.put("cons_on", elecNo);
        String s = HttpUtil.get(getMarketingArchivesUrl, params);
        System.out.println(s);
        Map map = JSONUtil.toBean(s, Map.class);
        List data = (List) map.get("data");
        Cons cons = new Cons();
        if (CollectionUtils.isNotEmpty(data)) {
            Map info = JSONUtil.toBean((JSONObject) data.get(0), Map.class);
            /*Integer custId = (Integer) info.get("CUST_ID");
            cons.setCustId(custId.longValue());*/
            String orgNo1 = (String) info.get("ORG_NO");
            cons.setOrgNo(orgNo1);
            String elecAddr = (String) info.get("ELEC_ADDR");
            cons.setElecAddr(elecAddr);
            String voltCode = (String) info.get("VOLTCODE");
            cons.setVoltCode(voltCode);
            String consName1 = (String) info.get("CONS_NAME");
            cons.setConsName(consName1);
            String bigTradeCode = (String) info.get("BIG_TRADE_CODE");
            cons.setBigTradeCode(bigTradeCode);
            Double contractCap = (Double) info.get("CONTRACT_CAP");
            cons.setContractCap(new BigDecimal(contractCap));
            String id = (String) info.get("ID");
            cons.setId(id);
            Double runCap = (Double) info.get("RUN_CAP");
            cons.setRunCap(new BigDecimal(runCap));
        }
        log.info("营销档案获取用户数据返回 cons: {}", cons);
        return cons;
    }

    @Override
    public List<EquipmentRecordVO> queryDeviceHistoryCurvePage(String deviceId, String date) {
        return null;
    }

    @Override
    public List<EquipmentRecordVO> queryDeviceRealTimeCurvePage(String deviceId) {
        return null;
    }

    /**
     * 获取负荷历史数据 多条
     * @param data
     * @return
     */
    private List<ConsCurve> getConsCurveList(List data) {
        List<ConsCurve> consCurveList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(data)) {
            for (int i = 0; i < data.size(); i++) {
                ConsCurve consCurve = new ConsCurve();
                Map info = JSONUtil.toBean((JSONObject) data.get(i), Map.class);
                String consNo = (String) info.get("cons_no");
                consCurve.setConsId(consNo);
                String dataDate = (String) info.get("data_date");
                if (StringUtils.isNotEmpty(dataDate)) {
                    consCurve.setDataDate(LocalDate.parse(dataDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                }
                Integer dataPointFlag = (Integer) info.get("data_point_flag");
                consCurve.setDataPointFlag(String.valueOf(dataPointFlag));

                Double p1 = (Double) info.get("p1");
                consCurve.setP1(new BigDecimal(p1).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p2 = (Double) info.get("p2");
                consCurve.setP2(new BigDecimal(p2).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p3 = (Double) info.get("p3");
                consCurve.setP3(new BigDecimal(p3).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p4 = (Double) info.get("p4");
                consCurve.setP4(new BigDecimal(p4).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p5 = (Double) info.get("p5");
                consCurve.setP5(new BigDecimal(p5).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p6 = (Double) info.get("p6");
                consCurve.setP6(new BigDecimal(p6).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p7 = (Double) info.get("p7");
                consCurve.setP7(new BigDecimal(p7).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p8 = (Double) info.get("p8");
                consCurve.setP8(new BigDecimal(p8).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p9 = (Double) info.get("p9");
                consCurve.setP9(new BigDecimal(p9).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p10 = (Double) info.get("p10");
                consCurve.setP10(new BigDecimal(p10).setScale(4, BigDecimal.ROUND_HALF_UP));

                Double p11 = (Double) info.get("p11");
                consCurve.setP11(new BigDecimal(p11).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p12 = (Double) info.get("p12");
                consCurve.setP12(new BigDecimal(p12).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p13 = (Double) info.get("p13");
                consCurve.setP13(new BigDecimal(p13).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p14 = (Double) info.get("p14");
                consCurve.setP14(new BigDecimal(p14).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p15 = (Double) info.get("p15");
                consCurve.setP15(new BigDecimal(p15).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p16 = (Double) info.get("p16");
                consCurve.setP16(new BigDecimal(p16).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p17 = (Double) info.get("p17");
                consCurve.setP17(new BigDecimal(p17).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p18 = (Double) info.get("p18");
                consCurve.setP18(new BigDecimal(p18).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p19 = (Double) info.get("p19");
                consCurve.setP19(new BigDecimal(p19).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p20 = (Double) info.get("p20");
                consCurve.setP20(new BigDecimal(p20).setScale(4, BigDecimal.ROUND_HALF_UP));

                Double p21 = (Double) info.get("p21");
                consCurve.setP21(new BigDecimal(p21).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p22 = (Double) info.get("p22");
                consCurve.setP22(new BigDecimal(p22).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p23 = (Double) info.get("p23");
                consCurve.setP23(new BigDecimal(p23).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p24 = (Double) info.get("p24");
                consCurve.setP24(new BigDecimal(p24).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p25 = (Double) info.get("p25");
                consCurve.setP25(new BigDecimal(p25).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p26 = (Double) info.get("p26");
                consCurve.setP26(new BigDecimal(p26).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p27 = (Double) info.get("p27");
                consCurve.setP27(new BigDecimal(p27).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p28 = (Double) info.get("p28");
                consCurve.setP28(new BigDecimal(p28).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p29 = (Double) info.get("p29");
                consCurve.setP29(new BigDecimal(p29).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p30 = (Double) info.get("p30");
                consCurve.setP30(new BigDecimal(p30).setScale(4, BigDecimal.ROUND_HALF_UP));

                Double p31 = (Double) info.get("p31");
                consCurve.setP31(new BigDecimal(p31).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p32 = (Double) info.get("p32");
                consCurve.setP32(new BigDecimal(p32).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p33 = (Double) info.get("p33");
                consCurve.setP33(new BigDecimal(p33).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p34 = (Double) info.get("p34");
                consCurve.setP34(new BigDecimal(p34).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p35 = (Double) info.get("p35");
                consCurve.setP35(new BigDecimal(p35).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p36 = (Double) info.get("p36");
                consCurve.setP36(new BigDecimal(p36).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p37 = (Double) info.get("p37");
                consCurve.setP37(new BigDecimal(p37).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p38 = (Double) info.get("p38");
                consCurve.setP38(new BigDecimal(p38).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p39 = (Double) info.get("p39");
                consCurve.setP39(new BigDecimal(p39).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p40 = (Double) info.get("p40");
                consCurve.setP40(new BigDecimal(p40).setScale(4, BigDecimal.ROUND_HALF_UP));

                Double p41 = (Double) info.get("p41");
                consCurve.setP41(new BigDecimal(p41).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p42 = (Double) info.get("p42");
                consCurve.setP42(new BigDecimal(p42).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p43 = (Double) info.get("p43");
                consCurve.setP43(new BigDecimal(p43).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p44 = (Double) info.get("p44");
                consCurve.setP44(new BigDecimal(p44).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p45 = (Double) info.get("p45");
                consCurve.setP45(new BigDecimal(p45).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p46 = (Double) info.get("p46");
                consCurve.setP46(new BigDecimal(p46).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p47 = (Double) info.get("p47");
                consCurve.setP47(new BigDecimal(p47).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p48 = (Double) info.get("p48");
                consCurve.setP48(new BigDecimal(p48).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p49 = (Double) info.get("p49");
                consCurve.setP49(new BigDecimal(p49).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p50 = (Double) info.get("p50");
                consCurve.setP50(new BigDecimal(p50).setScale(4, BigDecimal.ROUND_HALF_UP));

                Double p51 = (Double) info.get("p51");
                consCurve.setP51(new BigDecimal(p51).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p52 = (Double) info.get("p52");
                consCurve.setP52(new BigDecimal(p52).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p53 = (Double) info.get("p53");
                consCurve.setP53(new BigDecimal(p53).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p54 = (Double) info.get("p54");
                consCurve.setP54(new BigDecimal(p54).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p55 = (Double) info.get("p55");
                consCurve.setP55(new BigDecimal(p55).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p56 = (Double) info.get("p56");
                consCurve.setP56(new BigDecimal(p56).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p57 = (Double) info.get("p57");
                consCurve.setP57(new BigDecimal(p57).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p58 = (Double) info.get("p58");
                consCurve.setP58(new BigDecimal(p58).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p59 = (Double) info.get("p59");
                consCurve.setP59(new BigDecimal(p59).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p60 = (Double) info.get("p60");
                consCurve.setP60(new BigDecimal(p60).setScale(4, BigDecimal.ROUND_HALF_UP));

                Double p61 = (Double) info.get("p61");
                consCurve.setP61(new BigDecimal(p61).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p62 = (Double) info.get("p62");
                consCurve.setP62(new BigDecimal(p62).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p63 = (Double) info.get("p63");
                consCurve.setP63(new BigDecimal(p63).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p64 = (Double) info.get("p64");
                consCurve.setP64(new BigDecimal(p64).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p65 = (Double) info.get("p65");
                consCurve.setP65(new BigDecimal(p65).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p66 = (Double) info.get("p66");
                consCurve.setP66(new BigDecimal(p66).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p67 = (Double) info.get("p67");
                consCurve.setP67(new BigDecimal(p67).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p68 = (Double) info.get("p68");
                consCurve.setP68(new BigDecimal(p68).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p69 = (Double) info.get("p69");
                consCurve.setP69(new BigDecimal(p69).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p70 = (Double) info.get("p70");
                consCurve.setP70(new BigDecimal(p70).setScale(4, BigDecimal.ROUND_HALF_UP));

                Double p71 = (Double) info.get("p71");
                consCurve.setP71(new BigDecimal(p71).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p72 = (Double) info.get("p72");
                consCurve.setP72(new BigDecimal(p72).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p73 = (Double) info.get("p73");
                consCurve.setP73(new BigDecimal(p73).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p74 = (Double) info.get("p74");
                consCurve.setP74(new BigDecimal(p74).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p75 = (Double) info.get("p75");
                consCurve.setP75(new BigDecimal(p75).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p76 = (Double) info.get("p76");
                consCurve.setP76(new BigDecimal(p76).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p77 = (Double) info.get("p77");
                consCurve.setP77(new BigDecimal(p77).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p78 = (Double) info.get("p78");
                consCurve.setP78(new BigDecimal(p78).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p79 = (Double) info.get("p79");
                consCurve.setP79(new BigDecimal(p79).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p80 = (Double) info.get("p80");
                consCurve.setP80(new BigDecimal(p80).setScale(4, BigDecimal.ROUND_HALF_UP));

                Double p81 = (Double) info.get("p81");
                consCurve.setP81(new BigDecimal(p81).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p82 = (Double) info.get("p82");
                consCurve.setP82(new BigDecimal(p82).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p83 = (Double) info.get("p83");
                consCurve.setP83(new BigDecimal(p83).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p84 = (Double) info.get("p84");
                consCurve.setP84(new BigDecimal(p84).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p85 = (Double) info.get("p85");
                consCurve.setP85(new BigDecimal(p85).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p86 = (Double) info.get("p86");
                consCurve.setP86(new BigDecimal(p86).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p87 = (Double) info.get("p87");
                consCurve.setP87(new BigDecimal(p87).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p88 = (Double) info.get("p88");
                consCurve.setP88(new BigDecimal(p88).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p89 = (Double) info.get("p89");
                consCurve.setP89(new BigDecimal(p89).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p90 = (Double) info.get("p90");
                consCurve.setP90(new BigDecimal(p90).setScale(4, BigDecimal.ROUND_HALF_UP));

                Double p91 = (Double) info.get("p91");
                consCurve.setP91(new BigDecimal(p91).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p92 = (Double) info.get("p92");
                consCurve.setP92(new BigDecimal(p92).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p93 = (Double) info.get("p93");
                consCurve.setP93(new BigDecimal(p93).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p94 = (Double) info.get("p94");
                consCurve.setP94(new BigDecimal(p94).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p95 = (Double) info.get("p95");
                consCurve.setP95(new BigDecimal(p95).setScale(4, BigDecimal.ROUND_HALF_UP));
                Double p96 = (Double) info.get("p96");
                consCurve.setP96(new BigDecimal(p96).setScale(4, BigDecimal.ROUND_HALF_UP));
                consCurveList.add(consCurve);
            }
        }
        return consCurveList;
    }

    /**
     * 获取负荷历史数据 单条
     * @param data
     * @return
     */
    private ConsCurve getConsCurveInfo(List data) {
        ConsCurve consCurve = new ConsCurve();
        if (CollectionUtils.isNotEmpty(data)) {
            Map info = JSONUtil.toBean((JSONObject) data.get(0), Map.class);
            String consNo = (String) info.get("cons_no");
            consCurve.setConsId(consNo);
            String dataDate = (String) info.get("data_date");
            if (StringUtils.isNotEmpty(dataDate)) {
                consCurve.setDataDate(LocalDate.parse(dataDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            }
            Integer dataPointFlag = (Integer) info.get("data_point_flag");
            consCurve.setDataPointFlag(String.valueOf(dataPointFlag));

            Double p1 = (Double) info.get("p1");
            consCurve.setP1(new BigDecimal(p1).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p2 = (Double) info.get("p2");
            consCurve.setP2(new BigDecimal(p2).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p3 = (Double) info.get("p3");
            consCurve.setP3(new BigDecimal(p3).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p4 = (Double) info.get("p4");
            consCurve.setP4(new BigDecimal(p4).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p5 = (Double) info.get("p5");
            consCurve.setP5(new BigDecimal(p5).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p6 = (Double) info.get("p6");
            consCurve.setP6(new BigDecimal(p6).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p7 = (Double) info.get("p7");
            consCurve.setP7(new BigDecimal(p7).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p8 = (Double) info.get("p8");
            consCurve.setP8(new BigDecimal(p8).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p9 = (Double) info.get("p9");
            consCurve.setP9(new BigDecimal(p9).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p10 = (Double) info.get("p10");
            consCurve.setP10(new BigDecimal(p10).setScale(4, BigDecimal.ROUND_HALF_UP));

            Double p11 = (Double) info.get("p11");
            consCurve.setP11(new BigDecimal(p11).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p12 = (Double) info.get("p12");
            consCurve.setP12(new BigDecimal(p12).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p13 = (Double) info.get("p13");
            consCurve.setP13(new BigDecimal(p13).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p14 = (Double) info.get("p14");
            consCurve.setP14(new BigDecimal(p14).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p15 = (Double) info.get("p15");
            consCurve.setP15(new BigDecimal(p15).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p16 = (Double) info.get("p16");
            consCurve.setP16(new BigDecimal(p16).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p17 = (Double) info.get("p17");
            consCurve.setP17(new BigDecimal(p17).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p18 = (Double) info.get("p18");
            consCurve.setP18(new BigDecimal(p18).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p19 = (Double) info.get("p19");
            consCurve.setP19(new BigDecimal(p19).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p20 = (Double) info.get("p20");
            consCurve.setP20(new BigDecimal(p20).setScale(4, BigDecimal.ROUND_HALF_UP));

            Double p21 = (Double) info.get("p21");
            consCurve.setP21(new BigDecimal(p21).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p22 = (Double) info.get("p22");
            consCurve.setP22(new BigDecimal(p22).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p23 = (Double) info.get("p23");
            consCurve.setP23(new BigDecimal(p23).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p24 = (Double) info.get("p24");
            consCurve.setP24(new BigDecimal(p24).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p25 = (Double) info.get("p25");
            consCurve.setP25(new BigDecimal(p25).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p26 = (Double) info.get("p26");
            consCurve.setP26(new BigDecimal(p26).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p27 = (Double) info.get("p27");
            consCurve.setP27(new BigDecimal(p27).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p28 = (Double) info.get("p28");
            consCurve.setP28(new BigDecimal(p28).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p29 = (Double) info.get("p29");
            consCurve.setP29(new BigDecimal(p29).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p30 = (Double) info.get("p30");
            consCurve.setP30(new BigDecimal(p30).setScale(4, BigDecimal.ROUND_HALF_UP));

            Double p31 = (Double) info.get("p31");
            consCurve.setP31(new BigDecimal(p31).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p32 = (Double) info.get("p32");
            consCurve.setP32(new BigDecimal(p32).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p33 = (Double) info.get("p33");
            consCurve.setP33(new BigDecimal(p33).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p34 = (Double) info.get("p34");
            consCurve.setP34(new BigDecimal(p34).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p35 = (Double) info.get("p35");
            consCurve.setP35(new BigDecimal(p35).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p36 = (Double) info.get("p36");
            consCurve.setP36(new BigDecimal(p36).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p37 = (Double) info.get("p37");
            consCurve.setP37(new BigDecimal(p37).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p38 = (Double) info.get("p38");
            consCurve.setP38(new BigDecimal(p38).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p39 = (Double) info.get("p39");
            consCurve.setP39(new BigDecimal(p39).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p40 = (Double) info.get("p40");
            consCurve.setP40(new BigDecimal(p40).setScale(4, BigDecimal.ROUND_HALF_UP));

            Double p41 = (Double) info.get("p41");
            consCurve.setP41(new BigDecimal(p41).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p42 = (Double) info.get("p42");
            consCurve.setP42(new BigDecimal(p42).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p43 = (Double) info.get("p43");
            consCurve.setP43(new BigDecimal(p43).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p44 = (Double) info.get("p44");
            consCurve.setP44(new BigDecimal(p44).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p45 = (Double) info.get("p45");
            consCurve.setP45(new BigDecimal(p45).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p46 = (Double) info.get("p46");
            consCurve.setP46(new BigDecimal(p46).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p47 = (Double) info.get("p47");
            consCurve.setP47(new BigDecimal(p47).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p48 = (Double) info.get("p48");
            consCurve.setP48(new BigDecimal(p48).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p49 = (Double) info.get("p49");
            consCurve.setP49(new BigDecimal(p49).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p50 = (Double) info.get("p50");
            consCurve.setP50(new BigDecimal(p50).setScale(4, BigDecimal.ROUND_HALF_UP));

            Double p51 = (Double) info.get("p51");
            consCurve.setP51(new BigDecimal(p51).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p52 = (Double) info.get("p52");
            consCurve.setP52(new BigDecimal(p52).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p53 = (Double) info.get("p53");
            consCurve.setP53(new BigDecimal(p53).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p54 = (Double) info.get("p54");
            consCurve.setP54(new BigDecimal(p54).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p55 = (Double) info.get("p55");
            consCurve.setP55(new BigDecimal(p55).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p56 = (Double) info.get("p56");
            consCurve.setP56(new BigDecimal(p56).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p57 = (Double) info.get("p57");
            consCurve.setP57(new BigDecimal(p57).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p58 = (Double) info.get("p58");
            consCurve.setP58(new BigDecimal(p58).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p59 = (Double) info.get("p59");
            consCurve.setP59(new BigDecimal(p59).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p60 = (Double) info.get("p60");
            consCurve.setP60(new BigDecimal(p60).setScale(4, BigDecimal.ROUND_HALF_UP));

            Double p61 = (Double) info.get("p61");
            consCurve.setP61(new BigDecimal(p61).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p62 = (Double) info.get("p62");
            consCurve.setP62(new BigDecimal(p62).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p63 = (Double) info.get("p63");
            consCurve.setP63(new BigDecimal(p63).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p64 = (Double) info.get("p64");
            consCurve.setP64(new BigDecimal(p64).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p65 = (Double) info.get("p65");
            consCurve.setP65(new BigDecimal(p65).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p66 = (Double) info.get("p66");
            consCurve.setP66(new BigDecimal(p66).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p67 = (Double) info.get("p67");
            consCurve.setP67(new BigDecimal(p67).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p68 = (Double) info.get("p68");
            consCurve.setP68(new BigDecimal(p68).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p69 = (Double) info.get("p69");
            consCurve.setP69(new BigDecimal(p69).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p70 = (Double) info.get("p70");
            consCurve.setP70(new BigDecimal(p70).setScale(4, BigDecimal.ROUND_HALF_UP));

            Double p71 = (Double) info.get("p71");
            consCurve.setP71(new BigDecimal(p71).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p72 = (Double) info.get("p72");
            consCurve.setP72(new BigDecimal(p72).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p73 = (Double) info.get("p73");
            consCurve.setP73(new BigDecimal(p73).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p74 = (Double) info.get("p74");
            consCurve.setP74(new BigDecimal(p74).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p75 = (Double) info.get("p75");
            consCurve.setP75(new BigDecimal(p75).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p76 = (Double) info.get("p76");
            consCurve.setP76(new BigDecimal(p76).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p77 = (Double) info.get("p77");
            consCurve.setP77(new BigDecimal(p77).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p78 = (Double) info.get("p78");
            consCurve.setP78(new BigDecimal(p78).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p79 = (Double) info.get("p79");
            consCurve.setP79(new BigDecimal(p79).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p80 = (Double) info.get("p80");
            consCurve.setP80(new BigDecimal(p80).setScale(4, BigDecimal.ROUND_HALF_UP));

            Double p81 = (Double) info.get("p81");
            consCurve.setP81(new BigDecimal(p81).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p82 = (Double) info.get("p82");
            consCurve.setP82(new BigDecimal(p82).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p83 = (Double) info.get("p83");
            consCurve.setP83(new BigDecimal(p83).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p84 = (Double) info.get("p84");
            consCurve.setP84(new BigDecimal(p84).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p85 = (Double) info.get("p85");
            consCurve.setP85(new BigDecimal(p85).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p86 = (Double) info.get("p86");
            consCurve.setP86(new BigDecimal(p86).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p87 = (Double) info.get("p87");
            consCurve.setP87(new BigDecimal(p87).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p88 = (Double) info.get("p88");
            consCurve.setP88(new BigDecimal(p88).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p89 = (Double) info.get("p89");
            consCurve.setP89(new BigDecimal(p89).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p90 = (Double) info.get("p90");
            consCurve.setP90(new BigDecimal(p90).setScale(4, BigDecimal.ROUND_HALF_UP));

            Double p91 = (Double) info.get("p91");
            consCurve.setP91(new BigDecimal(p91).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p92 = (Double) info.get("p92");
            consCurve.setP92(new BigDecimal(p92).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p93 = (Double) info.get("p93");
            consCurve.setP93(new BigDecimal(p93).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p94 = (Double) info.get("p94");
            consCurve.setP94(new BigDecimal(p94).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p95 = (Double) info.get("p95");
            consCurve.setP95(new BigDecimal(p95).setScale(4, BigDecimal.ROUND_HALF_UP));
            Double p96 = (Double) info.get("p96");
            consCurve.setP96(new BigDecimal(p96).setScale(4, BigDecimal.ROUND_HALF_UP));

        }
        return consCurve;
    }


}
