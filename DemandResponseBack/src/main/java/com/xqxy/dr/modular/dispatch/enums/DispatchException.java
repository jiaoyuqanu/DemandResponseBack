package com.xqxy.dr.modular.dispatch.enums;

import com.xqxy.core.annotion.ExpEnumType;
import com.xqxy.core.consts.SysExpEnumConstant;
import com.xqxy.core.exception.enums.abs.AbstractBaseExceptionEnum;
import com.xqxy.core.factory.ExpEnumCodeFactory;

@ExpEnumType(module = SysExpEnumConstant.EVENT_MODULE_EXP_CODE, kind = SysExpEnumConstant.EVENT_EVENT_EXCEPTION_ENUM)
public enum DispatchException implements AbstractBaseExceptionEnum {

    /**
     * 事件不存在
     */
    DISPATCH_NOT_EXIST(1, "调度指令不存在"),
    DISPATCH_EVENT_EXIST(2, "事件已生成，不可撤销"),
    DISPATCH_EVENT_REPEAT(3, "事件不可重复生成"),
    DISPATCH_RESOVLE_SUCCESS(4, "事件生成"),
    DISPATCH_RESOVLE_FAIL(5, "无签约用户，下发失败!"),
    BLACK_RESOVLE_FAIL(7, "用户都在黑名单，下发失败!"),
    DISPATCH_OVERTIME_FAIL(8, "调度指令已超时"),
    DISPATCH_REMOTE_SUCCESS(6, "撤销成功");

    private final Integer code;

    private final String message;

    DispatchException(Integer code, String message) {
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
