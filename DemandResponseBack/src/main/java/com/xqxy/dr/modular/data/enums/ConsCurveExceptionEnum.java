
package com.xqxy.dr.modular.data.enums;

import com.xqxy.core.annotion.ExpEnumType;
import com.xqxy.core.consts.SysExpEnumConstant;
import com.xqxy.core.exception.enums.abs.AbstractBaseExceptionEnum;
import com.xqxy.core.factory.ExpEnumCodeFactory;

/**
 * 用户功率曲线相关异常枚举
 *
 * @author xuyuxiang
 * @date 2020/3/31 20:47
 */
@ExpEnumType(module = SysExpEnumConstant.DATA_MODULE_EXP_CODE, kind = SysExpEnumConstant.DATA_CONS_CURVE_EXCEPTION_ENUM)
public enum ConsCurveExceptionEnum implements AbstractBaseExceptionEnum {

    /**
     * 用户功率曲线不存在
     */
    CONS_CURVE_NOT_EXIST(1, "用户功率曲线不存在"),
    NO_STATDATE(2, "请传入曲线日期"),
    DATACENTER_EXCPETION(3, "中台接口请求失败，数据转换异常");

    private final Integer code;

    private final String message;

    ConsCurveExceptionEnum(Integer code, String message) {
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
