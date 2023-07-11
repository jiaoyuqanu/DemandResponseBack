package com.xqxy.dr.modular.evaluation.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.xqxy.core.pojo.base.entity.BaseEntity;
import lombok.Data;
import java.io.Serializable;

@Data
@TableName("dr_plan")
public class DrPlan extends BaseEntity implements Serializable {

    private static final long serialVersionUID=1L;

    private Long planID;

    private Long baselinId;

    private String regulateId;
}
