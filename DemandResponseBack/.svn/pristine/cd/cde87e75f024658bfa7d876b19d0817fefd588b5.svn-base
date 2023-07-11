package com.xqxy.dr.modular.project.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xqxy.core.annotion.NeedSetValue;
import com.xqxy.core.pojo.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 需求响应项目
 * </p>
 *
 * @author PengChuqing
 * @since 2021-10-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dr_project")
@ApiModel(value="Project对象", description="需求响应项目")
public class Project  extends BaseEntity implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "项目标识")
    @TableId(type = IdType.ASSIGN_ID)
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

    /**
     * 时段数量
     */
    @TableField(exist = false)
    @NeedSetValue(beanClass = com.xqxy.dr.modular.project.service.ProjectDetailService.class, method = "getPeriodNum", params = {"projectId"}, targetField = "periodNum")
    private Integer periodNum;

    @TableField(exist = false)
    @NeedSetValue(beanClass = com.xqxy.dr.modular.project.service.ProjectDetailService.class, method = "getAdvanceNotice", params = {"projectId"}, targetField = "advanceNoticeTime")
    private String advanceNoticeTime;

    @TableField(exist = false)
    private ConsContractInfo consContractInfo;
}
