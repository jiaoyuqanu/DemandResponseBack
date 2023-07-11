package com.xqxy.dr.modular.newloadmanagement.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @Description
 * @Author Rabbit
 * @Date 2022/6/20 17:05
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "需求响应执行事件及负荷执行率信息")
public class EventLoadVo {
    @ApiModelProperty(value = "组织机构ID")
    private String orgId;
    @ApiModelProperty(value = "应邀负荷")
    private BigDecimal invitedLoad;
    @ApiModelProperty(value = "应邀户数")
    private Integer invitedCons;
    @ApiModelProperty(value = "应邀率")
    private BigDecimal invitedRate;
    @ApiModelProperty(value = "实时负荷")
    private BigDecimal realLoad;
    @ApiModelProperty(value = "执行率")
    private BigDecimal executeRate;
    @ApiModelProperty(value = "邀约负荷")
    private BigDecimal invitationLoad;
    @ApiModelProperty(value = "邀约户数")
    private Integer invitationCons;
    @ApiModelProperty(value = "负荷执行时段  示例：08:00-12:00")
    private String executeTime;
}
