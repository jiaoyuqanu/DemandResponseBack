package com.xqxy.sys.modular.calendar.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.sys.modular.calendar.entity.CalendarInfo;
import com.xqxy.sys.modular.calendar.mapper.CalendarInfoMapper;
import com.xqxy.sys.modular.calendar.param.CalendarInfoParam;
import com.xqxy.sys.modular.calendar.service.CalendarInfoService;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 日历 服务实现类
 * </p>
 *
 * @author liuyu
 * @since 2021-05-08
 */
@Service
public class CalendarInfoServiceImpl extends ServiceImpl<CalendarInfoMapper, CalendarInfo> implements CalendarInfoService {



    @Override
    public List<CalendarInfo> page(CalendarInfoParam param) {
       String dataTime=param.getDataTime();
        LambdaQueryWrapper<CalendarInfo> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(dataTime)) {
            //开始日期
            int year=Integer.parseInt(dataTime.split("-")[0]);
            int month=Integer.parseInt(dataTime.split("-")[1])-1;
            SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal=Calendar.getInstance();
            cal.set(year,month,1);
            Date time = cal.getTime();
            String start=format.format(time);
            Calendar cal2=Calendar.getInstance();
            cal2.set(year,month, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            String end=format.format(cal2.getTime());
            if (ObjectUtil.isNotEmpty(dataTime) ){
                queryWrapper.between(CalendarInfo::getCdrDate, start,end);
            }
        }
        queryWrapper.orderByAsc(CalendarInfo::getCdrDate);
        return this.list(queryWrapper);

    }
/*    public PageResult<CalendarInfo> page(CalendarInfoParam calendarInfoParam) {
        LambdaQueryWrapper<CalendarInfo> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(calendarInfoParam)) {
            //根据日期类别查询
            if (ObjectUtil.isNotEmpty(calendarInfoParam.getDateType())) {
                queryWrapper.like(CalendarInfo::getDateType, calendarInfoParam.getDateType());
            }
            //根据查询开始时间查询
            if (ObjectUtil.isNotEmpty(calendarInfoParam.getSearchBeginTime())) {
                queryWrapper.ge(CalendarInfo::getCdrDate, calendarInfoParam.getSearchBeginTime());
            }
            //根据查询结束时间查询
            if (ObjectUtil.isNotEmpty(calendarInfoParam.getSearchEndTime())) {
                queryWrapper.le(CalendarInfo::getCdrDate, calendarInfoParam.getSearchEndTime());
            }
        }
        queryWrapper.orderByAsc(CalendarInfo::getCdrDate);
        return new PageResult<>(this.page(PageFactory.defaultPage(), queryWrapper));
    }*/

 /*   @Override
    public PageResult<CalendarInfo> page(Page page, CalendarInfoParam calendarInfoParam) {
        LambdaQueryWrapper<CalendarInfo> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(calendarInfoParam)) {
            //根据日期类别查询
            if (ObjectUtil.isNotEmpty(calendarInfoParam.getDateType())) {
                queryWrapper.like(CalendarInfo::getDateType, calendarInfoParam.getDateType());
            }
            //根据查询开始时间查询
            if (ObjectUtil.isNotEmpty(calendarInfoParam.getSearchBeginDate())) {
                queryWrapper.gt(CalendarInfo::getCdrDate, calendarInfoParam.getSearchBeginDate());
            }
            //根据查询结束时间查询
            if (ObjectUtil.isNotEmpty(calendarInfoParam.getSearchEndDate())) {
                queryWrapper.lt(CalendarInfo::getCdrDate, calendarInfoParam.getSearchEndDate());
            }
        }
        queryWrapper.orderByDesc(CalendarInfo::getCdrDate);
        return new PageResult<>(this.page(new Page<>(page.getCurrent(), page.getSize()), queryWrapper));
    }*/

/*    @Override
    public List<CalendarInfo> list(CalendarInfoParam param) {
        String dataTime = param.getDataTime();
        List<CalendarInfo> list = calendarInfoDao.list(dataTime);
        return list;
    }*/

    @Override
    public void add(CalendarInfoParam param) {
        System.out.println("getDateName:" + param.getDateName());
        String[] dateArr = param.getDateArr();
        if (dateArr == null) {
            //一条记录
            int year = Integer.parseInt(param.getYear());
            int month = Integer.parseInt(param.getMonth());
            int day = Integer.parseInt(param.getDay());
            param.setCdrDate(LocalDate.of(year, month, day));
            CalendarInfo calendarInfo = new CalendarInfo();
            BeanUtil.copyProperties(param, calendarInfo);
            List<CalendarInfo> detail = this.detail(param);
            if(detail.size()==0){
                this.save(calendarInfo);
                return;
            }
            CalendarInfo calendarInfo1 = detail.get(0);
            if (calendarInfo1 == null) {
                this.save(calendarInfo);
            } else {
                calendarInfo1.setDateName(param.getDateName());
                calendarInfo1.setDateType(param.getDateType());
                calendarInfo1.setYear(param.getYear());
                calendarInfo1.setMonth(param.getMonth());
                calendarInfo1.setDay(param.getDay());
                this.updateById(calendarInfo1);
            }
            return;
        }
        if (dateArr != null && dateArr.length >= 1) {
            //多条记录新增
            CalendarInfo calendarInfo1=null;
            for (String date : dateArr) {
                CalendarInfoParam param1 = new CalendarInfoParam();
                int year = Integer.parseInt(date.split("-")[0]);
                int month = Integer.parseInt(date.split("-")[1]);
                int day = Integer.parseInt(date.split("-")[2]);
                param1.setCdrDate(LocalDate.of(year, month, day));
                param1.setYear(date.split("-")[0]);
                param1.setMonth(date.split("-")[1]);
                param1.setDay(date.split("-")[2]);
                param1.setDateType(param.getDateType());
                param1.setDateName(param.getDateName());
                calendarInfo1 = new CalendarInfo();
                BeanUtil.copyProperties(param1, calendarInfo1);
                List<CalendarInfo> detail = this.detail(param1);
                if(detail.size()==0){
                    this.save(calendarInfo1);
                    continue;
                }
                CalendarInfo cal = detail.get(0);
                if (cal == null) {
                    this.save(calendarInfo1);
                    continue;
                }
                cal.setDateName(param1.getDateName());
                cal.setDateType(param1.getDateType());
                cal.setYear(param1.getYear());
                cal.setMonth(param1.getMonth());
                cal.setDay(param1.getDay());
                this.updateById(cal);
            }
        }
    }

    @Override
    public List<CalendarInfo> detail(CalendarInfoParam param) {
        LambdaQueryWrapper<CalendarInfo> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(param)) {
            if (ObjectUtil.isNotEmpty(param.getYear())) {
                queryWrapper.eq(CalendarInfo::getYear, param.getYear());
            }
            if (ObjectUtil.isNotEmpty(param.getMonth())) {
                queryWrapper.eq(CalendarInfo::getMonth, param.getMonth());
            }
            if (ObjectUtil.isNotEmpty(param.getDay())) {
                queryWrapper.eq(CalendarInfo::getDay, param.getDay());
            }
            if (ObjectUtil.isNotEmpty(param.getCdrDate())) {
                queryWrapper.eq(CalendarInfo::getCdrDate, param.getCdrDate());
            }
            if (ObjectUtil.isNotEmpty(param.getId())) {
                queryWrapper.eq(CalendarInfo::getId, param.getId());
            }
        }
        queryWrapper.orderByAsc(CalendarInfo::getCdrDate);
        return this.list(queryWrapper);
    }



    @Override
    public void deleteById(Long id) {
        try {
            LambdaQueryWrapper<CalendarInfo> queryWrapper = new LambdaQueryWrapper<>();
            if(ObjectUtil.isNotNull(id)){
                if(ObjectUtil.isNotEmpty(id)){
                    queryWrapper.eq(CalendarInfo::getId,id);
                }
            }
            List<CalendarInfo> list = this.list(queryWrapper);
            if(list.size()!=0){
                this.removeById(id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public CalendarInfo getByCdrDate(LocalDate cdrDate) {
        LambdaQueryWrapper<CalendarInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CalendarInfo::getCdrDate, cdrDate);
        return this.getOne(queryWrapper, true);
    }



    @Override
    public Page<CalendarInfo> page(Page page, CalendarInfoParam calendarInfoParam) {
        LambdaQueryWrapper<CalendarInfo> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(calendarInfoParam)) {
            //根据日期类别查询
            if (ObjectUtil.isNotEmpty(calendarInfoParam.getDateType())) {
                queryWrapper.like(CalendarInfo::getDateType, calendarInfoParam.getDateType());
            }
            //根据查询开始时间查询
            if (ObjectUtil.isNotEmpty(calendarInfoParam.getSearchBeginDate())) {
                queryWrapper.ge(CalendarInfo::getCdrDate, calendarInfoParam.getSearchBeginDate());
            }
            //根据查询结束时间查询
            if (ObjectUtil.isNotEmpty(calendarInfoParam.getSearchEndDate())) {
                queryWrapper.le(CalendarInfo::getCdrDate, calendarInfoParam.getSearchEndDate());
            }
        }
        queryWrapper.orderByDesc(CalendarInfo::getCdrDate);
        return this.page(new Page<>(page.getCurrent(), page.getSize()), queryWrapper);
    }

}
