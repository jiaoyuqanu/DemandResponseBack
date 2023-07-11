
package com.xqxy.sys.modular.dict.enums;

import com.xqxy.core.annotion.ExpEnumType;
import com.xqxy.core.consts.SysExpEnumConstant;
import com.xqxy.core.exception.enums.abs.AbstractBaseExceptionEnum;
import com.xqxy.core.factory.ExpEnumCodeFactory;

/**
 * 系统字典类型相关异常枚举
 *
 * @author xuyuxiang
 * @date 2020/3/31 20:44
 */
@ExpEnumType(module = SysExpEnumConstant.DICT_MODULE_EXP_CODE, kind = SysExpEnumConstant.DICT_DICT_TYPE_EXCEPTION_ENUM)
public enum DictTypeExceptionEnum implements AbstractBaseExceptionEnum {

    /**
     * 字典类型不存在
     */
    DICT_TYPE_NOT_EXIST(1, "字典类型不存在"),

    /**
     * 字典类型编码重复
     */
    DICT_TYPE_CODE_REPEAT(2, "字典类型编码重复，请检查code参数"),

    /**
     * 字典类型名称重复
     */
    DICT_TYPE_NAME_REPEAT(3, "类型名称重复");

    private final Integer code;

    private final String message;

    DictTypeExceptionEnum(Integer code, String message) {
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
