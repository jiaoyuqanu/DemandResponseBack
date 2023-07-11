package com.xqxy.dr.modular.event.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 电力用户执行信息
 * </p>
 *
 * @author Shen
 * @since 2021-10-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dr_cust_execute")
@ApiModel(value="CustExecute对象", description="客户执行信息")
public class CustExecute implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "本实体记录的唯一标识，产生规则为流水号")
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty(value = "关联事件ID")
    private Long eventId;

    @ApiModelProperty(value = "关联用户ID")
    private String custId;

    @ApiModelProperty(value = "基线负荷")
    private BigDecimal baselineCap;

    @ApiModelProperty(value = "基线最大负荷")
    private BigDecimal maxLoadBaseline;

    @ApiModelProperty(value = "响应负荷确认值")
    private BigDecimal replyCap;

    @ApiModelProperty(value = "实时负荷")
    private BigDecimal realTimeCap;

    @ApiModelProperty(value = "执行时间（yyyy-MM-dd HH:mm:ss）")
    private LocalDateTime executeTime;

    @ApiModelProperty(value = "实时执行负荷:基线负荷与实时负荷差值")
    private BigDecimal executeCap;

    @ApiModelProperty(value = "实时执行率:执行负荷/响应负荷确认值计算百分比")
    private BigDecimal executeRate;

    @ApiModelProperty(value = "是否越界:(是：Y，否：N)")
    private String isOut;

    @ApiModelProperty(value = "是否达标:(是：Y，否：N)")
    private String isQualified;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建人")
    private Long createUser;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "更新人")
    private Long updateUser;

    @ApiModelProperty(value = "实时执行负荷:基线负荷与实时负荷差值")
    private BigDecimal maxExecuteCap;


}
