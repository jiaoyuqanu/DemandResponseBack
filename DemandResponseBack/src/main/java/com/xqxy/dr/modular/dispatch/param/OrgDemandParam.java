package com.xqxy.dr.modular.dispatch.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrgDemandParam {

    @ApiModelProperty("调度指令id")
    private Long regulateId;
    @ApiModelProperty("调度指令分解详情")
    private List<OrgDemandInfo> orgDemandInfos;

    @Data
    public static class OrgDemandInfo {
        @ApiModelProperty("组织机构id")
        private String orgId;
        @ApiModelProperty("组织机构名称")
        private String orgName;
        @ApiModelProperty("指标")
        private BigDecimal goal;
    }

}
