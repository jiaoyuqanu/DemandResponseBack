package com.xqxy.dr.modular.project.enums;

import lombok.Getter;

/**
 * <p>
 * 签约状态枚举
 * </p>
 *
 * @author Caoj
 * @since 2021-10-15 16:06
 */
@Getter
public enum DeclareProjectEnums {
    SAVE("1", "保存"),

    SIGNED("2", "已签约"),

    WITHDRAW("3", "撤销");

    private final String code;

    private final String message;

    DeclareProjectEnums(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static String getMessage(String code) {
        for (DeclareProjectEnums status : DeclareProjectEnums.values()) {
            if(status.getCode().equals(code)) {
                return status.getMessage();
            }
        }
        return null;
    }
}
