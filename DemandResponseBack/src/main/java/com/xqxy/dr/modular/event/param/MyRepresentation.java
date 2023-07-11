package com.xqxy.dr.modular.event.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MyRepresentation {
    @ApiModelProperty(value = "dr_subsidy_appeal主键")
    private String drSubsidyAppealId;

    @ApiModelProperty(value = "区分页面：不为空就是;用户侧user,市侧city,省侧province,能源局energy")
    private String distinguish;

    @ApiModelProperty(value = "市专责审批意见")
    private String examineSuggestionCity;

    @ApiModelProperty(value = "省机构审批意见")
    private String examineSuggestionProvince;

    @ApiModelProperty(value = "能源局审批意见")
    private String examineSuggestionEnergy;


    @ApiModelProperty(value = "市专责提交时间")
    private String submitCityTime;

    @ApiModelProperty(value = "省专机构提交时间")
    private String submitProvinceTime;

    @ApiModelProperty(value = "能源局提交时间")
    private String submitEnergyTime;

    @ApiModelProperty(value = "核对文件id")
    private String checkIds;

    @ApiModelProperty(value = "关联文件名称")
    private String filesName;

    //核对文件id
    @ApiModelProperty(value = "核对文件名称")
    private String checkName;


    @ApiModelProperty(value = "关联文件ID")
    private String fileIds;


    //核定负荷
    @ApiModelProperty(value = "核定负荷")
    private String remark;

    @ApiModelProperty(value = "申诉原因（字典1基线最大负荷异议 2基线平均负荷异议 3响应效果认定异议）")
    private String appealReason;
}
