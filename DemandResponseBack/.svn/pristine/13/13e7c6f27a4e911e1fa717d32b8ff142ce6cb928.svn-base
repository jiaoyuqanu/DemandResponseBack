package com.xqxy.dr.modular.strategy;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xqxy.core.context.requestno.RequestNoContext;
import com.xqxy.core.exception.ServiceException;
import com.xqxy.core.log.RequestLogManager;
import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.data.entity.ConsCurve;
import com.xqxy.dr.modular.data.entity.ConsCurveToday;
import com.xqxy.dr.modular.data.entity.ConsEnergyCurve;
import com.xqxy.dr.modular.data.entity.PageArchives;
import com.xqxy.dr.modular.data.enums.ConsCurveExceptionEnum;
import com.xqxy.dr.modular.data.result.ProfileObject;
import com.xqxy.dr.modular.data.result.ProfileResult;
import com.xqxy.dr.modular.data.result.centerResult.DataAccessHistoryCurve;
import com.xqxy.dr.modular.data.result.centerResult.DataAccessHistoryData;
import com.xqxy.dr.modular.data.result.centerResult.DataAccessHistoryResult;
import com.xqxy.dr.modular.data.service.ConsCurveService;
import com.xqxy.dr.modular.data.service.ConsCurveTodayService;
import com.xqxy.dr.modular.data.service.ConsEnergyCurveService;
import com.xqxy.dr.modular.device.VO.EquipmentRecordVO;
import com.xqxy.sys.modular.cust.entity.Cons;
import com.xqxy.sys.modular.cust.enums.ConsExceptionEnum;
import com.xqxy.sys.modular.cust.param.ConsParam;
import com.xqxy.sys.modular.cust.service.ConsService;
import com.xqxy.sys.modular.log.entity.DcRequestLog;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @Description
 * @ClassName LocalDataAccessStrategy
 * @Author User
 * @date 2021.04.13 17:49
 */
@Component
public class LocalDataAccessStrategy implements DataAccessStrategy {

    private static final Log log = Log.get();

    @Resource
    ConsService consService;

    @Resource
    ConsCurveService consCurveService;

    @Resource
    ConsCurveTodayService consCurveTodayService;

    @Resource
    ConsEnergyCurveService consEnergyCurveService;


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
        LambdaQueryWrapper<ConsCurve> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ConsCurve::getConsId,elecConsNo);
        queryWrapper.ge(ConsCurve::getDataDate,startDate);
        queryWrapper.le(ConsCurve::getDataDate,endDate);
        List<ConsCurve> list = consCurveService.list(queryWrapper);
        return list;
    }

    @Override
    public ConsCurve queryTodayLoadCurveByConsNo(String elecConsNo, String dataDate) {
        if (StringUtils.isEmpty(elecConsNo) || StringUtils.isEmpty(dataDate)) {
            ConsCurve consCurve = new ConsCurve();
            consCurve.setConsId(elecConsNo);
            return consCurve;
        }

        Cons cons =  consService.getById(elecConsNo);
        if(ObjectUtil.isNull(cons)) throw new ServiceException(ConsExceptionEnum.CONS_NOT_EXIST);
        /*if (consList == null || consList.size() < 1) {
            throw new ServiceException(ConsExceptionEnum.CONS_NOT_EXIST);
        } else if (consList.size() > 1) {
            throw new ServiceException(ConsExceptionEnum.CONS_REPEAT);
        }*/
        // Cons cons = consList.get(0);
        ConsCurve consCurve = new ConsCurve();
        log.info(">>> 本地实时接口，请求号为：{}，请求户号：{}，请求日期：{}", RequestNoContext.get(), elecConsNo, dataDate);
        ConsCurveToday consCurveToday = consCurveTodayService.getCurveByConsIdAndDate(cons.getId(), dataDate);
        consCurve.setConsId(cons.getId());
        if (ObjectUtil.isNotNull(consCurveToday)) {
            BeanUtils.copyProperties(consCurveToday, consCurve);
        } else {
            return consCurve;
        }

        log.info(">>> 本地实时接口，请求号为：{}，结果：{}", RequestNoContext.get(), consCurve);
        return consCurve;
    }

    @Override
    public List<ConsCurve> queryTodayCurveList(List<String> elecConsNoList, String dataDate) {
        //实时历史本地库数据相同
        if (CollectionUtil.isEmpty(elecConsNoList) || ObjectUtil.isEmpty(dataDate)) {
            return null;
        }
        LambdaQueryWrapper<ConsCurveToday> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ConsCurveToday::getConsId,elecConsNoList);
        queryWrapper.eq(ConsCurveToday::getDataDate,LocalDate.parse(dataDate));
        List<ConsCurveToday> list = consCurveTodayService.list(queryWrapper);
        log.info(">>> 本地实时日期参数：{},本地实时结果：{}",dataDate,list);
        List<ConsCurve>  consCurves = new ArrayList<>();
        if(null!=list && list.size()>0) {
            for(ConsCurveToday consCurveToday : list) {
                ConsCurve consCurve = new ConsCurve();
                BeanUtils.copyProperties(consCurveToday,consCurve);
                if(null!=consCurveToday.getConsId()) {
                    consCurve.setConsId(consCurveToday.getConsId().toString());
                }
                consCurves.add(consCurve);
            }
        }
        log.info(">>> 本地拷贝实时多日期结果：{}",consCurves);
        return consCurves;
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

    @Override
    public List<ConsCurve> queryHistoryCurvePage(List<String> elecConsNoList, String dataDate) {
        if (CollectionUtil.isEmpty(elecConsNoList) || ObjectUtil.isEmpty(dataDate)) {
            return null;
        }
        LambdaQueryWrapper<ConsCurve> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ConsCurve::getConsId,elecConsNoList);
        queryWrapper.eq(ConsCurve::getDataDate,LocalDate.parse(dataDate));
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
}
