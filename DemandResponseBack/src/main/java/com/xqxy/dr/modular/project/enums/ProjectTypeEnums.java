package com.xqxy.dr.modular.project.enums;

import lombok.Getter;

/**
 * 有效性判定枚举
 */
@Getter
public enum ProjectTypeEnums {

    PRICE_TYPE("1", "价格型"),

    EXCITATION_TYPE("2", "激励型");

    private final String code;

    private final String message;

    ProjectTypeEnums(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
