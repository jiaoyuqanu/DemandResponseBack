package com.xqxy.dr.modular.project.params;

import com.baomidou.mybatisplus.annotation.TableField;
import com.xqxy.core.pojo.base.param.BaseParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@ApiModel(description = "需求响应项目 参数")
@EqualsAndHashCode(callSuper = true)
@Data
public class ConsConstractInfoParam   extends BaseParam {

    @ApiModelProperty(value = "申报标识")
    private Long contractId;

    @ApiModelProperty(value = "用户标识")
    @TableField("CONS_ID")
    private String consId;

    @ApiModelProperty(value = "项目标识")
    private Long projectId;

    @ApiModelProperty(value = "1直接参与，2代理参与")
    private String particType;

    @ApiModelProperty(value = "分成比例")
    @TableField("EXTRACT_RATIO")
    private BigDecimal extractRatio;

    @ApiModelProperty(value = "第一联系人")
    private String firstContactName;

    @ApiModelProperty(value = "第一联系人联系方式")
    private String firstContactInfo;

    @ApiModelProperty(value = "第二联系人姓名")
    private String secondContactName;

    @ApiModelProperty(value = "第二联系人联系方式")
    private String secondContactInifo;

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

    @ApiModelProperty(value = "1:未提交，2：审核中，3：审核通过，4：审核不通过")
    private String checkStatus;

    @ApiModelProperty(value = "1 保存  2 已签约 3撤销")
    private String status;

}
