package com.xqxy.dr.modular.event.enums;

import lombok.Getter;

@Getter
public enum EndConditionEnum {


//    截止条件 1.截止日期 2. 反馈目标*反馈倍数 3.即不符合要求
    DEADLINE_TIME(1, "截止日期"),

    REGULATE_CAP(2, "反馈目标*反馈倍数"),

    ALL(3, "满足任意条件，即不符合要求");


    private final Integer code;

    private final String message;

    EndConditionEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
