package com.xqxy.dr.modular.dispatch.param;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.xqxy.core.pojo.base.param.BaseParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@ApiModel(description = "调度负荷参数")
@EqualsAndHashCode(callSuper = true)
@Data
public class DispatchEditorParam extends BaseParam {

    /**
     * 本实体记录的唯一标识，产生规则为流水号
     */
    @ApiModelProperty(value = "需求标识")
    @NotNull(message = "regulateId不能为空，请检查regulateId参数", groups = {edit.class, delete.class, detail.class})
    @TableField("REGULATE_ID")
    private Long regulateId;

    /**
     * 负荷调度日期
     */
    @ApiModelProperty(value = "负荷调度日期")
    @TableField("REGULATE_DATE")
    private LocalDate regulateDate;

    /**
     * 区域类型（调控范围类别）
     */
    @ApiModelProperty(value = "区域类型")
    @TableField("RANGE_TYPE")
    private String rangeType;

    /**
     * 目标区域（调控范围）
     */
    @ApiModelProperty(value = "目标区域")
    @TableField("REGULATE_RANGE")
    private String regulateRange;

    /**
     * 所属项目
     */
    @ApiModelProperty(value = "所属项目")
    @NotNull(message = "project_id，请检查projectId参数", groups = {edit.class})
    @TableField("project_id")
    private Long projectId;


    /**
     * 响应开始时段:
     */
    @ApiModelProperty(value = "开始时间")
    @TableField("START_TIME")
    private String startTime;

    /**
     * 响应结束时段:
     */
    @ApiModelProperty(value = "结束时间")
    @TableField("END_TIME")
    private String endTime;

    /**
     * 响应类型
     */
    @ApiModelProperty(value = "调节类型")
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
     * 状态(0未下发 1已下发)
     */
    @ApiModelProperty(value = "是否下发01否03是")
    @TableField("status")
    private String status;


}
