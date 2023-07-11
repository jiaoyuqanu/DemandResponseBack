package com.xqxy.dr.modular.evaluation.enums;

import com.xqxy.core.annotion.ExpEnumType;
import com.xqxy.core.consts.SysExpEnumConstant;
import com.xqxy.core.exception.enums.abs.AbstractBaseExceptionEnum;
import com.xqxy.core.factory.ExpEnumCodeFactory;

@ExpEnumType(module = SysExpEnumConstant.EVALUATION_MODULE_EXP_CODE, kind = SysExpEnumConstant.EVALUATION_EVALUATION_EXCEPTION_ENUM)
public enum ExecutePowerException implements AbstractBaseExceptionEnum {

    /**
     * 实时负荷数据不存在
     */

    EXECUTE_POWER_NOT_EXIST(3, "负荷数据不存在");

    private final Integer code;

    private final String message;

    ExecutePowerException(Integer code, String message) {
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
