package com.xqxy.dr.modular.event.enums;

import com.xqxy.core.annotion.ExpEnumType;
import com.xqxy.core.consts.SysExpEnumConstant;
import com.xqxy.core.exception.enums.abs.AbstractBaseExceptionEnum;
import com.xqxy.core.factory.ExpEnumCodeFactory;

@ExpEnumType(module = SysExpEnumConstant.EVENT_MODULE_EXP_CODE, kind = SysExpEnumConstant.EVENT_EVENT_EXCEPTION_ENUM)
public enum EventMonitorTastException implements AbstractBaseExceptionEnum {

    /**
     * 事件不存在
     */
    EVENT_MONITOR_NOT_EXIST(1, "事件监测信息不存在");

    private final Integer code;

    private final String message;

    EventMonitorTastException(Integer code, String message) {
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
