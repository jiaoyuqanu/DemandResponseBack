package com.xqxy.dr.modular.event.enums;

import lombok.Getter;

/**
 * 需求响应方案状态
 */
@Getter
public enum PlanExecuteEnum {

    NOCONS("1", "方案同步中"),

    SUCCESS("2", "执行成功"),

    NOCAP("3", "调控目标为0或者为空"),

    OVERTIME("4", "距离上次执行时间不足1分钟");

    private final String code;

    private final String message;

    PlanExecuteEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
