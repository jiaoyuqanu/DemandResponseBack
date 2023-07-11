package com.xqxy.dr.modular.event.result;

import com.xqxy.dr.modular.data.entity.ConsCurve;
import com.xqxy.dr.modular.event.entity.ConsBaseline;
import com.xqxy.dr.modular.event.entity.ConsBaselineAll;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class EventUserConsLineResult {

    /**
     * 区间基线数据
     */
    @ApiModelProperty("区间基线数据")
    private ConsBaseline consBaseline;

    /**
     * 全量基线数据
     */
    @ApiModelProperty("全量基线数据")
    private ConsBaselineAll consBaselineAll;

    /***
     * 实际负荷
     */
    @ApiModelProperty("实际负荷")
    private ConsCurve consCurve;
}
