package com.xqxy.dr.modular.project.enums;

import lombok.Getter;

@Getter
public enum ProjectTimeTypeEnum {

    PRICE_TYPE("1", "邀约"),

    EXCITATION_TYPE("2", "实时");

    private final String code;

    private final String message;

    ProjectTimeTypeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ProjectTimeTypeEnum getByCode(String code) {
        for (ProjectTimeTypeEnum value : ProjectTimeTypeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

    public static String getMessageByCode(String code) {
        ProjectTimeTypeEnum projectTimeTypeEnum = getByCode(code);
        if (projectTimeTypeEnum == null) {
            return "";
        } else {
            return projectTimeTypeEnum.getMessage();
        }
    }
}
