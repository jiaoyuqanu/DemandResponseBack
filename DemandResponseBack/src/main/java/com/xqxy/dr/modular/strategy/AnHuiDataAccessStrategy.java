package com.xqxy.dr.modular.strategy;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.log.Log;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xqxy.core.context.requestno.RequestNoContext;
import com.xqxy.core.exception.ServiceException;
import com.xqxy.dr.modular.data.entity.ConsCurve;
import com.xqxy.dr.modular.data.entity.ConsCurveToday;
import com.xqxy.dr.modular.data.entity.ConsEnergyCurve;
import com.xqxy.dr.modular.data.service.ConsCurveService;
import com.xqxy.dr.modular.data.service.ConsCurveTodayService;
import com.xqxy.dr.modular.data.service.ConsEnergyCurveService;
import com.xqxy.dr.modular.device.VO.EquipmentRecordVO;
import com.xqxy.dr.modular.anhui.service.HefeiCurverSgDataService;
import com.xqxy.sys.modular.cust.entity.Cons;
import com.xqxy.sys.modular.cust.enums.ConsExceptionEnum;
import com.xqxy.sys.modular.cust.service.ConsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

/**
 * @Description
 * @ClassName LocalDataAccessStrategy
 * @Author User
 * @date 2021.04.13 17:49
 */
@Component
public class AnHuiDataAccessStrategy implements DataAccessStrategy {

    private static final Log log = Log.get();

    @Resource
    ConsService consService;

    @Resource
    ConsCurveService consCurveService;

    @Resource
    ConsCurveTodayService consCurveTodayService;

    @Resource
    ConsEnergyCurveService consEnergyCurveService;

    /**
     * 合肥实时数据业务类对象注入
     */
    @Resource
    private HefeiCurverSgDataService hefeiCurverSgDataService;

    class RequestField {
        String fieldName;
        String fieldValue;
        String operator = "=";

        RequestField(String fieldName, String fieldValue) {
            this.fieldName = fieldName;
            this.fieldValue = fieldValue;
        }

        RequestField(String fieldName, String fieldValue, String operator) {
            this.fieldName = fieldName;
            this.fieldValue = fieldValue;
            this.operator = operator;
        }

        public void setOperator(String operator) {
            this.operator = operator;
        }

        public String getFieldName() {
            return fieldName;
        }

        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

        public String getFieldValue() {
            return fieldValue;
        }

        public void setFieldValue(String fieldValue) {
            this.fieldValue = fieldValue;
        }

        public String getOperator() {
            return operator;
        }

        @Override
        public String toString() {
            return "RequestField{" +
                    "fieldName='" + fieldName + '\'' +
                    ", fieldValue='" + fieldValue + '\'' +
                    ", operator='" + operator + '\'' +
                    '}';
        }
    }
    @Override
    public ConsCurve queryDayLoadCurveByConsNo(String elecConsNo, String dataDate) {
        if (StringUtils.isEmpty(elecConsNo) || StringUtils.isEmpty(dataDate)) {
            ConsCurve consCurve = new ConsCurve();
            consCurve.setConsId(elecConsNo);
            return consCurve;
        }
        Cons cons =  consService.getById(elecConsNo);
        if (ObjectUtil.isNull(cons)) {
            throw new ServiceException(ConsExceptionEnum.CONS_NOT_EXIST);
//        } else if (consList.size() > 1) {
//            throw new ServiceException(ConsExceptionEnum.CONS_REPEAT);
        }
        // Cons cons = consList.get(0);
        log.info(">>> 本地历史接口，请求号为：{}，请求户号：{}，请求日期：{}", RequestNoContext.get(), elecConsNo, dataDate);
        ConsCurve consCurve = consCurveService.getCurveByConsIdAndDate(cons.getId(), dataDate);
        if (ObjectUtil.isNull(consCurve)) {
            consCurve = new ConsCurve();
        }
        consCurve.setConsId(cons.getId());
        log.info(">>> 本地历史接口，请求号为：{}，结果：{}", RequestNoContext.get(), consCurve);
        return consCurve;
    }

    @Override
    public List<ConsCurve> queryHistoryCurveList(String elecConsNo, String startDate, String endDate) {
        return null;
    }

    @Override
    public ConsCurve queryTodayLoadCurveByConsNo(String elecConsNo, String dataDate) {
        if (StringUtils.isEmpty(elecConsNo) || StringUtils.isEmpty(dataDate)) {
            ConsCurve consCurve = new ConsCurve();
            consCurve.setConsId(elecConsNo);
            return consCurve;
        }

        //安徽的实时库调用
        Map<String, BigDecimal> stringBigDecimalMap = hefeiCurverSgDataService.queryDataByConsDate(elecConsNo, dataDate);
        ConsCurve resCon=new ConsCurve();
        Class<? extends ConsCurve> clazz=null;
        try{
            //Map<p,负荷>集合转换成对象属性
            clazz = resCon.getClass();
            for(int i=0;i<96;i++) {
                String p="p"+(i+1);
                BigDecimal bigDecimal = stringBigDecimalMap.get(p);
                String methodName = "setP" + (i+1);
                Method method = clazz.getMethod(methodName, BigDecimal.class);
                method.invoke(resCon, bigDecimal );
            }
            resCon.setConsNo(elecConsNo);
            resCon.setConsId(elecConsNo);
            resCon.setDataType("1");
            resCon.setSourceId("1");
            resCon.setDataPointFlag("1");
        }catch (Exception e){
            e.printStackTrace();
        }
        return resCon;

    }

    @Override
    public List<ConsCurve> queryTodayCurveList(List<String> elecConsNoList, String dataDate) {
        List<ConsCurve> res=new ArrayList<>();
        //安徽的实时库调用
        if(elecConsNoList!=null && elecConsNoList.size()>0){
            for(int k=0;k<elecConsNoList.size();k++){
                String elecConsNo = elecConsNoList.get(k);

                Map<String, BigDecimal> stringBigDecimalMap = hefeiCurverSgDataService.queryDataByConsDate(elecConsNo, dataDate);
                System.out.println("stringBigDecimalMap"+stringBigDecimalMap.toString());
                ConsCurve resCon=new ConsCurve();
                Class<? extends ConsCurve> clazz=null;
                try{
                    //Map<p,负荷>集合转换成对象属性
                    clazz = resCon.getClass();
                    for(int i=0;i<96;i++) {
                        String p="p"+(i+1);
                        BigDecimal bigDecimal = stringBigDecimalMap.get(p);
                        String methodName = "setP" + (i+1);
                        Method method = clazz.getMethod(methodName, BigDecimal.class);
                        method.invoke(resCon, bigDecimal );
                    }
                    resCon.setConsNo(elecConsNo);
                    resCon.setConsId(elecConsNo);
                    resCon.setDataType("1");
                    resCon.setDataPointFlag("1");
                    resCon.setSourceId("1");
                    res.add(resCon);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }
        return res;
    }

    @Override
    public List<ConsCurve> queryHistoryCurveList(List<String> elecConsNolist, List<String> dataDateList) {
        if (CollectionUtil.isEmpty(dataDateList) || CollectionUtil.isEmpty(elecConsNolist)) {
            return null;
        }
        LambdaQueryWrapper<ConsCurve> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ConsCurve::getConsId,elecConsNolist);
        queryWrapper.in(ConsCurve::getDataDate,dataDateList);
        List<ConsCurve> list = consCurveService.list(queryWrapper);
        log.info(">>> 本地历史多日期户号参数：{},本地历史多日期日期参数：{},本地历史多日期结果：{}",elecConsNolist,dataDateList,list);
        return list;
    }

    //历史负荷，资源监测
    @Override
    public List<ConsCurve> queryHistoryCurvePage(List<String> elecConsNoList, String dataDate) {
        //如果用户id列表或查询日期为空，则返回空曲线列表
        if (ObjectUtil.isEmpty(elecConsNoList) || StringUtils.isEmpty(dataDate)) {
            return null;
        }
        //根据用户id列表和日期查询 该日所有用户当日曲线列表
        LambdaQueryWrapper<ConsCurve> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ConsCurve::getDataDate, LocalDate.parse(dataDate));
        queryWrapper.in(ConsCurve::getConsId,elecConsNoList);
        List<ConsCurve> list = consCurveService.list(queryWrapper);
        log.info(">>> 本地历史多日期户号参数：{},本地历史多日期日期参数：{},本地历史多日期结果：{}",elecConsNoList,dataDate,list);
        return list;
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
        List<Cons> consList = (List<Cons>) consService.getById(elecConsNo);
        if (consList == null || consList.size() < 1) {
            throw new ServiceException(ConsExceptionEnum.CONS_NOT_EXIST);
        } else if (consList.size() > 1) {
            throw new ServiceException(ConsExceptionEnum.CONS_REPEAT);
        }
        Cons cons = consList.get(0);
        ConsEnergyCurve energyCurve = consEnergyCurveService.getCurveByConsIdAndDate(cons.getId(), dataDate);
        energyCurve.setConsNo(cons.getId());
        return energyCurve;
//        ConsCurve consCurve = consCurveService.getCurveByConsIdAndDate(cons.getConsId(), dataDate);
//        consCurve.setConsNo(cons.getElecConsNo());
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

//    @Override
//    public Page<Cons> page(ConsParam consParam) {
//        return null;
//    }
}
