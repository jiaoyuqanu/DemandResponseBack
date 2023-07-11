package com.xqxy.sys.modular.calendar.enums;


import lombok.Getter;

/**
 * 日历中的dateType字典枚举
 *
 */
@Getter
public enum CalendarInfoTypeEnum {

    TYPE_01("1","工作日"),
    TYPE_02("2","周末"),
    TYPE_03("3","节假日");

    private final String code;

    private final String message;

    CalendarInfoTypeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
