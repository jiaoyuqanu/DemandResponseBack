
package com.xqxy.dr.modular.event.enums;

import com.xqxy.core.annotion.ExpEnumType;
import com.xqxy.core.consts.SysExpEnumConstant;
import com.xqxy.core.exception.enums.abs.AbstractBaseExceptionEnum;
import com.xqxy.core.factory.ExpEnumCodeFactory;

/**
 * 事件相关异常枚举
 *
 * @author xuyuxiang
 * @date 2020/3/26 10:12
 */
@ExpEnumType(module = SysExpEnumConstant.EVENT_MODULE_EXP_CODE, kind = SysExpEnumConstant.EVENT_EVENT_EXCEPTION_ENUM)
public enum EventExceptionEnum implements AbstractBaseExceptionEnum {

    /**
     * 事件不存在
     */
    EVENT_NOT_EXIST(1, "事件不存在"),
    EVENT_PLAN_NOT_EXIST(2, "事件方案不存在"),
    BASELINE_NOT_CAL(3, "基线正在计算中，请稍后再试！"),
    EVENT_START(4, "邀约截止时间不能晚于事件执行开始时间！"),
    DEADLINE_TIME(6, "事件发布时间未截止！"),
    NO_USERS(7, "无邀约用户！"),
    REVOKE_EVENT_FAIL(8, "事件撤销失败！"),
    REVOKE_EVENT_STATUS_ERROR(9, "事件状态必须为已发布,事件撤销失败！"),
    NO_TIME(5, "邀约截止时间和邀约响应量为空！"),
    CANNOT_RELEASE(11, "待执行和已发布状态可发布事件！"),
    EVENT_SAVE_STATE(12, "事件状态为保存才可生成方案!"),
    EVENT_BEGIN(13, "事件已开始!"),
    EVENT_EXECUTE_STATE(14, "事件状态为待执行可剔除或恢复!"),
    EVENT_RELEASE_STATE(15, "事件状态为已发布可操作!"),
    EVENT_REVOKE_STATE(16, "事件状态为撤销可操作!"),
    EVENT_EXECUTE_COM_STATE(17, "事件状态为执行中可操作!"),
    DEADLINE_TIME_NO(18, "邀约时间已截止！"),
    CANNOT_OPERATOR(19, "数据正在推送，不可操作按钮！"),
    CANNOT_OPERATOR_COM(19, "数据已推送，不可操作按钮！"),
    EVENT_CONS_NOT_EXIST(10, "事件方案不存在");

    private final Integer code;

    private final String message;

    EventExceptionEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return ExpEnumCodeFactory.getExpEnumCode(this.getClass(), code);
    }

    @Override
    public String getMessage() {
        return message;
    }

}
