package com.xqxy.dr.modular.evaluation.param;

import com.baomidou.mybatisplus.annotation.TableField;
import com.xqxy.core.pojo.base.param.BaseParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * <p>
 * 客户效果评估 参数
 * </p>
 *
 * @author hu xingxing
 * @since 2021-10-22 15:00
 */
@ApiModel(description = "客户效果评估")
@EqualsAndHashCode(callSuper = true)
@Data
public class CustEvaluationParam extends BaseParam {

    //评估标识
    private Long evaluationId;

    //事件标识
    @NotNull(message = "custId不能为空，请检查custId参数", groups = {trace.class})
    private Long eventId;

    //客户标识
    @NotNull(message = "custId不能为空，请检查custId参数", groups = {trace.class})
    private Long custId;

    private String creditCode;

    private String legalName;

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

    //是否有效:N 无效；Y 有效
    private String isEffective;

    //有效响应时长(**分钟)
    private Integer effectiveTime;

    //是否合格(平均负荷)
    private String isQualified;

    //是否越界(最大负荷)
    private String isOut;

    //是否集成商
    private String integrator;
}
