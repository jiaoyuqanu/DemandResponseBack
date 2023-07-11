package com.xqxy.sys.modular.sms.param;

import com.xqxy.core.pojo.base.param.BaseParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

@ApiModel(description = "短信模板参数")
@EqualsAndHashCode(callSuper = true)
@Data
public class SmsSendTemplateParam extends BaseParam {
    /**
     * 模板标识
     */
    @ApiModelProperty(value = "模板标识")
    @NotNull(message = "templateId不能为空，请检查declareId参数", groups = {edit.class, delete.class, detail.class})
    private Long templateId;

    /**
     * 模板名称
     */
    @ApiModelProperty(value = "模板名称")
    private String templateName;

    /**
     * 模板类型
     */
    @ApiModelProperty(value = "模板类型")
    private String templateType;

    /**
     * 模板内容
     */
    @ApiModelProperty(value = "模板内容")
    private String templateContent;

    /**
     * 是否生效
     */
    @ApiModelProperty(value = "是否生效")
    private String isValid;

    /**
     * 是否当前执行
     */
    @ApiModelProperty(value = "是否当前执行")
    private String isExec;

    private String minTemplateType;

    private String maxTemplateType;

}
