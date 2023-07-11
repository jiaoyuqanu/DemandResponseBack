package com.xqxy.sys.modular.cust.enums;

import lombok.Getter;

/**
 * 账号状态枚举
 */
@Getter
public enum CustCheckStatusEnum {

    SAVED("1", "未提交"),

    APPROVING("2", "审核中"),

    AUDIT("3", "审核通过"),

    FAILAUDIT("4", "审核不通过");

    private final String code;

    private final String message;

    CustCheckStatusEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static String getValue(String code) {
        for (CustCheckStatusEnum ele : CustCheckStatusEnum.values()) {
            if (ele.getCode().equals(code)) {
                return ele.getMessage();
            }
        }
        return code;
    }
}


