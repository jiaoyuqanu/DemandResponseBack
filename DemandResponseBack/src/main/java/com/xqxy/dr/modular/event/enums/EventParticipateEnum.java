package com.xqxy.dr.modular.event.enums;

import lombok.Getter;

/**
 * 邀约是否参与枚举
 */
@Getter
public enum EventParticipateEnum {

    IS_PARTICIPATE("Y","参与"),
    NOT_PARTICIPATE("N","不参与");

    private final String code;

    private final String message;

    EventParticipateEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
