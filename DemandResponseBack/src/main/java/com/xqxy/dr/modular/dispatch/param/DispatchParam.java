package com.xqxy.dr.modular.dispatch.param;

import com.baomidou.mybatisplus.annotation.TableField;
import com.xqxy.core.pojo.base.param.BaseParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@ApiModel(description = "调度负荷参数")
@EqualsAndHashCode(callSuper = true)
@Data
public class DispatchParam extends BaseParam {

    /**
     * 主键
     */
    @ApiModelProperty(value = "需求标识")
    @NotNull(message = "regulateId不能为空，请检查regulateId参数", groups = {edit.class, delete.class, detail.class})
    @TableField("REGULATE_ID")
    private Long regulateId;

    /**
     * 负荷调度日期
     */
    @ApiModelProperty(value = "负荷调度日期")
    @NotNull(message = "regulateDate不能为空，请检查regulateDate参数", groups = {edit.class})
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
    @NotNull(message = "projectId不能为空，请检查projectId参数", groups = {edit.class})
    @TableField("project_id")
    private Long projectId;

    /**
     * 时间类型
     */
    @ApiModelProperty(value = "时间类型")
    @NotNull(message = "timeType不能为空，请检查timeType参数", groups = {edit.class})
    @TableField("TIME_TYPE")
    private String timeType;

    /**
     * 年份，查询条件
     */
    @ApiModelProperty(value = "年份，查询条件")
    private String year;


    /**
     * 目标区域（调控范围）
     */
   /* @ApiModelProperty(value = "目标区域数组英文")
    @TableField(exist = false)
    private List<List<String>> regulateRangeList;*/


}
