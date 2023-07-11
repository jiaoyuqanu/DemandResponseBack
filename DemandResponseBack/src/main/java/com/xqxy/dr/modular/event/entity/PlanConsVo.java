package com.xqxy.dr.modular.event.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xqxy.core.pojo.base.entity.BaseEntity;
import com.xqxy.sys.modular.cust.result.ConsResult;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 方案参与用户
 * </p>
 *
 * @author PengChuqing
 * @since 2021-10-09
 */
@Data
public class PlanConsVo extends BaseEntity implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 用户标识
     */
    private String consId;

    /**
     * 基线负荷标识
     */
    private Long baselineId;

    /**
     * 可响应负荷
     */
    private BigDecimal demandCap;

    /**
     * 签约负荷
     */
    private BigDecimal demandSingCap;

    /**
     * 用户名称
     */
    private String consName;

    /**
     * 事件标识
     */
    private Long eventId;

    /**
     * 事件名称
     */
    private String eventName;

    /**
     * 调度指令编号
     */
    private String planNo;

    /**
     * 是否参与事件
     */
    private String planName;

    /**
     * 原始机构
     */
    private String orgId;

    /**
     * 市级机构
     */
    private String orgNo;

    /**
     * 1：需求响应，2：有序用电，3：两高轮停
     */
    private String type;

    private Long planId;

}
