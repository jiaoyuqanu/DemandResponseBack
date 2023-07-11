package com.xqxy.dr.modular.gwapp.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class GwappCustPageVo {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("法定代表人姓名")
    @TableField("legal_name")
    private String legalName;


    @ApiModelProperty("法定代表人号码")
    @TableField("legal_no")
    private String legalNo;

    @ApiModelProperty("注册手机号")
    @TableField("tel")
    private String tel;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty("1:未提交，2：审核中，3：审核通过，4：审核不通过")
    private String checkStatus;

    @ApiModelProperty("审批人")
    @TableField("approval_user")
    private Integer approvalUser;

    @ApiModelProperty("审批意见")
    @TableField("approval_comments")
    private String approvalComments;


}
