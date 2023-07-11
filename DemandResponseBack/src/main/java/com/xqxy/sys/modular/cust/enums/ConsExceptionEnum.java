package com.xqxy.sys.modular.cust.enums;

import com.xqxy.core.annotion.ExpEnumType;
import com.xqxy.core.consts.SysExpEnumConstant;
import com.xqxy.core.exception.enums.abs.AbstractBaseExceptionEnum;
import com.xqxy.core.factory.ExpEnumCodeFactory;


@ExpEnumType(module = SysExpEnumConstant.EVENT_MODULE_EXP_CODE, kind = SysExpEnumConstant.EVENT_EVENT_INVITATION_EXCEPTION_ENUM)
public enum ConsExceptionEnum  implements AbstractBaseExceptionEnum {

    /**
     * 用户档案不存在
     */
    CUST_NOT_EXIST(2, "客户信息不存在"),
    CONS_NOT_EXIST(1, "用户档案不存在"),
    PROXY_USER_NOT_EXIST(3, "无代理用户"),
    CONS_REPEAT(4,"用户重复"),
    ORG_NOT_EXIST(5,"供电单位不存在");

    private final Integer code;

    private final String message;

    ConsExceptionEnum(Integer code, String message) {
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
