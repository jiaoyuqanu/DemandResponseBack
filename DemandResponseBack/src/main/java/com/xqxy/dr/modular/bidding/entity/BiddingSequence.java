package com.xqxy.dr.modular.bidding.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 竞价序位
 * </p>
 *
 * @author Shen
 * @since 2021-10-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dr_bidding_sequence")
@ApiModel(value="BiddingSequence对象", description="竞价序位")
public class BiddingSequence implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "序位标识")
    @TableId(type = IdType.ASSIGN_ID)
    private Long sequenceId;

    @ApiModelProperty(value = "竞价公告标识")
    private Long noticeId;

    @ApiModelProperty(value = "用户标识")
    @TableField("CONS_ID")
    private String consId;

    @ApiModelProperty(value = "响应类型")
    @TableField("RESPONSE_TYPE")
    private String responseType;

    @ApiModelProperty(value = "时间类型")
    @TableField("TIME_TYPE")
    private String timeType;

    @ApiModelProperty(value = "提前通知时间")
    private String advanceNoticeTime;

    @ApiModelProperty(value = "响应时段开始时间")
    @TableField("START_TIME")
    private String startTime;

    @ApiModelProperty(value = "响应时段开始时间")
    @TableField("END_TIME")
    private String endTime;

    @ApiModelProperty(value = "签约价格")
    @TableField("CONTRACT_PRICE")
    private BigDecimal contractPrice;

    @ApiModelProperty(value = "最大容量")
    @TableField("CONTRACT_CAP")
    private BigDecimal contractCap;

    @ApiModelProperty(value = "方案序位")
    private Integer sequenceNo;

    @ApiModelProperty(value = "创建时间")
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建人")
    @TableField("CREATE_USER")
    private Long createUser;

    @ApiModelProperty(value = "更新时间")
    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "更新人")
    @TableField("UPDATE_USER")
    private Long updateUser;


}
