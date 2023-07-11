package com.xqxy.dr.modular.evaluation.param;

import com.xqxy.core.pojo.base.param.BaseParam;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * <p>
 * 效果评估补贴任务标识 参数
 * </p>
 *
 * @author hu xingxing
 * @since 2021-10-19 15:00
 */
@ApiModel(description = "效果评估补贴任务标识")
@EqualsAndHashCode(callSuper = true)
@Data
public class EvaluTaskParam extends BaseParam {

    //主键标识
    private long id;

    //事件标识
    private long eventId;

    //用户标识
    private String consId;

    //调控日期
    private LocalDate regulateDate;

    private String regulateDateStr;

    //响应开始时间
    private String startTime;

    //响应结束时间
    private String endTime;

    //当日效果评估状态
    private String evaluTodayStatus;

    //当日效果评估标识
    private long evaluTodayId;

    //当日效果评估描述
    private String evaluTodatDesc;

    //次日效果评估状态
    private String evaluNextDayStatus;

    //次日效果评估标识
    private long evaluNextDayId;

    //次日效果评估描述
    private String evaluNextDayDesc;

    //补贴计算状态
    private String subsidyStatus;

    //补贴计算标识
    private long subsidyId;

    //补贴计算描述
    private String subsidyDesc;
}
