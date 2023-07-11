package com.xqxy.dr.modular.project.params;

import com.xqxy.core.pojo.base.param.BaseParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * Custom Contract Param
 * </p>
 *
 * @author Caoj
 * @since 2021-10-15 15:14
 */
@Data
public class CustContractParam extends BaseParam {

    @ApiModelProperty(value = "申报标识")
    private Long contractId;

    @ApiModelProperty(value = "项目标识")
    private Long projectId;

    @ApiModelProperty(value = "本实体记录的唯一标识，产生规则为流水号")
    private Long custId;

    @ApiModelProperty(value = "第一联系人")
    private String firstContactName;

    @ApiModelProperty(value = "第一联系人联系方式")
    private String firstContactInfo;

    @ApiModelProperty(value = "第二联系人姓名")
    private String secondContactName;

    @ApiModelProperty(value = "第二联系人联系方式")
    private String secondContactInifo;

    @ApiModelProperty(value = "1 保存  2 已签约 3撤销")
    private String status;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建人")
    private Long createUser;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "更新人")
    private Long updateUser;

    @ApiModelProperty("户号")
    private Long consId;

    List<ConsContractParam> consContractParams;
}
