package com.xqxy.dr.modular.device.entity;


import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 终端分路
 * </p>
 *
 * @author dw
 * @since 2022-04-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="TDevBranch对象", description="终端分路")
@TableName("sc_loadctl.t_dev_branch")
public class TDevBranch implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "分路标识,主键唯一")
    @TableId(value = "BRANCH_ID", type = IdType.AUTO)
    private String branchId;

    @ApiModelProperty(value = "分路名称")
    @TableField("BRANCH_NAME")
    private String branchName;

    @ApiModelProperty(value = "控制轮次,字典类型1006")
    @TableField("CRTL_TURN")
    private Integer crtlTurn;

    @ApiModelProperty(value = "额定负荷,单位kw")
    @TableField("RATING_LOAD")
    private BigDecimal ratingLoad;

    @ApiModelProperty(value = "通讯地址")
    @TableField("COMM_FAC_NO")
    private String commFacNo;

    @ApiModelProperty(value = "通讯模板标识")
    @TableField("COMM_MODEL_ID")
    private String commModelId;

    @ApiModelProperty(value = "设备资产号")
    @TableField("ASSET_NO")
    private String assetNo;

    @ApiModelProperty(value = "所属终端标识")
    @TableField("DEV_ID")
    private String devId;

    @ApiModelProperty(value = "所属用能企业")
    @TableField("COMP_ID")
    private String compId;

    @ApiModelProperty(value = "供电单位标识")
    @TableField("ORG_ID")
    private String orgId;

    @ApiModelProperty(value = "备注")
    @TableField("REMARK")
    private String remark;

    @ApiModelProperty(value = "创建人")
    @TableField("CREATED_BY")
    private String createdBy;

    @ApiModelProperty(value = "创建时间")
    @TableField("CREATED_TIME")
    private Date createdTime;

    @ApiModelProperty(value = "更新人")
    @TableField("UPDATED_BY")
    private String updatedBy;

    @ApiModelProperty(value = "更新时间")
    @TableField("UPDATED_TIME")
    private Date updatedTime;

    @ApiModelProperty(value = "电压互感器变比")
    @TableField("PT")
    private String pt;

    @ApiModelProperty(value = "电流互感器变比")
    @TableField("CT")
    private String ct;

    @ApiModelProperty(value = "负荷类型")
    @TableField("LOAD_TYPE")
    private Integer loadType;

    @ApiModelProperty(value = "表序号")
    @TableField("EQUIP_ID")
    private Integer equipId;

    @ApiModelProperty(value = "波特率")
    @TableField("BAUD_RATE")
    private Integer baudRate;

    @ApiModelProperty(value = "规约类型")
    @TableField("PROTOCOL")
    private Integer protocol;

    @ApiModelProperty(value = "端口")
    @TableField("PORT_NUN")
    private Integer portNun;

    @ApiModelProperty(value = "通信密码")
    @TableField("PASSWORD")
    private String password;

    @ApiModelProperty(value = "费率数")
    @TableField("TARIFF")
    private String tariff;

    @ApiModelProperty(value = "额定电压")
    @TableField("RATED_VOLTAGE")
    private BigDecimal ratedVoltage;

    @ApiModelProperty(value = "额定电流")
    @TableField("RATED_CURRENT")
    private BigDecimal ratedCurrent;

    @ApiModelProperty(value = "分路号")
    @TableField("ELECTRIC_RELAY")
    private String electricRelay;

    @ApiModelProperty(value = "是否有效 0：有效；1:无效")
    @TableField("EFFECT_STATUS")
    private Integer effectStatus;

    @ApiModelProperty(value = "是否同步 0：未同步；1：已同步 ；字典码：1011")
    @TableField("SYNCHRONIZ_STATUS")
    private Integer synchronizStatus;

    @ApiModelProperty(value = "审核状态： 0 待审核 1 审核通过 2 审核失败 3待提交 ；字典码：1012")
    @TableField("AUDIT_STATUS")
    private Integer auditStatus;

    @ApiModelProperty(value = "最近通过的审核记录，关联中间表审核记录")
    @TableField("VERIFY_ID")
    private String verifyId;

    @ApiModelProperty(value = "最近待审核记录，关联审核记录表，")
    @TableField("NEW_VERIFY_ID")
    private String newVerifyId;

    @ApiModelProperty(value = "分路接入设备")
    @TableField("BRANCH_ACCESS_EQUIPMENT")
    private String branchAccessEquipment;

    @ApiModelProperty(value = "接线方式")
    @TableField("CONNECTION_MODE")
    private Integer connectionMode;

    @ApiModelProperty(value = "量测设备")
    @TableField("GAUGE")
    private Integer gauge;

    @ApiModelProperty(value = "附加域")
    @TableField("ADDITIONAL_DOMAIN")
    private String additionalDomain;

    @ApiModelProperty(value = "电压互感器变比原值")
    @TableField("PT_DATA")
    private String ptData;

    @ApiModelProperty(value = "电流互感器变比原值")
    @TableField("CT_DATA")
    private String ctData;

    @ApiModelProperty(value = "用户类型")
    @TableField("USER_TYPE")
    private Integer userType;


}
