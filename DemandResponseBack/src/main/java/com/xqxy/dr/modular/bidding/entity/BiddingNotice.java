package com.xqxy.dr.modular.bidding.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 竞价公告
 * </p>
 *
 * @author Shen
 * @since 2021-10-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dr_bidding_notice")
@ApiModel(value="BiddingNotice对象", description="竞价公告")
public class BiddingNotice implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "竞价公告标识")
    @TableId(type = IdType.ASSIGN_ID)
    private Long noticeId;

    @ApiModelProperty(value = "公告编号")
    private String noticeNo;

    @ApiModelProperty(value = "公告名称")
    private String noticeName;

    @ApiModelProperty(value = "发布时间")
    private LocalDateTime releaseTime;

    @ApiModelProperty(value = "反馈截止时间")
    private LocalDateTime deadlineTime;

    @ApiModelProperty(value = "生效日期")
    private LocalDate effectDate;

    @ApiModelProperty(value = "失效日期")
    private LocalDate failureDate;

    @ApiModelProperty(value = "项目标识")
    private String projectId;

    @ApiModelProperty(value = "1保存，2已发布")
    private String state;

    @ApiModelProperty(value = "1:未提交，2：审核中，3：审核通过，4：审核不通过")
    private String checkStatus;

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
