package com.xqxy.dr.modular.project.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 各单位任务指标
 * </p>
 *
 * @author liqirui
 * @since 2022-01-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DrOrgGoal implements Serializable {

private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 项目标识
     */
    @TableField("project_id")
    private Long projectId;

    /**
     * 项目明细标识
     */
    @TableField("project_detail_id")
    private Long projectDetailId;

    /**
     * 供电单位标识
     */
    @TableField("org_id")
    private String orgId;

    /**
     * 供电单位名称
     */
    @TableField("org_name")
    private String orgName;

    /**
     * 任务指标
     */
    @TableField("goal")
    private BigDecimal goal;

    /**
     * 年份
     */
    @TableField("year")
    private String year;

    /**
     * 审核通过 签约容量
     */
    @TableField(exist = false)
    private BigDecimal contractCap;

    /**
     * 审核中 签约容量
     */
    @TableField(exist = false)
    private BigDecimal contractCapUnderReview;

    /**
     *  审核通过 + 审核中 签约容量
     */
    @TableField(exist = false)
    private BigDecimal contractCapSum;

    /**
     * 签约完成率device-adjustable-base
     */
    @TableField(exist = false)
    private BigDecimal contractRatio;


}
