package com.xqxy.dr.modular.bidding.param;

import com.baomidou.mybatisplus.annotation.TableField;
import com.xqxy.core.pojo.base.param.BaseParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

@ApiModel(description = "竞价 参数")
@EqualsAndHashCode(callSuper = true)
@Data
public class BiddingNoticeParam extends BaseParam {

    private static final long serialVersionUID=1L;

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

}
