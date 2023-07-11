package com.xqxy.dr.modular.event.enums;

import com.xqxy.core.annotion.ExpEnumType;
import com.xqxy.core.consts.SysExpEnumConstant;
import com.xqxy.core.exception.enums.abs.AbstractBaseExceptionEnum;
import com.xqxy.core.factory.ExpEnumCodeFactory;

@ExpEnumType(module = SysExpEnumConstant.EVENT_MODULE_EXP_CODE, kind = SysExpEnumConstant.EVENT_EVENT_INVITATION_EXCEPTION_ENUM)
public enum BaselineLibraryExceptionEnum  implements AbstractBaseExceptionEnum {

    BASELIBRARY_NOT_EXIST(1,"无对应基线库");

    private final Integer code;

    private final String message;

    BaselineLibraryExceptionEnum(Integer code, String message) {
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
