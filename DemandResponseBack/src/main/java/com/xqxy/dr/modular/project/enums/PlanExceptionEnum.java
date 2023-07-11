package com.xqxy.dr.modular.project.enums;

import com.xqxy.core.annotion.ExpEnumType;
import com.xqxy.core.consts.SysExpEnumConstant;
import com.xqxy.core.exception.enums.abs.AbstractBaseExceptionEnum;
import com.xqxy.core.factory.ExpEnumCodeFactory;

@ExpEnumType(module = SysExpEnumConstant.EVENT_MODULE_EXP_CODE, kind = SysExpEnumConstant.EVENT_EVENT_INVITATION_EXCEPTION_ENUM)
public enum PlanExceptionEnum implements AbstractBaseExceptionEnum {

    NO_SIGNED_USER(1, "项目无签约用户"),
    NO_REGION_INFO(2, "无法获取事件对应区域信息"),
    NO_ORG_INFO(3, "获取机构失败"),
    NO_SIGNED_CUST(4, "项目无签约用户");

    private final Integer code;

    private final String message;

    PlanExceptionEnum(Integer code, String message) {
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
