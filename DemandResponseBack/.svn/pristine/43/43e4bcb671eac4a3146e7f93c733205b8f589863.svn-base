package com.xqxy.dr.modular.evaluation.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.xqxy.core.pojo.base.entity.BaseEntity;
import com.xqxy.sys.modular.cust.entity.Cons;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 今日响应效果评估
 * </p>
 *
 * @author Peng
 * @since 2021-10-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dr_cons_evaluation_immediate")
@ApiModel(value="ConsEvaluationImmediate对象", description="今日响应效果评估")
public class ConsEvaluationImmediate  extends BaseEntity implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "本实体记录的唯一标识，产生规则为流水号")
    @TableId(type = IdType.ASSIGN_ID)
    private Long evaluationId;

    @ApiModelProperty(value = "本实体记录的唯一标识，产生规则为流水号")
    private String consId;

    @ApiModelProperty(value = "本实体记录的唯一标识，产生规则为流水号")
    private Long eventId;

    @ApiModelProperty(value = "约定响应量")
    private BigDecimal invitationCap;

    @ApiModelProperty(value = "反馈响应量")
    private BigDecimal replyCap;

    @ApiModelProperty(value = "实际响应量")
    private BigDecimal actualCap;

    @ApiModelProperty(value = "核定响应量")
    private BigDecimal confirmCap;

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

    @ApiModelProperty(value = "基线用电量")
    private BigDecimal electricityBaseline;

    @ApiModelProperty(value = "实际用电量")
    private BigDecimal electricityActual;

    @ApiModelProperty(value = "是否有效响应:0 无效 1 有效")
    private String isEffective;

    @ApiModelProperty(value = "有效响应时长:**分钟")
    private Long effectiveTime;

    @ApiModelProperty(value = "异常描述")
    private String exceptionRemark;



    /**
     * 客户主键
     */
    @TableField(exist = false)
    private Long custId;

    /**
     * 用户名称
     */
    @TableField(exist = false)
    private String consName;

    /**
     * 用电地址
     */
    @TableField(exist = false)
    private String elecAddr;

    /**
     * 行业类别名称
     */
    @TableField(exist = false)
    private String bigTradeName;

    /**
     * 行业类别编码
     */
    @TableField(exist = false)
    private String bigTradeCode;

    /**
     * 行业分类名称
     */
    @TableField(exist = false)
    private String tradeName;

    /**
     * 行业分类编码
     */
    @TableField(exist = false)
    private String tradeCode;

    /**
     * 日最大负荷（本月~去年同月）
     */
    @TableField(exist = false)
    private BigDecimal dayMaxPower;

    /**
     * 最大容量
     */
    @TableField(exist = false)
    private BigDecimal contractCap;

    /**
     * 运行容量
     */
    @TableField(exist = false)
    private BigDecimal runCap;

    /**
     * 电源类型（专线、专变、公变）
     */
    @TableField(exist = false)
    private String typeCode;

    /**
     * 供电单位名称
     */
    @TableField(exist = false)
    private String orgName;

    /**
     * 供电单位编码
     */
    @TableField(exist = false)
    private String orgNo;

    /**
     * 省码
     */
    @TableField(exist = false)
    private String provinceCode;

    /**
     * 市码
     */
    @TableField(exist = false)
    private String cityCode;

    /**
     * 区县码
     */
    @TableField(exist = false)
    private String countyCode;

    /**
     * 街道码（乡镇）
     */
    @TableField(exist = false)
    private String streetCode;

    /**
     * 变电站名称
     */
    @TableField(exist = false)
    private String subsName;

    /**
     * 变电站编码
     */
    @TableField(exist = false)
    private String subsNo;

    /**
     * 线路名称
     */
    @TableField(exist = false)
    private String lineName;

    /**
     * 线路编号
     */
    @TableField(exist = false)
    private String lineNo;

    /**
     * 台区名称
     */
    @TableField(exist = false)
    private String tgName;

    /**
     * 台区编号
     */
    @TableField(exist = false)
    private String tgNo;

    /**
     * 第一联系人姓名
     */
    @TableField(exist = false)
    private String firstContactName;

    /**
     * 第一联系人联系方式
     */
    @TableField(exist = false)
    private String firstContactInfo;

    /**
     * 第二联系人姓名
     */
    @TableField(exist = false)
    private String secondContactName;

    /**
     * 第二联系人联系方式
     */
    @TableField(exist = false)
    private String secondContactInifo;

    /**
     * 1:正常，2：撤销
     */
    @TableField(exist = false)
    private String state;

    @ApiModelProperty(value = "描述")
    @TableField("remark")
    private String remark;


}
