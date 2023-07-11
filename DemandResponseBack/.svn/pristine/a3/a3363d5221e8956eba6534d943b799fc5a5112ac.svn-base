package com.xqxy.dr.modular.statistics.result;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@TableName("dr_total_static")
public class TotalStatisticsTableResult implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty(value = "组织编码id")
    @TableField("org_id")
    private String orgId;

    @ApiModelProperty(value = "组织编码")
    @TableField("org_name")
    private String orgName;

    @ApiModelProperty(value = "累计执行户次")
    @TableField("total_cons_count")
    private Integer consCount;

    @ApiModelProperty(value = "累计执行天数")
    @TableField("total_day_count")
    private Integer eventDays;

    @ApiModelProperty(value = "累计压降负荷")
    @TableField("total_actual_cap")
    private BigDecimal actualCapSum;

    @ApiModelProperty(value = "累计影响电量")
    @TableField("total_energy")
    private BigDecimal actualEnergySum;

    @ApiModelProperty(value = "最大压降负荷")
    @TableField("max_actual_cap")
    private BigDecimal actualCapMax;

    @ApiModelProperty(value = "最大压降时影响电量")
    @TableField("max_actual_energy")
    private BigDecimal actualEnergyMax;

    @ApiModelProperty(value = "最大压降时执行日期")
    @TableField("max_actual_day")
    private String regulateDate;

    @ApiModelProperty(value = "最大压降时电力缺口")
    @TableField("max_actual_target")
    private BigDecimal regulateCap;

    @ApiModelProperty(value = "年份")
    @TableField("year")
    private String year;

    @ApiModelProperty(value = "项目id")
    @TableField("project_id")
    private Long projectId;

    @ApiModelProperty(value = "项目名称")
    @TableField(exist = false)
    private String projectName;

}
