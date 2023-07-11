package com.xqxy.sys.modular.calendar.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.sys.modular.calendar.entity.CalendarInfo;
import com.xqxy.sys.modular.calendar.param.CalendarInfoParam;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 日历 服务类
 * </p>
 *
 * @author liuyu
 * @since 2021-05-08
 */
public interface CalendarInfoService extends IService<CalendarInfo> {







    /**
     * 查询日历
     *
     * @param calendarInfoParam 查询参数
     * @return 查询分页结果
     * @author xiao jun
     * @date 2021/5/13 14:53
     */
   // PageResult<CalendarInfo> page(CalendarInfoParam calendarInfoParam);

    /**
     * 查询日历
     *
     * @param calendarInfoParam 查询参数
     * @return 查询分页结果
     * @author xiao jun
     * @date 2021/5/13 14:53
     */
  //  PageResult<CalendarInfo> page(Page page, CalendarInfoParam calendarInfoParam);

    /**
     * 查询单位月的所有日程安排
     *
     * @param param
     * @return
     */
    List<CalendarInfo> page(CalendarInfoParam param);

    /**
     * 增加一条记录或者多条日程记录
     *
     * @param param
     */
    void add(CalendarInfoParam param);

    /**
     * 查询单条记录
     *
     * @param param
     * @return
     */
    List<CalendarInfo> detail(CalendarInfoParam param);

    /**
     * 修改单条记录
     * @param param
     * @return
     */
    /*  void update(CalendarInfoParam param);*/

    /**
     * 删除单条记录
     *
     * @param id
     * @return
     */
    void deleteById(Long id);

    /**
     * @param cdrDate
     * @return
     */
    CalendarInfo getByCdrDate(LocalDate cdrDate);


    Page<CalendarInfo> page(Page page, CalendarInfoParam calendarInfoParam);
}
