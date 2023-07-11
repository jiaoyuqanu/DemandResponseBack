package com.xqxy.dr.modular.powerplant.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.xqxy.core.pojo.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 负荷聚合商信息表
 * </p>
 *
 * @author liqirui
 * @since 2021-12-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DrDaAggregator extends BaseEntity implements Serializable {

private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 负荷聚合商组号
     */
    private String aggregatorNo;

    /**
     * 负荷聚合商名称
     */
    private String aggregatorName;

    /**
     * 省码
     */
    private String provinceCode;

    /**
     * 省名称
     */
    private String provinceName;

    /**
     * 市码
     */
    private String cityCode;

    /**
     * 市名称
     */
    private String cityName;

    /**
     * 区码
     */
    private String countyCode;

    /**
     * 区名称
     */
    private String countyName;

    /**
     * 供电单位编码
     */
    private String orgNo;

    /**
     * 供电单位名称
     */
    private String orgName;

    /**
     * 统一社会信用代码
     */
    private String creditCode;

    /**
     * 联系人名称
     */
    private String contactName;

    /**
     * 联系人电话
     */
    private String contactPhone;

    /**
     * 审核状态 1.未审核 2.审核通过 3. 审核不通过
     */
    private String status;

    /**
     * 基线负荷
     */
    private BigDecimal baselineLoad;

    /**
     * 计划响应负荷
     */
    private BigDecimal planResponseLoad;

    /**
     * 实际响应负荷
     */
    private BigDecimal replyLoad;

}
