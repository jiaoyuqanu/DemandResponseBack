package com.xqxy.dr.modular.subsidy.param;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.xqxy.core.pojo.base.param.BaseParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@ApiModel(description = "客户日补贴 参数")
@EqualsAndHashCode(callSuper = true)
@Data
public class CustSubsidyDailyParam extends BaseParam {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "关联用户ID")
    private Long custId;

    @ApiModelProperty(value = "是否集成商 字典： 1是，0 否")
    private String integrator;

    @ApiModelProperty(value = "补贴日期")
    private LocalDate subsidyDate;

    // 开始日期
    private LocalDate subsidyStartDate;

    // 结束日期
    private LocalDate subsidyEndDate;

    @ApiModelProperty(value = "结算金额")
    private BigDecimal settledAmount;

    @ApiModelProperty(value = "状态(字典 0 待确认 1 复核中 2 已确认)")
    private String status;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建人")
    private Long createUser;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "更新人")
    private Long updateUser;

    @ApiModelProperty(value = "计算时间")
    private LocalDateTime subTime;

    @ApiModelProperty(value = "计算状态")
    private String subStatus;

    @ApiModelProperty(value = "计算异常 描述")
    private String subException;

    @ApiModelProperty(value = "事件id,多个逗号隔开")
    private String eventIds;

    @ApiModelProperty(value = "结算批号")
    private String settlementNo;

    @ApiModelProperty(value = "事件次数")
    private int eventNum;

    private Long projectId;
}
