package com.xqxy.sys.modular.sms.enums;

import lombok.Getter;

/**
 * 是否参与执行，反馈后方案编制最终确认参与用户标志，字典 Y 或者 N
 */
@Getter
public enum ImplementEnum {

    Y("Y","参与"),
    N("N","未参与");

    private final String code;

    private final String message;

    ImplementEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
