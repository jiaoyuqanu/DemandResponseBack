package com.xqxy.dr.modular.project.result;

import com.xqxy.core.pojo.base.entity.BaseEntity;
import com.xqxy.dr.modular.project.entity.ProjectDetail;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProjectInfo  extends BaseEntity implements Serializable {

    @ApiModelProperty(value = "项目标识")
    private Long projectId;

    @ApiModelProperty(value = "项目编号")
    private String projectNo;

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "项目类型，1价格型，2激励型")
    private String projectType;

    @ApiModelProperty(value = "项目目标，1削峰，2填谷，3消纳新能源")
    private String projectTarget;

    @ApiModelProperty(value = "开始日期")
    private LocalDate beginDate;

    @ApiModelProperty(value = "截至日期")
    private LocalDate endDate;

    @ApiModelProperty(value = "电价")
    private String elecPrice;

    @ApiModelProperty(value = "参与条件  [{type:1,value:1,2,3,4},....]")
    private String particiCondition;

    @ApiModelProperty(value = "激励标准 格式：1，2，3")
    private String incentiveStandard;

    @ApiModelProperty(value = "有效性判定,格式：1，2，3")
    private String validityJudgment;

    @ApiModelProperty(value = "基线计算准则 格式：1，2，3")
    private String baseLineCal;

    @ApiModelProperty(value = "1:新建，2:公示，3：执行中，4：已结束")
    private String state;

    @ApiModelProperty(value = "1:未提交，2：审核中，3：审核通过，4：审核不通过")
    private String checkStatus;

    @ApiModelProperty(value = "发布时间")
    private LocalDateTime pubTime;

    private List<ProjectDetail> projectDetails;

}
