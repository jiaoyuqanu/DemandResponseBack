package com.xqxy.dr.modular.project.VO;

import com.xqxy.dr.modular.project.entity.DrOrgGoal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DrOrgGoalVO {
    /**
     * 目标负荷
     */
    private BigDecimal targerCup;
    /**
     * 签约负荷
     */
    private BigDecimal provinceContractSum;

    /**
     * 任务指标集合
     */
    private List<DrOrgGoal> orgGoalList;

}
