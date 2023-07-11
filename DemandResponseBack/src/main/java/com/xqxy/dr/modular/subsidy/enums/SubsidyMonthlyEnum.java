package com.xqxy.dr.modular.subsidy.enums;

import lombok.Getter;

/**
 * 月补贴状态 枚举
 *
 * @author hu xingxing
 * @date 2020-11-01 9:30
 */
@Getter
public enum SubsidyMonthlyEnum {

    NOPUBLIC("0", "未公示"),

    PUBLIC("1", "已公示"),

    FROZEN("2", "已冻结");

    private final String code;

    private final String message;

    SubsidyMonthlyEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
