package com.xqxy.dr.modular.event.result;

import com.xqxy.dr.modular.baseline.entity.CustBaseLineDetail;
import com.xqxy.dr.modular.data.entity.ConsCurve;
import com.xqxy.dr.modular.event.entity.CustBaselineAll;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class EventUserCustLineResult {

    @ApiModelProperty("基线数据")
    private CustBaseLineDetail custBaseLine;

    @ApiModelProperty("客户全量基线")
    private CustBaselineAll custBaselineAll;

    @ApiModelProperty("实际负荷")
    private ConsCurve custConsCurve;

    @ApiModelProperty("签约响应容量")
    private BigDecimal contractCap;
    @ApiModelProperty("应邀负荷")
    private BigDecimal replyCap;
    @ApiModelProperty("基线平均负荷")
    private BigDecimal avgLoadBaseline;
    @ApiModelProperty("基线最大负荷")
    private BigDecimal maxLoadBaseline;
}
