package com.xqxy.core.enums;

import lombok.Getter;

/**
 * 区域等级枚举
 */
@Getter
public enum RegionLevelEnum {

    /**
     * 地区不存在
     */
    REGION_NOT_EXIST("1", "地区不存在"),

    LEVEL_ONE("1","省"),
    LEVEL_TWO("2","市"),
    LEVEL_THREE("3","区");

    private final String code;

    private final String message;

    RegionLevelEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
