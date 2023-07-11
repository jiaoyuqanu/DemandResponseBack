package com.xqxy.dr.modular.powerplant.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xqxy.core.pojo.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author Caoj
 * @since 2021-12-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dr_da_market_voidance_price")
public class DaMarketVoidancePrice extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 出清价格唯一标识
     */
    private Long id;

    /**
     * 集成商标识
     */
    private Long custId;

    /**
     * 集成商编号
     */
    private Long custNo;

    /**
     * 调峰日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date pickLoadDate;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 基线负荷
     */
    private BigDecimal baselineLoad;

    /**
     * 实际负荷
     */
    private BigDecimal realLoad;

    /**
     * 计划响应负荷
     */
    private BigDecimal planResponseLoad;

    /**
     * 实际响应负荷
     */
    private BigDecimal realResponseLoad;

    /**
     * 实际响应电量
     */
    private BigDecimal realResponseElect;

    /**
     * 出清价格
     */
    private BigDecimal voidancePrice;

    /**
     * 1:未提交，2：审核中，3：审核通过，4：审核不通过
     */
    private String state;
}
