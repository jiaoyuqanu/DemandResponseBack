package com.xqxy.dr.modular.event.param;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.xqxy.core.pojo.base.param.BaseParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@ApiModel(description = "客户邀约 参数")
@EqualsAndHashCode(callSuper = true)
@Data
public class PlanCustParam extends BaseParam {

    /**
     * 参与标识
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long particId;

    /**
     * 方案标识
     */
    @TableField("PLAN_ID")
    private Long planId;

    /**
     * 本实体记录的唯一标识，产生规则为流水号
     */
    @TableField("CUST_ID")
    private Long custId;

    /**
     * 客户名称
     */
    @ApiModelProperty(value = "客户名称")
    private String custName;

    /**
     * 可响应负荷
     */
    @TableField("DEMAND_CAP")
    private BigDecimal demandCap;

    /**
     * 方案序位
     */
    private Integer sequenceNo;

    /**
     * 是否被剔除
     */
    private String deleted;

    /**
     * 被剔除使用的规则
     */
    private String delRule;

    /**
     * 事件标识
     */
    private Long eventId;

    private String provinceCode;

    private String cityCode;

    private String countryCode;

    private String creditCode;

    private String integrator;

    /**
     * 法定代表人姓名
     */
    @ApiModelProperty(value = "法定代表人姓名")
    private String legalName;

}
