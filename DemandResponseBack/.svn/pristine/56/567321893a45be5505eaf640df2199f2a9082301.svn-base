package com.xqxy.dr.modular.prediction.service.impl;

import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.dr.modular.data.entity.ConsCurve;
import com.xqxy.dr.modular.data.service.ConsCurveService;
import com.xqxy.dr.modular.prediction.entity.ConsAbility;
import com.xqxy.dr.modular.prediction.enums.RegulatoryTypeEnum;
import com.xqxy.dr.modular.prediction.mapper.ConsAbilityMapper;
import com.xqxy.dr.modular.prediction.param.*;
import com.xqxy.dr.modular.prediction.result.AreaPrediction;
import com.xqxy.dr.modular.prediction.result.ConsPrediction;
import com.xqxy.dr.modular.prediction.service.ConsAbilityService;
import com.xqxy.dr.modular.prediction.util.DateFormatUtil;
import com.xqxy.sys.modular.calendar.entity.CalendarInfo;
import com.xqxy.sys.modular.calendar.service.CalendarInfoService;
import com.xqxy.sys.modular.cust.entity.Cons;
import com.xqxy.sys.modular.cust.service.ConsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *  负荷预测、潜力分析页面 接口实现
 * @author wangyunfei
 * @since 2021-11-04
 */
@Service
public class ConsAbilityServiceImpl extends ServiceImpl<ConsAbilityMapper, ConsAbility> implements ConsAbilityService {

    private static final Log log = Log.get();

    @Resource
    ConsAbilityMapper consAbilityMapper;

    @Resource
    private ConsCurveService consCurveService;

    @Resource
    private ConsService consService;

    @Resource
    private CalendarInfoService calendarInfoService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public AreaPrediction queryAreaPrediction(String areaId, String statDate, String areaType) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate parse;
        if(StringUtils.isEmpty(statDate)){
            parse = LocalDate.now();
        }else {
            parse = LocalDate.parse(statDate, fmt);
        }
        //得到一个 日历集合
        List<CalendarInfo> calendarInfoList = calendarInfoService.list();

        //判断是否 在日历表
        CalendarInfo calInfo = new CalendarInfo();
        for (CalendarInfo calendarInfo : calendarInfoList) {
            if(calendarInfo.getCdrDate().equals(parse)){
                BeanUtils.copyProperties(calendarInfo,calInfo);
                break;
            }
        }

        //查询历史负荷 的 时间
        LocalDate paramDate = null;
        //判断 date 是否是工作日
        if(calInfo != null){
            //在日历表
            if(!"1".equals(calInfo.getDateType())){
                //不是工作日  取上一个非工作日
                //取非工作日集合
                List<CalendarInfo> noWorkDays = calendarInfoList.stream().filter(n -> !"1".equals(n.getDateType())).collect(Collectors.toList());

                Integer index = null;
                for (int i = 0; i < noWorkDays.size(); i++) {
                    CalendarInfo calendarInfo = noWorkDays.get(i);
                    if(calendarInfo.getId().equals(calInfo.getId())){
                        index = i;
                        break;
                    }
                }

                if(index == 0){
                    //如果是 取得日期是第一个 节假日 返回null
                    return null;
                }else {
                    //前一个周末
                    CalendarInfo info = noWorkDays.get(index - 1);
                    paramDate = info.getCdrDate();

                    log.info("是节假日 取前一个周末 paramDate 的值 ===============> "+paramDate);
                }
            }else {
                //是工作日  取上一个工作日
                List<CalendarInfo> workDays = calendarInfoList.stream().filter(n -> "1".equals(n.getDateType())).collect(Collectors.toList());
                Integer index = null;
                for (int i = 0; i < workDays.size(); i++) {
                    CalendarInfo calendarInfo = workDays.get(i);
                    if(calendarInfo.getId().equals(calInfo.getId())){
                        index = i;
                        break;
                    }
                }

                if(index == 0){
                    //如果是 取得日期是第一个 工作日 返回null
                    return null;
                }else {
                    //前一个工作日
                    CalendarInfo info = workDays.get(index - 1);
                    paramDate = info.getCdrDate();

                    log.info("是工作日 取前一个工作日 paramDate 的值 ===============> "+paramDate);
                }
            }
        }else {
            //不在日历表 直接 日期-2 的历史负荷
            // 传参转换成localdate
            paramDate = LocalDate.parse(statDate, fmt).plusDays(-2);
            log.info("不在日历表  paramDate 的值 ===============> "+paramDate);
        }

        //根据区域id 拿到consIDList
        LambdaQueryWrapper<Cons> consqueryWrapper = new LambdaQueryWrapper<>();
        if(!StringUtils.isEmpty(areaType)){
            if("PROVINCE_CODE".equals(areaType)){
                consqueryWrapper.eq(Cons::getProvinceCode,areaId);
            }
            if("CITY_CODE".equals(areaType)){
                consqueryWrapper.eq(Cons::getCityCode,areaId);
            }
            if("COUNTY_CODE".equals(areaType)){
                consqueryWrapper.eq(Cons::getCountyCode,areaId);
            }
            if("STREET_CODE".equals(areaType)){
                consqueryWrapper.eq(Cons::getStreetCode,areaId);
            }
            List<Cons> consList = consService.list(consqueryWrapper);
            if(!CollectionUtils.isEmpty(consList)){
                List<String> condIdList = consList.stream().map(Cons::getId).collect(Collectors.toList());
                //sql  计算总的 p1,p2 ......
                ConsCurve consCurve = consCurveService.getCurveByConsIdListAndDate(condIdList,paramDate.toString());
                if(consCurve == null) {
                    log.info("未查找到 负荷条件数据");
                    return null;
                }
                AreaPrediction areaPrediction = new AreaPrediction();
                AreaCurveBase areaCurveBase = new AreaCurveBase();

                BeanUtils.copyProperties(consCurve,areaCurveBase);
                areaPrediction.setAreaCurveBase(areaCurveBase);

                return areaPrediction;
            }
        }
        /*
         *//**
         * 区域基线(用户基线求和)
         *//*
        AreaCurveBase areaCurveBase = consAbilityMapper.sumByArea(statDate, areaId, areaType);
        areaPrediction.setAreaCurveBase(areaCurveBase);

        *//**
         * 上调和下调(用户调节能力求和)
         *//*
        AreaAbility areaAbilityIncreate = consAbilityMapper.sumByAreaIdsAndStatDate( statDate, RegulatoryTypeEnum.Regulatory_TypeEnum_2.getCode(),areaId,areaType);
        AreaAbility areaAbilityReduce = consAbilityMapper.sumByAreaIdsAndStatDate( statDate, RegulatoryTypeEnum.Regulatory_TypeEnum_1.getCode(),areaId,areaType);

        *//**
         * 申明上调和下调 能力
         *//*
        AreaCurveUpper areaCurveUpper = new AreaCurveUpper();
        areaCurveUpper.setAreaId(areaId);
        areaCurveUpper.setStatDate(DateFormatUtil.string2LocalDate(statDateTemp));
        AreaCurveLower areaCurveLower = new AreaCurveLower();
        areaCurveLower.setAreaId(areaId);
        areaCurveLower.setStatDate(DateFormatUtil.string2LocalDate(statDateTemp));

        //遍历点坐标 设置到原来的地区上限下限 实体类中
        for (int p = 1; p <= 96; p++) {
//            int index288 = 3 * (p - 1) + 1;
            int index288 = p;
            if (areaAbilityIncreate != null && areaCurveBase != null && areaAbilityIncreate.getPByNum(p) != null && areaCurveBase.getPByNum(index288) != null) {
                areaCurveUpper.setPointByNum(index288, Float.parseFloat(String.valueOf(areaCurveBase.getPByNum(index288))) + areaAbilityIncreate.getPByNum(p));
            }
            if (areaAbilityReduce != null && areaCurveBase != null && areaAbilityReduce.getPByNum(p) != null && areaCurveBase.getPByNum(index288) != null) {
                areaCurveLower.setPointByNum(index288, Float.parseFloat(String.valueOf(areaCurveBase.getPByNum(index288))) - areaAbilityReduce.getPByNum(p));
            }
        }
        if (areaAbilityIncreate != null && areaCurveBase != null) {
            areaPrediction.setAreaCurveUpper(areaCurveUpper);
        }
        if (areaAbilityReduce != null && areaCurveBase != null) {
            areaPrediction.setAreaCurveLower(areaCurveLower);
        }*/
        return null;
    }

    /**
     * 用户潜力分析。负荷预测
     * @param consId
     * @param statDate
     * @return
     */
    @Override
    @Transactional(rollbackFor=Exception.class)
    public ConsPrediction queryConsPrediction(String consId, String statDate) {

        ConsPrediction consPrediction = new ConsPrediction();
        ConsCurveBase consCurveBase = new ConsCurveBase();

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate parse;
        if(StringUtils.isEmpty(statDate)){
            parse = LocalDate.now();
        }else {
            parse = LocalDate.parse(statDate, fmt);
        }

        List<LocalDate> localDateList = new ArrayList<>();
        for (int i =1 ;i <= 5; i++){
            LocalDate localDate = parse.plusDays(-i);
            localDateList.add(localDate);
        }

        LambdaQueryWrapper<ConsCurve> queryWrapper = new LambdaQueryWrapper<>();
        if(!StringUtils.isEmpty(consId)){
            queryWrapper.eq(ConsCurve::getConsId,consId);
        }
        queryWrapper.in(ConsCurve::getDataDate,localDateList);
        //查询
        List<ConsCurve> consCurveList = consCurveService.list(queryWrapper);

        ConsCurve consCurve1 = new ConsCurve();
        if(!CollectionUtils.isEmpty(consCurveList)){
            for (int i = 1; i <= 96; i++) {

                List<BigDecimal> bigDecimalList = new ArrayList<>();

                for (ConsCurve consCurve : consCurveList) {
                    Class<? extends ConsCurve> clazz = consCurve.getClass();
                    Field[] declaredFields = clazz.getDeclaredFields();

                    for (Field field : declaredFields) {
                        field.setAccessible(true);

                        String name = field.getName();
                        try {
                            if(name.startsWith("p") && name.trim().length() <= 3){
                                if(Integer.valueOf(name.replace("p","")).equals(i)){
                                    Object object = field.get(consCurve);

                                    if(object != null){
                                        BigDecimal p = new BigDecimal(object.toString());
                                        bigDecimalList.add(p);
                                    }
                                }
                            }
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                    if(!CollectionUtils.isEmpty(bigDecimalList)){
                        try {
                            Class<? extends ConsCurve> consCurveClass = consCurve1.getClass();

                            String methodName = "setP" + (i);
                            Method method = consCurveClass.getMethod(methodName, BigDecimal.class);

                            BigDecimal sumBigDecimal = new BigDecimal(0);
                            for (BigDecimal bigDecimal : bigDecimalList) {
                                sumBigDecimal = sumBigDecimal.add(bigDecimal);
                            }
                            BigDecimal divide = sumBigDecimal.divide(new BigDecimal(bigDecimalList.size()),4, BigDecimal.ROUND_HALF_UP);
                            method.invoke(consCurve1, new BigDecimal(divide.toString()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                bigDecimalList.clear();;
            }
        }

        if(consCurve1 != null){
            BeanUtils.copyProperties(consCurve1,consCurveBase);
            consPrediction.setConsCurveBase(consCurveBase);

            return consPrediction;
        }


//        /**
//         *  获取基线数据
//         */
//        List<ConsCurveBase> list=consAbilityMapper.loadConsCurveBase(consId, statDate);
//        ConsCurveBase consCurveBase=new ConsCurveBase();
//        if (list!=null&&list.size()>0){
//            consCurveBase=list.get(0);
//            consPrediction.setConsCurveBase(list.get(0));
//        }
//        List<ConsAbilityParam> consAbilities= consAbilityMapper.queryByConsIdAndStatDate(consId,statDate);
//        ConsAbilityParam consAbilityIncreate=new ConsAbilityParam();
//        ConsAbilityParam consAbilityReduce=new ConsAbilityParam();
//        for(ConsAbilityParam consAbility:consAbilities){
//            if (consAbility.getRegulatoryType().equals(RegulatoryTypeEnum.Regulatory_TypeEnum_2.getCode())){
//                consAbilityIncreate=consAbility;
//            }else if(consAbility.getRegulatoryType().equals(RegulatoryTypeEnum.Regulatory_TypeEnum_1.getCode())){
//                consAbilityReduce=consAbility;
//            }
//        }
//
//        /**
//         * 申明上调和下调负荷
//         */
//        ConsCurveUpper consCurveUpper=new ConsCurveUpper();
//        consCurveUpper.setConsId(consId);
//        ConsCurveLower consCurveLower=new ConsCurveLower();
//        consCurveLower.setConsId(consId);
//        consCurveLower.setStatDate(DateFormatUtil.string2LocalDate(statDate));
//        //循环遍历点坐标 赋值给原本的上下限实体类
//        for(int p=1;p<=96;p++){
////            int index288=3*(p-1)+1;
//            int index288=p;
//            if (consAbilityIncreate!=null && consCurveBase!=null && consAbilityIncreate.getPByNum(p)!=null&& consCurveBase.getPByNum(index288)!=null) {
//                consCurveUpper.setPointByNum(index288,Float.parseFloat(String.valueOf(consCurveBase.getPByNum(index288)))+consAbilityIncreate.getPByNum(p));
//            }
//            if (consAbilityReduce!=null && consCurveBase!=null  && consAbilityReduce.getPByNum(p)!=null&& consCurveBase.getPByNum(index288)!=null) {
//                consCurveLower.setPointByNum(index288,Float.parseFloat(String.valueOf(consCurveBase.getPByNum(index288)))-consAbilityReduce.getPByNum(p));
//            }
//        }
//        if (consAbilityIncreate!=null && consCurveBase!=null){
//            consPrediction.setConsCurveUpper(consCurveUpper);
//        }
//        if (consAbilityReduce!=null && consCurveBase!=null ){
//            consPrediction.setConsCurveLower(consCurveLower);
//        }

        return consPrediction;
    }

}
