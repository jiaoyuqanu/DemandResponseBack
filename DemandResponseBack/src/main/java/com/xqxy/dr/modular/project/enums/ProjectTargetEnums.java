package com.xqxy.dr.modular.project.enums;

import lombok.Getter;

/**
 * 项目目标枚举
 */
@Getter
public enum ProjectTargetEnums {

    /**
     * 削峰
     */
    PEEK("1", "削峰"),

    /**
     * 填谷
     */
    FILL("2", "填谷"),

    /**
     * 消纳清洁能源
     */
    CLEAN_ENERGY("3", "消纳清洁能源");

    private final String code;

    private final String message;

    ProjectTargetEnums(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ProjectTargetEnums getByCode(String code) {
        for (ProjectTargetEnums value : ProjectTargetEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

    public static String getMessageByCode(String code) {
        ProjectTargetEnums projectTargetEnums = getByCode(code);
        if (projectTargetEnums == null) {
            return "";
        } else {
            return projectTargetEnums.getMessage();
        }
    }

}
