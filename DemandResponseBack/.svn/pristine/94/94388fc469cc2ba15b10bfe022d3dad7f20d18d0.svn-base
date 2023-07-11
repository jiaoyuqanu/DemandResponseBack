package com.xqxy.dr.modular.subsidy.param;

import com.xqxy.core.pojo.base.param.BaseParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@ApiModel(description = "用户补贴申诉 参数")
@EqualsAndHashCode(callSuper = true)
@Data
public class SubsidyAppealAmendParam extends BaseParam {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "关联评估ID")
    private Long evaluationId;

    @NotNull(message = "电力户号不能为空，请检查consId参数", groups = {detail.class})
    @ApiModelProperty(value = "电力户号")
    private String consId;

    @NotNull(message = "事件标识不能为空，请检查eventId参数", groups = {detail.class})
    @ApiModelProperty(value = "事件标识")
    private Long eventId;

    @NotNull(message = "最大基线负荷不能为空，请检查maxLoadBaseline参数", groups = {detail.class})
    @ApiModelProperty(value = "最大基线负荷")
    private BigDecimal maxLoadBaseline;

    @NotNull(message = "最小基线负荷不能为空，请检查minLoadBaseline参数", groups = {detail.class})
    @ApiModelProperty(value = "最小基线负荷")
    private BigDecimal minLoadBaseline;

    @NotNull(message = "平均基线负荷不能为空，请检查avgLoadBaseline参数", groups = {detail.class})
    @ApiModelProperty(value = "平均基线负荷")
    private BigDecimal avgLoadBaseline;

    @NotNull(message = "最大实际负荷不能为空，请检查maxLoadActual参数", groups = {detail.class})
    @ApiModelProperty(value = "最大实际负荷")
    private BigDecimal maxLoadActual;

    @NotNull(message = "最小实际负荷不能为空，请检查minLoadActual参数", groups = {detail.class})
    @ApiModelProperty(value = "最小实际负荷")
    private BigDecimal minLoadActual;

    @NotNull(message = "平均实际负荷不能为空，请检查avgLoadActual参数", groups = {detail.class})
    @ApiModelProperty(value = "平均实际负荷")
    private BigDecimal avgLoadActual;
}
