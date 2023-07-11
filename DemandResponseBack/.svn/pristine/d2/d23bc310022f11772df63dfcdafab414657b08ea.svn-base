package com.xqxy.sys.modular.cust.enums;

import lombok.Getter;

@Getter
public enum IdentityTypeEnum {

    IDCARD("1", "居民身份证"),

    PASSPORT("2", "护照");


    private final String code;

    private final String message;

    IdentityTypeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static String getValue(String code) {
        for (IdentityTypeEnum ele : IdentityTypeEnum.values()) {
            if (ele.getCode().equals(code)) {
                return ele.getMessage();
            }
        }
        return code;
    }
}
