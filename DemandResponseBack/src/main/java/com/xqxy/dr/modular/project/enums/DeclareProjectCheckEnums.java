package com.xqxy.dr.modular.project.enums;

import lombok.Getter;

/**
 * <p>
 * 签约提交状态枚举
 * </p>
 *
 * @author Caoj
 * @date 2021-10-15 16:06
 */
@Getter
public enum DeclareProjectCheckEnums {
    UNSUBMIT("1", "未提交"),

    VERIFYING("2", "审核中"),

    VERIFIED("3", "审核通过"),

    UNVERIFIED("4", "审核不通过");


    private final String code;

    private final String message;

    DeclareProjectCheckEnums(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static String getMessage(String code) {
        for (DeclareProjectCheckEnums status : DeclareProjectCheckEnums.values()) {
            if(status.getCode().equals(code)) {
                return status.getMessage();
            }
        }
        return null;
    }
}
