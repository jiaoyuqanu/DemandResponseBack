package com.xqxy.dr.modular.bidding.result;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.xqxy.dr.modular.bidding.entity.BiddingDetail;
import com.xqxy.dr.modular.bidding.entity.BiddingRange;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class BiddingNoticeInfo {

    @ApiModelProperty(value = "竞价公告标识")
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

    /**
     * 竞价明细
     */
    private List<BiddingDetail> biddingDetailList;

    /**
     * 竞价发布范围
     */
    private List<BiddingRange> biddingRangeList;
}
