
package com.xqxy.dr.modular.data.enums;

import lombok.Getter;

/**
 * 策略类型相关枚举
 *
 * @author xuyuxiang
 * @date 2020/3/26 10:12
 */
@Getter
public enum CalculateStrategyEnum {

    /**
     * 标准策略
     */
    NORMAL_STRATEGY("normalStrategy", "标准策略"),

    /**
     * 湖南策略
     */
    HUNAN_STRATEGY("hunanStrategy", "湖南策略");

    private final String code;

    private final String message;

    CalculateStrategyEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }


}
