
package com.xqxy.dr.modular.event.enums;

import lombok.Getter;

/**
 * 事件状态枚举
 *
 * @author xuyuxiang
 * @date 2020/3/26 10:12
 */
@Getter
public enum EventStatusEnum {

    /**
     * 保存
     */
    STATUS_SAVE("01", "保存"),

    /**
     * 审核中
     */
    STATUS_REVIEW("02", "待执行"),

    /**
     * 审核通过
     */
    STATUS_PASS("03", "执行中"),

    /**
     * 审核不通过
     */
    STATUS_FAIL("04", "结束"),

    /**
     * 下达事件
     */
    STATUS_SEND("05", "废弃"),

    /**
     * 事件已发布
     */
    STATUS_PUBLISH("06", "事件已发布"),

//    /**
//     *  执行中
//     */
//    STATUS_EXECUTE("09", " 执行中"),
//
//    /**
//     *  执行结束
//     */
//    STATUS_TEN("10", " 执行结束"),
//    /**
//     *  评估公示中
//     */
//    STATUS_ELEVEN("11", " 评估公示中"),
//
//    /**
//     *  已归档
//     */
//    STATUS_TWELVE("02", " 已归档"),
//
    /**
     *  事件撤销
     */
    STATUS_THIRTEEN("07", " 事件撤销");

    /**
     * 事件中止
     */
//    STATUS_STOP("14", "事件中止");

    private final String code;

    private final String message;

    EventStatusEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
