package com.xqxy.dr.modular.subsidy.param;

import com.xqxy.core.pojo.base.param.BaseParam;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * <p>
 * 用户补贴发放 参数
 * </p>
 *
 * @author hu xingxing
 * @since 2021-10-20 15:20
 */
@ApiModel(description = "用户补贴发放")
@EqualsAndHashCode(callSuper = true)
@Data
public class ConsSubsidyPayParam extends BaseParam {

    //发放标识
    private Long payId;

    //用户标识
    private String consId;

    //用户名称
    private String consName;

    //供电单位
    private String orgNo;

    //激励费用批号
    private String payNo;

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
}
