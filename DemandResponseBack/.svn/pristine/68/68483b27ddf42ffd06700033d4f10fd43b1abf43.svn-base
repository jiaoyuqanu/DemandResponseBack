
package com.xqxy.core.exception.enums;

import com.xqxy.core.annotion.ExpEnumType;
import com.xqxy.core.consts.ExpEnumConstant;
import com.xqxy.core.exception.enums.abs.AbstractBaseExceptionEnum;
import com.xqxy.core.factory.ExpEnumCodeFactory;

/**
 * 请求类型相关异常枚举
 *
 * @author xiao jun
 * @date 2020/4/2 15:42
 */
@ExpEnumType(module = ExpEnumConstant.CORE_MODULE_EXP_CODE, kind = ExpEnumConstant.REQUEST_TYPE_EXCEPTION_ENUM)
public enum RequestTypeExceptionEnum implements AbstractBaseExceptionEnum {

    /**
     * 参数传递格式不支持
     */
    REQUEST_TYPE_IS_JSON(1, "参数传递格式不支持，请使用JSON格式传参"),

    /**
     * 请求JSON参数格式不正确
     */
    REQUEST_JSON_ERROR(2, "请求JSON参数格式不正确，请检查参数格式");

    private final Integer code;

    private final String message;

    RequestTypeExceptionEnum(Integer code, String message) {
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
