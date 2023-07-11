package com.xqxy.dr.modular.baseline.param;

import com.baomidou.mybatisplus.annotation.TableField;
import com.xqxy.core.pojo.base.param.BaseParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@ApiModel(description = "基线管理参数")
@EqualsAndHashCode(callSuper = true)
@Data
public class BaseLineParam extends BaseParam {

    /**
     * 本实体记录的唯一标识，基线主键
     */
    @ApiModelProperty(value = "基线标识id")
    @NotNull(message = "baselinId不能为空，请检查baselinId参数", groups = {edit.class, delete.class, detail.class})
    @TableField("baselin_id")
    private Long baselinId;

    /**
     * 基线描述
     */
    @ApiModelProperty(value = "基线描述")
    @NotNull(message = "基线描述不能为空，请检查descr参数", groups = {add.class})
    @TableField("descr")
    private String descr;

    /**
     * 基线生成日：即首次需求响应日
     */
    @ApiModelProperty(value = "基线日期")
    @NotNull(message = "基线日期不能为空，请检查generateDate参数", groups = {add.class})
    @TableField("generate_date")
    private LocalDate generateDate;

    /**
     * 响应开始时段:hh:mm:ss
     */
    @ApiModelProperty(value = "响应开始时段:hh:mm")
    @TableField("start_period")
    private String startPeriod;

    /**
     * 响应结束时段:hh:mm:ss
     */
    @ApiModelProperty(value = "响应结束时段:hh:mm")
    @TableField("end_period")
    private String endPeriod;

    /**
     * 日期逗号隔开，20210101，20210102，20210103
     */
    @ApiModelProperty(value = "样本日期,逗号隔开如20210101，20210102")
    @NotNull(message = "样本日期不能为空，请检查simplesDate参数", groups = {add.class})
    @TableField("simples_date")
    private String simplesDate;

    /**
     * 计算规则1,2,3
     */
    @ApiModelProperty(value = "计算规则1,2,3")
    @NotNull(message = "计算规则不能为空，请检查calRule参数", groups = {add.class})
    @TableField("cal_rule")
    private String calRule;

    /**
     * 基线类型，1：时间段基线，2：全天基线
     */
    @ApiModelProperty(value = "基线类型，1：时间段基线，2：全天基线")
    @NotNull(message = "基线类型不能为空，请检查baselineType参数", groups = {add.class})
    @TableField("baseline_type")
    private String baselineType;


    @ApiModelProperty(value = "开始结束时间集")
    @NotNull(message = "开始结束时间集不能为空，请检查times参数", groups = {add.class})
    List<Map<String,Object>> times;


}
