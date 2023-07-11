package com.xqxy.dr.modular.subsidy.param;

import com.xqxy.core.pojo.base.param.BaseParam;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * <p>
 * 客户月补贴 参数
 * </p>
 *
 * @author hu xingxing
 * @since 2021-10-28 15:00
 */
@ApiModel(description = "客户月补贴")
@EqualsAndHashCode(callSuper = true)
@Data
public class SubsidyMonthlyCustParam extends BaseParam {

    //主键
    private Long id;

    //关联用户ID
    private String custId;

    //客户名称
    private String custName;

    //是否集成商
    private String integrator;

    //补贴月份
    private String subsidyMonth;

    //总补贴金额
    private BigDecimal totalAmount;

    //电力用户补偿金额
    private BigDecimal consAmount;

    //负荷集成商补偿金额
    private BigDecimal aggregateAmount;

    //有效响应户次
    private Integer effectiveNum;

    //电力用户有效响应户次
    private Integer consEffectiveNum;

    //负荷集成商有效响应户次
    private Integer aggregateEffectiveNum;

    //状态(字典 0 未公示 1 已公示 2 已冻结)
    private String status;
}
