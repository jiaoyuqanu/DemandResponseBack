package com.xqxy.dr.modular.dispatch.param;

import com.baomidou.mybatisplus.annotation.TableField;
import com.xqxy.core.pojo.base.param.BaseParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@ApiModel(description = "调度负荷时段 参数")
@EqualsAndHashCode(callSuper = true)
@Data
public class DispatchSoltParam extends BaseParam {

    /**
     * 响应开始时段:
     */
    @ApiModelProperty(value = "开始时间")
    @NotNull(message = "startTime不能为空，请检查startTime参数", groups = {add.class})
    @TableField("START_TIME")
    private String startTime;

    /**
     * 响应结束时段:
     */
    @ApiModelProperty(value = "结束时间")
    @NotNull(message = "endTime不能为空，请检查endTime参数", groups = {add.class})
    @TableField("END_TIME")
    private String endTime;

    /**
     * 响应类型
     */
    @ApiModelProperty(value = "调节类型")
    @NotNull(message = "responseType不能为空，请检查responseType参数", groups = {add.class})
    @TableField("RESPONSE_TYPE")
    private String responseType;

    /**
     * 时间类型
     */
    @ApiModelProperty(value = "时间类型")
    @NotNull(message = "timeType不能为空，请检查timeType参数", groups = {add.class})
    @TableField("TIME_TYPE")
    private String timeType;


    /**
     * 调节目标值
     */
    @ApiModelProperty(value = "预测供电缺口")
    @NotNull(message = "regulateCap不能为空，请检查regulateCap参数", groups = {add.class})
    @TableField("REGULATE_CAP")
    private BigDecimal regulateCap;

    /**
     * 提前通知时间(分钟)
     */
    @ApiModelProperty(value = "提前通知时间(分钟)")
    @NotNull(message = "advanceNoticeTime不能为空，请检查advanceNoticeTime参数", groups = {add.class})
    @TableField("ADVANCE_NOTICE_TIME")
    private BigDecimal advanceNoticeTime;

    /**
     * 状态(01保存，02事件已生成，03已下发)
     */
    @ApiModelProperty(value = "01保存，02事件已生成，03已下发")
    @TableField("status")
    private String status;

}
