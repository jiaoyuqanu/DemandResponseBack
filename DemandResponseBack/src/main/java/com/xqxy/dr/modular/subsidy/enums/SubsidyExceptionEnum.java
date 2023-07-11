package com.xqxy.dr.modular.subsidy.enums;

import com.xqxy.core.annotion.ExpEnumType;
import com.xqxy.core.consts.SysExpEnumConstant;
import com.xqxy.core.exception.enums.abs.AbstractBaseExceptionEnum;
import com.xqxy.core.factory.ExpEnumCodeFactory;

@ExpEnumType(module = SysExpEnumConstant.EVENT_MODULE_EXP_CODE, kind = SysExpEnumConstant.EVENT_EVENT_INVITATION_EXCEPTION_ENUM)
public enum SubsidyExceptionEnum implements AbstractBaseExceptionEnum {

    SUBSIDY_EXCEPTION_NUll(1, "异议工单不存在"),
    SETTLEMENT_ID_EXIST(2, "结算批号已存在"),
    SETTLEMENT_WEEK_REPEAT(3, "结算周期重叠"),
    PAY_RECORD_REPEAT(4, "发放记录重复生成"),
    NO_SETTLEMENT_REC(5, "无结算记录"),
    NO_SETTLEMENT_DALLIY(6, "无日补贴记录"),
    NO_PARAM_REC(7, "缺少必填参数"),
    NO_EVENT_REC(8, "无事件信息"),
    NO_TIME_REC(9, "效果评估核算中，第二天可申诉"),
    HAS_SUBSIDY_REC(10, "已存在待处理申诉"),
    EVENT_NOTEND_REC(11, "事件未结束"),
    NO_PLAN_REC(12, "无方案信息"),
    NO_PLAN_EXECUTE_REC(13, "用户未参与执行"),
    DUPLICATE_APPEAL(14, "申诉重复");

    private final Integer code;

    private final String message;

    SubsidyExceptionEnum(Integer code, String message) {
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
