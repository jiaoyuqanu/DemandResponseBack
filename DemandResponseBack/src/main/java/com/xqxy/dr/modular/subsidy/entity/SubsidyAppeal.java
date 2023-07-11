package com.xqxy.dr.modular.subsidy.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import com.xqxy.core.annotion.NeedSetValue;
import com.xqxy.core.pojo.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 补贴申诉
 * </p>
 *
 * @author Shen
 * @since 2021-10-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dr_subsidy_appeal")
@ApiModel(value = "SubsidyAppeal对象", description = "补贴申诉")
public class SubsidyAppeal extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty(value = "关联评估ID")
    private Long evaluationId;

    @ApiModelProperty(value = "申诉原因（字典1基线最大负荷异议 2基线平均负荷异议 3响应效果认定异议）")
    private String appealReason;

    //原因
    @ApiModelProperty(value = "原因说明")
    private String remark;

    //状态
    @ApiModelProperty(value = "状态(1 保存  2受理中 3撤回 4驳回(市级) 5申诉成功(能源局) 6申诉失败(省级或能源局))")
    private String status;

    @ApiModelProperty(value = "状态(1 未审批（展示处理按钮）  2审批中(二级页面提交后) 3撤回(提交后展示撤回按钮) 4修正完成(数据修正后)  5审批通过(能源局) 6审批失败(省级或能源局))")
    private String statusCity;

    @ApiModelProperty(value = "状态(1 未审批  2审批中 3审批失不通过省级或能源局) 4审批通过(能源局))")
    private String statusProvince;

    @ApiModelProperty(value = "状态(1 未审批  2审批不通过(能源局) 3审批通过(能源局))")
    private String statusEnergy;

    @ApiModelProperty(value = "市专责审批意见")
    private String examineSuggestionCity;

    @ApiModelProperty(value = "省机构审批意见")
    private String examineSuggestionProvince;

    @ApiModelProperty(value = "能源局审批意见")
    private String examineSuggestionEnergy;

    @ApiModelProperty(value = "市专责审批人")
    @TableField("user_city")
    private String cityUser;

    @ApiModelProperty(value = "省专责审批人")
    @TableField("user_province")
    private String provinceUser;

    @ApiModelProperty(value = "能源局审批人")
    @TableField("user_energy")
    private String energyUser;


    @ApiModelProperty(value = "市专责提交时间")
    private String submitCityTime;

    @ApiModelProperty(value = "省专机构提交时间")
    private String submitProvinceTime;

    @ApiModelProperty(value = "能源局提交时间")
    private String submitEnergyTime;    //文件id
    @ApiModelProperty(value = "关联文件ID")
    private String fileIds;

    //核对文件id
    @ApiModelProperty(value = "核对文件ID")
    private String checkIds;

    //文件名称
    @TableField(exist = false)
    @NeedSetValue(beanClass = com.xqxy.dr.modular.subsidy.service.SubsidyAppealService.class, method = "getFileName", params = {"fileIds"}, targetField = "fileName")
    private String fileName;

    @ApiModelProperty(value = "关联文件名称")
    private String filesName;

    //核对文件id
    @ApiModelProperty(value = "核对文件名称")
    @TableField("check_name")
    private String checkName;

    //用户户号
    @ApiModelProperty(value = "户号")
    private String consId;

    //用户户号
    @ApiModelProperty(value = "事件标识")
    private Long eventId;

    //用户名称
    @TableField(exist = false)
    private String consName;

    //联系方式
    @TableField(exist = false)
    private String phone;

    //事件编号
    @TableField(exist = false)
    private String eventNo;

    //事件名称
    @TableField(exist = false)
    private String eventName;

    //是否有效
    @TableField(exist = false)
    private String isEffective;

    //邀约响应量
    @TableField(exist = false)
    private String invitationCap;

    //核定负荷
    @TableField(exist = false)
    private String confirmCap;

    //重算后实际响应负荷
    @TableField(exist = false)
    private String recalculateActualCap;

    //重算后核定负荷
    @TableField(exist = false)
    private String recalculateConfirmCap;

    //重算后是否有效
    @TableField(exist = false)
    private String recalculateIsEffective;

    //基线最大负荷
    @TableField(exist = false)
    private String maxLoadBaseline;

    //实际最大负荷
    @TableField(exist = false)
    private String maxLoadActual;

    //基线平均负荷
    @TableField(exist = false)
    private String avgLoadBaseline;

    //实际平均负荷
    @TableField(exist = false)
    private String avgLoadActual;

    //基线最小负荷
    @TableField(exist = false)
    private String minLoadBaseline;

    //实际最小负荷
    @TableField(exist = false)
    private String minLoadActual;

    //核对文件id
    @ApiModelProperty(value = "是否同步数据Y同步N未同步")
    private String isAppeal;

    //最终审批意见(展示优先级 能源局>省>市)
    @TableField(exist = false)
    private String finalSuggestion;

    //获取展示最终审批意见
    public String acquireFinalSuggestion(){
        if(!StringUtils.isEmpty(examineSuggestionEnergy)){
            finalSuggestion = examineSuggestionEnergy;
        }else if(!StringUtils.isEmpty(examineSuggestionProvince)) {
            finalSuggestion = examineSuggestionProvince;
        }else{
            finalSuggestion = examineSuggestionCity;
        }
        return finalSuggestion;
    }
}
