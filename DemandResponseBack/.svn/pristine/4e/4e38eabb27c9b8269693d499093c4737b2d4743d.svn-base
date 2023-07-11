
package com.xqxy.core.enums;

import com.xqxy.core.annotion.ExpEnumType;
import com.xqxy.core.consts.SysExpEnumConstant;
import com.xqxy.core.exception.enums.abs.AbstractBaseExceptionEnum;
import com.xqxy.core.factory.ExpEnumCodeFactory;

/**
 * 登录用户信息相关异常枚举
 *
 * @author xuyuxiang
 * @date 2020/3/28 14:47
 */
@ExpEnumType(module = SysExpEnumConstant.SYS_MODULE_EXP_CODE, kind = SysExpEnumConstant.SYS_ROLE_EXCEPTION_ENUM)
public enum CurrenUserInfoExceptionEnum implements AbstractBaseExceptionEnum {

    /**
     * 登录用户信息不存在
     */
    CURRENT_USER_INFO_NOT_EXIST(1, "登录用户信息不存在");

    private final Integer code;

    private final String message;

    CurrenUserInfoExceptionEnum(Integer code, String message) {
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
