package com.xqxy.dr.modular.event.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xqxy.core.pojo.base.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 需求响应事件
 * </p>
 *
 * @author czj
 * @since 2022-01-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dr_plan_implem_rec")
public class DrPlanImplemRec extends BaseEntity implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 主键标识
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 事件标识
     */
    @TableField("event_id")
    private Long eventId;

    /**
     * 执行时间
     */
    @TableField("execute_time")
    private String executeTime;


}
