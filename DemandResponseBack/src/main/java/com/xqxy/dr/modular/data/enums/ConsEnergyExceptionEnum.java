package com.xqxy.dr.modular.data.enums;

import com.xqxy.core.annotion.ExpEnumType;
import com.xqxy.core.consts.SysExpEnumConstant;
import com.xqxy.core.exception.enums.abs.AbstractBaseExceptionEnum;

/**
 * 用户电能势值相关异常枚举
 *
 * @author xuyuxiang
 * @date 2020/3/31 20:47
 */
@ExpEnumType(module = SysExpEnumConstant.DATA_MODULE_EXP_CODE, kind = SysExpEnumConstant.DATA_CONS_CURVE_EXCEPTION_ENUM)
public enum ConsEnergyExceptionEnum implements AbstractBaseExceptionEnum {
    /**
     * 用户功率曲线不存在
     */
    DATE_TOOLATE(1, "电能势值时间不能早于当前时间");

    private final Integer code;

    private final String message;

    ConsEnergyExceptionEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }


    @Override
    public Integer getCode() {
        return null;
    }

    @Override
    public String getMessage() {
        return null;
    }
}
