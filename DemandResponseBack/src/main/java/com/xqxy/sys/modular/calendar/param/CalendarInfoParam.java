package com.xqxy.sys.modular.calendar.param;


import com.baomidou.mybatisplus.annotation.TableName;
import com.xqxy.core.pojo.base.param.BaseParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
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
public class CalendarInfoParam extends BaseParam {



    /**
     * 主键
     */
    @NotNull(message = "id不能为空，请检查id参数", groups = {BaseParam.delete.class, BaseParam.detail.class})
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

    /**
     * 连续时段的集合
     */
    private String[] dateArr;

    /**
     * 生成记录时间
     */
    private String dataTime;

}
