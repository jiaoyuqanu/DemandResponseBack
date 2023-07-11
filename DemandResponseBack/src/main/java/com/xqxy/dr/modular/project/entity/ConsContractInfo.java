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
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 用户项目申报基本信息
 * </p>
 *
 * @author PengChuqing
 * @since 2021-10-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dr_cons_contract_info")
@Accessors(chain = true)
@ApiModel(value="ConsContractInfo对象", description = "用户项目申报基本信息")
public class ConsContractInfo extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "申报标识")
    @TableId(type = IdType.ASSIGN_ID)
    private Long contractId;

    @ApiModelProperty(value = "用户标识")
    @TableField("CONS_ID")
    private String consId;

    @ApiModelProperty(value = "项目标识")
    private Long projectId;

    @ApiModelProperty(value = "1直接参与，2代理参与")
    private String particType;

    @ApiModelProperty(value = "分成比例")
    @TableField("EXTRACT_RATIO")
    private BigDecimal extractRatio;

    @ApiModelProperty(value = "第一联系人")
    private String firstContactName;

    @ApiModelProperty(value = "第一联系人联系方式")
    private String firstContactInfo;

    @ApiModelProperty(value = "第二联系人姓名")
    private String secondContactName;

    @ApiModelProperty(value = "第二联系人联系方式")
    private String secondContactInifo;

    @ApiModelProperty(value = "1:未提交，2：审核中，3：审核通过，4：审核不通过")
    private String checkStatus;

    @ApiModelProperty(value = "1 保存  2 已签约 3撤销")
    private String status;

    @ApiModelProperty(value = "签约文件名称")
    private String fileName;

    @ApiModelProperty(value = "签约关联文件ID")
    private Long fileId;

    @ApiModelProperty(value = "签约文件类型")
    private String fileType;

    @TableField(exist = false)
    private ConsContractDetail consContractDetail;

    @TableField(exist = false)
    private String projectName;

    @TableField(exist = false)
    private String consName;

    @TableField(exist = false)
    private BigDecimal contractCap;

    @TableField(exist = false)
    private String runCap;

    @TableField(exist = false)
    private String orgName;

    @TableField(exist = false)
    private String state;

    @ApiModelProperty(value = "客户主键")
    @TableField("cust_id")
    private Long custId;

    @TableField(exist = false)
    private Project project;
}
