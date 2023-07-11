package com.xqxy.dr.modular.event.result;

import com.xqxy.dr.modular.event.entity.Event;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Description
 * @ClassName EventInvitationResult
 * @Author User
 * @date 2021.05.13 19:55
 */
@Data
public class EventInvitationResult {

    /**
     * 需求响应事件
     */
    private Event event;

    /**
     * 反馈响应量
     */
    private BigDecimal totalReplyCap;


    /**
     * 反馈时间
     */
    private LocalDateTime lastReplyTime;


}
