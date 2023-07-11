package com.xqxy.dr.modular.project.enums;

import lombok.Getter;

@Getter
public enum ResponseTypeEnum {
    /**
     * 事件 削峰或者填谷 类型
     */
    DES("1","削峰"),

    RIS("2","填谷");

    private final String code;

    private final String message;

    ResponseTypeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ResponseTypeEnum getByCode(String code) {
        for (ResponseTypeEnum value : ResponseTypeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

    public static String getMessageByCode(String code) {
        ResponseTypeEnum advanceNoticeEnums = getByCode(code);
        if (advanceNoticeEnums == null) {
            return "";
        } else {
            return advanceNoticeEnums.getMessage();
        }
    }
}
