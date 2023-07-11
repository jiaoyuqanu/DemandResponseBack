package com.xqxy.dr.modular.event.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.core.enums.YesOrNotEnum;
import com.xqxy.core.exception.ServiceException;
import com.xqxy.core.poi.POIExcelUtil;
import com.xqxy.core.util.HttpServletUtil;
import com.xqxy.dr.modular.baseline.service.BaseLineService;
import com.xqxy.dr.modular.baseline.util.CurveUtil;
import com.xqxy.dr.modular.event.entity.*;
import com.xqxy.dr.modular.event.enums.EventInvitationExceptionEnum;
import com.xqxy.dr.modular.event.mapper.EventPowerSampleMapper;
import com.xqxy.dr.modular.event.service.*;
import com.xqxy.sys.modular.cust.entity.Cons;
import com.xqxy.sys.modular.cust.service.ConsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 样本负荷曲线 服务实现类
 * </p>
 *
 * @author xiao jun
 * @since 2021-05-13
 */
@Service
public class EventPowerSampleServiceImpl extends ServiceImpl<EventPowerSampleMapper, EventPowerSample> implements EventPowerSampleService {

    @Resource
    EventService eventService;

    @Resource
    ConsService consService;

    @Resource
    PlanService planService;

    @Resource
    ConsBaselineService consBaselineService;

    @Override
    public void delete(EventPowerSample eventPowerSampleParam) {
        LambdaQueryWrapper<EventPowerSample> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(eventPowerSampleParam)) {
            // 根据事件ID查询
            if (ObjectUtil.isNotEmpty(eventPowerSampleParam.getEventId())) {
                queryWrapper.eq(EventPowerSample::getEventId, eventPowerSampleParam.getEventId());
            }

            // 根据用户ID查询
            if (ObjectUtil.isNotEmpty(eventPowerSampleParam.getConsId())) {
                queryWrapper.eq(EventPowerSample::getConsId, eventPowerSampleParam.getConsId());
            }
        }
        this.remove(queryWrapper);

    }

    @Override
    public List<EventPowerSample> list(Long eventId, String consId) {
        // 通过事件id查询方案信息，拿到方案信息中的基线库标识
        Plan planInfo = planService.getByEventId(eventId);
        // 通过基线库标识和用户id获取用户基线信息，拿到基线信息中的样本负荷标识
        ConsBaseline consBaseline = consBaselineService.getConsBaseByLibId(consId, planInfo.getBaselinId());
        if(ObjectUtil.isNull(consBaseline)) {
            throw new ServiceException(EventInvitationExceptionEnum.BASELINE_NOT_EXIST);
        }
        String simplesId = consBaseline.getSimplesId();
        if(ObjectUtil.isNull(simplesId)) {
            throw new ServiceException(EventInvitationExceptionEnum.EMPTY_POWER_RECORDS);
        }
        // 查询该用户样本负荷标识记录并返回
        String[] simpleIds = simplesId.split(",");
        LambdaQueryWrapper<EventPowerSample> queryWrapper = new LambdaQueryWrapper<>();
        //queryWrapper.eq(EventPowerSample::getConsId, consId);
        queryWrapper.in(EventPowerSample::getId, simpleIds);
        //根据排序升序排列，序号越小越在前
        queryWrapper.orderByAsc(EventPowerSample::getDataDate);
        return this.list(queryWrapper);
    }

    @Override
    public void export(Long eventId, String consId) {
        Event event = eventService.getById(eventId);
        Cons cons = consService.getById(consId);
        String excelName = StrUtil.format("{}-{}", event.getEventName(), cons.getConsName());
        // 样本负荷
        List<EventPowerSample> eventPowerSampleList = this.list(eventId, consId);

        String sheetName = "样本负荷数据";
        String titleName = StrUtil.format("{}-{}-样本负荷数据", event.getEventName(), cons.getConsName());

        List<String> titleList = new ArrayList<>();
        titleList.add("用户名称");
        titleList.add("数据日期");
        titleList.add("是否有效");
        for (int i = 1; i <= 96; i++) {
            titleList.add(CurveUtil.covPointToDateTime(i));
        }
        String[] titleRow = titleList.toArray(new String[]{});
        String[][] dataRows = new String[eventPowerSampleList.size()][titleRow.length];

        for (int i = 0; i < eventPowerSampleList.size(); i++) {
            EventPowerSample eventPowerSample = eventPowerSampleList.get(i);

            dataRows[i][0] = cons.getConsName();
            dataRows[i][1] = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(eventPowerSample.getDataDate());
            dataRows[i][2] = eventPowerSample.getIsValid().equals(YesOrNotEnum.Y.getCode()) ? "是" : "否";
            for (int j = 1; j <= 96; j++) {
                dataRows[i][j + 2] = String.valueOf(ReflectUtil.getFieldValue(eventPowerSample, "p" + j));
            }
        }

         POIExcelUtil.generatorExcel(excelName, titleName, sheetName, titleRow, dataRows, HttpServletUtil.getResponse());
        // POIExcelUtil.generatorExcel(excelName, sheetList, HttpServletUtil.getResponse());
    }

    @Override
    public List<EventPowerSample> getEventSimpInfo() {
        return baseMapper.getEventSimpInfo();
    }
}
