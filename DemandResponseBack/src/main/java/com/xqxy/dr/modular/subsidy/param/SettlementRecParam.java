package com.xqxy.dr.modular.subsidy.param;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xqxy.core.pojo.base.param.BaseParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * <p>
 * 响应补贴结算记录表
 * </p>
 *
 * @author hu xingxing
 * @since 2022-01-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dr_settlement_rec")
public class SettlementRecParam extends BaseParam {

    /**
     * 记录标识
     */
    private Long id;

    /**
     * 年份
     */
    private Integer year;

    /**
     * 结算批号,不能重复，自动生成格式：‘yyyyMMddhhmmssSSS’
     */
    private String settlementId;

    /**
     * 开始日期
     */
    private LocalDate beginDate;

    /**
     * 截至日期
     */
    private LocalDate endDate;

    /**
     * 总金额
     */
    private BigDecimal subsidyAmount;

    /**
     * 结算周期,依据开始结束日志自动生成
     */
    private String settlementDesc;

    /**
     * 结算状态: 1 待发放 2已发放
     */
    private String state;

    private Long projectId;
}
