package com.xqxy.dr.modular.evaluation.entity;

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
import java.util.List;

/**
 * <p>
 * 客户执行效果评估
 * </p>
 *
 * @author hu xingxing
 * @since 2021-10-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dr_cust_evaluation_appeal")
@ApiModel(value="CustEvaluation对象", description="客户执行效果评估")
public class CustEvaluationAppeal extends BaseEntity implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "评估标识")
    @TableId(type = IdType.ASSIGN_ID)
    private Long evaluationId;

    @ApiModelProperty(value = "事件标识")
    @TableField("EVENT_ID")
    private Long eventId;

    @ApiModelProperty(value = "本实体记录的唯一标识，产生规则为流水号")
    @TableField("CUST_ID")
    private Long custId;

    @ApiModelProperty(value = "邀约响应量")
    private BigDecimal invitationCap;

    @ApiModelProperty(value = "反馈响应量")
    private BigDecimal replyCap;

    @ApiModelProperty(value = "基线最大负荷")
    private BigDecimal maxLoadBaseline;

    @ApiModelProperty(value = "基线最小负荷")
    private BigDecimal minLoadBaseline;

    @ApiModelProperty(value = "基线平均负荷")
    private BigDecimal avgLoadBaseline;

    @ApiModelProperty(value = "实际最大负荷")
    private BigDecimal maxLoadActual;

    @ApiModelProperty(value = "实际最小负荷")
    private BigDecimal minLoadActual;

    @ApiModelProperty(value = "实际平均负荷")
    private BigDecimal avgLoadActual;

    @ApiModelProperty(value = "实际响应负荷")
    private BigDecimal actualCap;

    @ApiModelProperty(value = "实际响应电量")
    private BigDecimal actualEnergy;

    @ApiModelProperty(value = "核定响应负荷")
    private BigDecimal confirmCap;

    @ApiModelProperty(value = "是否有效:N 无效；Y 有效")
    private String isEffective;

    @ApiModelProperty(value = "有效响应时长(**分钟)")
    private Integer effectiveTime;

    @ApiModelProperty(value = "是否合格(平均负荷)")
    private String isQualified;

    @ApiModelProperty(value = "是否越界(最大负荷)")
    private String isOut;


    /**
     * 注册手机号
     */
    @TableField(exist = false)
    private String tel;

    /**
     * 统一社会信用代码证就是三证合一。 三证合一,就是把营业执照、税务登记证和组织机构代码证这三个证件合三为一
     */
    @TableField(exist = false)
    private String creditCode;

    /**
     * 是否集成商  字典： 1是，0 否
     */
    @TableField(exist = false)
    private Integer integrator;

    /**
     * 法定代表人姓名
     */
    @TableField(exist = false)
    private String legalName;

    /**
     * 法定代表人号码
     */
    @TableField(exist = false)
    private String legalNo;

    /**
     * 1身份证，2护照
     */
    @TableField(exist = false)
    private String legalCardType;

    /**
     * 经办人证件号码
     */
    @TableField(exist = false)
    private String applyNo;

    /**
     * 1身份证，2护照
     */
    @TableField(exist = false)
    private String applyCardType;

    /**
     * 经办人
     */
    @TableField(exist = false)
    private String applyName;

    /**
     * 客户的编号（保留）
     */
    @TableField(exist = false)
    private String custNo;

    /**
     * 客户的名称（保留）
     */
    @TableField(exist = false)
    private String custName;

    /**
     * 省码（保留）
     */
    @TableField(exist = false)
    private String provinceCode;

    /**
     * 市码（保留）
     */
    @TableField(exist = false)
    private String cityCode;

    /**
     * 区县码（保留）
     */
    @TableField(exist = false)
    private String countyCode;

    /**
     * 街道码（乡镇）（保留）
     */
    @TableField(exist = false)
    private String streetCode;

    /**
     * 客户地址（保留）
     */
    @TableField(exist = false)
    private String custAddr;

    /**
     * 1:未提交，2：审核中，3：审核通过，4：审核不通过
     */
    @TableField(exist = false)
    private String checkStatus;

    /**
     * 1:未认证，2:已认证，3：撤销，4：认证失败
     */
    @TableField(exist = false)
    private String state;

    /**
     * 项目有效性判断
     */
    @TableField(exist = false)
    private List<String> validityJudgment;

    @ApiModelProperty(value = "描述")
    @TableField("remark")
    private String remark;

    @ApiModelProperty(value = "负荷响应率")
    private BigDecimal executeRate;
}
