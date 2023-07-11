package com.xqxy.dr.modular.project.enums;

import lombok.Getter;

/**
 * 参与方式
 */
@Getter
public enum ResponseTypeEnums {

    /**
     * 负荷集成商
     */
    AGGR_USER("1", "负荷集成商"),

    /**
     * 直接参与用户
     */
    DIRECT_USER("2", "直接参与用户"),

    /**
     * 代理参与用户
     */
    PROXY_USER("3", "代理参与用户");

    private final String code;

    private final String message;

    ResponseTypeEnums(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
