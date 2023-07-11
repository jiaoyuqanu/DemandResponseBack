package com.xqxy.dr.modular.event.enums;

import lombok.Getter;

@Getter
public enum InvitationEvalStatusEnum {

    /**
     * WEB
     */
    EVAL_FAIL("0", "计算失败"),

    /**
     * APP
     */
    EVAL_SUCCESS("1", "计算成功");

    private final String code;

    private final String message;

    InvitationEvalStatusEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
