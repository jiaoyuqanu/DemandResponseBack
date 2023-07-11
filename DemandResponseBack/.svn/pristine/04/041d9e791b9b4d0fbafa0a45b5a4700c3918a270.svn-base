package com.xqxy.sys.modular.sms.enums;

import com.xqxy.core.annotion.ExpEnumType;
import com.xqxy.core.consts.SysExpEnumConstant;
import com.xqxy.core.exception.enums.abs.AbstractBaseExceptionEnum;
import com.xqxy.core.factory.ExpEnumCodeFactory;

@ExpEnumType(module = SysExpEnumConstant.FILE_MODULE_EXP_CODE, kind = SysExpEnumConstant.FILE_FILE_INFO_EXCEPTION_ENUM)
public enum SmsTemplateException implements AbstractBaseExceptionEnum {

    TEMPLATE_NOT_EXIST(1,"短信模板不存在");

    private final Integer code;

    private final String message;

    SmsTemplateException(int code, String message) {
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
