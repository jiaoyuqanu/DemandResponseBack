package com.xqxy.dr.modular.event.enums;

import lombok.Getter;

/**
 * 邀约轮次枚举
 */
@Getter
public enum EventInvitationRoundEnum {

    FIRST_INVITATION_ROUND(1,"一次邀约"),
    SECOND_INVITATION_ROUND(2,"二次邀约");

    private final Integer code;

    private final String message;

    EventInvitationRoundEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
