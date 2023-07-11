package com.xqxy.dr.modular.subsidy.param;

import com.xqxy.core.pojo.base.param.BaseParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

@ApiModel(description = "用户补贴申诉 参数")
@EqualsAndHashCode(callSuper = true)
@Data
public class SubsidyAppealParam extends BaseParam {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "关联评估ID")
    private Long evaluationId;

    @ApiModelProperty(value = "申诉原因（字典1基线最大负荷异议 2基线平均负荷异议 3响应效果认定异议）")
    private String appealReason;

    @ApiModelProperty(value = "原因说明")
    private String remark;

    @ApiModelProperty(value = "用户侧状态(1 保存  2受理中 3撤回 4驳回(市级) 5申诉成功(能源局) 6申诉失败(省级或能源局))")
    private String status;

    @ApiModelProperty(value = "市区状态(1 未审批  2审批中 3撤回 4修正完成  5审批通过(能源局) 6审批失败(省级或能源局))")
    private String statusCity;

    @ApiModelProperty(value = "省层次状态(1 未审批  2审批中 3审批失不通过省级或能源局) 4审批通过(能源局))")
    private String statusProvince;

    @ApiModelProperty(value = "能源局状态(1 未审批  2审批不通过(能源局) 3审批通过(能源局))")
    private String statusEnergy;

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



    @ApiModelProperty(value = "关联文件名称")
    private String filesName;

    //核对文件id
    @ApiModelProperty(value = "核对文件名称")
    private String checkName;


    @ApiModelProperty(value = "关联文件ID")
    private String fileIds;

	@ApiModelProperty(value = "核对文件ID")
    private String checkIds;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建人")
    private Long createUser;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "更新人")
    private Long updateUser;

    @ApiModelProperty(value = "市专责审批人")
    private String cityUser;

    @ApiModelProperty(value = "省级审批人")
    private String provinceUser;

    @ApiModelProperty(value = "能源局审批人")
    private String energyUser;

    // 用户标识
    private String consId;

    private Long eventId;

    // 客户标识
    private Long custId;

    // 用户标识集合
    private List<Long> consIdList;

    //用户权限下机构集合
    private List<String> orgNos;

    //用户名称
    private String consName;

    //开始时间
    private String startTime;

    //结束时间
    private String endTime;

    public boolean isDesc(){
        if("asc".equals(this.getOrder())){
            return false;
        }
        return true;
    }
}
