package com.xqxy.dr.modular.subsidy.model;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.xqxy.core.pojo.base.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ConsSubsidyModel extends BaseEntity implements Serializable {

    private static final long serialVersionUID=1L;


    @ApiModelProperty(value = "用户标识")
    @Excel(name = "电力户号", width = 20)
    private String consId;

    @ApiModelProperty(value = "用户名称")
    @Excel(name = "用户名称", width = 25)
    private String consName;




    @ApiModelProperty(value = "是否有效")
    @Excel(name = "是否有效")
    private String isEffective;

    @ApiModelProperty(value = "供电单位名称")
    @Excel(name = "供电单位名称", width = 20)
    private String orgName;


    @ApiModelProperty(value = "市供电单位名称")
    @Excel(name = "市供电单位名称", width = 20)
    @TableField(exist = false)
    private String cityOrgName;

    @ApiModelProperty(value = "县供电单位名称")
    @Excel(name = "县供电单位名称", width = 20)
    @TableField(exist = false)
    private String countyOrgName;


    @ApiModelProperty(value = "供电单位编码")
    private String orgNo;

    @ApiModelProperty(value = "核定响应负荷")
    @Excel(name = "核定响应负荷(单位:kW)", width = 25)
    private String confirmCap;

    @ApiModelProperty(value = "实际响应负荷")
    @Excel(name = "实际响应负荷(单位：kW)", width = 25)
    private BigDecimal actualCap;

    @ApiModelProperty(value = "响应负荷(单位：kW)")
    @Excel(name = "响应负荷(单位：kW)", width = 20)
    private String replyCap;

    //???
    @ApiModelProperty(value = "补贴价格")
    @Excel(name = "补贴价格（元）", width = 15)
    private BigDecimal contractPrice;

    @ApiModelProperty(value = "调控时间系数")
    @Excel(name = "调控时间系数", width = 15)
    private BigDecimal timeCoefficient;

    @ApiModelProperty(value = "负荷响应率系数")
    @Excel(name = "负荷响应率系数", width = 20)
    private BigDecimal rateCoefficient;

    @ApiModelProperty(value = "补贴金额（元）")
    @Excel(name = "补贴金额（元）", width = 20)
    private BigDecimal subsidyAmount;

    @ApiModelProperty(value = "实得金额（元）")
    @Excel(name = "实得金额（元）", width = 20 )
    private BigDecimal settledAmount;


    @ApiModelProperty(value = "实际响应电量")
    private BigDecimal actualEnergy;

    @ApiModelProperty(value = "异常描述")
    private String remark;

    @ApiModelProperty(value = "计算规则，1，2，3")
    private String calRule;

    @ApiModelProperty(value = "用户参与方式")
    private String joinUserType;
}
