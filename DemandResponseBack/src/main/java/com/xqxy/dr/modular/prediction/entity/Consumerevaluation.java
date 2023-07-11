package com.xqxy.dr.modular.prediction.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户执行评价
 * </p>
 *
 * @author wangyunfei
 * @since 2021-11-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dr_consumerevaluation")
@ApiModel(value="Consumerevaluation对象", description="用户执行评价")
public class Consumerevaluation implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "评价标识")
      @TableId("EVALUATIONID")
    private Long evaluationid;

    @ApiModelProperty(value = "用户标识")
    @TableField("CONS_ID")
    private String consId;

    @ApiModelProperty(value = "事件标识")
    @TableField("EVENT_ID")
    private Long eventId;

    @ApiModelProperty(value = "准确率")
    @TableField("ACCURACYRATE")
    private BigDecimal accuracyrate;

    @ApiModelProperty(value = "实际支出成本(补贴金额)")
    @TableField("ACTUALCOSTS")
    private BigDecimal actualcosts;

    @ApiModelProperty(value = "实际响应容量")
    @TableField("ACTUALDRCAP")
    private BigDecimal actualdrcap;

    @ApiModelProperty(value = "达标率")
    @TableField("COMPLIANCERATE")
    private BigDecimal compliancerate;

    @ApiModelProperty(value = "电费节省效益")
    @TableField("ELECTRICBILLBENEFITS")
    private BigDecimal electricbillbenefits;

    @ApiModelProperty(value = "环境效益")
    @TableField("ENVBENEFITS")
    private BigDecimal envbenefits;

    @ApiModelProperty(value = "减少电网投资效益")
    @TableField("INVESTMENTBENEFITS")
    private BigDecimal investmentbenefits;

    @ApiModelProperty(value = "可靠性评分")
    @TableField("RELIABILITYSCORE")
    private BigDecimal reliabilityscore;


}
