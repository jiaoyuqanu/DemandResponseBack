package com.xqxy.dr.modular.evaluation.param;

import com.xqxy.core.pojo.base.param.BaseParam;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@ApiModel(description = "次日效果评估负荷 参数")
@EqualsAndHashCode(callSuper = true)
@Data
public class EventPowerExecuteParam extends BaseParam {

    /**
     * 本实体记录的唯一标识，产生规则为流水号
     */
    private Long dataId;

    /**
     * 关联用户ID
     */
    private String consId;

    /**
     * 关联事件ID
     */
    private Long eventId;

    /**
     * 数据日期
     */
    private LocalDate dataDate;

    /**
     * 数据点数标志	(1：96点2:	48点3:	24点)
     */
    private String dataPointFlag;
}
