package com.xqxy.dr.modular.newloadmanagement.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class DemandEvaluationParam {

    @ApiModelProperty("组织机构id")
    private String orgId;

    /**
     * 根据flag决定是否必传
     */
    @ApiModelProperty("日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date queryDate;

    /**
     * 根据flag决定是否必传
     */
    @ApiModelProperty("开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startTime;

    /**
     * 根据flag决定是否必传
     */
    @ApiModelProperty("结束日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endTime;

    @ApiModelProperty("单日或多日累计: 1,当日 2,多日")
    private Integer flag;

}
