package com.xqxy.dr.modular.project.enums;

import com.xqxy.core.annotion.ExpEnumType;
import com.xqxy.core.consts.SysExpEnumConstant;
import com.xqxy.core.exception.enums.abs.AbstractBaseExceptionEnum;
import com.xqxy.core.factory.ExpEnumCodeFactory;

@ExpEnumType(module = SysExpEnumConstant.EVENT_MODULE_EXP_CODE, kind = SysExpEnumConstant.EVENT_EVENT_INVITATION_EXCEPTION_ENUM)
public enum ExamineExceptionEnum  implements AbstractBaseExceptionEnum {

    /**
     * 创建待办任务失败
     */
    CREATION_FAILED(1, "创建待办任务失败"),
    APPROVAL_FAIL(2,"审核流程失败"),
    RESULT_FAIL(4,"返回结果为空"),
    NO_APPROVAL(3,"未配置审核流程单位");

    private final Integer code;

    private final String message;

    ExamineExceptionEnum(Integer code, String message) {
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
