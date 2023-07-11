package com.xqxy.dr.modular.project.VO;

import lombok.Data;

@Data
public class EventDetailByProjectVO {

    /**
     * 响应类型
     */
    private String responseType;

    /**
     * 时间类型
     */
    private String timeType;

    /**
     * 提前通知时间
     */
    private String advanceNoticeTime;

    /**
     *  总次数
     */
    private Integer count;

    /**
     *  待执行 次数
     */
    private Integer reviewCount;

    /**
     *  执行中 次数
     */
    private Integer passCount;

    /**
     *  已结束 次数
     */
    private Integer failCount;

    /**
     *  已终止  (撤销状态) 次数
     */
    private Integer thirteenCount;


}
