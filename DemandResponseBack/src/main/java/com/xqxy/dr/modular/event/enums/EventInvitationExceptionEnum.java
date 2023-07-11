package com.xqxy.dr.modular.event.enums;

import com.xqxy.core.exception.enums.abs.AbstractBaseExceptionEnum;
import com.xqxy.core.factory.ExpEnumCodeFactory;
import lombok.Getter;

@Getter
public enum EventInvitationExceptionEnum implements AbstractBaseExceptionEnum {
    /**
     * 用户邀约不存在
     */
    EVENT_NOT_EXIST(1, "用户邀约不存在"),
    BASELINE_NOT_EXIST(2,"无用户基线记录"),
    EMPTY_POWER_RECORDS(3, "基线样本记录不存在");

    private final Integer code;

    private final String message;

    EventInvitationExceptionEnum(Integer code, String message) {
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
