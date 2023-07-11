package com.xqxy.dr.modular.dispatch.enums;

import com.xqxy.core.annotion.ExpEnumType;
import com.xqxy.core.consts.SysExpEnumConstant;
import com.xqxy.core.exception.enums.abs.AbstractBaseExceptionEnum;
import com.xqxy.core.factory.ExpEnumCodeFactory;

@ExpEnumType(module = SysExpEnumConstant.EVENT_MODULE_EXP_CODE, kind = SysExpEnumConstant.EVENT_EVENT_EXCEPTION_ENUM)
public enum DispatchSoltException implements AbstractBaseExceptionEnum {

    /**
     * 事件不存在
     */
    DISPATCH_NOT_EXIST(1, "调度时段不存在"),
    ALREADY_GENERATE_EVENT(2, "请勿重复生成事件"),
    DISPATCH_REVOKE_TOO_LATE(3, "事件执行一小时前不允许撤销");

    private final Integer code;

    private final String message;

    DispatchSoltException(Integer code, String message) {
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
