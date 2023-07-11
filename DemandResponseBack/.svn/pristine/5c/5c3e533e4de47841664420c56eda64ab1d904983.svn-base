package com.xqxy.dr.modular.event.param;

import com.baomidou.mybatisplus.annotation.TableField;
import com.xqxy.core.pojo.base.param.BaseParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

@ApiModel(description = "用户基线 参数")
@EqualsAndHashCode(callSuper = true)
@Data
public class ConsBaselineParam extends BaseParam {

    @ApiModelProperty(value = "负荷基线标识")
    private Long baselineId;

    @ApiModelProperty(value = "基线库标识")
    private Long baselineLibId;

    @ApiModelProperty(value = "用户名称")
    private String consName;

    @ApiModelProperty(value = "用户标识")
    @TableField("CONS_ID")
    private String consId;

    @ApiModelProperty(value = "基线日期")
    private LocalDate baselineDate;

    @ApiModelProperty(value = "日期逗号隔开")
    private String simplesDate;

    @ApiModelProperty(value = "基线最大负荷")
    private BigDecimal maxLoadBaseline;

    @ApiModelProperty(value = "基线最小负荷")
    private BigDecimal minLoadBaseline;

    @ApiModelProperty(value = "基线平均负荷")
    private BigDecimal avgLoadBaseline;

    @ApiModelProperty(value = "异常描述")
    private String exceptionRemark;

    @ApiModelProperty(value = "逗号隔开")
    private String simplesId;

    @ApiModelProperty(value = "计算规则，1，2，3")
    private String calRule;

    private String orgId;
}
