package com.xqxy.dr.modular.newloadmanagement.param;

import lombok.Data;
import org.springframework.util.StringUtils;
import java.math.BigDecimal;

@Data
public class DemandParam  {

   private String regulateId;//指令ID
    private String eventname;//指令名称
    private String strategyName;//方案名称
   private String rangeType;//调控类型
   private String regulateRange;//调控范围
    private BigDecimal regulateCap;//调控目标
    private String startTime;//响应开始时间
    private String endTime;//'响应结束时间'
    private String responseType;//响应类型 1、削峰 2、填谷
    private String regulateDate;//调度日期

    public void regularRangeType(){
        if(!StringUtils.isEmpty(rangeType)){
            rangeType  = "[[\"" + rangeType + "\"]]";
        }
    }

    public void regularTime(){
        if(!StringUtils.isEmpty(startTime)){
            startTime = startTime.substring(0, 5);
        }
        if(!StringUtils.isEmpty(endTime)){
            endTime = endTime.substring(0, 5);
        }
    }

}
