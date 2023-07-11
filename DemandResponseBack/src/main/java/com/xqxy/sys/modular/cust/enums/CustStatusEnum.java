package com.xqxy.sys.modular.cust.enums;

import lombok.Getter;

/**
 * 账号状态枚举
 */
@Getter
public enum CustStatusEnum {

    SAVED("1", "未认证"),

    APPROVING("2", "已认证"),

    AUDIT("3", "撤销"),

    FAILAUDIT("4", "认证失败");

    private final String code;

    private final String message;

    CustStatusEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
