package com.xqxy.dr.modular.evaluation.enums;

import lombok.Getter;

/**
 * 评估/补贴标识 枚举
 *
 * @author hu xingxing
 * @date 2020-10-19 16:35
 */
@Getter
public enum EvaluTaskEnum {

    /**
     * 未计算
     */
    CAL_NO("1", "未计算"),

    /**
     * 计算完成
     */
    CAL_COMPLETE("2", "计算完成"),

    /**
     * 计算异常
     */
    CAL_EXCEPTION("3", "计算异常");

    private final String code;

    private final String message;

    EvaluTaskEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
