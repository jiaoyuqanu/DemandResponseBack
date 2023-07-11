package com.xqxy.dr.modular.evaluation.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.core.exception.ServiceException;
import com.xqxy.core.poi.POIExcelUtil;
import com.xqxy.core.util.HttpServletUtil;
import com.xqxy.dr.modular.baseline.util.CurveUtil;
import com.xqxy.dr.modular.evaluation.entity.EventPowerExecute;
import com.xqxy.dr.modular.evaluation.enums.ExecutePowerException;
import com.xqxy.dr.modular.evaluation.mapper.EventPowerExecuteMapper;
import com.xqxy.dr.modular.evaluation.param.EventPowerExecuteParam;
import com.xqxy.dr.modular.evaluation.service.EventPowerExecuteService;
import com.xqxy.dr.modular.event.entity.Event;
import com.xqxy.dr.modular.event.service.EventService;
import com.xqxy.sys.modular.cust.entity.Cons;
import com.xqxy.sys.modular.cust.service.ConsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 用户执行曲线 服务实现类
 * </p>
 *
 * @author xiao jun
 * @since 2021-07-04
 */
@Service
public class EventPowerExecuteServiceImpl extends ServiceImpl<EventPowerExecuteMapper, EventPowerExecute> implements EventPowerExecuteService {

    @Resource
    private ConsService consService;

    @Resource
    private EventService eventService;

    @Override
    public EventPowerExecute detail(EventPowerExecuteParam executeParam) {
        LambdaQueryWrapper<EventPowerExecute> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(executeParam)) {
            // 根据用户ID查询
            if (ObjectUtil.isNotEmpty(executeParam.getConsId())) {
                queryWrapper.eq(EventPowerExecute::getConsId, executeParam.getConsId());
            }
            // 根据事件ID查询
            if (ObjectUtil.isNotEmpty(executeParam.getEventId())) {
                queryWrapper.eq(EventPowerExecute::getEventId, executeParam.getEventId());
            }
            // 根据预测日期查询
            if (ObjectUtil.isNotEmpty(executeParam.getDataDate())) {
                queryWrapper.eq(EventPowerExecute::getDataDate, executeParam.getDataDate());
            }
        }
        return this.getOne(queryWrapper);
    }

    @Override
    public List<EventPowerExecute> list(EventPowerExecuteParam executeParam) {

        LambdaQueryWrapper<EventPowerExecute> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(executeParam)) {
            // 根据用户ID查询
            if (ObjectUtil.isNotEmpty(executeParam.getConsId())) {
                queryWrapper.eq(EventPowerExecute::getConsId, executeParam.getConsId());
            }
            // 根据事件ID查询
            if (ObjectUtil.isNotEmpty(executeParam.getEventId())) {
                queryWrapper.eq(EventPowerExecute::getEventId, executeParam.getEventId());
            }
            // 根据预测日期查询
            if (ObjectUtil.isNotEmpty(executeParam.getDataDate())) {
                queryWrapper.eq(EventPowerExecute::getDataDate, executeParam.getDataDate());
            }
        }
        return this.list(queryWrapper);
    }

    @Override
    public void export(EventPowerExecuteParam executeParam) {
        Cons cons = consService.getById(executeParam.getConsId());
        Event event = eventService.getById(executeParam.getEventId());
        EventPowerExecute detail = this.detail(executeParam);
        /*if(ObjectUtil.isNull(detail)) {
            throw  new ServiceException(ExecutePowerException.EXECUTE_POWER_NOT_EXIST);
        }*/
        String excelName = StrUtil.format("{}-{}--负荷数据", cons.getConsName(), event.getRegulateDate());
        String titleName = StrUtil.format("{}-{}--负荷数据", cons.getConsName(), event.getRegulateDate());;
        String sheetName = "负荷数据";
        List<String> titleLists = new ArrayList<>();
        titleLists.add("数据日期");
        for (int i = 1; i <= 96; i++) {
            titleLists.add(CurveUtil.covPointToDateTime(i));
        }
        String[] titleRows = titleLists.toArray(new String[]{});
        String[][] dataRows = new String[1][97];
        for (int i = 0 ; i < 97; i++) {
            if(i == 0) {
                if(null!=detail && null!=detail.getDataDate()) {
                    dataRows[0][i] = detail.getDataDate().toString();
                }
                continue;
            }
            BigDecimal fieldValue = null;
            if(null!=detail) {
                fieldValue = (BigDecimal) ReflectUtil.getFieldValue(detail, "p" + i);
            }
            if(ObjectUtil.isNull(fieldValue)) {
                dataRows[0][i] = "";
            } else {
                dataRows[0][i] = fieldValue.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
            }
        }

        POIExcelUtil.generatorExcel(excelName, titleName, sheetName, titleRows, dataRows,  HttpServletUtil.getResponse());
    }
}
