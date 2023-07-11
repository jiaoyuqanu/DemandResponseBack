package com.xqxy.core.enums;

import lombok.Getter;

/**
 * 审批流程返回状态码
 */
@Getter
public enum ApprovalCodeEnum {
    /**
     * 正常
     */
    SUCCESS("000000", "接口调用成功"),

    BUILD_SUCCESS("000", "待办事项创建成功！"),

    BUILD_FAIL("001", "待办事项创建失败!"),

    PROCESS_FAIL("0002", "审批流程失败！"),

    PROCESS_SUCCESS("003", "审批流程结束成功！"),

    APPROVAL_REJECT("004", "申请被驳回！"),

    LOG_SUCCESS("005", "创建操作日志成功！"),

    LOG_FAIL("006", "创建操作日志失败！");

    private final String code;

    private final String message;

    ApprovalCodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
