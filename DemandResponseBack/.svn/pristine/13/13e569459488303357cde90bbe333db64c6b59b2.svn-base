package com.xqxy.dr.modular.strategy;


import com.xqxy.dr.modular.event.param.PlanParam;

import java.time.LocalDateTime;

public interface CalculateStrategy {

    //事件方案
    void createPlan(PlanParam planParam);

    //事件一次邀约(事件下发)
    void createFirstInvitationList(Long eventId, LocalDateTime deadlineTime, String replySource,Integer regulateMultiple,Integer endCondition);

}
