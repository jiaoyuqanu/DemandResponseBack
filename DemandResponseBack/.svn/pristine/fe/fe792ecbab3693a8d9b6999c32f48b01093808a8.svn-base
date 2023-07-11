package com.xqxy.dr.modular.baseline.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.xqxy.core.pojo.base.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 基线负控参数
 * </p>
 *
 * @author chen zhi jun
 * @since 2021-10-18
 */
@Data
public class CityTargetSend extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 调度指令编号
     */
    @ApiModelProperty(value = "调度指令编号")
    private String planNo;

    /**
     * 调度指令名称
     */
    @ApiModelProperty(value = "调度指令名称")
    private String planName;

    /**
     * 事件名称
     */
    @ApiModelProperty(value = "事件名称")
    private String eventName;

    /**
     * 事件标识
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "事件标识")
    private Long eventId;

    /**
     * 目标负荷
     */
    @ApiModelProperty(value = "目标负荷")
    private BigDecimal regulateCap;

    /**
     * 应邀负荷
     */
    @ApiModelProperty(value = "应邀负荷")
    private BigDecimal demandCap;

    /**
     * 市级组织机构
     */
    @ApiModelProperty(value = "市级组织机构")
    private String orgId;

    /**
     * 1：需求响应，2：有序用电，3：两高轮停
     */
    private String type;

    /**
     * 调度日期
     */
    @ApiModelProperty(value = "调度日期")
    private String regulateDate;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    private String startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private String endTime;

    /**
     * 参与户数
     */
    @ApiModelProperty(value = "参与户数")
    private Integer countUser;

    private String orgName;
}
