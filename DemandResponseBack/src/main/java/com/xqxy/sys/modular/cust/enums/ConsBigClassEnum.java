package com.xqxy.sys.modular.cust.enums;

import lombok.Getter;

@Getter
public enum ConsBigClassEnum {

    INDUSTRIAL_USERS("100", "工业用户"),

    RESIDENT_USER("300", "居民用户"),

    BUILDING_USERS("200", "楼宇用户"),

    EMERGING_LOAD_USERS("400", "新兴负荷用户"),

    AGRICULTURE_USER("500", "农林牧渔用户"),

    OTHER_USERS("600", "其他用户");


    private final String code;

    private final String message;

    ConsBigClassEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
