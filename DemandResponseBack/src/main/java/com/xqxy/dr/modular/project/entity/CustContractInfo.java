package com.xqxy.dr.modular.project.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xqxy.core.pojo.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 客户项目申报基本信息
 * </p>
 *
 * @author PengChuqing
 * @since 2021-10-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dr_cust_contract_info")
@ApiModel(value="CustContractInfo对象", description="客户项目申报基本信息")
public class CustContractInfo  extends BaseEntity implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "申报标识")
    @TableId(type = IdType.ASSIGN_ID)
    private Long contractId;

    @ApiModelProperty(value = "项目标识")
    private Long projectId;

    @ApiModelProperty(value = "本实体记录的唯一标识，产生规则为流水号")
    @TableField("CUST_ID")
    private Long custId;

    @ApiModelProperty(value = "是否集成商 1是 2否")
    private String integrator;

    @ApiModelProperty(value = "第一联系人")
    private String firstContactName;

    @ApiModelProperty(value = "第一联系人联系方式")
    private String firstContactInfo;

    @ApiModelProperty(value = "第二联系人姓名")
    private String secondContactName;

    @ApiModelProperty(value = "第二联系人联系方式")
    private String secondContactInifo;

    @ApiModelProperty(value = "签约容量")
    @TableField("CONTRACT_CAP")
    private BigDecimal contractCap;

    @ApiModelProperty(value = "1 保存  2 已签约 3撤销")
    private String status;

    @ApiModelProperty(value = "1:未提交，2：审核中，3：审核通过，4：审核不通过")
    private String checkStatus;

    @ApiModelProperty(value = "签约文件名称")
    private String fileName;

    @ApiModelProperty(value = "签约关联文件ID")
    private Long fileId;

    @ApiModelProperty(value = "签约文件类型")
    private String fileType;

    @ApiModelProperty(value = "客户签约明细")
    @TableField(exist = false)
    private CustContractDetail custContractDetail;

    @ApiModelProperty(value = "项目名称")
    @TableField(exist = false)
    private String projectName;

    @ApiModelProperty(value = "项目类型")
    @TableField(exist = false)
    private String projectType;

    @ApiModelProperty(value = "客户名称")
    @TableField(exist = false)
    private String custName;

    @ApiModelProperty(value = "签约参与类型")
    @TableField(exist = false)
    private String particType;

    @ApiModelProperty(value = "项目目标，1削峰，2填谷，3消纳新能源")
    @TableField(exist = false)
    private String projectTarget;

    @ApiModelProperty(value = "发布时间")
    @TableField(exist = false)
    private String pubTime;

    @ApiModelProperty(value = "用户数量")
    @TableField(exist = false)
    private Integer countCons;

    @ApiModelProperty(value = "签约用户数量")
    @TableField(exist = false)
    private Integer contractCons;

}
