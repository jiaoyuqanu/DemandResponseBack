package com.xqxy.dr.modular.evaluation.param;

import com.xqxy.core.pojo.base.param.BaseParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * <p>
 * 用户效果评估 参数
 * </p>
 *
 * @author hu xingxing
 * @since 2021-10-19 15:00
 */
@ApiModel(description = "用户效果评估")
@EqualsAndHashCode(callSuper = true)
@Data
public class ConsEvaluationParam extends BaseParam {

    //评估标识
    private long evaluationId;

    //事件标识
    private long eventId;

    //用户标识
    private String consId;

    //用户名称
    private String consName;

    private String legalName;

    private String creditCode;

    //邀约响应量
    private BigDecimal invitationCap;

    //反馈响应量
    private BigDecimal replyCap;

    //基线最大负荷
    private BigDecimal maxLoadBaseline;

    //基线最小负荷
    private BigDecimal minLoadBaseline;

    //基线平均负荷
    private BigDecimal avgLoadBaseline;

    //实际最大负荷
    private BigDecimal maxLoadActual;

    //实际最小负荷
    private BigDecimal minLoadActual;

    //实际平均负荷
    private BigDecimal avgLoadActual;

    //实际响应负荷
    private BigDecimal actualCap;

    //实际响应电量
    private BigDecimal actualEnergy;

    //核定响应负荷
    private BigDecimal confirmCap;

    //是否有效
    private String isEffective;

    //有效响应时长
    private int effectiveTime;

    //是否合格
    private String isQualified;

    //是否越界
    private String isOut;

    @ApiModelProperty(value = "机构")
    private String orgNo;
}
