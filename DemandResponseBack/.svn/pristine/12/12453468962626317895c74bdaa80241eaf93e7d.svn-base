package com.xqxy.sys.modular.sms.enums;

import lombok.Getter;

/**
 * 短信发送状态
 */
@Getter
public enum SmsSendStatusEnum {

    NOT_SEND(0,"未发送"),
    WAITING_SEND(1,"待发送"),
    ALREADY_SEND(2,"已发送"),
    SEND_SUCCESS(3,"发送成功"),
    SEND_FAIL(4,"发送失败"),
    SEND_INVALID(5,"失效");

    private final Integer code;

    private final String message;

    SmsSendStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
