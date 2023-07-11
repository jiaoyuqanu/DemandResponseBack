package com.xqxy.dr.modular.newloadmanagement.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description
 * @Author Rabbit
 * @Date 2022/6/10 10:20
 */
@Data
@ApiModel(description = "未执行到位用户清单")
public class UserListVo {
    @ApiModelProperty(value = "基准日负荷")
    private String baseLoad;
    @ApiModelProperty(value = "完成率")
    private String completeRate;
    @ApiModelProperty(value = "用户名称")
    private String consName;
    @ApiModelProperty(value = "用户编号")
    private String consNo;
    @ApiModelProperty(value = "客户联系方式")
    private String contactInfo;
    @ApiModelProperty(value = "客户联系人")
    private String enterpriseUser;
    @ApiModelProperty(value = "需求响应用电指标")
    private String indexValue;
    @ApiModelProperty(value = "是否执行(0 未执行,1 已执行)")
    private String isExecute = "2";
    @ApiModelProperty(value = "供电公司联系方式")
    private String orgInfo;
    @ApiModelProperty(value = "供电公司联系人")
    private String orgUser;
    @ApiModelProperty(value = "实际负荷")
    private String realLoad;
    @ApiModelProperty(value = "错避峰负荷")
    private String regulationLoad;
}
