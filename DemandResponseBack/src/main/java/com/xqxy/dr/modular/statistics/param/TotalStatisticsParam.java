package com.xqxy.dr.modular.statistics.param;

import com.xqxy.core.pojo.base.param.BaseParam;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@ApiModel(description = "统计 参数")
@EqualsAndHashCode(callSuper = true)
@Data
public class TotalStatisticsParam extends BaseParam {

    private String year;//年份

    private Long projectId;//项目id

    private String orgId;//组织机构id
}
