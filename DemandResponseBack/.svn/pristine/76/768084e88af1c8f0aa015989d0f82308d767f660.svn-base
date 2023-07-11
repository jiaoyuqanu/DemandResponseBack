package com.xqxy.dr.modular.baseline.param;

import com.baomidou.mybatisplus.annotation.TableField;
import com.xqxy.core.pojo.base.param.BaseParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.util.List;

@ApiModel(description = "基线管理详情参数")
@EqualsAndHashCode(callSuper = true)
@Data
public class BaseLineDetailParam extends BaseParam {

    /**
     * 本实体记录的唯一标识，基线主键
     */
    @ApiModelProperty(value = "基线标识id")
    @NotNull(message = "baselinId不能为空，请检查baselinId参数")
    @TableField("baselin_id")
    private Long baselinId;

    @ApiModelProperty(value = "电力户号")
    private String consId;

    @ApiModelProperty(value = "用户名")
    private String name;

    @ApiModelProperty(value = "是否正常")
    private String normal;

    @ApiModelProperty(value = "统一信用代码")
    private String creditCode;

    private List<String> orgs;





}
