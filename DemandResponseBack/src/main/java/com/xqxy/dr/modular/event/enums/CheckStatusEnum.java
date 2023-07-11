package com.xqxy.dr.modular.event.enums;

import lombok.Getter;

@Getter
public enum CheckStatusEnum {

    UNSUBMITTED("1", "未提交"),

    UNDER_REVIEW("2", "审核中"),

    PASS_THE_AUDIT("3", "审核通过"),

    AUDIT_FAILED("4", "审核不通过");

    private final String code;

    private final String message;

    CheckStatusEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
