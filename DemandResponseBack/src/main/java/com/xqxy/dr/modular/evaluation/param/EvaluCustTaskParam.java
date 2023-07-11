package com.xqxy.dr.modular.evaluation.param;

import com.xqxy.core.pojo.base.param.BaseParam;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * <p>
 * 客户效果评估补贴任务标识 参数
 * </p>
 *
 * @author hu xingxing
 * @since 2021-10-20 13:50
 */
@ApiModel(description = "客户效果评估补贴任务标识")
@EqualsAndHashCode(callSuper = true)
@Data
public class EvaluCustTaskParam extends BaseParam {

    //记录标识
    private Long id;

    //事件标识
    private Long eventId;

    //调控日期
    private LocalDate regulateDate;

    //调控日期
    private String regulateDateStr;

    //响应开始时间
    private String startTime;

    //响应结束时间
    private String endTime;

    //客户标识
    private Long custId;

    //是否集成商
    private String integrator;

    //当日效果评估状态1：未计算，2：计算完成，3：异常
    private String evaluTodayStatus;

    //当日效果评估标识
    private Long evaluTodayId;

    //当日效果评估描述
    private String evaluTodayDesc;

    //次日效果评估状态1：未计算，2：计算完成，3：异常
    private String evaluNextdayStatus;

    //次日效果评估标识
    private Long evaluNextdayId;

    //次日效果评估描述
    private String evaluNextdayDesc;

    //补贴计算状态1：未计算，2：计算完成，3：异常
    private String subsidyStatus;

    //补贴计算标识
    private Long subsidyId;

    //补贴计算描述
    private String subsidyDesc;
}
