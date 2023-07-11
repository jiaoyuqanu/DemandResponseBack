package com.xqxy.dr.modular.bidding.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 竞价明细
 * </p>
 *
 * @author Shen
 * @since 2021-10-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dr_bidding_detail")
@ApiModel(value="BiddingDetail对象", description="竞价明细")
public class BiddingDetail implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "明细标识")
      @TableId("DETAIL_ID")
    private Long detailId;

    @ApiModelProperty(value = "竞价公告标识")
    private Long noticeId;

    @ApiModelProperty(value = "响应类型")
    @TableField("RESPONSE_TYPE")
    private String responseType;

    @ApiModelProperty(value = "时间类型")
    @TableField("TIME_TYPE")
    private String timeType;

    @ApiModelProperty(value = "响应时段开始时间")
    @TableField("START_TIME")
    private String startTime;

    @ApiModelProperty(value = "响应时段开始时间")
    @TableField("END_TIME")
    private String endTime;

    @ApiModelProperty(value = "提前通知时间")
    private String advanceNoticeTime;

    @ApiModelProperty(value = "单位类型 1元/千瓦，2元/千瓦时")
    private String unit;

    @ApiModelProperty(value = "招标总量")
    private Double totalCap;

    @ApiModelProperty(value = "补偿价格上限")
    private Double maxPrice;

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
