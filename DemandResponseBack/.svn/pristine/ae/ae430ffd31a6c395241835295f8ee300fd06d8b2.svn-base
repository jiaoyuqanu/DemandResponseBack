package com.xqxy.dr.modular.bidding.entity;

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
 * 竞价发布范围
 * </p>
 *
 * @author Shen
 * @since 2021-10-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dr_bidding_range")
@ApiModel(value="BiddingRange对象", description="竞价发布范围")
public class BiddingRange implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "范围标识")
    private Long id;

    @ApiModelProperty(value = "竞价公告标识")
    private Long noticeId;

    @ApiModelProperty(value = "范围类别：1地区/2分区/3变电站/4线路/5台区")
    private String rangeType;

    @ApiModelProperty(value = "范围")
    private String rangeIds;

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
