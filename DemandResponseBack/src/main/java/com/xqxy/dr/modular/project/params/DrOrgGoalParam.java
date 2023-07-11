package com.xqxy.dr.modular.project.params;


import com.xqxy.dr.modular.project.entity.DrOrgGoal;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@ApiModel(description = "指标分解 参数")
@Data
public class DrOrgGoalParam {

    /**
     * 项目标识
     */
    private BigDecimal targerCup;

    /**
     * 项目标识
     */
    private Long projectId;

    /**
     * 项目明细标识
     */
    private Long projectDetailId;

    /**
     * 供电单位标识
     */
    private String orgId;

    /**
     * 供电单位名称
     */
    private String orgName;

    /**
     * 任务指标集合
     */
    private List<DrOrgGoal> orgGoalList;


}
