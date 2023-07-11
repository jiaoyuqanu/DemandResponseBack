package com.xqxy.dr.modular.project.params;

import com.xqxy.core.pojo.base.param.BaseParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@ApiModel(description = "需求响应项目 参数")
@EqualsAndHashCode(callSuper = true)
@Data
public class ProjectParam extends BaseParam {

    @ApiModelProperty(value = "项目标识")
    @NotNull(message = "projectNo不能为空，请检查projectNo参数", groups = {delete.class,detail.class})
    private Long projectId;

    @ApiModelProperty(value = "项目编号")
    @NotNull(message = "projectNo不能为空，请检查projectNo参数", groups = {add.class})
    private String projectNo;

    @ApiModelProperty(value = "项目名称")
    @NotNull(message = "projectName不能为空，请检查projectName参数", groups = {add.class})
    private String projectName;

    @ApiModelProperty(value = "项目类型，1价格型，2激励型")
    @NotNull(message = "projectType不能为空，请检查projectType参数", groups = {add.class})
    private String projectType;

    @ApiModelProperty(value = "项目目标，1削峰，2填谷，3消纳新能源")
    @NotNull(message = "projectTarget不能为空，请检查projectTarget参数", groups = {add.class})
    private String projectTarget;

    @ApiModelProperty(value = "开始日期")
    @NotNull(message = "beginDate不能为空，请检查beginDate参数", groups = {add.class})
    private LocalDate beginDate;

    @ApiModelProperty(value = "截至日期")
    @NotNull(message = "endDate不能为空，请检查endDate参数", groups = {add.class})
    private LocalDate endDate;

    @ApiModelProperty(value = "电价")
    // @NotNull(message = "elecPrice不能为空，请检查elecPrice参数", groups = {add.class})
    private String elecPrice;

    @ApiModelProperty(value = "参与条件  [{type:1,value:1,2,3,4},....]")
    @NotNull(message = "particiCondition不能为空，请检查particiCondition参数", groups = {add.class})
    private String particiCondition;

    @ApiModelProperty(value = "激励标准 格式：1，2，3")
    @NotNull(message = "incentiveStandard不能为空，请检查incentiveStandard参数", groups = {add.class})
    private String incentiveStandard;

    @ApiModelProperty(value = "有效性判定,格式：1，2，3")
    @NotNull(message = "validityJudgment不能为空，请检查validityJudgment参数", groups = {add.class})
    private String validityJudgment;

    @ApiModelProperty(value = "基线计算准则 格式：1，2，3")
    @NotNull(message = "baseLineCal不能为空，请检查baseLineCal参数", groups = {add.class})
    private String baseLineCal;

    @ApiModelProperty(value = "1:新建，2:公示，3：执行中，4：已结束")
    private String state;

    @ApiModelProperty(value = "1:未提交，2：审核中，3：审核通过，4：审核不通过")
    private String checkStatus;

    @ApiModelProperty(value = "发布时间")
    private LocalDateTime pubTime;

    @ApiModelProperty(value = "提前通知时间 单位分钟")
    private Integer advanceNoticeTime;

    @ApiModelProperty(value = "时段信息")
    @NotNull(message = "projectDetailList不能为空，请检查projectDetailList参数", groups = {add.class})
    private List<ProjectDetailParam> projectDetailList;


    // @NotNull(message = "timeType不能为空，请检查timeType参数", groups = {add.class})
    @ApiModelProperty(value = "时间类型")
    private String timeType;

    private String consId;

}
