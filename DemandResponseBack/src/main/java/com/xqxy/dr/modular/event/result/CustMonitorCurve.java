package com.xqxy.dr.modular.event.result;

import com.xqxy.dr.modular.baseline.entity.CustBaseLineDetail;
import com.xqxy.dr.modular.data.entity.ConsCurve;
import com.xqxy.dr.modular.event.entity.ConsBaseline;
import com.xqxy.dr.modular.event.entity.CustBaselineAll;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CustMonitorCurve {


    /**
     * 本实体记录的唯一标识，产生规则为流水号
     */
    private Long planId;

    /**
     * 本实体记录的唯一标识，产生规则为流水号
     */
    private String eventId;

    /**
     * 本实体记录的唯一标识，产生规则为流水号
     */
    private String custId;
    /**
     * 邀约事件负荷基线
     */
    private CustBaseLineDetail custBaseline;

    /**
     * 客户全量基线
     */
    private CustBaselineAll custBaselineAll;

    /**
     * 邀约事件目标负荷曲线
     */
    private CustBaseLineDetail targetCurve;

    /**
     * 邀约事件实时负荷曲线
     */
    private ConsCurve consCurve;

    /**
     * 有效响应判定值上限
     */
    private ConsCurve tempCurve;

    /**
     * 基线最大负荷
     */
    private BigDecimal maxLoadBaseline;

    /**
     * 基线最小负荷
     */
    private BigDecimal minLoadBaseline;

    /**
     * 基线平均负荷
     */
    private BigDecimal avgLoadBaseline;

}
