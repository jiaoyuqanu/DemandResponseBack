package com.xqxy.dr.modular.baseline.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xqxy.core.pojo.base.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * <p>
 * 基线管理
 * </p>
 *
 * @author chen zhi jun
 * @since 2021-10-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dr_baseline_library")
public class BaseLine extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 本实体记录的唯一标识，基线主键id
     */
    @ApiModelProperty(value = "基线主键id")
    @TableId(type = IdType.ASSIGN_ID)
    private Long baselinId;

    /**
     * 基线描述
     */
    @ApiModelProperty(value = "基线描述")
    private String descr;

    /**
     * 基线生成日：即首次需求响应日
     */
    @ApiModelProperty(value = "基线生成日")
    private LocalDate generateDate;

    /**
     * 响应开始时段:hh:mm:ss
     */
    @ApiModelProperty(value = "响应开始时段:hh:mm:ss")
    private String startPeriod;

    /**
     * 响应结束时段:hh:mm:ss
     */
    @ApiModelProperty(value = "响应结束时段:hh:mm:ss")
    private String endPeriod;

    /**
     * 日期逗号隔开，20210101，20210102，20210103
     */
    @ApiModelProperty(value = "样本日期,逗号隔开如20210101，20210102")
    private String simplesDate;

    /**
     * 计算规则1,2,3
     */
    @ApiModelProperty(value = "计算规则1,2,3")
    private String calRule;

    /**
     * 基线类型，1：时间段基线，2：全天基线
     */
    @ApiModelProperty(value = "基线类型，1：时间段基线，2：全天基线")
    private String baselineType;

    /**
     * 生成方式，1：自动生成，2：手动创建
     */
    @ApiModelProperty(value = "生成方式，1：自动生成，2：手动创建")
    private String genType="2";

    @ApiModelProperty(value = "用户时间段基线计算次数")
    private Integer consNum = 0;

    @ApiModelProperty(value = "用户全天基线计算次数")
    private Integer consAllNum = 0;

    @ApiModelProperty(value = "客户时间段基线计算次数")
    private Integer custNum = 0;

    @ApiModelProperty(value = "客户全天基线计算次数")
    private Integer custAllNum = 0;

}
