package com.xqxy.dr.modular.gwapp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.xqxy.core.pojo.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 客户(对应注册用户)
 * </p>
 *
 * @author Yechs
 * @since 2022-05-20
 */
@Getter
@Setter
@TableName("dr_gwapp_cust")
@ApiModel(value = "GwappCust对象", description = "客户(对应注册用户)")
public class GwappCust extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("本实体记录的唯一标识，产生规则为流水号")
    @TableId(value = "ID", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("注册手机号")
    @TableField("tel")
    private String tel;

    @ApiModelProperty("统一社会信用代码证就是三证合一。 三证合一,就是把营业执照、税务登记证和组织机构代码证这三个证件合三为一")
    @TableField("CREDIT_CODE")
    private String creditCode;

    @ApiModelProperty("字典： 1是，0 否")
    @TableField("integrator")
    private Integer integrator;

    @ApiModelProperty("法定代表人姓名")
    @TableField("legal_name")
    private String legalName;

    @ApiModelProperty("法定代表人号码")
    @TableField("legal_no")
    private String legalNo;

    @ApiModelProperty("1身份证，2护照")
    @TableField("legal_card_type")
    private String legalCardType;

    @ApiModelProperty("经办人证件号码")
    @TableField("apply_no")
    private String applyNo;

    @ApiModelProperty("1身份证，2护照")
    @TableField("apply_card_type")
    private String applyCardType;

    @ApiModelProperty("经办人")
    @TableField("apply_name")
    private String applyName;

    @ApiModelProperty("客户的编号（保留）")
    @TableField("CUST_NO")
    private String custNo;

    @ApiModelProperty("客户的名称（保留）")
    @TableField("CUST_NAME")
    private String custName;

    @ApiModelProperty("省码（保留）")
    @TableField("PROVINCE_CODE")
    private String provinceCode;

    @ApiModelProperty("市码（保留）")
    @TableField("CITY_CODE")
    private String cityCode;

    @ApiModelProperty("区县码（保留）")
    @TableField("COUNTY_CODE")
    private String countyCode;

    @ApiModelProperty("街道码（乡镇）（保留）")
    @TableField("STREET_CODE")
    private String streetCode;

    @ApiModelProperty("客户地址（保留）")
    @TableField("CUST_ADDR")
    private String custAddr;

    @ApiModelProperty("1:未提交，2：审核中，3：审核通过，4：审核不通过")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @TableField("check_status")
    private String checkStatus;

    @ApiModelProperty("1:未认证，2:已认证，3：撤销，4：认证失败")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @TableField("state")
    private String state;

    @ApiModelProperty("身份确认,是否允许修改")
    @TableField("ALLOW_CHANGE")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String allowChange;

    @ApiModelProperty("供电单位编码")
    @TableField("ORG_NO")
    private String orgNo;

    @ApiModelProperty("供电单位名称")
    @TableField("ORG_NAME")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String orgName;

    @ApiModelProperty("审批人")
    @TableField("approval_user")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long approvalUser;

    @ApiModelProperty("审批意见")
    @TableField("approval_comments")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String approvalComments;


}
