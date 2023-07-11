package com.xqxy.dr.modular.subsidy.param;

import com.xqxy.core.pojo.base.param.BaseParam;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * <p>
 * 客户补贴发放 参数
 * </p>
 *
 * @author hu xingxing
 * @since 2021-10-20 15:30
 */
@ApiModel(description = "客户补贴发放")
@EqualsAndHashCode(callSuper = true)
@Data
public class CustSubsidyPayParam extends BaseParam {

    //发放标识
    private Long payId;

    //激励费用批号
    private String payNo;

    //集成商名称
    private String legalName;

    //统一社会信用代码
    private String creditCode;

    //是否集成商
    private String integrator;

    //开始日期
    private LocalDate beginDate;

    //截至日期
    private LocalDate endDate;

    //激励金额
    private BigDecimal subsidyAmount;

    //参与次数
    private Integer particNum;

    //发放状态: 0 待发放 1 发放中 2 已发放
    private String payStatus;

    //客户标识
    private Long custId;
}
