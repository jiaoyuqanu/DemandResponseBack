package com.xqxy.dr.modular.subsidy.enums;

import lombok.Getter;

/**
 * 评估/补贴标识 枚举
 *
 * @author hu xingxing
 * @date 2020-10-19 16:35
 */
@Getter
public enum CalRuleEnum {

    /**
     * 计算规则一
     */
    ONE("1", "计算规则一"),

    /**
     * 计算规则二
     */
    TWO("2", "计算规则二"),

    /**
     * 计算规则三
     */
    THREE("3", "计算规则三");

    private final String code;

    private final String message;

    CalRuleEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
