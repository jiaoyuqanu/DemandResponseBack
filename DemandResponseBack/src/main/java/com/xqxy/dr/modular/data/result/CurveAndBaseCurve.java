package com.xqxy.dr.modular.data.result;

import com.xqxy.dr.modular.data.entity.ConsCurve;
import com.xqxy.dr.modular.data.entity.ConsCurveBase;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CurveAndBaseCurve {

    /**
     * 本实体记录的唯一标识，产生规则为流水号
     */
    private String consId;

    /**
     * 事件id
     */
    private Long eventId;


    /**
     * 响应开始时段:hh:mm:ss
     */
    private String startPeriod;

    /**
     * 响应结束时段:hh:mm:ss
     */
    private String endPeriod;

    /**
     * 响应开始日期
     */
    private LocalDate startDate;

    /**
     * 响应结束日期
     */
    private LocalDate endDate;

    /**
     * 实际负荷曲线
     */
   private ConsCurve consCurve;

    /**
     * 基线负荷曲线
     */
   private ConsCurveBase consCurveBase;

}
