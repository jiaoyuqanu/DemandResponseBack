package com.xqxy.dr.modular.event.enums;

import lombok.Getter;

/**
 * 基线剔除规则枚举
 */
@Getter
public enum EliminateRulesEnum {


    BASELINE_EMPTY("01", "基线为空"),

    LESS_THAN_TEN("02", "平均基线负荷小于10kW"),

    LESS_THAN_REPLYCAP("03", "平均基线负荷小于可响应容量"),

    REGION_RULE("04", "手动剔除"),
    MANUAL_RULE("05", "区域范围");

    /**
     * 事件中止
     */
//    STATUS_STOP("14", "事件中止");

    private final String code;

    private final String message;

    EliminateRulesEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
