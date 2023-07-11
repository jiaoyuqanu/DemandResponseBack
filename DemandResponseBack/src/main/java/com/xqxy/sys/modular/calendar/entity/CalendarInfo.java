package com.xqxy.sys.modular.calendar.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * <p>
 * 日历
 * </p>
 *
 * @author liuyu
 * @since 2021-05-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dr_sys_calendar_info")
public class CalendarInfo implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 日历日期
     */
    private LocalDate cdrDate;

    /**
     * 日期名称(标注工作日，周末，节假日名称：工作日，周末，春节......)
     */
    private String dateName;

    /**
     * 日期类别(字典：1 工作日 2 周末 3 节假日)
     */
    private String dateType;

    /**
     * 年
     */
    private String year;

    /**
     * 月
     */
    private String month;

    /**
     * 日
     */
    private String day;

    /**
     * 星期
     */
    private String week;


}
