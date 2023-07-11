package com.xqxy.dr.modular.event.result;

import com.xqxy.dr.modular.data.entity.ConsCurve;
import com.xqxy.dr.modular.event.entity.ConsBaseline;
import com.xqxy.dr.modular.event.entity.ConsBaselineAll;
import com.xqxy.dr.modular.newloadmanagement.vo.Point96Vo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class EventExecuteCurveResult {


    /**
     * 全量基线数据
     */
    @ApiModelProperty("基线数据")
    private Point96Vo baseline;

    /***
     * 实际负荷
     */
    @ApiModelProperty("实际负荷")
    private Point96Vo realLine;

    private BigDecimal baseLineCumulative;

    private BigDecimal realLineCumulative;

    private BigDecimal pressureLineCumulative;

}
