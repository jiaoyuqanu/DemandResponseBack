package com.xqxy.dr.modular.project.enums;

import com.xqxy.core.annotion.ExpEnumType;
import com.xqxy.core.consts.SysExpEnumConstant;
import com.xqxy.core.exception.enums.abs.AbstractBaseExceptionEnum;
import com.xqxy.core.factory.ExpEnumCodeFactory;

/**
 * 需求响应项目异常
 *
 * @author xuyuxiang
 * @date 2020/3/26 10:12
 */
@ExpEnumType(module = SysExpEnumConstant.EVENT_MODULE_EXP_CODE, kind = SysExpEnumConstant.EVENT_EVENT_INVITATION_EXCEPTION_ENUM)
public enum ProjectExceptionEnum implements AbstractBaseExceptionEnum {


    /**
     * 项目状态不明确
     */
    UNCORRECT_PROJECT_STATUS(1, "项目已公示或已提交审核"),
    PROCESS_NOT_FINISHED(2, "项目已公示或已提交审核"),
    NOT_EXACTLY_STATUS(3,"该项目状态下无法进行此操作"),
    NOT_RESULT(5,"微服务调用无返回结果"),
    PROJECTNO_REPEAT(4,"项目编号重复");

    private final Integer code;

    private final String message;

    ProjectExceptionEnum(Integer code, String message) {
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
