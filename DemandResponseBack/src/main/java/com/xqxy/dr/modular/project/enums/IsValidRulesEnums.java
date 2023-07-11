package com.xqxy.dr.modular.project.enums;

import lombok.Getter;

/**
 * 有效性判定枚举
 */
@Getter
public enum IsValidRulesEnums {

    VALID_RULE_ONE("1", "最大基线负荷与实际最大负荷的差值需不小于响应负荷确认值"),

    VALID_RULE_TWO("2", "平均基线负荷与实际平均负荷的差值需不低于响应负荷确认值*80%");

    private final String code;

    private final String message;

    IsValidRulesEnums(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
