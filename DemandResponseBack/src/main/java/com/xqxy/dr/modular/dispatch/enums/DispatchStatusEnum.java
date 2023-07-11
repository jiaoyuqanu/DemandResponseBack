package com.xqxy.dr.modular.dispatch.enums;

import lombok.Getter;

/**
 * 指令状态枚举
 */
@Getter
public enum DispatchStatusEnum {

    SAVED("01","保存"),
   /* UNDER_REVIEW("02","审核中"),
    APPROVED("03","审核通过"),
    FAIL_APPROVING("04","审核不通过"),*/
    ALREADY_SEND("02","指令已下发"),
    REVOKE("03","撤销");

    private final String code;

    private final String message;

    DispatchStatusEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
