package com.xqxy.dr.modular.dispatch.enums;

import lombok.Getter;

/**
 * 响应时段状态枚举
 */
@Getter
public enum DispatchSoltStatusEnum {

    SAVED("01","保存"),
    /*UNDER_REVIEW("02","审核中"),
    APPROVED("03","审核通过"),
    FAIL_APPROVING("04","审核不通过"),*/
    ALREADY_SEND("02","事件已生成"),
    REVOKE("03","撤销");

    private final String code;

    private final String message;

    DispatchSoltStatusEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
