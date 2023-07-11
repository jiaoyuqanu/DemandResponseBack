package com.xqxy.sys.modular.calendar.controller;


import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.sys.modular.calendar.entity.CalendarInfo;
import com.xqxy.sys.modular.calendar.param.CalendarInfoParam;
import com.xqxy.sys.modular.calendar.service.CalendarInfoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 日历 前端控制器
 * </p>
 *
 * @author liuyu
 * @since 2021-05-08
 */
@RestController
@RequestMapping("/calendar/calendar-info")
public class CalendarInfoController {

    @Resource
    private CalendarInfoService calendarInfoService;

    /**
     * 添加日程重复添加等于update覆盖
     *
     * @author liuyu
     * @date 2021/5/8
     */
    @ApiOperation(value = "添加日程", notes = "添加日程", produces = "application/json")
    @PostMapping("/add")
    public ResponseData add(@RequestBody @Validated(CalendarInfoParam.add.class) CalendarInfoParam param) {
        calendarInfoService.add(param);
        return ResponseData.success();
    }

    /**
     * 查询当月日程
     *
     * @author liuyu
     * @date 2021/5/8
     */
    @ApiOperation(value = "查询当月日程", notes = "查询当月日程", produces = "application/json")
    @PostMapping("/list")
    public ResponseData list(@RequestBody @Validated(CalendarInfoParam.add.class) CalendarInfoParam param) {
        List<CalendarInfo> list = calendarInfoService.page(param);
        return ResponseData.success(list);
    }


    /**
     * 根据日程的year，month，day 查询单条记录
     *
     * @author liuyu
     * @date 2021/5/8
     */
    @ApiOperation(value = "查询单条记录", notes = "查询单条记录", produces = "application/json")
    @PostMapping("/detail")
    public ResponseData detail(@RequestBody @Validated(CalendarInfoParam.add.class) CalendarInfoParam param) {
        List<CalendarInfo> list = calendarInfoService.detail(param);
        return ResponseData.success(list);
    }

    /**
     *
     *根据id 修改单条记录
     * @author liuyu
     * @date 2021/5/8
     */
  /*  @ApiOperation(value = "查询单条记录", notes = "查询单条记录", produces = "application/json")
    @PostMapping("/update")
    public ResponseData update(@RequestBody @Validated(CalendarInfoParam.add.class) CalendarInfoParam param) {
        calendarInfoService.update(param);
        return new SuccessResponseData();
    }*/

    /**
     * 根据id 删除单条记录
     *
     * @author liuyu
     * @date 2021/5/8
     */
    @ApiOperation(value = "删除单条记录", notes = "删除单条记录", produces = "application/json")
    @PostMapping("/delete")
    public ResponseData delete(@RequestParam("id") Long id) {
        calendarInfoService.deleteById(id);
        return ResponseData.success();
    }

    /**
     * 根据日历日期查询单条记录
     *
     * @author xiao jun
     * @date 2021/5/8
     */
    @ApiOperation(value = "查询单条记录", notes = "查询单条记录", produces = "application/json")
    @PostMapping("/getByCdrDate")
    public ResponseData getByCdrDate(@RequestParam("id") LocalDate cdrDate) {
        CalendarInfo calendarInfo = calendarInfoService.getByCdrDate(cdrDate);
        return ResponseData.success(calendarInfo);
    }
}

