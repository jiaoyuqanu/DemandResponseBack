package com.xqxy.dr.modular.project.enums;

import lombok.Getter;

/**
 * 提前通知时间
 */
@Getter
public enum AdvanceNoticeEnums {

    /**
     * 24小时
     */
    TWENTY_FOUR("1", "日前"),

    /**
     * 4小时
     */
    FOUR("2", "小时级"),

    /**
     * 0.5小时
     */
    ZERO_POINT_FIVE("3", "分钟级"),

    SECOND_POINT_FIVE("4","秒级");

    private final String code;

    private final String message;

    AdvanceNoticeEnums(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static AdvanceNoticeEnums getByCode(String code) {
        for (AdvanceNoticeEnums value : AdvanceNoticeEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

    public static String getMessageByCode(String code) {
        AdvanceNoticeEnums advanceNoticeEnums = getByCode(code);
        if (advanceNoticeEnums == null) {
            return "";
        } else {
            return advanceNoticeEnums.getMessage();
        }
    }
}
