package com.xqxy.dr.modular.powerplant.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.xqxy.core.pojo.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author czj
 * @since 2021-12-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DrDaVoidance extends BaseEntity implements Serializable {

private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 调峰日期
     */
    private LocalDate peakDate;

    /**
     * 调峰时段
     */
    private String peakTimeInterval;

    /**
     * 负荷聚合商组号
     */
    private String aggregatorNo;

    /**
     * 执行日期
     */
    private LocalDate executionDate;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 出清价格
     */
    private String voidancePrice;


}
