package com.xqxy.dr.modular.subsidy.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.xqxy.core.annotion.NeedSetValue;
import com.xqxy.core.pojo.base.entity.BaseEntity;
import com.xqxy.sys.modular.cust.entity.Cons;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 日补贴
 * </p>
 *
 * @author Shen
 * @since 2021-10-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dr_cons_subsidy_daily")
@ApiModel(value="ConsSubsidyDaily对象", description="日补贴")
public class ConsSubsidyDaily extends BaseEntity implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty(value = "关联用户ID")
    private String consId;

    @ApiModelProperty(value = "补贴日期")
    private LocalDate subsidyDate;

    @ApiModelProperty(value = "结算金额")
    private BigDecimal settledAmount;

    @ApiModelProperty(value = "状态(字典 0 待确认 1 复核中 2 已确认)")
    private String status;

    @ApiModelProperty(value = "计算时间")
    private LocalDateTime subTime;

    @ApiModelProperty(value = "计算状态")
    private String subStatus;

    @ApiModelProperty(value = "计算异常 描述")
    private String subException;

    @ApiModelProperty(value = "计算规则, 1:湖南老版规则，2：新版规则")
    private String cacRule;

    @ApiModelProperty(value = "事件id,多个逗号隔开")
    private String eventIds;

    @ApiModelProperty(value = "结算批号")
    private String settlementNo;

    @ApiModelProperty(value = "事件次数")
    private int eventNum;

    @ApiModelProperty(value = "申诉前结算金额")
    private BigDecimal settledAmountOld;

    @ApiModelProperty(value = "是否申诉过")
    private String isAppeal;

    /**
     * 用户信息
     */
    @TableField(exist = false)
    private Cons cons;

    @ApiModelProperty(value = "项目标识")
    private Long projectId;

    /**
     * 用户信息
     */
    @TableField(exist = false)
    private Long eventId;


}
